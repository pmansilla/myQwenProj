package com.amap.location.common.log;

/* loaded from: classes.dex */
public class LogConfig {
    private boolean mFileLogEnable;
    private a mILogToServerImpl;
    private boolean mIsTraceUpToServer;
    private boolean mIsTraceWriteToFile;
    private String mLogFileDir;
    private int mLogFileMaxCount;
    private int mLogMemoryBufferSize;
    private boolean mLogcatEnable;
    private Product mProduct;
    private boolean mServerLogEnable;
    private int mSignalLogFileLimit;

    /* loaded from: classes.dex */
    public static class Builder {
        private LogConfig logConfig = new LogConfig();

        public LogConfig build(Product product, String str) {
            if (product == null) {
                throw new IllegalArgumentException("product 不能为 null ");
            }
            if (this.logConfig.mFileLogEnable && (str == null || str.trim().length() == 0)) {
                this.logConfig.mFileLogEnable = false;
                str = null;
            }
            this.logConfig.mProduct = product;
            this.logConfig.mLogFileDir = str;
            return this.logConfig;
        }

        public Builder setFileLogEnable(boolean z) {
            this.logConfig.mFileLogEnable = z;
            return this;
        }

        public Builder setLogFileMaxCount(int i) {
            this.logConfig.mLogFileMaxCount = i;
            return this;
        }

        public Builder setLogMemoryBufferSize(int i) {
            this.logConfig.mLogMemoryBufferSize = i;
            return this;
        }

        public Builder setLogToServer(a aVar) {
            this.logConfig.mILogToServerImpl = aVar;
            return this;
        }

        public Builder setLogcatEnable(boolean z) {
            this.logConfig.mLogcatEnable = z;
            return this;
        }

        public Builder setServerLogEnable(boolean z) {
            this.logConfig.mServerLogEnable = z;
            return this;
        }

        public Builder setSignalLogFileLimit(int i) {
            this.logConfig.mSignalLogFileLimit = i;
            return this;
        }

        public Builder setTraceUpToServer(boolean z) {
            this.logConfig.mIsTraceUpToServer = z;
            return this;
        }

        public Builder setTraceWriteToFile(boolean z) {
            this.logConfig.mIsTraceWriteToFile = z;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public enum Product {
        FLP,
        NLP,
        SDK
    }

    /* loaded from: classes.dex */
    public interface a {
        void a(String str);

        boolean a();
    }

    private LogConfig() {
        this.mLogcatEnable = false;
        this.mFileLogEnable = false;
        this.mServerLogEnable = false;
        this.mLogFileDir = "";
        this.mProduct = Product.SDK;
        this.mIsTraceWriteToFile = false;
        this.mIsTraceUpToServer = true;
        this.mLogMemoryBufferSize = 204800;
        this.mSignalLogFileLimit = 1048576;
        this.mLogFileMaxCount = 20;
    }

    public String getLogFileDir() {
        return this.mLogFileDir;
    }

    public int getLogFileMaxCount() {
        return this.mLogFileMaxCount;
    }

    public int getLogMemoryBufferSize() {
        return this.mLogMemoryBufferSize;
    }

    public a getLogToServerImpl() {
        return this.mILogToServerImpl;
    }

    public Product getProduct() {
        return this.mProduct;
    }

    public int getSignalLogFileLimit() {
        return this.mSignalLogFileLimit;
    }

    public boolean isFileLogEnable() {
        return this.mFileLogEnable;
    }

    public boolean isLogcatEnable() {
        return this.mLogcatEnable;
    }

    public boolean isServerLogEnable() {
        return this.mServerLogEnable;
    }

    public boolean isTraceUpToServer() {
        return this.mIsTraceUpToServer;
    }

    public boolean isTraceWriteToFile() {
        return this.mIsTraceWriteToFile;
    }
}
