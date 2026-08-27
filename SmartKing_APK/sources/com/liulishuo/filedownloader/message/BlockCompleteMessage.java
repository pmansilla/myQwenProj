package com.liulishuo.filedownloader.message;

import com.liulishuo.filedownloader.util.FileDownloadUtils;

/* loaded from: classes.dex */
public interface BlockCompleteMessage {

    /* loaded from: classes.dex */
    public static class BlockCompleteMessageImpl extends MessageSnapshot implements BlockCompleteMessage {
        private final MessageSnapshot mCompletedSnapshot;

        public BlockCompleteMessageImpl(MessageSnapshot messageSnapshot) {
            super(messageSnapshot.getId());
            if (messageSnapshot.getStatus() != -3) {
                throw new IllegalArgumentException(FileDownloadUtils.formatString("can't create the block complete message for id[%d], status[%d]", Integer.valueOf(messageSnapshot.getId()), Byte.valueOf(messageSnapshot.getStatus())));
            }
            this.mCompletedSnapshot = messageSnapshot;
        }

        @Override // com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) 4;
        }

        @Override // com.liulishuo.filedownloader.message.BlockCompleteMessage
        public MessageSnapshot transmitToCompleted() {
            return this.mCompletedSnapshot;
        }
    }

    MessageSnapshot transmitToCompleted();
}
