package com.yide.calendar.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.yide.calendar.OnCalendarClickListener;
import com.yide.calendar.R;
import com.yide.calendar.month.MonthView;
import com.yide.calendar.month.OnMonthClickListener;
import com.yide.calendar.week.OnWeekClickListener;
import com.yide.calendar.week.WeekView;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu tao
 * @date 2021/12/24 15:22
 * @description 日历组件
 * 1，需要指定日历开始时间，日历结束时间
 * 2，可滑动选择周月，可直接通过日期选中指定月周
 */
public class CustomCalendarLayout extends FrameLayout implements OnMonthClickListener, OnWeekClickListener {
    private static final String TAG = CustomCalendarLayout.class.getSimpleName();
    //存月日历的view pager
    private ViewPager monthCalendarViewPager;
    //存周日历的view pager
    private ViewPager weekCalendarViewPager;
    private ScheduleState mState;
    private ImageView ivShowCalendar;
    private TypedArray mArray;
    private Context context;
    private List<View> monthViewList = new ArrayList<>();
    private List<View> weekViewList = new ArrayList<>();
    private OnCalendarClickListener mOnCalendarClickListener;
//    private ViewPagerAdapter adapterWeek;
//    private MyPagerAdapter adapterWeek;
//    private ViewPagerAdapter adapterMonth;
//    private MyPagerAdapter adapterMonth;
    public CustomCalendarLayout(@NonNull Context context) {
        super(context);
    }

    public CustomCalendarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mArray = context.obtainStyledAttributes(attrs, R.styleable.MonthCalendarView);
    }

    public CustomCalendarLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        monthCalendarViewPager = findViewById(R.id.viewpager_month_calendar_view);
        weekCalendarViewPager = findViewById(R.id.viewpager_week_calendar_view);
        final RelativeLayout rlShowCalendar = findViewById(R.id.rlShowCalendar);
        rlShowCalendar.setOnClickListener(v -> {
                    changeState();
                }
        );
        ivShowCalendar = findViewById(R.id.iv_show_calendar);
    }

    private void changeState() {
        if (mState == ScheduleState.OPEN) {
            mState = ScheduleState.CLOSE;
            monthCalendarViewPager.setVisibility(GONE);
            weekCalendarViewPager.setVisibility(VISIBLE);
            ivShowCalendar.setImageResource(R.drawable.calendar_down_icon);
            //显示周，调整周的位置
            MonthView monthView = (MonthView) monthViewList.get(monthCalendarViewPager.getCurrentItem());
            if (monthView != null){
                final int selectYear = monthView.getSelectYear();
                final int selectMonth = monthView.getSelectMonth();
                final int selectDay = monthView.getSelectDay();
                final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                final DateTime dateTime = DateTime.parse(selectYear + "-" + (selectMonth + 1) + "-" + selectDay, dateTimeFormatter);
                setCurrentWeekCalendar(dateTime);
            }
        } else {
            mState = ScheduleState.OPEN;
            monthCalendarViewPager.setVisibility(VISIBLE);
            weekCalendarViewPager.setVisibility(INVISIBLE);
            ivShowCalendar.setImageResource(R.drawable.calendar_up_icon);
            //显示月，调整月的位置
            final WeekView weekView = (WeekView)weekViewList.get(weekCalendarViewPager.getCurrentItem());
            if (weekView != null){
                final int selectYear = weekView.getSelectYear();
                final int selectMonth = weekView.getSelectMonth();
                final int selectDay = weekView.getSelectDay();
                final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                final DateTime dateTime = DateTime.parse(selectYear + "-" + (selectMonth + 1) + "-" + selectDay, dateTimeFormatter);
                setCurrentMonthCalendar(dateTime);
            }
        }
    }

    /**
     * 设置日历的开始和结束日期
     *
     * @param startDate 日历的开始
     * @param endDate   日历的结束
     */
    public void setCalendarInterval(DateTime startDate, DateTime endDate,DateTime currentDate) {
        for (int i = 0; i < monthViewList.size(); i++) {
            monthCalendarViewPager.removeView(monthViewList.get(i));
        }
        for (int i = 0; i < weekViewList.size(); i++) {
            weekCalendarViewPager.removeView(weekViewList.get(i));
        }
        monthViewList.clear();
        weekViewList.clear();
        //设置月日历
        final int months = Months.monthsBetween(startDate, endDate).getMonths() + 1;
        for (int i = 0; i < months; i++) {
            final DateTime dateTime = startDate.plusMonths(i);
            final MonthView monthView = new MonthView(context, mArray, dateTime.getYear(), dateTime.getMonthOfYear() - 1);
            monthView.setOnDateClickListener(this);
            monthViewList.add(monthView);
        }
        MyPagerAdapter adapterMonth = new MyPagerAdapter(context, monthViewList);
        monthCalendarViewPager.setAdapter(adapterMonth);
        //monthCalendarViewPager.registerOnPageChangeCallback(onMonthPageChangeCallback);
        monthCalendarViewPager.addOnPageChangeListener(onMonthPageChangeListener);
        setCurrentMonthCalendar(currentDate);
        //设置周日历
        final int weeks = Weeks.weeksBetween(startDate, endDate).getWeeks() + 1;
        for (int i = 0; i < weeks; i++) {
            DateTime dateTime = startDate.plusWeeks(i);
            dateTime = dateTime.plusDays(-dateTime.getDayOfWeek() % 7);
            WeekView weekView = new WeekView(context, mArray, dateTime);
            weekView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            weekView.setOnWeekClickListener(this);
            weekViewList.add(weekView);
        }
        MyPagerAdapter adapterWeek = new MyPagerAdapter(context, weekViewList);
        weekCalendarViewPager.setAdapter(adapterWeek);
        //weekCalendarViewPager.registerOnPageChangeCallback(onWeekPageChangeCallback);
        weekCalendarViewPager.addOnPageChangeListener(onWeekPageChangeListener);
        setCurrentWeekCalendar(currentDate);
    }

    private final ViewPager2.OnPageChangeCallback onMonthPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            final MonthView monthView = (MonthView) monthViewList.get(position);
            if (monthView != null){
                final int selectYear = monthView.getSelectYear();
                final int selectMonth = monthView.getSelectMonth();
                final int selectDay = monthView.getSelectDay();
                Log.e(TAG, "month onPageSelected: position="+position+", "+selectYear+"/"+selectMonth+"/"+selectDay);
                mOnCalendarClickListener.onPageChange(selectYear, selectMonth, selectDay);
//                monthView.clickThisMonth(selectYear, selectMonth, selectDay);
//                final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
//                final DateTime dateTime = DateTime.parse(selectYear + "-" + (selectMonth + 1) + "-" + selectDay, dateTimeFormatter);
//                setCurrentWeekCalendar(dateTime);
            }
        }
    };

    private final ViewPager.OnPageChangeListener onMonthPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            final MonthView monthView = (MonthView) monthViewList.get(position);
            if (monthView != null) {
                final int selectYear = monthView.getSelectYear();
                final int selectMonth = monthView.getSelectMonth();
                final int selectDay = monthView.getSelectDay();
                Log.e(TAG, "month onPageSelected: position=" + position + ", " + selectYear + "/" + selectMonth + "/" + selectDay);
                mOnCalendarClickListener.onPageChange(selectYear, selectMonth, selectDay);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private final ViewPager2.OnPageChangeCallback onWeekPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            final WeekView weekView = (WeekView) weekViewList.get(position);
            if (weekView != null){
                final int selectYear = weekView.getSelectYear();
                final int selectMonth = weekView.getSelectMonth();
                final int selectDay = weekView.getSelectDay();
                Log.e(TAG, "week onPageSelected: position="+position+", "+selectYear+"/"+selectMonth+"/"+selectDay);
                mOnCalendarClickListener.onPageChange(selectYear, selectMonth, selectDay);
//                weekView.clickThisWeek(selectYear,selectMonth,selectDay);
//                setCurrentMonthCalendar(weekView.getStartDate());
            }
        }
    };

    private final ViewPager.OnPageChangeListener onWeekPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            final WeekView weekView = (WeekView) weekViewList.get(position);
            if (weekView != null) {
                final int selectYear = weekView.getSelectYear();
                final int selectMonth = weekView.getSelectMonth();
                final int selectDay = weekView.getSelectDay();
                Log.e(TAG, "week onPageSelected: position=" + position + ", " + selectYear + "/" + selectMonth + "/" + selectDay);
                mOnCalendarClickListener.onPageChange(selectYear, selectMonth, selectDay);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 指定当前日历的日期
     *
     * @param dateTime 当前日历的日期
     */
    public void setCurrentMonthCalendar(DateTime dateTime) {
        for (int i = 0; i < monthViewList.size(); i++) {
            final MonthView view = (MonthView) monthViewList.get(i);
            final int selectYear = view.getSelectYear();
            final int selectMonth = view.getSelectMonth();
            if (selectMonth == dateTime.getMonthOfYear() - 1 && selectYear == dateTime.getYear()) {
                monthCalendarViewPager.setCurrentItem(i, false);
                view.setSelectYearMonth(dateTime.getYear(),dateTime.getMonthOfYear()-1,dateTime.getDayOfMonth());
                view.invalidate();
                break;
            }
        }
    }

    public void setCurrentWeekCalendar(DateTime dateTime) {
        for (int i = 0; i < weekViewList.size(); i++) {
            WeekView weekView = (WeekView) weekViewList.get(i);
            final DateTime startDate = weekView.getStartDate();
            final DateTime endDate = weekView.getEndDate();
            if (dateTime.compareTo(startDate) >= 0 && dateTime.compareTo(endDate) <= 0) {
                weekCalendarViewPager.setCurrentItem(i, false);
                weekView.setSelectYearMonth(dateTime.getYear(),dateTime.getMonthOfYear()-1,dateTime.getDayOfMonth());
                weekView.invalidate();
            }
        }
    }

    public void setCurrentCalendar(DateTime dateTime) {
        setCurrentWeekCalendar(dateTime);
        setCurrentMonthCalendar(dateTime);
    }

    @Override
    public void onClickThisMonth(int year, int month, int day) {
        mOnCalendarClickListener.onClickDate(year, month, day);
    }

    @Override
    public void onClickLastMonth(int year, int month, int day) {

    }

    @Override
    public void onClickNextMonth(int year, int month, int day) {

    }

    /**
     * 设置点击日期监听
     *
     * @param onCalendarClickListener
     */
    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        mOnCalendarClickListener = onCalendarClickListener;
    }

    @Override
    public void onClickDate(int year, int month, int day) {
        mOnCalendarClickListener.onClickDate(year, month, day);
    }

    public WeekView getCurrentWeekView() {
        final int currentItem = weekCalendarViewPager.getCurrentItem();
        return (WeekView) weekViewList.get(currentItem);
    }

    public MonthView getCurrentMonthView() {
        final int currentItem = monthCalendarViewPager.getCurrentItem();
        return (MonthView) monthViewList.get(currentItem);
    }
}
