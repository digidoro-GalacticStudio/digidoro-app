package com.galacticstudio.digidoro.ui.screens.forgotpassword.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.domain.usecase.fields.ValidateEmail
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.UserRepository
import com.galacticstudio.digidoro.ui.screens.forgotpassword.ForgotPasswordFormEvent
import com.galacticstudio.digidoro.ui.screens.forgotpassword.ForgotPasswordFormState
import com.galacticstudio.digidoro.ui.screens.forgotpassword.ForgotPasswordResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val repository: UserRepository,
) : ViewModel() {
    // The current state of the ForgotPassword form
    private val _state = mutableStateOf(ForgotPasswordFormState())
    val state: State<ForgotPasswordFormState> = _state

    // The current state of the ForgotPassword response from the API (nullable)
    private var apiState by mutableStateOf<ForgotPasswordResponse?>(
        ForgotPasswordResponse.Resume
    )

    // Channel for emitting ForgotPassword response states.
    private val responseEventChannel = Channel<ForgotPasswordResponse>()
    val responseEvents: Flow<ForgotPasswordResponse> = responseEventChannel.receiveAsFlow()


    fun onEvent(event: ForgotPasswordFormEvent) {
        when (event) {
            is ForgotPasswordFormEvent.EmailChanged -> {
                _state.value = state.value.copy(email = event.email)
                validateData()
            }

            is ForgotPasswordFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun validateData(): Boolean {
        val emailResult = validateEmail.execute(state.value.email)

        val hasError = listOf(
            emailResult,
        ).any { !it.successful }

        _state.value = _state.value.copy(
            emailError = if (hasError) emailResult.errorMessage else null,
        )

        return !hasError
    }

    private fun submitData() {
        if (!validateData()) {
            sendResponseEvent(ForgotPasswordResponse.ErrorWithMessage("Wrong information"))
            return
        }
        forgotPasswordSubmit(_state.value.email)
    }

    private fun sendResponseEvent(event: ForgotPasswordResponse) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun forgotPasswordSubmit(
        email: String,
    ) {
        viewModelScope.launch {
            when (val response = repository.emailRecuperation(email)) {
                // If the response is an error, create a ForgotPasswordResponseState.Error
                is ApiResponse.Error -> {
                    sendResponseEvent(ForgotPasswordResponse.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(ForgotPasswordResponse.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    sendResponseEvent(ForgotPasswordResponse.Success)
                }
            }
        }
    }

    companion object {
        // Factory for creating instances of ForgotPasswordViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[APPLICATION_KEY] as RetrofitApplication
                // Create a new instance of ForgotPasswordViewModel with dependencies.
                ForgotPasswordViewModel(
                    repository = app.userRepository
                )
            }
        }
    }
}