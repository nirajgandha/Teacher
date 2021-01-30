package com.school.teacher.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.activity.MainActivity
import com.school.teacher.adapter.LeaveRequestAdapter
import com.school.teacher.databinding.FragmentLeaveRequestBinding
import com.school.teacher.databinding.LeaveRequestDialogLayoutBinding
import com.school.teacher.interfaces.LeaveClickListener
import com.school.teacher.model.*
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LeaveRequestFragment : Fragment(), LeaveClickListener {

    private val leaveClickListener: LeaveClickListener = this
    var _binding : FragmentLeaveRequestBinding? = null
    val binding get() = _binding!!
    var preference: Preference ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentLeaveRequestBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.fab.setOnClickListener { showDialog(null) }
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
        preference = Preference(requireContext())
        callGetLeaveListApi()
    }

    private fun callGetLeaveListApi() {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val studentId = preference!!.getString(preference!!.code, "")
        apiInterface.getTeacherLeaveApi(studentId).enqueue(object : Callback<TeacherLeaveResponse> {
            override fun onResponse(call: Call<TeacherLeaveResponse>, response: Response<TeacherLeaveResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.code.equals("200", true)) {
                        binding.payRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                        binding.payRecyclerview.adapter = LeaveRequestAdapter(body.data, leaveClickListener, requireContext())
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<TeacherLeaveResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
    }

    override fun onEditClicked(teacherLeaveData: TeacherLeaveData) {
        showDialog(teacherLeaveData)
    }

    private fun showDialog(teacherLeaveData: TeacherLeaveData?) {
        val builder = AlertDialog.Builder(requireContext());
        // set the custom layout
        val dialogBinding = LeaveRequestDialogLayoutBinding.inflate(layoutInflater)

        builder.setView(dialogBinding.root)
        dialogBinding.bgImg.clipToOutline = true
        if (teacherLeaveData == null) {
            dialogBinding.title.text = "New Leave Request"
        } else {
            dialogBinding.title.text = "Update Leave Request"
            dialogBinding.fromValue.text = teacherLeaveData.fromDate
            dialogBinding.toValue.text = teacherLeaveData.toDate
            dialogBinding.reason.setText(teacherLeaveData.reason)
        }
        dialogBinding.fromCal.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (dialogBinding.fromValue.text.toString().isNotEmpty()) {
                val splitter = dialogBinding.fromValue.text.toString().split("-")
                calendar.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                calendar.set(Calendar.MONTH, splitter[1].toInt()-1)
                calendar.set(Calendar.YEAR, splitter[0].toInt())
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                dialogBinding.fromValue.text = "$year-${month + 1}-$dayOfMonth"
            }, mYear, mMonth, mDay).show()
        }
        dialogBinding.toCal.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (dialogBinding.toValue.text.toString().isNotEmpty()) {
                val splitter = dialogBinding.toValue.text.toString().split("-")
                calendar.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                calendar.set(Calendar.MONTH, splitter[1].toInt()-1)
                calendar.set(Calendar.YEAR, splitter[0].toInt())
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                dialogBinding.toValue.text = "$year-${month + 1}-$dayOfMonth"
            }, mYear, mMonth, mDay).show()
        }
        // create and show the alert dialog
        val dialog = builder.create();
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialogBinding.txtSubmit.setOnClickListener {
            if (dialogBinding.fromValue.text.isEmpty()) {
                showError("Please enter From date")
                return@setOnClickListener
            } else if (dialogBinding.toValue.text.isEmpty()) {
                showError("Please enter To date")
                return@setOnClickListener
            } else if (dialogBinding.reason.text.isEmpty()) {
                showError("Please enter Reason")
                return@setOnClickListener
            }

            if (teacherLeaveData == null) {
                callNewLeaveRequest(dialogBinding.fromValue.text.toString(), dialogBinding.toValue.text.toString(), dialogBinding.reason.text.toString())
            } else {
                callUpdateLeaveRequest(teacherLeaveData.id, dialogBinding.fromValue.text.toString(), dialogBinding.toValue.text.toString(), dialogBinding.reason.text.toString())
            }
            dialog.dismiss()
        }
        dialogBinding.txtClose.setOnClickListener { if (dialog.isShowing) dialog.dismiss() }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun callNewLeaveRequest(fromDate: String, toDate: String, reason: String) {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val code = preference!!.getString(preference!!.code, "")
        apiInterface.newLeaveRequestApi(code, reason, fromDate, toDate).enqueue(object : Callback<NewTeacherLeaveResponse> {
            override fun onResponse(call: Call<NewTeacherLeaveResponse>, response: Response<NewTeacherLeaveResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.code.equals("200", true)) {
                        Handler().postDelayed({
                        callGetLeaveListApi()
                        }, 150)
                    }
                    showError(meta.message)
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<NewTeacherLeaveResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun callUpdateLeaveRequest(leaveId: String, fromDate: String, toDate: String, reason: String) {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val code = preference!!.getString(preference!!.code, "")
        apiInterface.editLeaveRequestApi(code, reason, fromDate, toDate, leaveId).enqueue(object : Callback<NewTeacherLeaveResponse> {
            override fun onResponse(call: Call<NewTeacherLeaveResponse>, response: Response<NewTeacherLeaveResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.code.equals("200", true)) {
                        Handler().postDelayed({
                            callGetLeaveListApi()
                        }, 150)
                    }
                    showError(meta.message)
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<NewTeacherLeaveResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }
}