package me.panpf.sketch.util;

import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import me.panpf.sketch.SLog;

/* loaded from: classes2.dex */
public final class DiskLruCache implements Closeable {
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    private static final int IO_BUFFER_SIZE = 8192;
    public static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_TMP = "journal.tmp";
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final File directory;
    private final File journalFile;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final long maxSize;
    private int redundantOpCount;
    private final int valueCount;
    private long size = 0;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75f, true);
    private long nextSequenceNumber = 0;
    private final ExecutorService executorService = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final Callable<Void> cleanupCallable = new Callable<Void>() { // from class: me.panpf.sketch.util.DiskLruCache.1
        @Override // java.util.concurrent.Callable
        public Void call() throws Exception {
            synchronized (DiskLruCache.this) {
                if (DiskLruCache.this.journalWriter == null) {
                    return null;
                }
                DiskLruCache.this.trimToSize();
                if (DiskLruCache.this.journalRebuildRequired()) {
                    DiskLruCache.this.rebuildJournal();
                    DiskLruCache.this.redundantOpCount = 0;
                }
                return null;
            }
        }
    };

    /* loaded from: classes2.dex */
    public static class ClosedException extends Exception {
        public ClosedException(String str) {
            super(str);
        }
    }

    /* loaded from: classes2.dex */
    public final class Editor {
        private final Entry entry;
        private boolean hasErrors;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public class FaultHidingOutputStream extends FilterOutputStream {
            private FaultHidingOutputStream(OutputStream outputStream) {
                super(outputStream);
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                try {
                    this.out.close();
                } catch (IOException unused) {
                    Editor.this.hasErrors = true;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
            public void flush() {
                try {
                    this.out.flush();
                } catch (IOException unused) {
                    Editor.this.hasErrors = true;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public void write(int i) {
                try {
                    this.out.write(i);
                } catch (IOException unused) {
                    Editor.this.hasErrors = true;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public void write(byte[] bArr, int i, int i2) {
                try {
                    this.out.write(bArr, i, i2);
                } catch (IOException unused) {
                    Editor.this.hasErrors = true;
                }
            }
        }

        private Editor(Entry entry) {
            this.entry = entry;
        }

        public void abort() throws IOException, EditorChangedException, FileNotExistException {
            DiskLruCache.this.completeEdit(this, false);
        }

        public void commit() throws IOException, EditorChangedException, ClosedException, FileNotExistException {
            if (!this.hasErrors) {
                DiskLruCache.this.completeEdit(this, true);
            } else {
                DiskLruCache.this.completeEdit(this, false);
                DiskLruCache.this.remove(this.entry.key);
            }
        }

        public String getString(int i) throws IOException {
            InputStream newInputStream = newInputStream(i);
            if (newInputStream != null) {
                return DiskLruCache.inputStreamToString(newInputStream);
            }
            return null;
        }

        public InputStream newInputStream(int i) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    return null;
                }
                return new FileInputStream(this.entry.getCleanFile(i));
            }
        }

        public OutputStream newOutputStream(int i) throws IOException {
            FaultHidingOutputStream faultHidingOutputStream;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                faultHidingOutputStream = new FaultHidingOutputStream(new FileOutputStream(this.entry.getDirtyFile(i)));
            }
            return faultHidingOutputStream;
        }

        public void set(int i, String str) throws IOException {
            OutputStreamWriter outputStreamWriter = null;
            try {
                OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(newOutputStream(i), DiskLruCache.UTF_8);
                try {
                    outputStreamWriter2.write(str);
                    DiskLruCache.closeQuietly(outputStreamWriter2);
                } catch (Throwable th) {
                    th = th;
                    outputStreamWriter = outputStreamWriter2;
                    DiskLruCache.closeQuietly(outputStreamWriter);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class EditorChangedException extends Exception {
    }

    /* loaded from: classes2.dex */
    public final class Entry {
        private Editor currentEditor;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;

        private Entry(String str) {
            this.key = str;
            this.lengths = new long[DiskLruCache.this.valueCount];
        }

        private IOException invalidLengths(String[] strArr) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLengths(String[] strArr) throws IOException {
            if (strArr.length != DiskLruCache.this.valueCount) {
                throw invalidLengths(strArr);
            }
            for (int i = 0; i < strArr.length; i++) {
                try {
                    this.lengths[i] = Long.parseLong(strArr[i]);
                } catch (NumberFormatException unused) {
                    throw invalidLengths(strArr);
                }
            }
        }

        public File getCleanFile(int i) {
            return new File(DiskLruCache.this.directory, this.key + "." + i);
        }

        public File getDirtyFile(int i) {
            return new File(DiskLruCache.this.directory, this.key + "." + i + ".tmp");
        }

        public String getLengths() throws IOException {
            StringBuilder sb = new StringBuilder();
            for (long j : this.lengths) {
                sb.append(' ');
                sb.append(j);
            }
            return sb.toString();
        }
    }

    /* loaded from: classes2.dex */
    public static class FileNotExistException extends Exception {
        public FileNotExistException(String str) {
            super(str);
        }
    }

    /* loaded from: classes2.dex */
    public final class SimpleSnapshot {
        private final File[] cleanFiles;
        private final DiskLruCache diskLruCache;
        private final String key;
        private final long sequenceNumber;

        private SimpleSnapshot(String str, long j, File[] fileArr, DiskLruCache diskLruCache) {
            this.key = str;
            this.sequenceNumber = j;
            this.cleanFiles = fileArr;
            this.diskLruCache = diskLruCache;
        }

        public Editor edit() throws IOException, ClosedException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public DiskLruCache getDiskLruCache() {
            return this.diskLruCache;
        }

        public File getFile(int i) {
            return this.cleanFiles[i];
        }

        public String getKey() {
            return this.key;
        }

        public String getString(int i) throws IOException {
            return DiskLruCache.inputStreamToString(newInputStream(i));
        }

        public InputStream newInputStream(int i) throws FileNotFoundException {
            return new FileInputStream(this.cleanFiles[i]);
        }
    }

    /* loaded from: classes2.dex */
    public final class Snapshot implements Closeable {
        private final InputStream[] ins;
        private final String key;
        private final long sequenceNumber;

        private Snapshot(String str, long j, InputStream[] inputStreamArr) {
            this.key = str;
            this.sequenceNumber = j;
            this.ins = inputStreamArr;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            for (InputStream inputStream : this.ins) {
                DiskLruCache.closeQuietly(inputStream);
            }
        }

        public Editor edit() throws IOException, ClosedException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public InputStream getInputStream(int i) {
            return this.ins[i];
        }

        public String getString(int i) throws IOException {
            return DiskLruCache.inputStreamToString(getInputStream(i));
        }
    }

    private DiskLruCache(File file, int i, int i2, long j) {
        this.directory = file;
        this.appVersion = i;
        this.journalFile = new File(file, JOURNAL_FILE);
        this.journalFileTmp = new File(file, JOURNAL_FILE_TMP);
        this.valueCount = i2;
        this.maxSize = j;
    }

    private void checkNotClosed() throws ClosedException {
        if (this.journalWriter == null) {
            throw new ClosedException("cache is closed");
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void completeEdit(Editor editor, boolean z) throws IOException, EditorChangedException, FileNotExistException {
        Entry entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new EditorChangedException();
        }
        if (z && !entry.readable) {
            for (int i = 0; i < this.valueCount; i++) {
                if (!entry.getDirtyFile(i).exists()) {
                    editor.abort();
                    throw new FileNotExistException("edit didn't create file " + i);
                }
            }
        }
        for (int i2 = 0; i2 < this.valueCount; i2++) {
            File dirtyFile = entry.getDirtyFile(i2);
            if (!z) {
                deleteIfExists(dirtyFile);
            } else if (dirtyFile.exists()) {
                File cleanFile = entry.getCleanFile(i2);
                dirtyFile.renameTo(cleanFile);
                long j = entry.lengths[i2];
                long length = cleanFile.length();
                entry.lengths[i2] = length;
                this.size = (this.size - j) + length;
            }
        }
        this.redundantOpCount++;
        entry.currentEditor = null;
        if (entry.readable || z) {
            entry.readable = true;
            this.journalWriter.write("CLEAN " + entry.key + entry.getLengths() + '\n');
            this.journalWriter.flush();
            if (z) {
                long j2 = this.nextSequenceNumber;
                this.nextSequenceNumber = 1 + j2;
                entry.sequenceNumber = j2;
            }
        } else {
            this.lruEntries.remove(entry.key);
            this.journalWriter.write("REMOVE " + entry.key + '\n');
            this.journalWriter.flush();
        }
        if (this.size > this.maxSize || journalRebuildRequired()) {
            this.executorService.submit(this.cleanupCallable);
        }
    }

    private static <T> T[] copyOfRange(T[] tArr, int i, int i2) {
        int length = tArr.length;
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int min = Math.min(i3, length - i);
        T[] tArr2 = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i3));
        System.arraycopy(tArr, i, tArr2, 0, min);
        return tArr2;
    }

    public static void deleteContents(File file) throws IOException {
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return;
        }
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                deleteContents(file2);
            }
            if (!file2.delete()) {
                SLog.w("DiskLruCache", "failed to delete file: %s", file2.getPath());
            }
        }
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Editor edit(String str, long j) throws IOException, ClosedException {
        checkNotClosed();
        validateKey(str);
        Entry entry = this.lruEntries.get(str);
        if (j != -1 && (entry == null || entry.sequenceNumber != j)) {
            return null;
        }
        if (entry == null) {
            entry = new Entry(str);
            this.lruEntries.put(str, entry);
        } else if (entry.currentEditor != null) {
            return null;
        }
        Editor editor = new Editor(entry);
        entry.currentEditor = editor;
        this.journalWriter.write("DIRTY " + str + '\n');
        this.journalWriter.flush();
        return editor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String inputStreamToString(InputStream inputStream) throws IOException {
        return readFully(new InputStreamReader(inputStream, UTF_8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    public static DiskLruCache open(File file, int i, int i2, long j) throws IOException {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        DiskLruCache diskLruCache = new DiskLruCache(file, i, i2, j);
        if (diskLruCache.journalFile.exists()) {
            try {
                diskLruCache.readJournal();
                diskLruCache.processJournal();
                diskLruCache.journalWriter = new BufferedWriter(new FileWriter(diskLruCache.journalFile, true), 8192);
                return diskLruCache;
            } catch (IOException unused) {
                diskLruCache.delete();
            }
        }
        file.mkdirs();
        DiskLruCache diskLruCache2 = new DiskLruCache(file, i, i2, j);
        diskLruCache2.rebuildJournal();
        return diskLruCache2;
    }

    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        Iterator<Entry> it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            Entry next = it.next();
            int i = 0;
            if (next.currentEditor == null) {
                while (i < this.valueCount) {
                    this.size += next.lengths[i];
                    i++;
                }
            } else {
                next.currentEditor = null;
                while (i < this.valueCount) {
                    deleteIfExists(next.getCleanFile(i));
                    deleteIfExists(next.getDirtyFile(i));
                    i++;
                }
                it.remove();
            }
        }
    }

    public static String readAsciiLine(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder(80);
        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                throw new EOFException();
            }
            if (read == 10) {
                int length = sb.length();
                if (length > 0) {
                    int i = length - 1;
                    if (sb.charAt(i) == '\r') {
                        sb.setLength(i);
                    }
                }
                return sb.toString();
            }
            sb.append((char) read);
        }
    }

    public static String readFully(Reader reader) throws IOException {
        try {
            StringWriter stringWriter = new StringWriter();
            char[] cArr = new char[1024];
            while (true) {
                int read = reader.read(cArr);
                if (read == -1) {
                    return stringWriter.toString();
                }
                stringWriter.write(cArr, 0, read);
            }
        } finally {
            reader.close();
        }
    }

    private void readJournal() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.journalFile), 8192);
        try {
            String readAsciiLine = readAsciiLine(bufferedInputStream);
            String readAsciiLine2 = readAsciiLine(bufferedInputStream);
            String readAsciiLine3 = readAsciiLine(bufferedInputStream);
            String readAsciiLine4 = readAsciiLine(bufferedInputStream);
            String readAsciiLine5 = readAsciiLine(bufferedInputStream);
            if (!MAGIC.equals(readAsciiLine) || !"1".equals(readAsciiLine2) || !Integer.toString(this.appVersion).equals(readAsciiLine3) || !Integer.toString(this.valueCount).equals(readAsciiLine4) || !"".equals(readAsciiLine5)) {
                throw new IOException("unexpected journal header: [" + readAsciiLine + ", " + readAsciiLine2 + ", " + readAsciiLine4 + ", " + readAsciiLine5 + "]");
            }
            while (true) {
                try {
                    readJournalLine(readAsciiLine(bufferedInputStream));
                } catch (EOFException unused) {
                    return;
                }
            }
        } finally {
            closeQuietly(bufferedInputStream);
        }
    }

    private void readJournalLine(String str) throws IOException {
        String[] split = str.split(SQLBuilder.BLANK);
        if (split.length < 2) {
            throw new IOException("unexpected journal line: " + str);
        }
        String str2 = split[1];
        if (split[0].equals(REMOVE) && split.length == 2) {
            this.lruEntries.remove(str2);
            return;
        }
        Entry entry = this.lruEntries.get(str2);
        if (entry == null) {
            entry = new Entry(str2);
            this.lruEntries.put(str2, entry);
        }
        if (split[0].equals(CLEAN) && split.length == this.valueCount + 2) {
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths((String[]) copyOfRange(split, 2, split.length));
        } else if (split[0].equals(DIRTY) && split.length == 2) {
            entry.currentEditor = new Editor(entry);
        } else {
            if (split[0].equals(READ) && split.length == 2) {
                return;
            }
            throw new IOException("unexpected journal line: " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void rebuildJournal() throws IOException {
        if (this.journalWriter != null) {
            this.journalWriter.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.journalFileTmp), 8192);
        bufferedWriter.write(MAGIC);
        bufferedWriter.write("\n");
        bufferedWriter.write("1");
        bufferedWriter.write("\n");
        bufferedWriter.write(Integer.toString(this.appVersion));
        bufferedWriter.write("\n");
        bufferedWriter.write(Integer.toString(this.valueCount));
        bufferedWriter.write("\n");
        bufferedWriter.write("\n");
        for (Entry entry : this.lruEntries.values()) {
            if (entry.currentEditor != null) {
                bufferedWriter.write("DIRTY " + entry.key + '\n');
            } else {
                bufferedWriter.write("CLEAN " + entry.key + entry.getLengths() + '\n');
            }
        }
        bufferedWriter.close();
        this.journalFileTmp.renameTo(this.journalFile);
        this.journalWriter = new BufferedWriter(new FileWriter(this.journalFile, true), 8192);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trimToSize() throws IOException, ClosedException {
        while (this.size > this.maxSize) {
            remove(this.lruEntries.entrySet().iterator().next().getKey());
        }
    }

    private void validateKey(String str) {
        if (str.contains(SQLBuilder.BLANK) || str.contains("\n") || str.contains("\r")) {
            throw new IllegalArgumentException("keys must not contain spaces or newlines: \"" + str + "\"");
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.journalWriter == null) {
            return;
        }
        Iterator it = new ArrayList(this.lruEntries.values()).iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (entry.currentEditor != null) {
                try {
                    entry.currentEditor.abort();
                } catch (EditorChangedException e) {
                    e.printStackTrace();
                } catch (FileNotExistException e2) {
                    e2.printStackTrace();
                }
            }
        }
        try {
            trimToSize();
        } catch (ClosedException e3) {
            e3.printStackTrace();
        }
        this.journalWriter.close();
        this.journalWriter = null;
    }

    public void delete() throws IOException {
        close();
        deleteContents(this.directory);
    }

    public Editor edit(String str) throws IOException, ClosedException {
        return edit(str, -1L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0043, code lost:
    
        if (r0.readable != false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized boolean exist(java.lang.String r6) throws me.panpf.sketch.util.DiskLruCache.ClosedException, java.io.IOException {
        /*
            r5 = this;
            monitor-enter(r5)
            r5.checkNotClosed()     // Catch: java.lang.Throwable -> L49
            r5.validateKey(r6)     // Catch: java.lang.Throwable -> L49
            java.util.LinkedHashMap<java.lang.String, me.panpf.sketch.util.DiskLruCache$Entry> r0 = r5.lruEntries     // Catch: java.lang.Throwable -> L49
            java.lang.Object r0 = r0.get(r6)     // Catch: java.lang.Throwable -> L49
            me.panpf.sketch.util.DiskLruCache$Entry r0 = (me.panpf.sketch.util.DiskLruCache.Entry) r0     // Catch: java.lang.Throwable -> L49
            int r1 = r5.redundantOpCount     // Catch: java.lang.Throwable -> L49
            r2 = 1
            int r1 = r1 + r2
            r5.redundantOpCount = r1     // Catch: java.lang.Throwable -> L49
            java.io.Writer r1 = r5.journalWriter     // Catch: java.lang.Throwable -> L49
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L49
            r3.<init>()     // Catch: java.lang.Throwable -> L49
            java.lang.String r4 = "READ "
            r3.append(r4)     // Catch: java.lang.Throwable -> L49
            r3.append(r6)     // Catch: java.lang.Throwable -> L49
            r6 = 10
            r3.append(r6)     // Catch: java.lang.Throwable -> L49
            java.lang.String r6 = r3.toString()     // Catch: java.lang.Throwable -> L49
            r1.append(r6)     // Catch: java.lang.Throwable -> L49
            boolean r6 = r5.journalRebuildRequired()     // Catch: java.lang.Throwable -> L49
            if (r6 == 0) goto L3d
            java.util.concurrent.ExecutorService r6 = r5.executorService     // Catch: java.lang.Throwable -> L49
            java.util.concurrent.Callable<java.lang.Void> r1 = r5.cleanupCallable     // Catch: java.lang.Throwable -> L49
            r6.submit(r1)     // Catch: java.lang.Throwable -> L49
        L3d:
            if (r0 == 0) goto L46
            boolean r6 = me.panpf.sketch.util.DiskLruCache.Entry.access$600(r0)     // Catch: java.lang.Throwable -> L49
            if (r6 == 0) goto L46
            goto L47
        L46:
            r2 = 0
        L47:
            monitor-exit(r5)
            return r2
        L49:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: me.panpf.sketch.util.DiskLruCache.exist(java.lang.String):boolean");
    }

    public synchronized void flush() throws IOException, ClosedException {
        checkNotClosed();
        trimToSize();
        this.journalWriter.flush();
    }

    public synchronized Snapshot get(String str) throws IOException, ClosedException {
        checkNotClosed();
        validateKey(str);
        Entry entry = this.lruEntries.get(str);
        if (entry == null) {
            return null;
        }
        if (!entry.readable) {
            return null;
        }
        InputStream[] inputStreamArr = new InputStream[this.valueCount];
        for (int i = 0; i < this.valueCount; i++) {
            try {
                inputStreamArr[i] = new FileInputStream(entry.getCleanFile(i));
            } catch (FileNotFoundException unused) {
                return null;
            }
        }
        this.redundantOpCount++;
        this.journalWriter.append((CharSequence) ("READ " + str + '\n'));
        if (journalRebuildRequired()) {
            this.executorService.submit(this.cleanupCallable);
        }
        return new Snapshot(str, entry.sequenceNumber, inputStreamArr);
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized SimpleSnapshot getSimpleSnapshot(String str) throws IOException, ClosedException {
        checkNotClosed();
        validateKey(str);
        Entry entry = this.lruEntries.get(str);
        if (entry == null) {
            return null;
        }
        if (!entry.readable) {
            return null;
        }
        File[] fileArr = new File[this.valueCount];
        for (int i = 0; i < this.valueCount; i++) {
            fileArr[i] = entry.getCleanFile(i);
        }
        this.redundantOpCount++;
        this.journalWriter.append((CharSequence) ("READ " + str + '\n'));
        if (journalRebuildRequired()) {
            this.executorService.submit(this.cleanupCallable);
        }
        return new SimpleSnapshot(str, entry.sequenceNumber, fileArr, this);
    }

    public boolean isClosed() {
        return this.journalWriter == null;
    }

    public long maxSize() {
        return this.maxSize;
    }

    public synchronized boolean remove(String str) throws IOException, ClosedException {
        checkNotClosed();
        validateKey(str);
        Entry entry = this.lruEntries.get(str);
        if (entry != null && entry.currentEditor == null) {
            for (int i = 0; i < this.valueCount; i++) {
                File cleanFile = entry.getCleanFile(i);
                if (cleanFile.exists() && !cleanFile.delete()) {
                    throw new IOException("failed to delete " + cleanFile);
                }
                this.size -= entry.lengths[i];
                entry.lengths[i] = 0;
            }
            this.redundantOpCount++;
            this.journalWriter.append((CharSequence) ("REMOVE " + str + '\n'));
            this.lruEntries.remove(str);
            if (journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            return true;
        }
        return false;
    }

    public synchronized long size() {
        return this.size;
    }
}
