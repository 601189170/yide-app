package com.yyide.chatim.widget.scrollpicker.provider;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yyide.chatim.R;
import com.yyide.chatim.widget.scrollpicker.IViewProvider;

public class DefaultItemViewProvider implements IViewProvider<String> {
    @Override
    public int resLayout() {
        return R.layout.scroll_picker_default_item_layout;
    }

    @Override
    public void onBindView(@NonNull View view, @Nullable String text) {
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(text);
        view.setTag(text);
        tv.setTextSize(18);
    }

    @Override
    public void updateView(@NonNull View itemView, boolean isSelected) {
        TextView tv = itemView.findViewById(R.id.tv_content);
        tv.setTextSize(isSelected ? 18 : 14);
        tv.setTextColor(Color.parseColor(isSelected ? "#11C685" : "#000000"));
    }
}
