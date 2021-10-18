package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yanzhenjie.recyclerview.*
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleLabelManageBinding
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.utils.ColorUtil
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.LabelManageViewModel

class ScheduleLabelManageActivity : BaseActivity() {
    lateinit var labelManageBinding: ActivityScheduleLabelManageBinding
    private var labelList = mutableListOf<LabelListRsp.DataBean>()
    private val labelManageViewModel: LabelManageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        labelManageBinding = ActivityScheduleLabelManageBinding.inflate(layoutInflater)
        setContentView(labelManageBinding.root)
        initView()
        labelManageViewModel.getLabelList().observe(this, Observer {
            if (it.isEmpty()) {
                loge("没有数据")
            }
            labelList.clear()
            labelList.addAll(it)
            adapter.setList(labelList)
        })
    }

    override fun onRestart() {
        super.onRestart()
        labelManageViewModel.selectLabelList()
    }

    private val adapter = object :
        BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_label_manage_list) {
        override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
            holder.setText(R.id.tv_label, item.labelName)
            val drawable = GradientDrawable()
            drawable.cornerRadius =
                DisplayUtils.dip2px(this@ScheduleLabelManageActivity, 2f).toFloat()
            drawable.setColor(ColorUtil.parseColor(item.colorValue))
            holder.getView<TextView>(R.id.tv_label).background = drawable
        }
    }

    private fun initView() {
        labelManageBinding.top.title.text = "标签管理"
        labelManageBinding.top.backLayout.setOnClickListener {
            finish()
        }
        labelManageBinding.cvLabelList.setSwipeMenuCreator(swipeMenuCreator)
        labelManageBinding.cvLabelList.setOnItemMenuClickListener(mMenuItemClickListener)
        adapter.setList(labelList)
        labelManageBinding.cvLabelList.layoutManager = LinearLayoutManager(this)
        labelManageBinding.cvLabelList.addItemDecoration(DefaultItemDecoration(resources.getColor(R.color.default_item_decoration_color)))
        labelManageBinding.cvLabelList.adapter = adapter
        labelManageBinding.clAddLabel.setOnClickListener {
            startActivity(Intent(this, ScheduleLabelCreateActivity::class.java))
        }
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_label_manage
    }

    private val swipeMenuCreator: SwipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(
            swipeLeftMenu: SwipeMenu,
            swipeRightMenu: SwipeMenu,
            position: Int
        ) {
            val width = DisplayUtils.dip2px(this@ScheduleLabelManageActivity, 68f)
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            run {
                val modifyItem: SwipeMenuItem =
                    SwipeMenuItem(this@ScheduleLabelManageActivity).setBackground(R.drawable.selector_blue)
                        //.setImage(R.drawable.ic_action_delete)
                        .setText("修改")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height)
                swipeRightMenu.addMenuItem(modifyItem) // 添加菜单到右侧。
                val delItem: SwipeMenuItem =
                    SwipeMenuItem(this@ScheduleLabelManageActivity).setBackground(R.drawable.selector_red)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height)
                swipeRightMenu.addMenuItem(delItem) // 添加菜单到右侧。
            }
        }
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private val mMenuItemClickListener =
        OnItemMenuClickListener { menuBridge, position ->
            menuBridge.closeMenu()
            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                val item = labelList[position]
                if (menuPosition == 0) {
                    loge("修改便签$item")
                    val intent = Intent(
                        this@ScheduleLabelManageActivity,
                        ScheduleLabelCreateActivity::class.java
                    )
                    intent.putExtra("id", item.id)
                    intent.putExtra("labelName", item.labelName)
                    intent.putExtra("colorValue", item.colorValue)
                    startActivity(intent)
                    return@OnItemMenuClickListener
                }
                if (menuPosition == 1) {
                    loge("删除标签$item")
                    labelManageViewModel.deleteLabelById(item.id ?: "-1")
                    labelManageViewModel.getLabelDeleteResult()
                        .observe(this@ScheduleLabelManageActivity, {
                            if (it) {
                                labelManageViewModel.selectLabelList()
                            }
                        })
                    return@OnItemMenuClickListener
                }

            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                loge("list第$position; 左侧菜单第$menuPosition")
            }
        }
}