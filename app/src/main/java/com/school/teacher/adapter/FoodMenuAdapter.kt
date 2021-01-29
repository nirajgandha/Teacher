package com.school.teacher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.school.teacher.databinding.FoodMenuRecyclerViewItemBinding
import com.school.teacher.interfaces.FoodMenuClickListener
import com.school.teacher.model.FoodMenu
import java.util.*


class FoodMenuAdapter(private var foodMenuList: ArrayList<FoodMenu>, private val foodMenuClickListener: FoodMenuClickListener, private val context: Context) : RecyclerView.Adapter<FoodMenuAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = FoodMenuRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(foodMenuList[position]){
                val fromHtml = Html.fromHtml(title, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
                var end = fromHtml.indexOf("\n", fromHtml.length-4)
                if (end == -1)
                    end = fromHtml.length
                foodMenuRecyclerViewItemBinding.title.text = fromHtml.subSequence(0, end)
                foodMenuRecyclerViewItemBinding.imgView.setOnClickListener {
                    foodMenuClickListener.onViewClicked(this)
                }
                if (this.document.isNotEmpty()){
                    foodMenuRecyclerViewItemBinding.imgDownload.visibility = View.VISIBLE
                    foodMenuRecyclerViewItemBinding.imgDownload.setOnClickListener {
                        foodMenuClickListener.onDownloadClicked(this)
                    }
                } else {
                    foodMenuRecyclerViewItemBinding.imgDownload.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return foodMenuList.size
    }

    fun refreshList(foodMenuList: ArrayList<FoodMenu>) {
        this.foodMenuList = foodMenuList
        notifyDataSetChanged()
    }

    class ViewHolder(val foodMenuRecyclerViewItemBinding: FoodMenuRecyclerViewItemBinding) : RecyclerView.ViewHolder(foodMenuRecyclerViewItemBinding.root)

}