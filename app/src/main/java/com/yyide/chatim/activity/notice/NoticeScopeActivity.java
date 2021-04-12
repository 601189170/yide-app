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
        initView();
        //notifyPrice();
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

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            recursionChecked(noticeScopeBeans, isChecked);
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
        List<String> ids = getIds(noticeScopeBeans, classesIds);
        classesIds.addAll(ids);
        //返回上一页
        Intent intent = getIntent();
        intent.putExtra("ids", classesIds);
        this.setResult(this.RESULT_OK, intent);
        finish();
    }

    private List<String> getIds(List<NoticeScopeBean> noticeScopeBeans,List<String> ids){
        for (NoticeScopeBean noticeScopeBean : noticeScopeBeans) {
            int id = noticeScopeBean.getId();
            ids.add(""+id);
            List<NoticeScopeBean> list = noticeScopeBean.getList();
            if ( list!= null && !list.isEmpty()){
                getIds(list,ids);
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
            for (StudentScopeRsp.DataBean.ListBean listBean : studentScopeRsp.getData().getList()) {
                List<StudentScopeRsp.DataBean.ListBean.GradesBean> grades = listBean.getGrades();
                NoticeScopeBean noticeScopeBean = new NoticeScopeBean(listBean.getId(), listBean.getName());
                if (grades.isEmpty()) {
                    noticeScopeBeans.add(noticeScopeBean);
                    continue;
                }
                List<NoticeScopeBean> noticeScopeBeans1 = new ArrayList<>();
                for (StudentScopeRsp.DataBean.ListBean.GradesBean grade : grades) {
                    List<StudentScopeRsp.DataBean.ListBean.GradesBean.ClassesBean> classes = grade.getClasses();
                    NoticeScopeBean noticeScopeBean1 = new NoticeScopeBean(grade.getId(), grade.getName());
                    if (classes.isEmpty()) {
                        noticeScopeBeans1.add(noticeScopeBean1);
                        continue;
                    }
                    List<NoticeScopeBean> noticeScopeBeans2 = new ArrayList<>();
                    for (StudentScopeRsp.DataBean.ListBean.GradesBean.ClassesBean aClass : classes) {
                        noticeScopeBeans2.add(new NoticeScopeBean(aClass.getId(), aClass.getName()));
                    }
                    noticeScopeBean1.setList(noticeScopeBeans2);
                    noticeScopeBeans1.add(noticeScopeBean1);
                }
                noticeScopeBean.setList(noticeScopeBeans1);
                noticeScopeBeans.add(noticeScopeBean);
            }
            Log.e(TAG, "getStudentScopeSuccess: " + noticeScopeBeans.toString());
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
                List<DepartmentScopeRsp.DataBean.ListBeanXX> list = datum.getList();
                NoticeScopeBean noticeScopeBean = new NoticeScopeBean(datum.getId(), datum.getName());
                List<NoticeScopeBean> noticeScopeBeans1 = new ArrayList<>();
                if (list.isEmpty()) {
                    noticeScopeBeans.add(noticeScopeBean);
                    continue;
                }
                for (DepartmentScopeRsp.DataBean.ListBeanXX listBeanXX : list) {
                    List<DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX> list1 = listBeanXX.getList();
                    NoticeScopeBean noticeScopeBean2 = new NoticeScopeBean(listBeanXX.getId(), listBeanXX.getName());
                    if (list1.isEmpty()) {
                        noticeScopeBeans1.add(noticeScopeBean2);
                        continue;
                    }
                    List<NoticeScopeBean> noticeScopeBeans2 = new ArrayList<>();
                    for (DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX listBeanX : list1) {
                        List<DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX.ListBean> list2 = listBeanX.getList();
                        NoticeScopeBean noticeScopeBean3 = new NoticeScopeBean(listBeanX.getId(), listBeanX.getName());
                        if (list2.isEmpty()) {
                            noticeScopeBeans2.add(noticeScopeBean3);
                            continue;
                        }
                        List<NoticeScopeBean> noticeScopeBeans3 = new ArrayList<>();
                        for (DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX.ListBean listBean : list2) {
                            NoticeScopeBean noticeScopeBean4 = new NoticeScopeBean(listBean.getId(), listBean.getName());
                            noticeScopeBeans3.add(noticeScopeBean4);
                        }
                        noticeScopeBean3.setList(noticeScopeBeans3);
                        noticeScopeBeans2.add(noticeScopeBean3);
                    }
                    noticeScopeBean2.setList(noticeScopeBeans2);
                    noticeScopeBeans1.add(noticeScopeBean2);

                }
                noticeScopeBean.setList(noticeScopeBeans1);
                noticeScopeBeans.add(noticeScopeBean);
            }
            Log.e(TAG, "getStudentScopeSuccess: " + noticeScopeBeans.toString());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getDepartmentListFail(String msg) {
        Log.e(TAG, "getDepartmentListFail: " + "请求数据失败：" + msg);
        ToastUtils.showShort("请求数据失败：" + msg);
    }
}