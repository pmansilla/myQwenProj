package com.squareup.okhttp.internal.http;

import com.amap.location.common.model.AmapLoc;
import com.squareup.okhttp.Address;
import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.InternalCache;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.Version;
import com.squareup.okhttp.internal.http.CacheStrategy;
import com.squareup.okhttp.internal.io.RealConnection;
import com.wx.wheelview.common.WheelConstants;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

/* loaded from: classes2.dex */
public final class HttpEngine {
    private static final ResponseBody EMPTY_BODY = new ResponseBody() { // from class: com.squareup.okhttp.internal.http.HttpEngine.1
        @Override // com.squareup.okhttp.ResponseBody
        public long contentLength() {
            return 0L;
        }

        @Override // com.squareup.okhttp.ResponseBody
        public MediaType contentType() {
            return null;
        }

        @Override // com.squareup.okhttp.ResponseBody
        public BufferedSource source() {
            return new Buffer();
        }
    };
    public static final int MAX_FOLLOW_UPS = 20;
    public final boolean bufferRequestBody;
    private BufferedSink bufferedRequestBody;
    private Response cacheResponse;
    private CacheStrategy cacheStrategy;
    private final boolean callerWritesRequestBody;
    final OkHttpClient client;
    private final boolean forWebSocket;
    private HttpStream httpStream;
    private Request networkRequest;
    private final Response priorResponse;
    private Sink requestBodyOut;
    long sentRequestMillis = -1;
    private CacheRequest storeRequest;
    public final StreamAllocation streamAllocation;
    private boolean transparentGzip;
    private final Request userRequest;
    private Response userResponse;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class NetworkInterceptorChain implements Interceptor.Chain {
        private int calls;
        private final int index;
        private final Request request;

        NetworkInterceptorChain(int i, Request request) {
            this.index = i;
            this.request = request;
        }

        @Override // com.squareup.okhttp.Interceptor.Chain
        public Connection connection() {
            return HttpEngine.this.streamAllocation.connection();
        }

        @Override // com.squareup.okhttp.Interceptor.Chain
        public Response proceed(Request request) throws IOException {
            this.calls++;
            if (this.index > 0) {
                Interceptor interceptor = HttpEngine.this.client.networkInterceptors().get(this.index - 1);
                Address address = connection().getRoute().getAddress();
                if (!request.httpUrl().host().equals(address.getUriHost()) || request.httpUrl().port() != address.getUriPort()) {
                    throw new IllegalStateException("network interceptor " + interceptor + " must retain the same host and port");
                }
                if (this.calls > 1) {
                    throw new IllegalStateException("network interceptor " + interceptor + " must call proceed() exactly once");
                }
            }
            if (this.index < HttpEngine.this.client.networkInterceptors().size()) {
                NetworkInterceptorChain networkInterceptorChain = new NetworkInterceptorChain(this.index + 1, request);
                Interceptor interceptor2 = HttpEngine.this.client.networkInterceptors().get(this.index);
                Response intercept = interceptor2.intercept(networkInterceptorChain);
                if (networkInterceptorChain.calls != 1) {
                    throw new IllegalStateException("network interceptor " + interceptor2 + " must call proceed() exactly once");
                }
                if (intercept != null) {
                    return intercept;
                }
                throw new NullPointerException("network interceptor " + interceptor2 + " returned null");
            }
            HttpEngine.this.httpStream.writeRequestHeaders(request);
            HttpEngine.this.networkRequest = request;
            if (HttpEngine.this.permitsRequestBody(request) && request.body() != null) {
                BufferedSink buffer = Okio.buffer(HttpEngine.this.httpStream.createRequestBody(request, request.body().contentLength()));
                request.body().writeTo(buffer);
                buffer.close();
            }
            Response readNetworkResponse = HttpEngine.this.readNetworkResponse();
            int code = readNetworkResponse.code();
            if ((code != 204 && code != 205) || readNetworkResponse.body().contentLength() <= 0) {
                return readNetworkResponse;
            }
            throw new ProtocolException("HTTP " + code + " had non-zero Content-Length: " + readNetworkResponse.body().contentLength());
        }

        @Override // com.squareup.okhttp.Interceptor.Chain
        public Request request() {
            return this.request;
        }
    }

    public HttpEngine(OkHttpClient okHttpClient, Request request, boolean z, boolean z2, boolean z3, StreamAllocation streamAllocation, RetryableSink retryableSink, Response response) {
        this.client = okHttpClient;
        this.userRequest = request;
        this.bufferRequestBody = z;
        this.callerWritesRequestBody = z2;
        this.forWebSocket = z3;
        this.streamAllocation = streamAllocation == null ? new StreamAllocation(okHttpClient.getConnectionPool(), createAddress(okHttpClient, request)) : streamAllocation;
        this.requestBodyOut = retryableSink;
        this.priorResponse = response;
    }

    private Response cacheWritingResponse(final CacheRequest cacheRequest, Response response) throws IOException {
        Sink body;
        if (cacheRequest == null || (body = cacheRequest.body()) == null) {
            return response;
        }
        final BufferedSource source = response.body().source();
        final BufferedSink buffer = Okio.buffer(body);
        return response.newBuilder().body(new RealResponseBody(response.headers(), Okio.buffer(new Source() { // from class: com.squareup.okhttp.internal.http.HttpEngine.2
            boolean cacheRequestClosed;

            @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    this.cacheRequestClosed = true;
                    cacheRequest.abort();
                }
                source.close();
            }

            @Override // okio.Source
            public long read(Buffer buffer2, long j) throws IOException {
                try {
                    long read = source.read(buffer2, j);
                    if (read != -1) {
                        buffer2.copyTo(buffer.buffer(), buffer2.size() - read, read);
                        buffer.emitCompleteSegments();
                        return read;
                    }
                    if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        buffer.close();
                    }
                    return -1L;
                } catch (IOException e) {
                    if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        cacheRequest.abort();
                    }
                    throw e;
                }
            }

            @Override // okio.Source
            public Timeout timeout() {
                return source.timeout();
            }
        }))).build();
    }

    private static Headers combine(Headers headers, Headers headers2) throws IOException {
        Headers.Builder builder = new Headers.Builder();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            if ((!"Warning".equalsIgnoreCase(name) || !value.startsWith(AmapLoc.RESULT_TYPE_WIFI_ONLY)) && (!OkHeaders.isEndToEnd(name) || headers2.get(name) == null)) {
                builder.add(name, value);
            }
        }
        int size2 = headers2.size();
        for (int i2 = 0; i2 < size2; i2++) {
            String name2 = headers2.name(i2);
            if (!"Content-Length".equalsIgnoreCase(name2) && OkHeaders.isEndToEnd(name2)) {
                builder.add(name2, headers2.value(i2));
            }
        }
        return builder.build();
    }

    private HttpStream connect() throws RouteException, RequestException, IOException {
        return this.streamAllocation.newStream(this.client.getConnectTimeout(), this.client.getReadTimeout(), this.client.getWriteTimeout(), this.client.getRetryOnConnectionFailure(), !this.networkRequest.method().equals("GET"));
    }

    private static Address createAddress(OkHttpClient okHttpClient, Request request) {
        SSLSocketFactory sSLSocketFactory;
        HostnameVerifier hostnameVerifier;
        CertificatePinner certificatePinner;
        if (request.isHttps()) {
            SSLSocketFactory sslSocketFactory = okHttpClient.getSslSocketFactory();
            hostnameVerifier = okHttpClient.getHostnameVerifier();
            sSLSocketFactory = sslSocketFactory;
            certificatePinner = okHttpClient.getCertificatePinner();
        } else {
            sSLSocketFactory = null;
            hostnameVerifier = null;
            certificatePinner = null;
        }
        return new Address(request.httpUrl().host(), request.httpUrl().port(), okHttpClient.getDns(), okHttpClient.getSocketFactory(), sSLSocketFactory, hostnameVerifier, certificatePinner, okHttpClient.getAuthenticator(), okHttpClient.getProxy(), okHttpClient.getProtocols(), okHttpClient.getConnectionSpecs(), okHttpClient.getProxySelector());
    }

    public static boolean hasBody(Response response) {
        if (response.request().method().equals("HEAD")) {
            return false;
        }
        int code = response.code();
        return (((code >= 100 && code < 200) || code == 204 || code == 304) && OkHeaders.contentLength(response) == -1 && !"chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) ? false : true;
    }

    private void maybeCache() throws IOException {
        InternalCache internalCache = Internal.instance.internalCache(this.client);
        if (internalCache == null) {
            return;
        }
        if (CacheStrategy.isCacheable(this.userResponse, this.networkRequest)) {
            this.storeRequest = internalCache.put(stripBody(this.userResponse));
        } else if (HttpMethod.invalidatesCache(this.networkRequest.method())) {
            try {
                internalCache.remove(this.networkRequest);
            } catch (IOException unused) {
            }
        }
    }

    private Request networkRequest(Request request) throws IOException {
        Request.Builder newBuilder = request.newBuilder();
        if (request.header("Host") == null) {
            newBuilder.header("Host", Util.hostHeader(request.httpUrl()));
        }
        if (request.header("Connection") == null) {
            newBuilder.header("Connection", "Keep-Alive");
        }
        if (request.header("Accept-Encoding") == null) {
            this.transparentGzip = true;
            newBuilder.header("Accept-Encoding", "gzip");
        }
        CookieHandler cookieHandler = this.client.getCookieHandler();
        if (cookieHandler != null) {
            OkHeaders.addCookies(newBuilder, cookieHandler.get(request.uri(), OkHeaders.toMultimap(newBuilder.build().headers(), null)));
        }
        if (request.header("User-Agent") == null) {
            newBuilder.header("User-Agent", Version.userAgent());
        }
        return newBuilder.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Response readNetworkResponse() throws IOException {
        this.httpStream.finishRequest();
        Response build = this.httpStream.readResponseHeaders().request(this.networkRequest).handshake(this.streamAllocation.connection().getHandshake()).header(OkHeaders.SENT_MILLIS, Long.toString(this.sentRequestMillis)).header(OkHeaders.RECEIVED_MILLIS, Long.toString(System.currentTimeMillis())).build();
        if (!this.forWebSocket) {
            build = build.newBuilder().body(this.httpStream.openResponseBody(build)).build();
        }
        if ("close".equalsIgnoreCase(build.request().header("Connection")) || "close".equalsIgnoreCase(build.header("Connection"))) {
            this.streamAllocation.noNewStreams();
        }
        return build;
    }

    private static Response stripBody(Response response) {
        return (response == null || response.body() == null) ? response : response.newBuilder().body(null).build();
    }

    private Response unzip(Response response) throws IOException {
        if (!this.transparentGzip || !"gzip".equalsIgnoreCase(this.userResponse.header("Content-Encoding")) || response.body() == null) {
            return response;
        }
        GzipSource gzipSource = new GzipSource(response.body().source());
        Headers build = response.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build();
        return response.newBuilder().headers(build).body(new RealResponseBody(build, Okio.buffer(gzipSource))).build();
    }

    private static boolean validate(Response response, Response response2) {
        Date date;
        if (response2.code() == 304) {
            return true;
        }
        Date date2 = response.headers().getDate("Last-Modified");
        return (date2 == null || (date = response2.headers().getDate("Last-Modified")) == null || date.getTime() >= date2.getTime()) ? false : true;
    }

    public void cancel() {
        this.streamAllocation.cancel();
    }

    public StreamAllocation close() {
        if (this.bufferedRequestBody != null) {
            Util.closeQuietly(this.bufferedRequestBody);
        } else if (this.requestBodyOut != null) {
            Util.closeQuietly(this.requestBodyOut);
        }
        if (this.userResponse != null) {
            Util.closeQuietly(this.userResponse.body());
        } else {
            this.streamAllocation.connectionFailed();
        }
        return this.streamAllocation;
    }

    public Request followUpRequest() throws IOException {
        String header;
        HttpUrl resolve;
        if (this.userResponse == null) {
            throw new IllegalStateException();
        }
        RealConnection connection = this.streamAllocation.connection();
        Route route = connection != null ? connection.getRoute() : null;
        Proxy proxy = route != null ? route.getProxy() : this.client.getProxy();
        int code = this.userResponse.code();
        String method = this.userRequest.method();
        if (code != 401) {
            if (code != 407) {
                switch (code) {
                    case WheelConstants.WHEEL_SCROLL_DELAY_DURATION /* 300 */:
                    case 301:
                    case 302:
                    case 303:
                        break;
                    default:
                        switch (code) {
                            case 307:
                            case 308:
                                if (!method.equals("GET") && !method.equals("HEAD")) {
                                    return null;
                                }
                                break;
                            default:
                                return null;
                        }
                }
                if (!this.client.getFollowRedirects() || (header = this.userResponse.header("Location")) == null || (resolve = this.userRequest.httpUrl().resolve(header)) == null) {
                    return null;
                }
                if (!resolve.scheme().equals(this.userRequest.httpUrl().scheme()) && !this.client.getFollowSslRedirects()) {
                    return null;
                }
                Request.Builder newBuilder = this.userRequest.newBuilder();
                if (HttpMethod.permitsRequestBody(method)) {
                    if (HttpMethod.redirectsToGet(method)) {
                        newBuilder.method("GET", null);
                    } else {
                        newBuilder.method(method, null);
                    }
                    newBuilder.removeHeader("Transfer-Encoding");
                    newBuilder.removeHeader("Content-Length");
                    newBuilder.removeHeader("Content-Type");
                }
                if (!sameConnection(resolve)) {
                    newBuilder.removeHeader("Authorization");
                }
                return newBuilder.url(resolve).build();
            }
            if (proxy.type() != Proxy.Type.HTTP) {
                throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            }
        }
        return OkHeaders.processAuthHeader(this.client.getAuthenticator(), this.userResponse, proxy);
    }

    public BufferedSink getBufferedRequestBody() {
        BufferedSink bufferedSink = this.bufferedRequestBody;
        if (bufferedSink != null) {
            return bufferedSink;
        }
        Sink requestBody = getRequestBody();
        if (requestBody == null) {
            return null;
        }
        BufferedSink buffer = Okio.buffer(requestBody);
        this.bufferedRequestBody = buffer;
        return buffer;
    }

    public Connection getConnection() {
        return this.streamAllocation.connection();
    }

    public Request getRequest() {
        return this.userRequest;
    }

    public Sink getRequestBody() {
        if (this.cacheStrategy != null) {
            return this.requestBodyOut;
        }
        throw new IllegalStateException();
    }

    public Response getResponse() {
        if (this.userResponse != null) {
            return this.userResponse;
        }
        throw new IllegalStateException();
    }

    public boolean hasResponse() {
        return this.userResponse != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean permitsRequestBody(Request request) {
        return HttpMethod.permitsRequestBody(request.method());
    }

    public void readResponse() throws IOException {
        Response readNetworkResponse;
        if (this.userResponse != null) {
            return;
        }
        if (this.networkRequest == null && this.cacheResponse == null) {
            throw new IllegalStateException("call sendRequest() first!");
        }
        if (this.networkRequest == null) {
            return;
        }
        if (this.forWebSocket) {
            this.httpStream.writeRequestHeaders(this.networkRequest);
            readNetworkResponse = readNetworkResponse();
        } else if (this.callerWritesRequestBody) {
            if (this.bufferedRequestBody != null && this.bufferedRequestBody.buffer().size() > 0) {
                this.bufferedRequestBody.emit();
            }
            if (this.sentRequestMillis == -1) {
                if (OkHeaders.contentLength(this.networkRequest) == -1 && (this.requestBodyOut instanceof RetryableSink)) {
                    this.networkRequest = this.networkRequest.newBuilder().header("Content-Length", Long.toString(((RetryableSink) this.requestBodyOut).contentLength())).build();
                }
                this.httpStream.writeRequestHeaders(this.networkRequest);
            }
            if (this.requestBodyOut != null) {
                if (this.bufferedRequestBody != null) {
                    this.bufferedRequestBody.close();
                } else {
                    this.requestBodyOut.close();
                }
                if (this.requestBodyOut instanceof RetryableSink) {
                    this.httpStream.writeRequestBody((RetryableSink) this.requestBodyOut);
                }
            }
            readNetworkResponse = readNetworkResponse();
        } else {
            readNetworkResponse = new NetworkInterceptorChain(0, this.networkRequest).proceed(this.networkRequest);
        }
        receiveHeaders(readNetworkResponse.headers());
        if (this.cacheResponse != null) {
            if (validate(this.cacheResponse, readNetworkResponse)) {
                this.userResponse = this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).headers(combine(this.cacheResponse.headers(), readNetworkResponse.headers())).cacheResponse(stripBody(this.cacheResponse)).networkResponse(stripBody(readNetworkResponse)).build();
                readNetworkResponse.body().close();
                releaseStreamAllocation();
                InternalCache internalCache = Internal.instance.internalCache(this.client);
                internalCache.trackConditionalCacheHit();
                internalCache.update(this.cacheResponse, stripBody(this.userResponse));
                this.userResponse = unzip(this.userResponse);
                return;
            }
            Util.closeQuietly(this.cacheResponse.body());
        }
        this.userResponse = readNetworkResponse.newBuilder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).cacheResponse(stripBody(this.cacheResponse)).networkResponse(stripBody(readNetworkResponse)).build();
        if (hasBody(this.userResponse)) {
            maybeCache();
            this.userResponse = unzip(cacheWritingResponse(this.storeRequest, this.userResponse));
        }
    }

    public void receiveHeaders(Headers headers) throws IOException {
        CookieHandler cookieHandler = this.client.getCookieHandler();
        if (cookieHandler != null) {
            cookieHandler.put(this.userRequest.uri(), OkHeaders.toMultimap(headers, null));
        }
    }

    public HttpEngine recover(RouteException routeException) {
        if (!this.streamAllocation.recover(routeException) || !this.client.getRetryOnConnectionFailure()) {
            return null;
        }
        return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this.callerWritesRequestBody, this.forWebSocket, close(), (RetryableSink) this.requestBodyOut, this.priorResponse);
    }

    public HttpEngine recover(IOException iOException) {
        return recover(iOException, this.requestBodyOut);
    }

    public HttpEngine recover(IOException iOException, Sink sink) {
        if (!this.streamAllocation.recover(iOException, sink) || !this.client.getRetryOnConnectionFailure()) {
            return null;
        }
        return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this.callerWritesRequestBody, this.forWebSocket, close(), (RetryableSink) sink, this.priorResponse);
    }

    public void releaseStreamAllocation() throws IOException {
        this.streamAllocation.release();
    }

    public boolean sameConnection(HttpUrl httpUrl) {
        HttpUrl httpUrl2 = this.userRequest.httpUrl();
        return httpUrl2.host().equals(httpUrl.host()) && httpUrl2.port() == httpUrl.port() && httpUrl2.scheme().equals(httpUrl.scheme());
    }

    public void sendRequest() throws RequestException, RouteException, IOException {
        if (this.cacheStrategy != null) {
            return;
        }
        if (this.httpStream != null) {
            throw new IllegalStateException();
        }
        Request networkRequest = networkRequest(this.userRequest);
        InternalCache internalCache = Internal.instance.internalCache(this.client);
        Response response = internalCache != null ? internalCache.get(networkRequest) : null;
        this.cacheStrategy = new CacheStrategy.Factory(System.currentTimeMillis(), networkRequest, response).get();
        this.networkRequest = this.cacheStrategy.networkRequest;
        this.cacheResponse = this.cacheStrategy.cacheResponse;
        if (internalCache != null) {
            internalCache.trackResponse(this.cacheStrategy);
        }
        if (response != null && this.cacheResponse == null) {
            Util.closeQuietly(response.body());
        }
        if (this.networkRequest == null) {
            if (this.cacheResponse != null) {
                this.userResponse = this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).cacheResponse(stripBody(this.cacheResponse)).build();
            } else {
                this.userResponse = new Response.Builder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(EMPTY_BODY).build();
            }
            this.userResponse = unzip(this.userResponse);
            return;
        }
        this.httpStream = connect();
        this.httpStream.setHttpEngine(this);
        if (this.callerWritesRequestBody && permitsRequestBody(this.networkRequest) && this.requestBodyOut == null) {
            long contentLength = OkHeaders.contentLength(networkRequest);
            if (!this.bufferRequestBody) {
                this.httpStream.writeRequestHeaders(this.networkRequest);
                this.requestBodyOut = this.httpStream.createRequestBody(this.networkRequest, contentLength);
            } else {
                if (contentLength > 2147483647L) {
                    throw new IllegalStateException("Use setFixedLengthStreamingMode() or setChunkedStreamingMode() for requests larger than 2 GiB.");
                }
                if (contentLength == -1) {
                    this.requestBodyOut = new RetryableSink();
                } else {
                    this.httpStream.writeRequestHeaders(this.networkRequest);
                    this.requestBodyOut = new RetryableSink((int) contentLength);
                }
            }
        }
    }

    public void writingRequestHeaders() {
        if (this.sentRequestMillis != -1) {
            throw new IllegalStateException();
        }
        this.sentRequestMillis = System.currentTimeMillis();
    }
}
