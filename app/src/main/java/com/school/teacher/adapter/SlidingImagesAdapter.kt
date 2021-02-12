package com.school.teacher.adapter

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
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
        binding.imgDownload.setOnClickListener {
            val uri = Uri.parse(urls[position].replace("com//", "com/"))
            val urlSubstring = urls[position].split("/")
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(true)
            request.setTitle(urlSubstring[urlSubstring.size-1])
            request.setDescription(urlSubstring[urlSubstring.size-1])
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, urlSubstring[urlSubstring.size-1])
            (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
        }
        return binding.root
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}
    override fun saveState(): Parcelable? {
        return null
    }
}