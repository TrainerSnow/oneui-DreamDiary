package com.snow.diary.core.io.data

import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation

typealias Crossref = Pair<Int, Int>

data class IOData (

    val dreams: List<Dream>,

    val persons: List<Person>,

    val locations: List<Location>,

    val relations: List<Relation>,

    val dreamPersonCrossrefs: List<Crossref>,

    val dreamLocationsCrossrefs: List<Crossref>

)