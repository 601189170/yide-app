package com.yyide.chatim.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.TableAdapter;
import com.yyide.chatim.adapter.TableItemAdapter;
import com.yyide.chatim.adapter.TableSectionAdapter;
import com.yyide.chatim.adapter.TableTimeAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.TableRsp;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.model.listTimeDataByAppRsp;
import com.yyide.chatim.presenter.ClassTablePresenter;
import com.yyide.chatim.view.ClassTableView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import okhttp3.OkHttpClient;


public class ClassTableFragment2 extends BaseMvpFragment<ClassTablePresenter> implements ClassTableView {


    @BindView(R.id.grid)
    GridView grid;
    @BindView(R.id.tablegrid)
    GridView tablegrid;

    @BindView(R.id.listsection)
    GridView listsection;
    @BindView(R.id.classlayout)
    FrameLayout classlayout;
    @BindView(R.id.left_layout)
    RelativeLayout leftLayout;
    private View mBaseView;
    TableAdapter adapter1, adapter2, adapter3;
    List<TextView> listTextView = new ArrayList<>();
    int stuas = 0;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    TableTimeAdapter timeAdapter;
    TableItemAdapter tableItemAdapter;
    List<TableRsp> list;
    TableSectionAdapter tableSectionAdapter;
    int index = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.class_table_fragmnet2, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tableItemAdapter = new TableItemAdapter();
        tablegrid.setAdapter(tableItemAdapter);

        tableSectionAdapter = new TableSectionAdapter();
        listsection.setAdapter(tableSectionAdapter);

        timeAdapter = new TableTimeAdapter();
        grid.setAdapter(timeAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                timeAdapter.setPosition(position);
                index = position;
                tableItemAdapter.setIndex(index);
            }
        });
        tablegrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position % 7;
                tableItemAdapter.setIndex(index);
                timeAdapter.setPosition(index);
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        for (int i = 0; i < timeAdapter.list.size(); i++) {
            if (timeAdapter.list.get(i).day.equals(simpleDateFormat.format(date))) {

                timeAdapter.setPosition(i);

                tableItemAdapter.setIndex(i);
            }
        }
        mvpPresenter.listAllBySchoolId();
        mvpPresenter.listTimeDataByApp(630);
    }

    @Override
    protected ClassTablePresenter createPresenter() {
        return new ClassTablePresenter(this);
    }


    @Override
    public void listAllBySchoolId(listAllBySchoolIdRsp rsp) {
        Log.e("TAG", "listAllBySchoolId==>: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {

        }
    }

    @Override
    public void listAllBySchoolIdFail(String msg) {

    }

    @Override
    public void listTimeDataByApp(listTimeDataByAppRsp rsp) {
        Log.e("TAG", "listTimeDataByApp==>: " + JSON.toJSONString(rsp));

        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
//            setTable(rsp);
            tableItemAdapter.notifyData(rsp.data.subList);

            String jc = rsp.data.timetableStructure;
            String s = jc.replaceAll("节课", "");
            int num = Integer.parseInt(s);
            List<Integer> Sectionlist = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                Sectionlist.add(i);
            }
            tableSectionAdapter.notifyData(Sectionlist);
//            setData(rsp);

        }
        createLeftTypeView(0,1,1);
        createLeftTypeView(1,2,2);
        createLeftTypeView(3,3,3);
    }
    //创建"第上下午"视图
    private void createLeftTypeView(int selection,int type,int length) {

        int CouseHeight = 190;
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
        leftLayout.addView(view);

    }
    @Override
    public void listTimeDataByAppFail(String rsp) {

    }


}
