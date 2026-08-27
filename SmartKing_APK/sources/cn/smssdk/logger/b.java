package cn.smssdk.logger;

import android.content.ContentValues;
import android.database.Cursor;
import cn.smssdk.utils.SMSLog;
import com.mob.MobSDK;
import com.mob.tools.utils.SQLiteHelper;
import java.util.ArrayList;
import java.util.List;

/* compiled from: DBLoggerEngine.java */
/* loaded from: classes.dex */
public class b implements ILoggerEngine {
    private static b b;
    private SQLiteHelper.SingleTableDB a = SQLiteHelper.getDatabase(MobSDK.getContext(), "loggerdb");

    private b() {
        this.a.addField("method", "INTEGER", false);
        this.a.addField("beginTime", "INTEGER", false);
        this.a.addField("totalDesc", "VARCHAR", false);
    }

    public static b a() {
        if (b == null) {
            synchronized (b.class) {
                if (b == null) {
                    b = new b();
                }
            }
        }
        return b;
    }

    @Override // cn.smssdk.logger.ILoggerEngine
    public void deleteAllLogItems() {
        try {
            SQLiteHelper.delete(this.a, null, null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // cn.smssdk.logger.ILoggerEngine
    public void deleteLogItem(long j) {
        try {
            SQLiteHelper.delete(this.a, "beginTime=?", new String[]{String.valueOf(j)});
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // cn.smssdk.logger.ILoggerEngine
    public List<c> getLogList() {
        ArrayList arrayList = new ArrayList();
        try {
            Cursor query = SQLiteHelper.query(this.a, null, null, null, null);
            if (query != null) {
                query.moveToFirst();
                while (!query.isAfterLast()) {
                    int i = query.getInt(query.getColumnIndex("method"));
                    int i2 = query.getInt(query.getColumnIndex("beginTime"));
                    String string = query.getString(query.getColumnIndex("totalDesc"));
                    c cVar = new c();
                    cVar.b(i);
                    cVar.a(i2);
                    cVar.a(string);
                    arrayList.add(cVar);
                    query.moveToNext();
                }
                query.close();
            }
        } catch (Throwable th) {
            SMSLog.getInstance().d(th, SMSLog.FORMAT_SIMPLE, "query db error");
        }
        return arrayList;
    }

    @Override // cn.smssdk.logger.ILoggerEngine
    public void insertOneRequestLog(int i, long j, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("method", Integer.valueOf(i));
        contentValues.put("beginTime", Long.valueOf(j));
        contentValues.put("totalDesc", str);
        try {
            SQLiteHelper.insert(this.a, contentValues);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
