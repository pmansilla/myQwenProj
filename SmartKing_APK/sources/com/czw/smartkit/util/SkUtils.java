package com.czw.smartkit.util;

import android.content.Context;
import android.graphics.Bitmap;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import com.czw.smartkit.bleModule.data.UnitCfg;
import com.czw.smartkit.preferenceModule.SharePreferenceUnit;
import com.litesuits.orm.db.assit.SQLBuilder;

/* loaded from: classes.dex */
public class SkUtils {
    public static String froamtStrFroSleepData(String str) {
        return str.replace("\n", "").replace(SQLBuilder.BLANK, "").replace("\"", "");
    }

    public static String getHHMMSSTime(int i) {
        int i2 = i / 3600;
        return String.format("%02d:%02d:%02d", Integer.valueOf(i2), Integer.valueOf((i - (i2 * 3600)) / 60), Integer.valueOf(i % 60));
    }

    public static String getSleepTimeHtmlText(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<big>%02d</big>", Integer.valueOf(i / 60)));
        sb.append("<small>" + getString(R.string.hour) + "</small>");
        sb.append(String.format("<big>%02d</big>", Integer.valueOf(i % 60)));
        sb.append("<small>" + getString(R.string.minute) + "</small>");
        return sb.toString();
    }

    private static String getString(int i) {
        return MainApplication.getContext().getString(i);
    }

    public static String getTransTimeLem(String str, String str2) {
        String[] split = str.split(":");
        String[] split2 = str2.split(":");
        return getHHMMSSTime(((((Integer.valueOf(split2[0]).intValue() * 3600) + 0) + (Integer.valueOf(split2[1]).intValue() * 60)) + Integer.valueOf(split2[2]).intValue()) - ((((Integer.valueOf(split[0]).intValue() * 3600) + 0) + (Integer.valueOf(split[1]).intValue() * 60)) + Integer.valueOf(split[2]).intValue()));
    }

    public static int getTransTimeLenInt(String str, String str2) {
        String[] split = str.split(":");
        String[] split2 = str2.split(":");
        return ((((Integer.valueOf(split2[0]).intValue() * 3600) + 0) + (Integer.valueOf(split2[1]).intValue() * 60)) + Integer.valueOf(split2[2]).intValue()) - ((((Integer.valueOf(split[0]).intValue() * 3600) + 0) + (Integer.valueOf(split[1]).intValue() * 60)) + Integer.valueOf(split[2]).intValue());
    }

    public static boolean isChinaUnit() {
        UnitCfg read = SharePreferenceUnit.read();
        return read == null || read.getIndex() == 0;
    }

    public static String km2Mi(String str) {
        return String.format("%.2f", Double.valueOf(Double.valueOf(str).doubleValue() * 0.6213712d));
    }

    public static void showShare(Context context, Bitmap bitmap) {
        OnekeyShare onekeyShare = new OnekeyShare();
        onekeyShare.setSilent(true);
        onekeyShare.disableSSOWhenAuthorize();
        onekeyShare.setTitle(context.getString(R.string.app_name_sk));
        onekeyShare.setText(context.getString(R.string.app_name_sk));
        onekeyShare.setImagePath(TmpFileUtil.saveImage(bitmap));
        onekeyShare.setSite(context.getString(R.string.app_name_sk));
        onekeyShare.setSiteUrl(context.getString(R.string.app_name_sk));
        onekeyShare.setTitleUrl("http://sharesdk.cn");
        onekeyShare.show(context);
    }

    public static void showShareSk(Context context, Bitmap bitmap) {
        OnekeyShare onekeyShare = new OnekeyShare();
        onekeyShare.setSilent(true);
        onekeyShare.disableSSOWhenAuthorize();
        onekeyShare.setTitle(context.getString(R.string.app_name_sk));
        onekeyShare.setText(context.getString(R.string.app_name_sk));
        onekeyShare.setImagePath(TmpFileUtil.saveImage(bitmap));
        onekeyShare.setSite(context.getString(R.string.app_name_sk));
        onekeyShare.setTitleUrl("https://czw2014.1688.com");
        onekeyShare.show(context);
    }
}
