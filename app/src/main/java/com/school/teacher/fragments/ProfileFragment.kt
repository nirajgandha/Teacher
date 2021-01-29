package com.school.teacher.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.school.teacher.R
import com.school.teacher.activity.MainActivity
import com.school.teacher.databinding.FragmentProfileBinding
import com.school.teacher.utils.Preference

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
//        binding.classNumber.text = getString(R.string.class_s,"${preference!!.getString(preference!!.className, "")}-${preference!!.getString(preference!!.sectionName, "")}")
//        binding.rollValue.text = preference!!.getString(preference!!.ROLL_NO , "")
//        binding.dobValue.text = preference!!.getString(preference!!.DOB , "")
        if (preference!!.getString(preference!!.gender, "").equals("female", true)) {
            Glide.with(requireContext()).load("").into(binding.imgStudent).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_woman, requireActivity().theme))
//            Glide.with(requireContext()).load(preference!!.getString(preference!!.IMAGE, "")).into(binding.imgStudent).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_woman, requireActivity().theme))
        } else {
            Glide.with(requireContext()).load("").into(binding.imgStudent).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_man, requireActivity().theme))
//            Glide.with(requireContext()).load(preference!!.getString(preference!!.IMAGE, "")).into(binding.imgStudent).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_man, requireActivity().theme))
        }

        /*//father details
        if (preference!!.getString(preference!!.father_name, "").isBlank()){
            binding.fatherRootCl.visibility = View.GONE
        } else {
            binding.fatherRootCl.visibility = View.VISIBLE
            binding.fatherName.text = preference!!.getString(preference!!.father_name, "")
            binding.fatherCall.text = preference!!.getString(preference!!.father_phone, "")
            binding.fatherBusinness.text = preference!!.getString(preference!!.father_occupation, "")
            Glide.with(requireContext()).load(preference!!.getString(preference!!.father_pic, "")).into(binding.imgFather).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_man, requireActivity().theme))
        }

        //mother details
        if (preference!!.getString(preference!!.mother_name, "").isBlank()){
            binding.motherRootCl.visibility = View.GONE
        } else {
            binding.motherRootCl.visibility = View.VISIBLE
            binding.motherName.text = preference!!.getString(preference!!.mother_name, "")
            binding.motherCall.text = preference!!.getString(preference!!.mother_phone, "")
            binding.motherBusinness.text = preference!!.getString(preference!!.mother_occupation, "")
            Glide.with(requireContext()).load(preference!!.getString(preference!!.mother_pic, "")).into(binding.imgMother).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_woman, requireActivity().theme))
        }*/

        /*//guadian details
        if (preference!!.getString(preference!!.guardian_name, "").isBlank()){
            binding.guardianRootCl.visibility = View.GONE
        } else {
            binding.guardianRootCl.visibility = View.VISIBLE
            binding.guardianName.text = preference!!.getString(preference!!.guardian_name, "")
            binding.guardianCall.text = preference!!.getString(preference!!.guardian_phone, "")
            binding.guardianBusinness.text = preference!!.getString(preference!!.guardian_occupation, "")
            Glide.with(requireContext()).load(preference!!.getString(preference!!.guardian_pic, "")).into(binding.imgGuardian).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_man, requireActivity().theme))
        }*/
    }
}