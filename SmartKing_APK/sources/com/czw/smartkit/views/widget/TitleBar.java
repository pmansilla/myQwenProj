package com.czw.smartkit.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.czw.smartkit.R;
import com.czw.utils.LogUtil;

/* loaded from: classes.dex */
public class TitleBar extends RelativeLayout {
    private TextView centerText;
    private View layoutView;
    private ImageView leftIcon;
    private TextView leftText;
    private ImageView rightIcon;
    private TextView rightText;
    private TitleClick titleClick;

    /* loaded from: classes.dex */
    public interface LeftClick {
        void onLeftClick(View view);
    }

    /* loaded from: classes.dex */
    public static abstract class TitleClick implements LeftClick {
        public abstract void onRightClick(View view);
    }

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TitleBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.layoutView = null;
        this.titleClick = null;
        _init_(context);
    }

    protected <T extends View> T $View(int i) {
        return (T) this.layoutView.findViewById(i);
    }

    protected String $str(int i) {
        return getContext().getString(i);
    }

    void _init_(Context context) {
        this.layoutView = LayoutInflater.from(context).inflate(R.layout.title_bar, (ViewGroup) this, true);
        this.leftIcon = (ImageView) $View(R.id.leftIcon);
        this.rightIcon = (ImageView) $View(R.id.rightIcon);
        this.leftText = (TextView) $View(R.id.leftText);
        this.rightText = (TextView) $View(R.id.rightText);
        this.centerText = (TextView) $View(R.id.centerText);
        this.centerText.setText("");
        this.layoutView.findViewById(R.id.leftLayout).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.views.widget.TitleBar.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LogUtil.e("debug===>左边");
                if (TitleBar.this.titleClick != null) {
                    TitleBar.this.titleClick.onLeftClick(view);
                }
            }
        });
        this.layoutView.findViewById(R.id.rightLayout).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.views.widget.TitleBar.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LogUtil.e("debug===>右边");
                if (TitleBar.this.titleClick != null) {
                    TitleBar.this.titleClick.onRightClick(view);
                }
            }
        });
    }

    public void disableLeftImage() {
        this.leftIcon.setVisibility(8);
    }

    public void disableRight(boolean z, boolean z2) {
        this.rightText.setVisibility(z ? 8 : 0);
        this.rightIcon.setVisibility(z2 ? 8 : 0);
    }

    public void initTitleIcon(int i, int i2) {
        if (i != 0) {
            $View(R.id.leftLayout).setVisibility(0);
            this.leftIcon.setImageResource(i);
            this.leftIcon.setVisibility(0);
        } else {
            this.leftIcon.setVisibility(8);
        }
        if (i2 == 0) {
            this.rightIcon.setVisibility(8);
            return;
        }
        $View(R.id.rightLayout).setVisibility(0);
        this.rightIcon.setVisibility(0);
        this.rightIcon.setImageResource(i2);
    }

    public void isShwoTitle(boolean z) {
        this.layoutView.setVisibility(z ? 0 : 8);
    }

    public void setClick(TitleClick titleClick) {
        this.titleClick = titleClick;
    }

    public void setIcon(int i, int i2) {
        setLeftImage(i);
        setRightImage(i2);
    }

    public void setLeftImage(int i) {
        this.leftIcon.setVisibility(0);
        this.leftIcon.setImageResource(i);
    }

    public void setParentBg(int i) {
        ((View) getParent()).setBackgroundResource(i);
    }

    public void setRightImage(int i) {
        this.rightIcon.setVisibility(0);
        this.rightIcon.setImageResource(i);
    }

    public void setRightText(int i) {
        this.rightText.setVisibility(0);
        this.rightText.setText(i);
    }

    public void setRightText(int i, int i2) {
        this.rightText.setVisibility(0);
        this.rightText.setText(i);
        this.rightText.setTextColor(getResources().getColor(i2));
    }

    public void setTitle(int i) {
        this.centerText.setText(i);
    }

    public void setTitle(String str) {
        this.centerText.setText(str);
    }

    public void setTitleColor(int i) {
        this.centerText.setTextColor(i);
    }
}
