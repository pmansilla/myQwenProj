package com.qmuiteam.qmui.util;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import com.qmuiteam.qmui.span.QMUIMarginImageSpan;

/* loaded from: classes2.dex */
public class QMUISpanHelper {
    public static CharSequence generateSideIconText(boolean z, int i, CharSequence charSequence, Drawable drawable) {
        int length;
        int length2;
        if (drawable == null) {
            return charSequence;
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (z) {
            spannableStringBuilder.append((CharSequence) "[icon]");
            length2 = spannableStringBuilder.length();
            spannableStringBuilder.append(charSequence);
            length = 0;
        } else {
            spannableStringBuilder.append(charSequence);
            length = spannableStringBuilder.length();
            spannableStringBuilder.append((CharSequence) "[icon]");
            length2 = spannableStringBuilder.length();
        }
        QMUIMarginImageSpan qMUIMarginImageSpan = z ? new QMUIMarginImageSpan(drawable, -100, 0, i) : new QMUIMarginImageSpan(drawable, -100, i, 0);
        spannableStringBuilder.setSpan(qMUIMarginImageSpan, length, length2, 17);
        qMUIMarginImageSpan.setAvoidSuperChangeFontMetrics(true);
        return spannableStringBuilder;
    }
}
