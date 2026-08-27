package com.amap.api.mapcore.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* compiled from: DBOperation.java */
/* loaded from: classes.dex */
public class ig {
    private static Map<Class<? extends Cif>, Cif> d = new HashMap();
    private ij a;
    private SQLiteDatabase b;
    private Cif c;

    public ig(Context context, Cif cif) {
        try {
            this.a = new ij(context.getApplicationContext(), cif.b(), null, cif.c(), cif);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.c = cif;
    }

    private ContentValues a(Object obj, ih ihVar) {
        ContentValues contentValues = new ContentValues();
        for (Field field : a(obj.getClass(), ihVar.b())) {
            field.setAccessible(true);
            a(obj, field, contentValues);
        }
        return contentValues;
    }

    private SQLiteDatabase a(boolean z) {
        try {
            if (this.b == null) {
                this.b = this.a.getReadableDatabase();
            }
        } catch (Throwable th) {
            if (z) {
                th.printStackTrace();
            } else {
                hz.a(th, "dbs", "grd");
            }
        }
        return this.b;
    }

    public static synchronized Cif a(Class<? extends Cif> cls) throws IllegalAccessException, InstantiationException {
        Cif cif;
        synchronized (ig.class) {
            if (d.get(cls) == null) {
                d.put(cls, cls.newInstance());
            }
            cif = d.get(cls);
        }
        return cif;
    }

    private <T> T a(Cursor cursor, Class<T> cls, ih ihVar) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Field[] a = a((Class<?>) cls, ihVar.b());
        Constructor<T> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
        declaredConstructor.setAccessible(true);
        T newInstance = declaredConstructor.newInstance(new Object[0]);
        for (Field field : a) {
            field.setAccessible(true);
            Annotation annotation = field.getAnnotation(ii.class);
            if (annotation != null) {
                ii iiVar = (ii) annotation;
                int b = iiVar.b();
                int columnIndex = cursor.getColumnIndex(iiVar.a());
                switch (b) {
                    case 1:
                        field.set(newInstance, Short.valueOf(cursor.getShort(columnIndex)));
                        break;
                    case 2:
                        field.set(newInstance, Integer.valueOf(cursor.getInt(columnIndex)));
                        break;
                    case 3:
                        field.set(newInstance, Float.valueOf(cursor.getFloat(columnIndex)));
                        break;
                    case 4:
                        field.set(newInstance, Double.valueOf(cursor.getDouble(columnIndex)));
                        break;
                    case 5:
                        field.set(newInstance, Long.valueOf(cursor.getLong(columnIndex)));
                        break;
                    case 6:
                        field.set(newInstance, cursor.getString(columnIndex));
                        break;
                    case 7:
                        field.set(newInstance, cursor.getBlob(columnIndex));
                        break;
                }
            }
        }
        return newInstance;
    }

    private <T> String a(ih ihVar) {
        if (ihVar == null) {
            return null;
        }
        return ihVar.a();
    }

    public static String a(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String str : map.keySet()) {
            if (z) {
                sb.append(str);
                sb.append(" = '");
                sb.append(map.get(str));
                sb.append("'");
                z = false;
            } else {
                sb.append(" and ");
                sb.append(str);
                sb.append(" = '");
                sb.append(map.get(str));
                sb.append("'");
            }
        }
        return sb.toString();
    }

    private <T> void a(SQLiteDatabase sQLiteDatabase, T t) {
        ContentValues a;
        ih b = b(t.getClass());
        String a2 = a(b);
        if (TextUtils.isEmpty(a2) || t == null || sQLiteDatabase == null || (a = a(t, b)) == null) {
            return;
        }
        sQLiteDatabase.insert(a2, null, a);
    }

    private void a(Object obj, Field field, ContentValues contentValues) {
        Annotation annotation = field.getAnnotation(ii.class);
        if (annotation == null) {
            return;
        }
        ii iiVar = (ii) annotation;
        try {
            switch (iiVar.b()) {
                case 1:
                    contentValues.put(iiVar.a(), Short.valueOf(field.getShort(obj)));
                    break;
                case 2:
                    contentValues.put(iiVar.a(), Integer.valueOf(field.getInt(obj)));
                    break;
                case 3:
                    contentValues.put(iiVar.a(), Float.valueOf(field.getFloat(obj)));
                    break;
                case 4:
                    contentValues.put(iiVar.a(), Double.valueOf(field.getDouble(obj)));
                    break;
                case 5:
                    contentValues.put(iiVar.a(), Long.valueOf(field.getLong(obj)));
                    break;
                case 6:
                    contentValues.put(iiVar.a(), (String) field.get(obj));
                    break;
                case 7:
                    contentValues.put(iiVar.a(), (byte[]) field.get(obj));
                    break;
                default:
                    return;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private boolean a(Annotation annotation) {
        return annotation != null;
    }

    private Field[] a(Class<?> cls, boolean z) {
        if (cls == null) {
            return null;
        }
        return z ? cls.getSuperclass().getDeclaredFields() : cls.getDeclaredFields();
    }

    private SQLiteDatabase b(boolean z) {
        try {
            if (this.b == null || this.b.isReadOnly()) {
                if (this.b != null) {
                    this.b.close();
                }
                this.b = this.a.getWritableDatabase();
            }
        } catch (Throwable th) {
            hz.a(th, "dbs", "gwd");
        }
        return this.b;
    }

    private <T> ih b(Class<T> cls) {
        Annotation annotation = cls.getAnnotation(ih.class);
        if (a(annotation)) {
            return (ih) annotation;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:82:0x00c2 A[Catch: Throwable -> 0x00ca, all -> 0x00fd, TRY_LEAVE, TryCatch #7 {Throwable -> 0x00ca, blocks: (B:80:0x00be, B:82:0x00c2), top: B:79:0x00be, outer: #11 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x00b0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v2 */
    /* JADX WARN: Type inference failed for: r13v4, types: [android.database.Cursor] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public <T> java.util.List<T> a(java.lang.String r13, java.lang.Class<T> r14, boolean r15) {
        /*
            Method dump skipped, instructions count: 256
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.ig.a(java.lang.String, java.lang.Class, boolean):java.util.List");
    }

    public <T> void a(T t) {
        a((ig) t, false);
    }

    public void a(Object obj, String str) {
        synchronized (this.c) {
            List b = b(str, obj.getClass());
            if (b != null && b.size() != 0) {
                a(str, obj);
            }
            a((ig) obj);
        }
    }

    public <T> void a(T t, boolean z) {
        synchronized (this.c) {
            this.b = b(z);
            if (this.b == null) {
                return;
            }
            try {
                try {
                    a(this.b, (SQLiteDatabase) t);
                } catch (Throwable th) {
                    hz.a(th, "dbs", "itd");
                    if (this.b != null) {
                        this.b.close();
                    }
                }
                if (this.b != null) {
                    this.b.close();
                    this.b = null;
                }
            } catch (Throwable th2) {
                if (this.b != null) {
                    this.b.close();
                    this.b = null;
                }
                throw th2;
            }
        }
    }

    public <T> void a(String str, Class<T> cls) {
        synchronized (this.c) {
            String a = a(b(cls));
            if (TextUtils.isEmpty(a)) {
                return;
            }
            this.b = b(false);
            if (this.b == null) {
                return;
            }
            try {
                try {
                    this.b.delete(a, str, null);
                } catch (Throwable th) {
                    hz.a(th, "dbs", "dld");
                    if (this.b != null) {
                        this.b.close();
                    }
                }
                if (this.b != null) {
                    this.b.close();
                    this.b = null;
                }
            } catch (Throwable th2) {
                if (this.b != null) {
                    this.b.close();
                    this.b = null;
                }
                throw th2;
            }
        }
    }

    public <T> void a(String str, Object obj) {
        a(str, obj, false);
    }

    public <T> void a(String str, Object obj, boolean z) {
        synchronized (this.c) {
            try {
                if (obj == null) {
                    return;
                }
                ih b = b(obj.getClass());
                String a = a(b);
                if (TextUtils.isEmpty(a)) {
                    return;
                }
                ContentValues a2 = a(obj, b);
                if (a2 == null) {
                    return;
                }
                this.b = b(z);
                if (this.b == null) {
                    return;
                }
                try {
                    try {
                        this.b.update(a, a2, str, null);
                    } catch (Throwable th) {
                        if (this.b != null) {
                            this.b.close();
                            this.b = null;
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    if (z) {
                        th2.printStackTrace();
                    } else {
                        hz.a(th2, "dbs", "udd");
                    }
                    if (this.b != null) {
                        this.b.close();
                    }
                }
                if (this.b != null) {
                    this.b.close();
                    this.b = null;
                }
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }

    public <T> void a(List<T> list) {
        String str;
        String str2;
        synchronized (this.c) {
            if (list != null) {
                try {
                    if (list.size() != 0) {
                        this.b = b(false);
                        if (this.b == null) {
                            return;
                        }
                        try {
                            try {
                                this.b.beginTransaction();
                                Iterator<T> it = list.iterator();
                                while (it.hasNext()) {
                                    a(this.b, (SQLiteDatabase) it.next());
                                }
                                this.b.setTransactionSuccessful();
                                try {
                                    if (this.b.inTransaction()) {
                                        this.b.endTransaction();
                                    }
                                } catch (Throwable th) {
                                    hz.a(th, "dbs", "ild");
                                }
                            } catch (Throwable th2) {
                                try {
                                    if (this.b.inTransaction()) {
                                        this.b.endTransaction();
                                    }
                                } catch (Throwable th3) {
                                    hz.a(th3, "dbs", "ild");
                                }
                                try {
                                    this.b.close();
                                    this.b = null;
                                    throw th2;
                                } catch (Throwable th4) {
                                    hz.a(th4, "dbs", "ild");
                                    throw th2;
                                }
                            }
                        } catch (Throwable th5) {
                            hz.a(th5, "dbs", "ild");
                            try {
                                if (this.b.inTransaction()) {
                                    this.b.endTransaction();
                                }
                            } catch (Throwable th6) {
                                hz.a(th6, "dbs", "ild");
                            }
                            try {
                                this.b.close();
                                this.b = null;
                            } catch (Throwable th7) {
                                th = th7;
                                str = "dbs";
                                str2 = "ild";
                                hz.a(th, str, str2);
                            }
                        }
                        try {
                            this.b.close();
                            this.b = null;
                        } catch (Throwable th8) {
                            th = th8;
                            str = "dbs";
                            str2 = "ild";
                            hz.a(th, str, str2);
                        }
                    }
                } finally {
                }
            }
        }
    }

    public <T> List<T> b(String str, Class<T> cls) {
        return a(str, (Class) cls, false);
    }
}
