package okhttp3;

import android.support.v4.app.NotificationCompat;
import java.io.IOException;
import java.util.logging.Level;
import okhttp3.Interceptor;
import okhttp3.internal.Internal;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.http.HttpEngine;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class RealCall implements Call {
    volatile boolean canceled;
    private final OkHttpClient client;
    HttpEngine engine;
    private boolean executed;
    Request originalRequest;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class ApplicationInterceptorChain implements Interceptor.Chain {
        private final boolean forWebSocket;
        private final int index;
        private final Request request;

        ApplicationInterceptorChain(int i, Request request, boolean z) {
            this.index = i;
            this.request = request;
            this.forWebSocket = z;
        }

        @Override // okhttp3.Interceptor.Chain
        public Connection connection() {
            return null;
        }

        @Override // okhttp3.Interceptor.Chain
        public Response proceed(Request request) throws IOException {
            if (this.index >= RealCall.this.client.interceptors().size()) {
                return RealCall.this.getResponse(request, this.forWebSocket);
            }
            ApplicationInterceptorChain applicationInterceptorChain = new ApplicationInterceptorChain(this.index + 1, request, this.forWebSocket);
            Interceptor interceptor = RealCall.this.client.interceptors().get(this.index);
            Response intercept = interceptor.intercept(applicationInterceptorChain);
            if (intercept != null) {
                return intercept;
            }
            throw new NullPointerException("application interceptor " + interceptor + " returned null");
        }

        @Override // okhttp3.Interceptor.Chain
        public Request request() {
            return this.request;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public final class AsyncCall extends NamedRunnable {
        private final boolean forWebSocket;
        private final Callback responseCallback;

        private AsyncCall(Callback callback, boolean z) {
            super("OkHttp %s", RealCall.this.originalRequest.url().toString());
            this.responseCallback = callback;
            this.forWebSocket = z;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void cancel() {
            RealCall.this.cancel();
        }

        @Override // okhttp3.internal.NamedRunnable
        protected void execute() {
            IOException e;
            boolean z = true;
            try {
                try {
                    Response responseWithInterceptorChain = RealCall.this.getResponseWithInterceptorChain(this.forWebSocket);
                    try {
                        if (RealCall.this.canceled) {
                            this.responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
                        } else {
                            this.responseCallback.onResponse(RealCall.this, responseWithInterceptorChain);
                        }
                    } catch (IOException e2) {
                        e = e2;
                        if (z) {
                            Internal.logger.log(Level.INFO, "Callback failure for " + RealCall.this.toLoggableString(), (Throwable) e);
                        } else {
                            this.responseCallback.onFailure(RealCall.this, e);
                        }
                    }
                } finally {
                    RealCall.this.client.dispatcher().finished(this);
                }
            } catch (IOException e3) {
                e = e3;
                z = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public RealCall get() {
            return RealCall.this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public String host() {
            return RealCall.this.originalRequest.url().host();
        }

        Request request() {
            return RealCall.this.originalRequest;
        }

        Object tag() {
            return RealCall.this.originalRequest.tag();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RealCall(OkHttpClient okHttpClient, Request request) {
        this.client = okHttpClient;
        this.originalRequest = request;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Response getResponseWithInterceptorChain(boolean z) throws IOException {
        return new ApplicationInterceptorChain(0, this.originalRequest, z).proceed(this.originalRequest);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String toLoggableString() {
        return (this.canceled ? "canceled call" : NotificationCompat.CATEGORY_CALL) + " to " + this.originalRequest.url().resolve("/...");
    }

    @Override // okhttp3.Call
    public void cancel() {
        this.canceled = true;
        if (this.engine != null) {
            this.engine.cancel();
        }
    }

    @Override // okhttp3.Call
    public void enqueue(Callback callback) {
        enqueue(callback, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueue(Callback callback, boolean z) {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        this.client.dispatcher().enqueue(new AsyncCall(callback, z));
    }

    @Override // okhttp3.Call
    public Response execute() throws IOException {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        try {
            this.client.dispatcher().executed(this);
            Response responseWithInterceptorChain = getResponseWithInterceptorChain(false);
            if (responseWithInterceptorChain != null) {
                return responseWithInterceptorChain;
            }
            throw new IOException("Canceled");
        } finally {
            this.client.dispatcher().finished(this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00f3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    okhttp3.Response getResponse(okhttp3.Request r13, boolean r14) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 266
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.RealCall.getResponse(okhttp3.Request, boolean):okhttp3.Response");
    }

    @Override // okhttp3.Call
    public boolean isCanceled() {
        return this.canceled;
    }

    @Override // okhttp3.Call
    public synchronized boolean isExecuted() {
        return this.executed;
    }

    @Override // okhttp3.Call
    public Request request() {
        return this.originalRequest;
    }

    Object tag() {
        return this.originalRequest.tag();
    }
}
