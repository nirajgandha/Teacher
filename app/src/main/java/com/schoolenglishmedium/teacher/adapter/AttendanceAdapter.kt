package com.schoolenglishmedium.teacher.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schoolenglishmedium.teacher.databinding.TeacherAttendanceHeaderRecyclerViewItemBinding
import com.schoolenglishmedium.teacher.databinding.TeacherAttendanceRecyclerViewItemBinding
import com.schoolenglishmedium.teacher.interfaces.AttendanceListener
import com.schoolenglishmedium.teacher.model.AttendanceStudentCustomObject
import java.util.ArrayList

class AttendanceAdapter(private val context: Context, private val attendanceListener: AttendanceListener, generalizedArrayList: ArrayList<AttendanceStudentCustomObject>, private val maleHeadingPosition: Int, private val femaleHeadingPosition: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolderStudent(val attendanceRecyclerViewItemBinding: TeacherAttendanceRecyclerViewItemBinding) : RecyclerView.ViewHolder(attendanceRecyclerViewItemBinding.root)
    class ViewHolderHeading(val attendanceHeaderRecyclerViewItemBinding: TeacherAttendanceHeaderRecyclerViewItemBinding) : RecyclerView.ViewHolder(attendanceHeaderRecyclerViewItemBinding.root)
    private var currentGender = ""
    private val headLayout = 0
    private val studentLayout = 1
    private var generalizedArrayList:ArrayList<AttendanceStudentCustomObject> = ArrayList()
    init {
        this.generalizedArrayList = generalizedArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == headLayout) {
            ViewHolderHeading(TeacherAttendanceHeaderRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ViewHolderStudent(TeacherAttendanceRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == headLayout) {
            val headHolder = holder as ViewHolderHeading
            headHolder.attendanceHeaderRecyclerViewItemBinding.heading.text = generalizedArrayList[position].gender.plus(" Student")
        } else {
            val studentHolder = holder as ViewHolderStudent
            studentHolder.attendanceRecyclerViewItemBinding.studentName.text = generalizedArrayList[position].name
            studentHolder.attendanceRecyclerViewItemBinding.grno.text = "Gr. No.: ${generalizedArrayList[position].grNo}"
            studentHolder.attendanceRecyclerViewItemBinding.remark.setText(generalizedArrayList[position].remark)
            if (generalizedArrayList[position].attendanceType == 2) {
                studentHolder.attendanceRecyclerViewItemBinding.attendanceRadioGroup.check(studentHolder.attendanceRecyclerViewItemBinding.rbtAbsent.id)
                } else {
                studentHolder.attendanceRecyclerViewItemBinding.attendanceRadioGroup.check(studentHolder.attendanceRecyclerViewItemBinding.rbtPresent.id)
                }
            studentHolder.attendanceRecyclerViewItemBinding.rbtAbsent.setOnClickListener{
                generalizedArrayList[position].attendanceType = 2
                generalizedArrayList[position].remark = studentHolder.attendanceRecyclerViewItemBinding.remark.text.toString()
                attendanceListener.onChange(generalizedArrayList[position])
                }
            studentHolder.attendanceRecyclerViewItemBinding.rbtPresent.setOnClickListener{
                generalizedArrayList[position].attendanceType = 1
                generalizedArrayList[position].remark = studentHolder.attendanceRecyclerViewItemBinding.remark.text.toString()
                attendanceListener.onChange(generalizedArrayList[position])
                }
            studentHolder.attendanceRecyclerViewItemBinding.remark.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                    generalizedArrayList[position].remark = s.toString()
                    attendanceListener.onChange(generalizedArrayList[position])
                    }

                })
            }
        }

    override fun getItemCount(): Int {
        return generalizedArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == maleHeadingPosition || position == femaleHeadingPosition) {
            headLayout
        } else {
            studentLayout
        }
    }

    fun refreshAdapter(generalizedArrayList: ArrayList<AttendanceStudentCustomObject>){
        this.generalizedArrayList = generalizedArrayList
        notifyDataSetChanged()
    }
}
