package basecamera.module.functionModule.imagePreview.transfer;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import basecamera.module.functionModule.imagePreview.loader.ImageLoader;
import basecamera.module.functionModule.imagePreview.style.IProgressIndicator;
import basecamera.module.functionModule.imagePreview.view.image.TransferImage;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class EmptyThumState extends TransferState {
    /* JADX INFO: Access modifiers changed from: package-private */
    public EmptyThumState(TransferLayout transferLayout) {
        super(transferLayout);
    }

    private Drawable clipAndGetPlachHolder(TransferImage transferImage, int i) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        Drawable placeHolder = getPlaceHolder(i);
        int[] iArr = new int[2];
        ImageView imageView = transConfig.getOriginImageList().get(i);
        if (imageView != null) {
            iArr[0] = imageView.getWidth();
            iArr[1] = imageView.getHeight();
        }
        clipTargetImage(transferImage, placeHolder, iArr);
        return placeHolder;
    }

    private void clipTargetImage(TransferImage transferImage, Drawable drawable, int[] iArr) {
        DisplayMetrics displayMetrics = this.transfer.getContext().getResources().getDisplayMetrics();
        transferImage.setOriginalInfo(drawable, iArr[0], iArr[1], displayMetrics.widthPixels, getTransImageLocalY(displayMetrics.heightPixels));
        transferImage.transClip();
    }

    private Drawable getPlaceHolder(int i) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        ImageView imageView = transConfig.getOriginImageList().get(i);
        return imageView != null ? imageView.getDrawable() : transConfig.getMissDrawable(this.transfer.getContext());
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public TransferImage createTransferIn(int i) {
        ImageView imageView = this.transfer.getTransConfig().getOriginImageList().get(i);
        TransferImage createTransferImage = createTransferImage(imageView);
        createTransferImage.setImageDrawable(imageView.getDrawable());
        createTransferImage.transformIn(TransferImage.STAGE_TRANSLATE);
        this.transfer.addView(createTransferImage, 1);
        return createTransferImage;
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public void prepareTransfer(TransferImage transferImage, int i) {
        transferImage.setImageDrawable(clipAndGetPlachHolder(transferImage, i));
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public void transferLoad(final int i) {
        TransferAdapter transAdapter = this.transfer.getTransAdapter();
        final TransferConfig transConfig = this.transfer.getTransConfig();
        String str = transConfig.getSourceImageList().get(i);
        final TransferImage imageItem = transAdapter.getImageItem(i);
        Drawable placeHolder = transConfig.isJustLoadHitImage() ? getPlaceHolder(i) : clipAndGetPlachHolder(imageItem, i);
        final IProgressIndicator progressIndicator = transConfig.getProgressIndicator();
        progressIndicator.attach(i, transAdapter.getParentItem(i));
        transConfig.getImageLoader().showImage(str, imageItem, placeHolder, new ImageLoader.SourceCallback() { // from class: basecamera.module.functionModule.imagePreview.transfer.EmptyThumState.1
            @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.SourceCallback
            public void onDelivered(int i2) {
                switch (i2) {
                    case 0:
                        imageItem.setImageDrawable(transConfig.getErrorDrawable(EmptyThumState.this.transfer.getContext()));
                        return;
                    case 1:
                        progressIndicator.onFinish(i);
                        imageItem.transformIn(TransferImage.STAGE_SCALE);
                        imageItem.enable();
                        EmptyThumState.this.transfer.bindOnOperationListener(imageItem, i);
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
                progressIndicator.onProgress(i, i2);
            }

            @Override // basecamera.module.functionModule.imagePreview.loader.ImageLoader.SourceCallback
            public void onStart() {
                progressIndicator.onStart(i);
            }
        });
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferState
    public TransferImage transferOut(int i) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        List<ImageView> originImageList = transConfig.getOriginImageList();
        if (i > originImageList.size() - 1 || originImageList.get(i) == null) {
            return null;
        }
        TransferImage createTransferImage = createTransferImage(originImageList.get(i));
        createTransferImage.setImageDrawable(this.transfer.getTransAdapter().getImageItem(transConfig.getNowThumbnailIndex()).getDrawable());
        createTransferImage.transformOut(TransferImage.STAGE_TRANSLATE);
        this.transfer.addView(createTransferImage, 1);
        return createTransferImage;
    }
}
