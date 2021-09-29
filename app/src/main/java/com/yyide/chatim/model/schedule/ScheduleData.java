package com.yyide.chatim.model.schedule;

import java.util.List;

/**
 * @author liu tao
 * @date 2021/9/24 10:08
 * @description 描述
 */
public class ScheduleData {
    private String id;
    private String name;
    private String siteId;
    private String type;
    private String scheduleStatus;
    private String isTop;
    private String remark;
    private String filePath;
    private String isRepeat;
    private String status;
    private String rrule;
    private String remindType;
    private String remindTypeInfo;
    private String startTime;
    private String endTime;
    private String iconImg;
    private String isAllDay;
    private List<LabelListRsp.DataBean> label;
    private List<ParticipantBean> participant;

    public String getIsRepeat() {
        return isRepeat;
    }

    public String getIsAllDay() {
        return isAllDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setIsRepeat(String isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule;
    }

    public String getRemindType() {
        return remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    public String getRemindTypeInfo() {
        return remindTypeInfo;
    }

    public void setRemindTypeInfo(String remindTypeInfo) {
        this.remindTypeInfo = remindTypeInfo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIconImg() {
        return iconImg;
    }

    public void setIconImg(String iconImg) {
        this.iconImg = iconImg;
    }

    public void setIsAllDay(String isAllDay) {
        this.isAllDay = isAllDay;
    }

    public List<LabelListRsp.DataBean> getLabel() {
        return label;
    }

    public void setLabel(List<LabelListRsp.DataBean> label) {
        this.label = label;
    }

    public List<ParticipantBean> getParticipant() {
        return participant;
    }

    public void setParticipant(List<ParticipantBean> participant) {
        this.participant = participant;
    }

    public static class ParticipantBean {
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public ParticipantBean() {
        }

        public ParticipantBean(String userId) {
            this.userId = userId;
        }
    }
}
