package basecamera.module.functionModule.imagePreview.transfer;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import basecamera.module.functionModule.imagePreview.loader.ImageLoader;
import basecamera.module.functionModule.imagePreview.view.image.TransferImage;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class LocalThumState extends TransferState {
    /* JADX INFO: Access modifiers changed from: package-private */
    public LocalThumState(TransferLayout transferLayout) {
        super(transferLayout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSourceImage(String str, final TransferImage transferImage, Drawable drawable, final int i) {
        final TransferConfig transConfig = this.transfer.getTransConfig();
        transConfig.getImageLoader().showImage(str, transferImage, drawable, new ImageLoader.SourceCallback() { // from class: basecamera.module.functionModule.imagePreview.transfer.LocalThumState.2
            @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.SourceCallback
            public void onDelivered(int i2) {
                switch (i2) {
                    case 0:
                        transferImage.setImageDrawable(transConfig.getErrorDrawable(LocalThumState.this.transfer.getContext()));
                        return;
                    case 1:
                        if (3 == transferImage.getState()) {
                            transferImage.transformIn(TransferImage.STAGE_SCALE);
                        }
                        transferImage.enable();
                        LocalThumState.this.transfer.bindOnOperationListener(transferImage, i);
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
            }

            @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.SourceCallback
            public void onStart() {
            }
        });
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public TransferImage createTransferIn(int i) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        TransferImage createTransferImage = createTransferImage(transConfig.getOriginImageList().get(i));
        transformThumbnail(transConfig.getSourceImageList().get(i), createTransferImage, true);
        this.transfer.addView(createTransferImage, 1);
        return createTransferImage;
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public void prepareTransfer(TransferImage transferImage, int i) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        transConfig.getImageLoader().showImage(transConfig.getSourceImageList().get(i), transferImage, transConfig.getMissDrawable(this.transfer.getContext()), null);
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public void transferLoad(final int i) {
        final TransferConfig transConfig = this.transfer.getTransConfig();
        final String str = transConfig.getSourceImageList().get(i);
        final TransferImage imageItem = this.transfer.getTransAdapter().getImageItem(i);
        if (transConfig.isJustLoadHitImage()) {
            loadSourceImage(str, imageItem, imageItem.getDrawable(), i);
        } else {
            transConfig.getImageLoader().loadImageAsync(str, new ImageLoader.ThumbnailCallback() { // from class: basecamera.module.functionModule.imagePreview.transfer.LocalThumState.1
                @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.ThumbnailCallback
                public void onFinish(Drawable drawable) {
                    if (drawable == null) {
                        drawable = transConfig.getMissDrawable(LocalThumState.this.transfer.getContext());
                    }
                    LocalThumState.this.loadSourceImage(str, imageItem, drawable, i);
                }
            });
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
        transformThumbnail(transConfig.getSourceImageList().get(i), createTransferImage, false);
        this.transfer.addView(createTransferImage, 1);
        return createTransferImage;
    }
}
