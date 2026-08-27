package com.czw.smartkit.user;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.czw.smartkit.R;
import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.preferenceModule.SharePreferenceUser;
import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.process.CircleImageProcessor;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.DisplayListener;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes.dex */
public class UserUtil {
    public static int getTarget() {
        UserDTO read = SharePreferenceUser.read();
        if (read == null || TextUtils.isEmpty(read.getWalkGoal())) {
            return 8000;
        }
        return Integer.valueOf(read.getWalkGoal()).intValue();
    }

    public static String getUid() {
        UserDTO read = SharePreferenceUser.read();
        return (read == null || TextUtils.isEmpty(read.getUserId())) ? "" : read.getUserId();
    }

    public static void showHeader(final SketchImageView sketchImageView, String str) {
        if (sketchImageView == null) {
            return;
        }
        sketchImageView.getOptions().setProcessor((ImageProcessor) CircleImageProcessor.getInstance());
        if (TextUtils.isEmpty(str)) {
            sketchImageView.displayResourceImage(R.mipmap.ic_launcher_round);
        } else {
            sketchImageView.displayImage(str);
        }
        sketchImageView.setDisplayListener(new DisplayListener() { // from class: com.czw.smartkit.user.UserUtil.1
            @Override // me.panpf.sketch.request.Listener
            public void onCanceled(@NonNull CancelCause cancelCause) {
            }

            @Override // me.panpf.sketch.request.DisplayListener
            public void onCompleted(@NonNull Drawable drawable, @NonNull ImageFrom imageFrom, @NonNull ImageAttrs imageAttrs) {
            }

            @Override // me.panpf.sketch.request.Listener
            public void onError(@NonNull ErrorCause errorCause) {
                SketchImageView.this.displayResourceImage(R.mipmap.ic_launcher_round);
            }

            @Override // me.panpf.sketch.request.DisplayListener, me.panpf.sketch.request.Listener
            public void onStarted() {
            }
        });
    }
}
