package com.yyide.chatim.activity.notice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.presenter.NoticeScopePresenter;
import com.yyide.chatim.activity.notice.view.NoticeScopeView;
import com.yyide.chatim.adapter.NoticeScopeAdapter;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.DepartmentScopeRsp;
import com.yyide.chatim.model.NoticeScopeBean;
import com.yyide.chatim.model.StudentScopeRsp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeScopeActivity extends BaseMvpActivity<NoticeScopePresenter> implements NoticeScopeView {
    private static final String TAG = "NoticeScopeActivity";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.tv_selected_member)
    TextView tv_selected_member;
    //private TreeRecyclerAdapter adapter = new TreeRecyclerAdapter(TreeRecyclerType.SHOW_EXPAND);
    List<NoticeScopeBean> noticeScopeBeans = new ArrayList<>();

    private NoticeScopeAdapter adapter;
    private int sendObj;

    private int classTotal = 0;
    private int departmentTotal = 0;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_scope;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_scope_title);
        Intent intent = getIntent();
        sendObj = intent.getIntExtra("sendObj", 1);
        initRecyclerView();
        Log.e(TAG, "onCreate: " + sendObj);
        if (sendObj == 1 || sendObj == 2) {
            //请求数据
            mvpPresenter.getSectionList(1, 10);
        } else {
            //选择部门
            mvpPresenter.getDepartmentList(1, 10);
        }
    }

    private void initRecyclerView() {
        Log.e(TAG, "initRecyclerView: " + sendObj);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(adapter);
//        List<CartBean> beans = new ArrayList<>();
//        beans.add(new CartBean(3));
//
//        List<TreeItem> groupItem = ItemHelperFactory.createItems(beans);
//
//        adapter.getItemManager().replaceAllItem(groupItem);
//        adapter.setOnItemClickListener((viewHolder, position) -> {
//            //因为外部和内部会冲突
//            TreeItem item = adapter.getData(position);
//            if (item != null) {
//                item.onClick(viewHolder);
//            }
//            notifyPrice();
//        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoticeScopeAdapter(this, noticeScopeBeans);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnCheckBoxChangeListener(() -> {
            //选中改变，更新底部的数据显示
            if (sendObj == 1 || sendObj == 2) {
                showNoticeScopeNumber(getAllCheckedClassIds(noticeScopeBeans, new ArrayList<String>()).size());
            } else {
                showNoticeScopeNumber(getAllCheckedDepartmentIds(noticeScopeBeans, new ArrayList<String>()).size());
            }
        });
        initView();
        //notifyPrice();
    }

    private void showNoticeScopeNumber(int totalNumber) {
        tv_selected_member.setText(String.format(getString(R.string.notice_scope_number_text), totalNumber));
        if (totalNumber !=0){
            if (sendObj == 1 || sendObj == 2) {
                checkBox.setChecked(totalNumber == classTotal);
            }else {
                checkBox.setChecked(totalNumber == departmentTotal);
            }
        }else {
            checkBox.setChecked(false);
        }
    }

    /**
     * 获取班级ids,不获取上级id
     *
     * @param beanList
     * @param ids
     * @return
     */
    private List<String> getAllCheckedClassIds(List<NoticeScopeBean> beanList, List<String> ids) {
        for (NoticeScopeBean bean : beanList) {
            if ((bean.getList() == null || bean.getList().isEmpty()) && bean.isChecked() && bean.getType().equals("2")) {
                int id = bean.getId();
                ids.add("" + id);
            } else if (bean.getList() != null && !bean.getList().isEmpty()) {
                getAllCheckedClassIds(bean.getList(), ids);
            } else {
                continue;
            }
        }
        return ids;
    }

    /**
     * 获取部门ids,获取全部id
     *
     * @param beanList
     * @param ids
     * @return
     */
    private List<String> getAllCheckedDepartmentIds(List<NoticeScopeBean> beanList, List<String> ids) {
        for (NoticeScopeBean bean : beanList) {
            if (bean.isChecked()) {
                int id = bean.getId();
                ids.add("" + id);
            }
            if (bean.getList() != null && !bean.getList().isEmpty()) {
                getAllCheckedDepartmentIds(bean.getList(), ids);
            } else {
                continue;
            }
        }
        return ids;
    }

    /**
     * 更新价格
     */
    public void notifyPrice() {
//        boolean isSelectAll = false;//默认全选
//        int number = 0;
//        for (TreeItem item : adapter.getData()) {
//            if (item instanceof TreeSelectItemGroup) {
//                TreeSelectItemGroup group = (TreeSelectItemGroup) item;
//                if (!group.isSelect()) {//是否有选择的子类
//                    //有一个没选则不是全选
//                    isSelectAll = false;
//                    continue;
//                }
//                if (!group.isSelectAll()) {//是否全选了子类
//                    //有一个没选则不是全选
//                    isSelectAll = false;
//                }
//                List<TreeItem> selectItems = group.getSelectItems();
//                for (TreeItem child : selectItems) {
//                    if (child instanceof CartItem) {
//                        Integer data = (Integer) child.getData();
//                        number += data;
//                    }
//                }
//            }
//        }
//        adapter.notifyDataSetChanged();
//        tv_selected_member.setText(getString(R.string.notice_select_number, number));
//        checkBox.setChecked(isSelectAll);
    }

    public void initView() {
//        checkBox.setOnClickListener(v -> {
//            for (TreeItem item : adapter.getData()) {
//                if (item instanceof TreeSelectItemGroup) {
//                    TreeSelectItemGroup group = (TreeSelectItemGroup) item;
//                    group.selectAll(((CheckBox) v).isChecked());
//                }
//            }
//            notifyPrice();
//        });
        showNoticeScopeNumber(0);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.e(TAG, "initView: "+checkBox.isChecked());
            if (!buttonView.isPressed()){
                Log.e(TAG, "initView: 代码触发，不处理监听事件。" );
                return;
            }
            recursionChecked(noticeScopeBeans, isChecked);
            if (sendObj == 1 || sendObj == 2) {
                showNoticeScopeNumber(getAllCheckedClassIds(noticeScopeBeans, new ArrayList<String>()).size());
            } else {
                showNoticeScopeNumber(getAllCheckedDepartmentIds(noticeScopeBeans, new ArrayList<String>()).size());
            }
        });
    }

    /**
     * 递归实现下级是否选中
     *
     * @param noticeScopeBean
     */
    private void recursionChecked(List<NoticeScopeBean> noticeScopeBean, boolean isChecked) {
        for (NoticeScopeBean scopeBean : noticeScopeBean) {
            scopeBean.setChecked(isChecked);
            if (scopeBean.getList() != null) {
                recursionChecked(scopeBean.getList(), isChecked);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected NoticeScopePresenter createPresenter() {
        return new NoticeScopePresenter(this);
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    @OnClick(R.id.btn_complete)
    public void complete() {
        ArrayList<String> classesIds = new ArrayList<>();
        //List<String> ids = getIds(noticeScopeBeans, classesIds);
        if (sendObj == 1 || sendObj == 2) {
            List<String> allCheckedClassIds = getAllCheckedClassIds(noticeScopeBeans, new ArrayList<String>());
            classesIds.addAll(allCheckedClassIds);
        } else {
            List<String> allCheckedDepartmentIds = getAllCheckedDepartmentIds(noticeScopeBeans, new ArrayList<String>());
            classesIds.addAll(allCheckedDepartmentIds);
        }
        if (classesIds.isEmpty()){
            ToastUtils.showShort("请选择通知范围");
            return;
        }
        //返回上一页
        Intent intent = getIntent();
        intent.putExtra("ids", classesIds);
        this.setResult(this.RESULT_OK, intent);
        finish();
    }

    private List<String> getIds(List<NoticeScopeBean> noticeScopeBeans, List<String> ids) {
        for (NoticeScopeBean noticeScopeBean : noticeScopeBeans) {
            int id = noticeScopeBean.getId();
            ids.add("" + id);
            List<NoticeScopeBean> list = noticeScopeBean.getList();
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
        if (studentScopeRsp.getCode() == 200) {
            noticeScopeBeans.clear();
            for (StudentScopeRsp.DataBean.ListBeanXX listBean : studentScopeRsp.getData().getList()) {
                List<StudentScopeRsp.DataBean.ListBeanXX.ListBeanX> grades = listBean.getList();
                //第一层
                NoticeScopeBean noticeScopeBean = new NoticeScopeBean(listBean.getId(), listBean.getName(), listBean.getType());
                if (grades.isEmpty()) {
                    noticeScopeBean.setHasNext(false);
                    noticeScopeBeans.add(noticeScopeBean);
                    continue;
                }
                List<NoticeScopeBean> noticeScopeBeans1 = new ArrayList<>();
                for (StudentScopeRsp.DataBean.ListBeanXX.ListBeanX grade : grades) {
                    List<StudentScopeRsp.DataBean.ListBeanXX.ListBeanX.ListBean> classes = grade.getList();
                    NoticeScopeBean noticeScopeBean1 = new NoticeScopeBean(grade.getId(), grade.getName(), grade.getType());
                    if (classes.isEmpty()) {
                        noticeScopeBean1.setHasNext(false);
                        noticeScopeBeans1.add(noticeScopeBean1);
                        continue;
                    }
                    List<NoticeScopeBean> noticeScopeBeans2 = new ArrayList<>();
                    for (StudentScopeRsp.DataBean.ListBeanXX.ListBeanX.ListBean aClass : classes) {
                        classTotal++;
                        noticeScopeBeans2.add(new NoticeScopeBean(aClass.getId(), aClass.getName(), aClass.getType(), false));
                    }
                    noticeScopeBean1.setList(noticeScopeBeans2);
                    noticeScopeBeans1.add(noticeScopeBean1);
                }
                noticeScopeBean.setList(noticeScopeBeans1);
                noticeScopeBeans.add(noticeScopeBean);
            }
            Log.e(TAG, "getStudentScopeSuccess: " + noticeScopeBeans.toString());
            if (noticeScopeBeans.isEmpty()){
                ToastUtils.showShort("没有找到通知范围数据！");
            }
            adapter.notifyDataSetChanged();
        }
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
                if (datum == null){
                    continue;
                }
                List<DepartmentScopeRsp.DataBean.ListBeanXX> list = datum.getList();
                NoticeScopeBean noticeScopeBean = new NoticeScopeBean(datum.getId(), datum.getName());
                List<NoticeScopeBean> noticeScopeBeans1 = new ArrayList<>();
                if (list.isEmpty()) {
                    departmentTotal++;
                    noticeScopeBeans.add(noticeScopeBean);
                    continue;
                }
                for (DepartmentScopeRsp.DataBean.ListBeanXX listBeanXX : list) {
                    List<DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX> list1 = listBeanXX.getList();
                    NoticeScopeBean noticeScopeBean2 = new NoticeScopeBean(listBeanXX.getId(), listBeanXX.getName());
                    if (list1.isEmpty()) {
                        departmentTotal++;
                        noticeScopeBeans1.add(noticeScopeBean2);
                        continue;
                    }
                    List<NoticeScopeBean> noticeScopeBeans2 = new ArrayList<>();
                    for (DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX listBeanX : list1) {
                        List<DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX.ListBean> list2 = listBeanX.getList();
                        NoticeScopeBean noticeScopeBean3 = new NoticeScopeBean(listBeanX.getId(), listBeanX.getName());
                        if (list2.isEmpty()) {
                            departmentTotal++;
                            noticeScopeBeans2.add(noticeScopeBean3);
                            continue;
                        }
                        List<NoticeScopeBean> noticeScopeBeans3 = new ArrayList<>();
                        for (DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX.ListBean listBean : list2) {
                            departmentTotal++;
                            NoticeScopeBean noticeScopeBean4 = new NoticeScopeBean(listBean.getId(), listBean.getName());
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
            if (noticeScopeBeans.isEmpty()){
                ToastUtils.showShort("没有找到通知范围数据！");
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getDepartmentListFail(String msg) {
        Log.e(TAG, "getDepartmentListFail: " + "请求数据失败：" + msg);
        ToastUtils.showShort("请求数据失败：" + msg);
    }
}