package com.czw.smartkit.util;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ActivityManager {
    private static ActivityManager instance = new ActivityManager();
    private List<Activity> activityList = new ArrayList();

    public static ActivityManager getInstance() {
        return instance;
    }

    public void close(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.finish();
    }

    public void closeAll() {
        if (this.activityList.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.activityList.size(); i++) {
            close(this.activityList.get(i));
        }
        this.activityList.clear();
    }

    public void putActivity(Activity activity) {
        if (this.activityList != null) {
            this.activityList.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (this.activityList != null) {
            this.activityList.remove(activity);
        }
    }
}
