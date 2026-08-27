package com.czw.smartkit.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.BindView;
import com.czw.smartkit.R;
import com.czw.smartkit.util.YCAppUtils;
import com.liulishuo.filedownloader.model.FileDownloadModel;

/* loaded from: classes.dex */
public class WebActivity extends TitleActivity {

    @BindView(R.id.webView)
    WebView webView;

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        String stringExtra = getIntent().getStringExtra("title");
        String stringExtra2 = getIntent().getStringExtra(FileDownloadModel.URL);
        if (YCAppUtils.isChainess()) {
            stringExtra2 = stringExtra2 + "?lang=1";
        }
        this.titleBar.setTitle(stringExtra);
        this.webView.loadUrl(stringExtra2);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() { // from class: com.czw.smartkit.base.WebActivity.1
            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                builder.setMessage(R.string.ssl_error);
                builder.setPositiveButton(R.string.continue_text, new DialogInterface.OnClickListener() { // from class: com.czw.smartkit.base.WebActivity.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.czw.smartkit.base.WebActivity.1.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.czw.smartkit.base.WebActivity.1.3
                    @Override // android.content.DialogInterface.OnKeyListener
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() != 1 || i != 4) {
                            return false;
                        }
                        sslErrorHandler.cancel();
                        dialogInterface.dismiss();
                        return true;
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.activity_web;
    }
}
