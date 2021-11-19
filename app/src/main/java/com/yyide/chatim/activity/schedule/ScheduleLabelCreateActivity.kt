package com.yyide.chatim.activity.schedule

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.databinding.ActivityScheduleLabelCreateBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.schedule.LabelColor
import com.yyide.chatim.model.schedule.NewLabel
import com.yyide.chatim.model.schedule.OldLabel
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.MaxTextLengthFilter
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.LabelManageViewModel
import com.yyide.chatim.widget.CircleFrameLayout
import com.yyide.chatim.widget.SpaceItemDecoration
import org.greenrobot.eventbus.EventBus

class ScheduleLabelCreateActivity : BaseActivity() {
    lateinit var labelCreateBinding: ActivityScheduleLabelCreateBinding
    val labelManageViewModel: LabelManageViewModel by viewModels()
    val labelColorList = LabelColor.getLabelColorList()
    var editOps: Boolean = false
    lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        labelCreateBinding = ActivityScheduleLabelCreateBinding.inflate(layoutInflater)
        setContentView(labelCreateBinding.root)
        val id = intent.getStringExtra("id")
        this.id = id ?: ""
        val labelName = intent.getStringExtra("labelName")
        val colorValue = intent.getStringExtra("colorValue")
        if (!TextUtils.isEmpty(id)) {
            //编辑标签
            editOps = true
            labelCreateBinding.edit.setText(labelName)
            labelColorList.forEach {
                if (it.color == colorValue) {
                    it.checked = true
                }
            }
        }
        initView()
    }

    private fun initView() {
        labelCreateBinding.top.title.text = "标签管理"
        labelCreateBinding.top.backLayout.setOnClickListener {
            finish()
        }
        labelCreateBinding.top.tvRight.visibility = View.VISIBLE
        labelCreateBinding.top.tvRight.text = "确定"
        labelCreateBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        //labelCreateBinding.top.tvRight.textSize = DisplayUtils.px2sp(this,16f).toFloat()
        labelCreateBinding.top.tvRight.setOnClickListener {
            addOrEditLabel()
            //finish()
        }

        val adapter =
            object : BaseQuickAdapter<LabelColor, BaseViewHolder>(R.layout.item_label_color) {
                override fun convert(holder: BaseViewHolder, item: LabelColor) {
                    holder.getView<CircleFrameLayout>(R.id.v_border_circle)
                        .setRadius(DisplayUtils.dip2px(this@ScheduleLabelCreateActivity, 24f))
                    holder.getView<CircleFrameLayout>(R.id.v_border_circle)
                        .setBackgroundColor(Color.parseColor(item.color))
                    holder.getView<View>(R.id.v_border).visibility =
                        if (item.checked) View.VISIBLE else View.INVISIBLE
                    if (item.color == LabelColor.color1) {
                        holder.getView<CircleFrameLayout>(R.id.v_border_circle).visibility =
                            View.GONE
                        holder.getView<ImageView>(R.id.iv_default_color).visibility = View.VISIBLE
                    } else {
                        holder.getView<CircleFrameLayout>(R.id.v_border_circle).visibility =
                            View.VISIBLE
                        holder.getView<ImageView>(R.id.iv_default_color).visibility = View.GONE
                    }
                    holder.itemView.setOnClickListener {
                        labelColorList.forEach {
                            it.checked = false
                        }
                        item.checked = true
                        notifyDataSetChanged()
                    }
                }
            }
        adapter.setList(labelColorList)
        labelCreateBinding.rvLabelList.layoutManager = GridLayoutManager(this, 6)
        labelCreateBinding.rvLabelList.addItemDecoration(
            SpaceItemDecoration(DisplayUtils.dip2px(this, 10f), 6)
        )
        labelCreateBinding.rvLabelList.adapter = adapter
        labelCreateBinding.edit.filters = arrayOf<InputFilter>(MaxTextLengthFilter(20))
    }

    private fun addOrEditLabel() {
        val labelName = labelCreateBinding.edit.text.toString()
        if (TextUtils.isEmpty(labelName)) {
            ToastUtils.showShort("请输入标签名称")
            return
        }
        var colorValue = LabelColor.color1
        try {
            val labelColor: LabelColor = labelColorList.first { it.checked }
            colorValue = labelColor.color
        } catch (e: Exception) {
            loge("选择默认颜色")
        }
        if (editOps) {
            val oldLabel = OldLabel(id, labelName, colorValue)
            labelManageViewModel.editLabel(oldLabel)
        } else {
            val labelList = mutableListOf<NewLabel>()
            labelList.add(NewLabel(labelName, colorValue))
            labelManageViewModel.addLabel(labelList)
        }
        labelManageViewModel.getLabelAddOrEditResult().observe(this, {
            if (it) {
                if (editOps){
                    EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA,""))
                }
                ToastUtils.showShort(if (editOps) "标签修改成功" else "标签添加成功")
                finish()
            } else {
                ToastUtils.showShort(if (editOps) "标签修改失败" else "标签添加失败")
            }
        })
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_label_create
}