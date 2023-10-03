package com.snow.diary.core.io

enum class ExportFiletype(
    val mimeType: String,
    val fileExtension: String
) {

    JSON("application/json", ".json")

}


enum class ImportFiletype(
    val mimeType: String,
    val fileExtension: String
) {

    JSON("application/json", ".json")

}