package com.yyide.chatim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.imsdk.v2.V2TIMConversation;

import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.FriendProfileLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.yyide.chatim.BaseApplication;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.MainActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.chat.ChatActivity;
import com.yyide.chatim.utils.Constants;

import androidx.annotation.Nullable;

public class FriendProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_friend_profile_activity);
        FriendProfileLayout layout = findViewById(R.id.friend_profile);

        layout.initData(getIntent().getSerializableExtra(TUIKitConstants.ProfileType.CONTENT));
        layout.setOnButtonClickListener(new FriendProfileLayout.OnButtonClickListener() {
            @Override
            public void onStartConversationClick(ContactItemBean info) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                chatInfo.setId(info.getId());
                String chatName = info.getId();
                if (!TextUtils.isEmpty(info.getRemark())) {
                    chatName = info.getRemark();
                } else if (!TextUtils.isEmpty(info.getNickname())) {
                    chatName = info.getNickname();
                }
                chatInfo.setChatName(chatName);
                Intent intent = new Intent(BaseApplication.getInstance(), ChatActivity.class);
                intent.putExtra(Constants.CHAT_INFO, chatInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApplication.getInstance().startActivity(intent);
            }

            @Override
            public void onDeleteFriendClick(String id) {
                Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
