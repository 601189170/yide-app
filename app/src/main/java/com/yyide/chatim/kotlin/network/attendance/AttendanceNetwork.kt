package com.yyide.chatim.kotlin.network.attendance

import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.base.BaseNetworkApi
import okhttp3.RequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/3/30 17:55
 * @description 考勤api
 */
object AttendanceNetwork : BaseNetworkApi<IAttendanceService>(BaseConstant.API_SERVER_URL) {


    /**
     * 教师考勤-统计-月度考勤
     */
    suspend fun requestClockRecordByMonth(month:String) =
        getResult {
            service.requestClockRecordByMonth(month)
        }


    /**
     * 教师考勤-统计-日度考勤
     */
    suspend fun requestClockRecordByDay(day: String) =
        getResult {
            service.requestClockRecordByDay(day)
        }


    /**
     * 教师考勤-查询规则信息
     */
    suspend fun requestAttendanceRule() =
        getResult {
            service.requestAttendanceRule()
        }

    /**
     * 教师考勤-查询打卡信息
     */
    suspend fun requestPunchMessage(requestBody: RequestBody) =
        getResult {
            service.requestPunchMessage(requestBody)
        }

    /**
     * 地址打卡
     */
    suspend fun punchByAddress(requestBody: RequestBody) =
        getResult {
            service.punchByAddress(requestBody)
        }

    /**
     * wifi打卡
     */
    suspend fun punchByWifi(requestBody: RequestBody) =
        getResult {
            service.punchByWifi(requestBody)
        }


}