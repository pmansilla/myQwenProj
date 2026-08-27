package com.amap.location.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/* compiled from: GZipUtil.java */
/* loaded from: classes.dex */
public class d {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.io.Closeable, java.util.zip.GZIPOutputStream] */
    /* JADX WARN: Type inference failed for: r2v6 */
    public static byte[] a(byte[] bArr) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream;
        ?? r2;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                r2 = new GZIPOutputStream(byteArrayOutputStream);
            } catch (Exception e) {
                e = e;
                r2 = 0;
            } catch (Throwable th) {
                th = th;
                e.a(byteArrayOutputStream);
                e.a(byteArrayOutputStream2);
                throw th;
            }
            try {
                r2.write(bArr);
                r2.finish();
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                e.a(byteArrayOutputStream);
                e.a((Closeable) r2);
                return byteArray;
            } catch (Exception e2) {
                e = e2;
                byteArrayOutputStream2 = byteArrayOutputStream;
                r2 = r2;
                try {
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                    byteArrayOutputStream = byteArrayOutputStream2;
                    byteArrayOutputStream2 = r2;
                    e.a(byteArrayOutputStream);
                    e.a(byteArrayOutputStream2);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                byteArrayOutputStream2 = r2;
                e.a(byteArrayOutputStream);
                e.a(byteArrayOutputStream2);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            r2 = 0;
        } catch (Throwable th4) {
            th = th4;
            byteArrayOutputStream = null;
        }
    }

    public static byte[] b(byte[] bArr) throws Exception {
        InputStream inputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        InputStream inputStream2;
        InputStream inputStream3;
        InputStream inputStream4 = null;
        try {
            inputStream = new ByteArrayInputStream(bArr);
            try {
                InputStream gZIPInputStream = new GZIPInputStream(inputStream);
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        e.a(gZIPInputStream, byteArrayOutputStream);
                        byteArrayOutputStream.flush();
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        e.a((Closeable) inputStream);
                        e.a((Closeable) gZIPInputStream);
                        e.a(byteArrayOutputStream);
                        return byteArray;
                    } catch (Exception e) {
                        inputStream3 = inputStream;
                        inputStream2 = gZIPInputStream;
                        e = e;
                        inputStream4 = inputStream3;
                        try {
                            throw e;
                        } catch (Throwable th) {
                            th = th;
                            InputStream inputStream5 = inputStream2;
                            inputStream = inputStream4;
                            inputStream4 = inputStream5;
                            e.a((Closeable) inputStream);
                            e.a((Closeable) inputStream4);
                            e.a(byteArrayOutputStream);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        inputStream4 = gZIPInputStream;
                        th = th2;
                        e.a((Closeable) inputStream);
                        e.a((Closeable) inputStream4);
                        e.a(byteArrayOutputStream);
                        throw th;
                    }
                } catch (Exception e2) {
                    inputStream3 = inputStream;
                    inputStream2 = gZIPInputStream;
                    e = e2;
                    byteArrayOutputStream = null;
                } catch (Throwable th3) {
                    inputStream4 = gZIPInputStream;
                    th = th3;
                    byteArrayOutputStream = null;
                }
            } catch (Exception e3) {
                e = e3;
                byteArrayOutputStream = null;
                inputStream4 = inputStream;
                inputStream2 = null;
            } catch (Throwable th4) {
                th = th4;
                byteArrayOutputStream = null;
            }
        } catch (Exception e4) {
            e = e4;
            inputStream2 = null;
            byteArrayOutputStream = null;
        } catch (Throwable th5) {
            th = th5;
            inputStream = null;
            byteArrayOutputStream = null;
        }
    }
}
