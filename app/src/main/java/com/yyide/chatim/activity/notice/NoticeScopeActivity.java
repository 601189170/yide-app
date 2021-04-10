package com.yyide.chatim.activity.notice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.presenter.NoticeScopePresenter;
import com.yyide.chatim.activity.notice.view.NoticeScopeView;
import com.yyide.chatim.adapter.ItemDepartMentScopeAdapter;
import com.yyide.chatim.adapter.ItemStudentScopeAdapter;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.DepartmentScopeRsp;
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
    //private List<TreeItem> checkTreeList = new ArrayList<>();
    //private TreeRecyclerAdapter adapter = new TreeRecyclerAdapter(TreeRecyclerType.SHOW_EXPAND);
    List<StudentScopeRsp.DataBean.ListBean> data = new ArrayList<>();
    List<DepartmentScopeRsp.DataBean> data2 = new ArrayList<>();
    private ItemStudentScopeAdapter adapter;
    private ItemDepartMentScopeAdapter adapter2;
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
        Log.e(TAG, "onCreate: "+sendObj );
        if (sendObj == 1 || sendObj == 2){
            //请求数据
            mvpPresenter.getSectionList(1, 10);
        }else {
            //选择部门
            mvpPresenter.getDepartmentList(1,10);
        }
    }

    private void initRecyclerView() {
        Log.e(TAG, "initRecyclerView: "+sendObj );
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
        if (sendObj == 1 || sendObj == 2){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ItemStudentScopeAdapter(this, data);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.setAdapter(adapter);
        }else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter2 = new ItemDepartMentScopeAdapter(this, data2);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.setAdapter(adapter2);
        }
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
            if (sendObj == 1 || sendObj == 2){
                for (int i = 0; i < data.size(); i++) {
                    StudentScopeRsp.DataBean.ListBean listBean = data.get(i);
                    listBean.setChecked(isChecked);
                    List<StudentScopeRsp.DataBean.ListBean.GradesBean> grades = listBean.getGrades();
                    for (StudentScopeRsp.DataBean.ListBean.GradesBean grade : grades) {
                        grade.setChecked(isChecked);
                        List<StudentScopeRsp.DataBean.ListBean.GradesBean.ClassesBean> classes = grade.getClasses();
                        for (StudentScopeRsp.DataBean.ListBean.GradesBean.ClassesBean aClass : classes) {
                            aClass.setChecked(isChecked);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }else {
                for (int i = 0; i < data2.size(); i++) {
                    DepartmentScopeRsp.DataBean listBean = data2.get(i);
                    listBean.setChecked(isChecked);
                    List<DepartmentScopeRsp.DataBean.ListBeanXX> grades = listBean.getList();
                    for (DepartmentScopeRsp.DataBean.ListBeanXX grade : grades) {
                        grade.setChecked(isChecked);
                        List<DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX> classes = grade.getList();
                        for (DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX aClass : classes) {
                            aClass.setChecked(isChecked);
                        }
                    }
                }
                adapter2.notifyDataSetChanged();
            }

        });
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
        if (sendObj == 1 || sendObj == 2){
            for (StudentScopeRsp.DataBean.ListBean datum : data) {
                for (StudentScopeRsp.DataBean.ListBean.GradesBean grade : datum.getGrades()) {
                    for (StudentScopeRsp.DataBean.ListBean.GradesBean.ClassesBean aClass : grade.getClasses()) {
                        if (aClass.isChecked()){
                            classesIds.add(aClass.getId()+"");
                        }
                    }
                }
            }
        }else {
            for (DepartmentScopeRsp.DataBean datum : data2) {
                for (DepartmentScopeRsp.DataBean.ListBeanXX grade : datum.getList()) {
                    for (DepartmentScopeRsp.DataBean.ListBeanXX.ListBeanX aClass : grade.getList()) {
                        if (aClass.isChecked()){
                            classesIds.add(aClass.getId()+"");
                        }
                    }
                }
            }
        }

        //返回上一页
        Intent intent = getIntent();
        intent.putExtra("ids",classesIds);
        this.setResult(this.RESULT_OK,intent);
        finish();
    }

    @Override
    public void showError() {

    }

    @Override
    public void getStudentScopeSuccess(StudentScopeRsp studentScopeRsp) {
        Log.e(TAG, "getStudentScopeSuccess: "+studentScopeRsp.toString() );
        if (studentScopeRsp.getCode() == 200) {
            data.clear();
            List<StudentScopeRsp.DataBean.ListBean> list = studentScopeRsp.getData().getList();
            data.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getStudentScopeFail(String msg) {
        Log.e(TAG, "getStudentScopeFail: "+"请求数据失败："+msg );
        ToastUtils.showShort("请求数据失败："+msg);
    }

    @Override
    public void getDepartmentListSuccess(DepartmentScopeRsp departmentScopeRsp) {
        Log.e(TAG, "getDepartmentListSuccess: "+departmentScopeRsp.toString() );
        if (departmentScopeRsp.getCode() == 200){
            data2.clear();
            List<DepartmentScopeRsp.DataBean> data = departmentScopeRsp.getData();
            data2.addAll(data);
            adapter2.notifyDataSetChanged();
        }
    }

    @Override
    public void getDepartmentListFail(String msg) {
        Log.e(TAG, "getDepartmentListFail: "+"请求数据失败："+msg );
        ToastUtils.showShort("请求数据失败："+msg);
    }
}