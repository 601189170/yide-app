package com.yyide.chatim.activity.notice;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.activity.notice.presenter.NoticeAnnouncementFragmentPresenter;
import com.yyide.chatim.activity.notice.view.NoticeAnnouncementFragmentView;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoticeAnnouncementListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeAnnouncementListFragment extends BaseMvpFragment<NoticeAnnouncementFragmentPresenter> implements NoticeAnnouncementFragmentView {
    private static final String TAG = "NoticeAnnouncementListF";
    List<NoticeAnnouncementModel> list = new ArrayList<>();
    BaseQuickAdapter adapter;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private static final int REQUEST_CODE = 100;

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
            Log.e(TAG, "onCreate: "+mParam1 );
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
        adapter = new BaseQuickAdapter<NoticeAnnouncementModel, BaseViewHolder>(R.layout.item_notice_announcement) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, NoticeAnnouncementModel o) {
                Log.e(TAG, "convert: "+o.toString());
                baseViewHolder
                        .setText(R.id.tv_notice, o.getNoticeTitle())
                        .setText(R.id.tv_notice_time, o.getNoticeTime())
                        .setText(R.id.tv_notice_author, o.getNoticeAuthor())
                        .setText(R.id.tv_notice_content, Html.fromHtml(o.getNoticeContent()));
                if ("1".equals(o.getStatus())){
                    TextView view1 = baseViewHolder.getView(R.id.tv_confirm);
                    view1.setText("已确认");
                    view1.setBackground(getActivity().getDrawable(R.drawable.bg_corners_gray2_18));
                    view1.setTextColor(getActivity().getResources().getColor(R.color.black10));
                    ImageView iconView = baseViewHolder.getView(R.id.iv_pic);
                    iconView.setImageDrawable(getActivity().getDrawable(R.drawable.ic_announcement_mark_read));
                }else {
                    TextView view1 = baseViewHolder.getView(R.id.tv_confirm);
                    view1.setText("去确认");
                    view1.setBackground(getActivity().getDrawable(R.drawable.bg_corners_blue_18));
                    view1.setTextColor(getActivity().getResources().getColor(R.color.white));
                    ImageView iconView = baseViewHolder.getView(R.id.iv_pic);
                    iconView.setImageDrawable(getActivity().getDrawable(R.drawable.ic_announcement));
                }
                baseViewHolder.getView(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                        if (mParam1.equals("my_notice")){
                            intent.putExtra("type",1);
                        }else {
                            intent.putExtra("type",2);
                        }
                        intent.putExtra("id",o.getId());
                        intent.putExtra("status",o.getStatus());
                        //startActivity(intent);
                        startActivityForResult(intent,REQUEST_CODE);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(adapter);
        mvpPresenter.noticeList(1,1,10);
    }

    @Override
    protected NoticeAnnouncementFragmentPresenter createPresenter() {
        return new NoticeAnnouncementFragmentPresenter(this);
    }

    @Override
    public void noticeList(NoticeListRsp noticeListRsp) {
        Log.e(TAG, "noticeList: "+noticeListRsp.toString() );
        List<NoticeListRsp.DataBean.RecordsBean> records = noticeListRsp.getData().getRecords();
        list.clear();
        if (!records.isEmpty()){
            for (NoticeListRsp.DataBean.RecordsBean record : records) {
                //yyyy-MM-dd HH:mm 03.06 09:00
                String productionTime = DateUtils.formatTime(record.getProductionTime(),null,null);
                list.add(new NoticeAnnouncementModel(record.getId(),record.getTitle(),record.getProductionTarget(),record.getContent(),productionTime,record.getStatus()));
            }
            adapter.setList(list);
        }
    }

    @Override
    public void noticeListFail(String msg) {
        Log.e(TAG, "noticeListFail: "+msg );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode:"+requestCode+", resultCode:"+resultCode );
        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK){
            mvpPresenter.noticeList(1,1,10);
        }
    }
}