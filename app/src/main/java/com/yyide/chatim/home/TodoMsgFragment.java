package com.yyide.chatim.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.yyide.chatim.R;
import com.yyide.chatim.adapter.MessageAdapter;
import com.yyide.chatim.base.BaseFragment;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;


public class TodoMsgFragment extends BaseFragment {


    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.tab3)
    CheckedTextView tab3;
    @BindView(R.id.listview)
    ListView listview;
    private View mBaseView;

    MessageAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.todomsg_fragment, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new MessageAdapter();
        listview.setAdapter(adapter);
        setTab(0);

    }


    @OnClick({R.id.tab1, R.id.tab2, R.id.tab3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                setTab(0);
                break;
            case R.id.tab2:
                setTab(1);
                break;
            case R.id.tab3:
                setTab(2);
                break;
        }
    }


    void setTab(int position) {
        tab1.setChecked(false);
        tab2.setChecked(false);
        tab3.setChecked(false);
        switch (position) {
            case 0:
                tab1.setChecked(true);
                break;
            case 1:
                tab2.setChecked(true);
                break;
            case 2:
                tab3.setChecked(true);
                break;
        }

    }

}
