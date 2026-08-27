package basecamera.module.lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import basecamera.module.lib.listener.CaptureListener;
import basecamera.module.lib.listener.ClickListener;
import basecamera.module.lib.listener.ReturnListener;
import basecamera.module.lib.listener.TypeListener;
import basecamera.module.utils.DisplayHelperUtils;

/* loaded from: classes.dex */
public class CaptureLayout extends FrameLayout {
    private TypeButton btn_cancel;
    private CaptureButton btn_capture;
    private TypeButton btn_confirm;
    private ReturnButton btn_return;
    private int buttonWH;
    private int button_size;
    private CaptureListener captureLisenter;
    private int iconLeft;
    private int iconRight;
    private boolean isFirst;
    private ImageView iv_custom_left;
    private ImageView iv_custom_right;
    private int layout_height;
    private int layout_width;
    private ClickListener leftClickListener;
    private ReturnListener returnListener;
    private ClickListener rightClickListener;
    private TextView txt_tip;
    private TypeListener typeLisenter;

    public CaptureLayout(Context context) {
        this(context, null);
    }

    public CaptureLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CaptureLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.iconLeft = 0;
        this.iconRight = 0;
        this.buttonWH = 0;
        this.isFirst = true;
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        if (getResources().getConfiguration().orientation == 1) {
            this.layout_width = displayMetrics.widthPixels;
        } else {
            this.layout_width = displayMetrics.widthPixels / 2;
        }
        this.button_size = (int) (this.layout_width / 4.5f);
        this.layout_height = this.button_size + ((this.button_size / 5) * 2) + 100;
        this.buttonWH = DisplayHelperUtils.dp2px(getContext(), 44);
        initView();
        initEvent();
    }

    private void initView() {
        setWillNotDraw(false);
        this.btn_capture = new CaptureButton(getContext(), this.button_size);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        this.btn_capture.setLayoutParams(layoutParams);
        this.btn_capture.setCaptureLisenter(new CaptureListener() { // from class: basecamera.module.lib.CaptureLayout.2
            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordEnd(long j) {
                if (CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordEnd(j);
                }
                CaptureLayout.this.startAlphaAnimation();
                CaptureLayout.this.startTypeBtnAnimator();
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordError() {
                if (CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordError();
                }
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordShort(long j) {
                if (CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordShort(j);
                }
                CaptureLayout.this.startAlphaAnimation();
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordStart() {
                if (CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordStart();
                }
                CaptureLayout.this.startAlphaAnimation();
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordZoom(float f) {
                if (CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordZoom(f);
                }
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void takePictures() {
                if (CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.takePictures();
                }
            }
        });
        this.btn_cancel = new TypeButton(getContext(), 1, this.button_size);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
        layoutParams2.gravity = 16;
        layoutParams2.setMargins((this.layout_width / 4) - (this.button_size / 2), 0, 0, 0);
        this.btn_cancel.setLayoutParams(layoutParams2);
        this.btn_cancel.setOnClickListener(new View.OnClickListener() { // from class: basecamera.module.lib.CaptureLayout.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CaptureLayout.this.typeLisenter != null) {
                    CaptureLayout.this.typeLisenter.cancel();
                }
                CaptureLayout.this.startAlphaAnimation();
            }
        });
        this.btn_confirm = new TypeButton(getContext(), 2, this.button_size);
        FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(-1, -1);
        layoutParams3.gravity = 21;
        layoutParams3.setMargins(0, 0, (this.layout_width / 4) - (this.button_size / 2), 0);
        this.btn_confirm.setLayoutParams(layoutParams3);
        this.btn_confirm.setOnClickListener(new View.OnClickListener() { // from class: basecamera.module.lib.CaptureLayout.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CaptureLayout.this.typeLisenter != null) {
                    CaptureLayout.this.typeLisenter.confirm();
                }
                CaptureLayout.this.startAlphaAnimation();
            }
        });
        this.btn_return = new ReturnButton(getContext(), this.buttonWH);
        FrameLayout.LayoutParams layoutParams4 = new FrameLayout.LayoutParams(-2, -2);
        layoutParams4.gravity = 16;
        layoutParams4.setMargins(this.layout_width / 7, 0, 0, 0);
        this.btn_return.setLayoutParams(layoutParams4);
        this.btn_return.setOnClickListener(new View.OnClickListener() { // from class: basecamera.module.lib.CaptureLayout.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CaptureLayout.this.leftClickListener != null) {
                    CaptureLayout.this.leftClickListener.onClick();
                }
            }
        });
        this.iv_custom_left = new ImageView(getContext());
        FrameLayout.LayoutParams layoutParams5 = new FrameLayout.LayoutParams(this.buttonWH, this.buttonWH);
        layoutParams5.gravity = 16;
        layoutParams5.setMargins(this.layout_width / 7, 0, 0, 0);
        this.iv_custom_left.setLayoutParams(layoutParams5);
        this.iv_custom_left.setOnClickListener(new View.OnClickListener() { // from class: basecamera.module.lib.CaptureLayout.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CaptureLayout.this.leftClickListener != null) {
                    CaptureLayout.this.leftClickListener.onClick();
                }
            }
        });
        this.iv_custom_right = new ImageView(getContext());
        FrameLayout.LayoutParams layoutParams6 = new FrameLayout.LayoutParams(this.buttonWH, this.buttonWH);
        layoutParams6.gravity = 21;
        layoutParams6.setMargins(0, 0, this.layout_width / 8, 0);
        this.iv_custom_right.setLayoutParams(layoutParams6);
        this.iv_custom_right.setOnClickListener(new View.OnClickListener() { // from class: basecamera.module.lib.CaptureLayout.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CaptureLayout.this.rightClickListener != null) {
                    CaptureLayout.this.rightClickListener.onClick();
                }
            }
        });
        this.txt_tip = new TextView(getContext());
        FrameLayout.LayoutParams layoutParams7 = new FrameLayout.LayoutParams(-1, -2);
        layoutParams7.gravity = 1;
        layoutParams7.setMargins(0, 0, 0, 0);
        this.txt_tip.setText("轻触拍照，长按摄像");
        this.txt_tip.setTextColor(-1);
        this.txt_tip.setGravity(17);
        this.txt_tip.setLayoutParams(layoutParams7);
        addView(this.btn_capture);
        addView(this.btn_cancel);
        addView(this.btn_confirm);
        addView(this.btn_return);
        addView(this.iv_custom_left);
        addView(this.iv_custom_right);
        addView(this.txt_tip);
    }

    public void initEvent() {
        this.iv_custom_right.setVisibility(8);
        this.btn_cancel.setVisibility(8);
        this.btn_confirm.setVisibility(8);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(this.layout_width, this.layout_height);
    }

    public void resetCaptureLayout() {
        this.btn_capture.resetState();
        this.btn_cancel.setVisibility(8);
        this.btn_confirm.setVisibility(8);
        this.btn_capture.setVisibility(0);
        if (this.iconLeft != 0) {
            this.iv_custom_left.setVisibility(0);
        } else {
            this.btn_return.setVisibility(0);
        }
        if (this.iconRight != 0) {
            this.iv_custom_right.setVisibility(0);
        }
    }

    public void setButtonFeatures(int i) {
        this.btn_capture.setButtonFeatures(i);
    }

    public void setCaptureLisenter(CaptureListener captureListener) {
        this.captureLisenter = captureListener;
    }

    public void setDuration(int i) {
        this.btn_capture.setDuration(i);
    }

    public void setIconSrc(int i, int i2) {
        this.iconLeft = i;
        this.iconRight = i2;
        if (this.iconLeft != 0) {
            this.iv_custom_left.setImageResource(i);
            this.iv_custom_left.setVisibility(0);
            this.btn_return.setVisibility(8);
        } else {
            this.iv_custom_left.setVisibility(8);
            this.btn_return.setVisibility(0);
        }
        if (this.iconRight == 0) {
            this.iv_custom_right.setVisibility(8);
        } else {
            this.iv_custom_right.setImageResource(i2);
            this.iv_custom_right.setVisibility(0);
        }
    }

    public void setLeftClickListener(ClickListener clickListener) {
        this.leftClickListener = clickListener;
    }

    public void setReturnLisenter(ReturnListener returnListener) {
        this.returnListener = returnListener;
    }

    public void setRightClickListener(ClickListener clickListener) {
        this.rightClickListener = clickListener;
    }

    public void setTextWithAnimation(String str) {
        this.txt_tip.setText(str);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.txt_tip, "alpha", 0.0f, 1.0f, 1.0f, 0.0f);
        ofFloat.setDuration(2500L);
        ofFloat.start();
    }

    public void setTip(String str) {
        this.txt_tip.setText(str);
    }

    public void setTypeLisenter(TypeListener typeListener) {
        this.typeLisenter = typeListener;
    }

    public void showTip() {
        this.txt_tip.setVisibility(0);
    }

    public void startAlphaAnimation() {
        if (this.isFirst) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.txt_tip, "alpha", 1.0f, 0.0f);
            ofFloat.setDuration(500L);
            ofFloat.start();
            this.isFirst = false;
        }
    }

    public void startTypeBtnAnimator() {
        if (this.iconLeft != 0) {
            this.iv_custom_left.setVisibility(8);
        } else {
            this.btn_return.setVisibility(8);
        }
        if (this.iconRight != 0) {
            this.iv_custom_right.setVisibility(8);
        }
        this.btn_capture.setVisibility(8);
        this.btn_cancel.setVisibility(0);
        this.btn_confirm.setVisibility(0);
        this.btn_cancel.setClickable(false);
        this.btn_confirm.setClickable(false);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.btn_cancel, "translationX", this.layout_width / 4, 0.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.btn_confirm, "translationX", (-this.layout_width) / 4, 0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: basecamera.module.lib.CaptureLayout.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                CaptureLayout.this.btn_cancel.setClickable(true);
                CaptureLayout.this.btn_confirm.setClickable(true);
            }
        });
        animatorSet.setDuration(200L);
        animatorSet.start();
    }
}
