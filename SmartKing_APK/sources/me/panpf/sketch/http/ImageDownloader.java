package me.panpf.sketch.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;
import me.panpf.sketch.SLog;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.http.HttpStack;
import me.panpf.sketch.request.BaseRequest;
import me.panpf.sketch.request.CanceledException;
import me.panpf.sketch.request.DownloadRequest;
import me.panpf.sketch.request.DownloadResult;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.util.DiskLruCache;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ImageDownloader {
    private static final String NAME = "ImageDownloader";

    @NonNull
    private DownloadResult doDownload(@NonNull DownloadRequest downloadRequest, @NonNull String str, @NonNull HttpStack httpStack, @NonNull DiskCache diskCache, @NonNull String str2) throws IOException, CanceledException, DownloadException, RedirectsException {
        OutputStream bufferedOutputStream;
        downloadRequest.setStatus(BaseRequest.Status.CONNECTING);
        try {
            HttpStack.Response response = httpStack.getResponse(str);
            if (downloadRequest.isCanceled()) {
                response.releaseConnection();
                if (SLog.isLoggable(65538)) {
                    SLog.d(NAME, "Download canceled after opening the connection. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey());
                }
                throw new CanceledException();
            }
            try {
                int code = response.getCode();
                if (code != 200) {
                    response.releaseConnection();
                    if (code == 301 || code == 302) {
                        String header = response.getHeader("Location");
                        if (TextUtils.isEmpty(header)) {
                            SLog.w(NAME, "Uri redirects failed. newUri is empty, originUri: %s. %s", downloadRequest.getUri(), downloadRequest.getKey());
                        } else {
                            if (str.equals(downloadRequest.getUri())) {
                                if (SLog.isLoggable(65538)) {
                                    SLog.d(NAME, "Uri redirects. originUri: %s, newUri: %s. %s", downloadRequest.getUri(), header, downloadRequest.getKey());
                                }
                                throw new RedirectsException(header);
                            }
                            SLog.e(NAME, "Disable unlimited redirects, originUri: %s, redirectsUri=%s, newUri=%s. %s", downloadRequest.getUri(), str, header, downloadRequest.getKey());
                        }
                    }
                    String format = String.format("Response code exception. responseHeaders: %s. %s. %s", response.getHeadersString(), downloadRequest.getThreadName(), downloadRequest.getKey());
                    SLog.e(NAME, format);
                    throw new DownloadException(format, ErrorCause.DOWNLOAD_RESPONSE_CODE_EXCEPTION);
                }
                long contentLength = response.getContentLength();
                if (contentLength <= 0 && !response.isContentChunked()) {
                    response.releaseConnection();
                    String format2 = String.format("Content length exception. contentLength: %d, responseHeaders: %s. %s. %s", Long.valueOf(contentLength), response.getHeadersString(), downloadRequest.getThreadName(), downloadRequest.getKey());
                    SLog.e(NAME, format2);
                    throw new DownloadException(format2, ErrorCause.DOWNLOAD_CONTENT_LENGTH_EXCEPTION);
                }
                try {
                    InputStream content = response.getContent();
                    if (downloadRequest.isCanceled()) {
                        SketchUtils.close(content);
                        if (SLog.isLoggable(65538)) {
                            SLog.d(NAME, "Download canceled after get content. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey());
                        }
                        throw new CanceledException();
                    }
                    DiskCache.Editor edit = downloadRequest.getOptions().isCacheInDiskDisabled() ? null : diskCache.edit(str2);
                    if (edit != null) {
                        try {
                            bufferedOutputStream = new BufferedOutputStream(edit.newOutputStream(), 8192);
                        } catch (IOException e) {
                            SketchUtils.close(content);
                            edit.abort();
                            String format3 = String.format("Open disk cache exception. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey());
                            SLog.e(NAME, e, format3);
                            throw new DownloadException(format3, e, ErrorCause.DOWNLOAD_OPEN_DISK_CACHE_EXCEPTION);
                        }
                    } else {
                        bufferedOutputStream = new ByteArrayOutputStream();
                    }
                    OutputStream outputStream = bufferedOutputStream;
                    downloadRequest.setStatus(BaseRequest.Status.READ_DATA);
                    try {
                        try {
                            int readData = readData(downloadRequest, content, outputStream, (int) contentLength);
                            SketchUtils.close(outputStream);
                            SketchUtils.close(content);
                            if (!((contentLength <= 0 && response.isContentChunked()) || ((long) readData) == contentLength)) {
                                if (edit != null) {
                                    edit.abort();
                                }
                                String format4 = String.format("The data is not fully read. contentLength:%d, completedLength:%d, ContentChunked:%s. %s. %s", Long.valueOf(contentLength), Integer.valueOf(readData), Boolean.valueOf(response.isContentChunked()), downloadRequest.getThreadName(), downloadRequest.getKey());
                                SLog.e(NAME, format4);
                                throw new DownloadException(format4, ErrorCause.DOWNLOAD_DATA_NOT_FULLY_READ);
                            }
                            if (edit != null) {
                                try {
                                    edit.commit();
                                } catch (IOException | DiskLruCache.ClosedException | DiskLruCache.EditorChangedException | DiskLruCache.FileNotExistException e2) {
                                    String format5 = String.format("Disk cache commit exception. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey());
                                    SLog.e(NAME, e2, format5);
                                    throw new DownloadException(format5, e2, ErrorCause.DOWNLOAD_DISK_CACHE_COMMIT_EXCEPTION);
                                }
                            }
                            if (edit == null) {
                                if (SLog.isLoggable(65538)) {
                                    SLog.d(NAME, "Download success. Data is saved to disk cache. fileLength: %d/%d. %s. %s", Integer.valueOf(readData), Long.valueOf(contentLength), downloadRequest.getThreadName(), downloadRequest.getKey());
                                }
                                return new DownloadResult(((ByteArrayOutputStream) outputStream).toByteArray(), ImageFrom.NETWORK);
                            }
                            DiskCache.Entry entry = diskCache.get(str2);
                            if (entry != null) {
                                if (SLog.isLoggable(65538)) {
                                    SLog.d(NAME, "Download success. data is saved to memory. fileLength: %d/%d. %s. %s", Integer.valueOf(readData), Long.valueOf(contentLength), downloadRequest.getThreadName(), downloadRequest.getKey());
                                }
                                return new DownloadResult(entry, ImageFrom.NETWORK);
                            }
                            String format6 = String.format("Not found disk cache after download success. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey());
                            SLog.e(NAME, format6);
                            throw new DownloadException(format6, ErrorCause.DOWNLOAD_NOT_FOUND_DISK_CACHE_AFTER_SUCCESS);
                        } catch (Throwable th) {
                            SketchUtils.close(outputStream);
                            SketchUtils.close(content);
                            throw th;
                        }
                    } catch (IOException e3) {
                        if (edit != null) {
                            edit.abort();
                        }
                        String format7 = String.format("Read data exception. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey());
                        SLog.e(NAME, e3, format7);
                        throw new DownloadException(format7, e3, ErrorCause.DOWNLOAD_READ_DATA_EXCEPTION);
                    } catch (CanceledException e4) {
                        if (edit == null) {
                            throw e4;
                        }
                        edit.abort();
                        throw e4;
                    }
                } catch (IOException e5) {
                    response.releaseConnection();
                    throw e5;
                }
            } catch (IOException e6) {
                response.releaseConnection();
                String format8 = String.format("Get response code exception. responseHeaders: %s. %s. %s", response.getHeadersString(), downloadRequest.getThreadName(), downloadRequest.getKey());
                SLog.w(NAME, e6, format8);
                throw new DownloadException(format8, e6, ErrorCause.DOWNLOAD_GET_RESPONSE_CODE_EXCEPTION);
            }
        } catch (IOException e7) {
            throw e7;
        }
    }

    @NonNull
    private DownloadResult loopRetryDownload(@NonNull DownloadRequest downloadRequest, @NonNull DiskCache diskCache, @NonNull String str) throws CanceledException, DownloadException {
        HttpStack httpStack = downloadRequest.getConfiguration().getHttpStack();
        int maxRetryCount = httpStack.getMaxRetryCount();
        String uri = downloadRequest.getUri();
        int i = 0;
        while (true) {
            try {
                return doDownload(downloadRequest, uri, httpStack, diskCache, str);
            } catch (RedirectsException e) {
                uri = e.getNewUrl();
            } catch (Throwable th) {
                downloadRequest.getConfiguration().getErrorTracker().onDownloadError(downloadRequest, th);
                if (downloadRequest.isCanceled()) {
                    String format = String.format("Download exception, but canceled. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey());
                    if (SLog.isLoggable(65538)) {
                        SLog.d(NAME, th, format);
                    }
                    throw new DownloadException(format, th, ErrorCause.DOWNLOAD_EXCEPTION_AND_CANCELED);
                }
                if (!httpStack.canRetry(th) || i >= maxRetryCount) {
                    if (th instanceof CanceledException) {
                        throw ((CanceledException) th);
                    }
                    if (th instanceof DownloadException) {
                        throw ((DownloadException) th);
                    }
                    String format2 = String.format("Download failed. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey());
                    SLog.w(NAME, th, format2);
                    throw new DownloadException(format2, th, ErrorCause.DOWNLOAD_UNKNOWN_EXCEPTION);
                }
                th.printStackTrace();
                i++;
                SLog.w(NAME, th, String.format("Download exception but can retry. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey()));
            }
        }
    }

    private int readData(@NonNull DownloadRequest downloadRequest, @NonNull InputStream inputStream, @NonNull OutputStream outputStream, int i) throws IOException, CanceledException {
        byte[] bArr = new byte[8192];
        long j = 0;
        int i2 = 0;
        while (!downloadRequest.isCanceled()) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                downloadRequest.updateProgress(i, i2);
                outputStream.flush();
                return i2;
            }
            outputStream.write(bArr, 0, read);
            i2 += read;
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - j >= 100) {
                downloadRequest.updateProgress(i, i2);
                j = currentTimeMillis;
            }
        }
        if (SLog.isLoggable(65538)) {
            SLog.d(NAME, "Download canceled in read data. %s. %s. %s", i <= 0 || i2 == i ? "read fully" : "not read fully", downloadRequest.getThreadName(), downloadRequest.getKey());
        }
        throw new CanceledException();
    }

    @NonNull
    public DownloadResult download(@NonNull DownloadRequest downloadRequest) throws CanceledException, DownloadException {
        DiskCache diskCache = downloadRequest.getConfiguration().getDiskCache();
        String diskCacheKey = downloadRequest.getDiskCacheKey();
        ReentrantLock editLock = !downloadRequest.getOptions().isCacheInDiskDisabled() ? diskCache.getEditLock(diskCacheKey) : null;
        if (editLock != null) {
            editLock.lock();
        }
        try {
            if (downloadRequest.isCanceled()) {
                if (SLog.isLoggable(65538)) {
                    SLog.d(NAME, "Download canceled after get disk cache edit lock. %s. %s", downloadRequest.getThreadName(), downloadRequest.getKey());
                }
                throw new CanceledException();
            }
            if (editLock != null) {
                downloadRequest.setStatus(BaseRequest.Status.CHECK_DISK_CACHE);
                DiskCache.Entry entry = diskCache.get(diskCacheKey);
                if (entry != null) {
                    return new DownloadResult(entry, ImageFrom.DISK_CACHE);
                }
            }
            DownloadResult loopRetryDownload = loopRetryDownload(downloadRequest, diskCache, diskCacheKey);
            if (editLock != null) {
                editLock.unlock();
            }
            return loopRetryDownload;
        } finally {
            if (editLock != null) {
                editLock.unlock();
            }
        }
    }

    @NonNull
    public String toString() {
        return NAME;
    }
}
