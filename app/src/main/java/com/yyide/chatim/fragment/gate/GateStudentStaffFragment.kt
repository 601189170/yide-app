package com.yyide.chatim.fragment.gate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yyide.chatim.activity.gate.GateAllThroughActivity
import com.yyide.chatim.activity.gate.GateSecondBranchActivity
import com.yyide.chatim.adapter.gate.GateBranchData
import com.yyide.chatim.adapter.gate.GateStudentStaffBranchAdapter
import com.yyide.chatim.databinding.FragmentGateStudentStaffBinding
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpaceItemDecoration

/**
 *
 * @author liu tao
 * @date 2021/12/23 14:54
 * @description 闸机通行数据首页fragment
 *              type=1学生 type=2教职工(没有学段选择)
 */
class GateStudentStaffFragment() : Fragment() {
    lateinit var fragmentGateStudentStaffBinding: FragmentGateStudentStaffBinding
    private var type = 1

    constructor(type: Int) : this() {
        this.type = type
    }

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
        loge("type=$type")
        //学生
        if (type == 1) {
            fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvGateEventTitle.text =
                "全校学生出入校门口情况"
            fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.root.setOnClickListener {
                //查询全校学生出入校门口情况界面
                val intent = Intent(requireContext(), GateAllThroughActivity::class.java)
                intent.putExtra("type",1)
                requireContext().startActivity(intent)
            }
            fragmentGateStudentStaffBinding.rgType.visibility = View.VISIBLE
            //填充学段数据列表
            val dataList = mutableListOf<GateBranchData>()
            dataList.add(GateBranchData("一年级", 50, 20, 30))
            dataList.add(GateBranchData("二年级", 80, 10, 50))
            dataList.add(GateBranchData("三年级", 60, 40, 20))
            dataList.add(GateBranchData("四年级", 40, 20, 20))
            dataList.add(GateBranchData("五年级", 40, 20, 20))
            dataList.add(GateBranchData("六年级", 40, 20, 20))
            fragmentGateStudentStaffBinding.recyclerView.layoutManager =
                LinearLayoutManager(requireContext())
            val adapter = GateStudentStaffBranchAdapter(requireContext(), dataList){
                val data = dataList[it]
                val intent = Intent(requireContext(), GateSecondBranchActivity::class.java)
                intent.putExtra("title",data.branch)
                requireContext().startActivity(intent)
            }
            fragmentGateStudentStaffBinding.recyclerView.addItemDecoration(
                SpaceItemDecoration(
                    DisplayUtils.dip2px(requireContext(), 10f)
                )
            )
            fragmentGateStudentStaffBinding.recyclerView.adapter = adapter

            return
        }
        //教职工
        fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.tvGateEventTitle.text =
            "全校教职工出入校门口情况"
        fragmentGateStudentStaffBinding.layoutGateThroughSummaryAll.root.setOnClickListener {
            //查询全校教职工出入校门口情况界面
            val intent = Intent(requireContext(), GateAllThroughActivity::class.java)
            intent.putExtra("type",2)
            requireContext().startActivity(intent)
        }
        fragmentGateStudentStaffBinding.rgType.visibility = View.GONE
        //填充部门数据列表
        val dataList = mutableListOf<GateBranchData>()
        dataList.add(GateBranchData("教学部", 50, 20, 30))
        dataList.add(GateBranchData("后勤部", 80, 10, 50))
        dataList.add(GateBranchData("宣传部", 60, 40, 20))
        dataList.add(GateBranchData("校长办公室", 40, 20, 20))
        dataList.add(GateBranchData("校长办公室", 40, 20, 20))
        dataList.add(GateBranchData("校长办公室", 40, 20, 20))
        dataList.add(GateBranchData("校长办公室", 40, 20, 20))
        fragmentGateStudentStaffBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        val adapter = GateStudentStaffBranchAdapter(requireContext(), dataList){
            val data = dataList[it]
            val intent = Intent(requireContext(), GateSecondBranchActivity::class.java)
            intent.putExtra("title",data.branch)
            requireContext().startActivity(intent)
        }
        fragmentGateStudentStaffBinding.recyclerView.addItemDecoration(
            SpaceItemDecoration(
                DisplayUtils.dip2px(requireContext(), 10f)
            )
        )
        fragmentGateStudentStaffBinding.recyclerView.adapter = adapter

    }
}