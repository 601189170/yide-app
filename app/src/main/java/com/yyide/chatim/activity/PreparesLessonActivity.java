package com.yyide.chatim.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.PreparesLessonRep;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.presenter.PreparesLessonPresenter;
import com.yyide.chatim.view.PreparesLessonView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 备课
 */
public class PreparesLessonActivity extends BaseMvpActivity<PreparesLessonPresenter> implements PreparesLessonView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_class_name)
    TextView tv_class_name;
    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_before_class_num)
    TextView tv_before_class_num;
    @BindView(R.id.tv_after_class_num)
    TextView tv_after_class_num;
    @BindView(R.id.tv_after_class_name)
    TextView tv_after_class_name;
    @BindView(R.id.et_input_before_class)
    EditText et_input_before_class;
    @BindView(R.id.et_input_after_class)
    EditText et_input_after_class;
    @BindView(R.id.et_equipment)
    EditText et_tool;
    @BindView(R.id.cl_add)
    ConstraintLayout cl_add;

    @BindView(R.id.cl_homework1)
    ConstraintLayout cl_homework1;
    @BindView(R.id.cl_homework2)
    ConstraintLayout cl_homework2;
    @BindView(R.id.cl_homework3)
    ConstraintLayout cl_homework3;
    @BindView(R.id.cl_homework4)
    ConstraintLayout cl_homework4;
    @BindView(R.id.cl_homework5)
    ConstraintLayout cl_homework5;

    @BindView(R.id.et_input_homework1)
    EditText et_input_homework1;
    @BindView(R.id.et_input_homework2)
    EditText et_input_homework2;
    @BindView(R.id.et_input_homework3)
    EditText et_input_homework3;
    @BindView(R.id.et_input_homework4)
    EditText et_input_homework4;
    @BindView(R.id.et_input_homework5)
    EditText et_input_homework5;
    private SelectSchByTeaidRsp.DataBean dataBean;
    private String dateTime;

    @Override
    public int getContentViewID() {
        return R.layout.activity_prepareslesson;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("备课");
        initTextListener();
        dateTime = getIntent().getStringExtra("dateTime");
        setData();
    }

    private void setData() {
        dataBean = (SelectSchByTeaidRsp.DataBean) getIntent().getSerializableExtra("dataBean");
        if (dataBean != null) {
            tv_class_name.setText(dataBean.classesName + "\t " + dataBean.subjectName);
            tv_after_class_name.setText(dataBean.classesName);
            tv_code.setText("0".equals(dataBean.section) ? "早读" : "第" + dataBean.section + "节");
            tv_time.setText(dataBean.fromDateTime + "-" + dataBean.toDateTime);
            et_input_before_class.setText(dataBean.beforeClass);
            et_input_after_class.setText(dataBean.afterClass);
            et_tool.setText(dataBean.teachTool);
            if (dataBean.lessonsSubEntityList != null && dataBean.lessonsSubEntityList.size() > 0) {
                for (int i = 0; i < dataBean.lessonsSubEntityList.size(); i++) {
                    switch (i) {
                        case 0:
                            et_input_homework1.setText(dataBean.lessonsSubEntityList.get(i));
                            break;
                        case 1:
                            et_input_homework2.setText(dataBean.lessonsSubEntityList.get(i));
                            cl_homework2.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            et_input_homework3.setText(dataBean.lessonsSubEntityList.get(i));
                            cl_homework3.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            et_input_homework4.setText(dataBean.lessonsSubEntityList.get(i));
                            cl_homework4.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            et_input_homework5.setText(dataBean.lessonsSubEntityList.get(i));
                            cl_homework5.setVisibility(View.VISIBLE);
                            cl_add.setVisibility(View.GONE);
                            break;
                    }

                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initTextListener() {

        et_input_before_class.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_before_class_num.setText(s.length() + "/300");
                if (s.length() >= 300) {
                    ToastUtils.showShort("输入字数已达上限");
                }
            }
        });

        et_input_after_class.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_after_class_num.setText(s.length() + "/300");
                if (s.length() >= 300) {
                    ToastUtils.showShort("输入字数已达上限");
                }
            }
        });
        et_input_after_class.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                //通知父控件不要干扰
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                //通知父控件不要干扰
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });

        et_input_before_class.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //通知父控件不要干扰
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    //通知父控件不要干扰
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    protected PreparesLessonPresenter createPresenter() {
        return new PreparesLessonPresenter(this);
    }

    @OnClick({R.id.back_layout, R.id.tv_save, R.id.cl_add})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.tv_save:
                savePrepares();
                break;
            case R.id.cl_add:
                if (!cl_homework2.isShown()) {
                    cl_homework2.setVisibility(View.VISIBLE);
                } else if (!cl_homework3.isShown()) {
                    cl_homework3.setVisibility(View.VISIBLE);
                } else if (!cl_homework4.isShown()) {
                    cl_homework4.setVisibility(View.VISIBLE);
                } else if (!cl_homework5.isShown()) {
                    cl_homework5.setVisibility(View.VISIBLE);
                    cl_add.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void savePrepares() {
        String homework1 = et_input_homework1.getText().toString().trim();
        String homework2 = et_input_homework2.getText().toString().trim();
        String homework3 = et_input_homework3.getText().toString().trim();
        String homework4 = et_input_homework4.getText().toString().trim();
        String homework5 = et_input_homework5.getText().toString().trim();
        String before_class = et_input_before_class.getText().toString().trim();
        String after_class = et_input_after_class.getText().toString().trim();
        String equipment = et_tool.getText().toString().trim();

        //将参数装入实体类
        PreparesLessonRep lessonRep = new PreparesLessonRep();
        lessonRep.setTimetableSchedulSubId(dataBean.subid);
        lessonRep.setLessonsId(dataBean.lessonsId);
        lessonRep.setLessonsDate(dateTime);
        lessonRep.setId(dataBean.lessonsId);
        lessonRep.setBeforeClass(before_class);
        lessonRep.setAfterClass(after_class);
        List<String> teachToolList = new ArrayList<>();
        teachToolList.add(equipment);
        lessonRep.setTeachToolList(teachToolList);
        List<PreparesLessonRep.LessonsSubEntityList> lessonsSubEntityList = new ArrayList<>();
        if (!TextUtils.isEmpty(homework1)) {
            PreparesLessonRep.LessonsSubEntityList item = new PreparesLessonRep.LessonsSubEntityList();
            item.setInformation(homework1);
            item.setClassesId(dataBean.classesId);
            lessonsSubEntityList.add(item);
        }
        if (!TextUtils.isEmpty(homework2)) {
            PreparesLessonRep.LessonsSubEntityList item2 = new PreparesLessonRep.LessonsSubEntityList();
            item2.setInformation(homework2);
            item2.setClassesId(dataBean.classesId);
            lessonsSubEntityList.add(item2);
        }
        if (!TextUtils.isEmpty(homework3)) {
            PreparesLessonRep.LessonsSubEntityList item3 = new PreparesLessonRep.LessonsSubEntityList();
            item3.setInformation(homework3);
            item3.setClassesId(dataBean.classesId);
            lessonsSubEntityList.add(item3);
        }
        if (!TextUtils.isEmpty(homework4)) {
            PreparesLessonRep.LessonsSubEntityList item4 = new PreparesLessonRep.LessonsSubEntityList();
            item4.setInformation(homework4);
            item4.setClassesId(dataBean.classesId);
            lessonsSubEntityList.add(item4);
        }
        if (!TextUtils.isEmpty(homework5)) {
            PreparesLessonRep.LessonsSubEntityList item5 = new PreparesLessonRep.LessonsSubEntityList();
            item5.setInformation(homework5);
            item5.setClassesId(dataBean.classesId);
            lessonsSubEntityList.add(item5);
        }
        lessonRep.setLessonsSubEntityList(lessonsSubEntityList);
        if (dataBean.lessonsId <= 0) {//有id表示作业已存在 走修改逻辑
            mvpPresenter.addLessons(lessonRep);
        } else {
            mvpPresenter.updateLessons(lessonRep);
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void addPreparesLessonSuccess(String msg) {
        EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_PREPARES_SAVE, ""));
        ToastUtils.showShort(msg);
        finish();
    }

    @Override
    public void addPreparesLessonFail(String msg) {
        ToastUtils.showShort(msg);
    }
}