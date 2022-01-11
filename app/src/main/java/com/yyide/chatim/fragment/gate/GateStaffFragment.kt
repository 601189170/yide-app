package com.yyide.chatim.fragment.gate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.yyide.chatim.activity.gate.GateAllThroughActivity
import com.yyide.chatim.activity.gate.GateDetailInfoActivity
import com.yyide.chatim.activity.gate.GateSecondBranchActivity
import com.yyide.chatim.adapter.gate.GateBranchData
import com.yyide.chatim.adapter.gate.GateStudentStaffBranchAdapter
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.FragmentGateStudentStaffBinding
import com.yyide.chatim.model.gate.GateBaseInfoBean
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.model.gate.StaffBean
import com.yyide.chatim.utils.DatePickerDialogUtil
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.viewmodel.gate.AdminViewModel
import com.yyide.chatim.viewmodel.gate.GateSiteViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author liu tao
 * @date 2021/12/23 14:54
 * @description 闸机通行数据首页fragment
 *              type=1学生 type=2教职工(没有学段选择)
 */
class GateStaffFragment() : Fragment() {
    lateinit var fragmentGateStudentStaffBinding: FragmentGateStudentStaffBinding
    private var currentDate: DateTime = DateTime.now().simplifiedDataTime()
    private val adminViewModel: AdminViewModel by viewModels()
    private val siteViewModel: GateSiteViewModel by activityViewModels()
    private var curSiteId = ""
    val dataList = mutableListOf<GateBaseInfoBean>()
    private lateinit var adapter: GateStudentStaffBranchAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentGateStudentStaffBinding = FragmentGateStudentStaffBinding.inflate(layoutInflater)
        return fragmentGateStudentStaffBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        siteViewModel.curSiteId.observe(requireActivity()) {
            //监听当前的场地变化
            loge("当前场地变化：$it")
            curSiteId = it
            adminViewModel.queryTeacherBarrierPassageDataByDeptId(
                null,
                currentDate.toStringTime(),
                curSiteId
            )
        }
        initView()
        lifecycleScope.launch {
            adminViewModel.teacherThroughDeptList.collect {
                when (it) {
                    is Result.Success -> {
                        handleData(it.data)
                    }
                    is Result.Error -> {
                        loge("${it.exception?.localizedMessage}")
                        ToastUtils.showShort("${it.exception?.localizedMessage}")
                    }
                }
            }
        }
//        adminViewModel.queryTeacherBarrierPassageDataByDeptId(
//            null,
//            currentDate.toStringTime(),
//            curSiteId
//        )
    }

    private fun handleData(data: StaffBean?) {
        if (data == null || data.totalNumber == 0) {
            //空数据列表处理
            fragmentGateStudentStaffBinding.blankPage.visibility = View.VISIBLE
            fragmentGateStudentStaffBinding.rgType.visibility = View.GONE
            fragmentGateStudentStaffBinding.recyclerView.visibility = View.GONE
            fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvThroughNumber.text = "0"
            fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvGoOutNumber.text = "0"
            fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvGoIntoNumber.text = "0"
            return
        }
        fragmentGateStudentStaffBinding.blankPage.visibility = View.GONE
        fragmentGateStudentStaffBinding.rgType.visibility = View.GONE
        fragmentGateStudentStaffBinding.recyclerView.visibility = View.VISIBLE
        fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvGateEventTitle.text =
            "${data.name}"
        fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvThroughNumber.text =
            "${data.totalNumber}"
        fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvGoOutNumber.text =
            "${data.outNumber}"
        fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvGoIntoNumber.text =
            "${data.intoNumber}"
        dataList.clear()
        data.deptDataList?.forEach {
            val gateBaseInfoBean = it.copy()
            gateBaseInfoBean.isDept = true
            dataList.add(gateBaseInfoBean)
        }

        data.list?.let {
            dataList.addAll(it)
        }
        adapter.notifyDataSetChanged()
    }

    private fun initView() {
        //教职工
        fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvGateEventTitle.text =
            "全校教职工出入校门口情况"
        fragmentGateStudentStaffBinding.tvDatePick.text = currentDate.toStringTime("yyyy/MM/dd")
        fragmentGateStudentStaffBinding.tvDatePick.setOnClickListener {
            val now = DateTime.now()
            val millisMax = now.millis
            val millisMin = now.minusMonths(3).millis
            DatePickerDialogUtil.showDate(
                requireContext(),
                "选择日期",
                currentDate.toStringTime(),
                millisMin,
                millisMax,
                startTimeListener
            )
        }
        fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.root.setOnClickListener {
            //查询全校教职工出入校门口情况界面
            val intent = Intent(requireContext(), GateAllThroughActivity::class.java)
            intent.putExtra("type", 2)
            intent.putExtra("siteId",curSiteId)
            intent.putExtra("queryTime",currentDate.toStringTime())
            requireContext().startActivity(intent)
        }
        fragmentGateStudentStaffBinding.rgType.visibility = View.GONE
        //填充部门数据列表
        fragmentGateStudentStaffBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        adapter = GateStudentStaffBranchAdapter(requireContext(), dataList) {
            val data = dataList[it]
            if (data.isDept) {
                val intent = Intent(requireContext(), GateSecondBranchActivity::class.java)
                intent.putExtra("title", data.name)
                intent.putExtra("peopleType", 2)
                intent.putExtra("queryTime", currentDate.toStringTime())
                intent.putExtra("userId", data.userId)
                intent.putExtra("id", data.id)
                intent.putExtra("siteId", curSiteId)
                requireContext().startActivity(intent)
            } else {
                //跳入闸机详情
                val intent = Intent(context, GateDetailInfoActivity::class.java)
                intent.putExtra("peopleType", 2)
                intent.putExtra("queryTime", currentDate.toStringTime())
                intent.putExtra("userId", data.userId)
                intent.putExtra("siteId", curSiteId)
                requireContext().startActivity(intent)
            }
        }
        fragmentGateStudentStaffBinding.recyclerView.addItemDecoration(
            SpaceItemDecoration(
                DisplayUtils.dip2px(requireContext(), 10f)
            )
        )
        fragmentGateStudentStaffBinding.recyclerView.adapter = adapter
    }

    //日期选择监听
    private val startTimeListener =
        OnDateSetListener { _, millseconds: Long ->
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val timingTime = simpleDateFormat.format(Date(millseconds))
            currentDate = ScheduleDaoUtil.toDateTime(timingTime).simplifiedDataTime()
            loge("startTimeListener: $timingTime")
            val toStringTime = currentDate.toStringTime("yyyy/MM/dd")
            fragmentGateStudentStaffBinding.tvDatePick.text = toStringTime
            adminViewModel.queryTeacherBarrierPassageDataByDeptId(
                null,
                currentDate.toStringTime(),
                curSiteId
            )
        }
}