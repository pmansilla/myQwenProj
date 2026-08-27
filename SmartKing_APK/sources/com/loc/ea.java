package com.loc;

import com.loc.dy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/* compiled from: TransactionXMLFile.java */
/* loaded from: classes.dex */
public final class ea {
    private static final Object c = new Object();
    private File b;
    private final Object a = new Object();
    private HashMap<File, a> d = new HashMap<>();

    /* compiled from: TransactionXMLFile.java */
    /* loaded from: classes.dex */
    private static final class a implements dy {
        private static final Object f = new Object();
        private final File a;
        private final File b;
        private Map d;
        private WeakHashMap<Object, Object> g;
        private boolean e = false;
        private final int c = 0;

        /* compiled from: TransactionXMLFile.java */
        /* renamed from: com.loc.ea$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public final class C0047a implements dy.a {
            private final Map<String, Object> b = new HashMap();
            private boolean c = false;

            public C0047a() {
            }

            @Override // com.loc.dy.a
            public final dy.a a() {
                synchronized (this) {
                    this.c = true;
                }
                return this;
            }

            @Override // com.loc.dy.a
            public final dy.a a(String str, float f) {
                synchronized (this) {
                    this.b.put(str, Float.valueOf(f));
                }
                return this;
            }

            @Override // com.loc.dy.a
            public final dy.a a(String str, int i) {
                synchronized (this) {
                    this.b.put(str, Integer.valueOf(i));
                }
                return this;
            }

            @Override // com.loc.dy.a
            public final dy.a a(String str, long j) {
                synchronized (this) {
                    this.b.put(str, Long.valueOf(j));
                }
                return this;
            }

            @Override // com.loc.dy.a
            public final dy.a a(String str, String str2) {
                synchronized (this) {
                    this.b.put(str, str2);
                }
                return this;
            }

            @Override // com.loc.dy.a
            public final dy.a a(String str, boolean z) {
                synchronized (this) {
                    this.b.put(str, Boolean.valueOf(z));
                }
                return this;
            }

            @Override // com.loc.dy.a
            public final boolean b() {
                boolean z;
                ArrayList arrayList;
                HashSet hashSet;
                boolean f;
                synchronized (ea.c) {
                    z = a.this.g.size() > 0;
                    arrayList = null;
                    if (z) {
                        arrayList = new ArrayList();
                        hashSet = new HashSet(a.this.g.keySet());
                    } else {
                        hashSet = null;
                    }
                    synchronized (this) {
                        if (this.c) {
                            a.this.d.clear();
                            this.c = false;
                        }
                        for (Map.Entry<String, Object> entry : this.b.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (value == this) {
                                a.this.d.remove(key);
                            } else {
                                a.this.d.put(key, value);
                            }
                            if (z) {
                                arrayList.add(key);
                            }
                        }
                        this.b.clear();
                    }
                    f = a.this.f();
                    if (f) {
                        a.this.d();
                    }
                }
                if (z) {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        arrayList.get(size);
                        Iterator it = hashSet.iterator();
                        while (it.hasNext()) {
                            it.next();
                        }
                    }
                }
                return f;
            }
        }

        a(File file, Map map) {
            this.a = file;
            this.b = ea.b(file);
            this.d = map == null ? new HashMap() : map;
            this.g = new WeakHashMap<>();
        }

        private static FileOutputStream a(File file) {
            try {
                return new FileOutputStream(file);
            } catch (FileNotFoundException unused) {
                if (!file.getParentFile().mkdir()) {
                    return null;
                }
                try {
                    return new FileOutputStream(file);
                } catch (FileNotFoundException unused2) {
                    return null;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean f() {
            if (this.a.exists()) {
                if (this.b.exists()) {
                    this.a.delete();
                } else if (!this.a.renameTo(this.b)) {
                    return false;
                }
            }
            try {
                FileOutputStream a = a(this.a);
                if (a == null) {
                    return false;
                }
                Map map = this.d;
                dx dxVar = new dx();
                dxVar.setOutput(a, "utf-8");
                dxVar.startDocument(null, true);
                dxVar.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                cw.a(map, (String) null, (XmlSerializer) dxVar);
                dxVar.endDocument();
                a.close();
                this.b.delete();
                return true;
            } catch (IOException | XmlPullParserException unused) {
                if (this.a.exists()) {
                    this.a.delete();
                }
                return false;
            }
        }

        @Override // com.loc.dy
        public final long a(String str) {
            long longValue;
            synchronized (this) {
                Long l = (Long) this.d.get(str);
                longValue = l != null ? l.longValue() : 0L;
            }
            return longValue;
        }

        @Override // com.loc.dy
        public final String a(String str, String str2) {
            String str3;
            synchronized (this) {
                str3 = (String) this.d.get(str);
                if (str3 == null) {
                    str3 = str2;
                }
            }
            return str3;
        }

        public final void a(Map map) {
            if (map != null) {
                synchronized (this) {
                    this.d = map;
                }
            }
        }

        @Override // com.loc.dy
        public final boolean a() {
            return this.a != null && new File(this.a.getAbsolutePath()).exists();
        }

        @Override // com.loc.dy
        public final Map<String, ?> b() {
            HashMap hashMap;
            synchronized (this) {
                hashMap = new HashMap(this.d);
            }
            return hashMap;
        }

        @Override // com.loc.dy
        public final dy.a c() {
            return new C0047a();
        }

        public final void d() {
            synchronized (this) {
                this.e = true;
            }
        }

        public final boolean e() {
            boolean z;
            synchronized (this) {
                z = this.e;
            }
            return z;
        }
    }

    public ea(String str) {
        if (str == null || str.length() <= 0) {
            throw new RuntimeException("Directory can not be empty");
        }
        this.b = new File(str);
    }

    private File b() {
        File file;
        synchronized (this.a) {
            file = this.b;
        }
        return file;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File b(File file) {
        return new File(file.getPath() + ".bak");
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x009d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.loc.dy a(java.lang.String r8) {
        /*
            r7 = this;
            java.io.File r0 = r7.b()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r8)
            java.lang.String r8 = ".xml"
            r1.append(r8)
            java.lang.String r8 = r1.toString()
            char r1 = java.io.File.separatorChar
            int r1 = r8.indexOf(r1)
            if (r1 >= 0) goto Lc0
            java.io.File r1 = new java.io.File
            r1.<init>(r0, r8)
            java.lang.Object r0 = com.loc.ea.c
            monitor-enter(r0)
            java.util.HashMap<java.io.File, com.loc.ea$a> r8 = r7.d     // Catch: java.lang.Throwable -> Lbd
            java.lang.Object r8 = r8.get(r1)     // Catch: java.lang.Throwable -> Lbd
            com.loc.ea$a r8 = (com.loc.ea.a) r8     // Catch: java.lang.Throwable -> Lbd
            if (r8 == 0) goto L37
            boolean r2 = r8.e()     // Catch: java.lang.Throwable -> Lbd
            if (r2 != 0) goto L37
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lbd
            return r8
        L37:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lbd
            java.io.File r0 = b(r1)
            boolean r2 = r0.exists()
            if (r2 == 0) goto L48
            r1.delete()
            r0.renameTo(r1)
        L48:
            boolean r0 = r1.exists()
            if (r0 == 0) goto L51
            r1.canRead()
        L51:
            boolean r0 = r1.exists()
            r2 = 0
            if (r0 == 0) goto L9a
            boolean r0 = r1.canRead()
            if (r0 == 0) goto L9a
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch: org.xmlpull.v1.XmlPullParserException -> L79 java.lang.Throwable -> L9a
            r0.<init>(r1)     // Catch: org.xmlpull.v1.XmlPullParserException -> L79 java.lang.Throwable -> L9a
            org.xmlpull.v1.XmlPullParser r3 = android.util.Xml.newPullParser()     // Catch: org.xmlpull.v1.XmlPullParserException -> L79 java.lang.Throwable -> L9a
            r3.setInput(r0, r2)     // Catch: org.xmlpull.v1.XmlPullParserException -> L79 java.lang.Throwable -> L9a
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: org.xmlpull.v1.XmlPullParserException -> L79 java.lang.Throwable -> L9a
            java.lang.Object r3 = com.loc.cw.a(r3, r4)     // Catch: org.xmlpull.v1.XmlPullParserException -> L79 java.lang.Throwable -> L9a
            java.util.HashMap r3 = (java.util.HashMap) r3     // Catch: org.xmlpull.v1.XmlPullParserException -> L79 java.lang.Throwable -> L9a
            r0.close()     // Catch: java.lang.Throwable -> L76 org.xmlpull.v1.XmlPullParserException -> L78
        L76:
            r2 = r3
            goto L9a
        L78:
            r2 = r3
        L79:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch: java.io.IOException -> L91 java.io.FileNotFoundException -> L96
            r0.<init>(r1)     // Catch: java.io.IOException -> L91 java.io.FileNotFoundException -> L96
            int r3 = r0.available()     // Catch: java.io.IOException -> L91 java.io.FileNotFoundException -> L96
            byte[] r3 = new byte[r3]     // Catch: java.io.IOException -> L91 java.io.FileNotFoundException -> L96
            r0.read(r3)     // Catch: java.io.IOException -> L91 java.io.FileNotFoundException -> L96
            java.lang.String r0 = new java.lang.String     // Catch: java.io.IOException -> L91 java.io.FileNotFoundException -> L96
            r4 = 0
            int r5 = r3.length     // Catch: java.io.IOException -> L91 java.io.FileNotFoundException -> L96
            java.lang.String r6 = "UTF-8"
            r0.<init>(r3, r4, r5, r6)     // Catch: java.io.IOException -> L91 java.io.FileNotFoundException -> L96
            goto L9a
        L91:
            r0 = move-exception
            r0.printStackTrace()
            goto L9a
        L96:
            r0 = move-exception
            r0.printStackTrace()
        L9a:
            java.lang.Object r3 = com.loc.ea.c
            monitor-enter(r3)
            if (r8 == 0) goto La5
            r8.a(r2)     // Catch: java.lang.Throwable -> La3
            goto Lb9
        La3:
            r8 = move-exception
            goto Lbb
        La5:
            java.util.HashMap<java.io.File, com.loc.ea$a> r8 = r7.d     // Catch: java.lang.Throwable -> La3
            java.lang.Object r8 = r8.get(r1)     // Catch: java.lang.Throwable -> La3
            com.loc.ea$a r8 = (com.loc.ea.a) r8     // Catch: java.lang.Throwable -> La3
            if (r8 != 0) goto Lb9
            com.loc.ea$a r8 = new com.loc.ea$a     // Catch: java.lang.Throwable -> La3
            r8.<init>(r1, r2)     // Catch: java.lang.Throwable -> La3
            java.util.HashMap<java.io.File, com.loc.ea$a> r0 = r7.d     // Catch: java.lang.Throwable -> La3
            r0.put(r1, r8)     // Catch: java.lang.Throwable -> La3
        Lb9:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> La3
            return r8
        Lbb:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> La3
            throw r8
        Lbd:
            r8 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lbd
            throw r8
        Lc0:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "File "
            r1.<init>(r2)
            r1.append(r8)
            java.lang.String r8 = " contains a path separator"
            r1.append(r8)
            java.lang.String r8 = r1.toString()
            r0.<init>(r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ea.a(java.lang.String):com.loc.dy");
    }
}
