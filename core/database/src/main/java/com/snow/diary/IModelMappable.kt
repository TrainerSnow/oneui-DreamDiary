package com.snow.diary

interface IModelMappable <Model> {

    fun toModel(): Model

}