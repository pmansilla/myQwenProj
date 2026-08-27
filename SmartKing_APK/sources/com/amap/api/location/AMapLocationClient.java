package com.amap.api.location;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import com.loc.es;
import com.loc.m;
import com.loc.x;

/* loaded from: classes.dex */
public class AMapLocationClient {
    Context a;
    m b;

    public AMapLocationClient(Context context) {
        try {
            if (context == null) {
                throw new IllegalArgumentException("Context参数不能为null");
            }
            this.a = context.getApplicationContext();
            this.b = new m(this.a, null);
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "AMapLocationClient 1");
        }
    }

    public AMapLocationClient(Context context, Intent intent) {
        try {
            if (context == null) {
                throw new IllegalArgumentException("Context参数不能为null");
            }
            this.a = context.getApplicationContext();
            this.b = new m(this.a, intent);
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "AMapLocationClient 2");
        }
    }

    public static String getDeviceId(Context context) {
        return x.v(context);
    }

    public static void setApiKey(String str) {
        try {
            AMapLocationClientOption.a = str;
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "setApiKey");
        }
    }

    public void disableBackgroundLocation(boolean z) {
        try {
            if (this.b != null) {
                this.b.disableBackgroundLocation(z);
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "disableBackgroundLocation");
        }
    }

    public void enableBackgroundLocation(int i, Notification notification) {
        try {
            if (this.b != null) {
                this.b.enableBackgroundLocation(i, notification);
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "enableBackgroundLocation");
        }
    }

    public AMapLocation getLastKnownLocation() {
        try {
            if (this.b != null) {
                return this.b.getLastKnownLocation();
            }
            return null;
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "getLastKnownLocation");
            return null;
        }
    }

    public String getVersion() {
        return "4.7.1";
    }

    public boolean isStarted() {
        try {
            if (this.b != null) {
                return this.b.isStarted();
            }
            return false;
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "isStarted");
            return false;
        }
    }

    public void onDestroy() {
        try {
            if (this.b != null) {
                this.b.onDestroy();
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "onDestroy");
        }
    }

    public void setLocationListener(AMapLocationListener aMapLocationListener) {
        try {
            if (aMapLocationListener == null) {
                throw new IllegalArgumentException("listener参数不能为null");
            }
            if (this.b != null) {
                this.b.setLocationListener(aMapLocationListener);
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "setLocationListener");
        }
    }

    public void setLocationOption(AMapLocationClientOption aMapLocationClientOption) {
        try {
            if (aMapLocationClientOption == null) {
                throw new IllegalArgumentException("LocationManagerOption参数不能为null");
            }
            if (this.b != null) {
                this.b.setLocationOption(aMapLocationClientOption);
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "setLocationOption");
        }
    }

    public void startAssistantLocation() {
        try {
            if (this.b != null) {
                this.b.startAssistantLocation();
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "startAssistantLocation");
        }
    }

    public void startAssistantLocation(WebView webView) {
        try {
            if (this.b != null) {
                this.b.startAssistantLocation(webView);
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "startAssistantLocation1");
        }
    }

    public void startLocation() {
        try {
            if (this.b != null) {
                this.b.startLocation();
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "startLocation");
        }
    }

    public void stopAssistantLocation() {
        try {
            if (this.b != null) {
                this.b.stopAssistantLocation();
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "stopAssistantLocation");
        }
    }

    public void stopLocation() {
        try {
            if (this.b != null) {
                this.b.stopLocation();
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "stopLocation");
        }
    }

    public void unRegisterLocationListener(AMapLocationListener aMapLocationListener) {
        try {
            if (this.b != null) {
                this.b.unRegisterLocationListener(aMapLocationListener);
            }
        } catch (Throwable th) {
            es.a(th, "AMapLocationClient", "unRegisterLocationListener");
        }
    }
}
