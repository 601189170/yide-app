package com.yyide.chatim.activity.schedule

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleLabelCreateBinding
import com.yyide.chatim.model.schedule.LabelColor
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.widget.CircleFrameLayout
import com.yyide.chatim.widget.SpaceItemDecoration

class ScheduleLabelCreateActivity : BaseActivity() {
    lateinit var labelCreateBinding: ActivityScheduleLabelCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        labelCreateBinding = ActivityScheduleLabelCreateBinding.inflate(layoutInflater)
        setContentView(labelCreateBinding.root)
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
            finish()
        }

        val labelColorList = LabelColor.getLabelColorList()
        val adapter =
            object : BaseQuickAdapter<LabelColor, BaseViewHolder>(R.layout.item_label_color) {
                override fun convert(holder: BaseViewHolder, item: LabelColor) {
                    holder.getView<CircleFrameLayout>(R.id.v_border_circle)
                        .setRadius(DisplayUtils.dip2px(this@ScheduleLabelCreateActivity, 24f))
                    holder.getView<CircleFrameLayout>(R.id.v_border_circle)
                        .setBackgroundColor(Color.parseColor(item.color))
                    holder.getView<View>(R.id.v_border).visibility =
                        if (item.checked) View.VISIBLE else View.INVISIBLE
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
            SpaceItemDecoration(DisplayUtils.dip2px(this, 24f),6)
        )
        labelCreateBinding.rvLabelList.adapter = adapter
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_label_create
}