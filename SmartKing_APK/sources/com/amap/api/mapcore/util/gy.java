package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.autonavi.amap.mapcore.AeUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: TraceHandlerAbstract.java */
/* loaded from: classes.dex */
public class gy extends gw<List<TraceLocation>, List<LatLng>> implements Runnable {
    private List<TraceLocation> h;
    private Handler i;
    private int j;
    private int k;
    private String l;

    public gy(Context context, Handler handler, List<TraceLocation> list, int i, String str, int i2, int i3) {
        super(context, list);
        this.i = null;
        this.j = 0;
        this.k = 0;
        this.h = list;
        this.i = handler;
        this.k = i2;
        this.j = i3;
        this.l = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.gv
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public List<LatLng> b(String str) throws gu {
        JSONObject jSONObject;
        JSONArray optJSONArray;
        ArrayList arrayList = new ArrayList();
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (jSONObject.has(AeUtil.ROOT_DATA_PATH_OLD_NAME) && (optJSONArray = jSONObject.optJSONObject(AeUtil.ROOT_DATA_PATH_OLD_NAME).optJSONArray("points")) != null && optJSONArray.length() != 0) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                arrayList.add(new LatLng(Double.parseDouble(optJSONObject.optString("y")), Double.parseDouble(optJSONObject.optString("x"))));
            }
            return arrayList;
        }
        return arrayList;
    }

    @Override // com.amap.api.mapcore.util.gw
    protected String e() {
        JSONArray jSONArray = new JSONArray();
        long j = 0;
        for (int i = 0; i < this.h.size(); i++) {
            TraceLocation traceLocation = this.h.get(i);
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("x", traceLocation.getLongitude());
                jSONObject.put("y", traceLocation.getLatitude());
                jSONObject.put("ag", (int) traceLocation.getBearing());
                long time = traceLocation.getTime();
                if (i == 0) {
                    if (time == 0) {
                        time = (System.currentTimeMillis() - 10000) / 1000;
                    }
                    jSONObject.put("tm", time / 1000);
                } else {
                    if (time != 0) {
                        long j2 = time - j;
                        if (j2 >= 1000) {
                            jSONObject.put("tm", j2 / 1000);
                        }
                    }
                    jSONObject.put("tm", 1);
                }
                j = time;
                jSONObject.put("sp", (int) traceLocation.getSpeed());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jSONArray.put(jSONObject);
        }
        this.g = getURL() + "&" + jSONArray.toString();
        return jSONArray.toString();
    }

    @Override // com.amap.api.mapcore.util.ix
    public String getURL() {
        String str = "key=" + hd.f(this.f);
        String a = hg.a();
        return "http://restapi.amap.com/v4/grasproad/driving?" + str + ("&ts=" + a) + ("&scode=" + hg.a(this.f, a, str));
    }

    @Override // java.lang.Runnable
    public void run() {
        new ArrayList();
        try {
            try {
                ha.a().a(this.l, this.j, a());
                ha.a().a(this.l).a(this.i);
            } catch (gu e) {
                ha.a().a(this.i, this.k, e.a());
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
