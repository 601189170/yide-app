package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.FaceOssBean;
import com.yyide.chatim_pro.model.UserInfo;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface UserView extends BaseView {
    void updateSuccess(String msg);
    void updateFail(String msg);
    void uploadFileSuccess(String imgUrl);
    void uploadFileFail(String msg);
    void getFaceDataSuccess(FaceOssBean faceOssBean);
    void getFaceDataFail(String msg);
    void getUserInfoSuccess(UserInfo userInfo);
}
