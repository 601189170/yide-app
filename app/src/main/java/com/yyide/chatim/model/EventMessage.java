package com.yyide.chatim.model;

/**
 * Created by Administrator on 2019/4/4.
 */

public class EventMessage {

    private String code;
    private String message;
    private int count;
    private String type;
    private boolean isBoolean;

    public EventMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public EventMessage(String code, String message, int count) {
        this.code = code;
        this.message = message;
        this.count = count;
    }

    public EventMessage(String code, String message, String type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        isBoolean = aBoolean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}