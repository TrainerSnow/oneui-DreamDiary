package com.snow.diary.ui.callback

import com.snow.diary.model.data.Dream

interface DreamCallback {

    fun onClick(dream: Dream) { }

    fun onFavouriteClick(dream: Dream) { }

    //For setting default param
    companion object : DreamCallback

}