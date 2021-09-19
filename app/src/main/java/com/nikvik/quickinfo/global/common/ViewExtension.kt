package com.nikvik.quickinfo.global.common

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import com.nikvik.quickinfo.R

/**
 * visible view
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * invisible view
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * gone view
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Gets network state.
 *
 * @return the network state
 */
fun Context.isNetworkAvailable(): Boolean {
    val connMgr =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connMgr.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isAvailable && activeNetwork.isConnected
}


fun Context.isInternetConnected(): Boolean {
    val connMgr =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connMgr.activeNetworkInfo
    val internetState =
        activeNetwork != null && activeNetwork.isAvailable && activeNetwork.isConnected
    if (!internetState) showNoNetworkToast()
    return internetState
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * show internet connection toast
 */
fun Context.showNoNetworkToast() {
    showToast(getResources().getString(R.string.no_network_msg))
}

/**
 * show internet connection toast
 */
fun Context.showSomwthingWrongToast() {
    showToast(resources.getString(R.string.something_went_wrong))
}

