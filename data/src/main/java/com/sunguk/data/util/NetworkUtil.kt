package com.sunguk.data.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities


object NetworkUtil {
    enum class NetworkState {
        NO_ACCESS_NETWORK_STATE_PERMISSION,
        NOT_CONNECTED,
        CONNECTED_WIFI_NETWORK,
        CONNECTED_CELLULAR_NETWORK
    }

    @SuppressLint("MissingPermission")
    fun getNetworkConnectionState(context: Context): NetworkState {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

        val network: Network = connectivityManager.activeNetwork
            ?: return NetworkState.NO_ACCESS_NETWORK_STATE_PERMISSION
        val actNetwork: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(network)
                ?: return NetworkState.NO_ACCESS_NETWORK_STATE_PERMISSION

        return when {
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkState.CONNECTED_CELLULAR_NETWORK
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkState.CONNECTED_WIFI_NETWORK
            else -> NetworkState.NOT_CONNECTED
        }
    }
}

fun isNetworkConnectionAvailable(context: Context): Boolean {
    return NetworkUtil.getNetworkConnectionState(context).let {
        it == NetworkUtil.NetworkState.CONNECTED_WIFI_NETWORK
                || it == NetworkUtil.NetworkState.CONNECTED_CELLULAR_NETWORK
    }
}