package cn.smssdk.gui.layout;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/* loaded from: classes.dex */
public abstract class BasePageLayout {
    Context context;
    LinearLayout layout;

    public BasePageLayout(Context context, String str) {
        this.layout = null;
        this.context = null;
        this.context = context;
        this.layout = new LinearLayout(this.context);
        this.layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.layout.setOrientation(1);
        this.layout.setBackgroundColor(-1);
        this.layout.addView(TitleLayout.create(this.context, str));
        onCreateContent(this.layout);
    }

    public BasePageLayout(Context context, boolean z) {
        this.layout = null;
        this.context = null;
        this.context = context;
        this.layout = new LinearLayout(this.context);
        this.layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.layout.setOrientation(1);
        this.layout.setBackgroundColor(-1);
        this.layout.addView(TitleLayout.create(this.context, z));
        onCreateContent(this.layout);
    }

    public LinearLayout getLayout() {
        return this.layout;
    }

    protected abstract void onCreateContent(LinearLayout linearLayout);
}
