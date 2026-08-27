package com.luck.picture.lib.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.luck.picture.lib.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import kotlin.jvm.internal.LongCompanionObject;

/* loaded from: classes.dex */
public class LocalMediaLoader {
    private static final int AUDIO_DURATION = 500;
    private static final String NOT_GIF = "!='image/gif'";
    private static final String ORDER_BY = "_id DESC";
    private static final String SELECTION = "media_type=? AND _size>0";
    private static final String SELECTION_NOT_GIF = "media_type=? AND _size>0 AND mime_type!='image/gif'";
    private FragmentActivity activity;
    private boolean isGif;
    private int type;
    private long videoMaxS;
    private long videoMinS;
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String DURATION = "duration";
    private static final String[] PROJECTION = {FileDownloadModel.ID, "_data", "mime_type", "width", "height", DURATION};
    private static final String[] SELECTION_ALL_ARGS = {String.valueOf(1), String.valueOf(3)};

    /* loaded from: classes.dex */
    public interface LocalMediaLoadListener {
        void loadComplete(List<LocalMediaFolder> list);
    }

    public LocalMediaLoader(FragmentActivity fragmentActivity, int i, boolean z, long j, long j2) {
        this.type = 1;
        this.videoMaxS = 0L;
        this.videoMinS = 0L;
        this.activity = fragmentActivity;
        this.type = i;
        this.isGif = z;
        this.videoMaxS = j;
        this.videoMinS = j2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDurationCondition(long j, long j2) {
        long j3 = this.videoMaxS == 0 ? LongCompanionObject.MAX_VALUE : this.videoMaxS;
        if (j != 0) {
            j3 = Math.min(j3, j);
        }
        Locale locale = Locale.CHINA;
        Object[] objArr = new Object[3];
        objArr[0] = Long.valueOf(Math.max(j2, this.videoMinS));
        objArr[1] = Math.max(j2, this.videoMinS) == 0 ? "" : "=";
        objArr[2] = Long.valueOf(j3);
        return String.format(locale, "%d <%s duration and duration <= %d", objArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LocalMediaFolder getImageFolder(String str, List<LocalMediaFolder> list) {
        File parentFile = new File(str).getParentFile();
        for (LocalMediaFolder localMediaFolder : list) {
            if (localMediaFolder.getName().equals(parentFile.getName())) {
                return localMediaFolder;
            }
        }
        LocalMediaFolder localMediaFolder2 = new LocalMediaFolder();
        localMediaFolder2.setName(parentFile.getName());
        localMediaFolder2.setPath(parentFile.getAbsolutePath());
        localMediaFolder2.setFirstImagePath(str);
        list.add(localMediaFolder2);
        return localMediaFolder2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getSelectionArgsForAllMediaCondition(String str, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("(media_type=?");
        sb.append(z ? "" : " AND mime_type!='image/gif'");
        sb.append(" OR ");
        sb.append("media_type=? AND ");
        sb.append(str);
        sb.append(") AND ");
        sb.append("_size");
        sb.append(">0");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getSelectionArgsForSingleMediaCondition(String str) {
        return "media_type=? AND _size>0 AND " + str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] getSelectionArgsForSingleMediaType(int i) {
        return new String[]{String.valueOf(i)};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sortFolder(List<LocalMediaFolder> list) {
        Collections.sort(list, new Comparator<LocalMediaFolder>() { // from class: com.luck.picture.lib.model.LocalMediaLoader.2
            @Override // java.util.Comparator
            public int compare(LocalMediaFolder localMediaFolder, LocalMediaFolder localMediaFolder2) {
                int imageNum;
                int imageNum2;
                if (localMediaFolder.getImages() == null || localMediaFolder2.getImages() == null || (imageNum = localMediaFolder.getImageNum()) == (imageNum2 = localMediaFolder2.getImageNum())) {
                    return 0;
                }
                return imageNum < imageNum2 ? 1 : -1;
            }
        });
    }

    public void loadAllMedia(final LocalMediaLoadListener localMediaLoadListener) {
        this.activity.getSupportLoaderManager().initLoader(this.type, null, new LoaderManager.LoaderCallbacks<Cursor>() { // from class: com.luck.picture.lib.model.LocalMediaLoader.1
            @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                switch (i) {
                    case 0:
                        return new CursorLoader(LocalMediaLoader.this.activity, LocalMediaLoader.QUERY_URI, LocalMediaLoader.PROJECTION, LocalMediaLoader.getSelectionArgsForAllMediaCondition(LocalMediaLoader.this.getDurationCondition(0L, 0L), LocalMediaLoader.this.isGif), LocalMediaLoader.SELECTION_ALL_ARGS, LocalMediaLoader.ORDER_BY);
                    case 1:
                        return new CursorLoader(LocalMediaLoader.this.activity, LocalMediaLoader.QUERY_URI, LocalMediaLoader.PROJECTION, LocalMediaLoader.this.isGif ? LocalMediaLoader.SELECTION : LocalMediaLoader.SELECTION_NOT_GIF, LocalMediaLoader.getSelectionArgsForSingleMediaType(1), LocalMediaLoader.ORDER_BY);
                    case 2:
                        return new CursorLoader(LocalMediaLoader.this.activity, LocalMediaLoader.QUERY_URI, LocalMediaLoader.PROJECTION, LocalMediaLoader.getSelectionArgsForSingleMediaCondition(LocalMediaLoader.this.getDurationCondition(0L, 0L)), LocalMediaLoader.getSelectionArgsForSingleMediaType(3), LocalMediaLoader.ORDER_BY);
                    case 3:
                        return new CursorLoader(LocalMediaLoader.this.activity, LocalMediaLoader.QUERY_URI, LocalMediaLoader.PROJECTION, LocalMediaLoader.getSelectionArgsForSingleMediaCondition(LocalMediaLoader.this.getDurationCondition(0L, 500L)), LocalMediaLoader.getSelectionArgsForSingleMediaType(2), LocalMediaLoader.ORDER_BY);
                    default:
                        return null;
                }
            }

            @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                try {
                    ArrayList arrayList = new ArrayList();
                    LocalMediaFolder localMediaFolder = new LocalMediaFolder();
                    ArrayList arrayList2 = new ArrayList();
                    if (cursor != null) {
                        if (cursor.getCount() <= 0) {
                            localMediaLoadListener.loadComplete(arrayList);
                            return;
                        }
                        cursor.moveToFirst();
                        do {
                            String string = cursor.getString(cursor.getColumnIndexOrThrow(LocalMediaLoader.PROJECTION[1]));
                            LocalMedia localMedia = new LocalMedia(string, cursor.getInt(cursor.getColumnIndexOrThrow(LocalMediaLoader.PROJECTION[5])), LocalMediaLoader.this.type, cursor.getString(cursor.getColumnIndexOrThrow(LocalMediaLoader.PROJECTION[2])), cursor.getInt(cursor.getColumnIndexOrThrow(LocalMediaLoader.PROJECTION[3])), cursor.getInt(cursor.getColumnIndexOrThrow(LocalMediaLoader.PROJECTION[4])));
                            LocalMediaFolder imageFolder = LocalMediaLoader.this.getImageFolder(string, arrayList);
                            imageFolder.getImages().add(localMedia);
                            imageFolder.setImageNum(imageFolder.getImageNum() + 1);
                            arrayList2.add(localMedia);
                            localMediaFolder.setImageNum(localMediaFolder.getImageNum() + 1);
                        } while (cursor.moveToNext());
                        if (arrayList2.size() > 0) {
                            LocalMediaLoader.this.sortFolder(arrayList);
                            arrayList.add(0, localMediaFolder);
                            localMediaFolder.setFirstImagePath(arrayList2.get(0).getPath());
                            localMediaFolder.setName(LocalMediaLoader.this.type == PictureMimeType.ofAudio() ? LocalMediaLoader.this.activity.getString(R.string.picture_all_audio) : LocalMediaLoader.this.activity.getString(R.string.picture_camera_roll));
                            localMediaFolder.setImages(arrayList2);
                        }
                        localMediaLoadListener.loadComplete(arrayList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
    }
}
