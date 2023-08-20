package com.snow.diary.internal;

import android.content.Context
import com.snow.diary.form.R
import com.snow.diary.form.Rule

object IntegerRule : Rule {
    override fun isValid(str: String?): Boolean =
        try {
            str?.toInt()
            true
        } catch (_: NumberFormatException) {
            false
        }

    override fun error(context: Context): String =
        context
            .resources
            .getString(R.string.form_integer_error)
}