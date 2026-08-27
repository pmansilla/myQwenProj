package com.liulishuo.filedownloader.services;

import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

/* loaded from: classes.dex */
public class DefaultIdGenerator implements FileDownloadHelper.IdGenerator {
    @Override // com.liulishuo.filedownloader.util.FileDownloadHelper.IdGenerator
    public int generateId(String str, String str2, boolean z) {
        return z ? FileDownloadUtils.md5(FileDownloadUtils.formatString("%sp%s@dir", str, str2)).hashCode() : FileDownloadUtils.md5(FileDownloadUtils.formatString("%sp%s", str, str2)).hashCode();
    }

    @Override // com.liulishuo.filedownloader.util.FileDownloadHelper.IdGenerator
    public int transOldId(int i, String str, String str2, boolean z) {
        return generateId(str, str2, z);
    }
}
