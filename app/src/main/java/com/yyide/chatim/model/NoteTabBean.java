package com.yyide.chatim.model;

/**
 * Created by 86159 on 2021/3/9.
 */

public class NoteTabBean {

    public String tag;

    public String name;

    public String islast;

    public String organization;

    public NoteTabBean(String name, String tag, String islast, String organization) {

        this.name = name;

        this.tag = tag;

        this.islast = islast;

        this.organization = organization;
    }

    public NoteTabBean() {

    }

}
