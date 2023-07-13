package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.data.db.models.PendingRequestEntity
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class RequestRepository(
    database: DigidoroDataBase,
) {
    private val requestDao = database.PendingRequestDao()

    suspend fun getAllRequest(): ApiResponse<List<PendingRequestEntity>> {
        return handleApiCall {
            requestDao.getAllPendingRequests()
        }
    }

    suspend fun deletePendingRequest(request: PendingRequestEntity): ApiResponse<Unit> {
        return handleApiCall {
            requestDao.deletePendingRequest(request._id)
        }
    }
}