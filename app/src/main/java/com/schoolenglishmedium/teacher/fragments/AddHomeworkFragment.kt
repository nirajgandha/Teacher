package com.schoolenglishmedium.teacher.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.activity.MainActivity
import com.schoolenglishmedium.teacher.databinding.FragmentAddHomeWorkBinding
import com.schoolenglishmedium.teacher.model.*
import com.schoolenglishmedium.teacher.retrofit_api.APIClient
import com.schoolenglishmedium.teacher.retrofit_api.APIInterface
import com.schoolenglishmedium.teacher.utils.Preference
import com.schoolenglishmedium.teacher.utils.Utils
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
import java.util.*
import kotlin.collections.ArrayList


class AddHomeworkFragment : Fragment() {

    private var _binding : FragmentAddHomeWorkBinding? = null
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
    private var homeworkId: String = ""
    private var editArrayList: ArrayList<String> = ArrayList()
    private val requestCodeForOnActivityResult: Int = 1234
    private var isForStudent:Boolean = false


    private var studentArray: Array<String> = emptyArray()
    private var studentIdArray: Array<String> = emptyArray()
    private var selectedStudentArray: BooleanArray = BooleanArray(1)
    private var mUserItems: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAddHomeWorkBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.noOfFilesSelected.text = getString(R.string.number_of_files_selected, 0)
        val bundle = requireArguments()
        if (bundle.isEmpty) {
            binding.title.text = getString(R.string.add_homework_title)
            startAdd()
        } else {
            binding.title.text = getString(R.string.update_homework_title)
            editArrayList = bundle.getStringArrayList("editHomework")!!
            isEdit = true
            isForStudent = !(editArrayList[4].isEmpty() || editArrayList[4] == "[]")
            startUpdate()
        }

        binding.radioGrp.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == binding.rbtClass.id) {
                isForStudent = false
                binding.studentTv.visibility = View.GONE
            } else {
                isForStudent = true
                binding.studentTv.visibility = View.VISIBLE
            }
            resetEverything()
        }

        binding.studentTv.setOnClickListener { showStudentDialog() }

        return binding.root
    }

    private fun startUpdate() {
        callGetClassIdApi()
    }

    private fun startAdd() {
        binding.rbtClass.isChecked = true
        binding.studentTv.visibility = View.GONE
        callGetClassIdApi()
    }

    private fun resetEverything() {
        (binding.classSpinner.editText as AutoCompleteTextView).setText("")
        (binding.sectionSpinner.editText as AutoCompleteTextView).setText("")
        (binding.subjectSpinner.editText as AutoCompleteTextView).setText("")
        binding.studentTv.text = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (binding.classSpinner.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedClassId = classIdList[position]
            if (isForStudent) {
                binding.studentTv.text = ""
            }
            (binding.sectionSpinner.editText as AutoCompleteTextView).setText("")
            (binding.subjectSpinner.editText as AutoCompleteTextView).setText("")
        }

        (binding.sectionSpinner.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedSectionId = sectionIdList[position]
            (binding.subjectSpinner.editText as AutoCompleteTextView).setText("")
            if (isForStudent && selectedClassId.trim().isNotBlank() && selectedSectionId.trim().isNotEmpty()) {
                callGetStudentListApi()
            }
        }

        (binding.subjectSpinner.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedSubjectId = subjectIdList[position]
        }

        binding.submitDateCal.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (binding.submitDateValue.text.toString().isNotEmpty()) {
                val splitter = binding.submitDateValue.text.toString().split("-")
                calendar.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                calendar.set(Calendar.YEAR, splitter[0].toInt())
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.submitDateValue.text = getString(R.string.date_argument_string, year, month+1, dayOfMonth)

            }, mYear, mMonth, mDay).show()
        }
        binding.submitDateValue.setOnClickListener {
            binding.submitDateCal.performClick()
        }

        binding.homeworkDateCal.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (binding.homeworkDateValue.text.toString().isNotEmpty()) {
                val splitter = binding.homeworkDateValue.text.toString().split("-")
                calendar.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                calendar.set(Calendar.YEAR, splitter[0].toInt())
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.homeworkDateValue.text = getString(R.string.date_argument_string, year, month+1, dayOfMonth)
            }, mYear, mMonth, mDay).show()
        }
        binding.homeworkDateValue.setOnClickListener {
            binding.homeworkDateCal.performClick()
        }

        binding.btnSelectFiles.setOnClickListener { selectFiles() }
        binding.txtSubmit.setOnClickListener {
            if (selectedClassId.trim().isEmpty()) {
                showError("Select Class")
                return@setOnClickListener
            }
            if (selectedSectionId.trim().isEmpty()) {
                showError("Select Section")
                return@setOnClickListener
            }
            if (isForStudent && binding.studentTv.text.toString().trim().isEmpty()) {
                showError("Select Student")
                return@setOnClickListener
            }
            if (selectedSubjectId.trim().isEmpty()) {
                showError("Select Subject")
                return@setOnClickListener
            }
            if (binding.submitDateValue.text.trim().isEmpty()) {
                showError("Select Submission Date")
                return@setOnClickListener
            }
            if (binding.homeworkDateValue.text.trim().isEmpty()) {
                showError("Select Homework Date")
                return@setOnClickListener
            }
            /*if (!isEdit && requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.listFiles().isNullOrEmpty()) {
                showError("Please select the file to be uploaded.")
                return@setOnClickListener
            }*/

            if (isEdit){
                callUpdateHomeworkApi()
            } else {
                callAddHomeworkApi()
            }
        }
    }

    private fun callGetStudentListApi() {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getStudentListApi(selectedClassId, selectedSectionId)
                .enqueue(object : Callback<GetAllStudentListResponse> {
                    override fun onResponse(call: Call<GetAllStudentListResponse>, response: Response<GetAllStudentListResponse>) {
                        Utils.hideProgress()
                        val body = response.body()
                        if (body != null) {
                            val meta = body.meta
                            if (meta.code.equals("200", true)) {
                                studentArray = Array(body.getAllStudentListData.studentList.size) { "" }
                                studentIdArray = Array(body.getAllStudentListData.studentList.size) { "" }
                                selectedStudentArray = BooleanArray(body.getAllStudentListData.studentList.size)
                                for (index in 0 until body.getAllStudentListData.studentList.size) {
                                    val items = body.getAllStudentListData.studentList[index]
                                    studentArray[index] = items.fullName
                                    studentIdArray[index] = items.id
                                }
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

                    override fun onFailure(call: Call<GetAllStudentListResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun callUpdateHomeworkApi() {
        Utils.showProgress(requireContext())
        val mediaType = "text/plain".toMediaType()
        val code = preference!!.getString(preference!!.code, "").toRequestBody(mediaType)
        val homeworkFor = (if (isForStudent) "2" else "1").toRequestBody(mediaType)
        val classId = selectedClassId.toRequestBody(mediaType)
        val sectionId = selectedSectionId.toRequestBody(mediaType)
        val subjectId = selectedSubjectId.toRequestBody(mediaType)
        val studentsId = if (isForStudent) {
            getStudentArrayList().toRequestBody(mediaType)
        } else {
            "".toRequestBody(mediaType)
        }
        val homeworkDate = binding.homeworkDateValue.text.toString().toRequestBody(mediaType)
        val submitDate = binding.submitDateValue.text.toString().toRequestBody(mediaType)
        val description = binding.description.text.toString().toRequestBody(mediaType)
        val homeworkId1 = homeworkId.toRequestBody(mediaType)
        val fileList = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.listFiles()
        val files = if (fileList.isNullOrEmpty()) {
            MultipartBody.Part.createFormData("document", "", "".toRequestBody(mediaType))
        } else {
            val file = fileList[0]
            val fileBody: RequestBody = file.asRequestBody(MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)!!.toMediaType())
            MultipartBody.Part.createFormData("document", file.name, fileBody)
        }


        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.updateHomework(code, homeworkFor, classId, sectionId, subjectId, studentsId, homeworkDate, submitDate, description, homeworkId1, files)
                .enqueue(object : Callback<AddHomeworkResponse> {
                    override fun onResponse(call: Call<AddHomeworkResponse>, response: Response<AddHomeworkResponse>) {
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

                    override fun onFailure(call: Call<AddHomeworkResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun getStudentArrayList(): String {
        var studentLists = ""
        studentLists += "["
        for (item in mUserItems) {
            studentLists +="\"$item\","
        }
        val finalList = StringBuilder(studentLists)
        finalList.insert(studentLists.length-1, "]")
        return finalList.substring(0, finalList.length-1)
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
                                    if (isForStudent){
                                        binding.radioGrp.check(binding.rbtStudent.id)
                                        selectedClassId = editArrayList[1]
                                        selectedSectionId = editArrayList[2]
                                        callGetStudentListApi()
                                    } else {
                                        binding.radioGrp.check(binding.rbtClass.id)
                                        loadData()
                                    }
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
        homeworkId = editArrayList[0]
        selectedClassId = editArrayList[1]
        selectedSectionId = editArrayList[2]
        selectedSubjectId = editArrayList[3]
        binding.description.setText(editArrayList[5])
        binding.submitDateValue.text = editArrayList[6]
        binding.homeworkDateValue.text = editArrayList[7]

        (binding.classSpinner.editText as AutoCompleteTextView).setText((binding.classSpinner.editText as AutoCompleteTextView).adapter.getItem(classIdList.indexOf(selectedClassId)).toString(), false)
        (binding.sectionSpinner.editText as AutoCompleteTextView).setText((binding.sectionSpinner.editText as AutoCompleteTextView).adapter.getItem(sectionIdList.indexOf(selectedSectionId)).toString(), false)
        (binding.subjectSpinner.editText as AutoCompleteTextView).setText((binding.subjectSpinner.editText as AutoCompleteTextView).adapter.getItem(subjectIdList.indexOf(selectedSubjectId)).toString(), false)
        if (isForStudent) {
            setStudentsSelected(editArrayList[4])
        }
    }

    private fun setStudentsSelected(s: String) {
        if (s == "" || s == "[]") return
        var replaced = s.replace("[","", true)
        replaced = replaced.replace("]","", true)
        replaced = replaced.replace("\"","", true)
        val splits = replaced.split(",")
        var string = ""
        for (item in splits) {
            selectedStudentArray[studentIdArray.indexOf(item)] = true
            mUserItems.add(item)
            string += "${studentArray[studentIdArray.indexOf(item)]}\n"
        }
        binding.studentTv.text = string
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
        binding.noOfFilesSelected.text = getString(R.string.number_of_files_selected, 0)
        val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
        filesIntent.type = "image/*"
        startActivityForResult(filesIntent, requestCodeForOnActivityResult)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestCodeForOnActivityResult){
                // Checking whether data is null or not
                data?.data?.let { returnUri ->
                    val cursor = requireContext().contentResolver.query(returnUri, null, null, null, null)!!
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    val uploadFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), cursor.getString(nameIndex))
                    cursor.close()
                    val inputStream = requireContext().contentResolver.openInputStream(returnUri)
                    val outputSteam = FileOutputStream(uploadFile)
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (inputStream!!.read(buffer).also { read = it } != -1) {
                        outputSteam.write(buffer, 0, read)
                    }
                    inputStream.close()

                    // write the output file (You have now copied the file)

                    // write the output file (You have now copied the file)
                    outputSteam.flush()
                    outputSteam.close()
                    binding.noOfFilesSelected.text = getString(R.string.number_of_files_selected, 1)
                }
            }
        }
    }

    private fun callAddHomeworkApi() {
        Utils.showProgress(requireContext())
        val mediaType = "text/plain".toMediaType()
        val code = preference!!.getString(preference!!.code, "").toRequestBody(mediaType)
        val homeworkFor = (if (isForStudent) "2" else "1").toRequestBody(mediaType)
        val classId = selectedClassId.toRequestBody(mediaType)
        val sectionId = selectedSectionId.toRequestBody(mediaType)
        val subjectId = selectedSubjectId.toRequestBody(mediaType)
        val studentsId = if (isForStudent) {
            getStudentArrayList().toRequestBody(mediaType)
        } else {
            "".toRequestBody(mediaType)
        }
        val homeworkDate = binding.homeworkDateValue.text.toString().toRequestBody(mediaType)
        val submitDate = binding.submitDateValue.text.toString().toRequestBody(mediaType)
        val description = binding.description.text.toString().toRequestBody(mediaType)
        val fileList = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.listFiles()
        val files = if (fileList.isNullOrEmpty()) {
            MultipartBody.Part.createFormData("document", "", "".toRequestBody(mediaType))
        } else {
            val file = fileList[0]
            val fileBody: RequestBody = file.asRequestBody(MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)!!.toMediaType())
            MultipartBody.Part.createFormData("document", file.name, fileBody)
        }


        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.addHomework(code, homeworkFor, classId, sectionId, subjectId, studentsId, homeworkDate, submitDate, description, files)
                .enqueue(object : Callback<AddHomeworkResponse> {
                    override fun onResponse(call: Call<AddHomeworkResponse>, response: Response<AddHomeworkResponse>) {
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

                    override fun onFailure(call: Call<AddHomeworkResponse>, t: Throwable) {
                        Utils.hideProgress()
                        showError("Error occurred!! Please try again later")
                        t.printStackTrace()
                    }

                })
    }

    private fun showStudentDialog() {
        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setTitle("Select Students")
        mBuilder.setPositiveButton("Done") { dialog, _ -> dialog.dismiss() }
        mBuilder.setMultiChoiceItems(studentArray, selectedStudentArray) { _, which, isChecked ->
            if (isChecked) {
                mUserItems.add(studentIdArray[which])
            } else {
                mUserItems.remove(studentIdArray[which])
            }
        }

        mBuilder.setOnDismissListener {
            var item = ""

            for (index in 0 until mUserItems.size) {
                for (index1 in studentIdArray.indices) {
                    if (studentIdArray[index1] == mUserItems[index]) {
                        item += "${studentArray[index1]}\n"
                    }
                }

            }
            binding.studentTv.text = item
        }

        val mDialog = mBuilder.create()
        mDialog.show()
    }

}