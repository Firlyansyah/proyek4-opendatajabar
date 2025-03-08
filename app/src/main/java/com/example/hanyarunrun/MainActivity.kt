package com.example.hanyarunrun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hanyarunrun.ui.AppNavHost
import com.example.hanyarunrun.viewmodel.DataViewModel
import androidx.compose.runtime.LaunchedEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val dataViewModel: DataViewModel = viewModel()

            LaunchedEffect(Unit) {
                dataViewModel.fetchAndSaveBencana()
            }

            AppNavHost(viewModel = dataViewModel)
        }
    }
}
