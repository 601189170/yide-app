package com.yyide.chatim.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.DeviceUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.model.GetStuasRsp;
import com.yyide.chatim.presenter.GetstuasPresenter;
import com.yyide.chatim.view.DeviceUpdataView;
import com.yyide.chatim.view.getstuasView;

import androidx.annotation.Nullable;


public class Fragment1 extends BaseMvpFragment<GetstuasPresenter> implements getstuasView {

    private View mBaseView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment1, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter.getMyData();
        mvpPresenter.getMyData2(DeviceUtils.getAndroidID(),"1",17113);

    }
    @Override
    protected GetstuasPresenter createPresenter() {

        return new GetstuasPresenter(Fragment1.this);
    }

    @Override
    public void getData(GetStuasRsp rsp) {
        Log.e("TAG", "GetStuasRsp==》: "+ JSON.toJSONString(rsp));
    }



    @Override
    public void getDataFail(String msg) {

    }

    @Override
    public void getData2(DeviceUpdateRsp rsp) {
        hideLoading();
        Log.e("TAG", "DeviceUpdateRsp==》: "+ JSON.toJSONString(rsp));
    }

    @Override
    public void getDataFail2(String msg) {
        Log.e("TAG", "getDataFail2(==》: "+ JSON.toJSONString(msg));
    }
}
