package com.luck.picture.lib.observable;

import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.tools.DebugUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ImagesObservable implements SubjectListener {
    private static ImagesObservable sObserver;
    private List<ObserverListener> observers = new ArrayList();
    private List<LocalMediaFolder> folders = new ArrayList();
    private List<LocalMedia> medias = new ArrayList();
    private List<LocalMedia> selectedImages = new ArrayList();

    private ImagesObservable() {
    }

    public static ImagesObservable getInstance() {
        if (sObserver == null) {
            synchronized (ImagesObservable.class) {
                if (sObserver == null) {
                    sObserver = new ImagesObservable();
                }
            }
        }
        return sObserver;
    }

    @Override // com.luck.picture.lib.observable.SubjectListener
    public void add(ObserverListener observerListener) {
        this.observers.add(observerListener);
    }

    public void clearLocalFolders() {
        if (this.folders != null) {
            this.folders.clear();
        }
    }

    public void clearLocalMedia() {
        if (this.medias != null) {
            this.medias.clear();
        }
        DebugUtil.i("ImagesObservable:", "clearLocalMedia success!");
    }

    public void clearSelectedLocalMedia() {
        if (this.selectedImages != null) {
            this.selectedImages.clear();
        }
    }

    public List<LocalMediaFolder> readLocalFolders() {
        if (this.folders == null) {
            this.folders = new ArrayList();
        }
        return this.folders;
    }

    public List<LocalMedia> readLocalMedias() {
        if (this.medias == null) {
            this.medias = new ArrayList();
        }
        return this.medias;
    }

    public List<LocalMedia> readSelectLocalMedias() {
        return this.selectedImages;
    }

    @Override // com.luck.picture.lib.observable.SubjectListener
    public void remove(ObserverListener observerListener) {
        if (this.observers.contains(observerListener)) {
            this.observers.remove(observerListener);
        }
    }

    public void saveLocalFolders(List<LocalMediaFolder> list) {
        if (list != null) {
            this.folders = list;
        }
    }

    public void saveLocalMedia(List<LocalMedia> list) {
        this.medias = list;
    }
}
