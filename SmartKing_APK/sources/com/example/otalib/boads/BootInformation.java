package com.example.otalib.boads;

/* loaded from: classes.dex */
public class BootInformation {
    public byte[] magic_number = new byte[4];
    public byte[] boot_info_data = new byte[4];
    public byte[] app_offset = new byte[4];
    public byte[] app_len = new byte[4];
    public byte[] app_load_addr = new byte[4];
    public byte[] app_exe_addr = new byte[4];
    public byte[] reserve_1 = new byte[2];
    public byte[] app_img_crc = new byte[2];
    public byte[] profile_offset = new byte[4];
    public byte[] profile_len = new byte[4];
    public byte[] reserve_2 = new byte[2];
    public byte[] profile_crc = new byte[2];
    public byte[] cfg_offset = new byte[4];
    public byte[] cfg_len = new byte[4];
    public byte[] patch_offset = new byte[4];
    public byte[] patch_len = new byte[4];
    public byte[] reserve_3 = new byte[2];
    public byte[] patch_crc = new byte[2];
    public byte[] reserve_4 = new byte[66];
    public byte[] boot_info_crc = new byte[2];
    public byte[] boot_data = new byte[getBootInfomationSize()];

    public BootInformation() {
        for (int i = 0; i < this.boot_data.length; i++) {
            this.boot_data[i] = -1;
        }
    }

    public int getBootInfomationSize() {
        return 128;
    }

    public byte[] get_boot_info() {
        System.arraycopy(this.magic_number, 0, this.boot_data, 0, this.magic_number.length);
        int length = this.magic_number.length + 0;
        System.arraycopy(this.boot_info_data, 0, this.boot_data, length, this.boot_info_data.length);
        int length2 = length + this.boot_info_data.length;
        System.arraycopy(this.app_offset, 0, this.boot_data, length2, this.app_offset.length);
        int length3 = length2 + this.app_offset.length;
        System.arraycopy(this.app_len, 0, this.boot_data, length3, this.app_len.length);
        int length4 = length3 + this.app_len.length;
        System.arraycopy(this.app_load_addr, 0, this.boot_data, length4, this.app_load_addr.length);
        int length5 = length4 + this.app_load_addr.length;
        System.arraycopy(this.app_exe_addr, 0, this.boot_data, length5, this.app_exe_addr.length);
        int length6 = length5 + this.app_exe_addr.length;
        System.arraycopy(this.reserve_1, 0, this.boot_data, length6, this.reserve_1.length);
        int length7 = length6 + this.reserve_1.length;
        System.arraycopy(this.app_img_crc, 0, this.boot_data, length7, this.app_img_crc.length);
        int length8 = length7 + this.app_img_crc.length;
        System.arraycopy(this.profile_offset, 0, this.boot_data, length8, this.profile_offset.length);
        int length9 = length8 + this.profile_offset.length;
        System.arraycopy(this.profile_len, 0, this.boot_data, length9, this.profile_len.length);
        int length10 = length9 + this.profile_len.length;
        System.arraycopy(this.reserve_2, 0, this.boot_data, length10, this.reserve_2.length);
        int length11 = length10 + this.reserve_2.length;
        System.arraycopy(this.profile_crc, 0, this.boot_data, length11, this.profile_crc.length);
        int length12 = length11 + this.profile_crc.length;
        System.arraycopy(this.cfg_offset, 0, this.boot_data, length12, this.cfg_offset.length);
        int length13 = length12 + this.cfg_offset.length;
        System.arraycopy(this.cfg_len, 0, this.boot_data, length13, this.cfg_len.length);
        int length14 = length13 + this.cfg_len.length;
        System.arraycopy(this.patch_offset, 0, this.boot_data, length14, this.patch_offset.length);
        int length15 = length14 + this.patch_offset.length;
        System.arraycopy(this.patch_len, 0, this.boot_data, length15, this.patch_len.length);
        int length16 = length15 + this.patch_len.length;
        System.arraycopy(this.reserve_3, 0, this.boot_data, length16, this.reserve_3.length);
        int length17 = length16 + this.reserve_3.length;
        System.arraycopy(this.patch_crc, 0, this.boot_data, length17, this.patch_crc.length);
        int length18 = length17 + this.patch_crc.length;
        System.arraycopy(this.reserve_4, 0, this.boot_data, length18, this.reserve_4.length);
        System.arraycopy(this.boot_info_crc, 0, this.boot_data, length18 + this.reserve_4.length, this.boot_info_crc.length);
        int length19 = this.boot_info_crc.length;
        return this.boot_data;
    }

    public void set_boot_info() {
        System.arraycopy(this.boot_data, 0, this.magic_number, 0, this.magic_number.length);
        int length = this.magic_number.length + 0;
        System.arraycopy(this.boot_data, length, this.boot_info_data, 0, this.boot_info_data.length);
        int length2 = length + this.boot_info_data.length;
        System.arraycopy(this.boot_data, length2, this.app_offset, 0, this.app_offset.length);
        int length3 = length2 + this.app_offset.length;
        System.arraycopy(this.boot_data, length3, this.app_len, 0, this.app_len.length);
        int length4 = length3 + this.app_len.length;
        System.arraycopy(this.boot_data, length4, this.app_load_addr, 0, this.app_load_addr.length);
        int length5 = length4 + this.app_load_addr.length;
        System.arraycopy(this.boot_data, length5, this.app_exe_addr, 0, this.app_exe_addr.length);
        int length6 = length5 + this.app_exe_addr.length;
        System.arraycopy(this.boot_data, length6, this.reserve_1, 0, this.reserve_1.length);
        int length7 = length6 + this.reserve_1.length;
        System.arraycopy(this.boot_data, length7, this.app_img_crc, 0, this.app_img_crc.length);
        int length8 = length7 + this.app_img_crc.length;
        System.arraycopy(this.boot_data, length8, this.profile_offset, 0, this.profile_offset.length);
        int length9 = length8 + this.profile_offset.length;
        System.arraycopy(this.boot_data, length9, this.profile_len, 0, this.profile_len.length);
        int length10 = length9 + this.profile_len.length;
        System.arraycopy(this.boot_data, length10, this.reserve_2, 0, this.reserve_2.length);
        int length11 = length10 + this.reserve_2.length;
        System.arraycopy(this.boot_data, length11, this.profile_crc, 0, this.profile_crc.length);
        int length12 = length11 + this.profile_crc.length;
        System.arraycopy(this.boot_data, length12, this.cfg_offset, 0, this.cfg_offset.length);
        int length13 = length12 + this.cfg_offset.length;
        System.arraycopy(this.boot_data, length13, this.cfg_len, 0, this.cfg_len.length);
        int length14 = length13 + this.cfg_len.length;
        System.arraycopy(this.boot_data, length14, this.patch_offset, 0, this.patch_offset.length);
        int length15 = length14 + this.patch_offset.length;
        System.arraycopy(this.boot_data, length15, this.patch_len, 0, this.patch_len.length);
        int length16 = length15 + this.patch_len.length;
        System.arraycopy(this.boot_data, length16, this.reserve_3, 0, this.reserve_3.length);
        int length17 = length16 + this.reserve_3.length;
        System.arraycopy(this.boot_data, length17, this.patch_crc, 0, this.patch_crc.length);
        int length18 = length17 + this.patch_crc.length;
        System.arraycopy(this.boot_data, length18, this.reserve_4, 0, this.reserve_4.length);
        System.arraycopy(this.boot_data, length18 + this.reserve_4.length, this.boot_info_crc, 0, this.boot_info_crc.length);
        int length19 = this.boot_info_crc.length;
    }
}
