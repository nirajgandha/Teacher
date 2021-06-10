package com.schoolenglishmedium.teacher.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.schoolenglishmedium.teacher.R
import com.schoolenglishmedium.teacher.adapter.MenuAdapter
import com.schoolenglishmedium.teacher.customui.SpanningLinearLayoutManager
import com.schoolenglishmedium.teacher.databinding.ActivityMainBinding
import com.schoolenglishmedium.teacher.fragments.*
import com.schoolenglishmedium.teacher.interfaces.ItemClickListener
import com.schoolenglishmedium.teacher.model.Menu
import com.schoolenglishmedium.teacher.utils.Preference


class MainActivity : AppCompatActivity(), ItemClickListener {
    private var _activityMainBinding: ActivityMainBinding? = null
    private val activityMainBinding get() = _activityMainBinding!!
    private var menuArrayList: ArrayList<Menu>? = null
    private var itemClickListener: ItemClickListener? = null
    private var menuAdapter: MenuAdapter? = null
    private var selectedItem = 0
    private var selectedFragment: Fragment? = null
    private var doubleBackToExitPressedOnce = false
    private var resumeFragment: String? = null
    private var preference: Preference? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(this)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        resumeFragment = getString(R.string.menu_home)
        activityMainBinding.plusButton.setOnClickListener { openOtherFragment(ProfileFragment()) }
        loadData()
    }

    private fun loadData() {
        itemClickListener = this
        menuArrayList = ArrayList()

        var menu = Menu()
        menu.menuId = 1
        menu.menuName = getString(R.string.menu_home)
        menu.activeIconPath = R.drawable.ic_home_active
        menu.inActiveIconPath = R.drawable.ic_home_inactive
        menu.isSelected = false
        menuArrayList!!.add(menu)

        menu = Menu()
        menu.menuId = 1
        menu.menuName = getString(R.string.menu_homework)
        menu.activeIconPath = R.drawable.ic_homework_active
        menu.inActiveIconPath = R.drawable.ic_homework_inactive
        menu.isSelected = false
        menuArrayList!!.add(menu)

        menuArrayList!!.add(Menu())

        menu = Menu()
        menu.menuId = 1
        menu.menuName = getString(R.string.menu_leave)
        menu.activeIconPath = R.drawable.ic_slumber_active
        menu.inActiveIconPath = R.drawable.ic_slumber_inactive
        menu.isSelected = false
        menuArrayList!!.add(menu)

        menu = Menu()
        menu.menuId = 1
        menu.menuName = getString(R.string.menu_notice)
        menu.activeIconPath = R.drawable.ic_notice_active
        menu.inActiveIconPath = R.drawable.ic_notice_inactive
        menu.isSelected = false
        menuArrayList!!.add(menu)

        menuAdapter = MenuAdapter(menuArrayList!!, itemClickListener as MainActivity, this@MainActivity)

        val spanningLinearLayoutManager = SpanningLinearLayoutManager(this, menuAdapter!!.itemCount)
        spanningLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        spanningLinearLayoutManager.setScrollHorizontally(false)
        spanningLinearLayoutManager.setMaxItemsToShowInScreen(5)
        activityMainBinding.bottomRecyclerView.layoutManager = spanningLinearLayoutManager
        activityMainBinding.bottomRecyclerView.adapter = menuAdapter
        onItemClick(resumeFragment!!)
    }

    override fun onItemClick(menu_name: String) {
        if (menu_name == getString(R.string.menu_home)) {
            if (preference!!.getString(preference!!.ID,"").isEmpty()) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                if (selectedFragment !is HomeFragment) {
                    openFragment(HomeFragment(), false)
                }
                setSelected(0)
            }
        } else if (menu_name == getString(R.string.menu_homework)) {
            if (preference!!.getString(preference!!.ID,"").isEmpty()) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                if (selectedFragment !is HomeWorkFragment) {
                    openFragment(HomeWorkFragment(), false)
                }
                setSelected(1)
            }
        } else if (menu_name == getString(R.string.menu_leave)) {
            if (preference!!.getString(preference!!.ID,"").isEmpty()) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                if (selectedFragment !is LeaveRequestFragment) {
                    openFragment(LeaveRequestFragment(), false)
                }
                setSelected(3)
            }
        } else if (menu_name == getString(R.string.menu_notice)) {
            if (preference!!.getString(preference!!.ID,"").isEmpty()) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                if (selectedFragment !is NoticeFragment) {
                    openFragment(NoticeFragment(), false)
                }
                setSelected(4)
            }
        }
    }

    fun backPressFromOtherFragment(){
        if (selectedFragment is AddSyllabusFragment){
            openOtherFragment(SyllabusFragment())
        } else if (selectedFragment is AddHomeworkFragment){
            openOtherFragment(HomeWorkFragment())
        } else if (selectedFragment is AddNoticeFragment){
            openOtherFragment(NoticeFragment())
        } else if (selectedFragment is AddStudentActivityFragment){
            openOtherFragment(StudentActivityFragment())
        }  else {
            onItemClick(getString(R.string.menu_home))
        }
    }

    fun openOtherFragment(fragment: Fragment) {
        openFragment(fragment, false)
        setSelected(2)
    }

    /**
     * Item Selected Color Change
     *
     * @param index selected item index
     */
    private fun setSelected(index: Int) {
        if (menuArrayList != null && menuArrayList!!.size > 0) {
            selectedItem = index
            for (i in menuArrayList!!.indices) {
                val menu = menuArrayList!![i]
                menu.isSelected = i == index
                menuArrayList!![i] = menu
            }
            if (menuAdapter != null) {
                menuAdapter!!.refreshList(menuArrayList!!)
            }
        }
    }

    /**
     * Open Fragment
     *
     * @param fragment Fragment whichever has to be opened
     * @param addToStack boolean whether fragment has to be added to back-stack
     */
    fun openFragment(fragment: Fragment, addToStack: Boolean) {
        selectedFragment = fragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (addToStack) {
            transaction.addToBackStack(fragment.javaClass.name)
        }
        transaction.commit()
    }

    var toast: Toast? = null
    override fun onBackPressed() {
        if (selectedFragment is HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                if (toast != null)
                    toast!!.cancel()
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            toast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG)
            toast!!.show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            backPressFromOtherFragment()
        }
    }

    fun startSettingsActivity() {
        val bundle = Bundle()
//        val intent = Intent(this, SelectStudentActivity::class.java)
//        intent.putExtra(getString(R.string.selectStudentBundle), bundle)
//        startActivity(intent)
    }
}