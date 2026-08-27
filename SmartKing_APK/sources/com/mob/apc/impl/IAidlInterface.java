package com.mob.apc.impl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public abstract class IAidlInterface extends Binder implements IInterface {
    private static final String DESCRIPTOR = "com.mob.apc.impl.IAidlInterface";
    static final int TRANSACTION_SEND = 1;

    /* loaded from: classes.dex */
    private static class Proxy extends IAidlInterface {
        private IBinder mRemote;

        Proxy(IBinder iBinder) {
            this.mRemote = iBinder;
        }

        @Override // com.mob.apc.impl.IAidlInterface, android.os.IInterface
        public IBinder asBinder() {
            return this.mRemote;
        }

        @Override // android.os.Binder, android.os.IBinder
        public String getInterfaceDescriptor() {
            return IAidlInterface.DESCRIPTOR;
        }

        @Override // com.mob.apc.impl.IAidlInterface
        public InnerMessage send(InnerMessage innerMessage) throws RemoteException {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken(IAidlInterface.DESCRIPTOR);
                if (innerMessage != null) {
                    innerMessage.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.mRemote.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                return InnerMessage.createFromParcel(obtain2);
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
    }

    public IAidlInterface() {
        attachInterface(this, DESCRIPTOR);
    }

    public static IAidlInterface asInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
        return (queryLocalInterface == null || !(queryLocalInterface instanceof IAidlInterface)) ? new Proxy(iBinder) : (IAidlInterface) queryLocalInterface;
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this;
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (i != 1) {
            if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }
        parcel.enforceInterface(DESCRIPTOR);
        InnerMessage send = send(InnerMessage.createFromParcel(parcel));
        parcel2.writeNoException();
        if (send != null) {
            send.writeToParcel(parcel2, 1);
        } else {
            parcel2.writeInt(0);
        }
        return true;
    }

    public abstract InnerMessage send(InnerMessage innerMessage) throws RemoteException;
}
