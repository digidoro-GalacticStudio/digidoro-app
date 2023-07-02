package com.galacticstudio.digidoro.ui.shared.floatingCards.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.ui.screens.todo.item.ItemTodoEvent
import com.galacticstudio.digidoro.ui.screens.todo.item.ItemTodoViewModel
import com.galacticstudio.digidoro.ui.screens.todo.list.TodosEvent
import com.galacticstudio.digidoro.ui.screens.todo.list.viewmodel.TodoViewModel
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.ButtonControl
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.ColorBox
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.GrayInput
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.TextFieldType
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.TitleCard
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.util.ColorCustomUtils
import com.galacticstudio.digidoro.util.shadowWithCorner
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Preview()
@Composable
fun PreviewTodoUpdateFloatingBox() {
    DigidoroTheme() {
        TodoCreateFloatingBox(FloatingTodoHideHandler = fun() {})
    }
}


/**
 * This composable function represent a floating box to create new todo elements
 *  @param modifier is a dinamic modifier
 *  @param FloatingTodoHideHandler  handles on cancel method
 */

@Composable
fun TodoUpdateFloatingBox(
    modifier: Modifier = Modifier,
    itemViewModel: ItemTodoViewModel = viewModel(factory = ItemTodoViewModel.Factory),
    todosViewModel: TodoViewModel = viewModel(factory = TodoViewModel.Factory),
    FloatingTodoHideHandler: () -> Unit,
) {
    val app: RetrofitApplication = LocalContext.current.applicationContext as RetrofitApplication
    val cornerRadius = 5.dp
    val day =
        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Calendar.getInstance().time)
    Box(
        modifier = modifier
            .wrapContentWidth()
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
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
            Column(Modifier.wrapContentWidth()) {
                TitleCard(
                    placeHolder = "Nombra tu task",
                    value = itemViewModel.state.value.title
                ) {
                    itemViewModel.onEvent(ItemTodoEvent.titleChanged(it))
                }
                GrayInput(
                    label = "Fecha",
                    placeHolder = day,
                    fieldWidth = 100.dp,
                    type = TextFieldType.DATE,
                    modifier = Modifier.selectable(true, false, null, fun() {})
                )
                Column() {
                    Text(
                        text = "Color",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    ColorBox(
                        selectedColor = Color(android.graphics.Color.parseColor("#${itemViewModel.state.value.theme}")),
                        onColorChange = {
                            itemViewModel.onEvent(
                                ItemTodoEvent.themeChanged(ColorCustomUtils.convertColorToString(it))
                            )
                        },
                        isUserPremium = app.getRoles().contains("premium")
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                TodoUpdateControler(
                    FloatingTodoHideHandler = FloatingTodoHideHandler,
                    UpdateTodoHandler = {
                        itemViewModel.onEvent(ItemTodoEvent.UpdateTodo)
                    },
                    DeleteTodoHandler = { itemViewModel.onEvent(ItemTodoEvent.RemoveTodo) },
                    OnExitHandler = { itemViewModel.onExit() },
                    ForceRebuildHandler = { todosViewModel.onEvent(TodosEvent.Rebuild) }
                )
            }
        }
    }
}

/**
 * Composable function that control save and cancel buttons of todo floating card
 * @param FloatingTodoHideHandler handles on cancel method
 */
@Composable
fun TodoUpdateControler(
    DeleteTodoHandler: () -> Unit,
    UpdateTodoHandler: () -> Unit,
    FloatingTodoHideHandler: () -> Unit,
    OnExitHandler: () -> Unit,
    ForceRebuildHandler: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        ButtonControl(
            text = "Cancelar",
            contentColor = MaterialTheme.colorScheme.secondary,
            backgroundColor = Color.Transparent,
            borderColor = Color.Transparent,
            onClick = { FloatingTodoHideHandler() }
        )

        ButtonControl(
            text = "Eliminar",
            contentColor = MaterialTheme.colorScheme.secondary,
            backgroundColor = Color.Transparent,
            borderColor = Color.Transparent,
            onClick = {
                DeleteTodoHandler()
                OnExitHandler()
                FloatingTodoHideHandler()
                ForceRebuildHandler()
            }
        )

        ButtonControl(
            text = "Actualizar",
            contentColor = MaterialTheme.colorScheme.secondary,
            backgroundColor = colorResource(id = R.color.gray_text_color),
            borderColor = MaterialTheme.colorScheme.secondary,
            onClick = {
                UpdateTodoHandler()
                OnExitHandler()
                FloatingTodoHideHandler()
                ForceRebuildHandler()
            }
        )
    }
}