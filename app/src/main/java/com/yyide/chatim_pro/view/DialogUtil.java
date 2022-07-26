package com.yyide.chatim_pro.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;
import com.yyide.chatim_pro.BaseApplication;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.face.FaceCaptureProtocolActivity;
import com.yyide.chatim_pro.activity.meeting.MeetingSaveActivity;
import com.yyide.chatim_pro.activity.operation.viewmodel.OperationViewModel;
import com.yyide.chatim_pro.activity.schedule.ScheduleEditActivityMain;
import com.yyide.chatim_pro.activity.schedule.ScheduleTimetableClassActivity;
import com.yyide.chatim_pro.adapter.schedule.ScheduleMonthListAdapter;
import com.yyide.chatim_pro.database.ScheduleDaoUtil;
import com.yyide.chatim_pro.databinding.DialogAddLabelLayoutBinding;
import com.yyide.chatim_pro.databinding.DialogAddScheduleInputV2Binding;
import com.yyide.chatim_pro.databinding.DialogFaceRecognitionUserProtocolBinding;
import com.yyide.chatim_pro.databinding.DialogLabelTopMenuSelectLayoutBinding;
import com.yyide.chatim_pro.databinding.DialogRepetitionScheduleModifyBinding;
import com.yyide.chatim_pro.databinding.DialogScheduleCustomRepetitionBinding;
import com.yyide.chatim_pro.databinding.DialogScheduleDelBinding;
import com.yyide.chatim_pro.databinding.DialogScheduleEditBinding;
import com.yyide.chatim_pro.databinding.DialogScheduleLabelCreateBinding;
import com.yyide.chatim_pro.databinding.DialogScheduleMenuBinding;
import com.yyide.chatim_pro.databinding.DialogScheduleMonthListBinding;
import com.yyide.chatim_pro.databinding.DialogScheduleRemindBinding;
import com.yyide.chatim_pro.databinding.DialogScheduleRepetitionBinding;
import com.yyide.chatim_pro.databinding.DialogWorkSelect2Binding;
import com.yyide.chatim_pro.databinding.DialogWorkSelectBinding;
import com.yyide.chatim_pro.model.SubjectBean;
import com.yyide.chatim_pro.model.schedule.LabelColor;
import com.yyide.chatim_pro.model.schedule.LabelListRsp;
import com.yyide.chatim_pro.model.schedule.MonthBean;
import com.yyide.chatim_pro.model.schedule.NewLabel;
import com.yyide.chatim_pro.model.schedule.Remind;
import com.yyide.chatim_pro.model.schedule.Repetition;
import com.yyide.chatim_pro.model.schedule.RepetitionDataBean;
import com.yyide.chatim_pro.model.schedule.Schedule;
import com.yyide.chatim_pro.model.schedule.ScheduleData;
import com.yyide.chatim_pro.model.schedule.WeekBean;
import com.yyide.chatim_pro.model.selectBean;
import com.yyide.chatim_pro.model.selectSubjectByUserIdRsp;
import com.yyide.chatim_pro.utils.ColorUtil;
import com.yyide.chatim_pro.utils.DateUtils;
import com.yyide.chatim_pro.utils.DisplayUtils;
import com.yyide.chatim_pro.utils.MaxTextLengthFilter;
import com.yyide.chatim_pro.utils.ScheduleRepetitionRuleUtil;
import com.yyide.chatim_pro.viewmodel.LabelManageViewModel;
import com.yyide.chatim_pro.viewmodel.ScheduleEditViewModel;
import com.yyide.chatim_pro.widget.CircleFrameLayout;
import com.yyide.chatim_pro.widget.CustomInputDialog;
import com.yyide.chatim_pro.widget.SpaceItemDecoration;
import com.yyide.chatim_pro.widget.WheelView;
import com.yyide.chatim_pro.widget.scrollpicker.adapter.ScrollPickerAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showLabelCreateScheduleDialog(Context context, LifecycleOwner lifecycleOwner, OnClickListener onClickListener) {
        DialogScheduleLabelCreateBinding binding = DialogScheduleLabelCreateBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        final List<LabelColor> labelColorList = LabelColor.Companion.getLabelColorList();
        final BaseQuickAdapter adapter = new BaseQuickAdapter<LabelColor, BaseViewHolder>(R.layout.item_label_color) {

            @Override
            protected void convert(@NonNull BaseViewHolder holder, LabelColor item) {
                final CircleFrameLayout circleFrameLayout = (CircleFrameLayout) holder.getView(R.id.v_border_circle);
                final ImageView imageView = (ImageView) holder.getView(R.id.iv_default_color);
                circleFrameLayout.setRadius(DisplayUtils.dip2px(context, 24f));
                circleFrameLayout.setBackgroundColor(Color.parseColor(item.getColor()));
                final View view = holder.getView(R.id.v_border);
                view.setVisibility(item.getChecked() ? View.VISIBLE : View.INVISIBLE);

                if (item.getColor().equals(LabelColor.color1)) {
                    circleFrameLayout.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    circleFrameLayout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }
                holder.getView(R.id.itemView).setOnClickListener(v -> {
                    for (LabelColor labelColor : labelColorList) {
                        labelColor.setChecked(false);
                    }
                    item.setChecked(true);
                    notifyDataSetChanged();
                });
            }
        };
        adapter.setList(labelColorList);
        binding.rvLabelList.setLayoutManager(new GridLayoutManager(context, 6));
        binding.rvLabelList.addItemDecoration(new SpaceItemDecoration(DisplayUtils.dip2px(context, 10f), 6));
        binding.rvLabelList.setAdapter(adapter);
        final InputFilter[] inputFilter = {new MaxTextLengthFilter(20)};
        binding.edit.setFilters(inputFilter);
        binding.tvCancel.setOnClickListener(v -> {
            mDialog.dismiss();
        });
        final LabelManageViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(BaseApplication.getInstance()).create(LabelManageViewModel.class);
        binding.tvFinish.setOnClickListener(v -> {
            String labelName = binding.edit.getText().toString();
            if (TextUtils.isEmpty(labelName)) {
                ToastUtils.showShort("请输入标签名称");
                return;
            }
            String colorValue = LabelColor.color1;
            final Optional<LabelColor> first = labelColorList.stream().filter(it -> it.getChecked()).findFirst();
            if (first.isPresent()) {
                colorValue = first.get().getColor();
            }
            List<NewLabel> list = new ArrayList<>();
            list.add(new NewLabel("",labelName, colorValue));
            viewModel.addLabel(list);
            viewModel.getLabelAddOrEditResult().observe(lifecycleOwner, aBoolean -> {
                if (aBoolean) {
                    mDialog.dismiss();
                    onClickListener.onEnsure(v);
                } else {
                    ToastUtils.showShort("添加标签失败");
                }
            });
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

    private static ScrollPickerAdapter getScrollPickerAdapter(Context context, List<String> list, PickerAdapterListener listener) {
        ScrollPickerAdapter.ScrollPickerAdapterBuilder<String> builder2 =
                new ScrollPickerAdapter.ScrollPickerAdapterBuilder<String>(context)
                        .setDataList(list)
                        .selectedItemOffset(1)
                        .visibleItemNumber(3)
                        .setDivideLineColor("#F2F7FA")
                        .setItemViewProvider(null)
                        .setOnScrolledListener(v -> {
                            String text = (String) v.getTag();
                            Log.e(TAG, "onSelectedItemClicked: " + text);
                            if (listener != null) {
                                listener.change(text);
                            }
                        });
        return builder2.build();
    }

    public interface PickerAdapterListener {
        void change(String number);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showCustomRepetitionScheduleDialog(Context context, ScheduleEditViewModel scheduleEditViewModel) {
        List<WeekBean> weekList = WeekBean.Companion.getList();
        final List<MonthBean> monthList = MonthBean.Companion.getList();
        AtomicReference<String> number = new AtomicReference<>();
        AtomicReference<String> unit = new AtomicReference<>();
        final Repetition repetition = scheduleEditViewModel.getRepetitionLiveData().getValue();
        Log.e(TAG, "showCustomRepetitionScheduleDialog: " + JSON.toJSONString(repetition));
        unit.set("天");
        number.set("1");
        if (repetition != null && repetition.getRule() != null && !repetition.getRule().isEmpty()) {
            final Map<String, Object> repetitionRule = repetition.getRule();
            final String freq = (String) repetitionRule.get("freq");
            final String interval = (String) repetitionRule.get("interval");
            number.set(interval);
            if ("daily".equals(freq) || "DAILY".equals(freq)) {
                unit.set("天");
            } else if ("weekly".equals(freq) || "WEEKLY".equals(freq)) {
                unit.set("周");
                final String byweekday = (String) repetitionRule.get("byweekday");
                final String[] byweekdayList = byweekday.replace("[", "").replace("]", "").split(",");
                final List<String> stringList = new ArrayList<>();
                for (String s : byweekdayList) {
                    stringList.add(s.trim().replace("\"", ""));
                }
                for (WeekBean weekBean : weekList) {
                    weekBean.setChecked(stringList.contains(weekBean.getShortname()));
                }
            } else if ("monthly".equals(freq) || "MONTHLY".equals(freq)) {
                unit.set("月");
                final String bymonthday = (String) repetitionRule.get("bymonthday");
                final String[] bymonthdayList = bymonthday.replace("[", "").replace("]", "").split(",");
                final List<String> stringList = new ArrayList<>();
                for (String s : bymonthdayList) {
                    stringList.add(s.trim().replace("\"", ""));
                }
                for (MonthBean monthBean : monthList) {
                    monthBean.setChecked(stringList.contains(monthBean.getTitle()));
                }
            }
        }

        DialogScheduleCustomRepetitionBinding binding = DialogScheduleCustomRepetitionBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        List<String> list = new ArrayList<>();
        list.add("每");

        final List<RepetitionDataBean> repetitionDataBeanList = RepetitionDataBean.Companion.getList();
        final List<String> listunit = repetitionDataBeanList.stream().map(RepetitionDataBean::getUnit).collect(Collectors.toList());
        final int unitIndex = RepetitionDataBean.Companion.getUnitIndex(unit.get(), repetitionDataBeanList);
        binding.eachWv.setData(list);
        binding.eachWv.setDefault(0);
        binding.unitWv.setData(listunit);
        binding.unitWv.setDefault(unitIndex);
        binding.unitWv.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                unit.set(text);
                final List<String> numberList = RepetitionDataBean.Companion.getNumberList(text, repetitionDataBeanList);
                binding.numberWv.setData(numberList);
                binding.numberWv.setDefault(0);
                if ("月".equals(text)) {
                    binding.rvMonthList.setVisibility(View.VISIBLE);
                    binding.rvWeekList.setVisibility(View.GONE);
                } else if ("周".equals(text)) {
                    binding.rvMonthList.setVisibility(View.GONE);
                    binding.rvWeekList.setVisibility(View.VISIBLE);
                } else {
                    binding.rvMonthList.setVisibility(View.GONE);
                    binding.rvWeekList.setVisibility(View.GONE);
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        final List<String> numberlist = repetitionDataBeanList.get(unitIndex).getNumber();
        final int numberIndex = Integer.parseInt(number.get()) - 1;
        binding.numberWv.setData(numberlist);
        binding.numberWv.setDefault(numberIndex);
        binding.numberWv.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                number.set(text);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        binding.rvWeekList.setLayoutManager(new GridLayoutManager(context, 3));
        BaseQuickAdapter adapter = new BaseQuickAdapter<WeekBean, BaseViewHolder>(R.layout.item_dialog_week_custom_repetition) {

            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, WeekBean weekBean) {
                baseViewHolder.setText(R.id.title, weekBean.getTitle());
                baseViewHolder.getView(R.id.title).setSelected(weekBean.getChecked());
                baseViewHolder.getView(R.id.title).setOnClickListener(v -> {
                    weekBean.setChecked(!weekBean.getChecked());
                    baseViewHolder.getView(R.id.title).setSelected(weekBean.getChecked());
                });
            }
        };
        adapter.setList(weekList);
        binding.rvWeekList.addItemDecoration(new SpaceItemDecoration(DisplayUtils.dip2px(context, 2f), 3));
        binding.rvWeekList.setAdapter(adapter);

        binding.rvMonthList.setLayoutManager(new GridLayoutManager(context, 7));
        BaseQuickAdapter quickAdapter = new BaseQuickAdapter<MonthBean, BaseViewHolder>(R.layout.item_dialog_month_custom_repetition) {

            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, MonthBean monthBean) {
                baseViewHolder.setText(R.id.title, monthBean.getTitle());
                baseViewHolder.getView(R.id.title).setSelected(monthBean.getChecked());
                baseViewHolder.getView(R.id.title).setOnClickListener(v -> {
                    monthBean.setChecked(!monthBean.getChecked());
                    baseViewHolder.getView(R.id.title).setSelected(monthBean.getChecked());
                });
            }
        };
        quickAdapter.setList(monthList);
        binding.rvMonthList.addItemDecoration(new SpaceItemDecoration(DisplayUtils.dip2px(context, 2f), 7));
        binding.rvMonthList.setAdapter(quickAdapter);

        if ("月".equals(unit.get())) {
            binding.rvMonthList.setVisibility(View.VISIBLE);
            binding.rvWeekList.setVisibility(View.GONE);
        } else if ("周".equals(unit.get())) {
            binding.rvMonthList.setVisibility(View.GONE);
            binding.rvWeekList.setVisibility(View.VISIBLE);
        } else {
            binding.rvMonthList.setVisibility(View.GONE);
            binding.rvWeekList.setVisibility(View.GONE);
        }

        binding.tvCancel.setOnClickListener(v -> {
            mDialog.dismiss();
        });
        binding.tvFinish.setOnClickListener(v -> {
            Map<String, Object> rule = new HashMap<String, Object>();
            final String unitStr = unit.get();
            final String numberStr = number.get();
            if ("天".equals(unitStr)) {
                //rule = "{\"freq\": \"daily\",\"interval\": \"" + numberStr + "\"}";
                rule.put("freq", "daily");
                rule.put("interval", numberStr);

            } else if ("月".equals(unitStr)) {
                final List<MonthBean> collect = monthList.stream().filter(MonthBean::getChecked).collect(Collectors.toList());
                if (!collect.isEmpty()) {
                    List<String> bymonthday = collect.stream().map(MonthBean::getTitle).collect(Collectors.toList());
                    //rule = "{\"freq\": \"monthly\",\"interval\": \"" + numberStr + "\",\"bymonthday\":\"" + bymonthday + "\"}";
                    rule.put("freq", "monthly");
                    rule.put("interval", numberStr);
                    rule.put("bymonthday", JSON.toJSONString(bymonthday));

                } else {
                    //rule = "{\"freq\": \"monthly\",\"interval\": \"" + numberStr + "\"}";
                    rule.put("freq", "monthly");
                    rule.put("interval", numberStr);
                }

            } else if ("周".equals(unitStr)) {
                final List<WeekBean> collect = weekList.stream().filter(WeekBean::getChecked).collect(Collectors.toList());
                if (!collect.isEmpty()) {
                    List<String> byweekday = collect.stream().map(WeekBean::getShortname).collect(Collectors.toList());
                    //rule = "{\"freq\": \"weekly\",\"interval\": \"" + numberStr + "\",\"byweekday\":\"" + byweekday + "\"}";
                    rule.put("freq", "weekly");
                    rule.put("interval", numberStr);
                    rule.put("byweekday", JSON.toJSONString(byweekday));
                } else {
                    //rule = "{\"freq\": \"weekly\",\"interval\": \"" + numberStr + "\"}";
                    rule.put("freq", "weekly");
                    rule.put("interval", numberStr);
                }
            }
            scheduleEditViewModel.getRepetitionLiveData().setValue(new Repetition(8, "自定义重复", true, rule));
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showMonthScheduleListDialog(Context context, String date, List<ScheduleData> scheduleList, LifecycleOwner lifecycleOwner, OnScheduleAddSimpleListener onScheduleAddSimpleListener) {
        DialogScheduleMonthListBinding binding = DialogScheduleMonthListBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        binding.rvScheduleList.setLayoutManager(new LinearLayoutManager(context));
        final ScheduleMonthListAdapter adapter = new ScheduleMonthListAdapter();
        adapter.setList(scheduleList);
        binding.rvScheduleList.addItemDecoration(new com.yyide.chatim_pro.view.SpaceItemDecoration(10));
        binding.rvScheduleList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
                    mDialog.dismiss();
                    ScheduleData scheduleData = scheduleList.get(position);
                    if (scheduleData.getType().equals("" + Schedule.SCHEDULE_TYPE_CONFERENCE)) {
                        MeetingSaveActivity.Companion.jumpUpdate(context, scheduleData.getId());
                        return;
                    }
                    if (scheduleData.getType().equals("" + Schedule.SCHEDULE_TYPE_CLASS_SCHEDULE)) {
                        ScheduleTimetableClassActivity.Companion.jump(context, scheduleData);
                        return;
                    }
                    final Intent intent = new Intent(context, ScheduleEditActivityMain.class);
                    intent.putExtra("data", JSON.toJSONString(scheduleList.get(position)));
                    context.startActivity(intent);
                }
        );

        binding.clAddSchedule.setOnClickListener(v -> {
            final DateTime dateTime = ScheduleDaoUtil.INSTANCE.toDateTime(date);
            final DateTime nowTime = ScheduleDaoUtil.INSTANCE.dateTimeJointNowTime(dateTime);
            onScheduleAddSimpleListener.add(v, nowTime);
            mDialog.dismiss();
            //DialogUtil.showAddScheduleDialog(context, lifecycleOwner,nowTime);
        });

        binding.tvDate.setText(DateUtils.formatTime(date, "", "", true));
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

    public static void showScheduleDelDialog(Context context, View view, OnClickListener onClickListener) {
        DialogScheduleDelBinding binding = DialogScheduleDelBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        rootView.setOnClickListener(v -> {
            //删除日程
            onClickListener.onEnsure(v);
            mDialog.dismiss();
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
        lp.x = widthPixels - DisplayUtils.dip2px(context, 112f) - (widthPixels - right) - view.getWidth() / 2 + DisplayUtils.dip2px(context, 12f); //对 dialog 设置 x 轴坐标
        lp.y = location[1] + view.getHeight() - notificationBar; //对dialog设置y轴坐标

        lp.width = DisplayUtils.dip2px(context, 112f);
        lp.height = DisplayUtils.dip2px(context, 75f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.5f;
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
        lp.x = widthPixels - DisplayUtils.dip2px(context, 160f) - (widthPixels - right) - DisplayUtils.dip2px(context, 20f); //对 dialog 设置 x 轴坐标
        lp.y = location[1] + view.getHeight() / 2;// - notificationBar; //对dialog设置y轴坐标

        lp.width = DisplayUtils.dip2px(context, 115f);
        lp.height = DisplayUtils.dip2px(context, 140f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showRepetitionScheduleDialog(Context context, LifecycleOwner lifecycleOwner, ScheduleEditViewModel scheduleEditViewModel) {
        DialogScheduleRepetitionBinding binding = DialogScheduleRepetitionBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        final List<Repetition> list = Repetition.Companion.getList();
        final Repetition value = scheduleEditViewModel.getRepetitionLiveData().getValue();
        if (value != null) {
            for (Repetition repetition : list) {
                repetition.setChecked(Objects.equals(repetition.getRule(), value.getRule()));
            }
            if (list.stream().noneMatch(Repetition::getChecked)) {
                binding.ivNotRemind.setImageResource(R.drawable.schedule_remind_selected_icon);
            }
        }
        scheduleEditViewModel.getRepetitionLiveData().observe(lifecycleOwner, repetition -> {
            if (repetition.getCode() == 8) {
                String rule = ScheduleRepetitionRuleUtil.INSTANCE.ruleToString(JSON.toJSONString(repetition.getRule()));
                binding.tvCustomRule.setText(rule);
            }
        });

        BaseQuickAdapter adapter = new BaseQuickAdapter<Repetition, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {

            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, Repetition repetition) {
                baseViewHolder.setText(R.id.tv_title, repetition.getTitle());
                ImageView ivRemind = baseViewHolder.getView(R.id.iv_remind);
                ivRemind.setVisibility(repetition.getChecked() ? View.VISIBLE : View.GONE);
                baseViewHolder.getView(R.id.itemView).setOnClickListener(v -> {
                    for (Repetition repetition1 : list) {
                        repetition1.setChecked(false);
                    }
                    repetition.setChecked(true);
                    binding.ivNotRemind.setImageResource(R.drawable.schedule_arrow_right);
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
            final List<Repetition> collect = list.stream().filter(Repetition::getChecked).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                scheduleEditViewModel.getRepetitionLiveData().setValue(collect.get(0));
            }
            mDialog.dismiss();
        });
        binding.clCustom.setOnClickListener(v -> {
            showCustomRepetitionScheduleDialog(context, scheduleEditViewModel);
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showRemindScheduleDialog(Context context, ScheduleEditViewModel scheduleEditViewModel, List<Remind> list) {
        DialogScheduleRemindBinding binding = DialogScheduleRemindBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        //final List<Remind> list = Remind.Companion.getList();
        final Remind value = scheduleEditViewModel.getRemindLiveData().getValue();
        if (value != null) {
            //反选
            binding.ivNotRemind.setVisibility(View.GONE);
            for (Remind remind : list) {
                remind.setChecked(Objects.equals(remind.getId(), value.getId()));
            }
            if (!TextUtils.isEmpty(value.getId()) && "10".equals(value.getId())) {
                binding.ivNotRemind.setVisibility(View.VISIBLE);
            }
        }
        BaseQuickAdapter adapter = new BaseQuickAdapter<Remind, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {

            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, Remind remind) {
                baseViewHolder.setText(R.id.tv_title, remind.getTitle());
                ImageView ivRemind = baseViewHolder.getView(R.id.iv_remind);
                ivRemind.setVisibility(remind.getChecked() ? View.VISIBLE : View.GONE);
                baseViewHolder.getView(R.id.itemView).setOnClickListener(v -> {
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
            final List<Remind> collect = list.stream().filter(Remind::getChecked).collect(Collectors.toList());
            if (collect.isEmpty()) {
                scheduleEditViewModel.getRemindLiveData().setValue(Remind.Companion.getNotRemind());
            } else {
                scheduleEditViewModel.getRemindLiveData().setValue(collect.get(0));
            }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showEditScheduleDialog(Context context, LifecycleOwner lifecycleOwner, ScheduleEditViewModel scheduleEditViewModel) {
        DialogScheduleEditBinding binding = DialogScheduleEditBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        AtomicReference<String> dateStart = new AtomicReference<>("");
        AtomicReference<String> dateEnd = new AtomicReference<>("");
        final List<Remind> list = Remind.Companion.getList();
        final List<Remind> list2 = Remind.Companion.getList2();
        if (scheduleEditViewModel.getRemindLiveData().getValue() == null) {
            binding.tvRemind.setText("不重复");
            scheduleEditViewModel.getRemindLiveData().setValue(Remind.Companion.getNotRemind());
        }
        binding.clRemind.setOnClickListener(v -> {
            showRemindScheduleDialog(context, scheduleEditViewModel, binding.checkBox.isChecked() ? list2 : list);
        });
        binding.clRepetition.setOnClickListener(v -> {
            showRepetitionScheduleDialog(context, lifecycleOwner, scheduleEditViewModel);
        });
        binding.clStartTime.setOnClickListener(v -> {
            if (binding.llVLine.getVisibility() == View.GONE) {
                binding.llVLine.setVisibility(View.VISIBLE);
                binding.dateTimePicker.setVisibility(View.VISIBLE);
            }
            binding.vDateTopMarkLeft.setVisibility(View.VISIBLE);
            binding.vDateTopMarkRight.setVisibility(View.INVISIBLE);
            binding.dateTimePicker.setDefaultMillisecond(DateUtils.formatTime(dateStart.get(), ""));
        });
        binding.clEndTime.setOnClickListener(v -> {
            if (binding.llVLine.getVisibility() == View.GONE) {
                binding.llVLine.setVisibility(View.VISIBLE);
                binding.dateTimePicker.setVisibility(View.VISIBLE);
            }
            binding.vDateTopMarkLeft.setVisibility(View.INVISIBLE);
            binding.vDateTopMarkRight.setVisibility(View.VISIBLE);
            binding.dateTimePicker.setDefaultMillisecond(DateUtils.formatTime(dateEnd.get(), ""));
        });
        final Boolean allDay = scheduleEditViewModel.getAllDayLiveData().getValue();
        final String startTime = scheduleEditViewModel.getStartTimeLiveData().getValue();
        final String endTime = scheduleEditViewModel.getEndTimeLiveData().getValue();
        if (allDay != null && allDay) {
            binding.checkBox.setChecked(true);
            binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2);
            binding.tvTimeStart.setVisibility(View.GONE);
            binding.tvTimeEnd.setVisibility(View.GONE);
        }
        binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //设置是否全天
            scheduleEditViewModel.getAllDayLiveData().setValue(isChecked);
            if (isChecked) {
                binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2);
                binding.tvTimeStart.setVisibility(View.GONE);
                binding.tvTimeEnd.setVisibility(View.GONE);
                //修改提醒类型 全天
                final Optional<Remind> optionalRemind = list2.stream().filter(Remind::getChecked).findFirst();
                if (optionalRemind.isPresent()) {
                    final Remind remind = optionalRemind.get();
                    scheduleEditViewModel.getRemindLiveData().setValue(remind);
                    binding.tvRemind.setText(remind.getTitle());
                } else {
                    scheduleEditViewModel.getRemindLiveData().setValue(Remind.Companion.getNotRemind());
                    binding.tvRemind.setText("不重复");
                }
            } else {
                binding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation);
                binding.tvTimeStart.setVisibility(View.VISIBLE);
                binding.tvTimeEnd.setVisibility(View.VISIBLE);
                //修改提醒类型 非全天
                final Optional<Remind> optionalRemind = list.stream().filter(Remind::getChecked).findFirst();
                if (optionalRemind.isPresent()) {
                    final Remind remind = optionalRemind.get();
                    scheduleEditViewModel.getRemindLiveData().setValue(remind);
                    binding.tvRemind.setText(remind.getTitle());
                } else {
                    scheduleEditViewModel.getRemindLiveData().setValue(Remind.Companion.getNotRemind());
                    binding.tvRemind.setText("不重复");
                }
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
                if (TextUtils.isEmpty(startTime)) {
                    binding.tvDateStart.setText(time);
                    if (!binding.checkBox.isChecked()) {
                        binding.tvTimeStart.setText(DateUtils.formatTime(date, "", "HH:mm"));
                    }
                    dateStart.set(date);
                } else {
                    binding.tvDateStart.setText(DateUtils.formatTime(startTime, "", "", true));
                    if (!binding.checkBox.isChecked()) {
                        binding.tvTimeStart.setText(DateUtils.formatTime(startTime, "", "HH:mm"));
                    }
                    dateStart.set(startTime);
                }
                if (TextUtils.isEmpty(endTime)) {
                    binding.tvDateEnd.setText(time);
                    if (!binding.checkBox.isChecked()) {
                        binding.tvTimeEnd.setText(DateUtils.formatTime(date, "", "HH:mm"));
                    }
                    dateEnd.set(date);
                } else {
                    binding.tvDateEnd.setText(DateUtils.formatTime(endTime, "", "", true));
                    if (!binding.checkBox.isChecked()) {
                        binding.tvTimeEnd.setText(DateUtils.formatTime(endTime, "", "HH:mm"));
                    }
                    dateEnd.set(endTime);
                }
            }

            return null;
        });

        binding.tvFinish.setOnClickListener(v -> {
            final int compareDate = ScheduleDaoUtil.INSTANCE.compareDate(dateStart.get(), dateEnd.get());
            if (compareDate >= 0) {
                ToastUtils.showShort("开始时间不能大于或等于结束时间");
                return;
            }
            scheduleEditViewModel.getStartTimeLiveData().setValue(dateStart.get());
            scheduleEditViewModel.getEndTimeLiveData().setValue(dateEnd.get());
            scheduleEditViewModel.getAllDayLiveData().setValue(binding.checkBox.isChecked());
            mDialog.dismiss();
        });

        binding.tvCancel.setOnClickListener(v -> {
            mDialog.dismiss();
        });
        scheduleEditViewModel.getRemindLiveData().observe(lifecycleOwner, remind -> {
            binding.tvRemind.setText(remind.getTitle());
        });
        scheduleEditViewModel.getRepetitionLiveData().observe(lifecycleOwner, repetition -> {
            binding.tvRepetition.setText(repetition.getTitle());
            if (repetition.getCode() == 8) {
                String rule = ScheduleRepetitionRuleUtil.INSTANCE.ruleToString(JSON.toJSONString(repetition.getRule()));
                binding.tvRepetition.setText("重复");
                binding.tvCustomRule.setText(rule);
            }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showAddScheduleDialog(Context context, LifecycleOwner lifecycleOwner, DateTime date) {
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
        final ScheduleEditViewModel scheduleEditViewModel = new ViewModelProvider.AndroidViewModelFactory(BaseApplication.getInstance()).create(ScheduleEditViewModel.class);
        //String time = DateUtils.formatTime(DateUtils.switchTime(new Date()), "", "MM月dd日 HH:mm");
        if (date == null) {
            date = DateTime.now();
            //time = date.toString("MM月dd日 MM月dd日 HH:mm");
            //scheduleEditViewModel.getStartTimeLiveData().setValue(date.toString("yyyy-MM-dd HH:mm:ss"));
            //scheduleEditViewModel.getEndTimeLiveData().setValue(date.toString("yyyy-MM-dd ")+"23:59:59");

        }
        final List<DateTime> dateTimes = ScheduleDaoUtil.INSTANCE.defaultTwoTimeListOfDateTime(date);
        final DateTime time1 = dateTimes.get(0);
        final DateTime time2 = dateTimes.get(1);
        scheduleEditViewModel.getStartTimeLiveData().setValue(ScheduleDaoUtil.INSTANCE.toStringTime(time1, "yyyy-MM-dd HH:mm:ss"));
        scheduleEditViewModel.getEndTimeLiveData().setValue(ScheduleDaoUtil.INSTANCE.toStringTime(time2, "yyyy-MM-dd HH:mm:ss"));
        String time = ScheduleDaoUtil.INSTANCE.toStringTime(time1, "MM月dd日 HH:mm");
        //arrayOf<InputFilter>(MaxTextLengthFilter(100))
        final InputFilter[] inputFilter = {new MaxTextLengthFilter(20)};
        editView.setFilters(inputFilter);
        tvDate.setText(time);
        finished.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editView.getText().toString())) {
                ToastUtils.showShort("你还没告诉我您要准备做什么！");
                return;
            }
            //日程提交
            scheduleEditViewModel.getScheduleTitleLiveData().setValue(editView.getText().toString());
            scheduleEditViewModel.saveOrModifySchedule(false);
            mDialog.dismiss();
        });

        scheduleEditViewModel.getStartTimeLiveData().observe(lifecycleOwner, dateTime -> {
            Log.e(TAG, "showAddScheduleDialog: 日期已修改");
            if (Objects.equals(scheduleEditViewModel.getAllDayLiveData().getValue(), true)) {
                String startTime = DateUtils.formatTime(dateTime, "", "MM月dd日");
                tvDate.setText(startTime);
                return;
            }
            String startTime = DateUtils.formatTime(dateTime, "", "MM月dd日 HH:mm");
            tvDate.setText(startTime);
        });

        tvLabel.setOnClickListener(v -> {
            showAddLabelDialog(context, lifecycleOwner, scheduleEditViewModel);
        });
        ivLabel.setOnClickListener(v -> {
            showAddLabelDialog(context, lifecycleOwner, scheduleEditViewModel);
        });
        tvDate.setOnClickListener(v -> {
            showEditScheduleDialog(context, lifecycleOwner, scheduleEditViewModel);
        });
        ivTime.setOnClickListener(v -> {
            showEditScheduleDialog(context, lifecycleOwner, scheduleEditViewModel);
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

    /**
     * 添加日程 新v2
     *
     * @param context
     * @param date
     */
    public static Dialog showAddScheduleV2Dialog(Context context, LifecycleOwner lifecycleOwner, ScheduleEditViewModel scheduleEditViewModel, DateTime date, OnScheduleAddListener onScheduleAddListener) {
        DialogAddScheduleInputV2Binding binding = DialogAddScheduleInputV2Binding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        CustomInputDialog mDialog = new CustomInputDialog(context, R.style.inputDialog);
        mDialog.setContentView(rootView);
        if (date == null) {
            date = DateTime.now();
        }
        final List<DateTime> dateTimes = ScheduleDaoUtil.INSTANCE.defaultTwoTimeListOfDateTime(date);
        final DateTime time1 = dateTimes.get(0);
        final DateTime time2 = dateTimes.get(1);
        scheduleEditViewModel.getStartTimeLiveData().setValue(ScheduleDaoUtil.INSTANCE.toStringTime(time1, "yyyy-MM-dd HH:mm:ss"));
//        scheduleEditViewModel.getEndTimeLiveData().setValue(ScheduleDaoUtil.INSTANCE.toStringTime(time2, "MM-dd"));
        scheduleEditViewModel.getEndTimeLiveData().setValue(ScheduleDaoUtil.INSTANCE.toStringTime(time2, "yyyy-MM-dd HH:mm:ss"));
        String time = ScheduleDaoUtil.INSTANCE.toStringTime(time1, "MM月dd日 HH:mm");
        String toptime = ScheduleDaoUtil.INSTANCE.toStringTime(time1, "YYYY年MM日");
        final InputFilter[] inputFilter = {new MaxTextLengthFilter(20)};
        binding.edit.setFilters(inputFilter);
        binding.tvDate.setText(time);
        binding.time.setText(toptime);
        binding.imFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        binding.btnFinish.setOnClickListener(v -> {
            String title = binding.edit.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                ToastUtils.showShort("你还没告诉我您要准备做什么！");
                return;
            }
            if (isHasEmoji(title)) {
                ToastUtils.showShort("日程名称含有非字符的数据，请重新输入!");
                return;
            }
            //日程提交
            mDialog.dismiss();
            //防止重复提交
            binding.btnFinish.setEnabled(false);
            scheduleEditViewModel.getScheduleTitleLiveData().setValue(title);
            onScheduleAddListener.onFinish(v);
        });
        binding.edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                scheduleEditViewModel.getScheduleTitleLiveData().setValue(s.toString());
            }
        });
        scheduleEditViewModel.getStartTimeLiveData().observe(lifecycleOwner, startTime -> {
            if (TextUtils.isEmpty(startTime)) {
                return;
            }
            final DateTime dateTime = ScheduleDaoUtil.INSTANCE.toDateTime(startTime);
            final MutableLiveData<Boolean> allDayLiveData = scheduleEditViewModel.getAllDayLiveData();
            final Boolean allDayLiveDataValue = allDayLiveData.getValue();
            if (allDayLiveDataValue != null && allDayLiveDataValue) {
                binding.tvDate.setText(dateTime.toString("MM月dd日") + " 全天");
                return;
            }
            binding.tvDate.setText(dateTime.toString("MM月dd日 HH:mm"));
        });
        binding.tvLabel.setOnClickListener(onScheduleAddListener::onLabel);
        binding.ivLabel.setOnClickListener(onScheduleAddListener::onLabel);
        binding.tvDate.setOnClickListener(onScheduleAddListener::onDate);
        binding.ivTime.setOnClickListener(onScheduleAddListener::onDate);
        //切换版本
        binding.tvEditionSwitch.setOnClickListener(onScheduleAddListener::onSwitch);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height=WindowManager.LayoutParams.MATCH_PARENT;
        //设置宽高
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels;
        rootView.measure(0, 0);
//        lp.height = rootView.getMeasuredHeight();
        lp.dimAmount = 0.75f; //半透明背景的灰度 在0.0f和1.0f之间，0.0f完全不暗，1.0f全暗
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
        showKeyboard(context, binding.edit);
        return mDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showTopMenuLabelDialog(Context context, View view, LifecycleOwner lifecycleOwner, OnLabelItemListener onLabelItemListener) {
        List<LabelListRsp.DataBean> labelList = new ArrayList<LabelListRsp.DataBean>();
        DialogLabelTopMenuSelectLayoutBinding binding = DialogLabelTopMenuSelectLayoutBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        BaseQuickAdapter adapter = new BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_dialog_label_top_menu_select) {
            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, LabelListRsp.DataBean label) {
                Log.e(TAG, "convert: " + label.toString());
                baseViewHolder.setText(R.id.tv_label, label.getLabelName());
                final GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(DisplayUtils.dip2px(context, 2f));
                gradientDrawable.setColor(ColorUtil.parseColor(label.getColorValue()));
                baseViewHolder.getView(R.id.tv_label).setBackground(gradientDrawable);
                baseViewHolder.getView(R.id.checkBox).setSelected(label.getChecked());
                baseViewHolder.getView(R.id.itemView).setOnClickListener(v -> {
                    label.setChecked(!label.getChecked());
                    notifyDataSetChanged();
                });
            }
        };
        adapter.setList(labelList);
        binding.rvLabelList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvLabelList.setAdapter(adapter);

        mDialog.setOnCancelListener(dialog -> {
            onLabelItemListener.labelItem(labelList.stream().filter(it -> it.getChecked()).collect(Collectors.toList()));
        });
        final LabelManageViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(BaseApplication.getInstance()).create(LabelManageViewModel.class);
        viewModel.selectLabelList();
        viewModel.getLabelList().observe(lifecycleOwner, dataBeans -> {
            labelList.clear();
            labelList.addAll(dataBeans);
            adapter.setList(labelList);
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
        lp.x = widthPixels - DisplayUtils.dip2px(context, 200f) - (widthPixels - right); //对 dialog 设置 x 轴坐标
        lp.y = location[1] + view.getHeight() * 2 - notificationBar; //对dialog设置y轴坐标

        lp.width = DisplayUtils.dip2px(context, 200f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showAddLabelDialog(Context context, LifecycleOwner lifecycleOwner, ScheduleEditViewModel scheduleEditViewModel) {
        List<LabelListRsp.DataBean> labelList = new ArrayList<>();
        DialogAddLabelLayoutBinding binding = DialogAddLabelLayoutBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        binding.tvFinish.setOnClickListener(v -> {
            Log.e(TAG, "showAddLabelDialog: " + labelList.toString());
            final List<LabelListRsp.DataBean> collect = labelList.stream().filter(LabelListRsp.DataBean::getChecked).collect(Collectors.toList());
            scheduleEditViewModel.getLabelListLiveData().setValue(collect);
            mDialog.dismiss();
        });
        binding.tvCancel.setOnClickListener(v -> {
            mDialog.dismiss();
        });

        BaseQuickAdapter adapter = new BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_dialog_add_label) {
            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, LabelListRsp.DataBean label) {
                Log.e(TAG, "convert: " + label.toString());
                baseViewHolder.setText(R.id.tv_label, label.getLabelName());
                final GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(DisplayUtils.dip2px(context, 2f));
                gradientDrawable.setColor(ColorUtil.parseColor(label.getColorValue()));
                baseViewHolder.getView(R.id.tv_label).setBackground(gradientDrawable);
                baseViewHolder.getView(R.id.checkBox).setSelected(label.getChecked());
                baseViewHolder.getView(R.id.itemView).setOnClickListener(v -> {
                    label.setChecked(!label.getChecked());
                    notifyDataSetChanged();
                });
            }
        };

        //adapter.setList(labelList);
        binding.rvLabelList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvLabelList.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.default_item_decoration_color)));
        binding.rvLabelList.setAdapter(adapter);
        final LabelManageViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(BaseApplication.getInstance()).create(LabelManageViewModel.class);
        viewModel.selectLabelList();
        viewModel.getLabelList().observe(lifecycleOwner, dataBeans -> {
            Log.e(TAG, "showAddLabelDialog: 更新标签数据");
            labelList.clear();
            labelList.addAll(dataBeans);
            final List<LabelListRsp.DataBean> value = scheduleEditViewModel.getLabelListLiveData().getValue();
            if (value != null) {
                for (LabelListRsp.DataBean dataBean : labelList) {
                    if (value.stream().map(LabelListRsp.DataBean::getId).collect(Collectors.toList()).contains(dataBean.getId())) {
                        dataBean.setChecked(true);
                    }
                }
            }
            adapter.setList(labelList);
        });
        binding.tvAdd.setOnClickListener(v -> {
            showLabelCreateScheduleDialog(context, lifecycleOwner, new OnClickListener() {
                @Override
                public void onCancel(View view) {

                }

                @Override
                public void onEnsure(View view) {
                    viewModel.selectLabelList();
                }
            });
        });

        mDialog.setOnShowListener(dialog -> {
            Log.e(TAG, "setOnShowListener: " + dialog.toString());
        });

        mDialog.setOnDismissListener(dialog -> {
            Log.e(TAG, "setOnDismissListener: " + dialog.toString());
        });

        mDialog.setOnKeyListener((dialog, keyCode, event) -> {
            Log.e(TAG, "onKey: " + dialog.toString());
            Log.e(TAG, "onKey: " + keyCode);
            Log.e(TAG, "onKey: " + event.toString());
            return false;
        });
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        //lp.height = DisplayUtils.dip2px(context, 276f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }


    /**
     * 修改重复日程选项的弹框
     */
    public static void showRepetitionScheduleModifyDialog(Context context, OnMenuItemListener onMenuItemListener) {
        DialogRepetitionScheduleModifyBinding binding = DialogRepetitionScheduleModifyBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        AtomicBoolean repetitionType1 = new AtomicBoolean(false);
        AtomicBoolean repetitionType2 = new AtomicBoolean(false);
        AtomicBoolean repetitionType3 = new AtomicBoolean(false);
        binding.ivCancel.setOnClickListener(v -> {
            onMenuItemListener.onMenuItem(-1);
            mDialog.dismiss();
        });
        binding.btnCancel.setOnClickListener(v -> {
            onMenuItemListener.onMenuItem(-1);
            mDialog.dismiss();
        });
        binding.btnEnsure.setOnClickListener(v -> {
            Log.e(TAG, "showRepetitionScheduleModifyDialog: " + repetitionType1.get() + ", " + repetitionType2.get() + ", " + repetitionType3.get());
            if (repetitionType1.get()) {
                onMenuItemListener.onMenuItem(1);
            } else if (repetitionType2.get()) {
                onMenuItemListener.onMenuItem(2);
            } else if (repetitionType3.get()) {
                onMenuItemListener.onMenuItem(0);
            } else {
                ToastUtils.showShort("请选择编辑重复性日程类型");
                return;
            }
            mDialog.dismiss();
        });
        binding.cbRepetitionType1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setCheckboxListener(repetitionType1, repetitionType2, repetitionType3, isChecked, binding.cbRepetitionType2, binding.cbRepetitionType3);

        });
        binding.cbRepetitionType2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setCheckboxListener(repetitionType2, repetitionType1, repetitionType3, isChecked, binding.cbRepetitionType1, binding.cbRepetitionType3);
        });
        binding.cbRepetitionType3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setCheckboxListener(repetitionType3, repetitionType2, repetitionType1, isChecked, binding.cbRepetitionType2, binding.cbRepetitionType1);
        });
        binding.tvRepetitionType1.setOnClickListener(v -> {
            setCheckboxTextListener(repetitionType1, repetitionType2, repetitionType3, binding.cbRepetitionType1, binding.cbRepetitionType2, binding.cbRepetitionType3);
        });
        binding.tvRepetitionType2.setOnClickListener(v -> {
            setCheckboxTextListener(repetitionType2, repetitionType1, repetitionType3, binding.cbRepetitionType2, binding.cbRepetitionType1, binding.cbRepetitionType3);
        });
        binding.tvRepetitionType3.setOnClickListener(v -> {
            setCheckboxTextListener(repetitionType3, repetitionType2, repetitionType1, binding.cbRepetitionType3, binding.cbRepetitionType2, binding.cbRepetitionType1);
        });
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        lp.height = DisplayUtils.dip2px(context, 325f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();

    }


    /**
     * 我发布的and全部作业
     */
    public static void showWorkTypeWorkSelect(Context context, OnClassListener onClassListener,OperationViewModel viewModel) {
        DialogWorkSelectBinding binding = DialogWorkSelectBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        selectBean selectBean1=new selectBean();
        selectBean1.name="我发布的";
        selectBean1.type="1";

        selectBean selectBean2=new selectBean();
        selectBean2.name="全部作业";
        selectBean2.type="0";

        List<selectBean> list=new ArrayList<>();
        list.add(selectBean1);
        list.add(selectBean2);

//        viewModel.getTv1data();

         BaseQuickAdapter adapter2 = new BaseQuickAdapter<selectBean, BaseViewHolder>(R.layout.item_dialog_select1) {

            @Override
            protected void convert(@NonNull BaseViewHolder holder, selectBean item) {
                 ImageView img = (ImageView) holder.getView(R.id.img);
                 TextView classname = (TextView) holder.getView(R.id.classname);
                ConstraintLayout itemView = (ConstraintLayout) holder.getView(R.id.itemView);
                classname.setText(item.name);
//                 img.setVisibility(item.check?View.VISIBLE:View.GONE);

                img.setVisibility(viewModel.getLeftData().getValue().name.equals(item.name)?View.VISIBLE:View.GONE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (selectBean selectBean : list) {
                            selectBean.check=false;
                        }
                        img.setVisibility(View.VISIBLE);
                        item.check=true;
                        onClassListener.onClass(item);
                        viewModel.getLeftData().setValue(item);
                        notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                });
            }
        };
        binding.recy.setLayoutManager(new LinearLayoutManager(context));
        binding.recy.setAdapter(adapter2);
        adapter2.setList(list);

        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels );
        lp.height = DisplayUtils.dip2px(context, 200);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);


        mDialog.setCancelable(true);
        mDialog.show();


    }

    /**
     * 我发布的and全部作业
     */
    public static void showWorkTypeWorkSelect2(Context context, OnPostSubListener postSubListener,List<selectSubjectByUserIdRsp> list,String id) {
        DialogWorkSelect2Binding binding = DialogWorkSelect2Binding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);



        BaseQuickAdapter adapter2 = new BaseQuickAdapter<selectSubjectByUserIdRsp, BaseViewHolder>(R.layout.item_dialog_select1) {

            @Override
            protected void convert(@NonNull BaseViewHolder holder, selectSubjectByUserIdRsp item) {
                ImageView img = (ImageView) holder.getView(R.id.img);
                TextView classname = (TextView) holder.getView(R.id.classname);
                ConstraintLayout itemView = (ConstraintLayout) holder.getView(R.id.itemView);
                classname.setText(item.name);
//                 img.setVisibility(item.check?View.VISIBLE:View.GONE);
                if (id.equals(item.id)){
                    img.setVisibility(View.VISIBLE);
                }else {
                    img.setVisibility(View.GONE);
                }


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (selectSubjectByUserIdRsp selectBean : list) {
                            selectBean.check=false;
                        }
                        img.setVisibility(View.VISIBLE);
                        item.check=true;
                        postSubListener.onSubject(item);
                        notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                });
            }
        };
        binding.recy.setLayoutManager(new LinearLayoutManager(context));
        binding.recy.setAdapter(adapter2);
        adapter2.setList(list);

        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels );
        lp.height = DisplayUtils.dip2px(context, 225f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();

    }
    /**
     * 班级科目选择
     */
    public static void showSuBjectSelect(Context context,  OnSubjectListener onSubjectListener,List<SubjectBean> listbean,OperationViewModel viewModel) {
        DialogWorkSelectBinding binding = DialogWorkSelectBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);

        Log.e(TAG, "showSuBjectSelect: "+JSON.toJSONString(listbean) );

        BaseQuickAdapter adapter2 = new BaseQuickAdapter<SubjectBean, BaseViewHolder>(R.layout.item_dialog_select1) {

            @Override
            protected void convert(@NonNull BaseViewHolder holder, SubjectBean item) {
                ImageView img = (ImageView) holder.getView(R.id.img);
                TextView classname = (TextView) holder.getView(R.id.classname);
                ConstraintLayout itemView = (ConstraintLayout) holder.getView(R.id.itemView);
                classname.setText(item.getName());
                img.setVisibility(item.getChecked()?View.VISIBLE:View.GONE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (SubjectBean subjectBean : listbean) {
                            subjectBean.setChecked(false);
                        }
                        img.setVisibility(View.VISIBLE);
                        item.setChecked(true);
                        onSubjectListener.onSubject(item);
                        viewModel.getTv2data().setValue(item);
                        notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                });
            }
        };
        binding.recy.setLayoutManager(new LinearLayoutManager(context));
//        binding.recy.addItemDecoration(new SpaceItemDecoration(DisplayUtils.dip2px(context, 10f), 6));
        binding.recy.setAdapter(adapter2);
        adapter2.setList(listbean);

        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels );
        lp.height = DisplayUtils.dip2px(context, 325f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();

    }

    /**
     * 教职工关联数据
     */
    public static void showClassSubjecList(Context context,OnSubjectListener onSubjectListener,List<SubjectBean> listbean) {
        DialogWorkSelectBinding binding = DialogWorkSelectBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);

        Log.e(TAG, "showSuBjectSelect: "+JSON.toJSONString(listbean) );

        BaseQuickAdapter adapter2 = new BaseQuickAdapter<SubjectBean, BaseViewHolder>(R.layout.item_dialog_select1) {

            @Override
            protected void convert(@NonNull BaseViewHolder holder, SubjectBean item) {
                ImageView img = (ImageView) holder.getView(R.id.img);
                TextView classname = (TextView) holder.getView(R.id.classname);
                ConstraintLayout itemView = (ConstraintLayout) holder.getView(R.id.itemView);
                classname.setText(item.getName());
                img.setVisibility(item.getChecked()?View.VISIBLE:View.GONE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (SubjectBean subjectBean : listbean) {
                            subjectBean.setChecked(false);
                        }
                        img.setVisibility(View.VISIBLE);
                        item.setChecked(true);
                        onSubjectListener.onSubject(item);

                        notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                });
            }
        };
        binding.recy.setLayoutManager(new LinearLayoutManager(context));
//        binding.recy.addItemDecoration(new SpaceItemDecoration(DisplayUtils.dip2px(context, 10f), 6));
        binding.recy.setAdapter(adapter2);
        adapter2.setList(listbean);

        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels );
        lp.height = DisplayUtils.dip2px(context, 325f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();

    }
    private static void setCheckboxTextListener(AtomicBoolean repetitionType1, AtomicBoolean repetitionType2, AtomicBoolean repetitionType3, CheckBox p, CheckBox p2, CheckBox p3) {
        p.setChecked(!repetitionType1.get());
        if (!repetitionType1.get()) {
            repetitionType2.set(false);
            p2.setChecked(false);
            repetitionType3.set(false);
            p3.setChecked(false);
        }
    }

    private static void setCheckboxListener(AtomicBoolean repetitionType1, AtomicBoolean repetitionType2, AtomicBoolean repetitionType3, boolean isChecked, CheckBox p, CheckBox p2) {
        repetitionType1.set(isChecked);
        if (isChecked) {
            repetitionType2.set(false);
            p.setChecked(false);
            repetitionType3.set(false);
            p2.setChecked(false);
        }
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

    /**
     * 显示人脸采集需用户同意
     *
     * @param context
     * @param onClickListener
     */
    public static void showFaceProtocolDialog(Context context, OnClickListener onClickListener) {
        DialogFaceRecognitionUserProtocolBinding binding = DialogFaceRecognitionUserProtocolBinding.inflate(LayoutInflater.from(context));
        ConstraintLayout rootView = binding.getRoot();
        Dialog mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(rootView);
        binding.tvProtocol.setText(Html.fromHtml("同意 <font color='#11C685'>《一加壹人脸识别用户协议》</font>"));
        binding.btnAgreedOpen.setOnClickListener(v -> {
            if (!binding.checkBox.isChecked()) {
                ToastUtils.showShort("请先阅读并勾选内容!");
                return;
            }
            onClickListener.onEnsure(v);
            mDialog.dismiss();
        });
        binding.tvCancel.setOnClickListener(v -> {
            onClickListener.onCancel(v);
            mDialog.dismiss();
        });
        binding.tvProtocol.setOnClickListener(v -> {
            context.startActivity(new Intent(context, FaceCaptureProtocolActivity.class));
        });
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        //lp.height = DisplayUtils.dip2px(context, 276f);
        rootView.measure(0, 0);
        lp.dimAmount = 0.75f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public interface OnClickListener {
        void onCancel(View view);

        void onEnsure(View view);
    }

    public interface OnScheduleAddListener {
        //完成
        void onFinish(View view);

        //时间
        void onDate(View view);

        //标签
        void onLabel(View view);

        //版本切换
        void onSwitch(View view);
    }

    public interface OnScheduleAddSimpleListener {
        //完成
        void add(View view, DateTime nowTime);
    }

    public interface OnMenuItemListener {
        void onMenuItem(int index);
    }

    public interface OnLabelItemListener {
        void labelItem(List<LabelListRsp.DataBean> labels);
    }
    public interface OnClassListener {
        void onClass(selectBean bean);
    }
    public interface OnSubjectListener {
        void onSubject(SubjectBean bean);
    }

    public interface OnPostSubListener {
        void onSubject(selectSubjectByUserIdRsp bean);
    }
    /**
     * 判断字符串是否含有Emoji表情
     **/
    public static boolean isHasEmoji(String reviewerName) {
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(reviewerName);
        return matcher.find();
    }
}
