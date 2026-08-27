package basecamera.module.functionModule.imagePreview.view.image;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.amap.location.common.model.Adjacent;

/* loaded from: classes.dex */
public class TransferImage extends PhotoView {
    public static final int CATE_ANIMA_APART = 200;
    public static final int CATE_ANIMA_TOGETHER = 100;
    public static final int STAGE_SCALE = 202;
    public static final int STAGE_TRANSLATE = 201;
    public static final int STATE_TRANS_CLIP = 3;
    public static final int STATE_TRANS_IN = 1;
    public static final int STATE_TRANS_NORMAL = 0;
    public static final int STATE_TRANS_OUT = 2;
    private int cate;
    private long duration;
    private int originalHeight;
    private int originalLocationX;
    private int originalLocationY;
    private int originalWidth;
    private Paint paint;
    private int stage;
    private int state;
    private Matrix transMatrix;
    private Transfrom transform;
    private OnTransferListener transformListener;
    private boolean transformStart;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class LocationSizeF implements Cloneable {
        float height;
        float left;
        float top;
        float width;

        private LocationSizeF() {
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public String toString() {
            return "[left:" + this.left + " top:" + this.top + " width:" + this.width + " height:" + this.height + "]";
        }
    }

    /* loaded from: classes.dex */
    public interface OnTransferListener {
        void onTransferComplete(int i, int i2, int i3);

        void onTransferStart(int i, int i2, int i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Transfrom {
        LocationSizeF endRect;
        float endScale;
        LocationSizeF rect;
        float scale;
        LocationSizeF startRect;
        float startScale;

        private Transfrom() {
        }

        void initStartClip() {
            this.scale = this.startScale;
            try {
                this.rect = (LocationSizeF) this.endRect.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        void initStartIn() {
            this.scale = this.startScale;
            try {
                this.rect = (LocationSizeF) this.startRect.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        void initStartOut() {
            this.scale = this.endScale;
            try {
                this.rect = (LocationSizeF) this.endRect.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    public TransferImage(Context context) {
        this(context, null);
    }

    public TransferImage(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TransferImage(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.state = 0;
        this.cate = 100;
        this.stage = STAGE_TRANSLATE;
        this.duration = 300L;
        this.transformStart = false;
        init();
    }

    private void calcBmpMatrix() {
        if (getDrawable() == null || this.transform == null) {
            return;
        }
        this.transMatrix.setScale(this.transform.scale, this.transform.scale);
        this.transMatrix.postTranslate(-(((this.transform.scale * r0.getIntrinsicWidth()) / 2.0f) - (this.transform.rect.width / 2.0f)), -(((this.transform.scale * r0.getIntrinsicHeight()) / 2.0f) - (this.transform.rect.height / 2.0f)));
    }

    private Rect getClipOriginalInfo(Drawable drawable, int i, int i2, int i3, int i4) {
        Rect rect = new Rect();
        float intrinsicWidth = i / drawable.getIntrinsicWidth();
        float intrinsicHeight = i2 / drawable.getIntrinsicHeight();
        if (intrinsicWidth <= intrinsicHeight) {
            intrinsicWidth = intrinsicHeight;
        }
        float intrinsicWidth2 = drawable.getIntrinsicWidth() * intrinsicWidth;
        float intrinsicHeight2 = drawable.getIntrinsicHeight() * intrinsicWidth;
        rect.left = (int) ((i3 - intrinsicWidth2) / 2.0f);
        rect.top = (int) ((i4 - intrinsicHeight2) / 2.0f);
        rect.right = (int) intrinsicWidth2;
        rect.bottom = (int) intrinsicHeight2;
        return rect;
    }

    private void init() {
        this.transMatrix = new Matrix();
        this.paint = new Paint();
    }

    private void initTransform() {
        if (getDrawable() == null || getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.transform = new Transfrom();
        float intrinsicWidth = this.originalWidth / r0.getIntrinsicWidth();
        float intrinsicHeight = this.originalHeight / r0.getIntrinsicHeight();
        if (intrinsicWidth <= intrinsicHeight) {
            intrinsicWidth = intrinsicHeight;
        }
        this.transform.startScale = intrinsicWidth;
        float width = getWidth() / r0.getIntrinsicWidth();
        float height = getHeight() / r0.getIntrinsicHeight();
        if (width >= height) {
            width = height;
        }
        if (this.cate == 200 && this.stage == 201) {
            this.transform.endScale = intrinsicWidth;
        } else {
            this.transform.endScale = width;
        }
        this.transform.startRect = new LocationSizeF();
        this.transform.startRect.left = this.originalLocationX;
        this.transform.startRect.top = this.originalLocationY;
        this.transform.startRect.width = this.originalWidth;
        this.transform.startRect.height = this.originalHeight;
        this.transform.endRect = new LocationSizeF();
        float intrinsicWidth2 = r0.getIntrinsicWidth() * this.transform.endScale;
        float intrinsicHeight2 = r0.getIntrinsicHeight() * this.transform.endScale;
        this.transform.endRect.left = (getWidth() - intrinsicWidth2) / 2.0f;
        this.transform.endRect.top = (getHeight() - intrinsicHeight2) / 2.0f;
        this.transform.endRect.width = intrinsicWidth2;
        this.transform.endRect.height = intrinsicHeight2;
        this.transform.rect = new LocationSizeF();
    }

    private void startApartTrans() {
        if (this.transform == null) {
            return;
        }
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(this.duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (this.stage == 201) {
            valueAnimator.setValues(PropertyValuesHolder.ofFloat(Adjacent.LEFT, this.transform.startRect.left, this.transform.endRect.left), PropertyValuesHolder.ofFloat(Adjacent.TOP, this.transform.startRect.top, this.transform.endRect.top), PropertyValuesHolder.ofFloat("width", this.transform.startRect.width, this.transform.endRect.width), PropertyValuesHolder.ofFloat("height", this.transform.startRect.height, this.transform.endRect.height));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.TransferImage.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public synchronized void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    TransferImage.this.paint.setAlpha((int) (valueAnimator2.getAnimatedFraction() * 255.0f));
                    TransferImage.this.transform.rect.left = ((Float) valueAnimator2.getAnimatedValue(Adjacent.LEFT)).floatValue();
                    TransferImage.this.transform.rect.top = ((Float) valueAnimator2.getAnimatedValue(Adjacent.TOP)).floatValue();
                    TransferImage.this.transform.rect.width = ((Float) valueAnimator2.getAnimatedValue("width")).floatValue();
                    TransferImage.this.transform.rect.height = ((Float) valueAnimator2.getAnimatedValue("height")).floatValue();
                    TransferImage.this.invalidate();
                }
            });
        } else {
            valueAnimator.setValues(PropertyValuesHolder.ofFloat("scale", this.transform.startScale, this.transform.endScale), PropertyValuesHolder.ofFloat(Adjacent.LEFT, this.transform.startRect.left, this.transform.endRect.left), PropertyValuesHolder.ofFloat(Adjacent.TOP, this.transform.startRect.top, this.transform.endRect.top), PropertyValuesHolder.ofFloat("width", this.transform.startRect.width, this.transform.endRect.width), PropertyValuesHolder.ofFloat("height", this.transform.startRect.height, this.transform.endRect.height));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.TransferImage.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public synchronized void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    TransferImage.this.transform.rect.left = ((Float) valueAnimator2.getAnimatedValue(Adjacent.LEFT)).floatValue();
                    TransferImage.this.transform.rect.top = ((Float) valueAnimator2.getAnimatedValue(Adjacent.TOP)).floatValue();
                    TransferImage.this.transform.rect.width = ((Float) valueAnimator2.getAnimatedValue("width")).floatValue();
                    TransferImage.this.transform.rect.height = ((Float) valueAnimator2.getAnimatedValue("height")).floatValue();
                    TransferImage.this.transform.scale = ((Float) valueAnimator2.getAnimatedValue("scale")).floatValue();
                    TransferImage.this.invalidate();
                }
            });
        }
        valueAnimator.addListener(new AnimatorListenerAdapter() { // from class: basecamera.module.functionModule.imagePreview.view.image.TransferImage.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (TransferImage.this.stage == 201) {
                    TransferImage.this.originalLocationX = (int) TransferImage.this.transform.endRect.left;
                    TransferImage.this.originalLocationY = (int) TransferImage.this.transform.endRect.top;
                    TransferImage.this.originalWidth = (int) TransferImage.this.transform.endRect.width;
                    TransferImage.this.originalHeight = (int) TransferImage.this.transform.endRect.height;
                }
                if (TransferImage.this.state == 1 && TransferImage.this.stage == 202) {
                    TransferImage.this.state = 0;
                }
                if (TransferImage.this.transformListener != null) {
                    TransferImage.this.transformListener.onTransferComplete(TransferImage.this.state, TransferImage.this.cate, TransferImage.this.stage);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                if (TransferImage.this.transformListener != null) {
                    TransferImage.this.transformListener.onTransferStart(TransferImage.this.state, TransferImage.this.cate, TransferImage.this.stage);
                }
            }
        });
        if (this.state == 1) {
            valueAnimator.start();
        } else {
            valueAnimator.reverse();
        }
    }

    private void startTogetherTrans() {
        if (this.transform == null) {
            return;
        }
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(this.duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setValues(PropertyValuesHolder.ofFloat("scale", this.transform.startScale, this.transform.endScale), PropertyValuesHolder.ofFloat(Adjacent.LEFT, this.transform.startRect.left, this.transform.endRect.left), PropertyValuesHolder.ofFloat(Adjacent.TOP, this.transform.startRect.top, this.transform.endRect.top), PropertyValuesHolder.ofFloat("width", this.transform.startRect.width, this.transform.endRect.width), PropertyValuesHolder.ofFloat("height", this.transform.startRect.height, this.transform.endRect.height));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.TransferImage.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public synchronized void onAnimationUpdate(ValueAnimator valueAnimator2) {
                TransferImage.this.paint.setAlpha((int) (valueAnimator2.getAnimatedFraction() * 255.0f));
                TransferImage.this.transform.scale = ((Float) valueAnimator2.getAnimatedValue("scale")).floatValue();
                TransferImage.this.transform.rect.left = ((Float) valueAnimator2.getAnimatedValue(Adjacent.LEFT)).floatValue();
                TransferImage.this.transform.rect.top = ((Float) valueAnimator2.getAnimatedValue(Adjacent.TOP)).floatValue();
                TransferImage.this.transform.rect.width = ((Float) valueAnimator2.getAnimatedValue("width")).floatValue();
                TransferImage.this.transform.rect.height = ((Float) valueAnimator2.getAnimatedValue("height")).floatValue();
                TransferImage.this.invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() { // from class: basecamera.module.functionModule.imagePreview.view.image.TransferImage.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (TransferImage.this.transformListener != null) {
                    TransferImage.this.transformListener.onTransferComplete(TransferImage.this.state, TransferImage.this.cate, TransferImage.this.stage);
                }
                if (TransferImage.this.state == 1) {
                    TransferImage.this.state = 0;
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                if (TransferImage.this.transformListener != null) {
                    TransferImage.this.transformListener.onTransferStart(TransferImage.this.state, TransferImage.this.cate, TransferImage.this.stage);
                }
            }
        });
        if (this.state == 1) {
            valueAnimator.start();
        } else {
            valueAnimator.reverse();
        }
    }

    public long getDuration() {
        return this.duration;
    }

    public int getState() {
        return this.state;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        if (this.state == 0) {
            this.paint.setAlpha(255);
            canvas.drawPaint(this.paint);
            super.onDraw(canvas);
            return;
        }
        if (this.transformStart) {
            initTransform();
        }
        if (this.transform == null) {
            super.onDraw(canvas);
            return;
        }
        if (this.transformStart) {
            switch (this.state) {
                case 1:
                    this.transform.initStartIn();
                    break;
                case 2:
                    this.transform.initStartOut();
                    break;
                case 3:
                    this.paint.setAlpha(255);
                    this.transform.initStartClip();
                    break;
            }
        }
        canvas.drawPaint(this.paint);
        int saveCount = canvas.getSaveCount();
        canvas.save();
        calcBmpMatrix();
        canvas.translate(this.transform.rect.left, this.transform.rect.top);
        canvas.clipRect(0.0f, 0.0f, this.transform.rect.width, this.transform.rect.height);
        canvas.concat(this.transMatrix);
        getDrawable().draw(canvas);
        canvas.restoreToCount(saveCount);
        if (!this.transformStart || this.state == 3) {
            return;
        }
        this.transformStart = false;
        int i = this.cate;
        if (i == 100) {
            startTogetherTrans();
        } else {
            if (i != 200) {
                return;
            }
            startApartTrans();
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        this.paint.setColor(i);
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public void setOnTransferListener(OnTransferListener onTransferListener) {
        this.transformListener = onTransferListener;
    }

    public void setOriginalInfo(int i, int i2, int i3, int i4) {
        this.originalLocationX = i;
        this.originalLocationY = i2;
        this.originalWidth = i3;
        this.originalHeight = i4;
    }

    public void setOriginalInfo(Drawable drawable, int i, int i2, int i3, int i4) {
        Rect clipOriginalInfo = getClipOriginalInfo(drawable, i, i2, i3, i4);
        this.originalLocationX = clipOriginalInfo.left;
        this.originalLocationY = clipOriginalInfo.top;
        this.originalWidth = clipOriginalInfo.right;
        this.originalHeight = clipOriginalInfo.bottom;
    }

    public void setState(int i) {
        this.state = i;
    }

    public void transClip() {
        this.state = 3;
        this.transformStart = true;
    }

    public void transformIn() {
        this.cate = 100;
        this.state = 1;
        this.transformStart = true;
        this.paint.setAlpha(0);
        invalidate();
    }

    public void transformIn(int i) {
        this.cate = 200;
        this.state = 1;
        this.stage = i;
        this.transformStart = true;
        if (this.stage == 201) {
            this.paint.setAlpha(0);
        } else {
            this.paint.setAlpha(255);
        }
        invalidate();
    }

    public void transformOut() {
        this.cate = 100;
        this.state = 2;
        this.transformStart = true;
        this.paint.setAlpha(255);
        invalidate();
    }

    public void transformOut(int i) {
        this.cate = 200;
        this.state = 2;
        this.stage = i;
        this.transformStart = true;
        this.paint.setAlpha(255);
        invalidate();
    }
}
