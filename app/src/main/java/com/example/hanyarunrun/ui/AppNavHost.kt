package com.example.hanyarunrun.ui

import BottomNavigationBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hanyarunrun.viewmodel.DataViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.hanyarunrun.data.DataEntity
import com.proyek.jtk.ui.screen.profile.ProfileScreen

@Composable
fun AppNavHost(viewModel: DataViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "profile",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Navigasi Bottom Navigation
            composable(BottomNavItem.Add.route) {
                AddProfileScreen(navController = navController, viewModel = viewModel)
            }
            composable(BottomNavItem.CreatedList.route) {
                CreatedDataListScreen(navController = navController, viewModel = viewModel)
            }
            composable(BottomNavItem.DataList.route) {
                DataListScreen(navController = navController, viewModel = viewModel)
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(modifier = Modifier, viewModel = viewModel, navController = navController)
            }


            // Navigasi ke layar lain
            composable("form") {
                DataEntryScreen(navController = navController, viewModel = viewModel)
            }
            composable("addProfile"){
                AddProfileScreen(navController = navController, viewModel = viewModel)
            }

            composable(
                route = "edit/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                EditScreen(navController = navController, viewModel = viewModel, dataId = id)
            }
            composable("delete/{itemId}") { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()

                if (itemId != null) {

                    var data: DataEntity? by remember { mutableStateOf(null) }

                    LaunchedEffect(itemId) {
                        data = viewModel.getDataById(itemId)
                    }

                    if (data != null) {
                        DeleteConfirmationScreen(itemId, data!!, viewModel, navController)
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }

        }
    }
}

