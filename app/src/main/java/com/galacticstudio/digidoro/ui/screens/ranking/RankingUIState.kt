package com.galacticstudio.digidoro.ui.screens.ranking

import com.galacticstudio.digidoro.data.api.UserRankingModel
import com.galacticstudio.digidoro.ui.shared.button.ToggleButtonOptionType

data class RankingUIState (
    val user: UserRankingModel? = null,
    val users: List<UserRankingModel> = emptyList(),
    val resultType: ToggleButtonOptionType = ToggleButtonOptionType.Today
)