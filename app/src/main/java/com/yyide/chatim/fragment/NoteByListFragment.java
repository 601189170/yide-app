package com.yyide.chatim.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.NoteByListActivity;
import com.yyide.chatim.adapter.NotelistAdapter;
import com.yyide.chatim.adapter.NotelistAdapter2;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.NoteTabBean;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.presenter.NoteBookByListPresenter;
import com.yyide.chatim.view.NoteByListBookView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;


public class NoteByListFragment extends BaseMvpFragment<NoteBookByListPresenter> implements NoteByListBookView {


    @BindView(R.id.listview)
    ListView listview;
    private View mBaseView;

    NotelistAdapter adapter;
    String id;
    NotelistAdapter2 adapter2;

    String data;
    int type;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.layout_notebylist_fragmnet, container, false);
        id=getArguments().getString("id");
        data=getArguments().getString("data");


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new NotelistAdapter();
        adapter2=new NotelistAdapter2();

        if (TextUtils.isEmpty(data)){
            type=2;
            listview.setAdapter(adapter2);
            mvpPresenter.NoteBookByList(id,"","","","10","1");
        }else {
            type=1;
            listview.setAdapter(adapter);
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (type==1){
                    NoteByListActivity activity= (NoteByListActivity) getActivity();
                    activity.initDeptFragment();
                }else {
                    ToastUtils.showShort("跳转个人说明");
                }




            }
        });
    }

    @Override
    protected NoteBookByListPresenter createPresenter() {
        return new NoteBookByListPresenter(this);
    }


    @Override
    public void TeacherlistRsp(TeacherlistRsp rsp) {
        Log.e("TAG", "TeacherlistRsp: "+JSON.toJSONString(rsp) );
        if (rsp.code== BaseConstant.REQUEST_SUCCES2){
                adapter2.notifdata(rsp.data.records);
        }
    }

    @Override
    public void TeacherlistRspFail(String rsp) {

    }
}
