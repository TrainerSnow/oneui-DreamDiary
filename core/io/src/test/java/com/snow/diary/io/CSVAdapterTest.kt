package com.snow.diary.io

import com.snow.diary.io.data.IOData
import com.snow.diary.io.exporting.IExportAdapter
import com.snow.diary.io.importing.IImportAdapter
import com.snow.diary.model.data.Coordinates
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDate

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
                coordinates = Coordinates(it / 10F, it / 20F),
                notes = "Note $it"
            )
        },

        relations = (1..5).map {
            Relation(
                id = it.toLong(),
                name = "Name $it",
                color = it * 12000
            )
        },

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

    @Test
    fun `Export and import works as expected`() {
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

        Assert
                .assertArrayEquals(
                        data.locations.toTypedArray(),
                        readData.locations.toTypedArray()
                )

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