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
import com.yyide.chatim.model.gate.GateThroughPeopleListBean

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
    private var dataList: List<GateThroughPeopleListBean.PeopleListBean> = mutableListOf()
    private lateinit var fragmentGateThroughListBinding: FragmentGateThroughListBinding
    private var peopleType:String? = null
    private var queryTime:String? = null
    private var siteId:String? = null
    constructor(
        page_type: String,
        identity_type: String,
        peopleType:String,
        queryTime:String,
        siteId:String,
        dataList: List<GateThroughPeopleListBean.PeopleListBean>
    ) : this() {
        this.page_type = page_type
        this.identity_type = identity_type
        this.dataList = dataList
        this.peopleType = peopleType
        this.queryTime = queryTime
        this.siteId = siteId
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
        //1出校 2入校  3通行人数
        when (page_type) {
            "1" -> {
                when (identity_type) {
                    "1" -> {
                        fragmentGateThroughListBinding.layoutOneColumnData.root.visibility =
                            View.GONE
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
                            toDetail(it)
                        }
                        fragmentGateThroughListBinding.layoutThreeColumnData.recyclerView.adapter = adapter
                    }
                    "2" -> {
                        fragmentGateThroughListBinding.layoutOneColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.VISIBLE
                        //四列出校
                        fragmentGateThroughListBinding.layoutFourColumnData.tvTime.text = "出校时间"
                        fragmentGateThroughListBinding.layoutFourColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            toDetail(it)
                        }
                        fragmentGateThroughListBinding.layoutFourColumnData.recyclerView.adapter = adapter
                    }
                }
            }
            "2" -> {
                when (identity_type) {
                    "1" -> {
                        fragmentGateThroughListBinding.layoutOneColumnData.root.visibility =
                            View.GONE
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
                            toDetail(it)
                        }
                        fragmentGateThroughListBinding.layoutThreeColumnData.recyclerView.adapter = adapter
                    }
                    "2" -> {
                        fragmentGateThroughListBinding.layoutOneColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.VISIBLE
                        //四列入校
                        fragmentGateThroughListBinding.layoutFourColumnData.tvTime.text = "入校时间"
                        fragmentGateThroughListBinding.layoutFourColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            toDetail(it)
                        }
                        fragmentGateThroughListBinding.layoutFourColumnData.recyclerView.adapter = adapter
                    }
                }
            }
            "3" -> {
                when (identity_type) {
                    "1" -> {
                        fragmentGateThroughListBinding.layoutOneColumnData.root.visibility =
                            View.VISIBLE
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.GONE
                        //一列 通行人数
                        fragmentGateThroughListBinding.layoutOneColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            toDetail(it)
                        }
                        fragmentGateThroughListBinding.layoutOneColumnData.recyclerView.adapter = adapter
                    }
                    "2" -> {
                        fragmentGateThroughListBinding.layoutOneColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutTwoColumnData.root.visibility =
                            View.VISIBLE
                        fragmentGateThroughListBinding.layoutThreeColumnData.root.visibility =
                            View.GONE
                        fragmentGateThroughListBinding.layoutFourColumnData.root.visibility =
                            View.GONE
                        //两列 通行人数
                        fragmentGateThroughListBinding.layoutTwoColumnData.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = GateThroughListAdapter(requireContext(), dataList){
                            toDetail(it)
                        }
                        fragmentGateThroughListBinding.layoutTwoColumnData.recyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    /**
     * 查看详情
     */
    private fun toDetail(position:Int){
        val peopleListBean = dataList[position]
        val intent = Intent(context, GateDetailInfoActivity::class.java)
        intent.putExtra("peopleType",peopleType)
        intent.putExtra("queryTime",queryTime)
        intent.putExtra("userId",peopleListBean.userId)
        intent.putExtra("siteId",siteId)
        requireContext().startActivity(intent)
    }

    companion object {
        //1出校 2入校  3通行人数
        const val PAGE_TYPE = "page_type"

        //身份类型 1，班主任(显示三列数据) 2，全学校（显示四列数据）
        const val IDENTITY_TYPE = "identity_type"
    }
}