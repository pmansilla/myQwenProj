package no.nordicsemi.android.dfu.internal.manifest;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes2.dex */
public class Manifest {
    private FileInfo application;
    private FileInfo bootloader;

    @SerializedName("bootloader_application")
    private FileInfo bootloaderApplication;
    private FileInfo softdevice;

    @SerializedName("softdevice_application")
    private FileInfo softdeviceApplication;

    @SerializedName("softdevice_bootloader")
    private SoftDeviceBootloaderFileInfo softdeviceBootloader;

    @SerializedName("softdevice_bootloader_application")
    private FileInfo softdeviceBootloaderApplication;

    public FileInfo getApplicationInfo() {
        return this.application != null ? this.application : this.softdeviceApplication != null ? this.softdeviceApplication : this.bootloaderApplication != null ? this.bootloaderApplication : this.softdeviceBootloaderApplication;
    }

    public FileInfo getBootloaderInfo() {
        return this.bootloader;
    }

    public SoftDeviceBootloaderFileInfo getSoftdeviceBootloaderInfo() {
        return this.softdeviceBootloader;
    }

    public FileInfo getSoftdeviceInfo() {
        return this.softdevice;
    }

    public boolean isSecureDfuRequired() {
        return (this.bootloaderApplication == null && this.softdeviceApplication == null && this.softdeviceBootloaderApplication == null) ? false : true;
    }
}
