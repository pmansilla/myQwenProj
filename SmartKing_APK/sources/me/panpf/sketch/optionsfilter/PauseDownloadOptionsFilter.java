package me.panpf.sketch.optionsfilter;

import me.panpf.sketch.request.DownloadOptions;
import me.panpf.sketch.request.LoadOptions;
import me.panpf.sketch.request.RequestLevel;

/* loaded from: classes2.dex */
public class PauseDownloadOptionsFilter implements OptionsFilter {
    @Override // me.panpf.sketch.optionsfilter.OptionsFilter
    public void filter(DownloadOptions downloadOptions) {
        if ((downloadOptions instanceof LoadOptions) && downloadOptions.getRequestLevel() == null) {
            downloadOptions.setRequestLevel(RequestLevel.LOCAL);
        }
    }
}
