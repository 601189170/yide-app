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
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.NoteByListActivity;
import com.yyide.chatim.activity.PersonInfoActivity;
import com.yyide.chatim.activity.book.BookStudentDetailActivity;
import com.yyide.chatim.activity.book.BookTeacherDetailActivity;
import com.yyide.chatim.adapter.NoteItemAdapter;
import com.yyide.chatim.adapter.NoteItemAdapter2;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.BookStudentItem;
import com.yyide.chatim.model.BookTeacherItem;
import com.yyide.chatim.model.ListByAppRsp2;
import com.yyide.chatim.model.ListByAppRsp3;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.presenter.NoteBookByListPresenter;
import com.yyide.chatim.view.NoteByListBookView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;


public class NoteByListFragment extends BaseMvpFragment<NoteBookByListPresenter> implements NoteByListBookView {


    private View mBaseView;

    private String id;
    private int pageNum = 1;
    private int pageSize = 30;
//    private NoteItemAdapter noteItemAdapter;
    private String islast;
    private String organization;
    ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> listBean = new ArrayList<>();
    ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO> nowBean = new ArrayList<>();
    public static final String PARAMS_NAME = "listBean";
    public static final String PARAMS_NAME2 = "nowBean";
    ArrayList<ListByAppRsp3.DataDTO.AdlistDTO> studentlistbeanByJz = new ArrayList<>();
    ArrayList<ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO> studentlistbeanByTeacher = new ArrayList<>();

    NoteItemAdapter parent_noteItemAdapter_teacher, parent_noteItemAdapter_student,parent_noteItemAdapter_all;

    NoteItemAdapter teacher_noteItemAdapter_teacher, teacher_noteItemAdapter_student,teacher_noteItemAdapter_all;

    @BindView(R.id.teacher_recyclerview_all)
    RecyclerView teacher_recyclerview_all;

    @BindView(R.id.teacher_recyclerview_teacher)
    RecyclerView teacher_recyclerview_teacher;

    @BindView(R.id.teacher_recyclerview_student)
    RecyclerView teacher_recyclerview_student;

    @BindView(R.id.parent_recyclerview_all)
    RecyclerView parent_recyclerview_all;

    @BindView(R.id.parent_recyclerView_teacher)
    RecyclerView parent_recyclerView_teacher;

    @BindView(R.id.parent_recyclerView_student)
    RecyclerView parent_recyclerView_student;

    @BindView(R.id.empty_layout)
    ConstraintLayout empty_layout;
    //老师视角数据
    List<TeacherlistRsp.DataBean.RecordsBean> teacher_teacher_list = new ArrayList<>();
    List<TeacherlistRsp.DataBean.RecordsBean> teacher_ALLlist = new ArrayList<>();
    List<TeacherlistRsp.DataBean.RecordsBean> teacher_list_student = new ArrayList<>();

    //家长视角数据
    List<TeacherlistRsp.DataBean.RecordsBean> parent_ALLlist = new ArrayList<>();
    List<TeacherlistRsp.DataBean.RecordsBean> parent_list_teacher = new ArrayList<>();
    List<TeacherlistRsp.DataBean.RecordsBean> parent_list_student = new ArrayList<>();
    private String index;
    private String position;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.layout_notebylist_fragmnet, container, false);
        id = getArguments().getString("id");
        islast = getArguments().getString("islast");
        organization = getArguments().getString("organization");
        index = getArguments().getString("index");

//        String Jzdata = getArguments().getString(NoteByListActivity.TAGStudentlistBeanByJz);
//        String teacherdata = getArguments().getString(NoteByListActivity.TAGStudentlistBeanByTeacher);
//        studentlistbeanByJz=(List<ListByAppRsp3.DataDTO.AdlistDTO>) JSON.parseObject(Jzdata,new TypeReference<List<ListByAppRsp3.DataDTO.AdlistDTO>>(){});
//        studentlistbeanByTeacher=(List<ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO>) JSON.parseObject(teacherdata,new TypeReference<List<ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO>>(){});

        listBean = getArguments().getParcelableArrayList(PARAMS_NAME);
        nowBean = getArguments().getParcelableArrayList(PARAMS_NAME2);
        nowBean = getArguments().getParcelableArrayList(PARAMS_NAME2);
        studentlistbeanByJz = getArguments().getParcelableArrayList("StudentlistBeanByJz");
        studentlistbeanByTeacher = getArguments().getParcelableArrayList("StudentlistBeanByTeacher");
        Log.e("TAG", "NoteByListFragment: "+ JSON.toJSONString(listBean));
        Log.e("TAG", "NoteByListFragment==》: "+ JSON.toJSONString(nowBean));
//        Log.e("TAG", "Jzdata==》: "+ JSON.toJSONString(Jzdata));
//        Log.e("TAG", "teacherdata==》: "+ JSON.toJSONString(teacherdata));
        Log.e("TAG", "studentlistbeanByJz==》: "+ JSON.toJSONString(studentlistbeanByJz));
        Log.e("TAG", "studentlistbeanByTeacher==》: "+ JSON.toJSONString(studentlistbeanByTeacher));
        Log.e("TAG", "setStudentData==index: "+JSON.toJSONString(index) );
        return mBaseView;
    }




    void setStudentData(){

        String studentDataByParent = getArguments().getString("StudentData_by_Parent");
        String TeacherDatabyParent = getArguments().getString("TeacherData_by_Parent");

        parent_list_student=(ArrayList<TeacherlistRsp.DataBean.RecordsBean>) JSON.parseObject(studentDataByParent,new TypeReference<List<TeacherlistRsp.DataBean.RecordsBean>>(){});
        parent_list_teacher=(ArrayList<TeacherlistRsp.DataBean.RecordsBean>) JSON.parseObject(TeacherDatabyParent,new TypeReference<List<TeacherlistRsp.DataBean.RecordsBean>>(){});






        if (studentlistbeanByJz!=null&&studentlistbeanByJz.size()>0){
            TeacherlistRsp.DataBean.RecordsBean bean1=new TeacherlistRsp.DataBean.RecordsBean();
            bean1.itemType = 1;
            bean1.name = "老师";
//            Log.e("TAG", "setStudentData: "+JSON.toJSONString(index) );
//            Log.e("TAG", "setStudentData_position: "+JSON.toJSONString(position) );

            if (!TextUtils.isEmpty(index)){
                bean1.TeacherData_by_Parent =studentlistbeanByJz.get(Integer.parseInt(index)).elternList;
            }else {
                bean1.TeacherData_by_Parent =studentlistbeanByJz.get(0).elternList;
            }

            TeacherlistRsp.DataBean.RecordsBean bean2=new TeacherlistRsp.DataBean.RecordsBean();
            bean2.itemType = 1;
            bean2.name = "学生";

            if (!TextUtils.isEmpty(index)){
                bean2.StudentData_by_Parent =studentlistbeanByJz.get(Integer.parseInt(index)).studentList;
            }else {
                bean2.StudentData_by_Parent =studentlistbeanByJz.get(0).studentList;
            }

            parent_ALLlist.add(bean1);
            parent_ALLlist.add(bean2);
            parent_noteItemAdapter_all.setList(parent_ALLlist);
            parent_noteItemAdapter_all.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    NoteByListActivity activity = (NoteByListActivity) getActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(parent_noteItemAdapter_all.getItem(position).id));
                    bundle.putString("name", String.valueOf(parent_noteItemAdapter_all.getItem(position).name));
                    bundle.putString("organization", organization);
                    bundle.putString("StudentData_by_Parent", JSON.toJSONString(parent_noteItemAdapter_all.getItem(position).StudentData_by_Parent));
                    bundle.putString("TeacherData_by_Parent", JSON.toJSONString(parent_noteItemAdapter_all.getItem(position).TeacherData_by_Parent));
                    bundle.putString("islast", "1");
                    Log.e("TAG", "parent_noteItemAdapter_all: "+JSON.toJSONString(parent_noteItemAdapter_all.getItem(position).StudentData_by_Parent) );
                    Log.e("TAG", "parent_noteItemAdapter_all: "+JSON.toJSONString(parent_noteItemAdapter_all.getItem(position).TeacherData_by_Parent) );
                    activity.initDeptFragmentNew(bundle);
                }
            });
        }
        if (parent_list_teacher!=null&&parent_list_teacher.size()>0)
        parent_noteItemAdapter_teacher.setList(parent_list_teacher);
        if (parent_list_student!=null&&parent_list_student.size()>0)
        parent_noteItemAdapter_student.setList(parent_list_student);
        parent_noteItemAdapter_student.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    TeacherlistRsp.DataBean.RecordsBean student=parent_noteItemAdapter_student.getItem(position);
                    BookStudentItem studentItem = new BookStudentItem(
                            student.id,
                            student.name,
                            student.phone,
                            student.className,
                            student.userId,
                            student.primaryGuardianPhone,
                            student.primaryGuardianPhone,
                            student.sex,
                            student.address,
                            student.faceInformation,
                            student.email,
                            student.elternAddBookDTOList,
                            student.avatar);

                    Intent intent = new Intent(mActivity, BookStudentDetailActivity.class);
                    intent.putExtra("student", studentItem);
                    startActivity(intent);


            }
        });

        parent_noteItemAdapter_teacher.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BookTeacherItem teacherItem = new BookTeacherItem(
                        parent_noteItemAdapter_teacher.getItem(position).name,
                        parent_noteItemAdapter_teacher.getItem(position).sex,
                        parent_noteItemAdapter_teacher.getItem(position).phone,
                        parent_noteItemAdapter_teacher.getItem(position).userId,
                        parent_noteItemAdapter_teacher.getItem(position).email,
                        parent_noteItemAdapter_teacher.getItem(position).subjectName,
                        parent_noteItemAdapter_teacher.getItem(position).employeeSubjects,
                        "",
                        parent_noteItemAdapter_teacher.getItem(position).concealPhone,
                        parent_noteItemAdapter_teacher.getItem(position).avatar);
                Log.e("TAG", "parent_noteItemAdapter_teacher: "+JSON.toJSONString(parent_noteItemAdapter_teacher.getItem(position)) );
                Intent intent = new Intent(mActivity, BookTeacherDetailActivity.class);
                intent.putExtra("teacher", teacherItem);
                startActivity(intent);
            }
        });
//        if (list_student!=null&&list_student.size()>0){
//            parent_recyclerView_student.setVisibility(View.VISIBLE);
//            teacher_recyclerview_teacher.setVisibility(View.GONE);
//            teacher_recyclerview_all.setVisibility(View.GONE);
//            teacher_recyclerview_student.setVisibility(View.GONE);
//            parent_recyclerview_all.setVisibility(View.GONE);
//            parent_recyclerView_teacher.setVisibility(View.GONE);

        }
//        if (list_teacher!=null&&list_teacher.size()>0){
//            parent_recyclerView_teacher.setVisibility(View.VISIBLE);
//            teacher_recyclerview_teacher.setVisibility(View.GONE);
//            teacher_recyclerview_all.setVisibility(View.GONE);
//            teacher_recyclerview_student.setVisibility(View.GONE);
//            parent_recyclerview_all.setVisibility(View.GONE);
//            parent_recyclerView_teacher.setVisibility(View.GONE);
//        }

//        if (ALLlist!=null&&ALLlist.size()>0){
////            parent_recyclerview_all.setVisibility(View.GONE);
////            teacher_recyclerview_student.setVisibility(View.GONE);
////            teacher_recyclerview_teacher.setVisibility(View.GONE);
//        }
//    }

    void setStudentData2(){

        String studentDataByTeacher = getArguments().getString("StudentData_by_Teacher");
        teacher_list_student=(ArrayList<TeacherlistRsp.DataBean.RecordsBean>) JSON.parseObject(studentDataByTeacher,new TypeReference<List<TeacherlistRsp.DataBean.RecordsBean>>(){});
        Log.e("TAG", "list_student: "+JSON.toJSONString(teacher_list_student) );
        if (studentlistbeanByTeacher!=null&&studentlistbeanByTeacher.size()>0){

                TeacherlistRsp.DataBean.RecordsBean bean=new TeacherlistRsp.DataBean.RecordsBean();
                bean.itemType = 1;
                bean.name = "学生";
                bean.StudentData_by_Teacher =studentlistbeanByTeacher.get(0).studentList;
                teacher_ALLlist.add(bean);

//            Log.e("TAG", "setStudentData2: "+JSON.toJSONString(ALLlist) );
            Log.e("TAG", "setStudentData2: "+JSON.toJSONString(teacher_ALLlist.get(0).name) );

            if (teacher_ALLlist!=null&&teacher_ALLlist.size()>0){
//                teacher_recyclerview_all.setVisibility(View.VISIBLE);
//                teacher_recyclerview_student.setVisibility(View.GONE);
                teacher_noteItemAdapter_all.setList(teacher_ALLlist);
            }


        }
        teacher_noteItemAdapter_all.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (teacher_noteItemAdapter_all.getItem(position).getItemType() == 1){
                    NoteByListActivity activity = (NoteByListActivity) getActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(teacher_noteItemAdapter_all.getItem(position).id));
                    bundle.putString("name", String.valueOf(teacher_noteItemAdapter_all.getItem(position).name));
                    bundle.putString("organization", organization);
                    bundle.putString("StudentData_by_Teacher", JSON.toJSONString(teacher_noteItemAdapter_all.getItem(position).StudentData_by_Teacher));
                    bundle.putString("islast", "1");
                    Log.e("TAG", "teacher_noteItemAdapter_all: "+JSON.toJSONString(bundle) );
                    activity.initDeptFragmentNew(bundle);
                }else {
                    TeacherlistRsp.DataBean.RecordsBean student=teacher_noteItemAdapter_all.getItem(position);


                    BookStudentItem studentItem = new BookStudentItem(
                            student.id,
                            student.name,
                            student.phone,
                            student.className,
                            student.userId,
                            student.primaryGuardianPhone,
                            student.primaryGuardianPhone,
                            student.sex,
                            student.address,
                            student.faceInformation,
                            student.email,
                            student.elternAddBookDTOList,
                            student.avatar);
                    BookStudentDetailActivity.start(mActivity, studentItem);
//                    Intent intent = new Intent(mActivity, BookStudentDetailActivity.class);
//                    intent.putExtra("student", studentItem);
//                    startActivity(intent);
                }

            }
        });
        teacher_noteItemAdapter_student.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                TeacherlistRsp.DataBean.RecordsBean student=teacher_noteItemAdapter_student.getItem(position);
                Log.e("TAG", "onItemClick: "+JSON.toJSONString(student) );

                BookStudentItem studentItem = new BookStudentItem(
                        student.id,
                        student.name,
                        student.phone,
                        student.className,
                        student.userId,
                        student.primaryGuardianPhone,
                        student.primaryGuardianPhone,
                        student.sex,
                        student.address,
                        student.faceInformation,
                        student.email,
                        student.elternAddBookDTOList,
                        student.avatar);
                BookStudentDetailActivity.start(mActivity, studentItem);
            }
        });
        if (teacher_list_student!=null&&teacher_list_student.size()>0){
//            teacher_recyclerview_student.setVisibility(View.VISIBLE);
            teacher_noteItemAdapter_student.setList(teacher_list_student);

        }

    }

    void setStudentData3(){

        Log.e("TAG", "list_student: "+JSON.toJSONString(teacher_list_student) );
        if (studentlistbeanByTeacher!=null&&studentlistbeanByTeacher.size()>0){
            String studentDataByTeacher;
            if (!TextUtils.isEmpty(index)){
                studentDataByTeacher =JSON.toJSONString(studentlistbeanByTeacher.get(Integer.parseInt(index)).studentList);
            }else {
                studentDataByTeacher =JSON.toJSONString(studentlistbeanByTeacher.get(0).studentList);
            }

            teacher_list_student=(ArrayList<TeacherlistRsp.DataBean.RecordsBean>) JSON.parseObject(studentDataByTeacher,new TypeReference<List<TeacherlistRsp.DataBean.RecordsBean>>(){});
//            TeacherlistRsp.DataBean.RecordsBean bean=new TeacherlistRsp.DataBean.RecordsBean();
//            bean.itemType = 1;
//            bean.name = "学生";
//            bean.StudentData_by_Teacher =studentlistbeanByTeacher.get(0).studentList;
//            teacher_ALLlist.add(bean);
//
////            Log.e("TAG", "setStudentData2: "+JSON.toJSONString(ALLlist) );
//            Log.e("TAG", "setStudentData2: "+JSON.toJSONString(teacher_ALLlist.get(0).name) );
//
//            if (teacher_ALLlist!=null&&teacher_ALLlist.size()>0){
////                teacher_recyclerview_all.setVisibility(View.VISIBLE);
////                teacher_recyclerview_student.setVisibility(View.GONE);
//                teacher_noteItemAdapter_all.setList(teacher_ALLlist);
//            }


        }
        teacher_noteItemAdapter_all.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (teacher_noteItemAdapter_all.getItem(position).getItemType() == 1){
                    NoteByListActivity activity = (NoteByListActivity) getActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(teacher_noteItemAdapter_all.getItem(position).id));
                    bundle.putString("name", String.valueOf(teacher_noteItemAdapter_all.getItem(position).name));
                    bundle.putString("organization", organization);
                    bundle.putString("StudentData_by_Teacher", JSON.toJSONString(teacher_noteItemAdapter_all.getItem(position).StudentData_by_Teacher));
                    bundle.putString("islast", "1");
                    Log.e("TAG", "teacher_noteItemAdapter_all: "+JSON.toJSONString(bundle) );
                    activity.initDeptFragmentNew(bundle);
                }else {
                    TeacherlistRsp.DataBean.RecordsBean student=teacher_noteItemAdapter_all.getItem(position);


                    BookStudentItem studentItem = new BookStudentItem(
                            student.id,
                            student.name,
                            student.phone,
                            student.className,
                            student.userId,
                            student.primaryGuardianPhone,
                            student.primaryGuardianPhone,
                            student.sex,
                            student.address,
                            student.faceInformation,
                            student.email,
                            student.elternAddBookDTOList,
                            student.avatar);
                    BookStudentDetailActivity.start(mActivity, studentItem);
//                    Intent intent = new Intent(mActivity, BookStudentDetailActivity.class);
//                    intent.putExtra("student", studentItem);
//                    startActivity(intent);
                }

            }
        });
        teacher_noteItemAdapter_student.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                TeacherlistRsp.DataBean.RecordsBean student=teacher_noteItemAdapter_student.getItem(position);
                Log.e("TAG", "onItemClick: "+JSON.toJSONString(student) );

                BookStudentItem studentItem = new BookStudentItem(
                        student.id,
                        student.name,
                        student.phone,
                        student.className,
                        student.userId,
                        student.primaryGuardianPhone,
                        student.primaryGuardianPhone,
                        student.sex,
                        student.address,
                        student.faceInformation,
                        student.email,
                        student.elternAddBookDTOList,
                        student.avatar);
                BookStudentDetailActivity.start(mActivity, studentItem);
            }
        });
        if (teacher_list_student!=null&&teacher_list_student.size()>0){
//            teacher_recyclerview_student.setVisibility(View.VISIBLE);
            teacher_noteItemAdapter_student.setList(teacher_list_student);

        }

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearData();
        //老师视角==》教职工数据
        initView();
        //家长视角==》学生数据和教职工数据
        setStudentData();
        //老师视角==》学生数据
//        setStudentData2();
        setStudentData3();

        setVG();

        sendRequset();
    }

    void clearData(){
        if (teacher_teacher_list!=null)
        teacher_teacher_list.clear();
        if (teacher_ALLlist!=null)
        teacher_ALLlist.clear();
        if (teacher_list_student!=null)
        teacher_list_student.clear();
        if (parent_ALLlist!=null)
        parent_ALLlist.clear();
        if (parent_list_teacher!=null)
        parent_list_teacher.clear();
        if (parent_list_student!=null)
        parent_list_student.clear();
    }
    public void setVG(){
        boolean em=true;
        if (teacher_teacher_list!=null&&teacher_teacher_list.size()>0){
            teacher_recyclerview_teacher.setVisibility(View.VISIBLE);
            em=false;
        }
        if (teacher_list_student!=null&&teacher_list_student.size()>0){
            teacher_recyclerview_student.setVisibility(View.VISIBLE);
            em=false;
        }
        if (teacher_ALLlist!=null&&teacher_ALLlist.size()>0){
            teacher_recyclerview_all.setVisibility(View.VISIBLE);
            em=false;
        }
        if (parent_list_teacher!=null&&parent_list_teacher.size()>0){
            parent_recyclerView_teacher.setVisibility(View.VISIBLE);
            em=false;
        }
        if (parent_list_student!=null&&parent_list_student.size()>0){
            parent_recyclerView_student.setVisibility(View.VISIBLE);
            em=false;
        }
        if (parent_ALLlist!=null&&parent_ALLlist.size()>0){
            parent_recyclerview_all.setVisibility(View.VISIBLE);
            em=false;
        }

       empty_layout.setVisibility(em?View.VISIBLE:View.GONE);

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mActivity);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(mActivity);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(mActivity);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(mActivity);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(mActivity);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);

        teacher_recyclerview_all.setLayoutManager(linearLayoutManager);
        teacher_recyclerview_student.setLayoutManager(linearLayoutManager2);
        teacher_recyclerview_teacher.setLayoutManager(linearLayoutManager3);
        parent_recyclerview_all.setLayoutManager(linearLayoutManager4);
        parent_recyclerView_teacher.setLayoutManager(linearLayoutManager5);
        parent_recyclerView_student.setLayoutManager(linearLayoutManager6);

        parent_noteItemAdapter_student = new NoteItemAdapter();
        parent_noteItemAdapter_teacher = new NoteItemAdapter();
        parent_noteItemAdapter_all = new NoteItemAdapter();
        teacher_noteItemAdapter_teacher = new NoteItemAdapter();
        teacher_noteItemAdapter_student = new NoteItemAdapter();
        teacher_noteItemAdapter_all = new NoteItemAdapter();

        parent_recyclerView_student.setAdapter(parent_noteItemAdapter_student);
        parent_recyclerView_teacher.setAdapter(parent_noteItemAdapter_teacher);
        parent_recyclerview_all.setAdapter(parent_noteItemAdapter_all);
        teacher_recyclerview_all.setAdapter(teacher_noteItemAdapter_all);
        teacher_recyclerview_student.setAdapter(teacher_noteItemAdapter_student);
        teacher_recyclerview_teacher.setAdapter(teacher_noteItemAdapter_teacher);




        if (listBean != null && listBean.size() > 0) {
            //添加组织部门数据
            if (listBean != null) {

                for (ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO item : listBean) {
                    TeacherlistRsp.DataBean.RecordsBean bean = new TeacherlistRsp.DataBean.RecordsBean();
                    bean.organizationItem = item;

                    bean.itemType = 1;
                    bean.name = item.name;

                    bean.id = Long.parseLong(item.id);
                    teacher_teacher_list.add(bean);
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
                bean.concealPhone =item2.concealPhone;
//            bean.sex = String.valueOf(item2.gender);
                bean.id = Long.parseLong(item2.id);
                teacher_teacher_list.add(bean);
            }
        }

        teacher_noteItemAdapter_teacher.setList(teacher_teacher_list);
//        noteItemAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
//            //上拉加载时取消下拉刷新
//            noteItemAdapter.getLoadMoreModule().setEnableLoadMore(true);
//            //请求数据
//            pageNum++;
//            sendRequset();
//        });
        Log.e("TAG", "listBean: "+JSON.toJSONString(listBean) );
//        if (list.size()==0){
//            noteItemAdapter.setEmptyView(R.layout.empty);
//        }
//        noteItemAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
//        noteItemAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        teacher_noteItemAdapter_teacher.setOnItemClickListener((adapter, view, position) -> {
            if (teacher_noteItemAdapter_teacher.getItem(position).getItemType() == 1) {
                NoteByListActivity activity = (NoteByListActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(teacher_noteItemAdapter_teacher.getItem(position).id));
                bundle.putString("name", String.valueOf(teacher_noteItemAdapter_teacher.getItem(position).name));
                bundle.putString("organization", organization);


                ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> listBean = new ArrayList<>();
                ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO> nowBean = new ArrayList<>();
//                listBean.add(noteItemAdapter.getItem(position).organizationItem);
                Log.e("TAG", "teacher_noteItemAdapter_teacher: "+JSON.toJSONString(teacher_noteItemAdapter_teacher.getItem(position)) );

                bundle.putParcelableArrayList(PARAMS_NAME, (ArrayList<? extends Parcelable>) teacher_noteItemAdapter_teacher.getItem(position).organizationItem.children);
                bundle.putParcelableArrayList(PARAMS_NAME2, (ArrayList<? extends Parcelable>) teacher_noteItemAdapter_teacher.getItem(position).organizationItem.employeeAddBookDTOList);
                TeacherlistRsp.DataBean.RecordsBean item = teacher_noteItemAdapter_teacher.getItem(position);
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
//                Intent intent = new Intent();
//                teacher_noteItemAdapter_teacher.getItem(position);
//                Log.e("TAG", "initView: "+JSON.toJSONString(teacher_noteItemAdapter_teacher.getItem(position)) );
//                intent.putExtra("data", JSON.toJSONString(teacher_noteItemAdapter_teacher.getItem(position)));
//                intent.putExtra("organization", organization);
//                intent.setClass(mActivity, PersonInfoActivity.class);
//                startActivity(intent);
                Log.e("TAG", "teacher_noteItemAdapter_teacher: "+JSON.toJSONString(teacher_noteItemAdapter_teacher.getItem(position)) );

                BookTeacherItem teacherItem = new BookTeacherItem(
                        teacher_noteItemAdapter_teacher.getItem(position).name,
                        teacher_noteItemAdapter_teacher.getItem(position).sex,
                        teacher_noteItemAdapter_teacher.getItem(position).phone,
                        teacher_noteItemAdapter_teacher.getItem(position).userId,
                        teacher_noteItemAdapter_teacher.getItem(position).email,
                        teacher_noteItemAdapter_teacher.getItem(position).subjectName,
                        teacher_noteItemAdapter_teacher.getItem(position).employeeSubjects,
                        "",
                        teacher_noteItemAdapter_teacher.getItem(position).concealPhone,
                        teacher_noteItemAdapter_teacher.getItem(position).avatar);

                Intent intent = new Intent(mActivity, BookTeacherDetailActivity.class);
                intent.putExtra("teacher", teacherItem);
                startActivity(intent);
            }

        });
        if (listBean!=null&&listBean.size()>0){
//            teacher_recyclerview_teacher.setVisibility(View.VISIBLE);
//            teacher_recyclerview_all.setVisibility(View.GONE);
//            teacher_recyclerview_student.setVisibility(View.GONE);
//            parent_recyclerview_all.setVisibility(View.GONE);
//            parent_recyclerView_student.setVisibility(View.GONE);
//            parent_recyclerView_teacher.setVisibility(View.GONE);
        }
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
            teacher_noteItemAdapter_teacher.setList(rsp.data.records);
            if (rsp.data.records.size() < pageSize) {
                //如果不够一页,显示没有更多数据布局
                teacher_noteItemAdapter_teacher.getLoadMoreModule().loadMoreEnd();
            } else {
                teacher_noteItemAdapter_teacher.getLoadMoreModule().loadMoreComplete();
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
            teacher_noteItemAdapter_teacher.setList(rsp.data.records);
            if (rsp.data.records.size() < pageSize) {
                //如果不够一页,显示没有更多数据布局
                teacher_noteItemAdapter_teacher.getLoadMoreModule().loadMoreEnd();
            } else {
                teacher_noteItemAdapter_teacher.getLoadMoreModule().loadMoreComplete();
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
