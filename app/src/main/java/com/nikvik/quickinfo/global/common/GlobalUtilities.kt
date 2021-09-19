package com.nikvik.quickinfo.global.common

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.nikvik.quickinfo.R
import com.nikvik.quickinfo.global.common.AppApplication.Companion.context
import com.nikvik.quickinfo.model.NotificationData
import com.nikvik.quickinfo.view.home.HomeActivity
import java.lang.reflect.Modifier
import java.util.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class GlobalUtility {

    companion object {

        fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun getDate(context: Context, mDobEtm: TextView) {
            val datePickerDialog = DatePickerDialog(
                context,
                R.style.TimePicker,
                { view, year, month, dayOfMonth ->
                    val monthValue = month + 1
                    val day: String = if (dayOfMonth <= 9) "0$dayOfMonth"
                    else dayOfMonth.toString()
                    val dayMonth: String = if (monthValue <= 9) "0$monthValue"
                    else monthValue.toString()
                    mDobEtm.text = "$day/$dayMonth/$year"
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePickerDialog.show()
        }
//    {START HIDE SHOW KEYBOARD}

        /**
         * Method to hide keyboard
         *
         * @param activity Context of the calling class
         */
        fun hideKeyboard(activity: Activity) {
            try {
                val inputManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            } catch (ignored: Exception) {
                Log.d("TAG", "hideKeyboard: " + ignored.message)
            }

        }

        /***
         * Show SoftInput Keyboard
         * @param activity reference of current activity
         */
        fun showKeyboard(activity: Activity?) {
            if (activity != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
        }

//    {END HIDE SHOW KEYBOARD}


        //block up when loder show on screen
        /**
         * handle ui
         *
         * @param activity
         * @param view
         * @param isBlockUi
         */
        fun handleUI(activity: Activity, view: View, isBlockUi: Boolean) {
            if (isBlockUi) {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                view.visibility = View.VISIBLE
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                view.visibility = View.GONE
            }
        }

        /**
         * button click fade animation
         *
         * @param view view reference
         */
        fun btnClickAnimation(view: View) {
            val fadeAnimation = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
            view.startAnimation(fadeAnimation)
        }

        fun changeLanguage(context: Context, lancuageCode: String) {
//            val languageToLoad = "en" // your language
            val locale = Locale(lancuageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
        }
        fun <T> stringToJson(json: String, clazz: Class<T>): T {
            return GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create().fromJson(json, clazz)
        }
        fun getIntentForPush(
            ctx: Context,
            mNotificationData: NotificationData?
        ): Intent? {
            var mIntent: Intent? = null
            if (mNotificationData != null) {
//                if (mPrefMgr.getUserId() != null && !mPrefMgr.getUserId().isEmpty()) {
                mIntent = Intent(ctx, HomeActivity::class.java)
//                    mIntent.putExtra(
//                        AppConstant.NOTIFICATION_GAME_ID,
//                        String.valueOf(mNotificationData!!.getId())
//                    )
//                    mIntent.putExtra(AppConstant.NOTIFICATION_CODE, mNotificationData!!.getType())
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                }
            }
            return mIntent
        }
        fun getTwoDigitRandomNo(): Int {
            return Random().nextInt(90) + 10
        }
    }
}