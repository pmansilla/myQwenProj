package basecamera.module.functionModule.imagePreview.transfer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import basecamera.module.functionModule.imagePreview.view.image.TransferImage;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TransferAdapter extends PagerAdapter {
    private SparseArray<FrameLayout> containLayoutArray;
    private int imageSize;
    private OnInstantiateItemListener onInstantListener;
    private int showIndex;
    private TransferLayout transfer;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnInstantiateItemListener {
        void onComplete();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransferAdapter(TransferLayout transferLayout, int i, int i2) {
        this.transfer = transferLayout;
        this.imageSize = i;
        int i3 = i2 + 1;
        this.showIndex = i3 == i ? i2 - 1 : i3;
        this.showIndex = this.showIndex < 0 ? 0 : this.showIndex;
        this.containLayoutArray = new SparseArray<>();
    }

    @NonNull
    private FrameLayout newParentLayout(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        TransferConfig transConfig = this.transfer.getTransConfig();
        TransferImage transferImage = new TransferImage(context);
        transferImage.setDuration(transConfig.getDuration());
        transferImage.setBackgroundColor(transConfig.getBackgroundColor());
        transferImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        transferImage.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        frameLayout.addView(transferImage);
        if (transConfig.isJustLoadHitImage()) {
            this.transfer.getTransferState(i).prepareTransfer(transferImage, i);
        }
        return frameLayout;
    }

    @Override // android.support.v4.view.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return this.imageSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransferImage getImageItem(int i) {
        FrameLayout frameLayout = this.containLayoutArray.get(i);
        if (frameLayout != null) {
            int childCount = frameLayout.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = frameLayout.getChildAt(i2);
                if (childAt instanceof ImageView) {
                    return (TransferImage) childAt;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FrameLayout getParentItem(int i) {
        return this.containLayoutArray.get(i);
    }

    @Override // android.support.v4.view.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        FrameLayout frameLayout = this.containLayoutArray.get(i);
        if (frameLayout == null) {
            frameLayout = newParentLayout(viewGroup, i);
            this.containLayoutArray.put(i, frameLayout);
            if (i == this.showIndex && this.onInstantListener != null) {
                this.onInstantListener.onComplete();
            }
        }
        viewGroup.addView(frameLayout);
        return frameLayout;
    }

    @Override // android.support.v4.view.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnInstantListener(OnInstantiateItemListener onInstantiateItemListener) {
        this.onInstantListener = onInstantiateItemListener;
    }
}
