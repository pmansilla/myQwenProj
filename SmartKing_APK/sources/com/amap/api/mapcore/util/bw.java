package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.mapcore.util.he;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: OfflineUpdateCityHandlerAbstract.java */
/* loaded from: classes.dex */
public class bw extends cn<String, List<OfflineMapProvince>> {
    private Context d;

    public bw(Context context, String str) {
        super(context, str);
    }

    @Override // com.amap.api.mapcore.util.cn
    protected String a() {
        return "015";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.cn
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public List<OfflineMapProvince> b(JSONObject jSONObject) throws AMapException {
        try {
            if (this.d != null) {
                cm.c(jSONObject.toString(), this.d);
            }
        } catch (Throwable th) {
            ic.c(th, "OfflineUpdateCityHandlerAbstract", "loadData jsonInit");
            th.printStackTrace();
        }
        try {
            if (this.d != null) {
                return cm.a(jSONObject, this.d);
            }
            return null;
        } catch (JSONException e) {
            ic.c(e, "OfflineUpdateCityHandlerAbstract", "loadData parseJson");
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.cn
    protected JSONObject a(he.a aVar) {
        if (aVar == null || aVar.w == null) {
            return null;
        }
        JSONObject optJSONObject = aVar.w.optJSONObject("015");
        if (!optJSONObject.has("result")) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("result", new JSONObject().put("offlinemap_with_province_vfour", optJSONObject));
                return jSONObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return optJSONObject;
    }

    public void a(Context context) {
        this.d = context;
    }

    @Override // com.amap.api.mapcore.util.cn
    protected Map<String, String> b() {
        Hashtable hashtable = new Hashtable(16);
        hashtable.put("mapver", this.a);
        return hashtable;
    }
}
