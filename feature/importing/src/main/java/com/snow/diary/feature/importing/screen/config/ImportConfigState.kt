package com.snow.diary.feature.importing.screen.config

import com.snow.diary.core.io.ImportFiletype

data class ImportConfigState (

    val selectedType: ImportFiletype = ImportFiletype.JSON

)