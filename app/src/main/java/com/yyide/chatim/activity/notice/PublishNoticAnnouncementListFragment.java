package com.yyide.chatim.activity.notice;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.presenter.NoticeAnnouncementFragmentPresenter;
import com.yyide.chatim.activity.notice.presenter.PublishAnnouncementFragmentPresenter;
import com.yyide.chatim.activity.notice.view.NoticeAnnouncementFragmentView;
import com.yyide.chatim.activity.notice.view.PublishAnnouncementFragmentView;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.DialogUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PublishNoticAnnouncementListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublishNoticAnnouncementListFragment extends BaseMvpFragment<PublishAnnouncementFragmentPresenter> implements PublishAnnouncementFragmentView {
    private static final String TAG = "NoticeAnnouncementListF";
    List<NoticeListRsp.DataBean.RecordsBean> list = new ArrayList<>();
    BaseQuickAdapter adapter;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public PublishNoticAnnouncementListFragment() {
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
    public static PublishNoticAnnouncementListFragment newInstance(String param1) {
        PublishNoticAnnouncementListFragment fragment = new PublishNoticAnnouncementListFragment();
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
            Log.e(TAG, "onCreate: " + mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BaseQuickAdapter<NoticeListRsp.DataBean.RecordsBean, BaseViewHolder>(R.layout.item_my_publish_announcement) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, NoticeListRsp.DataBean.RecordsBean record) {
                String productionTime = DateUtils.switchCreateTime(record.getProductionTime(), "MM.dd HH:mm");
                int readNumber = record.getReadNumber();
                int totalNumber = record.getTotalNumber();
                String confirm_number_format = getActivity().getResources().getString(R.string.notice_confirm_number);
                String notice_send_obj = getActivity().getResources().getString(R.string.notice_send_obj);
                String confirmNumber = String.format(confirm_number_format, readNumber, totalNumber);
                String sendObj = String.format(notice_send_obj, record.getProductionTarget());
                baseViewHolder
                        .setText(R.id.tv_notice, record.getTitle())
                        .setText(R.id.tv_notice_time, productionTime)
                        .setText(R.id.tv_notice_author, record.getProductionTarget())
                        .setText(R.id.tv_confirm_number, confirmNumber)
                        .setText(R.id.tv_send_obj, sendObj)
                        .setText(R.id.tv_notice_content, record.getContent());
                int id = record.getId();
                baseViewHolder.getView(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.hintDialog(getActivity(), "确定删除？", "删除通知公告并将其他相关信息一并删除", null, null, new DialogUtil.OnClickListener() {
                            @Override
                            public void onCancel(View view) {

                            }

                            @Override
                            public void onEnsure(View view) {
                                Log.e(TAG, "删除: " + id);
                                mvpPresenter.delAnnouncement(id);
                            }
                        });
                    }
                });
                baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                        intent.putExtra("type", 3);
                        intent.putExtra("id", record.getId());
                        intent.putExtra("status", record.getStatus());
                        startActivity(intent);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(adapter);
        mvpPresenter.noticeList(2, 1, 10);
    }

    @Override
    protected PublishAnnouncementFragmentPresenter createPresenter() {
        return new PublishAnnouncementFragmentPresenter(this);
    }

    @Override
    public void noticeList(NoticeListRsp noticeListRsp) {
        Log.e(TAG, "noticeList: " + noticeListRsp.toString());
        List<NoticeListRsp.DataBean.RecordsBean> records = noticeListRsp.getData().getRecords();
        list.clear();
        if (!records.isEmpty()) {
            list.addAll(records);
            adapter.setList(list);
        }
    }

    @Override
    public void noticeListFail(String msg) {
        Log.e(TAG, "noticeListFail: " + msg);
    }

    @Override
    public void deleteAnnouncement(BaseRsp baseRsp) {
        Log.e(TAG, "deleteAnnouncement: " + baseRsp.toString());
        if (baseRsp.getCode() == 200) {
            ToastUtils.showShort("删除成功！");
            mvpPresenter.noticeList(2, 1, 10);
        }
    }

    @Override
    public void deleteFail(String msg) {
        ToastUtils.showShort("删除成功！");
    }
}