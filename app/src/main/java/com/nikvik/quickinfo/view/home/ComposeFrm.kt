package com.nikvik.quickinfo.view.home

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.FrmComposeBinding
import com.nikvik.quickinfo.global.common.DialogUtil
import com.nikvik.quickinfo.global.common.GlobalUtility
import com.nikvik.quickinfo.global.common.ValidationHelper
import com.nikvik.quickinfo.global.common.isInternetConnected
import com.nikvik.quickinfo.model.DraftReq
import com.nikvik.quickinfo.model.MailRespo
import com.nikvik.quickinfo.model.MessageReq
import com.nikvik.quickinfo.model.pref.PreferenceBean
import com.nikvik.quickinfo.view.base.BaseFragment
import com.prodege.shopathome.model.networkCall.ApiConstant.Companion.COLLECTION_MESSAGE
import com.prodege.shopathome.model.networkCall.ApiConstant.Companion.DOCUMENT_DRAFT_MSG
import com.prodege.shopathome.model.networkCall.ApiConstant.Companion.DOCUMENT_MESSAGE_LOG
import com.prodege.shopathome.model.networkCall.ApiConstant.Companion.FIELD_DRAFT_ARRAY

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */
class ComposeFrm : BaseFragment(R.layout.frm_compose) {
    private var draftInfo: MailRespo.DraftArray? = null
    private var userInfo: PreferenceBean? = null
    private var mailRespo: MailRespo? = MailRespo()
    private var desigBean: List<MailRespo.TagArray>? = null
    private lateinit var mBinding: FrmComposeBinding
    private val db = FirebaseFirestore.getInstance()
    private val colRefUsers = db.collection(COLLECTION_MESSAGE)

    companion object {
        val TAG = ComposeFrm::class.qualifiedName
        const val DRAFT_RESPO = "DRAFT_RESPO"

        fun getInstance(bundle: Bundle): ComposeFrm {
            val fragment = ComposeFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmComposeBinding
        init()
        clickListener()
    }

    private fun init() {
        if (arguments?.containsKey(DRAFT_RESPO)!!) {
            draftInfo =
                arguments?.getSerializable(DRAFT_RESPO) as MailRespo.DraftArray

            mBinding.edtIncidentDate.setText(draftInfo?.incident_date)
            mBinding.edtIncidentBrif.setText(draftInfo?.incident)
            mBinding.edtActionToken.setText(draftInfo?.action_token)
            mBinding.edtOtherInfo.setText(draftInfo?.other_info)
            mBinding.edtStand1.setText(draftInfo?.tag1)
            mBinding.edtStand2.setText(draftInfo?.tag2)
            mBinding.edtStand3.setText(draftInfo?.tag3)
            mBinding.edtStand4.setText(draftInfo?.tag4)
            mBinding.edtStand5.setText(draftInfo?.tag5)
        }
        userInfo = preferenceMgr.getUserInfo()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.compose)


        getUserInfo()
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.edtIncidentDate.setOnClickListener(this)
        mBinding.edtStand1.setOnClickListener(this)
        mBinding.edtStand2.setOnClickListener(this)
        mBinding.edtStand3.setOnClickListener(this)
        mBinding.edtStand4.setOnClickListener(this)
        mBinding.edtStand5.setOnClickListener(this)
        mBinding.btnSubmit.setOnClickListener(this)
        mBinding.txtSaveDraft.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.edt_incident_date -> GlobalUtility.getDate(mActivity, mBinding.edtIncidentDate)
            R.id.edt_stand1 -> getStandData(mBinding.edtStand1)
            R.id.edt_stand2 -> getStandData(mBinding.edtStand2)
            R.id.edt_stand3 -> getStandData(mBinding.edtStand3)
            R.id.edt_stand4 -> getStandData(mBinding.edtStand4)
            R.id.edt_stand5 -> getStandData(mBinding.edtStand5)
            R.id.btn_submit -> validate()
            R.id.txt_save_draft -> getDataList(DOCUMENT_DRAFT_MSG)
        }
    }

    private fun getUserInfo() {
        if (mActivity.isInternetConnected()) return
        colRefUsers.get().addOnSuccessListener {
            if (!it.isEmpty) {
                desigBean =
                    it.documents[0].toObject(MailRespo::class.java)?.tag_array
            } else GlobalUtility.showToast(getString(R.string.txt_no_data_found))

        }.addOnFailureListener { e ->
            GlobalUtility.showToast(getString(R.string.something_went_wrong) + e.message)
        }
    }

    private fun validate() {
        if (!ValidationHelper.validateBlank(
                mBinding.edtIncidentDate,
                mBinding.wrapperIncidentDate,
                getString(R.string.select_incident_date)
            ) ||
            !ValidationHelper.validateBlank(
                mBinding.edtIncidentBrif,
                mBinding.wrapperIncidentBrif,
                getString(R.string.enter_incident_brief)
            ) ||
            !ValidationHelper.validateBlank(
                mBinding.edtActionToken,
                mBinding.wrapperActionToken,
                getString(R.string.enter_action_token)
            )
            ||
            !ValidationHelper.validateBlank(
                mBinding.edtStand1,
                mBinding.wrapperStand1,
                getString(R.string.select_stand)
            )
//            !ValidationHelper.validateBlank(
//                mBinding.edtStand2,
//                mBinding.wrapperStand2,
//                getString(R.string.select_stand2)
//            ) ||
//            !ValidationHelper.validateBlank(
//                mBinding.edtStand3,
//                mBinding.wrapperStand3,
//                getString(R.string.select_stand3)
//            ) ||
//            !ValidationHelper.validateBlank(
//                mBinding.edtStand4,
//                mBinding.wrapperStand4,
//                getString(R.string.select_stand4)
//            ) ||
//            !ValidationHelper.validateBlank(
//                mBinding.edtStand5,
//                mBinding.wrapperStand5,
//                getString(R.string.select_stand5)
//            )
        ) return
        getDataList(DOCUMENT_MESSAGE_LOG)
    }

    private fun getDataList(docName: String) {
        if (mActivity.isInternetConnected()) return
        showApiLoader()
        colRefUsers.document(docName).get().addOnSuccessListener {
            hideApiLoader()
            if (it.exists())
                mailRespo = it.toObject(MailRespo::class.java)
            if (docName == DOCUMENT_DRAFT_MSG) {
                mailRespo?.draft_array?.add(getDetails())
                val draftReq = DraftReq()
                draftReq.draft_array = mailRespo?.draft_array
                addInDraft(draftReq)
            } else if (docName == DOCUMENT_MESSAGE_LOG) {
                if (mailRespo?.message_array != null) mailRespo?.message_array?.add(getDetails())
                else {
                    val msg = ArrayList<MailRespo.DraftArray>()
                    msg.add(getDetails())
                    mailRespo?.message_array = msg
                }
                val messageReq = MessageReq()
                messageReq.message_array = mailRespo?.message_array
                callSubmitApi(messageReq)
            }
        }.addOnFailureListener { e ->
            hideApiLoader()
            GlobalUtility.showToast(getString(R.string.something_went_wrong) + e.message)
        }
    }

    private fun callSubmitApi(messageReq: MessageReq) {
        showApiLoader()
        colRefUsers.document(DOCUMENT_MESSAGE_LOG).set(messageReq).addOnCompleteListener {
            if (arguments?.containsKey(DRAFT_RESPO)!! && draftInfo != null) {
                colRefUsers.document(DOCUMENT_DRAFT_MSG)
                    .update(FIELD_DRAFT_ARRAY, FieldValue.arrayRemove(draftInfo))
            }
            val details = getDetails()
            (mActivity as HomeActivity).sendPush(
                userInfo?.designation!!,
                details.incident,
                details.tag1,
                userInfo?.destrict!!
            )
            (mActivity as HomeActivity).sendPush(
                userInfo?.designation!!,
                details.incident,
                details.tag2,
                userInfo?.destrict!!
            )
            (mActivity as HomeActivity).sendPush(
                userInfo?.designation!!,
                details.incident,
                details.tag3,
                userInfo?.destrict!!
            )
            (mActivity as HomeActivity).sendPush(
                userInfo?.designation!!,
                details.incident,
                details.tag4,
                userInfo?.destrict!!
            )
            (mActivity as HomeActivity).sendPush(
                userInfo?.designation!!,
                details.incident,
                details.tag5,
                userInfo?.destrict!!
            )
            hideApiLoader()
            showMessage(getString(R.string.success), getString(R.string.message_successfully_sent))
        }.addOnFailureListener {
            hideApiLoader()
            GlobalUtility.showToast(getString(R.string.something_went_wrong) + it.message)
        }.addOnCanceledListener {
            hideApiLoader()
            GlobalUtility.showToast(getString(R.string.something_went_wrong))
        }
    }


    private fun addInDraft(draftList: DraftReq) {
        showApiLoader()
        colRefUsers.document(DOCUMENT_DRAFT_MSG).set(draftList).addOnCompleteListener {
            if (arguments?.containsKey(DRAFT_RESPO)!! && draftInfo != null) {
                colRefUsers.document(DOCUMENT_DRAFT_MSG)
                    .update(FIELD_DRAFT_ARRAY, FieldValue.arrayRemove(draftInfo))
            }
            hideApiLoader()
            showMessage(getString(R.string.success), getString(R.string.message_successfully_save))
        }.addOnFailureListener {
            hideApiLoader()
            GlobalUtility.showToast(getString(R.string.something_went_wrong) + it.message)
        }.addOnCanceledListener {
            hideApiLoader()
            GlobalUtility.showToast(getString(R.string.something_went_wrong))
        }
    }

    private fun showMessage(title: String, msg: String) {
        val dialog = DialogUtil.showOkDialog(
            mActivity,
            title,
            msg,
            getString(R.string.ok)
        ) { dialog, which ->
            (mActivity as HomeActivity).openFragment(getInstance(Bundle()))
            dialog.dismiss()
        }
        dialog.setCancelable(false)
    }

    private fun getStandData(edtStand1: TextInputEditText) {
        if (desigBean != null) {
            desigBean?.let {
                DialogUtil.showListDialog(mActivity,
                    resources.getString(R.string.select_desig),
                    it
                ) { dialog, which ->
                    edtStand1.setText(desigBean!![which].name)
                    dialog.dismiss()
                }
            }
        } else
            GlobalUtility.showToast(getString(R.string.something_went_wrong))
    }

    fun getDetails(): MailRespo.DraftArray {
        val mailRespo = MailRespo.DraftArray()
        mailRespo.apply {
            designation = userInfo?.designation!!
            incident_date = mBinding.edtIncidentDate.text.toString()
            incident = mBinding.edtIncidentBrif.text.toString()
            action_token = mBinding.edtActionToken.text.toString()
            other_info = mBinding.edtOtherInfo.text.toString()
            tag1 = mBinding.edtStand1.text.toString()
            tag2 = mBinding.edtStand2.text.toString()
            tag3 = mBinding.edtStand3.text.toString()
            tag4 = mBinding.edtStand4.text.toString()
            tag5 = mBinding.edtStand5.text.toString()
            unit_name = userInfo?.unit!!
            district_name = userInfo?.destrict!!
        }
        return mailRespo
    }

}

