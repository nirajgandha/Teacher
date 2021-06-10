package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.GalleryItem

interface GalleryClickListener {
    fun onItemClick(galleryItem: GalleryItem)
}