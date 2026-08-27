package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.amap.location.common.model.AmapLoc;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.sun.mail.imap.IMAPStore;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import me.panpf.sketch.uri.FileUriModel;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: UpdateItem.java */
@ih(a = "update_item", b = true)
/* loaded from: classes.dex */
public class bz extends cc {
    private String n = "";
    private Context o;

    public bz() {
    }

    public bz(OfflineMapCity offlineMapCity, Context context) {
        this.o = context;
        this.a = offlineMapCity.getCity();
        this.c = offlineMapCity.getAdcode();
        this.b = offlineMapCity.getUrl();
        this.g = offlineMapCity.getSize();
        this.e = offlineMapCity.getVersion();
        this.k = offlineMapCity.getCode();
        this.i = 0;
        this.l = offlineMapCity.getState();
        this.j = offlineMapCity.getcompleteCode();
        this.m = offlineMapCity.getPinyin();
        a();
    }

    public bz(OfflineMapProvince offlineMapProvince, Context context) {
        this.o = context;
        this.a = offlineMapProvince.getProvinceName();
        this.c = offlineMapProvince.getProvinceCode();
        this.b = offlineMapProvince.getUrl();
        this.g = offlineMapProvince.getSize();
        this.e = offlineMapProvince.getVersion();
        this.i = 1;
        this.l = offlineMapProvince.getState();
        this.j = offlineMapProvince.getcompleteCode();
        this.m = offlineMapProvince.getPinyin();
        a();
    }

    public static String a(JSONObject jSONObject, String str) throws JSONException {
        return (jSONObject == null || !jSONObject.has(str) || "[]".equals(jSONObject.getString(str))) ? "" : jSONObject.optString(str).trim();
    }

    protected void a() {
        this.d = fr.c(this.o) + this.m + ".zip.tmp";
    }

    public void a(String str) {
        this.n = str;
    }

    public String b() {
        return this.n;
    }

    public void b(String str) {
        JSONObject jSONObject;
        if (str != null) {
            try {
                if ("".equals(str) || (jSONObject = new JSONObject(str).getJSONObject(AmapLoc.TYPE_OFFLINE_CELL)) == null) {
                    return;
                }
                this.a = jSONObject.optString("title");
                this.c = jSONObject.optString("code");
                this.b = jSONObject.optString(FileDownloadModel.URL);
                this.d = jSONObject.optString("fileName");
                this.f = jSONObject.optLong("lLocalLength");
                this.g = jSONObject.optLong("lRemoteLength");
                this.l = jSONObject.optInt("mState");
                this.e = jSONObject.optString(IMAPStore.ID_VERSION);
                this.h = jSONObject.optString("localPath");
                this.n = jSONObject.optString("vMapFileNames");
                this.i = jSONObject.optInt("isSheng");
                this.j = jSONObject.optInt("mCompleteCode");
                this.k = jSONObject.optString("mCityCode");
                this.m = a(jSONObject, "pinyin");
                if ("".equals(this.m)) {
                    String substring = this.b.substring(this.b.lastIndexOf(FileUriModel.SCHEME) + 1);
                    this.m = substring.substring(0, substring.lastIndexOf("."));
                }
            } catch (Throwable th) {
                ic.c(th, "UpdateItem", "readFileToJSONObject");
                th.printStackTrace();
            }
        }
    }

    public void c() {
        OutputStreamWriter outputStreamWriter;
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("title", this.a);
            jSONObject2.put("code", this.c);
            jSONObject2.put(FileDownloadModel.URL, this.b);
            jSONObject2.put("fileName", this.d);
            jSONObject2.put("lLocalLength", this.f);
            jSONObject2.put("lRemoteLength", this.g);
            jSONObject2.put("mState", this.l);
            jSONObject2.put(IMAPStore.ID_VERSION, this.e);
            jSONObject2.put("localPath", this.h);
            if (this.n != null) {
                jSONObject2.put("vMapFileNames", this.n);
            }
            jSONObject2.put("isSheng", this.i);
            jSONObject2.put("mCompleteCode", this.j);
            jSONObject2.put("mCityCode", this.k);
            jSONObject2.put("pinyin", this.m);
            jSONObject.put(AmapLoc.TYPE_OFFLINE_CELL, jSONObject2);
            File file = new File(this.d + ".dt");
            file.delete();
            OutputStreamWriter outputStreamWriter2 = null;
            try {
                try {
                    outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, true), "utf-8");
                } catch (Throwable th) {
                    th = th;
                }
            } catch (IOException e) {
                e = e;
            }
            try {
                outputStreamWriter.write(jSONObject.toString());
                try {
                    outputStreamWriter.close();
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                }
            } catch (IOException e3) {
                e = e3;
                outputStreamWriter2 = outputStreamWriter;
                ic.c(e, "UpdateItem", "saveJSONObjectToFile");
                e.printStackTrace();
                if (outputStreamWriter2 != null) {
                    try {
                        outputStreamWriter2.close();
                    } catch (IOException e4) {
                        e = e4;
                        e.printStackTrace();
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                outputStreamWriter2 = outputStreamWriter;
                if (outputStreamWriter2 != null) {
                    try {
                        outputStreamWriter2.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            ic.c(th3, "UpdateItem", "saveJSONObjectToFile parseJson");
            th3.printStackTrace();
        }
    }
}
