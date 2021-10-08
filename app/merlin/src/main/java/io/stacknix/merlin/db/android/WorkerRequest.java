package io.stacknix.merlin.db.android;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;


public class WorkerRequest extends Worker {

    public static final String NET_WORKER = "NET_WORKER.WorkerRequest";

    public WorkerRequest(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        return Result.success();
    }

    public static void schedule(Context context) {
        WorkManager workManager = WorkManager.getInstance(context);
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest workerRequest = new OneTimeWorkRequest.Builder(WorkerRequest.class)
                .setConstraints(constraints)
                .build();
        workManager.enqueueUniqueWork(NET_WORKER, ExistingWorkPolicy.APPEND_OR_REPLACE, workerRequest);
    }
}
