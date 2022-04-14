package com.yyide.chatim_pro.activity.leave

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityAddBinding
import com.yyide.chatim_pro.databinding.ItemLeaveApprovaBinding
import com.yyide.chatim_pro.model.LeaveApprovalBean
import com.yyide.chatim_pro.utils.GlideUtil

/**
 * 添加审批人
 * 2022年3月15日
 * lrz
 */
class AddApprovalActivity : KTBaseActivity<ActivityAddBinding>(ActivityAddBinding::inflate) {
    private val selectList = arrayListOf<LeaveApprovalBean.Branappr>()
    private var itemSelect = LeaveApprovalBean.Branappr()
    private var dataList = mutableListOf<LeaveApprovalBean.Branappr>()
    private var isApproval = false
    private var itemId = ""
    override fun initView() {
        super.initView()
        binding.top.backLayout.setOnClickListener { finish() }
        binding.top.title.text = getString(R.string.leave_select_title)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@AddApprovalActivity)
        binding.recyclerView.adapter = mAdapter
        intent.apply {
            val json = getStringExtra("dataList")
            itemId = getStringExtra("itemId") as String
            isApproval = getBooleanExtra("isApproval", false)
            if (!TextUtils.isEmpty(json)) {
                dataList = JSON.parseArray(json, LeaveApprovalBean.Branappr::class.java)
                position = getPosition(dataList)
                mAdapter.setList(dataList)
            }
        }

        binding.btnCommit.setOnClickListener {
            //返回上一页
            val intent = intent
            intent.putExtra("approverList", JSON.toJSONString(itemSelect))
            this.setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun getPosition(dataList: List<LeaveApprovalBean.Branappr>): Int {
        dataList.forEachIndexed { index, branappr ->
            if (branappr.id == itemId) {
                return index
            }
        }
        return -1
    }

    private var position = -1

    private var mAdapter =
        object :
            BaseQuickAdapter<LeaveApprovalBean.Branappr, BaseViewHolder>(R.layout.item_leave_approva) {
            override fun convert(holder: BaseViewHolder, item: LeaveApprovalBean.Branappr) {
                val viewbinding = ItemLeaveApprovaBinding.bind(holder.itemView)
                viewbinding.tvName.text = item.name
//                binding.tvDesc.text = item.get
                viewbinding.cbCheck.isChecked = position == getItemPosition(item)
                if (holder.absoluteAdapterPosition == (dataList.size - 1)) {
                    viewbinding.viewLine.visibility = View.INVISIBLE
                }
                GlideUtil.loadImageHead(context, item.avatar, viewbinding.ivHead, 12)
                viewbinding.itemView.setOnClickListener {
                    viewbinding.cbCheck.isChecked = !viewbinding.cbCheck.isChecked
                    itemSelect = item
                    position = getItemPosition(item)
                    binding.tvCount.text = "${1}人"
                    notifyDataSetChanged()
                }

            }
        }
}