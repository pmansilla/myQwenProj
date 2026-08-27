package com.czw.modes.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.czw.modes.activity.RootActivity;

/* loaded from: classes.dex */
public abstract class RootFragment extends Fragment {
    protected RootActivity activity;
    protected View mView;

    /* JADX INFO: Access modifiers changed from: protected */
    public <T extends View> T $View(int i) {
        return (T) this.mView.findViewById(i);
    }

    protected String $str(int i) {
        return getString(i);
    }

    public abstract void initAfterCreate();

    public void jumpAndFish(Class<?> cls) {
        startActivity(new Intent(getActivity(), cls));
        getActivity().finish();
    }

    public void jumpFor(Intent intent, int i) {
        startActivityForResult(intent, i);
    }

    public void jumpFor(Class<?> cls, int i) {
        startActivityForResult(new Intent(getActivity(), cls), i);
    }

    public void jumpTo(Intent intent) {
        startActivity(intent);
    }

    public void jumpTo(Class<?> cls) {
        startActivity(new Intent(getActivity(), cls));
    }

    public abstract int loadLayout();

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (RootActivity) activity;
    }

    public void onCreateMap(Bundle bundle) {
    }

    @Override // android.support.v4.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        if (this.mView != null) {
            ViewGroup viewGroup2 = (ViewGroup) this.mView.getParent();
            if (viewGroup2 != null) {
                viewGroup2.removeView(this.mView);
            }
        } else {
            this.mView = layoutInflater.inflate(loadLayout(), viewGroup, false);
            ButterKnife.bind(this, this.mView);
            onCreateMap(bundle);
            initAfterCreate();
            if (Build.VERSION.SDK_INT >= 19) {
                WindowManager.LayoutParams attributes = this.activity.getWindow().getAttributes();
                attributes.flags = 67108864 | attributes.flags;
            }
        }
        return this.mView;
    }

    public final void toast(int i) {
        toast(getString(i));
    }

    public final void toast(int i, int i2) {
        toast(getString(i), i2);
    }

    public final void toast(final String str) {
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.modes.fragment.RootFragment.1
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(RootFragment.this.getActivity(), str, 0).show();
            }
        });
    }

    public final void toast(final String str, int i) {
        new Handler().postDelayed(new Runnable() { // from class: com.czw.modes.fragment.RootFragment.2
            @Override // java.lang.Runnable
            public void run() {
                RootFragment.this.toast(str);
            }
        }, i);
    }
}
