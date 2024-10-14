package com.benedetto.privoroapp.ui.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benedetto.data.repository.local.LogsRepository
import com.benedetto.data.repository.remote.NetworkAddressRepository
import com.benedetto.privoroapp.PrivoroApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val networkAddressRepository: NetworkAddressRepository,
    private val logsRepository: LogsRepository
) :
    ViewModel() {

    private val _wifiIp = MutableStateFlow<String>("")
    private val _cellIp = MutableStateFlow<String>("")
    val wifiIp: StateFlow<String> = _wifiIp.asStateFlow()
    val cellIp: StateFlow<String> = _cellIp.asStateFlow()


    //Receiver to handle connectivity changes
    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, inetent: Intent) {
            updateIpAddresses()
        }

    }

    init {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        PrivoroApp.appContext.registerReceiver(connectivityReceiver, filter)
        updateIpAddresses()
    }

    fun logToFile(msg: String) {
        viewModelScope.launch {
            logsRepository.log(msg)
        }
    }

    fun shareFile(context: Context) {
        viewModelScope.launch {
            val csvFIle = logsRepository.get()
            val csvUri = FileProvider.getUriForFile(
                context,
                "${PrivoroApp.appContext.packageName}.fileprovider",
                csvFIle
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, csvUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            }
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Share CSV file"
                )
            )

        }
    }

    fun updateIpAddresses() {
        getWifiAddress()
        getCellAddress()
        Log.d("IpAddress", "it fired()")
    }

    private fun getWifiAddress() {
        viewModelScope.launch {
            networkAddressRepository.getWifiAddress()
                .catch { exception ->
                    Log.e("PrivoroViewModel", exception.message, exception)
                }
                .buffer()
                .collect { result ->
                    _wifiIp.value = result
                }
        }
    }

    private fun getCellAddress() {
        viewModelScope.launch {
            networkAddressRepository.getCellAddress()
                .catch { exception ->
                    Log.e("PrivoroViewModel", exception.message, exception)
                }
                .buffer()
                .collect { result ->
                    _cellIp.value = result
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        //unregister the receiver when the ViewModel is cleared
        PrivoroApp.appContext.unregisterReceiver(connectivityReceiver)
    }
}