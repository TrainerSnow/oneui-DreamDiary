package com.snow.diary.builder;

import com.snow.diary.form.FormRule
import com.snow.diary.form.Rule
import com.snow.diary.internal.ExistsRule
import com.snow.diary.internal.FloatRule
import com.snow.diary.internal.IntegerRule
import com.snow.diary.internal.LengthRule

class FormBuilderScope {

    val form = FormRule()

    private fun add(rule: Rule) = form
        .rules
        .add(rule)

    fun length(
        max: Int = Int.MAX_VALUE,
        min: Int = 0
    ) = add(
        LengthRule(
            max, min
        )
    )

    fun minLength(
        min: Int
    ) = length(min = min)

    fun maxLength(
        max: Int
    ) = length(max)

    fun exists() = add(ExistsRule)

    fun integer() = add(IntegerRule)

    fun float() = add(FloatRule)

}

fun createFieldRules(
    block: FormBuilderScope.() -> Unit
): FormRule =
    FormBuilderScope()
        .apply(block)
        .form