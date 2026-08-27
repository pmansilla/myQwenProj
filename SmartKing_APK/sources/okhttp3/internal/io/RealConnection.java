package okhttp3.internal.io;

import io.reactivex.annotations.SchedulerSupport;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownServiceException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import kotlin.jvm.internal.LongCompanionObject;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.ConnectionSpec;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.ConnectionSpecSelector;
import okhttp3.internal.Platform;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.FramedConnection;
import okhttp3.internal.framed.FramedStream;
import okhttp3.internal.http.Http1xStream;
import okhttp3.internal.http.OkHeaders;
import okhttp3.internal.http.RouteException;
import okhttp3.internal.http.StreamAllocation;
import okhttp3.internal.tls.OkHostnameVerifier;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

/* loaded from: classes2.dex */
public final class RealConnection extends FramedConnection.Listener implements Connection {
    public int allocationLimit;
    public volatile FramedConnection framedConnection;
    private Handshake handshake;
    public boolean noNewStreams;
    private Protocol protocol;
    private Socket rawSocket;
    private final Route route;
    public BufferedSink sink;
    public Socket socket;
    public BufferedSource source;
    public int successCount;
    public final List<Reference<StreamAllocation>> allocations = new ArrayList();
    public long idleAtNanos = LongCompanionObject.MAX_VALUE;

    public RealConnection(Route route) {
        this.route = route;
    }

    private void connectSocket(int i, int i2, int i3, ConnectionSpecSelector connectionSpecSelector) throws IOException {
        this.rawSocket.setSoTimeout(i2);
        try {
            Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), i);
            this.source = Okio.buffer(Okio.source(this.rawSocket));
            this.sink = Okio.buffer(Okio.sink(this.rawSocket));
            if (this.route.address().sslSocketFactory() != null) {
                connectTls(i2, i3, connectionSpecSelector);
            } else {
                this.protocol = Protocol.HTTP_1_1;
                this.socket = this.rawSocket;
            }
            if (this.protocol != Protocol.SPDY_3 && this.protocol != Protocol.HTTP_2) {
                this.allocationLimit = 1;
                return;
            }
            this.socket.setSoTimeout(0);
            FramedConnection build = new FramedConnection.Builder(true).socket(this.socket, this.route.address().url().host(), this.source, this.sink).protocol(this.protocol).listener(this).build();
            build.sendConnectionPreface();
            this.allocationLimit = build.maxConcurrentStreams();
            this.framedConnection = build;
        } catch (ConnectException unused) {
            throw new ConnectException("Failed to connect to " + this.route.socketAddress());
        }
    }

    private void connectTls(int i, int i2, ConnectionSpecSelector connectionSpecSelector) throws IOException {
        SSLSocket sSLSocket;
        if (this.route.requiresTunnel()) {
            createTunnel(i, i2);
        }
        Address address = this.route.address();
        try {
            try {
                sSLSocket = (SSLSocket) address.sslSocketFactory().createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
            } catch (AssertionError e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
            sSLSocket = null;
        }
        try {
            ConnectionSpec configureSecureSocket = connectionSpecSelector.configureSecureSocket(sSLSocket);
            if (configureSecureSocket.supportsTlsExtensions()) {
                Platform.get().configureTlsExtensions(sSLSocket, address.url().host(), address.protocols());
            }
            sSLSocket.startHandshake();
            Handshake handshake = Handshake.get(sSLSocket.getSession());
            if (address.hostnameVerifier().verify(address.url().host(), sSLSocket.getSession())) {
                address.certificatePinner().check(address.url().host(), handshake.peerCertificates());
                String selectedProtocol = configureSecureSocket.supportsTlsExtensions() ? Platform.get().getSelectedProtocol(sSLSocket) : null;
                this.socket = sSLSocket;
                this.source = Okio.buffer(Okio.source(this.socket));
                this.sink = Okio.buffer(Okio.sink(this.socket));
                this.handshake = handshake;
                this.protocol = selectedProtocol != null ? Protocol.get(selectedProtocol) : Protocol.HTTP_1_1;
                if (sSLSocket != null) {
                    Platform.get().afterHandshake(sSLSocket);
                    return;
                }
                return;
            }
            X509Certificate x509Certificate = (X509Certificate) handshake.peerCertificates().get(0);
            throw new SSLPeerUnverifiedException("Hostname " + address.url().host() + " not verified:\n    certificate: " + CertificatePinner.pin(x509Certificate) + "\n    DN: " + x509Certificate.getSubjectDN().getName() + "\n    subjectAltNames: " + OkHostnameVerifier.allSubjectAltNames(x509Certificate));
        } catch (AssertionError e2) {
            e = e2;
            if (!Util.isAndroidGetsocknameError(e)) {
                throw e;
            }
            throw new IOException(e);
        } catch (Throwable th2) {
            th = th2;
            if (sSLSocket != null) {
                Platform.get().afterHandshake(sSLSocket);
            }
            Util.closeQuietly((Socket) sSLSocket);
            throw th;
        }
    }

    private void createTunnel(int i, int i2) throws IOException {
        Request createTunnelRequest = createTunnelRequest();
        String str = "CONNECT " + Util.hostHeader(createTunnelRequest.url(), true) + " HTTP/1.1";
        do {
            Http1xStream http1xStream = new Http1xStream(null, this.source, this.sink);
            this.source.timeout().timeout(i, TimeUnit.MILLISECONDS);
            this.sink.timeout().timeout(i2, TimeUnit.MILLISECONDS);
            http1xStream.writeRequest(createTunnelRequest.headers(), str);
            http1xStream.finishRequest();
            Response build = http1xStream.readResponse().request(createTunnelRequest).build();
            long contentLength = OkHeaders.contentLength(build);
            if (contentLength == -1) {
                contentLength = 0;
            }
            Source newFixedLengthSource = http1xStream.newFixedLengthSource(contentLength);
            Util.skipAll(newFixedLengthSource, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            newFixedLengthSource.close();
            int code = build.code();
            if (code == 200) {
                if (!this.source.buffer().exhausted() || !this.sink.buffer().exhausted()) {
                    throw new IOException("TLS tunnel buffered too many bytes!");
                }
                return;
            } else {
                if (code != 407) {
                    throw new IOException("Unexpected response code for CONNECT: " + build.code());
                }
                createTunnelRequest = this.route.address().proxyAuthenticator().authenticate(this.route, build);
            }
        } while (createTunnelRequest != null);
        throw new IOException("Failed to authenticate with proxy");
    }

    private Request createTunnelRequest() throws IOException {
        return new Request.Builder().url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
    }

    public void cancel() {
        Util.closeQuietly(this.rawSocket);
    }

    public void connect(int i, int i2, int i3, List<ConnectionSpec> list, boolean z) throws RouteException {
        Socket createSocket;
        if (this.protocol != null) {
            throw new IllegalStateException("already connected");
        }
        ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector(list);
        Proxy proxy = this.route.proxy();
        Address address = this.route.address();
        if (this.route.address().sslSocketFactory() == null && !list.contains(ConnectionSpec.CLEARTEXT)) {
            throw new RouteException(new UnknownServiceException("CLEARTEXT communication not supported: " + list));
        }
        RouteException routeException = null;
        while (this.protocol == null) {
            try {
            } catch (IOException e) {
                Util.closeQuietly(this.socket);
                Util.closeQuietly(this.rawSocket);
                this.socket = null;
                this.rawSocket = null;
                this.source = null;
                this.sink = null;
                this.handshake = null;
                this.protocol = null;
                if (routeException == null) {
                    routeException = new RouteException(e);
                } else {
                    routeException.addConnectException(e);
                }
                if (!z) {
                    throw routeException;
                }
                if (!connectionSpecSelector.connectionFailed(e)) {
                    throw routeException;
                }
            }
            if (proxy.type() != Proxy.Type.DIRECT && proxy.type() != Proxy.Type.HTTP) {
                createSocket = new Socket(proxy);
                this.rawSocket = createSocket;
                connectSocket(i, i2, i3, connectionSpecSelector);
            }
            createSocket = address.socketFactory().createSocket();
            this.rawSocket = createSocket;
            connectSocket(i, i2, i3, connectionSpecSelector);
        }
    }

    @Override // okhttp3.Connection
    public Handshake handshake() {
        return this.handshake;
    }

    boolean isConnected() {
        return this.protocol != null;
    }

    public boolean isHealthy(boolean z) {
        if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown()) {
            return false;
        }
        if (this.framedConnection == null && z) {
            try {
                int soTimeout = this.socket.getSoTimeout();
                try {
                    this.socket.setSoTimeout(1);
                    return !this.source.exhausted();
                } finally {
                    this.socket.setSoTimeout(soTimeout);
                }
            } catch (SocketTimeoutException unused) {
            } catch (IOException unused2) {
                return false;
            }
        }
        return true;
    }

    public boolean isMultiplexed() {
        return this.framedConnection != null;
    }

    @Override // okhttp3.internal.framed.FramedConnection.Listener
    public void onSettings(FramedConnection framedConnection) {
        this.allocationLimit = framedConnection.maxConcurrentStreams();
    }

    @Override // okhttp3.internal.framed.FramedConnection.Listener
    public void onStream(FramedStream framedStream) throws IOException {
        framedStream.close(ErrorCode.REFUSED_STREAM);
    }

    @Override // okhttp3.Connection
    public Protocol protocol() {
        return this.framedConnection == null ? this.protocol != null ? this.protocol : Protocol.HTTP_1_1 : this.framedConnection.getProtocol();
    }

    @Override // okhttp3.Connection
    public Route route() {
        return this.route;
    }

    @Override // okhttp3.Connection
    public Socket socket() {
        return this.socket;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Connection{");
        sb.append(this.route.address().url().host());
        sb.append(":");
        sb.append(this.route.address().url().port());
        sb.append(", proxy=");
        sb.append(this.route.proxy());
        sb.append(" hostAddress=");
        sb.append(this.route.socketAddress());
        sb.append(" cipherSuite=");
        sb.append(this.handshake != null ? this.handshake.cipherSuite() : SchedulerSupport.NONE);
        sb.append(" protocol=");
        sb.append(this.protocol);
        sb.append('}');
        return sb.toString();
    }
}
