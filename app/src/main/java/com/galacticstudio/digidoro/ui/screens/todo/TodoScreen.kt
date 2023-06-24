package com.galacticstudio.digidoro.ui.screens.todo

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.TodoModel
import com.galacticstudio.digidoro.data.todoList
import com.galacticstudio.digidoro.ui.screens.todo.components.DisplayTodo
import com.galacticstudio.digidoro.ui.screens.todo.components.TodosResponseState
import com.galacticstudio.digidoro.ui.shared.floatingCards.todo.TodoFloatingBox
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
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    todoViewModel: TodoViewModel = viewModel(factory = TodoViewModel.Factory),
){
    val state = todoViewModel.state.value
    val context = LocalContext.current

    LaunchedEffect(Unit){
        todoViewModel.onEvent(TodosEvent.Rebuild)
    }

    LaunchedEffect(key1 = context){
        todoViewModel.responseEvent.collect{
            event ->
            when(event){
                is TodosResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is TodosResponseState.ErrorWithMessage ->{
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is TodosResponseState.Success ->{
                    Toast.makeText(
                        context,
                        "Todos los todos me la pelan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
    }

//    variables and functions to use
    var isFloatingTodoVisible by remember {
        mutableStateOf(false)
    }
    val density = LocalDensity.current

    fun FloatingTodoVisibleHandler (){ isFloatingTodoVisible = true}
    fun FloatingTodoHideHandler (){ isFloatingTodoVisible = false}

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {

            //floating elements
            FloatingActionButton(
                onClick = { FloatingTodoVisibleHandler() },
                containerColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .clip(CircleShape)
                    .width(68.dp)
                    .height(68.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_icon),
                    contentDescription = "Create new todo",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
    ){
        contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            //static element
            //checking loading
            val isLoading = todoViewModel.state.value.isLoading
            if(isLoading)
                TodoStaticBody(
                    todoList = todoList.toList()
                )
            else 
                TodoStaticBody(todoList = todoViewModel.state.value.todos)

            //floating no scrollable element
            if(isFloatingTodoVisible)
                Box(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                )

            //animated floating card
           AnimatedVisibility(
               visible = isFloatingTodoVisible,
               enter = slideInVertically {
                   // Slide in from 40 dp from the top.
                   with(density) { -40.dp.roundToPx() }
               } + expandVertically(
                   // Expand from the top.
                   expandFrom = Alignment.Top
               ) + fadeIn(
                   // Fade in with the initial alpha of 0.3f.
                   initialAlpha = 0.3f
               ),
               exit = slideOutVertically() + shrinkVertically() + fadeOut()
           ) {
               TodoFloatingBox(
                   modifier = Modifier
                       .offset(x = 0.dp, y = 90.dp),
                   FloatingTodoHideHandler = { FloatingTodoHideHandler() }
               )
           }
        }
    }
}

@Composable
fun TodoStaticBody(
    modifier: Modifier = Modifier,
    todoList: List<TodoModel>
    ){
    //const
    val dateFormatter = SimpleDateFormat("EEE. MMMM dd", Locale.getDefault())
    val currentDate = dateFormatter.format(Date())

    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 75.dp)
    ) {

        //static elements
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

        DisplayTodo(todoList = todayFilter(todoList))

        Spacer(modifier = Modifier.height(16.dp))
        Title(
            message = CustomMessageData("Falta completar", "No olvides tus actividades"),
            alignment = Alignment.CenterHorizontally
        )
        Spacer(modifier = Modifier.height(16.dp))

        DisplayTodo(todoList = noTodayFilter(todoList))

        Spacer(modifier = Modifier.height(24.dp))
        Title(
            message = CustomMessageData("¡Lo lograste!", "Mira tus tareas completadas"),
            alignment = Alignment.CenterHorizontally
        )
        DisplayTodo(todoList = doneFilter(todoList))
    }
}

//display today todos
private fun todayFilter(list: List<TodoModel>): MutableList<TodoModel>{
    val today = Calendar.getInstance().time
    return list.filter { item -> item.createdAt!!.date == today.date && item.state == false}.toMutableList()
}

//display notToday todos
private fun noTodayFilter(list: List<TodoModel>): MutableList<TodoModel>{
    val today = Calendar.getInstance().time
    return list.filter { item -> item.createdAt!!.date != today.date && item.state == false}.toMutableList()
}

//display todos marked as complete
private fun doneFilter(list: List<TodoModel>): MutableList<TodoModel>{
    return list.filter { item -> item.state == true }.toMutableList()
}