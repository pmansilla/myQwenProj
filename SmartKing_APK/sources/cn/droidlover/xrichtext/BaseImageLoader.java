package cn.droidlover.xrichtext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: classes.dex */
public class BaseImageLoader implements ImageLoader {
    protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    public static final int DEFAULT_BUFFER_SIZE = 32768;
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    public static final int DEFAULT_HTTP_READ_TIMEOUT = 20000;
    protected final int connectTimeout;
    protected final Context context;
    protected final int readTimeout;

    public BaseImageLoader(Context context) {
        this(context, 5000, 20000);
    }

    public BaseImageLoader(Context context, int i, int i2) {
        this.context = context.getApplicationContext();
        this.connectTimeout = i;
        this.readTimeout = i2;
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    public static void readAndCloseStream(InputStream inputStream) {
        do {
            try {
            } catch (IOException unused) {
            } catch (Throwable th) {
                closeSilently(inputStream);
                throw th;
            }
        } while (inputStream.read(new byte[32768], 0, 32768) != -1);
        closeSilently(inputStream);
    }

    protected HttpURLConnection createConnection(String str) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Uri.encode(str, ALLOWED_URI_CHARS)).openConnection();
        httpURLConnection.setConnectTimeout(this.connectTimeout);
        httpURLConnection.setReadTimeout(this.readTimeout);
        return httpURLConnection;
    }

    @Override // cn.droidlover.xrichtext.ImageLoader
    public Bitmap getBitmap(String str) throws IOException {
        HttpURLConnection createConnection = createConnection(str);
        try {
            InputStream inputStream = createConnection.getInputStream();
            if (shouldBeProcessed(createConnection)) {
                return BitmapFactory.decodeStream(new BufferedInputStream(inputStream, 32768));
            }
            closeSilently(inputStream);
            throw new IOException("Image request failed with response code " + createConnection.getResponseCode());
        } catch (IOException e) {
            readAndCloseStream(createConnection.getErrorStream());
            throw e;
        }
    }

    protected boolean shouldBeProcessed(HttpURLConnection httpURLConnection) throws IOException {
        return httpURLConnection.getResponseCode() == 200;
    }
}
