package com.galacticstudio.digidoro.ui.screens.account.components.premium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.UserRepository
import com.galacticstudio.digidoro.ui.screens.noteitem.ActionType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PremiumViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val responseEventChannel = Channel<PremiumResponseState>()
    val responseEvents: Flow<PremiumResponseState> = responseEventChannel.receiveAsFlow()


    fun onEvent(event: PremiumUIEvent) {
        when (event) {
            is PremiumUIEvent.Upgrade -> {
                executeOperation(
                    operation = { userRepository.upgradeUser() },
                    onSuccess = { response ->

                    }
                )
            }
        }
    }

    private fun sendResponseEvent(event: PremiumResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun <T> executeOperation(
        operation: suspend () -> ApiResponse<T>,
        onSuccess: ((ApiResponse.Success<T>) -> Unit)? = null
    ) {
        viewModelScope.launch {
            when (val response = operation()) {
                is ApiResponse.Error -> {
                    sendResponseEvent(PremiumResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(PremiumResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    onSuccess?.invoke(response)
                    sendResponseEvent(PremiumResponseState.Success)
                }
            }
        }
    }

    companion object {
        // Factory for creating instances of PremiumViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                // Create a new instance of PremiumViewModel with dependencies.
                PremiumViewModel(
                    userRepository = app.userRepository
                )
            }
        }
    }
}

sealed class PremiumUIEvent {
    object Upgrade : PremiumUIEvent()
}

sealed class PremiumResponseState {
    object Resume : PremiumResponseState()
    class Error(val exception: Exception) : PremiumResponseState()
    data class ErrorWithMessage(val message: String) : PremiumResponseState()
    object Success : PremiumResponseState()
}