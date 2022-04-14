package com.yyide.chatim_pro.model;

import java.util.List;

public class BookSearchRsp2 {
    public Integer code;
    public DataDTO data;
    public String message;

   

    public static class DataDTO {
        public List<ElternListDTO> elternList;
        public List<?> studentList;

     
        public static class ElternListDTO {
            public String avatar;
            public String concealPhone;
            public String email;
            public String employeeSubjects;
            public String id;
            public String name;
            public String phone;
            public String userId;

           
        }
    }
}
