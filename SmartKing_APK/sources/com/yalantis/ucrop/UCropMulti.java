package com.yalantis.ucrop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.model.CutInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class UCropMulti {
    public static final String EXTRA_ASPECT_RATIO_X = "com.yalantis.ucrop.AspectRatioX";
    public static final String EXTRA_ASPECT_RATIO_Y = "com.yalantis.ucrop.AspectRatioY";
    public static final String EXTRA_ERROR = "com.yalantis.ucrop.Error";
    public static final String EXTRA_INPUT_URI = "com.yalantis.ucrop.InputUri";
    public static final String EXTRA_MAX_SIZE_X = "com.yalantis.ucrop.MaxSizeX";
    public static final String EXTRA_MAX_SIZE_Y = "com.yalantis.ucrop.MaxSizeY";
    public static final String EXTRA_OUTPUT_CROP_ASPECT_RATIO = "com.yalantis.ucrop.CropAspectRatio";
    public static final String EXTRA_OUTPUT_IMAGE_HEIGHT = "com.yalantis.ucrop.ImageHeight";
    public static final String EXTRA_OUTPUT_IMAGE_WIDTH = "com.yalantis.ucrop.ImageWidth";
    public static final String EXTRA_OUTPUT_OFFSET_X = "com.yalantis.ucrop.OffsetX";
    public static final String EXTRA_OUTPUT_OFFSET_Y = "com.yalantis.ucrop.OffsetY";
    public static final String EXTRA_OUTPUT_URI = "com.yalantis.ucrop.OutputUri";
    public static final String EXTRA_OUTPUT_URI_LIST = "com.yalantis.ucrop.OutputUriList";
    private static final String EXTRA_PREFIX = "com.yalantis.ucrop";
    public static final int REQUEST_MULTI_CROP = 609;
    public static final int RESULT_ERROR = 96;
    private Intent mCropIntent = new Intent();
    private Bundle mCropOptionsBundle = new Bundle();

    /* loaded from: classes2.dex */
    public static class Options {
        public static final String EXTRA_ALLOWED_GESTURES = "com.yalantis.ucrop.AllowedGestures";
        public static final String EXTRA_ASPECT_RATIO_OPTIONS = "com.yalantis.ucrop.AspectRatioOptions";
        public static final String EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT = "com.yalantis.ucrop.AspectRatioSelectedByDefault";
        public static final String EXTRA_CIRCLE_DIMMED_LAYER = "com.yalantis.ucrop.CircleDimmedLayer";
        public static final String EXTRA_COMPRESSION_FORMAT_NAME = "com.yalantis.ucrop.CompressionFormatName";
        public static final String EXTRA_COMPRESSION_QUALITY = "com.yalantis.ucrop.CompressionQuality";
        public static final String EXTRA_CROP_FRAME_COLOR = "com.yalantis.ucrop.CropFrameColor";
        public static final String EXTRA_CROP_FRAME_STROKE_WIDTH = "com.yalantis.ucrop.CropFrameStrokeWidth";
        public static final String EXTRA_CROP_GRID_COLOR = "com.yalantis.ucrop.CropGridColor";
        public static final String EXTRA_CROP_GRID_COLUMN_COUNT = "com.yalantis.ucrop.CropGridColumnCount";
        public static final String EXTRA_CROP_GRID_ROW_COUNT = "com.yalantis.ucrop.CropGridRowCount";
        public static final String EXTRA_CROP_GRID_STROKE_WIDTH = "com.yalantis.ucrop.CropGridStrokeWidth";
        public static final String EXTRA_CUT_CROP = "com.yalantis.ucrop.cuts";
        public static final String EXTRA_DIMMED_LAYER_COLOR = "com.yalantis.ucrop.DimmedLayerColor";
        public static final String EXTRA_FREE_STATUS_FONT = "com.yalantis.ucrop.StatusFont";
        public static final String EXTRA_FREE_STYLE_CROP = "com.yalantis.ucrop.FreeStyleCrop";
        public static final String EXTRA_HIDE_BOTTOM_CONTROLS = "com.yalantis.ucrop.HideBottomControls";
        public static final String EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = "com.yalantis.ucrop.ImageToCropBoundsAnimDuration";
        public static final String EXTRA_MAX_BITMAP_SIZE = "com.yalantis.ucrop.MaxBitmapSize";
        public static final String EXTRA_MAX_SCALE_MULTIPLIER = "com.yalantis.ucrop.MaxScaleMultiplier";
        public static final String EXTRA_ROTATE = "com.yalantis.ucrop.rotate";
        public static final String EXTRA_SCALE = "com.yalantis.ucrop.scale";
        public static final String EXTRA_SHOW_CROP_FRAME = "com.yalantis.ucrop.ShowCropFrame";
        public static final String EXTRA_SHOW_CROP_GRID = "com.yalantis.ucrop.ShowCropGrid";
        public static final String EXTRA_STATUS_BAR_COLOR = "com.yalantis.ucrop.StatusBarColor";
        public static final String EXTRA_TOOL_BAR_COLOR = "com.yalantis.ucrop.ToolbarColor";
        public static final String EXTRA_UCROP_COLOR_WIDGET_ACTIVE = "com.yalantis.ucrop.UcropColorWidgetActive";
        public static final String EXTRA_UCROP_LOGO_COLOR = "com.yalantis.ucrop.UcropLogoColor";
        public static final String EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR = "com.yalantis.ucrop.UcropRootViewBackgroundColor";
        public static final String EXTRA_UCROP_TITLE_TEXT_TOOLBAR = "com.yalantis.ucrop.UcropToolbarTitleText";
        public static final String EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE = "com.yalantis.ucrop.UcropToolbarCancelDrawable";
        public static final String EXTRA_UCROP_WIDGET_COLOR_TOOLBAR = "com.yalantis.ucrop.UcropToolbarWidgetColor";
        public static final String EXTRA_UCROP_WIDGET_CROP_DRAWABLE = "com.yalantis.ucrop.UcropToolbarCropDrawable";
        private final Bundle mOptionBundle = new Bundle();

        @NonNull
        public Bundle getOptionBundle() {
            return this.mOptionBundle;
        }

        public void setActiveWidgetColor(@ColorInt int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.UcropColorWidgetActive", i);
        }

        public void setAllowedGestures(int i, int i2, int i3) {
            this.mOptionBundle.putIntArray("com.yalantis.ucrop.AllowedGestures", new int[]{i, i2, i3});
        }

        public void setAspectRatioOptions(int i, AspectRatio... aspectRatioArr) {
            if (i > aspectRatioArr.length) {
                throw new IllegalArgumentException(String.format(Locale.US, "Index [selectedByDefault = %d] cannot be higher than aspect ratio options count [count = %d].", Integer.valueOf(i), Integer.valueOf(aspectRatioArr.length)));
            }
            this.mOptionBundle.putInt("com.yalantis.ucrop.AspectRatioSelectedByDefault", i);
            this.mOptionBundle.putParcelableArrayList("com.yalantis.ucrop.AspectRatioOptions", new ArrayList<>(Arrays.asList(aspectRatioArr)));
        }

        public void setCircleDimmedLayer(boolean z) {
            this.mOptionBundle.putBoolean("com.yalantis.ucrop.CircleDimmedLayer", z);
        }

        public void setCompressionFormat(@NonNull Bitmap.CompressFormat compressFormat) {
            this.mOptionBundle.putString("com.yalantis.ucrop.CompressionFormatName", compressFormat.name());
        }

        public void setCompressionQuality(@IntRange(from = 0) int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.CompressionQuality", i);
        }

        public void setCropFrameColor(@ColorInt int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.CropFrameColor", i);
        }

        public void setCropFrameStrokeWidth(@IntRange(from = 0) int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.CropFrameStrokeWidth", i);
        }

        public void setCropGridColor(@ColorInt int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.CropGridColor", i);
        }

        public void setCropGridColumnCount(@IntRange(from = 0) int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.CropGridColumnCount", i);
        }

        public void setCropGridRowCount(@IntRange(from = 0) int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.CropGridRowCount", i);
        }

        public void setCropGridStrokeWidth(@IntRange(from = 0) int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.CropGridStrokeWidth", i);
        }

        public void setCutListData(ArrayList<String> arrayList) {
            this.mOptionBundle.putStringArrayList("com.yalantis.ucrop.cuts", arrayList);
        }

        public void setDimmedLayerColor(@ColorInt int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.DimmedLayerColor", i);
        }

        public void setFreeStyleCropEnabled(boolean z) {
            this.mOptionBundle.putBoolean("com.yalantis.ucrop.FreeStyleCrop", z);
        }

        public void setHideBottomControls(boolean z) {
            this.mOptionBundle.putBoolean("com.yalantis.ucrop.HideBottomControls", z);
        }

        public void setImageToCropBoundsAnimDuration(@IntRange(from = 100) int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.ImageToCropBoundsAnimDuration", i);
        }

        public void setLogoColor(@ColorInt int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.UcropLogoColor", i);
        }

        public void setMaxBitmapSize(@IntRange(from = 100) int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.MaxBitmapSize", i);
        }

        public void setMaxScaleMultiplier(@FloatRange(from = 1.0d, fromInclusive = false) float f) {
            this.mOptionBundle.putFloat("com.yalantis.ucrop.MaxScaleMultiplier", f);
        }

        public void setRootViewBackgroundColor(@ColorInt int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.UcropRootViewBackgroundColor", i);
        }

        public void setRotateEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_ROTATE, z);
        }

        public void setScaleEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_SCALE, z);
        }

        public void setShowCropFrame(boolean z) {
            this.mOptionBundle.putBoolean("com.yalantis.ucrop.ShowCropFrame", z);
        }

        public void setShowCropGrid(boolean z) {
            this.mOptionBundle.putBoolean("com.yalantis.ucrop.ShowCropGrid", z);
        }

        public void setStatusBarColor(@ColorInt int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.StatusBarColor", i);
        }

        public void setStatusFont(boolean z) {
            this.mOptionBundle.putBoolean("com.yalantis.ucrop.StatusFont", z);
        }

        public void setToolbarCancelDrawable(@DrawableRes int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.UcropToolbarCancelDrawable", i);
        }

        public void setToolbarColor(@ColorInt int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.ToolbarColor", i);
        }

        public void setToolbarCropDrawable(@DrawableRes int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.UcropToolbarCropDrawable", i);
        }

        public void setToolbarTitle(@Nullable String str) {
            this.mOptionBundle.putString("com.yalantis.ucrop.UcropToolbarTitleText", str);
        }

        public void setToolbarWidgetColor(@ColorInt int i) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.UcropToolbarWidgetColor", i);
        }

        public void useSourceImageAspectRatio() {
            this.mOptionBundle.putFloat("com.yalantis.ucrop.AspectRatioX", 0.0f);
            this.mOptionBundle.putFloat("com.yalantis.ucrop.AspectRatioY", 0.0f);
        }

        public void withAspectRatio(float f, float f2) {
            this.mOptionBundle.putFloat("com.yalantis.ucrop.AspectRatioX", f);
            this.mOptionBundle.putFloat("com.yalantis.ucrop.AspectRatioY", f2);
        }

        public void withMaxResultSize(int i, int i2) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.MaxSizeX", i);
            this.mOptionBundle.putInt("com.yalantis.ucrop.MaxSizeY", i2);
        }
    }

    private UCropMulti(@NonNull Uri uri, @NonNull Uri uri2) {
        this.mCropOptionsBundle.putParcelable("com.yalantis.ucrop.InputUri", uri);
        this.mCropOptionsBundle.putParcelable("com.yalantis.ucrop.OutputUri", uri2);
    }

    @Nullable
    public static Throwable getError(@NonNull Intent intent) {
        return (Throwable) intent.getSerializableExtra("com.yalantis.ucrop.Error");
    }

    @Nullable
    public static List<CutInfo> getOutput(@NonNull Intent intent) {
        return (List) intent.getSerializableExtra(EXTRA_OUTPUT_URI_LIST);
    }

    public static float getOutputCropAspectRatio(@NonNull Intent intent) {
        return ((Float) intent.getParcelableExtra("com.yalantis.ucrop.CropAspectRatio")).floatValue();
    }

    public static int getOutputImageHeight(@NonNull Intent intent) {
        return intent.getIntExtra("com.yalantis.ucrop.ImageHeight", -1);
    }

    public static int getOutputImageWidth(@NonNull Intent intent) {
        return intent.getIntExtra("com.yalantis.ucrop.ImageWidth", -1);
    }

    public static UCropMulti of(@NonNull Uri uri, @NonNull Uri uri2) {
        return new UCropMulti(uri, uri2);
    }

    public Intent getIntent(@NonNull Context context) {
        this.mCropIntent.setClass(context, PictureMultiCuttingActivity.class);
        this.mCropIntent.putExtras(this.mCropOptionsBundle);
        return this.mCropIntent;
    }

    public void start(@NonNull Activity activity) {
        start(activity, REQUEST_MULTI_CROP);
    }

    public void start(@NonNull Activity activity, int i) {
        activity.startActivityForResult(getIntent(activity), i);
    }

    public void start(@NonNull Context context, @NonNull Fragment fragment) {
        start(context, fragment, REQUEST_MULTI_CROP);
    }

    @TargetApi(11)
    public void start(@NonNull Context context, @NonNull Fragment fragment, int i) {
        fragment.startActivityForResult(getIntent(context), i);
    }

    public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment fragment) {
        start(context, fragment, REQUEST_MULTI_CROP);
    }

    public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment fragment, int i) {
        fragment.startActivityForResult(getIntent(context), i);
    }

    public UCropMulti useSourceImageAspectRatio() {
        this.mCropOptionsBundle.putFloat("com.yalantis.ucrop.AspectRatioX", 0.0f);
        this.mCropOptionsBundle.putFloat("com.yalantis.ucrop.AspectRatioY", 0.0f);
        return this;
    }

    public UCropMulti withAspectRatio(float f, float f2) {
        this.mCropOptionsBundle.putFloat("com.yalantis.ucrop.AspectRatioX", f);
        this.mCropOptionsBundle.putFloat("com.yalantis.ucrop.AspectRatioY", f2);
        return this;
    }

    public UCropMulti withMaxResultSize(@IntRange(from = 100) int i, @IntRange(from = 100) int i2) {
        this.mCropOptionsBundle.putInt("com.yalantis.ucrop.MaxSizeX", i);
        this.mCropOptionsBundle.putInt("com.yalantis.ucrop.MaxSizeY", i2);
        return this;
    }

    public UCropMulti withOptions(@NonNull Options options) {
        this.mCropOptionsBundle.putAll(options.getOptionBundle());
        return this;
    }
}
