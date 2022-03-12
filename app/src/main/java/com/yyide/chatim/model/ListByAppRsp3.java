package com.yyide.chatim.model;

import java.util.List;

public class ListByAppRsp3 {
    public Integer code;
    public DataDTO data;
    public String message;

  

    public static class DataDTO {
        public List<AdlistDTO> adlist;
        public String schoolBadgeUrl;
        public String schoolName;

       

        public static class AdlistDTO {
            public List<ElternListDTO> elternList;
            public String name;
            public List<StudentListDTO> studentList;

           

            public static class ElternListDTO {
                public String concealPhone;
                public String employeeSubjects;
                public String gender;
                public String id;
                public String name;
                public String phone;
                public String subjectName;
                public String userId;

               
            }

            public static class StudentListDTO {
                public String address;
                public List<ElternAddBookDTOListDTO> elternAddBookDTOList;
                public Integer gender;
                public String id;
                public String name;
                public String phone;

               

                public static class ElternAddBookDTOListDTO {
                    public String phone;

                   
                }
            }
        }
    }
}
