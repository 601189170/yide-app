package com.yyide.chatim.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.attendance.AttendanceActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentAttendanceSchoolBinding;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.presenter.AttendancePresenter;
import com.yyide.chatim.utils.InitPieChart;
import com.yyide.chatim.view.AttendanceCheckView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * desc 校长考情.
 * time 2021年6月3日17:08:28
 */
public class AttendanceSchoolFragment extends BaseMvpFragment<AttendancePresenter> implements AttendanceCheckView {

    private FragmentAttendanceSchoolBinding mViewBinding;

    public static AttendanceSchoolFragment newInstance() {
        AttendanceSchoolFragment fragment = new AttendanceSchoolFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentAttendanceSchoolBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        mvpPresenter.homeAttendance("");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            mvpPresenter.homeAttendance("");
        }
    }

    @Override
    protected AttendancePresenter createPresenter() {
        return new AttendancePresenter(this);
    }

    private void initView() {
        mViewBinding.getRoot().setVisibility(View.INVISIBLE);
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mViewBinding.recyclerview.setAdapter(adapter);
    }

    //    -->设置各区块的颜色
    public static final int[] PIE_COLORS2 = {
            Color.rgb(145, 147, 153), Color.rgb(246, 189, 22)
            , Color.rgb(246, 108, 108), Color.rgb(55, 130, 255)
    };
    private BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceSchoolBean, BaseViewHolder>(R.layout.item_attendance_school) {

        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceSchoolBean item) {
            AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item1 = item.getItem1();
            AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item2 = item.getItem2();
            ConstraintLayout constraintLayout1 = holder.getView(R.id.constraintLayout1);
            ConstraintLayout constraintLayout2 = holder.getView(R.id.constraintLayout);
            PieChart piechart2 = holder.getView(R.id.piechart2);
            PieChart piechart = holder.getView(R.id.piechart);
            if (item1 != null) {
                holder.setText(R.id.tv_attendance_type, item1.getAttName());
                holder.setText(R.id.tv_desc, item1.getThingName());
                setPieChart(item1, piechart);
                constraintLayout1.setOnClickListener(v -> {
                    AttendanceActivity.start(getContext(), item1.getPeopleType(), getItemPosition(item));
                });
                piechart.setTouchEnabled(false);
                piechart.setOnClickListener(v -> AttendanceActivity.start(getContext(), item1.getPeopleType(), item1.getIndex()));
            }
            if (item2 != null) {
                constraintLayout2.setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_attendance_type2, item2.getAttName());
                holder.setText(R.id.tv_desc2, item1.getThingName());
                constraintLayout2.setOnClickListener(v -> {
                    AttendanceActivity.start(getContext(), item2.getPeopleType(), item2.getIndex());
                });
                piechart2.setTouchEnabled(false);
                piechart2.setOnClickListener(v -> AttendanceActivity.start(getContext(), item2.getPeopleType(), item2.getIndex()));
                setPieChart(item2, piechart2);
            } else {
                constraintLayout2.setVisibility(View.GONE);
            }
        }

        private void setPieChart(AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item, PieChart piechart) {
            InitPieChart.InitPieChart(((Activity) getContext()), piechart);
            List<PieEntry> entries = new ArrayList<>();
            int absence = item.getAbsence();
            int leave = item.getLeave();
            int late = item.getLate();
            int applyNum = item.getApplyNum();
            absence = (absence + late + applyNum + leave) > 0 ? absence : 1;
            entries.add(new PieEntry(absence, "缺勤"));
            entries.add(new PieEntry(item.getLeave(), "请假"));
            entries.add(new PieEntry(item.getLate(), "迟到"));
            entries.add(new PieEntry(item.getApplyNum(), "实到"));
            piechart.setCenterText((TextUtils.isEmpty(item.getRate()) ? 0 : item.getRate()) + "%\n" + "考勤率");
            piechart.setCenterTextSize(12);
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(0);//设置饼块之间的间隔
            dataSet.setColors(PIE_COLORS2);//设置饼块的颜色
            dataSet.setDrawValues(false);
            PieData pieData = new PieData(dataSet);
            piechart.setData(pieData);
            piechart.invalidate();
        }
    };

    @Override
    public void getAttendanceSuccess(AttendanceCheckRsp model) {
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            if (model.getData() != null) {
                setData(model.getData());
            }
        }
    }

    private void setData(AttendanceCheckRsp.DataBean dataBean) {
        List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean> schoolPeopleAllFormBeanList = new ArrayList<>();
        if (dataBean.getSchoolPeopleAllForm() != null) {
            schoolPeopleAllFormBeanList.addAll(dataBean.getSchoolPeopleAllForm());
        }
        if (dataBean.getAttendancesForm() != null && dataBean.getAttendancesForm().size() > 0) {
            for (AttendanceCheckRsp.DataBean.AttendancesFormBean item : dataBean.getAttendancesForm()) {
                AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolBean = new AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean();
                schoolBean.setAbsence(item.getAbsenceA());
                schoolBean.setApplyNum(item.getApplyNumA());
                schoolBean.setAttName(item.getAttNameA());
                schoolBean.setLate(item.getLateA());
                schoolBean.setLeave(item.getLeaveA());
                schoolBean.setNumber(item.getNumberA());
                schoolBean.setRate(item.getRateA());
                schoolBean.setPeopleType(item.getPeopleType());
                schoolPeopleAllFormBeanList.add(schoolBean);
            }
        }

        //数据处理
        List<AttendanceSchoolBean> list = new ArrayList<>();
        if (schoolPeopleAllFormBeanList != null && schoolPeopleAllFormBeanList.size() > 0) {
            for (int i = 0; i < schoolPeopleAllFormBeanList.size(); i++) {
                AttendanceSchoolBean attendanceSchoolBean = new AttendanceSchoolBean();
                AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolPeopleAllFormBean = schoolPeopleAllFormBeanList.get(i);
                schoolPeopleAllFormBean.setIndex(i);
                attendanceSchoolBean.setItem1(schoolPeopleAllFormBean);
                i++;
                if (i < schoolPeopleAllFormBeanList.size()) {
                    AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolPeopleAllFormBean2 = schoolPeopleAllFormBeanList.get(i);
                    schoolPeopleAllFormBean2.setIndex(i);
                    attendanceSchoolBean.setItem2(schoolPeopleAllFormBean2);
                }
                list.add(attendanceSchoolBean);
            }
        }
        if (list != null && list.size() > 0) {
            mViewBinding.getRoot().setVisibility(View.VISIBLE);
        }
        adapter.setList(list);
    }

    public class AttendanceSchoolBean {
        private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item1;
        private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item2;

        public AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean getItem1() {
            return item1;
        }

        public void setItem1(AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item1) {
            this.item1 = item1;
        }

        public AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean getItem2() {
            return item2;
        }

        public void setItem2(AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item2) {
            this.item2 = item2;
        }
    }

    @Override
    public void getAttendanceFail(String msg) {
        Log.e("TAG", "getAttendanceFail==>: " + msg);
    }
}