package com.yyide.chatim.homemodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseFragment;

import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;


public class WorkFragment extends BaseFragment {

    private View mBaseView;

    OkHttpClient mOkHttpClient = new OkHttpClient();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_work_fragmnet, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mvpPresenter.getMyData();



    }


}
