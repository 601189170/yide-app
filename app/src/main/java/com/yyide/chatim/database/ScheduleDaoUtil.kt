package com.yyide.chatim.database

import com.yyide.chatim.BaseApplication
import com.yyide.chatim.model.schedule.DayOfMonth
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/10/15 10:06
 * @description 日程本地数据查询工具
 */
object ScheduleDaoUtil {

    private fun scheduleDao(): ScheduleDao {
        return AppDatabase.getInstance(BaseApplication.getInstance()).scheduleDao()
    }

    /**
     * 今日清单日程列表
     */
    fun todayList(): List<ScheduleData> {
        //今日最后的时间
        val finallyTime = DateTime.now().toString("yyyy-MM-dd ") + "23:59:59"
        val startDateBeforeScheduleList = scheduleDao().getStartDateBeforeScheduleList(finallyTime)
        val scheduleDataList = mutableListOf<ScheduleData>()
        scheduleDataList.addAll(startDateBeforeScheduleList.map { it.scheduleWithParticipantAndLabelToScheduleData() })
        //需要的日程数据包括重复日程
        val listAllSchedule = mutableListOf<ScheduleData>()
        scheduleDataList.forEach {
            val repetitionDate =
                ScheduleRepetitionRuleUtil.calculate(it.startTime, finallyTime, it.rrule)
            if (repetitionDate.contains(DateTime.now().simplifiedDataTime())) {
                listAllSchedule.add(it)
            }
        }
        return listAllSchedule
    }

    /**
     * 查询本周未完成日程列表
     */
    fun undoneOfWeek(): List<ScheduleData> {
        val nowDateTime = DateTime.now()
        //本周第一天
        val firstDayOfWeek = nowDateTime.minusDays(nowDateTime.dayOfWeek % 7).simplifiedDataTime()
        val lastDayOfWeek =
            nowDateTime.plusDays(7 - nowDateTime.dayOfWeek % 7 - 1).simplifiedDataTime()
        //本周最后的时间
        val finallyTime = lastDayOfWeek.toString("yyyy-MM-dd ") + "23:59:59"
        val startDateBeforeScheduleList = scheduleDao().getOneWeekUndoneScheduleList(finallyTime)
        val scheduleDataList = mutableListOf<ScheduleData>()
        scheduleDataList.addAll(startDateBeforeScheduleList.map { it.scheduleWithParticipantAndLabelToScheduleData() })
        //需要的本周重复日程
        val listAllSchedule = mutableListOf<ScheduleData>()
        scheduleDataList.forEach { schedule ->
            val repetitionDate = ScheduleRepetitionRuleUtil.calculate(
                schedule.startTime,
                finallyTime,
                schedule.rrule
            )
            repetitionDate.forEach {
                if (it in firstDayOfWeek..lastDayOfWeek) {
                    listAllSchedule.add(schedule)
                }
            }
        }
        return listAllSchedule
    }

    /**
     * 查询指定月的数据
     */
    fun monthlyList(monthDateTime: DateTime): List<DayOfMonth> {
        val firstDayOfMonth = monthDateTime.dayOfMonth().withMinimumValue().simplifiedDataTime()
        val lastDayOfMonth = monthDateTime.dayOfMonth().withMaximumValue().simplifiedDataTime()
        //本周最后的时间
        val finallyTime = lastDayOfMonth.toString("yyyy-MM-dd ") + "23:59:59"
        val startDateBeforeScheduleList = scheduleDao().getStartDateBeforeScheduleList(finallyTime)
        val scheduleDataList = mutableListOf<ScheduleData>()
        scheduleDataList.addAll(startDateBeforeScheduleList.map { it.scheduleWithParticipantAndLabelToScheduleData() })
        //本月需要的日程数据
        val listAllSchedule = mutableListOf<DayOfMonth>()
        scheduleDataList.forEach { schedule ->
            val repetitionDate = ScheduleRepetitionRuleUtil.calculate(
                schedule.startTime,
                finallyTime,
                schedule.rrule
            )
            repetitionDate.forEach {
                if (it in firstDayOfMonth..lastDayOfMonth) {
                    listAllSchedule.add(DayOfMonth(it,schedule))
                }
            }
        }
        return listAllSchedule
    }

    /**
     * 删除一条本地的日程数据
     */
    fun deleteScheduleData(scheduleId:String){
        scheduleDao().deleteSchedule(scheduleId)
        scheduleDao().deleteParticipant(scheduleId)
        scheduleDao().deleteLabel(scheduleId)
    }
    /**
     * 清空数据 临时
     */
    fun clearAll() {
        scheduleDao().deleteAllSchedule()
        scheduleDao().deleteAllParticipant()
        scheduleDao().deleteAllLabel()
    }

//    /**
//     * 更新日程数据
//     */
//    fun updateScheduleData(scheduleData: ScheduleData){
//        val scheduleWithParticipantAndLabel = scheduleData.scheduleDataToScheduleWithParticipantAndLabel()
//        scheduleDao().updateSchedule(scheduleWithParticipantAndLabel)
//    }

}