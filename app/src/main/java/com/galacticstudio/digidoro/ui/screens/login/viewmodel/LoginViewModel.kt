package com.galacticstudio.digidoro.ui.screens.login.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.domain.usecase.fields.ValidateEmail
import com.galacticstudio.digidoro.domain.usecase.fields.ValidatePassword
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.CredentialsRepository
import com.galacticstudio.digidoro.repository.FavoriteNoteRepository
import com.galacticstudio.digidoro.repository.FolderRepository
import com.galacticstudio.digidoro.repository.NoteRepository
import com.galacticstudio.digidoro.repository.PomodoroRepository
import com.galacticstudio.digidoro.repository.TodoRepository
import com.galacticstudio.digidoro.ui.screens.login.LoginFormEvent
import com.galacticstudio.digidoro.ui.screens.login.LoginFormState
import com.galacticstudio.digidoro.ui.screens.login.LoginResponseState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel class for the login screen.
 *
 * @property validateEmail The email validation logic.
 * @property validatePassword The password validation logic.
 * @property repository The repository for handling login credentials.
 */
class LoginViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val repository: CredentialsRepository,
) : ViewModel() {
    // The current state of the login form
    var state by mutableStateOf(LoginFormState())

    // The current state of the login response from the API (nullable)
    private var apiState by mutableStateOf<LoginResponseState?>(LoginResponseState.Resume)

    // Channel for emitting login response states.
    private val responseEventChannel = Channel<LoginResponseState>()
    val responseEvents: Flow<LoginResponseState> = responseEventChannel.receiveAsFlow()


    /**
     * Handles the login form events.
     *
     * @param event The login form event to handle.
     */
    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailChanged -> {
                // When the email is changed, update the email value in the state
                state = state.copy(email = event.email)
                validateData() // Trigger data validation after the email change
            }

            is LoginFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
                validateData()
            }

            is LoginFormEvent.Submit -> {
                submitData()
            }
        }
    }

    /**
     * Submits the form data for login.
     */
    private fun submitData() {
        if (!validateData()) {
            apiState = LoginResponseState.ErrorWithMessage("Wrong information")
            return
        }

        login(email = state.email, password = state.password)
    }

    /**
     * Validates the form data and updates the state with any validation errors.
     *
     * @return `true` if the data is valid, `false` otherwise.
     */
    private fun validateData(): Boolean {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)

        val hasError = listOf(
            emailResult,
            passwordResult,
        ).any { !it.successful }

        state = state.copy(
            emailError = if (hasError) emailResult.errorMessage else null,
            passwordError = if (hasError) passwordResult.errorMessage else null
        )

        return !hasError
    }

    /**
     * Sends a login response event to the channel.
     *
     * @param event The login response event to send.
     */
    private fun sendResponseEvent(event: LoginResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    /**
     * Initiates the login process with the provided email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     */
    private fun login(email: String, password: String) {
        viewModelScope.launch {
            when (val response = repository.login(email, password)) {
                // If the response is an error, create a LoginResponseState.Error
                is ApiResponse.Error -> {
                    sendResponseEvent(LoginResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(LoginResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    sendResponseEvent(
                        LoginResponseState.Success(
                            response.data.token,
                            response.data.username,
                            response.data.roles,
                            response.data._id
                        )
                    )
                }
            }
        }
    }

    companion object {
        // Factory for creating instances of LoginViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as Application
                // Create a new instance of LoginViewModel with dependencies.
                LoginViewModel(
                    validateEmail = ValidateEmail(),
                    validatePassword = ValidatePassword(),
                    repository = app.credentialsRepository,
                )
            }
        }
    }

}