package com.yyide.chatim_pro.activity.operation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.alibaba.fastjson.JSON
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.operation.fragment.OperationDataByParentsFragment
import com.yyide.chatim_pro.activity.operation.viewmodel.OperationViewModelByParents
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.OperationFragmentBinding
import com.yyide.chatim_pro.dialog.SwitchChirldPop
import com.yyide.chatim_pro.dialog.SwitchClassAndSubjectPop2
import com.yyide.chatim_pro.dialog.SwitchClassAndSubjectPop2.SSSListener
import com.yyide.chatim_pro.model.*
import com.yyide.chatim_pro.utils.DatePickerDialogUtil
import com.yyide.chatim_pro.utils.DateUtils
import com.yyide.chatim_pro.utils.loge
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Desc 作业首页 家长身份
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationActivityByParents :
    KTBaseActivity<OperationFragmentBinding>(OperationFragmentBinding::inflate),SSSListener {

    companion object {
        private val mFragments = ArrayList<Fragment>()
        private val mTitles = arrayOf("作业数据", "作业图表")
    }

    private lateinit var viewModel: OperationViewModelByParents

    private var listclassssub = mutableListOf<selectParentStudent>()
    private var list2 = mutableListOf<selectParentStudent.Children>()
    var index:Int?=0

    private val requestServerTimeFormat = "yyyy-MM-dd"
    private val allDayTimeFormat = "yyyy-MM-dd"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(OperationViewModelByParents::class.java)

        supportFragmentManager.beginTransaction()
                .replace(binding.content.id, OperationDataByParentsFragment.newInstance()).commit()





        viewModel.startTime.observe(this) {
            binding.layoutTime.tvStartTime.text=it
        }
        viewModel.endTime.observe(this) {
            binding.layoutTime.tvEndTime.text=it
        }

        var starTime=DateUtils.getDate2(System.currentTimeMillis())
        var endTime=DateUtils.getDate2(System.currentTimeMillis())
        binding.layoutTime.tvStartTime.setText(starTime)
        binding.layoutTime.tvEndTime.setText(endTime)
        viewModel.startTime.value=starTime
        viewModel.endTime.value=endTime

        viewModel.getStudentDatas()
        binding.layoutTime.tvStartTime.setOnClickListener(View.OnClickListener {
            DatePickerDialogUtil.showDateTime(
                    this,
                    getString(R.string.select_begin_time2),
                    viewModel.startTime.value,
                    startTimeListener,
                    isAllDay = viewModel.allDayLiveData.value == true
            )
        })

        binding.layoutTime.tvEndTime.setOnClickListener(View.OnClickListener {
            DatePickerDialogUtil.showDateTime(
                    this,
                    getString(R.string.select_begin_time2),
                    viewModel.endTime.value,
                    startTimeListener2,
                    isAllDay = viewModel.allDayLiveData.value == true
            )
        })
        binding.tv1.setOnClickListener(View.OnClickListener {
            val pop = SwitchChirldPop(this,listclassssub)
            pop.setSelectClasses {date: selectParentStudent? ->
                if (date!=null){
                    binding.tv1.text=date.name
                    viewModel.studentId.value=date.id
                    viewModel.classesId.value=date.classesId
                    viewModel.studentId.postValue(it.id.toString())
                    for (i in listclassssub.indices) {
                        if (viewModel.studentId.equals(listclassssub[i].id)){

                            list2= listclassssub[i].children as MutableList<selectParentStudent.Children>

                            setFirstRightData(list2)

                        }
                    }
                }
            }
        })

        binding.tv2.setOnClickListener(View.OnClickListener {

            if (list2!=null&&list2.size>0){
                val pop = SwitchClassAndSubjectPop2(this,list2)
                pop.setSSS(this)
            }
        })



        viewModel.StudentDatasList.observe(this){
            if (it.isSuccess){
                if (it.getOrNull()!=null){
                    val  list=it.getOrNull()
                    loge("list==" +JSON.toJSONString(list) )
                    listclassssub= list as MutableList<selectParentStudent>
                    Log.e("TAG", "onCreate: "+JSON.toJSONString(listclassssub) )
                    if (!list.isNullOrEmpty()) {

                       /* Log.e("TAG", "viewModel.name: "+JSON.toJSONString(listclassssub.get(0).name) )
                        Log.e("TAG", "viewModel.studentId: "+JSON.toJSONString(listclassssub.get(0).id) )
                        Log.e("TAG", "viewModel.classesId: "+JSON.toJSONString(listclassssub.get(0).classesId) )*/


                        list2= listclassssub[0].children as MutableList<selectParentStudent.Children>
                        Log.e("TAG", "setFirstRightData: "+JSON.toJSONString(list2) )

                        setFirstRightData(list2)

                        binding.tv1.text=listclassssub.get(0).name
                        viewModel.studentId.value=listclassssub.get(0).id
                        viewModel.studentId.postValue(listclassssub.get(0).id)
                        viewModel.classesId.value=listclassssub.get(0).classesId


                        viewModel.startTime.value=binding.layoutTime.tvStartTime.text.toString()
                        viewModel.endTime.value=binding.layoutTime.tvEndTime.text.toString()

                    }

                }
            }

        }

    }
    fun setFirstRightData(list:List<selectParentStudent.Children>){
        var lj=list.get(0).name;

        var sub:String?=""
        if (list.get(0).children.size>0){
            sub=list.get(0).children[0].name
            viewModel.subjectId.value=list.get(0).children[0].id
        }

        binding.tv2.text=lj+sub
    }



    private val startTimeListener =
            OnDateSetListener { _: TimePickerDialog?, millSeconds: Long ->
                var startTime = DateUtils.switchTime(Date(millSeconds), requestServerTimeFormat)
                viewModel.allDayLiveData.value?.let {
                    if (it) {
                        startTime =
                                "${DateUtils.switchTime(Date(millSeconds), allDayTimeFormat)} 00:00:00"
                    }
                }
                viewModel.startTime.value = startTime
            }
    private val startTimeListener2 =
            OnDateSetListener { _: TimePickerDialog?, millSeconds: Long ->
                var startTime = DateUtils.switchTime(Date(millSeconds), requestServerTimeFormat)
                viewModel.allDayLiveData.value?.let {
                    if (it) {
                        startTime =
                                "${DateUtils.switchTime(Date(millSeconds), allDayTimeFormat)} 00:00:00"
                    }
                }
                viewModel.endTime.value = startTime
            }







      override fun initView() {

        binding.top.title.text = getString(R.string.operation_title)
        binding.top.backLayout.setOnClickListener { this?.finish() }



        binding.layoutTime.tvToDay.setOnClickListener {
            if (!binding.layoutTime.tvToDay.isChecked) {
                setCheck()
                var starTime=DateUtils.getDate2(System.currentTimeMillis())
                var endTime=DateUtils.getDate2(System.currentTimeMillis())
                binding.layoutTime.tvStartTime.setText(starTime)
                binding.layoutTime.tvEndTime.setText(endTime)
                viewModel.startTime.value=starTime
                viewModel.endTime.value=endTime
                binding.layoutTime.tvToDay.isChecked = true
            }
        }
        binding.layoutTime.tvWeek.setOnClickListener {
            if (!binding.layoutTime.tvWeek.isChecked) {
                setCheck()

                 var  starTime = DateUtils.getDate2(DateUtils.getBeginDayOfWeek2().time)
                 var  endTime = DateUtils.getDate2(DateUtils.getEndDayOfWeek2().time)

                binding.layoutTime.tvStartTime.setText(starTime)
                binding.layoutTime.tvEndTime.setText(endTime)
                viewModel.startTime.value=starTime
                viewModel.endTime.value=endTime
                binding.layoutTime.tvWeek.isChecked = true
            }
        }
        binding.layoutTime.tvLastWeek.setOnClickListener {
            if (!binding.layoutTime.tvLastWeek.isChecked) {
                setCheck()
                var  starTime = DateUtils.getDate2(DateUtils.getBeginDayOfLastWeek().time)
                var  endTime = DateUtils.getDate2(DateUtils.getEndDayOfLastWeek().time)

                binding.layoutTime.tvStartTime.setText(starTime)
                binding.layoutTime.tvEndTime.setText(endTime)
                viewModel.startTime.value=starTime
                viewModel.endTime.value=endTime
                binding.layoutTime.tvLastWeek.isChecked = true
            }
        }
        binding.layoutTime.tvMonth.setOnClickListener {
            if (!binding.layoutTime.tvMonth.isChecked) {
                setCheck()

                var  starTime = DateUtils.getDate2(DateUtils.getBeginDayOfMonth().time)
                var  endTime = DateUtils.getDate2(DateUtils.getEndDayOfMonth().time)

                binding.layoutTime.tvStartTime.setText(starTime)
                binding.layoutTime.tvEndTime.setText(endTime)
                viewModel.startTime.value=starTime
                viewModel.endTime.value=endTime
                binding.layoutTime.tvMonth.isChecked = true
            }
        }
        binding.layoutTime.tvLastMonth.setOnClickListener {
            if (!binding.layoutTime.tvLastMonth.isChecked) {
                setCheck()
                var  starTime = DateUtils.getDate2(DateUtils.getBeginDayOfMonth2().time)
                var  endTime = DateUtils.getDate2(DateUtils.getEndDayOfMonth2().time)
                binding.layoutTime.tvStartTime.setText(starTime)
                binding.layoutTime.tvEndTime.setText(endTime)
                viewModel.startTime.value=starTime
                viewModel.endTime.value=endTime
                binding.layoutTime.tvLastMonth.isChecked = true
            }
        }
    }

    private fun setCheck() {
        binding.layoutTime.tvToDay.isChecked = false
        binding.layoutTime.tvWeek.isChecked = false
        binding.layoutTime.tvLastWeek.isChecked = false
        binding.layoutTime.tvMonth.isChecked = false
        binding.layoutTime.tvLastMonth.isChecked = false
    }

//    private fun tl_3() {
//        mFragments.add(OperationDataByParentsFragment.newInstance())
//        mFragments.add(OperationChartFragment.newInstance())
//        val viewPager: ViewPager = binding.viewpager
//        viewPager.adapter = MyPagerAdapter(supportFragmentManager)
//        binding.mTabLayout.setTabData(mTitles)
//        binding.mTabLayout.setOnTabSelectListener(object : OnTabSelectListener {
//            override fun onTabSelect(position: Int) {
//                viewPager.currentItem = position
//            }
//
//            override fun onTabReselect(position: Int) {}
//        })
//        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(
//                    position: Int,
//                    positionOffset: Float,
//                    positionOffsetPixels: Int
//            ) {
//            }
//
//            override fun onPageSelected(position: Int) {
//                binding.mTabLayout.currentTab = position
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {}
//        })
//        viewPager.currentItem = 0
//    }

    private class MyPagerAdapter(fm: FragmentManager?) :
            FragmentPagerAdapter(fm!!) {
        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles.get(position)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments.get(position)
        }
    }

    override fun setRT(date1: selectParentStudent.Children?, date2: selectParentStudent.Children?) {
        if (date1!=null&&date2!=null){
            Log.e("TAG", "setSelectClasses: "+JSON.toJSONString(date1.name+"==>"+date2.name+"==》") )
            binding.tv2.text=date1.name+date2.name
            viewModel.subjectId.value=date2.id
            viewModel.subjectId.postValue(date2.id)
//                        viewModel.subjectId.value=date2.id

        }
    }


}