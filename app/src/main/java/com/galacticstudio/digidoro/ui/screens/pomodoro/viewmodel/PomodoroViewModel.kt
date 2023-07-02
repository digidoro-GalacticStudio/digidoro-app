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
import com.galacticstudio.digidoro.data.PomodoroModel
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroData
import com.galacticstudio.digidoro.repository.PomodoroRepository
import com.galacticstudio.digidoro.ui.screens.noteitem.ActionType
import com.galacticstudio.digidoro.ui.screens.pomodoro.PomodoroResponseState
import com.galacticstudio.digidoro.ui.screens.pomodoro.PomodoroTimerState
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

    private val _pomodoroType = mutableStateOf(PomodoroTimerState.Pomodoro)
    val pomodoroType: State<PomodoroTimerState> = _pomodoroType

    // The current state of the Pomodoro response from the API (nullable)
    private var apiState by mutableStateOf<PomodoroResponseState?>(PomodoroResponseState.Resume)

    // Channel for emitting Pomodoro response states.
    private val responseEventChannel = Channel<PomodoroResponseState>()
    val responseEvents: Flow<PomodoroResponseState> = responseEventChannel.receiveAsFlow()

    fun onEvent(event: PomodoroUIEvent) {
        when (event) {
            is PomodoroUIEvent.SelectedPomodoroChanged -> {
                _state.value = _state.value.copy(
                    selectedPomodoro = event.pomodoro
                )
            }

            is PomodoroUIEvent.Rebuild -> {
                // Handle the rebuild event
                Log.d("MyErrors", "Enter in rebuild alll")
                getAllPomodoros()
            }

            is PomodoroUIEvent.TypeChanged -> {
                _pomodoroType.value = event.type
            }

            is PomodoroUIEvent.ClearData -> {
                // Handle the clear data event
            }

            is PomodoroUIEvent.NameChanged -> {
                _state.value = _state.value.copy(
                    name = event.name
                )
                // Handle the name changed event
            }

            is PomodoroUIEvent.SessionsCompletedChanged -> {
                _state.value = _state.value.copy(
                    sessionsCompleted = event.sessionsCompleted
                )
            }

            is PomodoroUIEvent.TotalSessionsChanged -> {
                _state.value = _state.value.copy(
                    totalSessions = event.totalSessions
                )
            }

            is PomodoroUIEvent.SavePomodoro -> {
                addPomodoro(
                    _state.value.name,
                    0,
                    _state.value.totalSessions,
                    "#${_pomodoroColor.value}"
                )
            }

            is PomodoroUIEvent.UpdatePomodoro -> {
                Log.d("MyErrors", "------:------ ${_state.value.selectedPomodoro}")
                _state.value.selectedPomodoro?.let {
                    updatePomodoro(
                        it.id,
                        it.name,
                        it.sessionsCompleted + 1,
                        it.totalSessions,
                        it.theme,
                    )
                }
            }

            is PomodoroUIEvent.ThemeChanged -> {
                _pomodoroColor.value = event.color
            }

            is PomodoroUIEvent.DeletePomodoro -> {
                // Handle the delete pomodoro event
            }
        }
    }



    private fun getAllPomodoros() {
        Log.d("MyErrors", "Gett alll")
        executeOperation(
            operation = { pomodoroRepository.getAllPomodoros() },
            onSuccess = { response ->
                Log.d("MyErrors", "QUe paso ? ${response}")
                _state.value = _state.value.copy(
                    pomodoroList = mapToPomodoroModels(response.data)
                )

            }
        )
    }

    private fun addPomodoro(
        name: String,
        sessionsCompleted: Int,
        totalSessions: Int,
        theme: String
    ) {
        executeOperation(
            operation = {
                pomodoroRepository.createPomodoro(
                    name,
                    sessionsCompleted,
                    totalSessions,
                    theme
                )
            },
            onSuccess = { response ->
                getAllPomodoros()
            }
        )
    }

    private fun updatePomodoro(
        pomodoroId: String,
        name: String,
        sessionsCompleted: Int,
        totalSessions: Int,
        theme: String
    ) {
        Log.d("MyErrors", "tHIS IS AN UPDATE: ${totalSessions}")
        executeOperation(
            operation = {
                pomodoroRepository.updatePomodoro(
                    pomodoroId,
                    name,
                    sessionsCompleted,
                    totalSessions,
                    theme
                )
            },
            onSuccess = { response ->
                getAllPomodoros()
            }
        )
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

    private fun mapToPomodoroModels(pomodoroDataList: List<PomodoroData>): List<PomodoroModel> {
        return pomodoroDataList.map { pomodoroData ->
            PomodoroModel(
                id = pomodoroData.id,
                userId = pomodoroData.user_id,
                name = pomodoroData.name,
                sessionsCompleted = pomodoroData.sessionsCompleted,
                totalSessions = pomodoroData.totalSessions,
                theme = pomodoroData.theme,
                createdAt = pomodoroData.createdAt,
                updatedAt = pomodoroData.updatedAt
            )
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
