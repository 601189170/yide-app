package com.yyide.chatim_pro.fragment.gate

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.gate.GateAllThroughActivity
import com.yyide.chatim_pro.activity.gate.GateSecondBranchActivity
import com.yyide.chatim_pro.adapter.gate.GateStudentStaffBranchAdapter
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim_pro.databinding.FragmentGateStudentBinding
import com.yyide.chatim_pro.model.gate.BarrierSectionBean
import com.yyide.chatim_pro.model.gate.GateBaseInfoBean
import com.yyide.chatim_pro.model.gate.Result
import com.yyide.chatim_pro.utils.DatePickerDialogUtil
import com.yyide.chatim_pro.utils.DisplayUtils
import com.yyide.chatim_pro.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim_pro.utils.loge
import com.yyide.chatim_pro.view.SpaceItemDecoration
import com.yyide.chatim_pro.viewmodel.gate.AdminViewModel
import com.yyide.chatim_pro.viewmodel.gate.GateSiteViewModel
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
 *              type=1学生
 */
class GateStudentFragment() : Fragment() {
    lateinit var fragmentGateStudentStaffBinding: FragmentGateStudentBinding
    private var currentDate: DateTime = DateTime.now().simplifiedDataTime()
    private val adminViewModel: AdminViewModel by viewModels()
    private val siteViewModel: GateSiteViewModel by activityViewModels()
    private var curSiteId = ""
    private val dataList = mutableListOf<GateBaseInfoBean>()
    private lateinit var adapter: GateStudentStaffBranchAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentGateStudentStaffBinding = FragmentGateStudentBinding.inflate(layoutInflater)
        return fragmentGateStudentStaffBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        siteViewModel.curSiteId.observe(requireActivity()) {
            //监听当前的场地变化
            loge("当前场地变化：$it")
            curSiteId = it
            adminViewModel.queryBasicsInfoBySiteId(currentDate.toStringTime(""), it)
        }
        //首页顶部通行数据概况
        lifecycleScope.launch {
            adminViewModel.studentBaseInfo.collect {
                when (it) {
                    is Result.Success -> {
                        handleTopBaseInfo(it.data)
                    }

                    is Result.Error -> {
                        loge("${it.exception?.localizedMessage}")
                        ToastUtils.showShort("${it.exception?.localizedMessage}")
                    }
                }
            }
        }
        //首页学段列表
        lifecycleScope.launch {
            adminViewModel.sectionList.collect {
                when (it) {
                    is Result.Success -> {
                        handleSectionListInfo(it.data)
                    }
                    is Result.Error -> {
                        loge("${it.exception?.localizedMessage}")
                        ToastUtils.showShort("${it.exception?.localizedMessage}")
                    }
                }
            }
        }

        fragmentGateStudentStaffBinding.tvDatePick.text = currentDate.toStringTime("yyyy/MM/dd")
        fragmentGateStudentStaffBinding.tvDatePick.setOnClickListener {
            val now = DateTime.now()
            val millisMax = now.millis
            val millisMin = now.minusMonths(3).millis
            DatePickerDialogUtil.showDate(
                requireContext(),
                "选择日期",
                currentDate.toStringTime(""),
                millisMin,
                millisMax,
                startTimeListener
            )
        }

        fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.root.setOnClickListener {
            //查询全校学生出入校门口情况界面
            val intent = Intent(requireContext(), GateAllThroughActivity::class.java)
            intent.putExtra("type", 1)
            intent.putExtra("siteId",curSiteId)
            intent.putExtra("queryTime",currentDate.toStringTime())
            requireContext().startActivity(intent)
        }
        fragmentGateStudentStaffBinding.rgType.visibility = View.VISIBLE
        //填充学段数据列表
        fragmentGateStudentStaffBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        adapter = GateStudentStaffBranchAdapter(requireContext(), dataList) {
            val data = dataList[it]
            val intent = Intent(requireContext(), GateSecondBranchActivity::class.java)
            intent.putExtra("title", data.name)
            intent.putExtra("id",data.id)
            intent.putExtra("peopleType",1)
            intent.putExtra("queryTime",currentDate.toStringTime())
            intent.putExtra("queryType",3)
            intent.putExtra("siteId",curSiteId)
            requireContext().startActivity(intent)
        }
        fragmentGateStudentStaffBinding.recyclerView.addItemDecoration(
            SpaceItemDecoration(
                DisplayUtils.dip2px(requireContext(), 10f)
            )
        )
        fragmentGateStudentStaffBinding.recyclerView.adapter = adapter
    }

    /**
     * 处理学段列表
     */
    private fun handleSectionListInfo(data: List<BarrierSectionBean>?) {
        if (data == null || data.isEmpty()) {
            //空数据列表处理
            fragmentGateStudentStaffBinding.blankPage.visibility = View.VISIBLE
            fragmentGateStudentStaffBinding.rgType.visibility = View.GONE
            fragmentGateStudentStaffBinding.recyclerView.visibility = View.GONE
            fragmentGateStudentStaffBinding.rgType.removeAllViews()
            return
        }
        fragmentGateStudentStaffBinding.blankPage.visibility = View.GONE
        fragmentGateStudentStaffBinding.rgType.visibility = View.VISIBLE
        fragmentGateStudentStaffBinding.recyclerView.visibility = View.VISIBLE
        fragmentGateStudentStaffBinding.rgType.removeAllViews()
        val radioGroup = RadioGroup(requireContext())
        radioGroup.orientation = LinearLayout.HORIZONTAL
        val layoutParams = ViewGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.WRAP_CONTENT
        )
        radioGroup.layoutParams = layoutParams
        addRadioGroupView(radioGroup,data)
        fragmentGateStudentStaffBinding.rgType.addView(radioGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            loge("学段类型改变：$checkedId")
            val barrierSectionBean = data[checkedId]
            dataList.clear()
            dataList.addAll(barrierSectionBean.list)
            adapter.notifyDataSetChanged()
        }
        val barrierSectionBean = data[0]
        dataList.clear()
        dataList.addAll(barrierSectionBean.list)
        adapter.notifyDataSetChanged()

    }
    ////动态添加视图
    private fun addRadioGroupView(radiogroup: RadioGroup,data: List<BarrierSectionBean>) {
        radiogroup.removeAllViews()
        for (index in data.indices) {
            val button = RadioButton(requireContext())
            button.isChecked = false
            setRaidBtnAttribute(button, data[index].name ?: "", index)
            radiogroup.addView(button)
            val layoutParams: LinearLayout.LayoutParams =
                button.layoutParams as LinearLayout.LayoutParams
            layoutParams.setMargins(
                0,
                0,
                DisplayUtils.dip2px(requireContext(), 10f),
                0
            ) //4个参数按顺序分别是左上右下
            button.layoutParams = layoutParams
            if (index == 0) {
                loge("id = ${button.id}")
                radiogroup.check(button.id)
            }
            //button.isChecked = index == 0
        }
    }

    private fun setRaidBtnAttribute(codeBtn: RadioButton?, btnContent: String, id: Int) {
        if (null == codeBtn) {
            return
        }
        codeBtn.setBackgroundResource(R.drawable.selector_checked_tv)
        codeBtn.setTextColor(this.resources.getColorStateList(R.color.black_white_color3))
        codeBtn.buttonDrawable = ColorDrawable(Color.TRANSPARENT)
        codeBtn.textSize = 15f
        codeBtn.id = id
        codeBtn.text = btnContent
        //codeBtn.setPadding(2, 0, 2, 0);
        codeBtn.gravity = Gravity.CENTER
        //DensityUtilHelps.Dp2Px(this,40)
        val rlp = LinearLayout.LayoutParams(
            DisplayUtils.dip2px(requireContext(), 62f),
            DisplayUtils.dip2px(requireContext(), 28f)
        )
        codeBtn.layoutParams = rlp
    }

    /**
     * 界面顶部的闸机通行数据概况
     */
    private fun handleTopBaseInfo(data: GateBaseInfoBean?) {
        if (data == null){
            handleSectionListInfo(null)
            return
        }
        data.let {
            val layoutGateThroughSummaryAll =
                fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll
            layoutGateThroughSummaryAll.tvGateEventTitle.text = it.name
            layoutGateThroughSummaryAll.tvThroughNumber.text = "${it.totalNumber}"
            layoutGateThroughSummaryAll.tvGoOutNumber.text = "${it.outNumber}"
            layoutGateThroughSummaryAll.tvGoIntoNumber.text = "${it.intoNumber}"
        }
        adminViewModel.queryBarrierSectionStatistical(currentDate.toStringTime(""), curSiteId)
    }

    //日期选择监听
    private val startTimeListener =
        OnDateSetListener { timePickerView: TimePickerDialog?, millseconds: Long ->
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timingTime = simpleDateFormat.format(Date(millseconds))
            currentDate = ScheduleDaoUtil.toDateTime(timingTime, "yyyy-MM-dd")
            loge("startTimeListener: $timingTime")
            val toStringTime = currentDate.toStringTime("yyyy/MM/dd")
            fragmentGateStudentStaffBinding.tvDatePick.text = toStringTime
            //切换日期重新请求数据
            adminViewModel.queryBasicsInfoBySiteId(currentDate.toStringTime(""), curSiteId)
        }
}