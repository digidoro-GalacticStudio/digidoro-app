package com.galacticstudio.digidoro.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.Application

class MainViewModel(context: Context) : ViewModel() {
    private val application = context.applicationContext as Application

    fun hasToken(): Boolean {
        return application.hasToken()
    }

    companion object {
        // Factory for creating instances of MainViewModel.
        val Factory = { context: Context ->
            viewModelFactory {
                initializer {
                    val app = context.applicationContext as Application
                    // Create a new instance of MainViewModel with dependencies.
                    MainViewModel(app)
                }
            }
        }
    }
}
