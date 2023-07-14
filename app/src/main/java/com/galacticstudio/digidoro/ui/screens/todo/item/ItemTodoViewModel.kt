package com.galacticstudio.digidoro.ui.screens.todo.item

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.domain.usecase.todo.AddTodo
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.TodoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class ItemTodoViewModel(
    private val todoRepository: TodoRepository,
    private val addTodo: AddTodo = AddTodo()
) : ViewModel()
{
    private val _state = mutableStateOf(ItemTodoState())
    val state: State<ItemTodoState> = _state

    private val _actionTypeEvents = mutableStateOf<ItemTodoActionType>(ItemTodoActionType.CreateTodo)
    val actionTypeEvents: State<ItemTodoActionType> = _actionTypeEvents

    private var apiState by mutableStateOf<ItemTodoResponseState?>(ItemTodoResponseState.Resume)

    // Channel for emitting login response states.
    private val responseEventChannel = Channel<ItemTodoResponseState>()
    val responseEvents: Flow<ItemTodoResponseState> = responseEventChannel.receiveAsFlow()


    fun onEvent(event: ItemTodoEvent) {
        when (event) {
//            is NoteItemEvent.NoteIdChanged -> {
//                getNoteById(event.noteId)
//                isFavorite()
//            }
            is ItemTodoEvent.titleChanged -> {
                _state.value = state.value.copy(title = event.title)
            }

            is ItemTodoEvent.descriptionChanged -> {
                _state.value = state.value.copy(description = event.description)
            }


            is ItemTodoEvent.themeChanged -> {
                _state.value = state.value.copy(theme = event.theme)
            }

            is ItemTodoEvent.stateChanged -> {
                _state.value = state.value.copy(state = event.state)
            }

            is ItemTodoEvent.reminderChanged -> {
                _state.value = state.value.copy(reminder = event.reminder)
            }

            is ItemTodoEvent.ActionTypeChanged -> {
                _actionTypeEvents.value = actionTypeEvents.value
            }

            is ItemTodoEvent.RemoveTodo -> {
                deleteNote(_state.value.id)
            }

            is ItemTodoEvent.SaveTodo -> {
                submitData()
            }

            is ItemTodoEvent.UpdateTodo -> {
                updateData()
            }

            is ItemTodoEvent.ToggleComplete -> {
                _state.value = state.value.copy(id = event.id)
                toggleComplete(_state.value.id)
            }

        }
    }

    private fun sendResponseEvent(event: ItemTodoResponseState) {
        viewModelScope.launch {
            responseEventChannel.send(event)
        }
    }

    private fun deleteNote(id: String){
        executeOperation(
            operation = {
                todoRepository.deleteTodoById(id = id)
            },
            onSuccess = {
                onExit()
            }
        )
    }

    fun onExit(){
        _state.value.id = ""
        _state.value.title = ""
        _state.value.description = ""
        _state.value.createdAt = Calendar.getInstance().time
        _state.value.state = false
        _state.value.reminder = Calendar.getInstance().time
    }

    fun onElementClick(
        id: String,
        title: String,
        description: String,
        createdAt: Date,
        state: Boolean,
        reminder: Date
    ){
        _state.value.id = id
        _state.value.title = title
        _state.value.description = description
        _state.value.createdAt = createdAt
        _state.value.state = state
        _state.value.reminder = reminder
    }

    private fun submitData(){
        if(!validateData()){
            sendResponseEvent(ItemTodoResponseState.ErrorWithMessage("Wrong Information"))
            return
        }
        createTodo(
            _state.value.title,
            _state.value.description,
            _state.value.theme,
            _state.value.reminder,
        )
    }
    private fun updateData(){
        if(!validateData()){
            sendResponseEvent(ItemTodoResponseState.ErrorWithMessage("Wrong Information"))
            return
        }

        updateTodos(
            id = _state.value.id,
            _state.value.title,
            _state.value.description,
            _state.value.theme,
            _state.value.reminder,
        )
    }

    private fun validateData(): Boolean{
        val result = addTodo.invoke(
            _state.value.title,
            _state.value.title,
            _state.value.theme,
            _state.value.reminder
        )

        val hasError = listOf(
            result,

        ).any{
            !it.successful
        }

        return !hasError
    }

    private fun createTodo(
        title: String,
        description: String = "",
        theme: String,
        reminder: Date

    ){
        executeOperation(
            operation = {
                Log.d("MyErrors", "operation here")
                todoRepository.createTodo(
                    title = title,
                    description = description,
                    theme =  theme,
                    reminder =  reminder
                )
            },
            onSuccess = {
                _actionTypeEvents.value = ItemTodoActionType.CreateTodo
                onExit()
            }
        )
    }

    private fun updateTodos(
        id: String,
        title: String,
        description: String,
        theme: String,
        reminder: Date
    ){
        executeOperation(
            operation = {
                todoRepository.updateTodo(
                    id = id,
                    title, theme, reminder, description
                )
            },
            onSuccess = {
                _actionTypeEvents.value = ItemTodoActionType.UpdateTodo
                onExit()
            }
        )
    }

    private fun toggleComplete(id: String){

        executeOperation(
            operation  = {
                todoRepository.toggleTodoState(
                    _state.value.id
                )
            },
            onSuccess = { response ->
                _state.value = _state.value.copy(
                    state = response.data.state
                )
            }
        )
    }

    private fun <T> executeOperation(
        operation: suspend () -> ApiResponse<T>,
        onSuccess: ((ApiResponse.Success<T>) -> Unit)? = null
    ) {
        viewModelScope.launch {
            when (val response = operation()) {
                is ApiResponse.Error -> {
                    sendResponseEvent(ItemTodoResponseState.Error(response.exception))
                }

                is ApiResponse.ErrorWithMessage -> {
                    sendResponseEvent(ItemTodoResponseState.ErrorWithMessage(response.message))
                }

                is ApiResponse.Success -> {
                    onSuccess?.invoke(response)
                    sendResponseEvent(ItemTodoResponseState.Success(_actionTypeEvents.value))
                }
            }
        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application

                ItemTodoViewModel(
                    addTodo = AddTodo(),
                    todoRepository = app.todoRepository
                )
            }
        }
    }

}