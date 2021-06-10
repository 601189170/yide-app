package com.yyide.chatim.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/6 15:24
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 15:24
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDetailRsp {


    /**
     * code : 200
     * success : true
     * msg : 成功
     * data : {"id":3,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-09T02:55:49.327+0000","updatedBy":null,"updatedDateTime":"2021-04-09T02:55:49.327+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":274,"readNumber":12,"sendObject":null}
     */

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
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
    }
}
