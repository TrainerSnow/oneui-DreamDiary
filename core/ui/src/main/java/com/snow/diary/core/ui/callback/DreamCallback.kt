package com.snow.diary.core.ui.callback

import com.snow.diary.core.model.data.Dream

interface DreamCallback {

    fun onClick(dream: Dream) { }

    fun onFavouriteClick(dream: Dream) { }

    //For setting default param
    companion object : DreamCallback

}