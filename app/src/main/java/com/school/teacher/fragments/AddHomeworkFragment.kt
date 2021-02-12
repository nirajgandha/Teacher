package com.school.teacher.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.OpenableColumns
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.activity.MainActivity
import com.school.teacher.databinding.FragmentAddSyllabusBinding
import com.school.teacher.model.*
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.ArrayList


class AddHomeworkFragment : Fragment() {

    private var _binding : FragmentAddSyllabusBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val classIdList: ArrayList<String> = ArrayList()
    private val classNameList: ArrayList<String> = ArrayList()
    private val sectionIdList: ArrayList<String> = ArrayList()
    private val sectionNameList: ArrayList<String> = ArrayList()
    private val subjectIdList: ArrayList<String> = ArrayList()
    private val subjectNameList: ArrayList<String> = ArrayList()
    private var selectedClassId: String = ""
    private var selectedSectionId: String = ""
    private var selectedSubjectId: String = ""
    private var isEdit: Boolean = false
    private var syllabusId: String = ""
    private var editArrayList: ArrayList<String> = ArrayList()
    private val REQUEST_CODE_FOR_ON_ACTIVITY_RESULT: Int = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAddSyllabusBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.noOfFilesSelected.text = "0 file selected"
        val bundle = requireArguments()
        if (bundle.isEmpty) {
            binding.title.text = "Add Syllabus"
        } else {
            binding.title.text = "Update Syllabus"
            editArrayList = bundle.getStringArrayList("editSyllabus")!!
            isEdit = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callGetClassIdApi()

        (binding.classSpinner.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedClassId = classIdList[position]
        }

        (binding.sectionSpinner.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedSectionId = sectionIdList[position]
        }

        (binding.subjectSpinner.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedSubjectId = subjectIdList[position]
        }

        binding.btnSelectFiles.setOnClickListener { selectFiles() }
        binding.txtSubmit.setOnClickListener {
            if (isEdit){
                callUpdateSyllabusApi()
            } else {
                callAddSyllabusApi()
            }
        }
    }

    private fun callUpdateSyllabusApi() {
        Utils.showProgress(requireContext())
        val mediaType = "text/plain".toMediaType()
        val classId = selectedClassId.toRequestBody(mediaType)
        val sectionId = selectedSectionId.toRequestBody(mediaType)
        val subjectId = selectedSubjectId.toRequestBody(mediaType)
        val title = binding.syllabusTitle.text.toString().toRequestBody(mediaType)
        val syllabusIds = syllabusId.toRequestBody(mediaType)
        val fileList = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.listFiles()
        val files = if (fileList.isNullOrEmpty()) {
            MultipartBody.Part.createFormData("document", "", "".toRequestBody(mediaType))
        } else {
            val file = fileList[0]
            val fileBody: RequestBody = file.asRequestBody(requireContext().contentResolver.getType(file.toUri())!!.toMediaType())
            MultipartBody.Part.createFormData("document", file.name, fileBody)
        }


        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.updateSyllabusApi(classId, sectionId, subjectId, title, syllabusIds, files)
                .enqueue(object : Callback<AddUpdateSyllabusResponse> {
                    override fun onResponse(call: Call<AddUpdateSyllabusResponse>, response: Response<AddUpdateSyllabusResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            showError(meta.message)
                            if (meta.code.equals("200", true)) {
                                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.deleteRecursively()
                                (requireActivity() as MainActivity).onBackPressed()
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<AddUpdateSyllabusResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun callGetSubjectList() {
        Utils.showProgress(requireContext())
        val code = preference!!.getString(preference!!.code, "")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getSubjectListApi(code)
                .enqueue(object : Callback<SubjectResponse> {
                    override fun onResponse(call: Call<SubjectResponse>, response: Response<SubjectResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                subjectIdList.add(" ")
                                subjectNameList.add(" ")
                                for (item in body.data) {
                                    subjectIdList.add(item.id)
                                    subjectNameList.add("\t${item.name}\t")
                                }
                                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, subjectNameList)
                                (binding.subjectSpinner.editText as AutoCompleteTextView).setAdapter(adapter)
                                if (isEdit) {
                                    loadData()
                                }
                            } else {
                                showError(meta.message)
                            }
                        } else {
                            showError(response.message())
                        }
                    }

                    override fun onFailure(call: Call<SubjectResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun loadData() {
        syllabusId = editArrayList[0]
        selectedClassId = editArrayList[1]
        selectedSectionId = editArrayList[2]
        selectedSubjectId = editArrayList[3]
        binding.syllabusTitle.setText(editArrayList[4])

        (binding.classSpinner.editText as AutoCompleteTextView).setText((binding.classSpinner.editText as AutoCompleteTextView).adapter.getItem(classIdList.indexOf(selectedClassId)).toString(), false)
        (binding.sectionSpinner.editText as AutoCompleteTextView).setText((binding.sectionSpinner.editText as AutoCompleteTextView).adapter.getItem(sectionIdList.indexOf(selectedSectionId)).toString(), false)
        (binding.subjectSpinner.editText as AutoCompleteTextView).setText((binding.subjectSpinner.editText as AutoCompleteTextView).adapter.getItem(subjectIdList.indexOf(selectedSubjectId)).toString(), false)
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
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
                                callGetSectionApi()
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
                                callGetSubjectList()
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

    private fun selectFiles() {
        requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.deleteRecursively()
        binding.noOfFilesSelected.text = "0 file selected"
        val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
        filesIntent.type = "image/*"
        startActivityForResult(filesIntent, REQUEST_CODE_FOR_ON_ACTIVITY_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_ON_ACTIVITY_RESULT){
                // Checking whether data is null or not
                data?.data?.let { returnUri ->
                    val cursor = requireContext().contentResolver.query(returnUri, null, null, null, null)!!
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    val uploadFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), cursor.getString(nameIndex))
                    cursor.close()
                    var inputStream = requireContext().contentResolver.openInputStream(returnUri)
                    val outputSteam = FileOutputStream(uploadFile)
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (inputStream!!.read(buffer).also { read = it } != -1) {
                        outputSteam.write(buffer, 0, read)
                    }
                    inputStream.close()
                    inputStream = null

                    // write the output file (You have now copied the file)

                    // write the output file (You have now copied the file)
                    outputSteam.flush()
                    outputSteam.close()
                    binding.noOfFilesSelected.text = "1 file selected"
                }
            }
        }
    }

    private fun callAddSyllabusApi() {
        Utils.showProgress(requireContext())
        val mediaType = "text/plain".toMediaType()
        val classId = selectedClassId.toRequestBody(mediaType)
        val sectionId = selectedSectionId.toRequestBody(mediaType)
        val subjectId = selectedSubjectId.toRequestBody(mediaType)
        val title = binding.syllabusTitle.text.toString().toRequestBody(mediaType)
        val fileList = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.listFiles()
        val files = if (fileList.isNullOrEmpty()) {
            MultipartBody.Part.createFormData("document", "", "".toRequestBody(mediaType))
        } else {
            val file = fileList[0]
            val fileBody: RequestBody = file.asRequestBody(requireContext().contentResolver.getType(file.toUri())!!.toMediaType())
            MultipartBody.Part.createFormData("document", file.name, fileBody)
        }


        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.addSyllabusApi(classId, sectionId, subjectId, title, files)
                .enqueue(object : Callback<AddUpdateSyllabusResponse> {
            override fun onResponse(call: Call<AddUpdateSyllabusResponse>, response: Response<AddUpdateSyllabusResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    showError(meta.message)
                    if (meta.code.equals("200", true)) {
                        requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.deleteRecursively()
                        (requireActivity() as MainActivity).onBackPressed()
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<AddUpdateSyllabusResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

}