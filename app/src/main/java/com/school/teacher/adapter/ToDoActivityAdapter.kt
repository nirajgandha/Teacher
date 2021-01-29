package com.school.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.school.teacher.databinding.ActivityRecyclerViewItemBinding
import com.school.teacher.interfaces.ToDoClickListener
import com.school.teacher.model.ToDoActivityItem
import kotlin.collections.ArrayList


class ToDoActivityAdapter(private var toDoActivityItemList: ArrayList<ToDoActivityItem>, private val toDoClickListener: ToDoClickListener, private val context: Context) : RecyclerView.Adapter<ToDoActivityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = ActivityRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(toDoActivityItemList[position]){
                activityRecyclerViewItemBinding.title.text = title
                activityRecyclerViewItemBinding.date.text = date
                activityRecyclerViewItemBinding.teacherName.text = teacherName
                activityRecyclerViewItemBinding.imgView.setOnClickListener {
                    toDoClickListener.onViewClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return toDoActivityItemList.size
    }

    fun refreshList(toDoActivityItemList: ArrayList<ToDoActivityItem>) {
        this.toDoActivityItemList = toDoActivityItemList
        notifyDataSetChanged()
    }

    class ViewHolder(val activityRecyclerViewItemBinding: ActivityRecyclerViewItemBinding) : RecyclerView.ViewHolder(activityRecyclerViewItemBinding.root)

}