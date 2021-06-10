package com.schoolenglishmedium.teacher.customui

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import kotlin.math.min

class SpanningLinearLayoutManager(context: Context, var maxItemCounts: Int) : LinearLayoutManager(context) {
    private var setScrollHorizontally = false

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(c: Context, attrs: AttributeSet): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(c, attrs))
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(lp))
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        return super.checkLayoutParams(lp)
    }

    private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        if (orientation == HORIZONTAL) {
            layoutParams.width = Math.round(horizontalSpace / maxItemCount.toDouble()).toInt()
        } else if (orientation == VERTICAL) {
            layoutParams.height = Math.round(verticalSpace / maxItemCount.toDouble()).toInt()
        }
        return layoutParams
    }

    private val maxItemCount: Int get() = min(itemCount, maxItemCounts)

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return setScrollHorizontally
    }

    private val horizontalSpace: Int get() = width - paddingRight - paddingLeft
    private val verticalSpace: Int get() = height - paddingBottom - paddingTop

    fun setScrollHorizontally(setScrollHorizontally: Boolean) {
        this.setScrollHorizontally = setScrollHorizontally
    }

    fun setMaxItemsToShowInScreen(itemCount: Int) {
        this.maxItemCounts = itemCount
    }
}