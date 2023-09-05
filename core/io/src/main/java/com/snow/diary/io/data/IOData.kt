package com.snow.diary.io.data

import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation

typealias Crossref = Pair<Int, Int>

data class IOData (

    val dreams: List<Dream>,

    val persons: List<Person>,

    val locations: List<Location>,

    val relations: List<Relation>,

    val dreamPersonCrossrefs: List<Crossref>,

    val dreamLocationsCrossrefs: List<Crossref>

)