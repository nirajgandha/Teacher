package com.school.teacher.fragments

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.activity.MainActivity
import com.school.teacher.activity.SyllabusUpdateActivity
import com.school.teacher.adapter.AttendanceAdapter
import com.school.teacher.adapter.SyllabusAdapter
import com.school.teacher.databinding.FragmentSyllabusBinding
import com.school.teacher.interfaces.SyllabusClickListener
import com.school.teacher.model.*
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class SyllabusFragment : Fragment(), SyllabusClickListener {

    private var _binding : FragmentSyllabusBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val syllabusClickListener: SyllabusClickListener = this
    private var downloadManager: DownloadManager?= null
    private var syllabusAdapter: SyllabusAdapter ?= null
    val classIdList: ArrayList<String> = ArrayList()
    val classNameList: ArrayList<String> = ArrayList()
    val sectionIdList: ArrayList<String> = ArrayList()
    val sectionNameList: ArrayList<String> = ArrayList()
    var selectedClassId: String = ""
    var selectedSectionId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
        downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSyllabusBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.fab.setOnClickListener { startAddSyllabusFragment(null) }
        binding.fab.setOnLongClickListener { v ->
            v.setOnTouchListener { view, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_MOVE -> {
                        view.x = event.rawX + (binding.fab.layoutParams.width.div(2))
                        view.y = event.rawY + (binding.fab.layoutParams.height.div(2))
                    }
                    MotionEvent.ACTION_UP -> view.setOnTouchListener(null)
                    else -> {
                    }
                }
                true
            }
            true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callGetClassIdApi()
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.syllabusRecyclerview.layoutManager = linearLayoutManager
        syllabusAdapter = SyllabusAdapter(ArrayList(), syllabusClickListener, requireContext())
        binding.syllabusRecyclerview.adapter = syllabusAdapter

        (binding.classSpinner.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            selectedClassId = classIdList[position]
            if( selectedClassId != " "){
                callGetSectionApi()
            }
            syllabusAdapter!!.refreshList(ArrayList())
        }

        (binding.sectionSpinner.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            selectedSectionId = sectionIdList[position]
            syllabusAdapter!!.refreshList(ArrayList())
            if (selectedSectionId.isNotEmpty() && selectedClassId.isNotEmpty()) {
                callGetSyllabusList()
            }
        }
    }

    private fun callGetSyllabusList() {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val loginApi: Call<GetSyllabusResponse> = apiInterface.getSyllabusListApi(selectedClassId, selectedSectionId)
        loginApi.enqueue(object : Callback<GetSyllabusResponse> {
            override fun onResponse(call: Call<GetSyllabusResponse>, response: Response<GetSyllabusResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.code.equals("200", true)) {
                       syllabusAdapter!!.refreshList(body.data)
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<GetSyllabusResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
    }

    override fun onDownloadClicked(syllabus: Syllabus) {
        val homeworkDocuments = syllabus.document
        for (documentItem in homeworkDocuments) {
            val uri = Uri.parse(documentItem.downloadLink)
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(true)
            request.setTitle(documentItem.originalName)
            request.setDescription("Downloading ${documentItem.originalName}")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/${getString(R.string.app_name)}/${documentItem.originalName}")
            downloadManager!!.enqueue(request)
        }
    }

    override fun onEditClicked(syllabus: Syllabus) {
        startAddSyllabusFragment(syllabus)
    }

    override fun onViewClicked(syllabus: Syllabus) {
        val intent = Intent(requireContext(), SyllabusUpdateActivity::class.java)
        intent.putExtra("syllabusId", syllabus.id)
        startActivity(intent)
    }

    private fun callGetClassIdApi() {
        Utils.showProgress(requireContext())
        val code = preference!!.getString(preference!!.code, "")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getClassListApi(code)
                .enqueue(object : Callback<ClassListResponse> {
                    override fun onResponse(call: Call<ClassListResponse>, response: Response<ClassListResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                classIdList.add(" ")
                                classNameList.add(" ")
                                for (item in body.data) {
                                    classIdList.add(item.id)
                                    classNameList.add("\t${item.mmemberClass}\t")
                                }
                                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, classNameList)
                                (binding.classSpinner.editText as AutoCompleteTextView).setAdapter(adapter)
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<ClassListResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun callGetSectionApi() {
        Utils.showProgress(requireContext())
        val code = preference!!.getString(preference!!.code, "")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getSectionListApi(code)
                .enqueue(object : Callback<SectionListResponse> {
                    override fun onResponse(call: Call<SectionListResponse>, response: Response<SectionListResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                sectionIdList.clear()
                                sectionNameList.clear()
                                sectionIdList.add("")
                                sectionNameList.add("Section")
                                for (item in body.data) {
                                    sectionIdList.add(item.id)
                                    sectionNameList.add(item.section)
                                }
                                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, sectionNameList)
                                (binding.sectionSpinner.editText as AutoCompleteTextView).setAdapter(adapter)
                                Handler().postDelayed({
                                    (binding.sectionSpinner.editText as AutoCompleteTextView).setText((binding.sectionSpinner.editText as AutoCompleteTextView).adapter.getItem(0).toString(), false)
                                    selectedSectionId =sectionIdList[0]
                                },300)
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<SectionListResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    fun startAddSyllabusFragment(syllabus: Syllabus?) {
        val addSyllabusFragment = AddSyllabusFragment()
        val bundle = Bundle()
        if (syllabus != null) {
            val syllabusList: ArrayList<String> = ArrayList()
            syllabusList.add(syllabus.id)
            syllabusList.add(syllabus.classId)
            syllabusList.add(syllabus.sectionId)
            syllabusList.add(syllabus.subjectId)
            syllabusList.add(syllabus.title)
            bundle.putStringArrayList("editSyllabus", syllabusList)
        }
        addSyllabusFragment.arguments = bundle
        (requireActivity() as MainActivity).openOtherFragment(addSyllabusFragment)
    }
}