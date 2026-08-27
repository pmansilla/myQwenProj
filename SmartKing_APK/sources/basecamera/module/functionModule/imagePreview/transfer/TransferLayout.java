package basecamera.module.functionModule.imagePreview.transfer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import basecamera.module.functionModule.imagePreview.style.IIndexIndicator;
import basecamera.module.functionModule.imagePreview.transfer.TransferAdapter;
import basecamera.module.functionModule.imagePreview.view.image.TransferImage;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TransferLayout extends FrameLayout {
    private Context context;
    private TransferAdapter.OnInstantiateItemListener instantListener;
    private OnLayoutResetListener layoutResetListener;
    private Set<Integer> loadedIndexSet;
    private TransferAdapter transAdapter;
    private ViewPager.OnPageChangeListener transChangeListener;
    private TransferConfig transConfig;
    private TransferImage transImage;
    private TransferImage.OnTransferListener transListener;
    private ViewPager transViewPager;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnLayoutResetListener {
        void onReset();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransferLayout(Context context) {
        super(context);
        this.transChangeListener = new ViewPager.SimpleOnPageChangeListener() { // from class: basecamera.module.functionModule.imagePreview.transfer.TransferLayout.1
            @Override // android.support.v4.view.ViewPager.SimpleOnPageChangeListener, android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                TransferLayout.this.transConfig.setNowThumbnailIndex(i);
                if (TransferLayout.this.transConfig.isJustLoadHitImage()) {
                    TransferLayout.this.loadSourceImageOffset(i, 0);
                    return;
                }
                for (int i2 = 1; i2 <= TransferLayout.this.transConfig.getOffscreenPageLimit(); i2++) {
                    TransferLayout.this.loadSourceImageOffset(i, i2);
                }
            }
        };
        this.instantListener = new TransferAdapter.OnInstantiateItemListener() { // from class: basecamera.module.functionModule.imagePreview.transfer.TransferLayout.2
            @Override // basecamera.module.functionModule.imagePreview.transfer.TransferAdapter.OnInstantiateItemListener
            public void onComplete() {
                TransferLayout.this.transViewPager.addOnPageChangeListener(TransferLayout.this.transChangeListener);
                int nowThumbnailIndex = TransferLayout.this.transConfig.getNowThumbnailIndex();
                if (TransferLayout.this.transConfig.isJustLoadHitImage()) {
                    TransferLayout.this.loadSourceImageOffset(nowThumbnailIndex, 0);
                } else {
                    TransferLayout.this.loadSourceImageOffset(nowThumbnailIndex, 1);
                }
            }
        };
        this.transListener = new TransferImage.OnTransferListener() { // from class: basecamera.module.functionModule.imagePreview.transfer.TransferLayout.3
            @Override // basecamera.module.functionModule.imagePreview.view.image.TransferImage.OnTransferListener
            public void onTransferComplete(int i, int i2, int i3) {
                if (i2 == 100) {
                    switch (i) {
                        case 1:
                            TransferLayout.this.addIndexIndicator();
                            TransferLayout.this.transViewPager.setVisibility(0);
                            TransferLayout.this.removeFromParent(TransferLayout.this.transImage);
                            TransferLayout.this.setBackgroundColor(TransferLayout.this.transConfig.getBackgroundColor());
                            return;
                        case 2:
                            TransferLayout.this.resetTransfer();
                            TransferLayout.this.setBackgroundColor(0);
                            return;
                        default:
                            return;
                    }
                }
                switch (i) {
                    case 1:
                        if (i3 == 201) {
                            TransferLayout.this.addIndexIndicator();
                            TransferLayout.this.transViewPager.setVisibility(0);
                            TransferLayout.this.removeFromParent(TransferLayout.this.transImage);
                            TransferLayout.this.setBackgroundColor(TransferLayout.this.transConfig.getBackgroundColor());
                            return;
                        }
                        return;
                    case 2:
                        if (i3 == 201) {
                            TransferLayout.this.resetTransfer();
                            TransferLayout.this.setBackgroundColor(0);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }

            @Override // basecamera.module.functionModule.imagePreview.view.image.TransferImage.OnTransferListener
            public void onTransferStart(int i, int i2, int i3) {
                if (i == 2) {
                    TransferLayout.this.setBackgroundColor(0);
                }
            }
        };
        this.context = context;
        this.loadedIndexSet = new HashSet();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addIndexIndicator() {
        IIndexIndicator indexIndicator = this.transConfig.getIndexIndicator();
        if (indexIndicator == null || this.transConfig.getSourceImageList().size() < 2) {
            return;
        }
        indexIndicator.attach(this);
        indexIndicator.onShow(this.transViewPager);
    }

    private void createTransferViewPager() {
        this.transAdapter = new TransferAdapter(this, this.transConfig.getSourceImageList().size(), this.transConfig.getNowThumbnailIndex());
        this.transAdapter.setOnInstantListener(this.instantListener);
        this.transViewPager = new ViewPager(this.context);
        this.transViewPager.setVisibility(4);
        this.transViewPager.setOffscreenPageLimit(this.transConfig.getOffscreenPageLimit() + 1);
        this.transViewPager.setAdapter(this.transAdapter);
        this.transViewPager.setCurrentItem(this.transConfig.getNowThumbnailIndex());
        addView(this.transViewPager, new FrameLayout.LayoutParams(-1, -1));
    }

    private void diffusionTransfer(int i) {
        this.transImage = this.transAdapter.getImageItem(i);
        this.transImage.setState(2);
        this.transImage.disenable();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(this.transConfig.getDuration());
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setValues(PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f), PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.2f));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: basecamera.module.functionModule.imagePreview.transfer.TransferLayout.6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                float floatValue = ((Float) valueAnimator2.getAnimatedValue("alpha")).floatValue();
                float floatValue2 = ((Float) valueAnimator2.getAnimatedValue("scaleX")).floatValue();
                TransferLayout.this.transImage.setAlpha(floatValue);
                TransferLayout.this.transImage.setScaleX(floatValue2);
                TransferLayout.this.transImage.setScaleY(floatValue2);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() { // from class: basecamera.module.functionModule.imagePreview.transfer.TransferLayout.7
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                TransferLayout.this.resetTransfer();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                TransferLayout.this.setBackgroundColor(0);
            }
        });
        valueAnimator.start();
    }

    private void hideIndexIndicator() {
        IIndexIndicator indexIndicator = this.transConfig.getIndexIndicator();
        if (indexIndicator == null || this.transConfig.getSourceImageList().size() < 2) {
            return;
        }
        indexIndicator.onHide();
    }

    private void loadSourceImage(int i) {
        getTransferState(i).transferLoad(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSourceImageOffset(int i, int i2) {
        int i3 = i - i2;
        int i4 = i2 + i;
        if (!this.loadedIndexSet.contains(Integer.valueOf(i))) {
            loadSourceImage(i);
            this.loadedIndexSet.add(Integer.valueOf(i));
        }
        if (i3 >= 0 && !this.loadedIndexSet.contains(Integer.valueOf(i3))) {
            loadSourceImage(i3);
            this.loadedIndexSet.add(Integer.valueOf(i3));
        }
        if (i4 >= this.transConfig.getSourceImageList().size() || this.loadedIndexSet.contains(Integer.valueOf(i4))) {
            return;
        }
        loadSourceImage(i4);
        this.loadedIndexSet.add(Integer.valueOf(i4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromParent(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
    }

    private void removeIndexIndicator() {
        IIndexIndicator indexIndicator = this.transConfig.getIndexIndicator();
        if (indexIndicator == null || this.transConfig.getSourceImageList().size() < 2) {
            return;
        }
        indexIndicator.onRemove();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetTransfer() {
        this.loadedIndexSet.clear();
        removeIndexIndicator();
        removeAllViews();
        this.layoutResetListener.onReset();
    }

    public void apply(TransferConfig transferConfig) {
        this.transConfig = transferConfig;
    }

    public void bindOnOperationListener(final ImageView imageView, final int i) {
        imageView.setOnClickListener(new View.OnClickListener() { // from class: basecamera.module.functionModule.imagePreview.transfer.TransferLayout.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TransferLayout.this.dismiss(i);
            }
        });
        if (this.transConfig.getLongClickListener() != null) {
            imageView.setOnLongClickListener(new View.OnLongClickListener() { // from class: basecamera.module.functionModule.imagePreview.transfer.TransferLayout.5
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    TransferLayout.this.transConfig.getLongClickListener().onLongClick(imageView, i);
                    return false;
                }
            });
        }
    }

    public void dismiss(int i) {
        if (this.transImage == null || this.transImage.getState() != 2) {
            this.transImage = getTransferState(i).transferOut(i);
            if (this.transImage == null) {
                diffusionTransfer(i);
            } else {
                this.transViewPager.setVisibility(4);
            }
            hideIndexIndicator();
        }
    }

    public TransferAdapter getTransAdapter() {
        return this.transAdapter;
    }

    public TransferConfig getTransConfig() {
        return this.transConfig;
    }

    public TransferImage.OnTransferListener getTransListener() {
        return this.transListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransferState getTransferState(int i) {
        if (this.transConfig.isThumbnailEmpty()) {
            return this.transConfig.getImageLoader().isLoaded(this.transConfig.getSourceImageList().get(i)) ? new LocalThumState(this) : new EmptyThumState(this);
        }
        return new RemoteThumState(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.transViewPager.removeOnPageChangeListener(this.transChangeListener);
    }

    public void setOnLayoutResetListener(OnLayoutResetListener onLayoutResetListener) {
        this.layoutResetListener = onLayoutResetListener;
    }

    public void show() {
        createTransferViewPager();
        int nowThumbnailIndex = this.transConfig.getNowThumbnailIndex();
        this.transImage = getTransferState(nowThumbnailIndex).createTransferIn(nowThumbnailIndex);
    }
}
