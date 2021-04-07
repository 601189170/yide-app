package com.yyide.chatim.model;

/**
 * Created by 86159 on 2021/3/9.
 */

public class NoteTabBean {

    public String tag;

    public String name;

    public boolean islast;
    public NoteTabBean(String name,String tag,boolean islast){

        this.name=name;

        this.tag=tag;

        this.islast=islast;
    }
    public NoteTabBean(){

    }

}
