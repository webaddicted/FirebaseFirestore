package com.nikvik.quickinfo.view.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nikvik.quickinfo.global.common.AppApplication

abstract class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected val mContext = AppApplication.context
    protected abstract fun getListSize(): Int

    companion object {
        private val TAG = BaseAdapter::class.qualifiedName
    }

    override fun getItemCount(): Int {
        return getListSize()
    }

    protected abstract fun getLayoutId(viewType: Int): Int

    protected abstract fun onBindTo(rowBinding: ViewDataBinding, position: Int)

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowBindingUtil: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutId(viewType),
            parent,
            false
        )
        return ViewHolder(rowBindingUtil)
    }

    override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.binding(position)
    }

    /**
     * view holder
     */
    inner class ViewHolder(private val mRowBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(mRowBinding.root) {
        /**
         * @param position current item position
         */
        fun binding(position: Int) {
            //            sometime adapter position  is -1 that case handle by position
            if (bindingAdapterPosition >= 0) onBindTo(mRowBinding, bindingAdapterPosition)
            else onBindTo(mRowBinding, position)
        }
    }

    protected open fun onClickListener(view: View?, position: Int){
        view?.setOnClickListener { getClickEvent(view, position)}
    }

    protected open fun getClickEvent(view: View?, position: Int) {

    }

}