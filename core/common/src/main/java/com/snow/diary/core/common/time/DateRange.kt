package com.snow.diary.core.common.time;

import java.time.LocalDate
import java.time.temporal.TemporalUnit

sealed interface DateRange {

    operator fun contains(date: LocalDate): Boolean = resolve().let { range ->
        date >= range.from && date <= range.to
    }

    fun resolve(): FixedTimeRange

        data class LastN(
            val n: Long,
            val unit: TemporalUnit
        ): DateRange {

            init {
                assert(n >= 0){ "n can't be negative" }
            }

            override fun resolve() = FixedTimeRange(
                to = LocalDate.now(),
                from = LocalDate.now().minus(n, unit)
            )

        }

        data class Offset(
            val n: Long,
            val unit: TemporalUnit,
            val from: LocalDate
        ): DateRange {

            init {
                assert(n >= 0){ "n can't be negative" }
            }

            override fun resolve() = FixedTimeRange(
                to = from.plus(n, unit),
                from = from
            )

        }

        data class Fixed(
            val from: LocalDate,
            val to: LocalDate
        ): DateRange {

            init {
                assert(from < to){ "from < to fails" }
            }

            override fun resolve() = FixedTimeRange(from, to)
        }


    companion object {

        val AllTime = Fixed(
            LocalDate.MIN, LocalDate.MAX
        )

    }

}

data class FixedTimeRange(
    val from: LocalDate,
    val to: LocalDate
)