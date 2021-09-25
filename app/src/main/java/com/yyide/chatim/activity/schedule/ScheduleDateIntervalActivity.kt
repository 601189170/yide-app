package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleDateIntervalBinding
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.loge
import java.util.concurrent.atomic.AtomicReference

class ScheduleDateIntervalActivity : BaseActivity() {
    lateinit var binding: ActivityScheduleDateIntervalBinding
    private val dateStart = AtomicReference("")
    private val dateEnd = AtomicReference("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleDateIntervalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initDate()
    }

    private fun initDate() {
        val allDay = intent.getBooleanExtra("allDay", false)
        if (allDay){
            binding.checkBox.isChecked = allDay
            binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
            binding.tvTimeStart.visibility = View.GONE
            binding.tvTimeEnd.visibility = View.GONE
        }
        val startTime = intent.getStringExtra("startTime")
        val endTime = intent.getStringExtra("endTime")
        val ids: IntArray = binding.groupDateStart.referencedIds
        for (id in ids) {
            findViewById<View>(id).setOnClickListener { v: View? ->
                if (binding.llVLine.visibility == View.GONE) {
                    binding.llVLine.visibility = View.VISIBLE
                    binding.dateTimePicker.visibility = View.VISIBLE
                }
                binding.vDateTopMarkLeft.visibility = View.VISIBLE
                binding.vDateTopMarkRight.visibility = View.INVISIBLE
                binding.dateTimePicker.setDefaultMillisecond(DateUtils.formatTime(dateStart.get(),""))
            }
        }

        val ids2: IntArray = binding.groupDateEnd.referencedIds
        for (id in ids2) {
            findViewById<View>(id).setOnClickListener { v: View? ->
                if (binding.llVLine.visibility == View.GONE) {
                    binding.llVLine.visibility = View.VISIBLE
                    binding.dateTimePicker.visibility = View.VISIBLE
                }
                binding.vDateTopMarkLeft.visibility = View.INVISIBLE
                binding.vDateTopMarkRight.visibility = View.VISIBLE
                binding.dateTimePicker.setDefaultMillisecond(DateUtils.formatTime(dateEnd.get(),""))
            }
        }
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
                binding.tvTimeStart.visibility = View.GONE
                binding.tvTimeEnd.visibility = View.GONE
            } else {
                binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation)
                binding.tvTimeStart.visibility = View.VISIBLE
                binding.tvTimeEnd.visibility = View.VISIBLE
            }
        }
        binding.dateTimePicker.setOnDateTimeChangedListener { aLong ->
            val date = DateUtils.getDate(aLong)
            val time = DateUtils.formatTime(date, "", "", true)
            loge("showEditScheduleDialog: $aLong,date=$date, time=$time")
            if (binding.vDateTopMarkLeft.visibility == View.VISIBLE) {
                //左边选中设置左边的时间数据
                binding.tvDateStart.text = time
                if (!binding.checkBox.isChecked) {
                    binding.tvTimeStart.text = DateUtils.formatTime(date, "", "HH:mm")
                }
                dateStart.set(date)
            } else if (binding.vDateTopMarkRight.visibility == View.VISIBLE) {
                //右边被选中设置右边的时间数据
                binding.tvDateEnd.text = time
                if (!binding.checkBox.isChecked) {
                    binding.tvTimeEnd.text = DateUtils.formatTime(date, "", "HH:mm")
                }
                dateEnd.set(date)
            } else {
                //第一次设置两边的数据
                if (TextUtils.isEmpty(startTime)) {
                    binding.tvDateStart.text = time
                    if (!binding.checkBox.isChecked) {
                        binding.tvTimeStart.text = DateUtils.formatTime(date, "", "HH:mm")
                    }
                    dateStart.set(date)
                } else {
                    binding.tvDateStart.text = DateUtils.formatTime(startTime, "", "", true)
                    if (!binding.checkBox.isChecked) {
                        binding.tvTimeStart.text = DateUtils.formatTime(startTime, "", "HH:mm")
                    }
                    dateStart.set(startTime)
                }
                if (TextUtils.isEmpty(endTime)) {
                    binding.tvDateEnd.text = time
                    if (!binding.checkBox.isChecked) {
                        binding.tvTimeEnd.text = DateUtils.formatTime(date, "", "HH:mm")
                    }
                    dateEnd.set(date)
                } else {
                    binding.tvDateEnd.text = DateUtils.formatTime(endTime, "", "", true)
                    if (!binding.checkBox.isChecked) {
                        binding.tvTimeEnd.text = DateUtils.formatTime(endTime, "", "HH:mm")
                    }
                    dateEnd.set(endTime)
                }
            }
            null
        }
    }

    private fun initView() {
        binding.top.title.text = "自定义时间"
        binding.top.backLayout.setOnClickListener {
            finish()
        }
        binding.top.tvRight.visibility = View.VISIBLE
        binding.top.tvRight.text = "完成"
        binding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        binding.top.tvRight.setOnClickListener {
            val allDay = binding.checkBox.isChecked
            val startTime = dateStart.get()
            val endTime = dateEnd.get()
            val intent = intent
            intent.putExtra("allDay", allDay)
            intent.putExtra("startTime", startTime)
            intent.putExtra("endTime", endTime)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_date_interval
}