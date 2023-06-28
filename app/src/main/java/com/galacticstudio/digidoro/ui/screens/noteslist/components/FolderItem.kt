package com.galacticstudio.digidoro.ui.screens.noteslist.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.data.FolderModel
import com.galacticstudio.digidoro.data.FolderPopulatedModel
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesEvent
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme

@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FolderPreview() {
    DigidoroTheme {
        Column(modifier = Modifier.padding(25.dp)) {
//            FolderItem( FolderModel ) { }
        }
    }
}

@Composable
fun FolderItem(
    folder: FolderPopulatedModel,
    colorBackgroundFolder: Color = Color.Green,
    onFolderClick: () -> Unit,
) {
    val cornerRadius: Dp = 10.dp
    val spaceWidth: Dp = 50.dp
    val spaceHeight: Dp = 8.dp

    Box(
        modifier = Modifier
            .padding(end = 8.dp, top = 6.dp, bottom = 6.dp)
            .clickable { onFolderClick() }
            .width(100.dp)
            .height(70.dp)
    ) {
        val colorFolder = MaterialTheme.colorScheme.onSecondaryContainer
        val spacer  = 2.5.dp

        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = colorBackgroundFolder,
                topLeft = Offset(0f, spacer.toPx()),
                size = Size(size.width - 6.dp.toPx(), size.height - spacer.toPx()),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )

            val path = Path()

            path.moveTo(size.width - spaceWidth.toPx() - cornerRadius.toPx() , 0f)
            path.lineTo(size.width - 45.dp.toPx(), spaceHeight.toPx())
            path.lineTo(size.width - spaceWidth.toPx()  - cornerRadius.toPx() , spaceHeight.toPx())
            path.close()

            drawPath(path, color = colorFolder)

            drawRoundRect(
                color = colorFolder,
                size = Size(size.width - spaceWidth.toPx(), size.height),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
            drawRoundRect(
                color = colorFolder,
                topLeft = Offset(0f, spaceHeight.toPx()),
                size = Size(size.width, size.height - spaceHeight.toPx()),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                folder.notesId.size.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(0.7f)
            )
            Text(
                folder.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.W700
            )
        }
    }
}
