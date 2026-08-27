package basecamera.module.functionModule.imagePreview.transfer;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import basecamera.module.functionModule.imagePreview.loader.ImageLoader;
import basecamera.module.functionModule.imagePreview.view.image.TransferImage;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class TransferState {
    protected TransferLayout transfer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransferState(TransferLayout transferLayout) {
        this.transfer = transferLayout;
    }

    private void loadThumbnail(String str, TransferImage transferImage, boolean z) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        Drawable loadImageSync = transConfig.getImageLoader().loadImageSync(str);
        if (loadImageSync == null) {
            transferImage.setImageDrawable(transConfig.getMissDrawable(this.transfer.getContext()));
        } else {
            transferImage.setImageDrawable(loadImageSync);
        }
        if (z) {
            transferImage.transformIn();
        } else {
            transferImage.transformOut();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NonNull
    public TransferImage createTransferImage(ImageView imageView) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        int[] viewLocation = getViewLocation(imageView);
        TransferImage transferImage = new TransferImage(this.transfer.getContext());
        transferImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        transferImage.setOriginalInfo(viewLocation[0], getTransImageLocalY(viewLocation[1]), imageView.getWidth(), imageView.getHeight());
        transferImage.setBackgroundColor(transConfig.getBackgroundColor());
        transferImage.setDuration(transConfig.getDuration());
        transferImage.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        transferImage.setOnTransferListener(this.transfer.getTransListener());
        return transferImage;
    }

    public abstract TransferImage createTransferIn(int i);

    protected int getStatusBarHeight() {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return this.transfer.getContext().getResources().getDimensionPixelSize(((Integer) cls.getField("status_bar_height").get(cls.newInstance())).intValue());
        } catch (Exception unused) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getTransImageLocalY(int i) {
        return Build.VERSION.SDK_INT > 19 ? i : i - getStatusBarHeight();
    }

    protected int[] getViewLocation(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        return iArr;
    }

    public abstract void prepareTransfer(TransferImage transferImage, int i);

    public abstract void transferLoad(int i);

    public abstract TransferImage transferOut(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public void transformThumbnail(String str, TransferImage transferImage, boolean z) {
        TransferConfig transConfig = this.transfer.getTransConfig();
        ImageLoader imageLoader = transConfig.getImageLoader();
        if (!(this instanceof RemoteThumState)) {
            loadThumbnail(str, transferImage, z);
            return;
        }
        if (imageLoader.isLoaded(str)) {
            loadThumbnail(str, transferImage, z);
            return;
        }
        transferImage.setImageDrawable(transConfig.getMissDrawable(this.transfer.getContext()));
        if (z) {
            transferImage.transformIn();
        } else {
            transferImage.transformOut();
        }
    }
}
