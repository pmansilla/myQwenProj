package kotlinx.coroutines.internal;

import android.support.v4.app.NotificationCompat;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.sun.mail.imap.IMAPStore;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: FastServiceLoader.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J1\u0010\u0007\u001a\u0002H\b\"\u0004\b\u0000\u0010\b2\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\rH\u0002¢\u0006\u0002\u0010\u000eJ/\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\b0\u0010\"\u0004\b\u0000\u0010\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\r2\u0006\u0010\n\u001a\u00020\u000bH\u0000¢\u0006\u0002\b\u0011J/\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\b0\u0010\"\u0004\b\u0000\u0010\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\r2\u0006\u0010\n\u001a\u00020\u000bH\u0000¢\u0006\u0002\b\u0013J\u0016\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00060\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0016\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00060\u00102\u0006\u0010\u0018\u001a\u00020\u0019H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lkotlinx/coroutines/internal/FastServiceLoader;", "", "()V", "FAST_SERVICE_LOADER_ENABLED", "", "PREFIX", "", "getProviderInstance", "S", IMAPStore.ID_NAME, "loader", "Ljava/lang/ClassLoader;", NotificationCompat.CATEGORY_SERVICE, "Ljava/lang/Class;", "(Ljava/lang/String;Ljava/lang/ClassLoader;Ljava/lang/Class;)Ljava/lang/Object;", "load", "", "load$kotlinx_coroutines_core", "loadProviders", "loadProviders$kotlinx_coroutines_core", "parse", FileDownloadModel.URL, "Ljava/net/URL;", "parseFile", "r", "Ljava/io/BufferedReader;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class FastServiceLoader {
    private static final String PREFIX = "META-INF/services/";
    public static final FastServiceLoader INSTANCE = new FastServiceLoader();
    private static final boolean FAST_SERVICE_LOADER_ENABLED = SystemPropsKt.systemProp("kotlinx.coroutines.fast.service.loader", true);

    private FastServiceLoader() {
    }

    private final <S> S getProviderInstance(String name, ClassLoader loader, Class<S> service) {
        Class<?> cls = Class.forName(name, false, loader);
        if (service.isAssignableFrom(cls)) {
            return service.cast(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        }
        throw new IllegalArgumentException(("Expected service of class " + service + ", but found " + cls).toString());
    }

    private final List<String> parse(URL url) {
        Throwable th;
        String url2 = url.toString();
        Intrinsics.checkExpressionValueIsNotNull(url2, "url.toString()");
        if (!StringsKt.startsWith$default(url2, "jar", false, 2, (Object) null)) {
            th = (Throwable) null;
            try {
                return INSTANCE.parseFile(new BufferedReader(new InputStreamReader(url.openStream())));
            } finally {
            }
        }
        String substringBefore$default = StringsKt.substringBefore$default(StringsKt.substringAfter$default(url2, "jar:file:", (String) null, 2, (Object) null), '!', (String) null, 2, (Object) null);
        String substringAfter$default = StringsKt.substringAfter$default(url2, "!/", (String) null, 2, (Object) null);
        BufferedReader jarFile = new JarFile(substringBefore$default, false);
        th = (Throwable) null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(new ZipEntry(substringAfter$default)), "UTF-8"));
            Throwable th2 = (Throwable) null;
            try {
                try {
                    return INSTANCE.parseFile(bufferedReader);
                } finally {
                }
            } finally {
                CloseableKt.closeFinally(bufferedReader, th2);
            }
        } finally {
        }
    }

    private final List<String> parseFile(BufferedReader r) {
        boolean z;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        while (true) {
            String readLine = r.readLine();
            if (readLine == null) {
                return CollectionsKt.toList(linkedHashSet);
            }
            String substringBefore$default = StringsKt.substringBefore$default(readLine, "#", (String) null, 2, (Object) null);
            if (substringBefore$default == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            String obj = StringsKt.trim((CharSequence) substringBefore$default).toString();
            String str = obj;
            int i = 0;
            while (true) {
                if (i >= str.length()) {
                    z = true;
                    break;
                }
                char charAt = str.charAt(i);
                if (!(charAt == '.' || Character.isJavaIdentifierPart(charAt))) {
                    z = false;
                    break;
                }
                i++;
            }
            if (!z) {
                throw new IllegalArgumentException(("Illegal service provider class name: " + obj).toString());
            }
            if (str.length() > 0) {
                linkedHashSet.add(obj);
            }
        }
    }

    @NotNull
    public final <S> List<S> load$kotlinx_coroutines_core(@NotNull Class<S> service, @NotNull ClassLoader loader) {
        Intrinsics.checkParameterIsNotNull(service, "service");
        Intrinsics.checkParameterIsNotNull(loader, "loader");
        if (!FAST_SERVICE_LOADER_ENABLED) {
            ServiceLoader load = ServiceLoader.load(service, loader);
            Intrinsics.checkExpressionValueIsNotNull(load, "ServiceLoader.load(service, loader)");
            return CollectionsKt.toList(load);
        }
        try {
            return loadProviders$kotlinx_coroutines_core(service, loader);
        } catch (Throwable unused) {
            ServiceLoader load2 = ServiceLoader.load(service, loader);
            Intrinsics.checkExpressionValueIsNotNull(load2, "ServiceLoader.load(service, loader)");
            return CollectionsKt.toList(load2);
        }
    }

    @NotNull
    public final <S> List<S> loadProviders$kotlinx_coroutines_core(@NotNull Class<S> service, @NotNull ClassLoader loader) {
        Intrinsics.checkParameterIsNotNull(service, "service");
        Intrinsics.checkParameterIsNotNull(loader, "loader");
        Enumeration<URL> urls = loader.getResources(PREFIX + service.getName());
        Intrinsics.checkExpressionValueIsNotNull(urls, "urls");
        ArrayList<URL> list = Collections.list(urls);
        Intrinsics.checkExpressionValueIsNotNull(list, "java.util.Collections.list(this)");
        ArrayList arrayList = new ArrayList();
        for (URL it : list) {
            FastServiceLoader fastServiceLoader = INSTANCE;
            Intrinsics.checkExpressionValueIsNotNull(it, "it");
            CollectionsKt.addAll(arrayList, fastServiceLoader.parse(it));
        }
        Set set = CollectionsKt.toSet(arrayList);
        if (!(!set.isEmpty())) {
            throw new IllegalArgumentException("No providers were loaded with FastServiceLoader".toString());
        }
        Set set2 = set;
        ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(set2, 10));
        Iterator it2 = set2.iterator();
        while (it2.hasNext()) {
            arrayList2.add(INSTANCE.getProviderInstance((String) it2.next(), loader, service));
        }
        return arrayList2;
    }
}
