package com.schoolenglishmedium.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schoolenglishmedium.teacher.databinding.LeaveRequestRecyclerViewItemBinding
import com.schoolenglishmedium.teacher.interfaces.LeaveClickListener
import com.schoolenglishmedium.teacher.model.TeacherLeaveData
import java.util.*


class LeaveRequestAdapter(private var leaveRequestArrayList: ArrayList<TeacherLeaveData>, private val leaveClickListener: LeaveClickListener, private val context: Context) : RecyclerView.Adapter<LeaveRequestAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = LeaveRequestRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(leaveRequestArrayList[position]){
                if (position == leaveRequestArrayList.size - 1){
                    (leaveRequestRecyclerViewItemBinding.root.layoutParams as RecyclerView.LayoutParams).bottomMargin = 100
                }
                leaveRequestRecyclerViewItemBinding.fromDate.text = fromDate
                leaveRequestRecyclerViewItemBinding.toDate.text = toDate
                leaveRequestRecyclerViewItemBinding.reason.text = reason
                if (approveBy.trim().isEmpty() || approveBy.trim()=="0") {
                    leaveRequestRecyclerViewItemBinding.approvalTl.visibility = View.GONE
                    leaveRequestRecyclerViewItemBinding.imgEdit.visibility = View.VISIBLE
                } else {
                    leaveRequestRecyclerViewItemBinding.approvalTl.visibility = View.VISIBLE
                    leaveRequestRecyclerViewItemBinding.imgEdit.visibility = View.GONE
                    leaveRequestRecyclerViewItemBinding.approvedBy.text = approveByName
                    leaveRequestRecyclerViewItemBinding.approveDeclineTv.text = if(approveBy.trim()=="1") {
                        "Approved By: "
                    } else {
                        "Declined By: "
                    }
                }
                leaveRequestRecyclerViewItemBinding.imgEdit.setOnClickListener { leaveClickListener.onEditClicked(this) }
            }
        }
    }

    override fun getItemCount(): Int {
        return leaveRequestArrayList.size
    }

    class ViewHolder(val leaveRequestRecyclerViewItemBinding: LeaveRequestRecyclerViewItemBinding) : RecyclerView.ViewHolder(leaveRequestRecyclerViewItemBinding.root)

}