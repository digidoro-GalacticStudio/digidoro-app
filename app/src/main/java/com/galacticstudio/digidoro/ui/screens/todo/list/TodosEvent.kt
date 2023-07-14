package com.galacticstudio.digidoro.ui.screens.todo.list

import com.galacticstudio.digidoro.ui.screens.todo.list.viewmodel.TodoViewModel

sealed class TodosEvent {

    data class Order(val todoOrder: TodoViewModel.TodoOrder): TodosEvent()

    data class LoadingChanged(val isLoading: Boolean): TodosEvent()

    object ToggleTodoState: TodosEvent()

    object Rebuild: TodosEvent()
}