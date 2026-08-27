package com.mob.mobapm.proxy.okhttp2;

import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import okio.Buffer;
import okio.BufferedSource;

/* loaded from: classes.dex */
public class d {
    public static ResponseBody a(ResponseBody responseBody, long j) throws IOException {
        try {
            BufferedSource source = responseBody.source();
            source.request(j);
            Buffer clone = source.buffer().clone();
            if (clone.size() > j) {
                Buffer buffer = new Buffer();
                buffer.write(clone, j);
                clone.clear();
                clone = buffer;
            }
            return ResponseBody.create(responseBody.contentType(), clone.size(), clone);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: error:" + th, new Object[0]);
            return null;
        }
    }
}
