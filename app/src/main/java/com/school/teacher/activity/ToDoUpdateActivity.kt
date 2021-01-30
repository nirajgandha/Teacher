package com.school.teacher.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.adapter.ActivityUpdateAdapter
import com.school.teacher.databinding.ActivityToDoUpdateBinding
import com.school.teacher.databinding.ActivityUpdateDialogLayoutBinding
import com.school.teacher.interfaces.ToDoUpdateClickListener
import com.school.teacher.model.*
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ToDoUpdateActivity : AppCompatActivity(), ToDoUpdateClickListener {

    private var _binding : ActivityToDoUpdateBinding? = null
    private val binding get() = _binding!!
    private val toDoUpdateClickListener: ToDoUpdateClickListener = this
    private var activityId: String = ""
    private var code: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityToDoUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundleExtra = intent.getBundleExtra(getString(R.string.toDoActivityBundle))!!
        activityId = bundleExtra.getString(getString(R.string.activityId), "")
        code = bundleExtra.getString(getString(R.string.code), "")!!
        callGetActivityUpdateDetails(activityId, code)
        binding.backNavigation.setOnClickListener { onBackPressed() }
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
    }

    private fun callGetActivityUpdateDetails(activityId: String, code: String) {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getToDoNotesApi(activityId, code)
                .enqueue(object : Callback<TeacherActivityNotesResponse> {
                    override fun onResponse(call: Call<TeacherActivityNotesResponse>, response: Response<TeacherActivityNotesResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                val originalHomework = body.teacherActivityNoteData.activityDetail[0]
                                binding.activityLayout.title.text = originalHomework.title
                                binding.activityLayout.date.text = originalHomework.date
                                if(originalHomework.teacherName.trim().isEmpty()){
                                    binding.activityLayout.teacherName.visibility = View.GONE
                                }
                                binding.activityLayout.teacherName.text = originalHomework.teacherName
                                binding.activityLayout.imgEdit.visibility = View.GONE
                                binding.activityLayout.imgView.visibility = View.GONE

                                binding.activityRecyclerview.layoutManager = LinearLayoutManager(this@ToDoUpdateActivity)
                                binding.activityRecyclerview.adapter = ActivityUpdateAdapter(body.teacherActivityNoteData.activityNotesDetail, toDoUpdateClickListener, this@ToDoUpdateActivity)
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<TeacherActivityNotesResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, this).show()
    }

    override fun onEditClicked(activityNotesDetailItem: ActivityNotesDetailItem) {
        showDialog(activityNotesDetailItem)
    }

    private fun showDialog(activityNotesDetailItem: ActivityNotesDetailItem?){
        val builder = AlertDialog.Builder(this);
        // set the custom layout
        val dialogBinding = ActivityUpdateDialogLayoutBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        dialogBinding.bgImg.clipToOutline = true
        if (activityNotesDetailItem == null) {
            dialogBinding.title.text = "New Activity Note"
        } else {
            dialogBinding.title.text = "Update Activity Note"
            dialogBinding.commentValue.setText(activityNotesDetailItem.note)
        }
        // create and show the alert dialog
        val dialog = builder.create();
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialogBinding.txtSubmit.setOnClickListener {
            if (activityNotesDetailItem == null) {
                if (dialogBinding.commentValue.text.trim().isEmpty()) {
                    showError("Notes cannot be empty")
                    return@setOnClickListener
                }
                callNewActivityNote(dialogBinding.commentValue.text.toString().trim())
            } else {
                callUpdateActivityNote(activityNotesDetailItem.id, dialogBinding.commentValue.text.toString().trim())
            }
            dialog.dismiss()
        }
        dialogBinding.txtClose.setOnClickListener { if (dialog.isShowing) dialog.dismiss() }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun callUpdateActivityNote(activity_note_id: String, notes: String) {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.editToDoNotesApi(activityId, code, activity_note_id, notes)
                .enqueue(object : Callback<NewTeacherLeaveResponse> {
                    override fun onResponse(call: Call<NewTeacherLeaveResponse>, response: Response<NewTeacherLeaveResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                callGetActivityUpdateDetails(activityId, code)
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

    private fun callNewActivityNote(notes: String) {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.addToDoNotesApi(activityId, code, notes)
                .enqueue(object : Callback<NewTeacherLeaveResponse> {
                    override fun onResponse(call: Call<NewTeacherLeaveResponse>, response: Response<NewTeacherLeaveResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                callGetActivityUpdateDetails(activityId, code)
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