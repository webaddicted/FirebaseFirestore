package com.nikvik.quickinfo.global.common

import android.widget.TextView
import androidx.annotation.NonNull
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nikvik.quickinfo.R
import java.util.regex.Pattern

class ValidationHelper {
    companion object {
        fun isBlank(@NonNull targetEditText: TextView): Boolean {
            return targetEditText.text.toString().trim { it <= ' ' }.isEmpty()
        }
        /**
         * check password validation
         *
         * @param wrapperPsw
         * @param edtPassword password widget
         * @return password validation status
         */
        fun validatePwd(edtPassword: TextInputEditText, wrapperPsw: TextInputLayout): Boolean {
            if (isBlank(edtPassword)) {
                wrapperPsw.error = edtPassword.context.resources.getString(R.string.enter_passowrd)
            }
//            else if (!isValidPassword(
//                    wrapperPsw,
//                    edtPassword.getText().toString(),
//                    edtPassword.getContext().getResources().getString(R.string.password_length)
//                )
//            ) {
//            }
        else {
                wrapperPsw.error = null
                return true
            }
            return false
        }
        fun isValidPassword(
            layoutPassword: TextInputLayout,
            password: String,
            errorMsg: String
        ): Boolean {
            val upperCasePatten = Pattern.compile("[A-Z ]")
            val lowerCasePatten = Pattern.compile("[a-z ]")
            val digitCasePatten = Pattern.compile("[0-9 ]")
            if (!upperCasePatten.matcher(password).find()) {
                layoutPassword.error = layoutPassword.context.resources.getString(R.string.error_uppercase_pattern)
                return false
            } else if (!lowerCasePatten.matcher(password).find()) {
                layoutPassword.error = layoutPassword.context.resources.getString(R.string.error_lowercase_pattern)
                return false
            } else if (!digitCasePatten.matcher(password).find()) {
                layoutPassword.error = layoutPassword.context.resources.getString(R.string.error_digit_pattern)
                return false
            } else if (password.length < 8) {
                layoutPassword.error = errorMsg
                return false
            }
            layoutPassword.error = null
            return true

        }
        fun validateBlank(
            textInput: TextInputEditText,
            wrapper: TextInputLayout,
            enter_subject: String
        ): Boolean {
            if (isBlank(textInput)) {
                wrapper.error = enter_subject
            } else {
                wrapper.error = null
                return true
            }
            return false
        }
    }
}
