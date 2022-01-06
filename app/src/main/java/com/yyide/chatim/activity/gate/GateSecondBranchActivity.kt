package com.yyide.chatim.activity.gate

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.adapter.gate.GateStudentStaffBranchAdapter
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.ActivityGateSecondBranchBinding
import com.yyide.chatim.model.gate.GateBaseInfoBean
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.model.gate.StaffBean
import com.yyide.chatim.model.gate.StudentGradeInOutInfoBean
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.viewmodel.gate.AdminViewModel
import com.yyide.chatim.viewmodel.gate.GateSecondBranchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 *
 * @author liu tao
 * @date 2021/12/23 18:19
 * @description 第二级部门（最终通行记录列表前的部门/年级,可能多次使用）
 *              type = 1 学生
 *              type = 2 教职工
 */
class GateSecondBranchActivity : BaseActivity() {
    private lateinit var activityGateSecondBranchBinding: ActivityGateSecondBranchBinding
    private var title: String? = ""
    private var id: String? = null
    private var peopleType: Int? = null
    private var queryTime: String? = null
    private var queryType: Int? = null
    private var siteId: String? = null
    private val dataList = mutableListOf<GateBaseInfoBean>()
    private lateinit var adapter: GateStudentStaffBranchAdapter
    private val gateSecondBranchViewModel: GateSecondBranchViewModel by viewModels()
    private val adminViewModel: AdminViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateSecondBranchBinding = ActivityGateSecondBranchBinding.inflate(layoutInflater)
        setContentView(activityGateSecondBranchBinding.root)
        title = intent.getStringExtra("title")
        id = intent.getStringExtra("id")
        peopleType = intent.getIntExtra("peopleType", 1)
        queryTime = intent.getStringExtra("queryTime")
        siteId = intent.getStringExtra("siteId")
        queryType = intent.getIntExtra("queryType", 3)
        initView()
        if (peopleType == 2) {
            lifecycleScope.launch {
                adminViewModel.teacherThroughDeptList.collect {
                    adminViewModel.teacherThroughDeptList.collect {
                        when (it) {
                            is Result.Success -> {
                                handleTeacherData(it.data)
                            }
                            is Result.Error -> {
                                loge("${it.exception?.localizedMessage}")
                                ToastUtils.showShort("${it.exception?.localizedMessage}")
                            }
                        }
                    }
                }
            }
            adminViewModel.queryTeacherBarrierPassageDataByDeptId(id, queryTime, siteId)
        } else if (peopleType == 1) {
            lifecycleScope.launch {
                gateSecondBranchViewModel.gateSecondBranchData.collect {
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
            gateSecondBranchViewModel.queryAllStudentInOutInfo(peopleType, queryTime, queryType, id)
        }

    }

    private fun handleTeacherData(data: StaffBean?) {
        if (data == null) {
            return
        }
        activityGateSecondBranchBinding.layoutGateThroughBranchAll.tvThroughNumber.text =
            "${data.totalNumber}"
        activityGateSecondBranchBinding.layoutGateThroughBranchAll.tvGoOutNumber.text =
            "${data.outNumber}"
        activityGateSecondBranchBinding.layoutGateThroughBranchAll.tvGoIntoNumber.text =
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

    private fun handleData(data: StudentGradeInOutInfoBean?) {
        if (data == null) {
            return
        }
        data.basicsForm?.let {
            activityGateSecondBranchBinding.layoutGateThroughBranchAll.tvThroughNumber.text =
                "${it.totalNumber}"
            activityGateSecondBranchBinding.layoutGateThroughBranchAll.tvGoOutNumber.text =
                "${it.outNumber}"
            activityGateSecondBranchBinding.layoutGateThroughBranchAll.tvGoIntoNumber.text =
                "${it.intoNumber}"
        }
        dataList.clear()
        data.studentDataList?.let {
            dataList.addAll(it)
        }
        adapter.notifyDataSetChanged()

    }

    private fun initView() {
        activityGateSecondBranchBinding.top.title.text = title
        activityGateSecondBranchBinding.top.backLayout.setOnClickListener {
            finish()
        }
        //填充学段数据列表
        activityGateSecondBranchBinding.recyclerView.layoutManager =
            LinearLayoutManager(this)
        adapter = GateStudentStaffBranchAdapter(this, dataList) {
            val data = dataList[it]
            if (peopleType == 1) {
                val intent = Intent(this, GateThirdBranchActivity::class.java)
                intent.putExtra("title", data.name)
                intent.putExtra("id", data.id)
                intent.putExtra("peopleType", peopleType)
                intent.putExtra("queryTime", queryTime)
                intent.putExtra("queryType", data.type)
                startActivity(intent)
                return@GateStudentStaffBranchAdapter
            }
            if (peopleType == 2) {
                if (data.isDept) {
                    val intent = Intent(this, GateSecondBranchActivity::class.java)
                    intent.putExtra("title", data.name)
                    intent.putExtra("peopleType", 2)
                    intent.putExtra("queryTime", queryTime)
                    intent.putExtra("userId", data.userId)
                    intent.putExtra("id", data.id)
                    intent.putExtra("siteId", siteId)
                    startActivity(intent)
                } else {
                    //跳入闸机详情
                    val intent = Intent(this, GateDetailInfoActivity::class.java)
                    intent.putExtra("peopleType", 2)
                    intent.putExtra("queryTime", queryTime)
                    intent.putExtra("userId", data.userId)
                    intent.putExtra("siteId", siteId)
                    startActivity(intent)
                }
                return@GateStudentStaffBranchAdapter
            }

        }
        activityGateSecondBranchBinding.recyclerView.addItemDecoration(
            SpaceItemDecoration(
                DisplayUtils.dip2px(this, 10f)
            )
        )
        activityGateSecondBranchBinding.recyclerView.adapter = adapter
    }
}