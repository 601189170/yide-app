package com.yyide.chatim_pro.model;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/3/26 10:25
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/26 10:25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class StudentHonorCountBean {
    private String studentName;
    private int honorCount;

    public StudentHonorCountBean() {
    }

    public StudentHonorCountBean(String studentName, int honorCount) {
        this.studentName = studentName;
        this.honorCount = honorCount;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getHonorCount() {
        return honorCount;
    }

    public void setHonorCount(int honorCount) {
        this.honorCount = honorCount;
    }

    @Override
    public String toString() {
        return "StudentHonorCountBean{" +
                "studentName='" + studentName + '\'' +
                ", honorCount=" + honorCount +
                '}';
    }
}
