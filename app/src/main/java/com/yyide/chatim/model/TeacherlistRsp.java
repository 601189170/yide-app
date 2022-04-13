package com.yyide.chatim.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TeacherlistRsp implements Serializable {


    /**
     * code : 200
     * success : true
     * msg : 查询成功
     * data : {"records":[{"id":585,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"丁老师","phone":"13511111111","userName":"fgsfgwfw","email":"","sex":"","healthStatus":"","maritalStatus":"","jobNumber":"","national":"","hometown":"","cardType":"","birthday":null,"cardNo":"","politicalStatus":"","bloodType":"","highestEducation":"","jobTitle":"","timeInWork":null,"entryTime":null,"position":"","extensionNumber":"","icCardNo":"","faceInformation":"3","address":"","specialty":"","schoolId":524,"userId":1983,"remarks":"","departmentName":null,"departmentId":1194,"departments":[{"id":878,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"departmentId":1194,"departmentName":"安卓","teacherId":585,"schoolId":524}],"subjects":[{"id":1471,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3398,"teacherId":585,"schoolId":524},{"id":1472,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3399,"teacherId":585,"schoolId":524},{"id":1473,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3400,"teacherId":585,"schoolId":524},{"id":1474,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3401,"teacherId":585,"schoolId":524},{"id":1475,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3402,"teacherId":585,"schoolId":524}],"roles":[]},{"id":586,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:01.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"刘老师","phone":"13522222222","userName":"fwfwfw","email":"","sex":"","healthStatus":"","maritalStatus":"","jobNumber":"","national":"","hometown":"","cardType":"","birthday":null,"cardNo":"","politicalStatus":"","bloodType":"","highestEducation":"","jobTitle":"","timeInWork":null,"entryTime":null,"position":"","extensionNumber":"","icCardNo":"","faceInformation":"3","address":"","specialty":"","schoolId":524,"userId":1984,"remarks":"","departmentName":null,"departmentId":1194,"departments":[{"id":879,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"departmentId":1194,"departmentName":"安卓","teacherId":586,"schoolId":524}],"subjects":[{"id":1476,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:00.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3403,"teacherId":586,"schoolId":524},{"id":1477,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:00.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3404,"teacherId":586,"schoolId":524},{"id":1478,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3405,"teacherId":586,"schoolId":524},{"id":1479,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3406,"teacherId":586,"schoolId":524},{"id":1480,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3407,"teacherId":586,"schoolId":524}],"roles":[]},{"id":587,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"刘涛","phone":"13533333333","userName":"fgergwrgwg","email":"","sex":"","healthStatus":"","maritalStatus":"","jobNumber":"","national":"","hometown":"","cardType":"","birthday":null,"cardNo":"","politicalStatus":"","bloodType":"","highestEducation":"","jobTitle":"","timeInWork":null,"entryTime":null,"position":"","extensionNumber":"","icCardNo":"","faceInformation":"3","address":"","specialty":"","schoolId":524,"userId":1985,"remarks":"","departmentName":null,"departmentId":1194,"departments":[{"id":880,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"departmentId":1194,"departmentName":"安卓","teacherId":587,"schoolId":524}],"subjects":[{"id":1481,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3406,"teacherId":587,"schoolId":524},{"id":1482,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3407,"teacherId":587,"schoolId":524},{"id":1483,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3408,"teacherId":587,"schoolId":524},{"id":1484,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3409,"teacherId":587,"schoolId":524},{"id":1485,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3405,"teacherId":587,"schoolId":524}],"roles":[]}],"total":3,"size":10,"current":1,"searchCount":true,"pages":1}
     */

    public int code;
    public boolean success;
    public String msg;
    public DataBean data;


    public static class DataBean {
        /**
         * records : [{"id":585,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"丁老师","phone":"13511111111","userName":"fgsfgwfw","email":"","sex":"","healthStatus":"","maritalStatus":"","jobNumber":"","national":"","hometown":"","cardType":"","birthday":null,"cardNo":"","politicalStatus":"","bloodType":"","highestEducation":"","jobTitle":"","timeInWork":null,"entryTime":null,"position":"","extensionNumber":"","icCardNo":"","faceInformation":"3","address":"","specialty":"","schoolId":524,"userId":1983,"remarks":"","departmentName":null,"departmentId":1194,"departments":[{"id":878,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"departmentId":1194,"departmentName":"安卓","teacherId":585,"schoolId":524}],"subjects":[{"id":1471,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3398,"teacherId":585,"schoolId":524},{"id":1472,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3399,"teacherId":585,"schoolId":524},{"id":1473,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3400,"teacherId":585,"schoolId":524},{"id":1474,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3401,"teacherId":585,"schoolId":524},{"id":1475,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3402,"teacherId":585,"schoolId":524}],"roles":[]},{"id":586,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:01.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"刘老师","phone":"13522222222","userName":"fwfwfw","email":"","sex":"","healthStatus":"","maritalStatus":"","jobNumber":"","national":"","hometown":"","cardType":"","birthday":null,"cardNo":"","politicalStatus":"","bloodType":"","highestEducation":"","jobTitle":"","timeInWork":null,"entryTime":null,"position":"","extensionNumber":"","icCardNo":"","faceInformation":"3","address":"","specialty":"","schoolId":524,"userId":1984,"remarks":"","departmentName":null,"departmentId":1194,"departments":[{"id":879,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"departmentId":1194,"departmentName":"安卓","teacherId":586,"schoolId":524}],"subjects":[{"id":1476,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:00.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3403,"teacherId":586,"schoolId":524},{"id":1477,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:00.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3404,"teacherId":586,"schoolId":524},{"id":1478,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3405,"teacherId":586,"schoolId":524},{"id":1479,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3406,"teacherId":586,"schoolId":524},{"id":1480,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:01.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:00.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3407,"teacherId":586,"schoolId":524}],"roles":[]},{"id":587,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"刘涛","phone":"13533333333","userName":"fgergwrgwg","email":"","sex":"","healthStatus":"","maritalStatus":"","jobNumber":"","national":"","hometown":"","cardType":"","birthday":null,"cardNo":"","politicalStatus":"","bloodType":"","highestEducation":"","jobTitle":"","timeInWork":null,"entryTime":null,"position":"","extensionNumber":"","icCardNo":"","faceInformation":"3","address":"","specialty":"","schoolId":524,"userId":1985,"remarks":"","departmentName":null,"departmentId":1194,"departments":[{"id":880,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"departmentId":1194,"departmentName":"安卓","teacherId":587,"schoolId":524}],"subjects":[{"id":1481,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3406,"teacherId":587,"schoolId":524},{"id":1482,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3407,"teacherId":587,"schoolId":524},{"id":1483,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3408,"teacherId":587,"schoolId":524},{"id":1484,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3409,"teacherId":587,"schoolId":524},{"id":1485,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:26:19.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:26:19.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3405,"teacherId":587,"schoolId":524}],"roles":[]}]
         * total : 3
         * size : 10
         * current : 1
         * searchCount : true
         * pages : 1
         */

        public int total;
        public int size;
        public int current;
        public boolean searchCount;
        public int pages;
        public List<RecordsBean> records;


        public static class RecordsBean  implements MultiItemEntity,Serializable{
            /**
             * id : 585
             * delInd : 0
             * createdBy : 1982
             * createdDateTime : 2021-04-02T03:25:31.000+0000
             * updatedBy : null
             * updatedDateTime : 2021-04-02T03:25:31.000+0000
             * versionStamp : 0
             * total : 0
             * size : 10
             * current : 1
             * name : 丁老师
             * phone : 13511111111
             * userName : fgsfgwfw
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
             * schoolId : 524
             * userId : 1983
             * remarks :
             * departmentName : null
             * departmentId : 1194
             * departments : [{"id":878,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"departmentId":1194,"departmentName":"安卓","teacherId":585,"schoolId":524}]
             * subjects : [{"id":1471,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3398,"teacherId":585,"schoolId":524},{"id":1472,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3399,"teacherId":585,"schoolId":524},{"id":1473,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3400,"teacherId":585,"schoolId":524},{"id":1474,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3401,"teacherId":585,"schoolId":524},{"id":1475,"delInd":"0","createdBy":"1982","createdDateTime":"2021-04-02T03:25:31.000+0000","updatedBy":null,"updatedDateTime":"2021-04-02T03:25:31.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectId":3402,"teacherId":585,"schoolId":524}]
             * roles : []
             */

            public String id;
            public String delInd;
            public String createdBy;
            public String createdDateTime;
            public Object updatedBy;
            public String updatedDateTime;
            public int versionStamp;
            public int total;
            public int size;
            public int current;
            public String name;
            public String phone;
            public String userName;
            public String email;
            public String avatar;
            public String sex; //1 男  0 女
            public String className;//班级名称
            public String employeeSubjects;//班级名称
            public String primaryGuardianPhone;//主监护人
            public String deputyGuardianPhone;//副监护人
            public String healthStatus;
            public String maritalStatus;
            public String jobNumber;
            public String national;
            public String hometown;
            public String cardType;
            public Object birthday;
            public String cardNo;
            public String politicalStatus;
            public String bloodType;
            public String highestEducation;
            public String jobTitle;
            public Object timeInWork;
            public Object entryTime;
            public String position;
            public String extensionNumber;
            public String icCardNo;
            public String faceInformation;
            public String address;
            public String specialty;
            public long schoolId;
            public String userId;
            public String remarks;
            public String concealPhone;
            public Object departmentName;
            public long departmentId;
            public List<DepartmentsBean> departments;
            public List<SubjectsBean> subjects;
            public String subjectName;
            public String adress;
            public List<?> roles;
            public List<Parent> elternAddBookDTOList=new ArrayList<>();
            public String userType;//用户类型 1老师 2学生
            //组织架构列表
            public ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO organizationItem;
            public ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO organizationItem2;

            public List<ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO.StudentListDTO> StudentData_by_Teacher=new ArrayList<>();

            public List<ListByAppRsp3.DataDTO.AdlistDTO.ElternListDTO>  TeacherData_by_Parent=new ArrayList();
            public List<ListByAppRsp3.DataDTO.AdlistDTO.StudentListDTO> StudentData_by_Parent=new ArrayList<>();

            public int itemType; //0 成员 1 组织

            @Override
            public int getItemType() {
                return itemType;
            }



            public static class DepartmentsBean {
                /**
                 * id : 878
                 * delInd : 0
                 * createdBy : 1982
                 * createdDateTime : 2021-04-02T03:25:31.000+0000
                 * updatedBy : null
                 * updatedDateTime : 2021-04-02T03:25:31.000+0000
                 * versionStamp : 0
                 * total : 0
                 * size : 10
                 * current : 1
                 * departmentId : 1194
                 * departmentName : 安卓
                 * teacherId : 585
                 * schoolId : 524
                 */

                public long id;
                public String delInd;
                public String createdBy;
                public String createdDateTime;
                public Object updatedBy;
                public String updatedDateTime;
                public int versionStamp;
                public int total;
                public int size;
                public int current;
                public long departmentId;
                public String departmentName;
                public long teacherId;
                public long schoolId;


            }

            public static class SubjectsBean {
                /**
                 * id : 1471
                 * delInd : 0
                 * createdBy : 1982
                 * createdDateTime : 2021-04-02T03:25:31.000+0000
                 * updatedBy : null
                 * updatedDateTime : 2021-04-02T03:25:31.000+0000
                 * versionStamp : 0
                 * total : 0
                 * size : 10
                 * current : 1
                 * subjectId : 3398
                 * teacherId : 585
                 * schoolId : 524
                 */

                public long id;
                public String delInd;
                public String createdBy;
                public String createdDateTime;
                public Object updatedBy;
                public String updatedDateTime;
                public int versionStamp;
                public int total;
                public int size;
                public int current;
                public long subjectId;
                public String subjectName;
                public long teacherId;
                public long schoolId;


            }
        }
    }
}
