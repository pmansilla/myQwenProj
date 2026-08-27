package com.czw.smartkit.homeModule.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class FragmentTrain_ViewBinding implements Unbinder {
    private FragmentTrain target;

    @UiThread
    public FragmentTrain_ViewBinding(FragmentTrain fragmentTrain, View view) {
        this.target = fragmentTrain;
        fragmentTrain.sportTimeTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.sport_time, "field 'sportTimeTextView'", TextView.class);
        fragmentTrain.sportCalorieTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.sport_kcal, "field 'sportCalorieTextView'", TextView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        FragmentTrain fragmentTrain = this.target;
        if (fragmentTrain == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        fragmentTrain.sportTimeTextView = null;
        fragmentTrain.sportCalorieTextView = null;
    }
}
