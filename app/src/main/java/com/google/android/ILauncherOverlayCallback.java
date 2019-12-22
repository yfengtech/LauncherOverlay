package com.google.android;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by yf.
 *
 * @date 2019-12-22
 */
public interface ILauncherOverlayCallback extends IInterface {

    void overlayScrollChanged(float progress) throws RemoteException;

    void overlayStatusChanged(int status) throws RemoteException;

    abstract class Stub extends Binder implements ILauncherOverlayCallback {
        static final int OVERLAY_SCROLL_CHANGED_TRANSACTION = 1;
        static final int OVERLAY_STATUS_CHANGED_TRANSACTION = 2;

        public Stub() {
            attachInterface(this, ILauncherOverlayCallback.class.getName());
        }

        public static ILauncherOverlayCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }

            IInterface iin = obj.queryLocalInterface(ILauncherOverlayCallback.class.getName());
            if (iin != null && iin instanceof ILauncherOverlayCallback) {
                return (ILauncherOverlayCallback) iin;
            } else {
                return new Proxy(obj);
            }
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION:
                    reply.writeString(ILauncherOverlay.class.getName());
                    return true;
                case OVERLAY_SCROLL_CHANGED_TRANSACTION:
                    data.enforceInterface(ILauncherOverlayCallback.class.getName());
                    overlayScrollChanged(data.readFloat());
                    return true;
                case OVERLAY_STATUS_CHANGED_TRANSACTION:
                    data.enforceInterface(ILauncherOverlayCallback.class.getName());
                    overlayStatusChanged(data.readInt());
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ILauncherOverlayCallback {
            private IBinder mRemote;

            public Proxy(IBinder remote) {
                mRemote = remote;
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }

            @Override
            public void overlayScrollChanged(float progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILauncherOverlayCallback.class.getName());
                    _data.writeFloat(progress);

                    mRemote.transact(OVERLAY_SCROLL_CHANGED_TRANSACTION, _data, null, FLAG_ONEWAY);
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void overlayStatusChanged(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILauncherOverlayCallback.class.getName());
                    _data.writeInt(status);

                    mRemote.transact(OVERLAY_STATUS_CHANGED_TRANSACTION, _data, null, FLAG_ONEWAY);
                } finally {
                    _data.recycle();
                }
            }
        }

    }
}
