package com.yyide.chatim.activity.operation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.alibaba.fastjson.JSON
import com.flyco.tablayout.listener.OnTabSelectListener
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.yyide.chatim.R
import com.yyide.chatim.activity.operation.fragment.OperationChartFragment
import com.yyide.chatim.activity.operation.fragment.OperationDataByParentsFragment
import com.yyide.chatim.activity.operation.fragment.OperationDataByTeacherFragment
import com.yyide.chatim.activity.operation.viewmodel.OperationViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.OperationFragmentBinding
import com.yyide.chatim.dialog.SwitchClassAndSubjectPop
import com.yyide.chatim.model.SubjectBean
import com.yyide.chatim.model.getClassSubjectListRsp
import com.yyide.chatim.model.rightData
import com.yyide.chatim.model.selectBean
import com.yyide.chatim.utils.DatePickerDialogUtil
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.view.DialogUtil
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Desc 作业首页
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationActivityByTeacher :
    KTBaseActivity<OperationFragmentBinding>(OperationFragmentBinding::inflate) {

    companion object {
        private val mFragments = ArrayList<Fragment>()
        private val mTitles = arrayOf("作业数据", "作业图表")
    }

    private lateinit var viewModel: OperationViewModel


    private var sublist = mutableListOf<SubjectBean>()
    private var listclassssub = mutableListOf<getClassSubjectListRsp>()
    private var pageNum = 1
    private var pageSize = 20
    private val requestServerTimeFormat = "yyyy-MM-dd HH:mm"
    private val allDayTimeFormat = "yyyy-MM-dd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(OperationViewModel::class.java)

        supportFragmentManager.beginTransaction()
                .replace(binding.content.id, OperationDataByTeacherFragment.newInstance()).commit()
//        tl_3()
        initData()


        // 监听开始时间变化
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
            DialogUtil.showWorkTypeWorkSelect(this, DialogUtil.OnClassListener {
                Log.e("TAG", "OnClassListener: "+JSON.toJSONString(it) )
                binding.tv1.text=it.name
                viewModel.leftData.value=it
            },viewModel)
        })
        binding.tv2.setOnClickListener(View.OnClickListener {

            val switchClassAndSubjectPop = SwitchClassAndSubjectPop(this,listclassssub)
            switchClassAndSubjectPop.setSelectClasses { date1: getClassSubjectListRsp?, date2: getClassSubjectListRsp?, date3: getClassSubjectListRsp ->

                if (date1!=null&&date2!=null&&date3!=null){
                    Log.e("TAG", "setSelectClasses: "+JSON.toJSONString(date1.name+"==>"+date2.name+"==》"+date3.name) )
                    binding.tv2.text=date1.name+date2.name+date3.name
                    viewModel.classesId.value=date2.id
                    viewModel.subjectId.value=date3.id
                    val rightData=rightData(date1.toString(), date2.toString(), date3.toString())
                    viewModel.rightData.postValue(rightData)
                }

            }
        })


        viewModel.leftData.observe(this){

            Log.e("TAG", "tv1data.observe: "+JSON.toJSONString(it) )
            binding.tv1.text=it.name
//            viewModel.tv1data.value=it
            viewModel.getClassSubjectList(it.type)
        }


        viewModel.getClassSubjectListRsp.observe(this){



            val result = it.getOrNull()

            if (result!=null) {
                listclassssub= result as MutableList<getClassSubjectListRsp>
                Log.e("TAG", "getClassSubjectListRsp: "+ JSON.toJSONString(listclassssub))
                if (listclassssub.size>0){
                    var first1: getClassSubjectListRsp?=null
                    var first2: getClassSubjectListRsp?=null
                    var first3: getClassSubjectListRsp?=null

                    first1=listclassssub[0]
                    viewModel.ljName.value=first1.name
                    if (listclassssub[0].children.size>0){
                        first2=listclassssub[0].children[0]
                        viewModel.classesId.value=first2.id
                        viewModel.classesName.value=first2.name
                    }
                    if (listclassssub[0].children[0].children.size>0){
                        first3=listclassssub[0].children[0].children[0];
                        viewModel.subjectId.value=first3.id
                        viewModel.subjectName.value=first3.name
                    }
                    val rightData=rightData(first1.name, first2?.id.toString(), first3?.id.toString())

                    viewModel.rightData.postValue(rightData)
                    binding.tv2.text=viewModel.ljName.value+viewModel.classesName.value+viewModel.subjectName.value
                    viewModel.startTime.value=binding.layoutTime.tvStartTime.text.toString()
                    viewModel.endTime.value=binding.layoutTime.tvEndTime.text.toString()
//                    getTeacherData(pageNum,pageSize)
                }

            }

        }



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






    fun initData(){
        val selectBean = selectBean()
        selectBean.check=true;
        selectBean.name="我发布的"
        selectBean.type="1"
        viewModel.leftData.value=selectBean
    }



      override fun initView() {


          binding.top.title.text = getString(R.string.operation_title)
        binding.top.ivRight.visibility=View.VISIBLE

        binding.top.backLayout.setOnClickListener { this?.finish() }

          binding.top.ivRight.setOnClickListener(View.OnClickListener {
              val intent = Intent(this, OperationCreatWorkActivity::class.java)
              startActivity(intent)
          })
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
//        mFragments.add(OperationDataByTeacherFragment.newInstance())
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


}