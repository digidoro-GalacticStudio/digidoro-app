package com.galacticstudio.digidoro.ui.screens.verifyaccount.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.domain.usecase.fields.ValidateTextField
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.UserRepository
import com.galacticstudio.digidoro.ui.screens.verifyaccount.VerifyAccountFormEvent
import com.galacticstudio.digidoro.ui.screens.verifyaccount.VerifyAccountFormState
import com.galacticstudio.digidoro.ui.screens.verifyaccount.VerifyAccountResponseState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class VerifyAccountViewModel(
    private val validateField: ValidateTextField = ValidateTextField(),
    private val repository: UserRepository,
) : ViewModel() {
    // The current state of the VerifyAccount form
    private val _state = mutableStateOf(VerifyAccountFormState())
    val state: State<VerifyAccountFormState> = _state

    // The current state of the VerifyAccount response from the API (nullable)
    private var apiState by mutableStateOf<VerifyAccountResponseState?>(
        VerifyAccountResponseState.Resume
    )

    // Channel for emitting VerifyAccount response states.
    private val responseEventChannel = Channel<VerifyAccountResponseState>()
    val responseEvents: Flow<VerifyAccountResponseState> = responseEventChannel.receiveAsFlow()


    fun onEvent(event: VerifyAccountFormEvent) {
        when (event) {
            is VerifyAccountFormEvent.CodeChanged -> {
                _state.value = state.value.copy(code = event.code)
                validateData()
            }

            is VerifyAccountFormEvent.Submit -> {
                submitData()
            }

            is VerifyAccountFormEvent.EmailChanged -> {
                _state.value = state.value.copy(email = event.email)
            }

            is VerifyAccountFormEvent.ResendCode -> {
                resendCode(_state.value.email)
            }
        }
    }

    private fun validateData(): Boolean {
        val codeResult = validateField.execute(state.value.code, 8)

        val hasError = listOf(
            codeResult,
        ).any { !it.successful }

        _state.value = _state.value.copy(
            codeError = if (hasError) codeResult.errorMessage else null,
        )

        return !hasError
    }


    private fun resendCode(
        email: String,
    ) {
        viewModelScope.launch {
            when (val response = repository.emailRecuperation(email)) {
                is ApiResponse.Error -> {
                    sendResponseEvent(VerifyAccountResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(VerifyAccountResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    sendResponseEvent(VerifyAccountResponseState.Success(false))
                }
            }
        }
    }

    private fun submitData() {
        if (!validateData()) {
            sendResponseEvent(VerifyAccountResponseState.ErrorWithMessage("Wrong information"))
            return
        }
        sendResponseEvent(VerifyAccountResponseState.Success(true))
    }

    private fun sendResponseEvent(event: VerifyAccountResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    companion object {
        // Factory for creating instances of VerifyAccountViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                // Create a new instance of VerifyAccountViewModel with dependencies.
                VerifyAccountViewModel(
                    repository = app.userRepository
                )
            }
        }
    }
}