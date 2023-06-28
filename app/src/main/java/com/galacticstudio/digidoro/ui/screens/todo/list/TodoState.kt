package com.galacticstudio.digidoro.ui.screens.todo.list

import com.galacticstudio.digidoro.data.TodoModel
import com.galacticstudio.digidoro.ui.screens.todo.list.viewmodel.OrderType
import com.galacticstudio.digidoro.ui.screens.todo.list.viewmodel.TodoViewModel

data class TodoState(
    var todos: List<TodoModel> = emptyList(),
    var todayTodos: List<TodoModel> = emptyList(),
    var notTodayTodos: List<TodoModel> = emptyList(),
    var doneTodos: List<TodoModel> = emptyList(),
    val todosOrder: TodoViewModel.TodoOrder = TodoViewModel.TodoOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val isLoading: Boolean = false
)
