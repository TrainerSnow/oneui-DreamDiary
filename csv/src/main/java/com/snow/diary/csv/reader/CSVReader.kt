package com.snow.diary.csv.reader

import com.snow.diary.csv.Row
import com.snow.diary.csv.Rows
import com.snow.diary.csv.config.CSVConfig
import com.snow.diary.csv.config.csvConfig
import com.snow.diary.csv.util.base64decode
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.properties.Delegates

class CSVReader(
        `is`: InputStream,
        config: CSVConfig = csvConfig { }
) {

    private var rows: Rows by Delegates.notNull()

    init {
        val content = `is`.readAsString()
        rows = content
                .split(config.rowDelimiter)
                .map { strRow ->
                    strRow
                            .split(config.valueDelimiter)
                }.map { row ->
                    row.map {
                        if (it == config.nullPlaceholder)
                            null
                        else if (config.encodeBase64)
                            it.base64decode(config.charset)
                        else
                            it
                    }
                }
    }

    fun readRows(): Rows = rows

    fun readRow(index: Int): Row = rows[index]

    private fun InputStream.readAsString(
            charset: Charset = Charsets.UTF_8
    ): String {
        val all = readAllBytes()
        return all.decodeToString()
    }

}