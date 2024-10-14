package com.benedetto.data.repository.local

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

internal class LogsRepositoryImpl @Inject constructor(private val application: Application) :
    LogsRepository {

    override suspend fun log(msg: String) {
        writeLog(msg)
    }

    override suspend fun get(): File {
        return createFile()
    }


    private suspend fun writeLog(msg: String) {
        withContext(Dispatchers.IO) {
            val csvFile = createFile()
            try {
                val fileWriter = FileWriter(csvFile, true)//true to append to file
                fileWriter.append(msg)
                fileWriter.append("\n")
                fileWriter.flush()
                fileWriter.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private suspend fun createFile(): File {
        val directory = application.applicationContext.filesDir //internal app storage
        val csvFile = File(directory, "logs.csv")
        if (!csvFile.exists()) {
            withContext(Dispatchers.IO) {
                csvFile.createNewFile()
            }

        }
        return csvFile
    }


}