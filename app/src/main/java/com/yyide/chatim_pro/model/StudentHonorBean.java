package com.yyide.chatim_pro.model;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/3/25 17:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/25 17:27
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class StudentHonorBean {
    private String url;
    private String name;
    private String time;
    private int img;

    public StudentHonorBean() {
    }

    public StudentHonorBean(String url, String name, String time, int img) {
        this.url = url;
        this.name = name;
        this.time = time;
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "StudentHonorBean{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
