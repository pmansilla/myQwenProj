package com.sun.mail.handlers;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataSource;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;

/* loaded from: classes2.dex */
public class text_plain extends handler_base {
    private static ActivationDataFlavor[] myDF = {new ActivationDataFlavor(String.class, "text/plain", "Text String")};

    /* loaded from: classes2.dex */
    private static class NoCloseOutputStream extends FilterOutputStream {
        public NoCloseOutputStream(OutputStream outputStream) {
            super(outputStream);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }
    }

    private String getCharset(String str) {
        try {
            String parameter = new ContentType(str).getParameter("charset");
            if (parameter == null) {
                parameter = "us-ascii";
            }
            return MimeUtility.javaCharset(parameter);
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource dataSource) throws IOException {
        String str;
        try {
            str = getCharset(dataSource.getContentType());
        } catch (IllegalArgumentException unused) {
            str = null;
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(dataSource.getInputStream(), str);
            try {
                char[] cArr = new char[1024];
                int i = 0;
                while (true) {
                    int read = inputStreamReader.read(cArr, i, cArr.length - i);
                    if (read == -1) {
                        break;
                    }
                    i += read;
                    if (i >= cArr.length) {
                        int length = cArr.length;
                        char[] cArr2 = new char[length < 262144 ? length + length : length + 262144];
                        System.arraycopy(cArr, 0, cArr2, 0, i);
                        cArr = cArr2;
                    }
                }
                return new String(cArr, 0, i);
            } finally {
                try {
                    inputStreamReader.close();
                } catch (IOException unused2) {
                }
            }
        } catch (IllegalArgumentException unused3) {
            throw new UnsupportedEncodingException(str);
        }
    }

    @Override // com.sun.mail.handlers.handler_base
    protected ActivationDataFlavor[] getDataFlavors() {
        return myDF;
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String str, OutputStream outputStream) throws IOException {
        String str2;
        if (!(obj instanceof String)) {
            throw new IOException("\"" + getDataFlavors()[0].getMimeType() + "\" DataContentHandler requires String object, was given object of type " + obj.getClass().toString());
        }
        try {
            str2 = getCharset(str);
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new NoCloseOutputStream(outputStream), str2);
                String str3 = (String) obj;
                outputStreamWriter.write(str3, 0, str3.length());
                outputStreamWriter.close();
            } catch (IllegalArgumentException unused) {
                throw new UnsupportedEncodingException(str2);
            }
        } catch (IllegalArgumentException unused2) {
            str2 = null;
        }
    }
}
