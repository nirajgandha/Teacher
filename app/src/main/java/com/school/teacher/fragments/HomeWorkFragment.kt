package com.school.teacher.fragments

import android.app.Activity.RESULT_OK
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.activity.HomeworkUpdateActivity
import com.school.teacher.activity.MainActivity
import com.school.teacher.activity.OtpActivity
import com.school.teacher.adapter.HomeworkAdapter
import com.school.teacher.databinding.FragmentHomeWorkBinding
import com.school.teacher.interfaces.HomeWorkClickListener
import com.school.teacher.model.GetHomeworkResponse
import com.school.teacher.model.Homework
import com.school.teacher.model.UploadHomeworkResponse
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
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class HomeWorkFragment : Fragment(), HomeWorkClickListener {

    private var _binding : FragmentHomeWorkBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val homeWorkClickListener: HomeWorkClickListener = this
    private var homeworkArrayList: ArrayList<Homework>? = null
    private var homeworkAdapter: HomeworkAdapter? = null
    private var downloadManager: DownloadManager?= null
    private val REQUEST_CODE_FOR_ON_ACTIVITY_RESULT: Int = 1234
    private var homework: Homework ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
        downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeWorkBinding.inflate(inflater)
        homeworkArrayList = ArrayList()
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.homeworkRecyclerview.layoutManager = linearLayoutManager
        callGetHomeworkListApi()
    }

    private fun callGetHomeworkListApi() {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val code = preference!!.getString(preference!!.code, "")
        val loginApi: Call<GetHomeworkResponse> = apiInterface.getHomeWorkListApi(code)
        loginApi.enqueue(object : Callback<GetHomeworkResponse> {
            override fun onResponse(call: Call<GetHomeworkResponse>, response: Response<GetHomeworkResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.homework_found_successful), true)) {
                        binding.homeworkRecyclerview.adapter = HomeworkAdapter(body.data, homeWorkClickListener, requireContext())
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<GetHomeworkResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
    }

    override fun onViewClicked(homework: Homework) {

        val bundle = Bundle()
        bundle.putString(getString(R.string.homeworkId), homework.id)
        bundle.putString(getString(R.string.code), preference!!.getString(preference!!.code, ""))
        val intent = Intent(requireActivity(), HomeworkUpdateActivity::class.java)
        intent.putExtra(getString(R.string.homeworkUpdateBundle), bundle)
        startActivity(intent)
    }

    override fun onDownloadClicked(homework: Homework) {
        val homeworkDocuments = homework.document
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

    override fun onUploadClicked(homework: Homework) {
        this.homework = homework
        val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
        filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        filesIntent.type = "image/*"
        startActivityForResult(filesIntent, REQUEST_CODE_FOR_ON_ACTIVITY_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_ON_ACTIVITY_RESULT){
                // Checking whether data is null or not
                if (data != null) {
                    val homeworkId = homework!!.id
                    val code = preference!!.getString(preference!!.code, "")
                    Log.d("niraj", "onActivityResult: ${homework!!.id} $code")
                    // Checking for selection multiple files or single.
                    val fileArray: Array<MultipartBody.Part?>
                    if (data.clipData != null) {
                        fileArray = arrayOfNulls(data.clipData!!.itemCount)
                        // Getting the length of data and logging up the logs using index
                        for (index in 0 until data.clipData!!.itemCount) {
                            val uri = data.clipData!!.getItemAt(index).uri
                            val parcelFileDescriptor: ParcelFileDescriptor =
                                    requireContext().contentResolver.openFileDescriptor(uri, "r")!!
                            val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
                            val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                            parcelFileDescriptor.close()
                            val time = Calendar.getInstance().time.toString().replace(" ","_")
                            val file = convertBitmapToFile("${homework!!.subjectName}_${homework!!.className}${homework!!.sectionName}_$time.png", image)
                            val fileBody: RequestBody = file.asRequestBody(requireContext().contentResolver.getType(uri)!!.toMediaType())
                            fileArray[index] = MultipartBody.Part.createFormData("upload_files[]", file.name, fileBody)
                        }
                    } else {
                        fileArray = arrayOfNulls(1)
                        // Getting the URI of the selected file and logging into logcat at debug level
                        val uri: Uri = data.data!!
                        val parcelFileDescriptor: ParcelFileDescriptor =
                                requireContext().contentResolver.openFileDescriptor(uri, "r")!!
                        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
                        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                        parcelFileDescriptor.close()
                        val time = Calendar.getInstance().time.toString().replace(" ","_")
                        val file = convertBitmapToFile("${homework!!.subjectName}_${homework!!.className}${homework!!.sectionName}_$time.png", image)
                        val fileBody: RequestBody = file.asRequestBody(requireContext().contentResolver.getType(uri)!!.toMediaType())
                        fileArray[0] = MultipartBody.Part.createFormData("upload_files[]", file.name, fileBody)
                    }
                    val mediaType = "text/plain".toMediaType()
                    callUploadHomeWorkApi(preference!!.getString(preference!!.code, "").toRequestBody(mediaType),
                            homeworkId!!.toRequestBody(mediaType), fileArray)
                }
            }
        }
    }

    private fun callUploadHomeWorkApi(student_id: RequestBody, homework_id: RequestBody, fileArray: Array<MultipartBody.Part?>) {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val loginApi: Call<UploadHomeworkResponse> = apiInterface.uploadHomeworkApi(student_id, homework_id, fileArray)
        loginApi.enqueue(object : Callback<UploadHomeworkResponse> {
            override fun onResponse(call: Call<UploadHomeworkResponse>, response: Response<UploadHomeworkResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.homework_upload_successful), true)) {
//                        requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.deleteRecursively()
                        callGetHomeworkListApi()
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<UploadHomeworkResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}