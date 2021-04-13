package com.yyide.chatim.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.MessageAdapter;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.chat.ConversationFragment;
import com.yyide.chatim.fragment.TodoMsgPageFragment;
import com.yyide.chatim.model.AgentInformationRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.presenter.TodoFragmentPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.TodoFragmentView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class TodoMsgFragment extends BaseFragment {
    private static final String TAG = "TodoMsgFragment";
    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.tab3)
    CheckedTextView tab3;
    private View mBaseView;
    @BindView(R.id.content)
    FrameLayout content;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.todomsg_fragment, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fg1 = fm.findFragmentByTag(String.valueOf(tab1.getId()));
        Fragment fg2 = fm.findFragmentByTag(String.valueOf(tab2.getId()));
        Fragment fg3 = fm.findFragmentByTag(String.valueOf(tab3.getId()));

        if (fg1 != null) ft.hide(fg1);
        if (fg2 != null) ft.hide(fg2);
        if (fg3 != null) ft.hide(fg3);

        switch (position) {
            case 0:
                if (fg1 == null) {
                    fg1 = TodoMsgPageFragment.newInstance("3");
                    ft.add(R.id.content, fg1, String.valueOf(tab1.getId()));
                } else
                    ft.show(fg1);
                tab1.setChecked(true);
                break;
            case 1:
                if (fg2 == null) {
                    fg2 = TodoMsgPageFragment.newInstance("0");
                    ft.add(R.id.content, fg2, String.valueOf(tab2.getId()));
                } else
                    ft.show(fg2);
                tab2.setChecked(true);
                break;
            case 2:
                if (fg3 == null) {
                    fg3 = TodoMsgPageFragment.newInstance("1");
                    ft.add(R.id.content, fg3, String.valueOf(tab3.getId()));
                } else
                    ft.show(fg3);
                tab3.setChecked(true);
                break;
        }
        ft.commitAllowingStateLoss();
    }
}
