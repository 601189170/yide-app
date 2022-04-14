package com.yyide.chatim_pro.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
  private int space;
  private int column;
  public SpaceItemDecoration(int space,int column) {
    this.space = space;
    this.column = column;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    //不是第一个的格子都设一个左边和底部的间距
    outRect.left = space;
    outRect.bottom = space;
    //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
    if (parent.getChildLayoutPosition(view) % column == 0) {
      outRect.left = 0;
    }
  }

}
