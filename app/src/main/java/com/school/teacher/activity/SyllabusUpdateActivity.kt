package com.school.teacher.activity

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.adapter.SyllabusUpdatesAdapter
import com.school.teacher.databinding.ActivitySyllabusUpdateDetailsBinding
import com.school.teacher.databinding.SyllabusUpdateDialogLayoutBinding
import com.school.teacher.interfaces.SyllabusUpdateDetailClickListener
import com.school.teacher.model.*
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SyllabusUpdateActivity : AppCompatActivity(), SyllabusUpdateDetailClickListener {

    private var _binding : ActivitySyllabusUpdateDetailsBinding? = null
    private val binding get() = _binding!!
    private val syllabusUpdateDetailClickListener: SyllabusUpdateDetailClickListener = this
    private var syllabusId: String = ""
    private var code: String = ""
    private var preference: Preference? = null
    private var downloadManager: DownloadManager?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySyllabusUpdateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        syllabusId = intent.getStringExtra("syllabusId")!!
        preference = Preference(this)
        code = preference!!.getString(preference!!.code, "")
        callGetSyllabusUpdateDetails(syllabusId, code)
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

    private fun callGetSyllabusUpdateDetails(syllabusId: String, code: String) {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getSyllabusUpdateDetailsApi(syllabusId, code)
                .enqueue(object : Callback<SyllabusUpdateDetailResponse> {
                    override fun onResponse(call: Call<SyllabusUpdateDetailResponse>, response: Response<SyllabusUpdateDetailResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                val originalHomework = body.data.syllabusDetail[0]
                                binding.updateLayout.subject.text = originalHomework.subjectName
                                binding.updateLayout.title.text = originalHomework.title
                                binding.updateLayout.classNumber.text = getString(R.string.class_s, "${originalHomework.className}${originalHomework.sectionName}")
                                if (originalHomework.document.isNotEmpty()) {
                                    binding.updateLayout.imgDownload.setOnClickListener {
                                        for (documentItem in originalHomework.document) {
                                            val uri = Uri.parse(documentItem.downloadLink)
                                            val request = DownloadManager.Request(uri)
                                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                                            request.setAllowedOverRoaming(true)
                                            request.setTitle(documentItem.originalName)
                                            request.setDescription("Downloading ${documentItem.originalName}")
                                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                            request.setDestinationInExternalFilesDir(this@SyllabusUpdateActivity, Environment.DIRECTORY_DOWNLOADS, documentItem.originalName)
                                            downloadManager!!.enqueue(request)
                                        }
                                    }
                                } else {
                                    binding.updateLayout.imgDownload.visibility = View.GONE
                                }
                                binding.updateLayout.imgEdit.visibility = View.GONE
                                binding.updateLayout.imgList.visibility = View.GONE

                                binding.updateRecyclerview.layoutManager = LinearLayoutManager(this@SyllabusUpdateActivity)
                                binding.updateRecyclerview.adapter = SyllabusUpdatesAdapter(body.data.syllabusUpdateDetail, syllabusUpdateDetailClickListener, this@SyllabusUpdateActivity)
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<SyllabusUpdateDetailResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, this).show()
    }

    override fun onEditClicked(syllabusUpdateDetailItem: SyllabusUpdateDetailItem) {
        showDialog(syllabusUpdateDetailItem)
    }

    private fun showDialog(syllabusUpdateDetailItem: SyllabusUpdateDetailItem?){
        val builder = AlertDialog.Builder(this);
        // set the custom layout
        val dialogBinding = SyllabusUpdateDialogLayoutBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        dialogBinding.bgImg.clipToOutline = true
        if (syllabusUpdateDetailItem == null) {
            dialogBinding.title.text = "New Syllabus Update"
        } else {
            dialogBinding.title.text = "Edit Syllabus Update"
            dialogBinding.syllabusTitle.setText(syllabusUpdateDetailItem.title)
            dialogBinding.description.setText(syllabusUpdateDetailItem.description)
            dialogBinding.remark.setText(syllabusUpdateDetailItem.remark)
            dialogBinding.fromValue.text = syllabusUpdateDetailItem.startDate
            dialogBinding.toValue.text = syllabusUpdateDetailItem.endDate
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

            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                dialogBinding.fromValue.text = "$year-${month + 1}-$dayOfMonth"
            }, mYear, mMonth, mDay).show()
        }
        dialogBinding.fromValue.setOnClickListener {
            dialogBinding.fromCal.performClick()
        }
        dialogBinding.toValue.setOnClickListener {
            dialogBinding.toCal.performClick()
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

            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                dialogBinding.toValue.text = "$year-${month + 1}-$dayOfMonth"
            }, mYear, mMonth, mDay).show()
        }



        // create and show the alert dialog
        val dialog = builder.create();
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialogBinding.txtSubmit.setOnClickListener {
            if (dialogBinding.syllabusTitle.text.trim().isEmpty()) {
                showError("Title cannot be empty")
                return@setOnClickListener
            } else if (dialogBinding.fromValue.text.trim().isEmpty()) {
                showError("From date cannot be empty")
                return@setOnClickListener
            } else if (dialogBinding.toValue.text.trim().isEmpty()) {
                showError("To Date cannot be empty")
                return@setOnClickListener
            } else if (dialogBinding.description.text.trim().isEmpty()) {
                showError("Description cannot be empty")
                return@setOnClickListener
            } else if (dialogBinding.remark.text.trim().isEmpty()) {
                showError("Remark cannot be empty")
                return@setOnClickListener
            }
            if (syllabusUpdateDetailItem == null) {
                callNewSyllabusUpdateDetail(dialogBinding.syllabusTitle.text.toString().trim(),
                        dialogBinding.fromValue.text.toString(),
                        dialogBinding.toValue.text.toString(),
                        dialogBinding.description.text.toString(),
                        dialogBinding.remark.text.toString())
            } else {
                callEditSyllabusUpdateDetail(syllabusUpdateDetailItem.id,
                        dialogBinding.syllabusTitle.text.toString().trim(),
                        dialogBinding.fromValue.text.toString(),
                        dialogBinding.toValue.text.toString(),
                        dialogBinding.description.text.toString(),
                        dialogBinding.remark.text.toString())
            }
            dialog.dismiss()
        }
        dialogBinding.txtClose.setOnClickListener { if (dialog.isShowing) dialog.dismiss() }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun callEditSyllabusUpdateDetail(syllabus_update_id: String, syllabusTitle: String, fromDate: String, toDate: String, description: String, remark: String) {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.editSyllabusUpdateDetail(syllabusId, code, syllabusTitle, fromDate, toDate, description, remark, syllabus_update_id)
                .enqueue(object : Callback<AddEditSyllabusUpdateResponse> {
                    override fun onResponse(call: Call<AddEditSyllabusUpdateResponse>, response: Response<AddEditSyllabusUpdateResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                callGetSyllabusUpdateDetails(syllabusId, code)
                            }
                            showError(meta.message)
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<AddEditSyllabusUpdateResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun callNewSyllabusUpdateDetail(syllabusTitle: String, fromDate: String, toDate: String, description: String, remark: String) {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.addSyllabusUpdateDetail(syllabusId, code, syllabusTitle, fromDate, toDate, description, remark)
                .enqueue(object : Callback<AddEditSyllabusUpdateResponse> {
                    override fun onResponse(call: Call<AddEditSyllabusUpdateResponse>, response: Response<AddEditSyllabusUpdateResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                callGetSyllabusUpdateDetails(syllabusId, code)
                            }
                            showError(meta.message)
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<AddEditSyllabusUpdateResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }
}