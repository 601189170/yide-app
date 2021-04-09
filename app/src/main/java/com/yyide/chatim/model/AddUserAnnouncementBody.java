package com.yyide.chatim.model;

import java.util.Date;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/8 19:38
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/8 19:38
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AddUserAnnouncementBody {

    /**
     * type : 3
     * title : 这是标题
     * content : 内容1 内容2
     * equipmentType : 1
     * sendTarget : 1
     * departmentIds : ["430"]
     * classesIds : []
     * classCardIds : []
     * isTiming : false
     * timingTime :
     */

    private String type;
    private String title;
    private String content;
    private String equipmentType;
    private String sendTarget;
    private boolean isTiming;
    private Date timingTime;
    private List<String> departmentIds;
    private List<String> classesIds;
    private List<String> classCardIds;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getSendTarget() {
        return sendTarget;
    }

    public void setSendTarget(String sendTarget) {
        this.sendTarget = sendTarget;
    }

    public boolean isIsTiming() {
        return isTiming;
    }

    public void setIsTiming(boolean isTiming) {
        this.isTiming = isTiming;
    }

    public Date getTimingTime() {
        return timingTime;
    }

    public void setTimingTime(Date timingTime) {
        this.timingTime = timingTime;
    }

    public List<String> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<String> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<?> getClassesIds() {
        return classesIds;
    }

    public void setClassesIds(List<String> classesIds) {
        this.classesIds = classesIds;
    }

    public List<?> getClassCardIds() {
        return classCardIds;
    }

    public void setClassCardIds(List<String> classCardIds) {
        this.classCardIds = classCardIds;
    }

    @Override
    public String toString() {
        return "AddUserAnnouncementBody{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", equipmentType='" + equipmentType + '\'' +
                ", sendTarget='" + sendTarget + '\'' +
                ", isTiming=" + isTiming +
                ", timingTime='" + timingTime + '\'' +
                ", departmentIds=" + departmentIds +
                ", classesIds=" + classesIds +
                ", classCardIds=" + classCardIds +
                '}';
    }
}
