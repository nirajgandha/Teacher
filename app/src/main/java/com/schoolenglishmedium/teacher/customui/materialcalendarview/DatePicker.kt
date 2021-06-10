package com.schoolenglishmedium.teacher.customui.materialcalendarview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.customui.materialcalendarview.utils.CalendarProperties
import com.schoolenglishmedium.teacher.customui.materialcalendarview.utils.isMonthAfter
import com.schoolenglishmedium.teacher.customui.materialcalendarview.utils.isMonthBefore
import com.schoolenglishmedium.teacher.customui.materialcalendarview.utils.midnightCalendar
import kotlinx.android.synthetic.main.date_picker_dialog.view.*

/**
 * This class is responsible for creating DatePicker dialog.
 *
 * Created by Applandeo Team.
 */

class DatePicker(
        private val context: Context,
        private val calendarProperties: CalendarProperties
) {

    fun show(): DatePicker {
        val view = LayoutInflater.from(context).inflate(R.layout.date_picker_dialog, null)

        if (calendarProperties.pagesColor != 0) {
            view.setBackgroundColor(calendarProperties.pagesColor)
        }

        setTodayButtonVisibility(view.today_button)
        setDialogButtonsColors(view.negative_button, view.today_button)
        setOkButtonState(calendarProperties.calendarType == CalendarView.ONE_DAY_PICKER, view.positive_button)

        setDialogButtonsTypeface(view)

        calendarProperties.onSelectionAbilityListener = { enabled ->
            setOkButtonState(enabled, view.positive_button)
        }

        val calendarView = CalendarView(context = context, properties = calendarProperties)

        view.calendarContainer.addView(calendarView)

        calendarProperties.calendar?.let {
            runCatching { calendarView.setDate(it) }
        }

        val alertDialog = AlertDialog.Builder(context).create().apply {
            setView(view)
        }

        view.negative_button.setOnClickListener { alertDialog.cancel() }

        view.positive_button.setOnClickListener {
            alertDialog.cancel()
            calendarProperties.onSelectDateListener?.onSelect(calendarView.selectedDates)
        }

        view.today_button.setOnClickListener { calendarView.showCurrentMonthPage() }

        alertDialog.show()

        return this
    }

    private fun setDialogButtonsTypeface(view: View) {
        calendarProperties.typeface?.let { typeface ->
            view.today_button.typeface = typeface
            view.negative_button.typeface = typeface
            view.positive_button.typeface = typeface
        }
    }

    private fun setDialogButtonsColors(negativeButton: AppCompatButton, todayButton: AppCompatButton) {
        if (calendarProperties.dialogButtonsColor != 0) {
            negativeButton.setTextColor(ContextCompat.getColor(context, calendarProperties.dialogButtonsColor))
            todayButton.setTextColor(ContextCompat.getColor(context, calendarProperties.dialogButtonsColor))
        }
    }

    private fun setOkButtonState(enabled: Boolean, okButton: AppCompatButton) {
        okButton.isEnabled = enabled

        if (calendarProperties.dialogButtonsColor == 0) return

        val stateResource = if (enabled) {
            calendarProperties.dialogButtonsColor
        } else {
            R.color.disabledDialogButtonColor
        }

        okButton.setTextColor(ContextCompat.getColor(context, stateResource))
    }

    private fun setTodayButtonVisibility(todayButton: AppCompatButton) {
        calendarProperties.maximumDate?.let {
            if (it.isMonthBefore(midnightCalendar) || it.isMonthAfter(midnightCalendar)) {
                todayButton.visibility = View.GONE
            }
        }
    }
}
