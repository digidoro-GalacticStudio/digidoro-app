package com.galacticstudio.digidoro.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.navigation.HOME_GRAPH_ROUTE
import com.galacticstudio.digidoro.ui.screens.account.components.options.OptionComposable
import com.galacticstudio.digidoro.ui.screens.account.components.options.OptionsComposable
import com.galacticstudio.digidoro.ui.screens.account.components.userInformation.UserInformation
import com.galacticstudio.digidoro.ui.screens.ranking.RankingUIEvent
import com.galacticstudio.digidoro.ui.screens.ranking.mapper.UserRankingMapper
import com.galacticstudio.digidoro.ui.screens.ranking.viewmodel.RankingViewModel
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme


@Composable
@Preview(showSystemUi = true)
fun AccountPreview() {
    DigidoroTheme() {
        AccountScreen(navController = rememberNavController())

    }
}

@Composable
fun AccountScreen(
    navController: NavController,
    rankingViewModel: RankingViewModel = viewModel(factory = RankingViewModel.Factory),
) {
    // Retrieve the application instance from the current context
    val app: Application = LocalContext.current.applicationContext as Application

    val state = rankingViewModel.state

    LaunchedEffect(Unit) {
        rankingViewModel.onEvent(RankingUIEvent.Rebuild)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            val levelName = UserRankingMapper.getRankingName(state.value.user?.totalScore ?: 0)
            
            UserInformation(
                userName = state.value.user?.username ?: "",
                profilePic = R.drawable.manage_account_icon,
                levelName = levelName,
                currentScore = state.value.user?.totalScore ?: 0,
                nextLevelScore = UserRankingMapper.getScoreRange(levelName)
            )

            Spacer(modifier = Modifier.height(30.dp))
            OptionsComposable(navController)
        }

        OptionComposable(
            title = "Logout",
            icon = R.drawable.arrow_back,
            description = "Logout",
            backgroundColor = MaterialTheme.colorScheme.secondary,
            color = MaterialTheme.colorScheme.primary
        ) {
            app.clearAuthToken()
            navController.navigate(HOME_GRAPH_ROUTE)
        }
    }

    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            painter = painterResource(R.drawable.arrow_back),
            contentDescription = null,
            modifier = Modifier.size(25.dp),
        )
    }
}