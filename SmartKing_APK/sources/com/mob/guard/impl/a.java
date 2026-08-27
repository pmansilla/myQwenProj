package com.mob.guard.impl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.mob.MobSDK;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes.dex */
public class a {
    private static a j = new a();
    private ExecutorService a = Executors.newSingleThreadExecutor();
    private ExecutorService b = Executors.newSingleThreadExecutor();
    private ConcurrentHashMap<String, LinkedBlockingQueue<Boolean>> c = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, String> d = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, SelectionKey> e = new ConcurrentHashMap<>();
    private Socket f = null;
    private PendingIntent g = null;
    private int h = 5;
    private PingBroadcast i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.mob.guard.impl.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class RunnableC0057a implements Runnable {
        final /* synthetic */ BlockingQueue a;

        RunnableC0057a(BlockingQueue blockingQueue) {
            this.a = blockingQueue;
        }

        @Override // java.lang.Runnable
        public void run() {
            Selector open;
            try {
                ServerSocketChannel open2 = ServerSocketChannel.open();
                open2.configureBlocking(false);
                try {
                    open2.socket().bind(new InetSocketAddress(59898));
                    e.a().d("[GuardConnect] registerServerSocket success", new Object[0]);
                    this.a.offer(Boolean.TRUE);
                    open = Selector.open();
                    open2.register(open, 16);
                } catch (Throwable unused) {
                    e.a().d("[GuardConnect] registerServerSocket failed", new Object[0]);
                    this.a.offer(Boolean.FALSE);
                    return;
                }
            } catch (Throwable th) {
                e.a().d("[GuardConnect] serverSocket exception: " + th.getMessage(), new Object[0]);
                e.a().e(th);
                a.this.a();
                return;
            }
            while (open != null && open.isOpen()) {
                if (open.select() > 0) {
                    Iterator<SelectionKey> it = open.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey next = it.next();
                        it.remove();
                        if (next.isValid() && next.isAcceptable()) {
                            SocketChannel accept = ((ServerSocketChannel) next.channel()).accept();
                            accept.configureBlocking(false);
                            accept.register(open, 1);
                        }
                        if (next.isValid() && next.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) next.channel();
                            ByteBuffer allocate = ByteBuffer.allocate(1024);
                            int read = socketChannel.read(allocate);
                            e.a().d("[GuardConnect] serverSocket received bytes:" + read, new Object[0]);
                            if (read > 0) {
                                String str = new String(allocate.array(), 0, read);
                                e.a().d("[GuardConnect] serverSocket received msg:" + str, new Object[0]);
                                if (!"p".equals(str)) {
                                    if (str.startsWith("lg_")) {
                                        try {
                                            int port = ((InetSocketAddress) socketChannel.socket().getRemoteSocketAddress()).getPort();
                                            String substring = str.substring(3);
                                            a.this.d.put(Integer.valueOf(port), substring);
                                            a.this.e.put(Integer.valueOf(port), next);
                                            com.mob.guard.impl.c.e().a(substring);
                                        } catch (Throwable th2) {
                                            e.a().d(th2);
                                        }
                                    } else if (str.startsWith("chk_cb_")) {
                                        a.this.a(str.substring(7));
                                    }
                                }
                            } else {
                                try {
                                    int port2 = ((InetSocketAddress) socketChannel.socket().getRemoteSocketAddress()).getPort();
                                    String str2 = (String) a.this.d.remove(Integer.valueOf(port2));
                                    a.this.e.remove(Integer.valueOf(port2));
                                    e.a().d("[GuardConnect] serverSocket received client disconnect pkg: " + str2, new Object[0]);
                                    com.mob.guard.impl.c.e().a(str2, false);
                                } catch (Throwable th3) {
                                    e.a().d(th3);
                                }
                                try {
                                    socketChannel.close();
                                } catch (Throwable th4) {
                                    e.a().d(th4);
                                }
                            }
                            e.a().d("[GuardConnect] serverSocket exception: " + th.getMessage(), new Object[0]);
                            e.a().e(th);
                            a.this.a();
                            return;
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (a.this.f != null) {
                    a.this.f.close();
                    a.this.f = null;
                }
                a.this.f = new Socket(a.this.c(), 59898);
                if (a.this.f.isConnected()) {
                    a.this.h = 5;
                    e.a().d("[GuardConnect] clientSocket connected", new Object[0]);
                    try {
                        String packageName = MobSDK.getContext().getPackageName();
                        OutputStream outputStream = a.this.f.getOutputStream();
                        outputStream.write(("lg_" + packageName).getBytes("utf-8"));
                        outputStream.flush();
                    } catch (Throwable th) {
                        e.a().e(th);
                    }
                    a.this.f();
                }
                InputStream inputStream = a.this.f.getInputStream();
                while (a.this.f.isConnected() && !a.this.f.isClosed()) {
                    byte[] bArr = new byte[1024];
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        e.a().d("[GuardConnect] client received server disconnect", new Object[0]);
                        a.this.a(false);
                    } else {
                        String str = new String(bArr, 0, read);
                        e.a().d("[GuardConnect] client received server msg: " + str, new Object[0]);
                        if ("chk".equals(str)) {
                            try {
                                String packageName2 = MobSDK.getContext().getPackageName();
                                OutputStream outputStream2 = a.this.f.getOutputStream();
                                outputStream2.write(("chk_cb_" + packageName2).getBytes("utf-8"));
                                outputStream2.flush();
                                e.a().d("[GuardConnect] client send alive check msg callback to server: chk_cb_" + packageName2, new Object[0]);
                            } catch (Throwable th2) {
                                e.a().e(th2);
                            }
                        }
                    }
                }
            } catch (SocketException e) {
                e.a().d("[GuardConnect] client received socket exception: " + e.getMessage(), new Object[0]);
                e.a().d(e);
                a.this.a(true);
            } catch (Throwable th3) {
                e.a().d("[GuardConnect] clientSocket exception: " + th3.getMessage(), new Object[0]);
                e.a().d(th3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements Runnable {
        final /* synthetic */ boolean a;

        c(boolean z) {
            this.a = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (com.mob.guard.impl.c.e().b()) {
                    LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
                    a.this.a(linkedBlockingQueue);
                    try {
                        if (((Boolean) linkedBlockingQueue.take()).booleanValue()) {
                            com.mob.guard.impl.c.e().a((String) null, true);
                            return;
                        }
                    } catch (Throwable th) {
                        e.a().d(th);
                    }
                }
                if (a.this.h > 0) {
                    if (this.a && a.this.h < 5) {
                        try {
                            Thread.sleep((5 - a.this.h) * 1000);
                        } catch (Throwable unused) {
                        }
                    }
                    a.g(a.this);
                    a.this.e();
                }
            } catch (Throwable th2) {
                e.a().d(th2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements Runnable {
        d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                e.a().d("[GuardConnect] sendPing clientSocket: " + a.this.f, new Object[0]);
                if (a.this.f != null) {
                    OutputStream outputStream = a.this.f.getOutputStream();
                    outputStream.write("p".getBytes());
                    outputStream.flush();
                }
                a.this.g();
            } catch (Throwable th) {
                e.a().d(th);
            }
        }
    }

    private a() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        try {
            this.c.clear();
            this.d.clear();
            this.e.clear();
        } catch (Throwable th) {
            e.a().e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        LinkedBlockingQueue<Boolean> remove = this.c.remove(str);
        if (remove != null) {
            remove.offer(Boolean.TRUE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z) {
        e.a().d("[GuardConnect] onServerDisconnect maxRegisterClientFailedCount: " + this.h + ", isConnectException: " + z, new Object[0]);
        b();
        this.b.execute(new c(z));
    }

    private boolean a(SelectionKey selectionKey) {
        try {
            if (!selectionKey.isValid()) {
                return false;
            }
            ((SocketChannel) selectionKey.channel()).write(ByteBuffer.wrap("chk".getBytes("utf-8")));
            return true;
        } catch (Throwable th) {
            e.a().d(th);
            return false;
        }
    }

    private void b() {
        try {
            if (this.f != null) {
                this.f.close();
                this.f = null;
            }
            if (this.i != null) {
                MobSDK.getContext().unregisterReceiver(this.i);
            }
        } catch (Throwable th) {
            e.a().e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String c() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress nextElement = inetAddresses.nextElement();
                    if (!nextElement.isLoopbackAddress() && (nextElement instanceof Inet4Address)) {
                        return nextElement.getHostAddress();
                    }
                }
            }
            return null;
        } catch (Throwable th) {
            e.a().e(th);
            return null;
        }
    }

    public static a d() {
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        try {
            if (this.i == null) {
                this.i = new PingBroadcast();
            } else {
                try {
                    MobSDK.getContext().unregisterReceiver(this.i);
                } catch (Throwable th) {
                    e.a().d(th);
                }
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.mob.guard.intent.PING");
            MobSDK.getContext().registerReceiver(this.i, intentFilter);
            g();
        } catch (Throwable th2) {
            e.a().d(th2);
        }
    }

    static /* synthetic */ int g(a aVar) {
        int i = aVar.h;
        aVar.h = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        e.a().d("[GuardConnect] scheduleNextPing", new Object[0]);
        AlarmManager alarmManager = (AlarmManager) MobSDK.getContext().getSystemService(NotificationCompat.CATEGORY_ALARM);
        PendingIntent pendingIntent = this.g;
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
        Intent intent = new Intent("com.mob.guard.intent.PING");
        intent.setPackage(MobSDK.getContext().getPackageName());
        this.g = PendingIntent.getBroadcast(MobSDK.getContext(), 0, intent, AMapEngineUtils.HALF_MAX_P20_WIDTH);
        long elapsedRealtime = SystemClock.elapsedRealtime() + 240000;
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            alarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, this.g);
        } else if (i >= 19) {
            alarmManager.setExact(2, elapsedRealtime, this.g);
        } else {
            alarmManager.set(2, elapsedRealtime, this.g);
        }
    }

    public int a(String str, LinkedBlockingQueue linkedBlockingQueue) {
        int i = 0;
        for (Map.Entry<Integer, String> entry : this.d.entrySet()) {
            if (entry.getValue().equals(str) && entry.getKey() != null) {
                this.c.put(str, linkedBlockingQueue);
                SelectionKey selectionKey = this.e.get(entry.getKey());
                if (selectionKey != null) {
                    i = a(selectionKey) ? 1 : 2;
                }
            }
        }
        return i;
    }

    public void a(BlockingQueue<Boolean> blockingQueue) {
        this.a.execute(new RunnableC0057a(blockingQueue));
    }

    public void e() {
        this.a.execute(new b());
    }

    public void h() {
        this.b.execute(new d());
    }
}
