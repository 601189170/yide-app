package com.yyide.chatim.activity.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.yyide.chatim.R
import com.yyide.chatim.activity.message.viewmodel.NotifyParentViewModel
import com.yyide.chatim.adapter.message.ParentQuickAdapter
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityNotifyListBinding
import com.yyide.chatim.model.message.ElternsItem
import com.yyide.chatim.model.message.NotifyParentBean
import com.yyide.chatim.utils.logd
import com.yyide.chatim.utils.remove

/**
 *
 * @author shenzhibin
 * @date 2022/4/8 14:53
 * @description 通知人员Activity
 */
class NotifyParentActivity :
    KTBaseActivity<ActivityNotifyListBinding>(ActivityNotifyListBinding::inflate) {

    private val viewModel by viewModels<NotifyParentViewModel>()

    companion object {
        fun startGo(context: Context, item: Bundle) {
            val intent = Intent(context, NotifyParentActivity::class.java)
            intent.putExtra("data", item)
            context.startActivity(intent)
        }

        const val FIRST_TREE = 1
        const val SECOND_TREE = 2

    }

    private val adapter = ParentQuickAdapter()


    // 信息内容的id
    private var id = 0

    // 类型id
    private var typeID = "0"

    private var className = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        viewModel.requestStaffList(id, typeID)
    }

    override fun initView() {
        super.initView()

        val bundle = intent.getBundleExtra("data")
        bundle?.let {
            id = it.getInt("id")
            typeID = it.getString("typeID") ?: ""
            className = it.getString("class") ?: ""
        }

        binding.notifyInfoTop.title.text = "家长"
        binding.notifyInfoBtnCl.remove()

        binding.notifyInfoRv.layoutManager = LinearLayoutManager(this)
        binding.notifyInfoRv.adapter = adapter
        adapter.setEmptyView(R.layout.empty)

        viewModel.parentList.observe(this) {

            val data = it.getOrNull() ?: return@observe

            val notifyParentBean = mutableListOf<NotifyParentBean>()
            data.forEach { dataList ->
                val childBeanList = mutableListOf<BaseNode>()
                dataList.children?.forEach { children ->
                    val item = ElternsItem(children.id,children.name,children.relations)
                    childBeanList.add(item)
                }
                val parentData = NotifyParentBean(className,dataList.name,childBeanList)
                notifyParentBean.add(parentData)
            }
            adapter.setList(notifyParentBean)
        }

    }

    fun initListener() {
        binding.notifyInfoTop.backLayout.setOnClickListener {
            finish()
        }
    }



}