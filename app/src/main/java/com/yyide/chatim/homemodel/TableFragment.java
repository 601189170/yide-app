package com.yyide.chatim.homemodel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.yyide.chatim.utils.GlideUtil;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.BindView;
import okhttp3.OkHttpClient;


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
        tablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, TableActivity.class);
                startActivity(intent);
            }
        });
//        listTimeData(SpData.Schoolinfo().data.get(0).parentId);
//        listTimeData(1983);
        mvpPresenter.SelectSchByTeaid();
//        listTimeData();
    }

    @Override
    protected TablePresenter createPresenter() {
        return new TablePresenter(this);
    }

    @Override
    public void SelectSchByTeaid(SelectSchByTeaidRsp rsp) {
        Log.e(TAG, "SelectSchByTeaid: " + JSON.toJSONString(rsp));
        SelectSchByTeaidRsp.DataBean dataBean = null;
        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
            if (rsp.data.size() > 0) {
                if (SpData.getClassInfo() != null && !TextUtils.isEmpty(SpData.getClassInfo().classesId)) {
                    for (SelectSchByTeaidRsp.DataBean item : rsp.data) {
                        if (dataBean == null && item.classesId.equals(SpData.getClassInfo().classesId)) {
                            //开始时间
                            long fromDataTime = DateUtils.getWhenPoint(item.fromDateTime);
                            //结束时间
                            long toDateTime = DateUtils.getWhenPoint(item.toDateTime);
                            Calendar c = Calendar.getInstance();
                            String minute = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                            int weekDay = c.get(Calendar.DAY_OF_WEEK);
                            long mMillisecond = DateUtils.getWhenPoint(minute);
                            if (item.weekTime == (weekDay - 1)) {
                                if (item.weekTime > (weekDay - 1)) {//课前
                                    dataBean = item;
                                    break;
                                } else {
                                    if (mMillisecond > toDateTime) {//课后
                                        className.setText("今日无课");
                                        break;
                                    } else if (mMillisecond < fromDataTime) {//课前
                                        dataBean = item;
                                        break;
                                    } else {//正在上课
                                        dataBean = item;
                                        break;
                                    }
                                }
                            } else {
                                className.setText("今日无课");
                                break;
                            }
                        } else {
                            className.setText("今日无课");
                            break;
                        }
                    }
                } else {
                    className.setText("今日无课");
                }
                if (dataBean != null) {
                    setTableMsg(dataBean);
                }
            } else {
                className.setText("今日无课");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", TableFragment.class.getSimpleName());
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
        tips.setText(rsp.subjectName);
    }

    @Override
    public void SelectSchByTeaidFail(String msg) {

    }
}
