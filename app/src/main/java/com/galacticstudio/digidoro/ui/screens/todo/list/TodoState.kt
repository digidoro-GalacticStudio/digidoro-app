package com.galacticstudio.digidoro.ui.screens.todo.list

import com.galacticstudio.digidoro.data.TodoModel
import com.galacticstudio.digidoro.ui.screens.todo.list.viewmodel.OrderType
import com.galacticstudio.digidoro.ui.screens.todo.list.viewmodel.TodoViewModel

data class TodoState(
    val todos: List<TodoModel> = emptyList(),
    val todayTodos: List<TodoModel> = emptyList(),
    val notTodayTodos: List<TodoModel> = emptyList(),
    val doneTodos: List<TodoModel> = emptyList(),
    val todosOrder: TodoViewModel.TodoOrder = TodoViewModel.TodoOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val isLoading: Boolean = false
)
