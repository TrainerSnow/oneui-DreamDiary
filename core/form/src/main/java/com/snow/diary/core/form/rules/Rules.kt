package com.snow.diary.core.form.rules

import com.snow.diary.core.form.builder.createFieldRules

object Rules {

    val DreamContent = createFieldRules {
        exists()
    }

    val DreamNote = createFieldRules { }

}