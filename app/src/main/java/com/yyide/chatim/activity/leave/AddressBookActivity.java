package com.yyide.chatim.activity.leave;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.leave.AddressBookAdapter;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.AddressBookRsp;
import com.yyide.chatim.model.DepartmentScopeRsp;
import com.yyide.chatim.model.AddressBookBean;
import com.yyide.chatim.model.DepartmentScopeRsp2;
import com.yyide.chatim.model.StudentScopeRsp;
import com.yyide.chatim.model.UniversityScopeRsp;
import com.yyide.chatim.presenter.leave.AddressBookPresenter;
import com.yyide.chatim.view.leave.AddressBookView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressBookActivity extends BaseMvpActivity<AddressBookPresenter> implements AddressBookView {
    private static final String TAG = AddressBookActivity.class.getSimpleName();
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
//    @BindView(R.id.checkBox)
//    CheckBox checkBox;
    @BindView(R.id.tv_selected_member)
    TextView tv_selected_member;

    @BindView(R.id.blank_page)
    ConstraintLayout blank_page;
    @BindView(R.id.gp_main_content)
    Group gp_main_content;
    //private TreeRecyclerAdapter adapter = new TreeRecyclerAdapter(TreeRecyclerType.SHOW_EXPAND);
    List<AddressBookBean> noticeScopeBeans = new ArrayList<>();

    private AddressBookAdapter adapter;
    private int sendObj;//发送对象 ： 1家长 2学生 3部门

    private int classTotal = 0;
    private int departmentTotal = 0;
    private String schoolType;
    //记录当前点击的条目数据
    private int currentPosition;
    private int currentLevel;
    private AddressBookBean addressBookBean;

    @Override
    public int getContentViewID() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.select_carbon_copy_people);
        Intent intent = getIntent();
        sendObj = intent.getIntExtra("sendObj", 3);
        initRecyclerView();
        Log.e(TAG, "onCreate: " + sendObj);
        schoolType = SpData.getIdentityInfo().schoolType;
        Log.e(TAG, "schoolType: " + schoolType);
        if (sendObj == 1 || sendObj == 2) {
            //请求数据
            if (schoolType.equals("Y")) {
                mvpPresenter.queryDepartmentClassList();
            } else {
                mvpPresenter.getSectionList();
            }

        } else {
            //选择部门
            //mvpPresenter.getDepartmentList();
            mvpPresenter.queryDepartmentOverrideList();
        }
    }

    private void initRecyclerView() {
        Log.e(TAG, "initRecyclerView: " + sendObj);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressBookAdapter(this, noticeScopeBeans);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnCheckBoxChangeListener(() -> {
            //选中改变，更新底部的数据显示
            if (sendObj == 1 || sendObj == 2) {
                showNoticeScopeNumber(getAllCheckedClassIds(noticeScopeBeans, new ArrayList<String>()).size());
            } else {
                showNoticeScopeNumber(getAllCheckedDepartmentIds2(noticeScopeBeans, new ArrayList<AddressBookRsp.DataBean>()).size());
            }
            adapter.notifyDataSetChanged();
        });

        adapter.setOnItemClickListener(addressBookBean -> {
                    Log.e(TAG, "setOnItemClickListener: id=" + addressBookBean.getId() + " ,level=" + addressBookBean.getLevel()+",isExitInd="+addressBookBean.getIsExitInd());
                    this.addressBookBean = addressBookBean;
                    if (addressBookBean.getDeptMemberList() != null && addressBookBean.getDeptMemberList().isEmpty() && "Y".equals(addressBookBean.getIsExitInd())){
                        mvpPresenter.queryDeptMemberByDeptId(addressBookBean.getId(), addressBookBean.getLevel());
                    }else {
                        Log.e(TAG, "setOnItemClickListener: 已经请求过数据不在请求" );
                    }
                }
        );
        initView();
    }

//    private AddressBookBean currentDeptData(int position,int level,List<AddressBookBean> list){
//        for (int i = 0; i < list.size(); i++) {
//            final AddressBookBean addressBookBean = list.get(i);
//            if (addressBookBean.getLevel() == level && i == position) {
//                return addressBookBean;
//            }
//            final List<AddressBookBean> list1 = addressBookBean.getList();
//            if (list1 != null && !list1.isEmpty()) {
//                currentDeptData(position, level,list1);
//            }else {
//                continue;
//            }
//        }
//        return null;
//    }

    private void showNoticeScopeNumber(int totalNumber) {
        tv_selected_member.setText(String.format(getString(R.string.notice_scope_number_text), totalNumber));
        if (totalNumber != 0) {
            if (sendObj == 1 || sendObj == 2) {
                //checkBox.setChecked(totalNumber == classTotal);
            } else {
                //checkBox.setChecked(totalNumber == departmentTotal);
            }
        } else {
            //checkBox.setChecked(false);
        }
    }

    /**
     * 获取班级ids,不获取上级id
     *
     * @param beanList
     * @param ids
     * @return
     */
    private List<String> getAllCheckedClassIds(List<AddressBookBean> beanList, List<String> ids) {
//        for (AddressBookBean bean : beanList) {
//            if ((bean.getList() == null || bean.getList().isEmpty()) && bean.isChecked() && ((schoolType.equals("Y") && bean.getLevel().equals("1")) || (!schoolType.equals("Y") && bean.getType().equals("2")))) {
//                int id = bean.getId();
//                ids.add("" + id);
//            } else if (bean.getList() != null && !bean.getList().isEmpty()) {
//                getAllCheckedClassIds(bean.getList(), ids);
//            } else {
//                continue;
//            }
//        }
        return ids;
    }

    /**
     * 获取部门ids,获取全部id
     *
     * @param beanList
     * @param ids
     * @return
     */
//    private List<String> getAllCheckedDepartmentIds(List<AddressBookBean> beanList, List<String> ids) {
//        for (AddressBookBean bean : beanList) {
//            if (bean.isChecked()) {
//                int id = bean.getId();
//                ids.add("" + id);
//            }
//            if (bean.getList() != null && !bean.getList().isEmpty()) {
//                getAllCheckedDepartmentIds(bean.getList(), ids);
//            } else {
//                continue;
//            }
//        }
//        return ids;
//    }

    private List<AddressBookRsp.DataBean> getAllCheckedDepartmentIds2(List<AddressBookBean> beanList, List<AddressBookRsp.DataBean> ids) {
        for (AddressBookBean bean : beanList) {
            final List<AddressBookRsp.DataBean> deptMemberList = bean.getDeptMemberList();
            for (AddressBookRsp.DataBean dataBean : deptMemberList) {
                if (dataBean.isChecked()){
                    ids.add(dataBean);
                }
            }
//            if (bean.isChecked()) {
//                int id = bean.getId();
//                ids.add("" + id);
//            }
            if (bean.getList() != null && !bean.getList().isEmpty()) {
                getAllCheckedDepartmentIds2(bean.getList(), ids);
            } else {
                continue;
            }
        }
        return ids;
    }

    public void initView() {
        showNoticeScopeNumber(0);
//        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            Log.e(TAG, "initView: " + checkBox.isChecked());
//            if (!buttonView.isPressed()) {
//                Log.e(TAG, "initView: 代码触发，不处理监听事件。");
//                return;
//            }
//            recursionChecked(noticeScopeBeans, isChecked);
//            if (sendObj == 1 || sendObj == 2) {
//                showNoticeScopeNumber(getAllCheckedClassIds(noticeScopeBeans, new ArrayList<String>()).size());
//            } else {
//                showNoticeScopeNumber(getAllCheckedDepartmentIds(noticeScopeBeans, new ArrayList<String>()).size());
//            }
//        });
    }

    /**
     * 递归实现下级是否选中
     *
     * @param noticeScopeBean
     */
    private void recursionChecked(List<AddressBookBean> noticeScopeBean, boolean isChecked) {
        for (AddressBookBean scopeBean : noticeScopeBean) {
            scopeBean.setChecked(isChecked);
            if (scopeBean.getList() != null) {
                recursionChecked(scopeBean.getList(), isChecked);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected AddressBookPresenter createPresenter() {
        return new AddressBookPresenter(this);
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    @OnClick(R.id.btn_complete)
    public void complete() {
        ArrayList<String> classesIds = new ArrayList<>();
        ArrayList<AddressBookRsp.DataBean> deptList = new ArrayList<>();
        //List<String> ids = getIds(noticeScopeBeans, classesIds);
        if (sendObj == 1 || sendObj == 2) {
            List<String> allCheckedClassIds = getAllCheckedClassIds(noticeScopeBeans, new ArrayList<String>());
            classesIds.addAll(allCheckedClassIds);
        } else {
            List<AddressBookRsp.DataBean> allCheckedDepartmentIds = getAllCheckedDepartmentIds2(noticeScopeBeans, new ArrayList<AddressBookRsp.DataBean>());
            deptList.addAll(allCheckedDepartmentIds);
        }
        if (deptList.isEmpty()) {
            ToastUtils.showShort("请选择通知范围");
            return;
        }
        //返回上一页
        Intent intent = getIntent();
        intent.putExtra("deptList", JSON.toJSONString(deptList));
        this.setResult(RESULT_OK, intent);
        finish();
    }

    private List<String> getIds(List<AddressBookBean> noticeScopeBeans, List<String> ids) {
        for (AddressBookBean noticeScopeBean : noticeScopeBeans) {
            long id = noticeScopeBean.getId();
            ids.add("" + id);
            List<AddressBookBean> list = noticeScopeBean.getList();
            if (list != null && !list.isEmpty()) {
                getIds(list, ids);
            }
        }
        return ids;
    }

    @Override
    public void showError() {

    }

    @Override
    public void getStudentScopeSuccess(StudentScopeRsp studentScopeRsp) {
        Log.e(TAG, "getStudentScopeSuccess: " + studentScopeRsp.toString());
/*        if (studentScopeRsp.getCode() == 200) {
            noticeScopeBeans.clear();
            for (StudentScopeRsp.ListBeanXX listBean : studentScopeRsp.getData()) {
                List<StudentScopeRsp.ListBeanXX.ListBeanX> grades = listBean.getList();
                //第一层
                AddressBookBean noticeScopeBean = new AddressBookBean(listBean.getId(), listBean.getName(), listBean.getType());
                if (grades.isEmpty()) {
                    noticeScopeBean.setHasNext(false);
                    noticeScopeBeans.add(noticeScopeBean);
                    continue;
                }
                List<AddressBookBean> noticeScopeBeans1 = new ArrayList<>();
                for (StudentScopeRsp.ListBeanXX.ListBeanX grade : grades) {
                    List<StudentScopeRsp.ListBeanXX.ListBeanX.ListBean> classes = grade.getList();
                    AddressBookBean noticeScopeBean1 = new AddressBookBean(grade.getId(), grade.getName(), grade.getType());
                    if (classes.isEmpty()) {
                        noticeScopeBean1.setHasNext(false);
                        noticeScopeBeans1.add(noticeScopeBean1);
                        continue;
                    }
                    List<AddressBookBean> noticeScopeBeans2 = new ArrayList<>();
                    for (StudentScopeRsp.ListBeanXX.ListBeanX.ListBean aClass : classes) {
                        classTotal++;
                        noticeScopeBeans2.add(new AddressBookBean(aClass.getId(), aClass.getName(), aClass.getType(), false));
                    }
                    noticeScopeBean1.setList(noticeScopeBeans2);
                    noticeScopeBeans1.add(noticeScopeBean1);
                }
                noticeScopeBean.setList(noticeScopeBeans1);
                noticeScopeBeans.add(noticeScopeBean);
            }
            Log.e(TAG, "getStudentScopeSuccess: " + noticeScopeBeans.toString());
            if (noticeScopeBeans.isEmpty()) {
                ToastUtils.showShort("没有找到通知范围数据！");
            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShort("请求数据失败:" + studentScopeRsp.getMsg());
        }*/
    }


    @Override
    public void getStudentScopeFail(String msg) {
        Log.e(TAG, "getStudentScopeFail: " + "请求数据失败：" + msg);
        ToastUtils.showShort("请求数据失败：" + msg);
    }

    @Override
    public void getDepartmentListSuccess(DepartmentScopeRsp departmentScopeRsp) {
        Log.e(TAG, "getDepartmentListSuccess: " + departmentScopeRsp.toString());
        if (departmentScopeRsp.getCode() == 200) {
            noticeScopeBeans.clear();
            for (DepartmentScopeRsp.DataBean datum : departmentScopeRsp.getData()) {
                if (datum == null) {
                    continue;
                }
                List<DepartmentScopeRsp.DataBean.ListBeanXX> list = datum.getList();
                AddressBookBean noticeScopeBean = new AddressBookBean(datum.getId(), datum.getName(),datum.getLevel(),"Y");
                List<AddressBookBean> noticeScopeBeans1 = new ArrayList<>();
                if (list.isEmpty()) {
                    departmentTotal++;
                    noticeScopeBeans.add(noticeScopeBean);
                    continue;
                }
                for (DepartmentScopeRsp.DataBean.ListBeanXX listBeanXX : list) {
                    List<DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX> list1 = listBeanXX.getList();
                    AddressBookBean noticeScopeBean2 = new AddressBookBean(listBeanXX.getId(), listBeanXX.getName(),listBeanXX.getLevel(),"Y");
                    if (list1.isEmpty()) {
                        departmentTotal++;
                        noticeScopeBeans1.add(noticeScopeBean2);
                        continue;
                    }
                    List<AddressBookBean> noticeScopeBeans2 = new ArrayList<>();
                    for (DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX listBeanX : list1) {
                        List<DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX.ListBean> list2 = listBeanX.getList();
                        AddressBookBean noticeScopeBean3 = new AddressBookBean(listBeanX.getId(), listBeanX.getName(),listBeanX.getLevel(),"Y");
                        if (list2.isEmpty()) {
                            departmentTotal++;
                            noticeScopeBeans2.add(noticeScopeBean3);
                            continue;
                        }
                        List<AddressBookBean> noticeScopeBeans3 = new ArrayList<>();
                        for (DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX.ListBean listBean : list2) {
                            departmentTotal++;
                            AddressBookBean noticeScopeBean4 = new AddressBookBean(listBean.getId(), listBean.getName(),listBean.getLevel(),"Y");
                            noticeScopeBeans3.add(noticeScopeBean4);
                        }
                        noticeScopeBean3.setList(noticeScopeBeans3);
                        noticeScopeBeans2.add(noticeScopeBean3);
                        departmentTotal++;
                    }
                    noticeScopeBean2.setList(noticeScopeBeans2);
                    noticeScopeBeans1.add(noticeScopeBean2);
                    departmentTotal++;
                }
                noticeScopeBean.setList(noticeScopeBeans1);
                noticeScopeBeans.add(noticeScopeBean);
                departmentTotal++;
            }
            Log.e(TAG, "getStudentScopeSuccess: " + noticeScopeBeans.toString());
            showBlank(noticeScopeBeans.isEmpty());
//            if (noticeScopeBeans.isEmpty()) {
//                //ToastUtils.showShort("没有找到通知范围数据！");
//                showBlank(noticeScopeBeans.isEmpty());
//            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShort("请求数据失败:" + departmentScopeRsp.getMsg());
            showBlank(true);
        }
    }

    @Override
    public void getDepartmentListFail(String msg) {
        Log.e(TAG, "getDepartmentListFail: " + "请求数据失败：" + msg);
        ToastUtils.showShort("请求数据失败：" + msg);
        showBlank(true);
    }

    @Override
    public void getDepartmentListSuccess2(DepartmentScopeRsp2 departmentScopeRsp2) {
        Log.e(TAG, "getDepartmentListSuccess2: " + departmentScopeRsp2.toString());
        if (departmentScopeRsp2.getCode() == 200) {
            noticeScopeBeans.clear();
            for (DepartmentScopeRsp2.DataBean datum : departmentScopeRsp2.getData()) {
                if (datum == null) {
                    continue;
                }
                List<DepartmentScopeRsp2.DataBean.ListBeanXX> list = datum.getList();
                AddressBookBean noticeScopeBean = new AddressBookBean(datum.getId(), datum.getName(),datum.getLevel(),datum.getIsExitInd());
                List<AddressBookBean> noticeScopeBeans1 = new ArrayList<>();
                if (list.isEmpty()) {
                    departmentTotal++;
                    noticeScopeBeans.add(noticeScopeBean);
                    continue;
                }
                for (DepartmentScopeRsp2.DataBean.ListBeanXX listBeanXX : list) {
                    List<DepartmentScopeRsp2.DataBean.ListBeanXX.ListBeanX> list1 = listBeanXX.getList();
                    AddressBookBean noticeScopeBean2 = new AddressBookBean(listBeanXX.getId(), listBeanXX.getName(),listBeanXX.getLevel(),listBeanXX.getIsExitInd());
                    if (list1.isEmpty()) {
                        departmentTotal++;
                        noticeScopeBeans1.add(noticeScopeBean2);
                        continue;
                    }
                    List<AddressBookBean> noticeScopeBeans2 = new ArrayList<>();
                    for (DepartmentScopeRsp2.DataBean.ListBeanXX.ListBeanX listBeanX : list1) {
                        List<DepartmentScopeRsp2.DataBean.ListBeanXX.ListBeanX.ListBean> list2 = listBeanX.getList();
                        AddressBookBean noticeScopeBean3 = new AddressBookBean(listBeanX.getId(), listBeanX.getName(),listBeanX.getLevel(),listBeanX.getIsExitInd());
                        if (list2.isEmpty()) {
                            departmentTotal++;
                            noticeScopeBeans2.add(noticeScopeBean3);
                            continue;
                        }
                        List<AddressBookBean> noticeScopeBeans3 = new ArrayList<>();
                        for (DepartmentScopeRsp2.DataBean.ListBeanXX.ListBeanX.ListBean listBean : list2) {
                            departmentTotal++;
                            AddressBookBean noticeScopeBean4 = new AddressBookBean(listBean.getId(), listBean.getName(),listBean.getLevel(),listBean.getIsExitInd());
                            noticeScopeBeans3.add(noticeScopeBean4);
                        }
                        noticeScopeBean3.setList(noticeScopeBeans3);
                        noticeScopeBeans2.add(noticeScopeBean3);
                        departmentTotal++;
                    }
                    noticeScopeBean2.setList(noticeScopeBeans2);
                    noticeScopeBeans1.add(noticeScopeBean2);
                    departmentTotal++;
                }
                noticeScopeBean.setList(noticeScopeBeans1);
                noticeScopeBeans.add(noticeScopeBean);
                departmentTotal++;
            }
            Log.e(TAG, "getStudentScopeSuccess: " + noticeScopeBeans.toString());
            showBlank(noticeScopeBeans.isEmpty());
//            if (noticeScopeBeans.isEmpty()) {
//                //ToastUtils.showShort("没有找到通知范围数据！");
//                showBlank(noticeScopeBeans.isEmpty());
//            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShort("请求数据失败:" + departmentScopeRsp2.getMsg());
            showBlank(true);
        }
    }

    @Override
    public void getDepartmentListFail2(String msg) {
        Log.e(TAG, "getDepartmentListFail2: " + "请求数据失败：" + msg);
        ToastUtils.showShort("请求数据失败：" + msg);
        showBlank(true);
    }

    @Override
    public void getUniversityListSuccess(UniversityScopeRsp universityScopeRsp) {
        Log.e(TAG, "getUniversityListSuccess: " + universityScopeRsp.toString());
     /*   if (universityScopeRsp.getCode() == 200) {
            noticeScopeBeans.clear();
            for (UniversityScopeRsp.ListBeanXX listBean : universityScopeRsp.getData()) {
                List<UniversityScopeRsp.ListBeanXX.ListBeanX> grades = listBean.getList();
                //第一层
                AddressBookBean noticeScopeBean = new AddressBookBean(listBean.getId(), listBean.getName(), listBean.getType());
                if (grades.isEmpty()) {
                    noticeScopeBean.setHasNext(false);
                    noticeScopeBeans.add(noticeScopeBean);
                    continue;
                }
                List<AddressBookBean> noticeScopeBeans1 = new ArrayList<>();
                for (UniversityScopeRsp.ListBeanXX.ListBeanX grade : grades) {
                    List<UniversityScopeRsp.ListBeanXX.ListBeanX.ListBean> classes = grade.getList();
                    AddressBookBean noticeScopeBean1 = new AddressBookBean(grade.getId(), grade.getName(), grade.getType());
                    if (classes.isEmpty()) {
                        noticeScopeBean1.setHasNext(false);
                        noticeScopeBeans1.add(noticeScopeBean1);
                        continue;
                    }
                    List<AddressBookBean> noticeScopeBeans2 = new ArrayList<>();
                    for (UniversityScopeRsp.ListBeanXX.ListBeanX.ListBean aClass : classes) {
                        classTotal++;
                        noticeScopeBeans2.add(new AddressBookBean(aClass.getId(), aClass.getName(), aClass.getType(), false));
                    }
                    noticeScopeBean1.setList(noticeScopeBeans2);
                    noticeScopeBeans1.add(noticeScopeBean1);
                }

                noticeScopeBean.setList(noticeScopeBeans1);
                noticeScopeBeans.add(noticeScopeBean);
            }
            Log.e(TAG, "getStudentScopeSuccess: " + noticeScopeBeans.toString());
            if (noticeScopeBeans.isEmpty()) {
                ToastUtils.showShort("没有找到通知范围数据！");
            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShort("请求数据失败:" + universityScopeRsp.getMsg());
        }*/
    }

    @Override
    public void getUniversityListFail(String msg) {
        Log.e(TAG, "getUniversityListFail: " + "请求数据失败：" + msg);
        ToastUtils.showShort("请求数据失败：" + msg);
    }

    @Override
    public void getDeptMemberListSuccess(AddressBookRsp addressBookRsp) {
        Log.d(TAG, "getDeptMemberListSuccess: " + addressBookRsp.toString());
        if (addressBookRsp.getCode() == 200) {
            final List<AddressBookRsp.DataBean> data = addressBookRsp.getData();
            if (data != null && !data.isEmpty()){
                //final AddressBookBean addressBookBean = currentDeptData(currentPosition, currentLevel, noticeScopeBeans);
                if (addressBookBean != null){
                    List<AddressBookRsp.DataBean> deptMemberList = addressBookBean.getDeptMemberList();
                    if (deptMemberList == null){
                        deptMemberList = new ArrayList<>();
                    }
                    deptMemberList.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void getDeptMemberListFail(String msg) {
        Log.e(TAG, "getDeptMemberListFail: " + msg);
    }

    private void showBlank(boolean show){
        blank_page.setVisibility(show?View.VISIBLE:View.GONE);
        gp_main_content.setVisibility(show?View.GONE:View.VISIBLE);
    }
}