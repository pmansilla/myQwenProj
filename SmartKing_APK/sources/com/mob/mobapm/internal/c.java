package com.mob.mobapm.internal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class c extends b {
    private ArrayList<b> c = new ArrayList<>();

    public c a(b bVar) throws Throwable {
        this.c.add(bVar);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.mobapm.internal.b
    public InputStream a() throws Throwable {
        d dVar = new d();
        Iterator<b> it = this.c.iterator();
        while (it.hasNext()) {
            dVar.a(it.next().a());
        }
        return dVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.mobapm.internal.b
    public long b() throws Throwable {
        Iterator<b> it = this.c.iterator();
        long j = 0;
        while (it.hasNext()) {
            j += it.next().b();
        }
        return j;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<b> it = this.c.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        return sb.toString();
    }
}
