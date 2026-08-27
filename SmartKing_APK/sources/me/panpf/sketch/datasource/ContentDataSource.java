package me.panpf.sketch.datasource;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.decode.NotFoundGifLibraryException;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.drawable.SketchGifFactory;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ContentDataSource implements DataSource {
    private Uri contentUri;
    private Context context;
    private long length = -1;

    public ContentDataSource(@NonNull Context context, @NonNull Uri uri) {
        this.context = context;
        this.contentUri = uri;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    public File getFile(File file, String str) throws IOException {
        if (file == null) {
            return null;
        }
        if (!file.exists() && !file.getParentFile().mkdirs()) {
            return null;
        }
        File file2 = !TextUtils.isEmpty(str) ? new File(file, str) : new File(file, SketchUtils.generatorTempFileName(this, this.contentUri.toString()));
        InputStream inputStream = getInputStream();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            while (true) {
                try {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        return file2;
                    }
                    fileOutputStream.write(bArr, 0, read);
                } finally {
                    SketchUtils.close(fileOutputStream);
                    SketchUtils.close(inputStream);
                }
            }
        } catch (IOException e) {
            SketchUtils.close(inputStream);
            throw e;
        }
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public ImageFrom getImageFrom() {
        return ImageFrom.LOCAL;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public InputStream getInputStream() throws IOException {
        InputStream openInputStream = this.context.getContentResolver().openInputStream(this.contentUri);
        if (openInputStream != null) {
            return openInputStream;
        }
        throw new IOException("ContentResolver.openInputStream() return null. " + this.contentUri.toString());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v6, types: [long] */
    @Override // me.panpf.sketch.datasource.DataSource
    public synchronized long getLength() throws IOException {
        long j = 0;
        if (this.length >= 0) {
            return this.length;
        }
        AssetFileDescriptor assetFileDescriptor = 0;
        AssetFileDescriptor assetFileDescriptor2 = null;
        try {
            try {
                AssetFileDescriptor openAssetFileDescriptor = this.context.getContentResolver().openAssetFileDescriptor(this.contentUri, "r");
                if (openAssetFileDescriptor != null) {
                    try {
                        j = openAssetFileDescriptor.getLength();
                    } catch (IOException e) {
                        e = e;
                        assetFileDescriptor2 = openAssetFileDescriptor;
                        e.printStackTrace();
                        SketchUtils.close(assetFileDescriptor2);
                        assetFileDescriptor = this.length;
                        return assetFileDescriptor;
                    } catch (Throwable th) {
                        th = th;
                        assetFileDescriptor = openAssetFileDescriptor;
                        SketchUtils.close(assetFileDescriptor);
                        throw th;
                    }
                }
                this.length = j;
                SketchUtils.close(openAssetFileDescriptor);
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e2) {
            e = e2;
        }
        assetFileDescriptor = this.length;
        return assetFileDescriptor;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public SketchGifDrawable makeGifDrawable(@NonNull String str, @NonNull String str2, @NonNull ImageAttrs imageAttrs, @NonNull BitmapPool bitmapPool) throws IOException, NotFoundGifLibraryException {
        return SketchGifFactory.createGifDrawable(str, str2, imageAttrs, getImageFrom(), bitmapPool, this.context.getContentResolver(), this.contentUri);
    }
}
