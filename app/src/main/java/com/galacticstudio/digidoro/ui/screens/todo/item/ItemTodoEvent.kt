package com.galacticstudio.digidoro.ui.screens.todo.item
import java.util.Date

sealed class ItemTodoEvent {
    data class titleChanged(val title: String): ItemTodoEvent()

    data class descriptionChanged(val description: String): ItemTodoEvent()

    data class themeChanged(val theme: String): ItemTodoEvent()

    data class stateChanged(val state: Boolean): ItemTodoEvent()

    data class reminderChanged(val reminder: Date): ItemTodoEvent()

    data class ActionTypeChanged(val actionType: ItemTodoActionType): ItemTodoEvent()

    object SaveTodo: ItemTodoEvent()

    object UpdateTodo: ItemTodoEvent()

    object RemoveTodo: ItemTodoEvent()

    data class ToggleComplete(val id: String): ItemTodoEvent()
}