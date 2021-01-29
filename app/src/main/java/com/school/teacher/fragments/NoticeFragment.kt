package com.school.teacher.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.teacher.R
import com.school.teacher.activity.MainActivity
import com.school.teacher.adapter.NoticeAdapter
import com.school.teacher.databinding.FragmentNoticeBinding
import com.school.teacher.databinding.NoticeDialogLayoutBinding
import com.school.teacher.interfaces.NoticeClickListener
import com.school.teacher.model.Notice
import com.school.teacher.model.NoticeResponse
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NoticeFragment : Fragment(), NoticeClickListener {

    private var _binding : FragmentNoticeBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val noticeClickListener: NoticeClickListener = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentNoticeBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.noticeRecyclerview.layoutManager = linearLayoutManager
        callGetNoticeList()
    }

    private fun callGetNoticeList() {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val code = preference!!.getString(preference!!.code, "")
        val noticeapi: Call<NoticeResponse> = apiInterface.getNoticeListApi(code)
        noticeapi.enqueue(object : Callback<NoticeResponse> {
            override fun onResponse(call: Call<NoticeResponse>, response: Response<NoticeResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.notice_found_successful), true)) {
                        binding.noticeRecyclerview.adapter = NoticeAdapter(body.data, noticeClickListener, requireContext())
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<NoticeResponse>, t: Throwable) {
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
    override fun onViewClicked(notice: Notice) {
        val builder = AlertDialog.Builder(requireContext());
        // set the custom layout
        val dialogBinding = NoticeDialogLayoutBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        dialogBinding.bgImg.clipToOutline = true
        val fromHtml = Html.fromHtml(notice.title, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
        var end = fromHtml.indexOf("\n", fromHtml.length-4)
        if (end == -1)
            end = fromHtml.length
        dialogBinding.title.text = fromHtml.subSequence(0, end)
        dialogBinding.publishDate.text = notice.publishDate
        dialogBinding.message.text = notice.message

        // create and show the alert dialog
        val dialog = builder.create();
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialogBinding.txtClose.setOnClickListener { if (dialog.isShowing) dialog.dismiss() }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}