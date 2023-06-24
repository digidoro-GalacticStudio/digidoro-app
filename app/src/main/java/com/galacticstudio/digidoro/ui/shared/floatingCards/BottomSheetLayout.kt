package com.galacticstudio.digidoro.ui.shared.floatingCards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

/**
 * A composable function representing a bottom sheet layout.
 *
 * @param openBottomSheet Whether the bottom sheet is open or not.
 * @param onDismissRequest The callback for dismissing the bottom sheet.
 * @param content The content of the bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetLayout(
    openBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable (scope: CoroutineScope) -> Unit = {}
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onDismissRequest = onDismissRequest,
            sheetState = bottomSheetState,
            containerColor = Color.White,
        ) {
            content(scope)
        }
    }
}