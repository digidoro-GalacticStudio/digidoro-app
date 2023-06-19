package com.galacticstudio.digidoro.ui.screens.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.galacticstudio.digidoro.ui.screens.home.HomeUIEvent
import com.galacticstudio.digidoro.ui.screens.home.HomeUIState
import com.galacticstudio.digidoro.ui.screens.login.LoginFormEvent

class HomeViewModel() : ViewModel() {
    // The current state of the login form
    var state by mutableStateOf(HomeUIState())

    /**
     * Handles the home UI events.
     *
     * @param event The UI event to handle.
     */
    fun onEvent(event: HomeUIEvent) {
        when (event) {
            is HomeUIEvent.UsernameChanged -> {
                // When the email is changed, update the email value in the state
                state = state.copy(username = event.username)
            }
        }
    }
}