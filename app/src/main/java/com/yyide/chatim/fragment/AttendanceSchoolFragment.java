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
import com.yyide.chatim.model.AttendanceRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.presenter.AttendancePresenter;
import com.yyide.chatim.utils.InitPieChart;
import com.yyide.chatim.view.AttendanceCheckView;

import org.greenrobot.eventbus.EventBus;
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
        EventBus.getDefault().register(this);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            adapter.setList(null);
            mvpPresenter.homeAttendance("", "");
        }
    }

    @Override
    protected AttendancePresenter createPresenter() {
        return new AttendancePresenter(this);
    }

    private void initView() {
        mvpPresenter.homeAttendance("", "");
    }

    //    -->设置各区块的颜色
    public static final int[] PIE_COLORS2 = {
            Color.rgb(145, 147, 153), Color.rgb(246, 189, 22)
            , Color.rgb(246, 108, 108), Color.rgb(55, 130, 255)
    };

    private BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder>(R.layout.item_attendance_school) {

        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceRsp.DataBean.AttendanceListBean item) {
            ConstraintLayout constraintLayout1 = holder.getView(R.id.constraintLayout1);
            PieChart piechart = holder.getView(R.id.piechart);
            if (item != null) {
                holder.setText(R.id.tv_attendance_type, item.getTheme());
                holder.setText(R.id.tv_desc, item.getEventName());
                setPieChart(item, piechart);
                constraintLayout1.setOnClickListener(v -> {
                    AttendanceActivity.start(getContext(), item);
                });
                piechart.setTouchEnabled(false);
                piechart.setOnClickListener(v -> AttendanceActivity.start(getContext(), item));
            }
        }

        private void setPieChart(AttendanceRsp.DataBean.AttendanceListBean item, PieChart piechart) {
            InitPieChart.InitPieChart(((Activity) getContext()), piechart);
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(item.getAbsenteeism() > 0 ? item.getAbsenteeism() : 1, "缺勤"));
            entries.add(new PieEntry(item.getLeave(), "请假"));
            entries.add(new PieEntry(item.getLate(), "迟到"));
            entries.add(new PieEntry(item.getNormal(), "实到"));

            String desc = "1".equals(item.getAttendanceSignInOut()) ? "签退率" : "出勤率";//0 签到 1签退
            piechart.setCenterText(item.getSignInOutRate() + "%\n" + desc);
            piechart.setCenterTextSize(11);
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
    public void getAttendanceSuccess(AttendanceRsp model) {
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            if (model.getData() != null) {
                setData(model.getData());
            }
        }
    }

    private void setData(AttendanceRsp.DataBean dataBean) {
        //处理数组大于大于一个的时候用网格布局
        if (dataBean.getHeadmasterAttendanceList() != null && dataBean.getHeadmasterAttendanceList().size() > 1) {
            mViewBinding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mViewBinding.recyclerview.setAdapter(adapter);
        adapter.setList(dataBean.getHeadmasterAttendanceList());
    }

    @Override
    public void getAttendanceFail(String msg) {
        Log.e("TAG", "getAttendanceFail==>: " + msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}