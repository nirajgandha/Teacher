package com.school.teacher.fragments

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.activity.MainActivity
import com.school.teacher.adapter.SyllabusAdapter
import com.school.teacher.databinding.FragmentSyllabusBinding
import com.school.teacher.interfaces.SyllabusClickListener
import com.school.teacher.model.GetSyllabusResponse
import com.school.teacher.model.Syllabus
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SyllabusFragment : Fragment(), SyllabusClickListener {

    private var _binding : FragmentSyllabusBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val syllabusClickListener: SyllabusClickListener = this
    private var downloadManager: DownloadManager?= null
    private var syllabusAdapter: SyllabusAdapter ?= null

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.syllabusRecyclerview.layoutManager = linearLayoutManager
        syllabusAdapter = SyllabusAdapter(ArrayList(), syllabusClickListener, requireContext())
        binding.syllabusRecyclerview.adapter = syllabusAdapter
        callGetSyllabusList()
    }

    private fun callGetSyllabusList() {
        /*Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        //TODO class_id and //section_id
        val classId = preference!!.getString(preference!!.class_id, "")
        val sectionId = preference!!.getString(preference!!.section_id, "")
        val loginApi: Call<GetSyllabusResponse> = apiInterface.getSyllabusListApi(classId, sectionId)
        loginApi.enqueue(object : Callback<GetSyllabusResponse> {
            override fun onResponse(call: Call<GetSyllabusResponse>, response: Response<GetSyllabusResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.syllabus_found_successful), true)) {
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

        })*/
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
}