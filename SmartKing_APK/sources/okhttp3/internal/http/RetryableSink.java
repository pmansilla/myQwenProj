package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.Sink;
import okio.Timeout;

/* loaded from: classes2.dex */
public final class RetryableSink implements Sink {
    private boolean closed;
    private final Buffer content;
    private final int limit;

    public RetryableSink() {
        this(-1);
    }

    public RetryableSink(int i) {
        this.content = new Buffer();
        this.limit = i;
    }

    @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.content.size() >= this.limit) {
            return;
        }
        throw new ProtocolException("content-length promised " + this.limit + " bytes, but received " + this.content.size());
    }

    public long contentLength() throws IOException {
        return this.content.size();
    }

    @Override // okio.Sink, java.io.Flushable
    public void flush() throws IOException {
    }

    @Override // okio.Sink
    public Timeout timeout() {
        return Timeout.NONE;
    }

    @Override // okio.Sink
    public void write(Buffer buffer, long j) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        Util.checkOffsetAndCount(buffer.size(), 0L, j);
        if (this.limit == -1 || this.content.size() <= this.limit - j) {
            this.content.write(buffer, j);
            return;
        }
        throw new ProtocolException("exceeded content-length limit of " + this.limit + " bytes");
    }

    public void writeToSocket(Sink sink) throws IOException {
        Buffer buffer = new Buffer();
        this.content.copyTo(buffer, 0L, this.content.size());
        sink.write(buffer, buffer.size());
    }
}
