package com.galacticstudio.digidoro.ui.screens.noteitem.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.data.NoteModel
import com.galacticstudio.digidoro.domain.usecase.note.AddNote
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.repository.NoteRepository
import com.galacticstudio.digidoro.ui.screens.noteitem.ActionType
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemEvent
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemResponseState
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemState
import com.galacticstudio.digidoro.util.DateUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NoteItemViewModel(
    private val addNote: AddNote = AddNote(),
    private val repository: NoteRepository,
) : ViewModel() {
    private val _state = mutableStateOf(NoteItemState())
    val state: State<NoteItemState> = _state

    private val _noteColor = mutableStateOf("ffffff")
    val noteColor: State<String> = _noteColor

    private val _actionTypeEvents = mutableStateOf<ActionType>(ActionType.CreateNote)
    val actionTypeEvents: State<ActionType> = _actionTypeEvents

    // The current state of the note item response from the API (nullable)
    private var apiState by mutableStateOf<NoteItemResponseState?>(NoteItemResponseState.Resume)

    // Channel for emitting login response states.
    private val responseEventChannel = Channel<NoteItemResponseState>()
    val responseEvents: Flow<NoteItemResponseState> = responseEventChannel.receiveAsFlow()

    fun onEvent(event: NoteItemEvent) {
        when (event) {
            is NoteItemEvent.NoteIdChanged -> {
                getNoteById(event.noteId)
            }

            is NoteItemEvent.TitleChanged -> {
                _state.value = state.value.copy(title = event.title)
            }

            is NoteItemEvent.ContentChanged -> {
                _state.value = state.value.copy(message = event.message)
            }

            is NoteItemEvent.ColorChanged -> {
                _noteColor.value = event.color
            }

            is NoteItemEvent.TagsChanged -> {

            }

            is NoteItemEvent.ActionTypeChanged -> {
                _actionTypeEvents.value = actionTypeEvents.value
            }

            is NoteItemEvent.DeleteNote -> {
                deleteNote(_state.value.noteId)
            }

            is NoteItemEvent.SaveNote -> {
                submitData()
            }

            is NoteItemEvent.UpdateNote -> {
                updateData()
            }

            is NoteItemEvent.ToggleFavorite -> {

            }

            is NoteItemEvent.ToggleTrash -> {
                toggleTrashNote(_state.value.noteId)
            }
        }
    }

    private fun submitData() {
        if (!validateData()) {
            apiState = NoteItemResponseState.ErrorWithMessage("Wrong information")
            return
        }

        addNewNote(_state.value.title, _state.value.message, _state.value.tags, "#${_noteColor.value}")
    }

    private fun updateData() {
        if (!validateData()) {
            apiState = NoteItemResponseState.ErrorWithMessage("Wrong information")
            return
        }

        updateNote(
            _state.value.noteId,
            _state.value.title,
            _state.value.message,
            _state.value.tags,
            "#${_noteColor.value}"
        )
    }

    private fun validateData(): Boolean {
        val noteResult = addNote.invoke(
            _state.value.title,
            _state.value.message,
            _state.value.tags,
            "#${_noteColor.value}"
        )

        val hasError = listOf(
            noteResult,
        ).any { !it.successful }

        _state.value = state.value.copy(
            noteError = if (hasError) noteResult.errorMessage else null,
        )

        return !hasError
    }

    /**
     * Sends a note item response event to the channel.
     *
     * @param event The note response event to send.
     */
    private fun sendResponseEvent(event: NoteItemResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun addNewNote(title: String, message: String, tags: List<String>, color: String) {
        viewModelScope.launch {
            when (val response = repository.createNote(title, message, tags, color)) {
                is ApiResponse.Error -> {
                    sendResponseEvent(NoteItemResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NoteItemResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    _actionTypeEvents.value = ActionType.CreateNote
                    sendResponseEvent(
                        NoteItemResponseState.Success(_actionTypeEvents.value)
                    )
                }
            }
        }
    }

    private fun updateNote(
        noteId: String,
        title: String,
        message: String,
        tags: List<String>,
        toString: String
    ) {
        viewModelScope.launch {
            when (val response =
                repository.updateNoteById(noteId, title, message, tags, toString)) {
                is ApiResponse.Error -> {
                    sendResponseEvent(NoteItemResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NoteItemResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    _actionTypeEvents.value = ActionType.UpdateNote
                    sendResponseEvent(
                        NoteItemResponseState.Success(ActionType.CreateNote)
                    )
                }
            }
        }
    }

    private fun getNoteById(noteId: String) {
        viewModelScope.launch {
            when (val response = repository.getNoteById(noteId)) {
                is ApiResponse.Error -> {
                    sendResponseEvent(NoteItemResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NoteItemResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    val note = mapToNoteModel(response.data)
                    _state.value = _state.value.copy(
                        noteId = note.id,
                        title = note.title,
                        message = note.message,
                        tags = note.tags,
                    )

                    _actionTypeEvents.value = ActionType.UpdateNote
                    sendResponseEvent(
                        NoteItemResponseState.Success(_actionTypeEvents.value)
                    )
                }
            }
        }
    }

    private fun deleteNote(noteId: String) {
        viewModelScope.launch {
            when (val response = repository.deleteNoteById(noteId)) {
                is ApiResponse.Error -> {
                    sendResponseEvent(NoteItemResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NoteItemResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    sendResponseEvent(
                        NoteItemResponseState.Success(_actionTypeEvents.value)
                    )
                }
            }
        }
    }

    private fun toggleTrashNote(noteId: String) {
        viewModelScope.launch {
            when (val response = repository.toggleTrashNoteById(noteId)) {
                is ApiResponse.Error -> {
                    sendResponseEvent(NoteItemResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NoteItemResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    sendResponseEvent(
                        NoteItemResponseState.Success(_actionTypeEvents.value)
                    )
                }
            }
        }
    }

    private fun mapToNoteModel(noteData: NoteData): NoteModel {
        return NoteModel(
            id = noteData.id,
            user_id = noteData.userId,
            title = noteData.title,
            message = noteData.message,
            tags = noteData.tags,
            theme = noteData.theme,
            is_trashed = noteData.isTrashed,
            createdAt = DateUtils.convertISO8601ToDate(noteData.createdAt),
            updatedAt = DateUtils.convertISO8601ToDate(noteData.updatedAt)
        )
    }

    companion object {
        // Factory for creating instances of LoginViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RetrofitApplication
                // Create a new instance of LoginViewModel with dependencies.
                NoteItemViewModel(
                    addNote = AddNote(),
                    repository = app.notesRepository
                )
            }
        }
    }
}