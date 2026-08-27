package com.czw.smartkit.net.domain;

import android.text.TextUtils;
import com.czw.smartkit.BaseCfg;

/* loaded from: classes.dex */
public class FirmwareDTO {
    private String add_date;
    private Object az_pic;
    private String chi_name;
    private String download_url;
    private String file_name;
    private String id;
    private String ios_down_url;
    private Object ios_pic;
    private String name;
    private String note_log;
    private String remark;
    private String size;
    private String version;

    public static boolean canUpdateFirmare(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        return BaseCfg.isAllowSameVersionOTA || Integer.valueOf(str2.replaceAll("\\.", "")).intValue() > Integer.valueOf(str.replaceAll("\\.", "")).intValue();
    }

    public String getAdd_date() {
        return this.add_date;
    }

    public Object getAz_pic() {
        return this.az_pic;
    }

    public String getChi_name() {
        return this.chi_name;
    }

    public String getDownload_url() {
        return this.download_url;
    }

    public String getFile_name() {
        return this.file_name;
    }

    public String getId() {
        return this.id;
    }

    public String getIos_down_url() {
        return this.ios_down_url;
    }

    public Object getIos_pic() {
        return this.ios_pic;
    }

    public String getName() {
        return this.name;
    }

    public String getNote_log() {
        return this.note_log;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getSize() {
        return this.size;
    }

    public String getVersion() {
        return this.version;
    }

    public void setAdd_date(String str) {
        this.add_date = str;
    }

    public void setAz_pic(Object obj) {
        this.az_pic = obj;
    }

    public void setChi_name(String str) {
        this.chi_name = str;
    }

    public void setDownload_url(String str) {
        this.download_url = str;
    }

    public void setFile_name(String str) {
        this.file_name = str;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setIos_down_url(String str) {
        this.ios_down_url = str;
    }

    public void setIos_pic(Object obj) {
        this.ios_pic = obj;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNote_log(String str) {
        this.note_log = str;
    }

    public void setRemark(String str) {
        this.remark = str;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public void setVersion(String str) {
        this.version = str;
    }
}
