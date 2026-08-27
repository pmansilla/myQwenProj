package me.panpf.sketch.optionsfilter;

import android.support.annotation.NonNull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import me.panpf.sketch.Configuration;
import me.panpf.sketch.request.DownloadOptions;

/* loaded from: classes2.dex */
public class OptionsFilterManager {
    private List<OptionsFilter> extrasFilters;
    private InPreferQualityOverSpeedOptionsFilter inPreferQualityOverSpeedOptionsFilter;
    private LowQualityOptionsFilter lowQualityOptionsFilter;
    private MobileDataPauseDownloadController mobileDataPauseDownloadController;
    private PauseDownloadOptionsFilter pauseDownloadOptionsFilter;
    private PauseLoadOptionsFilter pauseLoadOptionsFilter;

    @NonNull
    public OptionsFilterManager add(int i, @NonNull OptionsFilter optionsFilter) {
        if (optionsFilter != null) {
            if (this.extrasFilters == null) {
                this.extrasFilters = new LinkedList();
            }
            this.extrasFilters.add(i, optionsFilter);
        }
        return this;
    }

    @NonNull
    public OptionsFilterManager add(@NonNull OptionsFilter optionsFilter) {
        if (optionsFilter != null) {
            if (this.extrasFilters == null) {
                this.extrasFilters = new LinkedList();
            }
            this.extrasFilters.add(optionsFilter);
        }
        return this;
    }

    public void filter(@NonNull DownloadOptions downloadOptions) {
        if (downloadOptions == null) {
            return;
        }
        if (this.pauseLoadOptionsFilter != null) {
            this.pauseLoadOptionsFilter.filter(downloadOptions);
        }
        if (this.pauseDownloadOptionsFilter != null) {
            this.pauseDownloadOptionsFilter.filter(downloadOptions);
        }
        if (this.lowQualityOptionsFilter != null) {
            this.lowQualityOptionsFilter.filter(downloadOptions);
        }
        if (this.inPreferQualityOverSpeedOptionsFilter != null) {
            this.inPreferQualityOverSpeedOptionsFilter.filter(downloadOptions);
        }
        if (this.extrasFilters != null) {
            Iterator<OptionsFilter> it = this.extrasFilters.iterator();
            while (it.hasNext()) {
                it.next().filter(downloadOptions);
            }
        }
    }

    public boolean isInPreferQualityOverSpeedEnabled() {
        return this.inPreferQualityOverSpeedOptionsFilter != null;
    }

    public boolean isLowQualityImageEnabled() {
        return this.lowQualityOptionsFilter != null;
    }

    public boolean isMobileDataPauseDownloadEnabled() {
        return this.mobileDataPauseDownloadController != null && this.mobileDataPauseDownloadController.isOpened();
    }

    public boolean isPauseDownloadEnabled() {
        return this.pauseDownloadOptionsFilter != null;
    }

    public boolean isPauseLoadEnabled() {
        return this.pauseLoadOptionsFilter != null;
    }

    public boolean remove(@NonNull OptionsFilter optionsFilter) {
        return (optionsFilter == null || this.extrasFilters == null || !this.extrasFilters.remove(optionsFilter)) ? false : true;
    }

    public void setInPreferQualityOverSpeedEnabled(boolean z) {
        if (isInPreferQualityOverSpeedEnabled() != z) {
            this.inPreferQualityOverSpeedOptionsFilter = z ? new InPreferQualityOverSpeedOptionsFilter() : null;
        }
    }

    public void setLowQualityImageEnabled(boolean z) {
        if (isLowQualityImageEnabled() != z) {
            this.lowQualityOptionsFilter = z ? new LowQualityOptionsFilter() : null;
        }
    }

    public void setMobileDataPauseDownloadEnabled(Configuration configuration, boolean z) {
        if (isMobileDataPauseDownloadEnabled() != z) {
            if (z) {
                if (this.mobileDataPauseDownloadController == null) {
                    this.mobileDataPauseDownloadController = new MobileDataPauseDownloadController(configuration);
                }
                this.mobileDataPauseDownloadController.setOpened(true);
            } else if (this.mobileDataPauseDownloadController != null) {
                this.mobileDataPauseDownloadController.setOpened(false);
            }
        }
    }

    public void setPauseDownloadEnabled(boolean z) {
        if (isPauseDownloadEnabled() != z) {
            this.pauseDownloadOptionsFilter = z ? new PauseDownloadOptionsFilter() : null;
        }
    }

    public void setPauseLoadEnabled(boolean z) {
        if (isPauseLoadEnabled() != z) {
            this.pauseLoadOptionsFilter = z ? new PauseLoadOptionsFilter() : null;
        }
    }

    @NonNull
    public String toString() {
        return "OptionsFilterManager";
    }
}
