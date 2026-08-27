package com.czw.smartkit.bleModule;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import java.io.IOException;

/* loaded from: classes.dex */
public class LostPlayUtil {
    private static LostPlayUtil lostPlayUtil = new LostPlayUtil();
    private static Context mContext;
    private MediaPlayer player = null;
    private AssetFileDescriptor assetFileDescriptor = null;

    private LostPlayUtil() {
    }

    public static LostPlayUtil getLostPlayUtil() {
        return lostPlayUtil;
    }

    private void init() {
        this.player = new MediaPlayer();
        try {
            this.assetFileDescriptor = mContext.getAssets().openFd("findPhone.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init(Context context) {
        mContext = context;
    }

    public void play() {
        if (this.player == null) {
            init();
        }
        stop();
        this.player.reset();
        try {
            this.player.setDataSource(this.assetFileDescriptor.getFileDescriptor(), this.assetFileDescriptor.getStartOffset(), this.assetFileDescriptor.getLength());
            this.player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.player.start();
        this.player.setLooping(true);
    }

    public void stop() {
        if (this.player == null || !this.player.isPlaying()) {
            return;
        }
        this.player.stop();
        this.player.reset();
    }
}
