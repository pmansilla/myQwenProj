package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.mapcore.util.he;
import com.amap.api.maps.AMapException;
import com.amap.location.common.model.AmapLoc;
import com.sun.mail.imap.IMAPStore;
import java.util.Hashtable;
import java.util.Map;
import org.json.JSONObject;

/* compiled from: OfflineInitHandlerAbstract.java */
/* loaded from: classes.dex */
public class br extends cn<String, bq> {
    public br(Context context, String str) {
        super(context, str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.cn
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public bq b(JSONObject jSONObject) throws AMapException {
        bq bqVar = new bq();
        try {
            String optString = jSONObject.optString("update", "");
            if (optString.equals(AmapLoc.RESULT_TYPE_GPS)) {
                bqVar.a(false);
            } else if (optString.equals(AmapLoc.RESULT_TYPE_WIFI_ONLY)) {
                bqVar.a(true);
            }
            bqVar.a(jSONObject.optString(IMAPStore.ID_VERSION, ""));
        } catch (Throwable th) {
            ic.c(th, "OfflineInitHandlerAbstract", "loadData parseJson");
        }
        return bqVar;
    }

    @Override // com.amap.api.mapcore.util.cn
    protected String a() {
        return "016";
    }

    @Override // com.amap.api.mapcore.util.cn
    protected JSONObject a(he.a aVar) {
        if (aVar == null || aVar.w == null) {
            return null;
        }
        return aVar.w.optJSONObject("016");
    }

    @Override // com.amap.api.mapcore.util.cn
    protected Map<String, String> b() {
        Hashtable hashtable = new Hashtable(16);
        hashtable.put("mapver", this.a);
        return hashtable;
    }
}
