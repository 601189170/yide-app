package com.yyide.chatim.fragment.schedule

import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.schedule.CalendarComposeLayout
import com.yide.calendar.schedule.ScheduleLayout
import com.yide.scheduleview.DateTimeInterpreter
import com.yide.scheduleview.MonthLoader
import com.yide.scheduleview.ScheduleEvent
import com.yide.scheduleview.ScheduleEventView
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentScheduleDayBinding
import com.yyide.chatim.model.schedule.Label
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程/日
 */
class ScheduleDayFragment : Fragment(), OnCalendarClickListener,
    ScheduleEventView.EventClickListener, MonthLoader.MonthChangeListener,
    ScheduleEventView.EmptyViewLongPressListener, ScheduleEventView.EventLongPressListener {
    private lateinit var scheduleEventView: ScheduleEventView
    lateinit var fragmentScheduleDayBinding: FragmentScheduleDayBinding
    private var labelList = mutableListOf<Label>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScheduleDayBinding = FragmentScheduleDayBinding.inflate(layoutInflater)
        return fragmentScheduleDayBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendarComposeLayout: CalendarComposeLayout =
            view.findViewById(R.id.calendarComposeLayout)
        calendarComposeLayout.setOnCalendarClickListener(this)
        scheduleEventView = calendarComposeLayout.scheduleEventView
        fragmentScheduleDayBinding.fab.setOnClickListener {
            DialogUtil.showAddScheduleDialog(context, labelList)
            //DialogUtil.showAddLabelDialog(context, labelList)
        }
        initScheduleView()
    }

    /**
     * 初始化日程view
     */
    private fun initScheduleView() {
        scheduleEventView.setNumberOfVisibleDays(1)
        // Lets change some dimensions to best fit the view.
        scheduleEventView.setColumnGap(
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
                .toInt()
        )
        scheduleEventView.setTextSize(
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
                .toInt()
        )
        scheduleEventView.setEventTextSize(
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
                .toInt()
        )
        // Show a toast message about the touched event.
        scheduleEventView.setOnEventClickListener(this)

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        scheduleEventView.setMonthChangeListener(this)

        // Set long press listener for events.
        scheduleEventView.setEventLongPressListener(this)

        // Set long press listener for empty view
        scheduleEventView.setEmptyViewLongPressListener(this)

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false)
    }

    override fun onClickDate(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
    }

    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
    }

    fun initData() {
        labelList.add(Label("工作", "#19ADF8", false))
        labelList.add(Label("阅读", "#56D72C", false))
        labelList.add(Label("睡觉", "#FD8208", false))
        labelList.add(Label("吃饭", "#56D72C", false))
        labelList.add(Label("嗨皮", "#FD8208", false))
    }

    override fun onEventClick(event: ScheduleEvent?, eventRect: RectF?) {
        ToastUtils.showShort("Clicked: " + event!!.name)
    }

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out ScheduleEvent> {
        ToastUtils.showShort("onMonthChange: newYear=$newYear,newMonth=$newMonth")
        val events: MutableList<ScheduleEvent> = getEvents(newMonth, newYear)
        return events
    }

    private fun getEvents(newMonth: Int, newYear: Int): MutableList<ScheduleEvent> {
        // Populate the week view with some events.
        val events: MutableList<ScheduleEvent> = ArrayList<ScheduleEvent>()

        var startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 3
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        var endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 1)
        endTime[Calendar.MONTH] = newMonth - 1
        var event = ScheduleEvent(1, getEventTitle(startTime), startTime, endTime)
        event.setColor("#59dbe0")
        event.setName("去公园跑步")
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 3
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 1)
        endTime[Calendar.MONTH] = newMonth - 1
        event = ScheduleEvent(1, getEventTitle(startTime), startTime, endTime)
        event.setColor("#8F81FE")
        event.setName("超市选购")
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 3
        startTime[Calendar.MINUTE] = 30
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime[Calendar.HOUR_OF_DAY] = 4
        endTime[Calendar.MINUTE] = 30
        endTime[Calendar.MONTH] = newMonth - 1
        event = ScheduleEvent(10, getEventTitle(startTime), startTime, endTime)
        event.setColor("#f57f68")
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 4
        startTime[Calendar.MINUTE] = 20
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime[Calendar.HOUR_OF_DAY] = 5
        endTime[Calendar.MINUTE] = 0
        event = ScheduleEvent(10, getEventTitle(startTime), startTime, endTime)
        event.setColor("#87d288")
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 5
        startTime[Calendar.MINUTE] = 30
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime[Calendar.MONTH] = newMonth - 1
        event = ScheduleEvent(2, getEventTitle(startTime), startTime, endTime)
        event.setColor("#f57f68")
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 5
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        startTime.add(Calendar.DATE, 1)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        endTime[Calendar.MONTH] = newMonth - 1
        event = ScheduleEvent(3, getEventTitle(startTime), startTime, endTime)
        event.setColor("#87d288")
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = 15
        startTime[Calendar.HOUR_OF_DAY] = 3
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        event = ScheduleEvent(4, getEventTitle(startTime), startTime, endTime)
        event.setColor("#f8b552")
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = 1
        startTime[Calendar.HOUR_OF_DAY] = 3
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        event = ScheduleEvent(5, getEventTitle(startTime), startTime, endTime)
        event.setColor("#59dbe0")
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = startTime.getActualMaximum(Calendar.DAY_OF_MONTH)
        startTime[Calendar.HOUR_OF_DAY] = 15
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        event = ScheduleEvent(5, getEventTitle(startTime), startTime, endTime)
        event.setColor("#f57f68")
        events.add(event)

        //AllDay event
        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 0
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 23)
        event = ScheduleEvent(7, getEventTitle(startTime), null, startTime, endTime, true)
        event.setColor("#f8b552")
        event.setName("全天日程1")
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = 8
        startTime[Calendar.HOUR_OF_DAY] = 2
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime[Calendar.DAY_OF_MONTH] = 10
        endTime[Calendar.HOUR_OF_DAY] = 23
        event = ScheduleEvent(8, getEventTitle(startTime), null, startTime, endTime, true)
        event.setColor("#87d288")
        events.add(event)

        // All day event until 00:00 next day
        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = 10
        startTime[Calendar.HOUR_OF_DAY] = 0
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.SECOND] = 0
        startTime[Calendar.MILLISECOND] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime[Calendar.DAY_OF_MONTH] = 11
        event = ScheduleEvent(8, getEventTitle(startTime), null, startTime, endTime, true)
        event.setColor("#59dbe0")
        events.add(event)
        return events
    }

    override fun onEmptyViewLongPress(time: Calendar?) {
        ToastUtils.showShort("onEmptyViewLongPress: " + time!!.toString())
    }

    override fun onEventLongPress(event: ScheduleEvent?, eventRect: RectF?) {
        ToastUtils.showShort("onEventLongPress: " + event!!.name)
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private fun setupDateTimeInterpreter(shortDate: Boolean) {
        scheduleEventView.setDateTimeInterpreter(object : DateTimeInterpreter {
            override fun interpretDate(date: Calendar): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                var weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat(" M/d", Locale.getDefault())

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate) weekday = weekday[0].toString()
                return weekday.toUpperCase() + format.format(date.time)
            }

            override fun interpretTime(hour: Int): String {
                Log.e("TAG", "interpretTime: $hour")
                //return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
                //00:00
                return if (hour < 10) "0$hour:00" else "$hour:00"
            }
        })
    }

    private fun getEventTitle(time: Calendar): String {
        return String.format(
            "Event of %02d:%02d %s/%d",
            time[Calendar.HOUR_OF_DAY],
            time[Calendar.MINUTE], time[Calendar.MONTH] + 1, time[Calendar.DAY_OF_MONTH]
        )
    }
}