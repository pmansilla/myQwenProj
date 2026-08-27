package com.czw.smartkit;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import com.czw.smartkit.bleModule.LanguageUtil;
import com.czw.smartkit.bleModule.MsgPushHelper;
import com.czw.smartkit.bleModule.gpsLocation.GpsLocationHelper;
import com.czw.smartkit.bleModule.receivers.ReceiverCfgHelper;
import com.czw.smartkit.databaseModule.DbCfgUtil;
import com.czw.smartkit.liveservice.BgMainService;
import com.czw.smartkit.util.SaveObjectUtils;
import com.czw.utils.LogUtil;
import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;
import java.util.List;
import ycble.runchinaup.aider.PushAiderHelper;
import ycble.runchinaup.core.ycBleSDK;
import ycble.runchinaup.log.ycBleLog;
import ycnet.runchinaup.log.ycNetLog;

/* loaded from: classes.dex */
public class MainApplication extends Application {
    private static final String PROCESS_NAME = "com.czw.smartkit";
    static MainApplication app;
    private DisplayMetrics displayMetrics = null;

    private void cfgDevMode() {
        ycBleLog.allowE = false;
        BaseCfg.isAllowSameVersionOTA = false;
        ycNetLog.allowE = false;
        LogUtil.allowE = false;
        BaseCfg.isAllowSameVersionUPG = false;
        CrashReport.initCrashReport(getApplicationContext(), "6129327f99", true);
    }

    public static MainApplication getApp() {
        return app;
    }

    public static MainApplication getContext() {
        return app;
    }

    private String getProcessName(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.pid == Process.myPid() && runningAppProcessInfo.processName != null) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        if (getProcessName(this).equalsIgnoreCase("com.czw.smartkit")) {
            app = this;
            ycBleSDK.initSDK(this);
            GpsLocationHelper.getInstance().initGps(this);
            LanguageUtil.getLanguageCode();
            SaveObjectUtils.init(this, "cfg_xml_local");
            DbCfgUtil.getDbCfgUtil().init(this);
            PushAiderHelper.getAiderHelper().isNotifyEnable(this);
            PushAiderHelper.getAiderHelper().setMsgReceiveCallback(MsgPushHelper.getMsgPushHelper());
            startService(new Intent(this, (Class<?>) BgMainService.class));
            MobSDK.init(this);
            ReceiverCfgHelper.getInstance().register(this);
            cfgDevMode();
        }
    }
}
