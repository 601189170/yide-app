package com.yyide.chatim_pro.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.FaceOssBean;
import com.yyide.chatim_pro.model.HomeNoticeRsp;

public interface FaceUploadView extends BaseView {
    void faceUploadSuccess(String url);
    void faceUploadFail(String msg);

    void getFaceDataSuccess(FaceOssBean faceOssBean);
    void getFaceDataFail(String msg);
    void updateSuccess();
    void updateFail(String msg);

}
