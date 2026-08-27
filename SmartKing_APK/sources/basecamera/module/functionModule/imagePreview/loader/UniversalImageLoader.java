package basecamera.module.functionModule.imagePreview.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import basecamera.module.functionModule.imagePreview.loader.ImageLoader;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import java.io.File;

/* loaded from: classes.dex */
public class UniversalImageLoader implements ImageLoader {
    private Context context;
    private DisplayImageOptions normalImageOptions;

    private UniversalImageLoader(Context context) {
        this.context = context;
        initImageLoader(context);
    }

    private void initImageLoader(Context context) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCache lruMemoryCache = Build.VERSION.SDK_INT >= 9 ? new LruMemoryCache(maxMemory) : new LRULimitedMemoryCache(maxMemory);
        this.normalImageOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(this.normalImageOptions).denyCacheImageMultipleSizesInMemory().memoryCache(lruMemoryCache).tasksProcessingOrder(QueueProcessingType.FIFO).threadPriority(3).threadPoolSize(3).build());
    }

    public static UniversalImageLoader with(Context context) {
        return new UniversalImageLoader(context);
    }

    @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader
    public void clearCache() {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().getMemoryCache().clear();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().getDiskCache().clear();
    }

    @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader
    public boolean isLoaded(String str) {
        File file = com.nostra13.universalimageloader.core.ImageLoader.getInstance().getDiskCache().get(str);
        return file != null && file.exists();
    }

    @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader
    public void loadImageAsync(String str, final ImageLoader.ThumbnailCallback thumbnailCallback) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(str, new ImageLoadingListener() { // from class: basecamera.module.functionModule.imagePreview.loader.UniversalImageLoader.3
            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingCancelled(String str2, View view) {
                thumbnailCallback.onFinish(null);
            }

            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingComplete(String str2, View view, Bitmap bitmap) {
                thumbnailCallback.onFinish(new BitmapDrawable(UniversalImageLoader.this.context.getResources(), bitmap));
            }

            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingFailed(String str2, View view, FailReason failReason) {
                thumbnailCallback.onFinish(null);
            }

            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingStarted(String str2, View view) {
            }
        });
    }

    @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader
    public Drawable loadImageSync(String str) {
        return new BitmapDrawable(com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(str, this.normalImageOptions));
    }

    @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader
    public void showImage(String str, ImageView imageView, Drawable drawable, final ImageLoader.SourceCallback sourceCallback) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(str, imageView, new DisplayImageOptions.Builder().showImageOnLoading(drawable).showImageOnFail(drawable).bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).build(), new ImageLoadingListener() { // from class: basecamera.module.functionModule.imagePreview.loader.UniversalImageLoader.1
            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingCancelled(String str2, View view) {
                if (sourceCallback != null) {
                    sourceCallback.onDelivered(-1);
                }
            }

            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingComplete(String str2, View view, Bitmap bitmap) {
                if (sourceCallback != null) {
                    sourceCallback.onFinish();
                    sourceCallback.onDelivered(1);
                }
            }

            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingFailed(String str2, View view, FailReason failReason) {
                if (sourceCallback != null) {
                    sourceCallback.onDelivered(0);
                }
            }

            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingStarted(String str2, View view) {
                if (sourceCallback != null) {
                    sourceCallback.onStart();
                }
            }
        }, new ImageLoadingProgressListener() { // from class: basecamera.module.functionModule.imagePreview.loader.UniversalImageLoader.2
            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener
            public void onProgressUpdate(String str2, View view, int i, int i2) {
                if (sourceCallback != null) {
                    sourceCallback.onProgress((i * 100) / i2);
                }
            }
        });
    }
}
