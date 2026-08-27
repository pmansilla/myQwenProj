package cn.sharesdk.twitter;

import android.util.Base64;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/* loaded from: classes.dex */
public class MappedFileReader {
    private byte[] array;
    private int arraySize;
    private FileInputStream fileIn;
    private long fileLength;
    private MappedByteBuffer mappedBuf;

    public MappedFileReader(String str, int i) throws IOException {
        this.fileIn = new FileInputStream(str);
        FileChannel channel = this.fileIn.getChannel();
        this.fileLength = channel.size();
        this.mappedBuf = channel.map(FileChannel.MapMode.READ_ONLY, 0L, this.fileLength);
        this.arraySize = i;
    }

    public static String byteToBase64(byte[] bArr) {
        return Base64.encodeToString(bArr, 0);
    }

    public void close() throws IOException {
        this.fileIn.close();
    }

    public byte[] getArray() {
        return this.array;
    }

    public long getFileLength() {
        return this.fileLength;
    }

    public int read() throws IOException {
        int limit = this.mappedBuf.limit();
        int position = this.mappedBuf.position();
        if (position == limit) {
            return -1;
        }
        int i = limit - position;
        if (i > this.arraySize) {
            this.array = new byte[this.arraySize];
            this.mappedBuf.get(this.array);
            return this.arraySize;
        }
        this.array = new byte[i];
        this.mappedBuf.get(this.array);
        return i;
    }
}
