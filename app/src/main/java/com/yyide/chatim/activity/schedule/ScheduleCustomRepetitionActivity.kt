package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.database.ScheduleBean
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.jsonToMap
import com.yyide.chatim.databinding.ActivityScheduleCustomRepetitionBinding
import com.yyide.chatim.model.schedule.MonthBean
import com.yyide.chatim.model.schedule.RepetitionDataBean
import com.yyide.chatim.model.schedule.WeekBean
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.widget.SpaceItemDecoration
import com.yyide.chatim.widget.WheelView
import com.yyide.chatim.widget.scrollpicker.adapter.ScrollPickerAdapter
import com.yyide.chatim.widget.scrollpicker.adapter.ScrollPickerAdapter.ScrollPickerAdapterBuilder
import com.yyide.chatim.widget.scrollpicker.view.ScrollPickerView
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.collections.ArrayList

class ScheduleCustomRepetitionActivity : BaseActivity() {
    var jsonToMap = mutableMapOf<String, Any?>()
    var number = AtomicReference<String>()
    var unit = AtomicReference<String>()
    //是否显示截止时期选择
    private var showDeadline = false
    //默认永不截止
    private var showDeadlineNever = true
    private var until = ""
    private var yearIndex = 0
    private var monthIndex = 0
    private var dayIndex = 0
    private lateinit var dayList:List<String>
    lateinit var scheduleRepetitionBinding: ActivityScheduleCustomRepetitionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleRepetitionBinding = ActivityScheduleCustomRepetitionBinding.inflate(layoutInflater)
        setContentView(scheduleRepetitionBinding.root)
        initData()
        initView()
    }

    private fun initData() {
        val stringExtra = intent.getStringExtra("rule")
        jsonToMap = jsonToMap(stringExtra ?: "")
        loge("stringExtra ${stringExtra}")
        //默认值
        number.set("1")
        unit.set("天")
        //{byweekday=[MO, TU, WE, FR], freq=weekly, interval=1}
        //{bymonthday=[1, 2, 3, 4, 5, 6], freq=monthly, interval=1}
    }

    private fun initView() {
        val weekList = WeekBean.getList()
        val monthList = MonthBean.getList()
        val repetitionDataBeanList = RepetitionDataBean.getList()
        val freq = jsonToMap["freq"]
        val interval = jsonToMap["interval"]?.toString() ?: "1"
        until = jsonToMap["until"]?.toString()?:""
        number.set(interval)
        if (freq == "daily" || freq == "DAILY"){
            unit.set("天")
        }

        if (freq == "weekly" || freq == "WEEKLY") {
            unit.set("周")
            var byweekday: List<String>
            try {
                byweekday = JSON.parseArray(jsonToMap["byweekday"]?.toString(), String::class.java)
            } catch (e: Exception) {
                byweekday =
                    jsonToMap["byweekday"].toString().replace("[", "").replace("]", "").split(",")
                        .map { it.trim() }
            }
            loge("byweekday $byweekday")
            byweekday.also {
                weekList.forEach { weekBean ->
                    weekBean.checked = it.contains(weekBean.shortname)
                }
            }
        }

        if (freq == "monthly" || freq == "MONTHLY") {
            unit.set("月")
            var bymonthday: List<String>
            try {
                bymonthday =
                    JSON.parseArray(jsonToMap["bymonthday"]?.toString(), String::class.java)
            } catch (e: Exception) {
                bymonthday =
                    jsonToMap["byweekday"].toString().replace("[", "").replace("]", "").split(",")
                        .map {
                            it.trim()
                        }
            }
            loge("bymonthday $bymonthday")
            bymonthday.also {
                monthList.forEach { monthBean ->
                    monthBean.checked = it.contains(monthBean.title)
                }
            }
        }

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
            val rule = mutableMapOf<String, Any?>()
            if (unitStr == "天") {
                //rule = "每${numberStr}天"
                //rule = "{\"freq\": \"daily\",\"interval\": \"${numberStr}\"}"
                rule["freq"] = "daily"
                rule["interval"] = numberStr

            } else if (unitStr == "月") {
                val selectMonth = monthList.filter { it.checked }
                //rule = "每${numberStr}月 $selectMonth"
                if (selectMonth.isNotEmpty()) {
                    val bymonthday =
                        selectMonth.map { it.title }
                            //.toString()//.replace("[", "").replace("]", "")
                    //rule = "{\"freq\": \"monthly\",\"interval\": \"${numberStr}\",\"bymonthday\":\"${bymonthday}\"}"
                    rule["freq"] = "monthly"
                    rule["interval"] = numberStr
                    rule["bymonthday"] = JSON.toJSONString(bymonthday)

                } else {
                    //rule = "{\"freq\": \"monthly\",\"interval\": \"${numberStr}\"}"
                    rule["freq"] = "monthly"
                    rule["interval"] = numberStr
                }
            } else if (unitStr == "周") {
                val selectWeek = weekList.filter { it.checked }
                if (selectWeek.isNotEmpty()) {
                    val byweekday =
                        selectWeek.map { it.shortname }
                            //.toString()//.replace("[", "").replace("]", "")
                    //rule = "{\"freq\": \"weekly\",\"interval\": \"${numberStr}\",\"byweekday\":\"${byweekday}\"}"
                    rule["freq"] = "weekly"
                    rule["interval"] = numberStr
                    rule["byweekday"] = JSON.toJSONString(byweekday)
                } else {
                    //rule = "{\"freq\": \"weekly\",\"interval\": \"${numberStr}\"}"
                    rule["freq"] = "weekly"
                    rule["interval"] = numberStr
                }
                //rule = "每${numberStr}周 $selectWeek"
            }
            //添加截止时间
            if (showDeadlineNever) {
                rule["until"] = null
            } else {
                until = "${RepetitionDataBean.getYearList()[yearIndex]}-${RepetitionDataBean.getMonthList()[monthIndex]}-${dayList[dayIndex]} 23:59:59"
                rule["until"] = until
            }
            val intent = intent
            intent.putExtra("rule", JSON.toJSONString(rule))
            setResult(RESULT_OK, intent)
            finish()
        }
        val listunit = repetitionDataBeanList.map { it.unit }
        val list = listOf("每")
        val unitIndex = RepetitionDataBean.getUnitIndex(unit.toString(), repetitionDataBeanList)
        scheduleRepetitionBinding.eachWv.setData(list)
        scheduleRepetitionBinding.eachWv.setDefault(0)
        scheduleRepetitionBinding.unitWv.setData(listunit)
        scheduleRepetitionBinding.unitWv.setDefault(unitIndex)
        scheduleRepetitionBinding.numberWv.setOnSelectListener(object : WheelView.OnSelectListener {
            override fun endSelect(id: Int, text: String?) {
                loge("endSelect id=$id,text=$text")
                number.set(text)
            }

            override fun selecting(id: Int, text: String?) {
                loge("selecting id=$id,text=$text")
            }
        })
        val numberlist = repetitionDataBeanList.get(unitIndex).number
        scheduleRepetitionBinding.numberWv.setData(numberlist as ArrayList<String>?)
        val numberIndex = (number.toString().toInt()?:1) -1
        scheduleRepetitionBinding.numberWv.setDefault(numberIndex)
        scheduleRepetitionBinding.unitWv.setOnSelectListener(object : WheelView.OnSelectListener {
            override fun endSelect(id: Int, text: String?) {
                loge("endSelect id=$id,text=$text")
                unit.set(text)
                text?.also {
                    this@ScheduleCustomRepetitionActivity.runOnUiThread {
                        val numberList = RepetitionDataBean.getNumberList(it, repetitionDataBeanList)
                        scheduleRepetitionBinding.numberWv.setData(numberList)
                        scheduleRepetitionBinding.numberWv.setDefault(0)

                        if (it == "月") {
                            scheduleRepetitionBinding.rvWeekList.visibility = View.GONE
                            scheduleRepetitionBinding.rvMonthList.visibility = View.VISIBLE
                            scheduleRepetitionBinding.clBottomDeadline.visibility = View.GONE
                        } else if (it == "周") {
                            scheduleRepetitionBinding.rvWeekList.visibility = View.VISIBLE
                            scheduleRepetitionBinding.rvMonthList.visibility = View.GONE
                            scheduleRepetitionBinding.clBottomDeadline.visibility = View.GONE
                        } else {
                            scheduleRepetitionBinding.rvWeekList.visibility = View.GONE
                            scheduleRepetitionBinding.rvMonthList.visibility = View.GONE
                        }
                    }
                }
            }

            override fun selecting(id: Int, text: String?) {
                loge("selecting id=$id,text=$text")
            }

        })

        //截止日期选择
        val yearList = RepetitionDataBean.getYearList()
        val monthList1 = RepetitionDataBean.getMonthList()
        var now = DateTime.now()
        if (TextUtils.isEmpty(until)) {
            showDeadlineNever = true
            scheduleRepetitionBinding.ivDeadlineNever.visibility = View.VISIBLE
        } else {
            showDeadlineNever = false
            scheduleRepetitionBinding.ivDeadlineNever.visibility = View.GONE
            now = ScheduleDaoUtil.toDateTime(until)
        }
        dayList = RepetitionDataBean.getDayList("${now.year}", "${now.monthOfYear}")
        yearIndex = yearList.indexOfFirst { it == "${now.year}" }
        monthIndex = monthList1.indexOfFirst { it == "${now.monthOfYear}" }
        dayIndex = dayList.indexOfFirst { it == "${now.dayOfMonth}" }

        scheduleRepetitionBinding.yearWv.setData(yearList.map { "$it 年" })
        scheduleRepetitionBinding.yearWv.setDefault(yearIndex)
        scheduleRepetitionBinding.monthWv.setData(monthList1.map { "$it 月" })
        scheduleRepetitionBinding.monthWv.setDefault(monthIndex)
        scheduleRepetitionBinding.dayWv.setData(dayList.map { "$it 日" })
        scheduleRepetitionBinding.dayWv.setDefault(dayIndex)

        scheduleRepetitionBinding.yearWv.setOnSelectListener(object :WheelView.OnSelectListener{
            override fun endSelect(id: Int, text: String?) {
                loge("id=$id,选中${text}")
                if (id != yearIndex){
                    yearIndex = id
                    val year = RepetitionDataBean.getYearList()[yearIndex]
                    val month = RepetitionDataBean.getMonthList()[monthIndex]
                    dayList = RepetitionDataBean.getDayList(year, month)
                    scheduleRepetitionBinding.dayWv.setData(dayList.map { "$it 日" })
                    showDeadlineNever = false
                    scheduleRepetitionBinding.ivDeadlineNever.visibility = View.GONE
                }

            }

            override fun selecting(id: Int, text: String?) {
                loge("selecting id=$id,text=$text")
            }

        })

        scheduleRepetitionBinding.monthWv.setOnSelectListener(object :WheelView.OnSelectListener{
            override fun endSelect(id: Int, text: String?) {
                loge("id=$id,选中${text}")
                if (id != monthIndex){
                    monthIndex = id
                    val year = RepetitionDataBean.getYearList()[yearIndex]
                    val month = RepetitionDataBean.getMonthList()[monthIndex]
                    dayList = RepetitionDataBean.getDayList(year, month)
                    scheduleRepetitionBinding.dayWv.setData(dayList.map { "$it 日" })
                    showDeadlineNever = false
                    scheduleRepetitionBinding.ivDeadlineNever.visibility = View.GONE
                }
            }

            override fun selecting(id: Int, text: String?) {
                loge("selecting id=$id,text=$text")
            }

        })

        scheduleRepetitionBinding.dayWv.setOnSelectListener(object :WheelView.OnSelectListener{
            override fun endSelect(id: Int, text: String?) {
                loge("id=$id,选中${text}")
                dayIndex = id
                showDeadlineNever = false
                scheduleRepetitionBinding.ivDeadlineNever.visibility = View.GONE
            }

            override fun selecting(id: Int, text: String?) {
                loge("selecting id=$id,text=$text")
            }

        })

        scheduleRepetitionBinding.clDeadline.setOnClickListener {
            if (showDeadline) {
                showDeadline = false
                scheduleRepetitionBinding.ivUnfold.setImageResource(R.drawable.icon_arrow_down)
                scheduleRepetitionBinding.clBottomDeadline.visibility = View.GONE
                if (unit.get() == "周") {
                    scheduleRepetitionBinding.rvWeekList.visibility = View.VISIBLE
                } else if (unit.get() == "月") {
                    scheduleRepetitionBinding.rvMonthList.visibility = View.VISIBLE
                }
                return@setOnClickListener
            }
            showDeadline = true
            scheduleRepetitionBinding.ivUnfold.setImageResource(R.drawable.icon_arrow_up)
            scheduleRepetitionBinding.clBottomDeadline.visibility = View.VISIBLE
            if (unit.get() == "周") {
                scheduleRepetitionBinding.rvWeekList.visibility = View.GONE
            } else if (unit.get() == "月") {
                scheduleRepetitionBinding.rvMonthList.visibility = View.GONE
            }
        }
        //选择永不重复
        scheduleRepetitionBinding.clDeadlineType.setOnClickListener {
            if (showDeadlineNever) {
                showDeadlineNever = false
                scheduleRepetitionBinding.ivDeadlineNever.visibility = View.GONE
            } else {
                showDeadlineNever = true
                scheduleRepetitionBinding.ivDeadlineNever.visibility = View.VISIBLE
            }
        }


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
            protected override fun convert(
                baseViewHolder: BaseViewHolder,
                monthBean: MonthBean
            ) {
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
        loge("unit:${unit.get()}")
        if (unit.get() == "月") {
            scheduleRepetitionBinding.rvWeekList.visibility = View.GONE
            scheduleRepetitionBinding.rvMonthList.visibility = View.VISIBLE
        } else if (unit.get() == "周") {
            scheduleRepetitionBinding.rvWeekList.visibility = View.VISIBLE
            scheduleRepetitionBinding.rvMonthList.visibility = View.GONE
        } else {
            scheduleRepetitionBinding.rvWeekList.visibility = View.GONE
            scheduleRepetitionBinding.rvMonthList.visibility = View.GONE
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_custom_repetition
}