package com.nikvik.quickinfo.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.nikvik.quickinfo.global.common.GlobalUtility
import com.nikvik.quickinfo.global.sharedpref.PreferenceMgr
import com.nikvik.quickinfo.view.LoaderDialog
import org.koin.android.ext.android.inject

/**
 * Created by Deepak Sharma on 15/1/19.
 */
abstract class BaseFragment(private val layoutId: Int) : Fragment(), View.OnClickListener {
    private var loaderDialog: LoaderDialog? = null
    private lateinit var mBinding: ViewDataBinding
    protected val mActivity by lazy { requireActivity() }
    protected val preferenceMgr: PreferenceMgr by inject()
    protected abstract fun onBindTo(binding: ViewDataBinding?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBindTo(mBinding)
        super.onViewCreated(view, savedInstanceState)
        if (loaderDialog == null) {
            loaderDialog = LoaderDialog.dialog()
            loaderDialog?.isCancelable = false
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalUtility.hideKeyboard(mActivity)
    }

    protected fun navigateFragment(
        layoutContainer: Int,
        fragment: Fragment,
        isEnableBackStack: Boolean
    ) {
        (mActivity as BaseActivity).navigateFragment(layoutContainer, fragment, isEnableBackStack)
    }

    protected fun navigateAddFragment(
        layoutContainer: Int,
        fragment: Fragment,
        isEnableBackStack: Boolean
    ) {
        (mActivity as BaseActivity).navigateAddFragment(
            layoutContainer,
            fragment,
            isEnableBackStack
        )
    }

    protected fun navigateChildFragment(
        layoutContainer: Int,
        fragment: Fragment,
        isEnableBackStack: Boolean
    ) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(layoutContainer, fragment)
        if (isEnableBackStack)
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        fragmentTransaction.commitAllowingStateLoss()
    }

    protected fun showApiLoader() {
        if (loaderDialog != null) {
            val fragment = parentFragmentManager.findFragmentByTag(LoaderDialog.TAG)
            if (fragment != null) parentFragmentManager.beginTransaction().remove(fragment).commit()
            loaderDialog?.show(parentFragmentManager, LoaderDialog.TAG)
        }
    }

    protected fun hideApiLoader() {
        if (loaderDialog != null && loaderDialog?.isVisible!!) loaderDialog?.dismiss()
    }

    override fun onClick(v: View) {
//        GlobalUtility.Companion.btnClickAnimation(v)
    }
}
