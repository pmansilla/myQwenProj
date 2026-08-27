package com.luck.picture.lib.rxbus2;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class RxBus {
    public static final String LOG_BUS = "RXBUS_LOG";
    private static volatile RxBus defaultInstance;
    private Map<Class, List<Disposable>> subscriptionsByEventType = new HashMap();
    private Map<Object, List<Class>> eventTypesBySubscriber = new HashMap();
    private Map<Class, List<SubscriberMethod>> subscriberMethodByEventType = new HashMap();
    private final Subject<Object> bus = PublishSubject.create().toSerialized();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Message {
        private int code;
        private Object object;

        public Message() {
        }

        private Message(int i, Object obj) {
            this.code = i;
            this.object = obj;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getCode() {
            return this.code;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Object getObject() {
            return this.object;
        }

        public void setCode(int i) {
            this.code = i;
        }

        public void setObject(Object obj) {
            this.object = obj;
        }
    }

    private RxBus() {
    }

    private void addEventTypeToMap(Object obj, Class cls) {
        List<Class> list = this.eventTypesBySubscriber.get(obj);
        if (list == null) {
            list = new ArrayList<>();
            this.eventTypesBySubscriber.put(obj, list);
        }
        if (list.contains(cls)) {
            return;
        }
        list.add(cls);
    }

    private void addSubscriber(final SubscriberMethod subscriberMethod) {
        addSubscriptionToMap(subscriberMethod.subscriber.getClass(), postToObservable(subscriberMethod.code == -1 ? toObservable(subscriberMethod.eventType) : toObservable(subscriberMethod.code, subscriberMethod.eventType), subscriberMethod).subscribe(new Consumer<Object>() { // from class: com.luck.picture.lib.rxbus2.RxBus.3
            @Override // io.reactivex.functions.Consumer
            public void accept(Object obj) throws Exception {
                RxBus.this.callEvent(subscriberMethod, obj);
            }
        }));
    }

    private void addSubscriberToMap(Class cls, SubscriberMethod subscriberMethod) {
        List<SubscriberMethod> list = this.subscriberMethodByEventType.get(cls);
        if (list == null) {
            list = new ArrayList<>();
            this.subscriberMethodByEventType.put(cls, list);
        }
        if (list.contains(subscriberMethod)) {
            return;
        }
        list.add(subscriberMethod);
    }

    private void addSubscriptionToMap(Class cls, Disposable disposable) {
        List<Disposable> list = this.subscriptionsByEventType.get(cls);
        if (list == null) {
            list = new ArrayList<>();
            this.subscriptionsByEventType.put(cls, list);
        }
        if (list.contains(disposable)) {
            return;
        }
        list.add(disposable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callEvent(SubscriberMethod subscriberMethod, Object obj) {
        List<SubscriberMethod> list = this.subscriberMethodByEventType.get(obj.getClass());
        if (list == null || list.size() <= 0) {
            return;
        }
        for (SubscriberMethod subscriberMethod2 : list) {
            if (((Subscribe) subscriberMethod2.method.getAnnotation(Subscribe.class)).code() == subscriberMethod.code && subscriberMethod.subscriber.equals(subscriberMethod2.subscriber) && subscriberMethod.method.equals(subscriberMethod2.method)) {
                subscriberMethod2.invoke(obj);
            }
        }
    }

    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    private Flowable postToObservable(Flowable flowable, SubscriberMethod subscriberMethod) {
        Scheduler mainThread;
        switch (subscriberMethod.threadMode) {
            case MAIN:
                mainThread = AndroidSchedulers.mainThread();
                break;
            case NEW_THREAD:
                mainThread = Schedulers.newThread();
                break;
            case CURRENT_THREAD:
                mainThread = Schedulers.trampoline();
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscriberMethod.threadMode);
        }
        return flowable.observeOn(mainThread);
    }

    private <T> Flowable<T> toObservable(final int i, final Class<T> cls) {
        return this.bus.toFlowable(BackpressureStrategy.BUFFER).ofType(Message.class).filter(new Predicate<Message>() { // from class: com.luck.picture.lib.rxbus2.RxBus.2
            @Override // io.reactivex.functions.Predicate
            public boolean test(Message message) throws Exception {
                return message.getCode() == i && cls.isInstance(message.getObject());
            }
        }).map(new Function<Message, Object>() { // from class: com.luck.picture.lib.rxbus2.RxBus.1
            @Override // io.reactivex.functions.Function
            public Object apply(Message message) throws Exception {
                return message.getObject();
            }
        }).cast(cls);
    }

    private void unSubscribeByEventType(Class cls) {
        List<Disposable> list = this.subscriptionsByEventType.get(cls);
        if (list != null) {
            Iterator<Disposable> it = list.iterator();
            while (it.hasNext()) {
                Disposable next = it.next();
                if (next != null && !next.isDisposed()) {
                    next.dispose();
                    it.remove();
                }
            }
        }
    }

    private void unSubscribeMethodByEventType(Object obj, Class cls) {
        List<SubscriberMethod> list = this.subscriberMethodByEventType.get(cls);
        if (list != null) {
            Iterator<SubscriberMethod> it = list.iterator();
            while (it.hasNext()) {
                if (it.next().subscriber.equals(obj)) {
                    it.remove();
                }
            }
        }
    }

    public synchronized boolean isRegistered(Object obj) {
        return this.eventTypesBySubscriber.containsKey(obj);
    }

    public void post(Object obj) {
        this.bus.onNext(obj);
    }

    public void register(Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes != null && parameterTypes.length == 1) {
                    Class<?> cls = parameterTypes[0];
                    addEventTypeToMap(obj, cls);
                    Subscribe subscribe = (Subscribe) method.getAnnotation(Subscribe.class);
                    SubscriberMethod subscriberMethod = new SubscriberMethod(obj, method, cls, subscribe.code(), subscribe.threadMode());
                    addSubscriberToMap(cls, subscriberMethod);
                    addSubscriber(subscriberMethod);
                } else if (parameterTypes == null || parameterTypes.length == 0) {
                    addEventTypeToMap(obj, BusData.class);
                    Subscribe subscribe2 = (Subscribe) method.getAnnotation(Subscribe.class);
                    SubscriberMethod subscriberMethod2 = new SubscriberMethod(obj, method, BusData.class, subscribe2.code(), subscribe2.threadMode());
                    addSubscriberToMap(BusData.class, subscriberMethod2);
                    addSubscriber(subscriberMethod2);
                }
            }
        }
    }

    public void send(int i) {
        this.bus.onNext(new Message(i, new BusData()));
    }

    public void send(int i, Object obj) {
        this.bus.onNext(new Message(i, obj));
    }

    public <T> Flowable<T> toObservable(Class<T> cls) {
        return (Flowable<T>) this.bus.toFlowable(BackpressureStrategy.BUFFER).ofType(cls);
    }

    public void unregister(Object obj) {
        List<Class> list = this.eventTypesBySubscriber.get(obj);
        if (list != null) {
            for (Class cls : list) {
                unSubscribeByEventType(obj.getClass());
                unSubscribeMethodByEventType(obj, cls);
            }
            this.eventTypesBySubscriber.remove(obj);
        }
    }
}
