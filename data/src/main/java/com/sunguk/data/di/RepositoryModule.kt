package com.sunguk.data.di

import com.sunguk.data.repository.BookRepositoryImpl
import com.sunguk.data.repository.NetworkStateRepositoryImpl
import com.sunguk.domain.repository.BookRepository
import com.sunguk.domain.repository.NetworkStateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(
        bookRepository: BookRepositoryImpl,
    ): BookRepository


    @Binds
    @Singleton
    abstract fun bindsNetworkStateRepository(
        networkStateRepositoryImpl: NetworkStateRepositoryImpl,
    ): NetworkStateRepository


}