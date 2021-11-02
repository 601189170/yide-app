package com.yyide.chatim.fragment;

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

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.Talble.Presenter.TablePresenter;
import com.yyide.chatim.Talble.View.listTimeDataByAppView;
import com.yyide.chatim.activity.TableActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.utils.DateUtils;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.BindView;


public class TableFragment extends BaseMvpFragment<TablePresenter> implements listTimeDataByAppView {

    @BindView(R.id.tablelayout)
    FrameLayout tablelayout;
    @BindView(R.id.subjectName)
    TextView subjectName;
    @BindView(R.id.className)
    TextView className;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.table_next)
    TextView table_next;
    @BindView(R.id.iv_tips)
    ImageView iv_tips;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;
    private View mBaseView;

    private static final String TAG = "TableFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.table_fragmnet, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mvpPresenter.getMyData();
        EventBus.getDefault().register(this);
        tablelayout.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, TableActivity.class);
            startActivity(intent);
        });
        getData();
    }

    @Override
    protected TablePresenter createPresenter() {
        return new TablePresenter(this);
    }

    @Override
    public void SelectSchByTeaid(SelectSchByTeaidRsp rsp) {
        Log.e(TAG, "SelectSchByTeaid: " + JSON.toJSONString(rsp));
        boolean isTable = false;
        SelectSchByTeaidRsp.DataBean dataBean = null;
        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
            if (rsp.data != null && rsp.data.size() > 0) {
                Calendar c = Calendar.getInstance();
                for (SelectSchByTeaidRsp.DataBean item : rsp.data) {
                    //开始时间
                    long fromDataTime = DateUtils.getWhenPoint(item.fromDateTime);
                    //结束时间
                    String minute = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                    int weekDay = c.get(Calendar.DAY_OF_WEEK);
                    long toDateTime = DateUtils.getWhenPoint(item.toDateTime);
                    long mMillisecond = DateUtils.getWhenPoint(minute);
                    if (weekDay == 1) {//系统日历周日默认==1
                        weekDay = 7;
                    } else {
                        weekDay = weekDay - 1;
                    }
                    if (item.weekTime == weekDay) {
                        if (mMillisecond > toDateTime) {//课后
                            isTable = true;
                        } else if (mMillisecond < fromDataTime) {//课前
                            dataBean = item;
                            table_next.setText("下一节");
                            break;
                        } else {//正在上课
                            table_next.setText("本节课");
                            dataBean = item;
                            break;
                        }
                    }
                }
            }
        }
        if (dataBean != null) {
            setTableMsg(dataBean);
        } else if (isTable) {
            setDefaultView("今日课已上完");
        } else {
            setDefaultView("今日无课");
        }
    }

    private void setDefaultView(String string) {
        className.setText(string);
        subjectName.setText("");
        time.setText("");
        tips.setText("");
        table_next.setVisibility(View.GONE);
        iv_tips.setVisibility(View.GONE);
        iv_logo.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())
                || BaseConstant.TYPE_PREPARES_SAVE.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", TableFragment.class.getSimpleName());
            getData();
        }
    }

    private void getData() {
        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
            if (SpData.getClassInfo() != null) {
                mvpPresenter.selectClassInfoByClassId(SpData.getClassInfo().classesId);
            } else {
                setDefaultView("今日无课");
            }
        } else {
            mvpPresenter.SelectSchByTeaid();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void setTableMsg(SelectSchByTeaidRsp.DataBean rsp) {
        subjectName.setText(rsp.subjectName);
        className.setText(rsp.classesName);
        time.setText(rsp.fromDateTime + "-" + rsp.toDateTime);
        tips.setText(TextUtils.isEmpty(rsp.beforeClass) ? "未设置课前提醒" : rsp.beforeClass);
        table_next.setVisibility(View.VISIBLE);
        iv_tips.setVisibility(View.VISIBLE);
        iv_logo.setVisibility(View.VISIBLE);
    }

    @Override
    public void SelectSchByTeaidFail(String msg) {
        setDefaultView("今日无课");
    }
}
