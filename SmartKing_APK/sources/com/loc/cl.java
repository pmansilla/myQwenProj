package com.loc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import com.sun.mail.imap.IMAPStore;

/* compiled from: SPUtil.java */
/* loaded from: classes.dex */
public final class cl {
    public static int a(Context context, String str, String str2) {
        try {
            return context.getSharedPreferences(str, 0).getInt(str2, -1);
        } catch (Throwable unused) {
            return -1;
        }
    }

    public static void a(Context context, String str, String str2, int i) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putString(IMAPStore.ID_COMMAND, str2);
            edit.putInt(IMAPStore.ID_VERSION, i);
            a(edit);
        } catch (Throwable unused) {
        }
    }

    public static void a(Context context, String str, String str2, String str3) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putString(str2, str3);
            a(edit);
        } catch (Throwable unused) {
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.loc.cl$1] */
    @SuppressLint({"NewApi"})
    private static void a(final SharedPreferences.Editor editor) {
        if (editor == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            try {
                new AsyncTask<Void, Void, Void>() { // from class: com.loc.cl.1
                    private Void a() {
                        try {
                            if (editor == null) {
                                return null;
                            }
                            editor.commit();
                            return null;
                        } catch (Throwable unused) {
                            return null;
                        }
                    }

                    @Override // android.os.AsyncTask
                    protected final /* synthetic */ Void doInBackground(Void[] voidArr) {
                        return a();
                    }
                }.execute(null, null, null);
            } catch (Throwable unused) {
            }
        }
    }

    public static String b(Context context, String str, String str2) {
        try {
            return context.getSharedPreferences(str, 0).getString(str2, null);
        } catch (Throwable unused) {
            return null;
        }
    }
}
