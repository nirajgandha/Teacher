package com.school.teacher.interfaces

import com.school.teacher.model.GalleryItem

interface GalleryClickListener {
    fun onItemClick(galleryItem: GalleryItem)
}