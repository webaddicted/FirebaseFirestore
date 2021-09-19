package com.nikvik.quickinfo.global.sharedpref

import com.nikvik.quickinfo.global.constant.PreferenceConstant
import com.nikvik.quickinfo.model.pref.PreferenceBean

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class PreferenceMgr constructor(var preferenceUtils: PreferenceUtils) {
    /**
     * set user session info
     */
    fun setUserInfo(preferenceBean: PreferenceBean) {
        preferenceUtils.setPreference(
            PreferenceConstant.PREF_USER_DESIGNATION,
            preferenceBean.designation
        )
        preferenceUtils.setPreference(
            PreferenceConstant.PREF_USER_DESTRICT,
            preferenceBean.destrict
        )
        preferenceUtils.setPreference(PreferenceConstant.PREF_USER_UNIT, preferenceBean.unit)
        preferenceUtils.setPreference(
            PreferenceConstant.PREF_USER_LANGUAGE,
            preferenceBean.language
        )
        preferenceUtils.setPreference(
            PreferenceConstant.PREF_USER_PASSWORD,
            preferenceBean.password
        )
    }

    /**
     * get user session info
     */
    fun getUserInfo(): PreferenceBean {
        val preferenceBean =
            PreferenceBean()
        preferenceBean.designation =
            preferenceUtils.getPreference(PreferenceConstant.PREF_USER_DESIGNATION, "")!!
        preferenceBean.destrict =
            preferenceUtils.getPreference(PreferenceConstant.PREF_USER_DESTRICT, "")!!
        preferenceBean.unit = preferenceUtils.getPreference(PreferenceConstant.PREF_USER_UNIT, "")!!
        preferenceBean.language =
            preferenceUtils.getPreference(PreferenceConstant.PREF_USER_LANGUAGE, "")!!
        preferenceBean.password =
            preferenceUtils.getPreference(PreferenceConstant.PREF_USER_PASSWORD, "")!!
        return preferenceBean
    }

    fun clearPref(){
        preferenceUtils.clearAllGlobalPreferences()
        preferenceUtils.clearAllPreferences()

    }
}