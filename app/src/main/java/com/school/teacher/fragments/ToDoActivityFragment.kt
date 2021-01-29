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
import com.school.teacher.adapter.ToDoActivityAdapter
import com.school.teacher.databinding.FragmentToDoActivityBinding
import com.school.teacher.databinding.ToDoActivityDialogLayoutBinding
import com.school.teacher.interfaces.ToDoClickListener
import com.school.teacher.model.ToDoActivityResponse
import com.school.teacher.model.ToDoActivityItem
import com.school.teacher.retrofit_api.APIClient
import com.school.teacher.retrofit_api.APIInterface
import com.school.teacher.utils.Preference
import com.school.teacher.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ToDoActivityFragment : Fragment(), ToDoClickListener {

    private var _binding : FragmentToDoActivityBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val toDoClickListener: ToDoClickListener = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentToDoActivityBinding.inflate(inflater)
        binding.imgSettings.setOnClickListener { (requireActivity() as MainActivity).startSettingsActivity() }
        binding.backNavigation.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.activityRecyclerview.layoutManager = linearLayoutManager
        callGetActivityList()
    }

    private fun callGetActivityList() {
        Utils.showProgress(requireContext())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val code = preference!!.getString(preference!!.code, "")
        apiInterface.getActivityListApi(code)
                .enqueue(object : Callback<ToDoActivityResponse> {
            override fun onResponse(call: Call<ToDoActivityResponse>, response: Response<ToDoActivityResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.message.equals(getString(R.string.activity_found_successful), true)) {
                        binding.activityRecyclerview.adapter = ToDoActivityAdapter(body.data, toDoClickListener, requireContext())
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<ToDoActivityResponse>, t: Throwable) {
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
    override fun onViewClicked(toDoActivityItem: ToDoActivityItem) {
        val builder = AlertDialog.Builder(requireContext());
        // set the custom layout
        val dialogBinding = ToDoActivityDialogLayoutBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        dialogBinding.bgImg.clipToOutline = true
        val fromHtml = Html.fromHtml(toDoActivityItem.description, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
        var end = fromHtml.indexOf("\n", fromHtml.length-4)
        if (end == -1)
            end = fromHtml.length
        dialogBinding.title.text = toDoActivityItem.title
        dialogBinding.date.text = getString(R.string.date_s, toDoActivityItem.date)
        dialogBinding.description.text = getString(R.string.description, fromHtml.subSequence(0, end))
        dialogBinding.instruction.text = getString(R.string.description, toDoActivityItem.instruction)

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