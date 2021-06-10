package com.schoolenglishmedium.teacher.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.adapter.SlidingImagesAdapter
import com.schoolenglishmedium.teacher.databinding.ActivitySlideShowBinding
import java.util.*


class SlideShowActivity : AppCompatActivity() {
    private var _binding: ActivitySlideShowBinding? = null
    private val binding get() = _binding!!
    private lateinit var swipeTimer: Timer

    companion object {
        var NUM_PAGES = 0
        var currentPage = 0
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySlideShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
    }

    private fun loadData() {
        val urlStrings = intent.extras!!.getStringArrayList(getString(R.string.galleryImagesUrlList))
        binding.pager.adapter = SlidingImagesAdapter(this, urlStrings!!.toTypedArray())
        binding.indicator.setViewPager(binding.pager)
        val density = resources.displayMetrics.density
        binding.indicator.radius = 5 * density
        NUM_PAGES = urlStrings.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                //currentPage = 0   //To repeat the slideshow continuously and remove onBackPressed()
                onBackPressed()
            }
            binding.pager.setCurrentItem(currentPage++, true)
        }
        swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 2000)


        // Pager listener over indicator
        binding.indicator.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(pos: Int) {}
        })
    }

    override fun onBackPressed() {
        swipeTimer.cancel()
        super.onBackPressed()

    }
}