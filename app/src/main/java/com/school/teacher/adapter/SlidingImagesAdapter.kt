package com.school.teacher.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.school.teacher.databinding.SlidingImagesLayoutBinding


class SlidingImagesAdapter(private val context: Context, private val urls: Array<String>) : PagerAdapter() {
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return urls.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val binding = SlidingImagesLayoutBinding.inflate(LayoutInflater.from(context))
        Glide.with(context)
                .load(urls[position].replace("com//", "com/"))
                .into(binding.image)
        view.addView(binding.root, 0)
        return binding.root
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}
    override fun saveState(): Parcelable? {
        return null
    }
}