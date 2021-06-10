package com.schoolenglishmedium.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.schoolenglishmedium.teacher.databinding.GalleryRecyclerItemBinding
import com.schoolenglishmedium.teacher.interfaces.GalleryClickListener
import com.schoolenglishmedium.teacher.model.GalleryItem
import java.util.*


class GalleryAdapter(private var galleryItemList: ArrayList<GalleryItem>,private val galleryClickListener: GalleryClickListener, private val context: Context) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = GalleryRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(galleryItemList[position]){
                if (images.isNotEmpty()) {
                    galleryRecyclerItemBinding.title.text = title
                    Glide.with(context).load(images[0]).into(galleryRecyclerItemBinding.imgs)
                    galleryRecyclerItemBinding.imgs.setOnClickListener {
                        galleryClickListener.onItemClick(this)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return galleryItemList.size
    }

    fun refreshList(galleryItemList: ArrayList<GalleryItem>) {
        this.galleryItemList = galleryItemList
        notifyDataSetChanged()
    }

    class ViewHolder(val galleryRecyclerItemBinding: GalleryRecyclerItemBinding) : RecyclerView.ViewHolder(galleryRecyclerItemBinding.root)

}