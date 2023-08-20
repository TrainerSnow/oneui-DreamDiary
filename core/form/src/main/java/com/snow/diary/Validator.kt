package com.snow.diary

import android.content.Context
import com.snow.diary.form.FormRule

object Validator {

    fun validate(rules: FormRule, context: Context, str: String): TextInput {
        val iter = rules.rules.iterator()

        var error: String? = null

        while (iter.hasNext()) {
            val rule = iter.next()
            val pass = rule.isValid(str)

            if (!pass) {
                error = rule.error(context)
                break
            }
        }

        return TextInput(str, error)
    }

}