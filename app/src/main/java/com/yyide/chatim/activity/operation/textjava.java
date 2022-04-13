package com.yyide.chatim.activity.operation;

import android.app.Activity;
import android.content.Intent;

import com.yyide.chatim.dialog.SwitchClassAndSubjectPop2;
import com.yyide.chatim.dialog.deletePop;
import com.yyide.chatim.dialog.postClassDataPop;
import com.yyide.chatim.model.AddClassBean;
import com.yyide.chatim.model.selectParentStudent;

import java.util.ArrayList;
import java.util.List;

public class textjava {
    List<AddClassBean> list = new ArrayList();


    void AAAAAAAA() {
        for (int i = 0; i < list.size(); i++) {

        }
        Activity activity = null;
        SwitchClassAndSubjectPop2 pop=new SwitchClassAndSubjectPop2(activity);
        pop.setSSS(new SwitchClassAndSubjectPop2.SSSListener() {
            @Override
            public void setRT(selectParentStudent.Children bean1, selectParentStudent.Children bean2) {

            }
        });
        pop.setSSS(new SwitchClassAndSubjectPop2.SSSListener() {
            @Override
            public void setRT(selectParentStudent.Children bean1, selectParentStudent.Children bean2) {

            }
        });

        for (int i = 0; i < list.size(); i++) {

        }
//        pop.setSelectClasses(new deletePop.SelectDateListener() {
//            @Override
//            public void onSelectDateListener(Boolean br) {
//
//            }
//        });
//        new postClassDataPop(OperationPostWorkActivity.this,postclassList).showAtLocation();
    }


}
