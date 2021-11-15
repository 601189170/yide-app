package com.yyide.chatim.model.schedule;

import static com.yyide.chatim.model.schedule.Schedule.TYPE_EXPIRED_COMPLETED;
import static com.yyide.chatim.model.schedule.Schedule.TYPE_EXPIRED_NOT_COMPLETED;
import static com.yyide.chatim.model.schedule.Schedule.TYPE_LIST_VIEW_HEAD;
import static com.yyide.chatim.model.schedule.Schedule.TYPE_TIME_AXIS;
import static com.yyide.chatim.model.schedule.Schedule.TYPE_UNEXPIRED_COMPLETED;
import static com.yyide.chatim.model.schedule.Schedule.TYPE_UNEXPIRED_NOT_COMPLETED;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yyide.chatim.database.ScheduleDaoUtil;
import com.yyide.chatim.utils.DateUtils;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liu tao
 * @date 2021/9/24 10:08
 * @description 描述
 */
public class ScheduleData implements MultiItemEntity, Cloneable, Comparable<ScheduleData> {
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
    private Map<String, Object> rrule;
    private String remindType;
    private String remindTypeInfo;
    private String startTime;
    private String endTime;
    private String moreDayStartTime;
    private String moreDayEndTime;
    private int moreDay;
    private String iconImg;
    private String isAllDay;
    private List<LabelListRsp.DataBean> label;
    private List<ParticipantRsp.DataBean.ParticipantListBean> participant;
    private String siteName;
    private String updateType;
    private String updateDate;
    private String dayOfMonth;
    //参与人id
    private String promoter;
    private String promoterName;
    //日程时间轴
    private boolean timeAxis;
    //日期头
    private boolean monthHead;
    //是否是月的第一天
    private boolean firstDayOfMonth;

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

    public Map<String, Object> getRrule() {
        return rrule;
    }

    public void setRrule(Map<String, Object> rrule) {
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

    public List<ParticipantRsp.DataBean.ParticipantListBean> getParticipant() {
        return participant;
    }

    public void setParticipant(List<ParticipantRsp.DataBean.ParticipantListBean> participant) {
        this.participant = participant;
    }

    public boolean isTimeAxis() {
        return timeAxis;
    }

    public void setTimeAxis(boolean timeAxis) {
        this.timeAxis = timeAxis;
    }

    public boolean isFirstDayOfMonth() {
        return firstDayOfMonth;
    }

    public void setFirstDayOfMonth(boolean firstDayOfMonth) {
        this.firstDayOfMonth = firstDayOfMonth;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getPromoterName() {
        return promoterName;
    }

    public void setPromoterName(String promoterName) {
        this.promoterName = promoterName;
    }

    public boolean isMonthHead() {
        return monthHead;
    }

    public void setMonthHead(boolean monthHead) {
        this.monthHead = monthHead;
    }

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

    public String getMoreDayStartTime() {
        return moreDayStartTime;
    }

    public void setMoreDayStartTime(String moreDayStartTime) {
        this.moreDayStartTime = moreDayStartTime;
    }

    public String getMoreDayEndTime() {
        return moreDayEndTime;
    }

    public void setMoreDayEndTime(String moreDayEndTime) {
        this.moreDayEndTime = moreDayEndTime;
    }

    public int getMoreDay() {
        return moreDay;
    }

    public void setMoreDay(int moreDay) {
        this.moreDay = moreDay;
    }

    @Override
    public int getItemType() {
        if (monthHead){
            //月日期
            return TYPE_LIST_VIEW_HEAD;
        }

        if (timeAxis) {
            //时间轴
            return TYPE_TIME_AXIS;
        }

        if (Objects.equals(status, "1")) {
            if (DateUtils.dateExpired(endTime)) {
                return TYPE_EXPIRED_COMPLETED;
            }
            return TYPE_UNEXPIRED_COMPLETED;
        }
        if (!TextUtils.isEmpty(endTime) && DateUtils.dateExpired(endTime)) {
            return TYPE_EXPIRED_NOT_COMPLETED;
        }
        return TYPE_UNEXPIRED_NOT_COMPLETED;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(ScheduleData other) {
        final DateTime dateTime1 = ScheduleDaoUtil.INSTANCE.toDateTime(this.startTime);
        final DateTime dateTimeOther1 = ScheduleDaoUtil.INSTANCE.toDateTime(other.startTime);
        final DateTime dateTime2 = ScheduleDaoUtil.INSTANCE.toDateTime(this.moreDayStartTime);
        final DateTime dateTimeOther2 = ScheduleDaoUtil.INSTANCE.toDateTime(other.moreDayStartTime);
        if (dateTime1.compareTo(dateTime2) > 0) {
            return 1;
        }

        if (dateTime1.compareTo(dateTime2) <= 0) {
            return dateTimeOther1.compareTo(dateTimeOther2);
        }
        return dateTime1.compareTo(dateTime2);
    }
}
