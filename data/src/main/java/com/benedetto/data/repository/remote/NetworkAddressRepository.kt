package com.benedetto.data.repository.remote

import kotlinx.coroutines.flow.Flow

interface NetworkAddressRepository {
    fun getWifiAddress(): Flow<String>
    fun getCellAddress(): Flow<String>

}