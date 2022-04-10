package com.yyide.chatim.model;

import java.util.List;

public class MyUserInfo {

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        private String avatar;
        private String email;
        private String gender;
        private String id;
        private String name;
        private String phone;
        private List<SchoolBean> school;
        private int status;
        private String username;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<SchoolBean> getSchool() {
            return school;
        }

        public void setSchool(List<SchoolBean> school) {
            this.school = school;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public static class SchoolBean {
            private String id;
            private boolean isInit;
            private String schoolLogo;
            private String schoolName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean getIsInit() {
                return isInit;
            }

            public void setIsInit(boolean isInit) {
                this.isInit = isInit;
            }

            public String getSchoolLogo() {
                return schoolLogo;
            }

            public void setSchoolLogo(String schoolLogo) {
                this.schoolLogo = schoolLogo;
            }

            public String getSchoolName() {
                return schoolName;
            }

            public void setSchoolName(String schoolName) {
                this.schoolName = schoolName;
            }
        }
    }
}
