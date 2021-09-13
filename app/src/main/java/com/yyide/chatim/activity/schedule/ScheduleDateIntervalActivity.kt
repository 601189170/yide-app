package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleAddressBinding
import com.yyide.chatim.databinding.ActivityScheduleDateIntervalBinding
import com.yyide.chatim.model.schedule.Address
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import java.util.concurrent.atomic.AtomicReference

class ScheduleDateIntervalActivity : BaseActivity() {
    lateinit var binding: ActivityScheduleDateIntervalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleDateIntervalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initDate()
    }

    private fun initDate() {
        val dateStart = AtomicReference("")
        val dateEnd = AtomicReference("")
        val ids: IntArray = binding.groupDateStart.getReferencedIds()
        for (id in ids) {
            findViewById<View>(id).setOnClickListener(View.OnClickListener { v: View? ->
                if (binding.llVLine.getVisibility() == View.GONE) {
                    binding.llVLine.setVisibility(View.VISIBLE)
                    binding.dateTimePicker.setVisibility(View.VISIBLE)
                }
                binding.vDateTopMarkLeft.setVisibility(View.VISIBLE)
                binding.vDateTopMarkRight.setVisibility(View.INVISIBLE)
            })
        }

        val ids2: IntArray = binding.groupDateEnd.getReferencedIds()
        for (id in ids2) {
            findViewById<View>(id).setOnClickListener(View.OnClickListener { v: View? ->
                if (binding.llVLine.getVisibility() == View.GONE) {
                    binding.llVLine.setVisibility(View.VISIBLE)
                    binding.dateTimePicker.setVisibility(View.VISIBLE)
                }
                binding.vDateTopMarkLeft.setVisibility(View.INVISIBLE)
                binding.vDateTopMarkRight.setVisibility(View.VISIBLE)
            })
        }
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
                binding.tvTimeStart.setVisibility(View.GONE)
                binding.tvTimeEnd.setVisibility(View.GONE)
            } else {
                binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation)
                binding.tvTimeStart.setVisibility(View.VISIBLE)
                binding.tvTimeEnd.setVisibility(View.VISIBLE)
            }
        }
        binding.dateTimePicker.setOnDateTimeChangedListener { aLong ->
            val date = DateUtils.getDate(aLong)
            val time = DateUtils.formatTime(date, "", "", true)
            loge("showEditScheduleDialog: $aLong,date=$date, time=$time")
            if (binding.vDateTopMarkLeft.visibility == View.VISIBLE) {
                //左边选中设置左边的时间数据
                binding.tvDateStart.setText(time)
                if (!binding.checkBox.isChecked()) {
                    binding.tvTimeStart.setText(DateUtils.formatTime(date, "", "HH:mm"))
                }
                dateStart.set(date)
            } else if (binding.vDateTopMarkRight.getVisibility() == View.VISIBLE) {
                //右边被选中设置右边的时间数据
                binding.tvDateEnd.setText(time)
                if (!binding.checkBox.isChecked()) {
                    binding.tvTimeEnd.setText(DateUtils.formatTime(date, "", "HH:mm"))
                }
                dateEnd.set(date)
            } else {
                //第一次设置两边的数据
                binding.tvDateStart.setText(time)
                if (!binding.checkBox.isChecked()) {
                    binding.tvTimeStart.setText(DateUtils.formatTime(date, "", "HH:mm"))
                }
                binding.tvDateEnd.setText(time)
                if (!binding.checkBox.isChecked()) {
                    binding.tvTimeEnd.setText(DateUtils.formatTime(date, "", "HH:mm"))
                }
                dateStart.set(date)
                dateEnd.set(date)
            }
            null
        }
    }

    private fun initView() {
        binding.top.title.text = "选择日期"
        binding.top.backLayout.setOnClickListener {
            finish()
        }
        binding.top.tvRight.visibility = View.VISIBLE
        binding.top.tvRight.text = "完成"
        binding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        binding.top.tvRight.setOnClickListener {
            finish()
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_date_interval
}