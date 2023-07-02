package com.galacticstudio.digidoro.ui.screens.pomodoro.components

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltipBox
import androidx.compose.material3.RichTooltipState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroRichTooltip(
    tooltipState: RichTooltipState,
    scope: CoroutineScope
) {
    RichTooltipBox(
        title = {
            Text(
                stringResource(R.string.pomodoro_title_tooltip),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.W800
            )
        },
        action = {},
        text = {
            Text(
                text = stringResource(R.string.pomodoro_tooltip),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.offset(y=(25).dp)
            )
        },
        tooltipState = tooltipState
    ) {}
}