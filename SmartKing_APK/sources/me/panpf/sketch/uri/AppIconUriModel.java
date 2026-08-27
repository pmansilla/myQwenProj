package me.panpf.sketch.uri;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class AppIconUriModel extends AbsBitmapDiskCacheUriModel {
    private static final String NAME = "AppIconUriModel";
    public static final String SCHEME = "app.icon://";

    public static String makeUri(String str, int i) {
        return SCHEME + str + FileUriModel.SCHEME + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // me.panpf.sketch.uri.AbsDiskCacheUriModel
    @NonNull
    public Bitmap getContent(@NonNull Context context, @NonNull String str) throws GetDataSourceException {
        Uri parse = Uri.parse(str);
        String host = parse.getHost();
        String path = parse.getPath();
        if (path != null && path.startsWith(FileUriModel.SCHEME)) {
            path = path.substring(1);
        }
        try {
            int intValue = Integer.valueOf(path).intValue();
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(host, 0);
                if (packageInfo.versionCode != intValue) {
                    String format = String.format("App versionCode mismatch, %d != %d. %s", Integer.valueOf(packageInfo.versionCode), Integer.valueOf(intValue), str);
                    SLog.e(NAME, format);
                    throw new GetDataSourceException(format);
                }
                Bitmap readApkIcon = SketchUtils.readApkIcon(context, packageInfo.applicationInfo.sourceDir, false, NAME, Sketch.with(context).getConfiguration().getBitmapPool());
                if (readApkIcon != null && !readApkIcon.isRecycled()) {
                    return readApkIcon;
                }
                String format2 = String.format("App icon bitmap invalid. %s", str);
                SLog.e(NAME, format2);
                throw new GetDataSourceException(format2);
            } catch (PackageManager.NameNotFoundException e) {
                String format3 = String.format("Not found PackageInfo by \"%s\". %s", host, str);
                SLog.e(NAME, e, format3);
                throw new GetDataSourceException(format3, e);
            }
        } catch (NumberFormatException e2) {
            String format4 = String.format("Conversion app versionCode failed. %s", str);
            SLog.e(NAME, e2, format4);
            throw new GetDataSourceException(format4, e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.UriModel
    public boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
