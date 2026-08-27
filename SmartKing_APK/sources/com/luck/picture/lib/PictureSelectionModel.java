package com.luck.picture.lib;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DoubleUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PictureSelectionModel {
    private PictureSelectionConfig selectionConfig = PictureSelectionConfig.getCleanInstance();
    private PictureSelector selector;

    public PictureSelectionModel(PictureSelector pictureSelector, int i) {
        this.selector = pictureSelector;
        this.selectionConfig.mimeType = i;
    }

    public PictureSelectionModel(PictureSelector pictureSelector, int i, boolean z) {
        this.selector = pictureSelector;
        this.selectionConfig.camera = z;
        this.selectionConfig.mimeType = i;
    }

    public PictureSelectionModel circleDimmedLayer(boolean z) {
        this.selectionConfig.circleDimmedLayer = z;
        return this;
    }

    public PictureSelectionModel compress(boolean z) {
        this.selectionConfig.isCompress = z;
        return this;
    }

    public PictureSelectionModel compressSavePath(String str) {
        this.selectionConfig.compressSavePath = str;
        return this;
    }

    public PictureSelectionModel cropCompressQuality(int i) {
        this.selectionConfig.cropCompressQuality = i;
        return this;
    }

    public PictureSelectionModel cropWH(int i, int i2) {
        this.selectionConfig.cropWidth = i;
        this.selectionConfig.cropHeight = i2;
        return this;
    }

    public PictureSelectionModel enableCrop(boolean z) {
        this.selectionConfig.enableCrop = z;
        return this;
    }

    public PictureSelectionModel enablePreviewAudio(boolean z) {
        this.selectionConfig.enablePreviewAudio = z;
        return this;
    }

    public void forResult(int i) {
        Activity activity;
        if (DoubleUtils.isFastDoubleClick() || (activity = this.selector.getActivity()) == null) {
            return;
        }
        Intent intent = new Intent(activity, (Class<?>) PictureSelectorActivity.class);
        Fragment fragment = this.selector.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, i);
        } else {
            activity.startActivityForResult(intent, i);
        }
        activity.overridePendingTransition(R.anim.a5, 0);
    }

    public PictureSelectionModel freeStyleCropEnabled(boolean z) {
        this.selectionConfig.freeStyleCropEnabled = z;
        return this;
    }

    public PictureSelectionModel glideOverride(@IntRange(from = 100) int i, @IntRange(from = 100) int i2) {
        this.selectionConfig.overrideWidth = i;
        this.selectionConfig.overrideHeight = i2;
        return this;
    }

    public PictureSelectionModel hideBottomControls(boolean z) {
        this.selectionConfig.hideBottomControls = z;
        return this;
    }

    public PictureSelectionModel imageFormat(String str) {
        this.selectionConfig.suffixType = str;
        return this;
    }

    public PictureSelectionModel imageSpanCount(int i) {
        this.selectionConfig.imageSpanCount = i;
        return this;
    }

    public PictureSelectionModel isCamera(boolean z) {
        this.selectionConfig.isCamera = z;
        return this;
    }

    public PictureSelectionModel isGif(boolean z) {
        this.selectionConfig.isGif = z;
        return this;
    }

    public PictureSelectionModel isZoomAnim(boolean z) {
        this.selectionConfig.zoomAnim = z;
        return this;
    }

    public PictureSelectionModel maxSelectNum(int i) {
        this.selectionConfig.maxSelectNum = i;
        return this;
    }

    public PictureSelectionModel minSelectNum(int i) {
        this.selectionConfig.minSelectNum = i;
        return this;
    }

    public PictureSelectionModel minimumCompressSize(int i) {
        this.selectionConfig.minimumCompressSize = i;
        return this;
    }

    public PictureSelectionModel openClickSound(boolean z) {
        this.selectionConfig.openClickSound = z;
        return this;
    }

    public PictureSelectionModel previewEggs(boolean z) {
        this.selectionConfig.previewEggs = z;
        return this;
    }

    public PictureSelectionModel previewImage(boolean z) {
        this.selectionConfig.enablePreview = z;
        return this;
    }

    public PictureSelectionModel previewVideo(boolean z) {
        this.selectionConfig.enPreviewVideo = z;
        return this;
    }

    public PictureSelectionModel recordVideoSecond(int i) {
        this.selectionConfig.recordVideoSecond = i;
        return this;
    }

    public PictureSelectionModel rotateEnabled(boolean z) {
        this.selectionConfig.rotateEnabled = z;
        return this;
    }

    public PictureSelectionModel scaleEnabled(boolean z) {
        this.selectionConfig.scaleEnabled = z;
        return this;
    }

    public PictureSelectionModel selectionMedia(List<LocalMedia> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.selectionConfig.selectionMedias = list;
        return this;
    }

    public PictureSelectionModel selectionMode(int i) {
        this.selectionConfig.selectionMode = i;
        return this;
    }

    public PictureSelectionModel setOutputCameraPath(String str) {
        this.selectionConfig.outputCameraPath = str;
        return this;
    }

    public PictureSelectionModel showCropFrame(boolean z) {
        this.selectionConfig.showCropFrame = z;
        return this;
    }

    public PictureSelectionModel showCropGrid(boolean z) {
        this.selectionConfig.showCropGrid = z;
        return this;
    }

    public PictureSelectionModel sizeMultiplier(@FloatRange(from = 0.10000000149011612d) float f) {
        this.selectionConfig.sizeMultiplier = f;
        return this;
    }

    public PictureSelectionModel synOrAsy(boolean z) {
        this.selectionConfig.synOrAsy = z;
        return this;
    }

    public PictureSelectionModel theme(@StyleRes int i) {
        this.selectionConfig.themeStyleId = i;
        return this;
    }

    public PictureSelectionModel videoMaxSecond(int i) {
        this.selectionConfig.videoMaxSecond = i * 1000;
        return this;
    }

    public PictureSelectionModel videoMinSecond(int i) {
        this.selectionConfig.videoMinSecond = i * 1000;
        return this;
    }

    public PictureSelectionModel videoQuality(int i) {
        this.selectionConfig.videoQuality = i;
        return this;
    }

    public PictureSelectionModel withAspectRatio(int i, int i2) {
        this.selectionConfig.aspect_ratio_x = i;
        this.selectionConfig.aspect_ratio_y = i2;
        return this;
    }
}
