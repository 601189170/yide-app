package com.yyide.chatim.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/19 17:00
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/19 17:00
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UniversityScopeRsp {


    private int code;
    private boolean success;
    private String msg;
    private List<ListBeanXX> data;

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

    public List<ListBeanXX> getData() {
        return data;
    }

    public void setData(List<ListBeanXX> data) {
        this.data = data;
    }

    public static class ListBeanXX {
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
        private int schoolId;
        private String name;
        private String showName;
        private Object parentId;
        private String parentName;
        private String type;
        private int sort;
        private int level;
        private Object headmasterId;
        private Object secondHeadmasterId;
        private int nextLevelType;
        private List<ListBeanX> list;
        private String isExitInd;
        private Object headmaster;
        private Object secondHeadmaster;

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

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

        public Object getParentId() {
            return parentId;
        }

        public void setParentId(Object parentId) {
            this.parentId = parentId;
        }

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Object getHeadmasterId() {
            return headmasterId;
        }

        public void setHeadmasterId(Object headmasterId) {
            this.headmasterId = headmasterId;
        }

        public Object getSecondHeadmasterId() {
            return secondHeadmasterId;
        }

        public void setSecondHeadmasterId(Object secondHeadmasterId) {
            this.secondHeadmasterId = secondHeadmasterId;
        }

        public int getNextLevelType() {
            return nextLevelType;
        }

        public void setNextLevelType(int nextLevelType) {
            this.nextLevelType = nextLevelType;
        }

        public List<ListBeanX> getList() {
            return list;
        }

        public void setList(List<ListBeanX> list) {
            this.list = list;
        }

        public String getIsExitInd() {
            return isExitInd;
        }

        public void setIsExitInd(String isExitInd) {
            this.isExitInd = isExitInd;
        }

        public Object getHeadmaster() {
            return headmaster;
        }

        public void setHeadmaster(Object headmaster) {
            this.headmaster = headmaster;
        }

        public Object getSecondHeadmaster() {
            return secondHeadmaster;
        }

        public void setSecondHeadmaster(Object secondHeadmaster) {
            this.secondHeadmaster = secondHeadmaster;
        }

        public static class ListBeanX {
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
            private int schoolId;
            private String name;
            private String showName;
            private int parentId;
            private String parentName;
            private String type;
            private int sort;
            private int level;
            private Object headmasterId;
            private Object secondHeadmasterId;
            private int nextLevelType;
            private List<ListBean> list;
            private String isExitInd;
            private Object headmaster;
            private Object secondHeadmaster;

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

            public int getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(int schoolId) {
                this.schoolId = schoolId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getShowName() {
                return showName;
            }

            public void setShowName(String showName) {
                this.showName = showName;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public String getParentName() {
                return parentName;
            }

            public void setParentName(String parentName) {
                this.parentName = parentName;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public Object getHeadmasterId() {
                return headmasterId;
            }

            public void setHeadmasterId(Object headmasterId) {
                this.headmasterId = headmasterId;
            }

            public Object getSecondHeadmasterId() {
                return secondHeadmasterId;
            }

            public void setSecondHeadmasterId(Object secondHeadmasterId) {
                this.secondHeadmasterId = secondHeadmasterId;
            }

            public int getNextLevelType() {
                return nextLevelType;
            }

            public void setNextLevelType(int nextLevelType) {
                this.nextLevelType = nextLevelType;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public String getIsExitInd() {
                return isExitInd;
            }

            public void setIsExitInd(String isExitInd) {
                this.isExitInd = isExitInd;
            }

            public Object getHeadmaster() {
                return headmaster;
            }

            public void setHeadmaster(Object headmaster) {
                this.headmaster = headmaster;
            }

            public Object getSecondHeadmaster() {
                return secondHeadmaster;
            }

            public void setSecondHeadmaster(Object secondHeadmaster) {
                this.secondHeadmaster = secondHeadmaster;
            }

            public static class ListBean {
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
                private int schoolId;
                private String name;
                private String showName;
                private int parentId;
                private String parentName;
                private String type;
                private int sort;
                private int level;
                private Object headmasterId;
                private Object secondHeadmasterId;
                private int nextLevelType;
                private List<?> list;
                private String isExitInd;
                private Object headmaster;
                private Object secondHeadmaster;

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

                public int getSchoolId() {
                    return schoolId;
                }

                public void setSchoolId(int schoolId) {
                    this.schoolId = schoolId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getShowName() {
                    return showName;
                }

                public void setShowName(String showName) {
                    this.showName = showName;
                }

                public int getParentId() {
                    return parentId;
                }

                public void setParentId(int parentId) {
                    this.parentId = parentId;
                }

                public String getParentName() {
                    return parentName;
                }

                public void setParentName(String parentName) {
                    this.parentName = parentName;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
                }

                public Object getHeadmasterId() {
                    return headmasterId;
                }

                public void setHeadmasterId(Object headmasterId) {
                    this.headmasterId = headmasterId;
                }

                public Object getSecondHeadmasterId() {
                    return secondHeadmasterId;
                }

                public void setSecondHeadmasterId(Object secondHeadmasterId) {
                    this.secondHeadmasterId = secondHeadmasterId;
                }

                public int getNextLevelType() {
                    return nextLevelType;
                }

                public void setNextLevelType(int nextLevelType) {
                    this.nextLevelType = nextLevelType;
                }

                public List<?> getList() {
                    return list;
                }

                public void setList(List<?> list) {
                    this.list = list;
                }

                public String getIsExitInd() {
                    return isExitInd;
                }

                public void setIsExitInd(String isExitInd) {
                    this.isExitInd = isExitInd;
                }

                public Object getHeadmaster() {
                    return headmaster;
                }

                public void setHeadmaster(Object headmaster) {
                    this.headmaster = headmaster;
                }

                public Object getSecondHeadmaster() {
                    return secondHeadmaster;
                }

                public void setSecondHeadmaster(Object secondHeadmaster) {
                    this.secondHeadmaster = secondHeadmaster;
                }
            }
        }
    }
}
