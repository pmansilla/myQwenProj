package com.android.volley.toolbox;

import android.os.SystemClock;
import android.support.graphics.drawable.PathInterpolatorCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.cookie.DateUtils;

/* loaded from: classes.dex */
public class BasicNetwork implements Network {
    protected final HttpStack mHttpStack;
    protected final ByteArrayPool mPool;
    protected static final boolean DEBUG = VolleyLog.DEBUG;
    private static int SLOW_REQUEST_THRESHOLD_MS = PathInterpolatorCompat.MAX_NUM_POINTS;
    private static int DEFAULT_POOL_SIZE = 4096;

    public BasicNetwork(HttpStack httpStack) {
        this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
    }

    public BasicNetwork(HttpStack httpStack, ByteArrayPool byteArrayPool) {
        this.mHttpStack = httpStack;
        this.mPool = byteArrayPool;
    }

    private void addCacheHeaders(Map<String, String> map, Cache.Entry entry) {
        if (entry == null) {
            return;
        }
        if (entry.etag != null) {
            map.put("If-None-Match", entry.etag);
        }
        if (entry.serverDate > 0) {
            map.put("If-Modified-Since", DateUtils.formatDate(new Date(entry.serverDate)));
        }
    }

    private static void attemptRetryOnException(String str, Request<?> request, VolleyError volleyError) throws VolleyError {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int timeoutMs = request.getTimeoutMs();
        try {
            retryPolicy.retry(volleyError);
            request.addMarker(String.format("%s-retry [timeout=%s]", str, Integer.valueOf(timeoutMs)));
        } catch (VolleyError e) {
            request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", str, Integer.valueOf(timeoutMs)));
            throw e;
        }
    }

    private static Map<String, String> convertHeaders(Header[] headerArr) {
        HashMap hashMap = new HashMap();
        for (int i = 0; i < headerArr.length; i++) {
            hashMap.put(headerArr[i].getName(), headerArr[i].getValue());
        }
        return hashMap;
    }

    private byte[] entityToBytes(HttpEntity httpEntity) throws IOException, ServerError {
        PoolingByteArrayOutputStream poolingByteArrayOutputStream = new PoolingByteArrayOutputStream(this.mPool, (int) httpEntity.getContentLength());
        byte[] bArr = null;
        try {
            InputStream content = httpEntity.getContent();
            if (content == null) {
                throw new ServerError();
            }
            byte[] buf = this.mPool.getBuf(1024);
            while (true) {
                try {
                    int read = content.read(buf);
                    if (read == -1) {
                        break;
                    }
                    poolingByteArrayOutputStream.write(buf, 0, read);
                } catch (Throwable th) {
                    th = th;
                    bArr = buf;
                    try {
                        httpEntity.consumeContent();
                    } catch (IOException unused) {
                        VolleyLog.v("Error occured when calling consumingContent", new Object[0]);
                    }
                    this.mPool.returnBuf(bArr);
                    poolingByteArrayOutputStream.close();
                    throw th;
                }
            }
            byte[] byteArray = poolingByteArrayOutputStream.toByteArray();
            try {
                httpEntity.consumeContent();
            } catch (IOException unused2) {
                VolleyLog.v("Error occured when calling consumingContent", new Object[0]);
            }
            this.mPool.returnBuf(buf);
            poolingByteArrayOutputStream.close();
            return byteArray;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void logSlowRequests(long j, Request<?> request, byte[] bArr, StatusLine statusLine) {
        if (DEBUG || j > SLOW_REQUEST_THRESHOLD_MS) {
            Object[] objArr = new Object[5];
            objArr[0] = request;
            objArr[1] = Long.valueOf(j);
            objArr[2] = bArr != null ? Integer.valueOf(bArr.length) : "null";
            objArr[3] = Integer.valueOf(statusLine.getStatusCode());
            objArr[4] = Integer.valueOf(request.getRetryPolicy().getCurrentRetryCount());
            VolleyLog.d("HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]", objArr);
        }
    }

    protected void logError(String str, String str2, long j) {
        VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", str, Long.valueOf(SystemClock.elapsedRealtime() - j), str2);
    }

    @Override // com.android.volley.Network
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        Map<String, String> map;
        byte[] bArr;
        HttpResponse httpResponse;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        while (true) {
            HashMap hashMap = new HashMap();
            try {
                try {
                    HashMap hashMap2 = new HashMap();
                    addCacheHeaders(hashMap2, request.getCacheEntry());
                    httpResponse = this.mHttpStack.performRequest(request, hashMap2);
                    try {
                        StatusLine statusLine = httpResponse.getStatusLine();
                        int statusCode = statusLine.getStatusCode();
                        map = convertHeaders(httpResponse.getAllHeaders());
                        if (statusCode == 304) {
                            return new NetworkResponse(304, request.getCacheEntry().data, map, true);
                        }
                        try {
                            byte[] entityToBytes = entityToBytes(httpResponse.getEntity());
                            try {
                                try {
                                    logSlowRequests(SystemClock.elapsedRealtime() - elapsedRealtime, request, entityToBytes, statusLine);
                                    if (statusCode != 200 && statusCode != 204) {
                                        throw new IOException();
                                        break;
                                    }
                                    bArr = entityToBytes;
                                    try {
                                        return new NetworkResponse(statusCode, bArr, map, false);
                                    } catch (IOException e) {
                                        e = e;
                                    }
                                } catch (IOException e2) {
                                    e = e2;
                                    bArr = entityToBytes;
                                }
                            } catch (IOException e3) {
                                e = e3;
                                bArr = entityToBytes;
                            }
                        } catch (IOException e4) {
                            e = e4;
                        }
                        e = e4;
                    } catch (IOException e5) {
                        e = e5;
                        map = hashMap;
                    }
                    bArr = null;
                } catch (IOException e6) {
                    e = e6;
                    map = hashMap;
                    bArr = null;
                    httpResponse = null;
                }
            } catch (MalformedURLException e7) {
                throw new RuntimeException("Bad URL " + request.getUrl(), e7);
            } catch (SocketTimeoutException unused) {
                attemptRetryOnException("socket", request, new TimeoutError());
            } catch (ConnectTimeoutException unused2) {
                attemptRetryOnException("connection", request, new TimeoutError());
            }
            if (httpResponse == null) {
                throw new NoConnectionError(e);
            }
            int statusCode2 = httpResponse.getStatusLine().getStatusCode();
            VolleyLog.e("Unexpected response code %d for %s", Integer.valueOf(statusCode2), request.getUrl());
            if (bArr == null) {
                throw new NetworkError((NetworkResponse) null);
            }
            NetworkResponse networkResponse = new NetworkResponse(statusCode2, bArr, map, false);
            if (statusCode2 != 401 && statusCode2 != 403) {
                throw new ServerError(networkResponse);
            }
            attemptRetryOnException("auth", request, new AuthFailureError(networkResponse));
        }
    }
}
