package com.luck.picture.lib;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.compress.OnCompressListener;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.dialog.PictureDialog;
import com.luck.picture.lib.entity.EventEntity;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.rxbus2.RxBus;
import com.luck.picture.lib.tools.AttrsUtils;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.DoubleUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropMulti;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PictureBaseActivity extends FragmentActivity {
    protected String cameraPath;
    protected PictureDialog compressDialog;
    protected PictureSelectionConfig config;
    protected PictureDialog dialog;
    protected Context mContext;
    protected boolean numComplete;
    protected String originalPath;
    protected String outputCameraPath;
    protected boolean previewStatusFont;
    protected List<LocalMedia> selectionMedias;
    protected boolean statusFont;

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCompressCallBack(List<LocalMedia> list, List<File> list2) {
        if (list2.size() == list.size()) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String path = list2.get(i).getPath();
                LocalMedia localMedia = list.get(i);
                boolean z = !TextUtils.isEmpty(path) && PictureMimeType.isHttp(path);
                localMedia.setCompressed(!z);
                if (z) {
                    path = "";
                }
                localMedia.setCompressPath(path);
            }
        }
        RxBus.getDefault().post(new EventEntity(PictureConfig.CLOSE_PREVIEW_FLAG));
        onResult(list);
    }

    private void initConfig() {
        this.outputCameraPath = this.config.outputCameraPath;
        this.statusFont = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_statusFontColor);
        this.previewStatusFont = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_preview_statusFontColor);
        this.numComplete = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_style_numComplete);
        this.config.checkNumMode = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_style_checkNumMode);
        this.selectionMedias = this.config.selectionMedias;
        if (this.selectionMedias == null) {
            this.selectionMedias = new ArrayList();
        }
        if (this.config.selectionMode == 1) {
            this.selectionMedias = new ArrayList();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void closeActivity() {
        finish();
        if (this.config.camera) {
            overridePendingTransition(0, R.anim.fade_out);
        } else {
            overridePendingTransition(0, R.anim.a3);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void compressImage(final List<LocalMedia> list) {
        showCompressDialog();
        if (this.config.synOrAsy) {
            Flowable.just(list).observeOn(Schedulers.io()).map(new Function<List<LocalMedia>, List<File>>() { // from class: com.luck.picture.lib.PictureBaseActivity.2
                @Override // io.reactivex.functions.Function
                public List<File> apply(@NonNull List<LocalMedia> list2) throws Exception {
                    List<File> list3 = Luban.with(PictureBaseActivity.this.mContext).setTargetDir(PictureBaseActivity.this.config.compressSavePath).ignoreBy(PictureBaseActivity.this.config.minimumCompressSize).loadLocalMedia(list2).get();
                    return list3 == null ? new ArrayList() : list3;
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<File>>() { // from class: com.luck.picture.lib.PictureBaseActivity.1
                @Override // io.reactivex.functions.Consumer
                public void accept(@NonNull List<File> list2) throws Exception {
                    PictureBaseActivity.this.handleCompressCallBack(list, list2);
                }
            });
        } else {
            Luban.with(this).loadLocalMedia(list).ignoreBy(this.config.minimumCompressSize).setTargetDir(this.config.compressSavePath).setCompressListener(new OnCompressListener() { // from class: com.luck.picture.lib.PictureBaseActivity.3
                @Override // com.luck.picture.lib.compress.OnCompressListener
                public void onError(Throwable th) {
                    RxBus.getDefault().post(new EventEntity(PictureConfig.CLOSE_PREVIEW_FLAG));
                    PictureBaseActivity.this.onResult(list);
                }

                @Override // com.luck.picture.lib.compress.OnCompressListener
                public void onStart() {
                }

                @Override // com.luck.picture.lib.compress.OnCompressListener
                public void onSuccess(List<LocalMedia> list2) {
                    RxBus.getDefault().post(new EventEntity(PictureConfig.CLOSE_PREVIEW_FLAG));
                    PictureBaseActivity.this.onResult(list2);
                }
            }).launch();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createNewFolder(List<LocalMediaFolder> list) {
        if (list.size() == 0) {
            LocalMediaFolder localMediaFolder = new LocalMediaFolder();
            localMediaFolder.setName(getString(this.config.mimeType == PictureMimeType.ofAudio() ? R.string.picture_all_audio : R.string.picture_camera_roll));
            localMediaFolder.setPath("");
            localMediaFolder.setFirstImagePath("");
            list.add(localMediaFolder);
        }
    }

    protected void dismissCompressDialog() {
        try {
            if (isFinishing() || this.compressDialog == null || !this.compressDialog.isShowing()) {
                return;
            }
            this.compressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dismissDialog() {
        try {
            if (this.dialog == null || !this.dialog.isShowing()) {
                return;
            }
            this.dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getAudioFilePathFromUri(Uri uri) {
        try {
            Cursor query = getContentResolver().query(uri, null, null, null, null);
            query.moveToFirst();
            return query.getString(query.getColumnIndex("_data"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LocalMediaFolder getImageFolder(String str, List<LocalMediaFolder> list) {
        File parentFile = new File(str).getParentFile();
        for (LocalMediaFolder localMediaFolder : list) {
            if (localMediaFolder.getName().equals(parentFile.getName())) {
                return localMediaFolder;
            }
        }
        LocalMediaFolder localMediaFolder2 = new LocalMediaFolder();
        localMediaFolder2.setName(parentFile.getName());
        localMediaFolder2.setPath(parentFile.getAbsolutePath());
        localMediaFolder2.setFirstImagePath(str);
        list.add(localMediaFolder2);
        return localMediaFolder2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getLastImageId(boolean z) {
        try {
            Cursor query = getContentResolver().query(z ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data like ?", new String[]{PictureFileUtils.getDCIMCameraPath() + "%"}, "_id DESC");
            if (!query.moveToFirst()) {
                return -1;
            }
            int i = query.getInt(z ? query.getColumnIndex(FileDownloadModel.ID) : query.getColumnIndex(FileDownloadModel.ID));
            int dateDiffer = DateUtils.dateDiffer(query.getLong(z ? query.getColumnIndex("duration") : query.getColumnIndex("date_added")));
            query.close();
            if (dateDiffer <= 30) {
                return i;
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handlerResult(List<LocalMedia> list) {
        if (this.config.isCompress) {
            compressImage(list);
        } else {
            onResult(list);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void isAudio(Intent intent) {
        if (intent == null || this.config.mimeType != PictureMimeType.ofAudio()) {
            return;
        }
        try {
            Uri data = intent.getData();
            PictureFileUtils.copyAudioFile(Build.VERSION.SDK_INT <= 19 ? data.getPath() : getAudioFilePathFromUri(data), this.cameraPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        if (bundle != null) {
            this.config = (PictureSelectionConfig) bundle.getParcelable(PictureConfig.EXTRA_CONFIG);
            this.cameraPath = bundle.getString(PictureConfig.BUNDLE_CAMERA_PATH);
            this.originalPath = bundle.getString(PictureConfig.BUNDLE_ORIGINAL_PATH);
        } else {
            this.config = PictureSelectionConfig.getInstance();
        }
        setTheme(this.config.themeStyleId);
        super.onCreate(bundle);
        this.mContext = this;
        initConfig();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        dismissCompressDialog();
        dismissDialog();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onResult(List<LocalMedia> list) {
        dismissCompressDialog();
        if (this.config.camera && this.config.selectionMode == 2 && this.selectionMedias != null) {
            list.addAll(list.size() > 0 ? list.size() - 1 : 0, this.selectionMedias);
        }
        setResult(-1, PictureSelector.putIntentResult(list));
        closeActivity();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(PictureConfig.BUNDLE_CAMERA_PATH, this.cameraPath);
        bundle.putString(PictureConfig.BUNDLE_ORIGINAL_PATH, this.originalPath);
        bundle.putParcelable(PictureConfig.EXTRA_CONFIG, this.config);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeImage(int i, boolean z) {
        try {
            getContentResolver().delete(z ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_id=?", new String[]{Long.toString(i)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void rotateImage(int i, File file) {
        if (i > 0) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                PictureFileUtils.saveBitmapFile(PictureFileUtils.rotaingImageView(i, BitmapFactory.decodeFile(file.getAbsolutePath(), options)), file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void showCompressDialog() {
        if (isFinishing()) {
            return;
        }
        dismissCompressDialog();
        this.compressDialog = new PictureDialog(this);
        this.compressDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showPleaseDialog() {
        if (isFinishing()) {
            return;
        }
        dismissDialog();
        this.dialog = new PictureDialog(this);
        this.dialog.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showToast(String str) {
        Toast.makeText(this.mContext, str, 1).show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startActivity(Class cls, Bundle bundle) {
        if (DoubleUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startActivity(Class cls, Bundle bundle, int i) {
        if (DoubleUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startCrop(String str) {
        UCrop.Options options = new UCrop.Options();
        int typeValueColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_toolbar_bg);
        int typeValueColor2 = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_status_color);
        int typeValueColor3 = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_title_color);
        options.setToolbarColor(typeValueColor);
        options.setStatusBarColor(typeValueColor2);
        options.setToolbarWidgetColor(typeValueColor3);
        options.setCircleDimmedLayer(this.config.circleDimmedLayer);
        options.setShowCropFrame(this.config.showCropFrame);
        options.setShowCropGrid(this.config.showCropGrid);
        options.setCompressionQuality(this.config.cropCompressQuality);
        options.setHideBottomControls(this.config.hideBottomControls);
        options.setFreeStyleCropEnabled(this.config.freeStyleCropEnabled);
        boolean isHttp = PictureMimeType.isHttp(str);
        String lastImgType = PictureMimeType.getLastImgType(str);
        Uri parse = isHttp ? Uri.parse(str) : Uri.fromFile(new File(str));
        UCrop.of(parse, Uri.fromFile(new File(PictureFileUtils.getDiskCacheDir(this), System.currentTimeMillis() + lastImgType))).withAspectRatio(this.config.aspect_ratio_x, this.config.aspect_ratio_y).withMaxResultSize(this.config.cropWidth, this.config.cropHeight).withOptions(options).start(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startCrop(ArrayList<String> arrayList) {
        UCropMulti.Options options = new UCropMulti.Options();
        int typeValueColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_toolbar_bg);
        int typeValueColor2 = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_status_color);
        int typeValueColor3 = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_title_color);
        options.setToolbarColor(typeValueColor);
        options.setStatusBarColor(typeValueColor2);
        options.setToolbarWidgetColor(typeValueColor3);
        options.setCircleDimmedLayer(this.config.circleDimmedLayer);
        options.setShowCropFrame(this.config.showCropFrame);
        options.setShowCropGrid(this.config.showCropGrid);
        options.setScaleEnabled(this.config.scaleEnabled);
        options.setRotateEnabled(this.config.rotateEnabled);
        options.setHideBottomControls(true);
        options.setCompressionQuality(this.config.cropCompressQuality);
        options.setCutListData(arrayList);
        options.setFreeStyleCropEnabled(this.config.freeStyleCropEnabled);
        String str = arrayList.size() > 0 ? arrayList.get(0) : "";
        boolean isHttp = PictureMimeType.isHttp(str);
        String lastImgType = PictureMimeType.getLastImgType(str);
        Uri parse = isHttp ? Uri.parse(str) : Uri.fromFile(new File(str));
        UCropMulti.of(parse, Uri.fromFile(new File(PictureFileUtils.getDiskCacheDir(this), System.currentTimeMillis() + lastImgType))).withAspectRatio(this.config.aspect_ratio_x, this.config.aspect_ratio_y).withMaxResultSize(this.config.cropWidth, this.config.cropHeight).withOptions(options).start(this);
    }
}
