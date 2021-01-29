package com.school.teacher.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.school.teacher.R
import com.school.teacher.databinding.ActivityLoginBinding
import com.school.teacher.model.LoginResponse
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private var token: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDeviceTokenFirebase()
        binding.btnLogin.setOnClickListener {
            val validationError = validateMobileNumber()
            if (validationError == getString(R.string.empty_string)){
                callLoginApi()
            } else {
                showError(validationError)
            }
        }
    }

    private fun callLoginApi() {
        Utils.showProgress(this)
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val loginApi: Call<LoginResponse> = apiInterface.loginApi(binding.edtMobile.text.toString(), getString(R.string.device_type), token!!)
        loginApi.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null){
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.login_successful_message), true)){
                        val data = body.data
                        val bundle = Bundle()
                        bundle.putString(getString(R.string.phone), data.phone)
                        bundle.putString(getString(R.string.code), data.code)
                        bundle.putString(getString(R.string.otp), data.otp)
                        val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                        intent.putExtra(getString(R.string.otpBundle), bundle)
                        startActivity(intent)
                        finish()
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

    private fun validateMobileNumber(): String {
        return when {
            binding.edtMobile.text.isEmpty() -> {
                getString(R.string.mobile_empty_error)
            }
            !binding.edtMobile.text.isDigitsOnly() -> {
                getString(R.string.enter_mobile_only)
            }
            binding.edtMobile.text.length != 10 -> {
                getString(R.string.enter_mobile_number_complete)
            }
            else -> {
                getString(R.string.empty_string)
            }
        }
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, this).show()
    }

    private fun getDeviceTokenFirebase(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(LoginActivity::class.toString(), "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result.toString()
            Preference(this).setString("token", token.toString())
            // Log and toast
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }
}