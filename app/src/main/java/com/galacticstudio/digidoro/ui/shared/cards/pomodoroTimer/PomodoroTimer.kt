package com.galacticstudio.digidoro.ui.shared.cards.pomodoroTimer

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
import com.galacticstudio.digidoro.util.shadowWithCorner

data class DataTime(val minutes: Int, val seconds: Int)

@Composable
fun TimerBlock(time: Int) {
    Box(
        modifier = Modifier
            .border(
                1.85.dp,
                colorResource(id = R.color.secondary_color),
                RoundedCornerShape(10.dp)
            )
            .shadowWithCorner(
                cornerRadius = 10.dp,
                shadowColor = colorResource(id = R.color.secondary_color),
                shadowOffset = Offset(17f, 17f)
            )
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = time.toString(), style = MaterialTheme.typography.displayLarge)
    }
}

@Composable
fun PomodoroTimer(dataTime: DataTime) {
    Row {
        TimerBlock(time = dataTime.minutes)
        Text(
            text = ":",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .padding(7.dp)
                .padding(start = 7.8.dp)
        )
        TimerBlock(time = dataTime.seconds)
    }
}

