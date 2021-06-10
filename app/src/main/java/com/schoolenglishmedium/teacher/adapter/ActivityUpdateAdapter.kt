package com.schoolenglishmedium.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.databinding.ActivityRecyclerViewItemBinding
import com.schoolenglishmedium.teacher.interfaces.ToDoUpdateClickListener
import com.schoolenglishmedium.teacher.model.ActivityNotesDetailItem
import java.util.*


class ActivityUpdateAdapter(private var activityNotesDetailItem: ArrayList<ActivityNotesDetailItem>, private val toDoUpdateClickListener: ToDoUpdateClickListener, private val context: Context) : RecyclerView.Adapter<ActivityUpdateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = ActivityRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(activityNotesDetailItem[position]){
                if (position == activityNotesDetailItem.size - 1){
                    (activityRecyclerViewItemBinding.root.layoutParams as RecyclerView.LayoutParams).bottomMargin = 100
                }
                activityRecyclerViewItemBinding.imgView.visibility = View.GONE
                activityRecyclerViewItemBinding.imgEdit.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil))
                activityRecyclerViewItemBinding.date.visibility = View.GONE
                activityRecyclerViewItemBinding.teacherName.visibility = View.GONE
                activityRecyclerViewItemBinding.title.text = note
                activityRecyclerViewItemBinding.imgEdit.setOnClickListener {
                    toDoUpdateClickListener.onEditClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return activityNotesDetailItem.size
    }

    class ViewHolder(val activityRecyclerViewItemBinding: ActivityRecyclerViewItemBinding) : RecyclerView.ViewHolder(activityRecyclerViewItemBinding.root)

}