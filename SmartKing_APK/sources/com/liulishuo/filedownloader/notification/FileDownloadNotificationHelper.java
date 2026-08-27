package com.liulishuo.filedownloader.notification;

import android.util.SparseArray;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;

/* loaded from: classes.dex */
public class FileDownloadNotificationHelper<T extends BaseNotificationItem> {
    private final SparseArray<T> notificationArray = new SparseArray<>();

    public void add(T t) {
        this.notificationArray.remove(t.getId());
        this.notificationArray.put(t.getId(), t);
    }

    public void cancel(int i) {
        T remove = remove(i);
        if (remove == null) {
            return;
        }
        remove.cancel();
    }

    public void clear() {
        SparseArray<T> clone = this.notificationArray.clone();
        this.notificationArray.clear();
        for (int i = 0; i < clone.size(); i++) {
            clone.get(clone.keyAt(i)).cancel();
        }
    }

    public boolean contains(int i) {
        return get(i) != null;
    }

    public T get(int i) {
        return this.notificationArray.get(i);
    }

    public T remove(int i) {
        T t = get(i);
        if (t == null) {
            return null;
        }
        this.notificationArray.remove(i);
        return t;
    }

    public void showIndeterminate(int i, int i2) {
        T t = get(i);
        if (t == null) {
            return;
        }
        t.updateStatus(i2);
        t.show(false);
    }

    public void showProgress(int i, int i2, int i3) {
        T t = get(i);
        if (t == null) {
            return;
        }
        t.updateStatus(3);
        t.update(i2, i3);
    }
}
