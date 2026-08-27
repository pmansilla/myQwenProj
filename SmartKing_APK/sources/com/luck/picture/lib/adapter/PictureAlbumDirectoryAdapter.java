package com.luck.picture.lib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.luck.picture.lib.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class PictureAlbumDirectoryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<LocalMediaFolder> folders = new ArrayList();
    private Context mContext;
    private int mimeType;
    private OnItemClickListener onItemClickListener;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(String str, List<LocalMedia> list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView first_image;
        TextView image_num;
        TextView tv_folder_name;
        TextView tv_sign;

        public ViewHolder(View view) {
            super(view);
            this.first_image = (ImageView) view.findViewById(R.id.first_image);
            this.tv_folder_name = (TextView) view.findViewById(R.id.tv_folder_name);
            this.image_num = (TextView) view.findViewById(R.id.image_num);
            this.tv_sign = (TextView) view.findViewById(R.id.tv_sign);
        }
    }

    public PictureAlbumDirectoryAdapter(Context context) {
        this.mContext = context;
    }

    public void bindFolderData(List<LocalMediaFolder> list) {
        this.folders = list;
        notifyDataSetChanged();
    }

    public List<LocalMediaFolder> getFolderData() {
        if (this.folders == null) {
            this.folders = new ArrayList();
        }
        return this.folders;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.folders.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final LocalMediaFolder localMediaFolder = this.folders.get(i);
        String name = localMediaFolder.getName();
        int imageNum = localMediaFolder.getImageNum();
        String firstImagePath = localMediaFolder.getFirstImagePath();
        boolean isChecked = localMediaFolder.isChecked();
        viewHolder.tv_sign.setVisibility(localMediaFolder.getCheckedNum() > 0 ? 0 : 4);
        viewHolder.itemView.setSelected(isChecked);
        if (this.mimeType == PictureMimeType.ofAudio()) {
            viewHolder.first_image.setImageResource(R.drawable.audio_placeholder);
        } else {
            Glide.with(viewHolder.itemView.getContext()).asBitmap().load(firstImagePath).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop().sizeMultiplier(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).override(160, 160)).into((RequestBuilder<Bitmap>) new BitmapImageViewTarget(viewHolder.first_image) { // from class: com.luck.picture.lib.adapter.PictureAlbumDirectoryAdapter.1
                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.bumptech.glide.request.target.BitmapImageViewTarget, com.bumptech.glide.request.target.ImageViewTarget
                public void setResource(Bitmap bitmap) {
                    RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(PictureAlbumDirectoryAdapter.this.mContext.getResources(), bitmap);
                    create.setCornerRadius(8.0f);
                    viewHolder.first_image.setImageDrawable(create);
                }
            });
        }
        viewHolder.image_num.setText(SQLBuilder.PARENTHESES_LEFT + imageNum + SQLBuilder.PARENTHESES_RIGHT);
        viewHolder.tv_folder_name.setText(name);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureAlbumDirectoryAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PictureAlbumDirectoryAdapter.this.onItemClickListener != null) {
                    Iterator it = PictureAlbumDirectoryAdapter.this.folders.iterator();
                    while (it.hasNext()) {
                        ((LocalMediaFolder) it.next()).setChecked(false);
                    }
                    localMediaFolder.setChecked(true);
                    PictureAlbumDirectoryAdapter.this.notifyDataSetChanged();
                    PictureAlbumDirectoryAdapter.this.onItemClickListener.onItemClick(localMediaFolder.getName(), localMediaFolder.getImages());
                }
            }
        });
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.picture_album_folder_item, viewGroup, false));
    }

    public void setMimeType(int i) {
        this.mimeType = i;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
