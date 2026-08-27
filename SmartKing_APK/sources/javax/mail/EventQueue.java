package javax.mail;

import java.util.EventListener;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import javax.mail.event.MailEvent;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class EventQueue implements Runnable {
    private static WeakHashMap<ClassLoader, EventQueue> appq;
    private Executor executor;
    private volatile BlockingQueue<QueueElement> q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class QueueElement {
        MailEvent event;
        Vector<? extends EventListener> vector;

        QueueElement(MailEvent mailEvent, Vector<? extends EventListener> vector) {
            this.event = null;
            this.vector = null;
            this.event = mailEvent;
            this.vector = vector;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class TerminatorEvent extends MailEvent {
        private static final long serialVersionUID = -2481895000841664111L;

        TerminatorEvent() {
            super(new Object());
        }

        @Override // javax.mail.event.MailEvent
        public void dispatch(Object obj) {
            Thread.currentThread().interrupt();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventQueue(Executor executor) {
        this.executor = executor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized EventQueue getApplicationEventQueue(Executor executor) {
        EventQueue eventQueue;
        synchronized (EventQueue.class) {
            ClassLoader contextClassLoader = Session.getContextClassLoader();
            if (appq == null) {
                appq = new WeakHashMap<>();
            }
            eventQueue = appq.get(contextClassLoader);
            if (eventQueue == null) {
                eventQueue = new EventQueue(executor);
                appq.put(contextClassLoader, eventQueue);
            }
        }
        return eventQueue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void enqueue(MailEvent mailEvent, Vector<? extends EventListener> vector) {
        if (this.q == null) {
            this.q = new LinkedBlockingQueue();
            if (this.executor != null) {
                this.executor.execute(this);
            } else {
                Thread thread = new Thread(this, "JavaMail-EventQueue");
                thread.setDaemon(true);
                thread.start();
            }
        }
        this.q.add(new QueueElement(mailEvent, vector));
    }

    @Override // java.lang.Runnable
    public void run() {
        BlockingQueue<QueueElement> blockingQueue = this.q;
        if (blockingQueue == null) {
            return;
        }
        while (true) {
            try {
                QueueElement take = blockingQueue.take();
                MailEvent mailEvent = take.event;
                Vector<? extends EventListener> vector = take.vector;
                for (int i = 0; i < vector.size(); i++) {
                    try {
                        mailEvent.dispatch(vector.elementAt(i));
                    } catch (Throwable th) {
                        if (th instanceof InterruptedException) {
                            return;
                        }
                    }
                }
            } catch (InterruptedException unused) {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void terminateQueue() {
        if (this.q != null) {
            Vector vector = new Vector();
            vector.setSize(1);
            this.q.add(new QueueElement(new TerminatorEvent(), vector));
            this.q = null;
        }
    }
}
