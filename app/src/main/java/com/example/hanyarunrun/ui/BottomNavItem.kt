package com.example.hanyarunrun.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Add : BottomNavItem("form", "Buat Data", Icons.Filled.Add)
    object CreatedList : BottomNavItem("created_list", "Data Baru", Icons.Filled.AddCircle)
    object DataList : BottomNavItem("data_list", "List Data", Icons.Filled.List)
    object Profile : BottomNavItem("profile", "Profil", Icons.Filled.Person)
}
