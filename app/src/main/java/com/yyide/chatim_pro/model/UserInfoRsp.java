package com.yyide.chatim_pro.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/12 17:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/12 17:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UserInfoRsp {


    /**
     * code : 200
     * success : true
     * msg : 查询成功
     * data : [{"id":132,"delInd":"1","createdBy":"448","createdDateTime":"2021-03-08T07:58:06.000+0000","updatedBy":"448","updatedDateTime":"2021-03-09T03:07:13.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"邓超","phone":"18948789456","userName":"111111","email":"","sex":"","healthStatus":"","maritalStatus":"","jobNumber":"","national":"","hometown":"","cardType":"","birthday":null,"cardNo":"","politicalStatus":"","bloodType":"","highestEducation":"","jobTitle":"","timeInWork":null,"entryTime":null,"position":"","extensionNumber":"","icCardNo":"","faceInformation":"3","address":"","specialty":"","schoolId":212,"remarks":"","classesId":null,"classesName":null,"primaryGuardianPhone":null,"deputyGuardianPhone":null,"studentNo":null,"accountTypes":null,"birthPlace":null,"enrollmentDate":null,"onlyChild":null,"personalIllness":null,"accommodation":null},{"id":672,"delInd":"0","createdBy":"1693","createdDateTime":"2021-04-07T06:31:06.000+0000","updatedBy":null,"updatedDateTime":"2021-04-07T06:31:06.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"邓芳","phone":"13537770413","userName":"dengfang001","email":"","sex":"","healthStatus":"","maritalStatus":"","jobNumber":"","national":"","hometown":"","cardType":"","birthday":null,"cardNo":"","politicalStatus":"","bloodType":"","highestEducation":"","jobTitle":"","timeInWork":null,"entryTime":null,"position":"","extensionNumber":"","icCardNo":"","faceInformation":"3","address":"","specialty":"","schoolId":488,"remarks":"","classesId":null,"classesName":null,"primaryGuardianPhone":null,"deputyGuardianPhone":null,"studentNo":null,"accountTypes":null,"birthPlace":null,"enrollmentDate":null,"onlyChild":null,"personalIllness":null,"accommodation":null},{"id":673,"delInd":"0","createdBy":"1693","createdDateTime":"2021-04-07T06:32:05.000+0000","updatedBy":null,"updatedDateTime":"2021-04-07T06:32:05.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"邓芳","phone":"15602861986","userName":"ddengfang001","email":"","sex":"","healthStatus":"","maritalStatus":"","jobNumber":"","national":"","hometown":"","cardType":"","birthday":null,"cardNo":"","politicalStatus":"","bloodType":"","highestEducation":"","jobTitle":"","timeInWork":null,"entryTime":null,"position":"","extensionNumber":"","icCardNo":"","faceInformation":"3","address":"","specialty":"","schoolId":488,"remarks":"","classesId":null,"classesName":null,"primaryGuardianPhone":null,"deputyGuardianPhone":null,"studentNo":null,"accountTypes":null,"birthPlace":null,"enrollmentDate":null,"onlyChild":null,"personalIllness":null,"accommodation":null}]
     */

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

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
        /**
         * id : 132
         * delInd : 1
         * createdBy : 448
         * createdDateTime : 2021-03-08T07:58:06.000+0000
         * updatedBy : 448
         * updatedDateTime : 2021-03-09T03:07:13.000+0000
         * versionStamp : 0
         * total : 0
         * size : 10
         * current : 1
         * name : 邓超
         * phone : 18948789456
         * userName : 111111
         * email :
         * sex :
         * healthStatus :
         * maritalStatus :
         * jobNumber :
         * national :
         * hometown :
         * cardType :
         * birthday : null
         * cardNo :
         * politicalStatus :
         * bloodType :
         * highestEducation :
         * jobTitle :
         * timeInWork : null
         * entryTime : null
         * position :
         * extensionNumber :
         * icCardNo :
         * faceInformation : 3
         * address :
         * specialty :
         * schoolId : 212
         * remarks :
         * classesId : null
         * classesName : null
         * primaryGuardianPhone : null
         * deputyGuardianPhone : null
         * studentNo : null
         * accountTypes : null
         * birthPlace : null
         * enrollmentDate : null
         * onlyChild : null
         * personalIllness : null
         * accommodation : null
         */

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
        private String phone;
        private String userName;
        private String email;
        private String sex;
        private String healthStatus;
        private String maritalStatus;
        private String jobNumber;
        private String national;
        private String hometown;
        private String cardType;
        private Object birthday;
        private String cardNo;
        private String politicalStatus;
        private String bloodType;
        private String highestEducation;
        private String jobTitle;
        private Object timeInWork;
        private Object entryTime;
        private String position;
        private String extensionNumber;
        private String icCardNo;
        private String faceInformation;
        private String address;
        private String specialty;
        private long schoolId;
        private String remarks;
        private String classesId;
        private String classesName;
        private String primaryGuardianPhone;
        private String deputyGuardianPhone;
        private String departmentName;
        private String studentNo;
        private Object accountTypes;
        private Object birthPlace;
        private Object enrollmentDate;
        private Object onlyChild;
        private Object personalIllness;
        private Object accommodation;
        private String userType;//用户类型 1老师 2学生
        private List<TeacherlistRsp.DataBean.RecordsBean.SubjectsBean> subjects;


        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public List<TeacherlistRsp.DataBean.RecordsBean.SubjectsBean> getSubjects() {
            return subjects;
        }

        public void setSubjects(List<TeacherlistRsp.DataBean.RecordsBean.SubjectsBean> subjects) {
            this.subjects = subjects;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHealthStatus() {
            return healthStatus;
        }

        public void setHealthStatus(String healthStatus) {
            this.healthStatus = healthStatus;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getJobNumber() {
            return jobNumber;
        }

        public void setJobNumber(String jobNumber) {
            this.jobNumber = jobNumber;
        }

        public String getNational() {
            return national;
        }

        public void setNational(String national) {
            this.national = national;
        }

        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getPoliticalStatus() {
            return politicalStatus;
        }

        public void setPoliticalStatus(String politicalStatus) {
            this.politicalStatus = politicalStatus;
        }

        public String getBloodType() {
            return bloodType;
        }

        public void setBloodType(String bloodType) {
            this.bloodType = bloodType;
        }

        public String getHighestEducation() {
            return highestEducation;
        }

        public void setHighestEducation(String highestEducation) {
            this.highestEducation = highestEducation;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public Object getTimeInWork() {
            return timeInWork;
        }

        public void setTimeInWork(Object timeInWork) {
            this.timeInWork = timeInWork;
        }

        public Object getEntryTime() {
            return entryTime;
        }

        public void setEntryTime(Object entryTime) {
            this.entryTime = entryTime;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getExtensionNumber() {
            return extensionNumber;
        }

        public void setExtensionNumber(String extensionNumber) {
            this.extensionNumber = extensionNumber;
        }

        public String getIcCardNo() {
            return icCardNo;
        }

        public void setIcCardNo(String icCardNo) {
            this.icCardNo = icCardNo;
        }

        public String getFaceInformation() {
            return faceInformation;
        }

        public void setFaceInformation(String faceInformation) {
            this.faceInformation = faceInformation;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(long schoolId) {
            this.schoolId = schoolId;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public Object getClassesId() {
            return classesId;
        }

        public void setClassesId(String classesId) {
            this.classesId = classesId;
        }

        public String getClassesName() {
            return classesName;
        }

        public void setClassesName(String classesName) {
            this.classesName = classesName;
        }

        public String getPrimaryGuardianPhone() {
            return primaryGuardianPhone;
        }

        public void setPrimaryGuardianPhone(String primaryGuardianPhone) {
            this.primaryGuardianPhone = primaryGuardianPhone;
        }

        public String getDeputyGuardianPhone() {
            return deputyGuardianPhone;
        }

        public void setDeputyGuardianPhone(String deputyGuardianPhone) {
            this.deputyGuardianPhone = deputyGuardianPhone;
        }

        public Object getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public Object getAccountTypes() {
            return accountTypes;
        }

        public void setAccountTypes(Object accountTypes) {
            this.accountTypes = accountTypes;
        }

        public Object getBirthPlace() {
            return birthPlace;
        }

        public void setBirthPlace(Object birthPlace) {
            this.birthPlace = birthPlace;
        }

        public Object getEnrollmentDate() {
            return enrollmentDate;
        }

        public void setEnrollmentDate(Object enrollmentDate) {
            this.enrollmentDate = enrollmentDate;
        }

        public Object getOnlyChild() {
            return onlyChild;
        }

        public void setOnlyChild(Object onlyChild) {
            this.onlyChild = onlyChild;
        }

        public Object getPersonalIllness() {
            return personalIllness;
        }

        public void setPersonalIllness(Object personalIllness) {
            this.personalIllness = personalIllness;
        }

        public Object getAccommodation() {
            return accommodation;
        }

        public void setAccommodation(Object accommodation) {
            this.accommodation = accommodation;
        }

        @Override
        public String toString() {
            return "DataBean{" +
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
                    ", phone='" + phone + '\'' +
                    ", userName='" + userName + '\'' +
                    ", email='" + email + '\'' +
                    ", sex='" + sex + '\'' +
                    ", healthStatus='" + healthStatus + '\'' +
                    ", maritalStatus='" + maritalStatus + '\'' +
                    ", jobNumber='" + jobNumber + '\'' +
                    ", national='" + national + '\'' +
                    ", hometown='" + hometown + '\'' +
                    ", cardType='" + cardType + '\'' +
                    ", birthday=" + birthday +
                    ", cardNo='" + cardNo + '\'' +
                    ", politicalStatus='" + politicalStatus + '\'' +
                    ", bloodType='" + bloodType + '\'' +
                    ", highestEducation='" + highestEducation + '\'' +
                    ", jobTitle='" + jobTitle + '\'' +
                    ", timeInWork=" + timeInWork +
                    ", entryTime=" + entryTime +
                    ", position='" + position + '\'' +
                    ", extensionNumber='" + extensionNumber + '\'' +
                    ", icCardNo='" + icCardNo + '\'' +
                    ", faceInformation='" + faceInformation + '\'' +
                    ", address='" + address + '\'' +
                    ", specialty='" + specialty + '\'' +
                    ", schoolId=" + schoolId +
                    ", remarks='" + remarks + '\'' +
                    ", classesId=" + classesId +
                    ", classesName=" + classesName +
                    ", primaryGuardianPhone=" + primaryGuardianPhone +
                    ", deputyGuardianPhone=" + deputyGuardianPhone +
                    ", studentNo=" + studentNo +
                    ", accountTypes=" + accountTypes +
                    ", birthPlace=" + birthPlace +
                    ", enrollmentDate=" + enrollmentDate +
                    ", onlyChild=" + onlyChild +
                    ", personalIllness=" + personalIllness +
                    ", accommodation=" + accommodation +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfoRsp{" +
                "code=" + code +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
