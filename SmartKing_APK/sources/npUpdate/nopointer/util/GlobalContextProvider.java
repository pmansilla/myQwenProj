package npUpdate.nopointer.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: GlobalContextProvider.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0000\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0005¢\u0006\u0002\u0010\u0002J3\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0010\u0010\u000b\u001a\f\u0012\u0006\b\u0001\u0012\u00020\n\u0018\u00010\fH\u0016¢\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u0004\u0018\u00010\n2\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0017J\u001e\u0010\u000f\u001a\u0004\u0018\u00010\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0017J\b\u0010\u0012\u001a\u00020\u0013H\u0016JQ\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0010\u0010\u0016\u001a\f\u0012\u0006\b\u0001\u0012\u00020\n\u0018\u00010\f2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0010\u0010\u000b\u001a\f\u0012\u0006\b\u0001\u0012\u00020\n\u0018\u00010\f2\b\u0010\u0017\u001a\u0004\u0018\u00010\nH\u0017¢\u0006\u0002\u0010\u0018J=\u0010\u0019\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0010\u0010\u000b\u001a\f\u0012\u0006\b\u0001\u0012\u00020\n\u0018\u00010\fH\u0016¢\u0006\u0002\u0010\u001aR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"LnpUpdate/nopointer/util/GlobalContextProvider;", "Landroid/content/ContentProvider;", "()V", "mContext", "Landroid/content/Context;", "delete", "", "uri", "Landroid/net/Uri;", "selection", "", "selectionArgs", "", "(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I", "getType", "insert", "values", "Landroid/content/ContentValues;", "onCreate", "", "query", "Landroid/database/Cursor;", "projection", "sortOrder", "(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;", "update", "(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I", "Companion", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class GlobalContextProvider extends ContentProvider {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Lazy instance$delegate = LazyKt.lazy(new Function0<GlobalContextProvider>() { // from class: npUpdate.nopointer.util.GlobalContextProvider$Companion$instance$2
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final GlobalContextProvider invoke() {
            return new GlobalContextProvider();
        }
    });
    private Context mContext;

    /* compiled from: GlobalContextProvider.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\nR\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u000b"}, d2 = {"LnpUpdate/nopointer/util/GlobalContextProvider$Companion;", "", "()V", "instance", "LnpUpdate/nopointer/util/GlobalContextProvider;", "getInstance", "()LnpUpdate/nopointer/util/GlobalContextProvider;", "instance$delegate", "Lkotlin/Lazy;", "getGlobalContext", "Landroid/content/Context;", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
    /* loaded from: classes2.dex */
    public static final class Companion {
        static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(Companion.class), "instance", "getInstance()LnpUpdate/nopointer/util/GlobalContextProvider;"))};

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final GlobalContextProvider getInstance() {
            Lazy lazy = GlobalContextProvider.instance$delegate;
            Companion companion = GlobalContextProvider.INSTANCE;
            KProperty kProperty = $$delegatedProperties[0];
            return (GlobalContextProvider) lazy.getValue();
        }

        @NotNull
        public final Context getGlobalContext() {
            Context context = getInstance().mContext;
            if (context == null) {
                Intrinsics.throwNpe();
            }
            return context;
        }
    }

    @Override // android.content.ContentProvider
    public int delete(@Nullable Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override // android.content.ContentProvider
    @android.support.annotation.Nullable
    @Nullable
    public String getType(@Nullable Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    @android.support.annotation.Nullable
    @Nullable
    public Uri insert(@Nullable Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        GlobalContextProvider companion = INSTANCE.getInstance();
        Context context = getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "context");
        companion.mContext = context.getApplicationContext();
        return true;
    }

    @Override // android.content.ContentProvider
    @android.support.annotation.Nullable
    @Nullable
    public Cursor query(@Nullable Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(@Nullable Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
