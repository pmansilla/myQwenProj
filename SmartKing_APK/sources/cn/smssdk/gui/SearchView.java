package cn.smssdk.gui;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class SearchView extends RelativeLayout {
    private Context context;
    private boolean enableClear;
    private EditText etSearch;

    public SearchView(Context context, boolean z) {
        super(context);
        this.context = context;
        this.enableClear = z;
        initView();
    }

    private void initView() {
        setBackgroundResource(ResHelper.getColorRes(this.context, "smssdk_white"));
        setPadding(ResHelper.dipToPx(this.context, 15), ResHelper.dipToPx(this.context, 8), ResHelper.dipToPx(this.context, 15), ResHelper.dipToPx(this.context, 8));
        setGravity(16);
        setLayoutParams(new RelativeLayout.LayoutParams(-1, ResHelper.dipToPx(this.context, 50)));
        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setBackgroundResource(ResHelper.getBitmapRes(this.context, "smssdk_conners_edittext_bg"));
        ImageView imageView = new ImageView(this.context);
        imageView.setImageResource(ResHelper.getBitmapRes(this.context, "smssdk_search"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.leftMargin = ResHelper.dipToPx(this.context, 15);
        linearLayout.addView(imageView, layoutParams);
        this.etSearch = new EditText(this.context);
        this.etSearch.setId(ResHelper.getIdRes(this.context, "et_put_identify"));
        this.etSearch.setHint(ResHelper.getStringRes(this.context, "smssdk_search"));
        this.etSearch.setHintTextColor(this.context.getResources().getColor(ResHelper.getColorRes(this.context, "smssdk_999999")));
        this.etSearch.setGravity(19);
        this.etSearch.setSingleLine(true);
        this.etSearch.setTextSize(2, 14.0f);
        this.etSearch.setPadding(0, 0, 0, 0);
        this.etSearch.setBackgroundResource(0);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, -2);
        layoutParams2.weight = 1.0f;
        layoutParams2.leftMargin = ResHelper.dipToPx(this.context, 3);
        linearLayout.addView(this.etSearch, layoutParams2);
        addView(linearLayout, new RelativeLayout.LayoutParams(-1, -1));
        if (this.enableClear) {
            final ImageView imageView2 = new ImageView(this.context);
            imageView2.setId(ResHelper.getIdRes(this.context, "iv_clear"));
            imageView2.setImageResource(ResHelper.getBitmapRes(this.context, "smssdk_clear_search"));
            imageView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView2.setVisibility(8);
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(ResHelper.dipToPx(this.context, 40), -1);
            layoutParams3.addRule(11);
            layoutParams3.setMargins(0, 0, ResHelper.dipToPx(this.context, 10), 0);
            imageView2.setLayoutParams(layoutParams3);
            addView(imageView2);
            this.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: cn.smssdk.gui.SearchView.1
                @Override // android.view.View.OnFocusChangeListener
                public void onFocusChange(View view, boolean z) {
                    if (z) {
                        imageView2.setVisibility(0);
                    }
                }
            });
        }
    }

    public void setEditTextBackgroundResource(int i) {
        if (i >= 0) {
            this.etSearch.setBackgroundResource(i);
        }
    }

    public void setEditTextGravity(int i) {
        this.etSearch.setGravity(i);
    }
}
