package com.yyide.chatim_pro.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserSigRsp {

    private int code;
    private IMDataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public IMDataBean getData() {
        return data;
    }

    public void setData(IMDataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class IMDataBean implements Parcelable {
        private String contentType;
        private String identifier;
        private int random;
        private int sdkAppId;
        private String userSig;

        public IMDataBean(){

        }

        protected IMDataBean(Parcel in) {
            contentType = in.readString();
            identifier = in.readString();
            random = in.readInt();
            sdkAppId = in.readInt();
            userSig = in.readString();
        }

        public static final Creator<IMDataBean> CREATOR = new Creator<IMDataBean>() {
            @Override
            public IMDataBean createFromParcel(Parcel in) {
                return new IMDataBean(in);
            }

            @Override
            public IMDataBean[] newArray(int size) {
                return new IMDataBean[size];
            }
        };

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public int getRandom() {
            return random;
        }

        public void setRandom(int random) {
            this.random = random;
        }

        public int getSdkAppId() {
            return sdkAppId;
        }

        public void setSdkAppId(int sdkAppId) {
            this.sdkAppId = sdkAppId;
        }

        public String getUserSig() {
            return userSig;
        }

        public void setUserSig(String userSig) {
            this.userSig = userSig;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(contentType);
            dest.writeString(identifier);
            dest.writeInt(random);
            dest.writeInt(sdkAppId);
            dest.writeString(userSig);
        }
    }
}
