package com.yyide.chatim.activity.leave

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityAddBinding
import com.yyide.chatim.databinding.ItemLeaveApprovaBinding
import com.yyide.chatim.model.LeaveApprovalBean
import com.yyide.chatim.utils.GlideUtil

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
                val viewbinding = ItemLeaveApprovaBinding.bind(holder.itemView)
                viewbinding.tvName.text = item.name
                viewbinding.cbCheck.isChecked = item.isCheck
                viewbinding.itemView.setOnClickListener {
                    viewbinding.cbCheck.isChecked = !viewbinding.cbCheck.isChecked
                    if (viewbinding.cbCheck.isChecked) {
                        selectList.add(item)
                    } else {
                        selectList.remove(item)
                    }
                    binding.tvCount.text = "${selectList.size}人"
                }
//                binding.tvDesc.text = item.get
                if (holder.absoluteAdapterPosition == (dataList.size - 1)) {
                    viewbinding.viewLine.visibility = View.INVISIBLE
                }
                GlideUtil.loadImageHead(context, item.avatar, viewbinding.ivHead, 12)
            }
        }
}