package com.schoolenglishmedium.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.schoolenglishmedium.teacher.databinding.CalendarRecyclerViewItemBinding
import com.schoolenglishmedium.teacher.interfaces.CalendarItemClickListener
import com.schoolenglishmedium.teacher.model.CalendarItem
import java.util.*


class CalendarAdapter(private var calendarList: ArrayList<CalendarItem>, private val calendarItemClickListener: CalendarItemClickListener, private val context: Context) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = CalendarRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(calendarList[position]){
                calendarRecyclerViewItemBinding.title.text = eventTitle
                val fromHtml = Html.fromHtml(note, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
                var end = fromHtml.indexOf("\n", fromHtml.length-4)
                if (end == -1)
                    end = fromHtml.length
                calendarRecyclerViewItemBinding.note.text = fromHtml.subSequence(0, end)
                calendarRecyclerViewItemBinding.publishDate.text = if (fromDate == toDate){
                    "Date: $fromDate"
                } else {
                    "Date: $fromDate to $toDate"
                }
                calendarRecyclerViewItemBinding.imgView.setOnClickListener {
                    calendarItemClickListener.onViewClicked(this)
                }
                if (image.isEmpty()) {
                    calendarRecyclerViewItemBinding.imgDownload.visibility = View.GONE
                } else {
                    calendarRecyclerViewItemBinding.imgDownload.visibility = View.VISIBLE
                    calendarRecyclerViewItemBinding.imgDownload.setOnClickListener {
                        calendarItemClickListener.onDownloadClicked(this)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return calendarList.size
    }

    fun refreshList(calendarList: ArrayList<CalendarItem>) {
        this.calendarList = calendarList
        notifyDataSetChanged()
    }

    class ViewHolder(val calendarRecyclerViewItemBinding: CalendarRecyclerViewItemBinding) : RecyclerView.ViewHolder(calendarRecyclerViewItemBinding.root)

}