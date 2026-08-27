package com.luck.picture.lib.tools;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;
import com.luck.picture.lib.R;
import com.luck.picture.lib.config.PictureMimeType;

/* loaded from: classes.dex */
public class StringUtils {
    public static boolean isCamera(String str) {
        return (!TextUtils.isEmpty(str) && str.startsWith("相机胶卷")) || str.startsWith("CameraRoll") || str.startsWith("所有音频") || str.startsWith("All audio");
    }

    public static void modifyTextViewDrawable(TextView textView, Drawable drawable, int i) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (i == 0) {
            textView.setCompoundDrawables(drawable, null, null, null);
            return;
        }
        if (i == 1) {
            textView.setCompoundDrawables(null, drawable, null, null);
        } else if (i == 2) {
            textView.setCompoundDrawables(null, null, drawable, null);
        } else {
            textView.setCompoundDrawables(null, null, null, drawable);
        }
    }

    public static void tempTextFont(TextView textView, int i) {
        String trim = textView.getText().toString().trim();
        String string = i == PictureMimeType.ofAudio() ? textView.getContext().getString(R.string.picture_empty_audio_title) : textView.getContext().getString(R.string.picture_empty_title);
        String str = string + trim;
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), string.length(), str.length(), 33);
        textView.setText(spannableString);
    }
}
