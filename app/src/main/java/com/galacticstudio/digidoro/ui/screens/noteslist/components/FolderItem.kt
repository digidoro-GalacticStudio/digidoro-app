package com.galacticstudio.digidoro.ui.screens.noteslist.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme

@Preview(name = "Full Preview", showSystemUi = true)
//@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FolderPreview() {
    val navController = rememberNavController()

    DigidoroTheme {
        Column() {
            FolderItem()
            Text("Hello")
        }
    }
}

@Composable
fun FolderItem() {
    val cornerRadius: Dp = 10.dp
    val cutCornerSize: Dp = 30.dp
    val spaceWidth: Dp = 33.dp
    val spaceHeight: Dp = 6.dp

    Box(
        modifier = Modifier.size(100.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            val path = Path()

            path.moveTo(size.width - spaceWidth.toPx(), 0f)
            path.lineTo(size.width - 15.dp.toPx(), spaceHeight.toPx())
            path.lineTo(size.width - spaceWidth.toPx(), spaceHeight.toPx())
            path.close()

            drawPath(path, color = Color.Blue)

            clipPath(clipPath) {


                drawRoundRect(
                    color = Color.Red,
                    size = Size(size.width - spaceWidth.toPx(), size.height),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color.Green,
                    topLeft = Offset(0f, spaceHeight.toPx()),
                    size = Size(size.width, size.height - spaceHeight.toPx()),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
    }
}
