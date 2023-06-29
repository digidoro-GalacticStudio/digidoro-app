package com.galacticstudio.digidoro.ui.screens.noteslist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
        "Move to folder",
    )

    object RemoveItem : ActionsMenu(
        R.drawable.delete_icon,
        "Remove",
    )

    object DuplicateItem : ActionsMenu(
        R.drawable.add_icon,
        "Duplicate note",
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
        modifier = Modifier.zIndex(Float.MAX_VALUE),
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        Column(
            modifier=Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){


        Surface(
            modifier = Modifier
                .zIndex(Float.MAX_VALUE)
                .padding(bottom = 0.dp, start = 9.dp, end = 9.dp)
                .height(80.dp)
                .widthIn(0.dp, 400.dp)
                .offset(y = (-10).dp)
                .dropShadow(
                    color = MaterialTheme.colorScheme.onPrimary.copy(0.4f),
                    blurRadius = 10.dp,
                )
                .clip(RoundedCornerShape(16.dp))
        ) {
            NavigationBar(
                modifier = Modifier
                    .zIndex(Float.MAX_VALUE)
                    .height(12.dp),
                containerColor = MaterialTheme.colorScheme.background,
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
}
