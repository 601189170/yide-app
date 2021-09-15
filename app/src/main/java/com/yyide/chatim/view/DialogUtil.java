package com.yyide.chatim.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.schedule.ScheduleEditActivity;
import com.yyide.chatim.databinding.DialogAddLabelLayoutBinding;
import com.yyide.chatim.databinding.DialogScheduleCustomRepetitionBinding;
import com.yyide.chatim.databinding.DialogScheduleEditBinding;
import com.yyide.chatim.databinding.DialogScheduleMenuBinding;
import com.yyide.chatim.databinding.DialogScheduleMonthListBinding;
import com.yyide.chatim.databinding.DialogScheduleRemindBinding;
import com.yyide.chatim.databinding.DialogScheduleRepetitionBinding;
import com.yyide.chatim.model.schedule.Label;
import com.yyide.chatim.model.schedule.MonthBean;
import com.yyide.chatim.model.schedule.Remind;
import com.yyide.chatim.model.schedule.Repetition;
import com.yyide.chatim.model.schedule.Schedule;
import com.yyide.chatim.model.schedule.WeekBean;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.DisplayUtils;
import com.yyide.chatim.widget.SpaceItemDecoration;
import com.yyide.chatim.widget.scrollpicker.adapter.ScrollPickerAdapter;
import com.yyide.chatim.widget.scrollpicker.view.ScrollPickerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.val;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/9 15:18
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/9 15:18
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DialogUtil {
    private static final String TAG = "DialogUtil";

    public static void showAlertDialog(Context context, String title, String hintText, String leftText, String rightText, OnClickListener onClickListener) {
        new MaterialAlertDialogBuilder(
                context,
                R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle(title)
                .setMessage(hintText)
                .setNegativeButton(leftText, (dialog, which) -> {
                    onClickListener.onCancel(null);
                })
                .setPositiveButton(rightText, (dialog, which) -> {
                    onClickListener.onEnsure(null);
                })
                .show();
    }

    /**
     * 提示打开消息推送权限
     *
     * @param context
     * @param onClickListener
     */
    public static void notificationHintDialog(Context context, OnClickListener onClickListener) {
        FullScreenDialog tipDialog = new FullScreenDialog(context, R.style.MyDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_open_notification, null);
        // final ConstraintLayout clNotification = view.findViewById(R.id.cl_notification);
        final Button btnOpen = view.findViewById(R.id.btn_open);
        final TextView tvSkip = view.findViewById(R.id.tv_skip);
        btnOpen.setOnClickListener(v -> {
            tipDialog.dismiss();
            onClickListener.onEnsure(v);
        });

        tvSkip.setOnClickListener(v -> {
            tipDialog.dismiss();
            onClickListener.onCancel(v);
        });

        tipDialog.setContentView(view);
        tipDialog.setCancelable(false);
        tipDialog.show();
        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        //重新设置布局高度，可以理解为设置dialog高度
//        ViewGroup.LayoutParams params = clNotification.getLayoutParams();
//        params.height = (int) (screenHeight * 0.3);
//        clNotification.setLayoutParams(params);
        //设置dialog宽度
        Window window = tipDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.setLayout((int) (screenWidth * 0.8), attributes.height);
        //设置dialog背景为透明色
        window.setBackgroundDrawableResource(R.color.transparent);
    }

    public static Dialog hintDialog(Context context, String title, String hintText, String leftText, String rightText, OnClickListener onClickListener) {
        FullScreenDialog tipDialog = new FullScreenDialog(context, R.style.MyDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_hint4, null);
        LinearLayout llhintDialog = view.findViewById(R.id.ll_hint_dialog);
        TextView titleView = view.findViewById(R.id.tv_title);
        titleView.setText(title);
        TextView titleHintText = view.findViewById(R.id.tv_hintText);
        titleHintText.setText(hintText);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        if (!TextUtils.isEmpty(leftText)) {
            tvCancel.setText(leftText);
        }
        tvCancel.setOnClickListener(v -> {
            onClickListener.onCancel(v);
            tipDialog.dismiss();
        });

        TextView tvEnsure = view.findViewById(R.id.tv_ensure);
        if (!TextUtils.isEmpty(rightText)) {
            tvEnsure.setText(rightText);
        }
        tvEnsure.setOnClickListener(v -> {
            onClickListener.onEnsure(view);
            tipDialog.dismiss();
        });

        tipDialog.setContentView(view);
        tipDialog.setCancelable(false);
        tipDialog.show();
        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        //重新设置布局高度，可以理解为设置dialog高度
        ViewGroup.LayoutParams params = llhintDialog.getLayoutParams();
        params.height = (int) (screenHeight * 0.3);
        llhintDialog.setLayoutParams(params);
        //设置dialog宽度
        Window window = tipDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.setLayout((int) (screenWidth * 0.8), attributes.height);
        //设置dialog背景为透明色
        window.setBackgroundDrawableResource(R.color.transparent);
        return tipDialog;
    }

    public static void showCustomRepetitionScheduleDialog(Context context) {
        List<String> list = new ArrayList<>();
        list.add("每");
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            list2.add("" + (i + 1));
        }
        List<String> list22 = new ArrayList<>();
        for (int i = 0; i < 72; i++) {
            list22.add(""+(i+1));
        }
        List<String> list23 = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list23.add(""+(i+1));
        }

        List<String> list3 = new ArrayList<>();
        list3.add("天");
        list3.add("月");
        list3.add("周");
        AtomicReference<String> number = new AtomicReference<>();
        AtomicReference<String> unit = new AtomicReference<>();
        DialogScheduleCustomRepetitionBinding binding = DialogScheduleCustomRepetitionBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        ScrollPickerView scrollPickerView = binding.scrollPickerView;
        scrollPickerView.setLayoutManager(new LinearLayoutManager(context));
        ScrollPickerAdapter.ScrollPickerAdapterBuilder<String> builder =
                new ScrollPickerAdapter.ScrollPickerAdapterBuilder<String>(context)
                        .setDataList(list)
                        .selectedItemOffset(1)
                        .visibleItemNumber(3)
                        .setDivideLineColor("#F2F7FA")
                        .setItemViewProvider(null)
                        .setOnClickListener(new ScrollPickerAdapter.OnClickListener() {
                            @Override
                            public void onSelectedItemClicked(View v) {
                                String text = (String) v.getTag();
                                Log.e(TAG, "onSelectedItemClicked: " + text);
                            }
                        });
        ScrollPickerAdapter mScrollPickerAdapter = builder.build();
        scrollPickerView.setAdapter(mScrollPickerAdapter);


        ScrollPickerView scrollPickerNumber = binding.scrollPickerNumber;
        scrollPickerNumber.setLayoutManager(new LinearLayoutManager(context));
        ScrollPickerAdapter.ScrollPickerAdapterBuilder<String> builder2 =
                new ScrollPickerAdapter.ScrollPickerAdapterBuilder<String>(context)
                        .setDataList(list2)
                        .selectedItemOffset(1)
                        .visibleItemNumber(3)
                        .setDivideLineColor("#F2F7FA")
                        .setItemViewProvider(null)
                        .setOnScrolledListener(v -> {
                            String text = (String) v.getTag();
                            Log.e(TAG, "onSelectedItemClicked: " + text);
                            number.set(text);
                        });
        final ScrollPickerAdapter scrollPickerNumberAdapter = builder2.build();
        scrollPickerNumber.setAdapter(scrollPickerNumberAdapter);

        ScrollPickerView scrollPickerUnit = binding.scrollPickerUnit;
        scrollPickerUnit.setLayoutManager(new LinearLayoutManager(context));
        ScrollPickerAdapter.ScrollPickerAdapterBuilder<String> builder3 =
                new ScrollPickerAdapter.ScrollPickerAdapterBuilder<String>(context)
                        .setDataList(list3)
                        .selectedItemOffset(1)
                        .visibleItemNumber(3)
                        .setDivideLineColor("#F2F7FA")
                        .setItemViewProvider(null)
                        .setOnScrolledListener(v->{
                            String text = (String) v.getTag();
                            Log.e(TAG, "onSelectedItemClicked: " + text);
                            unit.set(text);
                            if ("天".equals(text)){
                                scrollPickerNumberAdapter.setDataList(list2);
                                scrollPickerNumberAdapter.notifyDataSetChanged();
                                binding.rvWeekList.setVisibility(View.GONE);
                                binding.rvMonthList.setVisibility(View.GONE);
                            }else if("月".equals(text)){
                                scrollPickerNumberAdapter.setDataList(list23);
                                scrollPickerNumberAdapter.notifyDataSetChanged();
                                binding.rvWeekList.setVisibility(View.GONE);
                                binding.rvMonthList.setVisibility(View.VISIBLE);
                            }else if ("周".equals(text)){
                                scrollPickerNumberAdapter.setDataList(list22);
                                scrollPickerNumberAdapter.notifyDataSetChanged();
                                binding.rvWeekList.setVisibility(View.VISIBLE);
                                binding.rvMonthList.setVisibility(View.GONE);
                            }
                        });
        final ScrollPickerAdapter scrollPickerUnitAdapter = builder3.build();
        scrollPickerUnit.setAdapter(scrollPickerUnitAdapter);

        List<WeekBean> weekList = WeekBean.Companion.getList();
        binding.rvWeekList.setLayoutManager(new GridLayoutManager(context,3));
        BaseQuickAdapter adapter = new BaseQuickAdapter<WeekBean,BaseViewHolder>(R.layout.item_dialog_week_custom_repetition){

            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, WeekBean weekBean) {
                baseViewHolder.setText(R.id.title,weekBean.getTitle());
                baseViewHolder.getView(R.id.title).setSelected(weekBean.getChecked());
                baseViewHolder.itemView.setOnClickListener(v -> {
                    weekBean.setChecked(!weekBean.getChecked());
                    baseViewHolder.getView(R.id.title).setSelected(weekBean.getChecked());
                });
            }
        };
        adapter.setList(weekList);
        binding.rvWeekList.addItemDecoration(new SpaceItemDecoration(DisplayUtils.dip2px(context, 2f),3));
        binding.rvWeekList.setAdapter(adapter);

        final List<MonthBean> monthList = MonthBean.Companion.getList();
        binding.rvMonthList.setLayoutManager(new GridLayoutManager(context,7));
        BaseQuickAdapter quickAdapter = new BaseQuickAdapter<MonthBean,BaseViewHolder>(R.layout.item_dialog_month_custom_repetition){

            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, MonthBean monthBean) {
                baseViewHolder.setText(R.id.title,monthBean.getTitle());
                baseViewHolder.getView(R.id.title).setSelected(monthBean.getChecked());
                baseViewHolder.itemView.setOnClickListener(v -> {
                    monthBean.setChecked(!monthBean.getChecked());
                    baseViewHolder.getView(R.id.title).setSelected(monthBean.getChecked());
                });
            }
        };
        quickAdapter.setList(monthList);
        binding.rvMonthList.addItemDecoration(new SpaceItemDecoration(DisplayUtils.dip2px(context, 2f),7));
        binding.rvMonthList.setAdapter(quickAdapter);

        binding.tvCancel.setOnClickListener(v -> {
            mDialog.dismiss();
        });
        binding.tvFinish.setOnClickListener(v -> {
            mDialog.dismiss();
        });
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        lp.height = DisplayUtils.dip2px(context, 623f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public static void showMonthScheduleListDialog(Context context, String date, List<Schedule> scheduleList) {
        DialogScheduleMonthListBinding binding = DialogScheduleMonthListBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        BaseQuickAdapter adapter = new BaseQuickAdapter<Schedule, BaseViewHolder>(R.layout.item_dialog_month_schedule) {
            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, Schedule schedule) {
                baseViewHolder.setText(R.id.tv_title, schedule.getTitle());
                final ImageView imageView = baseViewHolder.getView(R.id.iv_type);
                switch (schedule.getType()) {
                    case Schedule.SCHEDULE_TYPE_SCHEDULE:
                        imageView.setImageResource(R.drawable.type_schedule_icon);
                        break;
                    case Schedule.SCHEDULE_TYPE_SCHOOL_SCHEDULE:
                        imageView.setImageResource(R.drawable.type_school_schedule_icon);
                        break;
                    case Schedule.SCHEDULE_TYPE_CONFERENCE:
                        imageView.setImageResource(R.drawable.type_conference_icon);
                        break;
                    case Schedule.SCHEDULE_TYPE_CLASS_SCHEDULE:
                        imageView.setImageResource(R.drawable.type_class_schedule_icon);
                        break;
                    default:
                        break;
                }
                final String startDate = schedule.getStartDate();
                final String endDate = schedule.getEndDate();
                baseViewHolder.setText(
                        R.id.tv_date,
                        DateUtils.formatTime(
                                startDate,
                                "",
                                "HH:mm"
                        ) + "-" + DateUtils.formatTime(endDate, "", "HH:mm")
                );
            }
        };

        adapter.setList(scheduleList);
        binding.rvScheduleList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvScheduleList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Log.e(TAG, "convert: " + scheduleList.get(position).toString());
            context.startActivity(new Intent(context, ScheduleEditActivity.class));
        });
        binding.clAddSchedule.setOnClickListener(v -> {
            mDialog.dismiss();
            DialogUtil.showAddScheduleDialog(context, null);
        });

        binding.tvDate.setText(date);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.popwin_anim_style2);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels);
        lp.height = DisplayUtils.dip2px(context, 330f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public static void showScheduleMenuDialog(Context context, View view, OnMenuItemListener onMenuItemListener) {
        DialogScheduleMenuBinding binding = DialogScheduleMenuBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        binding.clTodayList.setOnClickListener(v -> {
            mDialog.dismiss();
            onMenuItemListener.onMenuItem(0);
        });
        binding.clDayView.setOnClickListener(v -> {
            mDialog.dismiss();
            onMenuItemListener.onMenuItem(1);
        });
        binding.clMonthView.setOnClickListener(v -> {
            mDialog.dismiss();
            onMenuItemListener.onMenuItem(2);
        });
        binding.clListView.setOnClickListener(v -> {
            mDialog.dismiss();
            onMenuItemListener.onMenuItem(3);
        });
        binding.clLabel.setOnClickListener(v -> {
            mDialog.dismiss();
            onMenuItemListener.onMenuItem(4);
        });
        binding.clSetting.setOnClickListener(v -> {
            mDialog.dismiss();
            onMenuItemListener.onMenuItem(5);
        });
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP | Gravity.LEFT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //获取通知栏高度  重要的在这，获取到通知栏高度
        int notificationBar = Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
        //location [0] 为x绝对坐标;location [1] 为y绝对坐标
        int[] location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗体内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        final int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        final int right = view.getRight();
        lp.x = widthPixels - DisplayUtils.dip2px(context, 160f) - (widthPixels - right); //对 dialog 设置 x 轴坐标
        lp.y = location[1] + view.getHeight() * 2 - notificationBar; //对dialog设置y轴坐标

        lp.width = DisplayUtils.dip2px(context, 160f);
        lp.height = DisplayUtils.dip2px(context, 284f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public static void showRepetitionScheduleDialog(Context context) {
        DialogScheduleRepetitionBinding binding = DialogScheduleRepetitionBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        final val list = Repetition.Companion.getList();
        BaseQuickAdapter adapter = new BaseQuickAdapter<Repetition, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {

            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, Repetition repetition) {
                baseViewHolder.setText(R.id.tv_title, repetition.getTitle());
                ImageView ivRemind = baseViewHolder.getView(R.id.iv_remind);
                ivRemind.setVisibility(repetition.getChecked() ? View.VISIBLE : View.GONE);
                baseViewHolder.itemView.setOnClickListener(v -> {
                    for (Repetition repetition1 : list) {
                        repetition1.setChecked(false);
                    }
                    repetition.setChecked(true);
                    notifyDataSetChanged();
                });
            }
        };
        adapter.setList(list);
        binding.rvRepetitionList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvRepetitionList.setAdapter(adapter);

        binding.tvCancel.setOnClickListener(v -> {
            mDialog.dismiss();
        });
        binding.tvFinish.setOnClickListener(v -> {
            mDialog.dismiss();
        });
        binding.clCustom.setOnClickListener(v -> {
            showCustomRepetitionScheduleDialog(context);
            mDialog.dismiss();
        });
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        lp.height = DisplayUtils.dip2px(context, 623f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }


    public static void showRemindScheduleDialog(Context context) {
        DialogScheduleRemindBinding binding = DialogScheduleRemindBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        final val list = Remind.Companion.getList();
        BaseQuickAdapter adapter = new BaseQuickAdapter<Remind, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {

            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, Remind remind) {
                baseViewHolder.setText(R.id.tv_title, remind.getTitle());
                ImageView ivRemind = baseViewHolder.getView(R.id.iv_remind);
                ivRemind.setVisibility(remind.getChecked() ? View.VISIBLE : View.GONE);
                baseViewHolder.itemView.setOnClickListener(v -> {
                    for (Remind remind1 : list) {
                        remind1.setChecked(false);
                    }
                    binding.ivNotRemind.setVisibility(View.GONE);
                    remind.setChecked(true);
                    notifyDataSetChanged();
                });
            }
        };
        adapter.setList(list);
        binding.rvRemindList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvRemindList.setAdapter(adapter);
        binding.clWhetherRemind.setOnClickListener(v -> {
            for (Remind remind : list) {
                remind.setChecked(false);
            }
            binding.ivNotRemind.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        });
        binding.tvCancel.setOnClickListener(v -> {
            mDialog.dismiss();
        });
        binding.tvFinish.setOnClickListener(v -> {
            mDialog.dismiss();
        });
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        lp.height = DisplayUtils.dip2px(context, 623f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public static void showEditScheduleDialog(Context context) {
        DialogScheduleEditBinding binding = DialogScheduleEditBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        AtomicReference<String> dateStart = new AtomicReference<>("");
        AtomicReference<String> dateEnd = new AtomicReference<>("");
        binding.clRemind.setOnClickListener(v -> {
            showRemindScheduleDialog(context);
        });
        binding.clRepetition.setOnClickListener(v -> {
            showRepetitionScheduleDialog(context);
        });
        int[] ids = binding.groupDateStart.getReferencedIds();
        for (int id : ids) {
            rootView.findViewById(id).setOnClickListener(v -> {
                if (binding.llVLine.getVisibility() == View.GONE) {
                    binding.llVLine.setVisibility(View.VISIBLE);
                    binding.dateTimePicker.setVisibility(View.VISIBLE);
                }
                binding.vDateTopMarkLeft.setVisibility(View.VISIBLE);
                binding.vDateTopMarkRight.setVisibility(View.INVISIBLE);
            });
        }

        int[] ids2 = binding.groupDateEnd.getReferencedIds();
        for (int id : ids2) {
            rootView.findViewById(id).setOnClickListener(v -> {
                if (binding.llVLine.getVisibility() == View.GONE) {
                    binding.llVLine.setVisibility(View.VISIBLE);
                    binding.dateTimePicker.setVisibility(View.VISIBLE);
                }
                binding.vDateTopMarkLeft.setVisibility(View.INVISIBLE);
                binding.vDateTopMarkRight.setVisibility(View.VISIBLE);
            });
        }
        binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2);
                binding.tvTimeStart.setVisibility(View.GONE);
                binding.tvTimeEnd.setVisibility(View.GONE);
            } else {
                binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation);
                binding.tvTimeStart.setVisibility(View.VISIBLE);
                binding.tvTimeEnd.setVisibility(View.VISIBLE);
            }
        });
        binding.dateTimePicker.setOnDateTimeChangedListener(aLong -> {
            final String date = DateUtils.getDate(aLong);
            final String time = DateUtils.formatTime(date, "", "", true);
            Log.e(TAG, "showEditScheduleDialog: " + aLong + ",date=" + date + ", time=" + time);
            if (binding.vDateTopMarkLeft.getVisibility() == View.VISIBLE) {
                //左边选中设置左边的时间数据
                binding.tvDateStart.setText(time);
                if (!binding.checkBox.isChecked()) {
                    binding.tvTimeStart.setText(DateUtils.formatTime(date, "", "HH:mm"));
                }
                dateStart.set(date);
            } else if (binding.vDateTopMarkRight.getVisibility() == View.VISIBLE) {
                //右边被选中设置右边的时间数据
                binding.tvDateEnd.setText(time);
                if (!binding.checkBox.isChecked()) {
                    binding.tvTimeEnd.setText(DateUtils.formatTime(date, "", "HH:mm"));
                }
                dateEnd.set(date);
            } else {
                //第一次设置两边的数据
                binding.tvDateStart.setText(time);
                if (!binding.checkBox.isChecked()) {
                    binding.tvTimeStart.setText(DateUtils.formatTime(date, "", "HH:mm"));
                }
                binding.tvDateEnd.setText(time);
                if (!binding.checkBox.isChecked()) {
                    binding.tvTimeEnd.setText(DateUtils.formatTime(date, "", "HH:mm"));
                }
                dateStart.set(date);
                dateEnd.set(date);
            }

            return null;
        });

        binding.tvFinish.setOnClickListener(v -> {
            mDialog.dismiss();
        });

        binding.tvCancel.setOnClickListener(v -> {
            mDialog.dismiss();
        });

        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        lp.height = DisplayUtils.dip2px(context, 623f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public static void showAddScheduleDialog(Context context, List<Label> labelList) {
        ConstraintLayout rootView = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.dialog_add_schedule_input, null);
        final EditText editView = rootView.findViewById(R.id.edit);
        //完成
        final ImageView finished = rootView.findViewById(R.id.btn_finish);
        //标签
        final TextView tvLabel = rootView.findViewById(R.id.tv_label);
        final ImageView ivLabel = rootView.findViewById(R.id.iv_label);
        //日期
        final TextView tvDate = rootView.findViewById(R.id.tv_date);
        final ImageView ivTime = rootView.findViewById(R.id.iv_time);

        Dialog mDialog = new Dialog(context, R.style.inputDialog);
        mDialog.setContentView(rootView);
        finished.setOnClickListener(v -> {
            mDialog.dismiss();
        });

        tvLabel.setOnClickListener(v -> {
            showAddLabelDialog(context, labelList);
        });
        ivLabel.setOnClickListener(v -> {
            showAddLabelDialog(context, labelList);
        });
        tvDate.setOnClickListener(v -> {
            showEditScheduleDialog(context);
        });
        ivTime.setOnClickListener(v -> {
            showEditScheduleDialog(context);
        });
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //dialogWindow.setWindowAnimations(R.style.DialogAnimStyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //设置宽高
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels;
        rootView.measure(0, 0);
        lp.height = rootView.getMeasuredHeight();
        lp.dimAmount = 0.75f; //半透明背景的灰度 在0.0f和1.0f之间，0.0f完全不暗，1.0f全暗
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
        showKeyboard(context, editView);
    }

    public static void showAddLabelDialog(Context context, List<Label> labelList) {
        DialogAddLabelLayoutBinding binding = DialogAddLabelLayoutBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        binding.btnClose.setOnClickListener(v -> {
            Log.e(TAG, "showAddLabelDialog: " + labelList.toString());
            mDialog.dismiss();
        });
        BaseQuickAdapter adapter = new BaseQuickAdapter<Label, BaseViewHolder>(R.layout.item_dialog_add_label) {
            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, Label label) {
                Log.e(TAG, "convert: " + label.toString());
                baseViewHolder.setText(R.id.tv_label, label.getTitle());
                baseViewHolder.setBackgroundColor(R.id.tv_label, Color.parseColor(label.getColor()));
                baseViewHolder.itemView.setOnClickListener(v -> {
                    label.setChecked(!label.getChecked());
                    notifyDataSetChanged();
                });
                final CheckBox checkbox = baseViewHolder.getView(R.id.checkBox);
                checkbox.setChecked(label.getChecked());
                checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    label.setChecked(isChecked);
                });
            }
        };
        adapter.setList(labelList);
        binding.rvLabelList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvLabelList.setAdapter(adapter);

        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        lp.height = DisplayUtils.dip2px(context, 276f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }


    private static void showKeyboard(Context mContext, EditText messageTextView) {
        if (messageTextView != null) {
            //设置可获得焦点
            messageTextView.setFocusable(true);
            messageTextView.setFocusableInTouchMode(true);
            //请求获得焦点
            messageTextView.requestFocus();
            //调用系统输入法
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(messageTextView, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public interface OnClickListener {
        void onCancel(View view);

        void onEnsure(View view);
    }

    public interface OnMenuItemListener {
        void onMenuItem(int index);
    }
}
