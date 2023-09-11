package com.snow.diary.core.database

interface IModelMappable <Model> {

    fun toModel(): Model

}