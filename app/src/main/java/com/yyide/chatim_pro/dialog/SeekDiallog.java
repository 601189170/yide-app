package com.yyide.chatim_pro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.widget.TextView;

import com.yyide.chatim_pro.R;

import androidx.annotation.NonNull;


/**
 * Created by Hao on 2017/11/29.
 */

public class SeekDiallog extends Dialog {

    Context context;

    String str="设置串口成功,请重新启动";
    TextView msg;

    public SeekDiallog(@NonNull Context context, int theme) {
        super(context, theme);
    }


    public SeekDiallog(@NonNull Context context, String str) {
        this(context, R.style.shareDialog);
        this.context = context;
        this.str = str;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_layout);
        msg=findViewById(R.id.msg);
        msg.setText(str);


    }

}
