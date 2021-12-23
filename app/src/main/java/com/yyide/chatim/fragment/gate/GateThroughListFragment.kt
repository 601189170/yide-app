package com.yyide.chatim.fragment.gate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yyide.chatim.activity.gate.GateDetailInfoActivity
import com.yyide.chatim.adapter.gate.GateThroughData
import com.yyide.chatim.adapter.gate.GateThroughListAdapter
import com.yyide.chatim.databinding.FragmentGateThroughListBinding

/**
 * @author liu tao
 * @date 2021年12月23日10:15:23
 * @desc 闸机通行数据展示，三种情况
 *       1，两列 姓名--班级/部门
 *       2，三列 姓名--出校/入校时间 地址
 *       3，四列 姓名--班级--出入校时间 地址
 *
 */
class PeopleThroughListFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    //1出校 2入校  3通行人数
    private var page_type: String? = null
    private var identity_type: String? = null
    private var dataList: List<GateThroughData> = mutableListOf()
    private lateinit var fragmentGateThroughListBinding: FragmentGateThroughListBinding

    constructor(
        page_type: String,
        identity_type: String,
        dataList: List<GateThroughData>
    ) : this() {
        this.page_type = page_type
        this.identity_type = identity_type
        this.dataList = dataList
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentGateThroughListBinding = FragmentGateThroughListBinding.inflate(layoutInflater)
        return fragmentGateThroughListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (page_type) {
            "1" -> {
                when (identity_type) {
                    "1" -> {
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.VISIBLE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.GONE
                        //三列出校
                        fragmentGateThroughListBinding.layoutThreeColumnData.tvTime.text = "出校时间"
                        fragmentGateThroughListBinding.layoutThreeColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            val name = dataList[it].name
                            toDetail(name)
                        }
                        fragmentGateThroughListBinding.layoutThreeColumnData.recyclerView.adapter = adapter
                    }
                    "2" -> {
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.VISIBLE
                        //三列出校
                        fragmentGateThroughListBinding.layoutFourColumnData.tvTime.text = "出校时间"
                        fragmentGateThroughListBinding.layoutFourColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            val name = dataList[it].name
                            toDetail(name)
                        }
                        fragmentGateThroughListBinding.layoutFourColumnData.recyclerView.adapter = adapter
                    }
                }
            }
            "2" -> {
                when (identity_type) {
                    "1" -> {
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.VISIBLE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.GONE
                        //三列入校
                        fragmentGateThroughListBinding.layoutThreeColumnData.tvTime.text = "入校时间"
                        fragmentGateThroughListBinding.layoutThreeColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            val name = dataList[it].name
                            toDetail(name)
                        }
                        fragmentGateThroughListBinding.layoutThreeColumnData.recyclerView.adapter = adapter
                    }
                    "2" -> {
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.VISIBLE
                        //三列入校
                        fragmentGateThroughListBinding.layoutFourColumnData.tvTime.text = "入校时间"
                        fragmentGateThroughListBinding.layoutFourColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            val name = dataList[it].name
                            toDetail(name)
                        }
                        fragmentGateThroughListBinding.layoutFourColumnData.recyclerView.adapter = adapter
                    }
                }
            }
            "3" -> {
                when (identity_type) {
                    "1" -> {
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.VISIBLE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.GONE
                        //两列 通行人数
                        fragmentGateThroughListBinding.layoutTwoColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            val name = dataList[it].name
                            toDetail(name)
                        }
                        fragmentGateThroughListBinding.layoutTwoColumnData.recyclerView.adapter = adapter
                    }
                    "2" -> {
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.VISIBLE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.GONE
                        //两列 通行人数
                        fragmentGateThroughListBinding.layoutTwoColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            val name = dataList[it].name
                            toDetail(name)
                        }
                        fragmentGateThroughListBinding.layoutTwoColumnData.recyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    fun toDetail(username:String){
        val intent = Intent(context, GateDetailInfoActivity::class.java)
        intent.putExtra("username",username)
        requireContext().startActivity(intent)
    }

    companion object {
        //1出校 2入校  3通行人数
        const val PAGE_TYPE = "page_type"

        //身份类型 1，班主任(显示三列数据) 2，全学校（显示四列数据）
        const val IDENTITY_TYPE = "identity_type"
    }
}