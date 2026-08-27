package cn.sharesdk.framework.authorize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.TitleLayout;
import com.mob.tools.utils.ResHelper;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class RegisterView extends ResizeLayout {
    private TitleLayout a;
    private RelativeLayout b;
    private WebView c;
    private TextView d;

    public RegisterView(Context context) {
        super(context);
        a(context);
    }

    public RegisterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a(context);
    }

    private void a(Context context) {
        setBackgroundColor(-1);
        setOrientation(1);
        final int b = b(context);
        this.a = new TitleLayout(context);
        try {
            int bitmapRes = ResHelper.getBitmapRes(context, "ssdk_auth_title_back");
            if (bitmapRes > 0) {
                this.a.setBackgroundResource(bitmapRes);
            }
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
        this.a.getBtnRight().setVisibility(8);
        int stringRes = ResHelper.getStringRes(getContext(), "ssdk_weibo_oauth_regiseter");
        if (stringRes > 0) {
            this.a.getTvTitle().setText(stringRes);
        }
        addView(this.a);
        ImageView imageView = new ImageView(context);
        int bitmapRes2 = ResHelper.getBitmapRes(context, "ssdk_logo");
        if (bitmapRes2 > 0) {
            imageView.setImageResource(bitmapRes2);
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setPadding(0, 0, ResHelper.dipToPx(context, 10), 0);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
        imageView.setOnClickListener(new View.OnClickListener() { // from class: cn.sharesdk.framework.authorize.RegisterView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    int stringRes2 = ResHelper.getStringRes(view.getContext(), "ssdk_website");
                    String string = stringRes2 > 0 ? view.getResources().getString(stringRes2) : null;
                    if (TextUtils.isEmpty(string)) {
                        return;
                    }
                    view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(string)));
                } catch (Throwable th2) {
                    cn.sharesdk.framework.utils.e.b().d(th2);
                }
            }
        });
        this.a.addView(imageView);
        this.b = new RelativeLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 0);
        layoutParams.weight = 1.0f;
        this.b.setLayoutParams(layoutParams);
        addView(this.b);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.b.addView(linearLayout);
        this.d = new TextView(context);
        this.d.setLayoutParams(new LinearLayout.LayoutParams(-1, 5));
        this.d.setBackgroundColor(-12929302);
        linearLayout.addView(this.d);
        this.d.setVisibility(8);
        this.c = new WebView(context);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -1);
        layoutParams2.weight = 1.0f;
        this.c.setLayoutParams(layoutParams2);
        WebChromeClient webChromeClient = new WebChromeClient() { // from class: cn.sharesdk.framework.authorize.RegisterView.2
            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) RegisterView.this.d.getLayoutParams();
                layoutParams3.width = (b * i) / 100;
                RegisterView.this.d.setLayoutParams(layoutParams3);
                if (i <= 0 || i >= 100) {
                    RegisterView.this.d.setVisibility(8);
                } else {
                    RegisterView.this.d.setVisibility(0);
                }
            }
        };
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            try {
                Method method = this.c.getClass().getMethod("removeJavascriptInterface", String.class);
                method.setAccessible(true);
                method.invoke(this.c, "searchBoxJavaBridge_");
            } catch (Throwable th2) {
                cn.sharesdk.framework.utils.e.b().d(th2);
            }
        }
        this.c.setWebChromeClient(webChromeClient);
        linearLayout.addView(this.c);
        this.c.requestFocus();
    }

    private int b(Context context) {
        WindowManager windowManager;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (!(context instanceof Activity) || (windowManager = ((Activity) context).getWindowManager()) == null) {
            return 0;
        }
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public View a() {
        return this.a.getBtnBack();
    }

    public void a(boolean z) {
        this.a.setVisibility(z ? 8 : 0);
    }

    public WebView b() {
        return this.c;
    }

    public TitleLayout c() {
        return this.a;
    }

    public RelativeLayout d() {
        return this.b;
    }
}
