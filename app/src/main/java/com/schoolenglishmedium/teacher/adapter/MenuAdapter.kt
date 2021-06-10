package com.schoolenglishmedium.teacher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schoolenglishmedium.teacher.databinding.BottomNavigationItemBinding
import com.schoolenglishmedium.teacher.interfaces.ItemClickListener
import com.schoolenglishmedium.teacher.model.Menu
import java.util.*

class MenuAdapter(private var menuArrayList: ArrayList<Menu>, private val itemClickListener: ItemClickListener, private val context: Context) : RecyclerView.Adapter<MenuAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = BottomNavigationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(menuArrayList[position]){
                if (menuName.isNotBlank()) {
                    bottomNavigationItemBinding.txtMenuName.text = menuName
                    bottomNavigationItemBinding.layMainItem.setOnClickListener { itemClickListener.onItemClick(menuName) }
                    if (isSelected) {
                        holder.bottomNavigationItemBinding.imgMenu.setImageResource(activeIconPath)
                        holder.bottomNavigationItemBinding.imgMenu.elevation = 6.0f
                    } else {
                        holder.bottomNavigationItemBinding.imgMenu.elevation = 0.0f
                        holder.bottomNavigationItemBinding.imgMenu.setImageResource(inActiveIconPath)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return menuArrayList.size
    }

    fun refreshList(menuArrayList: ArrayList<Menu>) {
        this.menuArrayList = menuArrayList
        notifyDataSetChanged()
    }

    class ViewHolder(val bottomNavigationItemBinding: BottomNavigationItemBinding) : RecyclerView.ViewHolder(bottomNavigationItemBinding.root)

}