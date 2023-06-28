package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.ranking.RankingListResponse
import com.galacticstudio.digidoro.network.dto.ranking.RankingRequest
import com.galacticstudio.digidoro.network.dto.ranking.RankingResponse
import com.galacticstudio.digidoro.network.dto.ranking.UpdateScoreResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface RankingService {
    @GET("api/user/ranking")
    suspend fun getOwnRanking(): RankingResponse

    @GET("api/user/topUsers")
    suspend fun getTopUsers(
        @Query("sortBy") sortBy: String? = null,
    ): RankingListResponse

    @PATCH("api/user/updateScores/")
    suspend fun updateScore(@Body score: RankingRequest): UpdateScoreResponse
}