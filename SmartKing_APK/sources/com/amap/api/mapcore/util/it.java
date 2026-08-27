package com.amap.api.mapcore.util;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BinaryRequest.java */
/* loaded from: classes.dex */
public abstract class it extends ix {
    protected Context d;
    protected ho e;

    public it(Context context, ho hoVar) {
        if (context != null) {
            this.d = context.getApplicationContext();
        }
        this.e = hoVar;
    }

    private byte[] a() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byteArrayOutputStream.write(hp.a("PANDORA$"));
                byteArrayOutputStream.write(new byte[]{1});
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    hz.a(th, "bre", "gbh");
                }
                return byteArray;
            } catch (Throwable th2) {
                hz.a(th2, "bre", "gbh");
                try {
                    byteArrayOutputStream.close();
                    return null;
                } catch (Throwable th3) {
                    hz.a(th3, "bre", "gbh");
                    return null;
                }
            }
        } catch (Throwable th4) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th5) {
                hz.a(th5, "bre", "gbh");
            }
            throw th4;
        }
    }

    private byte[] k() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byte[] d = d();
                if (d != null && d.length != 0) {
                    byteArrayOutputStream.write(new byte[]{1});
                    byteArrayOutputStream.write(a(d));
                    byteArrayOutputStream.write(d);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th) {
                        hz.a(th, "bre", "grrd");
                    }
                    return byteArray;
                }
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    hz.a(th2, "bre", "grrd");
                }
                return byteArray2;
            } catch (Throwable th3) {
                hz.a(th3, "bre", "grrd");
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th4) {
                    hz.a(th4, "bre", "grrd");
                }
                return new byte[]{0};
            }
        } catch (Throwable th5) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th6) {
                hz.a(th6, "bre", "grrd");
            }
            throw th5;
        }
    }

    private byte[] l() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byte[] e = e();
                if (e != null && e.length != 0) {
                    byteArrayOutputStream.write(new byte[]{1});
                    byte[] a = hg.a(this.d, e);
                    byteArrayOutputStream.write(a(a));
                    byteArrayOutputStream.write(a);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th) {
                        hz.a(th, "bre", "gred");
                    }
                    return byteArray;
                }
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    hz.a(th2, "bre", "gred");
                }
                return byteArray2;
            } catch (Throwable th3) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th4) {
                    hz.a(th4, "bre", "gred");
                }
                throw th3;
            }
        } catch (Throwable th5) {
            hz.a(th5, "bre", "gred");
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th6) {
                hz.a(th6, "bre", "gred");
            }
            return new byte[]{0};
        }
    }

    protected byte[] a(byte[] bArr) {
        int length = bArr.length;
        return new byte[]{(byte) (length / 256), (byte) (length % 256)};
    }

    public abstract byte[] d();

    public abstract byte[] e();

    protected String f() {
        return "2.1";
    }

    public boolean g() {
        return true;
    }

    @Override // com.amap.api.mapcore.util.ix
    public final byte[] getEntityBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byteArrayOutputStream.write(a());
                byteArrayOutputStream.write(h());
                byteArrayOutputStream.write(k());
                byteArrayOutputStream.write(l());
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    hz.a(th, "bre", "geb");
                }
                return byteArray;
            } catch (Throwable th2) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th3) {
                    hz.a(th3, "bre", "geb");
                }
                throw th2;
            }
        } catch (Throwable th4) {
            hz.a(th4, "bre", "geb");
            try {
                byteArrayOutputStream.close();
                return null;
            } catch (Throwable th5) {
                hz.a(th5, "bre", "geb");
                return null;
            }
        }
    }

    @Override // com.amap.api.mapcore.util.ix
    public Map<String, String> getParams() {
        String f = hd.f(this.d);
        String a = hg.a();
        String a2 = hg.a(this.d, a, "key=" + f);
        HashMap hashMap = new HashMap();
        hashMap.put("ts", a);
        hashMap.put("key", f);
        hashMap.put("scode", a2);
        return hashMap;
    }

    public byte[] h() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byteArrayOutputStream.write(new byte[]{3});
                if (g()) {
                    byte[] a = hg.a(this.d, j());
                    byteArrayOutputStream.write(a(a));
                    byteArrayOutputStream.write(a);
                } else {
                    byteArrayOutputStream.write(new byte[]{0, 0});
                }
                byte[] a2 = hp.a(f());
                if (a2 == null || a2.length <= 0) {
                    byteArrayOutputStream.write(new byte[]{0, 0});
                } else {
                    byteArrayOutputStream.write(a(a2));
                    byteArrayOutputStream.write(a2);
                }
                byte[] a3 = hp.a(i());
                if (a3 == null || a3.length <= 0) {
                    byteArrayOutputStream.write(new byte[]{0, 0});
                } else {
                    byteArrayOutputStream.write(a(a3));
                    byteArrayOutputStream.write(a3);
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    hz.a(th, "bre", "gred");
                }
                return byteArray;
            } catch (Throwable th2) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th3) {
                    hz.a(th3, "bre", "gred");
                }
                throw th2;
            }
        } catch (Throwable th4) {
            hz.a(th4, "bre", "gpd");
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th5) {
                hz.a(th5, "bre", "gred");
            }
            return new byte[]{0};
        }
    }

    public String i() {
        return String.format("platform=Android&sdkversion=%s&product=%s", this.e.c(), this.e.a());
    }

    protected boolean j() {
        return false;
    }
}
