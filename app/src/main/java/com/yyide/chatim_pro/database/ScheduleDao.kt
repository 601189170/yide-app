package com.yyide.chatim_pro.database

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
     * 插入一条日程
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //fun insert(scheduleWithParticipantAndLabel: ScheduleWithParticipantAndLabel)
    fun insert(
        schedule: ScheduleBean,
        participantList: List<ParticipantList>,
        labelList: List<LabelList>
    )

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

    //@Query("UPDATE face SET msg = :msg, face_token = :faceToken, feature = :feature WHERE face_id = :faceId")
    //update schedule set status=4 where id = '1447918736553639938'
    @Query("update schedule set status=:status where id = :id")
    fun changeScheduleState(id: String, status: String)

    //select * from schedule left join label on schedule.id = label.scheduleCreatorId
    // where  name like '%历史%' and type in (1,2) and status in (0,1) and label.id in(1447838531185115138) group by schedule.id
    //搜索日程根据赛选条件
    @Transaction
    @Query("select schedule.* from schedule left join label on schedule.id = label.scheduleCreatorId where name like '%' || :name || '%' and status in (:status) and type in(:type) group by schedule.id")
    fun searchSchedule(
       name:String,
       status: List<Int>,
       type: List<Int>
    ): List<ScheduleWithParticipantAndLabel>

    @Transaction
    @Query("select schedule.* from schedule left join label on schedule.id = label.scheduleCreatorId where name like '%' || :name || '%' and status in (:status) and type in(:type) and label.id in(:labelId) group by schedule.id")
    fun searchSchedule(
        name:String,
        status: List<Int>,
        type: List<Int>,
        labelId: List<String>
    ): List<ScheduleWithParticipantAndLabel>

}