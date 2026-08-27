package com.luck.picture.lib;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.luck.picture.lib.adapter.PictureAlbumDirectoryAdapter;
import com.luck.picture.lib.adapter.PictureImageGridAdapter;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.dialog.CustomDialog;
import com.luck.picture.lib.entity.EventEntity;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.observable.ImagesObservable;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.rxbus2.RxBus;
import com.luck.picture.lib.rxbus2.Subscribe;
import com.luck.picture.lib.rxbus2.ThreadMode;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.DoubleUtils;
import com.luck.picture.lib.tools.LightStatusBarUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.StringUtils;
import com.luck.picture.lib.widget.FolderPopWindow;
import com.luck.picture.lib.widget.PhotoPopupWindow;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropMulti;
import com.yalantis.ucrop.model.CutInfo;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class PictureSelectorActivity extends PictureBaseActivity implements View.OnClickListener, PictureAlbumDirectoryAdapter.OnItemClickListener, PictureImageGridAdapter.OnPhotoSelectChangedListener, PhotoPopupWindow.OnItemClickListener {
    private static final int DISMISS_DIALOG = 1;
    private static final int SHOW_DIALOG = 0;
    private static final int STATUSBAR = 2;
    private static final String TAG = "PictureSelectorActivity";
    private PictureImageGridAdapter adapter;
    private CustomDialog audioDialog;
    private int audioH;
    private FolderPopWindow folderWindow;
    private LinearLayout id_ll_ok;
    private LocalMediaLoader mediaLoader;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;
    private TextView picture_id_preview;
    private ImageView picture_left_back;
    private RecyclerView picture_recycler;
    private TextView picture_right;
    private TextView picture_title;
    private TextView picture_tv_img_num;
    private TextView picture_tv_ok;
    private PhotoPopupWindow popupWindow;
    private RelativeLayout rl_bottom;
    private RelativeLayout rl_picture_title;
    private RxPermissions rxPermissions;
    private TextView tv_PlayPause;
    private TextView tv_Quit;
    private TextView tv_Stop;
    private TextView tv_empty;
    private TextView tv_musicStatus;
    private TextView tv_musicTime;
    private TextView tv_musicTotal;
    private List<LocalMedia> images = new ArrayList();
    private List<LocalMediaFolder> foldersList = new ArrayList();
    private Animation animation = null;
    private boolean anim = false;
    private boolean isPlayAudio = false;
    private Handler mHandler = new Handler() { // from class: com.luck.picture.lib.PictureSelectorActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 0:
                    PictureSelectorActivity.this.showPleaseDialog();
                    return;
                case 1:
                    PictureSelectorActivity.this.dismissDialog();
                    return;
                case 2:
                    LightStatusBarUtils.setLightStatusBar(PictureSelectorActivity.this, PictureSelectorActivity.this.statusFont);
                    return;
                default:
                    return;
            }
        }
    };
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() { // from class: com.luck.picture.lib.PictureSelectorActivity.9
        @Override // java.lang.Runnable
        public void run() {
            try {
                if (PictureSelectorActivity.this.mediaPlayer != null) {
                    PictureSelectorActivity.this.tv_musicTime.setText(DateUtils.timeParse(PictureSelectorActivity.this.mediaPlayer.getCurrentPosition()));
                    PictureSelectorActivity.this.musicSeekBar.setProgress(PictureSelectorActivity.this.mediaPlayer.getCurrentPosition());
                    PictureSelectorActivity.this.musicSeekBar.setMax(PictureSelectorActivity.this.mediaPlayer.getDuration());
                    PictureSelectorActivity.this.tv_musicTotal.setText(DateUtils.timeParse(PictureSelectorActivity.this.mediaPlayer.getDuration()));
                    PictureSelectorActivity.this.handler.postDelayed(PictureSelectorActivity.this.runnable, 200L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /* loaded from: classes.dex */
    public class audioOnClick implements View.OnClickListener {
        private String path;

        public audioOnClick(String str) {
            this.path = str;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tv_PlayPause) {
                PictureSelectorActivity.this.playAudio();
            }
            if (id == R.id.tv_Stop) {
                PictureSelectorActivity.this.tv_musicStatus.setText(PictureSelectorActivity.this.getString(R.string.picture_stop_audio));
                PictureSelectorActivity.this.tv_PlayPause.setText(PictureSelectorActivity.this.getString(R.string.picture_play_audio));
                PictureSelectorActivity.this.stop(this.path);
            }
            if (id == R.id.tv_Quit) {
                PictureSelectorActivity.this.handler.removeCallbacks(PictureSelectorActivity.this.runnable);
                new Handler().postDelayed(new Runnable() { // from class: com.luck.picture.lib.PictureSelectorActivity.audioOnClick.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PictureSelectorActivity.this.stop(audioOnClick.this.path);
                    }
                }, 30L);
                try {
                    if (PictureSelectorActivity.this.audioDialog == null || !PictureSelectorActivity.this.audioDialog.isShowing()) {
                        return;
                    }
                    PictureSelectorActivity.this.audioDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void audioDialog(final String str) {
        this.audioDialog = new CustomDialog(this.mContext, -1, this.audioH, R.layout.picture_audio_dialog, R.style.Theme_dialog);
        this.audioDialog.getWindow().setWindowAnimations(R.style.Dialog_Audio_StyleAnim);
        this.tv_musicStatus = (TextView) this.audioDialog.findViewById(R.id.tv_musicStatus);
        this.tv_musicTime = (TextView) this.audioDialog.findViewById(R.id.tv_musicTime);
        this.musicSeekBar = (SeekBar) this.audioDialog.findViewById(R.id.musicSeekBar);
        this.tv_musicTotal = (TextView) this.audioDialog.findViewById(R.id.tv_musicTotal);
        this.tv_PlayPause = (TextView) this.audioDialog.findViewById(R.id.tv_PlayPause);
        this.tv_Stop = (TextView) this.audioDialog.findViewById(R.id.tv_Stop);
        this.tv_Quit = (TextView) this.audioDialog.findViewById(R.id.tv_Quit);
        this.handler.postDelayed(new Runnable() { // from class: com.luck.picture.lib.PictureSelectorActivity.6
            @Override // java.lang.Runnable
            public void run() {
                PictureSelectorActivity.this.initPlayer(str);
            }
        }, 30L);
        this.tv_PlayPause.setOnClickListener(new audioOnClick(str));
        this.tv_Stop.setOnClickListener(new audioOnClick(str));
        this.tv_Quit.setOnClickListener(new audioOnClick(str));
        this.musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.luck.picture.lib.PictureSelectorActivity.7
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    PictureSelectorActivity.this.mediaPlayer.seekTo(i);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.audioDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.luck.picture.lib.PictureSelectorActivity.8
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                PictureSelectorActivity.this.handler.removeCallbacks(PictureSelectorActivity.this.runnable);
                new Handler().postDelayed(new Runnable() { // from class: com.luck.picture.lib.PictureSelectorActivity.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PictureSelectorActivity.this.stop(str);
                    }
                }, 30L);
                try {
                    if (PictureSelectorActivity.this.audioDialog == null || !PictureSelectorActivity.this.audioDialog.isShowing()) {
                        return;
                    }
                    PictureSelectorActivity.this.audioDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.handler.post(this.runnable);
        this.audioDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPlayer(String str) {
        this.mediaPlayer = new MediaPlayer();
        try {
            this.mediaPlayer.setDataSource(str);
            this.mediaPlayer.prepare();
            this.mediaPlayer.setLooping(true);
            playAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(Bundle bundle) {
        this.rl_picture_title = (RelativeLayout) findViewById(R.id.rl_picture_title);
        this.picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        this.picture_title = (TextView) findViewById(R.id.picture_title);
        this.picture_right = (TextView) findViewById(R.id.picture_right);
        this.picture_tv_ok = (TextView) findViewById(R.id.picture_tv_ok);
        this.picture_id_preview = (TextView) findViewById(R.id.picture_id_preview);
        this.picture_tv_img_num = (TextView) findViewById(R.id.picture_tv_img_num);
        this.picture_recycler = (RecyclerView) findViewById(R.id.picture_recycler);
        this.rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        this.id_ll_ok = (LinearLayout) findViewById(R.id.id_ll_ok);
        this.tv_empty = (TextView) findViewById(R.id.tv_empty);
        this.rl_bottom.setVisibility(this.config.selectionMode == 1 ? 8 : 0);
        isNumComplete(this.numComplete);
        if (this.config.mimeType == PictureMimeType.ofAll()) {
            this.popupWindow = new PhotoPopupWindow(this);
            this.popupWindow.setOnItemClickListener(this);
        }
        this.picture_id_preview.setOnClickListener(this);
        if (this.config.mimeType == PictureMimeType.ofAudio()) {
            this.picture_id_preview.setVisibility(8);
            this.audioH = ScreenUtils.getScreenHeight(this.mContext) + ScreenUtils.getStatusBarHeight(this.mContext);
        } else {
            this.picture_id_preview.setVisibility(this.config.mimeType != 2 ? 0 : 8);
        }
        this.picture_left_back.setOnClickListener(this);
        this.picture_right.setOnClickListener(this);
        this.id_ll_ok.setOnClickListener(this);
        this.picture_title.setOnClickListener(this);
        this.picture_title.setText(this.config.mimeType == PictureMimeType.ofAudio() ? getString(R.string.picture_all_audio) : getString(R.string.picture_camera_roll));
        this.folderWindow = new FolderPopWindow(this, this.config.mimeType);
        this.folderWindow.setPictureTitleView(this.picture_title);
        this.folderWindow.setOnItemClickListener(this);
        this.picture_recycler.setHasFixedSize(true);
        this.picture_recycler.addItemDecoration(new GridSpacingItemDecoration(this.config.imageSpanCount, ScreenUtils.dip2px(this, 2.0f), false));
        this.picture_recycler.setLayoutManager(new GridLayoutManager(this, this.config.imageSpanCount));
        ((SimpleItemAnimator) this.picture_recycler.getItemAnimator()).setSupportsChangeAnimations(false);
        this.mediaLoader = new LocalMediaLoader(this, this.config.mimeType, this.config.isGif, this.config.videoMaxSecond, this.config.videoMinSecond);
        this.rxPermissions.request("android.permission.READ_EXTERNAL_STORAGE").subscribe(new Observer<Boolean>() { // from class: com.luck.picture.lib.PictureSelectorActivity.3
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
            }

            @Override // io.reactivex.Observer
            public void onNext(Boolean bool) {
                if (!bool.booleanValue()) {
                    PictureSelectorActivity.this.showToast(PictureSelectorActivity.this.getString(R.string.picture_jurisdiction));
                } else {
                    PictureSelectorActivity.this.mHandler.sendEmptyMessage(0);
                    PictureSelectorActivity.this.readLocalMedia();
                }
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }
        });
        this.tv_empty.setText(this.config.mimeType == PictureMimeType.ofAudio() ? getString(R.string.picture_audio_empty) : getString(R.string.picture_empty));
        StringUtils.tempTextFont(this.tv_empty, this.config.mimeType);
        if (bundle != null) {
            this.selectionMedias = PictureSelector.obtainSelectorList(bundle);
        }
        this.adapter = new PictureImageGridAdapter(this.mContext, this.config);
        this.adapter.setOnPhotoSelectChangedListener(this);
        this.adapter.bindSelectImages(this.selectionMedias);
        this.picture_recycler.setAdapter(this.adapter);
        String trim = this.picture_title.getText().toString().trim();
        if (this.config.isCamera) {
            this.config.isCamera = StringUtils.isCamera(trim);
        }
    }

    private void isNumComplete(boolean z) {
        this.picture_tv_ok.setText(z ? getString(R.string.picture_done_front_num, new Object[]{0, Integer.valueOf(this.config.maxSelectNum)}) : getString(R.string.picture_please_select));
        if (!z) {
            this.animation = AnimationUtils.loadAnimation(this, R.anim.modal_in);
        }
        this.animation = z ? null : AnimationUtils.loadAnimation(this, R.anim.modal_in);
    }

    private void manualSaveFolder(LocalMedia localMedia) {
        try {
            createNewFolder(this.foldersList);
            LocalMediaFolder imageFolder = getImageFolder(localMedia.getPath(), this.foldersList);
            LocalMediaFolder localMediaFolder = this.foldersList.size() > 0 ? this.foldersList.get(0) : null;
            if (localMediaFolder == null || imageFolder == null) {
                return;
            }
            localMediaFolder.setFirstImagePath(localMedia.getPath());
            localMediaFolder.setImages(this.images);
            localMediaFolder.setImageNum(localMediaFolder.getImageNum() + 1);
            imageFolder.setImageNum(imageFolder.getImageNum() + 1);
            imageFolder.getImages().add(0, localMedia);
            imageFolder.setFirstImagePath(this.cameraPath);
            this.folderWindow.bindFolder(this.foldersList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Uri parUri(File file) {
        return Build.VERSION.SDK_INT >= 23 ? FileProvider.getUriForFile(this.mContext, getPackageName() + ".provider", file) : Uri.fromFile(file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playAudio() {
        if (this.mediaPlayer != null) {
            this.musicSeekBar.setProgress(this.mediaPlayer.getCurrentPosition());
            this.musicSeekBar.setMax(this.mediaPlayer.getDuration());
        }
        if (this.tv_PlayPause.getText().toString().equals(getString(R.string.picture_play_audio))) {
            this.tv_PlayPause.setText(getString(R.string.picture_pause_audio));
            this.tv_musicStatus.setText(getString(R.string.picture_play_audio));
            playOrPause();
        } else {
            this.tv_PlayPause.setText(getString(R.string.picture_play_audio));
            this.tv_musicStatus.setText(getString(R.string.picture_pause_audio));
            playOrPause();
        }
        if (this.isPlayAudio) {
            return;
        }
        this.handler.post(this.runnable);
        this.isPlayAudio = true;
    }

    public void changeImageNumber(List<LocalMedia> list) {
        String pictureType = list.size() > 0 ? list.get(0).getPictureType() : "";
        int i = 8;
        if (this.config.mimeType == PictureMimeType.ofAudio()) {
            this.picture_id_preview.setVisibility(8);
        } else {
            boolean isVideo = PictureMimeType.isVideo(pictureType);
            boolean z = this.config.mimeType == 2;
            TextView textView = this.picture_id_preview;
            if (!isVideo && !z) {
                i = 0;
            }
            textView.setVisibility(i);
        }
        if (!(list.size() != 0)) {
            this.id_ll_ok.setEnabled(false);
            this.picture_id_preview.setEnabled(false);
            this.picture_id_preview.setSelected(false);
            this.picture_tv_ok.setSelected(false);
            if (this.numComplete) {
                this.picture_tv_ok.setText(getString(R.string.picture_done_front_num, new Object[]{0, Integer.valueOf(this.config.maxSelectNum)}));
                return;
            } else {
                this.picture_tv_img_num.setVisibility(4);
                this.picture_tv_ok.setText(getString(R.string.picture_please_select));
                return;
            }
        }
        this.id_ll_ok.setEnabled(true);
        this.picture_id_preview.setEnabled(true);
        this.picture_id_preview.setSelected(true);
        this.picture_tv_ok.setSelected(true);
        if (this.numComplete) {
            this.picture_tv_ok.setText(getString(R.string.picture_done_front_num, new Object[]{Integer.valueOf(list.size()), Integer.valueOf(this.config.maxSelectNum)}));
            return;
        }
        if (!this.anim) {
            this.picture_tv_img_num.startAnimation(this.animation);
        }
        this.picture_tv_img_num.setVisibility(0);
        this.picture_tv_img_num.setText(list.size() + "");
        this.picture_tv_ok.setText(getString(R.string.picture_completed));
        this.anim = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventEntity eventEntity) {
        int i = eventEntity.what;
        if (i != 2771) {
            if (i != 2774) {
                return;
            }
            List<LocalMedia> list = eventEntity.medias;
            this.anim = list.size() > 0;
            int i2 = eventEntity.position;
            this.adapter.bindSelectImages(list);
            this.adapter.notifyItemChanged(i2);
            return;
        }
        List<LocalMedia> list2 = eventEntity.medias;
        if (list2.size() > 0) {
            String pictureType = list2.get(0).getPictureType();
            if (this.config.isCompress && pictureType.startsWith(PictureConfig.IMAGE)) {
                compressImage(list2);
            } else {
                onResult(list2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        int i3;
        String createVideoType;
        int lastImageId;
        if (i2 != -1) {
            if (i2 == 0) {
                if (this.config.camera) {
                    closeActivity();
                    return;
                }
                return;
            } else {
                if (i2 == 96) {
                    showToast(((Throwable) intent.getSerializableExtra("com.yalantis.ucrop.Error")).getMessage());
                    return;
                }
                return;
            }
        }
        ArrayList arrayList = new ArrayList();
        if (i == 69) {
            String path = UCrop.getOutput(intent).getPath();
            LocalMedia localMedia = new LocalMedia(this.originalPath, 0L, false, 0, 0, this.config.mimeType);
            localMedia.setCutPath(path);
            localMedia.setCut(true);
            String createImageType = PictureMimeType.createImageType(path);
            localMedia.setPictureType(createImageType);
            arrayList.add(localMedia);
            DebugUtil.i(TAG, "cut createImageType:" + createImageType);
            handlerResult(arrayList);
            return;
        }
        if (i == 609) {
            for (CutInfo cutInfo : UCropMulti.getOutput(intent)) {
                LocalMedia localMedia2 = new LocalMedia();
                String createImageType2 = PictureMimeType.createImageType(cutInfo.getPath());
                localMedia2.setCut(true);
                localMedia2.setPath(cutInfo.getPath());
                localMedia2.setCutPath(cutInfo.getCutPath());
                localMedia2.setPictureType(createImageType2);
                localMedia2.setMimeType(this.config.mimeType);
                arrayList.add(localMedia2);
            }
            handlerResult(arrayList);
            return;
        }
        if (i != 909) {
            return;
        }
        isAudio(intent);
        File file = new File(this.cameraPath);
        sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        String fileToType = PictureMimeType.fileToType(file);
        DebugUtil.i(TAG, "camera result:" + fileToType);
        if (this.config.mimeType != PictureMimeType.ofAudio()) {
            rotateImage(PictureFileUtils.readPictureDegree(file.getAbsolutePath()), file);
        }
        LocalMedia localMedia3 = new LocalMedia();
        localMedia3.setPath(this.cameraPath);
        boolean startsWith = fileToType.startsWith(PictureConfig.VIDEO);
        int localVideoDuration = startsWith ? PictureMimeType.getLocalVideoDuration(this.cameraPath) : 0;
        if (this.config.mimeType == PictureMimeType.ofAudio()) {
            createVideoType = "audio/mpeg";
            i3 = PictureMimeType.getLocalVideoDuration(this.cameraPath);
        } else {
            i3 = localVideoDuration;
            createVideoType = startsWith ? PictureMimeType.createVideoType(this.cameraPath) : PictureMimeType.createImageType(this.cameraPath);
        }
        localMedia3.setPictureType(createVideoType);
        localMedia3.setDuration(i3);
        localMedia3.setMimeType(this.config.mimeType);
        if (this.config.selectionMode == 1 || this.config.camera) {
            boolean startsWith2 = fileToType.startsWith(PictureConfig.IMAGE);
            if (this.config.enableCrop && startsWith2) {
                this.originalPath = this.cameraPath;
                startCrop(this.cameraPath);
            } else if (this.config.isCompress && startsWith2) {
                arrayList.add(localMedia3);
                compressImage(arrayList);
                if (this.adapter != null) {
                    this.images.add(0, localMedia3);
                    this.adapter.notifyDataSetChanged();
                }
            } else {
                arrayList.add(localMedia3);
                onResult(arrayList);
            }
        } else {
            this.images.add(0, localMedia3);
            if (this.adapter != null) {
                List<LocalMedia> selectedImages = this.adapter.getSelectedImages();
                if (selectedImages.size() < this.config.maxSelectNum) {
                    if ((PictureMimeType.mimeToEqual(selectedImages.size() > 0 ? selectedImages.get(0).getPictureType() : "", localMedia3.getPictureType()) || selectedImages.size() == 0) && selectedImages.size() < this.config.maxSelectNum) {
                        selectedImages.add(localMedia3);
                        this.adapter.bindSelectImages(selectedImages);
                    }
                    this.adapter.notifyDataSetChanged();
                }
            }
        }
        if (this.adapter != null) {
            manualSaveFolder(localMedia3);
            this.tv_empty.setVisibility(this.images.size() > 0 ? 4 : 0);
        }
        if (this.config.mimeType == PictureMimeType.ofAudio() || (lastImageId = getLastImageId(startsWith)) == -1) {
            return;
        }
        removeImage(lastImageId, startsWith);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    @Override // com.luck.picture.lib.adapter.PictureImageGridAdapter.OnPhotoSelectChangedListener
    public void onChange(List<LocalMedia> list) {
        changeImageNumber(list);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.picture_left_back || id == R.id.picture_right) {
            if (this.folderWindow.isShowing()) {
                this.folderWindow.dismiss();
            } else {
                closeActivity();
            }
        }
        if (id == R.id.picture_title) {
            if (this.folderWindow.isShowing()) {
                this.folderWindow.dismiss();
            } else if (this.images != null && this.images.size() > 0) {
                this.folderWindow.showAsDropDown(this.rl_picture_title);
                this.folderWindow.notifyDataCheckedStatus(this.adapter.getSelectedImages());
            }
        }
        if (id == R.id.picture_id_preview) {
            List<LocalMedia> selectedImages = this.adapter.getSelectedImages();
            ArrayList arrayList = new ArrayList();
            Iterator<LocalMedia> it = selectedImages.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(PictureConfig.EXTRA_PREVIEW_SELECT_LIST, arrayList);
            bundle.putSerializable(PictureConfig.EXTRA_SELECT_LIST, (Serializable) selectedImages);
            bundle.putBoolean(PictureConfig.EXTRA_BOTTOM_PREVIEW, true);
            startActivity(PicturePreviewActivity.class, bundle, UCropMulti.REQUEST_MULTI_CROP);
            overridePendingTransition(R.anim.a5, 0);
        }
        if (id == R.id.id_ll_ok) {
            List<LocalMedia> selectedImages2 = this.adapter.getSelectedImages();
            String pictureType = selectedImages2.size() > 0 ? selectedImages2.get(0).getPictureType() : "";
            int size = selectedImages2.size();
            boolean startsWith = pictureType.startsWith(PictureConfig.IMAGE);
            if (this.config.minSelectNum > 0 && this.config.selectionMode == 2 && size < this.config.minSelectNum) {
                showToast(startsWith ? getString(R.string.picture_min_img_num, new Object[]{Integer.valueOf(this.config.minSelectNum)}) : getString(R.string.picture_min_video_num, new Object[]{Integer.valueOf(this.config.minSelectNum)}));
                return;
            }
            if (this.config.enableCrop && startsWith) {
                ArrayList<String> arrayList2 = new ArrayList<>();
                Iterator<LocalMedia> it2 = selectedImages2.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(it2.next().getPath());
                }
                startCrop(arrayList2);
                return;
            }
            if (this.config.isCompress && startsWith) {
                compressImage(selectedImages2);
            } else {
                onResult(selectedImages2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().register(this);
        }
        this.rxPermissions = new RxPermissions(this);
        this.mHandler.sendEmptyMessage(2);
        if (!this.config.camera) {
            setContentView(R.layout.picture_selector);
            initView(bundle);
            return;
        }
        setTheme(R.style.activity_Theme_Transparent);
        if (bundle == null) {
            this.rxPermissions.request("android.permission.READ_EXTERNAL_STORAGE").subscribe(new Observer<Boolean>() { // from class: com.luck.picture.lib.PictureSelectorActivity.2
                @Override // io.reactivex.Observer
                public void onComplete() {
                }

                @Override // io.reactivex.Observer
                public void onError(Throwable th) {
                }

                @Override // io.reactivex.Observer
                public void onNext(Boolean bool) {
                    if (bool.booleanValue()) {
                        PictureSelectorActivity.this.onTakePhoto();
                    } else {
                        PictureSelectorActivity.this.showToast(PictureSelectorActivity.this.getString(R.string.picture_camera));
                        PictureSelectorActivity.this.closeActivity();
                    }
                }

                @Override // io.reactivex.Observer
                public void onSubscribe(Disposable disposable) {
                }
            });
        }
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.picture_empty);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().unregister(this);
        }
        ImagesObservable.getInstance().clearLocalMedia();
        if (this.animation != null) {
            this.animation.cancel();
            this.animation = null;
        }
        if (this.mediaPlayer == null || this.handler == null) {
            return;
        }
        this.handler.removeCallbacks(this.runnable);
        this.mediaPlayer.release();
        this.mediaPlayer = null;
    }

    @Override // com.luck.picture.lib.widget.PhotoPopupWindow.OnItemClickListener
    public void onItemClick(int i) {
        switch (i) {
            case 0:
                startOpenCamera();
                return;
            case 1:
                startOpenCameraVideo();
                return;
            default:
                return;
        }
    }

    @Override // com.luck.picture.lib.adapter.PictureAlbumDirectoryAdapter.OnItemClickListener
    public void onItemClick(String str, List<LocalMedia> list) {
        boolean isCamera = StringUtils.isCamera(str);
        if (!this.config.isCamera) {
            isCamera = false;
        }
        this.adapter.setShowCamera(isCamera);
        this.picture_title.setText(str);
        this.adapter.bindImagesData(list);
        this.folderWindow.dismiss();
    }

    @Override // com.luck.picture.lib.adapter.PictureImageGridAdapter.OnPhotoSelectChangedListener
    public void onPictureClick(LocalMedia localMedia, int i) {
        startPreview(this.adapter.getImages(), i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.adapter != null) {
            PictureSelector.saveSelectorList(bundle, this.adapter.getSelectedImages());
        }
    }

    @Override // com.luck.picture.lib.adapter.PictureImageGridAdapter.OnPhotoSelectChangedListener
    public void onTakePhoto() {
        this.rxPermissions.request("android.permission.CAMERA").subscribe(new Observer<Boolean>() { // from class: com.luck.picture.lib.PictureSelectorActivity.10
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
            }

            @Override // io.reactivex.Observer
            public void onNext(Boolean bool) {
                if (bool.booleanValue()) {
                    PictureSelectorActivity.this.startCamera();
                    return;
                }
                PictureSelectorActivity.this.showToast(PictureSelectorActivity.this.getString(R.string.picture_camera));
                if (PictureSelectorActivity.this.config.camera) {
                    PictureSelectorActivity.this.closeActivity();
                }
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }
        });
    }

    public void playOrPause() {
        try {
            if (this.mediaPlayer != null) {
                if (this.mediaPlayer.isPlaying()) {
                    this.mediaPlayer.pause();
                } else {
                    this.mediaPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void readLocalMedia() {
        this.mediaLoader.loadAllMedia(new LocalMediaLoader.LocalMediaLoadListener() { // from class: com.luck.picture.lib.PictureSelectorActivity.4
            @Override // com.luck.picture.lib.model.LocalMediaLoader.LocalMediaLoadListener
            public void loadComplete(List<LocalMediaFolder> list) {
                DebugUtil.i("loadComplete:" + list.size());
                if (list.size() > 0) {
                    PictureSelectorActivity.this.foldersList = list;
                    LocalMediaFolder localMediaFolder = list.get(0);
                    localMediaFolder.setChecked(true);
                    List<LocalMedia> images = localMediaFolder.getImages();
                    if (images.size() >= PictureSelectorActivity.this.images.size()) {
                        PictureSelectorActivity.this.images = images;
                        PictureSelectorActivity.this.folderWindow.bindFolder(list);
                    }
                }
                if (PictureSelectorActivity.this.adapter != null) {
                    if (PictureSelectorActivity.this.images == null) {
                        PictureSelectorActivity.this.images = new ArrayList();
                    }
                    PictureSelectorActivity.this.adapter.bindImagesData(PictureSelectorActivity.this.images);
                    PictureSelectorActivity.this.tv_empty.setVisibility(PictureSelectorActivity.this.images.size() > 0 ? 4 : 0);
                }
                PictureSelectorActivity.this.mHandler.sendEmptyMessage(1);
            }
        });
    }

    public void startCamera() {
        if (!DoubleUtils.isFastDoubleClick() || this.config.camera) {
            switch (this.config.mimeType) {
                case 0:
                    if (this.popupWindow == null) {
                        startOpenCamera();
                        return;
                    }
                    if (this.popupWindow.isShowing()) {
                        this.popupWindow.dismiss();
                    }
                    this.popupWindow.showAsDropDown(this.rl_picture_title);
                    return;
                case 1:
                    startOpenCamera();
                    return;
                case 2:
                    startOpenCameraVideo();
                    return;
                case 3:
                    startOpenCameraAudio();
                    return;
                default:
                    return;
            }
        }
    }

    public void startOpenCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(getPackageManager()) != null) {
            File createCameraFile = PictureFileUtils.createCameraFile(this, this.config.mimeType == 0 ? 1 : this.config.mimeType, this.outputCameraPath, this.config.suffixType);
            this.cameraPath = createCameraFile.getAbsolutePath();
            intent.putExtra("output", parUri(createCameraFile));
            startActivityForResult(intent, PictureConfig.REQUEST_CAMERA);
        }
    }

    public void startOpenCameraAudio() {
        this.rxPermissions.request("android.permission.RECORD_AUDIO").subscribe(new Observer<Boolean>() { // from class: com.luck.picture.lib.PictureSelectorActivity.5
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
            }

            @Override // io.reactivex.Observer
            public void onNext(Boolean bool) {
                if (!bool.booleanValue()) {
                    PictureSelectorActivity.this.showToast(PictureSelectorActivity.this.getString(R.string.picture_audio));
                    return;
                }
                Intent intent = new Intent("android.provider.MediaStore.RECORD_SOUND");
                if (intent.resolveActivity(PictureSelectorActivity.this.getPackageManager()) != null) {
                    File createCameraFile = PictureFileUtils.createCameraFile(PictureSelectorActivity.this, PictureSelectorActivity.this.config.mimeType, PictureSelectorActivity.this.outputCameraPath, PictureSelectorActivity.this.config.suffixType);
                    PictureSelectorActivity.this.cameraPath = createCameraFile.getAbsolutePath();
                    intent.putExtra("output", PictureSelectorActivity.this.parUri(createCameraFile));
                    PictureSelectorActivity.this.startActivityForResult(intent, PictureConfig.REQUEST_CAMERA);
                }
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }
        });
    }

    public void startOpenCameraVideo() {
        Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
        if (intent.resolveActivity(getPackageManager()) != null) {
            File createCameraFile = PictureFileUtils.createCameraFile(this, this.config.mimeType == 0 ? 2 : this.config.mimeType, this.outputCameraPath, this.config.suffixType);
            this.cameraPath = createCameraFile.getAbsolutePath();
            Uri parUri = parUri(createCameraFile);
            DebugUtil.i(TAG, "video second:" + this.config.recordVideoSecond);
            intent.putExtra("output", parUri);
            intent.putExtra("android.intent.extra.durationLimit", this.config.recordVideoSecond);
            intent.putExtra("android.intent.extra.videoQuality", this.config.videoQuality);
            startActivityForResult(intent, PictureConfig.REQUEST_CAMERA);
        }
    }

    public void startPreview(List<LocalMedia> list, int i) {
        LocalMedia localMedia = list.get(i);
        String pictureType = localMedia.getPictureType();
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        int isPictureType = PictureMimeType.isPictureType(pictureType);
        DebugUtil.i(TAG, "mediaType:" + isPictureType);
        switch (isPictureType) {
            case 1:
                if (this.config.selectionMode != 1) {
                    List<LocalMedia> selectedImages = this.adapter.getSelectedImages();
                    ImagesObservable.getInstance().saveLocalMedia(list);
                    bundle.putSerializable(PictureConfig.EXTRA_SELECT_LIST, (Serializable) selectedImages);
                    bundle.putInt(PictureConfig.EXTRA_POSITION, i);
                    startActivity(PicturePreviewActivity.class, bundle, UCropMulti.REQUEST_MULTI_CROP);
                    overridePendingTransition(R.anim.a5, 0);
                    return;
                }
                if (!this.config.enableCrop) {
                    arrayList.add(localMedia);
                    handlerResult(arrayList);
                    return;
                }
                this.originalPath = localMedia.getPath();
                if (!PictureMimeType.isGif(pictureType)) {
                    startCrop(this.originalPath);
                    return;
                } else {
                    arrayList.add(localMedia);
                    handlerResult(arrayList);
                    return;
                }
            case 2:
                if (this.config.selectionMode == 1) {
                    arrayList.add(localMedia);
                    onResult(arrayList);
                    return;
                } else {
                    bundle.putString("video_path", localMedia.getPath());
                    startActivity(PictureVideoPlayActivity.class, bundle);
                    return;
                }
            case 3:
                if (this.config.selectionMode != 1) {
                    audioDialog(localMedia.getPath());
                    return;
                } else {
                    arrayList.add(localMedia);
                    onResult(arrayList);
                    return;
                }
            default:
                return;
        }
    }

    public void stop(String str) {
        if (this.mediaPlayer != null) {
            try {
                this.mediaPlayer.stop();
                this.mediaPlayer.reset();
                this.mediaPlayer.setDataSource(str);
                this.mediaPlayer.prepare();
                this.mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
