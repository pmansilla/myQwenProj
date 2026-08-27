package no.nordicsemi.android.support.v18.scanner;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/* loaded from: classes2.dex */
public final class ScanSettings implements Parcelable {
    public static final int CALLBACK_TYPE_ALL_MATCHES = 1;
    public static final int CALLBACK_TYPE_FIRST_MATCH = 2;
    public static final int CALLBACK_TYPE_MATCH_LOST = 4;
    public static final Parcelable.Creator<ScanSettings> CREATOR = new Parcelable.Creator<ScanSettings>() { // from class: no.nordicsemi.android.support.v18.scanner.ScanSettings.1
        @Override // android.os.Parcelable.Creator
        public ScanSettings createFromParcel(Parcel parcel) {
            return new ScanSettings(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public ScanSettings[] newArray(int i) {
            return new ScanSettings[i];
        }
    };
    public static final long MATCH_LOST_DEVICE_TIMEOUT_DEFAULT = 10000;
    public static final long MATCH_LOST_TASK_INTERVAL_DEFAULT = 10000;
    public static final int MATCH_MODE_AGGRESSIVE = 1;
    public static final int MATCH_MODE_STICKY = 2;
    public static final int MATCH_NUM_FEW_ADVERTISEMENT = 2;
    public static final int MATCH_NUM_MAX_ADVERTISEMENT = 3;
    public static final int MATCH_NUM_ONE_ADVERTISEMENT = 1;
    public static final int PHY_LE_ALL_SUPPORTED = 255;
    public static final int SCAN_MODE_BALANCED = 1;
    public static final int SCAN_MODE_LOW_LATENCY = 2;
    public static final int SCAN_MODE_LOW_POWER = 0;
    public static final int SCAN_MODE_OPPORTUNISTIC = -1;
    private int callbackType;
    private boolean legacy;
    private long matchLostDeviceTimeout;
    private long matchLostTaskInterval;
    private int matchMode;
    private int numOfMatchesPerFilter;
    private int phy;
    private final long powerSaveRestInterval;
    private final long powerSaveScanInterval;
    private long reportDelayMillis;
    private int scanMode;
    private boolean useHardwareBatchingIfSupported;
    private boolean useHardwareCallbackTypesIfSupported;
    private boolean useHardwareFilteringIfSupported;

    /* loaded from: classes2.dex */
    public static final class Builder {
        private int scanMode = 0;
        private int callbackType = 1;
        private long reportDelayMillis = 0;
        private int matchMode = 1;
        private int numOfMatchesPerFilter = 3;
        private boolean legacy = true;
        private int phy = 255;
        private boolean useHardwareFilteringIfSupported = true;
        private boolean useHardwareBatchingIfSupported = true;
        private boolean useHardwareCallbackTypesIfSupported = true;
        private long matchLostDeviceTimeout = 10000;
        private long matchLostTaskInterval = 10000;
        private long powerSaveRestInterval = 0;
        private long powerSaveScanInterval = 0;

        private boolean isValidCallbackType(int i) {
            return i == 1 || i == 2 || i == 4 || i == 6;
        }

        private void updatePowerSaveSettings() {
            switch (this.scanMode) {
                case 1:
                    this.powerSaveScanInterval = 2000L;
                    this.powerSaveRestInterval = 3000L;
                    return;
                case 2:
                    this.powerSaveScanInterval = 0L;
                    this.powerSaveRestInterval = 0L;
                    return;
                default:
                    this.powerSaveScanInterval = 500L;
                    this.powerSaveRestInterval = 4500L;
                    return;
            }
        }

        @NonNull
        public ScanSettings build() {
            if (this.powerSaveRestInterval == 0 && this.powerSaveScanInterval == 0) {
                updatePowerSaveSettings();
            }
            return new ScanSettings(this.scanMode, this.callbackType, this.reportDelayMillis, this.matchMode, this.numOfMatchesPerFilter, this.legacy, this.phy, this.useHardwareFilteringIfSupported, this.useHardwareBatchingIfSupported, this.useHardwareCallbackTypesIfSupported, this.matchLostDeviceTimeout, this.matchLostTaskInterval, this.powerSaveScanInterval, this.powerSaveRestInterval);
        }

        @NonNull
        public Builder setCallbackType(int i) {
            if (isValidCallbackType(i)) {
                this.callbackType = i;
                return this;
            }
            throw new IllegalArgumentException("invalid callback type - " + i);
        }

        @NonNull
        public Builder setLegacy(boolean z) {
            this.legacy = z;
            return this;
        }

        @NonNull
        public Builder setMatchMode(int i) {
            if (i >= 1 && i <= 2) {
                this.matchMode = i;
                return this;
            }
            throw new IllegalArgumentException("invalid matchMode " + i);
        }

        @NonNull
        public Builder setMatchOptions(long j, long j2) {
            if (j <= 0 || j2 <= 0) {
                throw new IllegalArgumentException("maxDeviceAgeMillis and taskIntervalMillis must be > 0");
            }
            this.matchLostDeviceTimeout = j;
            this.matchLostTaskInterval = j2;
            return this;
        }

        @NonNull
        public Builder setNumOfMatches(int i) {
            if (i >= 1 && i <= 3) {
                this.numOfMatchesPerFilter = i;
                return this;
            }
            throw new IllegalArgumentException("invalid numOfMatches " + i);
        }

        @NonNull
        public Builder setPhy(int i) {
            this.phy = i;
            return this;
        }

        @NonNull
        public Builder setPowerSave(long j, long j2) {
            if (j <= 0 || j2 <= 0) {
                throw new IllegalArgumentException("scanInterval and restInterval must be > 0");
            }
            this.powerSaveScanInterval = j;
            this.powerSaveRestInterval = j2;
            return this;
        }

        @NonNull
        public Builder setReportDelay(long j) {
            if (j < 0) {
                throw new IllegalArgumentException("reportDelay must be > 0");
            }
            this.reportDelayMillis = j;
            return this;
        }

        @NonNull
        public Builder setScanMode(int i) {
            if (i >= -1 && i <= 2) {
                this.scanMode = i;
                return this;
            }
            throw new IllegalArgumentException("invalid scan mode " + i);
        }

        @NonNull
        public Builder setUseHardwareBatchingIfSupported(boolean z) {
            this.useHardwareBatchingIfSupported = z;
            return this;
        }

        @NonNull
        public Builder setUseHardwareCallbackTypesIfSupported(boolean z) {
            this.useHardwareCallbackTypesIfSupported = z;
            return this;
        }

        @NonNull
        public Builder setUseHardwareFilteringIfSupported(boolean z) {
            this.useHardwareFilteringIfSupported = z;
            return this;
        }
    }

    private ScanSettings(int i, int i2, long j, int i3, int i4, boolean z, int i5, boolean z2, boolean z3, boolean z4, long j2, long j3, long j4, long j5) {
        this.scanMode = i;
        this.callbackType = i2;
        this.reportDelayMillis = j;
        this.numOfMatchesPerFilter = i4;
        this.matchMode = i3;
        this.legacy = z;
        this.phy = i5;
        this.useHardwareFilteringIfSupported = z2;
        this.useHardwareBatchingIfSupported = z3;
        this.useHardwareCallbackTypesIfSupported = z4;
        this.matchLostDeviceTimeout = 1000000 * j2;
        this.matchLostTaskInterval = j3;
        this.powerSaveScanInterval = j4;
        this.powerSaveRestInterval = j5;
    }

    private ScanSettings(Parcel parcel) {
        this.scanMode = parcel.readInt();
        this.callbackType = parcel.readInt();
        this.reportDelayMillis = parcel.readLong();
        this.matchMode = parcel.readInt();
        this.numOfMatchesPerFilter = parcel.readInt();
        this.legacy = parcel.readInt() != 0;
        this.phy = parcel.readInt();
        this.useHardwareFilteringIfSupported = parcel.readInt() == 1;
        this.useHardwareBatchingIfSupported = parcel.readInt() == 1;
        this.powerSaveScanInterval = parcel.readLong();
        this.powerSaveRestInterval = parcel.readLong();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disableUseHardwareCallbackTypes() {
        this.useHardwareCallbackTypesIfSupported = false;
    }

    public int getCallbackType() {
        return this.callbackType;
    }

    public boolean getLegacy() {
        return this.legacy;
    }

    public long getMatchLostDeviceTimeout() {
        return this.matchLostDeviceTimeout;
    }

    public long getMatchLostTaskInterval() {
        return this.matchLostTaskInterval;
    }

    public int getMatchMode() {
        return this.matchMode;
    }

    public int getNumOfMatches() {
        return this.numOfMatchesPerFilter;
    }

    public int getPhy() {
        return this.phy;
    }

    public long getPowerSaveRest() {
        return this.powerSaveRestInterval;
    }

    public long getPowerSaveScan() {
        return this.powerSaveScanInterval;
    }

    public long getReportDelayMillis() {
        return this.reportDelayMillis;
    }

    public int getScanMode() {
        return this.scanMode;
    }

    public boolean getUseHardwareBatchingIfSupported() {
        return this.useHardwareBatchingIfSupported;
    }

    public boolean getUseHardwareCallbackTypesIfSupported() {
        return this.useHardwareCallbackTypesIfSupported;
    }

    public boolean getUseHardwareFilteringIfSupported() {
        return this.useHardwareFilteringIfSupported;
    }

    public boolean hasPowerSaveMode() {
        return this.powerSaveRestInterval > 0 && this.powerSaveScanInterval > 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.scanMode);
        parcel.writeInt(this.callbackType);
        parcel.writeLong(this.reportDelayMillis);
        parcel.writeInt(this.matchMode);
        parcel.writeInt(this.numOfMatchesPerFilter);
        parcel.writeInt(this.legacy ? 1 : 0);
        parcel.writeInt(this.phy);
        parcel.writeInt(this.useHardwareFilteringIfSupported ? 1 : 0);
        parcel.writeInt(this.useHardwareBatchingIfSupported ? 1 : 0);
        parcel.writeLong(this.powerSaveScanInterval);
        parcel.writeLong(this.powerSaveRestInterval);
    }
}
