package com.czw.smartkit.base;

import com.czw.modes.fragment.RootFragment;
import com.czw.smartkit.net.NetImpl;

/* loaded from: classes.dex */
public abstract class BaseFragment extends RootFragment {
    private final NetImpl netImpl = NetImpl.getNetImpl();

    protected synchronized NetImpl getNet() {
        return this.netImpl.loadActivity(getActivity());
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
    }

    public void runOnUiThread(Runnable runnable) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(runnable);
    }
}
