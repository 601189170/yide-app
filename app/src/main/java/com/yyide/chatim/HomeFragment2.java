package com.yyide.chatim;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.base.modelSettings;
import com.yyide.chatim.homemodel.TableFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;


public class HomeFragment2 extends BaseFragment {

    @BindView(R.id.content)
    FrameLayout content;
    private View mBaseView;

    public List<modelSettings> lists=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_fragmnet22, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mvpPresenter.getMyData();
        setmodel("Table");
        setmodel("Notice");
        initHome();

    }

    void setmodel(String id) {
        modelSettings bean=new modelSettings();
        switch (id){
            case "Table":
                bean.x=0;
                bean.y=0;
                break;
            case "Notice":
                bean.x=1;
                bean.y=0;
                break;
        }
        bean.type=id;
        lists.add(bean);
    }
    int[] ids = new int[]{R.id.module1, R.id.module2, R.id.module3, R.id.module4, R.id.module5, R.id.module6, R.id.module7, R.id.module8, R.id.module9, R.id.module10, R.id.module11, R.id.module12};

    void initHome() {
            for (int i = 0; i < lists.size(); i++) {
                LinearLayout frameLayout = new LinearLayout(mActivity);
                frameLayout.setId(ids[i]);
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

                Log.e("TAG", "initHome==>: "+JSON.toJSONString(lists.get(i)));
                if (lists.get(i).x==0){
                    lp.setMargins(10,10,10,10);
                }else {
//                    lp.setMargins(SizeUtils.dp2px(lists.get(i).x / 2 * BaseConstant.BaseWith), SizeUtils.dp2px(lists.get(i).y / 2 * BaseConstant.BaseHeight), 0, 0);
                    lp.setMargins(200,10, 10, 10);
                }
                frameLayout.setLayoutParams(lp);
                content.addView(frameLayout);

                if (fragment(lists.get(i).type) != null) {
                    FragmentManager fme = getChildFragmentManager();
                    FragmentTransaction fte = fme.beginTransaction();
                    Fragment moralAFragment = fragment(lists.get(i).type);
                    fte.replace(frameLayout.getId(), moralAFragment).commit();
                }
            }

    }

    Fragment fragment(String str) {
        if (str.equals("Table")){
            Log.e("TAG", "fragment==>: "+ JSON.toJSONString("Table"));
                return new TableFragment();
        }
        else return null;
    }


}
