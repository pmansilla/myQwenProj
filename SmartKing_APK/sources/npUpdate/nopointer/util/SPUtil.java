package npUpdate.nopointer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SPUtil.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J\u0018\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0004J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\nJ\u001c\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0007J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0011J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\u0015J\u001a\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0007J(\u0010\u0017\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00182\u0006\u0010\u0006\u001a\u00020\u00072\u0010\b\u0002\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0018J\u0016\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u0001J\u001c\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00070\u0018J \u0010\u001c\u001a\u00020\u001d2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u0007H\u0007J\u000e\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\u001f"}, d2 = {"LnpUpdate/nopointer/util/SPUtil;", "", "()V", "clearTableName", "", "getBoolean", "keyName", "", "defaultValue", "getFloat", "", "getFromStringSet", "Ljava/util/HashSet;", "context", "Landroid/content/Context;", "key", "getInt", "", "getLong", "", "getSp", "Landroid/content/SharedPreferences;", "getString", "getStringSet", "", "putBase", "value", "putStringSet", "putToStringSet", "", "removeKeyName", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class SPUtil {
    public static final SPUtil INSTANCE = new SPUtil();

    private SPUtil() {
    }

    public static /* synthetic */ boolean getBoolean$default(SPUtil sPUtil, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return sPUtil.getBoolean(str, z);
    }

    public static /* synthetic */ float getFloat$default(SPUtil sPUtil, String str, float f, int i, Object obj) {
        if ((i & 2) != 0) {
            f = -1.0f;
        }
        return sPUtil.getFloat(str, f);
    }

    public static /* synthetic */ int getInt$default(SPUtil sPUtil, String str, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = -1;
        }
        return sPUtil.getInt(str, i);
    }

    public static /* synthetic */ long getLong$default(SPUtil sPUtil, String str, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = -1;
        }
        return sPUtil.getLong(str, j);
    }

    @NotNull
    public static /* synthetic */ String getString$default(SPUtil sPUtil, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = (String) null;
        }
        return sPUtil.getString(str, str2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Nullable
    public static /* synthetic */ Set getStringSet$default(SPUtil sPUtil, String str, Set set, int i, Object obj) {
        if ((i & 2) != 0) {
            set = (Set) null;
        }
        return sPUtil.getStringSet(str, set);
    }

    public final boolean clearTableName() {
        SharedPreferences.Editor edit = getSp().edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "sharedPreferences.edit()");
        edit.clear();
        return edit.commit();
    }

    public final boolean getBoolean(@NotNull String keyName, boolean defaultValue) {
        Intrinsics.checkParameterIsNotNull(keyName, "keyName");
        return getSp().getBoolean(keyName, defaultValue);
    }

    public final float getFloat(@NotNull String keyName, float defaultValue) {
        Intrinsics.checkParameterIsNotNull(keyName, "keyName");
        return getSp().getFloat(keyName, defaultValue);
    }

    @NotNull
    public final HashSet<String> getFromStringSet(@NotNull Context context, @NotNull String key) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(key, "key");
        SharedPreferences sp = getSp();
        if (sp == null) {
            Intrinsics.throwNpe();
        }
        Set<String> stringSet = sp.getStringSet(key, new HashSet());
        if (stringSet != null) {
            return (HashSet) stringSet;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.util.HashSet<kotlin.String>");
    }

    public final int getInt(@NotNull String keyName, int defaultValue) {
        Intrinsics.checkParameterIsNotNull(keyName, "keyName");
        return getSp().getInt(keyName, defaultValue);
    }

    public final long getLong(@NotNull String keyName, long defaultValue) {
        Intrinsics.checkParameterIsNotNull(keyName, "keyName");
        return getSp().getLong(keyName, defaultValue);
    }

    @NotNull
    public final SharedPreferences getSp() {
        Context globalContext = GlobalContextProvider.INSTANCE.getGlobalContext();
        SharedPreferences sharedPreferences = globalContext.getSharedPreferences(globalContext.getPackageName(), 0);
        Intrinsics.checkExpressionValueIsNotNull(sharedPreferences, "context.getSharedPrefere…e, Activity.MODE_PRIVATE)");
        return sharedPreferences;
    }

    @NotNull
    public final String getString(@NotNull String keyName, @Nullable String defaultValue) {
        Intrinsics.checkParameterIsNotNull(keyName, "keyName");
        String string = getSp().getString(keyName, defaultValue);
        Intrinsics.checkExpressionValueIsNotNull(string, "sharedPreferences.getString(keyName, defaultValue)");
        return string;
    }

    @Nullable
    public final Set<String> getStringSet(@NotNull String keyName, @Nullable Set<String> defaultValue) {
        Intrinsics.checkParameterIsNotNull(keyName, "keyName");
        return getSp().getStringSet(keyName, defaultValue);
    }

    public final boolean putBase(@NotNull String keyName, @NotNull Object value) {
        Intrinsics.checkParameterIsNotNull(keyName, "keyName");
        Intrinsics.checkParameterIsNotNull(value, "value");
        SharedPreferences.Editor edit = getSp().edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "sharedPreferences.edit()");
        if (value instanceof Integer) {
            edit.putInt(keyName, ((Number) value).intValue());
        } else if (value instanceof Boolean) {
            edit.putBoolean(keyName, ((Boolean) value).booleanValue());
        } else if (value instanceof Float) {
            edit.putFloat(keyName, ((Number) value).floatValue());
        } else if (value instanceof String) {
            edit.putString(keyName, (String) value);
        } else {
            if (!(value instanceof Long)) {
                throw new IllegalArgumentException("SharedPreferences can,t be save this type");
            }
            edit.putLong(keyName, ((Number) value).longValue());
        }
        return edit.commit();
    }

    public final boolean putStringSet(@NotNull String keyName, @NotNull Set<String> value) {
        Intrinsics.checkParameterIsNotNull(keyName, "keyName");
        Intrinsics.checkParameterIsNotNull(value, "value");
        SharedPreferences.Editor edit = getSp().edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "sharedPreferences.edit()");
        edit.putStringSet(keyName, value);
        return edit.commit();
    }

    @SuppressLint({"ApplySharedPref"})
    public final void putToStringSet(@NotNull Context context, @NotNull String key, @NotNull String value) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(key, "key");
        Intrinsics.checkParameterIsNotNull(value, "value");
        SharedPreferences sp = getSp();
        if (sp == null) {
            Intrinsics.throwNpe();
        }
        Set<String> stringSet = sp.getStringSet(key, new HashSet());
        if (stringSet == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.util.HashSet<kotlin.String>");
        }
        HashSet hashSet = new HashSet((HashSet) stringSet);
        hashSet.add(value);
        sp.edit().putStringSet(key, hashSet).commit();
    }

    public final boolean removeKeyName(@NotNull String keyName) {
        Intrinsics.checkParameterIsNotNull(keyName, "keyName");
        SharedPreferences.Editor edit = getSp().edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "sharedPreferences.edit()");
        edit.remove(keyName);
        return edit.commit();
    }
}
