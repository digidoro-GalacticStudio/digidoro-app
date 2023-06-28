package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.ranking.RankingListResponse
import com.galacticstudio.digidoro.network.dto.ranking.RankingRequest
import com.galacticstudio.digidoro.network.dto.ranking.RankingResponse
import com.galacticstudio.digidoro.network.dto.ranking.UpdateScoreResponse
import com.galacticstudio.digidoro.network.service.RankingService
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class RankingRepository(private val rankingService: RankingService) {
    suspend fun getOwnRanking(): ApiResponse<RankingResponse> {
        return handleApiCall { rankingService.getOwnRanking() }
    }

    suspend fun getTopUsers(orderBy : String): ApiResponse<RankingListResponse> {
        return handleApiCall { rankingService.getTopUsers(orderBy) }
    }

    suspend fun updateScore(score: Int): ApiResponse<UpdateScoreResponse> {
        val request = RankingRequest(score)
        return handleApiCall { rankingService.updateScore(request) }
    }
}
