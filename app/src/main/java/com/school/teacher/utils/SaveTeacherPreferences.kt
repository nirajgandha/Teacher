package com.school.teacher.utils

import android.content.Context
import com.school.teacher.model.TeacherProfileData

object SaveTeacherPreferences {
    fun saveTeacherPreference(context: Context, code: String, teacher: TeacherProfileData){
        val preference = Preference(context)
        preference.setString(preference.ID, teacher.id)
        preference.setString(preference.code, code)
        preference.setString(preference.roleId, teacher.roleId)
        preference.setString(preference.first_name, teacher.firstName)
        preference.setString(preference.last_name, teacher.lastName)
        preference.setString(preference.father_name, teacher.fatherName)
        preference.setString(preference.mother_name, teacher.motherName)
        preference.setString(preference.gender, teacher.gender)
        preference.setString(preference.dob, teacher.dob)
        preference.setString(preference.date_of_joining, teacher.dateOfJoining)
        preference.setString(preference.mobileno, teacher.mobileno)
//        preference.setString(preference.email, teacher.email)
        preference.setString(preference.marital_status, teacher.maritalStatus)
        preference.setString(preference.emergency_no, teacher.emergencyNo)
        preference.setString(preference.current_address, teacher.currentAddress)
        preference.setString(preference.permanet_address, teacher.permanetAddress)
        preference.setString(preference.qualification, teacher.qualification)
        preference.setString(preference.experience, teacher.experience)
    }
}