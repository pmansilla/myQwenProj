package cn.sharesdk.framework.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.luck.picture.lib.config.PictureMimeType;
import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: ShareBypassApproval.java */
/* loaded from: classes.dex */
public class f {
    private String a;
    private String b;
    private boolean c = true;

    public void a(Platform.ShareParams shareParams, Platform platform) throws Throwable {
        Intent intent = new Intent();
        String imagePath = shareParams.getImagePath();
        String imageUrl = shareParams.getImageUrl();
        List<String> arrayList = new ArrayList();
        if (shareParams.getImageArray() != null) {
            arrayList = Arrays.asList(shareParams.getImageArray());
        }
        String text = shareParams.getText();
        if (!TextUtils.isEmpty(text)) {
            String shortLintk = platform.getShortLintk(text, false);
            shareParams.setText(shortLintk);
            intent.putExtra("android.intent.extra.TEXT", shortLintk);
            intent.putExtra("Kdescription", shortLintk);
        }
        if (arrayList == null || arrayList.size() <= 0) {
            if (TextUtils.isEmpty(imagePath) || !new File(imagePath).exists()) {
                Bitmap imageData = shareParams.getImageData();
                if (imageData != null && !imageData.isRecycled()) {
                    File file = new File(ResHelper.getCachePath(MobSDK.getContext(), "images"), System.currentTimeMillis() + PictureMimeType.PNG);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    imageData.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    imagePath = file.getAbsolutePath();
                } else if (!TextUtils.isEmpty(imageUrl)) {
                    imagePath = BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl);
                }
            }
            if (!TextUtils.isEmpty(imagePath)) {
                arrayList.add(imagePath);
            }
        }
        ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>();
        for (String str : arrayList) {
            if (str.startsWith("http")) {
                str = BitmapHelper.downloadBitmap(MobSDK.getContext(), str);
            }
            File file2 = new File(str);
            if (file2.exists()) {
                if (str.startsWith("/data/")) {
                    File file3 = new File(ResHelper.getCachePath(MobSDK.getContext(), "images"), System.currentTimeMillis() + file2.getName());
                    String absolutePath = file3.getAbsolutePath();
                    file3.createNewFile();
                    if (ResHelper.copyFile(str, absolutePath)) {
                        file2 = file3;
                    }
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    arrayList2.add(ResHelper.pathToContentUri(MobSDK.getContext(), file2.getAbsolutePath()));
                } else {
                    arrayList2.add(Uri.fromFile(file2));
                }
            }
        }
        if (arrayList2.size() <= 0) {
            intent.setAction("android.intent.action.SEND");
            intent.setType("text/plain");
        } else if (arrayList2.size() != 1 || arrayList2.get(0) == null) {
            intent.setAction("android.intent.action.SEND_MULTIPLE");
            intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList2);
            intent.setType("image/*");
        } else {
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", arrayList2.get(0));
            String contentTypeFor = URLConnection.getFileNameMap().getContentTypeFor(((Uri) arrayList2.get(0)).toString());
            if (contentTypeFor == null || contentTypeFor.length() <= 0) {
                contentTypeFor = "image/*";
            }
            intent.setType(contentTypeFor);
        }
        if (TextUtils.isEmpty(this.b)) {
            intent.setPackage(this.a);
        } else {
            intent.setClassName(this.a, this.b);
        }
        intent.addFlags(335544320);
        try {
            MobSDK.getContext().startActivity(intent);
        } catch (Throwable th) {
            e.b().d(th);
        }
    }

    public void a(String str) {
        this.a = str;
        this.b = "";
    }

    public void a(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public void b(String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        if (str.endsWith("mp4") || str.endsWith("mkv")) {
            intent.setType("video/*");
        }
        intent.setPackage("com.ss.android.ugc.aweme");
        if (Build.VERSION.SDK_INT >= 24) {
            String contentTypeFor = URLConnection.getFileNameMap().getContentTypeFor(str);
            if ((contentTypeFor == null || contentTypeFor.length() <= 0) && (str.endsWith("mp4") || str.endsWith("mkv"))) {
                contentTypeFor = "video/*";
            }
            intent.putExtra("android.intent.extra.STREAM", ResHelper.getMediaUri(MobSDK.getContext(), str, contentTypeFor));
        } else {
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(str)));
        }
        try {
            intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
            MobSDK.getContext().startActivity(intent);
        } catch (Throwable th) {
            e.b().d(th);
        }
    }
}
