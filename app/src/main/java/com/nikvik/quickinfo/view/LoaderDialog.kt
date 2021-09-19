package com.nikvik.quickinfo.view

import androidx.databinding.ViewDataBinding
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.databinding.DialogLoaderBinding
import com.nikvik.quickinfo.global.common.DialogUtil
import com.nikvik.quickinfo.view.base.BaseDialog

class LoaderDialog : BaseDialog(R.layout.dialog_loader) {
    private lateinit var mBinding: DialogLoaderBinding

    companion object {
        val TAG = LoaderDialog::class.qualifiedName
        fun dialog(): LoaderDialog {
            val dialog= LoaderDialog()
            return dialog
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as DialogLoaderBinding
    }

    override fun onResume() {
        super.onResume()
        dialog?.let { DialogUtil.modifyDialogBounds(requireActivity(), it) }
    }

}
