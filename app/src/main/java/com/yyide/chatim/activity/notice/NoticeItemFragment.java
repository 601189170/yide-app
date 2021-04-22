package com.yyide.chatim.activity.notice;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.presenter.NoticeConfirmListFragmentPresenter;
import com.yyide.chatim.activity.notice.presenter.NoticeTemplateListFragmentPresenter;
import com.yyide.chatim.activity.notice.view.NoticeConfirmListFragmentView;
import com.yyide.chatim.activity.notice.view.NoticeTemplateListFragmentView;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.ConfirmDetailRsp;
import com.yyide.chatim.utils.GlideUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class NoticeItemFragment extends BaseMvpFragment<NoticeConfirmListFragmentPresenter> implements NoticeConfirmListFragmentView {
    private static final String TAG = "NoticeItemFragment";
    BaseQuickAdapter adapter;
    private static final String ARG_PARAM1 = "confirmType";
    private static final String ARG_PARAM2 = "signId";
    private static final String ARG_PARAM3 = "size";
    private int confirmType;
    private long signId;
    private int size;
    private List<ConfirmDetailRsp.DataBean.RecordsBean> recordsBeans = new ArrayList<>();
    // TODO: Customize parameter initialization
    public static NoticeItemFragment newInstance(int confirmType,long signId,int size) {
        NoticeItemFragment fragment = new NoticeItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, confirmType);
        args.putLong(ARG_PARAM2, signId);
        args.putInt(ARG_PARAM3, size);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            confirmType = getArguments().getInt(ARG_PARAM1);
            signId = getArguments().getLong(ARG_PARAM2);
            size = getArguments().getInt(ARG_PARAM3);
            Log.e(TAG,"confirmType:"+confirmType+",signId:"+signId+",size:"+size);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            initAdapter();
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
            //adapter.setList();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");
        mvpPresenter.getConfirmDetails(confirmType,signId,1,size);
    }

    private void initAdapter(){
        adapter = new BaseQuickAdapter<ConfirmDetailRsp.DataBean.RecordsBean, BaseViewHolder>(R.layout.fragment_notice_item) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, ConfirmDetailRsp.DataBean.RecordsBean str) {
                baseViewHolder.setText(R.id.tv_notice_scope_name, str.getUserName());
                //GlideUtil.loadImage(getContext(), "https://www.thecrazyprogrammer.com/wp-content/uploads/2017/05/Android-Glide-Tutorial-with-Example-1.png", baseViewHolder.findView(R.id.iv_pic));
                //ImageView view = baseViewHolder.findView(R.id.iv_pic);
                //view.setImageDrawable(getActivity().getDrawable(R.drawable.icon_leave));
                String userName = str.getUserName();
                if (!TextUtils.isEmpty(userName)) {
                    if (userName.length()<=2){
                        baseViewHolder.setText(R.id.tv_pic,str.getUserName());
                    }else {
                        baseViewHolder.setText(R.id.tv_pic,userName.substring(userName.length()-2));
                    }
                }
            }
        };


    }

    @Override
    public void noticeConfirmList(ConfirmDetailRsp confirmDetailRsp) {
        Log.e(TAG, "noticeConfirmList: "+confirmDetailRsp.toString() );
        if (confirmDetailRsp.getCode() == 200) {
            List<ConfirmDetailRsp.DataBean.RecordsBean> records = confirmDetailRsp.getData().getRecords();
            recordsBeans.addAll(records);
            adapter.setList(recordsBeans);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void noticeConfirmListFail(String msg) {

    }

    @Override
    protected NoticeConfirmListFragmentPresenter createPresenter() {
        return new NoticeConfirmListFragmentPresenter(this);
    }
}