package com.yide.calendar.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.yide.calendar.OnCalendarClickListener;
import com.yide.calendar.R;
import com.yide.calendar.month.MonthView;
import com.yide.calendar.month.OnMonthClickListener;

import org.joda.time.DateTime;
import org.joda.time.Months;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu tao
 * @date 2021/12/24 15:22
 * @description 日历组件
 * 1，需要指定日历开始时间，日历结束时间
 * 2，可滑动选择周月，可直接通过日期选中指定月周
 */
public class CustomCalendarLayout extends FrameLayout implements OnMonthClickListener {
    //存月日历的view pager
    private ViewPager2 monthCalendarViewPager;
    //存周日历的view pager
    private ViewPager2 weekCalendarViewPager;
    private ScheduleState mState;
    private ImageView ivShowCalendar;
    private TypedArray mArray;
    private Context context;
    private List<View> monthViewList = new ArrayList<>();
    private OnCalendarClickListener mOnCalendarClickListener;
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
        } else {
            mState = ScheduleState.OPEN;
            monthCalendarViewPager.setVisibility(VISIBLE);
            weekCalendarViewPager.setVisibility(INVISIBLE);
            ivShowCalendar.setImageResource(R.drawable.calendar_up_icon);
        }
    }

    /**
     * 设置日历的开始和结束日期
     *
     * @param startDate 日历的开始
     * @param endDate   日历的结束
     */
    public void setCalendarInterval(DateTime startDate, DateTime endDate) {
        final int months = Months.monthsBetween(startDate, endDate).getMonths()+1;
        for (int i = 0; i < months; i++) {
            final DateTime dateTime = startDate.plusMonths(i);
            final MonthView monthView = new MonthView(context, mArray, dateTime.getYear(), dateTime.getMonthOfYear() - 1);
            monthView.setOnDateClickListener(this);
            monthViewList.add(monthView);
        }
        final ViewPagerAdapter adapter = new ViewPagerAdapter(context, monthViewList);
        monthCalendarViewPager.setAdapter(adapter);
        monthCalendarViewPager.registerOnPageChangeCallback(onPageChangeCallback);
        setCurrentCalendar(new DateTime());
    }
    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            final MonthView monthView = (MonthView) monthViewList.get(position);
            final int selectYear = monthView.getSelectYear();
            final int selectMonth = monthView.getSelectMonth();
            final int selectDay = monthView.getSelectDay();
            mOnCalendarClickListener.onPageChange(selectYear,selectMonth,selectDay);
        }
    };

    /**
     * 指定当前日历的日期
     *
     * @param dateTime 当前日历的日期
     */
    public void setCurrentCalendar(DateTime dateTime) {
        for (int i = 0; i < monthViewList.size(); i++) {
            final MonthView view = (MonthView) monthViewList.get(i);
            final int selectYear = view.getSelectYear();
            final int selectMonth = view.getSelectMonth();
            if (selectMonth == dateTime.getMonthOfYear()-1 && selectYear == dateTime.getYear()){
                monthCalendarViewPager.setCurrentItem(i,false);
                break;
            }
        }
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
}
