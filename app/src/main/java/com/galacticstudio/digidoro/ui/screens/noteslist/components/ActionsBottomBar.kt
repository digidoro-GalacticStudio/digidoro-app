package com.galacticstudio.digidoro.ui.screens.noteslist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.Gray30
import com.galacticstudio.digidoro.util.dropShadow

sealed class ActionsMenu(
    val icon: Int,
    val title: String,
) {
    object FolderItem : ActionsMenu(
        R.drawable.folder_icon,
        "move to folder",
    )

    object RemoveItem : ActionsMenu(
        R.drawable.delete_icon,
        "remove",
    )

    object DuplicateItem : ActionsMenu(
        R.drawable.add_icon,
        "duplicate note",
    )
}

@Composable
fun ActionsBottomBar(
    bottomBarState: MutableState<Boolean>,
    onRemoveClick: () -> Unit,
    onDuplicateClick: () -> Unit,
    onMoveFolderClick: () -> Unit
) {
    val actionsItems = listOf(
        ActionsMenu.FolderItem,
        ActionsMenu.RemoveItem,
        ActionsMenu.DuplicateItem,
    )

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        Surface(
            modifier = Modifier
                .padding(bottom = 90.dp, start = 8.dp, end = 8.dp)
                .zIndex(2f)
                .clip(RoundedCornerShape(16.dp))
                .dropShadow(
                    color = MaterialTheme.colorScheme.onPrimary.copy(0.25f),
                    blurRadius = 14.dp,
                    spread = 5.dp,
                    offsetY = (-15).dp,
                ),
        ) {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                actionsItems.forEach { item ->
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            when (item) {
                                is ActionsMenu.FolderItem -> {
                                    onMoveFolderClick()
                                }

                                is ActionsMenu.DuplicateItem -> {
                                    onDuplicateClick()
                                }

                                is ActionsMenu.RemoveItem -> {
                                    onRemoveClick()
                                }
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(23.dp)
                            )
                        },
                        label = { Text(item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Gray30,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    )
                }
            }
        }
    }
}
