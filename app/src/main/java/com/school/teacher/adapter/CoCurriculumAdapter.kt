package com.school.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.school.teacher.databinding.CocurriculumRecyclerItemBinding
import com.school.teacher.model.CoCurriculum
import java.util.*


class CoCurriculumAdapter(private var cocurriculumlist: ArrayList<CoCurriculum>, private val context: Context) : RecyclerView.Adapter<CoCurriculumAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = CocurriculumRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(cocurriculumlist[position]){
                cocurriculumRecyclerItemBinding.title.text = title
                Glide.with(context).load(vectorImages).into(cocurriculumRecyclerItemBinding.imgs)
            }
        }
    }

    override fun getItemCount(): Int {
        return cocurriculumlist.size
    }

    fun refreshList(cocurriculumlist: ArrayList<CoCurriculum>) {
        this.cocurriculumlist = cocurriculumlist
        notifyDataSetChanged()
    }

    class ViewHolder(val cocurriculumRecyclerItemBinding: CocurriculumRecyclerItemBinding) : RecyclerView.ViewHolder(cocurriculumRecyclerItemBinding.root)

}