package com.yide.calendar.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yide.calendar.CalendarUtils;
import com.yide.calendar.OnCalendarClickListener;
import com.yide.calendar.R;
import com.yide.calendar.month.MonthCalendarView;
import com.yide.calendar.month.MonthView;
import com.yide.calendar.week.WeekCalendarView;
import com.yide.calendar.week.WeekView;
import com.yide.scheduleview.ScheduleEventView;

import org.joda.time.DateTime;

import java.util.Calendar;

/**
 * @author liu tao
 * @date 2021/9/17 10:29
 * @description 描述
 */
public class CalendarComposeLayout extends FrameLayout {
    private static final String TAG = "CalendarComposeLayout";
    private int mDefaultView = 0;
    private final int DEFAULT_MONTH = 0;
    private final int DEFAULT_WEEK = 1;
    private boolean startWithSunday;
    private int mCurrentSelectYear;
    private int mCurrentSelectMonth;
    private int mCurrentSelectDay;
    private WeekCalendarView weekCalendarView;
    private MonthCalendarView monthCalendarView;
    private ScheduleEventView scheduleEventView;
    private ScheduleState mState;
    private boolean mCurrentRowsIsSix = true;
    private int mRowSize;
    private int mMinDistance;
    private int mAutoScrollDistance;
    private boolean mIsAutoChangeMonthRow;
    private OnCalendarClickListener mOnCalendarClickListener;
    private ImageView ivShowCalendar;
    private SwipeRecyclerView rvScheduleList;
    private ConstraintLayout blankPage;

    public CalendarComposeLayout(@NonNull Context context) {
        this(context, null);
    }

    public CalendarComposeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarComposeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context.obtainStyledAttributes(attrs, R.styleable.CalendarComposeLayout));
        initDate();
    }

    private void initAttrs(TypedArray array) {
        mDefaultView = array.getInt(R.styleable.CalendarComposeLayout_default_calendar_view, DEFAULT_MONTH);
        mIsAutoChangeMonthRow = array.getBoolean(R.styleable.CalendarComposeLayout_schedule_auto_change_month_row, false);
        startWithSunday = array.getBoolean(R.styleable.CalendarComposeLayout_schedule_calendar_list_start_with_sunday, true);
        array.recycle();
        mRowSize = getResources().getDimensionPixelSize(R.dimen.week_calendar_height);
        mMinDistance = getResources().getDimensionPixelSize(R.dimen.calendar_min_distance);
        mAutoScrollDistance = getResources().getDimensionPixelSize(R.dimen.auto_scroll_distance);
        Log.e(TAG, "initAttrs: mDefaultView="+mDefaultView +",startWithSunday="+startWithSunday);
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        resetCurrentSelectDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void resetCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
        Log.e(TAG, "resetCurrentSelectDate: "+mCurrentSelectYear+", "+mCurrentSelectMonth+", "+mCurrentSelectDay );
    }

    public void setSelectedData(int year, int month, int day){
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
        checkWeekCalendar();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        weekCalendarView = findViewById(R.id.weekCalendar);
        monthCalendarView = findViewById(R.id.monthCalendar);
        scheduleEventView = findViewById(R.id.scheduleEventView);
        ivShowCalendar = findViewById(R.id.iv_show_calendar);
        rvScheduleList = findViewById(R.id.rvScheduleList);
        blankPage = findViewById(R.id.blank_page);
        final RelativeLayout rlShowCalendar = findViewById(R.id.rlShowCalendar);
        bindingMonthAndWeekCalendar();
        Log.e(TAG, "onFinishInflate: " );
        rlShowCalendar.setOnClickListener(v -> {
            changeState();
        });
    }

    private void bindingMonthAndWeekCalendar() {
        monthCalendarView.setOnCalendarClickListener(mMonthCalendarClickListener);
        weekCalendarView.setOnCalendarClickListener(mWeekCalendarClickListener);
        // 初始化视图
        Calendar calendar = Calendar.getInstance();
        if (mIsAutoChangeMonthRow) {
            mCurrentRowsIsSix = CalendarUtils.getMonthRows(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),startWithSunday) == 6;
        }
        if (mDefaultView == DEFAULT_MONTH) {
            weekCalendarView.setVisibility(INVISIBLE);
            monthCalendarView.setVisibility(VISIBLE);
            mState = ScheduleState.OPEN;
        } else if (mDefaultView == DEFAULT_WEEK) {
            weekCalendarView.setVisibility(VISIBLE);
            monthCalendarView.setVisibility(GONE);
            mState = ScheduleState.CLOSE;
            int row = CalendarUtils.getWeekRow(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),startWithSunday);

        }
    }

    private OnCalendarClickListener mMonthCalendarClickListener = new OnCalendarClickListener() {
        @Override
        public void onClickDate(int year, int month, int day) {
            weekCalendarView.setOnCalendarClickListener(null);
            int weeks = CalendarUtils.getWeeksAgo(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, year, month, day,startWithSunday);
            resetCurrentSelectDate(year, month, day);
            int position = weekCalendarView.getCurrentItem() + weeks;
            if (weeks != 0) {
                weekCalendarView.setCurrentItem(position, false);
            }
            resetWeekView(position);
            weekCalendarView.setOnCalendarClickListener(mWeekCalendarClickListener);
        }

        @Override
        public void onPageChange(int year, int month, int day) {
            computeCurrentRowsIsSix(year, month);
        }
    };
    private void computeCurrentRowsIsSix(int year, int month) {
        if (mIsAutoChangeMonthRow) {
            boolean isSixRow = CalendarUtils.getMonthRows(year, month,startWithSunday) == 6;
            if (mCurrentRowsIsSix != isSixRow) {
                mCurrentRowsIsSix = isSixRow;
                if (mState == ScheduleState.OPEN && scheduleEventView!=null) {
                    if (mCurrentRowsIsSix) {
                        AutoMoveAnimation animation = new AutoMoveAnimation(scheduleEventView, mRowSize);
                        scheduleEventView.startAnimation(animation);
                    } else {
                        AutoMoveAnimation animation = new AutoMoveAnimation(scheduleEventView, -mRowSize);
                        scheduleEventView.startAnimation(animation);
                    }
                }
            }
        }
    }

    private void resetWeekView(int position) {
        WeekView weekView = weekCalendarView.getCurrentWeekView();
        if (weekView != null) {
            weekView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            weekView.invalidate();
        } else {
            WeekView newWeekView = weekCalendarView.getWeekAdapter().instanceWeekView(position);
            newWeekView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            newWeekView.invalidate();
            weekCalendarView.setCurrentItem(position);
        }
        if (mOnCalendarClickListener != null) {
            mOnCalendarClickListener.onClickDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            mOnCalendarClickListener.onPageChange(mCurrentSelectYear,mCurrentSelectMonth,mCurrentSelectDay);
        }
    }
    private OnCalendarClickListener mWeekCalendarClickListener = new OnCalendarClickListener() {
        @Override
        public void onClickDate(int year, int month, int day) {
            monthCalendarView.setOnCalendarClickListener(null);
            int months = CalendarUtils.getMonthsAgo(mCurrentSelectYear, mCurrentSelectMonth, year, month);
            resetCurrentSelectDate(year, month, day);
            if (months != 0) {
                int position = monthCalendarView.getCurrentItem() + months;
                monthCalendarView.setCurrentItem(position, false);
            }
            resetMonthView();
            monthCalendarView.setOnCalendarClickListener(mMonthCalendarClickListener);
            if (mIsAutoChangeMonthRow) {
                mCurrentRowsIsSix = CalendarUtils.getMonthRows(year, month,startWithSunday) == 6;
            }
        }

        @Override
        public void onPageChange(int year, int month, int day) {
            if (mIsAutoChangeMonthRow) {
                if (mCurrentSelectMonth != month) {
                    mCurrentRowsIsSix = CalendarUtils.getMonthRows(year, month,startWithSunday) == 6;
                }
            }
        }
    };

    private void resetMonthView() {
        MonthView monthView = monthCalendarView.getCurrentMonthView();
        if (monthView != null) {
            monthView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            monthView.invalidate();
        }
        if (mOnCalendarClickListener != null) {
            mOnCalendarClickListener.onClickDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            mOnCalendarClickListener.onPageChange(mCurrentSelectYear,mCurrentSelectMonth,mCurrentSelectDay);
        }
    }

    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        mOnCalendarClickListener = onCalendarClickListener;
    }

    private void changeState() {
        if (mState == ScheduleState.OPEN) {
            mState = ScheduleState.CLOSE;
            monthCalendarView.setVisibility(GONE);
            weekCalendarView.setVisibility(VISIBLE);
            ivShowCalendar.setImageResource(R.drawable.calendar_down_icon);
            checkWeekCalendar();
        } else {
            mState = ScheduleState.OPEN;
            monthCalendarView.setVisibility(VISIBLE);
            weekCalendarView.setVisibility(INVISIBLE);
            ivShowCalendar.setImageResource(R.drawable.calendar_up_icon);
        }
    }

    private void checkWeekCalendar() {
        WeekView weekView = weekCalendarView.getCurrentWeekView();
        DateTime start = weekView.getStartDate();
        DateTime end = weekView.getEndDate();
        DateTime current = new DateTime(mCurrentSelectYear, mCurrentSelectMonth + 1, mCurrentSelectDay, 23, 59, 59);
        int week = 0;
        while (current.getMillis() < start.getMillis()) {
            week--;
            start = start.plusDays(-7);
        }
        current = new DateTime(mCurrentSelectYear, mCurrentSelectMonth + 1, mCurrentSelectDay, 0, 0, 0);
        if (week == 0) {
            while (current.getMillis() > end.getMillis()) {
                week++;
                end = end.plusDays(7);
            }
        }
        if (week != 0) {
            int position = weekCalendarView.getCurrentItem() + week;
            if (weekCalendarView.getWeekViews().get(position) != null) {
                weekCalendarView.getWeekViews().get(position).setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
                weekCalendarView.getWeekViews().get(position).invalidate();
            } else {
                WeekView newWeekView = weekCalendarView.getWeekAdapter().instanceWeekView(position);
                newWeekView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
                newWeekView.invalidate();
            }
            weekCalendarView.setCurrentItem(position, false);
        }
    }

    public ScheduleEventView getScheduleEventView(){
        return scheduleEventView;
    }

    public SwipeRecyclerView getRvScheduleList(){
        return rvScheduleList;
    }

    public ConstraintLayout getBlankPage(){
        return blankPage;
    }
}
