package com.yyide.chatim.fragment.leave;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.presenter.leave.AskForLeaveListPresenter;
import com.yyide.chatim.view.leave.AskForLeaveListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AskForLeaveListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskForLeaveListFragment extends BaseMvpFragment<AskForLeaveListPresenter> implements AskForLeaveListView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NoticeAnnouncementListF";
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public AskForLeaveListFragment() {
        // Required empty public constructor
    }

    public static AskForLeaveListFragment newInstance(String param1) {
        AskForLeaveListFragment fragment = new AskForLeaveListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.e(TAG, "onCreate: " + mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected AskForLeaveListPresenter createPresenter() {
        return new AskForLeaveListPresenter(this);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onRefresh() {

    }


    @Override
    public void leaveList(NoticeListRsp noticeListRsp) {

    }

    @Override
    public void leaveListFail(String msg) {

    }
}