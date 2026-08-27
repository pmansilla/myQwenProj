package basecamera.module.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import basecamera.module.activity.adapter.GalleryAdapter;
import basecamera.module.activity.model.PhotoItem;
import basecamera.module.functionModule.imagePreview.style.index.NumberIndexIndicator;
import basecamera.module.functionModule.imagePreview.style.progress.ProgressBarIndicator;
import basecamera.module.functionModule.imagePreview.transfer.TransferConfig;
import basecamera.module.functionModule.imagePreview.transfer.Transferee;
import basecamera.module.lib.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class BaseCameraAlbumFragment extends Fragment {
    private GridView albums;
    protected TransferConfig config;
    protected Transferee transferee;
    private ArrayList<PhotoItem> photos = new ArrayList<>();
    private List<String> pathList = new ArrayList();

    private void initTransfereeConfig() {
        this.config = TransferConfig.build().setSourceImageList(this.pathList).setThumbnailImageList(this.pathList).setMissPlaceHolder(R.drawable.ic_empty_photo).setErrorPlaceHolder(R.drawable.ic_empty_photo).setProgressIndicator(new ProgressBarIndicator()).setIndexIndicator(new NumberIndexIndicator()).setJustLoadHitImage(true).bindListView(this.albums, R.id.gallery_sample_image);
    }

    public static Fragment newInstance(ArrayList<PhotoItem> arrayList) {
        BaseCameraAlbumFragment baseCameraAlbumFragment = new BaseCameraAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("photos", arrayList);
        baseCameraAlbumFragment.setArguments(bundle);
        return baseCameraAlbumFragment;
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View inflate = layoutInflater.inflate(R.layout.basecamera_fragment_album, (ViewGroup) null);
        this.photos = (ArrayList) getArguments().getSerializable("photos");
        this.albums = (GridView) inflate.findViewById(R.id.albums);
        this.albums.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: basecamera.module.activity.BaseCameraAlbumFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                BaseCameraAlbumFragment.this.config.setNowThumbnailIndex(i);
                BaseCameraAlbumFragment.this.transferee.apply(BaseCameraAlbumFragment.this.config).show();
            }
        });
        this.transferee = Transferee.getDefault(getContext());
        Iterator<PhotoItem> it = this.photos.iterator();
        while (it.hasNext()) {
            PhotoItem next = it.next();
            this.pathList.add("file:/" + next.getImageUri());
        }
        initTransfereeConfig();
        return inflate;
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.albums.setAdapter((ListAdapter) new GalleryAdapter(getActivity(), this.photos));
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }
}
