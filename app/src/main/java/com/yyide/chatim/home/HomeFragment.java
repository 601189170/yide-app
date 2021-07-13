package com.yyide.chatim.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.yyide.chatim.BuildConfig;
import com.yyide.chatim.R;
import com.yyide.chatim.ScanActivity;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.DialogHomeShowNoticeBinding;
import com.yyide.chatim.dialog.LeftMenuPop;
import com.yyide.chatim.fragment.AttendanceTeacherFragment;
import com.yyide.chatim.fragment.AttendancePatriarchFragment;
import com.yyide.chatim.fragment.AttendanceSchoolFragment;
import com.yyide.chatim.fragment.BannerFragment;
import com.yyide.chatim.fragment.ClassHonorFragment;
import com.yyide.chatim.fragment.NoticeFragment;
import com.yyide.chatim.fragment.StudentHonorFragment;
import com.yyide.chatim.fragment.TableFragment;
import com.yyide.chatim.fragment.WorkFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim.model.ResultBean;
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
        childFragmentManager = getChildFragmentManager();
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        initVerticalTextview();
        replaceFragment();
        setSchoolInfo();
        //mvpPresenter.getUserSchool();
        mvpPresenter.getHomeTodo();
        mvpPresenter.getNotice();
    }

    void initVerticalTextview() {
        setData();
        mVerticalTextView.setResources(list);
        mVerticalTextView.setTextStillTime(4000);
        mVerticalTextView.setOnItemClickListener(i -> {
            mListener.jumpFragment(1);
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, "", 1));
        });
    }

    @Override
    public void onRefresh() {
        EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_HOME, ""));
        //mvpPresenter.getUserSchool();
        mvpPresenter.getHomeTodo();
    }

    private Dialog dialog;

    private void showNotice(NoticeMyReleaseDetailBean.DataBean model) {
        if (model != null && model.isConfirm && !model.confirmOrRead) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            DialogHomeShowNoticeBinding previewBinding = DialogHomeShowNoticeBinding.inflate(getLayoutInflater());
            alertDialog.setView(previewBinding.getRoot());
            dialog = alertDialog.create();
            dialog.show();
            WindowManager m = getActivity().getWindowManager();
            m.getDefaultDisplay(); //为获取屏幕宽、高
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
            p.height = (int) (ScreenUtils.getScreenHeight() * 0.8); //高度设置为屏幕的0.3
            p.width = (int) (ScreenUtils.getScreenWidth() * 0.8); //宽度设置为屏幕的0.5
            //设置主窗体背景颜色为黑色
            previewBinding.icClose.setOnClickListener(v -> dialog.dismiss());
//        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(p);//设置生效
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (ScreenUtils.getScreenHeight() * 0.6));
            layoutParams.bottomMargin = SizeUtils.dp2px(20);
            previewBinding.cardView.setLayoutParams(layoutParams);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            if (model.type == 0) {
                previewBinding.ivImg.setVisibility(View.GONE);
                previewBinding.nestedScrollView.setVisibility(View.VISIBLE);
                previewBinding.tvTitle.setText(model.title);
                previewBinding.tvContent.setText(model.content);
            } else {
                previewBinding.ivImg.setVisibility(View.VISIBLE);
                previewBinding.nestedScrollView.setVisibility(View.GONE);
                GlideUtil.loadImageRadius(getActivity(), model.imgpath, previewBinding.ivImg, SizeUtils.dp2px(6f));
            }

            if (model.isConfirm) {//需要去人按钮
                if (!model.confirmOrRead) {
                    previewBinding.btnConfirm.setVisibility(View.VISIBLE);
                } else {
                    previewBinding.btnConfirm.setVisibility(View.INVISIBLE);
                }
            } else {
                previewBinding.btnConfirm.setVisibility(View.INVISIBLE);
            }
            previewBinding.btnConfirm.setOnClickListener(v -> {
                mvpPresenter.confirmNotice(model.id);
            });
        }
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

    private FragmentManager childFragmentManager;
    private FragmentTransaction fragmentTransaction;

    private void replaceFragment() {
        fragmentTransaction = childFragmentManager.beginTransaction();
        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PRESIDENT.equals(SpData.getIdentityInfo().status)) {//校长
            //校长身份
            bannerContent.setVisibility(View.GONE);
            workContent.setVisibility(View.GONE);
            studentHonorContent.setVisibility(View.GONE);
            classHonorContent.setVisibility(View.GONE);
            tableContent.setVisibility(View.GONE);
            kqContent.setVisibility(View.VISIBLE);
            noticeContent.setVisibility(View.VISIBLE);
            //通知
            fragmentTransaction.replace(R.id.notice_content, new NoticeFragment());
            //班级考勤情况
            fragmentTransaction.replace(R.id.kq_content, AttendanceSchoolFragment.newInstance());
        } else if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
            //家长身份
            tableContent.setVisibility(View.VISIBLE);
            bannerContent.setVisibility(View.VISIBLE);
            noticeContent.setVisibility(View.VISIBLE);
            workContent.setVisibility(View.VISIBLE);
            kqContent.setVisibility(View.VISIBLE);
            studentHonorContent.setVisibility(View.GONE);
            classHonorContent.setVisibility(View.GONE);
            //班级课表
            fragmentTransaction.replace(R.id.table_content, new TableFragment());
            //通知
            fragmentTransaction.replace(R.id.notice_content, new NoticeFragment());
            //班级考勤情况
            fragmentTransaction.replace(R.id.kq_content, new AttendancePatriarchFragment());
            //班级相册轮播
//            fragmentTransaction.replace(R.id.banner_content, new BannerFragment());
            //班级作业
            fragmentTransaction.replace(R.id.work_content, new WorkFragment());
            //班级学生荣誉
//            fragmentTransaction.replace(R.id.student_honor_content, new StudentHonorFragment());
        } else {
            tableContent.setVisibility(View.VISIBLE);
            tableContent.setVisibility(View.VISIBLE);
            bannerContent.setVisibility(View.VISIBLE);
            workContent.setVisibility(View.VISIBLE);
            studentHonorContent.setVisibility(View.VISIBLE);
            classHonorContent.setVisibility(View.VISIBLE);
            //班级课表
            fragmentTransaction.replace(R.id.table_content, new TableFragment());
            //通知
            fragmentTransaction.replace(R.id.notice_content, new NoticeFragment());
            //班级考勤情况
            fragmentTransaction.replace(R.id.kq_content, new AttendanceTeacherFragment());
            //班级相册轮播
            fragmentTransaction.replace(R.id.banner_content, new BannerFragment());
            //班级作业
            fragmentTransaction.replace(R.id.work_content, new WorkFragment());
            //班级相册
            fragmentTransaction.replace(R.id.class_honor_content, new ClassHonorFragment());
            //班级学生荣誉
            fragmentTransaction.replace(R.id.student_honor_content, new StudentHonorFragment());
        }
        fragmentTransaction.commit();
    }

    private void initFragment() {
        tableContent.setVisibility(View.GONE);
        tableContent.setVisibility(View.GONE);
        bannerContent.setVisibility(View.GONE);
        workContent.setVisibility(View.GONE);
        studentHonorContent.setVisibility(View.GONE);
        classHonorContent.setVisibility(View.GONE);
    }

    @Override
    public void confirmNotice(ResultBean model) {
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            //ToastUtils.showShort(model.getMsg());
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void getNotice(NoticeMyReleaseDetailBean model) {
        if (BaseConstant.REQUEST_SUCCES2 == model.code) {
            showNotice(model.data);
        }
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
        replaceFragment();
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
            mvpPresenter.getHomeTodo();
        } else if (BaseConstant.TYPE_HOME_CHECK_IDENTITY.equals(messageEvent.getCode())) {
            setSchoolInfo();
            replaceFragment();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getFail(String rsp) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.e("TAG", "getUserSchoolDataFail==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void getIndexMyNotice(TodoRsp rsp) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.e(TAG, "getIndexMyNotice: " + rsp.toString());
        if (rsp.getCode() == BaseConstant.REQUEST_SUCCES2) {
            if (rsp != null && rsp.getData() != null && rsp.getData().getRecords() != null && rsp.getData().getRecords().size() > 0) {
                tv_todo.setVisibility(View.VISIBLE);
                tv_todo.setText(rsp.getData().getTotal() + "");
                if (rsp.getData().getRecords() != null) {
                    list.clear();
                    for (TodoRsp.DataBean.RecordsBean item : rsp.getData().getRecords()) {
                        list.add(item.getFirstData());
                    }
                }
                mVerticalTextView.setResources(list);
            } else {
                tv_todo.setVisibility(View.GONE);
                setData();
            }
        }
    }

    private void setData() {
        List<TodoRsp.DataBean.RecordsBean> noticeHomeRsps = new ArrayList<>();
        TodoRsp.DataBean.RecordsBean dataBean = new TodoRsp.DataBean.RecordsBean();
        dataBean.setFirstData("暂无待办数据");
        noticeHomeRsps.add(dataBean);
        list.clear();
        for (TodoRsp.DataBean.RecordsBean item : noticeHomeRsps) {
            list.add(item.getFirstData());
        }
    }

    public interface FragmentListener {
        void jumpFragment(int index);
    }

}
