package basecamera.module.activity.model;

import basecamera.module.utils.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class Album implements Serializable {
    private static final long serialVersionUID = 5702699517846159671L;
    private String albumUri;
    private ArrayList<PhotoItem> photos;
    private String title;

    public Album(String str, String str2, ArrayList<PhotoItem> arrayList) {
        this.title = str;
        this.albumUri = str2;
        this.photos = arrayList;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Album)) {
            return false;
        }
        return StringUtils.equals(this.albumUri, ((Album) obj).getAlbumUri());
    }

    public String getAlbumUri() {
        return this.albumUri;
    }

    public ArrayList<PhotoItem> getPhotos() {
        return this.photos;
    }

    public String getTitle() {
        return this.title;
    }

    public int hashCode() {
        return this.albumUri == null ? super.hashCode() : this.albumUri.hashCode();
    }

    public void setAlbumUri(String str) {
        this.albumUri = str;
    }

    public void setPhotos(ArrayList<PhotoItem> arrayList) {
        this.photos = arrayList;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
