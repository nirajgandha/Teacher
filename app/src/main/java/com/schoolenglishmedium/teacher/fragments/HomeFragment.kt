package com.schoolenglishmedium.teacher.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.activity.MainActivity
import com.schoolenglishmedium.teacher.databinding.FragmentHomeBinding
import com.schoolenglishmedium.teacher.utils.Preference

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater)
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
        binding.studentNameTextView.text = "${preference!!.getString(preference!!.first_name, "")} ${preference!!.getString(preference!!.last_name, "")}"
        binding.homeworkCl.setOnClickListener { (requireActivity() as MainActivity).onItemClick(getString(R.string.menu_homework)) }
        binding.attendanceCl.setOnClickListener { (requireActivity() as MainActivity).openOtherFragment(AttendanceFragment()) }
        binding.noticeBoardCl.setOnClickListener { (requireActivity() as MainActivity).onItemClick(getString(R.string.menu_notice)) }
        binding.syllabusCl.setOnClickListener { (requireActivity() as MainActivity).openOtherFragment(SyllabusFragment()) }
        binding.leaveRequestCl.setOnClickListener { (requireActivity() as MainActivity).onItemClick(getString(R.string.menu_leave)) }
        binding.calendarCl.setOnClickListener { (requireActivity() as MainActivity).openOtherFragment(CalendarFragment()) }
        binding.toDoActivityCl.setOnClickListener { (requireActivity() as MainActivity).openOtherFragment(ToDoActivityFragment()) }
        binding.studentActivityCl.setOnClickListener { (requireActivity() as MainActivity).openOtherFragment(StudentActivityFragment()) }
        binding.galleryCl.setOnClickListener { (requireActivity() as MainActivity).openOtherFragment(GalleryFragment()) }
        binding.coCurriculumActivityCl.setOnClickListener { (requireActivity() as MainActivity).openOtherFragment(CoCurriculumFragment()) }
        binding.foodMenuCl.setOnClickListener { (requireActivity() as MainActivity).openOtherFragment(FoodMenuFragment()) }
    }
}