package me.panpf.sketch.util;

import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class KeyCounter {
    private AtomicInteger number = new AtomicInteger();

    public int getKey() {
        return this.number.get();
    }

    public void refresh() {
        if (this.number.get() == Integer.MAX_VALUE) {
            this.number.set(0);
        } else {
            this.number.addAndGet(1);
        }
    }
}
