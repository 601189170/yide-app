package com.yyide.chatim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.adapter.ItemStudentHonorAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.model.StudentHonorBean;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Description: 学生荣誉列表
 * @Author: liu tao
 * @CreateDate: 2021/3/25 15:55
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/25 15:55
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class StudentHonorListActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView title;//标题
    @BindView(R.id.honor_content_rv)
    RecyclerView honorContentRv;

    List<StudentHonorBean> studentHonorBeanList;
    @Override
    public int getContentViewID() {
        return R.layout.activity_student_honor_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("学生荣誉");
        studentHonorBeanList = new ArrayList<>();
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        honorContentRv.setLayoutManager(layoutManager);
        ItemStudentHonorAdapter adapter = new ItemStudentHonorAdapter(this, studentHonorBeanList);
        honorContentRv.setAdapter(adapter);
        //honorContentRv.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(this, 10)));
    }

    @OnClick({R.id.back_iv, R.id.search_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.search_iv:
                startActivity(new Intent(this, StudentHonorSearchActivity.class));
                break;
            default:
                break;
        }
    }
}