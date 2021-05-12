package com.yyide.chatim.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ItemMultiApp implements Serializable, MultiItemEntity {

    /**
     * 1 我的应用
     * 2 tab菜单
     * 3 其他应用
     */
    private int type;
    public static final int TYPE_MY_APP = 1;
    public static final int TYPE_TAB = 2;
    public static final int TYPE_OTHER_APP = 3;

    private MyAppListRsp myAppListRsp;
    private AppItemBean appItemBean;

    @Override
    public int getItemType() {
        return type;
    }
}
