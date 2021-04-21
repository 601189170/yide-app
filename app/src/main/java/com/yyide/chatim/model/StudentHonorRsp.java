package com.yyide.chatim.model;

import java.util.List;

public class StudentHonorRsp {

    private int code;
    private Boolean success;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
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
        private List<RecordsBean> records;
        private int total;
        private int size;
        private int current;
        private Boolean searchCount;
        private int pages;

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
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

        public Boolean getSearchCount() {
            return searchCount;
        }

        public void setSearchCount(Boolean searchCount) {
            this.searchCount = searchCount;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public static class RecordsBean {
            private int id;
            private int studentId;
            private String studentName;
            private String workName;
            private String workDesc;
            private Boolean isFine;
            private int schoolId;
            private int classesId;
            private int likeCount;
            private int commentCount;
            private List<String> worksUrl;
            private List<WorkCommentVosBean> workCommentVos;
            private Boolean isLike;
            private String createdDateTime;
            private String createdBy;
            private String userImg;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStudentId() {
                return studentId;
            }

            public void setStudentId(int studentId) {
                this.studentId = studentId;
            }

            public String getStudentName() {
                return studentName;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }

            public String getWorkName() {
                return workName;
            }

            public void setWorkName(String workName) {
                this.workName = workName;
            }

            public String getWorkDesc() {
                return workDesc;
            }

            public void setWorkDesc(String workDesc) {
                this.workDesc = workDesc;
            }

            public Boolean getIsFine() {
                return isFine;
            }

            public void setIsFine(Boolean isFine) {
                this.isFine = isFine;
            }

            public int getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(int schoolId) {
                this.schoolId = schoolId;
            }

            public int getClassesId() {
                return classesId;
            }

            public void setClassesId(int classesId) {
                this.classesId = classesId;
            }

            public int getLikeCount() {
                return likeCount;
            }

            public void setLikeCount(int likeCount) {
                this.likeCount = likeCount;
            }

            public int getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(int commentCount) {
                this.commentCount = commentCount;
            }

            public List<String> getWorksUrl() {
                return worksUrl;
            }

            public void setWorksUrl(List<String> worksUrl) {
                this.worksUrl = worksUrl;
            }

            public List<WorkCommentVosBean> getWorkCommentVos() {
                return workCommentVos;
            }

            public void setWorkCommentVos(List<WorkCommentVosBean> workCommentVos) {
                this.workCommentVos = workCommentVos;
            }

            public Boolean getIsLike() {
                return isLike;
            }

            public void setIsLike(Boolean isLike) {
                this.isLike = isLike;
            }

            public String getCreatedDateTime() {
                return createdDateTime;
            }

            public void setCreatedDateTime(String createdDateTime) {
                this.createdDateTime = createdDateTime;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getUserImg() {
                return userImg;
            }

            public void setUserImg(String userImg) {
                this.userImg = userImg;
            }

            public static class WorkCommentVosBean {
                private int id;
                private Object commentName;
                private String comment;
                private String createdDateTime;
                private String createdBy;
                private Object userImg;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public Object getCommentName() {
                    return commentName;
                }

                public void setCommentName(Object commentName) {
                    this.commentName = commentName;
                }

                public String getComment() {
                    return comment;
                }

                public void setComment(String comment) {
                    this.comment = comment;
                }

                public String getCreatedDateTime() {
                    return createdDateTime;
                }

                public void setCreatedDateTime(String createdDateTime) {
                    this.createdDateTime = createdDateTime;
                }

                public String getCreatedBy() {
                    return createdBy;
                }

                public void setCreatedBy(String createdBy) {
                    this.createdBy = createdBy;
                }

                public Object getUserImg() {
                    return userImg;
                }

                public void setUserImg(Object userImg) {
                    this.userImg = userImg;
                }
            }
        }
    }
}
