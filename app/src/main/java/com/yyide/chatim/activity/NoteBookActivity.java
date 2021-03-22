package com.yyide.chatim.activity;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteBookActivity extends BaseActivity {


    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_layout);
        ButterKnife.bind(this);


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
