package me.panpf.sketch.viewfun;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.RedisplayListener;
import me.panpf.sketch.request.RequestLevel;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public class ClickRetryFunction extends ViewFunction {
    private boolean clickRetryOnDisplayErrorEnabled;
    private boolean clickRetryOnPauseDownloadEnabled;
    private boolean displayError;
    private boolean pauseDownload;
    private RedisplayListener redisplayListener;
    private FunctionCallbackView view;

    /* loaded from: classes2.dex */
    private class RetryOnPauseDownloadRedisplayListener implements RedisplayListener {
        private RetryOnPauseDownloadRedisplayListener() {
        }

        @Override // me.panpf.sketch.request.RedisplayListener
        public void onPreCommit(String str, DisplayOptions displayOptions) {
            if (ClickRetryFunction.this.clickRetryOnPauseDownloadEnabled && ClickRetryFunction.this.pauseDownload) {
                displayOptions.setRequestLevel(RequestLevel.NET);
            }
        }
    }

    public ClickRetryFunction(FunctionCallbackView functionCallbackView) {
        this.view = functionCallbackView;
    }

    public boolean isClickRetryOnDisplayErrorEnabled() {
        return this.clickRetryOnDisplayErrorEnabled;
    }

    public boolean isClickRetryOnPauseDownloadEnabled() {
        return this.clickRetryOnPauseDownloadEnabled;
    }

    public boolean isClickable() {
        return (this.clickRetryOnDisplayErrorEnabled && this.displayError) || (this.clickRetryOnPauseDownloadEnabled && this.pauseDownload);
    }

    public boolean onClick(View view) {
        if (!isClickable()) {
            return false;
        }
        if (this.redisplayListener == null) {
            this.redisplayListener = new RetryOnPauseDownloadRedisplayListener();
        }
        return this.view.redisplay(this.redisplayListener);
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDisplayCanceled(@NonNull CancelCause cancelCause) {
        this.pauseDownload = cancelCause == CancelCause.PAUSE_DOWNLOAD;
        this.view.updateClickable();
        return false;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDisplayError(@NonNull ErrorCause errorCause) {
        this.displayError = (errorCause == ErrorCause.URI_INVALID || errorCause == ErrorCause.URI_NO_SUPPORT) ? false : true;
        this.view.updateClickable();
        return false;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onReadyDisplay(@Nullable UriModel uriModel) {
        this.displayError = false;
        this.pauseDownload = false;
        this.view.updateClickable();
        return false;
    }

    public void setClickRetryOnDisplayErrorEnabled(boolean z) {
        this.clickRetryOnDisplayErrorEnabled = z;
    }

    public void setClickRetryOnPauseDownloadEnabled(boolean z) {
        this.clickRetryOnPauseDownloadEnabled = z;
    }
}
