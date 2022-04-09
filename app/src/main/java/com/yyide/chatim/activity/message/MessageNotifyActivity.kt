package com.yyide.chatim.activity.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.message.viewmodel.MessageNotifyViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityNotifyListBinding
import com.yyide.chatim.databinding.ItemNoticeNotifyMessageBinding
import com.yyide.chatim.model.message.NotifyInfoBean
import com.yyide.chatim.utils.asColor
import com.yyide.chatim.utils.asDrawable

/**
 *
 * @author shenzhibin
 * @date 2022/4/8 9:59
 * @description 通知范围 班级/部门activity
 */
class MessageNotifyActivity :
    KTBaseActivity<ActivityNotifyListBinding>(ActivityNotifyListBinding::inflate) {

    companion object {
        fun startGo(context: Context, item: Int) {
            val intent = Intent(context, MessageNotifyActivity::class.java)
            intent.putExtra("data", item)
            context.startActivity(intent)
        }
    }

    private val viewModel by viewModels<MessageNotifyViewModel>()
    private var messageInfoId = 0

    // 当前点击项 0是教职工  1是家长
    private var clickType = 0
    private val adapter: BaseQuickAdapter<NotifyInfoBean, BaseViewHolder> by lazy {
        object :
            BaseQuickAdapter<NotifyInfoBean, BaseViewHolder>(R.layout.item_notice_notify_message) {
            override fun convert(holder: BaseViewHolder, item: NotifyInfoBean) {
                val viewBind = ItemNoticeNotifyMessageBinding.bind(holder.itemView)
                viewBind.itemNoticeNotifyTv.text = item.name
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        viewModel.requestStaffInfoList(messageInfoId)
    }

    override fun initView() {
        super.initView()
        messageInfoId = intent.getIntExtra("data", 0)

        binding.notifyInfoTop.title.text = "通知范围"

        binding.notifyInfoRv.layoutManager = LinearLayoutManager(this)
        binding.notifyInfoRv.adapter = adapter
        adapter.setEmptyView(R.layout.empty)

        viewModel.staffList.observe(this) {
            val data = it.getOrNull()
            data?.let {
                adapter.setList(data)
            }
        }


        viewModel.parentList.observe(this) {
            val data = it.getOrNull()
            data?.let {
                adapter.setList(data)
            }
        }

    }

    fun initListener() {

        binding.notifyInfoTop.backLayout.setOnClickListener {
            finish()
        }

        adapter.setOnItemClickListener { adapter, view, position ->
            val data = adapter.data[position] as NotifyInfoBean
            val bundle = Bundle()
            bundle.putInt("id", messageInfoId)
            bundle.putString("typeID", data.id)
            if (clickType == 0) {
                NotifyStaffActivity.startGo(this, bundle)
            } else {
                bundle.putString("class",data.name)
                NotifyParentActivity.startGo(this, bundle)
            }
        }

        binding.notifyInfoStaffBtn.setOnClickListener {
            if (clickType == 0) return@setOnClickListener
            clickType = 0
            binding.notifyInfoStaffBtn.setTextColor(R.color.white.asColor())
            it.background = R.drawable.bg_corners_blue_18.asDrawable()
            binding.notifyInfoParentBtn.background = R.drawable.bg_white_18_end.asDrawable()
            binding.notifyInfoParentBtn.setTextColor(R.color.black9.asColor())
            viewModel.requestStaffInfoList(messageInfoId)
        }
        binding.notifyInfoParentBtn.setOnClickListener {
            if (clickType == 1) return@setOnClickListener
            clickType = 1
            binding.notifyInfoParentBtn.setTextColor(R.color.white.asColor())
            it.background = R.drawable.bg_corners_blue_18.asDrawable()
            binding.notifyInfoStaffBtn.background = R.drawable.bg_white_18_start.asDrawable()
            binding.notifyInfoStaffBtn.setTextColor(R.color.black9.asColor())
            viewModel.requestParentInfoList(messageInfoId)
        }
    }


}