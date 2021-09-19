package com.nikvik.quickinfo.view.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import com.nikvik.quickinfo.view.base.BaseAdapter
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.RowDraftBinding
import com.nikvik.quickinfo.model.MailRespo
import com.nikvik.quickinfo.view.home.DraftFrm

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */
class DraftAdapter(
    private var draftFrm: DraftFrm,
    private var dataResultList: ArrayList<MailRespo.DraftArray>?
) : BaseAdapter() {
    override fun getListSize(): Int {
        if (dataResultList == null) return 0
        return dataResultList?.size!!
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_draft
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowDraftBinding) {
            val source = dataResultList?.get(position)
            rowBinding.txtInitial.text = source?.designation
            rowBinding.txtBref.text = source?.incident
            rowBinding.txtDate.text = source?.incident_date
//            val stringBuilder =
//                "https://besticon-demo.herokuapp.com/icon?url=" + source?.url + "&size=64..64..120"
//            rowBinding.imgChannelImg.showImage(stringBuilder, getPlaceHolder(0))
//            onClickListener(rowBinding, rowBinding.cardview, position)
            onClickListener(rowBinding.parent, position)
        }
    }

    override fun getClickEvent(view: View?, position: Int) {
        super.getClickEvent(view, position)
        when(view?.id){
            R.id.parent->draftFrm.rowClickEvent(dataResultList?.get(position))
        }
    }

    fun notifyAdapter(beanList: java.util.ArrayList<MailRespo.DraftArray>?) {
        dataResultList = beanList
        notifyDataSetChanged()
    }
}