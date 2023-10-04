package com.snow.diary.feature.importing.screen.config;

import com.snow.diary.core.io.ImportFiletype

sealed class ImportConfigEvent {

    data class SelectType(
        val type: ImportFiletype
    ): ImportConfigEvent()

}