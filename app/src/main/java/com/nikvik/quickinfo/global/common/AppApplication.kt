package com.nikvik.quickinfo.global.common

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.global.koin.commonModelModule
import com.nikvik.quickinfo.global.sharedpref.PreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class AppApplication : Application() {
    private val mNetworkReceiver = NetworkChangeReceiver()

    companion object {
        lateinit var context: Context

    }

    override fun onCreate() {
        super.onCreate()
        context = this
        setupDefaultFont()
        PreferenceUtils.getInstance(this)
        startKoin {
            androidLogger()
            androidContext(this@AppApplication)
            modules(getModule())
        }
    }

    private fun getModule(): List<Module> {
        return listOf(
            commonModelModule
        )
    }

    /**
     * set default font for app
     */
    fun setupDefaultFont() {
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("font/opensans_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

    private fun checkInternetConnection() {
        registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}