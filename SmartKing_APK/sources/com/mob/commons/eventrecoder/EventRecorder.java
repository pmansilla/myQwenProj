package com.mob.commons.eventrecoder;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.mob.MobSDK;
import com.mob.commons.LockAction;
import com.mob.commons.e;
import com.mob.tools.MobLog;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.FileLocker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/* loaded from: classes.dex */
public final class EventRecorder implements PublicMemberKeeper {
    private static File a;
    private static FileOutputStream b;

    private static final void a(LockAction lockAction) {
        e.a(new File(MobSDK.getContext().getFilesDir(), "comm/locks/.mrlock"), lockAction);
    }

    private static final void a(final String str) {
        a(new LockAction() { // from class: com.mob.commons.eventrecoder.EventRecorder.2
            @Override // com.mob.commons.LockAction
            public boolean run(FileLocker fileLocker) {
                try {
                    EventRecorder.b.write(str.getBytes("utf-8"));
                    EventRecorder.b.flush();
                    return false;
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                    return false;
                }
            }
        });
    }

    public static final synchronized void addBegin(String str, String str2) {
        synchronized (EventRecorder.class) {
            a(str + SQLBuilder.BLANK + str2 + " 0\n");
        }
    }

    public static final synchronized void addEnd(String str, String str2) {
        synchronized (EventRecorder.class) {
            a(str + SQLBuilder.BLANK + str2 + " 1\n");
        }
    }

    public static final synchronized String checkRecord(final String str) {
        synchronized (EventRecorder.class) {
            final LinkedList linkedList = new LinkedList();
            a(new LockAction() { // from class: com.mob.commons.eventrecoder.EventRecorder.3
                @Override // com.mob.commons.LockAction
                public boolean run(FileLocker fileLocker) {
                    int indexOf;
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(EventRecorder.a), "utf-8"));
                        for (String readLine = bufferedReader.readLine(); !TextUtils.isEmpty(readLine); readLine = bufferedReader.readLine()) {
                            String[] split = readLine.split(SQLBuilder.BLANK);
                            if (str.equals(split[0])) {
                                if (AmapLoc.RESULT_TYPE_GPS.equals(split[2])) {
                                    linkedList.add(split[1]);
                                } else if (AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(split[2]) && (indexOf = linkedList.indexOf(split[1])) != -1) {
                                    linkedList.remove(indexOf);
                                }
                            }
                        }
                        bufferedReader.close();
                    } catch (Throwable th) {
                        MobLog.getInstance().d(th);
                    }
                    return false;
                }
            });
            if (linkedList.size() <= 0) {
                return null;
            }
            return (String) linkedList.get(0);
        }
    }

    public static final synchronized void clear() {
        synchronized (EventRecorder.class) {
            a(new LockAction() { // from class: com.mob.commons.eventrecoder.EventRecorder.4
                @Override // com.mob.commons.LockAction
                public boolean run(FileLocker fileLocker) {
                    try {
                        EventRecorder.b.close();
                        EventRecorder.a.delete();
                        File unused = EventRecorder.a = new File(MobSDK.getContext().getFilesDir(), ".mrecord");
                        EventRecorder.a.createNewFile();
                        FileOutputStream unused2 = EventRecorder.b = new FileOutputStream(EventRecorder.a, true);
                        return false;
                    } catch (Throwable th) {
                        MobLog.getInstance().w(th);
                        return false;
                    }
                }
            });
        }
    }

    public static final synchronized void prepare() {
        synchronized (EventRecorder.class) {
            a(new LockAction() { // from class: com.mob.commons.eventrecoder.EventRecorder.1
                @Override // com.mob.commons.LockAction
                public boolean run(FileLocker fileLocker) {
                    try {
                        File unused = EventRecorder.a = new File(MobSDK.getContext().getFilesDir(), ".mrecord");
                        if (!EventRecorder.a.exists()) {
                            EventRecorder.a.createNewFile();
                        }
                        FileOutputStream unused2 = EventRecorder.b = new FileOutputStream(EventRecorder.a, true);
                        return false;
                    } catch (Throwable th) {
                        MobLog.getInstance().w(th);
                        return false;
                    }
                }
            });
        }
    }
}
