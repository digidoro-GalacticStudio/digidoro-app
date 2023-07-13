package com.galacticstudio.digidoro.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}