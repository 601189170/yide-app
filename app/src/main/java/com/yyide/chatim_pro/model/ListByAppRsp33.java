package com.yyide.chatim_pro.model;

import java.io.Serializable;
import java.util.List;

public class ListByAppRsp33 implements Serializable {


    private Integer code;
    private DataDTO data;
    private String message;

    public static class DataDTO {
        private List<ClassAddBookDTOListDTO> classAddBookDTOList;
        private List<DeptVOListDTO> deptVOList;
        private String schoolBadgeUrl;
        private String schoolName;



        public static class ClassAddBookDTOListDTO {
            private String id;
            private String name;
            private List<StudentListDTO> studentList;



            public static class StudentListDTO {
                private String address;
                private String avatar;
                private String className;
                private List<ElternAddBookDTOListDTO> elternAddBookDTOList;
                private String id;
                private String name;
                private String phone;


                public static class ElternAddBookDTOListDTO {
                    private String avatar;
                    private String id;
                    private String name;
                    private String phone;
                    private Integer relations;


                }
            }
        }

        public static class DeptVOListDTO {
            private String ancestors;
            private String bid;
            private List<ChildrenDTO> children;
            private List<EmployeeAddBookDTOListDTO> employeeAddBookDTOList;
            private String id;
            private String leader;
            private String name;
            private Integer pid;
            private Integer sort;
            private Integer status;



            public static class ChildrenDTO {
                private String ancestors;
                private String bid;
                private List<?> children;
                private List<EmployeeAddBookDTOListDTO> employeeAddBookDTOList;
                private String id;
                private String leader;
                private String name;
                private String pid;
                private Integer sort;
                private Integer status;



                public static class EmployeeAddBookDTOListDTO {
                    private String avatar;
                    private String concealPhone;
                    private String gender;
                    private String id;
                    private String name;
                    private String phone;
                    private String userId;
                    private String subjectName;


                }
            }

            public static class EmployeeAddBookDTOListDTO {
                private String avatar;
                private String concealPhone;
                private String email;
                private String id;
                private String name;
                private String phone;
                private String userId;
                private String subjectName;


            }
        }
    }
}
