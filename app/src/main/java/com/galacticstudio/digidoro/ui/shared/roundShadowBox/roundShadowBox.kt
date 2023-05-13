package com.galacticstudio.digidoro.ui.shared.roundShadowBox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R

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
@Composable
fun RoundShadowBox(
    shadowColor: Color,
    borderRadius: Dp = 18.dp,
    XYOffset:Dp = 5.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
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
                .border(1.85.dp, colorResource(id = R.color.secondary_color), RoundedCornerShape(borderRadius))
                .background(Color.White, RoundedCornerShape(borderRadius))
                .then(modifier)
        ){
            content()
        }

    }
}