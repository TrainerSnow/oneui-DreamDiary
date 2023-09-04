package com.snow.diary.common.search

import com.snow.diary.model.Searchable

object Search {

    fun Searchable.searchScore(
        query: String
    ): Int {
        var count = 1
        getStringFields().forEach {
            if (query in it) return@forEach
            count += 1
        }
        return count
    }

    fun Searchable.matches(
        query: String
    ): Boolean = getStringFields()
        .any {
            query in it
        }

    /**
     * Filters out non-matching objects and sorts the rest depending on their similarity.
     */
    fun <T : Searchable> List<T>.filterSearch(
        query: String
    ): List<T> = filter {
            it.matches(query)
        }
        .map {
            ScoredSearchable(
                it,
                it.searchScore(query)
            )
        }
        .sortedBy { it.score }
        .map { it.searchable }

    private data class ScoredSearchable<T : Searchable>(

        val searchable: T,

        val score: Int

    )

}