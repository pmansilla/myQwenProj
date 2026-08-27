package com.luck.picture.lib.observable;

import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import java.util.List;

/* loaded from: classes.dex */
public interface ObserverListener {
    void observerUpFoldersData(List<LocalMediaFolder> list);

    void observerUpSelectsData(List<LocalMedia> list);
}
