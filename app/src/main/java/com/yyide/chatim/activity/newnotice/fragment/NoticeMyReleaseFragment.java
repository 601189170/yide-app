package com.yyide.chatim.activity.newnotice.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.newnotice.NoticeDetailActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentNoticeMyPushListBinding;
import com.yyide.chatim.databinding.ItemNoticeMyPushBinding;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.presenter.NoticeReceivedPresenter;
import com.yyide.chatim.view.NoticeReceivedView;
import com.yyide.chatim.widget.itemDocoretion.ItemDecorationPowerful;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知公告
 * auther lrz
 * time  2021年6月17日11:00:27
 */
public class NoticeMyReleaseFragment extends BaseMvpFragment<NoticeReceivedPresenter> implements NoticeReceivedView {

    private final String TAG = NoticeMyReleaseFragment.class.getSimpleName();
    private FragmentNoticeMyPushListBinding viewBinding;

    public static NoticeMyReleaseFragment newInstance() {
        NoticeMyReleaseFragment fragment = new NoticeMyReleaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentNoticeMyPushListBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected NoticeReceivedPresenter createPresenter() {
        return new NoticeReceivedPresenter(this);
    }

    private void initView() {
        //默认选中第一个
        viewBinding.list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        viewBinding.list.addItemDecoration(new ItemDecorationPowerful(ItemDecorationPowerful.GRID_DIV, Color.TRANSPARENT, SizeUtils.dp2px(12)));
        viewBinding.list.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty);
        adapter.getEmptyLayout().setOnClickListener(v -> {
            //点击空数据界面刷新当前页数据
            ToastUtils.showShort("getEmptyLayout To Data");
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            startActivity(new Intent(getActivity(), NoticeDetailActivity.class));
        });

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        adapter.setList(list);
    }

    private final BaseQuickAdapter<String, BaseViewHolder> adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_notice_my_push) {

        @Override
        protected void convert(@NotNull BaseViewHolder holder, String s) {
            ItemNoticeMyPushBinding view = ItemNoticeMyPushBinding.bind(holder.itemView);
//            view.tvNoticeTitle.setText("");
//            view.tvNoticeTime.setText("");
            //view.ivNoticeImg
            //view.ivIconImg
        }
    };

    @Override
    public void getMyReceivedList(ResultBean model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {

        }
    }

    @Override
    public void getMyReceivedFail(String msg) {
        Log.d(TAG, "getMyReceivedFail>>>>>：" + msg);
    }
}