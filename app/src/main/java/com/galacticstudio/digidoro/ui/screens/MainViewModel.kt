package com.galacticstudio.digidoro.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.galacticstudio.digidoro.RetrofitApplication

class MainViewModel(context: Context) : ViewModel() {
    private val retrofitApplication = context.applicationContext as RetrofitApplication

    fun hasToken(): Boolean {
        return retrofitApplication.hasToken()
    }

    companion object {
        // Factory for creating instances of MainViewModel.
        val Factory = { context: Context ->
            viewModelFactory {
                initializer {
                    val app = context.applicationContext as RetrofitApplication
                    // Create a new instance of MainViewModel with dependencies.
                    MainViewModel(app)
                }
            }
        }
    }
}
