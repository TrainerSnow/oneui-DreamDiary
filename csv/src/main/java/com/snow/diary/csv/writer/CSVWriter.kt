package com.snow.diary.csv.writer;

import com.snow.diary.csv.Row
import com.snow.diary.csv.Rows
import com.snow.diary.csv.config.CSVConfig
import com.snow.diary.csv.config.csvConfig
import com.snow.diary.csv.util.base64encode
import java.io.OutputStream

class CSVWriter(
        private val os: OutputStream,
        private val config: CSVConfig = csvConfig { }
) {

    /**
     * Variable that stores the rows until the writer is commited
     */
    private val rows = mutableListOf<Row>()

    @JvmName("writeRow")
    fun write(row: Row) = rows.add(row)

    @JvmName("writeRows")
    fun write(rows: Rows) = this.rows.addAll(rows)

    fun writeAndCommit(
            scope: CSVWriter.() -> Unit
    ) {
        scope()
        commit()
    }


    /**
     * Writes the saved rows into the stream and closes it
     */
    fun commit() {
        os.write(
                rows
                        .toContent()
                        .toByteArray(config.charset)
        )
        os.flush()
        os.close()
    }

    private fun Rows.toContent(): String =
            joinToString(config.rowDelimiter) { row ->
                row.joinToString(config.valueDelimiter) {
                    if (it == null) config.nullPlaceholder else
                        if (config.encodeBase64) {
                            it.base64encode(config.charset)
                        } else it
                }
            }

}