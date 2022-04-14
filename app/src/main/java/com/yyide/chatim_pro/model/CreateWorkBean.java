package com.yyide.chatim_pro.model;

import java.util.List;

public class CreateWorkBean {

    public String title;
    public String content;
    public String imgPaths;
    public String feedbackEndTime;
    public String releaseTime;
    public String subjectId;
    public List<ClassesListDTO> classesList;


    public static class ClassesListDTO {
        public String classesId;
        public String timetableId;
        public String timetableTime;
        
    }
}
