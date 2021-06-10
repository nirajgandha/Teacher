package com.schoolenglishmedium.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schoolenglishmedium.teacher.databinding.SyllabusUpdateRecyclerViewItemBinding
import com.schoolenglishmedium.teacher.interfaces.SyllabusUpdateDetailClickListener
import com.schoolenglishmedium.teacher.model.SyllabusUpdateDetailItem
import java.util.*


class SyllabusUpdatesAdapter(private var syllabusUpdateDetailItemArrayList: ArrayList<SyllabusUpdateDetailItem>, private val toDoUpdateClickListener: SyllabusUpdateDetailClickListener, private val context: Context) : RecyclerView.Adapter<SyllabusUpdatesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = SyllabusUpdateRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(syllabusUpdateDetailItemArrayList[position]){
                if (position == syllabusUpdateDetailItemArrayList.size - 1){
                    (syllabusUpdateRecyclerViewItemBinding.root.layoutParams as RecyclerView.LayoutParams).bottomMargin = 100
                }
                syllabusUpdateRecyclerViewItemBinding.fromDate.text = startDate
                syllabusUpdateRecyclerViewItemBinding.toDate.text = endDate
                syllabusUpdateRecyclerViewItemBinding.title.text = title
                syllabusUpdateRecyclerViewItemBinding.imgEdit.setOnClickListener {
                    toDoUpdateClickListener.onEditClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return syllabusUpdateDetailItemArrayList.size
    }

    class ViewHolder(val syllabusUpdateRecyclerViewItemBinding: SyllabusUpdateRecyclerViewItemBinding) : RecyclerView.ViewHolder(syllabusUpdateRecyclerViewItemBinding.root)

}