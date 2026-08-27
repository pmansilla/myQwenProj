package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.request.LoadRequest;

/* loaded from: classes2.dex */
public abstract class DecodeHelper {
    /* JADX INFO: Access modifiers changed from: protected */
    public void correctOrientation(@NonNull ImageOrientationCorrector imageOrientationCorrector, @NonNull DecodeResult decodeResult, int i, @NonNull LoadRequest loadRequest) throws CorrectOrientationException {
        BitmapDecodeResult bitmapDecodeResult;
        Bitmap bitmap;
        Bitmap rotate;
        if (!(decodeResult instanceof BitmapDecodeResult) || (rotate = imageOrientationCorrector.rotate((bitmap = (bitmapDecodeResult = (BitmapDecodeResult) decodeResult).getBitmap()), i, loadRequest.getConfiguration().getBitmapPool())) == null || rotate == bitmap) {
            return;
        }
        if (rotate.isRecycled()) {
            throw new CorrectOrientationException("Bitmap recycled. exifOrientation=" + ImageOrientationCorrector.toName(i));
        }
        BitmapPoolUtils.freeBitmapToPool(bitmap, loadRequest.getConfiguration().getBitmapPool());
        bitmapDecodeResult.setBitmap(rotate);
        bitmapDecodeResult.setProcessed(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NonNull
    public abstract DecodeResult decode(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options, @NonNull BitmapFactory.Options options2, int i) throws DecodeException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean match(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options);
}
