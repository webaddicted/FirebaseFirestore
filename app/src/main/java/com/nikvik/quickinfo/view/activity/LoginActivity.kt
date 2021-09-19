package com.nikvik.quickinfo.view.activity

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.ActivityLoginBinding
import com.nikvik.quickinfo.global.common.DialogUtil
import com.nikvik.quickinfo.global.common.GlobalUtility
import com.nikvik.quickinfo.global.common.ValidationHelper
import com.nikvik.quickinfo.global.common.isInternetConnected
import com.nikvik.quickinfo.model.UserRespo
import com.nikvik.quickinfo.model.pref.PreferenceBean
import com.nikvik.quickinfo.view.base.BaseActivity
import com.nikvik.quickinfo.view.home.HomeActivity
import com.prodege.shopathome.model.networkCall.ApiConstant.Companion.COLLECTION_USERS


/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */
class LoginActivity() : BaseActivity(R.layout.activity_login) {
    private var userInfo: UserRespo? = UserRespo()
    private var unitList: List<UserRespo.District.UnitArray>? = null
    private lateinit var mBinding: ActivityLoginBinding
    private val db = FirebaseFirestore.getInstance()
    private val colRefUsers = db.collection(COLLECTION_USERS)

    companion object {
        val TAG = LoginActivity::class.qualifiedName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }

        fun newClearLogin(context: Activity?) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
            context?.finish()
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityLoginBinding
        init()
        clickListener()
        setNavigationColor(ContextCompat.getColor(this, R.color.app_color))
    }

    override fun isNetworkConnected(isConnected: Boolean) {
        TODO("Not yet implemented")
    }

    private fun init() {
//        firestoreDB = FirebaseFirestore.getInstance()
//        if (!BuildConfig.IS_QA) {
//        mBinding.edtMobile.setText("9024061407")
//            mBinding.edtDesig.setText(India)
//            mBinding.edtDistrict.setText(getCountryList()[2])
//            mBinding.edtUnit.setText(getCountryList()[2])
//            mBinding.edtLang.setText(getCountryList()[2])
//            mBinding.edtPwd.setText("123")
//        }
        GlobalUtility.changeLanguage(baseContext, "en")
        getUserInfo()

    }

    private fun getUserInfo() {
        if (!isInternetConnected()) return
        colRefUsers.get().addOnSuccessListener {
            if (!it.isEmpty) {
                userInfo?.designation_array =
                    it.documents[0].toObject(UserRespo::class.java)?.designation_array
                userInfo?.district_array =
                    it.documents[1].toObject(UserRespo::class.java)?.district_array
                userInfo?.lang_array = it.documents[2].toObject(UserRespo::class.java)?.lang_array
            } else {
                GlobalUtility.showToast(getString(R.string.txt_no_data_found))
            }
        }.addOnFailureListener { e ->
            GlobalUtility.showToast(getString(R.string.something_went_wrong) + e.message)
        }
    }

    private fun clickListener() {
        mBinding.btnLogin.setOnClickListener(this)
        mBinding.edtDesig.setOnClickListener(this)
        mBinding.edtDistrict.setOnClickListener(this)
        mBinding.edtUnit.setOnClickListener(this)
        mBinding.edtLang.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btn_login -> validate()
            R.id.edt_desig -> userInfo?.designation_array?.let {
                DialogUtil.showListDialog(
                    this,
                    resources.getString(R.string.select_desig),
                    it
                ) { dialog, which ->
                    mBinding.edtDesig.setText(userInfo?.designation_array!![which].name)
                    dialog.dismiss()
                }
            }
            R.id.edt_district -> userInfo?.district_array?.let {
                DialogUtil.showListDialog(
                    this,
                    resources.getString(R.string.select_district),
                    it
                ) { dialog, which ->
                    mBinding.edtDistrict.setText(userInfo?.district_array!![which].district_name)
                    unitList = userInfo?.district_array!![which].unit_array
                    dialog.dismiss()
                }
            }
            R.id.edt_unit -> {
                if (unitList != null) {
                    DialogUtil.showListDialog(
                        this,
                        resources.getString(R.string.select_unit),
                        unitList!!
                    ) { dialog, which ->
                        mBinding.edtUnit.setText(unitList!![which].unit_name)
                        mBinding.edtUnit.tag = unitList!![which].password
                        dialog.dismiss()
                    }
                } else
                    GlobalUtility.showToast(getString(R.string.please_select_distric_first))
            }
            R.id.edt_lang -> userInfo?.lang_array?.let {
                DialogUtil.showListDialog(
                    this,
                    resources.getString(R.string.select_lang),
                    it
                ) { dialog, which ->
                    mBinding.edtLang.setText(userInfo?.lang_array!![which].lang_name)
                    dialog.dismiss()
                }
            }
        }
    }

    private fun validate() {
        if (!ValidationHelper.validateBlank(
                mBinding.edtDesig,
                mBinding.wrapperDesig,
                getString(R.string.select_desig)
            ) ||
            !ValidationHelper.validateBlank(
                mBinding.edtDistrict,
                mBinding.wrapperDistrict,
                getString(R.string.select_district)
            ) ||
            !ValidationHelper.validateBlank(
                mBinding.edtUnit,
                mBinding.wrapperUnit,
                getString(R.string.select_unit)
            ) ||
            !ValidationHelper.validateBlank(
                mBinding.edtLang,
                mBinding.wrapperLang,
                getString(R.string.select_lang)
            ) ||
            !ValidationHelper.validatePwd(mBinding.edtPwd, mBinding.wrapperPwd)
        ) return
        if (!isInternetConnected()) return
        if (mBinding.edtPwd.text.toString().trim() == mBinding.edtUnit.tag.toString().trim()) {
            val prefBean = PreferenceBean()
                .apply {
                    designation = mBinding.edtDesig.text.toString()
                    destrict = mBinding.edtDistrict.text.toString()
                    unit = mBinding.edtUnit.text.toString()
                    language = mBinding.edtLang.text.toString()
                    password = mBinding.edtPwd.text.toString()
                }
            subscribeTopic()
            preferenceMgr.setUserInfo(prefBean)
            GlobalUtility.changeLanguage(
                baseContext,
                if (mBinding.edtLang.text.toString() == "Hindi") "hi" else "en"
            )
            HomeActivity.newClearLogin(this)
            finish()
        } else GlobalUtility.showToast(getString(R.string.enter_valid_passowrd))
    }

    private fun subscribeTopic() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isComplete) {
                val newToken = it.result.toString()
                Log.e("TAG", "token   :   $newToken")
            }
        }
        val tag: String
        val designation = mBinding.edtDesig.text.toString()
        if (designation.contains("/"))
            tag = designation.replace("/", "_")
        else tag = designation
        FirebaseMessaging.getInstance()
            .subscribeToTopic(tag + "_" + mBinding.edtDistrict.text.toString())
            .addOnCompleteListener { task ->
//                var msg = "getString(R.string.msg_subscribed)"
//                if (!task.isSuccessful) {
//                    msg = "getString(R.string.msg_subscribe_failed)"
//                }
//                Log.e("TAG", "msg :    $msg")
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
    }

}