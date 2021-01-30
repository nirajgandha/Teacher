package com.school.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.school.teacher.R
import com.school.teacher.databinding.SyllabusRecyclerViewItemBinding
import com.school.teacher.interfaces.SyllabusClickListener
import com.school.teacher.model.Syllabus
import java.util.*


class SyllabusAdapter(private var syllabusList: ArrayList<Syllabus>, private val syllabusClickListener: SyllabusClickListener, private val context: Context) : RecyclerView.Adapter<SyllabusAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = SyllabusRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(syllabusList[position]){
                if (position == syllabusList.size - 1){
                    (syllabusRecyclerViewItemBinding.root.layoutParams as RecyclerView.LayoutParams).bottomMargin = 100
                }
                syllabusRecyclerViewItemBinding.subject.text = subjectName
                syllabusRecyclerViewItemBinding.classNumber.text = context.resources.getString(R.string.str_class_section, "$className$sectionName")
                syllabusRecyclerViewItemBinding.title.text = title
                if (document.isNotEmpty()) {
                    syllabusRecyclerViewItemBinding.imgDownload.visibility = View.VISIBLE
                    syllabusRecyclerViewItemBinding.imgDownload.setOnClickListener {
                        syllabusClickListener.onDownloadClicked(this)
                    }
                } else {
                    syllabusRecyclerViewItemBinding.imgDownload.visibility = View.GONE
                }
                syllabusRecyclerViewItemBinding.imgEdit.setOnClickListener {
                    syllabusClickListener.onEditClicked(this)
                }
                syllabusRecyclerViewItemBinding.imgList.setOnClickListener {
                    syllabusClickListener.onViewClicked(this)
                }
                Log.d("niraj", "onBindViewHolder: ${this.toString()}")
            }
        }
    }

    override fun getItemCount(): Int {
        return syllabusList.size
    }

    fun refreshList(syllabus: ArrayList<Syllabus>) {
        this.syllabusList = syllabus
        notifyDataSetChanged()
    }

    class ViewHolder(val syllabusRecyclerViewItemBinding: SyllabusRecyclerViewItemBinding) : RecyclerView.ViewHolder(syllabusRecyclerViewItemBinding.root)

}