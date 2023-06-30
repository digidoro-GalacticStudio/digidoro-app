package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.galacticstudio.digidoro.network.dto.ranking.RankingData
import com.galacticstudio.digidoro.network.dto.ranking.RankingResponse

@Dao
interface RankingDao {

    @Query("SELECT " +
            "_id, firstname, username, profile_pic, level, daily_score, weekly_score, monthly_score, total_score " +
            "FROM users WHERE _id =:id")
    suspend fun getUserRanking(id: String): RankingData
}