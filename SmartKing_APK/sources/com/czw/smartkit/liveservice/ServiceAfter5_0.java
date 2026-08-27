package com.czw.smartkit.liveservice;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.czw.utils.LogUtil;
import java.util.concurrent.TimeUnit;

@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public class ServiceAfter5_0 extends JobService {
    private int kJobId = 0;

    private void doService() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService("jobscheduler");
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this, (Class<?>) BgMainService.class));
        builder.setMinimumLatency(TimeUnit.MILLISECONDS.toMillis(10L));
        builder.setOverrideDeadline(TimeUnit.MILLISECONDS.toMillis(15L));
        builder.setRequiredNetworkType(3);
        builder.setBackoffCriteria(TimeUnit.MINUTES.toMillis(10L), 0);
        builder.setRequiresCharging(false);
        jobScheduler.schedule(builder.build());
    }

    private void startMainService() {
        if (ServiceUtil.isServiceExisted(getApplicationContext(), BgMainService.class.getName())) {
            return;
        }
        LogUtil.e("重新启动主服务");
        Intent intent = new Intent(getApplicationContext(), (Class<?>) BgMainService.class);
        if (Build.VERSION.SDK_INT > 25) {
            startForegroundService(intent);
        }
        startService(intent);
    }

    public JobInfo getJobInfo() {
        JobInfo.Builder builder = new JobInfo.Builder(100, new ComponentName(getPackageName(), ServiceAfter5_0.class.getName()));
        builder.setRequiresCharging(false);
        builder.setRequiresDeviceIdle(false);
        builder.setPeriodic(100L);
        return builder.build();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.i("castiel", "jobService启动");
        scheduleJob(getJobInfo());
        return 2;
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        startMainService();
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        scheduleJob(getJobInfo());
        return false;
    }

    public void scheduleJob(JobInfo jobInfo) {
        Log.i("castiel", "调度job");
        ((JobScheduler) getSystemService("jobscheduler")).schedule(jobInfo);
    }

    public void startJobScheduler() {
        try {
            JobInfo.Builder builder = new JobInfo.Builder(100, new ComponentName(getPackageName(), ServiceAfter5_0.class.getName()));
            builder.setPersisted(true);
            builder.setRequiredNetworkType(0);
            builder.setOverrideDeadline(50000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
