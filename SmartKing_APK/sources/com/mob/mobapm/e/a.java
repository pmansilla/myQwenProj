package com.mob.mobapm.e;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import com.mob.MobSDK;
import com.mob.tools.utils.ActivityTracker;

/* loaded from: classes.dex */
public class a {
    private static a c;
    private String a = null;
    private long b = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.mob.mobapm.e.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0067a implements ActivityTracker.Tracker {
        C0067a() {
        }

        @Override // com.mob.tools.utils.ActivityTracker.Tracker
        public void onCreated(Activity activity, Bundle bundle) {
        }

        @Override // com.mob.tools.utils.ActivityTracker.Tracker
        public void onDestroyed(Activity activity) {
            if (a.this.b > 0) {
                onStopped(activity);
            }
        }

        @Override // com.mob.tools.utils.ActivityTracker.Tracker
        public void onPaused(Activity activity) {
        }

        @Override // com.mob.tools.utils.ActivityTracker.Tracker
        public void onResumed(Activity activity) {
            if (a.this.b == 0) {
                a.this.b = SystemClock.elapsedRealtime();
                com.mob.mobapm.core.b.e().c();
            }
            a.this.a = activity.toString();
        }

        @Override // com.mob.tools.utils.ActivityTracker.Tracker
        public void onSaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // com.mob.tools.utils.ActivityTracker.Tracker
        public void onStarted(Activity activity) {
        }

        @Override // com.mob.tools.utils.ActivityTracker.Tracker
        public void onStopped(Activity activity) {
            if (a.this.a == null || activity.toString().equals(a.this.a.toString())) {
                a.this.b = 0L;
                a.this.a = null;
                com.mob.mobapm.core.b.e().a();
            }
        }
    }

    public static synchronized a b() {
        a aVar;
        synchronized (a.class) {
            if (c == null) {
                c = new a();
            }
            aVar = c;
        }
        return aVar;
    }

    public void a() {
        ActivityTracker.getInstance(MobSDK.getContext()).addTracker(new C0067a());
    }
}
