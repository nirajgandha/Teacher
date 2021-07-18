package com.schoolenglishmedium.teacher.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.activity.MainActivity
import com.schoolenglishmedium.teacher.databinding.FragmentAddNoticeBinding
import com.schoolenglishmedium.teacher.model.*
import com.schoolenglishmedium.teacher.retrofit_api.APIClient
import com.schoolenglishmedium.teacher.retrofit_api.APIInterface
import com.schoolenglishmedium.teacher.utils.Preference
import com.schoolenglishmedium.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class AddNoticeFragment : Fragment() {

    private var _binding: FragmentAddNoticeBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private var isEdit: Boolean = false
    private var noticeId: String = ""
    private var visibleStudent: String = "yes"
    private var visibleStaff: String = "yes"
    private var visibleTeacher: String = "yes"
    private var visibleGujPrinciple: String = "yes"
    private var visibleEngPrinciple: String = "yes"
    private var editArrayList: ArrayList<String> = ArrayList()
    private var studentIdList: ArrayList<String> = ArrayList()
    private var studentNameList: ArrayList<String> = ArrayList()
    private var selectedStudentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAddNoticeBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        val bundle = requireArguments()
        if (bundle.isEmpty) {
            binding.title.text = "Add Notice"
        } else {
            binding.title.text = "Update Notice"
            editArrayList = bundle.getStringArrayList("editNotice")!!
            isEdit = true
        }
        callGetStudentListApi()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (binding.studentSpinner.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedStudentId = studentIdList[position]
        }
        
        binding.radioGrpVisibleStudent.setOnCheckedChangeListener { _, checkedId ->
            visibleStudent = if (checkedId == binding.visibleStudentYes.id) {
                "yes"
            } else {
                "no"
            }
        }

        binding.radioGrpVisibleStaff.setOnCheckedChangeListener { _, checkedId ->
            visibleStaff = if (checkedId == binding.visibleStaffYes.id) {
                "yes"
            } else {
                "no"
            }
        }

        binding.radioGrpVisibleTeacher.setOnCheckedChangeListener { _, checkedId ->
            visibleTeacher = if (checkedId == binding.visibleTeacherYes.id) {
                "yes"
            } else {
                "no"
            }
        }

        binding.radioGrpVisibleGujPrinciple.setOnCheckedChangeListener { _, checkedId ->
            visibleGujPrinciple = if (checkedId == binding.visibleGujPrincipleYes.id) {
                "yes"
            } else {
                "no"
            }
        }

        binding.radioGrpVisibleEngPrinciple.setOnCheckedChangeListener { _, checkedId ->
            visibleEngPrinciple = if (checkedId == binding.visibleEngPrincipleYes.id) {
                "yes"
            } else {
                "no"
            }
        }

        binding.submitDateCal.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (binding.publishDateValue.text.toString().isNotEmpty()) {
                val splitter = binding.publishDateValue.text.toString().split("-")
                calendar.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                calendar.set(Calendar.YEAR, splitter[0].toInt())
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.publishDateValue.text = "$year-${month + 1}-$dayOfMonth"
            }, mYear, mMonth, mDay).show()
        }
        binding.publishDateValue.setOnClickListener {
            binding.submitDateCal.performClick()
        }

        binding.homeworkDateCal.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (binding.DateValue.text.toString().isNotEmpty()) {
                val splitter = binding.DateValue.text.toString().split("-")
                calendar.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                calendar.set(Calendar.YEAR, splitter[0].toInt())
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.DateValue.text = "$year-${month + 1}-$dayOfMonth"
            }, mYear, mMonth, mDay).show()
        }
        binding.DateValue.setOnClickListener {
            binding.homeworkDateCal.performClick()
        }

        binding.txtSubmit.setOnClickListener {
            if (selectedStudentId.trim().isEmpty()) {
                showError("Select Student")
                return@setOnClickListener
            }
            if (binding.noticeTitle.text.trim().isEmpty()) {
                showError("Enter title")
                return@setOnClickListener
            }
            if (binding.message.text.trim().isEmpty()) {
                showError("Enter message")
                return@setOnClickListener
            }
            if (binding.noticeType.text.trim().isEmpty()) {
                showError("Enter notice type")
                return@setOnClickListener
            }
            if (binding.publishDateValue.text.trim().isEmpty()) {
                showError("Select Publish Date")
                return@setOnClickListener
            }
            if (binding.DateValue.text.trim().isEmpty()) {
                showError("Select Date")
                return@setOnClickListener
            }
            if (isEdit) {
                callUpdateNoticeApi()
            } else {
                callAddNoticeApi()
            }
        }
        binding.radioGrpVisibleStudent.check(binding.visibleStudentYes.id)
        binding.radioGrpVisibleStaff.check(binding.visibleStaffYes.id)
        binding.radioGrpVisibleTeacher.check(binding.visibleTeacherYes.id)
        binding.radioGrpVisibleGujPrinciple.check(binding.visibleGujPrincipleYes.id)
        binding.radioGrpVisibleEngPrinciple.check(binding.visibleEngPrincipleYes.id)
    }

    private fun callGetStudentListApi() {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getStudentListApi(preference!!.getString(preference!!.code, ""))
                .enqueue(object : Callback<GetAllStudentListResponse> {
                    override fun onResponse(call: Call<GetAllStudentListResponse>, response: Response<GetAllStudentListResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                for (items in body.getAllStudentListData.studentList) {
                                    studentIdList.add(items.id)
                                    studentNameList.add(items.fullName)
                                }
                                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, studentNameList)
                                (binding.studentSpinner.editText as AutoCompleteTextView).setAdapter(adapter)
                                if (isEdit) {
                                    loadData()
                                }
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<GetAllStudentListResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun callUpdateNoticeApi() {
        Utils.showProgress(requireContext())
        val code = preference!!.getString(preference!!.code, "")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.editNotice(code, noticeId, selectedStudentId, binding.noticeTitle.text.toString(), binding.message.text.toString(), binding.noticeType.text.toString(),
                binding.publishDateValue.text.toString(), binding.DateValue.text.toString(), visibleStudent, visibleStaff, visibleTeacher, visibleGujPrinciple, visibleEngPrinciple)
                .enqueue(object : Callback<NoticeResponse> {
                    override fun onResponse(call: Call<NoticeResponse>, response: Response<NoticeResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            showError(meta.message)
                            if (meta.code.equals("200", true)) {
                                (requireActivity() as MainActivity).onBackPressed()
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<NoticeResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun loadData() {
        noticeId = editArrayList[0]
        selectedStudentId = editArrayList[1]
        binding.noticeTitle.setText(editArrayList[2])
        binding.message.setText(editArrayList[3])
        binding.noticeType.setText(editArrayList[4])
        binding.publishDateValue.text = editArrayList[5]
        binding.DateValue.text = editArrayList[6]
        visibleStudent = editArrayList[7]
        visibleStaff = editArrayList[8]
        visibleTeacher = editArrayList[9]
        visibleGujPrinciple = editArrayList[10]
        visibleEngPrinciple = editArrayList[11]

        binding.radioGrpVisibleStudent.check(if (visibleStudent.equals("yes", false)) binding.visibleStudentYes.id else binding.visibleStudentNo.id)
        binding.radioGrpVisibleStaff.check(if (visibleStaff.equals("yes", false)) binding.visibleStaffYes.id else binding.visibleStaffNo.id)
        binding.radioGrpVisibleTeacher.check(if (visibleTeacher.equals("yes", false)) binding.visibleTeacherYes.id else binding.visibleTeacherNo.id)
        binding.radioGrpVisibleGujPrinciple.check(if (visibleGujPrinciple.equals("yes", false)) binding.visibleGujPrincipleYes.id else binding.visibleGujPrincipleNo.id)
        binding.radioGrpVisibleEngPrinciple.check(if (visibleEngPrinciple.equals("yes", false)) binding.visibleEngPrincipleYes.id else binding.visibleEngPrincipleNo.id)
        (binding.studentSpinner.editText as AutoCompleteTextView).setText((binding.studentSpinner.editText as AutoCompleteTextView).adapter.getItem(studentIdList.indexOf(selectedStudentId)).toString(), false)
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
    }

    private fun callAddNoticeApi() {
        Utils.showProgress(requireContext())
        val code = preference!!.getString(preference!!.code, "")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.addNotice(code, selectedStudentId, binding.noticeTitle.text.toString(), binding.message.text.toString(), binding.noticeType.text.toString(),
                binding.publishDateValue.text.toString(), binding.DateValue.text.toString(), visibleStudent, visibleStaff, visibleTeacher, visibleGujPrinciple, visibleEngPrinciple)
                .enqueue(object : Callback<NoticeResponse> {
                    override fun onResponse(call: Call<NoticeResponse>, response: Response<NoticeResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            showError(meta.message)
                            if (meta.code.equals("200", true)) {
                                (requireActivity() as MainActivity).onBackPressed()
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<NoticeResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

}