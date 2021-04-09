package com.yyide.chatim.model;

import java.util.List;

/**
 * Created by 86159 on 2021/4/1.
 */

public class AppItemBean {
    /**
     * Copyright 2021 json.cn
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
         * records : [{"id":13,"delInd":"0","name":"应用组17","schoolId":null,"type":null,"list":[{"id":6,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-07T13:14:23.248+0000","updatedBy":null,"updatedDateTime":"2021-04-07T13:14:23.248+0000","versionStamp":0,"total":0,"size":10,"current":1,"applicationId":null,"name":"应用13","groupId":null,"schoolId":null,"schoolApplicationGroupId":null,"sort":null,"type":null,"img":"http://cloud-yide.oss-cn-shenzhen.aliyuncs.com/application/428f9fbdcc2a4d659297f7690fb24ef6-1615542537(1).jpg","path":"str","owner":null,"num":null,"userId":null,"isAdd":null}],"applicationType":null,"owner":null}]
         * total : 2
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
        private List<DataBean.RecordsBean> records;

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

        public List<DataBean.RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<DataBean.RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean {
            /**
             * id : 13
             * delInd : 0
             * name : 应用组17
             * schoolId : null
             * type : null
             * list : [{"id":6,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-07T13:14:23.248+0000","updatedBy":null,"updatedDateTime":"2021-04-07T13:14:23.248+0000","versionStamp":0,"total":0,"size":10,"current":1,"applicationId":null,"name":"应用13","groupId":null,"schoolId":null,"schoolApplicationGroupId":null,"sort":null,"type":null,"img":"http://cloud-yide.oss-cn-shenzhen.aliyuncs.com/application/428f9fbdcc2a4d659297f7690fb24ef6-1615542537(1).jpg","path":"str","owner":null,"num":null,"userId":null,"isAdd":null}]
             * applicationType : null
             * owner : null
             */

            private int id;
            private String delInd;
            private String name;
            private String schoolId;
            private String type;
            private String applicationType;
            private String owner;
            private List<DataBean.RecordsBean.ListBean> list;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(String schoolId) {
                this.schoolId = schoolId;
            }

            public Object getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getApplicationType() {
                return applicationType;
            }

            public void setApplicationType(String applicationType) {
                this.applicationType = applicationType;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public List<DataBean.RecordsBean.ListBean> getList() {
                return list;
            }

            public void setList(List<DataBean.RecordsBean.ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * id : 6
                 * delInd : 0
                 * createdBy : null
                 * createdDateTime : 2021-04-07T13:14:23.248+0000
                 * updatedBy : null
                 * updatedDateTime : 2021-04-07T13:14:23.248+0000
                 * versionStamp : 0
                 * total : 0
                 * size : 10
                 * current : 1
                 * applicationId : null
                 * name : 应用13
                 * groupId : null
                 * schoolId : null
                 * schoolApplicationGroupId : null
                 * sort : null
                 * type : null
                 * img : http://cloud-yide.oss-cn-shenzhen.aliyuncs.com/application/428f9fbdcc2a4d659297f7690fb24ef6-1615542537(1).jpg
                 * path : str
                 * owner : null
                 * num : null
                 * userId : null
                 * isAdd : null
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
                private String applicationId;
                private String name;
                private String groupId;
                private String schoolId;
                private String schoolApplicationGroupId;
                private String sort;
                private String type;
                private String img;
                private String path;
                private String owner;
                private String num;
                private String userId;
                private String isAdd;

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

                public String getApplicationId() {
                    return applicationId;
                }

                public void setApplicationId(String applicationId) {
                    this.applicationId = applicationId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getGroupId() {
                    return groupId;
                }

                public void setGroupId(String groupId) {
                    this.groupId = groupId;
                }

                public String getSchoolId() {
                    return schoolId;
                }

                public void setSchoolId(String schoolId) {
                    this.schoolId = schoolId;
                }

                public String getSchoolApplicationGroupId() {
                    return schoolApplicationGroupId;
                }

                public void setSchoolApplicationGroupId(String schoolApplicationGroupId) {
                    this.schoolApplicationGroupId = schoolApplicationGroupId;
                }

                public String getSort() {
                    return sort;
                }

                public void setSort(String sort) {
                    this.sort = sort;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getPath() {
                    return path;
                }

                public void setPath(String path) {
                    this.path = path;
                }

                public String getOwner() {
                    return owner;
                }

                public void setOwner(String owner) {
                    this.owner = owner;
                }

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getIsAdd() {
                    return isAdd;
                }

                public void setIsAdd(String isAdd) {
                    this.isAdd = isAdd;
                }
            }
        }
    }
}
