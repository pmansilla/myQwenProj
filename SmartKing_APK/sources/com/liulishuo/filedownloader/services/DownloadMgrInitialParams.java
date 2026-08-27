package com.liulishuo.filedownloader.services;

import com.liulishuo.filedownloader.connection.DefaultConnectionCountAdapter;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.database.RemitDatabase;
import com.liulishuo.filedownloader.services.ForegroundServiceConfig;
import com.liulishuo.filedownloader.stream.FileDownloadRandomAccessFile;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

/* loaded from: classes.dex */
public class DownloadMgrInitialParams {
    private final InitCustomMaker mMaker;

    /* loaded from: classes.dex */
    public static class InitCustomMaker {
        FileDownloadHelper.ConnectionCountAdapter mConnectionCountAdapter;
        FileDownloadHelper.ConnectionCreator mConnectionCreator;
        FileDownloadHelper.DatabaseCustomMaker mDatabaseCustomMaker;
        ForegroundServiceConfig mForegroundServiceConfig;
        FileDownloadHelper.IdGenerator mIdGenerator;
        Integer mMaxNetworkThreadCount;
        FileDownloadHelper.OutputStreamCreator mOutputStreamCreator;

        public void commit() {
        }

        public InitCustomMaker connectionCountAdapter(FileDownloadHelper.ConnectionCountAdapter connectionCountAdapter) {
            this.mConnectionCountAdapter = connectionCountAdapter;
            return this;
        }

        public InitCustomMaker connectionCreator(FileDownloadHelper.ConnectionCreator connectionCreator) {
            this.mConnectionCreator = connectionCreator;
            return this;
        }

        public InitCustomMaker database(FileDownloadHelper.DatabaseCustomMaker databaseCustomMaker) {
            this.mDatabaseCustomMaker = databaseCustomMaker;
            return this;
        }

        public InitCustomMaker foregroundServiceConfig(ForegroundServiceConfig foregroundServiceConfig) {
            this.mForegroundServiceConfig = foregroundServiceConfig;
            return this;
        }

        public InitCustomMaker idGenerator(FileDownloadHelper.IdGenerator idGenerator) {
            this.mIdGenerator = idGenerator;
            return this;
        }

        public InitCustomMaker maxNetworkThreadCount(int i) {
            if (i > 0) {
                this.mMaxNetworkThreadCount = Integer.valueOf(i);
            }
            return this;
        }

        public InitCustomMaker outputStreamCreator(FileDownloadHelper.OutputStreamCreator outputStreamCreator) {
            this.mOutputStreamCreator = outputStreamCreator;
            if (this.mOutputStreamCreator == null || this.mOutputStreamCreator.supportSeek() || FileDownloadProperties.getImpl().fileNonPreAllocation) {
                return this;
            }
            throw new IllegalArgumentException("Since the provided FileDownloadOutputStream does not support the seek function, if FileDownloader pre-allocates file size at the beginning of the download, it will can not be resumed from the breakpoint. If you need to ensure that the resumption is available, please add and set the value of 'file.non-pre-allocation' field to 'true' in the 'filedownloader.properties' file which is in your application assets folder manually for resolving this problem.");
        }

        public String toString() {
            return FileDownloadUtils.formatString("component: database[%s], maxNetworkCount[%s], outputStream[%s], connection[%s], connectionCountAdapter[%s]", this.mDatabaseCustomMaker, this.mMaxNetworkThreadCount, this.mOutputStreamCreator, this.mConnectionCreator, this.mConnectionCountAdapter);
        }
    }

    public DownloadMgrInitialParams() {
        this.mMaker = null;
    }

    public DownloadMgrInitialParams(InitCustomMaker initCustomMaker) {
        this.mMaker = initCustomMaker;
    }

    private FileDownloadHelper.ConnectionCountAdapter createDefaultConnectionCountAdapter() {
        return new DefaultConnectionCountAdapter();
    }

    private FileDownloadHelper.ConnectionCreator createDefaultConnectionCreator() {
        return new FileDownloadUrlConnection.Creator();
    }

    private FileDownloadDatabase createDefaultDatabase() {
        return new RemitDatabase();
    }

    private ForegroundServiceConfig createDefaultForegroundServiceConfig() {
        return new ForegroundServiceConfig.Builder().needRecreateChannelId(true).build();
    }

    private FileDownloadHelper.IdGenerator createDefaultIdGenerator() {
        return new DefaultIdGenerator();
    }

    private FileDownloadHelper.OutputStreamCreator createDefaultOutputStreamCreator() {
        return new FileDownloadRandomAccessFile.Creator();
    }

    private int getDefaultMaxNetworkThreadCount() {
        return FileDownloadProperties.getImpl().downloadMaxNetworkThreadCount;
    }

    public FileDownloadHelper.ConnectionCountAdapter createConnectionCountAdapter() {
        FileDownloadHelper.ConnectionCountAdapter connectionCountAdapter;
        if (this.mMaker != null && (connectionCountAdapter = this.mMaker.mConnectionCountAdapter) != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize connection count adapter: %s", connectionCountAdapter);
            }
            return connectionCountAdapter;
        }
        return createDefaultConnectionCountAdapter();
    }

    public FileDownloadHelper.ConnectionCreator createConnectionCreator() {
        FileDownloadHelper.ConnectionCreator connectionCreator;
        if (this.mMaker != null && (connectionCreator = this.mMaker.mConnectionCreator) != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize connection creator: %s", connectionCreator);
            }
            return connectionCreator;
        }
        return createDefaultConnectionCreator();
    }

    public FileDownloadDatabase createDatabase() {
        if (this.mMaker == null || this.mMaker.mDatabaseCustomMaker == null) {
            return createDefaultDatabase();
        }
        FileDownloadDatabase customMake = this.mMaker.mDatabaseCustomMaker.customMake();
        if (customMake == null) {
            return createDefaultDatabase();
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "initial FileDownloader manager with the customize database: %s", customMake);
        }
        return customMake;
    }

    public ForegroundServiceConfig createForegroundServiceConfig() {
        ForegroundServiceConfig foregroundServiceConfig;
        if (this.mMaker != null && (foregroundServiceConfig = this.mMaker.mForegroundServiceConfig) != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize foreground service config: %s", foregroundServiceConfig);
            }
            return foregroundServiceConfig;
        }
        return createDefaultForegroundServiceConfig();
    }

    public FileDownloadHelper.IdGenerator createIdGenerator() {
        FileDownloadHelper.IdGenerator idGenerator;
        if (this.mMaker != null && (idGenerator = this.mMaker.mIdGenerator) != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize id generator: %s", idGenerator);
            }
            return idGenerator;
        }
        return createDefaultIdGenerator();
    }

    public FileDownloadHelper.OutputStreamCreator createOutputStreamCreator() {
        FileDownloadHelper.OutputStreamCreator outputStreamCreator;
        if (this.mMaker != null && (outputStreamCreator = this.mMaker.mOutputStreamCreator) != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize output stream: %s", outputStreamCreator);
            }
            return outputStreamCreator;
        }
        return createDefaultOutputStreamCreator();
    }

    public int getMaxNetworkThreadCount() {
        Integer num;
        if (this.mMaker != null && (num = this.mMaker.mMaxNetworkThreadCount) != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize maxNetworkThreadCount: %d", num);
            }
            return FileDownloadProperties.getValidNetworkThreadCount(num.intValue());
        }
        return getDefaultMaxNetworkThreadCount();
    }
}
