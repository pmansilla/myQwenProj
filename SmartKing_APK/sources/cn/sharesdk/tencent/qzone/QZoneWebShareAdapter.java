package cn.sharesdk.tencent.qzone;

import android.app.Activity;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import cn.sharesdk.framework.TitleLayout;

/* loaded from: classes.dex */
public class QZoneWebShareAdapter {
    private Activity activity;
    private boolean noTitle;
    private RelativeLayout rlBody;
    private TitleLayout title;
    private WebView webview;

    public Activity getActivity() {
        return this.activity;
    }

    public RelativeLayout getBodyView() {
        return this.rlBody;
    }

    public TitleLayout getTitleLayout() {
        return this.title;
    }

    public WebView getWebBody() {
        return this.webview;
    }

    boolean isNotitle() {
        return this.noTitle;
    }

    public void onCreate() {
    }

    public void onDestroy() {
    }

    public boolean onFinish() {
        return false;
    }

    public void onPause() {
    }

    public void onRestart() {
    }

    public void onResume() {
    }

    public void onStart() {
    }

    public void onStop() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBodyView(RelativeLayout relativeLayout) {
        this.rlBody = relativeLayout;
    }

    void setNotitle(boolean z) {
        this.noTitle = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTitleView(TitleLayout titleLayout) {
        this.title = titleLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWebView(WebView webView) {
        this.webview = webView;
    }
}
