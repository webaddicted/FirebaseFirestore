package com.nikvik.quickinfo.view.home

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikvik.quickinfo.global.common.isInternetConnected
import com.google.firebase.firestore.FirebaseFirestore
import com.prodege.shopathome.model.networkCall.ApiConstant
import com.prodege.shopathome.model.networkCall.ApiConstant.Companion.DOCUMENT_DRAFT_MSG
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.FrmDraftBinding
import com.nikvik.quickinfo.global.common.GlobalUtility
import com.nikvik.quickinfo.global.common.gone
import com.nikvik.quickinfo.global.common.visible
import com.nikvik.quickinfo.model.MailRespo
import com.nikvik.quickinfo.model.pref.PreferenceBean
import com.nikvik.quickinfo.view.adapter.DraftAdapter
import com.nikvik.quickinfo.view.base.BaseFragment

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */
class DraftFrm : BaseFragment(R.layout.frm_draft) {
    private var userInfo: PreferenceBean? = null
    private var mailRespo: MailRespo? = null
    private lateinit var mBinding: FrmDraftBinding
    private lateinit var mAdapter: DraftAdapter
    private val db = FirebaseFirestore.getInstance()
    private val colRefUsers = db.collection(ApiConstant.COLLECTION_MESSAGE)
    private var draftList: java.util.ArrayList<MailRespo.DraftArray>? = ArrayList()

    companion object {
        val TAG = DraftFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): DraftFrm {
            val fragment = DraftFrm()
            fragment.arguments = bundle
            return DraftFrm()
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmDraftBinding
        init()
        clickListener()
        setAdapter()
    }

    private fun init() {
        userInfo = preferenceMgr.getUserInfo()
//        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.draft)
        getDraftList()
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> mActivity.onBackPressed()
        }
    }

    private fun setAdapter() {
        mAdapter = DraftAdapter(this, ArrayList())
        mBinding.rvDraft.layoutManager = LinearLayoutManager(mActivity)
        mBinding.rvDraft.adapter = mAdapter
    }

    private fun getDraftList() {
        if (mActivity.isInternetConnected()) return
        mBinding.shimmerViewContainer.visible()
        mBinding.imgNoDataFound.gone()
        colRefUsers.document(DOCUMENT_DRAFT_MSG).get().addOnSuccessListener {
            mBinding.shimmerViewContainer.gone()
            if (it.exists() && it.data?.size!! > 0) {
                mailRespo = it.toObject(MailRespo::class.java)
                filterData(mailRespo?.draft_array)
            } else {
                mBinding.imgNoDataFound.visible()
                mBinding.rvDraft.gone()
                GlobalUtility.showToast(getString(R.string.txt_no_data_found))
            }
        }.addOnFailureListener { e ->
            mBinding.shimmerViewContainer.gone()
            mBinding.imgNoDataFound.visible()
            mBinding.rvDraft.gone()
            GlobalUtility.showToast(getString(R.string.something_went_wrong) + e.message)
        }
    }

    private fun filterData(draftArray: java.util.ArrayList<MailRespo.DraftArray>?) {
        for (msg in draftArray!!) {
            if (msg.designation == userInfo?.designation && msg.district_name == userInfo?.destrict) draftList?.add(
                msg
            )
        }
        if (draftList?.size!! > 0) {
            mBinding.imgNoDataFound.gone()
            mBinding.rvDraft.visible()
        } else {
            mBinding.imgNoDataFound.visible()
            mBinding.rvDraft.gone()
        }
        mAdapter.notifyAdapter(draftList)

    }

    fun rowClickEvent(draftInfo: MailRespo.DraftArray?) {
        (mActivity as HomeActivity).openComposeFragment(draftInfo)
    }


}

