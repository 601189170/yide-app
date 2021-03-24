package com.yyide.chatim.leave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseFragment;

import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;


public class MyLeaveFragment extends BaseFragment {

    private View mBaseView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.my_leave_fragmnet, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


}
