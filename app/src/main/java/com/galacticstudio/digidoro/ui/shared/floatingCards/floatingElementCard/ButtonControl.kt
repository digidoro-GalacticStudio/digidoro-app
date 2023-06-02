package com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

val cornerRadius = 12.dp

@Composable
fun ButtonControl(
    text: String,
    contentColor: Color,
    backgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            containerColor = backgroundColor
        ),
        modifier = Modifier
            .border(2.dp, borderColor, RoundedCornerShape(cornerRadius))
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
            .height(40.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
        )
    }
}