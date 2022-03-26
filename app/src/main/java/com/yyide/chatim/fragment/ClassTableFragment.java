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
import com.yyide.chatim.dialog.SwitchTableClassPop;
import com.yyide.chatim.dialog.TableClassPopUp;
import com.yyide.chatim.dialog.TablePopUp;
import com.yyide.chatim.dialog.TextPopUp;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.model.sitetable.SiteTableRsp;
import com.yyide.chatim.model.table.ChildrenItem;
import com.yyide.chatim.model.table.ClassInfoBean;
import com.yyide.chatim.presenter.ClassTablePresenter;
import com.yyide.chatim.utils.TimeUtil;
import com.yyide.chatim.view.ClassTableView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ClassTableFragment extends BaseMvpFragment<ClassTablePresenter> implements ClassTableView {


    TableTimeAdapter timeAdapter;
    TableItemAdapter tableItemAdapter;
    TableSectionAdapter tableSectionAdapter;
    int index = -1;
    //private List<SelectTableClassesRsp.DataBean> data;
    //private GetUserSchoolRsp.DataBean.FormBean classInfo;
    //private SwitchTableClassPop swichTableClassPop;

    private ClassTableFragmnet2Binding binding;
    // 当前所选周数
    private ChildrenItem selectWeek;
    // 班级列表
    List<ChildrenItem> classList = new ArrayList<>();
    // 当前所选班级
    ChildrenItem selectClassInfo = new ChildrenItem();

    private TablePopUp weekPopUp;
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

        binding.tableClassTop.tableTopWeekTv.setVisibility(View.VISIBLE);

        tableItemAdapter = new TableItemAdapter();
        tableItemAdapter.setOnItemClickListener((view13, content) -> {
            textPopUp.setText(content,view13);
        });
        binding.tablegrid.setAdapter(tableItemAdapter);

        tableSectionAdapter = new TableSectionAdapter();
        binding.listsection.setAdapter(tableSectionAdapter);
        TextView tvDesc = binding.empty.tvDesc;
        tvDesc.setText("本周暂无课表数据");
        timeAdapter = new TableTimeAdapter();
        binding.tableClassTop.grid.setAdapter(timeAdapter);
//        if (SpData.getIdentityInfo().weekNum <= 0) {
//            tv_week.setText("");
//        } else {
//            tv_week.setText(getString(R.string.weekNum, SpData.getIdentityInfo().weekNum + ""));
//        }
        binding.tableClassTop.grid.setOnItemClickListener((parent, view1, position, id) -> {
            timeAdapter.setPosition(position);
            index = position;
            tableItemAdapter.setIndex(index);
        });
        binding.tablegrid.setOnItemClickListener((parent, view12, position, id) -> {
            index = position % 7;
            tableItemAdapter.setIndex(index);
            timeAdapter.setPosition(index);
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        for (int i = 0; i < timeAdapter.list.size(); i++) {
            if (timeAdapter.list.get(i).day.equals(simpleDateFormat.format(date))) {
                timeAdapter.setPosition(i);
                timeAdapter.setToday(i);
                tableItemAdapter.setIndex(i);
            }
        }

        mvpPresenter.listAllBySchoolId();
        /*
        classInfo = SpData.getClassInfo();
        if (classInfo != null) {
            binding.tableClassTop.className.setText(classInfo.classesName);
            mvpPresenter.listTimeDataByApp(classInfo.classesId,selectWeek);
        } else {
            binding.tableClassTop.className.setText("暂无班级");
        }*/

        //暂时使用固定班级id测试
        //mvpPresenter.listTimeDataByApp("1491675357620822017",selectWeek);

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

    private void initView() {
        weekPopUp = new TablePopUp(this);
        weekPopUp.setPopupGravity(Gravity.BOTTOM);
        weekPopUp.setSubmitCallBack(data -> {
            if (data != null) {
                selectWeek = data;
                binding.tableClassTop.tableTopWeekTv.setText(selectWeek.getName());
                mvpPresenter.listTimeDataByApp(selectClassInfo.getId(), selectWeek.getId());
            }
        });

        classPopUp = new TableClassPopUp(this);
        classPopUp.setPopupGravity(Gravity.BOTTOM);
        classPopUp.setSubmitCallBack(data -> {
            if (data != null) {
                selectClassInfo = data;
                String showName = selectClassInfo.getParentName() + selectClassInfo.getName();
                binding.tableClassTop.className.setText(showName);
                mvpPresenter.listTimeDataByApp(selectClassInfo.getId(), selectWeek.getId());
            }
        });

        textPopUp = new TextPopUp(this);
    }

    public void initClickListener() {
        binding.tableClassTop.classlayout.setOnClickListener(v -> {
            /*if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().staffIdentity()) {
                if (classInfo != null) {
                    SwitchClassPopNew classPopNew = new SwitchClassPopNew(getActivity(), classInfo);
                    classPopNew.setOnCheckCallBack(classBean -> {
                        this.classInfo = classBean;
                        className.setText(classBean.classesName);
                        mvpPresenter.listTimeDataByApp(classBean.classesId);
                    });
                } else {
                    ToastUtils.showShort("您没有其他班级");
                }
            } else {//校长取全校班级列表
                if (data != null && data.size() > 0) {
                    swichTableClassPop = new SwitchTableClassPop(getActivity(), data);
                    swichTableClassPop.setSelectClasses(this);
                } else {
                    ToastUtils.showShort("您没有其他班级");
                }
            }*/
            if (classPopUp.isShowing()) {
                classPopUp.dismiss();
            } else {
                classPopUp.showPopupWindow(v);
            }
        });


        binding.tableClassTop.tableTopWeekTv.setOnClickListener(v -> {
            if (weekPopUp.isShowing()) {
                weekPopUp.dismiss();
            } else {
                weekPopUp.showPopupWindow(v);
            }
        });


        binding.tableClassReturnCurrent.setOnClickListener(v -> {
            index = -1;
            selectWeek = null;
            mvpPresenter.listTimeDataByApp(selectClassInfo.getId(), null);
        });
    }


    @Override
    protected ClassTablePresenter createPresenter() {
        return new ClassTablePresenter(this);
    }

    @Override
    public void listAllBySchoolId(List<ClassInfoBean> classInfo) {
        Log.e("TAG", "listAllBySchoolId==>: " + JSON.toJSONString(classInfo));
        classList.clear();
        if (classInfo.isEmpty()) {
            binding.tableClassTop.className.setText("暂无班级");
            return;
        }
        for (int i = 0; i < classInfo.size(); i++) {
            List<ChildrenItem> childrenData = classInfo.get(i).getChildren();
            String outerName = classInfo.get(i).getName();
            for (int j = 0; j < childrenData.size(); j++) {
                /*String name = childrenData.get(j).getName();
                childrenData.get(j).setName(outerName + name);*/
                childrenData.get(j).setParentName(outerName);
            }
            classList.addAll(childrenData);
        }
        selectClassInfo = classList.get(0);
        classPopUp.setData(classList, selectClassInfo);
        String showName = selectClassInfo.getParentName() + selectClassInfo.getName();
        binding.tableClassTop.className.setText(showName);
        mvpPresenter.listTimeDataByApp(selectClassInfo.getId(), null);
    }

    @Override
    public void listAllBySchoolIdFail(String msg) {
        Log.e("TAG", "listAllBySchoolId==>: " + msg);
    }

    @Override
    public void listTimeDataByApp(SiteTableRsp rsp) {
        Log.e("TAG", "listTimeDataByApp==>: " + JSON.toJSONString(rsp));
        if (rsp.getCode() == 0) {
            if (rsp.getData() != null) {
                binding.empty.getRoot().setVisibility(View.GONE);
                binding.svContent.setVisibility(View.VISIBLE);
                //String jc = rsp.data.timetableStructure;
                //String s = jc.replaceAll("节课", "");
                //int num = Integer.parseInt(s);
                final int thisWeek = rsp.getData().getThisWeek();
                // 设置总周数
                List<ChildrenItem> data = new ArrayList<>();
                for (int i = 0; i < rsp.getData().getWeekTotal(); i++) {
                    String weekNum = String.valueOf(i + 1);
                    ChildrenItem bean = new ChildrenItem( "第" + weekNum + "周","",weekNum);
                    data.add(bean);
                }
                selectWeek = data.get(thisWeek - 1);
                weekPopUp.setData(data, selectWeek);
                binding.tableClassTop.tableTopWeekTv.setText(selectWeek.getName());
                if (rsp.getData().getWeekList() != null) {
                    List<TimeUtil.WeekDay> toWeekDayList = TimeUtil.getWeekDay(rsp.getData().getWeekList());
                    timeAdapter.notifyData(toWeekDayList);
                }
                final SiteTableRsp.DataBean.SectionListBean sectionList = rsp.getData().getSectionList();
                int earlyReading = sectionList.getEarlySelfStudyList() != null ? sectionList.getEarlySelfStudyList().size() : 0;
                int morning = sectionList.getMorningList() != null ? sectionList.getMorningList().size() : 0;
                int afternoon = sectionList.getAfternoonList() != null ? sectionList.getAfternoonList().size() : 0;
                int night = sectionList.getNightList() != null ? sectionList.getNightList().size() : 0;
                int eveningStudy = sectionList.getLateSelfStudyList() != null ? sectionList.getLateSelfStudyList().size() : 0;
                int sectionCount = earlyReading + morning + afternoon + night + eveningStudy;

                // 数量为0
                if (sectionCount == 0){
                    binding.svContent.setVisibility(View.GONE);
                    binding.empty.getRoot().setVisibility(View.VISIBLE);
                    return;
                }


                List<String> sectionlist = new ArrayList<>();
                List<SiteTableRsp.DataBean.TimetableListBean> subListBeans = new ArrayList<>();
                if (rsp.getData().getTimetableList() != null && rsp.getData().getTimetableList().size() > 0) {
                    //val courseBoxCount = sectionCount * 7
                    //            for (index in 0 until courseBoxCount) {
                    //                val listBean = SiteTableRsp.DataBean.TimetableListBean()
                    //                courseList.add(listBean)
                    //                it.timetableList?.forEach {
                    //                    //if ((it.skxq - 1) == index % 7 && (it.xh) == ((index % sectionCount)+1)) {
                    //                    if (index == ((it.section - 1) * 7 + it.week % 7 - 1)) {
                    //                        courseList[index] = it
                    //                        return@forEach
                    //                    }
                    //                }
                    //            }
                    int courseBoxCount = sectionCount * 7;
                    for (int i = 0; i < courseBoxCount; i++) {
                        final SiteTableRsp.DataBean.TimetableListBean listBean = new SiteTableRsp.DataBean.TimetableListBean();
                        subListBeans.add(listBean);
                        final List<SiteTableRsp.DataBean.TimetableListBean> timetableList = rsp.getData().getTimetableList();
                        for (int j = 0; j < timetableList.size(); j++) {
                            final SiteTableRsp.DataBean.TimetableListBean timetableListBean = timetableList.get(j);
                            if (i == ((timetableListBean.getSection() - 1) * 7 + timetableListBean.getWeek() % 7 - 1)) {
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
    }
}
