package com.school.teacher.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.school.teacher.databinding.SyllabusRecyclerViewItemBinding
import com.school.teacher.databinding.TeacherAttendanceRecyclerViewItemBinding
import com.school.teacher.interfaces.AttendanceListener
import com.school.teacher.model.AttendanceStudentCustomObject
import java.util.ArrayList

class AttendanceAdapter(private val context: Context, private val attendanceListener: AttendanceListener, private var generalizedArrayList: ArrayList<AttendanceStudentCustomObject>) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {


    class ViewHolder(val attendanceRecyclerViewItemBinding: TeacherAttendanceRecyclerViewItemBinding) : RecyclerView.ViewHolder(attendanceRecyclerViewItemBinding.root)
    private var currentGender = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = TeacherAttendanceRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(generalizedArrayList[position]){
                val attendanceObject = this
                var localAttendanceType: Int = attendanceType
                if (!currentGender.equals(gender, true)){
                    currentGender = gender
                    attendanceRecyclerViewItemBinding.heading.visibility = View.VISIBLE
                    attendanceRecyclerViewItemBinding.heading.text = gender.plus(" Student")
                }
                attendanceRecyclerViewItemBinding.studentName.text = name
                attendanceRecyclerViewItemBinding.grno.text = "Gr. No.: $grNo"
                attendanceRecyclerViewItemBinding.remark.setText(remark)
                if (attendanceType == 2) {
                    attendanceRecyclerViewItemBinding.attendanceRadioGroup.check(attendanceRecyclerViewItemBinding.rbtAbsent.id)
                } else {
                    attendanceRecyclerViewItemBinding.attendanceRadioGroup.check(attendanceRecyclerViewItemBinding.rbtPresent.id)
                }
                attendanceRecyclerViewItemBinding.rbtAbsent.setOnClickListener{
                    attendanceObject.attendanceType = 2
                    localAttendanceType = 2
                    attendanceObject.remark = attendanceRecyclerViewItemBinding.remark.text.toString()
                    attendanceListener.onChange(attendanceObject)
                }
                attendanceRecyclerViewItemBinding.rbtPresent.setOnClickListener{
                    attendanceObject.attendanceType = 1
                    localAttendanceType = 1
                    attendanceObject.remark = attendanceRecyclerViewItemBinding.remark.text.toString()
                    attendanceListener.onChange(attendanceObject)
                }
                attendanceRecyclerViewItemBinding.remark.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        attendanceObject.remark = s.toString()
                        attendanceObject.attendanceType = localAttendanceType
                        attendanceListener.onChange(attendanceObject)
                    }

                })
            }
        }
    }

    override fun getItemCount(): Int {
        return generalizedArrayList.size
    }

    fun refreshAdapter(generalizedArrayList: ArrayList<AttendanceStudentCustomObject>){
        this.generalizedArrayList = generalizedArrayList
        currentGender = ""
        notifyDataSetChanged()
    }
}
