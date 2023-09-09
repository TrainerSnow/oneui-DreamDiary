package com.snow.diary.persons.screen.list

import com.snow.diary.model.data.Person
import com.snow.diary.model.sort.SortConfig

sealed class PersonListEvent {

    data class PersonFavouriteClick(
        val person: Person
    ): PersonListEvent()


    data class SortChange(
        val sortConfig: SortConfig
    ) : PersonListEvent()

}
