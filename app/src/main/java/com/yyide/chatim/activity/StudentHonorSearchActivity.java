package com.yyide.chatim.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.ItemStudentHonorAdapter;
import com.yyide.chatim.adapter.ItemStudentHonorCountAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.model.StudentHonorBean;
import com.yyide.chatim.model.StudentHonorCountBean;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Description: 学生荣誉搜索界面
 * @Author: liu tao
 * @CreateDate: 2021/3/26 9:20
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/26 9:20
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class StudentHonorSearchActivity extends BaseActivity {

    private static final String TAG = "StudentHonorSearchActiv";

    @BindView(R.id.student_honor_count_list_rv)
    RecyclerView studentHonorCountListRv;

    @BindView(R.id.student_honor_content_list_rv)
    RecyclerView studentHonorContentListRv;

    @BindView(R.id.search_et)
    EditText searchEt;//搜索框

    List<StudentHonorBean> studentHonorBeanList;
    List<StudentHonorCountBean> studentHonorCountBeanList;
    List<StudentHonorBean> currentSearchResultList;
    ItemStudentHonorCountAdapter adapter;
    ItemStudentHonorAdapter adapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        studentHonorCountBeanList = new ArrayList<>();
        currentSearchResultList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            studentHonorCountBeanList.add(new StudentHonorCountBean("学生" + i, (int) (Math.random() * 10)));
        }

        studentHonorBeanList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            studentHonorBeanList.add(new StudentHonorBean("url", "学生" + i, "2021.03.0" + i));
        }

        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        studentHonorCountListRv.setLayoutManager(layoutManager);
        adapter = new ItemStudentHonorCountAdapter(this, studentHonorCountBeanList);
        studentHonorCountListRv.setAdapter(adapter);
        studentHonorCountListRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setItemOnClickedListener(position -> {
            StudentHonorCountBean studentHonorCountBean = studentHonorCountBeanList.get(position);
            Log.e(TAG, "initView: " + position + "," + studentHonorCountBeanList.get(position));
            String studentName = studentHonorCountBean.getStudentName();

            searchEt.setText(studentName);
            searchByKeyWord(studentName);
        });
        initStudentHonorContent();
        //搜索学生荣誉
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e(TAG, "onEditorAction: "+actionId );
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) searchEt.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(StudentHonorSearchActivity
                                            .this.getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    String keyWord = searchEt.getText().toString();
                    searchByKeyWord(keyWord);
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 关键词搜索学生荣誉
     * @param keyWord
     */
    private void searchByKeyWord(String keyWord) {
        currentSearchResultList.clear();
        for (int i = 0; i < studentHonorBeanList.size(); i++) {
            StudentHonorBean bean = studentHonorBeanList.get(i);
            if (bean.getName().contains(keyWord)){
                currentSearchResultList.add(bean);
            }
        }
        //判断是否搜索到内容
        if (currentSearchResultList.isEmpty()){
            ToastUtils.showShort("没有搜索到");
        }else {
            studentHonorCountListRv.setVisibility(View.GONE);
            studentHonorContentListRv.setVisibility(View.VISIBLE);
            adapter2.notifyDataSetChanged();
        }
    }

    private void initStudentHonorContent() {
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        studentHonorContentListRv.setLayoutManager(layoutManager2);
        adapter2 = new ItemStudentHonorAdapter(this, currentSearchResultList);
        studentHonorContentListRv.setAdapter(adapter2);
        studentHonorContentListRv.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(this,10)));
    }


    @Override
    public int getContentViewID() {
        return R.layout.activity_student_honor_search;
    }

    @OnClick(R.id.cancel_tv)
    public void cancel(View view) {
        this.finish();
    }

}
