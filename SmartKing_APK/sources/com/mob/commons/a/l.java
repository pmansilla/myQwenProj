package com.mob.commons.a;

import android.os.Message;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: DvcvClt.java */
/* loaded from: classes.dex */
public class l extends d {
    private HashMap<String, Object> a;

    private HashMap<String, Object> a(String str, byte[] bArr) {
        try {
            return new Hashon().fromJson(Data.AES128Decode(str, bArr));
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "DvcvClt", "Decrypt error");
            return new HashMap<>();
        }
    }

    private void a(HashMap<String, Object> hashMap) {
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.dextvcd");
        try {
            byte[] a = a(DeviceHelper.getInstance(MobSDK.getContext()).getModel(), hashMap);
            FileChannel channel = new FileOutputStream(dataCacheFile).getChannel();
            channel.write(ByteBuffer.wrap(a));
            channel.force(true);
            channel.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "DvcvClt", th.getMessage());
        }
    }

    private byte[] a(String str, HashMap<String, Object> hashMap) {
        String fromHashMap = new Hashon().fromHashMap(hashMap);
        try {
            return Data.AES128Encode(str, fromHashMap);
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "DvcvClt", th.getMessage());
            return fromHashMap.getBytes();
        }
    }

    private void b(HashMap<String, Object> hashMap) {
        long H = com.mob.commons.i.H();
        long ah = com.mob.commons.b.ah() * 1000;
        if (H == 0) {
            H = com.mob.commons.b.a() + ah;
            com.mob.commons.i.k(H);
        }
        if (com.mob.commons.b.a() >= H) {
            c(hashMap);
        }
    }

    private void c(HashMap<String, Object> hashMap) {
        if (hashMap != null) {
            try {
                if (hashMap.isEmpty()) {
                    return;
                }
                long a = com.mob.commons.b.a();
                HashMap hashMap2 = new HashMap();
                hashMap2.putAll(hashMap);
                HashMap<String, Object> hashMap3 = new HashMap<>();
                hashMap3.put("type", "DEXTVARMT");
                hashMap3.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, hashMap2);
                hashMap3.put("datetime", Long.valueOf(a));
                com.mob.commons.c.a().a(a, hashMap3);
                this.a.clear();
                a(this.a);
                com.mob.commons.i.k(a + (com.mob.commons.b.ah() * 1000));
            } catch (Throwable th) {
                MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
            }
        }
    }

    private void h() {
        try {
            if (this.a == null) {
                this.a = j();
            }
            if (this.a == null) {
                this.a = new HashMap<>();
            }
            HashMap<String, Object> i = i();
            ArrayList arrayList = (ArrayList) this.a.get("list");
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            arrayList.add(i);
            this.a.put("list", arrayList);
            a(this.a);
            b(this.a);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    private HashMap<String, Object> i() {
        HashMap<String, Object> hashMap = new HashMap<>();
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        hashMap.putAll(deviceHelper.getCPUFreq());
        hashMap.put("photoCount", Integer.valueOf(deviceHelper.getAlbumCount()));
        hashMap.putAll(deviceHelper.getTraffic());
        hashMap.putAll(deviceHelper.getDeviceMemUsage());
        hashMap.put("createTime", Long.valueOf(com.mob.commons.b.a()));
        return hashMap;
    }

    private HashMap<String, Object> j() {
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.dextvcd");
        if (dataCacheFile.exists()) {
            try {
                FileChannel channel = new FileInputStream(dataCacheFile).getChannel();
                ByteBuffer allocate = ByteBuffer.allocate((int) channel.size());
                do {
                } while (channel.read(allocate) > 0);
                return a(DeviceHelper.getInstance(MobSDK.getContext()).getModel(), allocate.array());
            } catch (Throwable th) {
                MobLog.getInstance().d(th, "[%s] %s", "DvcvClt", "Read cache error");
            }
        }
        return new HashMap<>();
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.dvcv_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        if (message.what == 1 && b_()) {
            h();
            a(1, com.mob.commons.b.ag() * 1000);
        }
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.af();
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        b(1);
    }
}
