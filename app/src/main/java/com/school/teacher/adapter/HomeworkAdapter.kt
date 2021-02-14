package com.school.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.school.teacher.R
import com.school.teacher.databinding.HomeWorkRecyclerViewItemBinding
import com.school.teacher.interfaces.HomeWorkClickListener
import com.school.teacher.model.Homework
import java.util.*


class HomeworkAdapter(private var homework: ArrayList<Homework>, private val homeWorkClickListener: HomeWorkClickListener, private val context: Context) : RecyclerView.Adapter<HomeworkAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = HomeWorkRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(homework[position]){
                if (position == homework.size - 1){
                    (homeWorkRecyclerViewItemBinding.root.layoutParams as RecyclerView.LayoutParams).bottomMargin = 100
                }
                homeWorkRecyclerViewItemBinding.subject.text = subjectName
                homeWorkRecyclerViewItemBinding.classNumber.text = context.resources.getString(R.string.str_class_section, "$className$sectionName")
                homeWorkRecyclerViewItemBinding.createdOnValue.text = homeworkDate
                homeWorkRecyclerViewItemBinding.createdByValue.text = context.resources.getString(R.string.str_created_by, createdBy)
                homeWorkRecyclerViewItemBinding.submissionOnValue.text = submitDate
                homeWorkRecyclerViewItemBinding.evaluationOnValue.text = evaluationDate
                homeWorkRecyclerViewItemBinding.evaluatedByValue.text = context.resources.getString(R.string.str_evaluated_by, evaluatedByName)
                homeWorkRecyclerViewItemBinding.homeworkStatus.visibility = View.GONE
                homeWorkRecyclerViewItemBinding.imgUpload.visibility = View.GONE
                if (document.isNotEmpty()) {
                    homeWorkRecyclerViewItemBinding.imgDownload.visibility = View.VISIBLE
                    homeWorkRecyclerViewItemBinding.imgDownload.setOnClickListener {
                        homeWorkClickListener.onDownloadClicked(this)
                    }
                } else {
                    homeWorkRecyclerViewItemBinding.imgDownload.visibility = View.GONE
                }
                homeWorkRecyclerViewItemBinding.imgView.setOnClickListener {
                    homeWorkClickListener.onViewClicked(this)
                }
                homeWorkRecyclerViewItemBinding.viewLabel.setOnClickListener {
                    homeWorkClickListener.onViewClicked(this)
                }
                homeWorkRecyclerViewItemBinding.imgEdit.setOnClickListener {
                    homeWorkClickListener.onEditClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return homework.size
    }

    fun refreshList(homework: ArrayList<Homework>) {
        this.homework = homework
        notifyDataSetChanged()
    }

    class ViewHolder(val homeWorkRecyclerViewItemBinding: HomeWorkRecyclerViewItemBinding) : RecyclerView.ViewHolder(homeWorkRecyclerViewItemBinding.root)

}