package com.yyide.chatim.model;

import java.util.List;

public class PreparesLessonRep {
    private int timetableSchedulSubId;

    private String beforeClass;

    private String afterClass;

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

    public void setLessonsSubEntityList(List<LessonsSubEntityList> lessonsSubEntityList) {
        this.lessonsSubEntityList = lessonsSubEntityList;
    }

    public List<LessonsSubEntityList> getLessonsSubEntityList() {
        return this.lessonsSubEntityList;
    }

    public static class LessonsSubEntityList {
        private String information;

        public void setInformation(String information) {
            this.information = information;
        }

        public String getInformation() {
            return this.information;
        }
    }
}
