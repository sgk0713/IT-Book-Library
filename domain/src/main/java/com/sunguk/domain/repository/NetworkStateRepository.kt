package com.sunguk.domain.repository

interface NetworkStateRepository {
    suspend fun isNetworkConnectionAvailable(): Boolean
}