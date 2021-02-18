package com.school.teacher.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.school.teacher.R
import com.school.teacher.activity.MainActivity
import com.school.teacher.databinding.FragmentAddStudentActivityBinding
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


class AddStudentActivityFragment : Fragment() {

    private var _binding: FragmentAddStudentActivityBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private var isEdit: Boolean = false
    private var studentActivityId: String = ""
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
        _binding = FragmentAddStudentActivityBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        val bundle = requireArguments()
        if (bundle.isEmpty) {
            binding.title.text = "Add Student Activity"
        } else {
            binding.title.text = "Update Student Activity"
            editArrayList = bundle.getStringArrayList("editStudentActivity")!!
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

        binding.DateCal.setOnClickListener {
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
            binding.DateCal.performClick()
        }

        binding.txtSubmit.setOnClickListener {
            if (selectedStudentId.trim().isEmpty()) {
                showError("Select Student")
                return@setOnClickListener
            }
            if (binding.studentActivityTitle.text.trim().isEmpty()) {
                showError("Enter title")
                return@setOnClickListener
            }
            if (binding.description.text.trim().isEmpty()) {
                showError("Enter Description")
                return@setOnClickListener
            }
            if (binding.instruction.text.trim().isEmpty()) {
                showError("Enter Instruction")
                return@setOnClickListener
            }
            if (binding.DateValue.text.trim().isEmpty()) {
                showError("Select Date")
                return@setOnClickListener
            }
            if (isEdit) {
                callUpdateStudentActivityApi()
            } else {
                callAddStudentActivityApi()
            }
        }
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
                                    studentNameList.add("${items.grno} - ${items.firstname} ${items.lastname}")
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

    private fun callUpdateStudentActivityApi() {
        Utils.showProgress(requireContext())
        val code = preference!!.getString(preference!!.code, "")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.updateStudentActivityApi(code, studentActivityId, selectedStudentId, binding.studentActivityTitle.text.toString(),
                binding.description.text.toString(), binding.instruction.text.toString(), binding.DateValue.text.toString())
                .enqueue(object : Callback<StudentActivityResponse> {
                    override fun onResponse(call: Call<StudentActivityResponse>, response: Response<StudentActivityResponse>) {
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

                    override fun onFailure(call: Call<StudentActivityResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun loadData() {
        studentActivityId = editArrayList[0]
        selectedStudentId = editArrayList[1]
        binding.studentActivityTitle.setText(editArrayList[2])
        binding.description.setText(editArrayList[3])
        binding.instruction.setText(editArrayList[4])
        binding.DateValue.text = editArrayList[5]
        (binding.studentSpinner.editText as AutoCompleteTextView).setText((binding.studentSpinner.editText as AutoCompleteTextView).adapter.getItem(studentIdList.indexOf(selectedStudentId)).toString(), false)
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
    }

    private fun callAddStudentActivityApi() {
        Utils.showProgress(requireContext())
        val code = preference!!.getString(preference!!.code, "")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.addStudentActivityApi(code, selectedStudentId, binding.studentActivityTitle.text.toString(),
                binding.description.text.toString(), binding.instruction.text.toString(), binding.DateValue.text.toString())
                .enqueue(object : Callback<StudentActivityResponse> {
                    override fun onResponse(call: Call<StudentActivityResponse>, response: Response<StudentActivityResponse>) {
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

                    override fun onFailure(call: Call<StudentActivityResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

}