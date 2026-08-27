package com.loc;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* compiled from: ILocationProviderService.java */
/* loaded from: classes.dex */
public interface s extends IInterface {

    /* compiled from: ILocationProviderService.java */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements s {

        /* compiled from: ILocationProviderService.java */
        /* renamed from: com.loc.s$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0048a implements s {
            private IBinder a;

            C0048a(IBinder iBinder) {
                this.a = iBinder;
            }

            @Override // com.loc.s
            public final int a(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.amap.api.service.locationprovider.ILocationProviderService");
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    if (obtain2.readInt() != 0) {
                        bundle.readFromParcel(obtain2);
                    }
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public final IBinder asBinder() {
                return this.a;
            }
        }

        public static s a(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.amap.api.service.locationprovider.ILocationProviderService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof s)) ? new C0048a(iBinder) : (s) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString("com.amap.api.service.locationprovider.ILocationProviderService");
                return true;
            }
            parcel.enforceInterface("com.amap.api.service.locationprovider.ILocationProviderService");
            Bundle bundle = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null;
            int a = a(bundle);
            parcel2.writeNoException();
            parcel2.writeInt(a);
            if (bundle != null) {
                parcel2.writeInt(1);
                bundle.writeToParcel(parcel2, 1);
            } else {
                parcel2.writeInt(0);
            }
            return true;
        }
    }

    int a(Bundle bundle) throws RemoteException;
}
