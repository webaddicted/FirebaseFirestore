package com.nikvik.quickinfo.global.common

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.view.interfac.AlertDialogListener


class DialogUtil {
    companion object {
        private val TAG = DialogUtil::class.qualifiedName
//    {START SHOW DIALOG STYLE}
        /**
         * show dialog with transprant background
         *
         * @param activity reference of activity
         * @param dialog   reference of dialog
         */
        fun modifyDialogBounds(activity: Activity, dialog: Dialog) {
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        activity,
                        android.R.color.transparent
                    )
                )
            )
            dialog.window!!.decorView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val lp = WindowManager.LayoutParams()
            val window = dialog.window
            lp.copyFrom(window!!.attributes)
            //This makes the dialog take up the full width
            //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.width = (dialog.context.resources.displayMetrics.widthPixels * 0.83).toInt()
            //  lp.height = (int) (dialog.getContext().getResources().getDisplayMetrics().heightPixels * 0.55);
            window.attributes = lp
        }

        fun alertFunction(
            context: Context?,
            title: String,
            messgae: String,
            btnOk: String,
            btnCancel: String,
            alertDialogListener: AlertDialogListener
        ): AlertDialog {
            val dialogAnimation = R.style.DialogFadeAnimation
            val builder = AlertDialog.Builder(context, R.style.AlertDialogStyle)
            builder.setCancelable(false)
            builder.setTitle(title)
            builder.setMessage(messgae)
            builder.setPositiveButton(btnOk) { dialog, which -> alertDialogListener.okClick() }
            builder.setNegativeButton(btnCancel) { dialog, which -> alertDialogListener.cancelClick() }
            val dialogs = builder.create()
            dialogs.window?.attributes?.windowAnimations = dialogAnimation
            dialogs.show()
            return dialogs
        }

        /**
         * @param context    referance of activity
         * @param title      title of dialog
         * @param items      list show in diloag
         * @param okListener ok click listener
         * @param <T>
         * @return dialog
        </T> */
        fun <T> showListDialog(
            context: Context,
            title: String,
            items: List<T>,
            okListener: DialogInterface.OnClickListener
        ): AlertDialog {
            return showListDialog(
                context,
                R.style.AlertDialogStyle,
                R.style.DialogSlideUpAnimation,
                title,
                items,
                okListener
            )
        }
        fun <T> showListDialog(
            context: Context,
            style: Int,
            dialogAnimation: Int,
            title: String?,
            items: List<T>,
            listener: DialogInterface.OnClickListener
        ): AlertDialog {
            val size = items.size
            val itemArray = arrayOfNulls<String>(size)
            for (i in 0 until size) {
                itemArray[i] = items[i].toString()
            }
            val builder = AlertDialog.Builder(context, style)
            if (title != null) builder.setTitle(title)
            builder.setItems(itemArray, listener)
            val alertDialog = builder.create()
            alertDialog.window?.attributes?.windowAnimations = dialogAnimation
            alertDialog.show()
            return alertDialog
        }
        fun showOkDialog(
            context: Context,
            title: String,
            msg: String,
            okBtn: String,
            okListener: DialogInterface.OnClickListener
        ): AlertDialog {
            return showOkDialog(
                context,
                R.style.AlertDialogStyle,
                R.style.DialogSlideUpAnimation,
                title,
                msg,
                0,
                true,
                okBtn,
                okListener
            )
        }
        fun showOkDialog(
            context: Context,
            style: Int,
            dialogAnimation: Int,
            title: String,
            msg: String,
            icon: Int,
            isCancelable: Boolean,
            okBtn: String,
            okListener: DialogInterface.OnClickListener
        ): AlertDialog {
            val alertDialog = AlertDialog.Builder(context, style).create()
            alertDialog.window!!.attributes.windowAnimations = dialogAnimation
            alertDialog.setTitle(title)
            alertDialog.setMessage(msg)
            if (icon > 0) alertDialog.setIcon(icon)
            alertDialog.setCancelable(isCancelable)
            alertDialog.setButton(okBtn, okListener)
            alertDialog.show()
            return alertDialog
        }
        fun showOkCancelDialog(
            context: Context,
            title: String,
            msg: String,
            okListener: DialogInterface.OnClickListener,
            cancelListener: DialogInterface.OnClickListener
        ): AlertDialog.Builder {
            return showOkCancelDialog(
                context,
                R.style.AlertDialogStyle,
                R.style.DialogSlideUpAnimation,
                title,
                msg,
                0,
                false,
                context.getString(R.string.exit),
                context.getString(R.string.cancel),
                okListener,
                cancelListener
            )
        }
        fun showOkCancelDialog(
            context: Context,
            style: Int,
            dialogAnimation: Int,
            title: String,
            msg: String,
            icon: Int,
            isCancelable: Boolean,
            okBtn: String,
            cancelBtn: String,
            okListener: DialogInterface.OnClickListener,
            cancelListener: DialogInterface.OnClickListener
        ): AlertDialog.Builder {
            val alertDialog = AlertDialog.Builder(context, style)
            alertDialog.setTitle(title)
            alertDialog.setMessage(msg)
            if (icon > 0) alertDialog.setIcon(icon)
            alertDialog.setCancelable(isCancelable)
            alertDialog.setPositiveButton(okBtn, okListener)
            alertDialog.setNegativeButton(cancelBtn, cancelListener)
            val alertDialogs = alertDialog.create()
            alertDialogs.window?.attributes?.windowAnimations = dialogAnimation
            alertDialog.show()
            return alertDialog
        }

    }
}
