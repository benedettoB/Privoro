package com.benedetto.data.repository.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkAddressModule {
    @Binds
    @Singleton
    abstract fun bindNetworkAddressRepository(
        networkAddressRepositoryImpl: NetworkAddressRepositoryImpl
    ): NetworkAddressRepository

}