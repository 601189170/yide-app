package com.yyide.chatim.activity.operation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.alibaba.fastjson.JSON
import com.flyco.tablayout.listener.OnTabSelectListener
import com.yyide.chatim.R
import com.yyide.chatim.activity.operation.viewmodel.OperationViewModel
import com.yyide.chatim.databinding.OperationFragmentBinding
import com.yyide.chatim.dialog.SwitchClassAndSubjectPop
import com.yyide.chatim.dialog.SwitchNoticeTimePop
import com.yyide.chatim.model.SubjectBean
import com.yyide.chatim.model.WorkBean
import com.yyide.chatim.model.getClassSubjectListRsp
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.model.schedule.SchoolCalendarRsp
import com.yyide.chatim.model.selectBean
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.widget.WheelView

/**
 * @Desc 作业-家长
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationFragment : Fragment(){

    companion object {
        private val mFragments = ArrayList<Fragment>()
        private val mTitles = arrayOf("作业数据", "作业图表")
        fun newInstance() = OperationFragment()
    }

    private lateinit var viewModel: OperationViewModel
    private lateinit var viewBinding: OperationFragmentBinding

    private var sublist = mutableListOf<SubjectBean>()
    private var listclassssub = mutableListOf<getClassSubjectListRsp>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = OperationFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationViewModel::class.java)
        initView()
        tl_3()
        initData()
        // 请求数据
//        viewModel.getTecherWorkList("0","","","","","","")
        val stime=viewBinding.layoutTime.tvStartTime.text
        val etime=viewBinding.layoutTime.tvEndTime.text
        viewBinding.tv1.setOnClickListener(View.OnClickListener {
            DialogUtil.showWorkTypeWorkSelect(activity,DialogUtil.OnClassListener {
                Log.e("TAG", "OnClassListener: "+JSON.toJSONString(it) )
                viewBinding.tv1.text=it.name
                viewModel.tv1data.value=it
                getTeacherData()
            },viewModel)
        })
        viewBinding.tv2.setOnClickListener(View.OnClickListener {

            val switchClassAndSubjectPop = SwitchClassAndSubjectPop(activity,listclassssub)
            switchClassAndSubjectPop.setSelectClasses { date1: getClassSubjectListRsp?, date2: getClassSubjectListRsp?,date3:getClassSubjectListRsp ->

                if (date1!=null&&date2!=null&&date3!=null){
                    Log.e("TAG", "setSelectClasses: "+JSON.toJSONString(date1.name+"==>"+date2.name+"==》"+date3.name) )
                    viewBinding.tv2.text=date1.name+date2.name+date3.name
                    viewModel.classesId.value=date2.id
                    viewModel.subjectId.value=date3.id

                    getTeacherData()
                }
//                viewModel.tv1data.value?.let { it1 -> viewModel.getTecherWorkList(it1.type,viewModel.subjectId,viewModel.classesId,viewModel.subjectId,stime,etime) }

            }
        })


        viewModel.tv1data.observe(viewLifecycleOwner){

            Log.e("TAG", "tv1data.observe: "+JSON.toJSONString(it) )
            viewBinding.tv1.text=it.name
//            viewModel.tv1data.value=it
            viewModel.getClassSubjectList(it.type)
        }
        viewModel.SubjectBeanData.observe(viewLifecycleOwner){
            val  fd =it.getOrNull();
            if (fd!=null){
                sublist= fd as MutableList<SubjectBean>

            }
        }

        viewModel.getClassSubjectListRsp.observe(viewLifecycleOwner){
            Log.e("TAG", "getClassSubjectListRsp: "+ JSON.toJSONString(it))


                val result = it.getOrNull()

                if (result!=null) {
                    listclassssub= result as MutableList<getClassSubjectListRsp>
                    if (listclassssub.size>0){
                        val first1:getClassSubjectListRsp
                        val first2:getClassSubjectListRsp
                        val first3:getClassSubjectListRsp



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

                            viewBinding.tv2.text=viewModel.ljName.value+viewModel.classesName.value+viewModel.subjectName.value


                        viewModel.startTime.value=viewBinding.layoutTime.tvStartTime.text.toString()
                        viewModel.endTime.value=viewBinding.layoutTime.tvEndTime.text.toString()

                            getTeacherData()





                    }



                    Log.e("TAG", "getClassSubjectListRsp==》: "+ JSON.toJSONString(listclassssub))

                }

        }


    }
    fun getTeacherData(){
        val wh=viewModel.tv1data.value?.type
        val classesId=viewModel.classesId.value
        val subjectId=viewModel.subjectId.value
        val startTime=viewModel.startTime.value
        val endTime=viewModel.endTime.value
        if (wh!=null&&classesId!=null&&subjectId!=null&&startTime!=null&&endTime!=null){
            viewModel.getTecherWorkList(wh,subjectId,classesId,startTime,endTime)
        }
    }
    fun initData(){
        val selectBean = selectBean()
        selectBean.check=true;
        selectBean.name="我发布的"
        selectBean.type="1"
        viewModel.tv1data.value=selectBean



    }

    private fun initView() {

        viewBinding.top.title.text = getString(R.string.operation_title)
        viewBinding.top.backLayout.setOnClickListener { activity?.finish() }
        viewBinding.top.ivEdit.setOnClickListener { }
        viewBinding.top.ivRight.setOnClickListener { }


        viewBinding.layoutTime.tvToDay.setOnClickListener {
            if (!viewBinding.layoutTime.tvToDay.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvToDay.isChecked = true
            }
        }
        viewBinding.layoutTime.tvWeek.setOnClickListener {
            if (!viewBinding.layoutTime.tvWeek.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvWeek.isChecked = true
            }
        }
        viewBinding.layoutTime.tvLastWeek.setOnClickListener {
            if (!viewBinding.layoutTime.tvLastWeek.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvLastWeek.isChecked = true
            }
        }
        viewBinding.layoutTime.tvMonth.setOnClickListener {
            if (!viewBinding.layoutTime.tvMonth.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvMonth.isChecked = true
            }
        }
        viewBinding.layoutTime.tvLastMonth.setOnClickListener {
            if (!viewBinding.layoutTime.tvLastMonth.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvLastMonth.isChecked = true
            }
        }
    }

    private fun setCheck() {
        viewBinding.layoutTime.tvToDay.isChecked = false
        viewBinding.layoutTime.tvWeek.isChecked = false
        viewBinding.layoutTime.tvLastWeek.isChecked = false
        viewBinding.layoutTime.tvMonth.isChecked = false
        viewBinding.layoutTime.tvLastMonth.isChecked = false
    }

    private fun tl_3() {
        mFragments.add(OperationDataFragment.newInstance())
        mFragments.add(OperationChartFragment.newInstance())
        val viewPager: ViewPager = viewBinding.viewpager
        viewPager.adapter = MyPagerAdapter(childFragmentManager)
        viewBinding.mTabLayout.setTabData(mTitles)
        viewBinding.mTabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager.currentItem = position
            }

            override fun onTabReselect(position: Int) {}
        })
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                viewBinding.mTabLayout.currentTab = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewPager.currentItem = 0
    }

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