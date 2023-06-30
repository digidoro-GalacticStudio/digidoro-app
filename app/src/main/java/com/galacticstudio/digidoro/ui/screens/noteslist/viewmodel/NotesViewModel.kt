package com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Path.Companion.combine
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.data.api.FolderModel
import com.galacticstudio.digidoro.data.api.FolderPopulatedModel
import com.galacticstudio.digidoro.data.api.NoteModel
import com.galacticstudio.digidoro.domain.usecase.note.AddNote
import com.galacticstudio.digidoro.domain.usecase.note.DeleteNote
import com.galacticstudio.digidoro.domain.usecase.note.GetNotes
import com.galacticstudio.digidoro.domain.usecase.note.NoteUseCases
import com.galacticstudio.digidoro.domain.util.NoteOrder
import com.galacticstudio.digidoro.domain.util.OrderType
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.folder.FolderData
import com.galacticstudio.digidoro.network.dto.folder.FolderDataPopulated
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.repository.FavoriteNoteRepository
import com.galacticstudio.digidoro.repository.FolderRepository
import com.galacticstudio.digidoro.repository.NoteRepository
import com.galacticstudio.digidoro.ui.screens.noteslist.NoteResultsMode
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesEvent
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesResponseState
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesState
import com.galacticstudio.digidoro.util.DateUtils.Companion.convertISO8601ToDate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteUseCases: NoteUseCases,
    private val noteRepository: NoteRepository,
    private val favoriteNoteRepository: FavoriteNoteRepository,
    private val folderRepository: FolderRepository,
) : ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    // The current state of the notes response from the API (nullable)
    private var apiState by mutableStateOf<NotesResponseState?>(NotesResponseState.Resume)

    private val _resultsMode = mutableStateOf<NoteResultsMode>(NoteResultsMode.AllNotes)
    val resultsMode: State<NoteResultsMode> = _resultsMode

    // Channel for emitting notes response states.
    private val responseEventChannel = Channel<NotesResponseState>()
    val responseEvents: Flow<NotesResponseState> = responseEventChannel.receiveAsFlow()

    private val _roles = mutableStateOf<List<String>>(emptyList())
    val roles: State<List<String>> = _roles

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
                rebuildUI(event.resultsMode)
            }

            is NotesEvent.SelectedFolderChanged -> {
                _state.value = state.value.copy(
                    selectedFolder = event.folder,
                    notes = event.folder.notesId,
                )
            }

            is NotesEvent.ResultsChanged -> {
                rebuildUI(event.resultsMode)
            }

            is NotesEvent.RolesChanged -> {
                _roles.value = event.roles
            }


            is NotesEvent.DeleteNote -> {
                deleteNote(event.noteId)
            }

            is NotesEvent.ToggleTrash -> {
                toggleTrashNote(event.noteId)
            }

            is NotesEvent.DuplicateNote -> {
                if (event.note == null) return
                duplicateData(event.note)
            }

            is NotesEvent.NewFolderNoteChanged -> {
                _state.value = _state.value.copy(
                    newSelectedFolder = event.folder
                )
            }

            is NotesEvent.GetSelectedFolder -> {
                _state.value = _state.value.copy(actualNoteId = event.noteId)
                getNotesWithoutFolders(event.noteId)
            }

            is NotesEvent.MoveToAnotherFolder -> {
                saveNewFolder()
            }

            is NotesEvent.ClearData -> {
                _state.value = _state.value.copy(
                    newFolderList = emptyList(),
                    newSelectedFolder = null,
                    actualFolder = null,
                    actualNoteId = "",
                )
            }
        }
    }

    private fun rebuildUI(resultModel: NoteResultsMode) {
        if (resultModel == NoteResultsMode.FolderNotes) {
            if (_roles.value.contains("premium")) {
                _resultsMode.value = resultModel
                getNotesWithoutFolders()
                getFolders()
            } else {
                apiState = NotesResponseState.ErrorWithMessage("You are not a PRO user. Upgrade your account to unlock PRO features")
                viewModelScope.launch {
                    responseEventChannel.send(apiState as NotesResponseState.ErrorWithMessage)
                }
            }
        } else {
            _resultsMode.value = resultModel
            getNotes(state.value.noteOrder)
            _state.value = state.value.copy(selectedFolder = null)
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

            val apiNotesResponse = noteRepository.getAllNotesFromApi(sortBy, order, isTrashed = false)
            val dbNotes = noteRepository.getAllNotesFromDatabase().firstOrNull()

            when (apiNotesResponse) {
                is ApiResponse.Success -> {
                    // Oget notas from database
                    val notesFromDatabase = dbNotes?.map { it._id }

                    // get the notes from API
                    val notesFromApi = apiNotesResponse.data

                    val notes = mutableListOf<NoteData>()

                    notesFromApi.forEach { noteData ->
                        val matchingNoteId = notesFromDatabase?.find { it == noteData.id }
                        if (matchingNoteId == null) {
                            notes.add(noteData)
                        }
                    }

                    noteRepository.syncNotes(notesFromApi)
                    Log.d("MyErrors", "Sync")

                    _state.value = _state.value.copy(
                        notes = mapToNoteModels(notes),
                        noteOrder = noteOrder,
                        isLoading = false,
                    )
                    sendResponseEvent(NotesResponseState.Success)
                }

                is ApiResponse.Error -> {
                    sendResponseEvent(NotesResponseState.Error(apiNotesResponse.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NotesResponseState.ErrorWithMessage(apiNotesResponse.message))
                }
            }

//            val response = when (_resultsMode.value) {
//                is NoteResultsMode.AllNotes -> noteRepository.getAllNotes(
//                    sortBy,
//                    order,
//                    isTrashed = false
//                )
//
//                is NoteResultsMode.FavoriteNotes -> favoriteNoteRepository.getAllFavoriteNotes(
//                    populateFields = "notes_id"
//                )
//
//                is NoteResultsMode.TrashNotes -> noteRepository.getAllNotes(
//                    sortBy,
//                    order,
//                    isTrashed = true
//                )
//
//                is NoteResultsMode.FolderNotes -> noteRepository.getAllNotes(
//                    sortBy,
//                    order,
//                    isTrashed = false
//                ) //noteRepository.getFolderNotes(sortBy, order)
//            }

//            when (response) {
//                is ApiResponse.Error -> {
//                    sendResponseEvent(NotesResponseState.Error(response.exception))
//                }
//
//                is ApiResponse.ErrorWithMessage -> {
//                    sendResponseEvent(NotesResponseState.ErrorWithMessage(response.message))
//                }
//
//                is ApiResponse.Success -> {
//                    _state.value = _state.value.copy(
//                        notes = mapToNoteModels(response.data),
//                        noteOrder = noteOrder,
//                        isLoading = false,
//                    )
//                    sendResponseEvent(NotesResponseState.Success)
//                }
//            }
        }
    }

    private fun getFolders() {
        viewModelScope.launch {
//            _state.value = _state.value.copy(isLoading = true)

            when (val response = folderRepository.getAllFolders(populateFields = "notes_id")) {
                is ApiResponse.Error -> {
                    sendResponseEvent(NotesResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NotesResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    _state.value = _state.value.copy(
                        folders = mapToFolderNotesModels(response.data),
//                        isLoading = false,
                    )
                    sendResponseEvent(NotesResponseState.Success)
                }
            }
        }
    }


    private fun getNotesWithoutFolders() {
        viewModelScope.launch {
//            _state.value = _state.value.copy(isLoading = true)

            when (val response = folderRepository.getAllWithoutFolders()) {
                is ApiResponse.Error -> {
                    sendResponseEvent(NotesResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NotesResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    _state.value = _state.value.copy(
                        notes = mapToNoteModels(response.data),
//                        isLoading = false,
                    )
                    sendResponseEvent(NotesResponseState.Success)
                }
            }
        }
    }

    private fun saveNewFolder() {
        if (_state.value.actualNoteId == null) {
            apiState = NotesResponseState.ErrorWithMessage("There is no selected note")
            viewModelScope.launch {
                responseEventChannel.send(apiState as NotesResponseState.ErrorWithMessage)
            }
            return
        }

        if (_state.value.actualFolder == null) {
            //The note has no previous folder
            toggleNoteFolder(_state.value.newSelectedFolder!!.id, _state.value.actualNoteId!!)
        } else if (_state.value.newSelectedFolder == null) {
            //The new note is "No folder"
            toggleNoteFolder(_state.value.actualFolder!!.id, _state.value.actualNoteId!!)
        } else {
            //The note has previous folder
            toggleNoteFolder(_state.value.actualFolder!!.id, _state.value.actualNoteId!!)
            toggleNoteFolder(_state.value.newSelectedFolder!!.id, _state.value.actualNoteId!!)
        }
    }

    private fun getNotesWithoutFolders(noteId: String) {
        executeOperation(
            operation = { folderRepository.getSelectedFolders(noteId) },
            onSuccess = { response ->
                _state.value = _state.value.copy(
                    newFolderList = mapToFolderModels(response.data.folders),
                    actualFolder = mapToFolderModel(response.data.actualFolder),
                )

                rebuildUI(_resultsMode.value)
            }
        )
    }

    private fun deleteNote(noteId: String) {
        executeOperation(
            operation = { noteRepository.deleteNoteById(noteId) },
            onSuccess = { rebuildUI(_resultsMode.value) }
        )
    }

    private fun toggleTrashNote(noteId: String) {
        executeOperation(
            operation = { noteRepository.toggleTrashNoteById(noteId) },
            onSuccess = { rebuildUI(_resultsMode.value) }
        )
    }

    private fun duplicateData(note: NoteModel) {
        addNewNote(
            note.title,
            note.message,
            note.tags,
            note.theme
        )
    }

    private fun addNewNote(title: String, message: String, tags: List<String>, color: String) {
        executeOperation(
            operation = { noteRepository.createNote(title, message, tags, color) },
            onSuccess = { response ->
                val idNewNote = response.data.id

                //If I want to duplicate a note in a selected folder
                if (_state.value.selectedFolder != null) {
                    val idFolder = _state.value.selectedFolder?.id ?: ""
                    toggleNoteFolder(idFolder, idNewNote)
                }

                rebuildUI(_resultsMode.value)
            }
        )
    }

    private fun toggleNoteFolder(folderId: String, noteId: String) {
        executeOperation(
            operation = {
                folderRepository.toggleFolder(
                    folderId,
                    noteId
                )
            },
            onSuccess = { rebuildUI(_resultsMode.value) }
        )
    }

    private fun <T> executeOperation(
        operation: suspend () -> ApiResponse<T>,
        onSuccess: ((ApiResponse.Success<T>) -> Unit)? = null
    ) {
        viewModelScope.launch {
            when (val response = operation()) {
                is ApiResponse.Error -> {
                    sendResponseEvent(NotesResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(NotesResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    onSuccess?.invoke(response)
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

    private fun mapToFolderNotesModels(folderDataList: List<FolderDataPopulated>): List<FolderPopulatedModel> {
        return folderDataList.map { folderData ->
            FolderPopulatedModel(
                id = folderData.id,
                userId = folderData.userId,
                name = folderData.name,
                theme = folderData.theme,
                notesId = mapToNoteModels(folderData.notesId),
                createdAt = folderData.createdAt,
                updatedAt = folderData.updatedAt
            )
        }
    }

    private fun mapToFolderModels(folderDataList: List<FolderData>): List<FolderModel> {
        return folderDataList.map { folderData ->
            FolderModel(
                id = folderData.id,
                userId = folderData.userId,
                name = folderData.name,
                theme = folderData.theme,
                notesId = folderData.notesId,
                createdAt = folderData.createdAt,
                updatedAt = folderData.updatedAt
            )
        }
    }

    private fun mapToFolderModel(folderData: FolderData?): FolderModel? {
        if (folderData == null) return null
        return FolderModel(
            id = folderData.id,
            userId = folderData.userId,
            name = folderData.name,
            theme = folderData.theme,
            notesId = folderData.notesId,
            createdAt = folderData.createdAt,
            updatedAt = folderData.updatedAt
        )

    }

    companion object {
        // Factory for creating instances of LoginViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as Application
                // Create a new instance of LoginViewModel with dependencies.
                NotesViewModel(
                    noteUseCases = NoteUseCases(
                        getNotes = GetNotes(),
                        deleteNote = DeleteNote(app.notesRepository),
                        addNote = AddNote()
                    ),
                    noteRepository = app.notesRepository,
                    favoriteNoteRepository = app.favoriteNotesRepository,
                    folderRepository = app.folderRepository,
                )
            }
        }
    }
}