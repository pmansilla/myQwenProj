package basecamera.module.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import basecamera.module.lib.R;

/* loaded from: classes.dex */
public class CameraTitleBar extends RelativeLayout {
    private ImageView leftImageView;
    private TextView leftTxtView;
    private View leftView;
    private ImageView rightImageView;
    private TextView rightTxtView;
    private View rightView;
    private View rootView;
    private TextView titleTxtView;

    public CameraTitleBar(Context context) {
        super(context);
        initView();
    }

    public CameraTitleBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public CameraTitleBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    private void initView() {
        this.rootView = LayoutInflater.from(getContext()).inflate(R.layout.basecamera_title_bar_layout, (ViewGroup) this, true);
        this.leftView = this.rootView.findViewById(R.id.leftView);
        this.rightView = this.rootView.findViewById(R.id.rightView);
        this.leftImageView = (ImageView) this.rootView.findViewById(R.id.left_icon_view);
        this.rightImageView = (ImageView) this.rootView.findViewById(R.id.right_icon_view);
        this.leftTxtView = (TextView) this.rootView.findViewById(R.id.leftText);
        this.titleTxtView = (TextView) this.rootView.findViewById(R.id.title_txtView);
        this.rightTxtView = (TextView) this.rootView.findViewById(R.id.rightText);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        ((View) getParent()).setBackgroundColor(i);
    }

    @Override // android.view.View
    public void setBackgroundResource(int i) {
        ((View) getParent()).setBackgroundResource(i);
    }

    public void setLeftImage(int i) {
        if (i == -1) {
            this.leftImageView.setVisibility(8);
        } else {
            this.leftImageView.setVisibility(0);
            this.leftImageView.setImageResource(i);
        }
    }

    public void setLeftText(int i) {
        this.leftTxtView.setVisibility(0);
        this.leftTxtView.setText(i);
    }

    public void setLeftText(String str) {
        this.leftTxtView.setVisibility(0);
        this.leftTxtView.setText(str);
    }

    public void setLeftViewOnClickListener(View.OnClickListener onClickListener) {
        this.leftView.setOnClickListener(onClickListener);
    }

    public void setRightImage(int i) {
        this.rightImageView.setVisibility(0);
        this.rightImageView.setImageResource(i);
    }

    public void setRightText(int i) {
        this.rightTxtView.setVisibility(0);
        this.rightTxtView.setText(i);
    }

    public void setRightText(String str) {
        this.rightTxtView.setVisibility(0);
        this.rightTxtView.setText(str);
    }

    public void setRightViewOnClickListener(View.OnClickListener onClickListener) {
        this.rightView.setOnClickListener(onClickListener);
    }

    public void setTitle(int i) {
        this.titleTxtView.setText(i);
    }

    public void setTitle(String str) {
        this.titleTxtView.setText(str);
    }

    public void setTitleBg(int i) {
        this.rootView.setBackgroundResource(i);
    }

    public void setTitleColor(int i) {
        this.titleTxtView.setTextColor(getContext().getResources().getColor(i));
    }

    public void setTitleColor(String str) {
        this.titleTxtView.setTextColor(Color.parseColor(str));
    }
}
