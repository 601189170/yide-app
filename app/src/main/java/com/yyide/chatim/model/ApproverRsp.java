package com.yyide.chatim.model;

import java.util.List;

/**
 * @Description: 获取流程审批人和抄送人实体
 * @Author: liu tao
 * @CreateDate: 2021/5/20 10:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/20 10:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class ApproverRsp {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    public ApproverRsp() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private PeopleFormBean peopleForm;
        private List<ListBean> list;

        public DataBean() {
        }

        public PeopleFormBean getPeopleForm() {
            return peopleForm;
        }

        public void setPeopleForm(PeopleFormBean peopleForm) {
            this.peopleForm = peopleForm;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class PeopleFormBean {
            private long userId;
            private String name;
            private String image;

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public PeopleFormBean() {
            }
        }

        public static class ListBean {
            private long userId;
            private String name;
            private String image;

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public ListBean() {
            }
        }
    }
}
