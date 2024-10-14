package com.benedetto.data.repository.remote

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.InetAddress
import java.net.NetworkInterface
import javax.inject.Inject


internal class NetworkAddressRepositoryImpl @Inject constructor(private val application: Application) :
    NetworkAddressRepository {

    override fun getWifiAddress(): Flow<String> = flow {
        val data = getWifiIP()
        emit(data)
    }.flowOn(Dispatchers.IO)

    override fun getCellAddress(): Flow<String> = flow<String> {
        val data = getMobileIp()
        emit(data)
    }.flowOn(Dispatchers.IO)

    private fun getWifiIP(): String {
        val wifiManager: WifiManager =
            application.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ip: Int = wifiManager.connectionInfo.ipAddress
        return Formatter.formatIpAddress(ip)
    }

    //Get Cellular IP Address
    private fun getMobileIp(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces().toList()
            for (networkInterface in interfaces) {
                val addresses = networkInterface.inetAddresses.toList()
                for (address in addresses) {
                    //Check if it's Ipv4 and not a loopback address
                    if (!address.isLoopbackAddress && address is InetAddress && address.hostAddress.indexOf(
                            ':'
                        ) == -1
                    ) {
                        return address.hostAddress ?: "No Cellular IP"
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "No Cellular IP"
    }
}