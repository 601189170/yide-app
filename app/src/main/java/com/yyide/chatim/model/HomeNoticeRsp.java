package com.yyide.chatim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: HomeNoticeRsp
 * @Author: liu tao
 * @CreateDate: 2021/4/6 19:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 19:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeNoticeRsp {


    /**
     * code : 200
     * success : true
     * msg : 成功
     * data : {"id":222,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-08T08:35:37.647+0000","updatedBy":null,"updatedDateTime":"2021-04-08T08:35:37.647+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"这是标题","productionTarget":"小明班主任","productionTime":null,"content":"内容1 内容2","signId":310241259811766272,"timingTime":null,"type":"3","status":"0","totalNumber":1,"readNumber":0,"sendObject":"小明班主任"}
     */

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataBean {
        private long id;
        private String delInd;
        private String createdBy;
        private String createdDateTime;
        private String updatedBy;
        private String updatedDateTime;
        private int versionStamp;
        private int total;
        private int size;
        private int current;
        private String title;
        private String productionTarget;
        private String productionTime;
        private String content;
        private long signId;
        private String timingTime;
        private String type;
        private String status;
        private int totalNumber;
        private int readNumber;
        private String sendObject;
        private boolean hasNoticePermission;
    }
}
