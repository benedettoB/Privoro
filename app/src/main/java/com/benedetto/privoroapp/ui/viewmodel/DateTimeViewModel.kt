package com.benedetto.privoroapp.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DateTimeViewModel @Inject constructor(private val application: Application) : ViewModel() {

    private val TAG = "DateTimeViewModel"
    private val _deviceTime = MutableStateFlow<String>("")
    private val _utcTime = MutableStateFlow<String>("")
    val deviceTime: StateFlow<String> = _deviceTime.asStateFlow()
    val utcTime: StateFlow<String> = _utcTime.asStateFlow()

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        updateTimes()
    }

    fun updateTimes() {
        viewModelScope.launch {
            getUtcTime().catch { exception -> Log.e(TAG, exception.message, exception) }
                .buffer()
                .collect { result -> _utcTime.value = result }
            getDeviceTime().catch { exception -> Log.e(TAG, exception.message, exception) }
                .buffer()
                .collect { result -> _deviceTime.value = result }

        }
    }

    private fun getUtcTime(): Flow<String> = flow<String> {
        val utcZone: ZoneId = ZoneId.of("UTC")
        val currentDateTimeInUTC: ZonedDateTime = ZonedDateTime.now(utcZone)
        val formattedUtcDateTime = currentDateTimeInUTC.format(formatter)
        emit(formattedUtcDateTime)

    }.flowOn(Dispatchers.IO)

    private fun getDeviceTime(): Flow<String> = flow<String> {
        val userTimeZone: ZoneId = ZoneId.systemDefault()
        val currentDateTimeInUserTimeZone: ZonedDateTime = ZonedDateTime.now(userTimeZone)
        val formattedUserDateTime = currentDateTimeInUserTimeZone.format(formatter)
        emit(formattedUserDateTime)
    }.flowOn(Dispatchers.IO)

}