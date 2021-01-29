package com.school.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.school.teacher.R
import com.school.teacher.databinding.SelectStudentItemBinding
import com.school.teacher.interfaces.StudentSelectedListener
import com.school.teacher.model.Student
import java.util.*


class StudentListAdapter(private var student: ArrayList<Student>, private val studentSelectedListener: StudentSelectedListener, private val context: Context) : RecyclerView.Adapter<StudentListAdapter.ViewHolder?>() {
    private var lastSelectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = SelectStudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(student[position]){
                selectStudentItemBinding.name.text = "$firstname $lastname"
                selectStudentItemBinding.grno.text = context.resources.getString(R.string.str_grno, grno)
                selectStudentItemBinding.rollno.text = context.resources.getString(R.string.str_rollno, rollNo)
                selectStudentItemBinding.radio.isChecked = lastSelectedPosition == position
                Glide.with(context).load(image).into(selectStudentItemBinding.imgStudent)
                selectStudentItemBinding.radio.setOnClickListener {
                    notifyItemChanged(lastSelectedPosition)
                    notifyItemChanged(position)
                    lastSelectedPosition = position
                    studentSelectedListener.onStudentSelected(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return student.size
    }

    fun refreshList(student: ArrayList<Student>) {
        this.student = student
        notifyDataSetChanged()
    }

    class ViewHolder(val selectStudentItemBinding: SelectStudentItemBinding) : RecyclerView.ViewHolder(selectStudentItemBinding.root)

}