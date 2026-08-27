package com.mob.mobapm.proxy.d;

import com.mob.mobapm.e.d;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

/* loaded from: classes.dex */
public class a implements HttpEntity {
    private HttpEntity a;
    private ByteArrayOutputStream b;

    public a(HttpEntity httpEntity) {
        this.a = httpEntity;
    }

    @Override // org.apache.http.HttpEntity
    public void consumeContent() throws IOException {
        this.a.consumeContent();
    }

    @Override // org.apache.http.HttpEntity
    public InputStream getContent() throws IOException, IllegalStateException {
        ByteArrayOutputStream byteArrayOutputStream = this.b;
        if (byteArrayOutputStream != null && byteArrayOutputStream.size() <= 0) {
            return this.a.getContent();
        }
        if (this.b == null) {
            this.b = d.a(this.a.getContent());
        }
        return this.b.size() > 0 ? new ByteArrayInputStream(this.b.toByteArray()) : this.a.getContent();
    }

    @Override // org.apache.http.HttpEntity
    public Header getContentEncoding() {
        return this.a.getContentEncoding();
    }

    @Override // org.apache.http.HttpEntity
    public long getContentLength() {
        return this.a.getContentLength();
    }

    @Override // org.apache.http.HttpEntity
    public Header getContentType() {
        return this.a.getContentType();
    }

    @Override // org.apache.http.HttpEntity
    public boolean isChunked() {
        return this.a.isChunked();
    }

    @Override // org.apache.http.HttpEntity
    public boolean isRepeatable() {
        return this.a.isRepeatable();
    }

    @Override // org.apache.http.HttpEntity
    public boolean isStreaming() {
        return this.a.isStreaming();
    }

    @Override // org.apache.http.HttpEntity
    public void writeTo(OutputStream outputStream) throws IOException {
        this.a.writeTo(outputStream);
    }
}
