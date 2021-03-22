package com.yyide.chatim.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseFragment;

import androidx.annotation.Nullable;
import butterknife.BindView;


public class HelpFragment extends BaseFragment {


    @BindView(R.id.rmtext)
    TextView rmtext;
    @BindView(R.id.jjtext)
    TextView jjtext;
    private View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.help_layout, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rmtext.setText("视频和文字教程,\n帮助你快速上手");
        jjtext.setText("便捷省心的应用\n操作技巧");
    }


}
