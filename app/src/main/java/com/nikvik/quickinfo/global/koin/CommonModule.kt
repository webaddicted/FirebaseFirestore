package com.nikvik.quickinfo.global.koin

import com.nikvik.quickinfo.global.sharedpref.PreferenceMgr
import com.nikvik.quickinfo.global.sharedpref.PreferenceUtils
import org.koin.dsl.module

/**
 * Created by Deepak Sharma on 01/07/19.
 */
val commonModelModule = module {
    single { PreferenceUtils() }
    single { PreferenceMgr(get()) }
}