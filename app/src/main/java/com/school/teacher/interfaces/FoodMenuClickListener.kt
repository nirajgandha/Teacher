package com.school.teacher.interfaces

import com.school.teacher.model.FoodMenu

interface FoodMenuClickListener {
    fun onViewClicked(foodMenu: FoodMenu)
    fun onDownloadClicked(foodMenu: FoodMenu)
}