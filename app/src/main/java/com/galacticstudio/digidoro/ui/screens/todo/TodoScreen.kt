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
import androidx.compose.ui.platform.LocalConfiguration
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
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.ui.screens.todo.list.TodosEvent
import com.galacticstudio.digidoro.ui.screens.todo.components.DisplayTodo
import com.galacticstudio.digidoro.ui.screens.todo.components.TodosResponseState
import com.galacticstudio.digidoro.ui.screens.todo.item.ItemTodoResponseState
import com.galacticstudio.digidoro.ui.screens.todo.item.ItemTodoViewModel
import com.galacticstudio.digidoro.ui.screens.todo.list.viewmodel.TodoViewModel
import com.galacticstudio.digidoro.ui.shared.floatingCards.todo.TodoCreateFloatingBox
import com.galacticstudio.digidoro.ui.shared.floatingCards.todo.TodoUpdateFloatingBox
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.util.WindowSize
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
fun TodoScreenPreview() {
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
    itemTodoViewModel: ItemTodoViewModel = viewModel(factory = ItemTodoViewModel.Factory)
) {
    val state = todoViewModel.state.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        todoViewModel.onEvent(TodosEvent.Rebuild)
    }

    LaunchedEffect(key1 = context) {
        todoViewModel.responseEvent.collect { event ->
            when (event) {
                is TodosResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is TodosResponseState.ErrorWithMessage -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is TodosResponseState.Success -> {
                    Toast.makeText(
                        context,
                        "Todos retrieved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(key1 = context){
        itemTodoViewModel.responseEvents.collect{
                event ->
            when(event){

                is ItemTodoResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ItemTodoResponseState.ErrorWithMessage -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ItemTodoResponseState.Success ->{
                    todoViewModel.onEvent(TodosEvent.Rebuild)
                }
                else -> {}
            }
        }
    }

//    variables and functions to use
    var isCreateTodoFloatingVisible by remember {
        mutableStateOf(false)
    }
    var isUpdateTodoFloatingVisible by remember {
        mutableStateOf(false)
    }

    fun CreateTodoFloatingVisibleHandler() {
        isCreateTodoFloatingVisible = true
    }

    fun CreateTodoFloatingHideHandler() {
        isCreateTodoFloatingVisible = false
    }

    fun UpdateTodoFloatingVisibleHandler() {
        isUpdateTodoFloatingVisible = true
    }

    fun UpdateTodoFloatingHideHandler() {
        isUpdateTodoFloatingVisible = false
    }

    val screenSize = LocalConfiguration.current.screenWidthDp.dp

    val topPadding = if (screenSize < WindowSize.COMPACT) 80.dp else 5.dp
    val buttonPosition =
        if (screenSize < WindowSize.COMPACT) FabPosition.Center else FabPosition.End
    Scaffold(
        floatingActionButtonPosition = buttonPosition,
        floatingActionButton = {

            //floating elements
            FloatingActionButton(
                onClick = { CreateTodoFloatingVisibleHandler() },
                containerColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(bottom = topPadding)
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
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            //static element
            TodoStaticBody(
                todoViewModel = todoViewModel,
                itemTodoViewModel = itemTodoViewModel
            ) {
                UpdateTodoFloatingVisibleHandler()
            }

            //floating no scrollable element
            if (isCreateTodoFloatingVisible)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )


            AnimatedBox(visibility = isCreateTodoFloatingVisible) {
                TodoCreateFloatingBox(
                    modifier = Modifier
                        .offset(x = 0.dp, y = 90.dp),
                    itemViewModel = itemTodoViewModel,
                    todosViewModel = todoViewModel
                ) {
                    CreateTodoFloatingHideHandler()
                    itemTodoViewModel.onExit()
                }
            }

            AnimatedBox(visibility = isUpdateTodoFloatingVisible) {
                TodoUpdateFloatingBox(
                    modifier = Modifier
                        .offset(x = 0.dp, y = 90.dp),
                    itemViewModel = itemTodoViewModel
                ) {
                    UpdateTodoFloatingHideHandler()
                    itemTodoViewModel.onExit()
                }
            }
        }
    }
}

@Composable
fun AnimatedBox(
    visibility: Boolean,
    content: @Composable() () -> Unit,
) {
    val density = LocalDensity.current

    //animated floating card
    AnimatedVisibility(
        visible = visibility,
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
        content()
    }
}

@Composable
fun TodoStaticBody(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel,
    itemTodoViewModel: ItemTodoViewModel,
    onClick: () -> Unit
) {
    //const
    val dateFormatter = SimpleDateFormat("EEE. MMMM dd", Locale.getDefault())
    val currentDate = dateFormatter.format(Date())

    val emptyTodo: TodoModel = TodoModel(
        id = "",
        title = "",
        description = "",
        theme = "#FFFFFF",
        createdAt = Calendar.getInstance().time,
        state = false,
        reminder = Calendar.getInstance().time
    )


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
        Spacer(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .padding(end = 16.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )

        val isLoading = todoViewModel.state.value.isLoading

        if (isLoading && todoViewModel.state.value.todayTodos.isEmpty()) {
            DisplayTodo(
                todoList = listOf(emptyTodo),
                itemViewModel = itemTodoViewModel,
                todoViewModel = todoViewModel
            ) {}
        } else {
            DisplayTodo(
                todoList = todoViewModel.state.value.todayTodos,
                itemViewModel = itemTodoViewModel,
                todoViewModel = todoViewModel
            ) {
                onClick()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Title(
            message = CustomMessageData("Falta completar", "No olvides tus actividades"),
            alignment = Alignment.CenterHorizontally
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading && todoViewModel.state.value.notTodayTodos.isEmpty()) {
            DisplayTodo(
                todoList = listOf(emptyTodo),
                itemViewModel = itemTodoViewModel,
                todoViewModel = todoViewModel
            ) {}
        } else {
            DisplayTodo(
                todoList = todoViewModel.state.value.notTodayTodos,
                itemViewModel = itemTodoViewModel,
                todoViewModel = todoViewModel
            ) {
                onClick()
            }
        }


        Spacer(modifier = Modifier.height(24.dp))
        Title(
            message = CustomMessageData("¡Lo lograste!", "Mira tus tareas completadas"),
            alignment = Alignment.CenterHorizontally
        )

        if (isLoading && todoViewModel.state.value.doneTodos.isEmpty()) {
            DisplayTodo(
                todoList = listOf(emptyTodo),
                itemViewModel = itemTodoViewModel,
                todoViewModel = todoViewModel
            ) {}
        } else {
            DisplayTodo(
                todoList = todoViewModel.state.value.doneTodos,
                itemViewModel = itemTodoViewModel,
                todoViewModel = todoViewModel
            ) {
                onClick()
            }
        }

    }
}

