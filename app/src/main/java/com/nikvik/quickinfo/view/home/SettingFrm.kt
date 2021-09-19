package com.nikvik.quickinfo.view.home

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.FrmSettingBinding
import com.nikvik.quickinfo.view.activity.LoginActivity
import com.nikvik.quickinfo.view.base.BaseFragment


/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */
class SettingFrm : BaseFragment(R.layout.frm_setting) {
    private lateinit var mBinding: FrmSettingBinding

    companion object {
        val TAG = SettingFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): SettingFrm {
            val fragment = SettingFrm()
            fragment.arguments = bundle
            return SettingFrm()
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmSettingBinding
        init()
        clickListener()
    }

    private fun init() {
//        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.setting)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.relatLang.setOnClickListener(this)

        mBinding.txtLogout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.relat_lang -> {
//                DialogUtil.showListDialog(activity!!,
//                    resources.getString(R.string.select_lang),
//                    getLang(),
//                    DialogInterface.OnClickListener { dialog, which ->
//                        mBinding.txtLang.text = getLang()[which]
//                        dialog.dismiss()
//                    })
//                sendPush()
            }
            R.id.txt_logout -> {
                preferenceMgr.clearPref()
                LoginActivity.newClearLogin(activity)
                activity?.finish()
            }

        }
    }
}

