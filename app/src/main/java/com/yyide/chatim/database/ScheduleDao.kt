package com.yyide.chatim.database

import androidx.room.*

/**
 *
 * @author liu tao
 * @date 2021/10/14 14:33
 * @description 描述
 */
@Dao
interface ScheduleDao {
    /**
     * 查询所有日程
     */
    @Transaction
    @Query("SELECT * FROM schedule")
    fun getScheduleWithParticipantAndLabelLists(): List<ScheduleWithParticipantAndLabel>

    /**
     * 查询一条日程
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //fun insert(scheduleWithParticipantAndLabel: ScheduleWithParticipantAndLabel)
    fun insert(schedule: ScheduleBean, participantList: List<ParticipantList>, labelList: List<LabelList>)

    @Query("DELETE FROM schedule ")
    fun deleteAllSchedule()

    @Query("DELETE FROM participant ")
    fun deleteAllParticipant()

    @Query("DELETE FROM label ")
    fun deleteAllLabel()

    /**
     * 查询指定日期之前的日程数据
     * select * from schedule  WHERE startTime <='2021-10-11 00:00:00'
     */
    @Transaction
    @Query("select * from schedule  WHERE startTime <=:startTime")
    fun getStartDateBeforeScheduleList(startTime: String): List<ScheduleWithParticipantAndLabel>

    /**
     * 查询本周未完成的日程数据
     */
    @Transaction
    @Query("select * from schedule  WHERE startTime <=:startTime and status = 0")
    fun getOneWeekUndoneScheduleList(startTime: String): List<ScheduleWithParticipantAndLabel>

    @Query("delete from schedule where id=:id")
    fun deleteSchedule(id: String)

    @Query("delete from participant where scheduleCreatorId=:scheduleCreatorId")
    fun deleteParticipant(scheduleCreatorId: String)

    @Query("delete from label where id=:scheduleCreatorId")
    fun deleteLabel(scheduleCreatorId: String)

//    @Transaction
//    @Update
//    fun updateSchedule(scheduleWithParticipantAndLabel:ScheduleWithParticipantAndLabel)

}