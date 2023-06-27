package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.ranking.RankingListResponse
import com.galacticstudio.digidoro.network.dto.ranking.RankingRequest
import com.galacticstudio.digidoro.network.dto.ranking.RankingResponse
import com.galacticstudio.digidoro.network.dto.ranking.UpdateScoreResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface RankingService {
    @GET("api/user/topUsers")
    suspend fun getOwnRanking(): RankingResponse

    @GET("api/user/ranking")
    suspend fun getTopUsers(): RankingListResponse

    @PATCH("api/user/updateScores/")
    suspend fun updateScore(@Body score: RankingRequest): UpdateScoreResponse
}