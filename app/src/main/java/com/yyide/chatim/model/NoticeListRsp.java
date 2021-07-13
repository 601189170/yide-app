package com.yyide.chatim.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 通知公告列表数据
 * @Author: liu tao
 * @CreateDate: 2021/4/6 14:03
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 14:03
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListRsp {


    /**
     * code : 200
     * success : true
     * msg : 成功
     * data : {"records":[{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":3,"signId":null,"timingTime":null,"type":"待办","status":"0","totalNumber":null,"readNumber":null},{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":4,"signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":null,"readNumber":null},{"title":null,"productionTarget":null,"productionTime":1617349545000,"content":"这是推送内容","id":2,"signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":null,"readNumber":null}],"total":3,"size":10,"current":1,"searchCount":true,"pages":1}
     */

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataBean {
        /**
         * records : [{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":3,"signId":null,"timingTime":null,"type":"待办","status":"0","totalNumber":null,"readNumber":null},{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":4,"signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":null,"readNumber":null},{"title":null,"productionTarget":null,"productionTime":1617349545000,"content":"这是推送内容","id":2,"signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":null,"readNumber":null}]
         * total : 3
         * size : 10
         * current : 1
         * searchCount : true
         * pages : 1
         */

        private int total;
        private int size;
        private int current;
        private boolean searchCount;
        private int pages;
        private List<RecordsBean> records;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RecordsBean {
            /**
             * title : null
             * productionTarget : null
             * productionTime : null
             * content : 这是推送内容
             * id : 3
             * signId : null
             * timingTime : null
             * type : 待办
             * status : 0
             * totalNumber : null
             * readNumber : null
             * sendObject
             */

            private String title;
            private String productionTarget;
            private String productionTime;
            private String content;
            private long id;
            private long signId;
            private String timingTime;
            private String type;
            private String status;
            private int totalNumber;
            private int readNumber;
            private String sendObject;
        }}
}
