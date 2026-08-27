package com.liulishuo.filedownloader.message;

/* loaded from: classes.dex */
public class MessageSnapshotFlow {
    private volatile MessageSnapshotThreadPool flowThreadPool;
    private volatile MessageReceiver receiver;

    /* loaded from: classes.dex */
    public static final class HolderClass {
        private static final MessageSnapshotFlow INSTANCE = new MessageSnapshotFlow();
    }

    /* loaded from: classes.dex */
    public interface MessageReceiver {
        void receive(MessageSnapshot messageSnapshot);
    }

    public static MessageSnapshotFlow getImpl() {
        return HolderClass.INSTANCE;
    }

    public void inflow(MessageSnapshot messageSnapshot) {
        if (messageSnapshot instanceof IFlowDirectly) {
            if (this.receiver != null) {
                this.receiver.receive(messageSnapshot);
            }
        } else if (this.flowThreadPool != null) {
            this.flowThreadPool.execute(messageSnapshot);
        }
    }

    public void setReceiver(MessageReceiver messageReceiver) {
        this.receiver = messageReceiver;
        if (messageReceiver == null) {
            this.flowThreadPool = null;
        } else {
            this.flowThreadPool = new MessageSnapshotThreadPool(5, messageReceiver);
        }
    }
}
