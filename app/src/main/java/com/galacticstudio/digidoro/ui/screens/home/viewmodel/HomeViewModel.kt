package com.galacticstudio.digidoro.ui.screens.home.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.data.api.PomodoroModel
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroData
import com.galacticstudio.digidoro.repository.PomodoroRepository
import com.galacticstudio.digidoro.repository.RankingRepository
import com.galacticstudio.digidoro.repository.TodoRepository
import com.galacticstudio.digidoro.ui.screens.home.HomeResponseState
import com.galacticstudio.digidoro.ui.screens.home.HomeUIEvent
import com.galacticstudio.digidoro.ui.screens.home.HomeUIState
import com.galacticstudio.digidoro.ui.screens.ranking.mapper.UserRankingMapper.mapToUserRankingModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val rankingRepository: RankingRepository,
    private val todoRepository: TodoRepository,
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {
    // The current state of the login form
    private val _state = mutableStateOf(HomeUIState())
    val state: State<HomeUIState> = _state

    // Channel for emitting Ranking response states.
    private val responseEventChannel = Channel<HomeResponseState>()
    val responseEvents: Flow<HomeResponseState> = responseEventChannel.receiveAsFlow()

    /**
     * Handles the home UI events.
     *
     * @param event The UI event to handle.
     */
    fun onEvent(event: HomeUIEvent) {
        when (event) {
            is HomeUIEvent.UsernameChanged -> {
                // When the email is changed, update the email value in the state
                _state.value = state.value.copy(username = event.username)
            }

            is HomeUIEvent.Rebuild -> {
                getOwnRanking()
                getAllTodo()
                getAllPomodoros()
            }
        }
    }


    private fun sendResponseEvent(event: HomeResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun getOwnRanking() {
        executeOperation(
            operation = { rankingRepository.getOwnRanking() },
            onSuccess = { response ->
                val user = response.data.data
                _state.value = _state.value.copy(user = mapToUserRankingModel(user))
            }
        )
    }

    private fun getAllTodo() {
        executeOperation(
            operation = {
                todoRepository.getAllTodo(
                    sortBy = "createdAt",
                    order = "desc",
                    page = 1,
                    limit = 10,
                )
            },
            onSuccess = { response ->
                _state.value = _state.value.copy(
                    todos = response.data,
                )
            }
        )
    }

    private fun getAllPomodoros() {
        executeOperation(
            operation = {
                pomodoroRepository.getAllPomodoros(
                    sortBy = "createdAt",
                    order = "desc",
                    page = 1,
                    limit = 10,
                )
            },
            onSuccess = { response ->
                _state.value = _state.value.copy(
                    pomodoros = mapToPomodoroModels(response.data),
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
                    sendResponseEvent(HomeResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(HomeResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    onSuccess?.invoke(response)
                    sendResponseEvent(HomeResponseState.Success)
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
        // Factory for creating instances of RankingViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                // Create a new instance of RankingViewModel with dependencies.
                HomeViewModel(
                    rankingRepository = app.rankingRepository,
                    todoRepository = app.todoRepository,
                    pomodoroRepository = app.pomodoroRepository
                )
            }
        }
    }
}