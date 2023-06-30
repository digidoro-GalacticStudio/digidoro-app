package com.galacticstudio.digidoro.ui.screens.editcredentials.components

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.domain.usecase.fields.ValidatePassword
import com.galacticstudio.digidoro.domain.usecase.fields.ValidateRepeatedPassword
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.CredentialsRepository
import com.galacticstudio.digidoro.ui.screens.editcredentials.EditCredentialsFormEvent
import com.galacticstudio.digidoro.ui.screens.editcredentials.EditCredentialsFormState
import com.galacticstudio.digidoro.ui.screens.editcredentials.EditCredentialsResponseState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditCredentialsViewModel(
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
    private val repository: CredentialsRepository,
) : ViewModel() {
    // The current state of the EditCredentials form
    private val _state = mutableStateOf(EditCredentialsFormState())
    val state: State<EditCredentialsFormState> = _state

    // The current state of the EditCredentials response from the API (nullable)
    private var apiState by mutableStateOf<EditCredentialsResponseState?>(
        EditCredentialsResponseState.Resume
    )

    // Channel for emitting EditCredentials response states.
    private val responseEventChannel = Channel<EditCredentialsResponseState>()
    val responseEvents: Flow<EditCredentialsResponseState> = responseEventChannel.receiveAsFlow()


    fun onEvent(event: EditCredentialsFormEvent) {
        when (event) {
            is EditCredentialsFormEvent.PasswordChanged -> {
                _state.value = state.value.copy(password = event.password)
                validateData()
            }

            is EditCredentialsFormEvent.ConfirmPasswordChanged -> {
                _state.value = state.value.copy(repeatedPassword = event.confirmPassword)
                validateData()
            }

            is EditCredentialsFormEvent.Submit -> {
                submitData()
            }

            is EditCredentialsFormEvent.VerificationCodeChanged -> {
                _state.value = state.value.copy(verificationCode = event.code)
            }

            is EditCredentialsFormEvent.EmailChanged -> {
                _state.value = state.value.copy(email = event.email)
            }
        }
    }

    private fun validateData(): Boolean {
        val passwordResult = validatePassword.execute(state.value.password)
        val repeatedPassword =
            validateRepeatedPassword.execute(_state.value.password, _state.value.repeatedPassword)

        val hasError = listOf(
            passwordResult,
            repeatedPassword,
        ).any { !it.successful }

        _state.value = _state.value.copy(
            passwordError = if (hasError) passwordResult.errorMessage else null,
            repeatedPasswordError = if (hasError) repeatedPassword.errorMessage else null,
        )

        return !hasError
    }

    private fun submitData() {
        if (!validateData()) {
            sendResponseEvent(EditCredentialsResponseState.ErrorWithMessage("Wrong information"))
            return
        }
        editCredentialSubmit(
            _state.value.email,
            _state.value.verificationCode,
            _state.value.password
        )
    }

    private fun sendResponseEvent(event: EditCredentialsResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun editCredentialSubmit(
        email: String,
        recoveryCode: Int,
        newPassword: String,
    ) {
        viewModelScope.launch {
            when (val response = repository.recoveryCredentials(email, recoveryCode, newPassword)) {
                // If the response is an error, create a EditCredentialsResponseState.Error
                is ApiResponse.Error -> {
                    sendResponseEvent(EditCredentialsResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(EditCredentialsResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    sendResponseEvent(EditCredentialsResponseState.Success)
                }
            }
        }
    }

    companion object {
        // Factory for creating instances of EditCredentialsViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[APPLICATION_KEY] as Application
                // Create a new instance of EditCredentialsViewModel with dependencies.
                EditCredentialsViewModel(
                    repository = app.credentialsRepository
                )
            }
        }
    }
}