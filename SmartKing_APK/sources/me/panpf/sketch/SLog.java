package me.panpf.sketch;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import android.util.Log;
import com.amap.location.common.model.AmapLoc;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* loaded from: classes2.dex */
public class SLog {
    public static final int LEVEL_DEBUG = 2;
    public static final int LEVEL_ERROR = 16;
    public static final int LEVEL_INFO = 4;
    public static final String LEVEL_NAME_DEBUG = "DEBUG";
    public static final String LEVEL_NAME_ERROR = "ERROR";
    public static final String LEVEL_NAME_INFO = "INFO";
    public static final String LEVEL_NAME_NONE = "NONE";
    public static final String LEVEL_NAME_VERBOSE = "VERBOSE";
    public static final String LEVEL_NAME_WARNING = "WARNING";
    public static final int LEVEL_NONE = 32;
    public static final int LEVEL_VERBOSE = 1;
    public static final int LEVEL_WARNING = 8;
    private static final String NAME = "SLog";
    private static final String TAG = "Sketch";
    public static final int TYPE_CACHE = 131072;
    public static final int TYPE_FLOW = 65536;
    public static final int TYPE_TIME = 262144;
    public static final int TYPE_ZOOM = 524288;
    public static final int TYPE_ZOOM_BLOCK_DISPLAY = 1048576;
    private static int levelAndTypeFlags;
    private static Proxy proxy = new ProxyImpl();

    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Level {
    }

    /* loaded from: classes2.dex */
    public interface Proxy {
        int d(String str, String str2);

        int d(String str, String str2, Throwable th);

        int e(String str, String str2);

        int e(String str, String str2, Throwable th);

        int i(String str, String str2);

        int i(String str, String str2, Throwable th);

        void onReplaced();

        int v(String str, String str2);

        int v(String str, String str2, Throwable th);

        int w(String str, String str2);

        int w(String str, String str2, Throwable th);

        int w(String str, Throwable th);
    }

    /* loaded from: classes2.dex */
    private static class ProxyImpl implements Proxy {
        private ProxyImpl() {
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int d(String str, String str2) {
            return Log.d(str, str2);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int d(String str, String str2, Throwable th) {
            return Log.d(str, str2, th);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int e(String str, String str2) {
            return Log.e(str, str2);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int e(String str, String str2, Throwable th) {
            return Log.e(str, str2, th);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int i(String str, String str2) {
            return Log.i(str, str2);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int i(String str, String str2, Throwable th) {
            return Log.i(str, str2, th);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public void onReplaced() {
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int v(String str, String str2) {
            return Log.v(str, str2);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int v(String str, String str2, Throwable th) {
            return Log.v(str, str2, th);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int w(String str, String str2) {
            return Log.w(str, str2);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int w(String str, String str2, Throwable th) {
            return Log.w(str, str2, th);
        }

        @Override // me.panpf.sketch.SLog.Proxy
        public int w(String str, Throwable th) {
            return Log.w(str, th);
        }
    }

    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Type {
    }

    static {
        setLevel(4);
    }

    private static String assembleLog(@Nullable String str, @NonNull String str2, @Nullable Object... objArr) {
        if (TextUtils.isEmpty(str2)) {
            return "";
        }
        if (objArr == null || objArr.length <= 0) {
            if (TextUtils.isEmpty(str)) {
                return str2;
            }
            return str + ". " + str2;
        }
        if (TextUtils.isEmpty(str)) {
            return String.format(str2, objArr);
        }
        return str + ". " + String.format(str2, objArr);
    }

    public static void closeType(int i) {
        int i2 = i & SupportMenu.CATEGORY_MASK;
        String typeNames = getTypeNames();
        levelAndTypeFlags = (i2 ^ (-1)) & levelAndTypeFlags;
        Log.w(TAG, String.format("%s. closeType: %s -> %s", NAME, typeNames, getTypeNames()));
    }

    public static int d(@Nullable String str, @NonNull String str2) {
        if (isLoggable(2)) {
            return proxy.d(TAG, assembleLog(str, str2, (Object[]) null));
        }
        return 0;
    }

    public static int d(@Nullable String str, @NonNull String str2, @NonNull Object... objArr) {
        if (isLoggable(2)) {
            return proxy.d(TAG, assembleLog(str, str2, objArr));
        }
        return 0;
    }

    public static int d(@Nullable String str, @NonNull Throwable th, @NonNull String str2) {
        if (isLoggable(2)) {
            return proxy.d(TAG, assembleLog(str, str2, (Object[]) null), th);
        }
        return 0;
    }

    public static int e(@Nullable String str, @NonNull String str2) {
        if (isLoggable(16)) {
            return proxy.e(TAG, assembleLog(str, str2, (Object[]) null));
        }
        return 0;
    }

    public static int e(@Nullable String str, @NonNull String str2, @NonNull Object... objArr) {
        if (isLoggable(16)) {
            return proxy.e(TAG, assembleLog(str, str2, objArr));
        }
        return 0;
    }

    public static int e(@Nullable String str, @NonNull Throwable th, @NonNull String str2) {
        if (isLoggable(16)) {
            return proxy.e(TAG, assembleLog(str, str2, (Object[]) null), th);
        }
        return 0;
    }

    @SuppressLint({"WrongConstant"})
    public static int getLevel() {
        if (isLoggable(1)) {
            return 1;
        }
        if (isLoggable(2)) {
            return 2;
        }
        if (isLoggable(4)) {
            return 4;
        }
        if (isLoggable(8)) {
            return 8;
        }
        if (isLoggable(16)) {
            return 16;
        }
        return isLoggable(32) ? 32 : 0;
    }

    public static String getLevelName() {
        if (isLoggable(1)) {
            return LEVEL_NAME_VERBOSE;
        }
        if (isLoggable(2)) {
            return LEVEL_NAME_DEBUG;
        }
        if (isLoggable(4)) {
            return LEVEL_NAME_INFO;
        }
        if (isLoggable(8)) {
            return LEVEL_NAME_WARNING;
        }
        if (isLoggable(16)) {
            return LEVEL_NAME_ERROR;
        }
        if (isLoggable(32)) {
            return LEVEL_NAME_NONE;
        }
        return "UNKNOWN(" + getLevel() + SQLBuilder.PARENTHESES_RIGHT;
    }

    public static String getTypeNames() {
        StringBuilder sb = new StringBuilder();
        if (isLoggable(65536)) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("FLOW");
        }
        if (isLoggable(131072)) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("CACHE");
        }
        if (isLoggable(524288)) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("ZOOM");
        }
        if (isLoggable(1048576)) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("ZOOM_BLOCK_DISPLAY");
        }
        if (isLoggable(262144)) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("TIME");
        }
        if (sb.length() == 0) {
            sb.append(LEVEL_NAME_NONE);
        }
        return sb.toString();
    }

    public static int i(@Nullable String str, @NonNull String str2) {
        if (isLoggable(4)) {
            return proxy.i(TAG, assembleLog(str, str2, (Object[]) null));
        }
        return 0;
    }

    public static int i(@Nullable String str, @NonNull String str2, @NonNull Object... objArr) {
        if (isLoggable(4)) {
            return proxy.i(TAG, assembleLog(str, str2, objArr));
        }
        return 0;
    }

    public static boolean isLoggable(int i) {
        int low16One = low16One(i & 65535);
        int i2 = i & SupportMenu.CATEGORY_MASK;
        int i3 = 65535 & levelAndTypeFlags;
        int i4 = (-65536) & levelAndTypeFlags;
        return (low16One == 0 || (i3 != 0 && low16One >= i3)) && (i2 == 0 || (i4 != 0 && (i4 & i2) == i2));
    }

    private static int low16One(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(AmapLoc.RESULT_TYPE_WIFI_ONLY);
        int length = Integer.toBinaryString(65535 & i).length() - 1;
        for (int i2 = 1; i2 <= length; i2++) {
            sb.append(AmapLoc.RESULT_TYPE_GPS);
        }
        return (i & SupportMenu.CATEGORY_MASK) | (parseUnsignedInt(sb.toString(), 2) & i);
    }

    public static void openType(int i) {
        int i2 = i & SupportMenu.CATEGORY_MASK;
        int i3 = levelAndTypeFlags;
        if (i2 != 0) {
            i3 |= i2;
        }
        String typeNames = getTypeNames();
        levelAndTypeFlags = i3;
        Log.w(TAG, String.format("%s. openType: %s -> %s", NAME, typeNames, getTypeNames()));
    }

    public static int parseUnsignedInt(@NonNull String str, int i) throws NumberFormatException {
        if (str == null) {
            throw new NumberFormatException("null");
        }
        int length = str.length();
        if (length <= 0) {
            throw new NumberFormatException("For input string: \"" + str + "\"");
        }
        if (str.charAt(0) == '-') {
            throw new NumberFormatException(String.format("Illegal leading minus sign on unsigned string %s.", str));
        }
        if (length <= 5 || (i == 10 && length <= 9)) {
            return Integer.parseInt(str, i);
        }
        long parseLong = Long.parseLong(str, i);
        if (((-4294967296L) & parseLong) == 0) {
            return (int) parseLong;
        }
        throw new NumberFormatException(String.format("String value %s exceeds range of unsigned int.", str));
    }

    public static void setLevel(int i) {
        int low16One = low16One(i & 65535);
        int i2 = levelAndTypeFlags;
        if (low16One != 0) {
            i2 = (i2 & SupportMenu.CATEGORY_MASK) | low16One;
        }
        String levelName = getLevelName();
        levelAndTypeFlags = i2;
        Log.w(TAG, String.format("%s. setLevel. %s -> %s", NAME, levelName, getLevelName()));
    }

    public static void setProxy(@Nullable Proxy proxy2) {
        if (proxy != proxy2) {
            proxy.onReplaced();
            if (proxy2 == null) {
                proxy2 = new ProxyImpl();
            }
            proxy = proxy2;
        }
    }

    public static int v(@Nullable String str, @NonNull String str2) {
        if (isLoggable(1)) {
            return proxy.v(TAG, assembleLog(str, str2, (Object[]) null));
        }
        return 0;
    }

    public static int v(@Nullable String str, @NonNull String str2, @NonNull Object... objArr) {
        if (isLoggable(1)) {
            return proxy.v(TAG, assembleLog(str, str2, objArr));
        }
        return 0;
    }

    public static int w(@Nullable String str, @NonNull String str2) {
        if (isLoggable(8)) {
            return proxy.w(TAG, assembleLog(str, str2, (Object[]) null));
        }
        return 0;
    }

    public static int w(@Nullable String str, @NonNull String str2, @NonNull Object... objArr) {
        if (isLoggable(8)) {
            return proxy.w(TAG, assembleLog(str, str2, objArr));
        }
        return 0;
    }

    public static int w(@Nullable String str, @NonNull Throwable th, @NonNull String str2) {
        if (isLoggable(8)) {
            return proxy.w(TAG, assembleLog(str, str2, (Object[]) null), th);
        }
        return 0;
    }
}
