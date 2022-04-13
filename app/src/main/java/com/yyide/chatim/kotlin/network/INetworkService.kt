package com.yyide.chatim.kotlin.network

import com.yyide.chatim.kotlin.network.base.BaseResponse
import com.yyide.chatim.model.*
import com.yyide.chatim.model.schedule.ScheduleData
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface INetworkService {

    /**
     * 待办列表
     */
    @POST("/cloud/app/procAppr/pageTask")
    suspend fun todoList(@Body requestBody: RequestBody): BaseResponse<TodoRsp>


    /**
     * 请求IM签名数据
     */
    @GET("/cloud/im/user/sig/gen")
    suspend fun requestImSign(@Query("identifier") id: String): BaseResponse<UserSigRsp.IMDataBean>

    /**
     * 请假拒绝、同意
     */
    @POST("/cloud/proc/completeTask")
    suspend fun leaveRefuseOrPass(@Body requestBody: RequestBody): BaseResponse<String>

    /**
     * 登录
     */
    @POST("/auth/oauth/token")
    suspend fun login(@Body requestBody: RequestBody): BaseResponse<LoginRsp>

    /**
     * 选择身份后登录-获取学校身份信息
     */
    @POST("/cloud/mobile/user/login")
    suspend fun schoolIdentityLogin(@Body requestBody: RequestBody): BaseResponse<UserBean>

    /**
     * 刷新Token
     */
    @POST("/auth/oauth/token")
    suspend fun refreshToken(@Body requestBody: RequestBody): BaseResponse<LoginRsp>


    /**
     * 获取学校信息
     */
    @GET("/cloud/mobile/user/school")
    suspend fun schoolIdentityInfo(): BaseResponse<List<SchoolRsp>>

    /**
     * 教师班主任周报
     * classId	是	string	班级id
     * teacherId	是	string	老师id
     * startTime	是	string	这周开始时间
     * endTime	是	string	这周结束时间
     */
    @GET("/face/cloud-face/app/v1/teacher/weekly")
    suspend fun requestTeacherWeekly(
        @Query("classId") classId: String,
        @Query("teacherId") teacherId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<WeeklyTeacherBean>

    /**
     * 查看教师考勤
     */
    @GET("/face/cloud-face/app/v1/teacher/attend/teacher/detail")
    suspend fun requestAttendanceTeacherDetail(
        @Query("classId") classId: String,
        @Query("teacherId") teacherId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<WeeklyTeacherAttendanceBean>

    /**
     * 查看学生考勤
     */
    @GET("/face/cloud-face/app/v1/teacher/attend/student/detail")
    suspend fun requestAttendanceStudentDetail(
        @Query("classId") classId: String,
        @Query("teacherId") teacherId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<WeeklyStudentBean>


    /**
     * 校长周报统计
     * startTime 开始时间
     * endTime   结束时间
     */
    @GET("/face/cloud-face/app/v1/headmaster/weekly")
    suspend fun requestHeadmasterWeekly(
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<SchoolWeeklyData>

    /**
     * 家长查看学生周报统计
     * studentId  学生ID
     * startTime  开始时间
     * endTime    结束时间
     */
    @GET("/face/cloud-face/app/v1/student/weekly")
    suspend fun requestStudentWeekly(
        @Query("studentId") studentId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<WeeklyParentsBean>

    /**
     * 家长查看学生周报统计详情
     * studentId  学生ID
     * startTime  开始时间
     * endTime    结束时间
     */
    @GET("/face/cloud-face/app/v1/student/weekly/detail")
    suspend fun requestStudentDetail(
        @Query("studentId") studentId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<WeeklyParentsAttendDetailBean>

    /**
     * 底部文案描述
     */
    @GET("/face/cloud-face/copywriter")
    suspend fun requestCopywriter(): BaseResponse<ResultBean>

    /**
     * 校长查看考勤 教师
     */
    @GET("/face/cloud-face/app/v1/headmaster/teacher/weekly/detail")
    suspend fun requestSchoolTeacherAttendance(
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<SchoolWeeklyTeacherBean>

    /**
     * 校长查看考勤 学生
     */
    @GET("/face/cloud-face/app/v1/headmaster/student/weekly/detail")
    suspend fun requestSchoolStudentAttendance(
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<SchoolWeeklyTeacherBean>

    /**
     * 会议首页列表
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/app/schedule/meetingList")
    suspend fun requestMeetingHomeList(@Body requestBody: RequestBody): BaseResponse<MeetingListData>

    /**
     * 会议创建/修改
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/app/schedule/createSchedule")
    suspend fun requestMeetingSave(@Body requestBody: RequestBody): BaseResponse<String>

    /**
     * 会议列表
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/app/schedule/meetingList")
    suspend fun requestMeetingList(@Body requestBody: RequestBody): BaseResponse<MeetingListData>

    /**
     * 查看会议详情
     */
    @GET("/cloud/app/schedule/findMeetingDetails/{id}")
    suspend fun requestMeetingDetail(
        @Path("id") id: String
    ): BaseResponse<ScheduleData>

    /**
     * 删除会议
     */
    @GET("/cloud/app/schedule/deleteSchedule/{scheduleId}")
    suspend fun requestMeetingDel(
        @Path("scheduleId") id: String
    ): BaseResponse<Boolean>

    /**
     * 教职工作业列表
     */
    @POST("/cloud/app/work/page")
    suspend fun requestTeacherWorkList(@Body requestBody: RequestBody): BaseResponse<TeacherWorkListRsp>

    /**
     * 获取科目
     */
    @GET("/cloud/app/work/selectSubjectByUserId")
    suspend fun requestselectSubjectByUserId(): BaseResponse<List<SubjectBean>>

    /**
     * 获取年级班级科目联动数据
     */
    @GET("/cloud/app/work/getClassSubjectList")
    suspend fun getClassSubjectLis(@Query("isWhole") isWhole: String): BaseResponse<List<getClassSubjectListRsp>>

    /**
     * 教职工查看详情
     */
    @GET("/cloud/app/work/getSchoolWork")
    suspend fun getSchoolWorkData(@Query("id") id: String): BaseResponse<getSchoolWork>

    /**
     * 获取工作台App列表
     */
    @GET("/cloud/appCenter/list")
    suspend fun getAppList(): BaseResponse<MutableList<NewAppRspJ>>

    /**
     * 作业-撤销
     */
    @GET("/cloud/app/work/delete")
    suspend fun deleteWork(@Query("id") id: String): BaseResponse<Boolean>

    /**
     * 教职工作业-提醒家长
     */
    @POST("/cloud/app/work/updateRemind")
    suspend fun updateRemind(@Body requestBody: RequestBody): BaseResponse<Boolean>

    /**
     * 家长列表获取学生数据
     */
    @GET("/cloud/app/work/selectParentStudent")
    suspend fun selectParentStudent(): BaseResponse<List<selectParentStudent>>

    /**
     * 家长列表获取学生数据
     */
    @POST("/cloud/app/work/selectParentPage")
    suspend fun selectParentPage(@Body requestBody: RequestBody): BaseResponse<ParentWorkList>

    /**
     * 家长查看作业详情
     */
    @GET("/cloud/app/work/getSchoolWorkFeedback")
    suspend fun getSchoolWorkFeedback(@Query("id") id: String): BaseResponse<getSchoolWorkFeedback>

    /**
     * 家长-提交反馈
     */
    @POST("/cloud/app/work/updateWorkFeedback")
    suspend fun updateWorkFeedback(@Body requestBody: RequestBody): BaseResponse<Boolean>

    /**
     * 获取关联课表
     */
    @POST("/cloud/app/timetable/getWeekTime")
    suspend fun getWeekTime(@Body requestBody: RequestBody): BaseResponse<getWeekTimeRsp>

    /**
     * 作业添加-获取科目
     */
    @GET("/cloud/app/work/selectSubjectByUserId")
    suspend fun selectSubjectByUserId(): BaseResponse<List<selectSubjectByUserIdRsp>>

    /**
     * 作业添加-获取班级
     */
    @GET("/cloud/app/timetable/getClassList")
    suspend fun getClassList(): BaseResponse<List<getClassList>>

    /**
     * 作业-添加
     */
    @POST("/cloud/app/work/create")
    suspend fun createWork(@Body requestBody: RequestBody): BaseResponse<String>

    /**
     * 家长学生 信息列表
     */
    @GET("/cloud/student/getFaceList")
    suspend fun getFaceList(@Query("identityId") identityId: String): BaseResponse<List<FaceStudentBean>>

    /**
     * 批量上传图片
     */
    @Multipart
    @POST("/cloud/file/upload")
    suspend fun upload(@Part infos: List<MultipartBody.Part> ): BaseResponse<List<UploadRsp>>
}

