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
    fun insert(schedule: ScheduleBean,participantList: List<ParticipantList>,labelList: List<LabelList>)
}