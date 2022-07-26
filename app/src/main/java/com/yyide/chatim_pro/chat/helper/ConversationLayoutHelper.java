package com.yyide.chatim_pro.chat.helper;

import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;

public class ConversationLayoutHelper {

    public static void customizeConversation(final ConversationLayout layout) {

        ConversationListLayout listLayout = (ConversationListLayout) layout.getConversationList();
        listLayout.setItemTopTextSize(15); // 设置 item 中 top 文字大小
        listLayout.setItemBottomTextSize(12);// 设置 item 中 bottom 文字大小
        listLayout.setItemDateTextSize(14);// 设置 item 中 timeline 文字大小
        listLayout.setItemAvatarRadius(10); // 设置 adapter item 头像圆角大小
        listLayout.disableItemUnreadDot(false);// 设置 item 是否不显示未读红点，默认显示

        // 动态插入，删除Item，包括自定义会话
//        final ConversationInfo customInfo = new ConversationInfo();
//        customInfo.setType(ConversationInfo.TYPE_CUSTOM);
//        customInfo.setId("自定义会话");
//        customInfo.setGroup(false);
//        customInfo.setTitle("乔丹风行8代跑鞋 风随我动！");
//        customInfo.setIconUrl("https://img1.gtimg.com/ninja/2/2019/03/ninja155375585738456.jpg");
//
//
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                layout.addConversationInfo(0, customInfo);
//            }
//        }, 3000);

//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                layout.removeConversationInfo(0);
//            }
//        }, 5000);
    }

}
