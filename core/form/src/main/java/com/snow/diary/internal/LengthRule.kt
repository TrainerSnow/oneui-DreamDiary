package com.snow.diary.internal;

import android.content.Context
import com.snow.diary.form.R
import com.snow.diary.form.Rule

class LengthRule(
    private val min: Int,
    private val max: Int
) : Rule {
    override fun isValid(str: String?): Boolean =
        (str?.length ?: 0) in min..max

    override fun error(context: Context): String =
        context
            .resources
            .getString(R.string.form_lenght_error)
            .format(min, max)
}