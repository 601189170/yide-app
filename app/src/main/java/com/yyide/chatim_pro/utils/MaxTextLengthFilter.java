package com.yyide.chatim_pro.utils;

import android.text.InputFilter;
import android.text.Spanned;

import com.blankj.utilcode.util.ToastUtils;

public class MaxTextLengthFilter implements InputFilter {

    private int mMaxLength;

    //构造方法中传入最多能输入的字数
    public MaxTextLengthFilter(int max) {
        mMaxLength = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMaxLength - (dest.length() - (dend - dstart));
        if (keep < (end - start)) {
            ToastUtils.showShort("最多只能输入" + mMaxLength + "个字");
        }
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null;
        } else {
            return source.subSequence(start, start + keep);
        }
    }
}