package com.yyide.chatim.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/6 16:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 16:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TemplateListRsp {


    /**
     * code : 200
     * success : true
     * msg : 成功
     * data : {"records":[{"id":6,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-13T02:48:00.000+0000","updatedBy":null,"updatedDateTime":"2021-04-13T02:48:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"22322222大夫","content":"<p>2<img src=\"https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/plugins/emoticons/img/smiley-cool.gif\" alt=\"cool\" /><\/p>","tempId":null},{"id":7,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-13T02:55:03.000+0000","updatedBy":null,"updatedDateTime":"2021-04-13T02:55:03.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"阿顺丰到付","content":"<p>范德萨发的撒发暗室逢灯三<\/p>","tempId":null},{"id":8,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-12T08:27:32.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T08:27:32.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"范德萨发","content":"<p>发士大夫<\/p>","tempId":null},{"id":9,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-13T02:47:08.000+0000","updatedBy":null,"updatedDateTime":"2021-04-13T02:47:08.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"2321321","content":"<p>21213213<\/p>","tempId":null},{"id":10,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-12T08:39:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T08:39:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"发的发生","content":"<p>范德萨发电费<\/p>","tempId":null},{"id":11,"delInd":"0","createdBy":"13045614523","createdDateTime":"2021-04-12T10:08:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T10:08:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"233d","content":"<p><img src=\"https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/plugins/emoticons/img/smiley-foot-in-mouth.gif\" alt=\"foot-in-mouth\" /><\/p>","tempId":null},{"id":12,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-13T02:47:44.000+0000","updatedBy":null,"updatedDateTime":"2021-04-13T02:47:44.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"标2题1","content":"123","tempId":null},{"id":13,"delInd":"0","createdBy":"13045614523","createdDateTime":"2021-04-12T11:30:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T11:30:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"范德萨发","content":"<p>发的发<\/p>","tempId":null},{"id":14,"delInd":"0","createdBy":"13045614523","createdDateTime":"2021-04-12T11:52:15.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T11:52:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"范德萨发生","content":"<p>发的说法的<\/p>","tempId":null}],"total":9,"size":10,"current":1,"searchCount":true,"pages":1}
     */

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * records : [{"id":6,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-13T02:48:00.000+0000","updatedBy":null,"updatedDateTime":"2021-04-13T02:48:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"22322222大夫","content":"<p>2<img src=\"https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/plugins/emoticons/img/smiley-cool.gif\" alt=\"cool\" /><\/p>","tempId":null},{"id":7,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-13T02:55:03.000+0000","updatedBy":null,"updatedDateTime":"2021-04-13T02:55:03.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"阿顺丰到付","content":"<p>范德萨发的撒发暗室逢灯三<\/p>","tempId":null},{"id":8,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-12T08:27:32.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T08:27:32.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"范德萨发","content":"<p>发士大夫<\/p>","tempId":null},{"id":9,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-13T02:47:08.000+0000","updatedBy":null,"updatedDateTime":"2021-04-13T02:47:08.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"2321321","content":"<p>21213213<\/p>","tempId":null},{"id":10,"delInd":"0","createdBy":"18320782675","createdDateTime":"2021-04-12T08:39:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T08:39:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"发的发生","content":"<p>范德萨发电费<\/p>","tempId":null},{"id":11,"delInd":"0","createdBy":"13045614523","createdDateTime":"2021-04-12T10:08:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T10:08:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"233d","content":"<p><img src=\"https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/plugins/emoticons/img/smiley-foot-in-mouth.gif\" alt=\"foot-in-mouth\" /><\/p>","tempId":null},{"id":12,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-13T02:47:44.000+0000","updatedBy":null,"updatedDateTime":"2021-04-13T02:47:44.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"标2题1","content":"123","tempId":null},{"id":13,"delInd":"0","createdBy":"13045614523","createdDateTime":"2021-04-12T11:30:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T11:30:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"范德萨发","content":"<p>发的发<\/p>","tempId":null},{"id":14,"delInd":"0","createdBy":"13045614523","createdDateTime":"2021-04-12T11:52:15.000+0000","updatedBy":null,"updatedDateTime":"2021-04-12T11:52:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"范德萨发生","content":"<p>发的说法的<\/p>","tempId":null}]
         * total : 9
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

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public boolean isSearchCount() {
            return searchCount;
        }

        public void setSearchCount(boolean searchCount) {
            this.searchCount = searchCount;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean {
            /**
             * id : 6
             * delInd : 0
             * createdBy : 18320782675
             * createdDateTime : 2021-04-13T02:48:00.000+0000
             * updatedBy : null
             * updatedDateTime : 2021-04-13T02:48:00.000+0000
             * versionStamp : 0
             * total : 0
             * size : 10
             * current : 1
             * title : 22322222大夫
             * content : <p>2<img src="https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/plugins/emoticons/img/smiley-cool.gif" alt="cool" /></p>
             * tempId : null
             */

            private int id;
            private String delInd;
            private String createdBy;
            private String createdDateTime;
            private Object updatedBy;
            private String updatedDateTime;
            private int versionStamp;
            private int total;
            private int size;
            private int current;
            private String title;
            private String content;
            private Object tempId;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDelInd() {
                return delInd;
            }

            public void setDelInd(String delInd) {
                this.delInd = delInd;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getCreatedDateTime() {
                return createdDateTime;
            }

            public void setCreatedDateTime(String createdDateTime) {
                this.createdDateTime = createdDateTime;
            }

            public Object getUpdatedBy() {
                return updatedBy;
            }

            public void setUpdatedBy(Object updatedBy) {
                this.updatedBy = updatedBy;
            }

            public String getUpdatedDateTime() {
                return updatedDateTime;
            }

            public void setUpdatedDateTime(String updatedDateTime) {
                this.updatedDateTime = updatedDateTime;
            }

            public int getVersionStamp() {
                return versionStamp;
            }

            public void setVersionStamp(int versionStamp) {
                this.versionStamp = versionStamp;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getCurrent() {
                return current;
            }

            public void setCurrent(int current) {
                this.current = current;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public Object getTempId() {
                return tempId;
            }

            public void setTempId(Object tempId) {
                this.tempId = tempId;
            }
        }
    }
}
