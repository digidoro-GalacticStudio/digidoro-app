package com.galacticstudio.digidoro.ui.shared.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.Nunito

/**
 * A composable function that represents a custom button.
 *
 * @param text The text to be displayed on the button.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit
) {
    val shadowColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val buttonBorder = if (isSystemInDarkTheme()) {
        BorderStroke(1.dp, Color.White)
    } else {
        BorderStroke(1.dp, Color.Black)
    }

    Button(
        onClick = onClick,
        border = buttonBorder,
        shape = CircleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.secondary_color),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(8.dp),
//        elevation = ButtonDefaults.buttonElevation(
//            defaultElevation = 5.dp,
//            pressedElevation = 5.dp,
//            focusedElevation = 5.dp,
//            hoveredElevation = 5.dp,
//            disabledElevation = 0.dp
//        ),
        content = {
            Text(
                text = text,
                fontSize = 21.8596.sp,
                fontFamily = Nunito,
                fontWeight = FontWeight.W800,
                lineHeight = 25.sp
            )
        }
    )
}
