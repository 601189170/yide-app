package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.book.BookSearchActivity;
import com.yyide.chatim.adapter.NoBookItemAdapter;
import com.yyide.chatim.adapter.NoteItemAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.fragment.NoteByListFragment;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.ListByAppRsp;
import com.yyide.chatim.model.ListByAppRsp2;
import com.yyide.chatim.model.ListByAppRsp3;
import com.yyide.chatim.model.SchoolRsp;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.TemplateListRsp;
import com.yyide.chatim.model.selectListByAppRsp;
import com.yyide.chatim.presenter.NoteBookPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.NoteBookView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteBookActivity extends BaseMvpActivity<NoteBookPresenter> implements NoteBookView {

    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.listview)
    ListView listview;
    NoBookItemAdapter adapter;
    @BindView(R.id.listview2)
    ListView listview2;
    NoBookItemAdapter adapter2;
    @BindView(R.id.original)
    FrameLayout original;
    @BindView(R.id.student)
    FrameLayout student;

    @BindView(R.id.pName)
    TextView pName;
    public String TAG = "tag";
    private String schoolType = "";
    private String mSchoolName = "";

    @BindView(R.id.layout_jz_all)
    LinearLayout layout_jz_all;

    @BindView(R.id.recyclerView_teacher)
    RecyclerView recyclerView_teacher;
    @BindView(R.id.recyclerView_student)
    RecyclerView recyclerView_student;

    NoteItemAdapter noteItemAdapter_teacher, noteItemAdapter_student;

    @BindView(R.id.teacher_btn)
    TextView teacher_btn;
    @BindView(R.id.student_btn)
    TextView student_btn;

    @Override
    public int getContentViewID() {
        return R.layout.activity_note_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("通讯录");

//        mvpPresenter.listByApp();
        layout_jz_all.setVisibility(View.VISIBLE);
        mvpPresenter.universitySelectListByApp();
        SchoolRsp schoolRsp = SpData.Schoolinfo();
        if (schoolRsp != null) {
            mSchoolName = schoolRsp.getSchoolName();
            pName.setText(schoolRsp.getSchoolName());
        }
        adapter = new NoBookItemAdapter();
        adapter2 = new NoBookItemAdapter();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener((parent, view, position, id) -> setPostData((ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO>) adapter.getItem(position).children, adapter.getItem(position).employeeAddBookDTOList, adapter.getItem(position).name, String.valueOf(adapter.getItem(position).id), adapter.getItem(position).name, "staff", ""));
//        listview.setOnItemClickListener((parent, view, position, id) -> setPostData(adapter.getItem(position),adapter.getItem(position).employeeAddBookDTOList, adapter.getItem(position).name, String.valueOf(adapter.getItem(position).id), adapter.getItem(position).name, "staff", ""));
//        listview2.setOnItemClickListener((parent, view, position, id) -> setPostData((ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO>) adapter2.getItem(position).children,adapter2.getItem(position).employeeAddBookDTOList, adapter2.getItem(position).name, String.valueOf(adapter2.getItem(position).id), adapter2.getItem(position).name, "student", ""));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_teacher.setLayoutManager(linearLayoutManager);
        recyclerView_student.setLayoutManager(linearLayoutManager2);
        noteItemAdapter_student = new NoteItemAdapter();
        noteItemAdapter_teacher = new NoteItemAdapter();
        recyclerView_student.setAdapter(noteItemAdapter_student);
        recyclerView_teacher.setAdapter(noteItemAdapter_teacher);
        noteItemAdapter_teacher.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                setPostData2(noteItemAdapter_teacher.getItem(position));
            }
        });
        noteItemAdapter_student.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                setPostData2(noteItemAdapter_student.getItem(position));
            }
        });

    }

    /**
     * @param list
     * @param id
     * @param name
     * @param organization 教职工staff  学生student
     */
    void setPostData(ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> list, ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO> nowlist, String schoolName, String id, String name, String organization, String type) {
        Intent intent = new Intent();
//        intent.putParcelableArrayListExtra("listBean", list);
//        intent.putParcelableArrayListExtra("nowBean", nowlist);
        Log.e(TAG, "setPostData: " + JSON.toJSONString(list));
        Log.e(TAG, "setPostData==>: " + JSON.toJSONString(nowlist));
        intent.putExtra("listBean", JSON.toJSONString(list));
        intent.putExtra("nowBean", JSON.toJSONString(nowlist));
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("schoolName", mSchoolName);
        intent.putExtra("schoolType", schoolType);
        intent.putExtra("type", type);
        intent.putExtra("organization", organization);
        intent.setClass(NoteBookActivity.this, NoteByListActivity.class);
        startActivity(intent);
    }

    //    void setPostData(ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO childrenDTO, ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO employeeAddBookDTOListDTO,String schoolName, String id, String name, String organization, String type) {
//        Intent intent = new Intent();
//
//        intent.putExtra(NoteByListFragment.PARAMS_NAME,JSON.toJSONString(childrenDTO));
//        intent.putExtra(NoteByListFragment.PARAMS_NAME2,JSON.toJSONString(employeeAddBookDTOListDTO));
//        Log.e(TAG, "setPostData: "+JSON.toJSONString(childrenDTO) );
//        Log.e(TAG, "setPostData==>: "+JSON.toJSONString(employeeAddBookDTOListDTO) );
//        intent.putExtra("id", id);
//        intent.putExtra("name", name);
//        intent.putExtra("schoolName", mSchoolName);
//        intent.putExtra("schoolType", schoolType);
//        intent.putExtra("type", type);
//        intent.putExtra("organization", organization);
//        intent.setClass(NoteBookActivity.this, NoteByListActivity.class);
//        startActivity(intent);
//    }
    void setPostData2(TeacherlistRsp.DataBean.RecordsBean itemBean) {


        Intent intent = new Intent();
        Log.e("TAG", "initView: " + JSON.toJSONString(itemBean));
        intent.putExtra("data", JSON.toJSONString(itemBean));
        intent.putExtra("organization", "student");
        intent.setClass(mActivity, PersonInfoActivity.class);
        startActivity(intent);
    }

    @Override
    protected NoteBookPresenter createPresenter() {
        return new NoteBookPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.back_layout, R.id.title, R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
            case R.id.title:
                finish();
                break;
            case R.id.ll_search:
                startActivity(new Intent(this, BookSearchActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void showError() {

    }

    //    @Override
//    public void listByApp(ListByAppRsp rsp) {
//        Log.e("TAG", "listByAppRsp: " + JSON.toJSONString(rsp));
//        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
//            //学校组织结构
//            ArrayList<ListByAppRsp.DataBean.ListBean> listBeans1 = new ArrayList<>();
//            if (rsp.data.size() > 0) {
//                if (!TextUtils.isEmpty(rsp.data.get(0).schoolLogo)) {
//                    GlideUtil.loadImageHead(NoteBookActivity.this, rsp.data.get(0).schoolLogo, img);
//                }
//                for (ListByAppRsp.DataBean.ListBean listBean : rsp.data.get(0).list) {
//                    listBeans1.add(listBean);
//                }
//                original.setOnClickListener(v -> setPostData(listBeans1, rsp.data.get(0).parentName, String.valueOf(rsp.data.get(0).id), rsp.data.get(0).name, "staff", "origin"));
//            }
//            adapter.notifyData(listBeans1);
//        }
//    }
    @Override
    public void listByApp(ListByAppRsp2 rsp) {
        Log.e("TAG", "listByAppRsp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES_0) {
            //学校组织结构
            ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> listBeans1 = new ArrayList<>();
            ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO> listBeans2 = new ArrayList<>();
            if (rsp.data.deptVOList.size() > 0) {
                pName.setText(rsp.data.schoolName);
                if (!TextUtils.isEmpty(rsp.data.schoolBadgeUrl)) {
                    GlideUtil.loadImageHead(NoteBookActivity.this, rsp.data.schoolBadgeUrl, img);
                }
                for (ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO listBean : rsp.data.deptVOList.get(0).children) {
                    listBeans1.add(listBean);
                }
                for (ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO listBean : rsp.data.deptVOList.get(0).employeeAddBookDTOList) {
                    listBeans2.add(listBean);
                }
                original.setOnClickListener(v -> setPostData(listBeans1, listBeans2, rsp.data.deptVOList.get(0).name, String.valueOf(rsp.data.deptVOList.get(0).id), rsp.data.deptVOList.get(0).name, "staff", "origin"));
            }
            Log.e("TAG", "listBeans1: " + JSON.toJSONString(listBeans1));
            adapter.notifyData(listBeans1);
        }
    }

    @Override
    public void selectListByApp(ListByAppRsp rsp) {
        Log.e("TAG", "listByAppRsp: " + JSON.toJSONString(rsp));
//        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
//            //小初高组织结构
//            ArrayList<ListByAppRsp.DataBean.ListBean> listBeans = new ArrayList<>();
//            if (rsp.data != null && rsp.data.size() > 0) {
//                for (ListByAppRsp.DataBean listBean : rsp.data) {
//                    ListByAppRsp.DataBean.ListBean item = new ListByAppRsp.DataBean.ListBean();
//                    item.name = listBean.name;
//                    item.id = listBean.id;
//                    item.list = listBean.list;
//                    listBeans.add(item);
//                }
//                student.setOnClickListener(v -> setPostData(listBeans, mSchoolName, rsp.data.get(0).id + "", mSchoolName, "student", "origin"));
//            }
//            listview2.setAdapter(adapter2);
//            adapter2.notifyData(listBeans);
//        }
    }

    @Override
    public void universityListByApp(ListByAppRsp3 rsp) {
        Log.e("TAG", "listByAppRsp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES_0) {
            List<TeacherlistRsp.DataBean.RecordsBean> list_teacher = new ArrayList<>();
            List<TeacherlistRsp.DataBean.RecordsBean> list_student = new ArrayList<>();
            pName.setText(rsp.data.schoolName);
            if (!TextUtils.isEmpty(rsp.data.schoolBadgeUrl)) {
                GlideUtil.loadImageHead(NoteBookActivity.this, rsp.data.schoolBadgeUrl, img);
            }
            if (rsp.data.adlist.size() > 0) {

                for (ListByAppRsp3.DataDTO.AdlistDTO adlistDTO : rsp.data.adlist) {
                    for (ListByAppRsp3.DataDTO.AdlistDTO.ElternListDTO item : adlistDTO.elternList) {
                        TeacherlistRsp.DataBean.RecordsBean bean = new TeacherlistRsp.DataBean.RecordsBean();
                        bean.organizationItem3 = item;
                        bean.itemType = 0;
                        bean.name = item.name;
                        bean.subjectName = item.subjectName;
                        bean.phone = item.phone;
                        bean.sex = item.gender;
                        bean.employeeSubjects = item.employeeSubjects;
                        bean.userId = Long.parseLong(item.userId);
                        bean.id = Long.parseLong(item.id);
                        list_teacher.add(bean);
                    }

                    for (ListByAppRsp3.DataDTO.AdlistDTO.StudentListDTO item : adlistDTO.studentList) {
                        TeacherlistRsp.DataBean.RecordsBean bean = new TeacherlistRsp.DataBean.RecordsBean();
                        bean.organizationItem4 = item;
                        bean.itemType = 0;
                        bean.name = item.name;
                        bean.phone = item.phone;
                        bean.address = item.address;
                        bean.sex = String.valueOf(item.gender);
//                        bean.userId = Long.parseLong(item.userId);

                        bean.id = Long.parseLong(item.id);
                        list_student.add(bean);
                    }
                }
            }
            noteItemAdapter_teacher.addData(list_teacher);
            noteItemAdapter_student.addData(list_student);


//            List<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> listBeans1 = new ArrayList<>();
//            ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO> listBeans2 = new ArrayList<>();
//            if (rsp.data.deptVOList.size() > 0) {
//                for (ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO listBean : rsp.data.deptVOList.get(0).children) {
//                    listBeans1.add(listBean);
//                }
//                for (ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO listBean : rsp.data.deptVOList.get(0).employeeAddBookDTOList) {
//                    listBeans2.add(listBean);
//                }
//                original.setOnClickListener(v -> setPostData2(listBeans1,listBeans2, rsp.data.deptVOList.get(0).name, String.valueOf(rsp.data.deptVOList.get(0).id), rsp.data.deptVOList.get(0).name, "student", "origin"));
//
//
//            }
//            listview2.setAdapter(adapter2);
//            adapter2.notifyData(listBeans1);
        }
    }


    @Override
    public void listByAppDataFail(String rsp) {

    }

    @Override
    public void selectListByAppRsp(selectListByAppRsp rsp) {
        Log.e(TAG, "selectListByAppRsp: " + JSON.toJSONString(rsp));
    }

    @Override
    public void selectListByAppRspFail(String rsp) {

    }
}
