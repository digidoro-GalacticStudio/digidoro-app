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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.ButtonControl
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.ColorBox
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.TitleCard
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldItem
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.theme.Nunito
import com.galacticstudio.digidoro.util.shadowWithCorner

@Composable
fun PomodoroDialog(
    onDismissRequest: () -> Unit
) {
    val cornerRadius = 5.dp
    val title = remember { mutableStateOf("") }
    val number = remember { mutableStateOf("") }
    val app: RetrofitApplication = LocalContext.current.applicationContext as RetrofitApplication


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
                    value = title.value
                ) {
                    title.value = it
//                itemViewModel.onEvent(ItemTodoEvent.titleChanged(it))
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
                        value = number.value,
                        placeholder = "#",
                        type = TextFieldType.NUMBER,
                        leadingIcon = null,
                        isError = false,

                        ) {
//                    loginViewModel.onEvent(LoginFormEvent.PasswordChanged(it))
                        number.value = it
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
//                    selectedColor = Color(android.graphics.Color.parseColor("#${itemViewModel.state.value.theme}")),
                    selectedColor = Color.Red,
                    spacedBy = 0.dp,
                    isUserPremium = app.getRoles().contains("premium")
                ) {
//                        itemViewModel.onEvent(
//                            ItemTodoEvent.themeChanged(ColorCustomUtils.convertColorToString(it))
//                        )
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
                    ButtonControl(
                        text = "Save",
                        contentColor = MaterialTheme.colorScheme.secondary,
                        backgroundColor = MaterialTheme.colorScheme.secondary.copy(0.07f),
                        borderColor = MaterialTheme.colorScheme.secondary,
                    ) {
                        onDismissRequest()
                    }
                }
            }
        }
    }
}