package com.snow.diary.csv.config

import java.nio.charset.Charset

data class CSVConfig(

        val valueDelimiter: String,

        val rowDelimiter: String,

        val encodeBase64: Boolean,

        val nullPlaceholder: String,

        val charset: Charset
)

class CSVConfigScope {

    var valueDelimiter = ","

    var rowDelimiter = System.lineSeparator()

    var encodeBase64 = false

    var nullPlaceholder = "{NULL}"

    var charset = Charsets.UTF_8

    fun build(): CSVConfig = CSVConfig(
            valueDelimiter, rowDelimiter, encodeBase64, nullPlaceholder, charset
    )

}

fun csvConfig(
        scope: CSVConfigScope.() -> Unit
): CSVConfig = CSVConfigScope().apply(scope).build()