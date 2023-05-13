package com.galacticstudio.digidoro.ui.shared.cards.pomodoroTimer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.shared.roundShadowBox.RoundShadowBox

data class DataTime(val minutes: Int, val seconds: Int)

@Composable
fun TimerBlock(time:Int){
    RoundShadowBox(
        shadowColor = colorResource(id = R.color.secondary_color),
        borderRadius = 10.dp,
        XYOffset = 5.dp
    ) {
        Text(text = time.toString(), style = MaterialTheme.typography.displayLarge)
    }

}

@Composable
fun PomodoroTimer(dataTime: DataTime){
    Row {
        TimerBlock(time = dataTime.minutes)
        Text(text = ":", style = MaterialTheme.typography.displayLarge, modifier = Modifier.padding(7.dp).padding(start = 7.8.dp))
        TimerBlock(time = dataTime.seconds)
    }
}

