package com.luck.picture.lib.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.R;
import com.luck.picture.lib.anim.OptAnimationLoader;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.StringUtils;
import com.luck.picture.lib.tools.VoiceUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class PictureImageGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DURATION = 450;
    private Animation animation;
    private PictureSelectionConfig config;
    private Context context;
    private boolean enablePreview;
    private boolean enablePreviewAudio;
    private boolean enablePreviewVideo;
    private boolean enableVoice;
    private OnPhotoSelectChangedListener imageSelectChangedListener;
    private boolean is_checked_num;
    private int maxSelectNum;
    private int mimeType;
    private int overrideHeight;
    private int overrideWidth;
    private int selectMode;
    private boolean showCamera;
    private float sizeMultiplier;
    private boolean zoomAnim;
    private List<LocalMedia> images = new ArrayList();
    private List<LocalMedia> selectImages = new ArrayList();

    /* loaded from: classes.dex */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        View headerView;
        TextView tv_title_camera;

        public HeaderViewHolder(View view) {
            super(view);
            this.headerView = view;
            this.tv_title_camera = (TextView) view.findViewById(R.id.tv_title_camera);
            this.tv_title_camera.setText(PictureImageGridAdapter.this.mimeType == PictureMimeType.ofAudio() ? PictureImageGridAdapter.this.context.getString(R.string.picture_tape) : PictureImageGridAdapter.this.context.getString(R.string.picture_take_picture));
        }
    }

    /* loaded from: classes.dex */
    public interface OnPhotoSelectChangedListener {
        void onChange(List<LocalMedia> list);

        void onPictureClick(LocalMedia localMedia, int i);

        void onTakePhoto();
    }

    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView check;
        View contentView;
        ImageView iv_picture;
        LinearLayout ll_check;
        TextView tv_duration;
        TextView tv_isGif;
        TextView tv_long_chart;

        public ViewHolder(View view) {
            super(view);
            this.contentView = view;
            this.iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            this.check = (TextView) view.findViewById(R.id.check);
            this.ll_check = (LinearLayout) view.findViewById(R.id.ll_check);
            this.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            this.tv_isGif = (TextView) view.findViewById(R.id.tv_isGif);
            this.tv_long_chart = (TextView) view.findViewById(R.id.tv_long_chart);
        }
    }

    public PictureImageGridAdapter(Context context, PictureSelectionConfig pictureSelectionConfig) {
        this.showCamera = true;
        this.selectMode = 2;
        this.enablePreviewVideo = false;
        this.enablePreviewAudio = false;
        this.context = context;
        this.config = pictureSelectionConfig;
        this.selectMode = pictureSelectionConfig.selectionMode;
        this.showCamera = pictureSelectionConfig.isCamera;
        this.maxSelectNum = pictureSelectionConfig.maxSelectNum;
        this.enablePreview = pictureSelectionConfig.enablePreview;
        this.enablePreviewVideo = pictureSelectionConfig.enPreviewVideo;
        this.enablePreviewAudio = pictureSelectionConfig.enablePreviewAudio;
        this.is_checked_num = pictureSelectionConfig.checkNumMode;
        this.overrideWidth = pictureSelectionConfig.overrideWidth;
        this.overrideHeight = pictureSelectionConfig.overrideHeight;
        this.enableVoice = pictureSelectionConfig.openClickSound;
        this.sizeMultiplier = pictureSelectionConfig.sizeMultiplier;
        this.mimeType = pictureSelectionConfig.mimeType;
        this.zoomAnim = pictureSelectionConfig.zoomAnim;
        this.animation = OptAnimationLoader.loadAnimation(context, R.anim.modal_in);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeCheckboxState(ViewHolder viewHolder, LocalMedia localMedia) {
        boolean isSelected = viewHolder.check.isSelected();
        String pictureType = this.selectImages.size() > 0 ? this.selectImages.get(0).getPictureType() : "";
        if (!TextUtils.isEmpty(pictureType) && !PictureMimeType.mimeToEqual(pictureType, localMedia.getPictureType())) {
            Toast.makeText(this.context, this.context.getString(R.string.picture_rule), 1).show();
            return;
        }
        if (this.selectImages.size() >= this.maxSelectNum && !isSelected) {
            Toast.makeText(this.context, pictureType.startsWith(PictureConfig.IMAGE) ? this.context.getString(R.string.picture_message_max_num, Integer.valueOf(this.maxSelectNum)) : this.context.getString(R.string.picture_message_video_max_num, Integer.valueOf(this.maxSelectNum)), 1).show();
            return;
        }
        if (isSelected) {
            Iterator<LocalMedia> it = this.selectImages.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                LocalMedia next = it.next();
                if (next.getPath().equals(localMedia.getPath())) {
                    this.selectImages.remove(next);
                    DebugUtil.i("selectImages remove::", this.config.selectionMedias.size() + "");
                    subSelectPosition();
                    disZoom(viewHolder.iv_picture);
                    break;
                }
            }
        } else {
            this.selectImages.add(localMedia);
            DebugUtil.i("selectImages add::", this.config.selectionMedias.size() + "");
            localMedia.setNum(this.selectImages.size());
            VoiceUtils.playVoice(this.context, this.enableVoice);
            zoom(viewHolder.iv_picture);
        }
        notifyItemChanged(viewHolder.getAdapterPosition());
        selectImage(viewHolder, !isSelected, true);
        if (this.imageSelectChangedListener != null) {
            this.imageSelectChangedListener.onChange(this.selectImages);
        }
    }

    private void disZoom(ImageView imageView) {
        if (this.zoomAnim) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(imageView, "scaleX", 1.12f, 1.0f), ObjectAnimator.ofFloat(imageView, "scaleY", 1.12f, 1.0f));
            animatorSet.setDuration(450L);
            animatorSet.start();
        }
    }

    private void notifyCheckChanged(ViewHolder viewHolder, LocalMedia localMedia) {
        viewHolder.check.setText("");
        for (LocalMedia localMedia2 : this.selectImages) {
            if (localMedia2.getPath().equals(localMedia.getPath())) {
                localMedia.setNum(localMedia2.getNum());
                localMedia2.setPosition(localMedia.getPosition());
                viewHolder.check.setText(String.valueOf(localMedia.getNum()));
            }
        }
    }

    private void subSelectPosition() {
        if (this.is_checked_num) {
            int size = this.selectImages.size();
            int i = 0;
            while (i < size) {
                LocalMedia localMedia = this.selectImages.get(i);
                i++;
                localMedia.setNum(i);
                notifyItemChanged(localMedia.position);
            }
        }
    }

    private void zoom(ImageView imageView) {
        if (this.zoomAnim) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.12f), ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.12f));
            animatorSet.setDuration(450L);
            animatorSet.start();
        }
    }

    public void bindImagesData(List<LocalMedia> list) {
        this.images = list;
        notifyDataSetChanged();
    }

    public void bindSelectImages(List<LocalMedia> list) {
        ArrayList arrayList = new ArrayList();
        Iterator<LocalMedia> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        this.selectImages = arrayList;
        subSelectPosition();
        if (this.imageSelectChangedListener != null) {
            this.imageSelectChangedListener.onChange(this.selectImages);
        }
    }

    public List<LocalMedia> getImages() {
        if (this.images == null) {
            this.images = new ArrayList();
        }
        return this.images;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.showCamera ? this.images.size() + 1 : this.images.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return (this.showCamera && i == 0) ? 1 : 2;
    }

    public List<LocalMedia> getSelectedImages() {
        if (this.selectImages == null) {
            this.selectImages = new ArrayList();
        }
        return this.selectImages;
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

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == 1) {
            ((HeaderViewHolder) viewHolder).headerView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureImageGridAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (PictureImageGridAdapter.this.imageSelectChangedListener != null) {
                        PictureImageGridAdapter.this.imageSelectChangedListener.onTakePhoto();
                    }
                }
            });
            return;
        }
        final ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        final LocalMedia localMedia = this.images.get(this.showCamera ? i - 1 : i);
        localMedia.position = viewHolder2.getAdapterPosition();
        final String path = localMedia.getPath();
        String pictureType = localMedia.getPictureType();
        viewHolder2.ll_check.setVisibility(this.selectMode == 1 ? 8 : 0);
        if (this.is_checked_num) {
            notifyCheckChanged(viewHolder2, localMedia);
        }
        selectImage(viewHolder2, isSelected(localMedia), false);
        final int isPictureType = PictureMimeType.isPictureType(pictureType);
        viewHolder2.tv_isGif.setVisibility(PictureMimeType.isGif(pictureType) ? 0 : 8);
        if (this.mimeType == PictureMimeType.ofAudio()) {
            viewHolder2.tv_duration.setVisibility(0);
            StringUtils.modifyTextViewDrawable(viewHolder2.tv_duration, ContextCompat.getDrawable(this.context, R.drawable.picture_audio), 0);
        } else {
            StringUtils.modifyTextViewDrawable(viewHolder2.tv_duration, ContextCompat.getDrawable(this.context, R.drawable.video_icon), 0);
            viewHolder2.tv_duration.setVisibility(isPictureType == 2 ? 0 : 8);
        }
        viewHolder2.tv_long_chart.setVisibility(PictureMimeType.isLongImg(localMedia) ? 0 : 8);
        viewHolder2.tv_duration.setText(DateUtils.timeParse(localMedia.getDuration()));
        if (this.mimeType == PictureMimeType.ofAudio()) {
            viewHolder2.iv_picture.setImageResource(R.drawable.audio_placeholder);
        } else {
            RequestOptions requestOptions = new RequestOptions();
            if (this.overrideWidth > 0 || this.overrideHeight > 0) {
                requestOptions.override(this.overrideWidth, this.overrideHeight);
            } else {
                requestOptions.sizeMultiplier(this.sizeMultiplier);
            }
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.centerCrop();
            requestOptions.placeholder(R.drawable.image_placeholder);
            Glide.with(this.context).asBitmap().load(path).apply(requestOptions).into(viewHolder2.iv_picture);
        }
        if (this.enablePreview || this.enablePreviewVideo || this.enablePreviewAudio) {
            viewHolder2.ll_check.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureImageGridAdapter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (new File(path).exists()) {
                        PictureImageGridAdapter.this.changeCheckboxState(viewHolder2, localMedia);
                    } else {
                        Toast.makeText(PictureImageGridAdapter.this.context, PictureImageGridAdapter.this.context.getString(R.string.picture_error), 1).show();
                    }
                }
            });
        }
        viewHolder2.contentView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureImageGridAdapter.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!new File(path).exists()) {
                    Toast.makeText(PictureImageGridAdapter.this.context, PictureImageGridAdapter.this.context.getString(R.string.picture_error), 1).show();
                    return;
                }
                if (isPictureType == 1 && (PictureImageGridAdapter.this.enablePreview || PictureImageGridAdapter.this.selectMode == 1)) {
                    PictureImageGridAdapter.this.imageSelectChangedListener.onPictureClick(localMedia, PictureImageGridAdapter.this.showCamera ? i - 1 : i);
                    return;
                }
                if (isPictureType == 2 && (PictureImageGridAdapter.this.enablePreviewVideo || PictureImageGridAdapter.this.selectMode == 1)) {
                    PictureImageGridAdapter.this.imageSelectChangedListener.onPictureClick(localMedia, PictureImageGridAdapter.this.showCamera ? i - 1 : i);
                } else if (isPictureType == 3 && (PictureImageGridAdapter.this.enablePreviewAudio || PictureImageGridAdapter.this.selectMode == 1)) {
                    PictureImageGridAdapter.this.imageSelectChangedListener.onPictureClick(localMedia, PictureImageGridAdapter.this.showCamera ? i - 1 : i);
                } else {
                    PictureImageGridAdapter.this.changeCheckboxState(viewHolder2, localMedia);
                }
            }
        });
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return i == 1 ? new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_item_camera, viewGroup, false)) : new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_image_grid_item, viewGroup, false));
    }

    public void selectImage(ViewHolder viewHolder, boolean z, boolean z2) {
        viewHolder.check.setSelected(z);
        if (!z) {
            viewHolder.iv_picture.setColorFilter(ContextCompat.getColor(this.context, R.color.image_overlay_false), PorterDuff.Mode.SRC_ATOP);
            return;
        }
        if (z2 && this.animation != null) {
            viewHolder.check.startAnimation(this.animation);
        }
        viewHolder.iv_picture.setColorFilter(ContextCompat.getColor(this.context, R.color.image_overlay_true), PorterDuff.Mode.SRC_ATOP);
    }

    public void setOnPhotoSelectChangedListener(OnPhotoSelectChangedListener onPhotoSelectChangedListener) {
        this.imageSelectChangedListener = onPhotoSelectChangedListener;
    }

    public void setShowCamera(boolean z) {
        this.showCamera = z;
    }
}
