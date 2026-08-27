package com.czw.smartkit.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import java.io.IOException;

/* loaded from: classes.dex */
public class SoundUtil {
    private static SoundUtil util;
    private AssetFileDescriptor find;
    private MediaPlayer player;

    private SoundUtil(Context context) {
        this.player = null;
        this.find = null;
        try {
            this.player = new MediaPlayer();
            this.find = context.getAssets().openFd("findPhone.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SoundUtil getUtil(Context context) {
        synchronized (SoundUtil.class) {
            if (util == null) {
                synchronized (SoundUtil.class) {
                    util = new SoundUtil(context);
                }
            }
        }
        return util;
    }

    public void playFind() throws Exception {
        this.player.reset();
        this.player.setDataSource(this.find.getFileDescriptor(), this.find.getStartOffset(), this.find.getLength());
        this.player.prepare();
        this.player.start();
        this.player.setLooping(true);
    }

    public void stopFind() {
        if (this.player == null || !this.player.isPlaying()) {
            return;
        }
        this.player.stop();
        this.player.reset();
    }
}
