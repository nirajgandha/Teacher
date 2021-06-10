package com.schoolenglishmedium.teacher.fragments

import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.activity.MainActivity
import com.schoolenglishmedium.teacher.adapter.FoodMenuAdapter
import com.schoolenglishmedium.teacher.databinding.FoodMenuDialogLayoutBinding
import com.schoolenglishmedium.teacher.databinding.FragmentFoodMenuBinding
import com.schoolenglishmedium.teacher.interfaces.FoodMenuClickListener
import com.schoolenglishmedium.teacher.model.FoodMenu
import com.schoolenglishmedium.teacher.model.FoodMenuResponse
import com.schoolenglishmedium.teacher.retrofit_api.APIClient
import com.schoolenglishmedium.teacher.retrofit_api.APIInterface
import com.schoolenglishmedium.teacher.utils.Preference
import com.schoolenglishmedium.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FoodMenuFragment : Fragment(), FoodMenuClickListener {

    private var _binding : FragmentFoodMenuBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val foodMenuClickListener: FoodMenuClickListener = this
    private var downloadManager: DownloadManager?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
        downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFoodMenuBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.foodRecyclerView.layoutManager = linearLayoutManager
        callGetFoodMenuList()
    }

    private fun callGetFoodMenuList() {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val code = preference!!.getString(preference!!.code, "")
        val foodMenuapi: Call<FoodMenuResponse> = apiInterface.getFoodMenuListApi(code)
        foodMenuapi.enqueue(object : Callback<FoodMenuResponse> {
            override fun onResponse(call: Call<FoodMenuResponse>, response: Response<FoodMenuResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.food_menu_found_successful), true)) {
                        binding.foodRecyclerView.adapter = FoodMenuAdapter(body.data, foodMenuClickListener, requireContext())
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<FoodMenuResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string, requireContext()).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewClicked(foodMenu: FoodMenu) {
        val builder = AlertDialog.Builder(requireContext());
        // set the custom layout
        val dialogBinding = FoodMenuDialogLayoutBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        dialogBinding.bgImg.clipToOutline = true
        val fromHtml = Html.fromHtml(foodMenu.title, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
        var end = fromHtml.indexOf("\n", fromHtml.length-4)
        if (end == -1)
            end = fromHtml.length
        dialogBinding.title.text = fromHtml.subSequence(0, end)
        val fromHtml1 = Html.fromHtml(foodMenu.description, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
        var end1 = fromHtml.indexOf("\n", fromHtml.length-4)
        if (end1 == -1)
            end1 = fromHtml1.length
        dialogBinding.message.text = fromHtml1.subSequence(0, end1)

        // create and show the alert dialog
        val dialog = builder.create();
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialogBinding.txtClose.setOnClickListener { if (dialog.isShowing) dialog.dismiss() }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onDownloadClicked(foodMenu: FoodMenu) {
        val foodMenuDocuments = foodMenu.document
        for (documentItem in foodMenuDocuments) {
            val uri = Uri.parse(documentItem.downloadLink)
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(true)
            request.setTitle(documentItem.originalName)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDescription("Downloading ${documentItem.originalName}")
            request.setDestinationInExternalFilesDir(requireContext(), Environment.DIRECTORY_DOWNLOADS, documentItem.originalName)
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/${getString(R.string.app_name)}/${documentItem.originalName}")
            downloadManager!!.enqueue(request)
        }
    }
}