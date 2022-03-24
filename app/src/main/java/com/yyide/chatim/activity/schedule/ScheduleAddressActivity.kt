package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.baozi.treerecyclerview.adpater.TreeRecyclerAdapter
import com.baozi.treerecyclerview.factory.ItemHelperFactory
import com.baozi.treerecyclerview.item.TreeItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.adapter.address.AddressItem
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleAddressBinding
import com.yyide.chatim.model.address.ScheduleAddressBean
import com.yyide.chatim.model.address.SiteListItem
import com.yyide.chatim.model.schedule.SiteNameRsp
import com.yyide.chatim.utils.hide
import com.yyide.chatim.utils.logd
import com.yyide.chatim.utils.remove
import com.yyide.chatim.utils.show
import com.yyide.chatim.viewmodel.SiteManageViewModel


class ScheduleAddressActivity : BaseActivity() {
    lateinit var scheduleAddressBinding: ActivityScheduleAddressBinding
    private val siteManageViewModel: SiteManageViewModel by viewModels()
    //private val list = mutableListOf<SiteNameRsp.DataBean>()
    //private lateinit var adapter: BaseQuickAdapter<SiteNameRsp.DataBean, BaseViewHolder>

    private lateinit var treeAdapter: TreeRecyclerAdapter
    private val list = mutableListOf<ScheduleAddressBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleAddressBinding = ActivityScheduleAddressBinding.inflate(layoutInflater)
        setContentView(scheduleAddressBinding.root)
        initView()
        initData()
    }

    private fun initData() {
        val stringExtra = intent.getStringExtra("data")
        val siteNameBean: SiteNameRsp.DataBean? = JSON.parseObject(stringExtra, SiteNameRsp.DataBean::class.java)

        siteManageViewModel.siteInfoListData.observe(this) {
            if (!it.isNullOrEmpty()) {
                list.clear()
                list.addAll(it)
                if (siteNameBean != null) {
                    it.forEach { firstData ->
                        firstData.siteList.forEach { secondData ->
                            if (secondData.id == siteNameBean.id){
                                secondData.check = true
                                scheduleAddressBinding.ivNotRemind.remove()
                                siteManageViewModel.selectAddress.id = secondData.id
                                siteManageViewModel.selectAddress.name = secondData.name
                            }
                        }
                    }
                }
                val itemListData = ItemHelperFactory.createItems(it)
                treeAdapter.itemManager.replaceAllItem(itemListData)
            }
        }

        siteManageViewModel.getSiteName()


    }

    private fun initView() {
        scheduleAddressBinding.top.title.text = "选择场地"
        scheduleAddressBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleAddressBinding.top.tvRight.visibility = View.VISIBLE
        scheduleAddressBinding.top.tvRight.text = "完成"
        scheduleAddressBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleAddressBinding.top.tvRight.setOnClickListener {
            val intent1 = intent
            intent1.putExtra("address", JSON.toJSONString(siteManageViewModel.selectAddress))
            setResult(RESULT_OK, intent1)
            finish()
        }

        /*adapter = object :
            BaseQuickAdapter<SiteNameRsp.DataBean, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {
            override fun convert(holder: BaseViewHolder, item: SiteNameRsp.DataBean) {
                holder.setText(R.id.tv_title, item.name)
                val ivRemind = holder.getView<ImageView>(R.id.iv_remind)
                ivRemind.visibility = if (item.checked) View.VISIBLE else View.GONE
                holder.itemView.setOnClickListener { v: View? ->
                    for (remind1 in list) {
                        remind1.checked = false
                    }
                    scheduleAddressBinding.ivNotRemind.visibility = View.GONE
                    item.checked = true
                    notifyDataSetChanged()
                }
            }
        }
        adapter.setList(list)*/

        treeAdapter = TreeRecyclerAdapter()


        scheduleAddressBinding.rvRemindList.layoutManager = LinearLayoutManager(this)
        //scheduleAddressBinding.rvRemindList.adapter = adapter
        scheduleAddressBinding.rvRemindList.adapter = treeAdapter

        scheduleAddressBinding.clWhetherRemind.setOnClickListener { v ->
            siteManageViewModel.selectAddress.id = ""
            siteManageViewModel.selectAddress.name = "无场地"
            scheduleAddressBinding.ivNotRemind.show()
            list.forEach {
                for (secondData in it.siteList){
                    secondData.check = false
                }
            }
            val itemListData = ItemHelperFactory.createItems(list)
            treeAdapter.itemManager.replaceAllItem(itemListData)
        }


    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_address

    // 点击事件后将目标的属性赋予选中地址信息
    fun notifyCurrentSelectAddressInfo(data : SiteListItem){
        logd("info = ${data.name},id = ${data.id}")
        scheduleAddressBinding.ivNotRemind.remove()
        list.forEach { firstData ->
            firstData.siteList.forEach { secondData ->
                if (secondData.id == data.id){
                    logd("search get")
                    secondData.check = true
                    scheduleAddressBinding.ivNotRemind.remove()
                    siteManageViewModel.selectAddress.id = data.id
                    siteManageViewModel.selectAddress.name = data.name
                }else{
                    secondData.check = false
                }
            }
        }
        treeAdapter.itemManager.notifyDataChanged()
    }
}