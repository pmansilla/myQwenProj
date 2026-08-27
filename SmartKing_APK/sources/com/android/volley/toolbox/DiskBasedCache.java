package com.android.volley.toolbox;

import android.os.SystemClock;
import com.android.volley.Cache;
import com.android.volley.VolleyLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class DiskBasedCache implements Cache {
    private static final int CACHE_VERSION = 2;
    private static final int DEFAULT_DISK_USAGE_BYTES = 5242880;
    private static final float HYSTERESIS_FACTOR = 0.9f;
    private final Map<String, CacheHeader> mEntries;
    private final int mMaxCacheSizeInBytes;
    private final File mRootDirectory;
    private long mTotalSize;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CacheHeader {
        public String etag;
        public String key;
        public Map<String, String> responseHeaders;
        public long serverDate;
        public long size;
        public long softTtl;
        public long ttl;

        private CacheHeader() {
        }

        public CacheHeader(String str, Cache.Entry entry) {
            this.key = str;
            this.size = entry.data.length;
            this.etag = entry.etag;
            this.serverDate = entry.serverDate;
            this.ttl = entry.ttl;
            this.softTtl = entry.softTtl;
            this.responseHeaders = entry.responseHeaders;
        }

        public static CacheHeader readHeader(InputStream inputStream) throws IOException {
            CacheHeader cacheHeader = new CacheHeader();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            if (objectInputStream.readByte() != 2) {
                throw new IOException();
            }
            cacheHeader.key = objectInputStream.readUTF();
            cacheHeader.etag = objectInputStream.readUTF();
            if (cacheHeader.etag.equals("")) {
                cacheHeader.etag = null;
            }
            cacheHeader.serverDate = objectInputStream.readLong();
            cacheHeader.ttl = objectInputStream.readLong();
            cacheHeader.softTtl = objectInputStream.readLong();
            cacheHeader.responseHeaders = readStringStringMap(objectInputStream);
            return cacheHeader;
        }

        private static Map<String, String> readStringStringMap(ObjectInputStream objectInputStream) throws IOException {
            int readInt = objectInputStream.readInt();
            Map<String, String> emptyMap = readInt == 0 ? Collections.emptyMap() : new HashMap<>(readInt);
            for (int i = 0; i < readInt; i++) {
                emptyMap.put(objectInputStream.readUTF().intern(), objectInputStream.readUTF().intern());
            }
            return emptyMap;
        }

        private static void writeStringStringMap(Map<String, String> map, ObjectOutputStream objectOutputStream) throws IOException {
            if (map == null) {
                objectOutputStream.writeInt(0);
                return;
            }
            objectOutputStream.writeInt(map.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                objectOutputStream.writeUTF(entry.getKey());
                objectOutputStream.writeUTF(entry.getValue());
            }
        }

        public Cache.Entry toCacheEntry(byte[] bArr) {
            Cache.Entry entry = new Cache.Entry();
            entry.data = bArr;
            entry.etag = this.etag;
            entry.serverDate = this.serverDate;
            entry.ttl = this.ttl;
            entry.softTtl = this.softTtl;
            entry.responseHeaders = this.responseHeaders;
            return entry;
        }

        public boolean writeHeader(OutputStream outputStream) {
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeByte(2);
                objectOutputStream.writeUTF(this.key);
                objectOutputStream.writeUTF(this.etag == null ? "" : this.etag);
                objectOutputStream.writeLong(this.serverDate);
                objectOutputStream.writeLong(this.ttl);
                objectOutputStream.writeLong(this.softTtl);
                writeStringStringMap(this.responseHeaders, objectOutputStream);
                objectOutputStream.flush();
                return true;
            } catch (IOException e) {
                VolleyLog.d("%s", e.toString());
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CountingInputStream extends FilterInputStream {
        private int bytesRead;

        private CountingInputStream(InputStream inputStream) {
            super(inputStream);
            this.bytesRead = 0;
        }

        /* synthetic */ CountingInputStream(InputStream inputStream, CountingInputStream countingInputStream) {
            this(inputStream);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int read = super.read();
            if (read != -1) {
                this.bytesRead++;
            }
            return read;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                this.bytesRead += read;
            }
            return read;
        }
    }

    public DiskBasedCache(File file) {
        this(file, DEFAULT_DISK_USAGE_BYTES);
    }

    public DiskBasedCache(File file, int i) {
        this.mEntries = new LinkedHashMap(16, 0.75f, true);
        this.mTotalSize = 0L;
        this.mRootDirectory = file;
        this.mMaxCacheSizeInBytes = i;
    }

    private String getFilenameForKey(String str) {
        int length = str.length() / 2;
        return String.valueOf(String.valueOf(str.substring(0, length).hashCode())) + String.valueOf(str.substring(length).hashCode());
    }

    private void pruneIfNeeded(int i) {
        long j;
        long j2 = i;
        if (this.mTotalSize + j2 < this.mMaxCacheSizeInBytes) {
            return;
        }
        if (VolleyLog.DEBUG) {
            VolleyLog.v("Pruning old cache entries.", new Object[0]);
        }
        long j3 = this.mTotalSize;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Iterator<Map.Entry<String, CacheHeader>> it = this.mEntries.entrySet().iterator();
        int i2 = 0;
        while (it.hasNext()) {
            CacheHeader value = it.next().getValue();
            if (getFileForKey(value.key).delete()) {
                j = j2;
                this.mTotalSize -= value.size;
            } else {
                j = j2;
                VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", value.key, getFilenameForKey(value.key));
            }
            it.remove();
            i2++;
            if (((float) (this.mTotalSize + j)) < this.mMaxCacheSizeInBytes * 0.9f) {
                break;
            } else {
                j2 = j;
            }
        }
        if (VolleyLog.DEBUG) {
            VolleyLog.v("pruned %d files, %d bytes, %d ms", Integer.valueOf(i2), Long.valueOf(this.mTotalSize - j3), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
        }
    }

    private void putEntry(String str, CacheHeader cacheHeader) {
        if (this.mEntries.containsKey(str)) {
            this.mTotalSize += cacheHeader.size - this.mEntries.get(str).size;
        } else {
            this.mTotalSize += cacheHeader.size;
        }
        this.mEntries.put(str, cacheHeader);
    }

    private void removeEntry(String str) {
        CacheHeader cacheHeader = this.mEntries.get(str);
        if (cacheHeader != null) {
            this.mTotalSize -= cacheHeader.size;
            this.mEntries.remove(str);
        }
    }

    private static byte[] streamToBytes(InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read == -1) {
                break;
            }
            i2 += read;
        }
        if (i2 == i) {
            return bArr;
        }
        throw new IOException("Expected " + i + " bytes, read " + i2 + " bytes");
    }

    @Override // com.android.volley.Cache
    public synchronized void clear() {
        File[] listFiles = this.mRootDirectory.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                file.delete();
            }
        }
        this.mEntries.clear();
        this.mTotalSize = 0L;
        VolleyLog.d("Cache cleared.", new Object[0]);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0066 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.android.volley.Cache
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized com.android.volley.Cache.Entry get(java.lang.String r9) {
        /*
            r8 = this;
            monitor-enter(r8)
            java.util.Map<java.lang.String, com.android.volley.toolbox.DiskBasedCache$CacheHeader> r0 = r8.mEntries     // Catch: java.lang.Throwable -> L6d
            java.lang.Object r0 = r0.get(r9)     // Catch: java.lang.Throwable -> L6d
            com.android.volley.toolbox.DiskBasedCache$CacheHeader r0 = (com.android.volley.toolbox.DiskBasedCache.CacheHeader) r0     // Catch: java.lang.Throwable -> L6d
            r1 = 0
            if (r0 != 0) goto Le
            monitor-exit(r8)
            return r1
        Le:
            java.io.File r2 = r8.getFileForKey(r9)     // Catch: java.lang.Throwable -> L6d
            com.android.volley.toolbox.DiskBasedCache$CountingInputStream r3 = new com.android.volley.toolbox.DiskBasedCache$CountingInputStream     // Catch: java.lang.Throwable -> L3b java.io.IOException -> L3e
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L3b java.io.IOException -> L3e
            r4.<init>(r2)     // Catch: java.lang.Throwable -> L3b java.io.IOException -> L3e
            r3.<init>(r4, r1)     // Catch: java.lang.Throwable -> L3b java.io.IOException -> L3e
            com.android.volley.toolbox.DiskBasedCache.CacheHeader.readHeader(r3)     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L63
            long r4 = r2.length()     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L63
            int r6 = com.android.volley.toolbox.DiskBasedCache.CountingInputStream.access$1(r3)     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L63
            long r6 = (long) r6     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L63
            long r4 = r4 - r6
            int r4 = (int) r4     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L63
            byte[] r4 = streamToBytes(r3, r4)     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L63
            com.android.volley.Cache$Entry r0 = r0.toCacheEntry(r4)     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L63
            r3.close()     // Catch: java.io.IOException -> L37 java.lang.Throwable -> L6d
            monitor-exit(r8)
            return r0
        L37:
            monitor-exit(r8)
            return r1
        L39:
            r0 = move-exception
            goto L40
        L3b:
            r9 = move-exception
            r3 = r1
            goto L64
        L3e:
            r0 = move-exception
            r3 = r1
        L40:
            java.lang.String r4 = "%s: %s"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L63
            r6 = 0
            java.lang.String r2 = r2.getAbsolutePath()     // Catch: java.lang.Throwable -> L63
            r5[r6] = r2     // Catch: java.lang.Throwable -> L63
            r2 = 1
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L63
            r5[r2] = r0     // Catch: java.lang.Throwable -> L63
            com.android.volley.VolleyLog.d(r4, r5)     // Catch: java.lang.Throwable -> L63
            r8.remove(r9)     // Catch: java.lang.Throwable -> L63
            if (r3 == 0) goto L61
            r3.close()     // Catch: java.io.IOException -> L5f java.lang.Throwable -> L6d
            goto L61
        L5f:
            monitor-exit(r8)
            return r1
        L61:
            monitor-exit(r8)
            return r1
        L63:
            r9 = move-exception
        L64:
            if (r3 == 0) goto L6c
            r3.close()     // Catch: java.io.IOException -> L6a java.lang.Throwable -> L6d
            goto L6c
        L6a:
            monitor-exit(r8)
            return r1
        L6c:
            throw r9     // Catch: java.lang.Throwable -> L6d
        L6d:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.DiskBasedCache.get(java.lang.String):com.android.volley.Cache$Entry");
    }

    public File getFileForKey(String str) {
        return new File(this.mRootDirectory, getFilenameForKey(str));
    }

    @Override // com.android.volley.Cache
    public synchronized void initialize() {
        FileInputStream fileInputStream;
        if (!this.mRootDirectory.exists()) {
            if (!this.mRootDirectory.mkdirs()) {
                VolleyLog.e("Unable to create cache dir %s", this.mRootDirectory.getAbsolutePath());
            }
            return;
        }
        File[] listFiles = this.mRootDirectory.listFiles();
        if (listFiles == null) {
            return;
        }
        for (File file : listFiles) {
            FileInputStream fileInputStream2 = null;
            try {
                try {
                    fileInputStream = new FileInputStream(file);
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = null;
                }
            } catch (IOException unused) {
            }
            try {
                CacheHeader readHeader = CacheHeader.readHeader(fileInputStream);
                readHeader.size = file.length();
                putEntry(readHeader.key, readHeader);
                try {
                    fileInputStream.close();
                } catch (IOException unused2) {
                }
            } catch (IOException unused3) {
                fileInputStream2 = fileInputStream;
                if (file != null) {
                    file.delete();
                }
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException unused4) {
                    }
                }
                throw th;
            }
        }
    }

    @Override // com.android.volley.Cache
    public synchronized void invalidate(String str, boolean z) {
        Cache.Entry entry = get(str);
        if (entry != null) {
            entry.softTtl = 0L;
            if (z) {
                entry.ttl = 0L;
            }
            put(str, entry);
        }
    }

    @Override // com.android.volley.Cache
    public synchronized void put(String str, Cache.Entry entry) {
        pruneIfNeeded(entry.data.length);
        File fileForKey = getFileForKey(str);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileForKey);
            CacheHeader cacheHeader = new CacheHeader(str, entry);
            cacheHeader.writeHeader(fileOutputStream);
            fileOutputStream.write(entry.data);
            fileOutputStream.close();
            putEntry(str, cacheHeader);
        } catch (IOException unused) {
            if (fileForKey.delete()) {
                return;
            }
            VolleyLog.d("Could not clean up file %s", fileForKey.getAbsolutePath());
        }
    }

    @Override // com.android.volley.Cache
    public synchronized void remove(String str) {
        boolean delete = getFileForKey(str).delete();
        removeEntry(str);
        if (!delete) {
            VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", str, getFilenameForKey(str));
        }
    }
}
