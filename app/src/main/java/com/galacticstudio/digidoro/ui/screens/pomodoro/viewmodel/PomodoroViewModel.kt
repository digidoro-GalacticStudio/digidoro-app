package com.galacticstudio.digidoro.ui.screens.pomodoro.viewmodel

import android.util.Log
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
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.PomodoroRepository
import com.galacticstudio.digidoro.ui.screens.pomodoro.PomodoroResponseState
import com.galacticstudio.digidoro.ui.screens.pomodoro.PomodoroUIEvent
import com.galacticstudio.digidoro.ui.screens.pomodoro.PomodoroUIState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val pomodoroRepository: PomodoroRepository,
) : ViewModel() {
    private val _state = mutableStateOf(PomodoroUIState())
    val state: State<PomodoroUIState> = _state

    private val _pomodoroColor = mutableStateOf("ffffff")
    val pomodoroColor: State<String> = _pomodoroColor

    // The current state of the Pomodoro response from the API (nullable)
    private var apiState by mutableStateOf<PomodoroResponseState?>(PomodoroResponseState.Resume)

    // Channel for emitting Pomodoro response states.
    private val responseEventChannel = Channel<PomodoroResponseState>()
    val responseEvents: Flow<PomodoroResponseState> = responseEventChannel.receiveAsFlow()

    fun onEvent(event: PomodoroUIEvent) {
        when (event) {
            is PomodoroUIEvent.SelectedPomodoroChanged -> {
                val selectedPomodoro = event.pomodoro
                // Handle the selected pomodoro change
            }
            is PomodoroUIEvent.Rebuild -> {
                // Handle the rebuild event
            }
            is PomodoroUIEvent.ClearData -> {
                // Handle the clear data event
            }
            is PomodoroUIEvent.NameChanged -> {
                val name = event.name
                // Handle the name changed event
            }
            is PomodoroUIEvent.SessionsCompletedChanged -> {
                val sessionsCompleted = event.sessionsCompleted
                // Handle the sessions completed changed event
            }
            is PomodoroUIEvent.TotalSessionsChanged -> {
                val totalSessions = event.totalSessions
                // Handle the total sessions changed event
            }
            is PomodoroUIEvent.SavePomodoro -> {
                Log.d("MyErrors", "SAVE THE POMODODO -------------")
                // Handle the save pomodoro event
            }
            is PomodoroUIEvent.UpdatePomodoro -> {
                // Handle the update pomodoro event
            }
            is PomodoroUIEvent.DeletePomodoro -> {
                // Handle the delete pomodoro event
            }
        }
    }


    private fun sendResponseEvent(event: PomodoroResponseState) {
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
                    sendResponseEvent(PomodoroResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(PomodoroResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    onSuccess?.invoke(response)
                    sendResponseEvent(PomodoroResponseState.Success)
                }
            }
        }
    }
    
    companion object {
        // Factory for creating instances of PomodoroViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                // Create a new instance of PomodoroViewModel with dependencies.
                PomodoroViewModel(
                    pomodoroRepository = app.pomodoroRepository,
                )
            }
        }
    }
}
