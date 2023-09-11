package com.snow.diary.feature.relations.screen.detail;

import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation

internal sealed class RelationDetailState {

    data object Loading: RelationDetailState()

    data class Error(
        val msg: String? = null,
        val id: Long
    ): RelationDetailState()

    data class Success(
        val relation: Relation,
        val persons: List<Person>
    ): RelationDetailState()

}