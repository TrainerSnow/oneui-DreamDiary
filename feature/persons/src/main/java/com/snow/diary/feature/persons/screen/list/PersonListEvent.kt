package com.snow.diary.feature.persons.screen.list

import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.sort.SortConfig

sealed class PersonListEvent {

    data class PersonFavouriteClick(
        val person: Person
    ): PersonListEvent()


    data class SortChange(
        val sortConfig: SortConfig
    ) : PersonListEvent()

}
