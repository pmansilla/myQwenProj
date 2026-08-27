package com.mob.commons.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import com.alibaba.fastjson.asm.Opcodes;
import com.amap.location.common.model.AmapLoc;
import java.util.ArrayList;

/* compiled from: Meizu.java */
/* loaded from: classes.dex */
public class g extends f {
    private a c;
    private a d;
    private a e;
    private a f;
    private a g;
    private BroadcastReceiver h;

    /* compiled from: Meizu.java */
    /* loaded from: classes.dex */
    public static class a {
        private String a;
        private long b;
        private String c;

        public a(String str) {
            this.a = str;
        }

        public String a() {
            return this.c;
        }

        public void a(long j) {
            this.b = j;
        }

        public void a(String str) {
            this.c = str;
        }

        public boolean b() {
            return this.b > System.currentTimeMillis();
        }
    }

    public g(Context context) {
        super(context);
        this.c = new a(com.mob.commons.k.a(71));
        this.d = new a(com.mob.commons.k.a(69));
        this.e = new a(com.mob.commons.k.a(70));
        this.f = new a(com.mob.commons.k.a(75));
        this.g = new a(com.mob.commons.k.a(125));
    }

    private String a(Context context, a aVar, boolean z) {
        Cursor query;
        String str;
        if (aVar == null) {
            return null;
        }
        if (!z && aVar.b()) {
            return aVar.c;
        }
        try {
            query = context.getContentResolver().query(Uri.parse(com.mob.commons.k.a(124)), null, null, new String[]{aVar.a}, null);
        } catch (Throwable th) {
            c.a().a(th);
        }
        if (query == null) {
            if (z) {
                aVar.a(AmapLoc.RESULT_TYPE_WIFI_ONLY);
            }
            if (a(false)) {
                a(true);
            }
            return null;
        }
        query.moveToFirst();
        int columnIndex = query.getColumnIndex(com.mob.commons.k.a(Opcodes.IAND));
        if (columnIndex >= 0) {
            str = query.getString(columnIndex);
            aVar.a(str);
        } else {
            str = null;
        }
        if (!z) {
            int columnIndex2 = query.getColumnIndex(com.mob.commons.k.a(130));
            if (columnIndex2 >= 0) {
                aVar.a(query.getLong(columnIndex2));
            }
            int columnIndex3 = query.getColumnIndex(com.mob.commons.k.a(119));
            if (columnIndex3 >= 0 && query.getInt(columnIndex3) != 1000) {
                j();
                if (!a(false)) {
                    a(true);
                }
            }
        }
        query.close();
        return str;
    }

    private boolean a(boolean z) {
        if (!z && this.g != null && this.g.a() != null) {
            return this.g.a().equals(AmapLoc.RESULT_TYPE_GPS);
        }
        String a2 = a(this.a, this.g, true);
        return a2 != null && AmapLoc.RESULT_TYPE_GPS.equals(a2);
    }

    private void j() {
        try {
            if (this.h == null) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(com.mob.commons.k.a(131));
                this.h = new BroadcastReceiver() { // from class: com.mob.commons.b.g.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        String stringExtra;
                        ArrayList<String> stringArrayListExtra;
                        if (context == null || intent == null) {
                            return;
                        }
                        try {
                            boolean z = false;
                            if (intent.getIntExtra(com.mob.commons.k.a(127), 0) == 2 && (stringArrayListExtra = intent.getStringArrayListExtra(com.mob.commons.k.a(128))) != null) {
                                z = stringArrayListExtra.contains(context.getPackageName());
                            }
                            if (!z || (stringExtra = intent.getStringExtra(com.mob.commons.k.a(129))) == null) {
                                return;
                            }
                            if (stringExtra.equals(com.mob.commons.k.a(71))) {
                                g.this.c.a(0L);
                                return;
                            }
                            if (stringExtra.equals(com.mob.commons.k.a(69))) {
                                g.this.d.a(0L);
                            } else if (stringExtra.equals(com.mob.commons.k.a(70))) {
                                g.this.e.a(0L);
                            } else if (stringExtra.equals(com.mob.commons.k.a(75))) {
                                g.this.f.a(0L);
                            }
                        } catch (Throwable unused) {
                        }
                    }
                };
                this.a.registerReceiver(this.h, intentFilter, com.mob.commons.k.a(132), null);
            }
        } catch (Throwable unused) {
        }
    }

    @Override // com.mob.commons.b.f
    public synchronized String b() {
        if (this.a == null) {
            return null;
        }
        return a(this.a.getApplicationContext(), this.f, false);
    }

    @Override // com.mob.commons.b.f
    public synchronized String e() {
        if (this.a == null) {
            return null;
        }
        return a(this.a.getApplicationContext(), this.d, false);
    }

    @Override // com.mob.commons.b.f
    public synchronized String f() {
        if (this.a == null) {
            return null;
        }
        return a(this.a.getApplicationContext(), this.c, false);
    }

    @Override // com.mob.commons.b.f
    public synchronized String g() {
        if (this.a == null) {
            return null;
        }
        return a(this.a.getApplicationContext(), this.e, false);
    }
}
