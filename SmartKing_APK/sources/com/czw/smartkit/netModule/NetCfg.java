package com.czw.smartkit.netModule;

/* loaded from: classes.dex */
public class NetCfg {
    public static final String URL1 = "http://sk.runchinaup.com/index.php/home/common/serviceAgree.html";
    public static final String URL2 = "http://sk.runchinaup.com/index.php/home/common/privacy.html";
    public static String domainUrl = "http://sk.runchinaup.com/index.php/home";

    private NetCfg() {
    }

    public static String getDomainUrl() {
        return domainUrl;
    }
}
