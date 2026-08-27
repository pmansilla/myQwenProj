package basecamera.module.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import basecamera.module.activity.model.PhotoItem;
import basecamera.module.lib.R;
import basecamera.module.utils.DistanceUtil;
import java.util.List;
import me.panpf.sketch.SketchImageView;

/* loaded from: classes.dex */
public class GalleryAdapter extends BaseAdapter {
    public static GalleryHolder holder;
    private Context mContext;
    private List<PhotoItem> values;

    /* loaded from: classes.dex */
    class GalleryHolder {
        SketchImageView sample;

        GalleryHolder() {
        }
    }

    public GalleryAdapter(Context context, List<PhotoItem> list) {
        this.mContext = context;
        this.values = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.values.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.values.get(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        GalleryHolder galleryHolder;
        int cameraAlbumWidth = DistanceUtil.getCameraAlbumWidth(this.mContext);
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.basecamera_item_gallery, (ViewGroup) null);
            galleryHolder = new GalleryHolder();
            galleryHolder.sample = (SketchImageView) view.findViewById(R.id.gallery_sample_image);
            galleryHolder.sample.setLayoutParams(new AbsListView.LayoutParams(cameraAlbumWidth, cameraAlbumWidth));
            view.setTag(galleryHolder);
        } else {
            galleryHolder = (GalleryHolder) view.getTag();
        }
        galleryHolder.sample.displayContentImage(((PhotoItem) getItem(i)).getImageUri());
        return view;
    }
}
