package com.loc;

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
import java.util.Map;

/* compiled from: DBOperation.java */
/* loaded from: classes.dex */
public final class av {
    private static Map<Class<? extends au>, au> d = new HashMap();
    private ay a;
    private SQLiteDatabase b;
    private au c;

    public av(Context context, au auVar) {
        try {
            this.a = new ay(context.getApplicationContext(), auVar.a(), auVar);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.c = auVar;
    }

    private static ContentValues a(Object obj, aw awVar) {
        ContentValues contentValues = new ContentValues();
        for (Field field : a(obj.getClass(), awVar.b())) {
            field.setAccessible(true);
            Annotation annotation = field.getAnnotation(ax.class);
            if (annotation != null) {
                ax axVar = (ax) annotation;
                switch (axVar.b()) {
                    case 1:
                        contentValues.put(axVar.a(), Short.valueOf(field.getShort(obj)));
                        break;
                    case 2:
                        contentValues.put(axVar.a(), Integer.valueOf(field.getInt(obj)));
                        break;
                    case 3:
                        contentValues.put(axVar.a(), Float.valueOf(field.getFloat(obj)));
                        break;
                    case 4:
                        contentValues.put(axVar.a(), Double.valueOf(field.getDouble(obj)));
                        break;
                    case 5:
                        contentValues.put(axVar.a(), Long.valueOf(field.getLong(obj)));
                        break;
                    case 6:
                        contentValues.put(axVar.a(), (String) field.get(obj));
                        break;
                    case 7:
                        try {
                            contentValues.put(axVar.a(), (byte[]) field.get(obj));
                            break;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            break;
                        }
                }
            }
        }
        return contentValues;
    }

    private SQLiteDatabase a() {
        try {
            if (this.b == null || this.b.isReadOnly()) {
                if (this.b != null) {
                    this.b.close();
                }
                this.b = this.a.getWritableDatabase();
            }
        } catch (Throwable th) {
            an.a(th, "dbs", "gwd");
        }
        return this.b;
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
                an.a(th, "dbs", "grd");
            }
        }
        return this.b;
    }

    public static synchronized au a(Class<? extends au> cls) throws IllegalAccessException, InstantiationException {
        au auVar;
        synchronized (av.class) {
            if (d.get(cls) == null) {
                d.put(cls, cls.newInstance());
            }
            auVar = d.get(cls);
        }
        return auVar;
    }

    private static <T> T a(Cursor cursor, Class<T> cls, aw awVar) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object valueOf;
        Field[] a = a((Class<?>) cls, awVar.b());
        Constructor<T> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
        declaredConstructor.setAccessible(true);
        T newInstance = declaredConstructor.newInstance(new Object[0]);
        for (Field field : a) {
            field.setAccessible(true);
            Annotation annotation = field.getAnnotation(ax.class);
            if (annotation != null) {
                ax axVar = (ax) annotation;
                int b = axVar.b();
                int columnIndex = cursor.getColumnIndex(axVar.a());
                switch (b) {
                    case 1:
                        valueOf = Short.valueOf(cursor.getShort(columnIndex));
                        break;
                    case 2:
                        valueOf = Integer.valueOf(cursor.getInt(columnIndex));
                        break;
                    case 3:
                        valueOf = Float.valueOf(cursor.getFloat(columnIndex));
                        break;
                    case 4:
                        valueOf = Double.valueOf(cursor.getDouble(columnIndex));
                        break;
                    case 5:
                        valueOf = Long.valueOf(cursor.getLong(columnIndex));
                        break;
                    case 6:
                        valueOf = cursor.getString(columnIndex);
                        break;
                    case 7:
                        valueOf = cursor.getBlob(columnIndex);
                        break;
                }
                field.set(newInstance, valueOf);
            }
        }
        return newInstance;
    }

    private static <T> String a(aw awVar) {
        if (awVar == null) {
            return null;
        }
        return awVar.a();
    }

    public static String a(Map<String, String> map) {
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

    private static Field[] a(Class<?> cls, boolean z) {
        if (cls == null) {
            return null;
        }
        return z ? cls.getSuperclass().getDeclaredFields() : cls.getDeclaredFields();
    }

    private static <T> aw b(Class<T> cls) {
        Annotation annotation = cls.getAnnotation(aw.class);
        if (annotation != null) {
            return (aw) annotation;
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
    public final <T> java.util.List<T> a(java.lang.String r13, java.lang.Class<T> r14, boolean r15) {
        /*
            Method dump skipped, instructions count: 256
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.av.a(java.lang.String, java.lang.Class, boolean):java.util.List");
    }

    public final <T> void a(T t) {
        synchronized (this.c) {
            this.b = a();
            if (this.b == null) {
                return;
            }
            try {
                try {
                    SQLiteDatabase sQLiteDatabase = this.b;
                    aw b = b(t.getClass());
                    String a = a(b);
                    if (!TextUtils.isEmpty(a) && t != null && sQLiteDatabase != null) {
                        sQLiteDatabase.insert(a, null, a(t, b));
                    }
                } catch (Throwable th) {
                    an.a(th, "dbs", "itd");
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

    public final void a(Object obj, String str) {
        synchronized (this.c) {
            if (a(str, (Class) obj.getClass(), false).size() == 0) {
                a((av) obj);
            } else {
                a(str, obj);
            }
        }
    }

    public final <T> void a(String str, Object obj) {
        synchronized (this.c) {
            try {
                if (obj == null) {
                    return;
                }
                aw b = b(obj.getClass());
                String a = a(b);
                if (TextUtils.isEmpty(a)) {
                    return;
                }
                ContentValues a2 = a(obj, b);
                this.b = a();
                if (this.b == null) {
                    return;
                }
                try {
                    try {
                        this.b.update(a, a2, str, null);
                    } catch (Throwable th) {
                        an.a(th, "dbs", "udd");
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
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }
}
