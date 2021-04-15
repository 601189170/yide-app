package com.yyide.chatim.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.activity.NoteBookActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.chat.ConversationFragment;
import com.yyide.chatim.homemodel.TableFragment;
import com.yyide.chatim.model.EventMessage;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


public class MessageFragment extends BaseFragment {
    private static final String TAG = "MessageFragment";
    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.line1)
    TextView line1;
    @BindView(R.id.line2)
    TextView line2;
    @BindView(R.id.note)
    LinearLayout note;
    private View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.message_fragmnet, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        int type = getArguments().getInt("type", 0);
        Log.e(TAG, "onViewCreated: " + type);
        setTab(type);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged: hidden = " + hidden);
        if (!hidden) {
            int type = getArguments().getInt("type", 0);
            Log.e(TAG, "onHiddenChanged: " + type);
            if (type != 0) {
                setTab(type);
            }
        }
    }

    void setTab(int position) {
        tab1.setChecked(false);
        tab2.setChecked(false);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fg1 = fm.findFragmentByTag(String.valueOf(tab1.getId()));
        Fragment fg2 = fm.findFragmentByTag(String.valueOf(tab2.getId()));

        if (fg1 != null) ft.hide(fg1);
        if (fg2 != null) ft.hide(fg2);

        switch (position) {
            case 0:
                if (fg1 == null) {
                    fg1 = new ConversationFragment();
                    ft.add(R.id.content, fg1, String.valueOf(tab1.getId()));
                } else
                    ft.show(fg1);
                tab1.setChecked(true);
                line1.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (fg2 == null) {
                    fg2 = new TodoMsgFragment();
                    ft.add(R.id.content, fg2, String.valueOf(tab2.getId()));
                } else
                    ft.show(fg2);
                tab2.setChecked(true);
                line2.setVisibility(View.VISIBLE);
                break;
        }
        ft.commitAllowingStateLoss();
    }

    @OnClick({R.id.tab1, R.id.tab2, R.id.note})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                setTab(0);
                break;
            case R.id.tab2:
                setTab(1);
                break;
            case R.id.note:
                startActivity(new Intent(mActivity, NoteBookActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", MessageFragment.class.getSimpleName());

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
