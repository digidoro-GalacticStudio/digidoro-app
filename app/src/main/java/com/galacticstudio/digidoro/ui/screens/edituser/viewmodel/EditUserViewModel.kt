package com.galacticstudio.digidoro.ui.screens.edituser.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.domain.usecase.fields.ValidateDate
import com.galacticstudio.digidoro.domain.usecase.fields.ValidatePhoneNumber
import com.galacticstudio.digidoro.domain.usecase.fields.ValidateTextField
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.UserRepository
import com.galacticstudio.digidoro.ui.screens.edituser.EditUserFormEvent
import com.galacticstudio.digidoro.ui.screens.edituser.EditUserFormState
import com.galacticstudio.digidoro.ui.screens.edituser.EditUserResponseState
import com.galacticstudio.digidoro.util.DateUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditUserViewModel(
    private val validatePhoneNumber: ValidatePhoneNumber = ValidatePhoneNumber(),
    private val validateDate: ValidateDate = ValidateDate(),
    private val validateTextField: ValidateTextField = ValidateTextField(),
    private val repository: UserRepository,
) : ViewModel() {
    // The current state of the EditUser form
    private val _state = mutableStateOf(EditUserFormState())
    val state: State<EditUserFormState> = _state

    // Channel for emitting EditUser response states.
    private val responseEventChannel = Channel<EditUserResponseState>()
    val responseEvents: Flow<EditUserResponseState> = responseEventChannel.receiveAsFlow()


    /**
     * Handles the EditUser form events.
     *
     * @param event The EditUser form event to handle.
     */
    fun onEvent(event: EditUserFormEvent) {
        when (event) {
            is EditUserFormEvent.FirstnameChanged -> {
                _state.value = state.value.copy(firstname = event.firstname)
                validateData()
            }

            is EditUserFormEvent.LastnameChanged -> {
                _state.value = state.value.copy(lastname = event.lastname)
                validateData()
            }

            is EditUserFormEvent.UsernameChanged -> {
                _state.value = state.value.copy(username = event.username)
                validateData()
            }

            is EditUserFormEvent.PhoneNumberChanged -> {
                _state.value = state.value.copy(phoneNumber = event.phone_number)
                validateData()
            }

            is EditUserFormEvent.BirthdayChanged -> {
                _state.value = state.value.copy(birthday = event.birthdate)
                validateData()
            }

            is EditUserFormEvent.Submit -> {
                submitData()
            }

            is EditUserFormEvent.Rebuild -> {
                getCurrentUser()
            }
        }
    }

    /**
     * Submits the form data for EditUser.
     */
    private fun submitData() {
        if (!validateData()) return
        onEditUser()
    }

    /**
     * Validates the form data and updates the state with any validation errors.
     *
     * @return `true` if the data is valid, `false` otherwise.
     */
    @SuppressLint("NewApi")
    private fun validateData(): Boolean {
        val firstnameResult = validateTextField.execute(_state.value.firstname, 40)
        val lastnameResult = validateTextField.execute(_state.value.lastname, 40)
        val usernameResult = validateTextField.execute(_state.value.username, 40)
        val phoneNumberResult = validatePhoneNumber.execute(_state.value.phoneNumber)
        val birthdayResult = validateDate.execute(_state.value.birthday)

        val hasError = listOf(
            firstnameResult,
            lastnameResult,
            usernameResult,
            phoneNumberResult,
            birthdayResult
        ).any { !it.successful }

        _state.value = state.value.copy(
            firstnameError = if (hasError) firstnameResult.errorMessage else null,
            lastnameError = if (hasError) lastnameResult.errorMessage else null,
            usernameError = if (hasError) usernameResult.errorMessage else null,
            phoneNumberError = if (hasError) phoneNumberResult.errorMessage else null,
            birthdayError = if (hasError) birthdayResult.errorMessage else null
        )

        return !hasError
    }

    /**
     * Sends a EditUser response event to the channel.
     *
     * @param event The EditUser response event to send.
     */
    private fun sendResponseEvent(event: EditUserResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun getCurrentUser() {
        executeOperation(
            operation = {repository.getCurrentUser()},
            onSuccess = { response ->
                _state.value = _state.value.copy(
                    firstname = response.data.firstname,
                    lastname = response.data.lastname,
                    username = response.data.username,
                    phoneNumber = response.data.phoneNumber,
                    birthday = DateUtils.convertDateFormat(response.data.dateOfBirth),
                )
            }
        )
    }

    /**
     * Function to edit user information.
     *
     * @param firstname The new first name of the user.
     * @param lastname The new last name of the user.
     * @param username The new username of the user.
     * @param dateOfBirth The new date of birth of the user.
     * @param phoneNumber The new phone number of the user.
     */
    private fun editUser(
        firstname: String,
        lastname: String,
        username: String,
        dateOfBirth: String,
        phoneNumber: String
    ) {
        executeOperation(
            operation = {
                repository.updateUser(
                    firstname = firstname,
                    lastname = lastname,
                    username = username,
                    dateOfBirth = DateUtils.convertToISO8601(dateOfBirth),
                    phoneNumber = phoneNumber
                )
            }
        )
    }

    private fun <T> executeOperation(
        operation: suspend () -> ApiResponse<T>,
        onSuccess: ((ApiResponse.Success<T>) -> Unit)? = null
    ) {
        viewModelScope.launch {
            when (val response = operation()) {
                is ApiResponse.Error -> {
                    sendResponseEvent(EditUserResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(EditUserResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    onSuccess?.invoke(response)
                    sendResponseEvent(EditUserResponseState.Success)
                }
            }
        }
    }

    /**
     * Handles the EditUser process triggered by the submit event.
     * Validates the form data and initiates the EditUser operation if the data is valid.
     */
    private fun onEditUser() {
        if (!validateData()) {
            sendResponseEvent(EditUserResponseState.ErrorWithMessage("Wrong information"))
            return
        }

        editUser(
            firstname = _state.value.firstname,
            lastname = _state.value.lastname,
            username = _state.value.username,
            dateOfBirth = _state.value.birthday,
            phoneNumber = _state.value.phoneNumber
        )
    }

    companion object {
        // Factory for creating instances of EditUserViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RetrofitApplication
                // Create a new instance of EditUserViewModel with dependencies.
                EditUserViewModel(
                    validatePhoneNumber = ValidatePhoneNumber(),
                    validateDate = ValidateDate(),
                    validateTextField = ValidateTextField(),
                    repository = app.userRepository
                )
            }
        }
    }
}