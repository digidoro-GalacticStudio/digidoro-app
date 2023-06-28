package com.galacticstudio.digidoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.navigation.AppScaffold
import com.galacticstudio.digidoro.ui.screens.MainViewModel
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel by viewModels {
                MainViewModel.Factory(applicationContext)
            }

            DigidoroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScaffold(
                        navController = rememberNavController(),
                        mainViewModel = mainViewModel,
                    )
                }
            }
        }
    }
}
