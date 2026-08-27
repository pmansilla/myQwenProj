package me.panpf.sketch.http;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tencent.bugly.Bugly;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import me.panpf.sketch.http.HttpStack;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class HurlStack implements HttpStack {
    private static final String KEY = "HurlStack";
    private Map<String, String> addExtraHeaders;
    private Map<String, String> setExtraHeaders;
    private String userAgent;
    private int readTimeout = 7000;
    private int maxRetryCount = 0;
    private int connectTimeout = 7000;

    /* loaded from: classes2.dex */
    private static class HurlResponse implements HttpStack.Response {
        private HttpURLConnection connection;

        HurlResponse(HttpURLConnection httpURLConnection) {
            this.connection = httpURLConnection;
        }

        @Override // me.panpf.sketch.http.HttpStack.Response
        public int getCode() throws IOException {
            return this.connection.getResponseCode();
        }

        @Override // me.panpf.sketch.http.HttpStack.Response
        @NonNull
        public InputStream getContent() throws IOException {
            return this.connection.getInputStream();
        }

        @Override // me.panpf.sketch.http.HttpStack.Response
        public long getContentLength() {
            return this.connection.getContentLength();
        }

        @Override // me.panpf.sketch.http.HttpStack.Response
        @Nullable
        public String getHeader(@NonNull String str) {
            return this.connection.getHeaderField(str);
        }

        @Override // me.panpf.sketch.http.HttpStack.Response
        public String getHeadersString() {
            Map<String, List<String>> headerFields = this.connection.getHeaderFields();
            if (headerFields == null || headerFields.size() == 0) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                if (sb.length() != 1) {
                    sb.append(", ");
                }
                sb.append("{");
                sb.append(entry.getKey());
                sb.append(":");
                List<String> value = entry.getValue();
                if (value.size() == 0) {
                    sb.append("");
                } else if (value.size() == 1) {
                    sb.append(value.get(0));
                } else {
                    sb.append(value.toString());
                }
                sb.append("}");
            }
            sb.append("]");
            return sb.toString();
        }

        @Override // me.panpf.sketch.http.HttpStack.Response
        public String getMessage() throws IOException {
            return this.connection.getResponseMessage();
        }

        @Override // me.panpf.sketch.http.HttpStack.Response
        public boolean isContentChunked() {
            String headerField = this.connection.getHeaderField("Transfer-Encoding");
            if (headerField != null) {
                headerField = headerField.trim();
            }
            return headerField != null && "chunked".equalsIgnoreCase(headerField);
        }

        @Override // me.panpf.sketch.http.HttpStack.Response
        public void releaseConnection() {
            try {
                SketchUtils.close(getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // me.panpf.sketch.http.HttpStack
    @NonNull
    public /* bridge */ /* synthetic */ HttpStack addExtraHeaders(Map map) {
        return addExtraHeaders((Map<String, String>) map);
    }

    @Override // me.panpf.sketch.http.HttpStack
    @NonNull
    public HurlStack addExtraHeaders(Map<String, String> map) {
        this.addExtraHeaders = map;
        return this;
    }

    @Override // me.panpf.sketch.http.HttpStack
    public boolean canRetry(Throwable th) {
        return th instanceof SocketTimeoutException;
    }

    @Override // me.panpf.sketch.http.HttpStack
    public Map<String, String> getAddExtraHeaders() {
        return this.addExtraHeaders;
    }

    @Override // me.panpf.sketch.http.HttpStack
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    @Override // me.panpf.sketch.http.HttpStack
    public Map<String, String> getExtraHeaders() {
        return this.setExtraHeaders;
    }

    @Override // me.panpf.sketch.http.HttpStack
    public int getMaxRetryCount() {
        return this.maxRetryCount;
    }

    @Override // me.panpf.sketch.http.HttpStack
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override // me.panpf.sketch.http.HttpStack
    @NonNull
    public HttpStack.Response getResponse(String str) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setConnectTimeout(this.connectTimeout);
        httpURLConnection.setReadTimeout(this.readTimeout);
        httpURLConnection.setDoInput(true);
        if (Build.VERSION.SDK_INT < 8) {
            httpURLConnection.setRequestProperty("http.keepAlive", Bugly.SDK_IS_DEV);
        }
        if (this.userAgent != null) {
            httpURLConnection.setRequestProperty("User-Agent", this.userAgent);
        }
        if (this.addExtraHeaders != null && this.addExtraHeaders.size() > 0) {
            for (Map.Entry<String, String> entry : this.addExtraHeaders.entrySet()) {
                httpURLConnection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (this.setExtraHeaders != null && this.setExtraHeaders.size() > 0) {
            for (Map.Entry<String, String> entry2 : this.setExtraHeaders.entrySet()) {
                httpURLConnection.setRequestProperty(entry2.getKey(), entry2.getValue());
            }
        }
        processRequest(str, httpURLConnection);
        httpURLConnection.connect();
        return new HurlResponse(httpURLConnection);
    }

    @Override // me.panpf.sketch.http.HttpStack
    public String getUserAgent() {
        return this.userAgent;
    }

    protected void processRequest(String str, HttpURLConnection httpURLConnection) {
    }

    @Override // me.panpf.sketch.http.HttpStack
    @NonNull
    public HurlStack setConnectTimeout(int i) {
        this.connectTimeout = i;
        return this;
    }

    @Override // me.panpf.sketch.http.HttpStack
    @NonNull
    public /* bridge */ /* synthetic */ HttpStack setExtraHeaders(Map map) {
        return setExtraHeaders((Map<String, String>) map);
    }

    @Override // me.panpf.sketch.http.HttpStack
    @NonNull
    public HurlStack setExtraHeaders(Map<String, String> map) {
        this.setExtraHeaders = map;
        return this;
    }

    @Override // me.panpf.sketch.http.HttpStack
    @NonNull
    public HurlStack setMaxRetryCount(int i) {
        this.maxRetryCount = i;
        return this;
    }

    @Override // me.panpf.sketch.http.HttpStack
    @NonNull
    public HurlStack setReadTimeout(int i) {
        this.readTimeout = i;
        return this;
    }

    @Override // me.panpf.sketch.http.HttpStack
    @NonNull
    public HurlStack setUserAgent(String str) {
        this.userAgent = str;
        return this;
    }

    @NonNull
    public String toString() {
        return String.format("%s(maxRetryCount=%d,connectTimeout=%d,readTimeout=%d,userAgent=%s)", KEY, Integer.valueOf(this.maxRetryCount), Integer.valueOf(this.connectTimeout), Integer.valueOf(this.readTimeout), this.userAgent);
    }
}
