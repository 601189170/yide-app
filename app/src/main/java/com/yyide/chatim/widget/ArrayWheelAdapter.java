package com.yyide.chatim.widget;

import android.text.TextUtils;

import com.contrarywind.adapter.WheelAdapter;
import com.yyide.chatim.model.SelectTableClassesRsp;

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

    public SelectTableClassesRsp.DataBean getItemData(int index, String name) {
        if (items != null && items.size() > 0 && index <= items.size() && !TextUtils.isEmpty(name)) {
            SelectTableClassesRsp.DataBean dataBean = items.get(index);
            for (SelectTableClassesRsp.DataBean item : dataBean.getList()) {
                if (name.equals(item.getName())) {
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index).getName();
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
