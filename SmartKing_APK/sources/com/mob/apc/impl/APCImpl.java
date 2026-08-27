package com.mob.apc.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.text.TextUtils;
import com.mob.MobACService;
import com.mob.apc.APCException;
import com.mob.apc.APCMessage;
import com.mob.apc.MobAPC;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class APCImpl {
    private static APCImpl instance = new APCImpl();
    private HashMap<String, MobAPC.MobAPCMessageListener> apcMessageListenerMap = new HashMap<>();
    private AIDLManager aidlManager = new AIDLManager();

    private APCImpl() {
    }

    public static APCImpl getInstance() {
        return instance;
    }

    public void addMobIpcMsgListener(String str, MobAPC.MobAPCMessageListener mobAPCMessageListener) {
        MAPCLog.getInstance().i("[addMobIpcMsgListener] %s", str);
        this.apcMessageListenerMap.put(str, mobAPCMessageListener);
    }

    public List<String> getMAPCServiceList() {
        ArrayList arrayList = new ArrayList();
        try {
            List<ResolveInfo> queryIntentServices = MobAPC.getContext().getPackageManager().queryIntentServices(new Intent("com.mob.service.action.MOB_AC_SERVICE"), 0);
            if (queryIntentServices != null) {
                for (ResolveInfo resolveInfo : queryIntentServices) {
                    String str = resolveInfo.serviceInfo.packageName;
                    if (resolveInfo.serviceInfo.exported && !MobAPC.getContext().getPackageName().equals(str)) {
                        arrayList.add(resolveInfo.serviceInfo.packageName);
                    }
                }
            }
        } catch (Throwable th) {
            MAPCLog.getInstance().d(th);
        }
        MAPCLog.getInstance().i("[getMAPCServiceList] list: %s", arrayList);
        return arrayList;
    }

    public void init(final Context context) {
        try {
            Intent intent = new Intent(context, (Class<?>) MobACService.class);
            try {
                context.startService(intent);
            } catch (Throwable unused) {
                context.bindService(intent, new ServiceConnection() { // from class: com.mob.apc.impl.APCImpl.1
                    @Override // android.content.ServiceConnection
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        try {
                            context.unbindService(this);
                        } catch (Throwable unused2) {
                        }
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceDisconnected(ComponentName componentName) {
                    }
                }, 1);
            }
        } catch (Throwable th) {
            MAPCLog.getInstance().d(th);
        }
    }

    public InnerMessage onAIDLMessageReceive(InnerMessage innerMessage) {
        try {
            MobAPC.MobAPCMessageListener mobAPCMessageListener = this.apcMessageListenerMap.get(innerMessage.businessID);
            MAPCLog.getInstance().i("[onAIDLMessageReceive] innerMessage: %s, listener: %s", innerMessage, mobAPCMessageListener);
            if (mobAPCMessageListener == null) {
                return null;
            }
            APCMessage onMessageReceive = mobAPCMessageListener.onMessageReceive(innerMessage.pkg, innerMessage.apcMessage, innerMessage.timeout);
            MAPCLog.getInstance().i("[onAIDLMessageReceive] listener apcMessage: %s", onMessageReceive);
            return new InnerMessage(onMessageReceive, innerMessage.businessID, innerMessage.timeout);
        } catch (Throwable th) {
            MAPCLog.getInstance().i("[onAIDLMessageReceive] exception %s", th.getMessage());
            MAPCLog.getInstance().d(th);
            return null;
        }
    }

    public APCMessage sendMessage(int i, String str, String str2, APCMessage aPCMessage, long j) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            MAPCLog.getInstance().i("[sendMessage] pkg not allowed null.", new Object[0]);
            throw new APCException("pkg not allowed null.");
        }
        if (aPCMessage == null) {
            MAPCLog.getInstance().i("[sendMessage] param not allowed null.", new Object[0]);
            throw new APCException("param not allowed null.");
        }
        if (i == 1) {
            return this.aidlManager.sendAIDLMessage(str, str2, aPCMessage, j);
        }
        MAPCLog.getInstance().i("type " + i + " not support.", new Object[0]);
        throw new APCException("type " + i + " not support.");
    }
}
