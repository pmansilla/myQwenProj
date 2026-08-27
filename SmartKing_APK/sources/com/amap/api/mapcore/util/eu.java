package com.amap.api.mapcore.util;

import android.text.TextUtils;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.Tile;
import com.amap.api.maps.model.TileProvider;
import com.autonavi.amap.mapcore.MapConfig;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/* compiled from: BaseTileProvider.java */
/* loaded from: classes.dex */
public class eu implements TileProvider {
    Random a = new Random();
    private final int b;
    private final int c;
    private MapConfig d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BaseTileProvider.java */
    /* loaded from: classes.dex */
    public class a extends eq {
        private int e;
        private int f;
        private int g;
        private String h;
        private String i;

        public a(int i, int i2, int i3, String str) {
            this.i = "";
            this.e = i;
            this.f = i2;
            this.g = i3;
            this.h = str;
            this.i = d();
            setProxy(hm.a(ai.a));
            setConnectionTimeout(5000);
            setSoTimeout(50000);
        }

        private String a() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("key=");
            stringBuffer.append(hd.f(ai.a));
            stringBuffer.append("&channel=amapapi");
            if (fk.a(this.e, this.f, this.g) || this.g < 7) {
                stringBuffer.append("&z=");
                stringBuffer.append(this.g);
                stringBuffer.append("&x=");
                stringBuffer.append(this.e);
                stringBuffer.append("&y=");
                stringBuffer.append(this.f);
                stringBuffer.append("&lang=en&size=1&scale=1&style=7");
            } else if (MapsInitializer.isLoadWorldGridMap()) {
                stringBuffer.append("&x=");
                stringBuffer.append(this.e);
                stringBuffer.append("&y=");
                stringBuffer.append(this.f);
                stringBuffer.append("&z=");
                stringBuffer.append(this.g);
                stringBuffer.append("&ds=0");
                stringBuffer.append("&dpitype=webrd");
                stringBuffer.append("&lang=");
                stringBuffer.append(this.h);
                stringBuffer.append("&scale=2");
            }
            String stringBuffer2 = stringBuffer.toString();
            String a = a(stringBuffer2);
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(stringBuffer2);
            String a2 = hg.a();
            stringBuffer3.append("&ts=" + a2);
            stringBuffer3.append("&scode=" + hg.a(ai.a, a2, a));
            return stringBuffer3.toString();
        }

        private String a(String str) {
            String[] split = str.split("&");
            Arrays.sort(split);
            StringBuffer stringBuffer = new StringBuffer();
            for (String str2 : split) {
                stringBuffer.append(b(str2));
                stringBuffer.append("&");
            }
            String stringBuffer2 = stringBuffer.toString();
            return stringBuffer2.length() > 1 ? (String) stringBuffer2.subSequence(0, stringBuffer2.length() - 1) : str;
        }

        private String b(String str) {
            if (str == null) {
                return str;
            }
            try {
                return URLDecoder.decode(str, "utf-8");
            } catch (UnsupportedEncodingException e) {
                ic.c(e, "AbstractProtocalHandler", "strReEncoder");
                return "";
            } catch (Exception e2) {
                ic.c(e2, "AbstractProtocalHandler", "strReEncoderException");
                return "";
            }
        }

        private String d() {
            if (fk.a(this.e, this.f, this.g) || this.g < 7) {
                return String.format(Locale.US, "http://wprd0%d.is.autonavi.com/appmaptile?", Integer.valueOf((eu.this.a.nextInt(100000) % 4) + 1));
            }
            if (MapsInitializer.isLoadWorldGridMap()) {
                return "http://restapi.amap.com/v4/gridmap?";
            }
            return null;
        }

        @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
        public Map<String, String> getParams() {
            return null;
        }

        @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
        public Map<String, String> getRequestHead() {
            Hashtable hashtable = new Hashtable(16);
            hashtable.put("User-Agent", w.c);
            hashtable.put("Accept-Encoding", "gzip");
            hashtable.put("platinfo", String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s", "6.9.3", "3dmap"));
            hashtable.put("x-INFO", hg.a(ai.a));
            hashtable.put("key", hd.f(ai.a));
            hashtable.put("logversion", "2.1");
            return hashtable;
        }

        @Override // com.amap.api.mapcore.util.ix
        public String getURL() {
            if (TextUtils.isEmpty(this.i)) {
                return null;
            }
            return this.i + a();
        }
    }

    public eu(int i, int i2, MapConfig mapConfig) {
        this.b = i;
        this.c = i2;
        this.d = mapConfig;
    }

    private byte[] a(int i, int i2, int i3, String str) throws IOException {
        try {
            return new a(i, i2, i3, str).makeHttpRequest();
        } catch (Throwable unused) {
            return null;
        }
    }

    @Override // com.amap.api.maps.model.TileProvider
    public final Tile getTile(int i, int i2, int i3) {
        try {
            byte[] a2 = a(i, i2, i3, this.d != null ? this.d.getMapLanguage() : "zh_cn");
            return a2 == null ? NO_TILE : Tile.obtain(this.b, this.c, a2);
        } catch (IOException unused) {
            return NO_TILE;
        }
    }

    @Override // com.amap.api.maps.model.TileProvider
    public int getTileHeight() {
        return this.c;
    }

    @Override // com.amap.api.maps.model.TileProvider
    public int getTileWidth() {
        return this.b;
    }
}
