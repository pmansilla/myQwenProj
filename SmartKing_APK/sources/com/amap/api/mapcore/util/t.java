package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import com.amap.api.mapcore.util.he;
import com.amap.api.mapcore.util.u;
import com.amap.api.maps.MapsInitializer;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import java.lang.ref.WeakReference;
import org.json.JSONObject;

/* compiled from: AuthTask.java */
/* loaded from: classes.dex */
public class t extends Thread {
    WeakReference<ad> a;
    private Context b;

    public t(Context context, ad adVar) {
        this.a = null;
        this.b = context;
        this.a = new WeakReference<>(adVar);
    }

    private void a(he.a aVar) {
        try {
            he.a.C0023a c0023a = aVar.x;
            if (c0023a != null) {
                fo.a(this.b, "maploc", "ue", Boolean.valueOf(c0023a.a));
                JSONObject jSONObject = c0023a.c;
                int optInt = jSONObject.optInt("fn", 1000);
                int optInt2 = jSONObject.optInt("mpn", 0);
                if (optInt2 > 500) {
                    optInt2 = 500;
                }
                if (optInt2 < 30) {
                    optInt2 = 30;
                }
                jg.a(optInt, he.a(jSONObject.optString("igu"), false));
                fo.a(this.b, "maploc", "opn", Integer.valueOf(optInt2));
            }
        } catch (Throwable th) {
            ic.c(th, "AuthUtil", "loadConfigDataUploadException");
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        JSONObject optJSONObject;
        JSONObject optJSONObject2;
        JSONObject optJSONObject3;
        ho e;
        JSONObject optJSONObject4;
        try {
            if (MapsInitializer.getNetWorkEnable()) {
                hk.a().a(this.b);
                he.a a = he.a(this.b, fr.e(), "14S;11K;001;14M;14L;151;14Z;154;156;15C;15S", null);
                if (he.a != 1 && a != null && this.a != null && this.a.get() != null) {
                    Message obtainMessage = this.a.get().getMainHandler().obtainMessage();
                    obtainMessage.what = 2;
                    if (a.a != null) {
                        obtainMessage.obj = a.a;
                    }
                    this.a.get().getMainHandler().sendMessage(obtainMessage);
                }
                if (a != null && a.w != null && (optJSONObject4 = a.w.optJSONObject("154")) != null && he.a(optJSONObject4.getString("able"), true)) {
                    String optString = optJSONObject4.optString("mc");
                    String optString2 = optJSONObject4.optString("si");
                    if (!TextUtils.isEmpty(optString)) {
                        fh.a(this.b, "approval_number", "mc", (Object) optString);
                    }
                    if (!TextUtils.isEmpty(optString2)) {
                        fh.a(this.b, "approval_number", "si", (Object) optString2);
                    }
                }
                if (a != null && a.x != null && (e = fr.e()) != null) {
                    e.a(a.x.a);
                }
                if (MapsInitializer.isDownloadCoordinateConvertLibrary() && a != null && a.B != null) {
                    hh hhVar = new hh(this.b, "3dmap", a.B.a, a.B.b);
                    hhVar.a(a.a());
                    hhVar.a();
                }
                if (a != null) {
                    a(a);
                }
                if (a != null) {
                    try {
                        if (a.w != null && (optJSONObject = a.w.optJSONObject("14M")) != null && optJSONObject.has("able") && he.a(optJSONObject.getString("able"), true)) {
                            if (System.currentTimeMillis() - fh.a(this.b, "Map3DCache", "time", (Long) 0L).longValue() > (optJSONObject.has("time") ? Math.max(60, optJSONObject.getInt("time")) : 2592000) * 1000 && this.a != null && this.a.get() != null) {
                                this.a.get().b();
                            }
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                if (a != null && a.w != null) {
                    try {
                        JSONObject optJSONObject5 = a.w.optJSONObject("14L");
                        if (optJSONObject5 != null && optJSONObject5.has("able")) {
                            boolean a2 = he.a(optJSONObject5.getString("able"), false);
                            if (this.a != null && this.a.get() != null) {
                                this.a.get().i(!a2);
                            }
                        }
                    } catch (Throwable th2) {
                        th2.printStackTrace();
                    }
                }
                if (a != null && a.w != null && (optJSONObject3 = a.w.optJSONObject("156")) != null) {
                    fb.a(he.a(optJSONObject3.optString("able"), false));
                }
                if (a != null && a.w != null && (optJSONObject2 = a.w.optJSONObject("15C")) != null) {
                    final boolean a3 = he.a(optJSONObject2.optString("able"), false);
                    final String optString3 = optJSONObject2.optString("logo_day_url");
                    final String optString4 = optJSONObject2.optString("logo_day_md5");
                    final String optString5 = optJSONObject2.optString("logo_night_url");
                    final String optString6 = optJSONObject2.optString("logo_night_md5");
                    fq.a().a(new Runnable() { // from class: com.amap.api.mapcore.util.t.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!TextUtils.isEmpty(optString4) && !TextUtils.isEmpty(optString3)) {
                                boolean z = a3;
                                String str = AMapEngineUtils.LOGO_CUSTOM_ICON_DAY_NAME;
                                String str2 = optString3;
                                String str3 = optString4;
                                if (z) {
                                    u.d dVar = new u.d(str2, str3, str);
                                    dVar.a("amap_web_logo", "md5_day");
                                    new u(t.this.b, dVar, fr.e()).a();
                                }
                                if (t.this.a != null && t.this.a.get() != null) {
                                    t.this.a.get().a(str, z, 0);
                                }
                            }
                            if (TextUtils.isEmpty(optString6) || TextUtils.isEmpty(optString5)) {
                                return;
                            }
                            boolean z2 = a3;
                            String str4 = AMapEngineUtils.LOGO_CUSTOM_ICON_NIGHT_NAME;
                            String str5 = optString5;
                            String str6 = optString6;
                            if (z2) {
                                u.d dVar2 = new u.d(str5, str6, str4);
                                dVar2.a("amap_web_logo", "md5_night");
                                new u(t.this.b, dVar2, fr.e()).a();
                            }
                            if (t.this.a == null || t.this.a.get() == null) {
                                return;
                            }
                            t.this.a.get().a(str4, z2, 1);
                        }
                    });
                }
                ic.a(this.b, fr.e());
                interrupt();
                if (this.a == null || this.a.get() == null) {
                    return;
                }
                this.a.get().setRunLowFrame(false);
            }
        } catch (Throwable th3) {
            interrupt();
            ic.c(th3, "AMapDelegateImpGLSurfaceView", "mVerfy");
            th3.printStackTrace();
        }
    }
}
