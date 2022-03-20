package com.yyide.chatim.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.notice.NoticeScopeActivity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.databinding.LayoutScheduleSearchFilter2Binding
import com.yyide.chatim.databinding.LayoutScheduleSearchFilterBinding
import com.yyide.chatim.model.schedule.FilterTagCollect
import com.yyide.chatim.model.schedule.Label
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.model.schedule.ScheduleFilterTag
import com.yyide.chatim.utils.ColorUtil
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.atomic.AtomicReference

/**
 * Created by Administrator on 2019/5/15.
 */
class ScheduleSearchFilterPop(
    val context: Activity?,
    val labelList: List<LabelListRsp.DataBean>,
    val filterTagCollect: FilterTagCollect
) :
    PopupWindow() {
    lateinit var popupWindow: PopupWindow
    lateinit var mWindow: Window
    val dateStart = AtomicReference("")
    val dateEnd = AtomicReference("")

    //private var labelList = mutableListOf<Label>()
    private lateinit var onSelectListener: OnSelectListener
    lateinit var adapter: BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>

    init {
        init()
    }

    fun setOnSelectListener(onSelectListener: OnSelectListener) {
        this.onSelectListener = onSelectListener
    }

    private fun init() {
        val mView = LayoutScheduleSearchFilter2Binding.inflate(context!!.layoutInflater)
        popupWindow = PopupWindow(
            mView.root,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        popupWindow.animationStyle = R.style.popwin_anim_style2
        mView.clBg.setOnClickListener { v ->
            popupWindow.dismiss()
        }
        initView(mView)
        initLabel(mView)
        initDate(mView)
        mView.tvConfirm.setOnClickListener {
            commit(mView)
            //hide()
        }
        mView.tvReset.setOnClickListener {
            reset(mView)
        }
        popupWindow.isFocusable = false //获取焦点
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        popupWindow.isClippingEnabled = false
        // 获取当前Activity的window
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = false
        popupWindow.setBackgroundDrawable(null)
        popupWindow.contentView.isFocusable = true
        popupWindow.contentView.isFocusableInTouchMode = true
        popupWindow.contentView.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (popupWindow.isShowing) {
                    popupWindow.dismiss()
                }
                return@setOnKeyListener true
            }
            false
        }
        popupWindow.setOnDismissListener {

            //如果设置了背景变暗，那么在dissmiss的时候需要还原
            val params = mWindow.attributes
            params.alpha = 1.0f
            mWindow.attributes = params
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            }
        }
        //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
        mWindow = context.window
        val params = mWindow.attributes
        params.alpha = 0.7f
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        mWindow.attributes = params
        popupWindow.showAtLocation(
            mView.root, Gravity.BOTTOM, 0, 0
        )
    }

    private fun initView(binding: LayoutScheduleSearchFilter2Binding) {
        binding.imgFinish.setOnClickListener {
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            }
        }
        //反选类型 日程类型【0：校历日程，1：课表日程，2：事务日程, 3：会议日程】
        filterTagCollect.type?.forEach {
            when (it) {
                2 -> {
                    binding.cbTransactionSchedule.isChecked = true
                }
                0 -> {
                    binding.cbSchoolCalendar.isChecked = true
                }
                3 -> {
                    binding.cbMeeting.isChecked = true
                }
                1 -> {
                    binding.cbClassSchedule.isChecked = true
                }
                else -> {
                }
            }
        }
        //状态
        filterTagCollect.status?.forEach {
            when (it) {
                0 -> {
                    binding.cbUndone.isChecked = true
                }
                1 -> {
                    binding.cbDone.isChecked = true
                }
                else -> {
                }
            }
        }
        //开始日期
        //val startTime = DateTime.now().toString("yyyy-MM-dd ") + "00:00:00"
        dateStart.set(filterTagCollect.startTime)
        //结束日期
        //val endTime = DateTime.now().toString("yyyy-MM-dd ") + "23:59:59"
        dateEnd.set(filterTagCollect.endTime)
        //标签
        labelList.forEach {
            it.checked = filterTagCollect.labelId?.contains(it.id) == true
        }

        binding.cbUndone.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                loge("initView: 代码触发，不处理监听事件。")
                return@setOnCheckedChangeListener
            }
            binding.cbDone.isChecked = !isChecked
        }

        binding.cbDone.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                loge("initView: 代码触发，不处理监听事件。")
                return@setOnCheckedChangeListener
            }
            binding.cbUndone.isChecked = !isChecked
        }
    }

    private fun reset(binding: LayoutScheduleSearchFilter2Binding) {
        binding.cbTransactionSchedule.isChecked = false
        binding.cbSchoolCalendar.isChecked = false
        binding.cbMeeting.isChecked = false
        binding.cbClassSchedule.isChecked = false
        binding.cbUndone.isChecked = false
        binding.cbDone.isChecked = false

        labelList.forEach {
            it.checked = false
        }
        adapter.setList(labelList)
        //重置日期
        dateStart.set(null)
        dateEnd.set(null)
        binding.dateSelect.tvDateStart.text = "请选择"
        binding.dateSelect.tvTimeStart.text = "开始日期"
        binding.dateSelect.tvDateEnd.text = "请选择"
        binding.dateSelect.tvTimeEnd.text = "结束日期"
        //val startTime = DateTime.now().toString("yyyy-MM-dd ") + "00:00:00"
//        dateStart.set(filterTagCollect.startTime)
        //结束日期
        //val endTime = DateTime.now().toString("yyyy-MM-dd ") + "23:59:59"
//        dateEnd.set(filterTagCollect.endTime)
        binding.dateSelect.llVLine.visibility = View.GONE
        binding.dateSelect.dateTimePicker.visibility = View.GONE
    }

    private fun commit(binding: LayoutScheduleSearchFilter2Binding) {
        //日程类型【0：校历日程，1：课表日程，2：事务日程, 3：会议日程】
        val types = mutableListOf<Int>()
        val cbTransactionSchedule = binding.cbTransactionSchedule.isChecked
        if (cbTransactionSchedule) {
            types.add(2)
        }
        val cbSchoolCalendar = binding.cbSchoolCalendar.isChecked
        if (cbSchoolCalendar) {
            types.add(0)
        }
        val cbMeeting = binding.cbMeeting.isChecked
        if (cbMeeting) {
            types.add(3)
        }
        val cbClassSchedule = binding.cbClassSchedule.isChecked
        if (cbClassSchedule) {
            types.add(1)
        }
        val status = mutableListOf<Int>()
        val cbUndone = binding.cbUndone.isChecked
        if (cbUndone) {
            status.add(0)
        }
        val cbDone = binding.cbDone.isChecked
        if (cbDone) {
            status.add(1)
        }
        var startDate = dateStart.get()
        var endDate = dateEnd.get()
        //val endTime = DateTime.now().toString("yyyy-MM-dd ") + "23:59:59"
        if (!TextUtils.isEmpty(startDate) && TextUtils.isEmpty(endDate)){
            endDate = ScheduleDaoUtil.toDateTime(startDate).toString("yyyy-MM-dd ").plus("23:59:59")
        }

        if (TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)){
            startDate = ScheduleDaoUtil.toDateTime(endDate).toString("yyyy-MM-dd ").plus("00:00:00")
        }

        if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate) && ScheduleDaoUtil.toDateTime(
                startDate
            ) > ScheduleDaoUtil.toDateTime(endDate)
        ) {
            ToastUtils.showShort("开始日期不能大于结束日期！")
            return
        }

        val scheduleFilterTag =
            ScheduleFilterTag(
                types,
                status,
                startDate,
                endDate,
                labelList.filter { it.checked })
        onSelectListener.result(scheduleFilterTag)
        hide()
    }

    fun hide() {
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }

    private fun initLabel(binding: LayoutScheduleSearchFilter2Binding) {
        adapter = object :
            BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_schedule_search_filter_tag) {
            override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
                holder.setText(R.id.tv_tab_title, item.labelName)
                val drawable = GradientDrawable()
                drawable.cornerRadius = DisplayUtils.dip2px(context, 2f).toFloat()
                //drawable.setColor(Color.parseColor(item.colorValue))
                drawable.setColor(ColorUtil.parseColor(item.colorValue))

                holder.getView<TextView>(R.id.tv_tab_title).background = drawable
                holder.getView<CheckBox>(R.id.cb_check).isSelected = item.checked
                holder.itemView.setOnClickListener {
                    item.checked = !item.checked
                    notifyDataSetChanged()
                }
            }

        }
        adapter.setList(labelList)
        binding.rvLabelList.layoutManager = LinearLayoutManager(context)
        binding.rvLabelList.adapter = adapter
    }

    private fun initDate(binding: LayoutScheduleSearchFilter2Binding) {
        if (TextUtils.isEmpty(dateStart.get())) {
            binding.dateSelect.tvDateStart.text = "请选择"
            binding.dateSelect.tvTimeStart.text = "开始日期"
        } else {
            binding.dateSelect.tvDateStart.text =
                DateUtils.formatTime(dateStart.get(), "", "", true)
            //binding.dateSelect.tvTimeStart.text = DateUtils.formatTime(dateStart.get(), "", "HH:mm")
            binding.dateSelect.tvTimeStart.visibility = View.GONE
        }

        if (TextUtils.isEmpty(dateEnd.get())) {
            binding.dateSelect.tvDateEnd.text = "请选择"
            binding.dateSelect.tvTimeEnd.text = "结束日期"
        } else {
            binding.dateSelect.tvDateEnd.text = DateUtils.formatTime(dateEnd.get(), "", "", true)
            //binding.dateSelect.tvTimeEnd.text = DateUtils.formatTime(dateEnd.get(), "", "HH:mm")
            binding.dateSelect.tvTimeEnd.visibility = View.GONE
        }
        binding.dateSelect.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
        binding.dateSelect.clStartTime.setOnClickListener {
            if (binding.dateSelect.llVLine.visibility == View.GONE) {
                binding.dateSelect.llVLine.visibility = View.VISIBLE
                binding.dateSelect.dateTimePicker.visibility = View.VISIBLE
            }
            binding.dateSelect.vDateTopMarkLeft.visibility = View.VISIBLE
            binding.dateSelect.vDateTopMarkRight.visibility = View.INVISIBLE
            val startTime = DateTime.now().toString("yyyy-MM-dd ") + "00:00:00"
            val startTimeDateTime = DateUtils.formatTime(dateStart.get()?:startTime, "")
            binding.dateSelect.dateTimePicker.setDefaultMillisecond(startTimeDateTime)
        }

        binding.dateSelect.clEndTime.setOnClickListener {
            if (binding.dateSelect.llVLine.visibility == View.GONE) {
                binding.dateSelect.llVLine.visibility = View.VISIBLE
                binding.dateSelect.dateTimePicker.visibility = View.VISIBLE
            }
            binding.dateSelect.vDateTopMarkLeft.visibility = View.INVISIBLE
            binding.dateSelect.vDateTopMarkRight.visibility = View.VISIBLE
            val endTime = DateTime.now().toString("yyyy-MM-dd ") + "23:59:59"
            val endTimeDateTime = DateUtils.formatTime(dateEnd.get()?:endTime, "")
            binding.dateSelect.dateTimePicker.setDefaultMillisecond(endTimeDateTime)
        }

        binding.dateSelect.dateTimePicker.setOnDateTimeChangedListener { aLong ->
            val date = DateUtils.getDate(aLong)
            val time = DateUtils.formatTime(date, "", "", true)
            loge("showEditScheduleDialog: $aLong,date=$date, time=$time")
            if (binding.dateSelect.vDateTopMarkLeft.visibility == View.VISIBLE) {
                //左边选中设置左边的时间数据
                binding.dateSelect.tvDateStart.text = time
                //binding.dateSelect.tvTimeStart.text = DateUtils.formatTime(date, "", "HH:mm")
                binding.dateSelect.tvTimeStart.visibility = View.GONE
                dateStart.set(date)
            } else if (binding.dateSelect.vDateTopMarkRight.visibility == View.VISIBLE) {
                //右边被选中设置右边的时间数据
                binding.dateSelect.tvDateEnd.text = time
                //binding.dateSelect.tvTimeEnd.text = DateUtils.formatTime(date, "", "HH:mm")
                binding.dateSelect.tvTimeEnd.visibility = View.GONE
                dateEnd.set(date)
            } else {
                //第一次设置两边的数据
//                if (dateStart.get() == null) {
//                    binding.dateSelect.tvDateStart.text = time
//                    binding.dateSelect.tvTimeStart.text = DateUtils.formatTime(date, "", "HH:mm")
//                    dateStart.set(date)
//                }
//                if (dateEnd.get() == null) {
//                    binding.dateSelect.tvDateEnd.text = time
//                    binding.dateSelect.tvTimeEnd.text = DateUtils.formatTime(date, "", "HH:mm")
//                    dateEnd.set(date)
//                }
            }
            null
        }
    }

    interface OnSelectListener {
        fun result(tag: ScheduleFilterTag)
    }
}