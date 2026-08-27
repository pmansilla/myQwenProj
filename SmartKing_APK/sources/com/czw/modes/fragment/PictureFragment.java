package com.czw.modes.fragment;

import android.content.Intent;
import com.czw.utils.LogUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wx.wheelview.common.WheelConstants;
import java.util.List;

/* loaded from: classes.dex */
public abstract class PictureFragment extends RootFragment {
    public void jump2Camera() {
        PictureSelector.create(this).openCamera(PictureMimeType.ofImage()).forResult(188);
    }

    public void jump2Gallery(boolean z) {
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage()).isCamera(false).enableCrop(true).cropWH(WheelConstants.WHEEL_SCROLL_DELAY_DURATION, WheelConstants.WHEEL_SCROLL_DELAY_DURATION).scaleEnabled(true).selectionMode(1).freeStyleCropEnabled(true).withAspectRatio(1, 1).forResult(188);
    }

    public void jump2GalleryMulite(int i, int i2) {
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage()).isCamera(false).enableCrop(false).scaleEnabled(true).compress(true).maxSelectNum(i).minSelectNum(i2).selectionMode(i2 > 1 ? 2 : 1).forResult(188);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        LogUtil.e("debug_onActivityREsult==>" + i + "==" + i2);
        if (i2 == -1 && i == 188) {
            onImageSelect(PictureSelector.obtainMultipleResult(intent));
        }
    }

    protected abstract void onImageSelect(List<LocalMedia> list);
}
