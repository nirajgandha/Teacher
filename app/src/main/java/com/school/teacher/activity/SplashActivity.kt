package com.school.teacher.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.school.teacher.R
import com.school.teacher.utils.Preference

class SplashActivity : AppCompatActivity() {
    val EXTERNAL_STORAGE_REQUEST = 12345
    var preference: Preference?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        preference = Preference(this)
        if (!haveStoragePermission()) {
            askForPermission()
        } else {
            startMainTask()
        }
    }

    private fun askForPermission() {
        val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions, EXTERNAL_STORAGE_REQUEST)
    }

    private fun startMainTask() {
        val id = preference!!.getString(preference!!.ID, "")
        Handler().postDelayed({
            val intent = if (id.isEmpty()) {
                preference!!.clearAllPreferenceData()
                Intent(this, LoginActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun haveStoragePermission(): Boolean{
        val readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (readPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allPerms = true
        if (requestCode == EXTERNAL_STORAGE_REQUEST) {
            for (perm in grantResults) {
                allPerms = allPerms && (perm == PackageManager.PERMISSION_GRANTED)
            }
            if (allPerms) {
                startMainTask()
            } else {
                askForPermission()
            }
        }
    }
}