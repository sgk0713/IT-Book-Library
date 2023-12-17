package com.sunguk.data.util

import com.squareup.moshi.Moshi
import com.sunguk.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ServiceCreator @Inject constructor(
) {

    fun <T> createService(
        serviceClass: Class<T>,
        baseUrl: String,
        moshi: Moshi,
        header: Map<String, String>,
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createClient(header).build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(serviceClass)
    }

    private fun createClient(header: Map<String, String>): OkHttpClient.Builder {
        val httpClientBuilder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        val headerInterceptor = Interceptor { chain ->
            val builder = chain.request().newBuilder()
            header.forEach { (key, value) ->
                builder.addHeader(key, value)
            }
            chain.proceed(builder.build())
        }

        httpClientBuilder.addInterceptor(logging)
        httpClientBuilder.addInterceptor(headerInterceptor)
        httpClientBuilder.readTimeout(60, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(60, TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        return httpClientBuilder
    }
}