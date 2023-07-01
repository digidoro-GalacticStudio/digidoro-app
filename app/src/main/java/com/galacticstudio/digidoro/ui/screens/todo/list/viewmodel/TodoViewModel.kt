package com.galacticstudio.digidoro.ui.screens.todo.list.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.data.TodoModel
import com.galacticstudio.digidoro.domain.usecase.todo.AddTodo
import com.galacticstudio.digidoro.domain.usecase.todo.DeleteTodo
import com.galacticstudio.digidoro.domain.usecase.todo.GetTodos
import com.galacticstudio.digidoro.domain.usecase.todo.TodoUseCases
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.TodoRepository
import com.galacticstudio.digidoro.ui.screens.todo.list.TodoState
import com.galacticstudio.digidoro.ui.screens.todo.list.TodosEvent
import com.galacticstudio.digidoro.ui.screens.todo.components.TodosResponseState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Calendar


class TodoViewModel(
    private val todoUseCases: TodoUseCases,
    private val repository: TodoRepository
) : ViewModel(){
    private val _state = mutableStateOf(TodoState())
    val state: State<TodoState> = _state

    private var apiState by mutableStateOf<TodosResponseState?>(TodosResponseState.Resume)

    private val reponseEventChanel = Channel<TodosResponseState>()

    val responseEvent : Flow<TodosResponseState> = reponseEventChanel.receiveAsFlow()

    fun onEvent(event: TodosEvent){
        when(event){
            is TodosEvent.ToggleTodoState -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is TodosEvent.Rebuild ->{
                getAllTodo(state.value.todosOrder)
            }
            is TodosEvent.Order ->{
                if(
                    state.value.todosOrder::class == event.todoOrder::class &&
                            state.value.todosOrder.orderType == event.todoOrder.orderType
                ) return

                getAllTodo(event.todoOrder)
            }
        }
    }

    private fun sendResponseEvent(event: TodosResponseState){
        viewModelScope.launch {
            reponseEventChanel.send(event)
        }
    }

    private fun getAllTodo(todosOrder: TodoOrder) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            val sortBy = when (todosOrder) {
                is TodoOrder.Date -> "createdAt"
                else -> ""
            }
            val order = when(todosOrder.orderType){
                is OrderType.Ascending -> "asc"
                is OrderType.Descending -> "desc"
            }

            when (val response = repository.getAllTodo(
                sortBy = sortBy, order = order
            )) {
                is ApiResponse.Error -> {
                    sendResponseEvent(TodosResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(TodosResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    _state.value = _state.value.copy(
                        todos =  response.data,
                        todayTodos = todayFilter(response.data),
                        notTodayTodos = noTodayFilter(response.data),
                        doneTodos = doneFilter(response.data),
                        todosOrder = todosOrder,
                        isLoading = false
                    )
                    sendResponseEvent(TodosResponseState.Success)
                }
            }
        }
    }

    //display today todos
    private fun todayFilter(list: List<TodoModel>): MutableList<TodoModel>{
        val today = Calendar.getInstance().time
        return list.filter { item -> item.createdAt.date == today.date && !item.state }.toMutableList()
    }

    //display notToday todos
    private fun noTodayFilter(list: List<TodoModel>): MutableList<TodoModel>{
        val today = Calendar.getInstance().time
        return list.filter { item -> item.createdAt.date != today.date && !item.state }.toMutableList()
    }

    //display todos marked as complete
    private fun doneFilter(list: List<TodoModel>): MutableList<TodoModel>{
        return list.filter { item -> item.state }.toMutableList()
    }


// add code to TodosOrder:

    sealed class TodoOrder(val orderType: OrderType){
        class Date(orderType: OrderType): TodoOrder(orderType)
        class Done(orderType: OrderType): TodoOrder(orderType)

        fun copy(orderType: OrderType): TodoOrder {
            return when(this){
                is Date -> Date(orderType)
                is Done -> Done(orderType)
            }
        }
    }
    companion object{
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as Application
                TodoViewModel(
                    todoUseCases = TodoUseCases(
                        getTodos = GetTodos(),
                        deleteTodo = DeleteTodo(app.todoRepository),
                        addTodo = AddTodo()
                    ),
                    repository = app.todoRepository
                )
            }
        }
    }

}




sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()

}