package com.snow.diary.form

import android.content.Context

interface Rule {

    fun isValid(str: String?): Boolean

    fun error(context: Context): String

}