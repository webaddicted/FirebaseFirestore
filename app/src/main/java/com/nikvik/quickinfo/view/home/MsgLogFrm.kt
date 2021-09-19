package com.nikvik.quickinfo.view.home

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.FrmMsgLogBinding
import com.nikvik.quickinfo.global.common.GlobalUtility
import com.nikvik.quickinfo.global.common.gone
import com.nikvik.quickinfo.global.common.isInternetConnected
import com.nikvik.quickinfo.global.common.visible
import com.nikvik.quickinfo.model.MailRespo
import com.nikvik.quickinfo.model.pref.PreferenceBean
import com.nikvik.quickinfo.view.adapter.MessagesAdapter
import com.nikvik.quickinfo.view.base.BaseFragment
import com.prodege.shopathome.model.networkCall.ApiConstant

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */
class MsgLogFrm : BaseFragment(R.layout.frm_msg_log) {
    private var userInfo: PreferenceBean? = null
    private var mailRespo: MailRespo? = null
    private lateinit var mAdapter: MessagesAdapter
    private lateinit var mBinding: FrmMsgLogBinding
    private val db = FirebaseFirestore.getInstance()
    private val colRefUsers = db.collection(ApiConstant.COLLECTION_MESSAGE)
    private var msgList: java.util.ArrayList<MailRespo.DraftArray>? = ArrayList()

    companion object {
        val TAG = MsgLogFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): MsgLogFrm {
            val fragment = MsgLogFrm()
            fragment.arguments = bundle
            return MsgLogFrm()
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmMsgLogBinding
        init()
        clickListener()
        setAdapter()
    }

    private fun init() {
        userInfo = preferenceMgr.getUserInfo()
//        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.msg_log)
        getMsg()
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private fun setAdapter() {
        mAdapter = MessagesAdapter(this, ArrayList())
        mBinding.rvMessageLog.layoutManager = LinearLayoutManager(activity)
        mBinding.rvMessageLog.adapter = mAdapter
    }

    private fun getMsg() {
        if (!activity?.isInternetConnected()!!) return
        mBinding.shimmerViewContainer.visible()
        mBinding.imgNoDataFound.gone()
        colRefUsers.document(ApiConstant.DOCUMENT_MESSAGE_LOG).get().addOnSuccessListener {
            mBinding.shimmerViewContainer.gone()
            if (it.exists() && it.data?.size!! > 0) {
                mailRespo = it.toObject(MailRespo::class.java)
                filterData(mailRespo?.message_array)
            } else {
                mBinding.imgNoDataFound.visible()
                mBinding.rvMessageLog.gone()
                GlobalUtility.showToast(getString(R.string.txt_no_data_found))
            }
        }.addOnFailureListener { e ->
            mBinding.shimmerViewContainer.gone()
            mBinding.imgNoDataFound.visible()
            mBinding.rvMessageLog.gone()
            GlobalUtility.showToast(getString(R.string.something_went_wrong) + e.message)
        }
    }

    private fun filterData(messageArray: java.util.ArrayList<MailRespo.DraftArray>?) {
        for (msg in messageArray!!) {
            if ((msg.tag1 == userInfo?.designation
                        || msg.tag2 == userInfo?.designation
                        || msg.tag3 == userInfo?.designation
                        || msg.tag4 == userInfo?.designation
                        || msg.tag5 == userInfo?.designation
                        ) && msg.district_name == userInfo?.destrict
            ) {
                msgList?.add(msg)
            }
        }
        if (msgList?.size!! > 0) {
            mBinding.imgNoDataFound.gone()
            mBinding.rvMessageLog.visible()
        } else {
            mBinding.imgNoDataFound.visible()
            mBinding.rvMessageLog.gone()
        }
        mAdapter.notifyAdapter(msgList)
    }

    fun rowClickEvent(msgInfo: MailRespo.DraftArray?) {
        (activity as HomeActivity).navigateScreen(MsgLogDetailsFrm.TAG.toString(), msgInfo)
    }


}

