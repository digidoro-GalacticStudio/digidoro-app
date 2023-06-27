package com.galacticstudio.digidoro.ui.screens.ranking.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.data.UserRankingModel
import com.galacticstudio.digidoro.domain.util.OrderType
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.ranking.UserInformationData
import com.galacticstudio.digidoro.repository.RankingRepository
import com.galacticstudio.digidoro.ui.screens.ranking.RankingResponseState
import com.galacticstudio.digidoro.ui.screens.ranking.RankingUIEvent
import com.galacticstudio.digidoro.ui.screens.ranking.RankingUIState
import com.galacticstudio.digidoro.ui.shared.button.ToggleButtonOptionType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RankingViewModel(
    private val repository: RankingRepository,
) : ViewModel() {
    // The current state of the Ranking UI
    private val _state = mutableStateOf(RankingUIState())
    val state: State<RankingUIState> = _state

    // The current state of the Ranking response from the API (nullable)
    private var apiState by mutableStateOf<RankingResponseState?>(
        RankingResponseState.Resume
    )

    // Channel for emitting Ranking response states.
    private val responseEventChannel = Channel<RankingResponseState>()
    val responseEvents: Flow<RankingResponseState> = responseEventChannel.receiveAsFlow()

    fun onEvent(event: RankingUIEvent) {
        when (event) {
            is RankingUIEvent.Rebuild -> {
                getOwnRanking()
                getTopRanking(ToggleButtonOptionType.Today)
            }

            is RankingUIEvent.ResultTypeChange -> {
                _state.value = _state.value.copy(resultType = event.type)
                getTopRanking(event.type)
            }
        }
    }

    private fun sendResponseEvent(event: RankingResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun getOwnRanking() {
        executeOperation(
            operation = { repository.getOwnRanking() },
            onSuccess = { response ->
                val user = response.data.data
                _state.value = _state.value.copy(user = mapToUserRankingModel(user))
            }
        )
    }

    private fun getTopRanking(type: ToggleButtonOptionType) {

        val order = when (type) {
            is ToggleButtonOptionType.Today -> "daily_score"
            is ToggleButtonOptionType.Weekly -> "weekly_score"
            is ToggleButtonOptionType.Monthly -> "monthly_score"
            else -> {
                "daily_score"
            }
        }
        executeOperation(
            operation = { repository.getTopUsers(order) },
            onSuccess = { response ->
                val usersList = response.data.data
                _state.value = _state.value.copy(users = mapToUserRankingModelList(usersList))
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
                    sendResponseEvent(RankingResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(RankingResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    onSuccess?.invoke(response)
                    sendResponseEvent(RankingResponseState.Success)
                }
            }
        }
    }

    private fun mapToUserRankingModel(userInformationData: UserInformationData): UserRankingModel {
        return UserRankingModel(
            id = userInformationData.id,
            firstname = userInformationData.firstname,
            username = userInformationData.username,
            profilePic = userInformationData.profilePic,
            level = getRankingName(userInformationData.totalScore),
            dailyScore = userInformationData.dailyScore,
            weeklyScore = userInformationData.weeklyScore,
            monthlyScore = userInformationData.monthlyScore,
            totalScore = userInformationData.totalScore
        )
    }

    private fun mapToUserRankingModelList(userInformationDataList: List<UserInformationData>): List<UserRankingModel> {
        return userInformationDataList.map { userInformationData ->
            UserRankingModel(
                id = userInformationData.id,
                firstname = userInformationData.firstname,
                username = userInformationData.username,
                profilePic = userInformationData.profilePic,
                level = getRankingName(userInformationData.totalScore),
                dailyScore = userInformationData.dailyScore,
                weeklyScore = userInformationData.weeklyScore,
                monthlyScore = userInformationData.monthlyScore,
                totalScore = userInformationData.totalScore
            )
        }
    }

    private fun getRankingName(score: Int): String {
        return when (score) {
            in 0 until 250 -> "Dreamer"
            in 250 until 750 -> "Thunder"
            in 750 until 1000 -> "Engineer"
            in 1000 until 1300 -> "Master"
            else -> "Legend"
        }
    }


    companion object {
        // Factory for creating instances of RankingViewModel.
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RetrofitApplication
                // Create a new instance of RankingViewModel with dependencies.
                RankingViewModel(
                    repository = app.rankingRepository,
                )
            }
        }
    }
}