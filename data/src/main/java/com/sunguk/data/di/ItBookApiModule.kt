package com.sunguk.data.di

import android.content.Context
import com.sunguk.data.R
import com.sunguk.data.api.ItBookApi
import com.sunguk.data.util.MoshiCreator
import com.sunguk.data.util.ServiceCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ItBookApiModule {

    @Provides
    @Singleton
    fun providesItBookApi(
        @ApplicationContext appContext: Context,
        moshiCreator: MoshiCreator,
        serviceCreator: ServiceCreator,
    ): ItBookApi = serviceCreator.createService(
        ItBookApi::class.java,
        appContext.getString(R.string.it_book_api_base_url),
        moshiCreator.createMoshi(),
        emptyMap()
    )
}