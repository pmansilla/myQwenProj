package me.panpf.sketch.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/* loaded from: classes2.dex */
public interface HttpStack {
    public static final int DEFAULT_CONNECT_TIMEOUT = 7000;
    public static final int DEFAULT_MAX_RETRY_COUNT = 0;
    public static final int DEFAULT_READ_TIMEOUT = 7000;

    /* loaded from: classes2.dex */
    public interface Response {
        int getCode() throws IOException;

        @NonNull
        InputStream getContent() throws IOException;

        long getContentLength();

        @Nullable
        String getHeader(@NonNull String str);

        @Nullable
        String getHeadersString();

        @Nullable
        String getMessage() throws IOException;

        boolean isContentChunked();

        void releaseConnection();
    }

    @NonNull
    HttpStack addExtraHeaders(Map<String, String> map);

    boolean canRetry(Throwable th);

    @Nullable
    Map<String, String> getAddExtraHeaders();

    int getConnectTimeout();

    @Nullable
    Map<String, String> getExtraHeaders();

    int getMaxRetryCount();

    int getReadTimeout();

    @NonNull
    Response getResponse(String str) throws IOException;

    @Nullable
    String getUserAgent();

    @NonNull
    HttpStack setConnectTimeout(int i);

    @NonNull
    HttpStack setExtraHeaders(Map<String, String> map);

    @NonNull
    HttpStack setMaxRetryCount(int i);

    @NonNull
    HttpStack setReadTimeout(int i);

    @NonNull
    HttpStack setUserAgent(String str);
}
