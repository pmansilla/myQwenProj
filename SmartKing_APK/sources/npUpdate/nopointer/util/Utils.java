package npUpdate.nopointer.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import npUpdate.nopointer.extension.AnyKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Utils.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J\u0006\u0010\u0007\u001a\u00020\u0004J\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u0006J\b\u0010\f\u001a\u00020\rH\u0007J\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000f\u001a\u00020\rJ\u0016\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\u0006J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\r¨\u0006\u0013"}, d2 = {"LnpUpdate/nopointer/util/Utils;", "", "()V", "deleteFile", "", "filePath", "", "exitApp", "getAPPVersionCode", "", "getApkVersionCode", "apkPath", "getApp", "Landroid/content/Context;", "getAppName", "context", "installApk", "isWifiConnected", "", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class Utils {
    public static final Utils INSTANCE = new Utils();

    private Utils() {
    }

    @JvmStatic
    @NotNull
    public static final Context getApp() {
        return GlobalContextProvider.INSTANCE.getGlobalContext();
    }

    public final void deleteFile(@Nullable String filePath) {
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        try {
            if (file.isFile()) {
                file.delete();
                AnyKt.log(INSTANCE, "删除成功");
            }
        } catch (Exception e) {
            AnyKt.log(this, e.getMessage());
        }
    }

    public final void exitApp() {
        Object systemService = getApp().getSystemService("activity");
        if (systemService == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.app.ActivityManager");
        }
        ActivityManager activityManager = (ActivityManager) systemService;
        if (Build.VERSION.SDK_INT < 21) {
            System.exit(0);
            return;
        }
        List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
        Intrinsics.checkExpressionValueIsNotNull(appTasks, "manager.appTasks");
        Iterator<T> it = appTasks.iterator();
        while (it.hasNext()) {
            ((ActivityManager.AppTask) it.next()).finishAndRemoveTask();
        }
    }

    public final int getAPPVersionCode() {
        Context applicationContext = getApp().getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "getApp().applicationContext");
        PackageManager packageManager = applicationContext.getPackageManager();
        try {
            Context applicationContext2 = getApp().getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext2, "getApp().applicationContext");
            return packageManager.getPackageInfo(applicationContext2.getPackageName(), 0).versionCode;
        } catch (Exception unused) {
            return -1;
        }
    }

    public final int getApkVersionCode(@NotNull String apkPath) {
        Intrinsics.checkParameterIsNotNull(apkPath, "apkPath");
        Context applicationContext = getApp().getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "getApp().applicationContext");
        try {
            return applicationContext.getPackageManager().getPackageArchiveInfo(apkPath, 1).versionCode;
        } catch (Exception unused) {
            return -1;
        }
    }

    @Nullable
    public final String getAppName(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        try {
            return context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final void installApk(@NotNull Context context, @NotNull String apkPath) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(apkPath, "apkPath");
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(apkPath);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags(1);
            intent.setDataAndType(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file), "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
        context.startActivity(intent);
    }

    public final boolean isWifiConnected(@NotNull Context context) {
        NetworkInfo activeNetworkInfo;
        Intrinsics.checkParameterIsNotNull(context, "context");
        Object systemService = context.getSystemService("connectivity");
        if (!(systemService instanceof ConnectivityManager)) {
            systemService = null;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) systemService;
        return (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || activeNetworkInfo.getType() != 1) ? false : true;
    }
}
