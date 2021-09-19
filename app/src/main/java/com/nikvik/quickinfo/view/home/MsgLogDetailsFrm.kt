package com.nikvik.quickinfo.view.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.FrmMsgLogDetailsBinding
import com.nikvik.quickinfo.global.common.gone
import com.nikvik.quickinfo.global.common.visible
import com.nikvik.quickinfo.model.MailRespo
import com.nikvik.quickinfo.view.base.BaseFragment

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */
class MsgLogDetailsFrm : BaseFragment(R.layout.frm_msg_log_details) {
    private lateinit var mBinding: FrmMsgLogDetailsBinding

    companion object {
        val TAG = MsgLogDetailsFrm::class.qualifiedName
        val MSG_RESPO = "MSG_RESPO"
        fun getInstance(msgeInfo: MailRespo.DraftArray?): MsgLogDetailsFrm {
            val fragment = MsgLogDetailsFrm()
            val bundle = Bundle()
            bundle.putSerializable(MSG_RESPO,msgeInfo)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmMsgLogDetailsBinding
        init()
        clickListener()
    }

    private fun init() {
        val myList : MailRespo.DraftArray = arguments?.getSerializable(MSG_RESPO) as MailRespo.DraftArray
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.message_log)
        mBinding.txtIncidentDate.text = "  :   ${myList.incident_date}"
        mBinding.txtActionToken.text = myList.action_token
        mBinding.txtIncident.text = myList.incident
        setData(myList.other_info, mBinding.txtOtherInfo, mBinding.txtLabOther)
        setData(myList.tag1, mBinding.txtTag1, mBinding.txtLabTag1)
        setData(myList.tag2, mBinding.txtTag2, mBinding.txtLabTag2)
        setData(myList.tag3, mBinding.txtTag3, mBinding.txtLabTag3)
        setData(myList.tag4, mBinding.txtTag4, mBinding.txtLabTag4)
        setData(myList.tag5, mBinding.txtTag5, mBinding.txtLabTag5)
    }

    private fun setData(otherInfo: String, txtOtherInfo: TextView, txtLabOther: TextView) {
        if (otherInfo.isNotEmpty()) {
            txtOtherInfo.text = otherInfo
            txtOtherInfo.visible()
            txtLabOther.visible()
        }else{
            txtOtherInfo.gone()
            txtLabOther.gone()
        }
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.parent.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.parent -> {
            }
        }
    }
}

