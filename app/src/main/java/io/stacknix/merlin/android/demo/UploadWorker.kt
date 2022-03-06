package io.stacknix.merlin.android.demo

import android.content.Context
import androidx.work.*
import io.stacknix.merlin.android.demo.models.Project
import io.stacknix.merlin.android.demo.samples.AuthUtilSample.Companion.getClient

class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        return try {
            JsonRPCService(Project::class.java, getClient())
                .performSync()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {

        private const val HttpWorker = "HttpWorker"

        fun sync(context: Context) {
            val workManager = WorkManager.getInstance(context)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workerRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
                .setConstraints(constraints)
                .build()
            workManager.enqueueUniqueWork(
                HttpWorker,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                workerRequest
            )
        }
    }

}