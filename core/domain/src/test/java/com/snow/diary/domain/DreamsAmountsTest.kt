package com.snow.diary.domain;

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.model.DreamEntity
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.statistics.DreamAmounts
import com.snow.diary.core.domain.action.time.FirstDreamDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.LocalDate

@RunWith(JUnit4::class)
class DreamsAmountsTest {

    private lateinit var dreamDao: DreamDao
    private lateinit var allDreams: AllDreams
    private lateinit var firstDreamDate: FirstDreamDate

    //To test
    private lateinit var dreamAmounts: DreamAmounts

    @Before
    fun setup() {
        fun dummyDream(created: LocalDate): DreamEntity = DreamEntity(
            dreamId = created.hashCode().toLong(),
            description = "dummy",
            note = null,
            isFavourite = false,
            created = created,
            updated = LocalDate.now(),
            clearness = null,
            happiness = null
        )

        dreamDao = object : DreamDao {
            override fun insert(vararg entity: DreamEntity) = throw NotImplementedError()

            override fun update(vararg entity: DreamEntity) = throw NotImplementedError()

            override fun delete(vararg dream: DreamEntity) = throw NotImplementedError()

            override fun getById(id: Long): Flow<DreamEntity?> = throw NotImplementedError()

            override fun getDreamWithPersonsById(id: Long) = throw NotImplementedError()

            override fun getAllDreamsWithPersons() = throw NotImplementedError()

            override fun getDreamWithLocationsById(id: Long) = throw NotImplementedError()

            override fun getAllDreamsWithLocations() = throw NotImplementedError()

            override fun getAll(): Flow<List<DreamEntity>> = flowOf(
                listOf(
                    dummyDream(LocalDate.of(2023, 6, 1)),
                    dummyDream(LocalDate.of(2023, 6, 4)),
                    dummyDream(LocalDate.of(2023, 6, 8)),
                    dummyDream(LocalDate.of(2023, 6, 13)),
                    dummyDream(LocalDate.of(2023, 6, 18)),
                    dummyDream(LocalDate.of(2023, 6, 22)), //6 in June

                    dummyDream(LocalDate.of(2023, 7, 1)),
                    dummyDream(LocalDate.of(2023, 7, 4)),
                    dummyDream(LocalDate.of(2023, 7, 8)),
                    dummyDream(LocalDate.of(2023, 7, 9)),
                    dummyDream(LocalDate.of(2023, 7, 10)),
                    dummyDream(LocalDate.of(2023, 7, 11)),
                    dummyDream(LocalDate.of(2023, 7, 13)),
                    dummyDream(LocalDate.of(2023, 7, 18)),
                    dummyDream(LocalDate.of(2023, 7, 22)), //9 in July

                    dummyDream(LocalDate.of(2023, 8, 22)), //1 in August

                    dummyDream(LocalDate.of(2023, 9, 22)),
                    dummyDream(LocalDate.of(2023, 9, 22)),
                    dummyDream(LocalDate.of(2023, 9, 22)) //So far 3 in September
                )
            )
        }

        allDreams = AllDreams(dreamDao)
        firstDreamDate = FirstDreamDate(allDreams)

        dreamAmounts = DreamAmounts(allDreams, firstDreamDate)
    }


    @Test
    fun `DreamAmounts return the right answer`() {
        val amountsExpected = listOf(
            LocalDate.of(2023, 6, 1) to 6,
            LocalDate.of(2023, 7, 1) to 9,
            LocalDate.of(2023, 8, 1) to 1,
            LocalDate.of(2023, 9, 1) to 3
        )

        val expectedAmounts = amountsExpected.map{ it.second }.toIntArray()
        val expectedDates = amountsExpected.map { it.first }.toTypedArray()

        val amounts = runBlocking {
            dreamAmounts(
                DreamAmounts.Input(
                    totalEnd = LocalDate.now()
                )
            ).first()
        }

        val actualAmounts = amounts.map { it.second }.toIntArray()
        val actualDates = amounts.map { it.first }.toTypedArray()

        Assert.assertArrayEquals(expectedAmounts, actualAmounts)
        Assert.assertArrayEquals(expectedDates, actualDates)
    }

}