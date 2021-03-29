package com.yyide.chatim.leave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.yyide.chatim.R;
import com.yyide.chatim.adapter.SectionAdapter;
import com.yyide.chatim.base.BaseFragment;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;


public class MyLeaveFragmentByStudent extends BaseFragment {

    @BindView(R.id.check)
    CheckedTextView check;
    @BindView(R.id.grid)
    GridView grid;
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.s1)
    LinearLayout s1;
    private View mBaseView;

    SectionAdapter sectionAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.my_leave_fragmnet, container, false);

//
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sectionAdapter=new SectionAdapter();
        grid.setAdapter(sectionAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sectionAdapter.setIndex(position);
            }
        });
    }


    @OnClick({R.id.check, R.id.s1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check:
                if (check.isChecked()){
                    check.setChecked(false);
                }else {
                    check.setChecked(true);
                }
                break;
            case R.id.s1:
                break;
        }
    }
}
