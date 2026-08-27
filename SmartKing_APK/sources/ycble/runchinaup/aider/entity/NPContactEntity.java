package ycble.runchinaup.aider.entity;

/* loaded from: classes2.dex */
public class NPContactEntity {
    private String data1;
    private String data4;
    private String display_name;
    private String display_name_alt;

    public NPContactEntity(String str, String str2, String str3, String str4) {
        this.display_name_alt = str;
        this.display_name = str2;
        this.data1 = str3;
        this.data4 = str4;
    }

    public String getData1() {
        return this.data1;
    }

    public String getData4() {
        return this.data4;
    }

    public String getDisplay_name() {
        return this.display_name;
    }

    public String getDisplay_name_alt() {
        return this.display_name_alt;
    }

    public void setData1(String str) {
        this.data1 = str;
    }

    public void setData4(String str) {
        this.data4 = str;
    }

    public void setDisplay_name(String str) {
        this.display_name = str;
    }

    public void setDisplay_name_alt(String str) {
        this.display_name_alt = str;
    }

    public String toString() {
        return "NPContactInfo{display_name_alt='" + this.display_name_alt + "', display_name='" + this.display_name + "', data1='" + this.data1 + "', data4='" + this.data4 + "'}";
    }
}
