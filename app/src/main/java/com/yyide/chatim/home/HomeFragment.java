package com.yyide.chatim.home;

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

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.paradoxie.autoscrolltextview.VerticalTextview;
import com.yyide.chatim.R;
import com.yyide.chatim.ScanActivity;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.MessageNoticeActivity;
import com.yyide.chatim.activity.StudentHonorListActivity;
import com.yyide.chatim.activity.notice.NoticeAnnouncementActivity;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.dialog.LeftMenuPop;
import com.yyide.chatim.homemodel.AttenceFragment;
import com.yyide.chatim.homemodel.BannerFragment;
import com.yyide.chatim.homemodel.ClassHonorFragment;
import com.yyide.chatim.homemodel.NoticeFragment;
import com.yyide.chatim.homemodel.StudentHonorFragment;
import com.yyide.chatim.homemodel.TableFragment;
import com.yyide.chatim.homemodel.WorkFragment;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.presenter.HomeFragmentPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.StringUtils;
import com.yyide.chatim.view.HomeFragmentView;

import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class HomeFragment extends BaseMvpFragment<HomeFragmentPresenter> implements HomeFragmentView {

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
    RoundImageView head_img;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.school_name)
    TextView schoolName;
    @BindView(R.id.spmsg)
    VerticalTextview spmsg;
    @BindView(R.id.layout_message)
    FrameLayout layoutMessage;
    private View mBaseView;
    private long firstTime = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_fragmnet, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        showProgressDialog2();
        setFragment();
        mvpPresenter.getUserSchool();
        //mvpPresenter.getHomeNotice();
        initVerticalTextview(null);
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    void initVerticalTextview(List<NoticeHomeRsp.DataBean> noticeHomeRsps) {
        ArrayList<String> list = new ArrayList<>();
//        if(noticeHomeRsps != null){
//            for (NoticeHomeRsp.DataBean item : noticeHomeRsps){
//                list.add(item.getContent());
//            }
//        }
        list.add("罗欣的主监护人提交的请假需要你审批");
        list.add("李克用的主监护人提交的请假需要你审批");
        list.add("王保华的主监护人提交的请假需要你审批");
        list.add("秦鑫的主监护人提交的请假需要你审批");
        list.add("张玉明的主监护人提交的请假需要你审批");
//        spmsg.setText(20, 0, Color.WHITE);//设置属性
        spmsg.setTextStillTime(3000);//设置停留时长间隔
        spmsg.setAnimTime(300);//设置进入和退出的时间间隔
        if (spmsg != null) {
            if (null != list && list.size() > 0) {
                spmsg.setVisibility(View.VISIBLE);
                spmsg.setTextList(list);
            } else {
                spmsg.setVisibility(View.GONE);
            }
        }
        spmsg.setOnItemClickListener(i -> {
            startActivity(new Intent(getActivity(), MessageNoticeActivity.class));
        });
    }

    @Override
    protected HomeFragmentPresenter createPresenter() {
        return new HomeFragmentPresenter(this);
    }

    @OnClick({R.id.user_img, R.id.scan, R.id.student_honor_content, R.id.layout_message, R.id.notice_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_img:
                LeftMenuPop leftMenuPop = new LeftMenuPop(mActivity);
                break;
            case R.id.scan:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
            case R.id.student_honor_content:
                startActivity(new Intent(getActivity(), StudentHonorListActivity.class));
                break;
            case R.id.layout_message:
                startActivity(new Intent(getActivity(), MessageNoticeActivity.class));
                break;
            case R.id.notice_content:
                startActivity(new Intent(getActivity(), NoticeAnnouncementActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (spmsg != null) {
            spmsg.startAutoScroll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (spmsg != null) {
            spmsg.stopAutoScroll();
        }
    }

    void setFragment() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        //课表
        fragmentTransaction.add(R.id.table_content, new TableFragment());
        //通知
        fragmentTransaction.add(R.id.notice_content, new NoticeFragment());
        //考勤情况
        fragmentTransaction.add(R.id.kq_content, new AttenceFragment());
        //相册轮播
        fragmentTransaction.add(R.id.banner_content, new BannerFragment());
        //作业
        fragmentTransaction.add(R.id.work_content, new WorkFragment());
        //班级荣誉
        fragmentTransaction.add(R.id.class_honor_content, new ClassHonorFragment());
        //学生荣誉
        fragmentTransaction.add(R.id.student_honor_content, new StudentHonorFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void getUserSchool(GetUserSchoolRsp rsp) {
        Log.e("TAG", "getUserSchool==》: " + JSON.toJSONString(rsp));
        SPUtils.getInstance().put(SpData.SCHOOLINFO, JSON.toJSONString(rsp));
        GetUserSchoolRsp.DataBean dataBean = null;
        if (rsp.data.size() > 0) {
            for (int i = 0; i < rsp.data.size(); i++) {
                if (rsp.data.get(i).isCurrentUser) {//保存切换身份信息
                    dataBean = rsp.data.get(i);
                    SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(dataBean));
                    if(dataBean != null && dataBean.form != null && dataBean.form.size() > 0){//保存班级信息
                        SPUtils.getInstance().put(SpData.CLASS_INFO, JSON.toJSONString(dataBean.form.get(0)));
                    }
                }
            }
        }
        setSchoolInfo(dataBean);
    }

    void setSchoolInfo(GetUserSchoolRsp.DataBean rsp) {
        if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().userId > 0) {
            String qhSchool = "";
            String qhName = "";
            String imgUrl = "";
            for (GetUserSchoolRsp.DataBean datum : SpData.Schoolinfo().data) {
                if (datum.userId == SpData.getIdentityInfo().userId) {
                    qhSchool = datum.schoolName;
                    qhName = datum.realname;
                    imgUrl = datum.img;
                }
            }
            if (!TextUtils.isEmpty(imgUrl)) {
                userName.setVisibility(View.GONE);
                GlideUtil.loadImage(getActivity(), imgUrl, head_img);
            } else {
                head_img.setVisibility(View.GONE);
                userName.setText(StringUtils.subString(qhName, 2));
            }
            schoolName.setText(qhSchool);
            SPUtils.getInstance().put(SpData.USERNAME, qhName);
        }
    }

    @Override
    public void getUserSchoolDataFail(String rsp) {
        Log.e("TAG", "getUserSchoolDataFail==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void getIndexMyNotice(NoticeHomeRsp rsp) {
        if (rsp != null && rsp.getData() != null && rsp.getData().size() > 0) {
            initVerticalTextview(rsp.getData());
        }
    }
}
