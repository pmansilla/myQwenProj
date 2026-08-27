package com.qmuiteam.qmui.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;
import com.qmuiteam.qmui.R;

/* loaded from: classes2.dex */
public class QMUIResHelper {
    public static void assignTextViewWithAttr(TextView textView, int i) {
        TypedArray obtainStyledAttributes = textView.getContext().obtainStyledAttributes(null, R.styleable.QMUITextCommonStyleDef, i, 0);
        int indexCount = obtainStyledAttributes.getIndexCount();
        int paddingLeft = textView.getPaddingLeft();
        int paddingRight = textView.getPaddingRight();
        int paddingTop = textView.getPaddingTop();
        int paddingBottom = textView.getPaddingBottom();
        int i2 = paddingRight;
        int i3 = paddingLeft;
        for (int i4 = 0; i4 < indexCount; i4++) {
            int index = obtainStyledAttributes.getIndex(i4);
            if (index == R.styleable.QMUITextCommonStyleDef_android_gravity) {
                textView.setGravity(obtainStyledAttributes.getInt(index, -1));
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_textColor) {
                textView.setTextColor(obtainStyledAttributes.getColorStateList(index));
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_textSize) {
                textView.setTextSize(0, obtainStyledAttributes.getDimensionPixelSize(index, 0));
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_paddingLeft) {
                i3 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_paddingRight) {
                i2 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_paddingTop) {
                paddingTop = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_paddingBottom) {
                paddingBottom = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_singleLine) {
                textView.setSingleLine(obtainStyledAttributes.getBoolean(index, false));
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_ellipsize) {
                switch (obtainStyledAttributes.getInt(index, 3)) {
                    case 1:
                        textView.setEllipsize(TextUtils.TruncateAt.START);
                        break;
                    case 2:
                        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                        break;
                    case 3:
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                        break;
                    case 4:
                        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        break;
                }
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_maxLines) {
                textView.setMaxLines(obtainStyledAttributes.getInt(index, -1));
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_background) {
                QMUIViewHelper.setBackgroundKeepingPadding(textView, obtainStyledAttributes.getDrawable(index));
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_lineSpacingExtra) {
                textView.setLineSpacing(obtainStyledAttributes.getDimensionPixelSize(index, 0), 1.0f);
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_drawablePadding) {
                textView.setCompoundDrawablePadding(obtainStyledAttributes.getDimensionPixelSize(index, 0));
            } else if (index == R.styleable.QMUITextCommonStyleDef_android_textColorHint) {
                textView.setHintTextColor(obtainStyledAttributes.getColor(index, 0));
            }
        }
        textView.setPadding(i3, paddingTop, i2, paddingBottom);
        obtainStyledAttributes.recycle();
    }

    public static int getAttrColor(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        return typedValue.data;
    }

    public static ColorStateList getAttrColorStateList(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        return ContextCompat.getColorStateList(context, typedValue.resourceId);
    }

    public static int getAttrDimen(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        return TypedValue.complexToDimensionPixelSize(typedValue.data, QMUIDisplayHelper.getDisplayMetrics(context));
    }

    public static Drawable getAttrDrawable(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }

    public static float getAttrFloatValue(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        return typedValue.getFloat();
    }
}
