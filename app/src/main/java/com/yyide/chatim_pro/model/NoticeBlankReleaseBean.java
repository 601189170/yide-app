package com.yyide.chatim_pro.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class NoticeBlankReleaseBean implements Parcelable {

    public String messageTemplateId;
    public String title;
    public String content;
    public boolean isConfirm;
    public boolean isTimer;
    public String timerDate;
    public int notifyRange;
    public List<String> subIds;

    public List<RecordListBean> recordList;

    public NoticeBlankReleaseBean() {

    }

    protected NoticeBlankReleaseBean(Parcel in) {
        messageTemplateId = in.readString();
        title = in.readString();
        content = in.readString();
        isConfirm = in.readByte() != 0;
        isTimer = in.readByte() != 0;
        timerDate = in.readString();
        notifyRange = in.readInt();
        subIds = in.createStringArrayList();
        recordList = in.createTypedArrayList(RecordListBean.CREATOR);
    }

    public static final Creator<NoticeBlankReleaseBean> CREATOR = new Creator<NoticeBlankReleaseBean>() {
        @Override
        public NoticeBlankReleaseBean createFromParcel(Parcel in) {
            return new NoticeBlankReleaseBean(in);
        }

        @Override
        public NoticeBlankReleaseBean[] newArray(int size) {
            return new NoticeBlankReleaseBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageTemplateId);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeByte((byte) (isConfirm ? 1 : 0));
        dest.writeByte((byte) (isTimer ? 1 : 0));
        dest.writeString(timerDate);
        dest.writeInt(notifyRange);
        dest.writeStringList(subIds);
        dest.writeTypedList(recordList);
    }

    public static class RecordListBean implements Parcelable{
        public String specifieType;
        public int nums;
        public List<ListBean> list = new ArrayList<>();

        public RecordListBean() {

        }
        protected RecordListBean(Parcel in) {
            specifieType = in.readString();
            nums = in.readInt();
            list = in.createTypedArrayList(ListBean.CREATOR);
        }

        public static final Creator<RecordListBean> CREATOR = new Creator<RecordListBean>() {
            @Override
            public RecordListBean createFromParcel(Parcel in) {
                return new RecordListBean(in);
            }

            @Override
            public RecordListBean[] newArray(int size) {
                return new RecordListBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(specifieType);
            dest.writeInt(nums);
            dest.writeTypedList(list);
        }

        public static class ListBean implements Parcelable{
            public String type;
            public long specifieId;
            public long specifieParentId;
            public int nums;

            public ListBean() {
            }

            protected ListBean(Parcel in) {
                type = in.readString();
                specifieId = in.readLong();
                specifieParentId = in.readLong();
                nums = in.readInt();
            }

            public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
                @Override
                public ListBean createFromParcel(Parcel in) {
                    return new ListBean(in);
                }

                @Override
                public ListBean[] newArray(int size) {
                    return new ListBean[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(type);
                dest.writeLong(specifieId);
                dest.writeLong(specifieParentId);
                dest.writeInt(nums);
            }
        }
    }
}
