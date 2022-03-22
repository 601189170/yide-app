package com.yyide.chatim.activity.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.EventMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class EmailActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_input_email)
    EditText email;

    @Override
    public int getContentViewID() {
        return R.layout.activity_email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("邮箱");
        String emailStr = getIntent().getStringExtra("email");
        email.setText(emailStr);
    }

    @OnClick(R.id.back_layout)
    void click() {
        finish();
    }

    @OnClick(R.id.save)
    void confirm() {
        String emailStr = email.getText().toString().trim();
        if (TextUtils.isEmpty(emailStr)) {
            ToastUtils.showShort("请输入邮箱");
            return;
        } else if (!emailStr.contains("@")) {
            ToastUtils.showShort("请输入正确的邮箱");
            return;
        }
        EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_USER_EMAIL, emailStr));
        finish();
    }
}