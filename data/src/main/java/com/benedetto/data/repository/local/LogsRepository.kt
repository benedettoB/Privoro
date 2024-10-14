package com.benedetto.data.repository.local

import java.io.File

interface LogsRepository {

    suspend fun log(msg: String)

    suspend fun get(): File


}