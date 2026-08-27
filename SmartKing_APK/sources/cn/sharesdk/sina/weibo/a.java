package cn.sharesdk.sina.weibo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.mob.MobSDK;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/* compiled from: SinaActivity.java */
/* loaded from: classes.dex */
public class a extends FakeActivity implements Handler.Callback {
    private long a = PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE;
    private boolean b;
    private String c;
    private Platform.ShareParams d;
    private AuthorizeListener e;

    private Bitmap a(Bitmap bitmap, double d) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        double sqrt = Math.sqrt(d);
        double d2 = width;
        Double.isNaN(d2);
        double d3 = height;
        Double.isNaN(d3);
        return Bitmap.createScaledBitmap(bitmap, (int) (d2 / sqrt), (int) (d3 / sqrt), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        Bundle bundle = new Bundle();
        bundle.putInt("_weibo_command_type", 1);
        bundle.putString("_weibo_transaction", String.valueOf(System.currentTimeMillis()));
        bundle.putLong("callbackId", 0L);
        if (!TextUtils.isEmpty(this.d.getText())) {
            bundle.putParcelable("_weibo_message_text", c());
            bundle.putString("_weibo_message_text_extra", "");
        }
        if (!TextUtils.isEmpty(this.d.getUrl())) {
            this.a = PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID;
            WebpageObject d = d();
            if (d.checkArgs()) {
                bundle.putParcelable("_weibo_message_media", d);
                String str = "";
                if (!TextUtils.isEmpty(d.defaultText)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(WebpageObject.EXTRA_KEY_DEFAULTTEXT, d.defaultText);
                    str = new Hashon().fromHashMap(hashMap);
                }
                bundle.putString("_weibo_message_media_extra", str);
            }
        } else if (this.d.getImageArray() != null && this.d.getImageArray().length > 0) {
            bundle.putParcelable("_weibo_message_multi_image", f());
        } else if (!TextUtils.isEmpty(this.d.getFilePath())) {
            bundle.putParcelable("_weibo_message_video_source", g());
        } else if (this.d.getImageData() != null || !TextUtils.isEmpty(this.d.getImagePath())) {
            this.a = PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE;
            ImageObject e = e();
            if (e.checkArgs()) {
                bundle.putParcelable("_weibo_message_image", e);
                bundle.putString("_weibo_message_image_extra", "");
            }
        }
        try {
            try {
                a(this.activity, g.a().b(), this.c, bundle);
            } catch (Throwable th) {
                if (this.e != null) {
                    this.e.onError(new Throwable(th));
                }
            }
        } catch (Throwable unused) {
            a(this.activity, "com.sina.weibog3", this.c, bundle);
        }
    }

    private void a(int i, String str) {
        switch (i) {
            case 0:
                if (this.e != null) {
                    this.e.onComplete(null);
                    break;
                }
                break;
            case 1:
                if (this.e != null) {
                    this.e.onCancel();
                    break;
                }
                break;
            case 2:
                if (this.e != null) {
                    this.e.onError(new Throwable(str));
                    break;
                }
                break;
        }
        finish();
    }

    private boolean a(Activity activity, String str, String str2, Bundle bundle) {
        if (activity == null || TextUtils.isEmpty("com.sina.weibo.sdk.action.ACTION_WEIBO_ACTIVITY") || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            cn.sharesdk.framework.utils.e.b().e("launchWeiboActivity fail, invalid arguments", new Object[0]);
            return false;
        }
        String packageName = activity.getPackageName();
        Intent intent = new Intent();
        intent.setPackage(str);
        intent.setAction("com.sina.weibo.sdk.action.ACTION_WEIBO_ACTIVITY");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("_weibo_sdkVersion", "0031405000");
        intent.putExtra("_weibo_appPackage", packageName);
        intent.putExtra("_weibo_appKey", str2);
        intent.putExtra("_weibo_flag", 538116905);
        intent.putExtra("_weibo_sign", cn.sharesdk.sina.weibo.sdk.a.a(activity, packageName));
        intent.putExtra("_weibo_transaction", String.valueOf(System.currentTimeMillis()));
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        try {
            cn.sharesdk.framework.utils.e.b().d("launchWeiboActivity intent=" + intent + ", extra=" + intent.getExtras(), new Object[0]);
            startActivityForResult(intent, 765);
            return true;
        } catch (ActivityNotFoundException e) {
            cn.sharesdk.framework.utils.e.b().e(e.getMessage(), new Object[0]);
            return false;
        }
    }

    private byte[] a(Context context, Bitmap bitmap) throws Throwable {
        if (bitmap == null) {
            throw new RuntimeException("checkArgs fail, thumbData is null");
        }
        if (bitmap.isRecycled()) {
            throw new RuntimeException("checkArgs fail, thumbData is recycled");
        }
        return b(context, bitmap);
    }

    private byte[] a(Context context, String str) throws Throwable {
        if (new File(str).exists()) {
            return b(context, BitmapHelper.getBitmap(str));
        }
        throw new FileNotFoundException();
    }

    private String b() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private byte[] b(Context context, Bitmap bitmap) throws Throwable {
        if (bitmap == null) {
            throw new RuntimeException("checkArgs fail, thumbData is null");
        }
        if (bitmap.isRecycled()) {
            throw new RuntimeException("checkArgs fail, thumbData is recycled");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        int length = byteArray.length;
        while (length > this.a) {
            double d = length;
            double d2 = this.a;
            Double.isNaN(d);
            Double.isNaN(d2);
            bitmap = a(bitmap, d / d2);
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream2);
            byteArrayOutputStream2.flush();
            byteArrayOutputStream2.close();
            byteArray = byteArrayOutputStream2.toByteArray();
            length = byteArray.length;
        }
        return byteArray;
    }

    private TextObject c() {
        TextObject textObject = new TextObject();
        textObject.text = this.d.getText();
        return textObject;
    }

    private WebpageObject d() {
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.identify = b();
        webpageObject.title = this.d.getTitle();
        webpageObject.description = this.d.getText();
        webpageObject.actionUrl = this.d.getUrl();
        webpageObject.defaultText = this.d.getText();
        try {
            if (this.d.getImageData() != null) {
                webpageObject.thumbData = a(this.activity, this.d.getImageData());
            } else if (!TextUtils.isEmpty(this.d.getImagePath())) {
                webpageObject.thumbData = a(this.activity, this.d.getImagePath());
            }
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            webpageObject.setThumbImage(null);
        }
        return webpageObject;
    }

    private ImageObject e() {
        ImageObject imageObject = new ImageObject();
        try {
            if (this.d.getImageData() != null) {
                imageObject.imageData = a(this.activity, this.d.getImageData());
            } else if (!TextUtils.isEmpty(this.d.getImagePath())) {
                DeviceHelper deviceHelper = DeviceHelper.getInstance(this.activity);
                try {
                    if (deviceHelper.getSdcardState() && this.d.getImagePath().startsWith(deviceHelper.getSdcardPath())) {
                        File file = new File(this.d.getImagePath());
                        if (file.exists() && file.length() != 0 && file.length() < 10485760) {
                            imageObject.imagePath = this.d.getImagePath();
                            return imageObject;
                        }
                    }
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().d(th);
                }
                imageObject.imageData = a(this.activity, this.d.getImagePath());
            }
        } catch (Throwable th2) {
            cn.sharesdk.framework.utils.e.b().d(th2);
        }
        return imageObject;
    }

    private MultiImageObject f() {
        MultiImageObject multiImageObject = new MultiImageObject();
        multiImageObject.identify = b();
        multiImageObject.title = this.d.getTitle();
        multiImageObject.description = this.d.getText();
        multiImageObject.actionUrl = this.d.getUrl();
        multiImageObject.defaultText = this.d.getText();
        try {
            if (this.d.getImageData() != null) {
                multiImageObject.thumbData = a(this.activity, this.d.getImageData());
            } else if (!TextUtils.isEmpty(this.d.getImagePath())) {
                multiImageObject.thumbData = a(this.activity, this.d.getImagePath());
            }
            List asList = Arrays.asList(this.d.getImageArray());
            ArrayList<Uri> arrayList = new ArrayList<>();
            Iterator it = asList.iterator();
            while (it.hasNext()) {
                File file = new File((String) it.next());
                if (file.exists()) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        arrayList.add(ResHelper.pathToContentUri(MobSDK.getContext(), file.getAbsolutePath()));
                    } else {
                        arrayList.add(Uri.fromFile(file));
                    }
                }
            }
            multiImageObject.setImageList(arrayList);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            multiImageObject.setThumbImage(null);
        }
        return multiImageObject;
    }

    private VideoSourceObject g() {
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        videoSourceObject.identify = b();
        videoSourceObject.title = this.d.getTitle();
        videoSourceObject.description = this.d.getText();
        videoSourceObject.actionUrl = this.d.getUrl();
        videoSourceObject.defaultText = this.d.getText();
        try {
            if (this.d.getImageData() != null) {
                videoSourceObject.thumbData = a(this.activity, this.d.getImageData());
            } else if (!TextUtils.isEmpty(this.d.getImagePath())) {
                videoSourceObject.thumbData = a(this.activity, this.d.getImagePath());
            }
            Uri uri = null;
            String filePath = this.d.getFilePath();
            File file = new File(filePath);
            if (file.exists()) {
                if (filePath.startsWith("/data/")) {
                    File file2 = new File(ResHelper.getCachePath(MobSDK.getContext(), "videos"), System.currentTimeMillis() + file.getName());
                    String absolutePath = file2.getAbsolutePath();
                    file2.createNewFile();
                    if (ResHelper.copyFile(filePath, absolutePath)) {
                        file = file2;
                    }
                }
                uri = Uri.fromFile(file);
            }
            videoSourceObject.videoPath = uri;
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
        return videoSourceObject;
    }

    public void a(Platform.ShareParams shareParams) {
        this.d = shareParams;
    }

    public void a(AuthorizeListener authorizeListener) {
        this.e = authorizeListener;
    }

    public void a(String str) {
        this.c = str;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what != 1) {
            return false;
        }
        if (!this.b && this.e != null) {
            this.e.onCancel();
        }
        finish();
        return false;
    }

    @Override // com.mob.tools.FakeActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        cn.sharesdk.framework.utils.e.b().d("sina activity requestCode = %s, resultCode = %s", Integer.valueOf(i), Integer.valueOf(i));
        finish();
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        UIHandler.sendEmptyMessageDelayed(1, 700L, new Handler.Callback() { // from class: cn.sharesdk.sina.weibo.a.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                a.this.a();
                return true;
            }
        });
    }

    @Override // com.mob.tools.FakeActivity
    public void onNewIntent(Intent intent) {
        this.b = true;
        Bundle extras = intent.getExtras();
        cn.sharesdk.framework.utils.e.b().i("onNewIntent ==>>", extras.toString());
        String stringExtra = intent.getStringExtra("_weibo_appPackage");
        String stringExtra2 = intent.getStringExtra("_weibo_transaction");
        if (TextUtils.isEmpty(stringExtra)) {
            cn.sharesdk.framework.utils.e.b().e("handleWeiboResponse faild appPackage is null", new Object[0]);
            return;
        }
        String callingPackage = this.activity.getCallingPackage();
        cn.sharesdk.framework.utils.e.b().d("handleWeiboResponse getCallingPackage : " + callingPackage, new Object[0]);
        if (TextUtils.isEmpty(stringExtra2)) {
            cn.sharesdk.framework.utils.e.b().e("handleWeiboResponse faild intent _weibo_transaction is null", new Object[0]);
        } else if (g.a(stringExtra) || stringExtra.equals(this.activity.getPackageName())) {
            a(extras.getInt("_weibo_resp_errcode"), extras.getString("_weibo_resp_errstr"));
        } else {
            cn.sharesdk.framework.utils.e.b().e("handleWeiboResponse faild appPackage validateSign faild", new Object[0]);
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onStop() {
        super.onStop();
    }
}
