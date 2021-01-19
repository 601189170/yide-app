package com.yyide.chatim.home.adress;

        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.alibaba.fastjson.JSON;
        import com.yyide.chatim.R;
        import com.yyide.chatim.adapter.TableAdapter;
        import com.yyide.chatim.base.BaseMvpFragment;
        import com.yyide.chatim.model.DeviceUpdateRsp;
        import com.yyide.chatim.model.GetStuasRsp;
        import com.yyide.chatim.presenter.GetstuasPresenter;
        import com.yyide.chatim.view.getstuasView;

        import java.util.ArrayList;
        import java.util.List;

        import androidx.annotation.Nullable;
        import butterknife.BindView;


public class AdressFragment extends BaseMvpFragment<GetstuasPresenter> implements getstuasView {


    @BindView(R.id.day)
    RelativeLayout day;
    @BindView(R.id.left_couse)
    LinearLayout leftCouse;
    @BindView(R.id.titleLayout)
    LinearLayout titleLayout;
    private View mBaseView;
    TableAdapter adapter1, adapter2, adapter3;
    List<TextView> listTextView = new ArrayList<>();
    int stuas=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.adress_fragmnet, container, false);


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
        titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (stuas==1){
                    for (TextView textView : listTextView) {
                        textView.setVisibility(View.VISIBLE);
                    }
                    stuas=0;
                }else {
                    for (TextView textView : listTextView) {
                        textView.setVisibility(View.GONE);
                    }
                    stuas=1;
                }


            }
        });

    }

    //创建"第几节数"视图
    private void createLeftView(int course) {

        int CouseHeight = 200;
        for (int i = 0; i < course; i++) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.course_card, null);
            view.setBackground(getResources().getDrawable(R.drawable.table_bg2));
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

        int CouseWith = 180;
        int CouseHeight = 200;
        final View v = LayoutInflater.from(mActivity).inflate(R.layout.course_card, null); //加载单个课程布局
//            v.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始

        v.setBackground(getResources().getDrawable(R.drawable.table_bg2));
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
    protected GetstuasPresenter createPresenter() {

        return new GetstuasPresenter(AdressFragment.this);
    }

    @Override
    public void getData(GetStuasRsp rsp) {
        Log.e("TAG", "GetStuasRsp==》: " + JSON.toJSONString(rsp));
//        showError();
//        mvpPresenter.getMyData2(DeviceUtils.getAndroidID(),"1",17113);
    }


    @Override
    public void getDataFail(String msg) {

    }

    @Override
    public void getData2(DeviceUpdateRsp rsp) {
//        hideLoading();
        Log.e("TAG", "DeviceUpdateRsp==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void getDataFail2(String msg) {
        Log.e("TAG", "getDataFail2(==》: " + JSON.toJSONString(msg));
    }
}
