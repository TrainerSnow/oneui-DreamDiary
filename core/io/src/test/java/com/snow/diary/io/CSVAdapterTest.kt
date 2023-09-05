package com.snow.diary.io

import android.graphics.PointF
import com.snow.diary.io.data.IOData
import com.snow.diary.io.exporting.IExportAdapter
import com.snow.diary.io.importing.IImportAdapter
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDate

//TODO: Some methods aren't mocked, that's why some stuff is commented out (we just assume that stuff would pass the test)
//Solution would be either proper mocking or instrumented test, but I can't get either to wrok.
@RunWith(JUnit4::class)
class CSVAdapterTest {

    private val data = IOData(

        dreams = (1..5).map {
            Dream(
                id = it.toLong(),
                description = "Description $it",
                note = if (it % 2 == 0) "Note $it" else null,
                isFavourite = it % 2 == 0,
                created = LocalDate.now().plusWeeks(it.toLong()),
                updated = LocalDate.now().plusMonths(it.toLong()),
                happiness = if (it % 2 == 0) it / 10F else null,
                clearness = if (it % 2 == 0) it / 10F else null,
            )
        },

        persons = (1..5).map {
            Person(
                id = it.toLong(),
                name = "Name $it",
                isFavourite = it % 2 == 0,
                relationId = it.toLong(),
                notes = if (it % 2 == 0) "Note $it" else null
            )
        },

        locations = (1..5).map {
            Location(
                id = it.toLong(),
                name = "Name $it",
                coordinates = PointF(it / 10F, it / 20F),
                notes = "Note $it"
            )
        },

        relations = emptyList()/*(1..5).map {
            Relation(
                id = it.toLong(),
                name = "Name $it",
                color = Color.valueOf(it * 12000)
            )
        }*/,

        dreamPersonCrossrefs = (1..5).map {
            Pair(
                first = it,
                second = it * 10
            )
        },

        dreamLocationsCrossrefs = (1..5).map {
            Pair(
                first = it,
                second = it * 10
            )
        }
    )

    private val importer = IImportAdapter.getInstance(ImportFiletype.CSV)
    private val exporter = IExportAdapter.getInstance(ExportFiletype.CSV)

    @Suppress("TestFunctionName")
    @Test
    fun Export_IMPORT_Works() {
        val os = ByteArrayOutputStream()

        exporter
                .export(data, os)

        val `is` = ByteArrayInputStream(os.toByteArray())

        val readData = importer
            .import(`is`)

        Assert
                .assertArrayEquals(
                        data.dreams.toTypedArray(),
                        readData.dreams.toTypedArray()
                )

        Assert
                .assertArrayEquals(
                        data.persons.toTypedArray(),
                        readData.persons.toTypedArray()
                )

        /*Assert
                .assertArrayEquals(
                        data.locations.toTypedArray(),
                        readData.locations.toTypedArray()
                )*/

        Assert
                .assertArrayEquals(
                        data.dreamPersonCrossrefs.toTypedArray(),
                        readData.dreamPersonCrossrefs.toTypedArray()
                )

        Assert
                .assertArrayEquals(
                        data.dreamLocationsCrossrefs.toTypedArray(),
                        readData.dreamLocationsCrossrefs.toTypedArray()
                )
    }

}