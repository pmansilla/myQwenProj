package kotlinx.coroutines.internal;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.MainCoroutineDispatcher;
import org.jetbrains.annotations.NotNull;

/* compiled from: MainDispatchers.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0004H\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lkotlinx/coroutines/internal/MainDispatcherLoader;", "", "()V", "dispatcher", "Lkotlinx/coroutines/MainCoroutineDispatcher;", "loadMainDispatcher", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class MainDispatcherLoader {
    public static final MainDispatcherLoader INSTANCE;

    @JvmField
    @NotNull
    public static final MainCoroutineDispatcher dispatcher;

    static {
        MainDispatcherLoader mainDispatcherLoader = new MainDispatcherLoader();
        INSTANCE = mainDispatcherLoader;
        dispatcher = mainDispatcherLoader.loadMainDispatcher();
    }

    private MainDispatcherLoader() {
    }

    private final MainCoroutineDispatcher loadMainDispatcher() {
        Object next;
        MainCoroutineDispatcher tryCreateDispatcher;
        try {
            FastServiceLoader fastServiceLoader = FastServiceLoader.INSTANCE;
            ClassLoader classLoader = MainDispatcherFactory.class.getClassLoader();
            Intrinsics.checkExpressionValueIsNotNull(classLoader, "clz.classLoader");
            List load$kotlinx_coroutines_core = fastServiceLoader.load$kotlinx_coroutines_core(MainDispatcherFactory.class, classLoader);
            Iterator it = load$kotlinx_coroutines_core.iterator();
            if (it.hasNext()) {
                next = it.next();
                int loadPriority = ((MainDispatcherFactory) next).getLoadPriority();
                while (it.hasNext()) {
                    Object next2 = it.next();
                    int loadPriority2 = ((MainDispatcherFactory) next2).getLoadPriority();
                    if (loadPriority < loadPriority2) {
                        next = next2;
                        loadPriority = loadPriority2;
                    }
                }
            } else {
                next = null;
            }
            MainDispatcherFactory mainDispatcherFactory = (MainDispatcherFactory) next;
            return (mainDispatcherFactory == null || (tryCreateDispatcher = MainDispatchersKt.tryCreateDispatcher(mainDispatcherFactory, load$kotlinx_coroutines_core)) == null) ? new MissingMainCoroutineDispatcher(null, null, 2, null) : tryCreateDispatcher;
        } catch (Throwable th) {
            return new MissingMainCoroutineDispatcher(th, null, 2, null);
        }
    }
}
