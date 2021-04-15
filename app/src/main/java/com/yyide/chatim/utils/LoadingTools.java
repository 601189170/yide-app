package com.yyide.chatim.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Window;

import com.yyide.chatim.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Hao on 2016/10/19.
 */

public class LoadingTools {

    public SweetAlertDialog pd(Context context) {
        final SweetAlertDialog sd = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        Window window = sd.getWindow();
        window.setWindowAnimations(R.style.NoAnimationDialog); // 添加动画
        sd.getProgressHelper().setBarColor(Color.parseColor("#37A4FF"));
        sd.setTitleText("Loading");
        sd.setCancelable(true);
        sd.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    sd.dismiss();
                return false;
            }
        });
        return sd;
    }


    public SweetAlertDialog pd2(Context context) {
        final SweetAlertDialog sd = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sd.getProgressHelper().setBarColor(Color.parseColor("#37A4FF"));
        sd.setTitleText("Loading");
        sd.setCancelable(false);

//        sd.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK)
//                    sd.dismiss();
//                return false;
//            }
//        });
        return sd;
    }

}
