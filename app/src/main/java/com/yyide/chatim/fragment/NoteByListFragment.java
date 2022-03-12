package com.yyide.chatim.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.NoteByListActivity;
import com.yyide.chatim.activity.PersonInfoActivity;
import com.yyide.chatim.adapter.NoteItemAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.ListByAppRsp2;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.ListByAppRsp;
import com.yyide.chatim.presenter.NoteBookByListPresenter;
import com.yyide.chatim.view.NoteByListBookView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;


public class NoteByListFragment extends BaseMvpFragment<NoteBookByListPresenter> implements NoteByListBookView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private View mBaseView;

    private String id;
    private int pageNum = 1;
    private int pageSize = 30;
    private NoteItemAdapter noteItemAdapter;
    private String islast;
    private String organization;
    ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> listBean = new ArrayList<>();
    ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO> nowBean = new ArrayList<>();
    public static final String PARAMS_NAME = "listBean";
    public static final String PARAMS_NAME2 = "nowBean";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.layout_notebylist_fragmnet, container, false);
        id = getArguments().getString("id");
        islast = getArguments().getString("islast");
        organization = getArguments().getString("organization");
        listBean = getArguments().getParcelableArrayList(PARAMS_NAME);
        nowBean = getArguments().getParcelableArrayList(PARAMS_NAME2);
        Log.e("TAG", "NoteByListFragment: "+ JSON.toJSONString(listBean));
        Log.e("TAG", "NoteByListFragment==》: "+ JSON.toJSONString(nowBean));
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        sendRequset();
    }

    private void sendRequset() {
        if ("2".equals(islast) && "student".equals(organization)) {//代表还要下一级不查询当前Id数据
            id = "0";
        }
        //不穿ID表示查询当下你所属部门人员或学生列表
        if (!TextUtils.isEmpty(organization) && "staff".equals(organization)) {
            mvpPresenter.NoteBookByList(id, pageSize, pageNum);
        } else {
            mvpPresenter.getStudentList(id, pageSize, pageNum);
        }
    }

    private void initView() {
        noteItemAdapter = new NoteItemAdapter();
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.color_DCDFE6)));
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setAdapter(noteItemAdapter);

        List<TeacherlistRsp.DataBean.RecordsBean> list = new ArrayList<>();
        if (listBean != null && listBean.size() > 0) {
            //添加组织部门数据
            if (listBean != null) {

                for (ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO item : listBean) {
                    TeacherlistRsp.DataBean.RecordsBean bean = new TeacherlistRsp.DataBean.RecordsBean();
                    bean.organizationItem = item;

                    bean.itemType = 1;
                    bean.name = item.name;

                    bean.id = Long.parseLong(item.id);
                    list.add(bean);
                }
            }
        }
        if (nowBean!=null){
            for (ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO item2  : nowBean) {
                TeacherlistRsp.DataBean.RecordsBean bean = new TeacherlistRsp.DataBean.RecordsBean();
                bean.organizationItem2 = item2;
                bean.itemType = 0;
                bean.name = item2.name;
                bean.phone =item2.phone;
                bean.email =item2.email;
                bean.sex =item2.gender;
                bean.subjectName =item2.subjectName;
//            bean.sex = String.valueOf(item2.gender);
                bean.id = Long.parseLong(item2.id);
                list.add(bean);
            }
        }

        noteItemAdapter.addData(list);
//        noteItemAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
//            //上拉加载时取消下拉刷新
//            noteItemAdapter.getLoadMoreModule().setEnableLoadMore(true);
//            //请求数据
//            pageNum++;
//            sendRequset();
//        });
        Log.e("TAG", "listBean: "+JSON.toJSONString(listBean) );
        if (list.size()==0){
            noteItemAdapter.setEmptyView(R.layout.empty);
        }
//        noteItemAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
//        noteItemAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        noteItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (noteItemAdapter.getItem(position).getItemType() == 1) {
                NoteByListActivity activity = (NoteByListActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(noteItemAdapter.getItem(position).id));
                bundle.putString("name", String.valueOf(noteItemAdapter.getItem(position).name));
                bundle.putString("organization", organization);
                ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> listBean = new ArrayList<>();
                ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO> nowBean = new ArrayList<>();
//                listBean.add(noteItemAdapter.getItem(position).organizationItem);
                Log.e("TAG", "noteItemAdapter: "+JSON.toJSONString(noteItemAdapter.getItem(position)) );
                Log.e("TAG", "setOnItemClickListener: "+JSON.toJSONString(noteItemAdapter.getItem(position).organizationItem) );
                Log.e("TAG", "setOnItemClickListener==》employeeAddBookDTOList: "+JSON.toJSONString(noteItemAdapter.getItem(position).organizationItem2) );
                bundle.putParcelableArrayList(PARAMS_NAME, (ArrayList<? extends Parcelable>) noteItemAdapter.getItem(position).organizationItem.children);
                bundle.putParcelableArrayList(PARAMS_NAME2, (ArrayList<? extends Parcelable>) noteItemAdapter.getItem(position).organizationItem.employeeAddBookDTOList);
                TeacherlistRsp.DataBean.RecordsBean item = noteItemAdapter.getItem(position);
                if (item.organizationItem.children != null && item.organizationItem.children.size() > 0) {
                    bundle.putString("islast", "2");
                } else {
                    bundle.putString("islast", "1");
                }

//                if (noteItemAdapter.getItem(position).organizationItem.children != null && noteItemAdapter.getItem(position).organizationItem.children.size() > 0) {
//                    bundle.putParcelableArrayList(PARAMS_NAME, (ArrayList<? extends Parcelable>) noteItemAdapter.getItem(position).organizationItem.children);
//                }
                activity.initDeptFragmentNew(bundle);
            } else {
                Intent intent = new Intent();
                noteItemAdapter.getItem(position);
                Log.e("TAG", "initView: "+JSON.toJSONString(noteItemAdapter.getItem(position)) );
                intent.putExtra("data", JSON.toJSONString(noteItemAdapter.getItem(position)));
                intent.putExtra("organization", organization);
                intent.setClass(mActivity, PersonInfoActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    protected NoteBookByListPresenter createPresenter() {
        return new NoteBookByListPresenter(this);
    }

    @Override
    public void TeacherlistRsp(TeacherlistRsp rsp) {
        Log.e("TAG", "TeacherlistRsp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCESS) {
            Log.e("TAG", "records: " + JSON.toJSONString(rsp.data.records));
//            adapter2.notifdata(rsp.data.records);
            noteItemAdapter.addData(rsp.data.records);
            if (rsp.data.records.size() < pageSize) {
                //如果不够一页,显示没有更多数据布局
                noteItemAdapter.getLoadMoreModule().loadMoreEnd();
            } else {
                noteItemAdapter.getLoadMoreModule().loadMoreComplete();
            }
        }
    }

    @Override
    public void TeacherlistRspFail(String rsp) {

    }

    @Override
    public void studentListRsp(TeacherlistRsp rsp) {
        if (rsp.code == BaseConstant.REQUEST_SUCCESS) {
            Log.e("TAG", "records: " + JSON.toJSONString(rsp.data.records));
//            adapter2.notifdata(rsp.data.records);
            noteItemAdapter.addData(rsp.data.records);
            if (rsp.data.records.size() < pageSize) {
                //如果不够一页,显示没有更多数据布局
                noteItemAdapter.getLoadMoreModule().loadMoreEnd();
            } else {
                noteItemAdapter.getLoadMoreModule().loadMoreComplete();
            }
        }
    }

    @Override
    public void studentListRspFail(String msg) {

    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
}
