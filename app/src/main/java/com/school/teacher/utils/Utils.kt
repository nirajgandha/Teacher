package com.school.teacher.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.school.teacher.customui.CustomDialog


object Utils {
    /**
     * Call snack bar which will disapper in 3-5 seconds
     *
     * @return Snack bar
     */
    fun showSnackBar(view: View, message: String, context: Context): Snackbar {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
//        val snackBarView: View = snackBar.view
//        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.holo_red_light))
        return snackBar
    }

    private var customDialog: CustomDialog? = null

    fun showProgress(context: Context) {
        customDialog = CustomDialog.getInstance()
        customDialog!!.showProgress(context, false)
    }

    fun hideProgress() {
        if (customDialog != null) {
            customDialog!!.hideProgress()
            customDialog = null
        }
    }
}