package com.yyide.chatim_pro.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;


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

    public ItemMultiApp() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static int getTypeMyApp() {
        return TYPE_MY_APP;
    }

    public static int getTypeTab() {
        return TYPE_TAB;
    }

    public static int getTypeOtherApp() {
        return TYPE_OTHER_APP;
    }

    public MyAppListRsp getMyAppListRsp() {
        return myAppListRsp;
    }

    public void setMyAppListRsp(MyAppListRsp myAppListRsp) {
        this.myAppListRsp = myAppListRsp;
    }

    public AppItemBean getAppItemBean() {
        return appItemBean;
    }

    public void setAppItemBean(AppItemBean appItemBean) {
        this.appItemBean = appItemBean;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
