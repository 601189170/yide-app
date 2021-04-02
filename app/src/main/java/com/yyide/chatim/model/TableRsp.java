package com.yyide.chatim.model;

import java.io.Serializable;

/**
 * Created by Hao on 2018/3/5.
 */

public class TableRsp implements Serializable {

    public String id;

    public String subjectsName;

    public String teacherName;

    public int mark;

    public int flag;

    public int week;

    public String startTime;

    public String endTime;

    public int type;

    public String slot="";

    public int seletion;

    public boolean isselect=false;

    public String teacherPhoto;

    public String sex;

    public String lessonId;


}
