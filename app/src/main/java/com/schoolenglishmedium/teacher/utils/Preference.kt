package com.schoolenglishmedium.teacher.utils

import android.content.Context
import android.content.SharedPreferences
import com.schoolenglishmedium.teacher.R

/**
 * Preference class to use SharedPreference class through out application. Use this class to store or retrieve data from SharedPreference.
 */
class Preference(context: Context) {
    /**
     * Preference key for userId
     */
    val ID = "ID"
    val code = "code"
    val roleId = "role_id"
    val first_name = "first_name"
    val last_name = "last_name"
    val father_name = "father_name"
    val mother_name = "mother_name"
    val gender = "gender"
    val dob = "dob"
    val date_of_joining = "date_of_joining"
    val mobileno = "mobileno"
    val email = "email"
    val marital_status = "marital_status"
    val emergency_no = "emergency_no"
    val current_address = "current_address"
    val permanet_address = "permanet_address"
    val qualification = "qualification"
    val experience = "experience"

    /**
     * Shared Preference instance
     */
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE) as SharedPreferences

    fun getString(key: String, default: String): String {
        return sharedPreferences.getString(key, default)!!
    }

    fun setString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getInt(key: String, default: Int): Int {
        return sharedPreferences.getInt(key, default)
    }

    fun setInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    /**
     * Clears the all Shared Preference data
     */
    fun clearAllPreferenceData() {
        val editor = sharedPreferences.edit()
        editor.clear().commit()
    }

}