package me.panpf.sketch.cache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;
import me.panpf.sketch.Configuration;
import me.panpf.sketch.SLog;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.util.DiskLruCache;
import me.panpf.sketch.util.NoSpaceException;
import me.panpf.sketch.util.SketchMD5Utils;
import me.panpf.sketch.util.SketchUtils;
import me.panpf.sketch.util.UnableCreateDirException;
import me.panpf.sketch.util.UnableCreateFileException;

/* loaded from: classes2.dex */
public class LruDiskCache implements DiskCache {
    private static final String NAME = "LruDiskCache";
    private int appVersionCode;
    private DiskLruCache cache;
    private File cacheDir;
    private boolean closed;
    private Configuration configuration;
    private Context context;
    private boolean disabled;
    private Map<String, ReentrantLock> editLockMap;
    private int maxSize;

    /* loaded from: classes2.dex */
    public static class LruDiskCacheEditor implements DiskCache.Editor {
        private DiskLruCache.Editor diskEditor;

        public LruDiskCacheEditor(DiskLruCache.Editor editor) {
            this.diskEditor = editor;
        }

        @Override // me.panpf.sketch.cache.DiskCache.Editor
        public void abort() {
            try {
                this.diskEditor.abort();
            } catch (IOException | DiskLruCache.EditorChangedException | DiskLruCache.FileNotExistException e) {
                e.printStackTrace();
            }
        }

        @Override // me.panpf.sketch.cache.DiskCache.Editor
        public void commit() throws IOException, DiskLruCache.EditorChangedException, DiskLruCache.ClosedException, DiskLruCache.FileNotExistException {
            this.diskEditor.commit();
        }

        @Override // me.panpf.sketch.cache.DiskCache.Editor
        public OutputStream newOutputStream() throws IOException {
            return this.diskEditor.newOutputStream(0);
        }
    }

    /* loaded from: classes2.dex */
    public static class LruDiskCacheEntry implements DiskCache.Entry {
        private String key;
        private DiskLruCache.SimpleSnapshot snapshot;

        public LruDiskCacheEntry(String str, DiskLruCache.SimpleSnapshot simpleSnapshot) {
            this.key = str;
            this.snapshot = simpleSnapshot;
        }

        @Override // me.panpf.sketch.cache.DiskCache.Entry
        public boolean delete() {
            try {
                this.snapshot.getDiskLruCache().remove(this.snapshot.getKey());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (DiskLruCache.ClosedException e2) {
                e2.printStackTrace();
                return false;
            }
        }

        @Override // me.panpf.sketch.cache.DiskCache.Entry
        @NonNull
        public File getFile() {
            return this.snapshot.getFile(0);
        }

        @Override // me.panpf.sketch.cache.DiskCache.Entry
        @NonNull
        public String getKey() {
            return this.key;
        }

        @Override // me.panpf.sketch.cache.DiskCache.Entry
        @NonNull
        public InputStream newInputStream() throws IOException {
            return this.snapshot.newInputStream(0);
        }
    }

    public LruDiskCache(Context context, Configuration configuration, int i, int i2) {
        Context applicationContext = context.getApplicationContext();
        this.context = applicationContext;
        this.maxSize = i2;
        this.appVersionCode = i;
        this.configuration = configuration;
        this.cacheDir = SketchUtils.getDefaultSketchCacheDir(applicationContext, DiskCache.DISK_CACHE_DIR_NAME, true);
    }

    protected boolean checkCacheDir() {
        return this.cacheDir != null && this.cacheDir.exists();
    }

    protected boolean checkDiskCache() {
        return (this.cache == null || this.cache.isClosed()) ? false : true;
    }

    @Override // me.panpf.sketch.cache.DiskCache
    public synchronized void clear() {
        if (this.closed) {
            return;
        }
        if (this.cache != null) {
            try {
                this.cache.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.cache = null;
        }
        installDiskCache();
    }

    @Override // me.panpf.sketch.cache.DiskCache
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.cache != null) {
            try {
                this.cache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.cache = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0088 A[Catch: all -> 0x008f, TRY_LEAVE, TryCatch #4 {, blocks: (B:3:0x0001, B:9:0x0008, B:11:0x000c, B:13:0x0015, B:16:0x0024, B:18:0x002a, B:21:0x003b, B:24:0x0088, B:39:0x0048, B:42:0x0056, B:45:0x0062, B:29:0x0068, B:32:0x0076, B:35:0x0082, B:47:0x0030), top: B:2:0x0001, inners: #1, #2, #5 }] */
    @Override // me.panpf.sketch.cache.DiskCache
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized me.panpf.sketch.cache.DiskCache.Editor edit(@android.support.annotation.NonNull java.lang.String r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.closed     // Catch: java.lang.Throwable -> L8f
            r1 = 0
            if (r0 == 0) goto L8
            monitor-exit(r5)
            return r1
        L8:
            boolean r0 = r5.disabled     // Catch: java.lang.Throwable -> L8f
            if (r0 == 0) goto L24
            r0 = 131074(0x20002, float:1.83674E-40)
            boolean r0 = me.panpf.sketch.SLog.isLoggable(r0)     // Catch: java.lang.Throwable -> L8f
            if (r0 == 0) goto L22
            java.lang.String r0 = "LruDiskCache"
            java.lang.String r2 = "Disabled. Unable edit, key=%s"
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L8f
            r4 = 0
            r3[r4] = r6     // Catch: java.lang.Throwable -> L8f
            me.panpf.sketch.SLog.d(r0, r2, r3)     // Catch: java.lang.Throwable -> L8f
        L22:
            monitor-exit(r5)
            return r1
        L24:
            boolean r0 = r5.checkDiskCache()     // Catch: java.lang.Throwable -> L8f
            if (r0 == 0) goto L30
            boolean r0 = r5.checkCacheDir()     // Catch: java.lang.Throwable -> L8f
            if (r0 != 0) goto L3b
        L30:
            r5.installDiskCache()     // Catch: java.lang.Throwable -> L8f
            boolean r0 = r5.checkDiskCache()     // Catch: java.lang.Throwable -> L8f
            if (r0 != 0) goto L3b
            monitor-exit(r5)
            return r1
        L3b:
            me.panpf.sketch.util.DiskLruCache r0 = r5.cache     // Catch: me.panpf.sketch.util.DiskLruCache.ClosedException -> L47 java.io.IOException -> L67 java.lang.Throwable -> L8f
            java.lang.String r2 = r5.keyEncode(r6)     // Catch: me.panpf.sketch.util.DiskLruCache.ClosedException -> L47 java.io.IOException -> L67 java.lang.Throwable -> L8f
            me.panpf.sketch.util.DiskLruCache$Editor r0 = r0.edit(r2)     // Catch: me.panpf.sketch.util.DiskLruCache.ClosedException -> L47 java.io.IOException -> L67 java.lang.Throwable -> L8f
            r6 = r0
            goto L86
        L47:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L8f
            r5.installDiskCache()     // Catch: java.lang.Throwable -> L8f
            boolean r0 = r5.checkDiskCache()     // Catch: java.lang.Throwable -> L8f
            if (r0 != 0) goto L56
            monitor-exit(r5)
            return r1
        L56:
            me.panpf.sketch.util.DiskLruCache r0 = r5.cache     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L8f
            java.lang.String r6 = r5.keyEncode(r6)     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L8f
            me.panpf.sketch.util.DiskLruCache$Editor r6 = r0.edit(r6)     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L8f
            goto L86
        L61:
            r6 = move-exception
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L8f
        L65:
            r6 = r1
            goto L86
        L67:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L8f
            r5.installDiskCache()     // Catch: java.lang.Throwable -> L8f
            boolean r0 = r5.checkDiskCache()     // Catch: java.lang.Throwable -> L8f
            if (r0 != 0) goto L76
            monitor-exit(r5)
            return r1
        L76:
            me.panpf.sketch.util.DiskLruCache r0 = r5.cache     // Catch: java.lang.Throwable -> L81 java.lang.Throwable -> L8f
            java.lang.String r6 = r5.keyEncode(r6)     // Catch: java.lang.Throwable -> L81 java.lang.Throwable -> L8f
            me.panpf.sketch.util.DiskLruCache$Editor r6 = r0.edit(r6)     // Catch: java.lang.Throwable -> L81 java.lang.Throwable -> L8f
            goto L86
        L81:
            r6 = move-exception
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L8f
            goto L65
        L86:
            if (r6 == 0) goto L8d
            me.panpf.sketch.cache.LruDiskCache$LruDiskCacheEditor r1 = new me.panpf.sketch.cache.LruDiskCache$LruDiskCacheEditor     // Catch: java.lang.Throwable -> L8f
            r1.<init>(r6)     // Catch: java.lang.Throwable -> L8f
        L8d:
            monitor-exit(r5)
            return r1
        L8f:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: me.panpf.sketch.cache.LruDiskCache.edit(java.lang.String):me.panpf.sketch.cache.DiskCache$Editor");
    }

    @Override // me.panpf.sketch.cache.DiskCache
    public boolean exist(@NonNull String str) {
        if (this.closed) {
            return false;
        }
        if (this.disabled) {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Disabled. Unable judge exist, key=%s", str);
            }
            return false;
        }
        if (!checkDiskCache()) {
            installDiskCache();
            if (!checkDiskCache()) {
                return false;
            }
        }
        try {
            return this.cache.exist(keyEncode(str));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DiskLruCache.ClosedException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    @Override // me.panpf.sketch.cache.DiskCache
    public synchronized DiskCache.Entry get(@NonNull String str) {
        DiskLruCache.SimpleSnapshot simpleSnapshot;
        if (this.closed) {
            return null;
        }
        if (this.disabled) {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Disabled. Unable get, key=%s", str);
            }
            return null;
        }
        if (!checkDiskCache() || !checkCacheDir()) {
            installDiskCache();
            if (!checkDiskCache()) {
                return null;
            }
        }
        try {
            simpleSnapshot = this.cache.getSimpleSnapshot(keyEncode(str));
        } catch (IOException | DiskLruCache.ClosedException e) {
            e.printStackTrace();
            simpleSnapshot = null;
        }
        return simpleSnapshot != null ? new LruDiskCacheEntry(str, simpleSnapshot) : null;
    }

    @Override // me.panpf.sketch.cache.DiskCache
    @NonNull
    public synchronized File getCacheDir() {
        return this.cacheDir;
    }

    @Override // me.panpf.sketch.cache.DiskCache
    @NonNull
    public synchronized ReentrantLock getEditLock(@NonNull String str) {
        ReentrantLock reentrantLock;
        if (this.editLockMap == null) {
            synchronized (this) {
                if (this.editLockMap == null) {
                    this.editLockMap = new WeakHashMap();
                }
            }
        }
        reentrantLock = this.editLockMap.get(str);
        if (reentrantLock == null) {
            reentrantLock = new ReentrantLock();
            this.editLockMap.put(str, reentrantLock);
        }
        return reentrantLock;
    }

    @Override // me.panpf.sketch.cache.DiskCache
    public long getMaxSize() {
        return this.maxSize;
    }

    @Override // me.panpf.sketch.cache.DiskCache
    public synchronized long getSize() {
        if (this.closed) {
            return 0L;
        }
        if (!checkDiskCache()) {
            return 0L;
        }
        return this.cache.size();
    }

    protected synchronized void installDiskCache() {
        if (this.closed) {
            return;
        }
        if (this.cache != null) {
            try {
                this.cache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.cache = null;
        }
        try {
            this.cacheDir = SketchUtils.buildCacheDir(this.context, DiskCache.DISK_CACHE_DIR_NAME, true, 209715200L, true, true, 10);
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "diskCacheDir: %s", this.cacheDir.getPath());
            }
            try {
                this.cache = DiskLruCache.open(this.cacheDir, this.appVersionCode, 1, this.maxSize);
            } catch (IOException e2) {
                e2.printStackTrace();
                this.configuration.getErrorTracker().onInstallDiskCacheError(e2, this.cacheDir);
            }
        } catch (NoSpaceException | UnableCreateDirException | UnableCreateFileException e3) {
            e3.printStackTrace();
            this.configuration.getErrorTracker().onInstallDiskCacheError(e3, this.cacheDir);
        }
    }

    @Override // me.panpf.sketch.cache.DiskCache
    public synchronized boolean isClosed() {
        return this.closed;
    }

    @Override // me.panpf.sketch.cache.DiskCache
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override // me.panpf.sketch.cache.DiskCache
    @NonNull
    public String keyEncode(@NonNull String str) {
        return SketchMD5Utils.md5(str);
    }

    @Override // me.panpf.sketch.cache.DiskCache
    public void setDisabled(boolean z) {
        if (this.disabled != z) {
            this.disabled = z;
            if (z) {
                SLog.w(NAME, "setDisabled. %s", true);
            } else {
                SLog.w(NAME, "setDisabled. %s", false);
            }
        }
    }

    @NonNull
    public String toString() {
        return String.format("%s(maxSize=%s,appVersionCode=%d,cacheDir=%s)", NAME, Formatter.formatFileSize(this.context, this.maxSize), Integer.valueOf(this.appVersionCode), this.cacheDir.getPath());
    }
}
