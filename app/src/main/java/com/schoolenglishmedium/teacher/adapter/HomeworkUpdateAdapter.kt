package com.schoolenglishmedium.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.databinding.HomeWorkUpdateRecyclerViewItemBinding
import com.schoolenglishmedium.teacher.interfaces.HomeWorkUpdateClickListener
import com.schoolenglishmedium.teacher.model.HomeworkUpdateDetailItem
import java.util.*


class HomeworkUpdateAdapter(private var homeworkUpdateDetailItem: ArrayList<HomeworkUpdateDetailItem>, private val homeWorkUpdateClickListener: HomeWorkUpdateClickListener, private val context: Context) : RecyclerView.Adapter<HomeworkUpdateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = HomeWorkUpdateRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(homeworkUpdateDetailItem[position]){
                if (position == homeworkUpdateDetailItem.size - 1){
                    (homeWorkUpdateRecyclerViewItemBinding.root.layoutParams as RecyclerView.LayoutParams).bottomMargin = 100
                }
                homeWorkUpdateRecyclerViewItemBinding.nameValue.text = studentName
                homeWorkUpdateRecyclerViewItemBinding.grno.text = context.resources.getString(R.string.str_grno, studentId)
                if (homeworkStatus == "0" || homeworkStatus == "1"){
                    homeWorkUpdateRecyclerViewItemBinding.homeworkStatus.text = "Incomplete"
                    homeWorkUpdateRecyclerViewItemBinding.homeworkStatus.background = ContextCompat.getDrawable(context, R.drawable.home_work_incomplete_background)

                } else {
                    homeWorkUpdateRecyclerViewItemBinding.homeworkStatus.text = "Complete"
                    homeWorkUpdateRecyclerViewItemBinding.homeworkStatus.background = ContextCompat.getDrawable(context, R.drawable.home_work_complete_background)
                }
                homeWorkUpdateRecyclerViewItemBinding.imgEdit.setOnClickListener {
                    homeWorkUpdateClickListener.onEditClicked(this)
                }
                homeWorkUpdateRecyclerViewItemBinding.imgView.setOnClickListener {
                    homeWorkUpdateClickListener.onViewClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return homeworkUpdateDetailItem.size
    }

    class ViewHolder(val homeWorkUpdateRecyclerViewItemBinding: HomeWorkUpdateRecyclerViewItemBinding) : RecyclerView.ViewHolder(homeWorkUpdateRecyclerViewItemBinding.root)

}