package com.luck.picture.lib;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.autonavi.amap.mapcore.tools.GlMapUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.dialog.CustomDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.photoview.OnViewTapListener;
import com.luck.picture.lib.photoview.PhotoView;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.widget.PreviewViewPager;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class PictureExternalPreviewActivity extends PictureBaseActivity implements View.OnClickListener {
    private SimpleFragmentAdapter adapter;
    private String directory_path;
    private LayoutInflater inflater;
    private ImageButton left_back;
    private loadDataThread loadDataThread;
    private RxPermissions rxPermissions;
    private TextView tv_title;
    private PreviewViewPager viewPager;
    private List<LocalMedia> images = new ArrayList();
    private int position = 0;
    private Handler handler = new Handler() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.4
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 200) {
                return;
            }
            String str = (String) message.obj;
            PictureExternalPreviewActivity.this.showToast(PictureExternalPreviewActivity.this.getString(R.string.picture_save_success) + "\n" + str);
            PictureExternalPreviewActivity.this.dismissDialog();
        }
    };

    /* loaded from: classes.dex */
    public class SimpleFragmentAdapter extends PagerAdapter {
        public SimpleFragmentAdapter() {
        }

        @Override // android.support.v4.view.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return PictureExternalPreviewActivity.this.images.size();
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            View inflate = PictureExternalPreviewActivity.this.inflater.inflate(R.layout.picture_image_preview, viewGroup, false);
            final PhotoView photoView = (PhotoView) inflate.findViewById(R.id.preview_image);
            final SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) inflate.findViewById(R.id.longImg);
            LocalMedia localMedia = (LocalMedia) PictureExternalPreviewActivity.this.images.get(i);
            if (localMedia != null) {
                String pictureType = localMedia.getPictureType();
                final String compressPath = (!localMedia.isCut() || localMedia.isCompressed()) ? (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) ? localMedia.getCompressPath() : localMedia.getPath() : localMedia.getCutPath();
                if (PictureMimeType.isHttp(compressPath)) {
                    PictureExternalPreviewActivity.this.showPleaseDialog();
                }
                boolean isGif = PictureMimeType.isGif(pictureType);
                final boolean isLongImg = PictureMimeType.isLongImg(localMedia);
                int i2 = 8;
                photoView.setVisibility((!isLongImg || isGif) ? 0 : 8);
                if (isLongImg && !isGif) {
                    i2 = 0;
                }
                subsamplingScaleImageView.setVisibility(i2);
                if (!isGif || localMedia.isCompressed()) {
                    Glide.with((FragmentActivity) PictureExternalPreviewActivity.this).asBitmap().load(compressPath).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into((RequestBuilder<Bitmap>) new SimpleTarget<Bitmap>(GlMapUtil.DEVICE_DISPLAY_DPI_XHIGH, GLMapStaticValue.ANIMATION_MOVE_TIME) { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.SimpleFragmentAdapter.2
                        @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
                        public void onLoadFailed(@Nullable Drawable drawable) {
                            super.onLoadFailed(drawable);
                            PictureExternalPreviewActivity.this.dismissDialog();
                        }

                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            PictureExternalPreviewActivity.this.dismissDialog();
                            if (isLongImg) {
                                PictureExternalPreviewActivity.this.displayLongPic(bitmap, subsamplingScaleImageView);
                                return;
                            }
                            if (bitmap.getWidth() > 480 && bitmap.getHeight() > 1080) {
                                photoView.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                            photoView.setImageBitmap(bitmap);
                        }

                        @Override // com.bumptech.glide.request.target.Target
                        public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                            onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
                        }
                    });
                } else {
                    Glide.with((FragmentActivity) PictureExternalPreviewActivity.this).asGif().apply(new RequestOptions().override(GlMapUtil.DEVICE_DISPLAY_DPI_XHIGH, GLMapStaticValue.ANIMATION_MOVE_TIME).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.NONE)).load(compressPath).listener(new RequestListener<GifDrawable>() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.SimpleFragmentAdapter.1
                        @Override // com.bumptech.glide.request.RequestListener
                        public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<GifDrawable> target, boolean z) {
                            PictureExternalPreviewActivity.this.dismissDialog();
                            return false;
                        }

                        @Override // com.bumptech.glide.request.RequestListener
                        public boolean onResourceReady(GifDrawable gifDrawable, Object obj, Target<GifDrawable> target, DataSource dataSource, boolean z) {
                            PictureExternalPreviewActivity.this.dismissDialog();
                            return false;
                        }
                    }).into(photoView);
                }
                photoView.setOnViewTapListener(new OnViewTapListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.SimpleFragmentAdapter.3
                    @Override // com.luck.picture.lib.photoview.OnViewTapListener
                    public void onViewTap(View view, float f, float f2) {
                        PictureExternalPreviewActivity.this.finish();
                        PictureExternalPreviewActivity.this.overridePendingTransition(0, R.anim.a3);
                    }
                });
                subsamplingScaleImageView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.SimpleFragmentAdapter.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        PictureExternalPreviewActivity.this.finish();
                        PictureExternalPreviewActivity.this.overridePendingTransition(0, R.anim.a3);
                    }
                });
                photoView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.SimpleFragmentAdapter.5
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view) {
                        if (PictureExternalPreviewActivity.this.rxPermissions == null) {
                            PictureExternalPreviewActivity.this.rxPermissions = new RxPermissions(PictureExternalPreviewActivity.this);
                        }
                        PictureExternalPreviewActivity.this.rxPermissions.request("android.permission.WRITE_EXTERNAL_STORAGE").subscribe(new Observer<Boolean>() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.SimpleFragmentAdapter.5.1
                            @Override // io.reactivex.Observer
                            public void onComplete() {
                            }

                            @Override // io.reactivex.Observer
                            public void onError(Throwable th) {
                            }

                            @Override // io.reactivex.Observer
                            public void onNext(Boolean bool) {
                                if (bool.booleanValue()) {
                                    PictureExternalPreviewActivity.this.showDownLoadDialog(compressPath);
                                } else {
                                    PictureExternalPreviewActivity.this.showToast(PictureExternalPreviewActivity.this.getString(R.string.picture_jurisdiction));
                                }
                            }

                            @Override // io.reactivex.Observer
                            public void onSubscribe(Disposable disposable) {
                            }
                        });
                        return true;
                    }
                });
            }
            viewGroup.addView(inflate, 0);
            return inflate;
        }

        @Override // android.support.v4.view.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
    }

    /* loaded from: classes.dex */
    public class loadDataThread extends Thread {
        private String path;

        public loadDataThread(String str) {
            this.path = str;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                PictureExternalPreviewActivity.this.showLoadingImage(this.path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayLongPic(Bitmap bitmap, SubsamplingScaleImageView subsamplingScaleImageView) {
        subsamplingScaleImageView.setQuickScaleEnabled(true);
        subsamplingScaleImageView.setZoomEnabled(true);
        subsamplingScaleImageView.setPanEnabled(true);
        subsamplingScaleImageView.setDoubleTapZoomDuration(100);
        subsamplingScaleImageView.setMinimumScaleType(2);
        subsamplingScaleImageView.setDoubleTapZoomDpi(2);
        subsamplingScaleImageView.setImage(ImageSource.cachedBitmap(bitmap), new ImageViewState(0.0f, new PointF(0.0f, 0.0f), 0));
    }

    private void initViewPageAdapterData() {
        this.tv_title.setText((this.position + 1) + FileUriModel.SCHEME + this.images.size());
        this.adapter = new SimpleFragmentAdapter();
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.setCurrentItem(this.position);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                PictureExternalPreviewActivity.this.tv_title.setText((i + 1) + FileUriModel.SCHEME + PictureExternalPreviewActivity.this.images.size());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDownLoadDialog(final String str) {
        final CustomDialog customDialog = new CustomDialog(this, (ScreenUtils.getScreenWidth(this) * 3) / 4, ScreenUtils.getScreenHeight(this) / 4, R.layout.picture_wind_base_dialog_xml, R.style.Theme_dialog);
        Button button = (Button) customDialog.findViewById(R.id.btn_cancel);
        Button button2 = (Button) customDialog.findViewById(R.id.btn_commit);
        TextView textView = (TextView) customDialog.findViewById(R.id.tv_title);
        TextView textView2 = (TextView) customDialog.findViewById(R.id.tv_content);
        textView.setText(getString(R.string.picture_prompt));
        textView2.setText(getString(R.string.picture_prompt_content));
        button.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PictureExternalPreviewActivity.this.showPleaseDialog();
                if (PictureMimeType.isHttp(str)) {
                    PictureExternalPreviewActivity.this.loadDataThread = new loadDataThread(str);
                    PictureExternalPreviewActivity.this.loadDataThread.start();
                } else {
                    try {
                        String createDir = PictureFileUtils.createDir(PictureExternalPreviewActivity.this, System.currentTimeMillis() + PictureMimeType.PNG, PictureExternalPreviewActivity.this.directory_path);
                        PictureFileUtils.copyFile(str, createDir);
                        PictureExternalPreviewActivity.this.showToast(PictureExternalPreviewActivity.this.getString(R.string.picture_save_success) + "\n" + createDir);
                        PictureExternalPreviewActivity.this.dismissDialog();
                    } catch (IOException e) {
                        PictureExternalPreviewActivity.this.showToast(PictureExternalPreviewActivity.this.getString(R.string.picture_save_error) + "\n" + e.getMessage());
                        PictureExternalPreviewActivity.this.dismissDialog();
                        e.printStackTrace();
                    }
                }
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.a3);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        finish();
        overridePendingTransition(0, R.anim.a3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.picture_activity_external_preview);
        this.inflater = LayoutInflater.from(this);
        this.tv_title = (TextView) findViewById(R.id.picture_title);
        this.left_back = (ImageButton) findViewById(R.id.left_back);
        this.viewPager = (PreviewViewPager) findViewById(R.id.preview_pager);
        this.position = getIntent().getIntExtra(PictureConfig.EXTRA_POSITION, 0);
        this.directory_path = getIntent().getStringExtra(PictureConfig.DIRECTORY_PATH);
        this.images = (List) getIntent().getSerializableExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST);
        this.left_back.setOnClickListener(this);
        initViewPageAdapterData();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.loadDataThread != null) {
            this.handler.removeCallbacks(this.loadDataThread);
            this.loadDataThread = null;
        }
    }

    public void showLoadingImage(String str) {
        try {
            URL url = new URL(str);
            String createDir = PictureFileUtils.createDir(this, System.currentTimeMillis() + PictureMimeType.PNG, this.directory_path);
            byte[] bArr = new byte[8192];
            long currentTimeMillis = System.currentTimeMillis();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(createDir));
            int i = 0;
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read <= -1) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    Message obtainMessage = this.handler.obtainMessage();
                    obtainMessage.what = 200;
                    obtainMessage.obj = createDir;
                    this.handler.sendMessage(obtainMessage);
                    return;
                }
                bufferedOutputStream.write(bArr, 0, read);
                i += read;
                DebugUtil.i("Download: " + i + " byte(s)    avg speed: " + (i / (System.currentTimeMillis() - currentTimeMillis)) + "  (kb/s)");
            }
        } catch (IOException e) {
            showToast(getString(R.string.picture_save_error) + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
