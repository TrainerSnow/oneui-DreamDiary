package com.snow.diary.core.form

import android.content.Context
import com.snow.diary.core.form.field.FormRule

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