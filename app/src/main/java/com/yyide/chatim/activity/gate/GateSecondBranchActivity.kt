package com.yyide.chatim.activity.gate

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yyide.chatim.adapter.gate.GateBranchData
import com.yyide.chatim.adapter.gate.GateStudentStaffBranchAdapter
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityGateSecondBranchBinding
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.view.SpaceItemDecoration

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateSecondBranchBinding = ActivityGateSecondBranchBinding.inflate(layoutInflater)
        setContentView(activityGateSecondBranchBinding.root)
        title = intent.getStringExtra("title")
        initView()
    }

    private fun initView() {
        activityGateSecondBranchBinding.top.title.text = title
        activityGateSecondBranchBinding.top.backLayout.setOnClickListener {
            finish()
        }

        //填充学段数据列表
        val dataList = mutableListOf<GateBranchData>()
        dataList.add(GateBranchData("${title}1班", 50, 20, 30))
        dataList.add(GateBranchData("${title}2班", 80, 10, 50))
        dataList.add(GateBranchData("${title}3班", 60, 40, 20))
        dataList.add(GateBranchData("${title}4班", 40, 20, 20))
        dataList.add(GateBranchData("${title}5班", 40, 20, 20))
        dataList.add(GateBranchData("${title}6班", 40, 20, 20))
        activityGateSecondBranchBinding.recyclerView.layoutManager =
            LinearLayoutManager(this)
        val adapter = GateStudentStaffBranchAdapter(this, dataList){
            val data = dataList[it]
            val intent = Intent(this, GateThirdBranchActivity::class.java)
            intent.putExtra("title",data.branch)
            startActivity(intent)
        }
        activityGateSecondBranchBinding.recyclerView.addItemDecoration(
            SpaceItemDecoration(
                DisplayUtils.dip2px(this, 10f)
            )
        )
        activityGateSecondBranchBinding.recyclerView.adapter = adapter
    }
}