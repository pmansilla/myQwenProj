package com.liulishuo.filedownloader.message;

import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class MessageSnapshotThreadPool {
    private final List<FlowSingleExecutor> executorList = new ArrayList();
    private final MessageSnapshotFlow.MessageReceiver receiver;

    /* loaded from: classes.dex */
    public class FlowSingleExecutor {
        private final List<Integer> enQueueTaskIdList = new ArrayList();
        private final Executor mExecutor;

        public FlowSingleExecutor(int i) {
            this.mExecutor = FileDownloadExecutors.newDefaultThreadPool(1, "Flow-" + i);
        }

        public void enqueue(int i) {
            this.enQueueTaskIdList.add(Integer.valueOf(i));
        }

        public void execute(final MessageSnapshot messageSnapshot) {
            this.mExecutor.execute(new Runnable() { // from class: com.liulishuo.filedownloader.message.MessageSnapshotThreadPool.FlowSingleExecutor.1
                @Override // java.lang.Runnable
                public void run() {
                    MessageSnapshotThreadPool.this.receiver.receive(messageSnapshot);
                    FlowSingleExecutor.this.enQueueTaskIdList.remove(Integer.valueOf(messageSnapshot.getId()));
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MessageSnapshotThreadPool(int i, MessageSnapshotFlow.MessageReceiver messageReceiver) {
        this.receiver = messageReceiver;
        for (int i2 = 0; i2 < i; i2++) {
            this.executorList.add(new FlowSingleExecutor(i2));
        }
    }

    public void execute(MessageSnapshot messageSnapshot) {
        FlowSingleExecutor flowSingleExecutor = null;
        try {
            synchronized (this.executorList) {
                int id = messageSnapshot.getId();
                Iterator<FlowSingleExecutor> it = this.executorList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    FlowSingleExecutor next = it.next();
                    if (next.enQueueTaskIdList.contains(Integer.valueOf(id))) {
                        flowSingleExecutor = next;
                        break;
                    }
                }
                if (flowSingleExecutor == null) {
                    int i = 0;
                    Iterator<FlowSingleExecutor> it2 = this.executorList.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        FlowSingleExecutor next2 = it2.next();
                        if (next2.enQueueTaskIdList.size() <= 0) {
                            flowSingleExecutor = next2;
                            break;
                        } else if (i == 0 || next2.enQueueTaskIdList.size() < i) {
                            i = next2.enQueueTaskIdList.size();
                            flowSingleExecutor = next2;
                        }
                    }
                }
                flowSingleExecutor.enqueue(id);
            }
        } finally {
            flowSingleExecutor.execute(messageSnapshot);
        }
    }
}
