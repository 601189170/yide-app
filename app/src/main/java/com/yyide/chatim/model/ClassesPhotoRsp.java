package com.yyide.chatim.model;

import java.util.List;

public class ClassesPhotoRsp {

    private String msg;
    private Integer code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private Integer id;
        private String delInd;
        private Object createdBy;
        private String createdDateTime;
        private Object updatedBy;
        private String updatedDateTime;
        private Integer versionStamp;
        private Integer total;
        private Integer size;
        private Integer current;
        private String name;
        private Object schoolId;
        private Object classesId;
        private String show;
        private Object kind;
        private String sort;
        private List<AlbumEntityBean> albumEntity;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDelInd() {
            return delInd;
        }

        public void setDelInd(String delInd) {
            this.delInd = delInd;
        }

        public Object getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Object createdBy) {
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

        public Integer getVersionStamp() {
            return versionStamp;
        }

        public void setVersionStamp(Integer versionStamp) {
            this.versionStamp = versionStamp;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
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

        public void setSchoolId(Object schoolId) {
            this.schoolId = schoolId;
        }

        public Object getClassesId() {
            return classesId;
        }

        public void setClassesId(Object classesId) {
            this.classesId = classesId;
        }

        public String getShow() {
            return show;
        }

        public void setShow(String show) {
            this.show = show;
        }

        public Object getKind() {
            return kind;
        }

        public void setKind(Object kind) {
            this.kind = kind;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public List<AlbumEntityBean> getAlbumEntity() {
            return albumEntity;
        }

        public void setAlbumEntity(List<AlbumEntityBean> albumEntity) {
            this.albumEntity = albumEntity;
        }

        public static class AlbumEntityBean {
            private Integer id;
            private String delInd;
            private Object createdBy;
            private String createdDateTime;
            private Object updatedBy;
            private String updatedDateTime;
            private Integer versionStamp;
            private Integer total;
            private Integer size;
            private Integer current;
            private Object classifyId;
            private Object schoolId;
            private Object classesId;
            private String url;
            private Object type;
            private Object kind;
            private Object show;
            private Object page;
            private Object list;
            private Object files;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getDelInd() {
                return delInd;
            }

            public void setDelInd(String delInd) {
                this.delInd = delInd;
            }

            public Object getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(Object createdBy) {
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

            public Integer getVersionStamp() {
                return versionStamp;
            }

            public void setVersionStamp(Integer versionStamp) {
                this.versionStamp = versionStamp;
            }

            public Integer getTotal() {
                return total;
            }

            public void setTotal(Integer total) {
                this.total = total;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getCurrent() {
                return current;
            }

            public void setCurrent(Integer current) {
                this.current = current;
            }

            public Object getClassifyId() {
                return classifyId;
            }

            public void setClassifyId(Object classifyId) {
                this.classifyId = classifyId;
            }

            public Object getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(Object schoolId) {
                this.schoolId = schoolId;
            }

            public Object getClassesId() {
                return classesId;
            }

            public void setClassesId(Object classesId) {
                this.classesId = classesId;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public Object getType() {
                return type;
            }

            public void setType(Object type) {
                this.type = type;
            }

            public Object getKind() {
                return kind;
            }

            public void setKind(Object kind) {
                this.kind = kind;
            }

            public Object getShow() {
                return show;
            }

            public void setShow(Object show) {
                this.show = show;
            }

            public Object getPage() {
                return page;
            }

            public void setPage(Object page) {
                this.page = page;
            }

            public Object getList() {
                return list;
            }

            public void setList(Object list) {
                this.list = list;
            }

            public Object getFiles() {
                return files;
            }

            public void setFiles(Object files) {
                this.files = files;
            }
        }
    }
}
