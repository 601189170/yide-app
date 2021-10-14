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

    private int code;
    private boolean success;
    private String msg;
    private List<ListBeanXX> data;

    public StudentScopeRsp(int code, boolean success, String msg, List<ListBeanXX> data) {
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public StudentScopeRsp() {
    }

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
        private long id;
        private String delInd;
        private String createdBy;
        private String createdDateTime;
        private Object updatedBy;
        private String updatedDateTime;
        private int versionStamp;
        private int total;
        private int size;
        private int current;
        private String name;
        private Object alias;
        private Object showType;
        private String showName;
        private long schoolId;
        private List<ListBeanX> list;
        private String type;
        private String isExitInd;

        public ListBeanXX(long id, String delInd, String createdBy, String createdDateTime, Object updatedBy, String updatedDateTime, int versionStamp, int total, int size, int current, String name, Object alias, Object showType, String showName, long schoolId, List<ListBeanX> list, String type, String isExitInd) {
            this.id = id;
            this.delInd = delInd;
            this.createdBy = createdBy;
            this.createdDateTime = createdDateTime;
            this.updatedBy = updatedBy;
            this.updatedDateTime = updatedDateTime;
            this.versionStamp = versionStamp;
            this.total = total;
            this.size = size;
            this.current = current;
            this.name = name;
            this.alias = alias;
            this.showType = showType;
            this.showName = showName;
            this.schoolId = schoolId;
            this.list = list;
            this.type = type;
            this.isExitInd = isExitInd;
        }

        public ListBeanXX() {
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getAlias() {
            return alias;
        }

        public void setAlias(Object alias) {
            this.alias = alias;
        }

        public Object getShowType() {
            return showType;
        }

        public void setShowType(Object showType) {
            this.showType = showType;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

        public long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(long schoolId) {
            this.schoolId = schoolId;
        }

        public List<ListBeanX> getList() {
            return list;
        }

        public void setList(List<ListBeanX> list) {
            this.list = list;
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

        public static class ListBeanX {
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
            private String name;
            private String alias;
            private String showType;
            private String showName;
            private int level;
            private long schoolId;
            private long learningSectionId;
            private List<ListBean> list;
            private String type;
            private String isExitInd;

            public ListBeanX(long id, String delInd, String createdBy, String createdDateTime, String updatedBy, String updatedDateTime, int versionStamp, int total, int size, int current, String name, String alias, String showType, String showName, int level, long schoolId, long learningSectionId, List<ListBean> list, String type, String isExitInd) {
                this.id = id;
                this.delInd = delInd;
                this.createdBy = createdBy;
                this.createdDateTime = createdDateTime;
                this.updatedBy = updatedBy;
                this.updatedDateTime = updatedDateTime;
                this.versionStamp = versionStamp;
                this.total = total;
                this.size = size;
                this.current = current;
                this.name = name;
                this.alias = alias;
                this.showType = showType;
                this.showName = showName;
                this.level = level;
                this.schoolId = schoolId;
                this.learningSectionId = learningSectionId;
                this.list = list;
                this.type = type;
                this.isExitInd = isExitInd;
            }

            public ListBeanX() {
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
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

            public long getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(long schoolId) {
                this.schoolId = schoolId;
            }

            public long getLearningSectionId() {
                return learningSectionId;
            }

            public void setLearningSectionId(long learningSectionId) {
                this.learningSectionId = learningSectionId;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
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

            public static class ListBean {
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
                private String name;
                private long schoolId;
                private String alias;
                private String showType;
                private long gradeId;
                private String showName;
                private long headmasterId;
                private Object secondHeadmasterId;
                private String type;
                private String headmaster;
                private Object secondHeadmaster;
                private Object classesIds;
                private String isExitInd;

                public ListBean(long id, String delInd, String createdBy, String createdDateTime, String updatedBy, String updatedDateTime, int versionStamp, int total, int size, int current, String name, long schoolId, String alias, String showType, long gradeId, String showName, long headmasterId, Object secondHeadmasterId, String type, String headmaster, Object secondHeadmaster, Object classesIds, String isExitInd) {
                    this.id = id;
                    this.delInd = delInd;
                    this.createdBy = createdBy;
                    this.createdDateTime = createdDateTime;
                    this.updatedBy = updatedBy;
                    this.updatedDateTime = updatedDateTime;
                    this.versionStamp = versionStamp;
                    this.total = total;
                    this.size = size;
                    this.current = current;
                    this.name = name;
                    this.schoolId = schoolId;
                    this.alias = alias;
                    this.showType = showType;
                    this.gradeId = gradeId;
                    this.showName = showName;
                    this.headmasterId = headmasterId;
                    this.secondHeadmasterId = secondHeadmasterId;
                    this.type = type;
                    this.headmaster = headmaster;
                    this.secondHeadmaster = secondHeadmaster;
                    this.classesIds = classesIds;
                    this.isExitInd = isExitInd;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
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

                public long getSchoolId() {
                    return schoolId;
                }

                public void setSchoolId(long schoolId) {
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

                public long getGradeId() {
                    return gradeId;
                }

                public void setGradeId(long gradeId) {
                    this.gradeId = gradeId;
                }

                public String getShowName() {
                    return showName;
                }

                public void setShowName(String showName) {
                    this.showName = showName;
                }

                public long getHeadmasterId() {
                    return headmasterId;
                }

                public void setHeadmasterId(long headmasterId) {
                    this.headmasterId = headmasterId;
                }

                public Object getSecondHeadmasterId() {
                    return secondHeadmasterId;
                }

                public void setSecondHeadmasterId(Object secondHeadmasterId) {
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

                public Object getSecondHeadmaster() {
                    return secondHeadmaster;
                }

                public void setSecondHeadmaster(Object secondHeadmaster) {
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

                public ListBean() {
                }
            }
        }
    }
}
