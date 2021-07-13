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
import com.yyide.chatim.databinding.ActivityAddressBookBinding;
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
    List<DepartmentScopeRsp2.DataBean> noticeScopeBeans = new ArrayList<>();
    private AddressBookAdapter adapter;
    private DepartmentScopeRsp2.DataBean addressBookBean;
    private ActivityAddressBookBinding viewBinding;

    @Override
    public int getContentViewID() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityAddressBookBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewBinding.top.title.setText(R.string.select_carbon_copy_people);
        viewBinding.top.backLayout.setOnClickListener(v -> finish());
        viewBinding.btnComplete.setOnClickListener(v -> complete());
        initRecyclerView();
        mvpPresenter.queryDepartmentOverrideList();
    }

    private void initRecyclerView() {
        viewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressBookAdapter(this, noticeScopeBeans);
        viewBinding.recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        viewBinding.recyclerview.setAdapter(adapter);
        adapter.setOnCheckBoxChangeListener(() -> {
            //选中改变，更新底部的数据显示
            showNoticeScopeNumber(getAllCheckedDepartmentIds2(noticeScopeBeans, new ArrayList<>()).size());
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
        showNoticeScopeNumber(0);
    }

    private void showNoticeScopeNumber(int totalNumber) {
        viewBinding.tvSelectedMember.setText(String.format(getString(R.string.notice_scope_number_text), totalNumber));
    }

    private List<AddressBookRsp.DataBean> getAllCheckedDepartmentIds2(List<DepartmentScopeRsp2.DataBean> beanList, List<AddressBookRsp.DataBean> ids) {
        for (DepartmentScopeRsp2.DataBean bean : beanList) {
            final List<AddressBookRsp.DataBean> deptMemberList = bean.getDeptMemberList();
            for (AddressBookRsp.DataBean dataBean : deptMemberList) {
                if (dataBean.isChecked()){
                    ids.add(dataBean);
                }
            }
            if (bean.getList() != null && !bean.getList().isEmpty()) {
                getAllCheckedDepartmentIds2(bean.getList(), ids);
            } else {
                continue;
            }
        }
        return ids;
    }

    @Override
    protected AddressBookPresenter createPresenter() {
        return new AddressBookPresenter(this);
    }

    public void complete() {
        ArrayList<AddressBookRsp.DataBean> deptList = new ArrayList<>();
        List<AddressBookRsp.DataBean> allCheckedDepartmentIds = getAllCheckedDepartmentIds2(noticeScopeBeans, new ArrayList<>());
        deptList.addAll(allCheckedDepartmentIds);
        if (deptList.isEmpty()) {
            ToastUtils.showShort("请选择抄送人");
            return;
        }
        //返回上一页
        Intent intent = getIntent();
        intent.putExtra("deptList", JSON.toJSONString(deptList));
        this.setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void showError() {

    }

    @Override
    public void getDepartmentListSuccess2(DepartmentScopeRsp2 departmentScopeRsp2) {
        if (departmentScopeRsp2.getCode() == 200) {
            noticeScopeBeans.clear();
            noticeScopeBeans.addAll(departmentScopeRsp2.getData());
            Log.e(TAG, "getStudentScopeSuccess: " + noticeScopeBeans.toString());
            showBlank(noticeScopeBeans.isEmpty());
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
        viewBinding.blankPage.setVisibility(show?View.VISIBLE:View.GONE);
        viewBinding.gpMainContent.setVisibility(show?View.GONE:View.VISIBLE);
    }
}