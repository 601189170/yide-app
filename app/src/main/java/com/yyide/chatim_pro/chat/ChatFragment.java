package com.yyide.chatim_pro.chat;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.yyide.chatim_pro.BaseApplication;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.FriendProfileActivity;
import com.yyide.chatim_pro.chat.helper.ChatLayoutHelper;
import com.yyide.chatim_pro.utils.Constants;
import com.yyide.chatim_pro.utils.DemoLog;

import java.util.List;

import butterknife.BindView;


public class ChatFragment extends BaseFragment {

    ImageView back;
    TextView title;
    @BindView(R.id.chat_layout)
    ChatLayout chatLayout;
    private View mBaseView;
    private ChatLayout mChatLayout;
    private TitleBarLayout mTitleBar;
    private ChatInfo mChatInfo;

    private static final String TAG = ChatFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.chat_fragment, container, false);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return mBaseView;
        }
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        return mBaseView;
    }

    private void initView() {
        //从布局文件中获取聊天面板组件
        mChatLayout = mBaseView.findViewById(R.id.chat_layout);
        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault();
        title = mBaseView.findViewById(R.id.text);
        back = mBaseView.findViewById(R.id.back);
        title.setText(mChatInfo.getChatName());
        back.setOnClickListener(v -> getActivity().finish());
        /*
         * 需要聊天的基本信息
         */
        mChatLayout.setChatInfo(mChatInfo);
        //获取messageLayout UI
        MessageLayout messageLayout = mChatLayout.getMessageLayout();
        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();
        InputLayout inputLayout = mChatLayout.getInputLayout();
        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        mTitleBar.setOnLeftClickListener(view -> getActivity().finish());
        mTitleBar.setVisibility(GONE);
//        mTitleBar.getRightIcon().setImageDrawable(getResources().getDrawable(R.drawable.create_c2c));

        if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
            mTitleBar.setOnRightClickListener(v -> {
                Intent intent = new Intent(BaseApplication.getInstance(), FriendProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, mChatInfo);
                BaseApplication.getInstance().startActivity(intent);
            });
        }
//        mChatLayout.setForwardSelectActivityListener(new AbsChatLayout.onForwardSelectActivityListener(){
//            @Override
//            public void onStartForwardSelectActivity(int mode, List<MessageInfo> msgIds) {
//                mForwardMode = mode;
//                mForwardSelectMsgInfos = msgIds;
//
//                Intent intent = new Intent(BaseApplication.getInstance(), ForwardSelectActivity.class);
//                intent.putExtra(ForwardSelectActivity.FORWARD_MODE, mode);
//                startActivityForResult(intent, TUIKitConstants.FORWARD_SELECT_ACTIVTY_CODE);
//            }
//        });

//        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemLongClickListener() {
//            @Override
//            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
//                //因为adapter中第一条为加载条目，位置需减1
//                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
//            }
//
//            @Override
//            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
//                if (null == messageInfo) {
//                    return;
//                }

//                V2TIMMergerElem mergerElem = messageInfo.getTimMessage().getMergerElem();
//                if (mergerElem != null){
//                    Intent intent = new Intent(BaseApplication.getInstance(), ForwardChatActivity.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable(TUIKitConstants.FORWARD_MERGE_MESSAGE_KEY, messageInfo);
//                    intent.putExtras(bundle);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    BaseApplication.getInstance().startActivity(intent);
//                }else {
//                    ChatInfo info = new ChatInfo();
//                    info.setId(messageInfo.getFromUser());
//                    Intent intent = new Intent(BaseApplication.getInstance(), FriendProfileActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, info);
//                    BaseApplication.getInstance().startActivity(intent);
//                }
//            }
//        });

//        mChatLayout.getInputLayout().setStartActivityListener(new InputLayout.OnStartActivityListener() {
//            @Override
//            public void onStartGroupMemberSelectActivity() {
//                Intent intent = new Intent(BaseApplication.getInstance(), StartGroupMemberSelectActivity.class);
//                GroupInfo groupInfo = new GroupInfo();
//                groupInfo.setId(mChatInfo.getId());
//                groupInfo.setChatName(mChatInfo.getChatName());
//                intent.putExtra(TUIKitConstants.Group.GROUP_INFO, groupInfo);
//                startActivityForResult(intent, 1);
//            }
//        });

        if (false/*mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP*/) {
            V2TIMManager.getConversationManager().getConversation(mChatInfo.getId(), new V2TIMValueCallback<V2TIMConversation>() {
                @Override
                public void onError(int code, String desc) {
                    Log.e(TAG, "getConversation error:" + code + ", desc:" + desc);
                }

                @Override
                public void onSuccess(V2TIMConversation v2TIMConversation) {
                    if (v2TIMConversation == null) {
                        DemoLog.d(TAG, "getConversation failed");
                        return;
                    }
                    mChatInfo.setAtInfoList(v2TIMConversation.getGroupAtInfoList());

                    final V2TIMMessage lastMessage = v2TIMConversation.getLastMessage();

                    updateAtInfoLayout();
                    mChatLayout.getAtInfoLayout().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final List<V2TIMGroupAtInfo> atInfoList = mChatInfo.getAtInfoList();
                            if (atInfoList == null || atInfoList.isEmpty()) {
                                mChatLayout.getAtInfoLayout().setVisibility(GONE);
                                return;
                            } else {
                                mChatLayout.getChatManager().getAtInfoChatMessages(atInfoList.get(atInfoList.size() - 1).getSeq(), lastMessage, new IUIKitCallBack() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        mChatLayout.getMessageLayout().scrollToPosition((int) atInfoList.get(atInfoList.size() - 1).getSeq());
                                        LinearLayoutManager mLayoutManager = (LinearLayoutManager) mChatLayout.getMessageLayout().getLayoutManager();
                                        mLayoutManager.scrollToPositionWithOffset((int) atInfoList.get(atInfoList.size() - 1).getSeq(), 0);

                                        atInfoList.remove(atInfoList.size() - 1);
                                        mChatInfo.setAtInfoList(atInfoList);

                                        updateAtInfoLayout();
                                    }

                                    @Override
                                    public void onError(String module, int errCode, String errMsg) {
                                        DemoLog.d(TAG, "getAtInfoChatMessages failed");
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    private void updateAtInfoLayout() {
        int atInfoType = getAtInfoType(mChatInfo.getAtInfoList());
        switch (atInfoType) {
            case V2TIMGroupAtInfo.TIM_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(BaseApplication.getInstance().getString(R.string.ui_at_me));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(BaseApplication.getInstance().getString(R.string.ui_at_all));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(BaseApplication.getInstance().getString(R.string.ui_at_all_me));
                break;
            default:
                mChatLayout.getAtInfoLayout().setVisibility(GONE);
                break;

        }
    }

    private int getAtInfoType(List<V2TIMGroupAtInfo> atInfoList) {
        int atInfoType = 0;
        boolean atMe = false;
        boolean atAll = false;

        if (atInfoList == null || atInfoList.isEmpty()) {
            return V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        for (V2TIMGroupAtInfo atInfo : atInfoList) {
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ME) {
                atMe = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL) {
                atAll = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME) {
                atMe = true;
                atAll = true;
                continue;
            }
        }

        if (atAll && atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME;
        } else if (atAll) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL;
        } else if (atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ME;
        } else {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        return atInfoType;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            String result_id = data.getStringExtra(TUIKitConstants.Selection.USER_ID_SELECT);
            String result_name = data.getStringExtra(TUIKitConstants.Selection.USER_NAMECARD_SELECT);
            mChatLayout.getInputLayout().updateInputText(result_name, result_id);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getArguments();
        assert bundle != null;
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        if (mChatInfo == null) {
            return;
        }
        initView();

        // TODO 通过api设置ChatLayout各种属性的样例
        ChatLayoutHelper helper = new ChatLayoutHelper(getActivity());
        helper.setGroupId(mChatInfo.getId());
        helper.customizeChatLayout(mChatLayout);
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioPlayer.getInstance().stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
    }
}
