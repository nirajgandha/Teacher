package com.school.teacher.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.school.teacher.R
import com.school.teacher.utils.Preference

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val preference = Preference(this)
        val id = preference.getString(preference.ID, "")

        Handler().postDelayed({
                              val intent = if (id.isEmpty()){
                                  preference.clearAllPreferenceData()
                                  Intent(this, LoginActivity::class.java)
                              } else {
                                  Intent(this, MainActivity::class.java)
                              }
            startActivity(intent)
            finish()
        },2000)
    }
}