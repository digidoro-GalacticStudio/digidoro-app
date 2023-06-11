package com.galacticstudio.digidoro.ui.shared.titles


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.galacticstudio.digidoro.ui.theme.Nunito

data class CustomMessageData(val title: String, val subTitle: String)

/**
 * A composable function that displays a title and subtitle.
 *
 * @param message The data containing the title and subtitle.
 * @param alignment The horizontal alignment of the column.
 * @param modifier The modifier to be applied to the column of the Title item.
 */
@Composable
fun Title(
    message: CustomMessageData,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    subtitleStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Column(
        horizontalAlignment = alignment,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
    ) {
        TextElement(
            text = message.title,
            textStyle = titleStyle,
            fontWeight = FontWeight.W800,
        )
        TextElement(
            text = message.subTitle,
            textStyle = subtitleStyle
        )
    }
}

/**
 * A composable function that displays a text element.
 *
 * @param text The text to be displayed.
 * @param textStyle The style to be applied to the text.
 * @param fontWeight The font weight to be applied to the text.
 */
@Composable
fun TextElement(
    text: String, textStyle: TextStyle, fontWeight: FontWeight = FontWeight.W500
) {
    Text(
        text = text,
        style = textStyle,
        fontFamily = Nunito,
        fontWeight = fontWeight,
        color = Color(0xFF202124)
    )
}