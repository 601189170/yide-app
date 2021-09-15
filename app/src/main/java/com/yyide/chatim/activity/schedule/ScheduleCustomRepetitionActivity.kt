package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleCustomRepetitionBinding
import com.yyide.chatim.databinding.ActivityScheduleRepetitionBinding
import com.yyide.chatim.model.schedule.MonthBean
import com.yyide.chatim.model.schedule.MonthBean.Companion.getList
import com.yyide.chatim.model.schedule.Repetition
import com.yyide.chatim.model.schedule.Repetition.Companion.getList
import com.yyide.chatim.model.schedule.WeekBean
import com.yyide.chatim.model.schedule.WeekBean.Companion.getList
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.widget.SpaceItemDecoration
import com.yyide.chatim.widget.scrollpicker.adapter.ScrollPickerAdapter
import com.yyide.chatim.widget.scrollpicker.adapter.ScrollPickerAdapter.ScrollPickerAdapterBuilder
import com.yyide.chatim.widget.scrollpicker.view.ScrollPickerView
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicReference

class ScheduleCustomRepetitionActivity : BaseActivity() {
    val list: MutableList<String> = ArrayList()
    val list2: MutableList<String> = ArrayList()
    val list22: MutableList<String> = ArrayList()
    val list23: MutableList<String> = ArrayList()
    val list3: MutableList<String> = ArrayList()
    var number = AtomicReference<String>()
    var unit = AtomicReference<String>()
    lateinit var scheduleRepetitionBinding: ActivityScheduleCustomRepetitionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleRepetitionBinding = ActivityScheduleCustomRepetitionBinding.inflate(layoutInflater)
        setContentView(scheduleRepetitionBinding.root)
        initData()
        initView()
    }

    private fun initData() {
        list.add("每")
        for (i in 0..30) {
            list2.add("" + (i + 1))
        }
        for (i in 0..71) {
            list22.add("" + (i + 1))
        }
        for (i in 0..11) {
            list23.add("" + (i + 1))
        }
        list3.add("天")
        list3.add("月")
        list3.add("周")
    }

    private fun initView() {
        scheduleRepetitionBinding.top.title.text = "自定义重复"
        scheduleRepetitionBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleRepetitionBinding.top.tvRight.visibility = View.VISIBLE
        scheduleRepetitionBinding.top.tvRight.text = "完成"
        scheduleRepetitionBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleRepetitionBinding.top.tvRight.setOnClickListener {
            finish()
        }

        val scrollPickerView: ScrollPickerView = scheduleRepetitionBinding.scrollPickerView
        scrollPickerView.layoutManager = LinearLayoutManager(this)
        val builder = ScrollPickerAdapterBuilder<String>(this)
            .setDataList(list)
            .selectedItemOffset(1)
            .visibleItemNumber(3)
            .setDivideLineColor("#F2F7FA")
            .setItemViewProvider(null)
            .setOnClickListener { v ->
                val text = v.tag as String
                loge("onSelectedItemClicked: $text")
            }
        val mScrollPickerAdapter = builder.build()
        scrollPickerView.adapter = mScrollPickerAdapter


        val scrollPickerNumber: ScrollPickerView = scheduleRepetitionBinding.scrollPickerNumber
        scrollPickerNumber.layoutManager = LinearLayoutManager(this)
        val builder2 = ScrollPickerAdapterBuilder<String>(this)
            .setDataList(list2)
            .selectedItemOffset(1)
            .visibleItemNumber(3)
            .setDivideLineColor("#F2F7FA")
            .setItemViewProvider(null)
            .setOnScrolledListener { v: View ->
                val text = v.tag as String
                loge("onSelectedItemClicked: $text")
                number.set(text)
            }
        val scrollPickerNumberAdapter = builder2.build()
        scrollPickerNumber.adapter = scrollPickerNumberAdapter

        val scrollPickerUnit: ScrollPickerView = scheduleRepetitionBinding.scrollPickerUnit
        scrollPickerUnit.layoutManager = LinearLayoutManager(this)
        val builder3 = ScrollPickerAdapterBuilder<String>(this)
            .setDataList(list3)
            .selectedItemOffset(1)
            .visibleItemNumber(3)
            .setDivideLineColor("#F2F7FA")
            .setItemViewProvider(null)
            .setOnScrolledListener { v: View ->
                val text = v.tag as String
                loge("onSelectedItemClicked: $text")
                unit.set(text)
                if ("天" == text) {
                    scrollPickerNumberAdapter.setDataList(list2)
                    scrollPickerNumberAdapter.notifyDataSetChanged()
                    scheduleRepetitionBinding.rvWeekList.setVisibility(View.GONE)
                    scheduleRepetitionBinding.rvMonthList.setVisibility(View.GONE)
                } else if ("月" == text) {
                    scrollPickerNumberAdapter.setDataList(list23)
                    scrollPickerNumberAdapter.notifyDataSetChanged()
                    scheduleRepetitionBinding.rvWeekList.setVisibility(View.GONE)
                    scheduleRepetitionBinding.rvMonthList.setVisibility(View.VISIBLE)
                } else if ("周" == text) {
                    scrollPickerNumberAdapter.setDataList(list22)
                    scrollPickerNumberAdapter.notifyDataSetChanged()
                    scheduleRepetitionBinding.rvWeekList.setVisibility(View.VISIBLE)
                    scheduleRepetitionBinding.rvMonthList.setVisibility(View.GONE)
                }
            }
        val scrollPickerUnitAdapter = builder3.build()
        scrollPickerUnit.adapter = scrollPickerUnitAdapter

        val weekList = WeekBean.getList()
        scheduleRepetitionBinding.rvWeekList.setLayoutManager(GridLayoutManager(this, 3))
        val adapter: BaseQuickAdapter<WeekBean, BaseViewHolder> = object :
            BaseQuickAdapter<WeekBean, BaseViewHolder>(R.layout.item_dialog_week_custom_repetition) {
            protected override fun convert(baseViewHolder: BaseViewHolder, weekBean: WeekBean) {
                baseViewHolder.setText(R.id.title, weekBean.title)
                baseViewHolder.getView<View>(R.id.title).isSelected = weekBean.checked
                baseViewHolder.itemView.setOnClickListener { v: View? ->
                    weekBean.checked = !weekBean.checked
                    baseViewHolder.getView<View>(R.id.title).isSelected = weekBean.checked
                }
            }
        }
        adapter.setList(weekList)
        scheduleRepetitionBinding.rvWeekList.addItemDecoration(
            SpaceItemDecoration(
                DisplayUtils.dip2px(this, 2f),
                3
            )
        )
        scheduleRepetitionBinding.rvWeekList.setAdapter(adapter)

        val monthList = MonthBean.getList()
        scheduleRepetitionBinding.rvMonthList.setLayoutManager(GridLayoutManager(this, 7))
        val quickAdapter: BaseQuickAdapter<MonthBean, BaseViewHolder> = object :
            BaseQuickAdapter<MonthBean, BaseViewHolder>(R.layout.item_dialog_month_custom_repetition) {
            protected override fun convert(baseViewHolder: BaseViewHolder, monthBean: MonthBean) {
                baseViewHolder.setText(R.id.title, monthBean.title)
                baseViewHolder.getView<View>(R.id.title).isSelected = monthBean.checked
                baseViewHolder.itemView.setOnClickListener { v: View? ->
                    monthBean.checked = !monthBean.checked
                    baseViewHolder.getView<View>(R.id.title).isSelected = monthBean.checked
                }
            }
        }
        quickAdapter.setList(monthList)
        scheduleRepetitionBinding.rvMonthList.addItemDecoration(
            SpaceItemDecoration(
                DisplayUtils.dip2px(this, 2f),
                7
            )
        )
        scheduleRepetitionBinding.rvMonthList.setAdapter(quickAdapter)
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_custom_repetition
}