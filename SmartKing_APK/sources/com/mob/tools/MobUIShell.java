package com.mob.tools;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.mob.tools.network.KVPair;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class MobUIShell extends Activity {
    private static HashMap<String, FakeActivity> executors = new HashMap<>();
    private FakeActivity executor;

    static {
        MobLog.getInstance().d("===============================", new Object[0]);
        MobLog.getInstance().d("MobTools " + "2021-03-23".replace("-0", "-").replace("-", "."), new Object[0]);
        MobLog.getInstance().d("===============================", new Object[0]);
    }

    private FakeActivity activityForName(String str) {
        Object newInstance;
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            if (str.startsWith(".")) {
                str = getPackageName() + str;
            }
            String importClass = ReflectHelper.importClass(str);
            if (TextUtils.isEmpty(importClass) || (newInstance = ReflectHelper.newInstance(importClass, new Object[0])) == null || !(newInstance instanceof FakeActivity)) {
                return null;
            }
            return (FakeActivity) newInstance;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    private boolean fixOrientation() {
        if (Build.VERSION.SDK_INT > 27) {
            return false;
        }
        try {
            Field declaredField = Activity.class.getDeclaredField("mActivityInfo");
            declaredField.setAccessible(true);
            ((ActivityInfo) declaredField.get(this)).screenOrientation = -1;
            declaredField.setAccessible(false);
            return true;
        } catch (Exception e) {
            MobLog.getInstance().w(e, "Fix orientation for 8.0 encountered exception", new Object[0]);
            return false;
        }
    }

    private boolean initExecutor() {
        if (this.executor == null) {
            Intent intent = getIntent();
            Uri data = intent.getData();
            if (data != null && "mobui".equals(data.getScheme())) {
                this.executor = activityForName(data.getHost());
                if (this.executor != null) {
                    MobLog.getInstance().i("MobUIShell found executor: " + this.executor.getClass(), new Object[0]);
                    this.executor.setActivity(this);
                    return true;
                }
            }
            try {
                String stringExtra = intent.getStringExtra("launch_time");
                String stringExtra2 = intent.getStringExtra("executor_name");
                this.executor = executors.remove(stringExtra);
                if (this.executor == null) {
                    this.executor = executors.remove(intent.getScheme());
                    if (this.executor == null) {
                        this.executor = getDefault();
                        if (this.executor == null) {
                            MobLog.getInstance().w(new RuntimeException("Executor lost! launchTime = " + stringExtra + ", executorName: " + stringExtra2));
                            return false;
                        }
                    }
                }
                MobLog.getInstance().i("MobUIShell found executor: " + this.executor.getClass(), new Object[0]);
                this.executor.setActivity(this);
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
                return false;
            }
        }
        return true;
    }

    private boolean isTranslucentOrFloating() {
        Method method;
        boolean booleanValue;
        boolean z = false;
        if (Build.VERSION.SDK_INT > 27) {
            return false;
        }
        try {
            TypedArray obtainStyledAttributes = this.executor.activity.obtainStyledAttributes((int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null));
            method = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            method.setAccessible(true);
            booleanValue = ((Boolean) method.invoke(null, obtainStyledAttributes)).booleanValue();
        } catch (Exception e) {
            e = e;
        }
        try {
            method.setAccessible(false);
            return booleanValue;
        } catch (Exception e2) {
            z = booleanValue;
            e = e2;
            MobLog.getInstance().w(e);
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String registerExecutor(Object obj) {
        return registerExecutor(String.valueOf(System.currentTimeMillis()), obj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String registerExecutor(String str, Object obj) {
        executors.put(str, (FakeActivity) obj);
        return str;
    }

    public static Uri toMobUIShellUri(String str, HashMap<String, Object> hashMap) {
        ArrayList arrayList = new ArrayList();
        for (String str2 : hashMap.keySet()) {
            arrayList.add(new KVPair(str2, String.valueOf(hashMap.get(str2))));
        }
        return Uri.parse("mobui://" + str + "?" + ResHelper.encodeUrl((ArrayList<KVPair<String>>) arrayList));
    }

    @Override // android.app.Activity
    public void finish() {
        if (this.executor == null || !this.executor.onFinish()) {
            super.finish();
        }
    }

    public FakeActivity getDefault() {
        String str;
        try {
            str = getPackageManager().getActivityInfo(getComponentName(), 128).metaData.getString("defaultActivity");
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            str = null;
        }
        return activityForName(str);
    }

    public Object getExecutor() {
        return this.executor;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (this.executor != null) {
            this.executor.onActivityResult(i, i2, intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.executor != null) {
            this.executor.onConfigurationChanged(configuration);
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        if (!initExecutor()) {
            super.onCreate(bundle);
            finish();
            return;
        }
        MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onCreate", new Object[0]);
        if (Build.VERSION.SDK_INT == 26 && isTranslucentOrFloating()) {
            fixOrientation();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            this.executor.activity.getWindow().addFlags(Integer.MIN_VALUE);
            this.executor.activity.getWindow().setStatusBarColor(0);
        }
        super.onCreate(bundle);
        this.executor.onCreate();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return this.executor != null ? this.executor.onCreateOptionsMenu(menu) : super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        if (this.executor != null) {
            this.executor.sendResult();
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onDestroy", new Object[0]);
            this.executor.onDestroy();
        }
        super.onDestroy();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        try {
            if (this.executor != null ? this.executor.onKeyEvent(i, keyEvent) : false) {
                return true;
            }
            return super.onKeyDown(i, keyEvent);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return false;
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        try {
            if (this.executor != null ? this.executor.onKeyEvent(i, keyEvent) : false) {
                return true;
            }
            return super.onKeyUp(i, keyEvent);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return false;
        }
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        if (this.executor == null) {
            super.onNewIntent(intent);
        } else {
            this.executor.onNewIntent(intent);
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return this.executor != null ? this.executor.onOptionsItemSelected(menuItem) : super.onOptionsItemSelected(menuItem);
    }

    @Override // android.app.Activity
    protected void onPause() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onPause", new Object[0]);
            this.executor.onPause();
        }
        super.onPause();
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (this.executor != null) {
            this.executor.onRequestPermissionsResult(i, strArr, iArr);
        }
    }

    @Override // android.app.Activity
    protected void onRestart() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onRestart", new Object[0]);
            this.executor.onRestart();
        }
        super.onRestart();
    }

    @Override // android.app.Activity
    protected void onResume() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onResume", new Object[0]);
            this.executor.onResume();
        }
        super.onResume();
    }

    @Override // android.app.Activity
    protected void onStart() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onStart", new Object[0]);
            this.executor.onStart();
        }
        super.onStart();
    }

    @Override // android.app.Activity
    protected void onStop() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onStop", new Object[0]);
            this.executor.onStop();
        }
        super.onStop();
    }

    @Override // android.app.Activity
    public void setContentView(int i) {
        setContentView(LayoutInflater.from(this).inflate(i, (ViewGroup) null));
    }

    @Override // android.app.Activity
    public void setContentView(View view) {
        if (view == null) {
            return;
        }
        super.setContentView(view);
        if (this.executor != null) {
            this.executor.setContentView(view);
        }
    }

    @Override // android.app.Activity
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        if (view == null) {
            return;
        }
        if (layoutParams == null) {
            super.setContentView(view);
        } else {
            super.setContentView(view, layoutParams);
        }
        if (this.executor != null) {
            this.executor.setContentView(view);
        }
    }

    @Override // android.app.Activity
    public void setRequestedOrientation(int i) {
        if (Build.VERSION.SDK_INT == 26 && isTranslucentOrFloating()) {
            return;
        }
        super.setRequestedOrientation(i);
    }

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public final void setTheme(int i) {
        if (initExecutor()) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            int i2 = 0;
            while (i2 < stackTrace.length) {
                if (stackTrace[i2].toString().startsWith("java.lang.Thread.getStackTrace") && (i2 = i2 + 2) < stackTrace.length) {
                    int onSetTheme = this.executor.onSetTheme(i, stackTrace[i2].toString().startsWith("android.app.ActivityThread.performLaunchActivity"));
                    if (onSetTheme > 0) {
                        super.setTheme(onSetTheme);
                        return;
                    }
                    return;
                }
                i2++;
            }
        }
        super.setTheme(i);
    }

    @Override // android.app.Activity
    public void startActivityForResult(Intent intent, int i) {
        if (this.executor != null) {
            this.executor.beforeStartActivityForResult(intent, i, null);
        }
        super.startActivityForResult(intent, i);
    }

    @Override // android.app.Activity
    public void startActivityForResult(Intent intent, int i, Bundle bundle) {
        if (this.executor != null) {
            this.executor.beforeStartActivityForResult(intent, i, bundle);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            super.startActivityForResult(intent, i, bundle);
        } else {
            super.startActivityForResult(intent, i);
        }
    }
}
