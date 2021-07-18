package com.schoolenglishmedium.teacher.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.activity.MainActivity
import com.schoolenglishmedium.teacher.adapter.StudentActivityAdapter
import com.schoolenglishmedium.teacher.databinding.FragmentStudentActivityBinding
import com.schoolenglishmedium.teacher.databinding.ToDoActivityDialogLayoutBinding
import com.schoolenglishmedium.teacher.interfaces.StudentActivityClickListener
import com.schoolenglishmedium.teacher.model.GetAllStudentListResponse
import com.schoolenglishmedium.teacher.model.StudentActivityDetail
import com.schoolenglishmedium.teacher.model.StudentActivityResponse
import com.schoolenglishmedium.teacher.retrofit_api.APIClient
import com.schoolenglishmedium.teacher.retrofit_api.APIInterface
import com.schoolenglishmedium.teacher.utils.Preference
import com.schoolenglishmedium.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StudentActivityFragment : Fragment(), StudentActivityClickListener {

    private var _binding : FragmentStudentActivityBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val studentActivityClickListener: StudentActivityClickListener = this
    private var studentIdList: ArrayList<String> = ArrayList()
    private var studentNameList: ArrayList<String> = ArrayList()
    private var selectedStudentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentStudentActivityBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.fab.setOnClickListener { startAddStudentActivityDetail(null) }
        binding.fab.setOnLongClickListener { v ->
            v.setOnTouchListener { view, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_MOVE -> {
                        view.x = event.rawX + (binding.fab.layoutParams.width.div(2))
                        view.y = event.rawY + (binding.fab.layoutParams.height.div(2))
                    }
                    MotionEvent.ACTION_UP -> view.setOnTouchListener(null)
                    else -> {
                    }
                }
                true
            }
            true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.activityRecyclerview.layoutManager = linearLayoutManager
        callGetStudentListApi()
        (binding.studentSpinner.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedStudentId = studentIdList[position]
            callGetStudentActivityList()
        }
    }

    private fun callGetStudentActivityList() {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val code = preference!!.getString(preference!!.code, "")
        apiInterface.getStudentActivityListApi(code, selectedStudentId)
                .enqueue(object : Callback<StudentActivityResponse> {
            override fun onResponse(call: Call<StudentActivityResponse>, response: Response<StudentActivityResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.activity_found_successful), true)) {
                        binding.activityRecyclerview.adapter = StudentActivityAdapter(body.data, studentActivityClickListener, requireContext())
                    } else {
                        showError(meta.message)
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

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
    }

    override fun onEditClicked(studentActivityDetail: StudentActivityDetail) {
        startAddStudentActivityDetail(studentActivityDetail)
    }

    private fun startAddStudentActivityDetail(studentActivityDetail: StudentActivityDetail?) {
        val addStudentActivityFragment = AddStudentActivityFragment()
        val bundle = Bundle()
        if (studentActivityDetail != null) {
            val studentActivityDetailsList: ArrayList<String> = ArrayList()
            studentActivityDetailsList.add(studentActivityDetail.id)
            studentActivityDetailsList.add(studentActivityDetail.studentId)
            studentActivityDetailsList.add(studentActivityDetail.title)
            studentActivityDetailsList.add(studentActivityDetail.description)
            studentActivityDetailsList.add(studentActivityDetail.instruction)
            studentActivityDetailsList.add(studentActivityDetail.date)
            bundle.putStringArrayList("editStudentActivity", studentActivityDetailsList)
        }
        addStudentActivityFragment.arguments = bundle
        (requireActivity() as MainActivity).openOtherFragment(addStudentActivityFragment)
    }

    override fun onViewClicked(studentActivityDetail: StudentActivityDetail) {
        val builder = AlertDialog.Builder(requireContext());
        // set the custom layout
        val dialogBinding = ToDoActivityDialogLayoutBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        dialogBinding.bgImg.clipToOutline = true
        val fromHtml = Html.fromHtml(studentActivityDetail.description, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
        var end = fromHtml.indexOf("\n", fromHtml.length-4)
        if (end == -1)
            end = fromHtml.length
        dialogBinding.title.text = studentActivityDetail.title
        dialogBinding.date.text = getString(R.string.date_s, studentActivityDetail.date)
        dialogBinding.description.text = getString(R.string.description, fromHtml.subSequence(0, end))
        dialogBinding.instruction.text = getString(R.string.instruction, studentActivityDetail.instruction)

        // create and show the alert dialog
        val dialog = builder.create();
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialogBinding.txtClose.setOnClickListener { if (dialog.isShowing) dialog.dismiss() }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
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
}

