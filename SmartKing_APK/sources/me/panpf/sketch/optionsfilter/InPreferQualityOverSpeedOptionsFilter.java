package me.panpf.sketch.optionsfilter;

import me.panpf.sketch.request.DownloadOptions;
import me.panpf.sketch.request.LoadOptions;

/* loaded from: classes2.dex */
public class InPreferQualityOverSpeedOptionsFilter implements OptionsFilter {
    @Override // me.panpf.sketch.optionsfilter.OptionsFilter
    public void filter(DownloadOptions downloadOptions) {
        if (downloadOptions instanceof LoadOptions) {
            ((LoadOptions) downloadOptions).setInPreferQualityOverSpeed(true);
        }
    }
}
