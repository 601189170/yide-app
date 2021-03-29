package com.yyide.chatim.notice;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.notice.presenter.NoticeAnnouncementFragmentPresenter;
import com.yyide.chatim.notice.view.NoticeAnnouncementFragmentView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoticeAnnouncementListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeAnnouncementListFragment extends BaseMvpFragment<NoticeAnnouncementFragmentPresenter> implements NoticeAnnouncementFragmentView {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public NoticeAnnouncementListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment NoticeAnnouncementListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoticeAnnouncementListFragment newInstance(String param1) {
        NoticeAnnouncementListFragment fragment = new NoticeAnnouncementListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BaseQuickAdapter adapter = new BaseQuickAdapter<NoticeAnnouncementModel, BaseViewHolder>(R.layout.item_notice_announcement) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, NoticeAnnouncementModel o) {
                baseViewHolder
                        .setText(R.id.tv_notice, o.getNoticeTitle())
                        .setText(R.id.tv_notice_time, o.getNoticeTime())
                        .setText(R.id.tv_notice_author, o.getNoticeAuthor())
                        .setText(R.id.tv_notice_content, o.getNoticeContent());
                baseViewHolder.getView(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(adapter);
        List<NoticeAnnouncementModel> list = new ArrayList<>();
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
        adapter.setList(list);
    }

    @Override
    protected NoticeAnnouncementFragmentPresenter createPresenter() {
        return new NoticeAnnouncementFragmentPresenter(this);
    }
}