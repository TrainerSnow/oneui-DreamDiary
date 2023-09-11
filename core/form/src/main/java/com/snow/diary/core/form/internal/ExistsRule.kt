package com.snow.diary.core.form.internal;

import android.content.Context
import com.snow.diary.core.form.R
import com.snow.diary.core.form.field.Rule

object ExistsRule : Rule {
    override fun isValid(str: String?): Boolean =
        !str.isNullOrBlank()

    override fun error(context: Context): String =
        context
            .resources
            .getString(R.string.form_lenght_error)
}