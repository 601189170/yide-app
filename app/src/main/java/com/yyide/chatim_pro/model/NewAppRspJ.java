package com.yyide.chatim_pro.model;

import java.util.List;

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/8 17:13
 * @Description : 文件描述
 */
public class NewAppRspJ {

    private List<AppsDTO> apps;
    private String categoryName;

    private String status;

    public NewAppRspJ(List<AppsDTO> apps, String categoryName, String status) {
        this.apps = apps;
        this.categoryName = categoryName;
        this.status = status;
    }

    public NewAppRspJ() {
    }

    public void setApps(List<AppsDTO> apps) {
        this.apps = apps;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AppsDTO> getApps() {
        return apps;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getStatus() {
        return status;
    }

    public static class AppsDTO {
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAppurl() {
            return appurl;
        }

        public void setAppurl(String appurl) {
            this.appurl = appurl;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public AppsDTO(String id, String appurl, String logo, String name) {
            this.id = id;
            this.appurl = appurl;
            this.logo = logo;
            this.name = name;
        }

        public AppsDTO() {

        }

        private String id;
        private String appurl;
        private String logo;
        private String name;

    }
}
