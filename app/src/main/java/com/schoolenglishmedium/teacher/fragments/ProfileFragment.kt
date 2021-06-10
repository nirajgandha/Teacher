package com.schoolenglishmedium.teacher.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.activity.MainActivity
import com.schoolenglishmedium.teacher.databinding.FragmentProfileBinding
import com.schoolenglishmedium.teacher.utils.Preference

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        preference = Preference(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }

        //student details
        binding.studentNameTextView.text = "${preference!!.getString(preference!!.first_name, "")} ${preference!!.getString(preference!!.last_name, "")}"
        binding.mobileTeacher.text = getString(R.string.mobile_s, preference!!.getString(preference!!.mobileno, ""))
        binding.emailValue.text = preference!!.getString(preference!!.email , "")
        binding.dobValue.text = preference!!.getString(preference!!.dob , "")
        if (preference!!.getString(preference!!.gender, "").equals("female", true)) {
            Glide.with(requireContext()).load("").into(binding.imgStudent).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_woman, requireActivity().theme))
//            Glide.with(requireContext()).load(preference!!.getString(preference!!.IMAGE, "")).into(binding.imgStudent).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_woman, requireActivity().theme))
        } else {
            Glide.with(requireContext()).load("").into(binding.imgStudent).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_man, requireActivity().theme))
//            Glide.with(requireContext()).load(preference!!.getString(preference!!.IMAGE, "")).into(binding.imgStudent).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_man, requireActivity().theme))
        }

        //father details
        if (preference!!.getString(preference!!.father_name, "").isBlank()){
            binding.fatherRootCl.visibility = View.GONE
        } else {
            binding.fatherRootCl.visibility = View.VISIBLE
            binding.fatherName.text = preference!!.getString(preference!!.father_name, "")
            binding.fatherCall.text = preference!!.getString(preference!!.emergency_no, "")
        }

        //mother details
        binding.motherRootCl.visibility = View.GONE
        binding.guardianRootCl.visibility = View.GONE
    }
}