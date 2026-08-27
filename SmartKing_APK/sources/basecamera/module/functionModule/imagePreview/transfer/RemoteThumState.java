package basecamera.module.functionModule.imagePreview.transfer;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import basecamera.module.functionModule.imagePreview.loader.ImageLoader;
import basecamera.module.functionModule.imagePreview.style.IProgressIndicator;
import basecamera.module.functionModule.imagePreview.view.image.TransferImage;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RemoteThumState extends TransferState {
    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteThumState(TransferLayout transferLayout) {
        super(transferLayout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSourceImage(Drawable drawable, final int i, final TransferImage transferImage, final IProgressIndicator iProgressIndicator) {
        final TransferConfig transConfig = this.transfer.getTransConfig();
        transConfig.getImageLoader().showImage(transConfig.getSourceImageList().get(i), transferImage, drawable, new ImageLoader.SourceCallback() { // from class: basecamera.module.functionModule.imagePreview.transfer.RemoteThumState.2
            @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.SourceCallback
            public void onDelivered(int i2) {
                switch (i2) {
                    case 0:
                        transferImage.setImageDrawable(transConfig.getErrorDrawable(RemoteThumState.this.transfer.getContext()));
                        return;
                    case 1:
                        iProgressIndicator.onFinish(i);
                        transferImage.enable();
                        RemoteThumState.this.transfer.bindOnOperationListener(transferImage, i);
                        return;
                    default:
                        return;
                }
            }

            @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.SourceCallback
            public void onFinish() {
            }

            @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.SourceCallback
            public void onProgress(int i2) {
                iProgressIndicator.onProgress(i, i2);
            }

            @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.SourceCallback
            public void onStart() {
                iProgressIndicator.onStart(i);
            }
        });
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public TransferImage createTransferIn(int i) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        TransferImage createTransferImage = createTransferImage(transConfig.getOriginImageList().get(i));
        transformThumbnail(transConfig.getThumbnailImageList().get(i), createTransferImage, true);
        this.transfer.addView(createTransferImage, 1);
        return createTransferImage;
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public void prepareTransfer(TransferImage transferImage, int i) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        ImageLoader imageLoader = transConfig.getImageLoader();
        String str = transConfig.getThumbnailImageList().get(i);
        if (imageLoader.isLoaded(str)) {
            imageLoader.showImage(str, transferImage, transConfig.getMissDrawable(this.transfer.getContext()), null);
        } else {
            transferImage.setImageDrawable(transConfig.getMissDrawable(this.transfer.getContext()));
        }
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public void transferLoad(final int i) {
        TransferAdapter transAdapter = this.transfer.getTransAdapter();
        final TransferConfig transConfig = this.transfer.getTransConfig();
        final TransferImage imageItem = this.transfer.getTransAdapter().getImageItem(i);
        ImageLoader imageLoader = transConfig.getImageLoader();
        final IProgressIndicator progressIndicator = transConfig.getProgressIndicator();
        progressIndicator.attach(i, transAdapter.getParentItem(i));
        if (transConfig.isJustLoadHitImage()) {
            loadSourceImage(imageItem.getDrawable(), i, imageItem, progressIndicator);
            return;
        }
        String str = transConfig.getThumbnailImageList().get(i);
        if (imageLoader.isLoaded(str)) {
            imageLoader.loadImageAsync(str, new ImageLoader.ThumbnailCallback() { // from class: basecamera.module.functionModule.imagePreview.transfer.RemoteThumState.1
                @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.ThumbnailCallback
                public void onFinish(Drawable drawable) {
                    if (drawable == null) {
                        drawable = transConfig.getMissDrawable(RemoteThumState.this.transfer.getContext());
                    }
                    RemoteThumState.this.loadSourceImage(drawable, i, imageItem, progressIndicator);
                }
            });
        } else {
            loadSourceImage(transConfig.getMissDrawable(this.transfer.getContext()), i, imageItem, progressIndicator);
        }
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public TransferImage transferOut(int i) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        List<ImageView> originImageList = transConfig.getOriginImageList();
        if (i > originImageList.size() - 1 || originImageList.get(i) == null) {
            return null;
        }
        TransferImage createTransferImage = createTransferImage(originImageList.get(i));
        transformThumbnail(transConfig.getThumbnailImageList().get(i), createTransferImage, false);
        this.transfer.addView(createTransferImage, 1);
        return createTransferImage;
    }
}
