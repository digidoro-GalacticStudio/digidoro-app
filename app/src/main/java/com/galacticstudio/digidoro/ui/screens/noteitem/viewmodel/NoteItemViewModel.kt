package com.galacticstudio.digidoro.ui.screens.noteitem.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galacticstudio.digidoro.domain.usecase.note.AddNote
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.NoteRepository
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemEvent
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemResponseState
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NoteItemViewModel(
    private val addNote: AddNote = AddNote(),
    private val repository: NoteRepository,
): ViewModel() {
    private val _state = mutableStateOf(NoteItemState())
    val state: State<NoteItemState> = _state

    private val _noteColor = mutableStateOf(0)
    val noteColor: State<Int> = _noteColor

    // The current state of the note item response from the API (nullable)
    private var apiState by mutableStateOf<NoteItemResponseState?>(NoteItemResponseState.Resume)

    // Channel for emitting login response states.
    private val responseEventChannel = Channel<NoteItemResponseState>()
    val responseEvents: Flow<NoteItemResponseState> = responseEventChannel.receiveAsFlow()

    fun onEvent(event: NoteItemEvent) {
        when (event) {
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

            is NoteItemEvent.DeleteNote -> {

            }

            is NoteItemEvent.SaveNote -> {
                submitData()
            }

            is NoteItemEvent.ToggleFavorite -> {

            }

        }
    }

    private fun submitData() {
        if (!validateData()) {
            apiState = NoteItemResponseState.ErrorWithMessage("Wrong information")
            return
        }

        addNewNote(_state.value.title, _state.value.message, _state.value.tags, _noteColor.value.toString())
    }

    private fun validateData(): Boolean {
        val noteResult = addNote.invoke(
            _state.value.title,
            _state.value.message,
            _state.value.tags,
            _noteColor.value.toString() //TODO CHECK THIS
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

    private fun addNewNote(title: String, message: String, tags: List<String>, toString: String) {
        viewModelScope.launch {
            when (val response = repository.createNote(title, message, tags, toString)) {
                // If the response is an error, create a LoginResponseState.Error
                is ApiResponse.Error -> {
                    sendResponseEvent(NoteItemResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NoteItemResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    sendResponseEvent(
                        NoteItemResponseState.Success
                    )
                }
            }
        }
    }
}