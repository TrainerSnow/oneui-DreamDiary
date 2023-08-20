package com.snow.diary.rules

import com.snow.diary.builder.createFieldRules

object Rules {

    val DreamContent = createFieldRules {
        exists()
    }

    val DreamNote = createFieldRules { }

}