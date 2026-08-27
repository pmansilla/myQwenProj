package npUpdate.nopointer.update;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UpdateAppService.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"LnpUpdate/nopointer/update/UpdateAppService;", "Landroid/app/Service;", "()V", "updateAppReceiver", "LnpUpdate/nopointer/update/UpdateAppReceiver;", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "", "onDestroy", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class UpdateAppService extends Service {
    private final UpdateAppReceiver updateAppReceiver = new UpdateAppReceiver();

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(@NotNull Intent intent) {
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        registerReceiver(this.updateAppReceiver, new IntentFilter(getPackageName() + UpdateAppReceiver.ACTION_UPDATE));
        registerReceiver(this.updateAppReceiver, new IntentFilter(getPackageName() + UpdateAppReceiver.ACTION_RE_DOWNLOAD));
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.updateAppReceiver);
    }
}
