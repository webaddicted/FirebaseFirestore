package com.nikvik.quickinfo.view.activity

import android.os.Handler
import androidx.databinding.ViewDataBinding
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.ActivitySplashBinding
import com.nikvik.quickinfo.global.constant.AppConstant
import com.nikvik.quickinfo.view.base.BaseActivity

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class SplashActivity : BaseActivity(R.layout.activity_splash) {
    val TAG = SplashActivity::class.qualifiedName
    private lateinit var mBinding: ActivitySplashBinding

    override fun isNetworkConnected(isConnected: Boolean) {
        showInternetSnackbar(isConnected, mBinding.txtNoInternet)
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivitySplashBinding
        init()
    }

    private fun init() {
        navigateToNext()
    }

    /**
     * navigate to welcome activity after Splash timer Delay
     */
    private fun navigateToNext() {
//        val desig = preferenceMgr.getUserInfo().designation
        preferenceMgr.clearPref()
        Handler().postDelayed({
//            if (desig != null && desig.isNotEmpty())
//                HomeActivity.newIntent(this)
//            else
            LoginActivity.newClearLogin(this)
        }, AppConstant.SPLASH_DELAY)
    }
}