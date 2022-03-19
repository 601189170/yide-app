package com.yyide.chatim.activity.leave

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityAddBinding
import com.yyide.chatim.databinding.ItemLeaveApprovaBinding
import com.yyide.chatim.model.LeaveApprovalBean

/**
 * 添加抄送人
 * 2022年3月15日
 * lrz
 */
class AddCcActivity : KTBaseActivity<ActivityAddBinding>(ActivityAddBinding::inflate) {
    private val selectList = arrayListOf<LeaveApprovalBean.Cc>()
    private var dataList = mutableListOf<LeaveApprovalBean.Cc>()
    private var isApproval = false
    override fun initView() {
        super.initView()
        binding.top.backLayout.setOnClickListener { finish() }
        binding.top.title.text = getString(R.string.leave_select_title)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@AddCcActivity)
        binding.recyclerView.adapter = mAdapter
        intent.apply {
            val json = getStringExtra("dataList")
            isApproval = getBooleanExtra("isApproval", false)
            if (!TextUtils.isEmpty(json)) {
                dataList = JSON.parseArray(json, LeaveApprovalBean.Cc::class.java)
                mAdapter.setList(dataList)
            }
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bind = ItemLeaveApprovaBinding.bind(view)
            bind.cbCheck.isChecked = !bind.cbCheck.isChecked
            if (bind.cbCheck.isChecked) {
                selectList.add(mAdapter.data[position])
            } else {
                selectList.remove(mAdapter.data[position])
            }
            binding.tvCount.text = "${selectList.size}人"
        }

        binding.btnCommit.setOnClickListener {
            //返回上一页
            val intent = intent
            intent.putExtra("ccList", JSON.toJSONString(selectList))
            this.setResult(RESULT_OK, intent)
            finish()
        }
    }

    private var mAdapter =
        object :
            BaseQuickAdapter<LeaveApprovalBean.Cc, BaseViewHolder>(R.layout.item_leave_approva) {
            override fun convert(holder: BaseViewHolder, item: LeaveApprovalBean.Cc) {
                val binding = ItemLeaveApprovaBinding.bind(holder.itemView)
                binding.tvName.text = item.name
//                binding.tvDesc.text = item.get
                if (holder.absoluteAdapterPosition == (dataList.size - 1)) {
                    binding.viewLine.visibility = View.INVISIBLE
                }
            }
        }
}