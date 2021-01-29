package com.school.teacher.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.school.teacher.R
import com.school.teacher.databinding.ActivityOtpBinding
import com.school.teacher.model.LoginResponse
import com.school.teacher.model.TeacherProfileResponse
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import com.school.teacher.utils.SaveTeacherPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpActivity : AppCompatActivity() {

    private var _binding : ActivityOtpBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundleExtra = intent.getBundleExtra(getString(R.string.otpBundle))!!
        val phone: String = bundleExtra.getString(getString(R.string.phone),"")
        val code = bundleExtra.getString(getString(R.string.code), "")!!
        val otp = bundleExtra.getString(getString(R.string.otp), "")!!
        binding.edtOtp.text = Editable.Factory.getInstance().newEditable(otp)
        binding.btnVerify.setOnClickListener {
            callVerifyApi(phone, code, otp)
        }
    }

    private fun callVerifyApi(phone: String, code: String, otp: String) {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.verifyOtpApi(phone, code, otp)
        .enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null){
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.otp_successful_message), true)){
                        callGetTeacherDetails(phone, code)
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun callGetTeacherDetails(phone: String, code: String) {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getTeacherProfileApi(phone, code)
                .enqueue(object : Callback<TeacherProfileResponse> {
                    override fun onResponse(call: Call<TeacherProfileResponse>, response: Response<TeacherProfileResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null){
                            val meta = body.meta
                            if (meta.message.equals(getString(R.string.teacher_found_successful_message), true)){
                                val data = body.data
                                val preference = Preference(this@OtpActivity)
                                SaveTeacherPreferences.saveTeacherPreference(this@OtpActivity, code, data[0])
                                val bundle = Bundle()
                                val intent = Intent(this@OtpActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<TeacherProfileResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, this).show()
    }
}