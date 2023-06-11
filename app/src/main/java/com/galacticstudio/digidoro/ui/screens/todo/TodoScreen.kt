package com.galacticstudio.digidoro.ui.screens.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.data.TodoModel
import com.galacticstudio.digidoro.data.todoList
import com.galacticstudio.digidoro.navigation.AppScaffold
import com.galacticstudio.digidoro.ui.shared.cards.todoItems.TodoItem
import com.galacticstudio.digidoro.ui.shared.cards.todoItems.TodoMessageData
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DateFormatData(
    val day: String,
    val date: String
)

@Preview(showSystemUi = true)
@Composable
fun TodoScreenPreview(){
    DigidoroTheme {
        val navController = rememberNavController()
        TodoScreen(navController = navController)
    }
}
/**
 * A composable function representing Todo Screen.
*/
@Composable
fun TodoScreen(
    navController: NavHostController = rememberNavController()
){
    AppScaffold(
        navController = navController,
        content = {
            TodoContent()
        }
    )
}

/**
 * A composable function representing Todo Screen content.
 *
 * @param modifier styles will be applied.
 */
@Composable
fun TodoContent(modifier: Modifier = Modifier){
    //const
    val dateFormatter = SimpleDateFormat("EEE. MMMM dd", Locale.getDefault())
    val currentDate = dateFormatter.format(Date())

    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Title(
            message = CustomMessageData("Tus task", "Comienza tu día paso a paso"),
            alignment = Alignment.CenterHorizontally
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = currentDate,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier
            .height(2.dp)
            .fillMaxWidth()
            .padding(end = 16.dp)
            .background(MaterialTheme.colorScheme.secondary))

        LazyColumn(
            modifier = Modifier.heightIn(100.dp, 270.dp),
            contentPadding = PaddingValues(16.dp),
            state = rememberLazyListState()
        ){

            itemsIndexed(todayFilter(todoList)){ _, item ->
                val date = dayFormat(item.createdAt!!)

                TodoItem(
                    message = TodoMessageData(
                        mainMessage = item.title,
                        messageBold = date.day,
                        messageNoBold = date.date
                    ),
                    colorTheme = Color(android.graphics.Color.parseColor(item.theme))
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Title(
            message = CustomMessageData("Falta completar", "No olvides tus actividades"),
            alignment = Alignment.CenterHorizontally
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.heightIn(100.dp, 270.dp),
            contentPadding = PaddingValues(16.dp),
            state = rememberLazyListState()
        ){
            itemsIndexed(noTodayFilter(todoList)){ _, item ->
                val date = dayFormat(item.createdAt!!)

                TodoItem(
                    message = TodoMessageData(
                        mainMessage = item.title,
                        messageBold = date.day,
                        messageNoBold = date.date
                    ),
                    colorTheme = Color(android.graphics.Color.parseColor(item.theme))
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Title(
            message = CustomMessageData("¡Lo lograste!", "Mira tus tareas completadas"),
            alignment = Alignment.CenterHorizontally
        )
        LazyColumn(
            modifier = Modifier.heightIn(100.dp, 270.dp),
            contentPadding = PaddingValues(16.dp),
            state = rememberLazyListState()
        ){
            itemsIndexed(doneFilter(todoList)){ _, item ->
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
}

private fun dayFormat(date: Date): DateFormatData{
    val day = SimpleDateFormat("EEE", Locale.getDefault()).format(date)
    val completeDate = SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(date)

    return DateFormatData(day, completeDate)
}

//display today todos
private fun todayFilter(list: MutableList<TodoModel>): MutableList<TodoModel>{
    val today = Calendar.getInstance().time
    return list.filter { item -> item.createdAt!!.date == today.date }.toMutableList()
}

//display notToday todos
private fun noTodayFilter(list: MutableList<TodoModel>): MutableList<TodoModel>{
    val today = Calendar.getInstance().time
    return list.filter { item -> item.createdAt!!.date != today.date }.toMutableList()
}

//display todos marked as complete
private fun doneFilter(list: MutableList<TodoModel>): MutableList<TodoModel>{
    return list.filter { item -> item.state == true }.toMutableList()
}