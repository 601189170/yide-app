package com.yyide.chatim.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.paradoxie.autoscrolltextview.VerticalTextview;
import com.yyide.chatim.BuildConfig;
import com.yyide.chatim.R;
import com.yyide.chatim.ScanActivity;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.ClassesHonorPhotoListActivity;
import com.yyide.chatim.activity.notice.NoticeDetailActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.dialog.LeftMenuPop;
import com.yyide.chatim.homemodel.AttendanceFragment;
import com.yyide.chatim.homemodel.BannerFragment;
import com.yyide.chatim.homemodel.ClassHonorFragment;
import com.yyide.chatim.homemodel.NoticeFragment;
import com.yyide.chatim.homemodel.StudentHonorFragment;
import com.yyide.chatim.homemodel.TableFragment;
import com.yyide.chatim.homemodel.WorkFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.presenter.HomeFragmentPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.StringUtils;
import com.yyide.chatim.view.HomeFragmentView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.raphets.roundimageview.RoundImageView;

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
    @BindView(R.id.spmsg)
    VerticalTextview spmsg;
    @BindView(R.id.layout_message)
    FrameLayout layoutMessage;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private View mBaseView;
    ArrayList<String> list = new ArrayList<>();
    public FragmentListener mListener;

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
//        showProgressDialog2();
        setFragment();
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mvpPresenter.getUserSchool();
        mvpPresenter.getHomeNotice();
        //initVerticalTextview(null);
        initVerticalTextview(null);
    }

    void initVerticalTextview(List<NoticeHomeRsp.DataBean> noticeHomeRsps) {
        if (noticeHomeRsps != null) {
            list.clear();
            for (NoticeHomeRsp.DataBean item : noticeHomeRsps) {
                list.add(item.getContent());
            }
        }
        list.add("罗欣的主监护人提交的请假需要你审批");
        list.add("李克用的主监护人提交的请假需要你审批");
        list.add("王保华的主监护人提交的请假需要你审批");
        list.add("秦鑫的主监护人提交的请假需要你审批");
        list.add("张玉明的主监护人提交的请假需要你审批");
        //spmsg.setText(20, 0, Color.WHITE);//设置属性
        Log.e(TAG, "initVerticalTextview: " + list);
        spmsg.setTextStillTime(3000);//设置停留时长间隔
        spmsg.setAnimTime(300);//设置进入和退出的时间间隔
        if (spmsg != null) {
            if (null != list && list.size() > 0) {
                spmsg.setVisibility(View.VISIBLE);
                spmsg.setTextList(list);
                spmsg.startAutoScroll();
                if (list.size() < 2) {
                    spmsg.stopAutoScroll();
                }
            } else {
                spmsg.setVisibility(View.GONE);
            }
        }

        spmsg.setOnItemClickListener(i -> {
            //startActivity(new Intent(getActivity(), MessageNoticeActivity.class));
            //EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, ""));
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
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, ""));
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
        if (spmsg != null && list.size() > 1) {
            spmsg.startAutoScroll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (spmsg != null && list.size() > 1) {
            spmsg.stopAutoScroll();
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
//                userName.setVisibility(View.GONE);
//                head_img.setVisibility(View.VISIBLE);
                GlideUtil.loadImageHead(getActivity(), messageEvent.getMessage(), head_img);
            }
        } else if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            mvpPresenter.getUserSchool();
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
    public void getIndexMyNotice(NoticeHomeRsp rsp) {
        Log.e(TAG, "getIndexMyNotice: " + rsp.toString());
        if (rsp != null && rsp.getData() != null && rsp.getData().size() > 0) {
//            tv_todo.setVisibility(View.VISIBLE);
//            initVerticalTextview(rsp.getData());
        } else {
//            tv_todo.setVisibility(View.GONE);
//            List<NoticeHomeRsp.DataBean> noticeHomeRsps = new ArrayList<>();
//            NoticeHomeRsp.DataBean dataBean = new NoticeHomeRsp.DataBean();
//            dataBean.setContent("暂无代办消息通知");
//            noticeHomeRsps.add(dataBean);
//            initVerticalTextview(noticeHomeRsps);
        }
    }

    public interface FragmentListener {
        void jumpFragment(int index);
    }

}
