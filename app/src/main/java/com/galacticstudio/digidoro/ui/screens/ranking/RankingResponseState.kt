package com.galacticstudio.digidoro.ui.screens.ranking

sealed class RankingResponseState {
    object Resume : RankingResponseState()
    class Error(val exception: Exception) : RankingResponseState()
    data class ErrorWithMessage(val message: String) : RankingResponseState()
    object Success : RankingResponseState()
}