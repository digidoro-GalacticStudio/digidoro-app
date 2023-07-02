package com.galacticstudio.digidoro.ui.shared.floatingCards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
                .fillMaxWidth(),
            onDismissRequest = onDismissRequest,
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            content(scope)
        }
    }
}