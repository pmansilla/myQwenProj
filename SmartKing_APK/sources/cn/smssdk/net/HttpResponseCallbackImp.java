package cn.smssdk.net;

import android.support.v4.app.NotificationCompat;
import com.amap.location.common.model.AmapLoc;
import com.mob.tools.network.HttpConnection;
import com.mob.tools.network.HttpResponseCallback;
import com.mob.tools.utils.Hashon;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class HttpResponseCallbackImp implements HttpResponseCallback {
    private HashMap<String, Object> a;

    public HttpResponseCallbackImp(HashMap<String, Object> hashMap) {
        this.a = hashMap;
    }

    public void handleInput(InputStream inputStream) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[65536];
        int read = inputStream.read(bArr);
        while (read > 0) {
            byteArrayOutputStream.write(bArr, 0, read);
            read = inputStream.read(bArr);
        }
        byteArrayOutputStream.flush();
        this.a.put("bResp", byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.close();
    }

    @Override // com.mob.tools.network.HttpResponseCallback
    public void onResponse(HttpConnection httpConnection) throws Throwable {
        int responseCode = httpConnection.getResponseCode();
        this.a.put("httpStatus", Integer.valueOf(responseCode));
        List<String> list = httpConnection.getHeaderFields().get("sign");
        if (list != null && list.size() > 0) {
            this.a.put("sign", list.get(0));
        }
        List<String> list2 = httpConnection.getHeaderFields().get("Content-Length");
        if (list2 != null && list2.size() > 0) {
            this.a.put("Content-Length", list2.get(0));
        }
        List<String> list3 = httpConnection.getHeaderFields().get("zip");
        if (list3 != null && list3.size() > 0) {
            this.a.put("zip", Boolean.valueOf(AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(list3.get(0))));
        }
        if (responseCode != 200) {
            List<String> list4 = httpConnection.getHeaderFields().get(NotificationCompat.CATEGORY_MESSAGE);
            String str = null;
            if (list4 != null && list4.size() > 0) {
                str = list4.get(0);
            }
            HashMap hashMap = new HashMap();
            hashMap.put("error", str);
            hashMap.put("status", Integer.valueOf(responseCode));
            throw new Throwable(new Hashon().fromHashMap(hashMap));
        }
        InputStream inputStream = httpConnection.getInputStream();
        try {
            handleInput(inputStream);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable unused) {
                }
            }
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable unused2) {
                    }
                }
                throw th2;
            }
        }
    }
}
