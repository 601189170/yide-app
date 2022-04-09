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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.indicator.DrawableIndicator;
import com.yyide.chatim.R;
import com.yyide.chatim.ScanActivity;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.attendance.teacher.NAttendanceActivity;
import com.yyide.chatim.activity.leave.AskForLeaveActivity;
import com.yyide.chatim.activity.meeting.MeetingHomeActivity;
import com.yyide.chatim.activity.message.MessagePushActivity;
import com.yyide.chatim.activity.message.NoticeContentActivity;
import com.yyide.chatim.activity.operation.OperationActivity;
import com.yyide.chatim.activity.table.TableActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.DialogHomeShowNoticeBinding;
import com.yyide.chatim.dialog.LeftMenuPop;
import com.yyide.chatim.fragment.AttendancePatriarchFragment;
import com.yyide.chatim.fragment.AttendanceSchoolFragment;
import com.yyide.chatim.fragment.AttendanceTeacherFragment;
import com.yyide.chatim.fragment.ClassHonorFragment;
import com.yyide.chatim.fragment.StudentHonorFragment;
import com.yyide.chatim.fragment.TableFragment;
import com.yyide.chatim.fragment.WorkFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.SchoolRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.model.UserBean;
import com.yyide.chatim.model.message.AcceptMessageItem;
import com.yyide.chatim.model.message.HomeNoticeDetaiBean;
import com.yyide.chatim.presenter.HomeFragmentPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.TakePicUtil;
import com.yyide.chatim.view.HomeFragmentView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kotlin.Result;


public class HomeFragment extends BaseMvpFragment<HomeFragmentPresenter> implements HomeFragmentView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "HomeFragment";

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
    @BindView(R.id.school_name)
    TextView schoolName;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.homeBanner)
    Banner<AcceptMessageItem, BannerExampleAdapter> banner;
    private BannerExampleAdapter bannerAdapter;
    private View mBaseView;
    public FragmentListener mListener;

    private HomeViewModel viewModel;

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
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        childFragmentManager = getChildFragmentManager();
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        replaceFragment();
        setSchoolInfo();
        initBanner();
        initView();
        viewModel.requestAcceptMessage();
        viewModel.showDialogMessage();
    }

    @Override
    protected HomeFragmentPresenter createPresenter() {
        return new HomeFragmentPresenter(this);
    }

    private void initView() {
        viewModel.getAcceptMessage().observe(requireActivity(), acceptMessageBean -> {
            if (!acceptMessageBean.getAcceptMessage().isEmpty()) {
                bannerAdapter.setDatas(acceptMessageBean.getAcceptMessage());
            }else {
                List<AcceptMessageItem> noDataList = new ArrayList<>();
                AcceptMessageItem noData = new AcceptMessageItem();
                noData.setTitle("暂无通知公告");
                noDataList.add(noData);
                bannerAdapter.setDatas(noDataList);
            }
        });


        viewModel.getConfirmInfo().observe(requireActivity(), result -> {
            isClose = false;
            dismiss();
            if (!result) {
                ToastUtils.showLong("确认失败");
                return;
            }
            closeDialog();
        });

        viewModel.getDialogInfo().observe(requireActivity(), noticeMessage -> {
            if (noticeMessage.getId() != 0) {
                showNotice(noticeMessage);
            }
        });
    }

    private void initBanner() {
        bannerAdapter = new BannerExampleAdapter(null);
        DrawableIndicator indicator = new DrawableIndicator(getContext(), R.mipmap.icon_banner_nomral, R.mipmap.icon_banner_select);
        banner.addBannerLifecycleObserver(this)
                .setAdapter(bannerAdapter)
                .setIndicator(indicator)
                .isAutoLoop(true)
                .setLoopTime(3000);

        banner.setOnBannerListener((data, position) -> {
            if (!data.getTitle().equals("暂无通知公告")) {
                NoticeContentActivity.Companion.startGo(requireContext(), data);
            }
        });



    }

    @Override
    public void onRefresh() {
        EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_HOME, ""));
//        mvpPresenter.getUserSchool();
    }

    private Dialog dialog;
    private Boolean isClose = false;

    private void showNotice(HomeNoticeDetaiBean model) {
        if (model != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireActivity());
            DialogHomeShowNoticeBinding previewBinding = DialogHomeShowNoticeBinding.inflate(getLayoutInflater());
            alertDialog.setView(previewBinding.getRoot());
            dialog = alertDialog.create();

            WindowManager m = requireActivity().getWindowManager();
            m.getDefaultDisplay(); //为获取屏幕宽、高
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
            p.height = (int) (ScreenUtils.getScreenHeight() * 0.8);
            p.width = (int) (ScreenUtils.getScreenWidth() * 0.8);
            //设置主窗体背景颜色为黑色
            dialog.getWindow().setAttributes(p);//设置生效
            /*ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (ScreenUtils.getScreenHeight() * 0.65));
            layoutParams.bottomMargin = SizeUtils.dp2px(10);
            previewBinding.cardView.setLayoutParams(layoutParams);*/
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setOnDismissListener(dialog -> {

            });
            previewBinding.icClose.setOnClickListener(v -> closeDialog());
            if (model.getContentType() == 0) {
                previewBinding.ivImg.setVisibility(View.GONE);
                previewBinding.dialogHomeShowMv.setVisibility(View.VISIBLE);
                previewBinding.dialogHomeShowMv.loadDataWithBaseURL(
                        null,
                        model.getContent(),
                        "text/html",
                        "utf-8",
                        null
                );
            } else {
                previewBinding.ivImg.setVisibility(View.VISIBLE);
                previewBinding.dialogHomeShowMv.setVisibility(View.GONE);
                GlideUtil.loadImageRadius(getActivity(), model.getContentImg(), previewBinding.ivImg, SizeUtils.dp2px(6f));
            }

            if (model.isNeedConfirm()) {//需要去人按钮
                if (!model.isConfirm()) {
                    previewBinding.btnConfirm.setVisibility(View.VISIBLE);
                } else {
                    previewBinding.btnConfirm.setVisibility(View.INVISIBLE);
                }
            } else {
                previewBinding.btnConfirm.setVisibility(View.INVISIBLE);
            }

            previewBinding.btnConfirm.setOnClickListener(v -> {
                if (isClose) return;
                loading();
                isClose = true;
                viewModel.confirmDetail(model.getId());
            });

            dialog.show();

        }
    }

    private LeftMenuPop mLeftMenuPop;

    @OnClick({R.id.head_img, R.id.scan, R.id.student_honor_content, R.id.class_honor_content, R.id.school_name,
            R.id.tvMenu1, R.id.tvMenu2, R.id.tvMenu3, R.id.tvMenu4, R.id.tvMenu5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_img:
                mLeftMenuPop = null;
                mLeftMenuPop = new LeftMenuPop(mActivity);
                break;
            case R.id.scan:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
            case R.id.student_honor_content:
                //startActivity(new Intent(getActivity(), StudentHonorListActivity.class));
                break;
//            case R.id.layout_message:
////                startActivity(new Intent(getActivity(), MessageNoticeActivity.class));
//                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, "", 1));
//                break;
            case R.id.notice_content:
                //startActivity(new Intent(getActivity(), NoticeAnnouncementActivity.class));
                break;
            case R.id.class_honor_content:
                //startActivity(new Intent(getContext(), ClassesHonorPhotoListActivity.class));
                break;
            case R.id.school_name:
                startActivity(new Intent(getActivity(), OperationActivity.class));
                break;
            case R.id.tvMenu1:
                startActivity(new Intent(getContext(), AskForLeaveActivity.class));
                break;
            case R.id.tvMenu2:
                //startActivity(new Intent(getContext(), MessagePushActivity.class));
                MessagePushActivity.Companion.startGo(requireContext());
                break;
            case R.id.tvMenu3:
                startActivity(new Intent(getContext(), MeetingHomeActivity.class));
                break;
            case R.id.tvMenu4:
                startActivity(new Intent(getContext(), TableActivity.class));
                break;
            case R.id.tvMenu5:
                startActivity(new Intent(getContext(), NAttendanceActivity.class));
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
        if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().staffIdentity()) {//校长
            //校长身份
            workContent.setVisibility(View.GONE);
            studentHonorContent.setVisibility(View.GONE);
            classHonorContent.setVisibility(View.GONE);
            kqContent.setVisibility(View.VISIBLE);
            noticeContent.setVisibility(View.VISIBLE);
            tableContent.setVisibility(View.GONE);
            if (SpData.getClassInfo() != null) {
                tableContent.setVisibility(View.VISIBLE);
                fragmentTransaction.replace(R.id.table_content, new TableFragment());
            }
            //通知
            /*NoticeFragment noticeFragment = new NoticeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "school");
            noticeFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.notice_content, noticeFragment);*/
            //班级考勤情况
            fragmentTransaction.replace(R.id.kq_content, AttendanceSchoolFragment.newInstance());
        } else if (SpData.getIdentityInfo() != null && !SpData.getIdentityInfo().staffIdentity()) {
            //家长身份
            tableContent.setVisibility(View.VISIBLE);
            noticeContent.setVisibility(View.VISIBLE);
            workContent.setVisibility(View.VISIBLE);
            kqContent.setVisibility(View.VISIBLE);
            studentHonorContent.setVisibility(View.GONE);
            classHonorContent.setVisibility(View.GONE);
            //班级课表
            fragmentTransaction.replace(R.id.table_content, new TableFragment());
            //通知
//            fragmentTransaction.replace(R.id.notice_content, new NoticeFragment());
            //班级考勤情况
            fragmentTransaction.replace(R.id.kq_content, new AttendancePatriarchFragment());
            //班级作业
            fragmentTransaction.replace(R.id.work_content, new WorkFragment());
            //班级学生荣誉
//            fragmentTransaction.replace(R.id.student_honor_content, new StudentHonorFragment());
        } /*else if (SpData.getIdentityInfo() != null && !SpData.getIdentityInfo().staffIdentity()) {
            //校长身份
            workContent.setVisibility(View.GONE);
            studentHonorContent.setVisibility(View.GONE);
            classHonorContent.setVisibility(View.GONE);
            kqContent.setVisibility(View.GONE);
            noticeContent.setVisibility(View.VISIBLE);
            tableContent.setVisibility(View.GONE);
            //通知
            NoticeFragment noticeFragment = new NoticeFragment();
            fragmentTransaction.replace(R.id.notice_content, noticeFragment);
        }*/ else {
            tableContent.setVisibility(View.VISIBLE);
            workContent.setVisibility(View.VISIBLE);
            studentHonorContent.setVisibility(View.VISIBLE);
            classHonorContent.setVisibility(View.VISIBLE);
            kqContent.setVisibility(View.VISIBLE);
            noticeContent.setVisibility(View.VISIBLE);
            //班级课表
            fragmentTransaction.replace(R.id.table_content, new TableFragment());
            //通知
//            fragmentTransaction.replace(R.id.notice_content, new NoticeFragment());
            //班级考勤情况
            fragmentTransaction.replace(R.id.kq_content, new AttendanceTeacherFragment());
            //班级作业
            fragmentTransaction.replace(R.id.work_content, new WorkFragment());
            //班级相册
            fragmentTransaction.replace(R.id.class_honor_content, new ClassHonorFragment());
            //班级学生荣誉
            fragmentTransaction.replace(R.id.student_honor_content, new StudentHonorFragment());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File corpFile = TakePicUtil.onActivityResult(getActivity(), requestCode, resultCode, data);
        if (corpFile != null) {
            //showPicFileByLuban(corpFile);
        }
    }


    @Override
    public void getUserSchool(GetUserSchoolRsp rsp) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d("TAG", "getUserSchool==》: " + JSON.toJSONString(rsp));
        if (BaseConstant.REQUEST_SUCCESS == rsp.code) {
            //SPUtils.getInstance().put(SpData.SCHOOLINFO, JSON.toJSONString(rsp));
            //SpData.setIdentityInfo(rsp);
        }
        //setSchoolInfo();
        //replaceFragment();
    }

    private void setSchoolInfo() {
        SchoolRsp schoolinfo = SpData.Schoolinfo();
        if (schoolinfo != null) {
            schoolName.setText(schoolinfo.getSchoolName());
        }
        if (SpData.getUser() != null) {
            UserBean userBean = SpData.getUser();
            GlideUtil.loadImageHead(getActivity(), userBean.getAvatar(), head_img);
            //SPUtils.getInstance().put(SpData.USERNAME, identityInfo.realname);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_IMG.equals(messageEvent.getCode())) {
            if (!TextUtils.isEmpty(messageEvent.getMessage())) {
                GlideUtil.loadImageHead(getActivity(), messageEvent.getMessage(), head_img);
            }
        } else if (BaseConstant.TYPE_SELECT_MESSAGE_TODO.equals(messageEvent.getCode())) {
            //关闭所有的Activity  MainActivity除外
            if (mLeftMenuPop != null && mLeftMenuPop.isShow()) {
                mLeftMenuPop.hide();
            }
        } else if (BaseConstant.TYPE_HOME_CHECK_IDENTITY.equals(messageEvent.getCode())) {
            setSchoolInfo();
            replaceFragment();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDialog();
        EventBus.getDefault().unregister(this);
    }

    private void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void getFail(String rsp) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.e("TAG", "getUserSchoolDataFail==》: " + JSON.toJSONString(rsp));
    }


    public interface FragmentListener {
        void jumpFragment(int index);
    }

}
