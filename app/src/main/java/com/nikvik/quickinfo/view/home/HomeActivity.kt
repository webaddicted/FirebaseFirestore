package com.nikvik.quickinfo.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.messaging.FirebaseMessaging
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.ActivityHomeBinding
import com.nikvik.quickinfo.global.common.DialogUtil
import com.nikvik.quickinfo.global.common.MySing
import com.nikvik.quickinfo.model.MailRespo
import com.nikvik.quickinfo.model.pref.PreferenceBean
import com.nikvik.quickinfo.view.activity.LoginActivity
import com.nikvik.quickinfo.view.base.BaseActivity
import com.prodege.shopathome.model.networkCall.ApiConstant.Companion.FCM_API
import com.prodege.shopathome.model.networkCall.ApiConstant.Companion.contentType
import com.prodege.shopathome.model.networkCall.ApiConstant.Companion.serverKey
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */
class HomeActivity : BaseActivity(R.layout.activity_home) {

    private lateinit var mBinding: ActivityHomeBinding
    private var userInfo: PreferenceBean? = null

    companion object {
        val TAG = HomeActivity::class.qualifiedName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }

        fun newClearLogin(context: Activity?) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
            context?.finish()
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityHomeBinding
//        navigateScreen(NewsFrm.TAG)
        userInfo = preferenceMgr.getUserInfo()
        bottomNav()
    }

    override fun isNetworkConnected(isConnected: Boolean) {
    }

    private fun bottomNav() {
        mBinding.navBar.add(MeowBottomNavigation.Model(1, R.drawable.ic_msg_log))
        mBinding.navBar.add(MeowBottomNavigation.Model(2, R.drawable.ic_compose))
        mBinding.navBar.add(MeowBottomNavigation.Model(3, R.drawable.ic_draft))
        mBinding.navBar.add(MeowBottomNavigation.Model(4, R.drawable.ic_settings))
        mBinding.navBar.show(1)
        openFragment(MsgLogFrm.getInstance(Bundle()))
        mBinding.navBar.setOnShowListener {
            // YOUR CODES
        }

        mBinding.navBar.setOnClickMenuListener {
            when (it.id) {
                1 -> openFragment(MsgLogFrm.getInstance(Bundle()))
                2 -> openFragment(ComposeFrm.getInstance(Bundle()))
                3 -> openFragment(DraftFrm.getInstance(Bundle()))
                4 -> openFragment(SettingFrm.getInstance(Bundle()))
            }
        }
//        mBinding.navBar.setOnReselectListener {
//            // YOUR CODES
//        }
    }

    fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
//        transaction?.addToBackStack(null)
        transaction.commit()
    }

    fun openComposeFragment(draftInfo: MailRespo.DraftArray?) {
        mBinding.navBar.show(2)
        val bundle = Bundle()
        bundle.putSerializable(ComposeFrm.DRAFT_RESPO, draftInfo)
        val composeFrm = ComposeFrm.getInstance(bundle)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, composeFrm)
//        transaction?.addToBackStack(null)
        transaction.commit()
    }

    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    fun navigateScreen(
        tag: String,
        msgInfo: MailRespo.DraftArray?
    ) {
        var frm: Fragment? = null
        when (tag) {
            MsgLogDetailsFrm.TAG -> frm = MsgLogDetailsFrm.getInstance(msgInfo)
        }
        if (frm != null) navigateFragment(R.id.info_container, frm, true)
    }

    override fun onBackPressed() {
        val fm: FragmentManager = supportFragmentManager
        if (fm.fragments.size > 1) {
            super.onBackPressed()
            return
        }
        DialogUtil.showOkCancelDialog(
            this,
            getString(R.string.confirm_exit),
            getString(R.string.do_you_want_to_exit),
            { dialog, which ->
                preferenceMgr.clearPref()
                LoginActivity.newClearLogin(this@HomeActivity)
                finish()
                dialog.dismiss()
            },
            { dialog, which -> dialog.dismiss() })
    }

    fun sendPush(title: String, msg: String, designation: String, destrict: String) {
        if (designation.isEmpty()) return
        val tag: String = if (designation.contains("/"))
            designation.replace("/", "_")
        else designation
        val notification = JSONObject()
        val notifcationBody = JSONObject()
        try {
            notifcationBody.put("title", title)
            notifcationBody.put("message", msg)
            notification.put("to", "/topics/${tag}_$destrict")
            notification.put("data", notifcationBody)
        } catch (e: JSONException) {
            Log.e(SettingFrm.TAG, "onCreate: " + e.message)
        }
        sendNotification(notification)
    }

    private fun sendNotification(notification: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener { response ->
                Log.e("TAG", "onResponse: $response")
//                Toast.makeText(activity, "onResponse: $response", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener {
//                Toast.makeText(activity, "Request error", Toast.LENGTH_LONG).show()
                Log.e("TAG", "onErrorResponse: Didn't work")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
//            @get:Throws(AuthFailureError::class)
//            val headers: Map<String, String>?
//                get() {
//                    val params: MutableMap<String, String> = HashMap()
//                    params["Authorization"] = serverKey
//                    params["Content-Type"] = contentType
//                    return params
//                }
        }
        MySing.getInstance(this)?.addToRequestQueue(jsonObjectRequest)
    }

    override fun onDestroy() {
        FirebaseMessaging.getInstance()
            .subscribeToTopic(userInfo?.designation + "_" + userInfo?.destrict)
            .addOnCompleteListener {
            }
        preferenceMgr.clearPref()
        super.onDestroy()
    }
}