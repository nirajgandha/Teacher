package com.schoolenglishmedium.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.schoolenglishmedium.teacher.databinding.StudentActivityRecyclerViewItemBinding
import com.schoolenglishmedium.teacher.interfaces.StudentActivityClickListener
import com.schoolenglishmedium.teacher.model.StudentActivityDetail
import kotlin.collections.ArrayList


class StudentActivityAdapter(private var studentActivityDetailArrayList: ArrayList<StudentActivityDetail>, private val studentActivityClickListener: StudentActivityClickListener, private val context: Context) : RecyclerView.Adapter<StudentActivityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = StudentActivityRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(studentActivityDetailArrayList[position]){
                studentActivityRecyclerViewItemBinding.title.text = title
                studentActivityRecyclerViewItemBinding.date.text = date
                studentActivityRecyclerViewItemBinding.teacherName.text = teacherName
                studentActivityRecyclerViewItemBinding.imgView.setOnClickListener {
                    studentActivityClickListener.onViewClicked(this)
                }
                studentActivityRecyclerViewItemBinding.imgEdit.setOnClickListener {
                    studentActivityClickListener.onEditClicked(this)
                }
                if(teacherName.trim().isEmpty()){
                    studentActivityRecyclerViewItemBinding.teacherName.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return studentActivityDetailArrayList.size
    }

    fun refreshList(studentActivityItemList: ArrayList<StudentActivityDetail>) {
        this.studentActivityDetailArrayList = studentActivityItemList
        notifyDataSetChanged()
    }

    class ViewHolder(val studentActivityRecyclerViewItemBinding: StudentActivityRecyclerViewItemBinding) : RecyclerView.ViewHolder(studentActivityRecyclerViewItemBinding.root)

}