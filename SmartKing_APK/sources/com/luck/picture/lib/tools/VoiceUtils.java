package com.luck.picture.lib.tools;

import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;
import com.luck.picture.lib.R;

/* loaded from: classes.dex */
public class VoiceUtils {
    private static int soundID;
    private static SoundPool soundPool;
    private boolean isPlay;

    public static void play(boolean z, SoundPool soundPool2) {
        if (z) {
            soundPool2.play(soundID, 0.1f, 0.5f, 0, 1, 1.0f);
        }
    }

    public static void playVoice(Context context, final boolean z) {
        if (soundPool == null) {
            soundPool = new SoundPool(1, 4, 0);
            soundID = soundPool.load(context, R.raw.music, 1);
        }
        new Handler().postDelayed(new Runnable() { // from class: com.luck.picture.lib.tools.VoiceUtils.1
            @Override // java.lang.Runnable
            public void run() {
                VoiceUtils.play(z, VoiceUtils.soundPool);
            }
        }, 20L);
    }

    public static void release() {
        if (soundPool != null) {
            soundPool.stop(soundID);
        }
        soundPool = null;
    }
}
