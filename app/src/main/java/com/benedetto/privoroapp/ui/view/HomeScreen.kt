package com.benedetto.privoroapp.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.benedetto.privoroapp.ui.viewmodel.AddressViewModel
import com.benedetto.privoroapp.ui.viewmodel.DateTimeViewModel
import com.benedetto.privoroapp.ui.viewmodel.LocationViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier,
    addressViewModel: AddressViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    dateTimeViewModel: DateTimeViewModel = hiltViewModel()
) {
    val wifiAddress by addressViewModel.wifiIp.collectAsState()
    val cellAddress by addressViewModel.cellIp.collectAsState()
    val usersDeviceTime by dateTimeViewModel.deviceTime.collectAsState()
    val utcTime by dateTimeViewModel.utcTime.collectAsState()
    val locationStateFlow by locationViewModel.locationStateFlow.collectAsState()
    val latitude = "${locationStateFlow?.latitude ?: "Pending"}"
    val longitude = "${locationStateFlow?.longitude ?: "Pending"}"

    addressViewModel.logToFile("wifiIp: $wifiAddress, cellularIp: $cellAddress,  usersDeviceTime: $usersDeviceTime, utcTime: $utcTime, latitude: $latitude, longitude: $longitude")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)

    ) {
        DisplayWifiAddress(address = wifiAddress, modifier = modifier)
        Spacer(modifier = Modifier.height(16.dp))
        DisplayCellAddress(address = cellAddress, modifier = modifier)
        Spacer(modifier = Modifier.height(16.dp))
        DisplayLocation(latitude = latitude, longitude = longitude, modifier = modifier)
        Spacer(modifier = Modifier.height(16.dp))
        DisplayTimes(usersDeviceTime = usersDeviceTime, utcTime = utcTime, modifier = modifier)
        Spacer(modifier = Modifier.height(16.dp))
        DisplayShareFile(addressViewModel = addressViewModel, modifier = modifier)
    }
}

@Composable
fun DisplayWifiAddress(address: String, modifier: Modifier = Modifier) {
    Text(text = "Wifi Ip: $address")
}

@Composable
fun DisplayCellAddress(address: String, modifier: Modifier = Modifier) {
    Text(text = "Cellular Ip: $address")
}

@Composable
fun DisplayLocation(latitude: String, longitude: String, modifier: Modifier) {

    Text(
        text = "Latitude: $latitude"
    )
    Text(
        text = "Longitude: $longitude"
    )
}

@Composable
fun DisplayTimes(usersDeviceTime: String, utcTime: String, modifier: Modifier) {
    Text(text = "Device time: $usersDeviceTime")
    Text(text = "UTC time: $utcTime")
}

@Composable
fun DisplayShareFile(addressViewModel: AddressViewModel, modifier: Modifier) {
    val context = LocalContext.current
    Button(onClick = { addressViewModel.shareFile(context = context) }
    ) {
        Text(text = "Share Log CSV")
    }
}


