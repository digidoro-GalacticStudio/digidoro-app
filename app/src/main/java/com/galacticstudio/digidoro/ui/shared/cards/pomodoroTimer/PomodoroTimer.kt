package com.galacticstudio.digidoro.ui.shared.cards.pomodoroTimer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.util.shadowWithCorner

data class DataTime(val minutes: String, val seconds: String)

val cornerRadius = 10.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerBlock(time: String) {
    DigidoroTheme() {
        Box(
            modifier = Modifier
                .shadowWithCorner(
                    cornerRadius = cornerRadius,
                    shadowOffset = Offset(15.0f, 15.0f),
                    shadowColor = MaterialTheme.colorScheme.onPrimary
                )
                .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(cornerRadius))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(cornerRadius))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            AnimatedContent(targetState = time, transitionSpec = { addAnimation() }) {
                Text(text = time, style = MaterialTheme.typography.displayLarge)
            }
        }
    }
}

@ExperimentalAnimationApi
fun addAnimation(duration: Int = 300): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}

@Composable
fun PomodoroTimer(dataTime: DataTime) {
    Row {
        TimerBlock(time = dataTime.minutes)
        Text(
            text = ":", style = MaterialTheme.typography.displayLarge, modifier = Modifier
                .padding(7.dp)
                .padding(start = 7.8.dp)
        )
        TimerBlock(time = dataTime.seconds)
    }
}

