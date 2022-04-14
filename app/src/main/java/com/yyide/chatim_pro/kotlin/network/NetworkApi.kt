package com.yyide.chatim_pro.kotlin.network

import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.base.BaseNetworkApi
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * 网络请求具体实现
 */
object NetworkApi : BaseNetworkApi<INetworkService>(BaseConstant.API_SERVER_URL) {

    /**
     * 登录
     */
    suspend fun login(requestBody: RequestBody) = getResult {
        service.login(requestBody)
    }


    /**
     * 请假拒接或同意
     */
    suspend fun leaveRefuseOrPass(requestBody: RequestBody) = getResult {
        service.leaveRefuseOrPass(requestBody)
    }

    /**
     * 待办列表
     */
    suspend fun todoList(requestBody: RequestBody) = getResult {
        service.todoList(requestBody)
    }

    /**
     * 待办列表
     */
    suspend fun requestIMSign(id: String) = getResult {
        service.requestImSign(id)
    }

    /**
     * 获取学校和身份信息
     */
    suspend fun getSchoolIdentityInfo() = getResult {
        service.schoolIdentityInfo()
    }

    /**
     * 学校身份登录应用
     */
    suspend fun schoolIdentityLogin(requestBody: RequestBody) = getResult {
        service.schoolIdentityLogin(requestBody)
    }

    /**
     * 教师/班主任周报
     */
    suspend fun teacherWeekly(
        classId: String,
        teacherId: String,
        startTime: String,
        endTime: String
    ) = getResult {
        service.requestTeacherWeekly(classId, teacherId, startTime, endTime)
    }

    /**
     * 校长周报
     */
    suspend fun schoolWeekly(startTime: String, endTime: String) = getResult {
        service.requestHeadmasterWeekly(startTime, endTime)
    }

    /**
     * 家长周报
     */
    suspend fun parentsWeekly(studentId: String, startTime: String, endTime: String) = getResult {
        service.requestStudentWeekly(studentId, startTime, endTime)
    }

    /**
     * 家长周报
     */
    suspend fun requestCopywriter() = getResult {
        service.requestCopywriter()
    }

    /**
     * 校长查看全校教师考勤
     */
    suspend fun requestSchoolTeacherAttendance(startTime: String, endTime: String) = getResult {
        service.requestSchoolTeacherAttendance(startTime, endTime)
    }

    /**
     * 校长查看全校学生考勤
     */
    suspend fun requestSchoolStudentAttendance(startTime: String, endTime: String) = getResult {
        service.requestSchoolStudentAttendance(startTime, endTime)
    }

    /**
     * 班主任查看老师考勤
     */
    suspend fun requestAttendanceTeacherDetail(
        classId: String,
        teacherId: String,
        startTime: String,
        endTime: String
    ) = getResult {
        service.requestAttendanceTeacherDetail(classId, teacherId, startTime, endTime)
    }

    /**
     * 老师查看学生考勤
     */
    suspend fun requestAttendanceStudentDetail(
        classId: String,
        teacherId: String,
        startTime: String,
        endTime: String
    ) = getResult {
        service.requestAttendanceStudentDetail(classId, teacherId, startTime, endTime)
    }

    /**
     * 家长周报
     */
    suspend fun requestParentsStudentDetail(studentId: String, startTime: String, endTime: String) =
        getResult {
            service.requestStudentDetail(studentId, startTime, endTime)
        }

    /**
     * 会议首页今日列表
     */
    suspend fun requestMeetingHomeList(requestBody: RequestBody) =
        getResult {
            service.requestMeetingHomeList(requestBody)
        }

    /**
     * 会议创建、修改
     */
    suspend fun requestMeetingSave(requestBody: RequestBody) =
        getResult {
            service.requestMeetingSave(requestBody)
        }

    /**
     * 会议列表
     */
    suspend fun requestMeetingList(requestBody: RequestBody) =
        getResult {
            service.requestMeetingList(requestBody)
        }

    /**
     * 会议详情
     */
    suspend fun requestMeetingDetail(id: String) =
        getResult {
            service.requestMeetingDetail(id)
        }

    /**
     * 删除会议
     */
    suspend fun requestMeetingDel(scheduleId: String) =
        getResult {
            service.requestMeetingDel(scheduleId)
        }

    /**
     * 教职工作业列表
     */
    suspend fun requestTeacherWorkList(requestBody: RequestBody) =
        getResult {
            service.requestTeacherWorkList(requestBody)
        }


    /**
     * 获取科目
     */
    suspend fun selectSubjectByUserId() =
        getResult {
            service.requestselectSubjectByUserId()
        }

    /**
     * 获取年级班级科目联动数据
     */
    suspend fun getClassSubjectList(isWhole: String) =
        getResult {
            service.getClassSubjectLis(isWhole)
        }

    /**
     * 教职工查看详情
     */
    suspend fun getSchoolWorkData(id: String) =
        getResult {
            service.getSchoolWorkData(id)
        }

    /**
     * 作业-撤销
     */
    suspend fun deleteWork(id: String) =
        getResult {
            service.deleteWork(id)
        }

    /**
     * 教职工作业-提醒家长
     */
    suspend fun updateRemind(requestBody: RequestBody) =
        getResult {
            service.updateRemind(requestBody)
        }

    /**
     * 家长列表获取学生数据
     */
    suspend fun selectParentStudent() =
        getResult {
            service.selectParentStudent()
        }

    /**
     * 家长端-作业列表
     */
    suspend fun selectParentPage(requestBody: RequestBody) =
        getResult {
            service.selectParentPage(requestBody)
        }

    /**
     * 家长端-作业列表
     */
    suspend fun getParensWorkInfo(id: String) =
        getResult {
            service.getSchoolWorkFeedback(id)
        }


    /**
     * 家长-提交反馈
     */
    suspend fun CommitFeedback(requestBody: RequestBody) =
        getResult {
            service.updateWorkFeedback(requestBody)
        }

    /**
     * 获取关联课表
     */
    suspend fun getWeekTime(requestBody: RequestBody) =
        getResult {
            service.getWeekTime(requestBody)
        }

    /**
     * 作业添加-获取科目
     */
    suspend fun getSubject() =
        getResult {
            service.selectSubjectByUserId()
        }

    /**
     * 作业添加-获取班级
     */
    suspend fun getClassList() =
        getResult {
            service.getClassList()
        }

    /**
     * 作业-添加
     */
    suspend fun createWork(requestBody: RequestBody) =
        getResult {
            service.createWork(requestBody)
        }


    /**
     * 获取工作台App列表
     */
    suspend fun getAppList() =
        getResult {
            service.getAppList()
        }

    /**
     * 获取学生信息列表
     */
    suspend fun getFaceList(id: String) =
        getResult {
            service.getFaceList(id)
        }


    /**
     * 获取学生信息列表
     */
    suspend fun upPohto(lists: List<MultipartBody.Part>) =
            getResult {

                service.upload(lists)
            }
}