package com.czw.smartkit.observerModule;

import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class TargetChangeHelper {
    private static final TargetChangeHelper ourInstance = new TargetChangeHelper();
    private HashSet<TargetListener> targetListenerHashSet = new HashSet<>();

    /* loaded from: classes.dex */
    public interface TargetListener {
        void currentTarget(String str);
    }

    private TargetChangeHelper() {
    }

    public static TargetChangeHelper getInstance() {
        return ourInstance;
    }

    public void notifyTargetChange(String str) {
        Iterator<TargetListener> it = this.targetListenerHashSet.iterator();
        while (it.hasNext()) {
            it.next().currentTarget(str);
        }
    }

    public void registerListener(TargetListener targetListener) {
        if (this.targetListenerHashSet.contains(targetListener)) {
            return;
        }
        this.targetListenerHashSet.add(targetListener);
    }

    public void unRegisterListener(TargetListener targetListener) {
        if (this.targetListenerHashSet.contains(targetListener)) {
            this.targetListenerHashSet.remove(targetListener);
        }
    }
}
