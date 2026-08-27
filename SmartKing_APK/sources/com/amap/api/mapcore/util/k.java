package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.mapcore.util.eh;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.MyTrafficStyle;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.autonavi.amap.mapcore.FileUtil;
import com.autonavi.amap.mapcore.MapConfig;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.GZIPInputStream;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: AMapCustomStyleManager.java */
/* loaded from: classes.dex */
public class k implements eh.a {
    private ad a;
    private CustomMapStyleOptions b;
    private Context h;
    private boolean n;
    private boolean o;
    private eh s;
    private eh t;
    private boolean c = false;
    private boolean d = false;
    private boolean e = false;
    private boolean f = false;
    private int g = 1;
    private byte[] i = null;
    private byte[] j = null;
    private byte[] k = null;
    private byte[] l = null;
    private byte[] m = null;
    private boolean p = false;
    private boolean q = false;
    private boolean r = false;
    private byte[] u = null;
    private byte[] v = null;
    private boolean w = false;
    private MyTrafficStyle x = new MyTrafficStyle();

    public k(ad adVar, Context context) {
        this.n = false;
        this.o = false;
        this.a = adVar;
        this.h = context;
        this.n = false;
        this.o = false;
    }

    private void a(String str, boolean z) {
        boolean z2;
        int a = !TextUtils.isEmpty(str) ? eo.a(str) : Integer.MIN_VALUE;
        if (this.a == null || this.a.a() == null) {
            return;
        }
        if (this.k == null) {
            this.k = FileUtil.readFileContentsFromAssets(this.h, AMapEngineUtils.MAP_MAP_ASSETS_NAME + File.separator + AMapEngineUtils.MAP_MAP_ASSETS_BACKGROUND_NAME);
        }
        if (this.k != null) {
            if (!z) {
                a = 0;
            } else if (a == Integer.MIN_VALUE) {
                z2 = true;
                this.a.a().setBackgroundTexture(this.g, fr.a((byte[]) this.k.clone(), 0, a, z2));
            }
            z2 = false;
            this.a.a().setBackgroundTexture(this.g, fr.a((byte[]) this.k.clone(), 0, a, z2));
        }
    }

    public static byte[] a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(bArr));
            byte[] bArr2 = new byte[256];
            while (true) {
                int read = gZIPInputStream.read(bArr2);
                if (read < 0) {
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr2, 0, read);
            }
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private void b(byte[] bArr) {
        el a;
        JSONObject optJSONObject;
        if (bArr == null || (a = eo.a(bArr)) == null || a.a() == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(a.a());
            JSONObject optJSONObject2 = jSONObject.optJSONObject("bg");
            String str = null;
            boolean z = true;
            if (optJSONObject2 != null) {
                z = optJSONObject2.optBoolean("visible", true);
                str = optJSONObject2.optString("lineColor", null);
            }
            a(str, z);
            JSONObject optJSONObject3 = jSONObject.optJSONObject("traffic");
            if (optJSONObject3 == null || (optJSONObject = optJSONObject3.optJSONObject("multiFillColors")) == null) {
                return;
            }
            int a2 = eo.a(optJSONObject.optString("smooth"));
            int a3 = eo.a(optJSONObject.optString("slow"));
            int a4 = eo.a(optJSONObject.optString("congested"));
            int a5 = eo.a(optJSONObject.optString("seriousCongested"));
            this.x.setSmoothColor(a2);
            this.x.setSlowColor(a3);
            this.x.setCongestedColor(a4);
            this.x.setSeriousCongestedColor(a5);
            if (this.a == null || this.a.a() == null) {
                return;
            }
            this.a.a().setTrafficStyle(this.g, this.x.getSmoothColor(), this.x.getSlowColor(), this.x.getCongestedColor(), this.x.getSeriousCongestedColor(), true);
        } catch (Throwable th) {
            ic.c(th, "AMapCustomStyleManager", "setExtraStyle");
        }
    }

    private boolean c(byte[] bArr) {
        el a;
        if (bArr == null) {
            return true;
        }
        try {
            if (bArr.length < 10240 && (a = eo.a((byte[]) bArr.clone())) != null && a.a() != null) {
                try {
                    new JSONObject(a.a());
                    return false;
                } catch (JSONException e) {
                    ic.c(e, "AMapCustomStyleManager", "checkData");
                }
            }
        } catch (Throwable th) {
            ic.c(th, "AMapCustomStyleManager", "checkData");
        }
        return true;
    }

    private void f() {
        if (this.a != null && this.a.a() != null && this.k != null) {
            this.a.a().setBackgroundTexture(this.g, this.k);
        }
        if (this.a != null && this.a.a() != null) {
            this.a.a().setTrafficStyle(this.g, 0, 0, 0, 0, false);
        }
        this.r = false;
    }

    private void g() {
        if (this.j == null) {
            this.j = a(FileUtil.readFileContentsFromAssets(this.h, AMapEngineUtils.MAP_MAP_ASSETS_NAME + File.separator + AMapEngineUtils.MAP_MAP_ASSETS_STYLE_DATA));
        }
        byte[] bArr = this.i;
        this.a.a().setCustomStyleData(this.g, this.j, this.i);
        this.q = false;
    }

    private void h() {
        if (this.p) {
            if (this.l == null) {
                this.l = FileUtil.readFileContentsFromAssets(this.h, AMapEngineUtils.MAP_MAP_ASSETS_NAME + File.separator + AMapEngineUtils.MAP_MAP_ASSETS_ICON_5_NAME_FOR_CUSTOM);
            }
            this.p = false;
            this.a.a().setCustomStyleTexture(this.g, this.l);
        }
    }

    private void i() {
        if (this.b != null) {
            this.b.setStyleId(null);
            this.b.setStyleDataPath(null);
            this.b.setStyleData(null);
            this.b.setStyleTexturePath(null);
            this.b.setStyleTextureData(null);
            this.b.setStyleExtraData(null);
            this.b.setStyleExtraPath(null);
        }
    }

    public void a() {
        if (this.b == null || this.o) {
            return;
        }
        try {
            MapConfig mapConfig = this.a.getMapConfig();
            if (mapConfig == null) {
                return;
            }
            synchronized (this) {
                if (mapConfig.isHideLogoEnable() && this.a != null && this.a.h() != null) {
                    if (this.a.h().isLogoEnable()) {
                        if (!this.b.isEnable()) {
                            this.a.h().setLogoEnable(true);
                        } else if (this.q) {
                            this.a.h().setLogoEnable(false);
                        }
                    } else if (!this.q) {
                        this.a.h().setLogoEnable(true);
                    }
                }
                if (this.c) {
                    if (!this.b.isEnable()) {
                        this.a.a().setNativeMapModeAndStyle(this.g, mapConfig.getMapStyleMode(), mapConfig.getMapStyleTime(), mapConfig.getMapStyleState(), false, false, null);
                        this.q = false;
                        if (mapConfig.isCustomStyleEnable()) {
                            if (mapConfig.getMapStyleMode() == 0 && mapConfig.getMapStyleTime() == 0 && mapConfig.getMapStyleState() == 0) {
                                g();
                            }
                            h();
                            if (this.r) {
                                f();
                            }
                            mapConfig.setCustomStyleEnable(false);
                        }
                        this.c = false;
                        return;
                    }
                    this.a.a().setNativeMapModeAndStyle(this.g, 0, 0, 0, false, false, null);
                    mapConfig.setCustomStyleEnable(true);
                    this.c = false;
                }
                if (this.e) {
                    String styleTexturePath = this.b.getStyleTexturePath();
                    if (this.b.getStyleTextureData() == null && !TextUtils.isEmpty(styleTexturePath)) {
                        this.b.setStyleTextureData(FileUtil.readFileContents(styleTexturePath));
                    }
                    if (this.b.getStyleTextureData() != null) {
                        this.w = true;
                        if (mapConfig.isProFunctionAuthEnable()) {
                            this.p = true;
                            this.a.a().setCustomStyleTexture(this.g, this.b.getStyleTextureData());
                            mapConfig.setUseProFunction(true);
                        } else {
                            h();
                        }
                    } else {
                        h();
                        this.w = false;
                    }
                    this.e = false;
                }
                if (this.d) {
                    String styleDataPath = this.b.getStyleDataPath();
                    if (this.b.getStyleData() == null && !TextUtils.isEmpty(styleDataPath)) {
                        this.b.setStyleData(FileUtil.readFileContents(styleDataPath));
                    }
                    if (this.b.getStyleData() == null && this.u == null) {
                        if (this.q) {
                            this.c = true;
                            this.b.setEnable(false);
                        }
                        this.d = false;
                    }
                    if (this.m == null) {
                        this.m = a(FileUtil.readFileContentsFromAssets(this.h, AMapEngineUtils.MAP_MAP_ASSETS_NAME + File.separator + AMapEngineUtils.MAP_MAP_ASSETS_STYLE_DATA_0_FOR_TEXTURE));
                    }
                    byte[] styleData = this.u != null ? this.u : this.b.getStyleData();
                    if (c(styleData)) {
                        this.a.a().setCustomStyleData(this.g, styleData, this.m);
                        this.q = true;
                        if (this.a != null) {
                            this.a.resetRenderTime();
                        }
                    } else {
                        et.a();
                    }
                    this.d = false;
                }
                if (this.f) {
                    String styleExtraPath = this.b.getStyleExtraPath();
                    if (this.b.getStyleExtraData() == null && !TextUtils.isEmpty(styleExtraPath)) {
                        this.b.setStyleExtraData(FileUtil.readFileContents(styleExtraPath));
                    }
                    if (this.b.getStyleExtraData() != null || this.v != null) {
                        byte[] styleExtraData = this.v != null ? this.v : this.b.getStyleExtraData();
                        if (styleExtraData != null) {
                            b(styleExtraData);
                            this.r = true;
                        }
                    }
                    this.f = false;
                }
            }
        } catch (Throwable th) {
            ic.c(th, "AMapCustomStyleManager", "updateStyle");
        }
    }

    public void a(CustomMapStyleOptions customMapStyleOptions) {
        if (this.b == null || customMapStyleOptions == null) {
            return;
        }
        synchronized (this) {
            if (!this.n) {
                this.n = true;
                if (this.b.isEnable()) {
                    this.c = true;
                }
            }
            if (this.b.isEnable() != customMapStyleOptions.isEnable()) {
                this.b.setEnable(customMapStyleOptions.isEnable());
                this.c = true;
            }
            if (this.b.isEnable()) {
                if (!TextUtils.equals(this.b.getStyleId(), customMapStyleOptions.getStyleId())) {
                    this.b.setStyleId(customMapStyleOptions.getStyleId());
                    String styleId = this.b.getStyleId();
                    if (!TextUtils.isEmpty(styleId) && this.a != null && this.a.getMapConfig() != null && this.a.getMapConfig().isProFunctionAuthEnable()) {
                        if (this.s == null) {
                            this.s = new eh(this.h, this, 1);
                        }
                        this.s.a(styleId);
                        this.s.b();
                        if (this.t == null) {
                            this.t = new eh(this.h, this, 0);
                        }
                        this.t.a(styleId);
                        this.t.b();
                    }
                }
                if (!TextUtils.equals(this.b.getStyleDataPath(), customMapStyleOptions.getStyleDataPath())) {
                    this.b.setStyleDataPath(customMapStyleOptions.getStyleDataPath());
                    this.d = true;
                }
                if (this.b.getStyleData() != customMapStyleOptions.getStyleData()) {
                    this.b.setStyleData(customMapStyleOptions.getStyleData());
                    this.d = true;
                }
                if (!TextUtils.equals(this.b.getStyleTexturePath(), customMapStyleOptions.getStyleTexturePath())) {
                    this.b.setStyleTexturePath(customMapStyleOptions.getStyleTexturePath());
                    this.e = true;
                }
                if (this.b.getStyleTextureData() != customMapStyleOptions.getStyleTextureData()) {
                    this.b.setStyleTextureData(customMapStyleOptions.getStyleTextureData());
                    this.e = true;
                }
                if (!TextUtils.equals(this.b.getStyleExtraPath(), customMapStyleOptions.getStyleExtraPath())) {
                    this.b.setStyleExtraPath(customMapStyleOptions.getStyleExtraPath());
                    this.f = true;
                }
                if (this.b.getStyleExtraData() != customMapStyleOptions.getStyleExtraData()) {
                    this.b.setStyleExtraData(customMapStyleOptions.getStyleExtraData());
                    this.f = true;
                }
                fp.a(this.h, true);
            } else {
                i();
                fp.a(this.h, false);
            }
        }
    }

    @Override // com.amap.api.mapcore.util.eh.a
    public void a(byte[] bArr, int i) {
        MapConfig mapConfig;
        if (this.b != null) {
            synchronized (this) {
                if (this.a != null && (mapConfig = this.a.getMapConfig()) != null && mapConfig.isProFunctionAuthEnable()) {
                    mapConfig.setUseProFunction(true);
                    if (i == 1) {
                        this.u = bArr;
                        this.d = true;
                    } else if (i == 0) {
                        this.v = bArr;
                        this.f = true;
                    }
                }
            }
        }
    }

    public void b() {
        if (this.b == null) {
            return;
        }
        synchronized (this) {
            if (this.a != null && this.a.getMapConfig() != null && !this.a.getMapConfig().isProFunctionAuthEnable()) {
                this.b.setStyleId(null);
                this.u = null;
                this.v = null;
            }
            this.e = true;
            this.d = true;
            if (this.r) {
                this.f = true;
            }
            this.c = true;
        }
    }

    public void c() {
        if (this.b == null) {
            this.b = new CustomMapStyleOptions();
        }
    }

    public boolean d() {
        return this.b != null;
    }

    public void e() {
        synchronized (this) {
            if (this.b != null) {
                this.b.setEnable(false);
                i();
                this.c = true;
            }
        }
    }
}
