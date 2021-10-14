package com.yyide.chatim.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/10 17:28
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/10 17:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class DepartmentScopeRsp {

    /**
     * code : 200
     * success : true
     * msg : data
     * data : [{"id":430,"delInd":"0","createdBy":"636","createdDateTime":"2021-03-22T08:21:49.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-03-22T08:21:49.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":234,"name":"深圳宝安西乡中学","parentId":427,"type":"1","headId":272,"headName":"范德萨","sort":0,"level":0,"list":[{"id":585,"delInd":"0","createdBy":"636","createdDateTime":"2021-03-18T12:53:30.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-03-18T12:53:30.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":234,"name":"问问3地方","parentId":430,"type":"0","headId":0,"headName":null,"sort":0,"level":0,"list":[{"id":596,"delInd":"0","createdBy":"636","createdDateTime":"2021-03-18T12:53:15.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-03-18T12:53:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":234,"name":"发第三方13","parentId":585,"type":"0","headId":0,"headName":null,"sort":0,"level":0,"list":[{"id":597,"delInd":"0","createdBy":"636","createdDateTime":"2021-03-29T13:42:01.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-03-29T13:42:01.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":234,"name":"发发送到热1","parentId":596,"type":"0","headId":0,"headName":null,"sort":0,"level":0,"list":[]}]}]}]}]
     */

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    public DepartmentScopeRsp(int code, boolean success, String msg, List<DataBean> data) {
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public DepartmentScopeRsp() {
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

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
        private long schoolId;
        private String name;
        private long parentId;
        private String type;
        private long headId;
        private String headName;
        private int sort;
        private int level;
        private List<ListBeanXX> list;
        private boolean checked;

        public DataBean(long id, String delInd, String createdBy, String createdDateTime, String updatedBy, String updatedDateTime, int versionStamp, int total, int size, int current, long schoolId, String name, long parentId, String type, long headId, String headName, int sort, int level, List<ListBeanXX> list, boolean checked) {
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
            this.schoolId = schoolId;
            this.name = name;
            this.parentId = parentId;
            this.type = type;
            this.headId = headId;
            this.headName = headName;
            this.sort = sort;
            this.level = level;
            this.list = list;
            this.checked = checked;
        }

        public DataBean() {
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

        public long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(long schoolId) {
            this.schoolId = schoolId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getParentId() {
            return parentId;
        }

        public void setParentId(long parentId) {
            this.parentId = parentId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getHeadId() {
            return headId;
        }

        public void setHeadId(long headId) {
            this.headId = headId;
        }

        public String getHeadName() {
            return headName;
        }

        public void setHeadName(String headName) {
            this.headName = headName;
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

        public List<ListBeanXX> getList() {
            return list;
        }

        public void setList(List<ListBeanXX> list) {
            this.list = list;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public static class ListBeanXX {
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
            private long schoolId;
            private String name;
            private long parentId;
            private String type;
            private long headId;
            private Object headName;
            private int sort;
            private int level;
            private List<ListBeanX> list;
            private boolean checked;

            public ListBeanXX(long id, String delInd, String createdBy, String createdDateTime, String updatedBy, String updatedDateTime, int versionStamp, int total, int size, int current, long schoolId, String name, long parentId, String type, long headId, Object headName, int sort, int level, List<ListBeanX> list, boolean checked) {
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
                this.schoolId = schoolId;
                this.name = name;
                this.parentId = parentId;
                this.type = type;
                this.headId = headId;
                this.headName = headName;
                this.sort = sort;
                this.level = level;
                this.list = list;
                this.checked = checked;
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

            public long getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(long schoolId) {
                this.schoolId = schoolId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getParentId() {
                return parentId;
            }

            public void setParentId(long parentId) {
                this.parentId = parentId;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public long getHeadId() {
                return headId;
            }

            public void setHeadId(long headId) {
                this.headId = headId;
            }

            public Object getHeadName() {
                return headName;
            }

            public void setHeadName(Object headName) {
                this.headName = headName;
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

            public List<ListBeanX> getList() {
                return list;
            }

            public void setList(List<ListBeanX> list) {
                this.list = list;
            }

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
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
                private long schoolId;
                private String name;
                private long parentId;
                private String type;
                private long headId;
                private Object headName;
                private int sort;
                private int level;
                private List<ListBean> list;
                private boolean checked;

                public ListBeanX(long id, String delInd, String createdBy, String createdDateTime, String updatedBy, String updatedDateTime, int versionStamp, int total, int size, int current, long schoolId, String name, long parentId, String type, long headId, Object headName, int sort, int level, List<ListBean> list, boolean checked) {
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
                    this.schoolId = schoolId;
                    this.name = name;
                    this.parentId = parentId;
                    this.type = type;
                    this.headId = headId;
                    this.headName = headName;
                    this.sort = sort;
                    this.level = level;
                    this.list = list;
                    this.checked = checked;
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

                public long getSchoolId() {
                    return schoolId;
                }

                public void setSchoolId(long schoolId) {
                    this.schoolId = schoolId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public long getParentId() {
                    return parentId;
                }

                public void setParentId(long parentId) {
                    this.parentId = parentId;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public long getHeadId() {
                    return headId;
                }

                public void setHeadId(long headId) {
                    this.headId = headId;
                }

                public Object getHeadName() {
                    return headName;
                }

                public void setHeadName(Object headName) {
                    this.headName = headName;
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

                public List<ListBean> getList() {
                    return list;
                }

                public void setList(List<ListBean> list) {
                    this.list = list;
                }

                public boolean isChecked() {
                    return checked;
                }

                public void setChecked(boolean checked) {
                    this.checked = checked;
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
                    private long schoolId;
                    private String name;
                    private long parentId;
                    private String type;
                    private long headId;
                    private Object headName;
                    private int sort;
                    private int level;
                    private List<?> list;
                    private boolean checked;

                    public ListBean(long id, String delInd, String createdBy, String createdDateTime, String updatedBy, String updatedDateTime, int versionStamp, int total, int size, int current, long schoolId, String name, long parentId, String type, long headId, Object headName, int sort, int level, List<?> list, boolean checked) {
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
                        this.schoolId = schoolId;
                        this.name = name;
                        this.parentId = parentId;
                        this.type = type;
                        this.headId = headId;
                        this.headName = headName;
                        this.sort = sort;
                        this.level = level;
                        this.list = list;
                        this.checked = checked;
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

                    public long getSchoolId() {
                        return schoolId;
                    }

                    public void setSchoolId(long schoolId) {
                        this.schoolId = schoolId;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public long getParentId() {
                        return parentId;
                    }

                    public void setParentId(long parentId) {
                        this.parentId = parentId;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public long getHeadId() {
                        return headId;
                    }

                    public void setHeadId(long headId) {
                        this.headId = headId;
                    }

                    public Object getHeadName() {
                        return headName;
                    }

                    public void setHeadName(Object headName) {
                        this.headName = headName;
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

                    public List<?> getList() {
                        return list;
                    }

                    public void setList(List<?> list) {
                        this.list = list;
                    }

                    public boolean isChecked() {
                        return checked;
                    }

                    public void setChecked(boolean checked) {
                        this.checked = checked;
                    }

                    public ListBean() {
                    }
                }
            }
        }
    }
}
