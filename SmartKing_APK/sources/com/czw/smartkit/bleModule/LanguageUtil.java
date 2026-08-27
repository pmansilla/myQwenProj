package com.czw.smartkit.bleModule;

import com.czw.utils.LogUtil;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import java.util.Locale;

/* loaded from: classes.dex */
public class LanguageUtil {
    private LanguageUtil() {
    }

    public static byte getLanguageCode() {
        Locale locale = Locale.getDefault();
        String locale2 = locale.toString();
        LogUtil.e("语言:->locale/" + locale.getLanguage());
        LogUtil.e("语言:->stringLocal/" + locale.toString());
        if (locale2.contains("zh_CN")) {
            return (byte) 1;
        }
        if (locale2.contains("en_US")) {
            return (byte) 0;
        }
        if (locale2.contains("es_ES")) {
            return (byte) 2;
        }
        if (locale2.contains("pt_PT")) {
            return (byte) 3;
        }
        if (locale2.contains("fr_FR")) {
            return (byte) 4;
        }
        if (locale2.contains("de_DE")) {
            return (byte) 5;
        }
        if (locale2.contains("ja_JP")) {
            return (byte) 6;
        }
        if (locale2.contains("ru_RU")) {
            return (byte) 7;
        }
        if (locale2.contains("pl_PL")) {
            return (byte) 8;
        }
        if (locale2.contains("ko_KR")) {
            return (byte) 9;
        }
        if (locale2.contains("zh_TW") || locale2.contains("zh_HK")) {
            return (byte) 10;
        }
        if (locale2.contains("ar_EG")) {
            return FileDownloadStatus.toFileDownloadService;
        }
        if (locale2.contains("th_TH")) {
            return (byte) 12;
        }
        if (locale2.contains("ms_MY")) {
            return (byte) 13;
        }
        return locale2.contains("in_ID") ? (byte) 14 : (byte) 0;
    }
}
