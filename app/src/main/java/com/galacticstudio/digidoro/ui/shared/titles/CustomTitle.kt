package com.galacticstudio.digidoro.ui.shared.titles

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

data class CustomMessageData(val Title: String, val subTitle: String)

@Composable
fun Title(Message: CustomMessageData, textAlign: TextAlign, alignment: Alignment.Horizontal){
    Column(
        horizontalAlignment = alignment
    ) {
        TextElement(
            text = Message.Title,
            textAlign = textAlign,
            textStyle = MaterialTheme.typography.displayMedium,


            )
        TextElement(
            text = Message.subTitle,
            textAlign = textAlign,
            textStyle = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun TextElement(text:String, textAlign: TextAlign, textStyle: TextStyle
){
    Text(
        text = text,
        textAlign = textAlign,
        style = textStyle)
}