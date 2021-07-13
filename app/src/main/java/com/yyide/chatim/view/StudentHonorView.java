package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.StudentHonorRsp;

/**
 * 作者：LRZ on 2021年4月5日
 */
public interface StudentHonorView extends BaseView {
    void getStudentHonorSuccess(StudentHonorRsp model);

    void getStudentHonorFail(String msg);

}
