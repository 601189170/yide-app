package com.yyide.chatim.notice.tree;

import java.util.List;

/**
 * Created by KaelLi on 2018/11/26.
 */
public class TreeItem {
    public String title;
    public int itemLevel;
    public int itemState;
    public boolean isCheck;
    public List<TreeItem> child;
}
