package com.benedetto.privoroapp.ui.viewmodel

import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val application: Application) : ViewModel() {

    //FusedLocationProviderClient instance
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application.applicationContext)

    private val _locationStateFlow = MutableStateFlow<Location?>(null)
    val locationStateFlow: StateFlow<Location?> = _locationStateFlow.asStateFlow()

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        10000L //interval of 10 seconds
    )
        .setMinUpdateIntervalMillis(5000L) //fastest interval of 5 seconds, can also update based on how many meters the device has moved
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                _locationStateFlow.value = location
            }
        }
    }

    init {
        startLocationUpdates()
    }

    fun startLocationUpdates() {
        CoroutineScope(Dispatchers.IO).launch {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }

    override fun onCleared() {
        super.onCleared()
        //remove location updates when viewmodel is cleared
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}