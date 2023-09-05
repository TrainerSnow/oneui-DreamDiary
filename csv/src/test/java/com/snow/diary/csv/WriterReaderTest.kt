package com.snow.diary.csv

import com.snow.diary.csv.config.csvConfig
import com.snow.diary.csv.reader.CSVReader
import com.snow.diary.csv.writer.CSVWriter
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class WriterReaderTest {

    private val rows = listOf(
            listOf("Eins", "Zwei", "Drei", "Vier", "Fünf"),
            listOf("Sechs", "Sieben"),
            listOf("Acht"),
            listOf(""),
            listOf("Neun", "Zehn", "Elf", "Zwölf", "Dreizehn", "Vierzehn", "Fünfzehn")
    )

    private val nullableRows = listOf(
            listOf("Eins", "Zwei", "Drei", null, "Vier", "Fünf", null),
            listOf("Sechs", "Sieben", null),
            listOf("Acht"),
            listOf(null),
            listOf("Neun", null, null, "Zehn", "Elf", "Zwölf", null, "Dreizehn", "Vierzehn", null, "Fünfzehn", null)
    )

    @Test
    fun `Writer works as expected`() {
        val os = ByteArrayOutputStream()

        val writer = CSVWriter(os)

        writer.writeAndCommit {
            write(rows)
        }

        val bytes = os.toByteArray()
        val string = bytes.decodeToString()

        val crlf = System.lineSeparator()

        Assert.assertEquals(
                "Eins,Zwei,Drei,Vier,Fünf" + crlf +
                "Sechs,Sieben" + crlf +
                "Acht" + crlf +
                "" + crlf +
                "Neun,Zehn,Elf,Zwölf,Dreizehn,Vierzehn,Fünfzehn",
                string
        )
    }

    @Test
    fun `Reader works as expected`() {
        val crlf = System.lineSeparator()
        val csv = "Eins,Zwei,Drei,Vier,Fünf" + crlf +
                "Sechs,Sieben" + crlf +
                "Acht" + crlf +
                "" + crlf +
                "Neun,Zehn,Elf,Zwölf,Dreizehn,Vierzehn,Fünfzehn"

        val `is` = ByteArrayInputStream(csv.toByteArray())

        val reader = CSVReader(`is`)

        val readRows = reader.readRows()
        Assert.assertEquals(
                rows,
                readRows
        )
    }

    @Test
    fun `Reader and Writer work together as expected`() {
        val os = ByteArrayOutputStream()

        val writer = CSVWriter(os)

        writer.writeAndCommit {
            write(rows)
        }
        val bytes = os.toByteArray()

        val `is` = ByteArrayInputStream(bytes)
        val reader = CSVReader(`is`)
        val readRows = reader.readRows()

        Assert.assertEquals(
                rows,
                readRows
        )
    }

    @Test
    fun `Reader and Writer work together as expected with Base64 encoding`() {
        val os = ByteArrayOutputStream()
        val config = csvConfig {
            encodeBase64 = true
        }

        val writer = CSVWriter(os, config)

        writer.writeAndCommit {
            write(rows)
        }
        val bytes = os.toByteArray()

        val `is` = ByteArrayInputStream(bytes)
        val reader = CSVReader(`is`, config)
        val readRows = reader.readRows()

        Assert.assertEquals(
                rows,
                readRows
        )
    }

    @Test
    fun `Reader and Writer work together as expected with nullability`() {
        val os = ByteArrayOutputStream()

        val writer = CSVWriter(os)

        writer.writeAndCommit {
            write(nullableRows)
        }
        val bytes = os.toByteArray()

        val `is` = ByteArrayInputStream(bytes)
        val reader = CSVReader(`is`)
        val readRows = reader.readRows()

        Assert.assertEquals(
                nullableRows,
                readRows
        )
    }

    @Test
    fun `Reader and Writer work together as expected with nullability and Base64 encoding`() {
        val os = ByteArrayOutputStream()
        val config = csvConfig {
            encodeBase64 = true
        }

        val writer = CSVWriter(os, config)

        writer.writeAndCommit {
            write(nullableRows)
        }
        val bytes = os.toByteArray()

        val `is` = ByteArrayInputStream(bytes)
        val reader = CSVReader(`is`, config)
        val readRows = reader.readRows()

        Assert.assertEquals(
                nullableRows,
                readRows
        )
    }
}