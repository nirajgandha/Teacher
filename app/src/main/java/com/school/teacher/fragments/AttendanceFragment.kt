package com.school.teacher.fragments

import android.app.DatePickerDialog
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.activity.MainActivity
import com.school.teacher.adapter.AttendanceAdapter
import com.school.teacher.customui.materialcalendarview.EventDay
import com.school.teacher.customui.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.school.teacher.databinding.FragmentAttendanceBinding
import com.school.teacher.interfaces.AttendanceListener
import com.school.teacher.model.*
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AttendanceFragment : Fragment(), AttendanceListener {

    var _binding: FragmentAttendanceBinding? = null
    val binding get() = _binding!!
    var preference: Preference? = null
    val classIdList: ArrayList<String> = ArrayList()
    val classNameList: ArrayList<String> = ArrayList()
    val sectionIdList: ArrayList<String> = ArrayList()
    val sectionNameList: ArrayList<String> = ArrayList()
    var selectedClassId: String = ""
    var selectedSectionId: String = ""
    val generalizedArrayList: ArrayList<AttendanceStudentCustomObject> = ArrayList()
    val studentHashtable: HashMap<String, AttendanceStudentCustomObject> = HashMap()
    val attendanceListener: AttendanceListener = this
    var attendanceAdapter: AttendanceAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAttendanceBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callGetClassIdApi()
        binding.attendanceRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.attendanceRecycler.adapter = AttendanceAdapter(requireContext(), attendanceListener, generalizedArrayList)
        binding.submitBtn.visibility = View.GONE
        (binding.classSpinner.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            selectedClassId = classIdList[position]
            if( selectedClassId != " "){
                callGetSectionApi()
            }
            binding.tvDate.text = ""
            generalizedArrayList.clear()
            studentHashtable.clear()
            binding.attendanceRecycler.adapter = AttendanceAdapter(requireContext(), attendanceListener, generalizedArrayList)
            binding.submitBtn.visibility = View.GONE
        }

        (binding.sectionSpinner.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            Log.e("niraj", "onViewCreated: $position" )
            selectedSectionId = sectionIdList[position]
            binding.tvDate.text = ""
            generalizedArrayList.clear()
            studentHashtable.clear()
            binding.attendanceRecycler.adapter = AttendanceAdapter(requireContext(), attendanceListener, generalizedArrayList)
            binding.submitBtn.visibility = View.GONE
        }

        binding.tvDate.setOnClickListener { showDatePicker() }
        binding.btnCalendar.setOnClickListener { showDatePicker() }
        binding.submitBtn.setOnClickListener {
            if (studentHashtable.isEmpty()) {
                return@setOnClickListener
            }
            val idArrayList: ArrayList<String> = ArrayList()
            val attendanceArrayList: ArrayList<String> = ArrayList()
            val remarkArrayList: ArrayList<String> = ArrayList()
            for (item in studentHashtable){
                idArrayList.add(item.value.id)
                attendanceArrayList.add(item.value.attendanceType.toString())
                remarkArrayList.add(item.value.remark)
            }
            callSubmitAttendance(selectedClassId, selectedSectionId, idArrayList, attendanceArrayList, remarkArrayList, binding.tvDate.text.toString())
        }
    }

    private fun callSubmitAttendance(selectedClassId: String, selectedSectionId: String, idArrayList: ArrayList<String>, attendanceArrayList: ArrayList<String>, remarkArrayList: ArrayList<String>, date: String) {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.submitAttendance(selectedClassId, selectedSectionId, idArrayList, attendanceArrayList, remarkArrayList, date)
                .enqueue(object : Callback<SubmitAttendanceResponse> {
                    override fun onResponse(call: Call<SubmitAttendanceResponse>, response: Response<SubmitAttendanceResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            showError(meta.message)
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<SubmitAttendanceResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun callGetClassIdApi() {
        Utils.showProgress(requireContext())
        val code = preference!!.getString(preference!!.code, "")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getClassListApi(code)
                .enqueue(object : Callback<ClassListResponse> {
                    override fun onResponse(call: Call<ClassListResponse>, response: Response<ClassListResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                classIdList.add(" ")
                                classNameList.add(" ")
                                for (item in body.data) {
                                    classIdList.add(item.id)
                                    classNameList.add(item.mmemberClass)
                                }
                                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, classNameList)
                                (binding.classSpinner.editText as AutoCompleteTextView).setAdapter(adapter)
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<ClassListResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun callGetSectionApi() {
        Utils.showProgress(requireContext())
        val code = preference!!.getString(preference!!.code, "")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getSectionListApi(code)
                .enqueue(object : Callback<SectionListResponse> {
                    override fun onResponse(call: Call<SectionListResponse>, response: Response<SectionListResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                sectionIdList.clear()
                                sectionNameList.clear()
                                sectionIdList.add("")
                                sectionNameList.add("Section")
                                for (item in body.data) {
                                    sectionIdList.add(item.id)
                                    sectionNameList.add(item.section)
                                }

                                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, sectionNameList)
                                (binding.sectionSpinner.editText as AutoCompleteTextView).setAdapter(adapter)
                                Handler().postDelayed({
                                    (binding.sectionSpinner.editText as AutoCompleteTextView).setText((binding.sectionSpinner.editText as AutoCompleteTextView).adapter.getItem(0).toString(), false)
                                    selectedSectionId =sectionIdList[0]
                                },300)
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<SectionListResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun callGetAttendanceList(date: String) {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<AttendanceListResponse> = apiInterface.getAttandanceStudentlist(selectedClassId, selectedSectionId, date)
        call.enqueue(object : Callback<AttendanceListResponse> {
            override fun onResponse(call: Call<AttendanceListResponse>, response: Response<AttendanceListResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null){
                    val meta = body.meta
                    if (meta.code.equals( "200", true)){
                        generalizedArrayList.clear()
                        studentHashtable.clear()
                        for (student in body.data.maleStudent) {
                            val customObject = AttendanceStudentCustomObject(
                                    student.id,
                                    "${student.firstname} ${student.lastname}",
                                    student.grno,
                                    student.gender,
                                    student.attendenceTypeIdOf,
                                    student.attendenceTypeRemark
                            )
                            generalizedArrayList.add(customObject)
                            studentHashtable[student.id] = customObject
                        }
                        for (student in body.data.femaleStudent) {
                            val customObject = AttendanceStudentCustomObject(
                                    student.id,
                                    "${student.firstname} ${student.lastname}",
                                    student.grno,
                                    student.gender,
                                    student.attendenceTypeIdOf,
                                    student.attendenceTypeRemark
                            )
                            generalizedArrayList.add(customObject)
                            studentHashtable[student.id] = customObject
                        }
                        binding.attendanceRecycler.adapter = AttendanceAdapter(requireContext(), attendanceListener, generalizedArrayList)
                        if (generalizedArrayList.isNotEmpty()) {
                            binding.submitBtn.visibility = View.VISIBLE
                        }
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<AttendanceListResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun getFormattedDateArray(date_time: String): List<String> {
        val split = date_time.split(" ")
        return split[0].split("-")
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            binding.tvDate.text = "$dayOfMonth-${month + 1}-$year"
            if (selectedClassId.trim().isNotEmpty() && selectedSectionId.trim().isNotEmpty()){
                callGetAttendanceList(binding.tvDate.text.toString())
            }
        }, mYear, mMonth, mDay).show()
    }

    override fun onChange(attendanceStudentCustomObject: AttendanceStudentCustomObject) {
        studentHashtable[attendanceStudentCustomObject.id] = attendanceStudentCustomObject
    }
}