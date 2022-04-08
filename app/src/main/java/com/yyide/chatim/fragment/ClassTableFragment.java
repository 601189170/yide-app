package com.yyide.chatim.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.TableItemAdapter;
import com.yyide.chatim.adapter.TableSectionAdapter;
import com.yyide.chatim.adapter.TableTimeAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.ClassTableFragmnet2Binding;
import com.yyide.chatim.dialog.TableClassPopUp;
import com.yyide.chatim.dialog.TableWeekPopUp;
import com.yyide.chatim.dialog.TextPopUp;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.model.sitetable.SiteTableRsp;
import com.yyide.chatim.model.table.ChildrenItem;
import com.yyide.chatim.model.table.ClassInfo;
import com.yyide.chatim.model.table.ClassInfoBean;
import com.yyide.chatim.presenter.ClassTablePresenter;
import com.yyide.chatim.utils.TimeUtil;
import com.yyide.chatim.view.ClassTableView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import razerdp.basepopup.BasePopupWindow;


public class ClassTableFragment extends BaseMvpFragment<ClassTablePresenter> implements ClassTableView {


    TableTimeAdapter timeAdapter;
    TableItemAdapter tableItemAdapter;
    TableSectionAdapter tableSectionAdapter;
    int index = -1;
    //private List<SelectTableClassesRsp.DataBean> data;
    //private GetUserSchoolRsp.DataBean.FormBean classInfo;
    //private SwitchTableClassPop swichTableClassPop;

    private boolean first = true;
    private int thisWeek = 0;

    private ClassTableFragmnet2Binding binding;
    // 当前所选周数
    private ChildrenItem selectWeek = new ChildrenItem();
    // 班级列表
    //List<ClassInfo> classList = new ArrayList<>();
    // 当前所选班级
    ClassInfo selectClassInfo = new ClassInfo();

    private TableWeekPopUp weekPopUp;
    private TableClassPopUp classPopUp;
    private TextPopUp textPopUp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = ClassTableFragmnet2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();


        tableItemAdapter = new TableItemAdapter();
        tableItemAdapter.setOnItemClickListener((view13, content) -> {
            textPopUp.setText(content, view13);
        });
        binding.tablegrid.setAdapter(tableItemAdapter);

        tableSectionAdapter = new TableSectionAdapter();
        binding.listsection.setAdapter(tableSectionAdapter);
        TextView tvDesc = binding.empty.tvDesc;
        tvDesc.setText("本周暂无课表数据");
        timeAdapter = new TableTimeAdapter();
        timeAdapter.setOnItemClickListener((view12, position) -> {
            timeAdapter.setPosition(position);
            index = position;
            tableItemAdapter.setIndex(index);
        });
        binding.tableClassTop.grid.setAdapter(timeAdapter);

        /*binding.tableClassTop.grid.setOnItemClickListener((parent, view1, position, id) -> {
            Log.d("grid click", "onViewCreated: class click");
            timeAdapter.setPosition(position);
            index = position;
            tableItemAdapter.setIndex(index);
        });*/

        binding.tablegrid.setOnItemClickListener((parent, view12, position, id) -> {
            index = position % 7;
            tableItemAdapter.setIndex(index);
            timeAdapter.setPosition(index);
        });

        setToday();

        mvpPresenter.listAllBySchoolId();

//        GetUserSchoolRsp.DataBean identityInfo = SpData.getIdentityInfo();
//        if (identityInfo != null) {
//            if ("Y".equalsIgnoreCase(identityInfo.schoolType)) {
//                mvpPresenter.selectClassByAllSchool();
//            } else if ("N".equalsIgnoreCase(identityInfo.schoolType)) {
//                mvpPresenter.selectListBySchoolAll();
//            }
//        }

        initClickListener();
    }

    private void setToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        for (int i = 0; i < timeAdapter.list.size(); i++) {
            if (timeAdapter.list.get(i).day.equals(simpleDateFormat.format(date))) {
                timeAdapter.setPosition(i);
                timeAdapter.setToday(i);
                tableItemAdapter.setIndex(i);
            }
        }
    }

    private void initView() {

        binding.empty.tvDesc.setText("本周暂无课表数据");

        weekPopUp = new TableWeekPopUp(this);
        weekPopUp.setPopupGravity(Gravity.BOTTOM);
        weekPopUp.setSubmitCallBack(data -> {
            if (data != null) {
                selectWeek = data;
                binding.tableClassTop.tableTopWeekTv.setText(selectWeek.getName());
                if (!selectClassInfo.getId().equals("")) {
                    mvpPresenter.listTimeDataByApp(selectClassInfo.getId(), selectWeek.getId());
                } else {
                    mvpPresenter.listTimeDataByApp("0", selectWeek.getId());
                }
            }
        });

        classPopUp = new TableClassPopUp(this);
        classPopUp.setPopupGravity(Gravity.BOTTOM);
        classPopUp.setSubmitCallBack(data -> {
            selectClassInfo = data;
            String showName = selectClassInfo.getParentName() + selectClassInfo.getName();
            binding.tableClassTop.className.setText(showName);
            if (!selectClassInfo.getId().equals("")) {
                mvpPresenter.listTimeDataByApp(selectClassInfo.getId(), selectWeek.getId());
            }
        });

        weekPopUp.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                binding.tableClassTop.tableTopWeekTv.setTextColor(0xFF666666);
                binding.tableClassTop.tableTopWeekLogo.setImageResource(R.mipmap.table_week_button);
            }
        });

        classPopUp.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                binding.tableClassTop.className.setTextColor(0xFF666666);
                binding.tableClassTop.classNameLogo.setImageResource(R.mipmap.table_week_button);
            }
        });

        textPopUp = new TextPopUp(this);
    }

    public void initClickListener() {
        binding.tableClassTop.className.setOnClickListener(v -> {
            if (classPopUp.isShowing()) {
                classPopUp.dismiss();
            } else {
                binding.tableClassTop.className.setTextColor(0xFF11C685);
                binding.tableClassTop.classNameLogo.setImageResource(R.mipmap.table_week_button_pack);
                classPopUp.showPopupWindow(v);
            }
        });

        binding.tableClassTop.classNameLogo.setOnClickListener(v -> {
            if (classPopUp.isShowing()) {
                classPopUp.dismiss();
            } else {
                binding.tableClassTop.className.setTextColor(0xFF11C685);
                binding.tableClassTop.classNameLogo.setImageResource(R.mipmap.table_week_button_pack);
                classPopUp.showPopupWindow(v);
            }
        });


        binding.tableClassTop.tableTopWeekTv.setOnClickListener(v -> {
            if (weekPopUp.isShowing()) {
                weekPopUp.dismiss();
            } else {
                binding.tableClassTop.tableTopWeekTv.setTextColor(0xFF11C685);
                binding.tableClassTop.tableTopWeekLogo.setImageResource(R.mipmap.table_week_button_pack);
                weekPopUp.showPopupWindow(v);
            }
        });

        binding.tableClassTop.tableTopWeekLogo.setOnClickListener(v -> {
            if (weekPopUp.isShowing()) {
                weekPopUp.dismiss();
            } else {
                binding.tableClassTop.tableTopWeekTv.setTextColor(0xFF11C685);
                binding.tableClassTop.tableTopWeekLogo.setImageResource(R.mipmap.table_week_button_pack);
                weekPopUp.showPopupWindow(v);
            }
        });

        // 下拉刷新
        binding.slContent.setOnRefreshListener(() -> {
            if (!selectClassInfo.getId().equals("")) {
                mvpPresenter.listTimeDataByApp(selectClassInfo.getId(), selectWeek.getId());
            } else {
                mvpPresenter.listTimeDataByApp("0", selectWeek.getId());
            }
        });
        binding.slContent.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));


        binding.tableClassReturnCurrent.setOnClickListener(v -> {
            index = -1;
            selectWeek = new ChildrenItem();
            first = true;
            if (!selectClassInfo.getId().equals("")) {
                mvpPresenter.listTimeDataByApp(selectClassInfo.getId(), null);
            } else {
                mvpPresenter.listTimeDataByApp("0", null);
            }
        });
    }


    @Override
    protected ClassTablePresenter createPresenter() {
        return new ClassTablePresenter(this);
    }

    @Override
    public void listAllBySchoolId(List<ClassInfoBean> classInfo) {
        Log.e("TAG", "listAllBySchoolId==>: " + JSON.toJSONString(classInfo));
        //classList.clear();
        if (classInfo.isEmpty()) {
            binding.tableClassTop.className.setEnabled(false);
            binding.tableClassTop.className.setText("暂无班级");
            binding.tableClassTop.classNameLogo.setVisibility(View.INVISIBLE);
            mvpPresenter.listTimeDataByApp("0", null);
            return;
        }

        List<ChildrenItem> firstChildren = classInfo.get(0).getChildren();
        if (firstChildren.isEmpty()) {
            binding.tableClassTop.className.setEnabled(false);
            binding.tableClassTop.className.setText("暂无班级");
            binding.tableClassTop.classNameLogo.setVisibility(View.INVISIBLE);
            mvpPresenter.listTimeDataByApp("0", null);
            return;
        }


        binding.tableClassTop.className.setEnabled(true);
        binding.tableClassTop.classNameLogo.setVisibility(View.VISIBLE);

        /*for (int i = 0; i < classInfo.size(); i++) {
            List<ChildrenItem> childrenData = classInfo.get(i).getChildren();
            String outerName = classInfo.get(i).getName();
            for (int j = 0; j < childrenData.size(); j++) {
                childrenData.get(j).setParentName(outerName);
            }
            classList.addAll(childrenData);
        }*/

        ClassInfo firstData = new ClassInfo();
        firstData.setParentId(classInfo.get(0).getId());
        firstData.setParentName(classInfo.get(0).getName());
        firstData.setId(firstChildren.get(0).getId());
        firstData.setName(firstChildren.get(0).getName());
        selectClassInfo = firstData;
        classPopUp.setData(classInfo, selectClassInfo);

        String showName = selectClassInfo.getParentName() + selectClassInfo.getName();
        binding.tableClassTop.className.setText(showName);
        if (!selectClassInfo.getId().equals("")) {
            mvpPresenter.listTimeDataByApp(selectClassInfo.getId(), null);
        }
    }

    @Override
    public void listAllBySchoolIdFail(String msg) {
        Log.e("TAG", "listAllBySchoolId==>: " + msg);
        binding.tableClassTop.className.setEnabled(false);
        binding.tableClassTop.className.setText("暂无班级");
        binding.tableClassTop.classNameLogo.setVisibility(View.INVISIBLE);
        mvpPresenter.listTimeDataByApp("0", null);
    }

    @Override
    public void listTimeDataByApp(SiteTableRsp rsp) {
        Log.e("TAG", "listTimeDataByApp==>: " + JSON.toJSONString(rsp));
        binding.slContent.setRefreshing(false);
        if (rsp.getCode() == 0) {
            if (rsp.getData() != null) {
                binding.empty.getRoot().setVisibility(View.GONE);
                binding.svContent.setVisibility(View.VISIBLE);

                if (rsp.getData().getWeekTotal() != 0) {
                    binding.tableClassTop.tableTopWeekTv.setVisibility(View.VISIBLE);
                    binding.tableClassTop.tableTopWeekLogo.setVisibility(View.VISIBLE);
                }

                final int getThisWeek = rsp.getData().getThisWeek();
                if (first) {
                    thisWeek = getThisWeek;
                    first = false;
                }

                if (getThisWeek == thisWeek) {
                    binding.tableClassReturnCurrent.setVisibility(View.INVISIBLE);
                } else {
                    binding.tableClassReturnCurrent.setVisibility(View.VISIBLE);
                }


                // 设置总周数
                List<ChildrenItem> data = new ArrayList<>();
                for (int i = 0; i < rsp.getData().getWeekTotal(); i++) {
                    String weekNum = String.valueOf(i + 1);
                    ChildrenItem bean = new ChildrenItem("第" + weekNum + "周", "", weekNum);
                    data.add(bean);
                }
                if (getThisWeek == 0) {
                    binding.tableClassTop.tableTopWeekTv.setText("选择周次");
                }else {
                    selectWeek = data.get(getThisWeek - 1);
                    binding.tableClassTop.tableTopWeekTv.setText(selectWeek.getName());
                }
                weekPopUp.setData(data, selectWeek);

                if (rsp.getData().getWeekList() != null) {
                    List<TimeUtil.WeekDay> toWeekDayList = TimeUtil.getWeekDay(rsp.getData().getWeekList());
                    timeAdapter.notifyData(toWeekDayList);
                    setToday();
                }
                final SiteTableRsp.DataBean.SectionListBean sectionList = rsp.getData().getSectionList();
                int earlyReading = sectionList.getEarlySelfStudyList() != null ? sectionList.getEarlySelfStudyList().size() : 0;
                int morning = sectionList.getMorningList() != null ? sectionList.getMorningList().size() : 0;
                int afternoon = sectionList.getAfternoonList() != null ? sectionList.getAfternoonList().size() : 0;
                int night = sectionList.getNightList() != null ? sectionList.getNightList().size() : 0;
                int eveningStudy = sectionList.getLateSelfStudyList() != null ? sectionList.getLateSelfStudyList().size() : 0;
                int sectionCount = earlyReading + morning + afternoon + night + eveningStudy;

                // 数量为0
                if (sectionCount == 0) {
                    binding.svContent.setVisibility(View.GONE);
                    binding.empty.getRoot().setVisibility(View.VISIBLE);
                    return;
                }


                List<String> sectionlist = new ArrayList<>();
                List<SiteTableRsp.DataBean.TimetableListBean> subListBeans = new ArrayList<>();
                if (rsp.getData().getTimetableList() != null && rsp.getData().getTimetableList().size() > 0) {
                    int courseBoxCount = sectionCount * 7;
                    for (int i = 0; i < courseBoxCount; i++) {
                        final SiteTableRsp.DataBean.TimetableListBean listBean = new SiteTableRsp.DataBean.TimetableListBean();
                        subListBeans.add(listBean);
                        final List<SiteTableRsp.DataBean.TimetableListBean> timetableList = rsp.getData().getTimetableList();
                        for (int j = 0; j < timetableList.size(); j++) {
                            final SiteTableRsp.DataBean.TimetableListBean timetableListBean = timetableList.get(j);
                            int week = timetableListBean.getWeek();
                            int column = week - 1;
                            /*
                            int column = timetableListBean.getWeek() % 7 - 1;
                            if (week == 7){
                                column = timetableListBean.getWeek() % 7 - 1 + 7;
                            }*/
                            if (i == ((timetableListBean.getSection() - 1) * 7 + column)) {
                                subListBeans.set(i, timetableListBean);
                                break;
                            }
                        }
                    }
                }
                tableItemAdapter.notifyData(subListBeans);

                if (earlyReading > 0) {
                    createLeftTypeView(0, 1, earlyReading);//早读
                }
                if (morning > 0) {
                    createLeftTypeView(earlyReading, 2, morning);//上午
                }
                if (afternoon > 0) {
                    createLeftTypeView(earlyReading + morning, 3, afternoon);//下午
                }
                if (night > 0) {
                    createLeftTypeView(morning + afternoon + earlyReading, 4, night);//晚上
                }
                if (eveningStudy > 0) {
                    createLeftTypeView(morning + afternoon + earlyReading + night, 5, eveningStudy);//晚自习
                }
//                for (int i = 0; i < sectionCount; i++) {
//                    if (earlyReading > 0 && i == 0) {
//                        sectionlist.add("早读");
//                    } else {
//                        if (earlyReading > 0) {
//                            sectionlist.add(i + "");
//                        } else {
//                            sectionlist.add(i + 1 + "");
//                        }
//                    }
//                }
                for (int i = 0; i < sectionList.getEarlySelfStudyList().size(); i++) {
                    final SiteTableRsp.DataBean.SectionListBean.ListBean listBean = sectionList.getEarlySelfStudyList().get(i);
                    sectionlist.add(listBean.getName());
                }
                for (int i = 0; i < sectionList.getMorningList().size(); i++) {
                    final SiteTableRsp.DataBean.SectionListBean.ListBean listBean = sectionList.getMorningList().get(i);
                    sectionlist.add(listBean.getName());
                }
                for (int i = 0; i < sectionList.getAfternoonList().size(); i++) {
                    final SiteTableRsp.DataBean.SectionListBean.ListBean listBean = sectionList.getAfternoonList().get(i);
                    sectionlist.add(listBean.getName());
                }
                for (int i = 0; i < sectionList.getNightList().size(); i++) {
                    final SiteTableRsp.DataBean.SectionListBean.ListBean listBean = sectionList.getNightList().get(i);
                    sectionlist.add(listBean.getName());
                }
                for (int i = 0; i < sectionList.getLateSelfStudyList().size(); i++) {
                    final SiteTableRsp.DataBean.SectionListBean.ListBean listBean = sectionList.getLateSelfStudyList().get(i);
                    sectionlist.add(listBean.getName());
                }
                tableSectionAdapter.notifyData(sectionlist);

            } else {
                binding.svContent.setVisibility(View.GONE);
                binding.empty.getRoot().setVisibility(View.VISIBLE);
            }
        }
    }

    //创建"第上下午"视图
    private void createLeftTypeView(int selection, int type, int length) {

//        int CouseHeight = SizeUtils.dp2px(80) + 1;
        int CouseHeight = getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_75) + 1;
        int CouseWith = SizeUtils.dp2px(30);

        View view = LayoutInflater.from(mActivity).inflate(R.layout.course_card_type2, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CouseWith, CouseHeight * length); //设置布局高度,即跨多少节课
        TextView text = view.findViewById(R.id.text_view);
        text.setText("");
        switch (type) {
            case 1:
                view.setY(CouseHeight * selection);
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type1));
                text.setText("早\n读");
                break;
            case 2:
                view.setY((CouseHeight * selection) + SizeUtils.dp2px(1));
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type2));
                text.setText("上\n午");
                break;
            case 3:
                view.setY((CouseHeight * selection) + SizeUtils.dp2px(2));
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type3));
                text.setText("下\n午");
                break;
            case 4:
                view.setY((CouseHeight * selection) + SizeUtils.dp2px(3));
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type5));
                text.setText("晚\n上");
                break;
            case 5:
                view.setY((CouseHeight * selection) + SizeUtils.dp2px(4));
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type4));
                text.setText("晚\n自\n习");
                break;
        }
        params.gravity = Gravity.CENTER;
        text.setLayoutParams(params);
        binding.leftLayout.addView(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void listTimeDataByAppFail(String msg) {
        Log.d("selectTableClassListSuccess", msg);
        binding.slContent.setRefreshing(false);
    }

    @Override
    public void selectTableClassListSuccess(SelectTableClassesRsp model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCESS) {
            //data = model.getData();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void selectTableClassListFail(String msg) {
    }

    /*@Override
    public void OnSelectClassesListener(long classesId, String classesName) {
        binding.tableClassTop.className.setText(classesName);
        mvpPresenter.listTimeDataByApp(classesId + "",selectWeek);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        weekPopUp.setSubmitCallBack(null);
        classPopUp.setSubmitCallBack(null);
        tableItemAdapter.setOnItemClickListener(null);
        timeAdapter.setOnItemClickListener(null);
    }
}
