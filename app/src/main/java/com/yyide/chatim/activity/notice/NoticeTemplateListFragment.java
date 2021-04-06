package com.yyide.chatim.activity.notice;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.presenter.NoticeTemplateListFragmentPresenter;
import com.yyide.chatim.activity.notice.view.NoticeTemplateListFragmentView;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.activity.notice.presenter.NoticeAnnouncementFragmentPresenter;
import com.yyide.chatim.activity.notice.view.NoticeAnnouncementFragmentView;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.model.TemplateListRsp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoticeTemplateListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeTemplateListFragment extends BaseMvpFragment<NoticeTemplateListFragmentPresenter> implements NoticeTemplateListFragmentView {
    private static final String TAG = "NoticeTemplateListFragm";
    List<TemplateListRsp.DataBean.RecordsBean.MessagesBean> list = new ArrayList<>();
    BaseQuickAdapter adapter;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "name";
    private static final String ARG_PARAM3 = "tempId";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private String name;
    private String tempId;

    public NoticeTemplateListFragment() {
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
    public static NoticeTemplateListFragment newInstance(String param1,String name,String tempId) {
        NoticeTemplateListFragment fragment = new NoticeTemplateListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, name);
        args.putString(ARG_PARAM3, tempId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            name = getArguments().getString(ARG_PARAM2);
            tempId = getArguments().getString(ARG_PARAM3);
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
        adapter = new BaseQuickAdapter<TemplateListRsp.DataBean.RecordsBean.MessagesBean, BaseViewHolder>(R.layout.item_notice_template) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, TemplateListRsp.DataBean.RecordsBean.MessagesBean o) {
                baseViewHolder
                        .setText(R.id.tv_title, o.getTitle().toString())
                        .setText(R.id.tv_desc, o.getName())
                        .setText(R.id.tv_notice_content, o.getTitle().toString());
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                NoticeAnnouncementModel model = (NoticeAnnouncementModel) adapter.getData().get(position);
            }
        });

        mRecyclerView.setAdapter(adapter);
        mvpPresenter.noticeTemplateList(name,tempId);

//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        list.add(new NoticeAnnouncementModel("放假通知", "刘校长发布", "Fragments的生命周期. 每一个fragments 都有自己的一套生命周期回调方法和处理自己的用户输入事件。. 对应生命周期可参考下图：. 详解Android Fragment之二：Fragment的创建和生命周期. 创建片元（Creating a Fragment）. To create a fragment, you must create a subclass of Fragment (or an existing subclass of it). The Fragment class has code that looks a lot like an Activity.", "2021-03-25 16点11分"));
//        adapter.setList(list);
    }

    @Override
    protected NoticeTemplateListFragmentPresenter createPresenter() {
        return new NoticeTemplateListFragmentPresenter(this);
    }


    @Override
    public void noticeTemplateList(TemplateListRsp templateListRsp) {
        Log.e(TAG, "noticeTemplateList: " + templateListRsp.toString());
        if (templateListRsp.getCode() == 200) {
            TemplateListRsp.DataBean data = templateListRsp.getData();
            List<TemplateListRsp.DataBean.RecordsBean> records = data.getRecords();
            if (!records.isEmpty()){
                list.clear();
                TemplateListRsp.DataBean.RecordsBean recordsBean = records.get(0);
                List<TemplateListRsp.DataBean.RecordsBean.MessagesBean> messages = recordsBean.getMessages();
                list.addAll(messages);
                adapter.setList(list);
            }
        }
    }

    @Override
    public void noticeTemplateListFail(String msg) {
        Log.e(TAG, "noticeTemplateListFail: " + msg);
    }
}