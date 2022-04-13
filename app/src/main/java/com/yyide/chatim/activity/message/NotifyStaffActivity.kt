package com.yyide.chatim.activity.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.message.viewmodel.NotifyStaffViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityNotifyListBinding
import com.yyide.chatim.databinding.ItemNoticeNotifyMessageBinding
import com.yyide.chatim.model.message.NotifyInfoBean
import com.yyide.chatim.utils.hide
import com.yyide.chatim.utils.remove

/**
 *
 * @author shenzhibin
 * @date 2022/4/8 17:48
 * @description 通知范围的部门职员Activity
 */
class NotifyStaffActivity :
    KTBaseActivity<ActivityNotifyListBinding>(ActivityNotifyListBinding::inflate) {
    companion object {
        fun startGo(context: Context, item: Bundle) {
            val intent = Intent(context, NotifyStaffActivity::class.java)
            intent.putExtra("data", item)
            context.startActivity(intent)
        }
    }

    private val adapter: BaseQuickAdapter<NotifyInfoBean, BaseViewHolder> by lazy {
        object :
            BaseQuickAdapter<NotifyInfoBean, BaseViewHolder>(R.layout.item_notice_notify_message) {
            override fun convert(holder: BaseViewHolder, item: NotifyInfoBean) {
                val viewBind = ItemNoticeNotifyMessageBinding.bind(holder.itemView)
                viewBind.notifyRightIv.hide()
                viewBind.itemNoticeNotifyTv.text = item.name
            }
        }
    }

    private val viewModel by viewModels<NotifyStaffViewModel>()

    // 信息内容的id
    private var id = 0

    // 类型id
    private var typeID = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        viewModel.requestStaffList(id,typeID)
    }

    override fun initView() {
        super.initView()

        val bundle = intent.getBundleExtra("data")
        bundle?.let {
            id = it.getInt("id")
            typeID = it.getString("typeID") ?: "0"
        }

        binding.notifyInfoTop.title.text = "教职工"
        binding.notifyInfoBtnCl.remove()

        binding.notifyInfoRv.layoutManager = LinearLayoutManager(this)
        binding.notifyInfoRv.adapter = adapter
        adapter.setEmptyView(R.layout.empty)

        viewModel.staffList.observe(this) {
            val data = it.getOrNull() ?: return@observe
            adapter.setList(data)
        }

    }

    fun initListener() {
        binding.notifyInfoTop.backLayout.setOnClickListener {
            finish()
        }
    }
}