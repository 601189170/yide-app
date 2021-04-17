package com.yyide.chatim.model;

import java.util.List;

public class PreparesLessonRep {
    private int id;
    private int timetableSchedulSubId;

    private String beforeClass;

    private String afterClass;

    private int lessonsId;

    private List<String> teachToolList;

    private List<LessonsSubEntityList> lessonsSubEntityList;

    public void setTimetableSchedulSubId(int timetableSchedulSubId) {
        this.timetableSchedulSubId = timetableSchedulSubId;
    }

    public int getTimetableSchedulSubId() {
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

    public int getLessonsId() {
        return lessonsId;
    }

    public void setLessonsId(int lessonsId) {
        this.lessonsId = lessonsId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLessonsSubEntityList(List<LessonsSubEntityList> lessonsSubEntityList) {
        this.lessonsSubEntityList = lessonsSubEntityList;
    }

    public List<LessonsSubEntityList> getLessonsSubEntityList() {
        return this.lessonsSubEntityList;
    }

    public static class LessonsSubEntityList {
        private String information;
        private String classesId;
        private String lessonsDate;

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

        public String getLessonsDate() {
            return lessonsDate;
        }

        public void setLessonsDate(String lessonsDate) {
            this.lessonsDate = lessonsDate;
        }
    }
}
