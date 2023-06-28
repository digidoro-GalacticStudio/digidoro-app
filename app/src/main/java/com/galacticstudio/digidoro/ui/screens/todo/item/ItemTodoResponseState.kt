package com.galacticstudio.digidoro.ui.screens.todo.item

sealed class ItemTodoResponseState{
    object Resume: ItemTodoResponseState()

    class Error(val exception: Exception): ItemTodoResponseState()

    data class ErrorWithMessage(val message: String) : ItemTodoResponseState()

    data class Success(val actionType: ItemTodoActionType) : ItemTodoResponseState()
}

sealed class ItemTodoActionType{
    object CreateTodo : ItemTodoActionType()

    object DeleteTodo : ItemTodoActionType()

    object UpdateTodo: ItemTodoActionType()
}