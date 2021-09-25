package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleCustomRepetitionBinding
import com.yyide.chatim.model.schedule.MonthBean
import com.yyide.chatim.model.schedule.WeekBean
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.widget.SpaceItemDecoration
import com.yyide.chatim.widget.scrollpicker.adapter.ScrollPickerAdapter
import com.yyide.chatim.widget.scrollpicker.adapter.ScrollPickerAdapter.ScrollPickerAdapterBuilder
import com.yyide.chatim.widget.scrollpicker.view.ScrollPickerView
import java.util.*
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
        val weekList = WeekBean.getList()
        val monthList = MonthBean.getList()
        scheduleRepetitionBinding.top.title.text = "自定义重复"
        scheduleRepetitionBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleRepetitionBinding.top.tvRight.visibility = View.VISIBLE
        scheduleRepetitionBinding.top.tvRight.text = "完成"
        scheduleRepetitionBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleRepetitionBinding.top.tvRight.setOnClickListener {
            val unitStr = unit.get()
            val numberStr = number.get()
            var rule = ""
            if (unitStr == "天") {
                //rule = "每${numberStr}天"
                rule = "{\"freq\": \"daily\",\"interval\": \"${numberStr}\"}"
            } else if (unitStr == "月") {
                val selectMonth = monthList.filter { it.checked }
                //rule = "每${numberStr}月 $selectMonth"
                if (selectMonth.isNotEmpty()) {
                    val bymonthday =
                        selectMonth.map { it.title }.toString().replace("[", "").replace("]", "")
                    rule =
                        "{\"freq\": \"monthly\",\"interval\": \"${numberStr}\",\"bymonthday\":\"${bymonthday}\"}"
                } else {
                    rule = "{\"freq\": \"monthly\",\"interval\": \"${numberStr}\"}"
                }
            } else if (unitStr == "周") {
                val selectWeek = weekList.filter { it.checked }
                if (selectWeek.isNotEmpty()) {
                    val byday =
                        selectWeek.map { it.shortname }.toString().replace("[", "").replace("]", "")
                    rule =
                        "{\"freq\": \"weekly\",\"interval\": \"${numberStr}\",\"byday\":\"${byday}\"}"
                } else {
                    rule = "{\"freq\": \"weekly\",\"interval\": \"${numberStr}\"}"
                }
                //rule = "每${numberStr}周 $selectWeek"
            }
            val intent = intent
            intent.putExtra("rule", rule)
            setResult(RESULT_OK, intent)
            finish()
        }

        val scrollPickerView: ScrollPickerView = scheduleRepetitionBinding.scrollPickerView
        scrollPickerView.layoutManager = LinearLayoutManager(this)
        scrollPickerView.adapter = getScrollPickerAdapter(list) {}


        val scrollPickerNumber: ScrollPickerView = scheduleRepetitionBinding.scrollPickerNumber
        scrollPickerNumber.layoutManager = LinearLayoutManager(this)
        scrollPickerNumber.adapter = getScrollPickerAdapter(list2, number::set)

        val scrollPickerUnit: ScrollPickerView = scheduleRepetitionBinding.scrollPickerUnit
        scrollPickerUnit.layoutManager = LinearLayoutManager(this)
        val scrollPickerUnitAdapter = getScrollPickerAdapter(list3) {

            if (unit.get() == it) {
                return@getScrollPickerAdapter
            }
            unit.set(it)
            if ("天" == it) {
                scrollPickerNumber.adapter = getScrollPickerAdapter(list2, number::set)
                scheduleRepetitionBinding.rvWeekList.setVisibility(View.GONE)
                scheduleRepetitionBinding.rvMonthList.setVisibility(View.GONE)
            } else if ("月" == it) {
                scrollPickerNumber.adapter = getScrollPickerAdapter(list23, number::set)
                scheduleRepetitionBinding.rvWeekList.setVisibility(View.GONE)
                scheduleRepetitionBinding.rvMonthList.setVisibility(View.VISIBLE)
            } else if ("周" == it) {
                scrollPickerNumber.adapter = getScrollPickerAdapter(list22, number::set)
                scheduleRepetitionBinding.rvWeekList.setVisibility(View.VISIBLE)
                scheduleRepetitionBinding.rvMonthList.setVisibility(View.GONE)
            }
        }
        scrollPickerUnit.adapter = scrollPickerUnitAdapter

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

    private fun getScrollPickerAdapter(
        numberList: List<String>,
        listener: (String) -> Unit
    ): ScrollPickerAdapter<*> {
        val builder2 = ScrollPickerAdapterBuilder<String>(this)
            .setDataList(numberList)
            .selectedItemOffset(1)
            .visibleItemNumber(3)
            .setDivideLineColor("#F2F7FA")
            .setItemViewProvider(null)
            .setOnScrolledListener { v: View ->
                val text = v.tag as String
                loge("onSelectedItemClicked: $text")
                listener(text)
            }
        return builder2.build()
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_custom_repetition
}