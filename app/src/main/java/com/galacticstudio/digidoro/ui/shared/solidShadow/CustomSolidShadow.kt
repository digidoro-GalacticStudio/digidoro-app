package com.galacticstudio.digidoro.ui.shared.solidShadow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.shared.roundShadowBox.RoundShadowBox

/*
all ways applied Modifier.matchParentSize()
*Usage:
* Box{
*   Shadow(addborderradius, color*, xyoffset, modifier*)
*   element which shadow will be applied to
* }
* */

@Composable
fun SolidShadow(
    borderRadius: Dp = 18.dp,
    color: Color,
    XYOffset:Dp = 5.dp,
    modifier: Modifier
){
    Box(
        modifier
            .offset(XYOffset, XYOffset)
            .background(color, shape = RoundedCornerShape(borderRadius))
    )
}