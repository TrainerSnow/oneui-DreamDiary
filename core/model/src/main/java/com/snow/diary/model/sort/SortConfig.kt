package com.snow.diary.model.sort

data class SortConfig(

    val mode: SortMode = SortMode.None,

    val direction: SortDirection = SortDirection.Unspecified

)
