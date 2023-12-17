package com.sunguk.data.repository

import android.content.Context
import com.sunguk.data.util.NetworkUtil
import com.sunguk.domain.repository.NetworkStateRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NetworkStateRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkStateRepository {
    override suspend fun isNetworkConnectionAvailable(): Boolean {
        return NetworkUtil.getNetworkConnectionState(context).let {
            it == NetworkUtil.NetworkState.CONNECTED_WIFI_NETWORK
                    || it == NetworkUtil.NetworkState.CONNECTED_CELLULAR_NETWORK
        }
    }
}

