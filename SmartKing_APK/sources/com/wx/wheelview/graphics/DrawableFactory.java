package com.wx.wheelview.graphics;

import android.graphics.drawable.Drawable;
import com.wx.wheelview.widget.WheelView;

/* loaded from: classes2.dex */
public class DrawableFactory {
    public static Drawable createDrawable(WheelView.Skin skin, int i, int i2, WheelView.WheelViewStyle wheelViewStyle, int i3, int i4) {
        return skin.equals(WheelView.Skin.Common) ? new CommonDrawable(i, i2, wheelViewStyle, i3, i4) : skin.equals(WheelView.Skin.Holo) ? new HoloDrawable(i, i2, wheelViewStyle, i3, i4) : new WheelDrawable(i, i2, wheelViewStyle);
    }
}
