package com.yyide.chatim.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.action.PopActionClickListener;
import com.tencent.qcloud.tim.uikit.component.action.PopDialogAdapter;
import com.tencent.qcloud.tim.uikit.component.action.PopMenuAction;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationProvider;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.holder.ConversationCommonHolder;
import com.tencent.qcloud.tim.uikit.modules.conversation.interfaces.IConversationAdapter;
import com.tencent.qcloud.tim.uikit.modules.conversation.interfaces.IConversationProvider;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.yyide.chatim.BaseApplication;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.BookSearchActivity;
import com.yyide.chatim.activity.MessageNoticeActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.chat.menu.Menu;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.UserInfo;
import com.yyide.chatim.model.UserMsgNoticeRsp;
import com.yyide.chatim.model.UserNoticeRsp;
import com.yyide.chatim.model.getUserSigRsp;
import com.yyide.chatim.presenter.UserNoticePresenter;
import com.yyide.chatim.utils.Constants;
import com.yyide.chatim.utils.DemoLog;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.UserNoticeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConversationFragment extends BaseMvpFragment<UserNoticePresenter> implements UserNoticeView {

    private View mBaseView;
    private ConversationLayout mConversationLayout;
    private ListView mConversationPopList;
    private PopDialogAdapter mConversationPopAdapter;
    private PopupWindow mConversationPopWindow;
    private List<PopMenuAction> mConversationPopActions = new ArrayList<>();
    private Menu mMenu;

    @BindView(R.id.cl_message)
    ConstraintLayout cl_message;
    @BindView(R.id.tv_user_notice_content)
    TextView tv_user_notice_content;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.tv_unNum)
    TextView tv_unNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.conversation_fragment, container, false);
        //initView();
        return mBaseView;
    }

    private void initView() {
        V2TIMManager.getConversationManager().getConversationList(0, 100, new V2TIMSendCallback<V2TIMConversationResult>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage("onError==>" + desc);
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                v2TIMConversationResult.getConversationList();
            }
        });

        // 从布局文件中获取会话列表面板
        mConversationLayout = mBaseView.findViewById(R.id.conversation_layout);
        mMenu = new Menu(getActivity(), (TitleBarLayout) mConversationLayout.getTitleBar(), Menu.MENU_TYPE_CONVERSATION);
        ConstraintLayout constraintLayout = mBaseView.findViewById(R.id.cl_message);
        LinearLayout ll_search = mBaseView.findViewById(R.id.ll_search);
        constraintLayout.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MessageNoticeActivity.class));
        });
        ll_search.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), BookSearchActivity.class));
        });
        // 会话列表面板的默认UI和交互初始化
        mConversationLayout.initDefault();
        ConversationListLayout listLayout = mConversationLayout.getConversationList();
        listLayout.setItemTopTextSize(16); // 设置 item 中 top 文字大小
        listLayout.setItemBottomTextSize(12);// 设置 item 中 bottom 文字大小
        listLayout.setItemDateTextSize(10);// 设置 item 中 timeline 文字大小
        listLayout.setItemAvatarRadius(150); // 设置 adapter item 头像圆角大小
        listLayout.disableItemUnreadDot(false);// 设置 item 是否不显示未读红点，默认显示
        // 通过API设置ConversataonLayout各种属性的样例，开发者可以打开注释，体验效果
//        ConversationLayoutHelper.customizeConversation(mConversationLayout);
        mConversationLayout.getConversationList().setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo conversationInfo) {
                //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
                startChatActivity(conversationInfo);
            }
        });
        mConversationLayout.getConversationList().setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo conversationInfo) {
                startPopShow(view, position, conversationInfo);
            }
        });

        initTitleAction();
        initPopMenuAction();
        TitleBarLayout titleBarLayout = mConversationLayout.findViewById(R.id.conversation_title);
        titleBarLayout.setVisibility(View.GONE);

////取控件当前的布局参数
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleBarLayout.getLayoutParams();
////设置宽度值
////      params.width = dip2px(getActivity(), width);
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
////设置高度值
//        params.height = SizeUtils.dp2px(100);
//        titleBarLayout.setLayoutParams(params);
//        titleBarLayout.setBackgroundColor(R.color.black);
//使设置好的布局参数应用到控件
//        imageView.setLayoutParams(params);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        mvpPresenter.getUserNoticePage(1, 1);
    }

    private void initTitleAction() {
        mConversationLayout.getTitleBar().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMenu.isShowing()) {
                    mMenu.hide();
                } else {
                    mMenu.show();
                }
            }
        });
    }

    private void initPopMenuAction() {

        // 设置长按conversation显示PopAction
        List<PopMenuAction> conversationPopActions = new ArrayList<PopMenuAction>();
        PopMenuAction action = new PopMenuAction();
        action.setActionName(getResources().getString(R.string.chat_top));
        action.setActionClickListener((position, data) -> mConversationLayout.setConversationTop(position, (ConversationInfo) data));
        conversationPopActions.add(action);
        action = new PopMenuAction();
        action.setActionClickListener((position, data) -> mConversationLayout.deleteConversation(position, (ConversationInfo) data));
        action.setActionName(getResources().getString(R.string.chat_delete));
        conversationPopActions.add(action);
        mConversationPopActions.clear();
        mConversationPopActions.addAll(conversationPopActions);
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    /**
     * 长按会话item弹框
     *
     * @param index            会话序列号
     * @param conversationInfo 会话数据对象
     * @param locationX        长按时X坐标
     * @param locationY        长按时Y坐标
     */
    private void showItemPopMenu(final int index, final ConversationInfo conversationInfo, float locationX, float locationY) {
        if (mConversationPopActions == null || mConversationPopActions.size() == 0)
            return;
        View itemPop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu_layout, null);
        mConversationPopList = itemPop.findViewById(R.id.pop_menu_list);
        mConversationPopList.setOnItemClickListener((parent, view, position, id) -> {
            PopMenuAction action = mConversationPopActions.get(position);
            if (action.getActionClickListener() != null) {
                action.getActionClickListener().onActionClick(index, conversationInfo);
            }
            mConversationPopWindow.dismiss();
        });

        for (int i = 0; i < mConversationPopActions.size(); i++) {
            PopMenuAction action = mConversationPopActions.get(i);
            if (conversationInfo.isTop()) {
                if (action.getActionName().equals(getResources().getString(R.string.chat_top))) {
                    action.setActionName(getResources().getString(R.string.quit_chat_top));
                }
            } else {
                if (action.getActionName().equals(getResources().getString(R.string.quit_chat_top))) {
                    action.setActionName(getResources().getString(R.string.chat_top));
                }

            }
        }
        mConversationPopAdapter = new PopDialogAdapter();
        mConversationPopList.setAdapter(mConversationPopAdapter);
        mConversationPopAdapter.setDataSource(mConversationPopActions);
        mConversationPopWindow = PopWindowUtil.popupWindow(itemPop, mBaseView, (int) locationX, (int) locationY);
        mBaseView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mConversationPopWindow.dismiss();
            }
        }, 10000); // 10s后无操作自动消失
    }

    private void startPopShow(View view, int position, ConversationInfo info) {
        showItemPopMenu(position, info, view.getX(), view.getY() + view.getHeight() / 2);
    }

    private void startChatActivity(ConversationInfo conversationInfo) {
        Log.e("TAG", "startChatActivity==>: " + JSON.toJSONString(conversationInfo));
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(conversationInfo.isGroup() ? V2TIMConversation.V2TIM_GROUP : V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(conversationInfo.getId());
        chatInfo.setChatName(conversationInfo.getTitle());
        Intent intent = new Intent(BaseApplication.getInstance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.getInstance().startActivity(intent);
    }

    @Override
    protected UserNoticePresenter createPresenter() {
        return new UserNoticePresenter(this);
    }

    @Override
    public void getUserNoticePageSuccess(UserMsgNoticeRsp userNoticeRsp) {
        if (userNoticeRsp.getCode() == BaseConstant.REQUEST_SUCCES2) {
            List<UserMsgNoticeRsp.DataBean.RecordsBean> records = userNoticeRsp.getData().getRecords();
            if (records != null && records.size() > 0) {
                final int total = userNoticeRsp.getData().getTotal();
                tv_unNum.setVisibility(View.VISIBLE);
                tv_unNum.setText(total + "");
            } else {
                tv_unNum.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void getUserNoticePageFail(String msg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_IM_LOGIN.equals(messageEvent.getCode()) ||
                BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            initView();
        }
    }

}
