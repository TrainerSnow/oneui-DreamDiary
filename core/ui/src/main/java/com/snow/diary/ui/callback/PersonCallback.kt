package com.snow.diary.ui.callback

import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation

interface PersonCallback {

    fun onClick(person: Person) { }

    fun onRelationClick(relation: Relation) { }

    fun onFavouriteClick(person: Person) { }

    //For setting default param
    companion object : PersonCallback

}