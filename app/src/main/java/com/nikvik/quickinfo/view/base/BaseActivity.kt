package com.nikvik.quickinfo.view.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.global.common.*
import com.nikvik.quickinfo.global.sharedpref.PreferenceMgr
import com.nikvik.quickinfo.view.LoaderDialog
import org.koin.android.ext.android.inject


/**
 * Created by Deepak Sharma on 01/07/19.
 */
abstract class BaseActivity(private val layoutId: Int) : AppCompatActivity(), View.OnClickListener {
    private var loaderDialog: LoaderDialog? = null
    protected val preferenceMgr: PreferenceMgr by inject()

    companion object {
        val TAG = BaseActivity::class.qualifiedName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out)
        supportActionBar?.hide()
        setNavigationColor(ContextCompat.getColor(this, R.color.app_color))
        fullScreen()
        GlobalUtility.hideKeyboard(this)
        GlobalUtility.changeLanguage(
            baseContext,
            if (preferenceMgr.getUserInfo().language == "Hindi") "hi" else "en"
        )
        val binding: ViewDataBinding?
        if (layoutId != 0) {
            try {
                binding = DataBindingUtil.setContentView(this, layoutId)
                onBindTo(binding)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (loaderDialog == null) {
            loaderDialog = LoaderDialog.dialog()
            loaderDialog?.isCancelable = false
        }
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            if (window != null) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    protected fun setNavigationColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.navigationBarColor = color;
        }
    }
    abstract fun onBindTo(binding: ViewDataBinding)

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
    }

    override fun onClick(v: View?) {}

    fun navigateFragment(layoutContainer: Int, fragment: Fragment, isEnableBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out, R.anim.trans_right_in, R.anim.trans_right_out)
        fragmentTransaction.replace(layoutContainer, fragment)
        if (isEnableBackStack)
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun navigateAddFragment(layoutContainer: Int, fragment: Fragment, isEnableBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out, R.anim.trans_right_in, R.anim.trans_right_out)
        fragmentTransaction.add(layoutContainer, fragment)
        if (isEnableBackStack)
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        fragmentTransaction.commitAllowingStateLoss()
    }

    protected fun showApiLoader() {
        if (loaderDialog != null) {
            val fragment = supportFragmentManager.findFragmentByTag(LoaderDialog.TAG)
            if (fragment != null) supportFragmentManager.beginTransaction().remove(fragment)
                .commit()
            loaderDialog?.show(supportFragmentManager, LoaderDialog.TAG)
        }
    }

    protected fun hideApiLoader() {
        if (loaderDialog != null && loaderDialog?.isVisible!!) loaderDialog?.dismiss()
    }

    /**
     * broadcast receiver for check internet connectivity
     *
     * @return
     */
    private fun getNetworkStateReceiver() {
        NetworkChangeReceiver.isInternetAvailable(object :
            NetworkChangeReceiver.ConnectivityReceiverListener {
            override fun onNetworkConnectionChanged(isConnected: Boolean) {
                try {
                    isNetworkConnected(isConnected)
                } catch (exception: Exception) {
                    Lg.d(TAG, "getNetworkStateReceiver : $exception")
                }
            }
        })
    }

    abstract fun isNetworkConnected(isConnected: Boolean)

    protected fun showInternetSnackbar(internetConnected: Boolean, txtNoInternet: TextView) {
        if (internetConnected) {

            txtNoInternet.text = getString(R.string.back_online)
            val color = arrayOf(
                ColorDrawable(ContextCompat.getColor(this, R.color.red_ff090b)),
                ColorDrawable(ContextCompat.getColor(this, R.color.green_00de4a))
            )
            val trans = TransitionDrawable(color)
            txtNoInternet.background = (trans)
            trans.startTransition(500)
            Handler().postDelayed({ txtNoInternet.gone() }, 1300)
        } else {
            txtNoInternet.text = getString(R.string.no_internet_connection)
            txtNoInternet.setBackgroundResource(R.color.red_ff090b)
            txtNoInternet.visible()
        }
    }
}