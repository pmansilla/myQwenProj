package com.mob.mobapm.proxy;

import com.mob.mobapm.core.Transaction;
import com.mob.mobapm.e.d;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Permission;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class a extends HttpURLConnection {
    public HttpURLConnection a;
    public Transaction b;
    private c c;

    public a(HttpURLConnection httpURLConnection) {
        super(httpURLConnection.getURL());
        this.a = httpURLConnection;
        Transaction transaction = new Transaction();
        this.b = transaction;
        com.mob.mobapm.c.a.b(transaction, httpURLConnection);
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        try {
            com.mob.mobapm.c.a.c(this.b, this.a);
            this.a.connect();
        } catch (IOException e) {
            com.mob.mobapm.c.a.a(this.b, this.a, e.getMessage());
            throw e;
        }
    }

    @Override // java.net.HttpURLConnection
    public void disconnect() {
        com.mob.mobapm.c.a.a(this.b, this.a);
        this.a.disconnect();
    }

    @Override // java.net.URLConnection
    public boolean getAllowUserInteraction() {
        return this.a.getAllowUserInteraction();
    }

    @Override // java.net.URLConnection
    public int getConnectTimeout() {
        return this.a.getConnectTimeout();
    }

    @Override // java.net.URLConnection
    public Object getContent() throws IOException {
        try {
            return this.a.getContent();
        } catch (IOException e) {
            com.mob.mobapm.c.a.a(this.b, this.a);
            throw e;
        }
    }

    @Override // java.net.URLConnection
    public Object getContent(Class[] clsArr) throws IOException {
        try {
            return this.a.getContent(clsArr);
        } catch (IOException e) {
            com.mob.mobapm.c.a.a(this.b, this.a);
            throw e;
        }
    }

    @Override // java.net.URLConnection
    public String getContentEncoding() {
        return this.a.getContentEncoding();
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        return this.a.getContentLength();
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        return this.a.getContentType();
    }

    @Override // java.net.URLConnection
    public long getDate() {
        return this.a.getDate();
    }

    @Override // java.net.URLConnection
    public boolean getDefaultUseCaches() {
        return this.a.getDefaultUseCaches();
    }

    @Override // java.net.URLConnection
    public boolean getDoInput() {
        return this.a.getDoInput();
    }

    @Override // java.net.URLConnection
    public boolean getDoOutput() {
        return this.a.getDoOutput();
    }

    @Override // java.net.HttpURLConnection
    public InputStream getErrorStream() {
        try {
            ByteArrayOutputStream a = d.a(this.a.getErrorStream());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(a.toByteArray());
            ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(a.toByteArray());
            com.mob.mobapm.c.a.a(this.b, this.a, d.b(byteArrayInputStream));
            return byteArrayInputStream2;
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: httpURLConn get errorStream error: " + th, new Object[0]);
            return this.a.getErrorStream();
        }
    }

    @Override // java.net.URLConnection
    public long getExpiration() {
        return this.a.getExpiration();
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderField(int i) {
        return this.a.getHeaderField(i);
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public long getHeaderFieldDate(String str, long j) {
        return this.a.getHeaderFieldDate(str, j);
    }

    @Override // java.net.URLConnection
    public int getHeaderFieldInt(String str, int i) {
        return this.a.getHeaderFieldInt(str, i);
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderFieldKey(int i) {
        return this.a.getHeaderFieldKey(i);
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getHeaderFields() {
        return this.a.getHeaderFields();
    }

    @Override // java.net.URLConnection
    public long getIfModifiedSince() {
        return this.a.getIfModifiedSince();
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        try {
            com.mob.mobapm.c.a.a(this.b, this.a);
            return this.a.getInputStream();
        } catch (IOException e) {
            com.mob.mobapm.c.a.a(this.b, this.a, e.getMessage());
            throw e;
        }
    }

    @Override // java.net.HttpURLConnection
    public boolean getInstanceFollowRedirects() {
        return this.a.getInstanceFollowRedirects();
    }

    @Override // java.net.URLConnection
    public long getLastModified() {
        return this.a.getLastModified();
    }

    @Override // java.net.URLConnection
    public OutputStream getOutputStream() throws IOException {
        try {
            com.mob.mobapm.c.a.c(this.b, this.a);
            if (com.mob.mobapm.core.c.j <= 0) {
                return this.a.getOutputStream();
            }
            if (this.c == null) {
                this.c = new c(this.a.getOutputStream(), this.b);
            }
            return this.c;
        } catch (IOException e) {
            com.mob.mobapm.c.a.a(this.b, this.a, e.getMessage());
            throw e;
        }
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public Permission getPermission() throws IOException {
        return this.a.getPermission();
    }

    @Override // java.net.URLConnection
    public int getReadTimeout() {
        return this.a.getReadTimeout();
    }

    @Override // java.net.HttpURLConnection
    public String getRequestMethod() {
        return this.a.getRequestMethod();
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getRequestProperties() {
        return this.a.getRequestProperties();
    }

    @Override // java.net.URLConnection
    public String getRequestProperty(String str) {
        return this.a.getRequestProperty(str);
    }

    @Override // java.net.HttpURLConnection
    public int getResponseCode() throws IOException {
        try {
            return this.a.getResponseCode();
        } catch (IOException e) {
            com.mob.mobapm.c.a.a(this.b, this.a, e.getMessage());
            throw e;
        }
    }

    @Override // java.net.HttpURLConnection
    public String getResponseMessage() throws IOException {
        try {
            return this.a.getResponseMessage();
        } catch (IOException e) {
            com.mob.mobapm.c.a.a(this.b, this.a);
            throw e;
        }
    }

    @Override // java.net.URLConnection
    public URL getURL() {
        return this.a.getURL();
    }

    @Override // java.net.URLConnection
    public boolean getUseCaches() {
        return this.a.getUseCaches();
    }

    @Override // java.net.URLConnection
    public void setAllowUserInteraction(boolean z) {
        this.a.setAllowUserInteraction(z);
    }

    @Override // java.net.HttpURLConnection
    public void setChunkedStreamingMode(int i) {
        this.a.setChunkedStreamingMode(i);
    }

    @Override // java.net.URLConnection
    public void setConnectTimeout(int i) {
        this.a.setConnectTimeout(i);
    }

    @Override // java.net.URLConnection
    public void setDefaultUseCaches(boolean z) {
        this.a.setDefaultUseCaches(z);
    }

    @Override // java.net.URLConnection
    public void setDoInput(boolean z) {
        this.a.setDoInput(z);
    }

    @Override // java.net.URLConnection
    public void setDoOutput(boolean z) {
        this.a.setDoOutput(z);
    }

    @Override // java.net.HttpURLConnection
    public void setFixedLengthStreamingMode(int i) {
        this.a.setFixedLengthStreamingMode(i);
    }

    @Override // java.net.URLConnection
    public void setIfModifiedSince(long j) {
        this.a.setIfModifiedSince(j);
    }

    @Override // java.net.HttpURLConnection
    public void setInstanceFollowRedirects(boolean z) {
        this.a.setInstanceFollowRedirects(z);
    }

    @Override // java.net.URLConnection
    public void setReadTimeout(int i) {
        this.a.setReadTimeout(i);
    }

    @Override // java.net.HttpURLConnection
    public void setRequestMethod(String str) throws ProtocolException {
        try {
            this.a.setRequestMethod(str);
        } catch (ProtocolException e) {
            com.mob.mobapm.c.a.a(this.b, this.a, e.getMessage());
            throw e;
        }
    }

    @Override // java.net.URLConnection
    public void setRequestProperty(String str, String str2) {
        this.a.setRequestProperty(str, str2);
    }

    @Override // java.net.URLConnection
    public void setUseCaches(boolean z) {
        this.a.setUseCaches(z);
    }

    @Override // java.net.URLConnection
    public String toString() {
        return this.a.toString();
    }

    @Override // java.net.HttpURLConnection
    public boolean usingProxy() {
        return this.a.usingProxy();
    }
}
