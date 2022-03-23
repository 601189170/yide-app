package com.yyide.chatim.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListByAppRsp2 implements Serializable {


    public Integer code;
    public DataDTO data;
    public String message;

   

    public static class DataDTO {
        public List<ClassAddBookDTOListDTO> classAddBookDTOList;
        public List<DeptVOListDTO> deptVOList;
        public String schoolBadgeUrl;
        public String schoolName;

        public static class ClassAddBookDTOListDTO implements Parcelable{
            public String id;
            public String name;
            public boolean isperson;
            public List<ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO.StudentListDTO> studentList;

            protected ClassAddBookDTOListDTO(Parcel in) {
                id = in.readString();
                name = in.readString();
                isperson = in.readByte() != 0;
            }

            public static final Creator<ClassAddBookDTOListDTO> CREATOR = new Creator<ClassAddBookDTOListDTO>() {
                @Override
                public ClassAddBookDTOListDTO createFromParcel(Parcel in) {
                    return new ClassAddBookDTOListDTO(in);
                }

                @Override
                public ClassAddBookDTOListDTO[] newArray(int size) {
                    return new ClassAddBookDTOListDTO[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(id);
                dest.writeString(name);
                dest.writeByte((byte) (isperson ? 1 : 0));
            }


            public static class StudentListDTO {
                public String address;
                public String avatar;
                public String className;
                public List<ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO.StudentListDTO.ElternAddBookDTOListDTO> elternAddBookDTOList;
                public String id;
                public String name;
                public String phone;


                public static class ElternAddBookDTOListDTO {
                    public String avatar;
                    public String id;
                    public String name;
                    public String phone;
                    public Integer relations;


                }
            }
            public  ClassAddBookDTOListDTO(){

            }
        }

        public static class DeptVOListDTO {
            public String ancestors;
            public String bid;
            public List<ChildrenDTO> children=new ArrayList<>();
            public List<EmployeeAddBookDTOListDTO> employeeAddBookDTOList=new ArrayList<>();
            public String id;
            public String leader;
            public String name;
            public Integer pid;
            public Integer sort;
            public Integer status;



            public static class ChildrenDTO implements Parcelable, MultiItemEntity {
                public String ancestors;
                public String bid;
                public List<ChildrenDTO> children=new ArrayList<>();
                public ArrayList<DeptVOListDTO.EmployeeAddBookDTOListDTO> employeeAddBookDTOList=new ArrayList<>();
                public String id;
                public String leader;
                public String name;
                public String pid;
                public Integer sort;
                public Integer status;
                public boolean isperson;
                public String concealPhone;
                public String email;
                public String avatar;
                public String employeeSubjects;
                public String faceInformation;
                public String fa;
                public int itemType; //0 成员 1 组织
                public String phone;
                public String userId;
                public String subjectName;
                public String gender;
                protected ChildrenDTO(Parcel in) {
                    ancestors = in.readString();
                    bid = in.readString();
                    id = in.readString();
                    leader = in.readString();
                    name = in.readString();
                    pid = in.readString();
                    if (in.readByte() == 0) {
                        sort = null;
                    } else {
                        sort = in.readInt();
                    }
                    if (in.readByte() == 0) {
                        status = null;
                    } else {
                        status = in.readInt();
                    }
                }

                public static final Creator<ChildrenDTO> CREATOR = new Creator<ChildrenDTO>() {
                    @Override
                    public ChildrenDTO createFromParcel(Parcel in) {
                        return new ChildrenDTO(in);
                    }

                    @Override
                    public ChildrenDTO[] newArray(int size) {
                        return new ChildrenDTO[size];
                    }
                };

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(ancestors);
                    dest.writeString(bid);
                    dest.writeString(id);
                    dest.writeString(leader);
                    dest.writeString(name);
                    dest.writeString(pid);
                    if (sort == null) {
                        dest.writeByte((byte) 0);
                    } else {
                        dest.writeByte((byte) 1);
                        dest.writeInt(sort);
                    }
                    if (status == null) {
                        dest.writeByte((byte) 0);
                    } else {
                        dest.writeByte((byte) 1);
                        dest.writeInt(status);
                    }
                }

            public ChildrenDTO(){

            }

                @Override
                public int getItemType() {
                    return 0;
                }

                public static class EmployeeAddBookDTOListDTO {
                    public String concealPhone;
                    public String email;
                    public String employeeSubjects;
                    public String id;
                    public String name;
                    public String phone;
                    public String userId;
                    public String subjectName;

                   
                }
            }

            public static class EmployeeAddBookDTOListDTO implements Parcelable{
                public String concealPhone;
                public String email;
                public String employeeSubjects;
                public String id;
                public String name;
                public String phone;
                public String userId;
                public String subjectName;
                public String gender;
                public String avatar;

            public EmployeeAddBookDTOListDTO(){

            }

                protected EmployeeAddBookDTOListDTO(Parcel in) {
                    concealPhone = in.readString();
                    email = in.readString();
                    employeeSubjects = in.readString();
                    id = in.readString();
                    name = in.readString();
                    phone = in.readString();
                    userId = in.readString();
                }

                public static final Creator<EmployeeAddBookDTOListDTO> CREATOR = new Creator<EmployeeAddBookDTOListDTO>() {
                    @Override
                    public EmployeeAddBookDTOListDTO createFromParcel(Parcel in) {
                        return new EmployeeAddBookDTOListDTO(in);
                    }

                    @Override
                    public EmployeeAddBookDTOListDTO[] newArray(int size) {
                        return new EmployeeAddBookDTOListDTO[size];
                    }
                };

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(concealPhone);
                    dest.writeString(email);
                    dest.writeString(employeeSubjects);
                    dest.writeString(id);
                    dest.writeString(name);
                    dest.writeString(phone);
                    dest.writeString(userId);
                }
            }
        }
    }
}
