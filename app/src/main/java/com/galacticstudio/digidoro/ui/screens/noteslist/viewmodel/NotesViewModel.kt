package com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.data.NoteModel
import com.galacticstudio.digidoro.domain.usecase.note.AddNote
import com.galacticstudio.digidoro.domain.usecase.note.DeleteNote
import com.galacticstudio.digidoro.domain.usecase.note.GetNotes
import com.galacticstudio.digidoro.domain.usecase.note.NoteUseCases
import com.galacticstudio.digidoro.domain.util.NoteOrder
import com.galacticstudio.digidoro.domain.util.OrderType
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.repository.NoteRepository
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesEvent
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesResponseState
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesState
import com.galacticstudio.digidoro.util.DateUtils.Companion.convertISO8601ToDate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteUseCases: NoteUseCases,
    private val repository: NoteRepository,
) : ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    // The current state of the notes response from the API (nullable)
    private var apiState by mutableStateOf<NotesResponseState?>(NotesResponseState.Resume)

    // Channel for emitting notes response states.
    private val responseEventChannel = Channel<NotesResponseState>()
    val responseEvents: Flow<NotesResponseState> = responseEventChannel.receiveAsFlow()

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }

                getNotes(event.noteOrder)
            }

            is NotesEvent.Rebuild -> {
                getNotes(state.value.noteOrder)
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    /**
     * Sends a login response event to the channel.
     *
     * @param event The login response event to send.
     */
    private fun sendResponseEvent(event: NotesResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val sortBy = when (noteOrder) {
                is NoteOrder.Date -> "createdAt"
            }

            val order = when (noteOrder.orderType) {
                is OrderType.Ascending -> "asc"
                is OrderType.Descending -> "desc"
            }

            when (val response = repository.getAllNotes(sortBy, order)) {
                is ApiResponse.Error -> {
                    sendResponseEvent(NotesResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NotesResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    _state.value = _state.value.copy(
                        notes = mapToNoteModels(response.data),
                        noteOrder = noteOrder,
                        isLoading = false,
                    )
                    sendResponseEvent(NotesResponseState.Success)
                }
            }
        }
    }

    private fun mapToNoteModels(noteDataList: List<NoteData>): List<NoteModel> {
        return noteDataList.map { noteData ->
            NoteModel(
                id = noteData.id,
                user_id = noteData.userId,
                title = noteData.title,
                message = noteData.message,
                tags = noteData.tags,
                theme = noteData.theme,
                is_trashed = noteData.isTrashed,
                createdAt = convertISO8601ToDate(noteData.createdAt),
                updatedAt = convertISO8601ToDate(noteData.updatedAt)
            )
        }
    }

    companion object {
        // Factory for creating instances of LoginViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as RetrofitApplication
                // Create a new instance of LoginViewModel with dependencies.
                NotesViewModel(
                    noteUseCases = NoteUseCases(
                        getNotes = GetNotes(),
                        deleteNote = DeleteNote(app.notesRepository),
                        addNote = AddNote()
                    ),
                    repository = app.notesRepository
                )
            }
        }
    }
}