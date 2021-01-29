package com.school.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.school.teacher.databinding.NoticeBoardRecyclerViewItemBinding
import com.school.teacher.interfaces.NoticeClickListener
import com.school.teacher.model.Notice
import java.util.*


class NoticeAdapter(private var noticeList: ArrayList<Notice>, private val noticeClickListener: NoticeClickListener, private val context: Context) : RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = NoticeBoardRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(noticeList[position]){
                val fromHtml = Html.fromHtml(title, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
                var end = fromHtml.indexOf("\n", fromHtml.length-4)
                if (end == -1)
                    end = fromHtml.length
                noticeBoardRecyclerViewItemBinding.title.text = fromHtml.subSequence(0, end)
                noticeBoardRecyclerViewItemBinding.publishDate.text = publishDate
                noticeBoardRecyclerViewItemBinding.type.text = type
                noticeBoardRecyclerViewItemBinding.imgView.setOnClickListener {
                    noticeClickListener.onViewClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    fun refreshList(noticeList: ArrayList<Notice>) {
        this.noticeList = noticeList
        notifyDataSetChanged()
    }

    class ViewHolder(val noticeBoardRecyclerViewItemBinding: NoticeBoardRecyclerViewItemBinding) : RecyclerView.ViewHolder(noticeBoardRecyclerViewItemBinding.root)

}