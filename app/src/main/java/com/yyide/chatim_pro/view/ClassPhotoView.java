package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.ClassesPhotoBannerRsp;
import com.yyide.chatim_pro.model.ClassesPhotoRsp;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.model.StudentHonorRsp;

/**
 * 作者：LRZ on 2021年4月5日
 */
public interface ClassPhotoView extends BaseView {
    void getClassesPhotoSuccess(ClassesPhotoRsp model);

    void getClassesPhotoFail(String msg);

}
