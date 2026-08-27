package com.yalantis.ucrop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.model.CutInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class PicturePhotoGalleryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<CutInfo> list;
    private LayoutInflater mInflater;

    /* loaded from: classes2.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_dot;
        ImageView mIvPhoto;

        public ViewHolder(View view) {
            super(view);
            this.mIvPhoto = (ImageView) view.findViewById(R.id.iv_photo);
            this.iv_dot = (ImageView) view.findViewById(R.id.iv_dot);
        }
    }

    public PicturePhotoGalleryAdapter(Context context, List<CutInfo> list) {
        this.list = new ArrayList();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    public void bindData(List<CutInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CutInfo cutInfo = this.list.get(i);
        String path = cutInfo != null ? cutInfo.getPath() : "";
        if (cutInfo.isCut()) {
            viewHolder.iv_dot.setVisibility(0);
            viewHolder.iv_dot.setImageResource(R.drawable.ucrop_oval_true);
        } else {
            viewHolder.iv_dot.setVisibility(8);
        }
        Glide.with(this.context).load(path).transition(DrawableTransitionOptions.withCrossFade()).apply(new RequestOptions().placeholder(R.color.ucrop_color_grey).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)).into(viewHolder.mIvPhoto);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.ucrop_picture_gf_adapter_edit_list, viewGroup, false));
    }
}
