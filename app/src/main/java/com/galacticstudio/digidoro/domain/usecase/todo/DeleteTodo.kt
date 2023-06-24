package com.galacticstudio.digidoro.domain.usecase.todo

import com.galacticstudio.digidoro.repository.TodoRepository

class DeleteTodo (
    private val repository: TodoRepository
    ){
    suspend operator fun invoke(id: String){
        repository.deleteTodoById(id)
    }
}