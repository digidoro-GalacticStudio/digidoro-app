package com.galacticstudio.digidoro.ui.screens.todo.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.data.TodoModel
import com.galacticstudio.digidoro.ui.screens.todo.DateFormatData
import com.galacticstudio.digidoro.ui.shared.cards.todoItems.TodoItem
import com.galacticstudio.digidoro.ui.shared.cards.todoItems.TodoMessageData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * A composable function representing Todo Items Displayer component.
 * @param todoList its a casted list of todos data
 */
@Composable
fun DisplayTodo(todoList: MutableList<TodoModel>){
    LazyColumn(
        modifier = Modifier.heightIn(100.dp, 270.dp),
        contentPadding = PaddingValues(16.dp),
        state = rememberLazyListState()
    ){
        itemsIndexed(todoList){ _, item ->
            val date = dayFormat(item.createdAt!!)

            TodoItem(
                message = TodoMessageData(
                    mainMessage = item.title,
                    messageBold = date.day,
                    messageNoBold = date.date
                ),
                colorTheme = Color(android.graphics.Color.parseColor(item.theme)),
                done = item.state!!
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

//Day formatter
private fun dayFormat(date: Date): DateFormatData {
    val day = SimpleDateFormat("EEE", Locale.getDefault()).format(date)
    val completeDate = SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(date)

    return DateFormatData(day, completeDate)
}
