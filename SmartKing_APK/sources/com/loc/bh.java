package com.loc;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BinaryRequest.java */
/* loaded from: classes.dex */
public abstract class bh extends bj {
    protected Context a;
    protected ac b;

    public bh(Context context, ac acVar) {
        if (context != null) {
            this.a = context.getApplicationContext();
        }
        this.b = acVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] a(byte[] bArr) {
        int length = bArr.length;
        return new byte[]{(byte) (length / 256), (byte) (length % 256)};
    }

    private static byte[] m() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byteArrayOutputStream.write(ad.a("PANDORA$"));
                byteArrayOutputStream.write(new byte[]{1});
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    an.a(th, "bre", "gbh");
                }
                return byteArray;
            } catch (Throwable th2) {
                an.a(th2, "bre", "gbh");
                try {
                    byteArrayOutputStream.close();
                    return null;
                } catch (Throwable th3) {
                    an.a(th3, "bre", "gbh");
                    return null;
                }
            }
        } catch (Throwable th4) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th5) {
                an.a(th5, "bre", "gbh");
            }
            throw th4;
        }
    }

    private byte[] n() {
        byte[] bArr;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byteArrayOutputStream.write(new byte[]{3});
                if (g()) {
                    bArr = w.a(this.a, i());
                    byteArrayOutputStream.write(a(bArr));
                } else {
                    bArr = new byte[]{0, 0};
                }
                byteArrayOutputStream.write(bArr);
                byte[] a = ad.a(f());
                if (a == null || a.length <= 0) {
                    a = new byte[]{0, 0};
                } else {
                    byteArrayOutputStream.write(a(a));
                }
                byteArrayOutputStream.write(a);
                byte[] a2 = ad.a(h());
                if (a2 == null || a2.length <= 0) {
                    a2 = new byte[]{0, 0};
                } else {
                    byteArrayOutputStream.write(a(a2));
                }
                byteArrayOutputStream.write(a2);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    an.a(th, "bre", "gred");
                }
                return byteArray;
            } catch (Throwable th2) {
                an.a(th2, "bre", "gpd");
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th3) {
                    an.a(th3, "bre", "gred");
                }
                return new byte[]{0};
            }
        } catch (Throwable th4) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th5) {
                an.a(th5, "bre", "gred");
            }
            throw th4;
        }
    }

    private byte[] o() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byte[] a_ = a_();
                if (a_ != null && a_.length != 0) {
                    byteArrayOutputStream.write(new byte[]{1});
                    byteArrayOutputStream.write(a(a_));
                    byteArrayOutputStream.write(a_);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th) {
                        an.a(th, "bre", "grrd");
                    }
                    return byteArray;
                }
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    an.a(th2, "bre", "grrd");
                }
                return byteArray2;
            } catch (Throwable th3) {
                an.a(th3, "bre", "grrd");
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th4) {
                    an.a(th4, "bre", "grrd");
                }
                return new byte[]{0};
            }
        } catch (Throwable th5) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th6) {
                an.a(th6, "bre", "grrd");
            }
            throw th5;
        }
    }

    private byte[] p() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byte[] e = e();
                if (e != null && e.length != 0) {
                    byteArrayOutputStream.write(new byte[]{1});
                    byte[] a = y.a(e);
                    byteArrayOutputStream.write(a(a));
                    byteArrayOutputStream.write(a);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th) {
                        an.a(th, "bre", "gred");
                    }
                    return byteArray;
                }
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    an.a(th2, "bre", "gred");
                }
                return byteArray2;
            } catch (Throwable th3) {
                an.a(th3, "bre", "gred");
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th4) {
                    an.a(th4, "bre", "gred");
                }
                return new byte[]{0};
            }
        } catch (Throwable th5) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th6) {
                an.a(th6, "bre", "gred");
            }
            throw th5;
        }
    }

    public abstract byte[] a_();

    @Override // com.loc.bj
    public Map<String, String> b_() {
        String f = u.f(this.a);
        String a = w.a();
        String a2 = w.a(this.a, a, "key=" + f);
        HashMap hashMap = new HashMap();
        hashMap.put("ts", a);
        hashMap.put("key", f);
        hashMap.put("scode", a2);
        return hashMap;
    }

    @Override // com.loc.bj
    public final byte[] d() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byteArrayOutputStream.write(m());
                byteArrayOutputStream.write(n());
                byteArrayOutputStream.write(o());
                byteArrayOutputStream.write(p());
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    an.a(th, "bre", "geb");
                }
                return byteArray;
            } catch (Throwable th2) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th3) {
                    an.a(th3, "bre", "geb");
                }
                throw th2;
            }
        } catch (Throwable th4) {
            an.a(th4, "bre", "geb");
            try {
                byteArrayOutputStream.close();
                return null;
            } catch (Throwable th5) {
                an.a(th5, "bre", "geb");
                return null;
            }
        }
    }

    public abstract byte[] e();

    protected String f() {
        return "2.1";
    }

    public boolean g() {
        return true;
    }

    public String h() {
        return String.format("platform=Android&sdkversion=%s&product=%s", this.b.c(), this.b.a());
    }

    protected boolean i() {
        return false;
    }
}
