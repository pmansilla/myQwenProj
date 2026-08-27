package basecamera.module.activity.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class PhotoItem implements Parcelable, Comparable<PhotoItem> {
    public static final Parcelable.Creator<PhotoItem> CREATOR = new Parcelable.Creator<PhotoItem>() { // from class: basecamera.module.activity.model.PhotoItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PhotoItem createFromParcel(Parcel parcel) {
            return new PhotoItem(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PhotoItem[] newArray(int i) {
            return new PhotoItem[i];
        }
    };
    private boolean checked;
    private long date;
    private String dateStr;
    private String imageUri;
    private boolean uploaded;

    public PhotoItem(Parcel parcel) {
        this.imageUri = parcel.readString();
        this.date = parcel.readLong();
    }

    public PhotoItem(String str, long j) {
        this.imageUri = str;
        this.date = j;
        this.uploaded = false;
    }

    @Override // java.lang.Comparable
    public int compareTo(PhotoItem photoItem) {
        if (photoItem == null) {
            return 1;
        }
        return (int) ((photoItem.getDate() - this.date) / 1000);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public long getDate() {
        return this.date;
    }

    public String getDateStr() {
        return this.dateStr;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public boolean isUploaded() {
        return this.uploaded;
    }

    public void setChecked(boolean z) {
        this.checked = z;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public void setDateStr(String str) {
        this.dateStr = str;
    }

    public void setImageUri(String str) {
        this.imageUri = str;
    }

    public void setUploaded(boolean z) {
        this.uploaded = z;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.imageUri);
        parcel.writeLong(this.date);
    }
}
