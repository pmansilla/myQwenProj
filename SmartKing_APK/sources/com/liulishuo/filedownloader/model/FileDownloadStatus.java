package com.liulishuo.filedownloader.model;

import com.liulishuo.filedownloader.BaseDownloadTask;

/* loaded from: classes.dex */
public class FileDownloadStatus {
    public static final byte INVALID_STATUS = 0;
    public static final byte blockComplete = 4;
    public static final byte completed = -3;
    public static final byte connected = 2;
    public static final byte error = -1;
    public static final byte paused = -2;
    public static final byte pending = 1;
    public static final byte progress = 3;
    public static final byte retry = 5;
    public static final byte started = 6;
    public static final byte toFileDownloadService = 11;
    public static final byte toLaunchPool = 10;
    public static final byte warn = -4;

    public static boolean isIng(int i) {
        return i > 0;
    }

    public static boolean isKeepAhead(int i, int i2) {
        if ((i != 3 && i != 5 && i == i2) || isOver(i)) {
            return false;
        }
        if (i >= 1 && i <= 6 && i2 >= 10 && i2 <= 11) {
            return false;
        }
        switch (i) {
            case 1:
                return i2 != 0;
            case 2:
                if (i2 != 6) {
                    switch (i2) {
                        case 0:
                        case 1:
                            break;
                        default:
                            return true;
                    }
                }
                return false;
            case 3:
                if (i2 != 6) {
                    switch (i2) {
                        case 0:
                        case 1:
                        case 2:
                            break;
                        default:
                            return true;
                    }
                }
                return false;
            case 4:
            default:
                return true;
            case 5:
                return (i2 == 1 || i2 == 6) ? false : true;
            case 6:
                switch (i2) {
                    case 0:
                    case 1:
                        return false;
                    default:
                        return true;
                }
        }
    }

    public static boolean isKeepFlow(int i, int i2) {
        if ((i != 3 && i != 5 && i == i2) || isOver(i)) {
            return false;
        }
        if (i2 == -2 || i2 == -1) {
            return true;
        }
        switch (i) {
            case 0:
                return i2 == 10;
            case 1:
                return i2 == 6;
            case 2:
            case 3:
                return i2 == -3 || i2 == 3 || i2 == 5;
            case 4:
            case 7:
            case 8:
            case 9:
            default:
                return false;
            case 5:
            case 6:
                return i2 == 2 || i2 == 5;
            case 10:
                return i2 == 11;
            case 11:
                if (i2 != 1) {
                    switch (i2) {
                        case -4:
                        case -3:
                            break;
                        default:
                            return false;
                    }
                }
                return true;
        }
    }

    public static boolean isMoreLikelyCompleted(BaseDownloadTask baseDownloadTask) {
        return baseDownloadTask.getStatus() == 0 || baseDownloadTask.getStatus() == 3;
    }

    public static boolean isOver(int i) {
        return i < 0;
    }
}
