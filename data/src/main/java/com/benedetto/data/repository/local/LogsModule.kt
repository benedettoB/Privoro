package com.benedetto.data.repository.local

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal abstract class LogsModule {

    @Binds
    @Singleton
    abstract fun bindLogsRepository(
        logsRepositoryImpl: LogsRepositoryImpl
    ): LogsRepository

}