package com.luck.picture.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DoubleUtils;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class PictureSelector {
    private final WeakReference<Activity> mActivity;
    private final WeakReference<Fragment> mFragment;

    private PictureSelector(Activity activity) {
        this(activity, null);
    }

    private PictureSelector(Activity activity, Fragment fragment) {
        this.mActivity = new WeakReference<>(activity);
        this.mFragment = new WeakReference<>(fragment);
    }

    private PictureSelector(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    public static PictureSelector create(Activity activity) {
        return new PictureSelector(activity);
    }

    public static PictureSelector create(Fragment fragment) {
        return new PictureSelector(fragment);
    }

    public static List<LocalMedia> obtainMultipleResult(Intent intent) {
        ArrayList arrayList = new ArrayList();
        if (intent == null) {
            return arrayList;
        }
        List<LocalMedia> list = (List) intent.getSerializableExtra(PictureConfig.EXTRA_RESULT_SELECTION);
        return list == null ? new ArrayList() : list;
    }

    public static List<LocalMedia> obtainSelectorList(Bundle bundle) {
        return bundle != null ? (List) bundle.getSerializable(PictureConfig.EXTRA_SELECT_LIST) : new ArrayList();
    }

    public static Intent putIntentResult(List<LocalMedia> list) {
        return new Intent().putExtra(PictureConfig.EXTRA_RESULT_SELECTION, (Serializable) list);
    }

    public static void saveSelectorList(Bundle bundle, List<LocalMedia> list) {
        bundle.putSerializable(PictureConfig.EXTRA_SELECT_LIST, (Serializable) list);
    }

    public void externalPictureAudio(String str) {
        if (DoubleUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), (Class<?>) PicturePlayAudioActivity.class);
        intent.putExtra("audio_path", str);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.a5, 0);
    }

    public void externalPicturePreview(int i, String str, List<LocalMedia> list) {
        if (DoubleUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), (Class<?>) PictureExternalPreviewActivity.class);
        intent.putExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST, (Serializable) list);
        intent.putExtra(PictureConfig.EXTRA_POSITION, i);
        intent.putExtra(PictureConfig.DIRECTORY_PATH, str);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.a5, 0);
    }

    public void externalPicturePreview(int i, List<LocalMedia> list) {
        if (DoubleUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), (Class<?>) PictureExternalPreviewActivity.class);
        intent.putExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST, (Serializable) list);
        intent.putExtra(PictureConfig.EXTRA_POSITION, i);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.a5, 0);
    }

    public void externalPictureVideo(String str) {
        if (DoubleUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), (Class<?>) PictureVideoPlayActivity.class);
        intent.putExtra("video_path", str);
        getActivity().startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public Activity getActivity() {
        return this.mActivity.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public Fragment getFragment() {
        if (this.mFragment != null) {
            return this.mFragment.get();
        }
        return null;
    }

    public PictureSelectionModel openCamera(int i) {
        return new PictureSelectionModel(this, i, true);
    }

    public PictureSelectionModel openGallery(int i) {
        return new PictureSelectionModel(this, i);
    }
}
