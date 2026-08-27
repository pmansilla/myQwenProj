package com.liulishuo.filedownloader.i;

import android.app.Notification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;
import com.liulishuo.filedownloader.model.FileDownloadHeader;

/* loaded from: classes.dex */
public interface IFileDownloadIPCService extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IFileDownloadIPCService {
        private static final String DESCRIPTOR = "com.liulishuo.filedownloader.i.IFileDownloadIPCService";
        static final int TRANSACTION_checkDownloading = 3;
        static final int TRANSACTION_clearAllTaskData = 15;
        static final int TRANSACTION_clearTaskData = 14;
        static final int TRANSACTION_getSofar = 8;
        static final int TRANSACTION_getStatus = 10;
        static final int TRANSACTION_getTotal = 9;
        static final int TRANSACTION_isIdle = 11;
        static final int TRANSACTION_pause = 5;
        static final int TRANSACTION_pauseAllTasks = 6;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_setMaxNetworkThreadCount = 7;
        static final int TRANSACTION_start = 4;
        static final int TRANSACTION_startForeground = 12;
        static final int TRANSACTION_stopForeground = 13;
        static final int TRANSACTION_unregisterCallback = 2;

        /* loaded from: classes.dex */
        private static class Proxy implements IFileDownloadIPCService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public boolean checkDownloading(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public void clearAllTaskData() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public boolean clearTaskData(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public long getSofar(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public byte getStatus(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readByte();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public long getTotal(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public boolean isIdle() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public boolean pause(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public void pauseAllTasks() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public void registerCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iFileDownloadIPCCallback != null ? iFileDownloadIPCCallback.asBinder() : null);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public boolean setMaxNetworkThreadCount(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public void start(String str, String str2, boolean z, int i, int i2, int i3, boolean z2, FileDownloadHeader fileDownloadHeader, boolean z3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(z2 ? 1 : 0);
                    if (fileDownloadHeader != null) {
                        obtain.writeInt(1);
                        fileDownloadHeader.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(z3 ? 1 : 0);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public void startForeground(int i, Notification notification) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (notification != null) {
                        obtain.writeInt(1);
                        notification.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(12, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public void stopForeground(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(13, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCService
            public void unregisterCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iFileDownloadIPCCallback != null ? iFileDownloadIPCCallback.asBinder() : null);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFileDownloadIPCService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IFileDownloadIPCService)) ? new Proxy(iBinder) : (IFileDownloadIPCService) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback(IFileDownloadIPCCallback.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterCallback(IFileDownloadIPCCallback.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean checkDownloading = checkDownloading(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(checkDownloading ? 1 : 0);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    start(parcel.readString(), parcel.readString(), parcel.readInt() != 0, parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0 ? FileDownloadHeader.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean pause = pause(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(pause ? 1 : 0);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    pauseAllTasks();
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean maxNetworkThreadCount = setMaxNetworkThreadCount(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(maxNetworkThreadCount ? 1 : 0);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    long sofar = getSofar(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeLong(sofar);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    long total = getTotal(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeLong(total);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    byte status = getStatus(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeByte(status);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isIdle = isIdle();
                    parcel2.writeNoException();
                    parcel2.writeInt(isIdle ? 1 : 0);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    startForeground(parcel.readInt(), parcel.readInt() != 0 ? (Notification) Notification.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    stopForeground(parcel.readInt() != 0);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean clearTaskData = clearTaskData(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(clearTaskData ? 1 : 0);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    clearAllTaskData();
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    boolean checkDownloading(String str, String str2) throws RemoteException;

    void clearAllTaskData() throws RemoteException;

    boolean clearTaskData(int i) throws RemoteException;

    long getSofar(int i) throws RemoteException;

    byte getStatus(int i) throws RemoteException;

    long getTotal(int i) throws RemoteException;

    boolean isIdle() throws RemoteException;

    boolean pause(int i) throws RemoteException;

    void pauseAllTasks() throws RemoteException;

    void registerCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) throws RemoteException;

    boolean setMaxNetworkThreadCount(int i) throws RemoteException;

    void start(String str, String str2, boolean z, int i, int i2, int i3, boolean z2, FileDownloadHeader fileDownloadHeader, boolean z3) throws RemoteException;

    void startForeground(int i, Notification notification) throws RemoteException;

    void stopForeground(boolean z) throws RemoteException;

    void unregisterCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) throws RemoteException;
}
