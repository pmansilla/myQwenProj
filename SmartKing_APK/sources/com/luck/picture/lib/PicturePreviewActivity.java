package com.luck.picture.lib;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.autonavi.amap.mapcore.tools.GlMapUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.anim.OptAnimationLoader;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.EventEntity;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.observable.ImagesObservable;
import com.luck.picture.lib.photoview.OnViewTapListener;
import com.luck.picture.lib.photoview.PhotoView;
import com.luck.picture.lib.rxbus2.RxBus;
import com.luck.picture.lib.rxbus2.Subscribe;
import com.luck.picture.lib.rxbus2.ThreadMode;
import com.luck.picture.lib.tools.AttrsUtils;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.LightStatusBarUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.ToolbarUtil;
import com.luck.picture.lib.tools.VoiceUtils;
import com.luck.picture.lib.widget.PreviewViewPager;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.yalantis.ucrop.UCropMulti;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class PicturePreviewActivity extends PictureBaseActivity implements View.OnClickListener, Animation.AnimationListener {
    private SimpleFragmentAdapter adapter;
    private Animation animation;
    private TextView check;
    private LinearLayout id_ll_ok;
    private int index;
    private LayoutInflater inflater;
    private LinearLayout ll_check;
    private Handler mHandler;
    private ImageView picture_left_back;
    private int position;
    private boolean refresh;
    private int screenWidth;
    private TextView tv_img_num;
    private TextView tv_ok;
    private TextView tv_title;
    private PreviewViewPager viewPager;
    private List<LocalMedia> images = new ArrayList();
    private List<LocalMedia> selectImages = new ArrayList();

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
            return PicturePreviewActivity.this.images.size();
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            View inflate = PicturePreviewActivity.this.inflater.inflate(R.layout.picture_image_preview, viewGroup, false);
            final PhotoView photoView = (PhotoView) inflate.findViewById(R.id.preview_image);
            final SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) inflate.findViewById(R.id.longImg);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_play);
            LocalMedia localMedia = (LocalMedia) PicturePreviewActivity.this.images.get(i);
            if (localMedia != null) {
                String pictureType = localMedia.getPictureType();
                int i2 = 8;
                imageView.setVisibility(pictureType.startsWith(PictureConfig.VIDEO) ? 0 : 8);
                final String compressPath = (!localMedia.isCut() || localMedia.isCompressed()) ? (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) ? localMedia.getCompressPath() : localMedia.getPath() : localMedia.getCutPath();
                boolean isGif = PictureMimeType.isGif(pictureType);
                final boolean isLongImg = PictureMimeType.isLongImg(localMedia);
                photoView.setVisibility((!isLongImg || isGif) ? 0 : 8);
                if (isLongImg && !isGif) {
                    i2 = 0;
                }
                subsamplingScaleImageView.setVisibility(i2);
                if (!isGif || localMedia.isCompressed()) {
                    Glide.with((FragmentActivity) PicturePreviewActivity.this).asBitmap().load(compressPath).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into((RequestBuilder<Bitmap>) new SimpleTarget<Bitmap>(GlMapUtil.DEVICE_DISPLAY_DPI_XHIGH, GLMapStaticValue.ANIMATION_MOVE_TIME) { // from class: com.luck.picture.lib.PicturePreviewActivity.SimpleFragmentAdapter.1
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            if (isLongImg) {
                                PicturePreviewActivity.this.displayLongPic(bitmap, subsamplingScaleImageView);
                            } else {
                                photoView.setImageBitmap(bitmap);
                            }
                        }

                        @Override // com.bumptech.glide.request.target.Target
                        public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                            onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
                        }
                    });
                } else {
                    Glide.with((FragmentActivity) PicturePreviewActivity.this).asGif().load(compressPath).apply(new RequestOptions().override(GlMapUtil.DEVICE_DISPLAY_DPI_XHIGH, GLMapStaticValue.ANIMATION_MOVE_TIME).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.NONE)).into(photoView);
                }
                photoView.setOnViewTapListener(new OnViewTapListener() { // from class: com.luck.picture.lib.PicturePreviewActivity.SimpleFragmentAdapter.2
                    @Override // com.luck.picture.lib.photoview.OnViewTapListener
                    public void onViewTap(View view, float f, float f2) {
                        PicturePreviewActivity.this.onBackPressed();
                    }
                });
                subsamplingScaleImageView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PicturePreviewActivity.SimpleFragmentAdapter.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        PicturePreviewActivity.this.onBackPressed();
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PicturePreviewActivity.SimpleFragmentAdapter.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("video_path", compressPath);
                        PicturePreviewActivity.this.startActivity(PictureVideoPlayActivity.class, bundle);
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
        onSelectNumChange(false);
        onImageChecked(this.position);
        if (this.images.size() > 0) {
            LocalMedia localMedia = this.images.get(this.position);
            this.index = localMedia.getPosition();
            if (this.config.checkNumMode) {
                this.tv_img_num.setSelected(true);
                this.check.setText(localMedia.getNum() + "");
                notifyCheckChanged(localMedia);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void isPreviewEggs(boolean z, int i, int i2) {
        if (!z || this.images.size() <= 0 || this.images == null) {
            return;
        }
        if (i2 < this.screenWidth / 2) {
            LocalMedia localMedia = this.images.get(i);
            this.check.setSelected(isSelected(localMedia));
            if (this.config.checkNumMode) {
                int num = localMedia.getNum();
                this.check.setText(num + "");
                notifyCheckChanged(localMedia);
                onImageChecked(i);
                return;
            }
            return;
        }
        int i3 = i + 1;
        LocalMedia localMedia2 = this.images.get(i3);
        this.check.setSelected(isSelected(localMedia2));
        if (this.config.checkNumMode) {
            int num2 = localMedia2.getNum();
            this.check.setText(num2 + "");
            notifyCheckChanged(localMedia2);
            onImageChecked(i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCheckChanged(LocalMedia localMedia) {
        if (this.config.checkNumMode) {
            this.check.setText("");
            for (LocalMedia localMedia2 : this.selectImages) {
                if (localMedia2.getPath().equals(localMedia.getPath())) {
                    localMedia.setNum(localMedia2.getNum());
                    this.check.setText(String.valueOf(localMedia.getNum()));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void subSelectPosition() {
        int size = this.selectImages.size();
        int i = 0;
        while (i < size) {
            LocalMedia localMedia = this.selectImages.get(i);
            i++;
            localMedia.setNum(i);
        }
    }

    private void updateSelector(boolean z) {
        if (z) {
            RxBus.getDefault().post(new EventEntity(PictureConfig.UPDATE_FLAG, this.selectImages, this.index));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventEntity eventEntity) {
        if (eventEntity.what != 2770) {
            return;
        }
        dismissDialog();
        this.mHandler.postDelayed(new Runnable() { // from class: com.luck.picture.lib.PicturePreviewActivity.1
            @Override // java.lang.Runnable
            public void run() {
                PicturePreviewActivity.this.onBackPressed();
            }
        }, 150L);
    }

    public boolean isSelected(LocalMedia localMedia) {
        Iterator<LocalMedia> it = this.selectImages.iterator();
        while (it.hasNext()) {
            if (it.next().getPath().equals(localMedia.getPath())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            if (i2 == 96) {
                showToast(((Throwable) intent.getSerializableExtra("com.yalantis.ucrop.Error")).getMessage());
            }
        } else {
            if (i != 609) {
                return;
            }
            setResult(-1, new Intent().putExtra(UCropMulti.EXTRA_OUTPUT_URI_LIST, (Serializable) UCropMulti.getOutput(intent)));
            finish();
        }
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationEnd(Animation animation) {
        updateSelector(this.refresh);
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationRepeat(Animation animation) {
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationStart(Animation animation) {
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        closeActivity();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.picture_left_back) {
            onBackPressed();
        }
        if (id == R.id.id_ll_ok) {
            int size = this.selectImages.size();
            String pictureType = this.selectImages.size() > 0 ? this.selectImages.get(0).getPictureType() : "";
            if (this.config.minSelectNum > 0 && size < this.config.minSelectNum && this.config.selectionMode == 2) {
                showToast(pictureType.startsWith(PictureConfig.IMAGE) ? getString(R.string.picture_min_img_num, new Object[]{Integer.valueOf(this.config.minSelectNum)}) : getString(R.string.picture_min_video_num, new Object[]{Integer.valueOf(this.config.minSelectNum)}));
                return;
            }
            if (!this.config.enableCrop || !pictureType.startsWith(PictureConfig.IMAGE) || this.config.selectionMode != 2) {
                onResult(this.selectImages);
                return;
            }
            ArrayList<String> arrayList = new ArrayList<>();
            Iterator<LocalMedia> it = this.selectImages.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().getPath());
            }
            startCrop(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.picture_preview);
        if (!RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().register(this);
        }
        this.mHandler = new Handler();
        this.inflater = LayoutInflater.from(this);
        this.screenWidth = ScreenUtils.getScreenWidth(this);
        ToolbarUtil.setColorNoTranslucent(this, AttrsUtils.getTypeValueColor(this, R.attr.picture_status_color));
        LightStatusBarUtils.setLightStatusBar(this, this.previewStatusFont);
        this.animation = OptAnimationLoader.loadAnimation(this, R.anim.modal_in);
        this.animation.setAnimationListener(this);
        this.picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        this.viewPager = (PreviewViewPager) findViewById(R.id.preview_pager);
        this.ll_check = (LinearLayout) findViewById(R.id.ll_check);
        this.id_ll_ok = (LinearLayout) findViewById(R.id.id_ll_ok);
        this.check = (TextView) findViewById(R.id.check);
        this.picture_left_back.setOnClickListener(this);
        this.tv_ok = (TextView) findViewById(R.id.tv_ok);
        this.id_ll_ok.setOnClickListener(this);
        this.tv_img_num = (TextView) findViewById(R.id.tv_img_num);
        this.tv_title = (TextView) findViewById(R.id.picture_title);
        this.position = getIntent().getIntExtra(PictureConfig.EXTRA_POSITION, 0);
        this.tv_ok.setText(this.numComplete ? getString(R.string.picture_done_front_num, new Object[]{0, Integer.valueOf(this.config.maxSelectNum)}) : getString(R.string.picture_please_select));
        this.tv_img_num.setSelected(this.config.checkNumMode);
        this.selectImages = (List) getIntent().getSerializableExtra(PictureConfig.EXTRA_SELECT_LIST);
        if (getIntent().getBooleanExtra(PictureConfig.EXTRA_BOTTOM_PREVIEW, false)) {
            this.images = (List) getIntent().getSerializableExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST);
        } else {
            this.images = ImagesObservable.getInstance().readLocalMedias();
        }
        initViewPageAdapterData();
        this.ll_check.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PicturePreviewActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                boolean z;
                if (PicturePreviewActivity.this.images == null || PicturePreviewActivity.this.images.size() <= 0) {
                    return;
                }
                LocalMedia localMedia = (LocalMedia) PicturePreviewActivity.this.images.get(PicturePreviewActivity.this.viewPager.getCurrentItem());
                String pictureType = PicturePreviewActivity.this.selectImages.size() > 0 ? ((LocalMedia) PicturePreviewActivity.this.selectImages.get(0)).getPictureType() : "";
                if (!TextUtils.isEmpty(pictureType) && !PictureMimeType.mimeToEqual(pictureType, localMedia.getPictureType())) {
                    PicturePreviewActivity.this.showToast(PicturePreviewActivity.this.getString(R.string.picture_rule));
                    return;
                }
                if (PicturePreviewActivity.this.check.isSelected()) {
                    PicturePreviewActivity.this.check.setSelected(false);
                    z = false;
                } else {
                    PicturePreviewActivity.this.check.setSelected(true);
                    PicturePreviewActivity.this.check.startAnimation(PicturePreviewActivity.this.animation);
                    z = true;
                }
                if (PicturePreviewActivity.this.selectImages.size() >= PicturePreviewActivity.this.config.maxSelectNum && z) {
                    PicturePreviewActivity.this.showToast(PicturePreviewActivity.this.getString(R.string.picture_message_max_num, new Object[]{Integer.valueOf(PicturePreviewActivity.this.config.maxSelectNum)}));
                    PicturePreviewActivity.this.check.setSelected(false);
                    return;
                }
                if (!z) {
                    Iterator it = PicturePreviewActivity.this.selectImages.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        LocalMedia localMedia2 = (LocalMedia) it.next();
                        if (localMedia2.getPath().equals(localMedia.getPath())) {
                            PicturePreviewActivity.this.selectImages.remove(localMedia2);
                            PicturePreviewActivity.this.subSelectPosition();
                            PicturePreviewActivity.this.notifyCheckChanged(localMedia2);
                            break;
                        }
                    }
                } else {
                    VoiceUtils.playVoice(PicturePreviewActivity.this.mContext, PicturePreviewActivity.this.config.openClickSound);
                    PicturePreviewActivity.this.selectImages.add(localMedia);
                    localMedia.setNum(PicturePreviewActivity.this.selectImages.size());
                    if (PicturePreviewActivity.this.config.checkNumMode) {
                        PicturePreviewActivity.this.check.setText(localMedia.getNum() + "");
                    }
                }
                PicturePreviewActivity.this.onSelectNumChange(true);
            }
        });
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.luck.picture.lib.PicturePreviewActivity.3
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
                PicturePreviewActivity.this.isPreviewEggs(PicturePreviewActivity.this.config.previewEggs, i, i2);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                PicturePreviewActivity.this.position = i;
                PicturePreviewActivity.this.tv_title.setText((PicturePreviewActivity.this.position + 1) + FileUriModel.SCHEME + PicturePreviewActivity.this.images.size());
                LocalMedia localMedia = (LocalMedia) PicturePreviewActivity.this.images.get(PicturePreviewActivity.this.position);
                PicturePreviewActivity.this.index = localMedia.getPosition();
                if (PicturePreviewActivity.this.config.previewEggs) {
                    return;
                }
                if (PicturePreviewActivity.this.config.checkNumMode) {
                    PicturePreviewActivity.this.check.setText(localMedia.getNum() + "");
                    PicturePreviewActivity.this.notifyCheckChanged(localMedia);
                }
                PicturePreviewActivity.this.onImageChecked(PicturePreviewActivity.this.position);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().unregister(this);
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        if (this.animation != null) {
            this.animation.cancel();
            this.animation = null;
        }
    }

    public void onImageChecked(int i) {
        if (this.images == null || this.images.size() <= 0) {
            this.check.setSelected(false);
        } else {
            this.check.setSelected(isSelected(this.images.get(i)));
        }
    }

    @Override // com.luck.picture.lib.PictureBaseActivity
    public void onResult(List<LocalMedia> list) {
        RxBus.getDefault().post(new EventEntity(PictureConfig.PREVIEW_DATA_FLAG, list));
        if (this.config.isCompress) {
            DebugUtil.i("**** loading compress");
            showPleaseDialog();
        } else {
            DebugUtil.i("**** not compress finish");
            onBackPressed();
        }
    }

    public void onSelectNumChange(boolean z) {
        this.refresh = z;
        if (this.selectImages.size() != 0) {
            this.tv_ok.setSelected(true);
            this.id_ll_ok.setEnabled(true);
            if (this.numComplete) {
                this.tv_ok.setText(getString(R.string.picture_done_front_num, new Object[]{Integer.valueOf(this.selectImages.size()), Integer.valueOf(this.config.maxSelectNum)}));
            } else {
                if (this.refresh) {
                    this.tv_img_num.startAnimation(this.animation);
                }
                this.tv_img_num.setVisibility(0);
                this.tv_img_num.setText(this.selectImages.size() + "");
                this.tv_ok.setText(getString(R.string.picture_completed));
            }
        } else {
            this.id_ll_ok.setEnabled(false);
            this.tv_ok.setSelected(false);
            if (this.numComplete) {
                this.tv_ok.setText(getString(R.string.picture_done_front_num, new Object[]{0, Integer.valueOf(this.config.maxSelectNum)}));
            } else {
                this.tv_img_num.setVisibility(4);
                this.tv_ok.setText(getString(R.string.picture_please_select));
            }
        }
        updateSelector(this.refresh);
    }
}
