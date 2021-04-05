package com.yyide.chatim.activity.notice;

import android.content.Context;
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
import com.yyide.chatim.utils.GlideUtil;

import org.jetbrains.annotations.NotNull;

/**
 * A fragment representing a list of Items.
 */
public class NoticeItemFragment extends Fragment {
    BaseQuickAdapter adapter;

    // TODO: Customize parameter initialization
    public static NoticeItemFragment newInstance() {
        NoticeItemFragment fragment = new NoticeItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            //adapter.setList();
        }
        return view;
    }

    private void initAdapter(){
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.fragment_notice_item) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, String str) {
                baseViewHolder.setText(R.id.tv_notice_scope_name, str);
                GlideUtil.loadImage(getContext(), "https://www.thecrazyprogrammer.com/wp-content/uploads/2017/05/Android-Glide-Tutorial-with-Example-1.png", baseViewHolder.findView(R.id.iv_pic));
            }
        };


    }
}