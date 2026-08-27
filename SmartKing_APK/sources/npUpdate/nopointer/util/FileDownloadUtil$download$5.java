package npUpdate.nopointer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import npUpdate.nopointer.extension.AnyKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FileDownloadUtil.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "npUpdate.nopointer.util.FileDownloadUtil$download$5", f = "FileDownloadUtil.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes2.dex */
public final class FileDownloadUtil$download$5 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $fileName;
    final /* synthetic */ String $fileSavePath;
    final /* synthetic */ Function0 $onComplete;
    final /* synthetic */ Function1 $onError;
    final /* synthetic */ Function2 $onProgress;
    final /* synthetic */ Function0 $onStart;
    final /* synthetic */ String $url;
    int label;
    private CoroutineScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FileDownloadUtil$download$5(Function0 function0, String str, String str2, String str3, Function2 function2, Function0 function02, Function1 function1, Continuation continuation) {
        super(2, continuation);
        this.$onStart = function0;
        this.$url = str;
        this.$fileSavePath = str2;
        this.$fileName = str3;
        this.$onProgress = function2;
        this.$onComplete = function02;
        this.$onError = function1;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FileDownloadUtil$download$5 fileDownloadUtil$download$5 = new FileDownloadUtil$download$5(this.$onStart, this.$url, this.$fileSavePath, this.$fileName, this.$onProgress, this.$onComplete, this.$onError, completion);
        fileDownloadUtil$download$5.p$ = (CoroutineScope) obj;
        return fileDownloadUtil$download$5;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((FileDownloadUtil$download$5) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.net.HttpURLConnection, T] */
    /* JADX WARN: Type inference failed for: r1v27, types: [java.net.HttpURLConnection, T] */
    /* JADX WARN: Type inference failed for: r1v28, types: [T, java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r1v4, types: [T, java.io.FileOutputStream] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object m33constructorimpl;
        URLConnection openConnection;
        FileOutputStream fileOutputStream;
        Throwable th;
        Throwable th2;
        FileOutputStream fileOutputStream2;
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label != 0) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(obj);
        CoroutineScope coroutineScope = this.p$;
        AnyKt.log(coroutineScope, "----使用HttpURLConnection下载----");
        this.$onStart.invoke();
        final Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = (HttpURLConnection) 0;
        final Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
        objectRef2.element = (FileOutputStream) 0;
        try {
            Result.Companion companion = Result.INSTANCE;
            openConnection = new URL(this.$url).openConnection();
        } catch (Throwable th3) {
            Result.Companion companion2 = Result.INSTANCE;
            m33constructorimpl = Result.m33constructorimpl(ResultKt.createFailure(th3));
        }
        if (openConnection == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.net.HttpURLConnection");
        }
        objectRef.element = (HttpURLConnection) openConnection;
        objectRef2.element = new FileOutputStream(new File(this.$fileSavePath, this.$fileName));
        HttpURLConnection httpURLConnection = (HttpURLConnection) objectRef.element;
        if (httpURLConnection != null) {
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Charset", "utf-8");
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
            httpURLConnection.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
            httpURLConnection.connect();
        }
        HttpURLConnection httpURLConnection2 = (HttpURLConnection) objectRef.element;
        if (httpURLConnection2 == null) {
            Intrinsics.throwNpe();
        }
        if (httpURLConnection2.getResponseCode() == 200) {
            HttpURLConnection httpURLConnection3 = (HttpURLConnection) objectRef.element;
            if (httpURLConnection3 == null) {
                Intrinsics.throwNpe();
            }
            final int contentLength = httpURLConnection3.getContentLength();
            final Ref.IntRef intRef = new Ref.IntRef();
            intRef.element = -1;
            HttpURLConnection httpURLConnection4 = (HttpURLConnection) objectRef.element;
            if (httpURLConnection4 == null) {
                Intrinsics.throwNpe();
            }
            InputStream inputStream = httpURLConnection4.getInputStream();
            Throwable th4 = (Throwable) null;
            try {
                final InputStream input = inputStream;
                FileOutputStream fileOutputStream3 = (FileOutputStream) objectRef2.element;
                Throwable th5 = (Throwable) null;
                try {
                    try {
                        fileOutputStream2 = fileOutputStream3;
                        Intrinsics.checkExpressionValueIsNotNull(input, "input");
                        if (fileOutputStream2 == null) {
                            try {
                                Intrinsics.throwNpe();
                            } catch (Throwable th6) {
                                th2 = th6;
                                fileOutputStream = fileOutputStream3;
                                try {
                                    throw th2;
                                } catch (Throwable th7) {
                                    th = th7;
                                    th = th2;
                                    CloseableKt.closeFinally(fileOutputStream, th);
                                    throw th;
                                }
                            }
                        }
                        th = th5;
                        try {
                            fileOutputStream = fileOutputStream3;
                        } catch (Throwable th8) {
                            th = th8;
                            fileOutputStream = fileOutputStream3;
                            CloseableKt.closeFinally(fileOutputStream, th);
                            throw th;
                        }
                    } catch (Throwable th9) {
                        th = th9;
                        th = th5;
                    }
                } catch (Throwable th10) {
                    th = th10;
                    fileOutputStream = fileOutputStream3;
                }
                try {
                    Long boxLong = Boxing.boxLong(FileDownloadUtilKt.copyToWithProgress$default(input, fileOutputStream2, 0, new Function1<Long, Unit>() { // from class: npUpdate.nopointer.util.FileDownloadUtil$download$5$invokeSuspend$$inlined$runCatching$lambda$1

                        /* JADX INFO: Access modifiers changed from: package-private */
                        /* compiled from: FileDownloadUtil.kt */
                        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000¶\u0001\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0005\n\u0002\b\u0005\n\u0002\b\u0005\n\u0002\b\u0005\n\u0002\b\u0006\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004¨\u0006\b"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "npUpdate/nopointer/util/FileDownloadUtil$download$5$1$2$1$1$1$1", "npUpdate/nopointer/util/FileDownloadUtil$download$5$1$2$1$1$$special$$inlined$yes$lambda$1", "npUpdate/nopointer/util/FileDownloadUtil$download$5$$special$$inlined$use$lambda$1$1", "npUpdate/nopointer/util/FileDownloadUtil$download$5$$special$$inlined$use$lambda$2$1"}, k = 3, mv = {1, 1, 15})
                        /* renamed from: npUpdate.nopointer.util.FileDownloadUtil$download$5$invokeSuspend$$inlined$runCatching$lambda$1$1, reason: invalid class name */
                        /* loaded from: classes2.dex */
                        public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
                            final /* synthetic */ long $it$inlined;
                            int label;
                            private CoroutineScope p$;
                            final /* synthetic */ FileDownloadUtil$download$5$invokeSuspend$$inlined$runCatching$lambda$1 this$0;

                            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                            AnonymousClass1(Continuation continuation, FileDownloadUtil$download$5$invokeSuspend$$inlined$runCatching$lambda$1 fileDownloadUtil$download$5$invokeSuspend$$inlined$runCatching$lambda$1, long j) {
                                super(2, continuation);
                                this.this$0 = fileDownloadUtil$download$5$invokeSuspend$$inlined$runCatching$lambda$1;
                                this.$it$inlined = j;
                            }

                            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                            @NotNull
                            public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
                                Intrinsics.checkParameterIsNotNull(completion, "completion");
                                AnonymousClass1 anonymousClass1 = new AnonymousClass1(completion, this.this$0, this.$it$inlined);
                                anonymousClass1.p$ = (CoroutineScope) obj;
                                return anonymousClass1;
                            }

                            @Override // kotlin.jvm.functions.Function2
                            public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                                return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
                            }

                            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                            @Nullable
                            public final Object invokeSuspend(@NotNull Object obj) {
                                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                if (this.label != 0) {
                                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                }
                                ResultKt.throwOnFailure(obj);
                                CoroutineScope coroutineScope = this.p$;
                                this.$onProgress.invoke(Boxing.boxLong(this.$it$inlined), Boxing.boxLong(contentLength));
                                return Unit.INSTANCE;
                            }
                        }

                        /* JADX INFO: Access modifiers changed from: package-private */
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        @Override // kotlin.jvm.functions.Function1
                        public /* bridge */ /* synthetic */ Unit invoke(Long l) {
                            invoke(l.longValue());
                            return Unit.INSTANCE;
                        }

                        public final void invoke(long j) {
                            double d = j;
                            Double.isNaN(d);
                            double d2 = contentLength;
                            Double.isNaN(d2);
                            int i = (int) ((d * 100.0d) / d2);
                            if (intRef.element != i) {
                                BuildersKt__Builders_commonKt.launch$default(GlobalScope.INSTANCE, Dispatchers.getMain(), null, new AnonymousClass1(null, this, j), 2, null);
                            }
                            intRef.element = i;
                        }
                    }, 2, null));
                    CloseableKt.closeFinally(fileOutputStream, th);
                    Boxing.boxLong(boxLong.longValue());
                } catch (Throwable th11) {
                    th = th11;
                    CloseableKt.closeFinally(fileOutputStream, th);
                    throw th;
                }
            } finally {
                CloseableKt.closeFinally(inputStream, th4);
            }
        }
        m33constructorimpl = Result.m33constructorimpl(Unit.INSTANCE);
        if (Result.m40isSuccessimpl(m33constructorimpl)) {
            HttpURLConnection httpURLConnection5 = (HttpURLConnection) objectRef.element;
            if (httpURLConnection5 != null) {
                httpURLConnection5.disconnect();
            }
            FileOutputStream fileOutputStream4 = (FileOutputStream) objectRef2.element;
            if (fileOutputStream4 != null) {
                fileOutputStream4.close();
            }
            AnyKt.log(coroutineScope, "HttpURLConnection下载完成");
            BuildersKt__Builders_commonKt.launch$default(GlobalScope.INSTANCE, Dispatchers.getMain(), null, new FileDownloadUtil$download$5$invokeSuspend$$inlined$onSuccess$lambda$1(null, this, coroutineScope, objectRef, objectRef2), 2, null);
        }
        Throwable m36exceptionOrNullimpl = Result.m36exceptionOrNullimpl(m33constructorimpl);
        if (m36exceptionOrNullimpl != null) {
            HttpURLConnection httpURLConnection6 = (HttpURLConnection) objectRef.element;
            if (httpURLConnection6 != null) {
                httpURLConnection6.disconnect();
            }
            FileOutputStream fileOutputStream5 = (FileOutputStream) objectRef2.element;
            if (fileOutputStream5 != null) {
                fileOutputStream5.close();
            }
            AnyKt.log(coroutineScope, "HttpURLConnection下载失败：" + m36exceptionOrNullimpl.getMessage());
            BuildersKt__Builders_commonKt.launch$default(GlobalScope.INSTANCE, Dispatchers.getMain(), null, new FileDownloadUtil$download$5$invokeSuspend$$inlined$onFailure$lambda$1(m36exceptionOrNullimpl, null, this, coroutineScope, objectRef, objectRef2), 2, null);
        }
        return Unit.INSTANCE;
    }
}
