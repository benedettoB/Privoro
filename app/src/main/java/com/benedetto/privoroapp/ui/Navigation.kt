package com.benedetto.privoroapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.benedetto.privoroapp.ui.view.HomeScreen
import com.benedetto.privoroapp.ui.viewmodel.AddressViewModel
import com.benedetto.privoroapp.ui.viewmodel.DateTimeViewModel
import com.benedetto.privoroapp.ui.viewmodel.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigate(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val addressViewModel: AddressViewModel = hiltViewModel()
    val locationViewModel: LocationViewModel = hiltViewModel()
    val dateTimeViewModel: DateTimeViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PrivoroApp") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        content = { paddingValues ->
            Surface(
                modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                //content
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            navController,
                            modifier,
                            addressViewModel,
                            locationViewModel,
                            dateTimeViewModel
                        )
                    }
                }
            }
        }
    )
}