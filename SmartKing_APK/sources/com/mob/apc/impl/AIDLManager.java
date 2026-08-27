package com.mob.apc.impl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.mob.MobACService;
import com.mob.apc.APCException;
import com.mob.apc.APCMessage;
import com.mob.apc.MobAPC;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class AIDLManager implements ServiceConnection {
    private static final ThreadPoolExecutor AIDL_THREAD_POOL;
    private final ConcurrentHashMap<String, IAidlInterface> serverBinderMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, byte[]> lockMap = new ConcurrentHashMap<>();

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 8, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque());
        AIDL_THREAD_POOL = threadPoolExecutor;
        try {
            threadPoolExecutor.allowCoreThreadTimeOut(true);
        } catch (Throwable unused) {
        }
    }

    private Runnable asyncSendAIDLMessage(final String str, final InnerMessage innerMessage, final long j, final BlockingQueue<InnerMessage> blockingQueue) {
        Runnable runnable = new Runnable() { // from class: com.mob.apc.impl.AIDLManager.1
            @Override // java.lang.Runnable
            public void run() {
                InnerMessage innerMessage2;
                try {
                    try {
                        blockingQueue.offer(AIDLManager.this.realSendAIDLMessage(str, innerMessage, j));
                    } catch (Throwable th) {
                        InnerMessage innerMessage3 = null;
                        try {
                            innerMessage2 = new InnerMessage(null, innerMessage.businessID, j);
                        } catch (Throwable th2) {
                            th = th2;
                        }
                        try {
                            if (th instanceof APCException) {
                                innerMessage2.apcException = th;
                            }
                            blockingQueue.offer(innerMessage2);
                        } catch (Throwable th3) {
                            th = th3;
                            innerMessage3 = innerMessage2;
                            blockingQueue.offer(innerMessage3);
                            throw th;
                        }
                    }
                } catch (Throwable th4) {
                    MAPCLog.getInstance().d(th4);
                }
            }
        };
        AIDL_THREAD_POOL.execute(runnable);
        return runnable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public InnerMessage realSendAIDLMessage(String str, InnerMessage innerMessage, long j) throws Throwable {
        MAPCLog.getInstance().i("[realSendAIDLMessage] pkg: %s, InnerMessage: %s, timeout: %s", str, innerMessage, Long.valueOf(j));
        IAidlInterface iAidlInterface = this.serverBinderMap.get(str);
        if (iAidlInterface != null) {
            try {
                if (iAidlInterface.isBinderAlive()) {
                    MAPCLog.getInstance().i("[realSendAIDLMessage] serverBinder %s is alive.", str);
                    return iAidlInterface.send(innerMessage);
                }
            } catch (RemoteException e) {
                MAPCLog.getInstance().i("[realSendAIDLMessage] serverBinder send error: %s %s", str, e.getMessage());
                MAPCLog.getInstance().d(e);
            }
        }
        Intent intent = new Intent();
        intent.setClassName(str, MobACService.class.getName());
        try {
            boolean bindService = MobAPC.getContext().bindService(intent, this, 1);
            MAPCLog.getInstance().i("[realSendAIDLMessage] rebind service: %s %s", str, Boolean.valueOf(bindService));
            if (!bindService) {
                throw new APCException(1003, String.format("service %s bind failed", str));
            }
            try {
                byte[] bArr = this.lockMap.get(str);
                if (bArr == null) {
                    bArr = new byte[0];
                    this.lockMap.put(str, bArr);
                }
                synchronized (bArr) {
                    bArr.wait(j);
                }
                IAidlInterface iAidlInterface2 = this.serverBinderMap.get(str);
                MAPCLog.getInstance().i("[realSendAIDLMessage] rebind service binder: %s %s", str, iAidlInterface2);
                if (iAidlInterface2 == null) {
                    throw new APCException(1001, String.format("service binder %s is null or timeout", str));
                }
                try {
                    return iAidlInterface2.send(innerMessage);
                } catch (RemoteException e2) {
                    throw new APCException(1004, String.format("service binder %s send message RemoteException: %s", str, e2.getMessage()));
                }
            } catch (Throwable th) {
                MAPCLog.getInstance().i("[realSendAIDLMessage] service binder %s send exception: %s", str, th.getMessage());
                throw new APCException(th);
            }
        } catch (Throwable th2) {
            throw new APCException(1002, "service bind exception: " + th2.getMessage());
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        try {
            String packageName = componentName.getPackageName();
            MAPCLog.getInstance().i("[AIDLMessager][onServiceConnected] pkg: %s", packageName);
            this.serverBinderMap.put(packageName, IAidlInterface.asInterface(iBinder));
            byte[] remove = this.lockMap.remove(packageName);
            if (remove != null) {
                synchronized (remove) {
                    remove.notifyAll();
                }
            }
        } catch (Throwable th) {
            MAPCLog.getInstance().i("[AIDLMessager][onServiceConnected] exception: %s", th.getMessage());
            MAPCLog.getInstance().d(th);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        try {
            String packageName = componentName.getPackageName();
            MAPCLog.getInstance().i("[AIDLMessager][onServiceDisconnected] pkg: %s", packageName);
            this.serverBinderMap.remove(packageName);
        } catch (Throwable th) {
            MAPCLog.getInstance().d(th);
            MAPCLog.getInstance().i("[AIDLMessager][onServiceDisconnected] exception: %s", th.getMessage());
        }
    }

    public APCMessage sendAIDLMessage(String str, String str2, APCMessage aPCMessage, long j) throws Throwable {
        InnerMessage innerMessage;
        MAPCLog.getInstance().i("[sendAIDLMessage] pkg: %s, businessID: %s, apcMessage: %s, timeout: %s", str, str2, aPCMessage, Long.valueOf(j));
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        try {
            Runnable asyncSendAIDLMessage = asyncSendAIDLMessage(str, new InnerMessage(aPCMessage, str2, j), j, linkedBlockingQueue);
            if (j <= 0) {
                innerMessage = (InnerMessage) linkedBlockingQueue.take();
            } else {
                InnerMessage innerMessage2 = (InnerMessage) linkedBlockingQueue.poll(j, TimeUnit.MILLISECONDS);
                if (innerMessage2 == null) {
                    AIDL_THREAD_POOL.remove(asyncSendAIDLMessage);
                }
                innerMessage = innerMessage2;
            }
            if (innerMessage != null) {
                if (innerMessage.apcMessage != null) {
                    return innerMessage.apcMessage;
                }
                if (innerMessage.apcException != null) {
                    throw innerMessage.apcException;
                }
            }
            throw new APCException("[sendAIDLMessage] callback is null or timeout.");
        } catch (Throwable th) {
            MAPCLog.getInstance().i("[sendAIDLMessage] exception: %s", th.getMessage());
            throw new APCException(th);
        }
    }
}
