package com.galacticstudio.digidoro.ui.screens.todo

sealed class TodosEvent {

    data class Order(val todoOrder: TodoOrder): TodosEvent()

    object ToggleTodoState: TodosEvent()

    object Rebuild: TodosEvent()
}