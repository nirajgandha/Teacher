package com.school.teacher.fragments

import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.activity.MainActivity
import com.school.teacher.adapter.CalendarAdapter
import com.school.teacher.customui.materialcalendarview.EventDay
import com.school.teacher.customui.materialcalendarview.getDatesRange
import com.school.teacher.customui.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.school.teacher.customui.materialcalendarview.listeners.OnDayClickListener
import com.school.teacher.customui.materialcalendarview.utils.EventImage
import com.school.teacher.databinding.CalendarDialogLayoutBinding
import com.school.teacher.databinding.FragmentCalendarBinding
import com.school.teacher.interfaces.CalendarItemClickListener
import com.school.teacher.model.CalendarItem
import com.school.teacher.model.CalendarResponse
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarFragment : Fragment(), CalendarItemClickListener {

    var _binding : FragmentCalendarBinding? = null
    val binding get() = _binding!!
    var preference : Preference ?= null
    val events: MutableList<EventDay> = ArrayList()
    val calendarItemClickListener: CalendarItemClickListener = this
    private var downloadManager: DownloadManager?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference= Preference(requireContext())
        downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCalendarBinding.inflate(inflater)
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendarView.setHeaderLabelColor(R.color.black)
        binding.calendarView.setHeaderColor(R.color.white)
        binding.eventsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        callGetCalenderEventList(getFirstDateOfMonth(binding.calendarView.getHeaderName()))

        binding.calendarView.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                binding.eventsRecyclerview.adapter = CalendarAdapter(ArrayList(), calendarItemClickListener, requireContext())
                callGetCalenderEventList(getFirstDateOfMonth(binding.calendarView.getHeaderName()))
            }
        })

        binding.calendarView.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                binding.eventsRecyclerview.adapter = CalendarAdapter(ArrayList(), calendarItemClickListener, requireContext())
                callGetCalenderEventList(getFirstDateOfMonth(binding.calendarView.getHeaderName()))
            }
        })

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener{
            override fun onDayClick(eventDay: EventDay) {
                if (eventDay.imageDrawable != EventImage.EmptyEventImage){
                    val cal = eventDay.calendar
                    callGetCalenderDayEventList("${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH)+1}-${cal.get(Calendar.DAY_OF_MONTH)}")
                } else {
                    binding.eventsRecyclerview.adapter = CalendarAdapter(ArrayList(), calendarItemClickListener, requireContext())
                }
            }

        })

    }

    private fun callGetCalenderEventList(firstDayString: String) {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<CalendarResponse> = apiInterface.getCalendarListApi(firstDayString)
        call.enqueue(object : Callback<CalendarResponse> {
            override fun onResponse(call: Call<CalendarResponse>, response: Response<CalendarResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.events_found_successfully), true)) {
                        val eventList = body.data
                        events.clear()
                        binding.calendarView.setEvents(events)
                        for (event in eventList) {
                            if (event.fromDate != event.toDate) {
                                val fromsplits = getFormattedDateArray(event.fromDate)
                                val fromCalendar = Calendar.getInstance()
                                fromCalendar.set(Calendar.YEAR, fromsplits[0].toInt())
                                fromCalendar.set(Calendar.MONTH, fromsplits[1].toInt() - 1)
                                fromCalendar.set(Calendar.DAY_OF_MONTH, fromsplits[2].toInt())
                                fromCalendar.set(Calendar.HOUR_OF_DAY, 0)
                                fromCalendar.set(Calendar.MINUTE, 0)
                                fromCalendar.set(Calendar.SECOND, 0)

                                val toSplits = getFormattedDateArray(event.toDate)
                                val toCalendar = Calendar.getInstance()
                                toCalendar.set(Calendar.YEAR, toSplits[0].toInt())
                                toCalendar.set(Calendar.MONTH, toSplits[1].toInt() - 1)
                                toCalendar.set(Calendar.DAY_OF_MONTH, toSplits[2].toInt())
                                fromCalendar.set(Calendar.HOUR_OF_DAY, 0)
                                fromCalendar.set(Calendar.MINUTE, 0)
                                fromCalendar.set(Calendar.SECOND, 0)

                                val list = fromCalendar.getDatesRange(toCalendar)
                                events.add(EventDay(fromCalendar, setEventDot()))
                                events.add(EventDay(toCalendar, setEventDot()))
                                for (i in list) {
                                    events.add(EventDay(i, setEventDot()))
                                }
                            } else {
                                val splits = getFormattedDateArray(event.fromDate)
                                val calendar = Calendar.getInstance()
                                calendar.set(Calendar.YEAR, splits[0].toInt())
                                calendar.set(Calendar.MONTH, splits[1].toInt() - 1)
                                calendar.set(Calendar.DAY_OF_MONTH, splits[2].toInt())
                                events.add(EventDay(calendar, setEventDot()))
                            }
                        }
                        binding.calendarView.setEvents(events)
                        for (event in events) {
                            Log.d("niraj", "onResponse: ${event.calendar.get(Calendar.YEAR)}-${event.calendar.get(Calendar.MONTH)+1}-${event.calendar.get(Calendar.DAY_OF_MONTH)}")
                        }
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun callGetCalenderDayEventList(dayString: String) {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<CalendarResponse> = apiInterface.getCalendarDayListApi(dayString)
        call.enqueue(object : Callback<CalendarResponse> {
            override fun onResponse(call: Call<CalendarResponse>, response: Response<CalendarResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.events_found_successfully), true)) {
                            binding.eventsRecyclerview.adapter = CalendarAdapter(body.data, calendarItemClickListener, requireContext())
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun setEventDot(): Drawable {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_dots)
        drawable?.setTint(resources.getColor(R.color.absent, requireContext().theme))
        //Add padding to too large icon
        return InsetDrawable(drawable, 100, 0, 100, 0)
    }

    private fun getFormattedDateArray(date_time: String): List<String> {
        val split = date_time.split(" ")
        return split[0].split("-")
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
    }

    private fun getDates(dateString1: String, dateString2: String): List<Date>? {
        val dates = ArrayList<Date>()
        val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = df1.parse(dateString1)
            date2 = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1!!
        val cal2 = Calendar.getInstance()
        cal2.time = date2!!
        while (!cal1.after(cal2)) {
            dates.add(cal1.time)
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }

    private fun getFirstDateOfMonth(date: String): String{
        val currentYear = date.split(" ")[2]
        val selectedMonth = date.split(" ")[0]
        val stringArray = resources.getStringArray(R.array.material_calendar_months_array)
        val month = stringArray.indexOf(selectedMonth) + 1
        return "$currentYear-$month-1"
    }

    override fun onViewClicked(calendarItem: CalendarItem) {
        val builder = AlertDialog.Builder(requireContext());
        // set the custom layout
        val dialogBinding = CalendarDialogLayoutBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        dialogBinding.bgImg.clipToOutline = true
        dialogBinding.title.text = calendarItem.eventTitle
        dialogBinding.date.text = if (calendarItem.fromDate == calendarItem.toDate){
            "Date: ${calendarItem.fromDate}"
        } else {
            "Date: ${calendarItem.fromDate} to ${calendarItem.toDate}"
        }
        dialogBinding.message.text = calendarItem.eventNotificationMessage
        // create and show the alert dialog
        val dialog = builder.create();
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialogBinding.txtClose.setOnClickListener { if (dialog.isShowing) dialog.dismiss() }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onDownloadClicked(calendarItem: CalendarItem) {
        val uri = Uri.parse(calendarItem.image)
        val fileSplitter = calendarItem.image.split("/")
        val filename = fileSplitter[fileSplitter.size - 1]
        val request = DownloadManager.Request(uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(requireContext(), Environment.DIRECTORY_DOWNLOADS, filename)
        downloadManager!!.enqueue(request)
    }
}