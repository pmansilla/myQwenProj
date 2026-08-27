package com.mob.tools.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import com.mob.tools.MobLog;
import java.util.HashSet;

/* loaded from: classes2.dex */
public class ActivityTracker {
    private static ActivityTracker instance;
    private HashSet<Tracker> trackers = new HashSet<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface EachTracker {
        void each(Tracker tracker);
    }

    /* loaded from: classes2.dex */
    public interface Tracker {
        void onCreated(Activity activity, Bundle bundle);

        void onDestroyed(Activity activity);

        void onPaused(Activity activity);

        void onResumed(Activity activity);

        void onSaveInstanceState(Activity activity, Bundle bundle);

        void onStarted(Activity activity);

        void onStopped(Activity activity);
    }

    private ActivityTracker(Context context) {
        if (Build.VERSION.SDK_INT >= 14) {
            initLevel14(context);
        } else {
            init(context);
        }
    }

    private void eachTracker(EachTracker eachTracker) {
        Tracker[] trackerArr;
        try {
            synchronized (this.trackers) {
                trackerArr = (Tracker[]) this.trackers.toArray(new Tracker[this.trackers.size()]);
            }
            for (Tracker tracker : trackerArr) {
                if (tracker != null) {
                    eachTracker.each(tracker);
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    public static synchronized ActivityTracker getInstance(Context context) {
        ActivityTracker activityTracker;
        synchronized (ActivityTracker.class) {
            if (instance == null) {
                instance = new ActivityTracker(context);
            }
            activityTracker = instance;
        }
        return activityTracker;
    }

    private void init(Context context) {
        try {
            DeviceHelper.getInstance(context);
            Object currentActivityThread = DeviceHelper.currentActivityThread();
            final Object instanceField = ReflectHelper.getInstanceField(currentActivityThread, "mInstrumentation");
            ReflectHelper.setInstanceField(currentActivityThread, "mInstrumentation", new Instrumentation() { // from class: com.mob.tools.utils.ActivityTracker.2
                @Override // android.app.Instrumentation
                public void callActivityOnCreate(Activity activity, Bundle bundle) {
                    if (instanceField != null) {
                        try {
                            ReflectHelper.invokeInstanceMethod(instanceField, "callActivityOnCreate", activity, bundle);
                        } catch (Throwable unused) {
                        }
                    }
                    super.callActivityOnCreate(activity, bundle);
                    ActivityTracker.this.onCreated(activity, bundle);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnDestroy(Activity activity) {
                    if (instanceField != null) {
                        try {
                            ReflectHelper.invokeInstanceMethod(instanceField, "callActivityOnDestroy", activity);
                        } catch (Throwable unused) {
                        }
                    }
                    super.callActivityOnDestroy(activity);
                    ActivityTracker.this.onDestroyed(activity);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnPause(Activity activity) {
                    if (instanceField != null) {
                        try {
                            ReflectHelper.invokeInstanceMethod(instanceField, "callActivityOnPause", activity);
                        } catch (Throwable unused) {
                        }
                    }
                    super.callActivityOnPause(activity);
                    ActivityTracker.this.onPaused(activity);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnResume(Activity activity) {
                    if (instanceField != null) {
                        try {
                            ReflectHelper.invokeInstanceMethod(instanceField, "callActivityOnResume", activity);
                        } catch (Throwable unused) {
                        }
                    }
                    super.callActivityOnResume(activity);
                    ActivityTracker.this.onResumed(activity);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnSaveInstanceState(Activity activity, Bundle bundle) {
                    if (instanceField != null) {
                        try {
                            ReflectHelper.invokeInstanceMethod(instanceField, "callActivityOnSaveInstanceState", activity, bundle);
                        } catch (Throwable unused) {
                        }
                    }
                    super.callActivityOnSaveInstanceState(activity, bundle);
                    ActivityTracker.this.onSaveInstanceState(activity, bundle);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnStart(Activity activity) {
                    if (instanceField != null) {
                        try {
                            ReflectHelper.invokeInstanceMethod(instanceField, "callActivityOnStart", activity);
                        } catch (Throwable unused) {
                        }
                    }
                    super.callActivityOnStart(activity);
                    ActivityTracker.this.onStarted(activity);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnStop(Activity activity) {
                    if (instanceField != null) {
                        try {
                            ReflectHelper.invokeInstanceMethod(instanceField, "callActivityOnStop", activity);
                        } catch (Throwable unused) {
                        }
                    }
                    super.callActivityOnStop(activity);
                    ActivityTracker.this.onStopped(activity);
                }
            });
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    private void initLevel14(Context context) {
        try {
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { // from class: com.mob.tools.utils.ActivityTracker.1
                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityCreated(Activity activity, Bundle bundle) {
                    ActivityTracker.this.onCreated(activity, bundle);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityDestroyed(Activity activity) {
                    ActivityTracker.this.onDestroyed(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityPaused(Activity activity) {
                    ActivityTracker.this.onPaused(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityResumed(Activity activity) {
                    ActivityTracker.this.onResumed(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                    ActivityTracker.this.onSaveInstanceState(activity, bundle);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStarted(Activity activity) {
                    ActivityTracker.this.onStarted(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStopped(Activity activity) {
                    ActivityTracker.this.onStopped(activity);
                }
            });
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCreated(final Activity activity, final Bundle bundle) {
        eachTracker(new EachTracker() { // from class: com.mob.tools.utils.ActivityTracker.3
            @Override // com.mob.tools.utils.ActivityTracker.EachTracker
            public void each(Tracker tracker) {
                tracker.onCreated(activity, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDestroyed(final Activity activity) {
        eachTracker(new EachTracker() { // from class: com.mob.tools.utils.ActivityTracker.8
            @Override // com.mob.tools.utils.ActivityTracker.EachTracker
            public void each(Tracker tracker) {
                tracker.onDestroyed(activity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPaused(final Activity activity) {
        eachTracker(new EachTracker() { // from class: com.mob.tools.utils.ActivityTracker.6
            @Override // com.mob.tools.utils.ActivityTracker.EachTracker
            public void each(Tracker tracker) {
                tracker.onPaused(activity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onResumed(final Activity activity) {
        eachTracker(new EachTracker() { // from class: com.mob.tools.utils.ActivityTracker.5
            @Override // com.mob.tools.utils.ActivityTracker.EachTracker
            public void each(Tracker tracker) {
                tracker.onResumed(activity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSaveInstanceState(final Activity activity, final Bundle bundle) {
        eachTracker(new EachTracker() { // from class: com.mob.tools.utils.ActivityTracker.9
            @Override // com.mob.tools.utils.ActivityTracker.EachTracker
            public void each(Tracker tracker) {
                tracker.onSaveInstanceState(activity, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStarted(final Activity activity) {
        eachTracker(new EachTracker() { // from class: com.mob.tools.utils.ActivityTracker.4
            @Override // com.mob.tools.utils.ActivityTracker.EachTracker
            public void each(Tracker tracker) {
                tracker.onStarted(activity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStopped(final Activity activity) {
        eachTracker(new EachTracker() { // from class: com.mob.tools.utils.ActivityTracker.7
            @Override // com.mob.tools.utils.ActivityTracker.EachTracker
            public void each(Tracker tracker) {
                tracker.onStopped(activity);
            }
        });
    }

    public void addTracker(Tracker tracker) {
        synchronized (this.trackers) {
            this.trackers.add(tracker);
        }
    }

    public void removeTracker(Tracker tracker) {
        synchronized (this.trackers) {
            this.trackers.remove(tracker);
        }
    }
}
