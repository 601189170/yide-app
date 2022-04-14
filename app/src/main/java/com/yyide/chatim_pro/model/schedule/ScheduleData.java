package com.yyide.chatim_pro.model.schedule;

import static com.yyide.chatim_pro.model.schedule.Schedule.TYPE_EXPIRED_COMPLETED;
import static com.yyide.chatim_pro.model.schedule.Schedule.TYPE_EXPIRED_NOT_COMPLETED;
import static com.yyide.chatim_pro.model.schedule.Schedule.TYPE_LIST_VIEW_HEAD;
import static com.yyide.chatim_pro.model.schedule.Schedule.TYPE_TIME_AXIS;
import static com.yyide.chatim_pro.model.schedule.Schedule.TYPE_UNEXPIRED_COMPLETED;
import static com.yyide.chatim_pro.model.schedule.Schedule.TYPE_UNEXPIRED_NOT_COMPLETED;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yyide.chatim_pro.database.ScheduleDaoUtil;
import com.yyide.chatim_pro.utils.DateUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liu tao
 * @date 2021/9/24 10:08
 * @description 描述
 */
public class ScheduleData implements MultiItemEntity, Cloneable, Comparable<ScheduleData>, Parcelable {
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
    private String historyShow;
    private String timeShow;
    private String startTime;
    private String endTime;
    private String moreDayStartTime;
    private String moreDayEndTime;
    private int moreDay;
    private int moreDayIndex;
    private int moreDayCount;
    private String iconImg;
    private String isAllDay;
    public List<LabelListRsp.DataBean> labelList=new ArrayList<>();
    public List<ParticipantRsp.DataBean.ParticipantListBean> participantList=new ArrayList<>();
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

    //日程创建时间
    private String createdDateTime;

    public ScheduleData(){

    }

    protected ScheduleData(Parcel in) {
        id = in.readString();
        name = in.readString();
        siteId = in.readString();
        type = in.readString();
        scheduleStatus = in.readString();
        isTop = in.readString();
        remark = in.readString();
        filePath = in.readString();
        isRepeat = in.readString();
        status = in.readString();
        remindType = in.readString();
        remindTypeInfo = in.readString();
        historyShow = in.readString();
        timeShow = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        moreDayStartTime = in.readString();
        moreDayEndTime = in.readString();
        moreDay = in.readInt();
        moreDayIndex = in.readInt();
        moreDayCount = in.readInt();
        iconImg = in.readString();
        isAllDay = in.readString();
        siteName = in.readString();
        updateType = in.readString();
        updateDate = in.readString();
        dayOfMonth = in.readString();
        promoter = in.readString();
        promoterName = in.readString();
        timeAxis = in.readByte() != 0;
        monthHead = in.readByte() != 0;
        firstDayOfMonth = in.readByte() != 0;
        createdDateTime = in.readString();
    }

    public static final Creator<ScheduleData> CREATOR = new Creator<ScheduleData>() {
        @Override
        public ScheduleData createFromParcel(Parcel in) {
            return new ScheduleData(in);
        }

        @Override
        public ScheduleData[] newArray(int size) {
            return new ScheduleData[size];
        }
    };

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

    public List<LabelListRsp.DataBean> getLabelList() {
        return labelList;
    }

    public void setLabel(List<LabelListRsp.DataBean> labelList) {
        this.labelList = labelList;
    }

    public List<ParticipantRsp.DataBean.ParticipantListBean> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<ParticipantRsp.DataBean.ParticipantListBean> participantList) {
        this.participantList = participantList;
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

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getMoreDayIndex() {
        return moreDayIndex;
    }

    public void setMoreDayIndex(int moreDayIndex) {
        this.moreDayIndex = moreDayIndex;
    }

    public int getMoreDayCount() {
        return moreDayCount;
    }

    public void setMoreDayCount(int moreDayCount) {
        this.moreDayCount = moreDayCount;
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
            if (TextUtils.isEmpty(moreDayEndTime)){
                moreDayStartTime = startTime;
                moreDayEndTime = endTime;
            }
            if (DateUtils.dateExpired(moreDayEndTime)) {
                return TYPE_EXPIRED_COMPLETED;
            }
            return TYPE_UNEXPIRED_COMPLETED;
        }
        if (!TextUtils.isEmpty(moreDayEndTime) && DateUtils.dateExpired(moreDayEndTime)) {
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
        final DateTime dateTime1 = ScheduleDaoUtil.INSTANCE.toDateTime(this.moreDayStartTime);
        final DateTime dateTimeOther1 = ScheduleDaoUtil.INSTANCE.toDateTime(other.moreDayStartTime);
        final int compareTo = dateTime1.compareTo(dateTimeOther1);
        if (compareTo == 0) {
            final DateTime dateTime2 = ScheduleDaoUtil.INSTANCE.toDateTime(this.moreDayEndTime);
            final DateTime dateTimeOther2 = ScheduleDaoUtil.INSTANCE.toDateTime(other.moreDayEndTime);
            return dateTime2.compareTo(dateTimeOther2);
        }
        return compareTo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(siteId);
        dest.writeString(type);
        dest.writeString(scheduleStatus);
        dest.writeString(isTop);
        dest.writeString(remark);
        dest.writeString(filePath);
        dest.writeString(isRepeat);
        dest.writeString(status);
        dest.writeString(remindType);
        dest.writeString(remindTypeInfo);
        dest.writeString(historyShow);
        dest.writeString(timeShow);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(moreDayStartTime);
        dest.writeString(moreDayEndTime);
        dest.writeInt(moreDay);
        dest.writeInt(moreDayIndex);
        dest.writeInt(moreDayCount);
        dest.writeString(iconImg);
        dest.writeString(isAllDay);
        dest.writeString(siteName);
        dest.writeString(updateType);
        dest.writeString(updateDate);
        dest.writeString(dayOfMonth);
        dest.writeString(promoter);
        dest.writeString(promoterName);
        dest.writeByte((byte) (timeAxis ? 1 : 0));
        dest.writeByte((byte) (monthHead ? 1 : 0));
        dest.writeByte((byte) (firstDayOfMonth ? 1 : 0));
        dest.writeString(createdDateTime);
    }


    @Override
    public String toString() {
        return "ScheduleData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", siteId='" + siteId + '\'' +
                ", type='" + type + '\'' +
                ", scheduleStatus='" + scheduleStatus + '\'' +
                ", isTop='" + isTop + '\'' +
                ", remark='" + remark + '\'' +
                ", filePath='" + filePath + '\'' +
                ", isRepeat='" + isRepeat + '\'' +
                ", status='" + status + '\'' +
                ", rrule=" + rrule +
                ", remindType='" + remindType + '\'' +
                ", remindTypeInfo='" + remindTypeInfo + '\'' +
                ", historyShow='" + historyShow + '\'' +
                ", timeShow='" + timeShow + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", moreDayStartTime='" + moreDayStartTime + '\'' +
                ", moreDayEndTime='" + moreDayEndTime + '\'' +
                ", moreDay=" + moreDay +
                ", moreDayIndex=" + moreDayIndex +
                ", moreDayCount=" + moreDayCount +
                ", iconImg='" + iconImg + '\'' +
                ", isAllDay='" + isAllDay + '\'' +
                ", labelList=" + labelList +
                ", participantList=" + participantList +
                ", siteName='" + siteName + '\'' +
                ", updateType='" + updateType + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", dayOfMonth='" + dayOfMonth + '\'' +
                ", promoter='" + promoter + '\'' +
                ", promoterName='" + promoterName + '\'' +
                ", timeAxis=" + timeAxis +
                ", monthHead=" + monthHead +
                ", firstDayOfMonth=" + firstDayOfMonth +
                ", createdDateTime='" + createdDateTime + '\'' +
                '}';
    }
}
