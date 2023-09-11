package com.snow.diary.core.form.field

import android.content.Context

interface Rule {

    fun isValid(str: String?): Boolean

    fun error(context: Context): String

}