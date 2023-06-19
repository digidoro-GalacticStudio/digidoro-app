package com.galacticstudio.digidoro.ui.screens.register.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.domain.usecase.ValidateDate
import com.galacticstudio.digidoro.domain.usecase.ValidateEmail
import com.galacticstudio.digidoro.domain.usecase.ValidatePassword
import com.galacticstudio.digidoro.domain.usecase.ValidatePhoneNumber
import com.galacticstudio.digidoro.domain.usecase.ValidateRepeatedPassword
import com.galacticstudio.digidoro.domain.usecase.ValidateTextField
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.CredentialsRepository
import com.galacticstudio.digidoro.ui.screens.register.RegisterFormEvent
import com.galacticstudio.digidoro.ui.screens.register.RegisterFormState
import com.galacticstudio.digidoro.ui.screens.register.RegisterResponseState
import com.galacticstudio.digidoro.util.DateUtils
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
class RegisterViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
    private val validatePhoneNumber: ValidatePhoneNumber = ValidatePhoneNumber(),
    private val validateDate: ValidateDate = ValidateDate(),
    private val validateTextField: ValidateTextField = ValidateTextField(),
    private val repository: CredentialsRepository,
) : ViewModel() {

    // The current state of the register form
    var state by mutableStateOf(RegisterFormState())

    // The current state of the register response from the API (nullable)
    private var apiState by mutableStateOf<RegisterResponseState?>(RegisterResponseState.Resume)

    // Channel for emitting register response states.
    private val responseEventChannel = Channel<RegisterResponseState>()
    val responseEvents: Flow<RegisterResponseState> = responseEventChannel.receiveAsFlow()


    /**
     * Handles the register form events.
     *
     * @param event The register form event to handle.
     */
    fun onEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.EmailChanged -> {
                // When the email is changed, update the email value in the state
                state = state.copy(email = event.email)
                validateData() // Trigger data validation after the email change
            }

            is RegisterFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
                validateData()
            }

            is RegisterFormEvent.FirstnameChanged -> {
                state = state.copy(firstname = event.firstname)
                validateData()
            }

            is RegisterFormEvent.LastnameChanged -> {
                state = state.copy(lastname = event.lastname)
                validateData()
            }

            is RegisterFormEvent.UsernameChanged -> {
                state = state.copy(username = event.username)
                validateData()
            }

            is RegisterFormEvent.PhoneNumberChanged -> {
                state = state.copy(phoneNumber = event.phone_number)
                validateData()
            }

            is RegisterFormEvent.BirthdayChanged -> {
                state = state.copy(birthday = event.birthdate)
                validateData()
            }

            is RegisterFormEvent.ConfirmPasswordChanged -> {
                state = state.copy(repeatedPassword = event.confirmPassword)
                validateData()
            }

            is RegisterFormEvent.Submit -> {
                submitData()
            }
        }
    }

    /**
     * Submits the form data for register.
     */
    private fun submitData() {
        if (!validateData()) return
        onRegister()
    }

    /**
     * Validates the form data and updates the state with any validation errors.
     *
     * @return `true` if the data is valid, `false` otherwise.
     */
    @SuppressLint("NewApi")
    private fun validateData(): Boolean {
        val firstnameResult = validateTextField.execute(state.firstname, 40)
        val lastnameResult = validateTextField.execute(state.lastname, 40)
        val usernameResult = validateTextField.execute(state.username, 40)
        val phoneNumberResult = validatePhoneNumber.execute(state.phoneNumber)
        val birthdayResult = validateDate.execute(state.birthday)
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val repeatedPassword =
            validateRepeatedPassword.execute(state.password, state.repeatedPassword)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPassword,
            firstnameResult,
            lastnameResult,
            usernameResult,
            phoneNumberResult,
            birthdayResult
        ).any { !it.successful }

        state = state.copy(
            emailError = if (hasError) emailResult.errorMessage else null,
            passwordError = if (hasError) passwordResult.errorMessage else null,
            repeatedPasswordError = if (hasError) repeatedPassword.errorMessage else null,
            firstnameError = if (hasError) firstnameResult.errorMessage else null,
            lastnameError = if (hasError) lastnameResult.errorMessage else null,
            usernameError = if (hasError) usernameResult.errorMessage else null,
            phoneNumberError = if (hasError) phoneNumberResult.errorMessage else null,
            birthdayError = if (hasError) birthdayResult.errorMessage else null
        )

        return !hasError
    }

    /**
     * Sends a register response event to the channel.
     *
     * @param event The register response event to send.
     */
    private fun sendResponseEvent(event: RegisterResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    /**
     * Initiates the Register process with the provided email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     */
    private fun register(
        firstname: String,
        lastname: String,
        email: String,
        username: String,
        password: String,
        dateOfBirth: String,
        phoneNumber: String
    ) {
        viewModelScope.launch {
            when (val response = repository.register(
                firstname = firstname,
                lastname = lastname,
                email = email,
                username = username,
                password = password,
                dateOfBirth = DateUtils.convertToISO8601(dateOfBirth),
                phoneNumber = phoneNumber
            )) {
                // If the response is an error, create a RegisterResponseState.Error
                is ApiResponse.Error -> {
                    sendResponseEvent(RegisterResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(RegisterResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    sendResponseEvent(RegisterResponseState.Success)
                }
            }
        }
    }

    /**
     * Handles the Register process triggered by the submit event.
     * Validates the form data and initiates the Register operation if the data is valid.
     */
    private fun onRegister() {
        if (!validateData()) {
            apiState = RegisterResponseState.ErrorWithMessage("Wrong information")
            return
        }

        register(
            firstname = state.firstname,
            lastname = state.lastname,
            email = state.email,
            username = state.username,
            password = state.password,
            dateOfBirth = state.birthday,
            phoneNumber = state.phoneNumber
        )
    }

    companion object {
        // Factory for creating instances of RegisterViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as RetrofitApplication
                // Create a new instance of RegisterViewModel with dependencies.
                RegisterViewModel(
                    validateEmail = ValidateEmail(),
                    validatePassword = ValidatePassword(),
                    validateRepeatedPassword = ValidateRepeatedPassword(),
                    validatePhoneNumber = ValidatePhoneNumber(),
                    validateDate = ValidateDate(),
                    validateTextField = ValidateTextField(),
                    repository = app.credentialsRepository
                )
            }
        }
    }
}