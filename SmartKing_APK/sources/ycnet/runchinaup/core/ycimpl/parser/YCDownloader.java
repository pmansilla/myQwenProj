package ycnet.runchinaup.core.ycimpl.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ycnet.runchinaup.core.OkHttpCore;
import ycnet.runchinaup.log.ycNetLog;

/* loaded from: classes2.dex */
public class YCDownloader {
    private static YCDownloader ycDownloader = new YCDownloader();
    static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(10);

    /* loaded from: classes2.dex */
    public interface OnDownloadListener {
        void onFailure(IOException iOException);

        void onProgress(int i);

        void onSuccess(File file);
    }

    private YCDownloader() {
    }

    public static YCDownloader getYcDownloader() {
        return ycDownloader;
    }

    public void download(String str, final File file, final OnDownloadListener onDownloadListener) {
        OkHttpCore.getRequest(str, new Callback() { // from class: ycnet.runchinaup.core.ycimpl.parser.YCDownloader.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                if (onDownloadListener != null) {
                    onDownloadListener.onFailure(iOException);
                }
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, final Response response) throws IOException {
                YCDownloader.cachedThreadPool.execute(new Runnable() { // from class: ycnet.runchinaup.core.ycimpl.parser.YCDownloader.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            byte[] bArr = new byte[10240];
                            ResponseBody body = response.body();
                            InputStream byteStream = body.byteStream();
                            long contentLength = body.contentLength();
                            ycNetLog.e("debug===totalLen===>" + contentLength);
                            long j = 0;
                            while (true) {
                                int read = byteStream.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                                j += read;
                                if (onDownloadListener != null) {
                                    onDownloadListener.onProgress(Float.valueOf(((((float) j) * 1.0f) / ((float) contentLength)) * 100.0f).intValue());
                                }
                            }
                            if (onDownloadListener != null) {
                                onDownloadListener.onSuccess(file);
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
