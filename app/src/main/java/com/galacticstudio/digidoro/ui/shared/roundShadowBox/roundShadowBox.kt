package com.galacticstudio.digidoro.ui.shared.roundShadowBox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.ui.shared.solidShadow.SolidShadow

@Composable
fun RoundShadowBox(
    shadowColor: Color,
    borderRadius: Dp = 18.dp,
    XYOffset:Dp = 5.dp,
    content: @Composable() () -> Unit
){
    Box(
        modifier = Modifier
            .padding(3.dp)

    ){
        SolidShadow(
            color = shadowColor,
            modifier = Modifier.matchParentSize(),
            borderRadius = borderRadius,
            XYOffset = XYOffset
            )
        Box(
            modifier = Modifier
                .border(1.85.dp , shadowColor, RoundedCornerShape(borderRadius))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(borderRadius))
                .padding(horizontal = 10.dp, vertical = 6.dp)

        ){
            content()
        }

    }
}