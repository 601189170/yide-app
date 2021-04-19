package com.yyide.chatim.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/10 14:25
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/10 14:25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class StudentScopeRsp {


    /**
     * code : 200
     * success : true
     * msg : 查詢成功
     * data : {"name":"深圳宝安西乡中学","type":"0","nextLevelType":0,"list":[{"id":155,"delInd":"0","createdBy":"13267180000","createdDateTime":"2021-04-05T01:08:03.000+0000","updatedBy":"636","updatedDateTime":"2021-04-05T01:08:03.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初中","alias":"初中","showType":"1","showName":"初中","schoolId":234,"list":[{"id":79,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初一2021级","alias":"七年级","showType":"1","showName":"七年级","level":0,"schoolId":234,"learningSectionId":155,"list":[{"id":1747,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:21.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:21.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级2班","schoolId":234,"alias":"","showType":"1","gradeId":79,"showName":"七年级2班","headmasterId":266,"secondHeadmasterId":249,"type":"2","headmaster":"老师1","secondHeadmaster":"老师110","classesIds":null,"isExitInd":"Y"},{"id":1748,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T02:35:35.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T02:36:01.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级3班","schoolId":234,"alias":"七年级三班","showType":"1","gradeId":79,"showName":"七年级三班","headmasterId":266,"secondHeadmasterId":273,"type":"2","headmaster":"老师1","secondHeadmaster":"fasfds","classesIds":null,"isExitInd":"Y"},{"id":1762,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:27.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:27.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级4班","schoolId":234,"alias":"","showType":"1","gradeId":79,"showName":"七年级4班","headmasterId":199,"secondHeadmasterId":null,"type":"2","headmaster":"老师002","secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2063,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级5班","schoolId":234,"alias":"","showType":"2","gradeId":79,"showName":"七年级5班","headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2064,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级6班","schoolId":234,"alias":"","showType":"2","gradeId":79,"showName":"七年级6班","headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"}],"type":"1","isExitInd":"Y"},{"id":184,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-06T06:24:02.000+0000","updatedBy":null,"updatedDateTime":"2021-04-06T06:24:02.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初二2020级","alias":"","showType":"1","showName":"初二2020级","level":2020,"schoolId":234,"learningSectionId":155,"list":[{"id":2089,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-06T06:24:02.000+0000","updatedBy":null,"updatedDateTime":"2021-04-06T06:24:02.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初二2020级1班","schoolId":234,"alias":"","showType":"0","gradeId":184,"showName":null,"headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2090,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-06T06:24:02.000+0000","updatedBy":null,"updatedDateTime":"2021-04-06T06:24:02.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初二2020级2班","schoolId":234,"alias":"","showType":"0","gradeId":184,"showName":null,"headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"}],"type":"1","isExitInd":"Y"}],"type":"0","isExitInd":"Y"}],"isExitInd":"Y"}
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
         * name : 深圳宝安西乡中学
         * type : 0
         * nextLevelType : 0
         * list : [{"id":155,"delInd":"0","createdBy":"13267180000","createdDateTime":"2021-04-05T01:08:03.000+0000","updatedBy":"636","updatedDateTime":"2021-04-05T01:08:03.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初中","alias":"初中","showType":"1","showName":"初中","schoolId":234,"list":[{"id":79,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初一2021级","alias":"七年级","showType":"1","showName":"七年级","level":0,"schoolId":234,"learningSectionId":155,"list":[{"id":1747,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:21.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:21.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级2班","schoolId":234,"alias":"","showType":"1","gradeId":79,"showName":"七年级2班","headmasterId":266,"secondHeadmasterId":249,"type":"2","headmaster":"老师1","secondHeadmaster":"老师110","classesIds":null,"isExitInd":"Y"},{"id":1748,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T02:35:35.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T02:36:01.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级3班","schoolId":234,"alias":"七年级三班","showType":"1","gradeId":79,"showName":"七年级三班","headmasterId":266,"secondHeadmasterId":273,"type":"2","headmaster":"老师1","secondHeadmaster":"fasfds","classesIds":null,"isExitInd":"Y"},{"id":1762,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:27.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:27.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级4班","schoolId":234,"alias":"","showType":"1","gradeId":79,"showName":"七年级4班","headmasterId":199,"secondHeadmasterId":null,"type":"2","headmaster":"老师002","secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2063,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级5班","schoolId":234,"alias":"","showType":"2","gradeId":79,"showName":"七年级5班","headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2064,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级6班","schoolId":234,"alias":"","showType":"2","gradeId":79,"showName":"七年级6班","headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"}],"type":"1","isExitInd":"Y"},{"id":184,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-06T06:24:02.000+0000","updatedBy":null,"updatedDateTime":"2021-04-06T06:24:02.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初二2020级","alias":"","showType":"1","showName":"初二2020级","level":2020,"schoolId":234,"learningSectionId":155,"list":[{"id":2089,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-06T06:24:02.000+0000","updatedBy":null,"updatedDateTime":"2021-04-06T06:24:02.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初二2020级1班","schoolId":234,"alias":"","showType":"0","gradeId":184,"showName":null,"headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2090,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-06T06:24:02.000+0000","updatedBy":null,"updatedDateTime":"2021-04-06T06:24:02.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初二2020级2班","schoolId":234,"alias":"","showType":"0","gradeId":184,"showName":null,"headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"}],"type":"1","isExitInd":"Y"}],"type":"0","isExitInd":"Y"}]
         * isExitInd : Y
         */

        private String name;
        private String type;
        private int nextLevelType;
        private String isExitInd;
        private List<ListBeanXX> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getNextLevelType() {
            return nextLevelType;
        }

        public void setNextLevelType(int nextLevelType) {
            this.nextLevelType = nextLevelType;
        }

        public String getIsExitInd() {
            return isExitInd;
        }

        public void setIsExitInd(String isExitInd) {
            this.isExitInd = isExitInd;
        }

        public List<ListBeanXX> getList() {
            return list;
        }

        public void setList(List<ListBeanXX> list) {
            this.list = list;
        }

        public static class ListBeanXX {
            /**
             * id : 155
             * delInd : 0
             * createdBy : 13267180000
             * createdDateTime : 2021-04-05T01:08:03.000+0000
             * updatedBy : 636
             * updatedDateTime : 2021-04-05T01:08:03.000+0000
             * versionStamp : 0
             * total : 0
             * size : 10
             * current : 1
             * name : 初中
             * alias : 初中
             * showType : 1
             * showName : 初中
             * schoolId : 234
             * list : [{"id":79,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初一2021级","alias":"七年级","showType":"1","showName":"七年级","level":0,"schoolId":234,"learningSectionId":155,"list":[{"id":1747,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:21.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:21.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级2班","schoolId":234,"alias":"","showType":"1","gradeId":79,"showName":"七年级2班","headmasterId":266,"secondHeadmasterId":249,"type":"2","headmaster":"老师1","secondHeadmaster":"老师110","classesIds":null,"isExitInd":"Y"},{"id":1748,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T02:35:35.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T02:36:01.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级3班","schoolId":234,"alias":"七年级三班","showType":"1","gradeId":79,"showName":"七年级三班","headmasterId":266,"secondHeadmasterId":273,"type":"2","headmaster":"老师1","secondHeadmaster":"fasfds","classesIds":null,"isExitInd":"Y"},{"id":1762,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:27.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:27.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级4班","schoolId":234,"alias":"","showType":"1","gradeId":79,"showName":"七年级4班","headmasterId":199,"secondHeadmasterId":null,"type":"2","headmaster":"老师002","secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2063,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级5班","schoolId":234,"alias":"","showType":"2","gradeId":79,"showName":"七年级5班","headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2064,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级6班","schoolId":234,"alias":"","showType":"2","gradeId":79,"showName":"七年级6班","headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"}],"type":"1","isExitInd":"Y"},{"id":184,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-06T06:24:02.000+0000","updatedBy":null,"updatedDateTime":"2021-04-06T06:24:02.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初二2020级","alias":"","showType":"1","showName":"初二2020级","level":2020,"schoolId":234,"learningSectionId":155,"list":[{"id":2089,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-06T06:24:02.000+0000","updatedBy":null,"updatedDateTime":"2021-04-06T06:24:02.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初二2020级1班","schoolId":234,"alias":"","showType":"0","gradeId":184,"showName":null,"headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2090,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-06T06:24:02.000+0000","updatedBy":null,"updatedDateTime":"2021-04-06T06:24:02.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"初二2020级2班","schoolId":234,"alias":"","showType":"0","gradeId":184,"showName":null,"headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"}],"type":"1","isExitInd":"Y"}]
             * type : 0
             * isExitInd : Y
             */

            private int id;
            private String delInd;
            private String createdBy;
            private String createdDateTime;
            private String updatedBy;
            private String updatedDateTime;
            private int versionStamp;
            private int total;
            private int size;
            private int current;
            private String name;
            private String alias;
            private String showType;
            private String showName;
            private int schoolId;
            private String type;
            private String isExitInd;
            private List<ListBeanX> list;
            private boolean checked;

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }
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

            public String getUpdatedBy() {
                return updatedBy;
            }

            public void setUpdatedBy(String updatedBy) {
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getShowType() {
                return showType;
            }

            public void setShowType(String showType) {
                this.showType = showType;
            }

            public String getShowName() {
                return showName;
            }

            public void setShowName(String showName) {
                this.showName = showName;
            }

            public int getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(int schoolId) {
                this.schoolId = schoolId;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getIsExitInd() {
                return isExitInd;
            }

            public void setIsExitInd(String isExitInd) {
                this.isExitInd = isExitInd;
            }

            public List<ListBeanX> getList() {
                return list;
            }

            public void setList(List<ListBeanX> list) {
                this.list = list;
            }

            public static class ListBeanX {
                /**
                 * id : 79
                 * delInd : 0
                 * createdBy : 636
                 * createdDateTime : 2021-04-05T01:08:16.000+0000
                 * updatedBy : 13267182222
                 * updatedDateTime : 2021-04-05T01:08:16.000+0000
                 * versionStamp : 0
                 * total : 0
                 * size : 10
                 * current : 1
                 * name : 初一2021级
                 * alias : 七年级
                 * showType : 1
                 * showName : 七年级
                 * level : 0
                 * schoolId : 234
                 * learningSectionId : 155
                 * list : [{"id":1747,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:21.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:21.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级2班","schoolId":234,"alias":"","showType":"1","gradeId":79,"showName":"七年级2班","headmasterId":266,"secondHeadmasterId":249,"type":"2","headmaster":"老师1","secondHeadmaster":"老师110","classesIds":null,"isExitInd":"Y"},{"id":1748,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T02:35:35.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T02:36:01.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级3班","schoolId":234,"alias":"七年级三班","showType":"1","gradeId":79,"showName":"七年级三班","headmasterId":266,"secondHeadmasterId":273,"type":"2","headmaster":"老师1","secondHeadmaster":"fasfds","classesIds":null,"isExitInd":"Y"},{"id":1762,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:27.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-04-05T01:08:27.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级4班","schoolId":234,"alias":"","showType":"1","gradeId":79,"showName":"七年级4班","headmasterId":199,"secondHeadmasterId":null,"type":"2","headmaster":"老师002","secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2063,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级5班","schoolId":234,"alias":"","showType":"2","gradeId":79,"showName":"七年级5班","headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"},{"id":2064,"delInd":"0","createdBy":"636","createdDateTime":"2021-04-05T01:08:16.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T01:08:16.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"七年级6班","schoolId":234,"alias":"","showType":"2","gradeId":79,"showName":"七年级6班","headmasterId":null,"secondHeadmasterId":null,"type":"2","headmaster":null,"secondHeadmaster":null,"classesIds":null,"isExitInd":"Y"}]
                 * type : 1
                 * isExitInd : Y
                 */

                private int id;
                private String delInd;
                private String createdBy;
                private String createdDateTime;
                private String updatedBy;
                private String updatedDateTime;
                private int versionStamp;
                private int total;
                private int size;
                private int current;
                private String name;
                private String alias;
                private String showType;
                private String showName;
                private int level;
                private int schoolId;
                private int learningSectionId;
                private String type;
                private String isExitInd;
                private List<ListBean> list;
                private boolean checked;

                public boolean isChecked() {
                    return checked;
                }

                public void setChecked(boolean checked) {
                    this.checked = checked;
                }
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

                public String getUpdatedBy() {
                    return updatedBy;
                }

                public void setUpdatedBy(String updatedBy) {
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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAlias() {
                    return alias;
                }

                public void setAlias(String alias) {
                    this.alias = alias;
                }

                public String getShowType() {
                    return showType;
                }

                public void setShowType(String showType) {
                    this.showType = showType;
                }

                public String getShowName() {
                    return showName;
                }

                public void setShowName(String showName) {
                    this.showName = showName;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
                }

                public int getSchoolId() {
                    return schoolId;
                }

                public void setSchoolId(int schoolId) {
                    this.schoolId = schoolId;
                }

                public int getLearningSectionId() {
                    return learningSectionId;
                }

                public void setLearningSectionId(int learningSectionId) {
                    this.learningSectionId = learningSectionId;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getIsExitInd() {
                    return isExitInd;
                }

                public void setIsExitInd(String isExitInd) {
                    this.isExitInd = isExitInd;
                }

                public List<ListBean> getList() {
                    return list;
                }

                public void setList(List<ListBean> list) {
                    this.list = list;
                }

                public static class ListBean {
                    /**
                     * id : 1747
                     * delInd : 0
                     * createdBy : 636
                     * createdDateTime : 2021-04-05T01:08:21.000+0000
                     * updatedBy : 13267182222
                     * updatedDateTime : 2021-04-05T01:08:21.000+0000
                     * versionStamp : 0
                     * total : 0
                     * size : 10
                     * current : 1
                     * name : 七年级2班
                     * schoolId : 234
                     * alias :
                     * showType : 1
                     * gradeId : 79
                     * showName : 七年级2班
                     * headmasterId : 266
                     * secondHeadmasterId : 249
                     * type : 2
                     * headmaster : 老师1
                     * secondHeadmaster : 老师110
                     * classesIds : null
                     * isExitInd : Y
                     */

                    private int id;
                    private String delInd;
                    private String createdBy;
                    private String createdDateTime;
                    private String updatedBy;
                    private String updatedDateTime;
                    private int versionStamp;
                    private int total;
                    private int size;
                    private int current;
                    private String name;
                    private int schoolId;
                    private String alias;
                    private String showType;
                    private int gradeId;
                    private String showName;
                    private int headmasterId;
                    private int secondHeadmasterId;
                    private String type;
                    private String headmaster;
                    private String secondHeadmaster;
                    private Object classesIds;
                    private String isExitInd;
                    private boolean checked;

                    public boolean isChecked() {
                        return checked;
                    }

                    public void setChecked(boolean checked) {
                        this.checked = checked;
                    }
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

                    public String getUpdatedBy() {
                        return updatedBy;
                    }

                    public void setUpdatedBy(String updatedBy) {
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

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getSchoolId() {
                        return schoolId;
                    }

                    public void setSchoolId(int schoolId) {
                        this.schoolId = schoolId;
                    }

                    public String getAlias() {
                        return alias;
                    }

                    public void setAlias(String alias) {
                        this.alias = alias;
                    }

                    public String getShowType() {
                        return showType;
                    }

                    public void setShowType(String showType) {
                        this.showType = showType;
                    }

                    public int getGradeId() {
                        return gradeId;
                    }

                    public void setGradeId(int gradeId) {
                        this.gradeId = gradeId;
                    }

                    public String getShowName() {
                        return showName;
                    }

                    public void setShowName(String showName) {
                        this.showName = showName;
                    }

                    public int getHeadmasterId() {
                        return headmasterId;
                    }

                    public void setHeadmasterId(int headmasterId) {
                        this.headmasterId = headmasterId;
                    }

                    public int getSecondHeadmasterId() {
                        return secondHeadmasterId;
                    }

                    public void setSecondHeadmasterId(int secondHeadmasterId) {
                        this.secondHeadmasterId = secondHeadmasterId;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getHeadmaster() {
                        return headmaster;
                    }

                    public void setHeadmaster(String headmaster) {
                        this.headmaster = headmaster;
                    }

                    public String getSecondHeadmaster() {
                        return secondHeadmaster;
                    }

                    public void setSecondHeadmaster(String secondHeadmaster) {
                        this.secondHeadmaster = secondHeadmaster;
                    }

                    public Object getClassesIds() {
                        return classesIds;
                    }

                    public void setClassesIds(Object classesIds) {
                        this.classesIds = classesIds;
                    }

                    public String getIsExitInd() {
                        return isExitInd;
                    }

                    public void setIsExitInd(String isExitInd) {
                        this.isExitInd = isExitInd;
                    }

                    @Override
                    public String toString() {
                        return "ListBean{" +
                                "id=" + id +
                                ", delInd='" + delInd + '\'' +
                                ", createdBy='" + createdBy + '\'' +
                                ", createdDateTime='" + createdDateTime + '\'' +
                                ", updatedBy='" + updatedBy + '\'' +
                                ", updatedDateTime='" + updatedDateTime + '\'' +
                                ", versionStamp=" + versionStamp +
                                ", total=" + total +
                                ", size=" + size +
                                ", current=" + current +
                                ", name='" + name + '\'' +
                                ", schoolId=" + schoolId +
                                ", alias='" + alias + '\'' +
                                ", showType='" + showType + '\'' +
                                ", gradeId=" + gradeId +
                                ", showName='" + showName + '\'' +
                                ", headmasterId=" + headmasterId +
                                ", secondHeadmasterId=" + secondHeadmasterId +
                                ", type='" + type + '\'' +
                                ", headmaster='" + headmaster + '\'' +
                                ", secondHeadmaster='" + secondHeadmaster + '\'' +
                                ", classesIds=" + classesIds +
                                ", isExitInd='" + isExitInd + '\'' +
                                ", checked=" + checked +
                                '}';
                    }
                }

                @Override
                public String toString() {
                    return "ListBeanX{" +
                            "id=" + id +
                            ", delInd='" + delInd + '\'' +
                            ", createdBy='" + createdBy + '\'' +
                            ", createdDateTime='" + createdDateTime + '\'' +
                            ", updatedBy='" + updatedBy + '\'' +
                            ", updatedDateTime='" + updatedDateTime + '\'' +
                            ", versionStamp=" + versionStamp +
                            ", total=" + total +
                            ", size=" + size +
                            ", current=" + current +
                            ", name='" + name + '\'' +
                            ", alias='" + alias + '\'' +
                            ", showType='" + showType + '\'' +
                            ", showName='" + showName + '\'' +
                            ", level=" + level +
                            ", schoolId=" + schoolId +
                            ", learningSectionId=" + learningSectionId +
                            ", type='" + type + '\'' +
                            ", isExitInd='" + isExitInd + '\'' +
                            ", list=" + list +
                            ", checked=" + checked +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "ListBeanXX{" +
                        "id=" + id +
                        ", delInd='" + delInd + '\'' +
                        ", createdBy='" + createdBy + '\'' +
                        ", createdDateTime='" + createdDateTime + '\'' +
                        ", updatedBy='" + updatedBy + '\'' +
                        ", updatedDateTime='" + updatedDateTime + '\'' +
                        ", versionStamp=" + versionStamp +
                        ", total=" + total +
                        ", size=" + size +
                        ", current=" + current +
                        ", name='" + name + '\'' +
                        ", alias='" + alias + '\'' +
                        ", showType='" + showType + '\'' +
                        ", showName='" + showName + '\'' +
                        ", schoolId=" + schoolId +
                        ", type='" + type + '\'' +
                        ", isExitInd='" + isExitInd + '\'' +
                        ", list=" + list +
                        ", checked=" + checked +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", nextLevelType=" + nextLevelType +
                    ", isExitInd='" + isExitInd + '\'' +
                    ", list=" + list +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "StudentScopeRsp{" +
                "code=" + code +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
