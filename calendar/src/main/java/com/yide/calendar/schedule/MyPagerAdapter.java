package com.yide.calendar.schedule;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

/**
 * @author liu tao
 * @date 2022/1/5 10:20
 * @description 描述
 */
public class MyPagerAdapter extends PagerAdapter {
    private Context context;
    private List<View> viewList;

    public MyPagerAdapter(Context context, List<View> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = viewList.get(position);
        //判断其父容器是否存在，如存在，先和此子控件解除关系
        ViewPager parent = (ViewPager) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        if (position < viewList.size()) {
            container.removeView(viewList.get(position));
        }
    }
}
