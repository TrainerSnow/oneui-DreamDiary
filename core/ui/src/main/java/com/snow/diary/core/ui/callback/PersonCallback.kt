package com.snow.diary.core.ui.callback

import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation

interface PersonCallback {

    fun onClick(person: Person) { }

    fun onRelationClick(relation: Relation) { }

    fun onRelationsClick(relations: List<Relation>){ }

    fun onFavouriteClick(person: Person) { }

    //For setting default param
    companion object : com.snow.diary.core.ui.callback.PersonCallback

}