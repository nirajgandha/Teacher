package com.school.teacher

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class TeacherApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}