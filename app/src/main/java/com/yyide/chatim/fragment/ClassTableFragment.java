package com.yyide.chatim.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.TableAdapter;
import com.yyide.chatim.adapter.TableTimeAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.ClassRsp;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.presenter.ClassTablePresenter;
import com.yyide.chatim.view.ClassTableView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ClassTableFragment extends BaseMvpFragment<ClassTablePresenter> implements ClassTableView {


    @BindView(R.id.day)
    RelativeLayout day;
    @BindView(R.id.left_couse)
    LinearLayout leftCouse;
    @BindView(R.id.titleLayout)
    LinearLayout titleLayout;
    @BindView(R.id.grid)
    GridView grid;
    @BindView(R.id.left_type)
    RelativeLayout leftType;
    private View mBaseView;
    TableAdapter adapter1, adapter2, adapter3;
    List<TextView> listTextView = new ArrayList<>();
    int stuas = 0;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    TableTimeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.class_table_fragmnet, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mvpPresenter.getMyData();

        createLeftView(10);
        createItemCourseView("111", 0, 1, 0);
        createItemCourseView("222", 1, 1, 0);
        createItemCourseView("语文", 2, 1, 0);
        createItemCourseView("數學", 3, 2, 0);
        createItemCourseView("英語", 5, 1, 0);
        createItemCourseView("英語", 6, 1, 0);
        createItemCourseView("英語", 7, 2, 0);
        createItemCourseView("英語", 9, 1, 0);
        createItemCourseView("物理", 0, 1, 1);
        createItemCourseView("物理", 9, 1, 1);
        titleLayout.setVisibility(View.GONE);
        adapter = new TableTimeAdapter();
        grid.setAdapter(adapter);
        createLeftTypeView(0,1,1);
        createLeftTypeView(1,2,2);
        createLeftTypeView(3,3,3);
//        createLeftTypeView(6,4,4);
//        titleLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (stuas==1){
//                    for (TextView textView : listTextView) {
//                        textView.setVisibility(View.VISIBLE);
//                    }
//                    stuas=0;
//                }else {
//                    for (TextView textView : listTextView) {
//                        textView.setVisibility(View.GONE);
//                    }
//                    stuas=1;
//                }
//
////                new LeftMenuPop(mActivity);
//                new BottomMenuPop(mActivity);
//            }
//        });
        mvpPresenter.listAllBySchoolId();

    }

    @Override
    protected ClassTablePresenter createPresenter() {
        return new ClassTablePresenter(this);
    }

    //创建"第上下午"视图
    private void createLeftTypeView(int selection,int type,int length) {

        int CouseHeight = 181;
        int CouseWith = 121;

            View view = LayoutInflater.from(mActivity).inflate(R.layout.course_card_type2, null);
//            View view = LayoutInflater.from(mActivity).inflate(R.layout.course_card, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CouseHeight * length); //设置布局高度,即跨多少节课
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CouseHeight*length);
            view.setY(CouseHeight * selection);

            view.setLayoutParams(params);
            TextView text = view.findViewById(R.id.text_view);
            text.setText("");

        switch (type){
            case 1:
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type1));
                text.setText("早\n读");
                break;
            case 2:
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type2));
                text.setText("上\n午");
                break;
            case 3:
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type3));
                text.setText("下\n午");
                break;
            case 4:
                view.setBackground(getResources().getDrawable(R.drawable.bg_table_type4));
                text.setText("晚\n自\n习");
                break;
        }
            leftType.addView(view);

    }
    //创建"第几节数"视图
    private void createLeftView(int course) {

        int CouseHeight = 181;
        for (int i = 0; i < course; i++) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.course_card_type, null);
//            view.setBackground(getResources().getDrawable(R.drawable.table_bg2));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CouseHeight);
//            view.setY(i*CouseHeight);
            view.setLayoutParams(params);
            TextView text = view.findViewById(R.id.text_view);
            text.setText(i + "");
            leftCouse.addView(view);
        }
    }

    //创建单个课程视图
    private void createItemCourseView(String name, int selection, int length, int week) {

        int CouseWith = 121;
        int CouseHeight = 181;
        final View v = LayoutInflater.from(mActivity).inflate(R.layout.course_card, null); //加载单个课程布局
//            v.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始

//        v.setBackground(getResources().getDrawable(R.drawable.table_bg2));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (CouseWith, CouseHeight * length); //设置布局高度,即跨多少节课
        //设置起始位置
        v.setX(week * CouseWith);
        v.setY(CouseHeight * selection);
        v.setLayoutParams(params);
        TextView text = v.findViewById(R.id.text_view);
        text.setText(name); //显示课程名
        listTextView.add(text);
        day.addView(v);


    }


    @Override
    public void listAllBySchoolId(listAllBySchoolIdRsp rsp) {

    }

    @Override
    public void listAllBySchoolIdFail(String msg) {

    }


    void listTimeData(int classId) {
        ClassRsp rsp = new ClassRsp();
        rsp.classId = classId;
        RequestBody requestBody = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(rsp));

        //请求组合创建
        Request request = new Request.Builder()
                .url(BaseConstant.URL_IP + "/timetable/cloud-timetable/timetable/listTimeDataByApp")
                .addHeader("Authorization", SpData.User().token)
                .post(requestBody)
                .build();
        //发起请求

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String data = response.body().string();
//                Log.e("TAG", "mOkHttpClient==>: " + data);
//                SelectUserSchoolRsp bean = JSON.parseObject(data, SelectUserSchoolRsp.class);
//                if (bean.code==BaseConstant.REQUEST_SUCCES2){
//                    Tologin(bean.data.username,bean.data.password, String.valueOf(schoolId));
//                }
            }
        });
    }
}
