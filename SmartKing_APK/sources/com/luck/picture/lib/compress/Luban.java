package com.luck.picture.lib.compress;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class Luban implements Handler.Callback {
    private static final String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";
    private static final int MSG_COMPRESS_ERROR = 2;
    private static final int MSG_COMPRESS_MULTIPLE_SUCCESS = 3;
    private static final int MSG_COMPRESS_START = 1;
    private static final String TAG = "Luban";
    private Context context;
    private int index;
    private OnCompressListener mCompressListener;
    private Handler mHandler;
    private int mLeastCompressSize;
    private List<String> mPaths;
    private String mTargetDir;
    private List<LocalMedia> medias;

    /* loaded from: classes.dex */
    public static class Builder {
        private Context context;
        private OnCompressListener mCompressListener;
        private int mLeastCompressSize = 100;
        private List<String> mPaths = new ArrayList();
        private String mTargetDir;
        private List<LocalMedia> medias;

        Builder(Context context) {
            this.context = context;
        }

        private Luban build() {
            return new Luban(this);
        }

        public File get(String str) throws IOException {
            return build().get(str, this.context);
        }

        public List<File> get() throws IOException {
            return build().get(this.context);
        }

        public Builder ignoreBy(int i) {
            this.mLeastCompressSize = i;
            return this;
        }

        public void launch() {
            build().launch(this.context);
        }

        public Builder load(File file) {
            this.mPaths.add(file.getAbsolutePath());
            return this;
        }

        public Builder load(String str) {
            this.mPaths.add(str);
            return this;
        }

        public Builder load(List<String> list) {
            this.mPaths.addAll(list);
            return this;
        }

        public Builder loadLocalMedia(List<LocalMedia> list) {
            if (list == null) {
                list = new ArrayList<>();
            }
            this.medias = list;
            for (LocalMedia localMedia : list) {
                this.mPaths.add(localMedia.isCut() ? localMedia.getCutPath() : localMedia.getPath());
            }
            return this;
        }

        public Builder setCompressListener(OnCompressListener onCompressListener) {
            this.mCompressListener = onCompressListener;
            return this;
        }

        public Builder setTargetDir(String str) {
            this.mTargetDir = str;
            return this;
        }
    }

    private Luban(Builder builder) {
        this.index = -1;
        this.mPaths = builder.mPaths;
        this.medias = builder.medias;
        this.context = builder.context;
        this.mTargetDir = builder.mTargetDir;
        this.mCompressListener = builder.mCompressListener;
        this.mLeastCompressSize = builder.mLeastCompressSize;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    static /* synthetic */ int access$608(Luban luban) {
        int i = luban.index;
        luban.index = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public File get(String str, Context context) throws IOException {
        return Checker.isNeedCompress(this.mLeastCompressSize, str) ? new Engine(str, getImageCacheFile(context, Checker.checkSuffix(str))).compress() : new File(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public List<File> get(Context context) throws IOException {
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = this.mPaths.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (Checker.isImage(next)) {
                arrayList.add(Checker.isNeedCompress(this.mLeastCompressSize, next) ? new Engine(next, getImageCacheFile(context, Checker.checkSuffix(next))).compress() : new File(next));
            }
            it.remove();
        }
        return arrayList;
    }

    @Nullable
    private File getImageCacheDir(Context context) {
        return getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    @Nullable
    private File getImageCacheDir(Context context, String str) {
        File file = new File(new File(PictureFileUtils.getDiskCacheDir(context)), str);
        if (file.mkdirs() || (file.exists() && file.isDirectory())) {
            return file;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File getImageCacheFile(Context context, String str) {
        if (TextUtils.isEmpty(this.mTargetDir)) {
            this.mTargetDir = getImageCacheDir(context).getAbsolutePath();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.mTargetDir);
        sb.append(FileUriModel.SCHEME);
        sb.append(System.currentTimeMillis());
        sb.append((int) (Math.random() * 1000.0d));
        if (TextUtils.isEmpty(str)) {
            str = ".jpg";
        }
        sb.append(str);
        return new File(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    @UiThread
    public void launch(final Context context) {
        if (this.mPaths == null || (this.mPaths.size() == 0 && this.mCompressListener != null)) {
            this.mCompressListener.onError(new NullPointerException("image file cannot be null"));
        }
        Iterator<String> it = this.mPaths.iterator();
        this.index = -1;
        while (it.hasNext()) {
            final String next = it.next();
            if (Checker.isImage(next)) {
                AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() { // from class: com.luck.picture.lib.compress.Luban.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            Luban.access$608(Luban.this);
                            boolean z = true;
                            Luban.this.mHandler.sendMessage(Luban.this.mHandler.obtainMessage(1));
                            File compress = Checker.isNeedCompress(Luban.this.mLeastCompressSize, next) ? new Engine(next, Luban.this.getImageCacheFile(context, Checker.checkSuffix(next))).compress() : new File(next);
                            if (Luban.this.medias == null || Luban.this.medias.size() <= 0) {
                                Luban.this.mHandler.sendMessage(Luban.this.mHandler.obtainMessage(2, new IOException()));
                                return;
                            }
                            LocalMedia localMedia = (LocalMedia) Luban.this.medias.get(Luban.this.index);
                            boolean isHttp = PictureMimeType.isHttp(compress.getAbsolutePath());
                            localMedia.setCompressed(!isHttp);
                            localMedia.setCompressPath(isHttp ? "" : compress.getAbsolutePath());
                            if (Luban.this.index != Luban.this.medias.size() - 1) {
                                z = false;
                            }
                            if (z) {
                                Luban.this.mHandler.sendMessage(Luban.this.mHandler.obtainMessage(3, Luban.this.medias));
                            }
                        } catch (IOException e) {
                            Luban.this.mHandler.sendMessage(Luban.this.mHandler.obtainMessage(2, e));
                        }
                    }
                });
            } else {
                this.mCompressListener.onError(new IllegalArgumentException("can not read the path : " + next));
            }
            it.remove();
        }
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (this.mCompressListener == null) {
            return false;
        }
        switch (message.what) {
            case 1:
                this.mCompressListener.onStart();
                break;
            case 2:
                this.mCompressListener.onError((Throwable) message.obj);
                break;
            case 3:
                this.mCompressListener.onSuccess((List) message.obj);
                break;
        }
        return false;
    }
}
