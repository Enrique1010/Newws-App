package com.erapps.newws.di

import com.erapps.newws.data.source.favs.FavsDefaultRepository
import com.erapps.newws.data.source.favs.IFavsRepository
import com.erapps.newws.data.source.local.FavsLocalDS
import com.erapps.newws.room.FavsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavsModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FavsLocalDataSource

    @Singleton
    @Provides
    @FavsLocalDataSource
    fun provideFavsLocalDS(
        favsDao: FavsDao,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): FavsLocalDS {
        return FavsLocalDS(
            favsDao,
            coroutineDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideFavsRepository(
        @FavsLocalDataSource favsLocalDS: FavsLocalDS,
        @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher
    ): IFavsRepository {
        return FavsDefaultRepository(
            favsLocalDS,
            coroutineDispatcher
        )
    }
}