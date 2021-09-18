package com.yyide.chatim.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.LayoutScheduleSearchFilterBinding
import com.yyide.chatim.model.schedule.Label
import com.yyide.chatim.model.schedule.ScheduleFilterTag
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import java.util.concurrent.atomic.AtomicReference

/**
 * Created by Administrator on 2019/5/15.
 */
class ScheduleSearchFilterPop(val context: Activity?) : PopupWindow() {
    lateinit var popupWindow: PopupWindow
    lateinit var mWindow: Window
    val dateStart = AtomicReference("")
    val dateEnd = AtomicReference("")
    private var labelList = mutableListOf<Label>()
    private lateinit var onSelectListener: OnSelectListener
    lateinit var adapter: BaseQuickAdapter<Label, BaseViewHolder>

    init {
        init()
    }

    fun setOnSelectListener(onSelectListener: OnSelectListener) {
        this.onSelectListener = onSelectListener
    }

    private fun init() {
        val mView = LayoutScheduleSearchFilterBinding.inflate(context!!.layoutInflater)
        popupWindow = PopupWindow(
            mView.root,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        popupWindow.animationStyle = R.style.popwin_anim_right_style
        mView.clBg.setOnClickListener { v ->
            popupWindow.dismiss()
        }
        initLabel(mView)
        initDate(mView)
        mView.tvConfirm.setOnClickListener {
            commit(mView)
            hide()
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
            mView.root, Gravity.RIGHT, 0, 0
        )
    }

    private fun reset(binding: LayoutScheduleSearchFilterBinding) {
        binding.cbTransactionSchedule.isChecked = false
        binding.cbSchoolCalendar.isChecked = false
        binding.cbMeeting.isChecked = false
        binding.cbClassSchedule.isChecked = false
        binding.cbUndone.isChecked = false
        binding.cbDone.isChecked = false

        labelList.forEach {
            it.checked = false
        }
        adapter.notifyDataSetChanged()
    }

    private fun commit(binding: LayoutScheduleSearchFilterBinding) {
        //类型1日程2校历3会议4课表
        val types = mutableListOf<Int>()
        val cbTransactionSchedule = binding.cbTransactionSchedule.isChecked
        if (cbTransactionSchedule) {
            types.add(1)
        }
        val cbSchoolCalendar = binding.cbSchoolCalendar.isChecked
        if (cbSchoolCalendar) {
            types.add(2)
        }
        val cbMeeting = binding.cbMeeting.isChecked
        if (cbMeeting) {
            types.add(3)
        }
        val cbClassSchedule = binding.cbClassSchedule.isChecked
        if (cbClassSchedule) {
            types.add(4)
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
        val startDate = dateStart.get()
        val endDate = dateEnd.get()

        val scheduleFilterTag =
            ScheduleFilterTag(types, status, startDate, endDate, labelList.filter { it.checked })
        onSelectListener.result(scheduleFilterTag)
    }

    fun hide() {
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }

    private fun initLabel(binding: LayoutScheduleSearchFilterBinding) {
        labelList.add(Label("工作", "#19ADF8", false))
        labelList.add(Label("阅读阅读阅读", "#56D72C", false))
        labelList.add(Label("睡阅读阅读阅读阅读阅读阅读阅读阅读阅读阅读", "#FD8208", false))
        labelList.add(Label("吃饭阅读", "#56D72C", false))
        labelList.add(Label("嗨皮阅读", "#FD8208", false))
        adapter = object :
            BaseQuickAdapter<Label, BaseViewHolder>(R.layout.item_schedule_search_filter_tag) {
            override fun convert(holder: BaseViewHolder, item: Label) {
                holder.setText(R.id.tv_tab_title, item.title)
                val drawable = GradientDrawable()
                drawable.cornerRadius = DisplayUtils.dip2px(context, 2f).toFloat()
                drawable.setColor(Color.parseColor(item.color))
                holder.getView<TextView>(R.id.tv_tab_title).background = drawable
                val checkBox = holder.getView<CheckBox>(R.id.cb_check)
                checkBox.isChecked = item.checked
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    item.checked = isChecked
                }

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

    private fun initDate(binding: LayoutScheduleSearchFilterBinding) {
        val ids: IntArray = binding.dateSelect.groupDateStart.referencedIds
        for (id in ids) {
            binding.dateSelect.root.findViewById<View>(id).setOnClickListener { v: View? ->
                if (binding.dateSelect.llVLine.visibility == View.GONE) {
                    binding.dateSelect.llVLine.visibility = View.VISIBLE
                    binding.dateSelect.dateTimePicker.visibility = View.VISIBLE
                }
                binding.dateSelect.vDateTopMarkLeft.visibility = View.VISIBLE
                binding.dateSelect.vDateTopMarkRight.visibility = View.INVISIBLE
            }
        }

        val ids2: IntArray = binding.dateSelect.groupDateEnd.referencedIds
        for (id in ids2) {
            binding.dateSelect.root.findViewById<View>(id).setOnClickListener { v: View? ->
                if (binding.dateSelect.llVLine.visibility == View.GONE) {
                    binding.dateSelect.llVLine.visibility = View.VISIBLE
                    binding.dateSelect.dateTimePicker.visibility = View.VISIBLE
                }
                binding.dateSelect.vDateTopMarkLeft.visibility = View.INVISIBLE
                binding.dateSelect.vDateTopMarkRight.visibility = View.VISIBLE
            }
        }

        binding.dateSelect.dateTimePicker.setOnDateTimeChangedListener { aLong ->
            val date = DateUtils.getDate(aLong)
            val time = DateUtils.formatTime(date, "", "", true)
            loge("showEditScheduleDialog: $aLong,date=$date, time=$time")
            if (binding.dateSelect.vDateTopMarkLeft.visibility == View.VISIBLE) {
                //左边选中设置左边的时间数据
                binding.dateSelect.tvDateStart.text = time
                binding.dateSelect.tvTimeStart.text = DateUtils.formatTime(date, "", "HH:mm")
                dateStart.set(date)
            } else if (binding.dateSelect.vDateTopMarkRight.visibility == View.VISIBLE) {
                //右边被选中设置右边的时间数据
                binding.dateSelect.tvDateEnd.text = time
                binding.dateSelect.tvTimeEnd.text = DateUtils.formatTime(date, "", "HH:mm")
                dateEnd.set(date)
            } else {
                //第一次设置两边的数据
                binding.dateSelect.tvDateStart.text = time
                binding.dateSelect.tvTimeStart.text = DateUtils.formatTime(date, "", "HH:mm")
                binding.dateSelect.tvDateEnd.text = time
                binding.dateSelect.tvTimeEnd.text = DateUtils.formatTime(date, "", "HH:mm")
                dateStart.set(date)
                dateEnd.set(date)
            }
            null
        }
    }

    interface OnSelectListener {
        fun result(tag: ScheduleFilterTag)
    }
}