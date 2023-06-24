package com.galacticstudio.digidoro.ui.screens.todo

import com.galacticstudio.digidoro.data.TodoModel

data class TodoState(
    val todos: List<TodoModel> = emptyList(),
    val todosOrder: TodoOrder = TodoOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val isLoading: Boolean = false
)
