package com.yyide.chatim.fragment.schedule

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.yyide.chatim.R
import com.yyide.chatim.activity.schedule.*
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.FragmentScheduleBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.model.schedule.ScheduleDataBean
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.viewmodel.*
import org.greenrobot.eventbus.EventBus
import org.joda.time.DateTime
import java.util.ArrayList

/**
 *
 * @author liu taofab
 * @date 2021/9/7 14:19
 * @description 日程主页
 */
class ScheduleFragment2 : Fragment() {
    lateinit var fragmentScheduleBinding: FragmentScheduleBinding
    private val scheduleViewModel by activityViewModels<ScheduleMangeViewModel>()
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    val fragments = mutableListOf<Fragment>()
    var showAddScheduleV2Dialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentScheduleBinding = FragmentScheduleBinding.inflate(layoutInflater)
        return fragmentScheduleBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initFragmentContainer()
//        setFragment(0)
        setTab(0);
        fragmentScheduleBinding.btnSearch.setOnClickListener {
            startActivity(Intent(activity, ScheduleSearchActivity::class.java))
        }

        fragmentScheduleBinding.btnSetting.setOnClickListener {
            DialogUtil.showScheduleMenuDialog(
                requireContext(),
                fragmentScheduleBinding.btnSetting
            ) {
                if (it == 4) {
                    startActivity(Intent(requireContext(), ScheduleLabelManageActivity::class.java))
                    return@showScheduleMenuDialog
                }

                if (it == 5) {
                    startActivity(Intent(requireContext(), ScheduleSettingsActivity::class.java))
                    return@showScheduleMenuDialog
                }
                setFragment(it)
            }
        }
        fragmentScheduleBinding.tvCurrentDate.text = DateTime.now().toStringTime("yyyy年MM月")
        scheduleViewModel.curDateTime.observe(requireActivity(), {
            loge("更新日期：${it.toStringTime()}")
            fragmentScheduleBinding.tvCurrentDate.text = it.toStringTime("yyyy年MM月")
        })

        fragmentScheduleBinding.fab.setOnClickListener {
            //新增日程
            //DialogUtil.showAddScheduleDialog(context, this, DateTime.now())
            showAddScheduleV2Dialog = DialogUtil.showAddScheduleV2Dialog(
                context,
                this,
                scheduleEditViewModel,
                DateTime.now(),
                onScheduleAddListener
            )
            showAddScheduleV2Dialog?.setOnDismissListener {
                scheduleEditViewModel.clearData()
            }
        }

        //日程修改监听
        scheduleEditViewModel.saveOrModifyResult.observe(requireActivity(), {
            if (it) {
                scheduleEditViewModel.clearData()
            }
        })

        //收到新增日程
        scheduleViewModel.monthAddSchedule.observe(requireActivity()) {
            showAddScheduleV2Dialog = DialogUtil.showAddScheduleV2Dialog(
                context,
                this,
                scheduleEditViewModel,
                DateTime.now(),
                onScheduleAddListener
            )
            showAddScheduleV2Dialog?.setOnDismissListener {
                scheduleEditViewModel.clearData()
            }
        }
    }

    fun setTab(position: Int) {
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add("今日清单")
        mTitles.add("月视图")
        mTitles.add("列表视图")
        fragmentScheduleBinding.viewPager.isSaveEnabled = false
        fragmentScheduleBinding.viewPager.adapter = object :
            FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {

                when (position) {
                    0 -> {
                        return ScheduleTodayFragment()
                    }
                    2 -> {
                        return ScheduleListFragment()
                    }
                    1 -> {
//                        return ScheduleMonthFragment()
                        return ScheduleMonthFragment3()

                    }

                }
                return ScheduleTodayFragment()
            }

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }
        }
        fragmentScheduleBinding.slidingTabLayout.setViewPager(fragmentScheduleBinding.viewPager)
        fragmentScheduleBinding.slidingTabLayout.setCurrentTab(position) // todo  默认选中 //第一次加载设置默认
    }

    //日程新增监听
    private val onScheduleAddListener = object : DialogUtil.OnScheduleAddListener {
        override fun onFinish(view: View?) {
            loge("onFinish")
            val toScheduleDataBean = scheduleEditViewModel.toScheduleDataBean()
            loge("toScheduleDataBean=$toScheduleDataBean")
            scheduleEditViewModel.saveOrModifySchedule(false)
        }

        override fun onDate(view: View?) {
            loge("onDate")
            val intent = Intent(context, ScheduleSimpleEditionActivity::class.java)
            val toScheduleDataBean = scheduleEditViewModel.toScheduleDataBean()
            intent.putExtra("data", JSON.toJSONString(toScheduleDataBean))
            dateLauncher.launch(intent)
        }

        override fun onLabel(view: View?) {
            loge("onLabel")
            val intent = Intent(context, ScheduleAddLabelActivity::class.java)
            intent.putExtra(
                "labelList",
                JSON.toJSONString(scheduleEditViewModel.labelListLiveData.value)
            )
            labelLauncher.launch(intent)
        }

        override fun onSwitch(view: View?) {
            loge("onSwitch")
            showAddScheduleV2Dialog!!.dismiss()
//            val intent = Intent(context, ScheduleFullEditionActivity::class.java)
            val intent = Intent(context, ScheduleEditActivityMain::class.java)
            val toScheduleDataBean = scheduleEditViewModel.toScheduleDataBean()
            intent.putExtra("data", JSON.toJSONString(toScheduleDataBean))
            //type:1 新增 type空 创建
            intent.putExtra("type", "1")
            fullEditionScheduleLauncher.launch(intent)
        }
    }

    //日期回调
    private val dateLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val stringExtra = it.data?.getStringExtra("data")
                loge("$stringExtra")
                val scheduleDataBean = JSON.parseObject(stringExtra, ScheduleDataBean::class.java)
                scheduleDataBean?.toScheduleEditViewModel(scheduleEditViewModel)
            }
        }

    //标签回调
    private val labelLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val stringExtra = it.data?.getStringExtra("labelList")
                loge("$stringExtra")
                val labelListSource =
                    JSONArray.parseArray(stringExtra, LabelListRsp.DataBean::class.java)
                scheduleEditViewModel.labelListLiveData.value = labelListSource
            }
        }

    //完整版返回数据
    private val fullEditionScheduleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val booleanExtra = it.data?.getBooleanExtra("saveOrModifyResult", false) ?: false
                if (booleanExtra) {
                    loge("日程添加成功")
                    if (showAddScheduleV2Dialog != null && showAddScheduleV2Dialog?.isShowing == true) {
                        showAddScheduleV2Dialog?.dismiss()
                    }
                    //清除数据
                    scheduleEditViewModel.clearData()
                    return@registerForActivityResult
                }
                val stringExtra = it.data?.getStringExtra("data")
                loge("$stringExtra")
                val scheduleDataBean = JSON.parseObject(stringExtra, ScheduleDataBean::class.java)
                scheduleDataBean.toScheduleEditViewModel(scheduleEditViewModel)
            }
        }

    private fun setFragment(index: Int) {
        if (!(index == 0 || index == 1 || index == 2 || index == 3)) {
            return
        }
        val fm: FragmentManager = childFragmentManager
        val ft = fm.beginTransaction()
        var fg0 = fm.findFragmentByTag("f0")
        var fg1 = fm.findFragmentByTag("f1")
        var fg2 = fm.findFragmentByTag("f2")
        var fg3 = fm.findFragmentByTag("f3")
        if (fg0 != null) ft.hide(fg0)
        if (fg1 != null) ft.hide(fg1)
        if (fg2 != null) ft.hide(fg2)
        if (fg3 != null) ft.hide(fg3)

        when (index) {
            0 -> {
                if (fg0 == null) {
                    fg0 = ScheduleTodayFragment()
                    ft.add(R.id.fl_content, fg0, "f0")
                } else {
                    ft.show(fg0)
                }
            }
            3 -> {
                if (fg1 == null) {
                    fg1 = ScheduleListFragment()
                    ft.add(R.id.fl_content, fg1, "f1")
                } else {
                    ft.show(fg1)
                }
            }
            1 -> {
                if (fg2 == null) {
                    fg2 = ScheduleDayFragment()
                    ft.add(R.id.fl_content, fg2, "f2")
                } else {
                    ft.show(fg2)
                }
            }

            2 -> {
                if (fg3 == null) {
//                    fg3 = ScheduleMonthFragment()
                    fg3 = ScheduleMonthFragment3()
                    ft.add(R.id.fl_content, fg3, "f3")
                } else {
                    ft.show(fg3)
                }
            }
            else -> {
            }
        }
        ft.commitAllowingStateLoss()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        loge("onHiddenChanged $hidden")
        if (!hidden) {
            EventBus.getDefault()
                .post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA, ""))
        }
    }

    override fun onResume() {
        super.onResume()
        loge("onResume")
        EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA, ""))
    }
}