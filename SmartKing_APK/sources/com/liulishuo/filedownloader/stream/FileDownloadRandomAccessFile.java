package com.liulishuo.filedownloader.stream;

import com.liulishuo.filedownloader.util.FileDownloadHelper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: classes.dex */
public class FileDownloadRandomAccessFile implements FileDownloadOutputStream {
    private final FileDescriptor fd;
    private final BufferedOutputStream out;
    private final RandomAccessFile randomAccess;

    /* loaded from: classes.dex */
    public static class Creator implements FileDownloadHelper.OutputStreamCreator {
        @Override // com.liulishuo.filedownloader.util.FileDownloadHelper.OutputStreamCreator
        public FileDownloadOutputStream create(File file) throws IOException {
            return new FileDownloadRandomAccessFile(file);
        }

        @Override // com.liulishuo.filedownloader.util.FileDownloadHelper.OutputStreamCreator
        public boolean supportSeek() {
            return true;
        }
    }

    FileDownloadRandomAccessFile(File file) throws IOException {
        this.randomAccess = new RandomAccessFile(file, "rw");
        this.fd = this.randomAccess.getFD();
        this.out = new BufferedOutputStream(new FileOutputStream(this.randomAccess.getFD()));
    }

    @Override // com.liulishuo.filedownloader.stream.FileDownloadOutputStream
    public void close() throws IOException {
        this.out.close();
        this.randomAccess.close();
    }

    @Override // com.liulishuo.filedownloader.stream.FileDownloadOutputStream
    public void flushAndSync() throws IOException {
        this.out.flush();
        this.fd.sync();
    }

    @Override // com.liulishuo.filedownloader.stream.FileDownloadOutputStream
    public void seek(long j) throws IOException {
        this.randomAccess.seek(j);
    }

    @Override // com.liulishuo.filedownloader.stream.FileDownloadOutputStream
    public void setLength(long j) throws IOException {
        this.randomAccess.setLength(j);
    }

    @Override // com.liulishuo.filedownloader.stream.FileDownloadOutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
    }
}
