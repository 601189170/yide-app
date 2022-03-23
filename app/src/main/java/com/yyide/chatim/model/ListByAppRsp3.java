package com.yyide.chatim.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ListByAppRsp3 {

    public Integer code;
    public DataDTO data;
    public String message;

   

    public static class DataDTO {
        public List<AdlistDTO> adlist;
        public String schoolBadgeUrl;
        public String schoolName;

       

        public static class AdlistDTO implements Parcelable {
            public List<ElternListDTO> elternList=new ArrayList<>();
            public String name;
            public List<StudentListDTO> studentList=new ArrayList<>();

            protected AdlistDTO(Parcel in) {
                name = in.readString();
            }

            public static final Creator<AdlistDTO> CREATOR = new Creator<AdlistDTO>() {
                @Override
                public AdlistDTO createFromParcel(Parcel in) {
                    return new AdlistDTO(in);
                }

                @Override
                public AdlistDTO[] newArray(int size) {
                    return new AdlistDTO[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(name);
            }


            public static class ElternListDTO {
                public String avatar;
                public String concealPhone;
                public String employeeSubjects;
                public String gender;
                public String id;
                public String name;
                public String phone;
                public String subjectName;
                public String userId;

           
            }

            public static class StudentListDTO {
                public String address;
                public String avatar;
                public String className;
                public List<Parent> elternAddBookDTOList;
                public Integer gender;
                public String id;
                public String name;
                public String phone;

               
                public static class ElternAddBookDTOListDTO {
                    public String phone;
                    
                }
            }
            public  AdlistDTO(){

            }
        }

    }

}
