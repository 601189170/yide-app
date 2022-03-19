package com.yyide.chatim.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.TableItemAdapter;
import com.yyide.chatim.adapter.TableSectionAdapter;
import com.yyide.chatim.adapter.TableTimeAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.dialog.SwitchTableClassPop;
import com.yyide.chatim.dialog.SwitchClassPopNew;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.model.sitetable.SiteTableRsp;
import com.yyide.chatim.presenter.ClassTablePresenter;
import com.yyide.chatim.view.ClassTableView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;


public class ClassTableFragment extends BaseMvpFragment<ClassTablePresenter> implements ClassTableView, SwitchTableClassPop.SelectClasses {

    @BindView(R.id.grid)
    GridView grid;
    @BindView(R.id.tablegrid)
    GridView tablegrid;
    @BindView(R.id.listsection)
    GridView listsection;
    @BindView(R.id.left_layout)
    RelativeLayout leftLayout;
    @BindView(R.id.className)
    TextView className;
    @BindView(R.id.tv_week)
    TextView tv_week;
    @BindView(R.id.classlayout)
    FrameLayout classlayout;
    @BindView(R.id.empty)
    View empty;
    @BindView(R.id.svContent)
    ScrollView mScrollView;
    private View mBaseView;
    TableTimeAdapter timeAdapter;
    TableItemAdapter tableItemAdapter;
    TableSectionAdapter tableSectionAdapter;
    int index = -1;
    private List<SelectTableClassesRsp.DataBean> data;
    private GetUserSchoolRsp.DataBean.FormBean classInfo;
    private SwitchTableClassPop swichTableClassPop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.class_table_fragmnet2, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tableItemAdapter = new TableItemAdapter();
        tablegrid.setAdapter(tableItemAdapter);
        tableSectionAdapter = new TableSectionAdapter();
        listsection.setAdapter(tableSectionAdapter);
        TextView tvDesc = empty.findViewById(R.id.tv_desc);
        tvDesc.setText("本周暂无课表数据");
        timeAdapter = new TableTimeAdapter();
        grid.setAdapter(timeAdapter);
//        if (SpData.getIdentityInfo().weekNum <= 0) {
//            tv_week.setText("");
//        } else {
//            tv_week.setText(getString(R.string.weekNum, SpData.getIdentityInfo().weekNum + ""));
//        }
        grid.setOnItemClickListener((parent, view1, position, id) -> {
            timeAdapter.setPosition(position);
            index = position;
            tableItemAdapter.setIndex(index);
        });
        tablegrid.setOnItemClickListener((parent, view12, position, id) -> {
            index = position % 7;
            tableItemAdapter.setIndex(index);
            timeAdapter.setPosition(index);
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        for (int i = 0; i < timeAdapter.list.size(); i++) {
            if (timeAdapter.list.get(i).day.equals(simpleDateFormat.format(date))) {
                timeAdapter.setPosition(i);
                timeAdapter.setToday(i);
                tableItemAdapter.setIndex(i);
            }
        }
        mvpPresenter.listAllBySchoolId();
        classInfo = SpData.getClassInfo();
//        if (classInfo != null) {
//            className.setText(classInfo.classesName);
//            mvpPresenter.listTimeDataByApp(classInfo.classesId);
//        } else {
//            className.setText("暂无班级");
//        }
        //暂时使用固定班级id测试
        mvpPresenter.listTimeDataByApp("1491675357620822017");
//        GetUserSchoolRsp.DataBean identityInfo = SpData.getIdentityInfo();
//        if (identityInfo != null) {
//            if ("Y".equalsIgnoreCase(identityInfo.schoolType)) {
//                mvpPresenter.selectClassByAllSchool();
//            } else if ("N".equalsIgnoreCase(identityInfo.schoolType)) {
//                mvpPresenter.selectListBySchoolAll();
//            }
//        }
    }

    @OnClick(R.id.classlayout)
    public void click() {
        if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().staffIdentity()) {
            if (classInfo != null) {
                SwitchClassPopNew classPopNew = new SwitchClassPopNew(getActivity(), classInfo);
                classPopNew.setOnCheckCallBack(classBean -> {
                    this.classInfo = classBean;
                    className.setText(classBean.classesName);
//                    mvpPresenter.listTimeDataByApp(classBean.classesId);
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
        }
    }

    @Override
    protected ClassTablePresenter createPresenter() {
        return new ClassTablePresenter(this);
    }

    @Override
    public void listAllBySchoolId(listAllBySchoolIdRsp rsp) {
        Log.e("TAG", "listAllBySchoolId==>: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCESS) {

        }
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
                empty.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
                //String jc = rsp.data.timetableStructure;
                //String s = jc.replaceAll("节课", "");
                //int num = Integer.parseInt(s);
                final int thisWeek = rsp.getData().getThisWeek();
                tv_week.setText(getString(R.string.weekNum, thisWeek + ""));
                final SiteTableRsp.DataBean.SectionListBean sectionList = rsp.getData().getSectionList();
                int earlyReading = sectionList.getEarlySelfStudyList() != null ? sectionList.getEarlySelfStudyList().size() : 0;
                int morning = sectionList.getMorningList() != null ? sectionList.getMorningList().size() : 0;
                int afternoon = sectionList.getAfternoonList() != null ? sectionList.getAfternoonList().size() : 0;
                int night = sectionList.getNightList() != null ? sectionList.getNightList().size() : 0;
                int eveningStudy = sectionList.getLateSelfStudyList() != null ? sectionList.getLateSelfStudyList().size() : 0;
                int sectionCount = earlyReading + morning + afternoon + night + eveningStudy;
                List<String> sectionlist = new ArrayList<>();
                if (rsp.getData().getTimetableList() != null && rsp.getData().getTimetableList().size() > 0) {
                    List<SiteTableRsp.DataBean.TimetableListBean> subListBeans = new ArrayList<>();
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
                    tableItemAdapter.notifyData(subListBeans);
                }

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
                    createLeftTypeView(morning + afternoon + earlyReading, 4, eveningStudy);//晚上
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
                mScrollView.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
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
        leftLayout.addView(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void listTimeDataByAppFail(String msg) {
        Log.d("selectTableClassListSuccess", msg);
    }

    @Override
    public void selectTableClassListSuccess(SelectTableClassesRsp model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCESS) {
            data = model.getData();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void selectTableClassListFail(String msg) {
    }

    @Override
    public void OnSelectClassesListener(long classesId, String classesName) {
        className.setText(classesName);
        //mvpPresenter.listTimeDataByApp(classesId + "");
    }
}
