package com.galacticstudio.digidoro.ui.screens.ranking

import com.galacticstudio.digidoro.ui.shared.button.ToggleButtonOptionType

sealed class RankingUIEvent {
    object Rebuild : RankingUIEvent()
    data class ResultTypeChange(val type: ToggleButtonOptionType) : RankingUIEvent()
}