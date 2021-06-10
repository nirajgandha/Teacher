package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.FoodMenu

interface FoodMenuClickListener {
    fun onViewClicked(foodMenu: FoodMenu)
    fun onDownloadClicked(foodMenu: FoodMenu)
}