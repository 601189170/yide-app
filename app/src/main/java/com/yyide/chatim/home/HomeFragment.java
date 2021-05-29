package com.yyide.chatim.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.chatim.BuildConfig;
import com.yyide.chatim.R;
import com.yyide.chatim.ScanActivity;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.dialog.LeftMenuPop;
import com.yyide.chatim.fragment.AttendanceFragment;
import com.yyide.chatim.fragment.BannerFragment;
import com.yyide.chatim.fragment.ClassHonorFragment;
import com.yyide.chatim.fragment.NoticeFragment;
import com.yyide.chatim.fragment.StudentHonorFragment;
import com.yyide.chatim.fragment.TableFragment;
import com.yyide.chatim.fragment.WorkFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.presenter.HomeFragmentPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.HomeFragmentView;
import com.yyide.chatim.view.VerticalTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class HomeFragment extends BaseMvpFragment<HomeFragmentPresenter> implements HomeFragmentView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.user_img)
    FrameLayout userImg;
    @BindView(R.id.scan)
    ImageView scan;
    @BindView(R.id.table_content)
    FrameLayout tableContent;
    @BindView(R.id.notice_content)
    FrameLayout noticeContent;
    @BindView(R.id.kq_content)
    FrameLayout kqContent;
    @BindView(R.id.banner_content)
    FrameLayout bannerContent;
    @BindView(R.id.work_content)
    FrameLayout workContent;
    @BindView(R.id.class_honor_content)
    FrameLayout classHonorContent;
    @BindView(R.id.student_honor_content)
    FrameLayout studentHonorContent;
    @BindView(R.id.head_img)
    ImageView head_img;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.school_name)
    TextView schoolName;
    @BindView(R.id.tv_todo)
    TextView tv_todo;
    @BindView(R.id.verticalTextView)
    VerticalTextView mVerticalTextView;
    @BindView(R.id.layout_message)
    FrameLayout layoutMessage;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private View mBaseView;
    public FragmentListener mListener;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentListener) {
            mListener = (FragmentListener) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_fragmnet, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        setFragment();
        mvpPresenter.getUserSchool();
        mvpPresenter.getHomeNotice();
    }

    void initVerticalTextview(List<TodoRsp.DataBean.RecordsBean> noticeHomeRsps) {
        if (noticeHomeRsps != null) {
            list.clear();
            for (TodoRsp.DataBean.RecordsBean item : noticeHomeRsps) {
                list.add(item.getFirstData());
            }
        }
        mVerticalTextView.setResources(list);
        mVerticalTextView.setTextStillTime(4000);
        mVerticalTextView.setOnItemClickListener(i -> {
            mListener.jumpFragment(1);
        });
    }

    @Override
    public void onRefresh() {
        EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_HOME, ""));
        mvpPresenter.getUserSchool();
        mvpPresenter.getHomeNotice();
    }

    @Override
    protected HomeFragmentPresenter createPresenter() {
        return new HomeFragmentPresenter(this);
    }

    @OnClick({R.id.user_img, R.id.scan, R.id.student_honor_content, R.id.class_honor_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_img:
                new LeftMenuPop(mActivity);
                break;
            case R.id.scan:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
            case R.id.student_honor_content:
                //startActivity(new Intent(getActivity(), StudentHonorListActivity.class));
                break;
            case R.id.layout_message:
//                startActivity(new Intent(getActivity(), MessageNoticeActivity.class));
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, "", 1));
                break;
            case R.id.notice_content:
                //startActivity(new Intent(getActivity(), NoticeAnnouncementActivity.class));
                break;
            case R.id.class_honor_content:
                //startActivity(new Intent(getContext(), ClassesHonorPhotoListActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setIdentityFragment(){//身份切换只做模块显示和隐藏
        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PRESIDENT.equals(SpData.getIdentityInfo().status)) {//校长
            tableContent.setVisibility(View.GONE);
            noticeContent.setVisibility(View.VISIBLE);
            kqContent.setVisibility(View.VISIBLE);
            bannerContent.setVisibility(View.GONE);
            workContent.setVisibility(View.GONE);
            classHonorContent.setVisibility(View.GONE);
            studentHonorContent.setVisibility(View.GONE);
        } else {
            tableContent.setVisibility(View.VISIBLE);
            noticeContent.setVisibility(View.VISIBLE);
            kqContent.setVisibility(View.VISIBLE);
            bannerContent.setVisibility(View.VISIBLE);
            workContent.setVisibility(View.VISIBLE);
            classHonorContent.setVisibility(View.VISIBLE);
            studentHonorContent.setVisibility(View.VISIBLE);
        }
    }

    void setFragment() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        //班级课表
        fragmentTransaction.add(R.id.table_content, new TableFragment());
        //通知
        fragmentTransaction.add(R.id.notice_content, new NoticeFragment());
        //班级考勤情况
        fragmentTransaction.add(R.id.kq_content, new AttendanceFragment());
        //班级相册轮播
        fragmentTransaction.add(R.id.banner_content, new BannerFragment());
        //班级作业
        fragmentTransaction.add(R.id.work_content, new WorkFragment());
        //班级相册
        fragmentTransaction.add(R.id.class_honor_content, new ClassHonorFragment());
        //班级学生荣誉
        fragmentTransaction.add(R.id.student_honor_content, new StudentHonorFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void getUserSchool(GetUserSchoolRsp rsp) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d("TAG", "getUserSchool==》: " + JSON.toJSONString(rsp));
        if (BaseConstant.REQUEST_SUCCES2 == rsp.code) {
            SPUtils.getInstance().put(SpData.SCHOOLINFO, JSON.toJSONString(rsp));
            SpData.setIdentityInfo(rsp);
        }
        setSchoolInfo();
        setIdentityFragment();
    }

    void setSchoolInfo() {
        if (SpData.getIdentityInfo() != null) {
            GetUserSchoolRsp.DataBean identityInfo = SpData.getIdentityInfo();
            GlideUtil.loadImageHead(getActivity(), identityInfo.img, head_img);
            if (BuildConfig.DEBUG) {
                schoolName.setText(identityInfo.schoolName + "-UAT");
            } else {
                schoolName.setText(identityInfo.schoolName);
            }
            SPUtils.getInstance().put(SpData.USERNAME, identityInfo.realname);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_IMG.equals(messageEvent.getCode())) {
            if (!TextUtils.isEmpty(messageEvent.getMessage())) {
                GlideUtil.loadImageHead(getActivity(), messageEvent.getMessage(), head_img);
            }
        } else if (BaseConstant.TYPE_LEAVE.equals(messageEvent.getCode())) {
            mvpPresenter.getHomeNotice();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getUserSchoolDataFail(String rsp) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.e("TAG", "getUserSchoolDataFail==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void getIndexMyNotice(TodoRsp rsp) {
        Log.e(TAG, "getIndexMyNotice: " + rsp.toString());
        if (rsp != null && rsp.getData() != null && rsp.getData().getRecords() != null && rsp.getData().getRecords().size() > 0) {
            tv_todo.setVisibility(View.VISIBLE);
            tv_todo.setText(rsp.getData().getTotal() + "");
            initVerticalTextview(rsp.getData().getRecords());
        } else {
            tv_todo.setVisibility(View.GONE);
            List<TodoRsp.DataBean.RecordsBean> noticeHomeRsps = new ArrayList<>();
            TodoRsp.DataBean.RecordsBean dataBean = new TodoRsp.DataBean.RecordsBean();
            dataBean.setFirstData("暂无待办数据");
            noticeHomeRsps.add(dataBean);
            initVerticalTextview(noticeHomeRsps);
        }
    }

    public interface FragmentListener {
        void jumpFragment(int index);
    }

}
