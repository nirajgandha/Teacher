package com.school.teacher.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.adapter.HomeworkUpdateAdapter
import com.school.teacher.databinding.ActivityHomeWorkUpdateBinding
import com.school.teacher.databinding.HomeworkDialogLayoutBinding
import com.school.teacher.interfaces.HomeWorkUpdateClickListener
import com.school.teacher.model.HomeWorkUpdateResponse
import com.school.teacher.model.HomeworkUpdateDetailItem
import com.school.teacher.model.UpdateHomeworkResponse
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeworkUpdateActivity : AppCompatActivity(), HomeWorkUpdateClickListener {

    private var _binding : ActivityHomeWorkUpdateBinding? = null
    private val binding get() = _binding!!
    private val homeWorkUpdateClickListener: HomeWorkUpdateClickListener = this
    private var homeworkId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeWorkUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backNavigation.setOnClickListener { onBackPressed() }
        val bundleExtra = intent.getBundleExtra(getString(R.string.homeworkUpdateBundle))!!
        homeworkId = bundleExtra.getString(getString(R.string.homeworkId),"")
        val code = bundleExtra.getString(getString(R.string.code), "")!!
        callGetHomeworkUpdateDetails(homeworkId, code)
    }

    private fun callGetHomeworkUpdateDetails(homeworkId: String, code: String) {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getHomeworkUpdateApi(homeworkId, code)
                .enqueue(object : Callback<HomeWorkUpdateResponse> {
                    override fun onResponse(call: Call<HomeWorkUpdateResponse>, response: Response<HomeWorkUpdateResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null){
                            val meta = body.meta
                            if (meta.code.equals("200", true)){
                                val originalHomework = body.data.homeworkDetail[0]
                                binding.homeworkLayout.subject.text = originalHomework.subjectName
                                binding.homeworkLayout.classNumber.text = resources.getString(R.string.str_class_section, "${originalHomework.className}${originalHomework.sectionName}")
                                binding.homeworkLayout.createdOnValue.text = originalHomework.homeworkDate
                                binding.homeworkLayout.createdByValue.text = resources.getString(R.string.str_created_by, originalHomework.createdBy)
                                binding.homeworkLayout.submissionOnValue.text = originalHomework.submitDate
                                binding.homeworkLayout.evaluationOnValue.text = originalHomework.evaluationDate
                                binding.homeworkLayout.evaluatedByValue.text = resources.getString(R.string.str_evaluated_by, originalHomework.evaluatedByName)
                                binding.homeworkLayout.imgUpload.visibility = View.GONE
                                binding.homeworkLayout.imgDownload.visibility = View.GONE
                                binding.homeworkLayout.imgView.visibility = View.GONE
                                binding.homeworkLayout.viewLabel.visibility = View.GONE
                                binding.homeworkLayout.homeworkStatus.visibility = View.GONE

                                binding.homeworkRecyclerview.layoutManager = LinearLayoutManager(this@HomeworkUpdateActivity)
                                binding.homeworkRecyclerview.adapter = HomeworkUpdateAdapter(body.data.homeworkUpdateDetail, homeWorkUpdateClickListener, this@HomeworkUpdateActivity)
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<HomeWorkUpdateResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, this).show()
    }

    override fun onViewClicked(homeworkUpdateDetailItem: HomeworkUpdateDetailItem) {
        val intent = Intent( this, SlideShowActivity::class.java)
        intent.putStringArrayListExtra(getString(R.string.galleryImagesUrlList), homeworkUpdateDetailItem.uploadFiles)
        startActivity(intent)
    }

    override fun onEditClicked(homeworkUpdateDetailItem: HomeworkUpdateDetailItem) {
        val builder = AlertDialog.Builder(this);
        // set the custom layout
        val dialogBinding = HomeworkDialogLayoutBinding.inflate(layoutInflater)
        var selectedId = 0
        builder.setView(dialogBinding.root)
        dialogBinding.bgImg.clipToOutline = true
        dialogBinding.title.text = "Update Homework Status"

        ArrayAdapter.createFromResource(
                this,
                R.array.status_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            dialogBinding.statusSpinner.adapter = adapter
            dialogBinding.statusSpinner.setSelection(0)
        }
        dialogBinding.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedId = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        // create and show the alert dialog
        val dialog = builder.create();
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialogBinding.txtSubmit.setOnClickListener {
            callSubmitStatus(selectedId, dialogBinding.commentValue.text.toString(), homeworkUpdateDetailItem.homeworkId, homeworkUpdateDetailItem.id)
            dialog.dismiss()
        }
        dialogBinding.txtClose.setOnClickListener { if (dialog.isShowing) dialog.dismiss() }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun callSubmitStatus(selectedId: Int, comment: String, homeworkId: String, homework_updated_id: String) {
        Utils.showProgress(this)
        val preference = Preference(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val code = preference.getString(preference.code, "")
        apiInterface.updateHomeworkStatusApi(code, homeworkId, homework_updated_id, comment, selectedId.toString())
                .enqueue(object : Callback<UpdateHomeworkResponse> {
                    override fun onResponse(call: Call<UpdateHomeworkResponse>, response: Response<UpdateHomeworkResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null){
                            val meta = body.meta
                            if (meta.code.equals("200", true)){
                                Handler().postDelayed({
                                    callGetHomeworkUpdateDetails(homeworkId, code)
                                },150)
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<UpdateHomeworkResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }
}