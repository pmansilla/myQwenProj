package com.autonavi.amap.mapcore.maploader;

import android.content.Context;
import com.amap.api.mapcore.util.eq;
import com.amap.api.mapcore.util.fr;
import com.amap.api.mapcore.util.hd;
import com.amap.api.mapcore.util.hg;
import com.amap.api.mapcore.util.hi;
import com.amap.api.mapcore.util.ho;
import com.amap.api.mapcore.util.ic;
import com.amap.api.mapcore.util.iu;
import com.amap.api.maps.MapsInitializer;
import com.autonavi.ae.gmap.GLMapEngine;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/* loaded from: classes.dex */
public class AMapLoader implements iu.a {
    private static final int GET_METHOD = 0;
    private static String mDiu;
    private iu downloadManager;
    private volatile boolean isCanceled = false;
    public boolean isFinish = false;
    ADataRequestParam mDataRequestParam;
    private int mEngineID;
    GLMapEngine mGLMapEngine;
    private boolean mRequestCancel;

    /* loaded from: classes.dex */
    public static class ADataRequestParam {
        public byte[] enCodeString;
        public long handler;
        public int nCompress;
        public int nRequestType;
        public String requestBaseUrl;
        public String requestUrl;
    }

    /* loaded from: classes.dex */
    public static class AMapGridDownloadRequest extends eq {
        private final Context mContext;
        private byte[] postEntityBytes;
        private String sUrl;
        private String userAgent;

        public AMapGridDownloadRequest(Context context, String str, String str2) {
            this.mContext = context;
            this.sUrl = str;
            this.userAgent = str2;
        }

        @Override // com.amap.api.mapcore.util.ix
        public byte[] getEntityBytes() {
            return this.postEntityBytes;
        }

        @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
        public Map<String, String> getParams() {
            return null;
        }

        @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
        public Map<String, String> getRequestHead() {
            ho e = fr.e();
            String b = e != null ? e.b() : null;
            String f = hd.f(this.mContext);
            try {
                f = URLEncoder.encode(f, "UTF-8");
            } catch (Throwable unused) {
            }
            Hashtable hashtable = new Hashtable(16);
            hashtable.put("User-Agent", this.userAgent);
            hashtable.put("platinfo", String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s", b, "3dmap"));
            hashtable.put("x-INFO", hg.a(this.mContext));
            hashtable.put("key", f);
            hashtable.put("logversion", "2.1");
            return hashtable;
        }

        @Override // com.amap.api.mapcore.util.ix
        public String getURL() {
            return this.sUrl;
        }

        public void setPostEntityBytes(byte[] bArr) {
            this.postEntityBytes = bArr;
        }
    }

    public AMapLoader(int i, GLMapEngine gLMapEngine, ADataRequestParam aDataRequestParam) {
        this.mEngineID = 0;
        this.mRequestCancel = false;
        this.mDataRequestParam = aDataRequestParam;
        this.mEngineID = i;
        this.mGLMapEngine = gLMapEngine;
        this.mRequestCancel = false;
    }

    private String generateQueryString(Context context, String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        String f = hd.f(this.mGLMapEngine.getContext());
        try {
            f = URLEncoder.encode(f, "UTF-8");
        } catch (Throwable unused) {
        }
        stringBuffer.append("&key=");
        stringBuffer.append(f);
        String sortReEncoderParams = sortReEncoderParams(stringBuffer.toString());
        String a = hg.a();
        stringBuffer.append("&ts=" + a);
        stringBuffer.append("&scode=" + hg.a(context, a, sortReEncoderParams));
        stringBuffer.append("&dip=");
        stringBuffer.append("16300");
        return stringBuffer.toString();
    }

    private String getEncodeRequestParams(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String sortReEncoderParams(String str) {
        String[] split = str.split("&");
        Arrays.sort(split);
        StringBuffer stringBuffer = new StringBuffer();
        for (String str2 : split) {
            stringBuffer.append(strReEncoder(str2));
            stringBuffer.append("&");
        }
        String stringBuffer2 = stringBuffer.toString();
        return stringBuffer2.length() > 1 ? (String) stringBuffer2.subSequence(0, stringBuffer2.length() - 1) : str;
    }

    private String strReEncoder(String str) {
        if (str == null) {
            return str;
        }
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            ic.c(e, "AbstractProtocalHandler", "strReEncoder");
            return "";
        } catch (Exception e2) {
            ic.c(e2, "AbstractProtocalHandler", "strReEncoderException");
            return "";
        }
    }

    public void doCancel() {
        this.mRequestCancel = true;
        if (this.downloadManager == null || this.isCanceled) {
            return;
        }
        synchronized (this.downloadManager) {
            try {
                this.isCanceled = true;
                this.downloadManager.a();
                this.mGLMapEngine.setMapLoaderToTask(this.mEngineID, this.mDataRequestParam.handler, null);
            } catch (Throwable th) {
                ic.c(th, "AMapLoader", "doCancel");
            }
        }
    }

    public void doRequest() {
        if (this.mRequestCancel) {
            return;
        }
        String str = this.mDataRequestParam.requestBaseUrl;
        String str2 = this.mDataRequestParam.requestUrl;
        if (!str.endsWith("?")) {
            str = str + "?";
        }
        String requestParams = getRequestParams(str2.replaceAll(";", getEncodeRequestParams(";").toString()), str != null && str.contains("http://m5.amap.com/"), this.mDataRequestParam.nRequestType);
        StringBuffer stringBuffer = new StringBuffer();
        if (this.mDataRequestParam.nRequestType == 0) {
            stringBuffer.append(requestParams);
            stringBuffer.append("&csid=" + UUID.randomUUID().toString());
        } else {
            stringBuffer.append("csid=" + UUID.randomUUID().toString());
        }
        try {
            try {
                AMapGridDownloadRequest aMapGridDownloadRequest = new AMapGridDownloadRequest(this.mGLMapEngine.getContext(), str + generateQueryString(this.mGLMapEngine.getContext(), stringBuffer.toString()), this.mGLMapEngine.getUserAgent());
                aMapGridDownloadRequest.setConnectionTimeout(1800000);
                aMapGridDownloadRequest.setSoTimeout(1800000);
                if (this.mDataRequestParam.nRequestType != 0) {
                    aMapGridDownloadRequest.setPostEntityBytes(requestParams.getBytes("UTF-8"));
                }
                this.downloadManager = new iu(aMapGridDownloadRequest, 0L, -1L, MapsInitializer.getProtocol() == 2);
                this.downloadManager.a(this);
            } catch (Throwable th) {
                onException(th);
            }
        } finally {
            doCancel();
        }
    }

    public String getDeviceId(Context context) {
        if (context != null) {
            return hi.u(context);
        }
        return null;
    }

    protected String getRequestParams(String str, boolean z, int i) {
        if (mDiu == null) {
            mDiu = getDeviceId(this.mGLMapEngine.getContext());
        }
        StringBuffer stringBuffer = new StringBuffer(str);
        if (z) {
            stringBuffer.append("&channel=amap7&div=GNaviMap");
            stringBuffer.append("&diu=");
            stringBuffer.append(mDiu);
        } else {
            stringBuffer.append("&channel=amapapi");
            stringBuffer.append("&div=GNaviMap");
            stringBuffer.append("&diu=");
            stringBuffer.append(mDiu);
        }
        return stringBuffer.toString();
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onDownload(byte[] bArr, long j) {
        if (bArr == null || this.mGLMapEngine == null || this.mDataRequestParam == null) {
            return;
        }
        this.mGLMapEngine.receiveNetData(this.mEngineID, this.mDataRequestParam.handler, bArr, bArr.length);
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onException(Throwable th) {
        if (this.mGLMapEngine != null && this.mDataRequestParam != null) {
            this.mGLMapEngine.netError(this.mEngineID, this.mDataRequestParam.handler, -1);
        }
        ic.c(th, "AMapLoader", "download onException");
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onFinish() {
        if (this.mGLMapEngine == null || this.mDataRequestParam == null) {
            return;
        }
        this.mGLMapEngine.finishDownLoad(this.mEngineID, this.mDataRequestParam.handler);
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onStop() {
        if (this.mGLMapEngine == null || this.mDataRequestParam == null) {
            return;
        }
        this.mGLMapEngine.netError(this.mEngineID, this.mDataRequestParam.handler, -1);
    }
}
