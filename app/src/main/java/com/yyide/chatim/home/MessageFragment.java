package com.yyide.chatim.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.widget.MsgView;
import com.tencent.mmkv.MMKV;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.book.NewBookActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.base.MMKVConstant;
import com.yyide.chatim.chat.ConversationFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.presenter.MessagePresenter;
import com.yyide.chatim.view.MessageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MessageFragment extends BaseMvpFragment<MessagePresenter> implements MessageView {
    private static final String TAG = "MessageFragment";
    @BindView(R.id.note)
    LinearLayout note;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.viewPager2)
    ViewPager viewPager;
    private MsgView msgView;
    private View mBaseView;
    private int type = 0;
    private TodoMsgFragment fragment;

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
        type = MMKV.defaultMMKV().decodeInt(MMKVConstant.MMKV_MAIN_JUMP_TYPE);
        fragment = new TodoMsgFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        Log.e(TAG, "onViewCreated: " + type);
        setTab(type);
        mvpPresenter.getMessageNumber();
    }

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged: hidden = " + hidden);
        type = MMKV.defaultMMKV().decodeInt(MMKVConstant.MMKV_MAIN_JUMP_TYPE);
        if (!hidden && type > 0) {
            if (mSlidingTabLayout != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", type);
                fragment.setArguments(bundle);
                mSlidingTabLayout.setCurrentTab(type);
                fragment.onHiddenChanged(false);
            }
        }
    }

    void setTab(int position) {
        List<String> mTitles = new ArrayList<>();
        mTitles.add("消息");
        mTitles.add("待办");
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new ConversationFragment();
                } else {
                    return fragment;
                }
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }

        });
        mSlidingTabLayout.setViewPager(viewPager);
        mSlidingTabLayout.setCurrentTab(position);// todo  默认选中 //第一次加载设置默认
    }

    @OnClick({R.id.note})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.note:
//                startActivity(new Intent(mActivity, NoteBookActivity.class));
                startActivity(new Intent(mActivity, NewBookActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_MESSAGE_TODO_NUM.equals(messageEvent.getCode())) {
            setNumber(messageEvent.getCount());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void messageNumberSuccess(TodoRsp model) {
        Log.d(TAG, "messageNumberSuccess>>:" + model.getData().getTotal());
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
            if (model.getData() != null) {
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MESSAGE_TODO_NUM, "", model.getData().getTotal()));
                setNumber(model.getData().getTotal());
            }
        }
    }

    private void setNumber(int count) {
        /**设置未读消息消息的背景*/
        msgView = mSlidingTabLayout.getMsgView(1);
        if (msgView != null) {
            msgView.setBackgroundColor(Color.parseColor("#fb2d24"));
        }
        if (count > 0) {
            mSlidingTabLayout.showMsg(1, count);
        } else {
            mSlidingTabLayout.hideMsg(1);
        }
    }

    @Override
    public void messageNumberFail(String msg) {
        Log.d(TAG, msg);
    }
}
