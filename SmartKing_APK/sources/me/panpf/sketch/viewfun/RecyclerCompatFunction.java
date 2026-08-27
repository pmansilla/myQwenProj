package me.panpf.sketch.viewfun;

import android.support.annotation.Nullable;
import me.panpf.sketch.SLog;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.RedisplayListener;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public class RecyclerCompatFunction extends ViewFunction {
    private static final String NAME = "RecyclerCompatFunction";
    private boolean isSetImage;
    private RedisplayListener redisplayListener;
    private SketchView sketchView;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class RecyclerRedisplayListener implements RedisplayListener {
        private RecyclerRedisplayListener() {
        }

        @Override // me.panpf.sketch.request.RedisplayListener
        public void onPreCommit(String str, DisplayOptions displayOptions) {
            if (SLog.isLoggable(65538)) {
                SLog.d(RecyclerCompatFunction.NAME, "restore image on attached to window. %s", str);
            }
        }
    }

    public RecyclerCompatFunction(SketchView sketchView) {
        this.sketchView = sketchView;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onAttachedToWindow() {
        if (this.isSetImage) {
            return;
        }
        if (this.redisplayListener == null) {
            this.redisplayListener = new RecyclerRedisplayListener();
        }
        this.sketchView.redisplay(this.redisplayListener);
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDetachedFromWindow() {
        this.isSetImage = false;
        return false;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onReadyDisplay(@Nullable UriModel uriModel) {
        this.isSetImage = true;
        return false;
    }
}
