package com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel

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
import com.galacticstudio.digidoro.domain.usecase.fields.ValidateTextField
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.FolderRepository
import com.galacticstudio.digidoro.ui.screens.noteitem.ActionType
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemResponseState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FolderViewModel(
    private val validateTextField: ValidateTextField = ValidateTextField(),
    private val folderRepository: FolderRepository,
) : ViewModel() {

    private val _state = mutableStateOf(FolderState(""))
    val state: State<FolderState> = _state

    private val _noteColor = mutableStateOf("ffffff")
    val noteColor: State<String> = _noteColor

    // The current state of the notes response from the API (nullable)
    private var apiState by mutableStateOf<FolderResponseState?>(FolderResponseState.Resume)

    // Channel for emitting notes response states.
    private val responseEventChannel = Channel<FolderResponseState>()
    val responseEvents: Flow<FolderResponseState> = responseEventChannel.receiveAsFlow()

    fun onEvent(event: FolderEvent) {
        when (event) {
            is FolderEvent.NameChanged -> {
                _state.value = state.value.copy(name = event.name)
            }

            is FolderEvent.ColorChanged -> {
                _noteColor.value = event.color
            }

            is FolderEvent.SaveFolder -> {
                submitData()
            }
        }
    }

    private fun sendResponseEvent(event: FolderResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun submitData() {
        if (!validateData()) {
            apiState = FolderResponseState.ErrorWithMessage("Wrong information")
            return
        }

        addNewFolder(_state.value.name, "#${_noteColor.value}", emptyList())
    }

    private fun validateData(): Boolean {
        val nameResult = validateTextField.execute(_state.value.name, 20)

        val hasError = listOf(
            nameResult,
        ).any { !it.successful }

        _state.value = state.value.copy(
            nameError = if (hasError) nameResult.errorMessage else null,
        )

        return !hasError
    }

    private fun addNewFolder(name: String, color: String, notesId: List<String> ) {
        viewModelScope.launch {
            when (val response = folderRepository.createFolder(name, color, notesId)) {
                is ApiResponse.Error -> {
                    sendResponseEvent(FolderResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(FolderResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    sendResponseEvent(FolderResponseState.Success)
                }
            }
        }
    }

    companion object {
        // Factory for creating instances of LoginViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RetrofitApplication
                // Create a new instance of LoginViewModel with dependencies.
                FolderViewModel(
                    validateTextField = ValidateTextField(),
                    folderRepository = app.folderRepository,
                )
            }
        }
    }
}

sealed class FolderEvent {
    data class NameChanged(val name: String): FolderEvent()
    data class ColorChanged(val color: String): FolderEvent()
    object SaveFolder: FolderEvent()
}

data class FolderState (
    val name: String,
    val nameError: String? = null,
)

sealed class FolderResponseState {
    object Resume : FolderResponseState()
    class Error(val exception: Exception) : FolderResponseState()
    data class ErrorWithMessage(val message: String) : FolderResponseState()
    object Success : FolderResponseState()
}