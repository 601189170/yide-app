package com.yyide.chatim_pro.model;

import java.util.List;

public class PreparesLessonRep {
    private long id;
    private long timetableSchedulSubId;

    private String beforeClass;

    private String afterClass;

    private long lessonsId;

    private String lessonsDate;

    private List<String> teachToolList;

    private List<LessonsSubEntityList> lessonsSubEntityList;

    public void setTimetableSchedulSubId(long timetableSchedulSubId) {
        this.timetableSchedulSubId = timetableSchedulSubId;
    }

    public long getTimetableSchedulSubId() {
        return this.timetableSchedulSubId;
    }

    public void setBeforeClass(String beforeClass) {
        this.beforeClass = beforeClass;
    }

    public String getBeforeClass() {
        return this.beforeClass;
    }

    public void setAfterClass(String afterClass) {
        this.afterClass = afterClass;
    }

    public String getAfterClass() {
        return this.afterClass;
    }

    public void setTeachToolList(List<String> teachToolList) {
        this.teachToolList = teachToolList;
    }

    public List<String> getTeachToolList() {
        return this.teachToolList;
    }

    public long getLessonsId() {
        return lessonsId;
    }

    public void setLessonsId(long lessonsId) {
        this.lessonsId = lessonsId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLessonsSubEntityList(List<LessonsSubEntityList> lessonsSubEntityList) {
        this.lessonsSubEntityList = lessonsSubEntityList;
    }

    public List<LessonsSubEntityList> getLessonsSubEntityList() {
        return this.lessonsSubEntityList;
    }

    public String getLessonsDate() {
        return lessonsDate;
    }

    public void setLessonsDate(String lessonsDate) {
        this.lessonsDate = lessonsDate;
    }

    public static class LessonsSubEntityList {
        private String information;
        private String classesId;


        public void setInformation(String information) {
            this.information = information;
        }

        public String getInformation() {
            return this.information;
        }

        public String getClassesId() {
            return classesId;
        }

        public void setClassesId(String classesId) {
            this.classesId = classesId;
        }

    }
}
