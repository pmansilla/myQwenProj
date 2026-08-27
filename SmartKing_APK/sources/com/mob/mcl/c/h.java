package com.mob.mcl.c;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;

/* compiled from: MlpClient.java */
/* loaded from: classes.dex */
public class h {
    a a;
    final f b;
    final AtomicLong c = new AtomicLong();

    public h(f fVar) {
        this.b = fVar;
    }

    public e a(g gVar) {
        if (gVar.c == 0) {
            gVar.c = this.c.incrementAndGet();
        }
        a aVar = this.a;
        if (aVar == null) {
            throw null;
        }
        e eVar = new e();
        synchronized (aVar.e) {
            aVar.e.put(eVar, Long.valueOf(gVar.c));
        }
        try {
            OutputStream outputStream = aVar.a.getOutputStream();
            ByteBuffer allocate = ByteBuffer.allocate(gVar.a + 17);
            allocate.put((byte) 1);
            allocate.putInt(gVar.a);
            allocate.putInt(gVar.b);
            allocate.putLong(gVar.c);
            String str = gVar.d;
            if (str != null) {
                allocate.put(str.getBytes(Charset.forName("UTF-8")));
            }
            outputStream.write(allocate.array());
            outputStream.flush();
            return eVar;
        } catch (Throwable th) {
            ((i) aVar.b).a(aVar, th);
            return null;
        }
    }
}
