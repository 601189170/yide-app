package com.yyide.chatim.fragment;

import android.content.Intent;
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
import com.yyide.chatim.activity.PersonInfoActivity;
import com.yyide.chatim.adapter.NotelistAdapter;
import com.yyide.chatim.adapter.NotelistAdapter2;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.NoteTabBean;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.presenter.NoteBookByListPresenter;
import com.yyide.chatim.view.NoteByListBookView;

import java.util.ArrayList;
import java.util.List;

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


    int type;
    int sum;
    List<listByAppRsp.DataBean.ListBean.ZBListBean> listBean=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.layout_notebylist_fragmnet, container, false);
        id=getArguments().getString("id");
        sum=getArguments().getInt("size");
        listBean.clear();
        for (int i=0;i<sum;i++){

            listByAppRsp.DataBean.ListBean.ZBListBean bean= (listByAppRsp.DataBean.ListBean.ZBListBean) getArguments().getSerializable(i+"");
            Log.e("TAG", "ZBListBeanfragment: "+JSON.toJSONString(bean) );
            listBean.add(bean);
        }

        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new NotelistAdapter();
        adapter2=new NotelistAdapter2();

        if (listBean.size()==0){
            type=2;
            listview.setAdapter(adapter2);

            mvpPresenter.NoteBookByList(id,"","","","10","1");
        }else {
            type=1;
            listview.setAdapter(adapter);
            adapter.notifydata(listBean);
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (type==1){
                    NoteByListActivity activity= (NoteByListActivity) getActivity();
                    Bundle bundle=new Bundle();
                    bundle.putInt("size",adapter.list.get(position).list.size());
                    bundle.putString("id", String.valueOf(adapter.list.get(position).id));
                    bundle.putString("name", String.valueOf(adapter.list.get(position).name));
                    if (adapter.list.get(position).list.size()==0){
                        bundle.putBoolean("islast",true);
                    }else {
                        bundle.putBoolean("islast",false);
                    }

                    for (int i = 0; i < adapter.list.get(position).list.size(); i++) {
                        bundle.putSerializable(i+"", adapter.list.get(position).list.get(i));
                    }
                    activity.initDeptFragment2(bundle);
                }else {
//                    ToastUtils.showShort("跳转个人说明");
                    Intent intent=new Intent();
                    intent.putExtra("data",JSON.toJSONString(adapter2.getItem(position)));
                    intent.setClass(mActivity,PersonInfoActivity.class);
                    startActivity(intent);
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
