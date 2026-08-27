package com.mob.mcl.b;

import com.mob.tools.network.HttpConnection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: TcpConnection.java */
/* loaded from: classes.dex */
public class c implements HttpConnection {
    private HashMap<String, Object> a;
    private boolean b;

    public c(HashMap<String, Object> hashMap) {
        this.a = hashMap;
        this.b = false;
    }

    public c(HashMap<String, Object> hashMap, boolean z) {
        this.a = hashMap;
        this.b = z;
    }

    private byte[] b() {
        String str = "{}";
        HashMap<String, Object> hashMap = this.a;
        if (hashMap != null && hashMap.containsKey("body")) {
            str = (String) this.a.get("body");
        }
        return str.getBytes();
    }

    public HashMap<String, Object> a() {
        return this.a;
    }

    @Override // com.mob.tools.network.HttpConnection
    public InputStream getErrorStream() throws IOException {
        return new ByteArrayInputStream(b());
    }

    @Override // com.mob.tools.network.HttpConnection
    public Map<String, List<String>> getHeaderFields() throws IOException {
        HashMap hashMap;
        HashMap hashMap2 = new HashMap();
        String valueOf = String.valueOf(this.b);
        ArrayList arrayList = new ArrayList();
        arrayList.add(valueOf);
        hashMap2.put("apc", arrayList);
        HashMap<String, Object> hashMap3 = this.a;
        if (hashMap3 != null && hashMap3.containsKey("headers") && (hashMap = (HashMap) this.a.get("headers")) != null) {
            for (Map.Entry entry : hashMap.entrySet()) {
                if (entry.getValue() instanceof String) {
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add((String) entry.getValue());
                    hashMap2.put(entry.getKey(), arrayList2);
                } else if (entry.getValue() instanceof List) {
                    hashMap2.put(entry.getKey(), (List) entry.getValue());
                }
            }
        }
        return hashMap2;
    }

    @Override // com.mob.tools.network.HttpConnection
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(b());
    }

    @Override // com.mob.tools.network.HttpConnection
    public int getResponseCode() throws IOException {
        HashMap<String, Object> hashMap = this.a;
        if (hashMap != null) {
            return ((Integer) hashMap.get("code")).intValue();
        }
        return -1;
    }
}
