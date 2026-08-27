package me.panpf.sketch.request;

/* loaded from: classes2.dex */
public enum RequestLevel {
    NET(2),
    LOCAL(1),
    MEMORY(0);

    private int level;

    RequestLevel(int i) {
        this.level = i;
    }

    public int getLevel() {
        return this.level;
    }
}
