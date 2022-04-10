package com.yyide.chatim.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.HomeNoticeRsp;

public interface FaceUploadView extends BaseView {
    void faceUploadSuccess(String url);
    void faceUploadFail(String msg);

    void getFaceDataSuccess(FaceOssBean faceOssBean);
    void getFaceDataFail(String msg);
    void updateSuccess();
    void updateFail(String msg);

}
