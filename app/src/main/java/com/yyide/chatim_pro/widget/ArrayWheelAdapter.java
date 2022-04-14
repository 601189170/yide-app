package com.yyide.chatim_pro.widget;

import android.text.TextUtils;

import com.contrarywind.adapter.WheelAdapter;
import com.yyide.chatim_pro.model.SelectTableClassesRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * The simple Array wheel adapter
 *
 * @param <T> the element type
 */
public class ArrayWheelAdapter<T> implements WheelAdapter {
    // items
    private List<SelectTableClassesRsp.DataBean> items;

    /**
     * Constructor
     *
     * @param items the items
     */
    public ArrayWheelAdapter(List<SelectTableClassesRsp.DataBean> items) {
        this.items = items;
    }

    public List<SelectTableClassesRsp.DataBean> getData() {
        return items == null ? items = new ArrayList<>() : items;
    }

    public SelectTableClassesRsp.DataBean getItemData(int index) {
        if (items != null && items.size() > 0 && index <= items.size()) {
            for (int i = 0; i < items.size(); i++) {
                if (i == index) {
                    return items.get(i);
                }
            }
        }
        return null;
    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index).getShowName();
        }
        return "";
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    @Override
    public int indexOf(Object o) {
        return items.indexOf(o);
    }

}
