package npUpdate.nopointer.update;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import npUpdate.nopointer.model.UiConfig;
import npUpdate.nopointer.model.UpdateConfig;
import npUpdate.nopointer.util.Utils;
import org.jetbrains.annotations.NotNull;

/* compiled from: UpdateAppReceiver.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u0005¢\u0006\u0002\u0010\u0002J\"\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00042\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0002J\u0018\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J0\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0018H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D¢\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\nR\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0011\u0010\f\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001f"}, d2 = {"LnpUpdate/nopointer/update/UpdateAppReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "lastProgress", "", "notificationChannel", "", "uiConfig", "LnpUpdate/nopointer/model/UiConfig;", "getUiConfig", "()LnpUpdate/nopointer/model/UiConfig;", "uiConfig$delegate", "Lkotlin/Lazy;", "updateConfig", "LnpUpdate/nopointer/model/UpdateConfig;", "getUpdateConfig", "()LnpUpdate/nopointer/model/UpdateConfig;", "updateConfig$delegate", "handleDownloadComplete", "", "context", "Landroid/content/Context;", "notifyId", "nm", "Landroid/app/NotificationManager;", "onReceive", "intent", "Landroid/content/Intent;", "showNotification", NotificationCompat.CATEGORY_PROGRESS, "Companion", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class UpdateAppReceiver extends BroadcastReceiver {

    @NotNull
    public static final String ACTION_RE_DOWNLOAD = "action_re_download";

    @NotNull
    public static final String ACTION_UPDATE = "teprinciple.update";
    private static final String KEY_OF_INTENT_PROGRESS = "KEY_OF_INTENT_PROGRESS";
    public static final int REQUEST_CODE = 1001;
    private int lastProgress;
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(UpdateAppReceiver.class), "updateConfig", "getUpdateConfig()LnpUpdate/nopointer/model/UpdateConfig;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(UpdateAppReceiver.class), "uiConfig", "getUiConfig()LnpUpdate/nopointer/model/UiConfig;"))};

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final String notificationChannel = "1001";

    /* renamed from: updateConfig$delegate, reason: from kotlin metadata */
    private final Lazy updateConfig = LazyKt.lazy(new Function0<UpdateConfig>() { // from class: npUpdate.nopointer.update.UpdateAppReceiver$updateConfig$2
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final UpdateConfig invoke() {
            return UpdateAppUtils.INSTANCE.getUpdateInfo$npUpdate_release().getConfig();
        }
    });

    /* renamed from: uiConfig$delegate, reason: from kotlin metadata */
    private final Lazy uiConfig = LazyKt.lazy(new Function0<UiConfig>() { // from class: npUpdate.nopointer.update.UpdateAppReceiver$uiConfig$2
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final UiConfig invoke() {
            return UpdateAppUtils.INSTANCE.getUpdateInfo$npUpdate_release().getUiConfig();
        }
    });

    /* compiled from: UpdateAppReceiver.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"LnpUpdate/nopointer/update/UpdateAppReceiver$Companion;", "", "()V", "ACTION_RE_DOWNLOAD", "", "ACTION_UPDATE", UpdateAppReceiver.KEY_OF_INTENT_PROGRESS, "REQUEST_CODE", "", "send", "", "context", "Landroid/content/Context;", NotificationCompat.CATEGORY_PROGRESS, "npUpdate_release"}, k = 1, mv = {1, 1, 15})
    /* loaded from: classes2.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void send(@NotNull Context context, int progress) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intent intent = new Intent(context.getPackageName() + UpdateAppReceiver.ACTION_UPDATE);
            intent.putExtra(UpdateAppReceiver.KEY_OF_INTENT_PROGRESS, progress);
            context.sendBroadcast(intent);
        }
    }

    private final UiConfig getUiConfig() {
        Lazy lazy = this.uiConfig;
        KProperty kProperty = $$delegatedProperties[1];
        return (UiConfig) lazy.getValue();
    }

    private final UpdateConfig getUpdateConfig() {
        Lazy lazy = this.updateConfig;
        KProperty kProperty = $$delegatedProperties[0];
        return (UpdateConfig) lazy.getValue();
    }

    private final void handleDownloadComplete(Context context, int notifyId, NotificationManager nm) {
        if (nm != null) {
            nm.cancel(notifyId);
            if (Build.VERSION.SDK_INT > 26) {
                nm.deleteNotificationChannel(this.notificationChannel);
            }
        }
        if (DownloadAppUtils.INSTANCE.getDownloadUpdateApkFilePath().length() > 0) {
            Utils.INSTANCE.installApk(context, DownloadAppUtils.INSTANCE.getDownloadUpdateApkFilePath());
        }
    }

    private final void showNotification(Context context, int notifyId, int progress, String notificationChannel, NotificationManager nm) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel2 = new NotificationChannel(notificationChannel, "notification", 4);
            notificationChannel2.enableLights(false);
            notificationChannel2.setShowBadge(false);
            notificationChannel2.enableVibration(false);
            nm.createNotificationChannel(notificationChannel2);
        }
        Notification.Builder builder = new Notification.Builder(context);
        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId(notificationChannel);
        }
        boolean z = getUpdateConfig().getNotifyImgRes() > 0;
        if (z) {
            builder.setSmallIcon(getUpdateConfig().getNotifyImgRes());
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), getUpdateConfig().getNotifyImgRes()));
        }
        if (!(z)) {
            builder.setSmallIcon(R.mipmap.sym_def_app_icon);
        }
        builder.setProgress(100, this.lastProgress, false);
        if (progress == -1000) {
            Intent intent = new Intent(context.getPackageName() + ACTION_RE_DOWNLOAD);
            intent.setPackage(context.getPackageName());
            builder.setContentIntent(PendingIntent.getBroadcast(context, 1001, intent, AMapEngineUtils.MAX_P20_WIDTH));
            builder.setContentTitle(getUiConfig().getDownloadFailText());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(getUiConfig().getDownloadingBtnText());
            sb.append(progress);
            sb.append('%');
            builder.setContentTitle(sb.toString());
        }
        builder.setOnlyAlertOnce(true);
        nm.notify(notifyId, builder.build());
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        String action = intent.getAction();
        if (!Intrinsics.areEqual(action, context.getPackageName() + ACTION_UPDATE)) {
            if (Intrinsics.areEqual(action, context.getPackageName() + ACTION_RE_DOWNLOAD)) {
                DownloadAppUtils.INSTANCE.reDownload();
                return;
            }
            return;
        }
        int intExtra = intent.getIntExtra(KEY_OF_INTENT_PROGRESS, 0);
        Object systemService = context.getSystemService("notification");
        if (systemService == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.app.NotificationManager");
        }
        NotificationManager notificationManager = (NotificationManager) systemService;
        if (intExtra != -1000) {
            this.lastProgress = intExtra;
        }
        if (getUpdateConfig().isShowNotification()) {
            showNotification(context, 1, intExtra, this.notificationChannel, notificationManager);
        }
        if (intExtra == 100) {
            handleDownloadComplete(context, 1, notificationManager);
        }
    }
}
