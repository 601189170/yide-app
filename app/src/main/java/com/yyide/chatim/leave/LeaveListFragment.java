package com.yyide.chatim.leave;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yyide.chatim.R;
import com.yyide.chatim.adapter.LeavelistAdapter;
import com.yyide.chatim.base.BaseFragment;

import androidx.annotation.Nullable;
import butterknife.BindView;


public class LeaveListFragment extends BaseFragment {

    @BindView(R.id.listview)
    ListView listview;
    private View mBaseView;
    LeavelistAdapter leavelistAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.leave_list_fragmnet, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        leavelistAdapter=new LeavelistAdapter();
        listview.setAdapter(leavelistAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(mActivity,LeaveInfoActivity.class));
            }
        });

    }


}
