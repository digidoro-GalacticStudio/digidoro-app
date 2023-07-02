package com.galacticstudio.digidoro.ui.screens.pomodoro.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.ui.screens.pomodoro.PomodoroUIEvent
import com.galacticstudio.digidoro.ui.screens.pomodoro.viewmodel.PomodoroViewModel
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.ButtonControl
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.ColorBox
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.TitleCard
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldItem
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.theme.Nunito
import com.galacticstudio.digidoro.util.ColorCustomUtils
import com.galacticstudio.digidoro.util.shadowWithCorner

@Composable
fun PomodoroDialog(
    pomodoroViewModel: PomodoroViewModel,
    onDelete: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val cornerRadius = 5.dp
    val app: Application = LocalContext.current.applicationContext as Application

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .shadowWithCorner(
                    cornerRadius = 5.dp,
                    shadowOffset = Offset(16.0f, 16.0f),
                    shadowColor = MaterialTheme.colorScheme.secondary
                )
                .border(
                    width = (1).dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp)
                .wrapContentWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                TitleCard(
                    placeHolder = "Name your Pomo.",
                    value = pomodoroViewModel.state.value.name
                ) {
                    pomodoroViewModel.onEvent(PomodoroUIEvent.NameChanged(it))
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Number of sessions",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.W800,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.widthIn(0.dp, 120.dp)) {
                    TextFieldItem(
                        value = pomodoroViewModel.state.value.totalSessions.toString(),
                        placeholder = "#",
                        type = TextFieldType.NUMBER,
                        leadingIcon = null,
                        isError = false,
                    ) {
                        val numberString = it
                        val totalSessions = if (numberString.isEmpty()) {
                            0
                        } else {
                            numberString.toIntOrNull() ?: 0
                        }
                        pomodoroViewModel.onEvent(PomodoroUIEvent.TotalSessionsChanged(totalSessions))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
//                ErrorMessage(state.passwordError)

                Text(
                    text = "Color",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.W800,
                )
                Spacer(modifier = Modifier.height(16.dp))
                ColorBox(
                    selectedColor = Color(android.graphics.Color.parseColor("#${pomodoroViewModel.pomodoroColor.value}")),
                    spacedBy = 0.dp,
                    isUserPremium = app.getRoles().contains("premium")
                ) {
                    pomodoroViewModel.onEvent(
                        PomodoroUIEvent.ThemeChanged(ColorCustomUtils.convertColorToString(it))
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonControl(
                        text = "Cancel",
                        contentColor = MaterialTheme.colorScheme.secondary,
                        backgroundColor = Color.Transparent,
                        borderColor = Color.Transparent,
                    ) {
                        onDismissRequest()
                    }
                    if (pomodoroViewModel.editedMode.value) {
                        ButtonControl(
                            text = "Delete",
                            contentColor = MaterialTheme.colorScheme.secondary,
                            backgroundColor = MaterialTheme.colorScheme.secondary.copy(0.07f),
                            borderColor = MaterialTheme.colorScheme.secondary,
                        ) {
                            onDelete(pomodoroViewModel.state.value.pomodoroId)
                            pomodoroViewModel.onEvent(PomodoroUIEvent.DeletePomodoro)
                            onDismissRequest()
                        }
                        ButtonControl(
                            text = "Update",
                            contentColor = MaterialTheme.colorScheme.secondary,
                            backgroundColor = MaterialTheme.colorScheme.secondary.copy(0.07f),
                            borderColor = MaterialTheme.colorScheme.secondary,
                        ) {
                            pomodoroViewModel.onEvent(PomodoroUIEvent.EditPomodoro)
                            pomodoroViewModel.onEvent(PomodoroUIEvent.ClearData)
                            onDismissRequest()
                        }
                    } else {
                        ButtonControl(
                            text = "Save",
                            contentColor = MaterialTheme.colorScheme.secondary,
                            backgroundColor = MaterialTheme.colorScheme.secondary.copy(0.07f),
                            borderColor = MaterialTheme.colorScheme.secondary,
                        ) {
                            pomodoroViewModel.onEvent(PomodoroUIEvent.SavePomodoro)
                            pomodoroViewModel.onEvent(PomodoroUIEvent.ClearData)
                            onDismissRequest()
                        }
                    }
                }
            }
        }
    }
}