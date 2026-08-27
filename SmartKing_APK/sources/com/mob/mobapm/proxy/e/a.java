package com.mob.mobapm.proxy.e;

import com.mob.mobapm.bean.SocketTransaction;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

/* loaded from: classes.dex */
public class a extends Socket {
    private Socket a;
    private SocketTransaction b = new SocketTransaction();

    public a(Socket socket) {
        this.a = socket;
    }

    @Override // java.net.Socket
    public void bind(SocketAddress socketAddress) throws IOException {
        this.a.bind(socketAddress);
    }

    @Override // java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        this.a.close();
    }

    @Override // java.net.Socket
    public void connect(SocketAddress socketAddress) throws IOException {
        try {
            com.mob.mobapm.d.a.a().d("APM: socket address:" + socketAddress.toString(), new Object[0]);
            b.a(this.b, (InetSocketAddress) socketAddress);
            this.a.connect(socketAddress);
            b.a(this.b);
        } catch (Throwable th) {
            b.a(this.b, th);
            throw th;
        }
    }

    @Override // java.net.Socket
    public void connect(SocketAddress socketAddress, int i) throws IOException {
        try {
            com.mob.mobapm.d.a.a().d("APM: socket address:" + socketAddress.toString(), new Object[0]);
            b.a(this.b, (InetSocketAddress) socketAddress);
            this.a.connect(socketAddress, i);
            b.a(this.b);
        } catch (Throwable th) {
            b.a(this.b, th);
            throw th;
        }
    }

    @Override // java.net.Socket
    public SocketChannel getChannel() {
        return this.a.getChannel();
    }

    @Override // java.net.Socket
    public InetAddress getInetAddress() {
        return this.a.getInetAddress();
    }

    @Override // java.net.Socket
    public InputStream getInputStream() throws IOException {
        return this.a.getInputStream();
    }

    @Override // java.net.Socket
    public boolean getKeepAlive() throws SocketException {
        return this.a.getKeepAlive();
    }

    @Override // java.net.Socket
    public InetAddress getLocalAddress() {
        return this.a.getLocalAddress();
    }

    @Override // java.net.Socket
    public int getLocalPort() {
        return this.a.getLocalPort();
    }

    @Override // java.net.Socket
    public SocketAddress getLocalSocketAddress() {
        return this.a.getLocalSocketAddress();
    }

    @Override // java.net.Socket
    public boolean getOOBInline() throws SocketException {
        return this.a.getOOBInline();
    }

    @Override // java.net.Socket
    public OutputStream getOutputStream() throws IOException {
        return this.a.getOutputStream();
    }

    @Override // java.net.Socket
    public int getPort() {
        return this.a.getPort();
    }

    @Override // java.net.Socket
    public synchronized int getReceiveBufferSize() throws SocketException {
        return this.a.getReceiveBufferSize();
    }

    @Override // java.net.Socket
    public SocketAddress getRemoteSocketAddress() {
        return this.a.getRemoteSocketAddress();
    }

    @Override // java.net.Socket
    public boolean getReuseAddress() throws SocketException {
        return this.a.getReuseAddress();
    }

    @Override // java.net.Socket
    public synchronized int getSendBufferSize() throws SocketException {
        return this.a.getSendBufferSize();
    }

    @Override // java.net.Socket
    public int getSoLinger() throws SocketException {
        return this.a.getSoLinger();
    }

    @Override // java.net.Socket
    public synchronized int getSoTimeout() throws SocketException {
        return this.a.getSoTimeout();
    }

    @Override // java.net.Socket
    public boolean getTcpNoDelay() throws SocketException {
        return this.a.getTcpNoDelay();
    }

    @Override // java.net.Socket
    public int getTrafficClass() throws SocketException {
        return this.a.getTrafficClass();
    }

    @Override // java.net.Socket
    public boolean isBound() {
        return this.a.isBound();
    }

    @Override // java.net.Socket
    public boolean isClosed() {
        return this.a.isClosed();
    }

    @Override // java.net.Socket
    public boolean isConnected() {
        return this.a.isConnected();
    }

    @Override // java.net.Socket
    public boolean isInputShutdown() {
        return this.a.isInputShutdown();
    }

    @Override // java.net.Socket
    public boolean isOutputShutdown() {
        return this.a.isOutputShutdown();
    }

    @Override // java.net.Socket
    public void sendUrgentData(int i) throws IOException {
        this.a.sendUrgentData(i);
    }

    @Override // java.net.Socket
    public void setKeepAlive(boolean z) throws SocketException {
        this.a.setKeepAlive(z);
    }

    @Override // java.net.Socket
    public void setOOBInline(boolean z) throws SocketException {
        this.a.setOOBInline(z);
    }

    @Override // java.net.Socket
    public void setPerformancePreferences(int i, int i2, int i3) {
        this.a.setPerformancePreferences(i, i2, i3);
    }

    @Override // java.net.Socket
    public synchronized void setReceiveBufferSize(int i) throws SocketException {
        this.a.setReceiveBufferSize(i);
    }

    @Override // java.net.Socket
    public void setReuseAddress(boolean z) throws SocketException {
        this.a.setReuseAddress(z);
    }

    @Override // java.net.Socket
    public synchronized void setSendBufferSize(int i) throws SocketException {
        this.a.setSendBufferSize(i);
    }

    @Override // java.net.Socket
    public void setSoLinger(boolean z, int i) throws SocketException {
        this.a.setSoLinger(z, i);
    }

    @Override // java.net.Socket
    public synchronized void setSoTimeout(int i) throws SocketException {
        this.a.setSoTimeout(i);
    }

    @Override // java.net.Socket
    public void setTcpNoDelay(boolean z) throws SocketException {
        this.a.setTcpNoDelay(z);
    }

    @Override // java.net.Socket
    public void setTrafficClass(int i) throws SocketException {
        this.a.setTrafficClass(i);
    }

    @Override // java.net.Socket
    public void shutdownInput() throws IOException {
        this.a.shutdownInput();
    }

    @Override // java.net.Socket
    public void shutdownOutput() throws IOException {
        this.a.shutdownOutput();
    }

    @Override // java.net.Socket
    public String toString() {
        return this.a.toString();
    }
}
