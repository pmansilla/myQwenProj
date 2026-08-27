package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Address;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.RouteDatabase;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.io.RealConnection;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.WeakReference;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import okio.Sink;

/* loaded from: classes2.dex */
public final class StreamAllocation {
    public final Address address;
    private boolean canceled;
    private RealConnection connection;
    private final ConnectionPool connectionPool;
    private boolean released;
    private RouteSelector routeSelector;
    private HttpStream stream;

    public StreamAllocation(ConnectionPool connectionPool, Address address) {
        this.connectionPool = connectionPool;
        this.address = address;
    }

    private void connectionFailed(IOException iOException) {
        synchronized (this.connectionPool) {
            if (this.routeSelector != null) {
                if (this.connection.streamCount == 0) {
                    this.routeSelector.connectFailed(this.connection.getRoute(), iOException);
                } else {
                    this.routeSelector = null;
                }
            }
        }
        connectionFailed();
    }

    private void deallocate(boolean z, boolean z2, boolean z3) {
        RealConnection realConnection;
        synchronized (this.connectionPool) {
            if (z3) {
                try {
                    this.stream = null;
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (z2) {
                this.released = true;
            }
            if (this.connection != null) {
                if (z) {
                    this.connection.noNewStreams = true;
                }
                if (this.stream == null && (this.released || this.connection.noNewStreams)) {
                    release(this.connection);
                    if (this.connection.streamCount > 0) {
                        this.routeSelector = null;
                    }
                    if (this.connection.allocations.isEmpty()) {
                        this.connection.idleAtNanos = System.nanoTime();
                        if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                            realConnection = this.connection;
                            this.connection = null;
                        }
                    }
                    realConnection = null;
                    this.connection = null;
                }
            }
            realConnection = null;
        }
        if (realConnection != null) {
            Util.closeQuietly(realConnection.getSocket());
        }
    }

    private RealConnection findConnection(int i, int i2, int i3, boolean z) throws IOException, RouteException {
        synchronized (this.connectionPool) {
            if (this.released) {
                throw new IllegalStateException("released");
            }
            if (this.stream != null) {
                throw new IllegalStateException("stream != null");
            }
            if (this.canceled) {
                throw new IOException("Canceled");
            }
            RealConnection realConnection = this.connection;
            if (realConnection != null && !realConnection.noNewStreams) {
                return realConnection;
            }
            RealConnection realConnection2 = Internal.instance.get(this.connectionPool, this.address, this);
            if (realConnection2 != null) {
                this.connection = realConnection2;
                return realConnection2;
            }
            if (this.routeSelector == null) {
                this.routeSelector = new RouteSelector(this.address, routeDatabase());
            }
            RealConnection realConnection3 = new RealConnection(this.routeSelector.next());
            acquire(realConnection3);
            synchronized (this.connectionPool) {
                Internal.instance.put(this.connectionPool, realConnection3);
                this.connection = realConnection3;
                if (this.canceled) {
                    throw new IOException("Canceled");
                }
            }
            realConnection3.connect(i, i2, i3, this.address.getConnectionSpecs(), z);
            routeDatabase().connected(realConnection3.getRoute());
            return realConnection3;
        }
    }

    private RealConnection findHealthyConnection(int i, int i2, int i3, boolean z, boolean z2) throws IOException, RouteException {
        while (true) {
            RealConnection findConnection = findConnection(i, i2, i3, z);
            synchronized (this.connectionPool) {
                if (findConnection.streamCount == 0) {
                    return findConnection;
                }
                if (findConnection.isHealthy(z2)) {
                    return findConnection;
                }
                connectionFailed();
            }
        }
    }

    private boolean isRecoverable(RouteException routeException) {
        IOException lastConnectException = routeException.getLastConnectException();
        if (lastConnectException instanceof ProtocolException) {
            return false;
        }
        return lastConnectException instanceof InterruptedIOException ? lastConnectException instanceof SocketTimeoutException : (((lastConnectException instanceof SSLHandshakeException) && (lastConnectException.getCause() instanceof CertificateException)) || (lastConnectException instanceof SSLPeerUnverifiedException)) ? false : true;
    }

    private boolean isRecoverable(IOException iOException) {
        return ((iOException instanceof ProtocolException) || (iOException instanceof InterruptedIOException)) ? false : true;
    }

    private void release(RealConnection realConnection) {
        int size = realConnection.allocations.size();
        for (int i = 0; i < size; i++) {
            if (realConnection.allocations.get(i).get() == this) {
                realConnection.allocations.remove(i);
                return;
            }
        }
        throw new IllegalStateException();
    }

    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }

    public void acquire(RealConnection realConnection) {
        realConnection.allocations.add(new WeakReference(this));
    }

    public void cancel() {
        HttpStream httpStream;
        RealConnection realConnection;
        synchronized (this.connectionPool) {
            this.canceled = true;
            httpStream = this.stream;
            realConnection = this.connection;
        }
        if (httpStream != null) {
            httpStream.cancel();
        } else if (realConnection != null) {
            realConnection.cancel();
        }
    }

    public synchronized RealConnection connection() {
        return this.connection;
    }

    public void connectionFailed() {
        deallocate(true, false, true);
    }

    public HttpStream newStream(int i, int i2, int i3, boolean z, boolean z2) throws RouteException, IOException {
        HttpStream http1xStream;
        try {
            RealConnection findHealthyConnection = findHealthyConnection(i, i2, i3, z, z2);
            if (findHealthyConnection.framedConnection != null) {
                http1xStream = new Http2xStream(this, findHealthyConnection.framedConnection);
            } else {
                findHealthyConnection.getSocket().setSoTimeout(i2);
                findHealthyConnection.source.timeout().timeout(i2, TimeUnit.MILLISECONDS);
                findHealthyConnection.sink.timeout().timeout(i3, TimeUnit.MILLISECONDS);
                http1xStream = new Http1xStream(this, findHealthyConnection.source, findHealthyConnection.sink);
            }
            synchronized (this.connectionPool) {
                findHealthyConnection.streamCount++;
                this.stream = http1xStream;
            }
            return http1xStream;
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    public void noNewStreams() {
        deallocate(true, false, false);
    }

    public boolean recover(RouteException routeException) {
        if (this.connection != null) {
            connectionFailed(routeException.getLastConnectException());
        }
        return (this.routeSelector == null || this.routeSelector.hasNext()) && isRecoverable(routeException);
    }

    public boolean recover(IOException iOException, Sink sink) {
        if (this.connection != null) {
            int i = this.connection.streamCount;
            connectionFailed(iOException);
            if (i == 1) {
                return false;
            }
        }
        return (this.routeSelector == null || this.routeSelector.hasNext()) && isRecoverable(iOException) && (sink == null || (sink instanceof RetryableSink));
    }

    public void release() {
        deallocate(false, true, false);
    }

    public HttpStream stream() {
        HttpStream httpStream;
        synchronized (this.connectionPool) {
            httpStream = this.stream;
        }
        return httpStream;
    }

    public void streamFinished(HttpStream httpStream) {
        synchronized (this.connectionPool) {
            if (httpStream != null) {
                if (httpStream == this.stream) {
                }
            }
            throw new IllegalStateException("expected " + this.stream + " but was " + httpStream);
        }
        deallocate(false, false, true);
    }

    public String toString() {
        return this.address.toString();
    }
}
