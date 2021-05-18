package com.yyide.chatim.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.TableAdapter;
import com.yyide.chatim.adapter.TableItemAdapter;
import com.yyide.chatim.adapter.TableSectionAdapter;
import com.yyide.chatim.adapter.TableTimeAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.dialog.SwichTableClassPop;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.model.TableRsp;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.model.listTimeDataByAppRsp;
import com.yyide.chatim.presenter.ClassTablePresenter;
import com.yyide.chatim.utils.TimeUtil;
import com.yyide.chatim.view.ClassTableView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;


public class ClassTableFragment extends BaseMvpFragment<ClassTablePresenter> implements ClassTableView, SwichTableClassPop.SelectClasses {

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
    private View mBaseView;
    TableTimeAdapter timeAdapter;
    TableItemAdapter tableItemAdapter;
    TableSectionAdapter tableSectionAdapter;
    int index = -1;
    private List<SelectTableClassesRsp.DataBean> data;

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
        Bundle arguments = getArguments();
        tableSectionAdapter = new TableSectionAdapter();
        listsection.setAdapter(tableSectionAdapter);

        timeAdapter = new TableTimeAdapter();
        grid.setAdapter(timeAdapter);
        tv_week.setText(TimeUtil.getWeek() + "周");
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
        GetUserSchoolRsp.DataBean.FormBean classInfo = SpData.getClassInfo();
        if (classInfo != null) {
            className.setText(classInfo.classesName);
            mvpPresenter.listTimeDataByApp(classInfo.classesId);
        }

        GetUserSchoolRsp.DataBean identityInfo = SpData.getIdentityInfo();
        if (identityInfo != null) {
            if ("Y".equalsIgnoreCase(identityInfo.schoolType)) {
                mvpPresenter.selectClassByAllSchool();
            } else if ("N".equalsIgnoreCase(identityInfo.schoolType)) {
                mvpPresenter.selectListBySchoolAll();
            }
        }
    }

    @OnClick(R.id.classlayout)
    public void click() {
        if (data != null && data.size() > 0) {
            swichTableClassPop = new SwichTableClassPop(getActivity(), data);
            swichTableClassPop.setSelectClasses(this);
        } else {
            ToastUtils.showShort("暂无切换班级");
        }
    }

    @Override
    protected ClassTablePresenter createPresenter() {
        return new ClassTablePresenter(this);
    }

    @Override
    public void listAllBySchoolId(listAllBySchoolIdRsp rsp) {
        Log.e("TAG", "listAllBySchoolId==>: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {

        }
    }

    @Override
    public void listAllBySchoolIdFail(String msg) {

    }

    @Override
    public void listTimeDataByApp(listTimeDataByAppRsp rsp) {
        Log.e("TAG", "listTimeDataByApp==>: " + JSON.toJSONString(rsp));

        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
            if (rsp.data != null) {
                tableItemAdapter.notifyData(rsp.data.subList);
                String jc = rsp.data.timetableStructure;
                String s = jc.replaceAll("节课", "");
                int num = Integer.parseInt(s);
                List<Integer> Sectionlist = new ArrayList<>();
                for (int i = 0; i < num; i++) {
                    Sectionlist.add(i);
                }
                tableSectionAdapter.notifyData(Sectionlist);
                int earlyReading = rsp.data.earlyReadingList != null ? rsp.data.earlyReadingList.size() : 0;
                int morning = rsp.data.morningList != null ? rsp.data.morningList.size() : 0;
                int afternoon = rsp.data.afternoonList != null ? rsp.data.afternoonList.size() : 0;
                int eveningStudy = rsp.data.eveningStudyList != null ? rsp.data.eveningStudyList.size() : 0;

                createLeftTypeView(0, 1, earlyReading);//早读
                createLeftTypeView(earlyReading, 2, morning);//上午
                createLeftTypeView(earlyReading + morning, 3, afternoon);//下午
                createLeftTypeView(morning + afternoon + earlyReading, 4, eveningStudy);//晚自习
            }
        }
    }

    //创建"第上下午"视图
    private void createLeftTypeView(int selection, int type, int length) {

        int CouseHeight = SizeUtils.dp2px(75) + 1;
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
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type4));
                text.setText("晚\n自\n习");
                break;
        }
        params.gravity = Gravity.CENTER;
        text.setLayoutParams(params);
        leftLayout.addView(view);

    }

    @Override
    public void listTimeDataByAppFail(String rsp) {

    }

    private SwichTableClassPop swichTableClassPop;

    @Override
    public void selectTableClassListSuccess(SelectTableClassesRsp model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
            data = model.getData();
        }
    }


    @SuppressLint("LongLogTag")
    @Override
    public void selectTableClassListFail(String msg) {
        Log.d("selectTableClassListSuccess", msg);
    }

    @Override
    public void OnSelectClassesListener(int classesId, String classesName) {
        className.setText(classesName);
        mvpPresenter.listTimeDataByApp(classesId + "");
    }
}
