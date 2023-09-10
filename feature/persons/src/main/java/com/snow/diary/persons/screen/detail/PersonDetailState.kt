package com.snow.diary.persons.screen.detail;

import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation

internal sealed class PersonDetailState {

    data object Loading: PersonDetailState()

    data class Error(
        val msg: String? = null,
        val id: Long
    ): PersonDetailState()

    data class Success(
        val person: Person,
        val relation: Relation,
        val dreams: List<Dream>
    ): PersonDetailState()

}