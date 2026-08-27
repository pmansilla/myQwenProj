package com.liulishuo.filedownloader.event;

import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class DownloadEventPoolImpl implements IDownloadEventPool {
    private final Executor threadPool = FileDownloadExecutors.newDefaultThreadPool(10, "EventPool");
    private final HashMap<String, LinkedList<IDownloadListener>> listenersMap = new HashMap<>();

    private void trigger(LinkedList<IDownloadListener> linkedList, IDownloadEvent iDownloadEvent) {
        for (Object obj : linkedList.toArray()) {
            if (obj != null && ((IDownloadListener) obj).callback(iDownloadEvent)) {
                break;
            }
        }
        if (iDownloadEvent.callback != null) {
            iDownloadEvent.callback.run();
        }
    }

    @Override // com.liulishuo.filedownloader.event.IDownloadEventPool
    public boolean addListener(String str, IDownloadListener iDownloadListener) {
        boolean add;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "setListener %s", str);
        }
        if (iDownloadListener == null) {
            throw new IllegalArgumentException("listener must not be null!");
        }
        LinkedList<IDownloadListener> linkedList = this.listenersMap.get(str);
        if (linkedList == null) {
            synchronized (str.intern()) {
                linkedList = this.listenersMap.get(str);
                if (linkedList == null) {
                    HashMap<String, LinkedList<IDownloadListener>> hashMap = this.listenersMap;
                    LinkedList<IDownloadListener> linkedList2 = new LinkedList<>();
                    hashMap.put(str, linkedList2);
                    linkedList = linkedList2;
                }
            }
        }
        synchronized (str.intern()) {
            add = linkedList.add(iDownloadListener);
        }
        return add;
    }

    @Override // com.liulishuo.filedownloader.event.IDownloadEventPool
    public void asyncPublishInNewThread(final IDownloadEvent iDownloadEvent) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "asyncPublishInNewThread %s", iDownloadEvent.getId());
        }
        if (iDownloadEvent == null) {
            throw new IllegalArgumentException("event must not be null!");
        }
        this.threadPool.execute(new Runnable() { // from class: com.liulishuo.filedownloader.event.DownloadEventPoolImpl.1
            @Override // java.lang.Runnable
            public void run() {
                DownloadEventPoolImpl.this.publish(iDownloadEvent);
            }
        });
    }

    @Override // com.liulishuo.filedownloader.event.IDownloadEventPool
    public boolean publish(IDownloadEvent iDownloadEvent) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "publish %s", iDownloadEvent.getId());
        }
        if (iDownloadEvent == null) {
            throw new IllegalArgumentException("event must not be null!");
        }
        String id = iDownloadEvent.getId();
        LinkedList<IDownloadListener> linkedList = this.listenersMap.get(id);
        if (linkedList == null) {
            synchronized (id.intern()) {
                linkedList = this.listenersMap.get(id);
                if (linkedList == null) {
                    if (FileDownloadLog.NEED_LOG) {
                        FileDownloadLog.d(this, "No listener for this event %s", id);
                    }
                    return false;
                }
            }
        }
        trigger(linkedList, iDownloadEvent);
        return true;
    }

    @Override // com.liulishuo.filedownloader.event.IDownloadEventPool
    public boolean removeListener(String str, IDownloadListener iDownloadListener) {
        boolean remove;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "removeListener %s", str);
        }
        LinkedList<IDownloadListener> linkedList = this.listenersMap.get(str);
        if (linkedList == null) {
            synchronized (str.intern()) {
                linkedList = this.listenersMap.get(str);
            }
        }
        if (linkedList == null || iDownloadListener == null) {
            return false;
        }
        synchronized (str.intern()) {
            remove = linkedList.remove(iDownloadListener);
            if (linkedList.size() <= 0) {
                this.listenersMap.remove(str);
            }
        }
        return remove;
    }
}
