package com.galacticstudio.digidoro.ui.shared.roundShadowBox

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.shared.solidShadow.SolidShadow

@Composable
fun RoundShadowBox(content: @Composable ()-> Unit ){
    Box(){
        SolidShadow(color = colorResource(id = R.color.secondary_color), modifier = Modifier.matchParentSize())
        content()

    }
}