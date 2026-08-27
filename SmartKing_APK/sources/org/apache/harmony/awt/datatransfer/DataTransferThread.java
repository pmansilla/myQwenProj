package org.apache.harmony.awt.datatransfer;

/* loaded from: classes2.dex */
public class DataTransferThread extends Thread {
    private final DTK dtk;

    public DataTransferThread(DTK dtk) {
        super("AWT-DataTransferThread");
        setDaemon(true);
        this.dtk = dtk;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        synchronized (this) {
            try {
                this.dtk.initDragAndDrop();
            } finally {
                notifyAll();
            }
        }
        this.dtk.runEventLoop();
    }

    @Override // java.lang.Thread
    public void start() {
        synchronized (this) {
            super.start();
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
