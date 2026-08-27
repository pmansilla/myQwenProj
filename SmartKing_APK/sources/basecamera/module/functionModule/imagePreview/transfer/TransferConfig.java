package basecamera.module.functionModule.imagePreview.transfer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.ImageView;
import basecamera.module.functionModule.imagePreview.loader.ImageLoader;
import basecamera.module.functionModule.imagePreview.style.IIndexIndicator;
import basecamera.module.functionModule.imagePreview.style.IProgressIndicator;
import basecamera.module.functionModule.imagePreview.transfer.Transferee;
import basecamera.module.lib.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class TransferConfig {
    private int backgroundColor;
    private long duration;
    private Drawable errorDrawable;
    private int errorPlaceHolder;

    @IdRes
    private int imageId;
    private ImageLoader imageLoader;
    private ImageView imageView;
    private IIndexIndicator indexIndicator;
    private boolean justLoadHitImage;
    private AbsListView listView;
    private Transferee.OnTransfereeLongClickListener longClickListener;
    private Drawable missDrawable;
    private int missPlaceHolder;
    private int nowThumbnailIndex;
    private int offscreenPageLimit;
    private List<ImageView> originImageList;
    private IProgressIndicator progressIndicator;
    private RecyclerView recyclerView;
    private List<String> sourceImageList;
    private List<String> thumbnailImageList;

    /* loaded from: classes.dex */
    public static class Builder {
        private int backgroundColor;
        private long duration;
        private Drawable errorDrawable;
        private int errorPlaceHolder;

        @IdRes
        private int imageId;
        private ImageLoader imageLoader;
        private ImageView imageView;
        private IIndexIndicator indexIndicator;
        private boolean justLoadHitImage;
        private AbsListView listView;
        private Transferee.OnTransfereeLongClickListener longClickListener;
        private Drawable missDrawable;
        private int missPlaceHolder;
        private int nowThumbnailIndex;
        private int offscreenPageLimit;
        private IProgressIndicator progressIndicator;
        private RecyclerView recyclerView;
        private List<String> sourceImageList;
        private List<String> thumbnailImageList;

        private TransferConfig create() {
            TransferConfig transferConfig = new TransferConfig();
            transferConfig.setNowThumbnailIndex(this.nowThumbnailIndex);
            transferConfig.setOffscreenPageLimit(this.offscreenPageLimit);
            transferConfig.setMissPlaceHolder(this.missPlaceHolder);
            transferConfig.setErrorPlaceHolder(this.errorPlaceHolder);
            transferConfig.setBackgroundColor(this.backgroundColor);
            transferConfig.setDuration(this.duration);
            transferConfig.setJustLoadHitImage(this.justLoadHitImage);
            transferConfig.setMissDrawable(this.missDrawable);
            transferConfig.setErrorDrawable(this.errorDrawable);
            transferConfig.setSourceImageList(this.sourceImageList);
            transferConfig.setThumbnailImageList(this.thumbnailImageList);
            transferConfig.setProgressIndicator(this.progressIndicator);
            transferConfig.setIndexIndicator(this.indexIndicator);
            transferConfig.setImageLoader(this.imageLoader);
            transferConfig.setImageId(this.imageId);
            transferConfig.setImageView(this.imageView);
            transferConfig.setListView(this.listView);
            transferConfig.setRecyclerView(this.recyclerView);
            transferConfig.setLongClickListener(this.longClickListener);
            return transferConfig;
        }

        public TransferConfig bindImageView(ImageView imageView, String str) {
            this.imageView = imageView;
            this.sourceImageList = new ArrayList();
            this.sourceImageList.add(str);
            return create();
        }

        public TransferConfig bindImageView(ImageView imageView, String str, String str2) {
            this.imageView = imageView;
            this.thumbnailImageList = new ArrayList();
            this.thumbnailImageList.add(str);
            this.sourceImageList = new ArrayList();
            this.sourceImageList.add(str2);
            return create();
        }

        public TransferConfig bindImageView(ImageView imageView, List<String> list) {
            this.imageView = imageView;
            this.sourceImageList = list;
            return create();
        }

        public TransferConfig bindImageView(ImageView imageView, List<String> list, List<String> list2) {
            this.imageView = imageView;
            this.thumbnailImageList = list;
            this.sourceImageList = list2;
            return create();
        }

        public TransferConfig bindListView(AbsListView absListView, int i) {
            this.listView = absListView;
            this.imageId = i;
            return create();
        }

        public TransferConfig bindRecyclerView(RecyclerView recyclerView, int i) {
            this.recyclerView = recyclerView;
            this.imageId = i;
            return create();
        }

        public Builder setBackgroundColor(int i) {
            this.backgroundColor = i;
            return this;
        }

        public Builder setDuration(long j) {
            this.duration = j;
            return this;
        }

        public Builder setErrorDrawable(Drawable drawable) {
            this.errorDrawable = drawable;
            return this;
        }

        public Builder setErrorPlaceHolder(int i) {
            this.errorPlaceHolder = i;
            return this;
        }

        public Builder setImageLoader(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }

        public Builder setIndexIndicator(IIndexIndicator iIndexIndicator) {
            this.indexIndicator = iIndexIndicator;
            return this;
        }

        public Builder setJustLoadHitImage(boolean z) {
            this.justLoadHitImage = z;
            return this;
        }

        public Builder setMissDrawable(Drawable drawable) {
            this.missDrawable = drawable;
            return this;
        }

        public Builder setMissPlaceHolder(int i) {
            this.missPlaceHolder = i;
            return this;
        }

        public Builder setNowThumbnailIndex(int i) {
            this.nowThumbnailIndex = i;
            return this;
        }

        public Builder setOffscreenPageLimit(int i) {
            this.offscreenPageLimit = i;
            return this;
        }

        public Builder setOnLongClcikListener(Transferee.OnTransfereeLongClickListener onTransfereeLongClickListener) {
            this.longClickListener = onTransfereeLongClickListener;
            return this;
        }

        public Builder setProgressIndicator(IProgressIndicator iProgressIndicator) {
            this.progressIndicator = iProgressIndicator;
            return this;
        }

        public Builder setSourceImageList(List<String> list) {
            this.sourceImageList = list;
            return this;
        }

        public Builder setThumbnailImageList(List<String> list) {
            this.thumbnailImageList = list;
            return this;
        }
    }

    public static Builder build() {
        return new Builder();
    }

    public int getBackgroundColor() {
        if (this.backgroundColor == 0) {
            return -16777216;
        }
        return this.backgroundColor;
    }

    public long getDuration() {
        return this.duration;
    }

    public Drawable getErrorDrawable(Context context) {
        return this.errorDrawable != null ? this.errorDrawable : this.errorPlaceHolder != 0 ? context.getResources().getDrawable(this.errorPlaceHolder) : context.getResources().getDrawable(R.drawable.ic_empty_photo);
    }

    public int getErrorPlaceHolder() {
        return this.errorPlaceHolder;
    }

    public int getImageId() {
        return this.imageId;
    }

    public ImageLoader getImageLoader() {
        return this.imageLoader;
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public IIndexIndicator getIndexIndicator() {
        return this.indexIndicator;
    }

    public AbsListView getListView() {
        return this.listView;
    }

    public Transferee.OnTransfereeLongClickListener getLongClickListener() {
        return this.longClickListener;
    }

    public Drawable getMissDrawable(Context context) {
        return this.missDrawable != null ? this.missDrawable : this.missPlaceHolder != 0 ? context.getResources().getDrawable(this.missPlaceHolder) : context.getResources().getDrawable(R.drawable.ic_empty_photo);
    }

    public int getMissPlaceHolder() {
        return this.missPlaceHolder;
    }

    public int getNowThumbnailIndex() {
        return this.nowThumbnailIndex;
    }

    public int getOffscreenPageLimit() {
        return this.offscreenPageLimit;
    }

    public List<ImageView> getOriginImageList() {
        return this.originImageList == null ? new ArrayList() : this.originImageList;
    }

    public IProgressIndicator getProgressIndicator() {
        return this.progressIndicator;
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public List<String> getSourceImageList() {
        return this.sourceImageList;
    }

    public List<String> getThumbnailImageList() {
        return this.thumbnailImageList;
    }

    public boolean isJustLoadHitImage() {
        return this.justLoadHitImage;
    }

    public boolean isSourceEmpty() {
        return this.sourceImageList == null || this.sourceImageList.isEmpty();
    }

    public boolean isThumbnailEmpty() {
        return this.thumbnailImageList == null || this.thumbnailImageList.isEmpty();
    }

    public void setBackgroundColor(int i) {
        this.backgroundColor = i;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public void setErrorDrawable(Drawable drawable) {
        this.errorDrawable = drawable;
    }

    public void setErrorPlaceHolder(int i) {
        this.errorPlaceHolder = i;
    }

    public void setImageId(int i) {
        this.imageId = i;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setIndexIndicator(IIndexIndicator iIndexIndicator) {
        this.indexIndicator = iIndexIndicator;
    }

    public void setJustLoadHitImage(boolean z) {
        this.justLoadHitImage = z;
    }

    public void setListView(AbsListView absListView) {
        this.listView = absListView;
    }

    public void setLongClickListener(Transferee.OnTransfereeLongClickListener onTransfereeLongClickListener) {
        this.longClickListener = onTransfereeLongClickListener;
    }

    public void setMissDrawable(Drawable drawable) {
        this.missDrawable = drawable;
    }

    public void setMissPlaceHolder(int i) {
        this.missPlaceHolder = i;
    }

    public void setNowThumbnailIndex(int i) {
        this.nowThumbnailIndex = i;
    }

    public void setOffscreenPageLimit(int i) {
        this.offscreenPageLimit = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOriginImageList(List<ImageView> list) {
        this.originImageList = list;
    }

    public void setProgressIndicator(IProgressIndicator iProgressIndicator) {
        this.progressIndicator = iProgressIndicator;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setSourceImageList(List<String> list) {
        this.sourceImageList = list;
    }

    public void setThumbnailImageList(List<String> list) {
        this.thumbnailImageList = list;
    }
}
