package me.panpf.sketch.uri;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.InputStream;
import java.io.OutputStream;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public abstract class AbsStreamDiskCacheUriModel extends AbsDiskCacheUriModel<InputStream> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.AbsDiskCacheUriModel
    public final void closeContent(@NonNull InputStream inputStream, @NonNull Context context) {
        SketchUtils.close(inputStream);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.AbsDiskCacheUriModel
    public final void outContent(@NonNull InputStream inputStream, @NonNull OutputStream outputStream) throws Exception {
        byte[] bArr = new byte[8192];
        while (true) {
            int read = inputStream.read(bArr);
            if (read < 0) {
                return;
            } else {
                outputStream.write(bArr, 0, read);
            }
        }
    }
}
