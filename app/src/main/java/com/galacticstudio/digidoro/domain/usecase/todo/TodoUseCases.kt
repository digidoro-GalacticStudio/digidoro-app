package com.galacticstudio.digidoro.domain.usecase.todo

data class TodoUseCases(
    val getTodos: GetTodos,
    val deleteTodo: DeleteTodo,
    val addTodo: AddTodo,

)
