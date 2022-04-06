package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.book.BookSearchActivity;
import com.yyide.chatim.activity.book.BookTeacherDetailActivity;
import com.yyide.chatim.adapter.NoBookItemAdapter;
import com.yyide.chatim.adapter.NoBookItemAdapter2;
import com.yyide.chatim.adapter.NoBookItemAdapter_student_By_Parent;
import com.yyide.chatim.adapter.NoBookItemAdapter_student_By_Teacher;
import com.yyide.chatim.adapter.NoteBookItemAdapter2;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.BookTeacherItem;
import com.yyide.chatim.model.ListByAppRsp;
import com.yyide.chatim.model.ListByAppRsp2;
import com.yyide.chatim.model.ListByAppRsp3;
import com.yyide.chatim.model.SchoolRsp;
import com.yyide.chatim.model.selectListByAppRsp;
import com.yyide.chatim.presenter.NoteBookPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.NoteBookView;

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

    NoBookItemAdapter adapterjzg;
    NoBookItemAdapter_student_By_Parent adapter_student_by_parent;
    NoBookItemAdapter_student_By_Teacher adapter_student_by_teacher;
    @BindView(R.id.listview2)
    ListView listview2;



    @BindView(R.id.pName)
    TextView pName;
    public String TAG = "tag";
    private String schoolType = "";
    private String mSchoolName = "";



    String logo;


    @Override
    public int getContentViewID() {
        return R.layout.activity_note_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("通讯录");




        SchoolRsp schoolRsp = SpData.Schoolinfo();
        if (schoolRsp != null) {
            mSchoolName = schoolRsp.getSchoolName();
            pName.setText(schoolRsp.getSchoolName());
        }
        adapterjzg = new NoBookItemAdapter();
        adapter_student_by_parent = new NoBookItemAdapter_student_By_Parent();
        adapter_student_by_teacher = new NoBookItemAdapter_student_By_Teacher();

        listview.setAdapter(adapterjzg);
//        listview2.setAdapter(adapter_student_by_parent);


        //教职工
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapterjzg.getItem(position).isperson){
                    BookTeacherItem teacherItem = new BookTeacherItem(
                            adapterjzg.getItem(position).name,
                            adapterjzg.getItem(position).gender,
                            adapterjzg.getItem(position).phone,
                            adapterjzg.getItem(position).userId,
                            adapterjzg.getItem(position).email,
                            adapterjzg.getItem(position).subjectName,
                            adapterjzg.getItem(position).employeeSubjects,
                            "",
                            adapterjzg.getItem(position).concealPhone,
                            adapterjzg.getItem(position).avatar);

                    Intent intent = new Intent(NoteBookActivity.this, BookTeacherDetailActivity.class);
                    intent.putExtra("teacher", teacherItem);
                    startActivity(intent);
                }else {
                    setPostData((ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO>) adapterjzg.getItem(position).children, adapterjzg.getItem(position).employeeAddBookDTOList, mSchoolName, String.valueOf(adapterjzg.getItem(position).id), adapterjzg.getItem(position).name, "staff", "");
                }
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
        intent.putExtra("logo", logo);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("schoolName", mSchoolName);
        intent.putExtra("schoolType", schoolType);
        intent.putExtra("type", type);
        intent.putExtra("organization", organization);
        intent.setClass(NoteBookActivity.this, NoteByListActivity.class);
        startActivity(intent);
    }


    void setPostStudentDataByParent(List<ListByAppRsp3.DataDTO.AdlistDTO> list,int index) {

        Intent intent = new Intent();
        intent.putExtra(NoteByListActivity.TAGStudentlistBeanByJz, JSON.toJSONString(list));
        intent.putExtra("schoolName", mSchoolName);
        intent.putExtra("schoolType", schoolType);
        intent.putExtra("id", "");
        intent.putExtra("index", index+"");
        intent.putExtra("logo", logo);
        intent.putExtra("name", list.get(index).name);
        intent.setClass(NoteBookActivity.this, NoteByListActivity.class);
        startActivity(intent);
    }

    void setPostStudentDataByTeacher(List<ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO> list,int index ) {

        Intent intent = new Intent();
        intent.putExtra(NoteByListActivity.TAGStudentlistBeanByTeacher, JSON.toJSONString(list));
        intent.putExtra("schoolName", mSchoolName);
        intent.putExtra("schoolType", schoolType);
        intent.putExtra("id", list.get(index).id);
        intent.putExtra("name", list.get(index).name);
        intent.putExtra("logo", logo);
        intent.putExtra("index", index+"");
        intent.setClass(NoteBookActivity.this, NoteByListActivity.class);
        startActivity(intent);
    }

    @Override
    protected NoteBookPresenter createPresenter() {
        return new NoteBookPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpData.getIdentityInfo().staffIdentity()) {
            mvpPresenter.listByApp();

        }else {
            mvpPresenter.universitySelectListByApp();
        }
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

    @Override
    public void listByApp(ListByAppRsp2 rsp) {
        Log.e("TAG", "listByAppRsp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES_0) {
            //学校组织结构
            mSchoolName=rsp.data.schoolName;
            logo=rsp.data.schoolBadgeUrl;
            ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> listBeans1 = new ArrayList<>();
            ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> listBeans111 = new ArrayList<>();
            if (!TextUtils.isEmpty(rsp.data.schoolBadgeUrl)) {
//                    GlideUtil.loadImageHead(NoteBookActivity.this, rsp.data.schoolBadgeUrl, img);
                GlideUtil.loadImage(NoteBookActivity.this, rsp.data.schoolBadgeUrl, img);
            }
            if (rsp.data.deptVOList.size() > 0) {
                pName.setText(rsp.data.schoolName);

                for (ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO listBean : rsp.data.deptVOList.get(0).children) {
                    listBean.itemType=1;
                    listBeans1.add(listBean);
                }
                for (ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO listBean : rsp.data.deptVOList.get(0).employeeAddBookDTOList) {

                    ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO bean=new ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO();
                    bean.children=new ArrayList<>();
                    bean.isperson=true;
                    bean.id=listBean.id;
                    bean.name=listBean.name;
                    bean.concealPhone=listBean.concealPhone;
                    bean.email=listBean.email;
                    bean.avatar=listBean.avatar;
                    bean.employeeSubjects=listBean.employeeSubjects;
                    bean.phone=listBean.phone;
                    bean.userId=listBean.userId;
                    bean.subjectName=listBean.subjectName;
                    bean.gender=listBean.gender;
                    bean.itemType=0;


                    listBeans111.add(bean);
                }
//                original.setOnClickListener(v -> setPostData(listBeans1, listBeans2, rsp.data.deptVOList.get(0).name, String.valueOf(rsp.data.deptVOList.get(0).id), rsp.data.deptVOList.get(0).name, "staff", "origin"));
            }
            listBeans1.addAll(listBeans111);
            listview2.setAdapter(adapter_student_by_teacher);
            adapter_student_by_teacher.notifyData(rsp.data.classAddBookDTOList);
            listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setPostStudentDataByTeacher(adapter_student_by_teacher.list,position);
                }
            });


            if (listBeans1!=null&&listBeans1.size()>0)
            adapterjzg.notifyData(listBeans1);
            if (adapter_student_by_teacher.list.size()>0){
                listview2.setVisibility(View.VISIBLE);
            }else {
                listview2.setVisibility(View.GONE);
            }
//            listview2.setVisibility(View.GONE);


            Log.e(TAG, "adapter_student_by_teacher: "+JSON.toJSONString(adapter_student_by_teacher.list.size()) );
            Log.e(TAG, "adapterjzg: "+JSON.toJSONString(adapterjzg.list.size()) );
        }
    }

    @Override
    public void selectListByApp(ListByAppRsp rsp) {


    }

    @Override
    public void universityListByApp(ListByAppRsp3 rsp) {

        Log.e("TAG", "universityListByApp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES_0) {
            mSchoolName=rsp.data.schoolName;
            logo=rsp.data.schoolBadgeUrl;
            if (!TextUtils.isEmpty(rsp.data.schoolBadgeUrl)) {
//                    GlideUtil.loadImageHead(NoteBookActivity.this, rsp.data.schoolBadgeUrl, img);
                GlideUtil.loadImage(NoteBookActivity.this, rsp.data.schoolBadgeUrl, img);
            }
            listview2.setAdapter(adapter_student_by_parent);
            adapter_student_by_parent.notifyData(rsp.data.adlist);
            if (adapter_student_by_parent.list.size()>0){
                listview2.setVisibility(View.VISIBLE);
            }else {
                listview2.setVisibility(View.GONE);
            }
//            listview2.setVisibility(View.GONE);
            listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setPostStudentDataByParent(adapter_student_by_parent.list,position);
                }
            });

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
