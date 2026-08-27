package cn.sharesdk.onekeyshare.themes.classic;

import android.R;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeySharePage;
import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;
import cn.sharesdk.onekeyshare.themes.classic.land.FriendListPageLand;
import cn.sharesdk.onekeyshare.themes.classic.port.FriendListPagePort;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.twitter.Twitter;
import com.mob.MobSDK;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class EditPage extends OnekeySharePage implements View.OnClickListener, TextWatcher, Runnable {
    protected AsyncImageView aivThumb;
    protected EditText etContent;
    private OnekeyShareThemeImpl impl;
    protected LinearLayout llBottom;
    protected LinearLayout llPage;
    protected int maxBodyHeight;
    protected Platform platform;
    protected RelativeLayout rlThumb;
    protected RelativeLayout rlTitle;
    protected Platform.ShareParams sp;
    protected ScrollView svContent;
    protected Bitmap thumb;
    protected TextView tvAt;
    protected TextView tvCancel;
    protected TextView tvShare;
    protected TextView tvTextCouter;
    protected XView xvRemove;

    public EditPage(OnekeyShareThemeImpl onekeyShareThemeImpl) {
        super(onekeyShareThemeImpl);
        this.impl = onekeyShareThemeImpl;
    }

    private void cancelAndFinish() {
        ShareSDK.logDemoEvent(5, this.platform);
        finish();
    }

    private String getJoinSelectedUser(HashMap<String, Object> hashMap) {
        if (hashMap == null || !hashMap.containsKey("selected")) {
            return null;
        }
        ArrayList arrayList = (ArrayList) hashMap.get("selected");
        if ("FacebookMessenger".equals(((Platform) hashMap.get("platform")).getName())) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            sb.append('@');
            sb.append(str);
            sb.append(' ');
        }
        return sb.toString();
    }

    private void removeThumb() {
        this.sp.setImageArray(null);
        this.sp.setImageData(null);
        this.sp.setImagePath(null);
        this.sp.setImageUrl(null);
    }

    private void shareAndFinish() {
        int stringRes = ResHelper.getStringRes(this.activity, "ssdk_oks_sharing");
        if (stringRes > 0) {
            Toast.makeText(this.activity, stringRes, 0).show();
        }
        if (isDisableSSO()) {
            this.platform.SSOSetting(true);
        }
        this.platform.setPlatformActionListener(getCallback());
        this.platform.share(this.sp);
        this.impl.callback = null;
        finish();
    }

    private void showFriendList() {
        FriendListPage friendListPagePort = this.activity.getResources().getConfiguration().orientation == 1 ? new FriendListPagePort(this.impl) : new FriendListPageLand(this.impl);
        friendListPagePort.setPlatform(this.platform);
        friendListPagePort.showForResult(MobSDK.getContext(), null, this);
    }

    private void showThumb(Bitmap bitmap) {
        PicViewerPage picViewerPage = new PicViewerPage(this.impl);
        picViewerPage.setImageBitmap(bitmap);
        picViewerPage.show(this.activity, null);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isShowAtUserLayout(String str) {
        return SinaWeibo.NAME.equals(str) || "TencentWeibo".equals(str) || Facebook.NAME.equals(str) || Twitter.NAME.equals(str);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.tvCancel)) {
            cancelAndFinish();
            return;
        }
        if (view.equals(this.tvShare)) {
            this.sp.setText(this.etContent.getText().toString().trim());
            shareAndFinish();
            return;
        }
        if (view.equals(this.aivThumb)) {
            showThumb(this.thumb);
            return;
        }
        if (!view.equals(this.xvRemove)) {
            if (view.equals(this.tvAt)) {
                showFriendList();
            }
        } else {
            this.maxBodyHeight = 0;
            this.rlThumb.setVisibility(8);
            this.llPage.measure(0, 0);
            onTextChanged(this.etContent.getText(), 0, 0, 0);
            removeThumb();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        this.activity.getWindow().setBackgroundDrawable(new ColorDrawable(-789517));
    }

    @Override // com.mob.tools.FakeActivity
    public void onPause() {
        DeviceHelper.getInstance(this.activity).hideSoftInput(getContentView());
        super.onPause();
    }

    @Override // com.mob.tools.FakeActivity
    public void onResult(HashMap<String, Object> hashMap) {
        String joinSelectedUser = getJoinSelectedUser(hashMap);
        if (TextUtils.isEmpty(joinSelectedUser)) {
            return;
        }
        this.etContent.append(joinSelectedUser);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.tools.FakeActivity
    public int onSetTheme(int i, boolean z) {
        if (!isDialogMode()) {
            this.activity.getWindow().setSoftInputMode(37);
            return super.onSetTheme(i, z);
        }
        this.activity.requestWindowFeature(1);
        if (Build.VERSION.SDK_INT < 11) {
            return R.style.Theme.Dialog;
        }
        try {
            ReflectHelper.invokeInstanceMethod(this.activity, "setFinishOnTouchOutside", false);
            return R.style.Theme.Dialog;
        } catch (Throwable unused) {
            return R.style.Theme.Dialog;
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.tvTextCouter.setText(String.valueOf(charSequence.length()));
        if (this.maxBodyHeight == 0) {
            this.maxBodyHeight = (this.llPage.getHeight() - this.rlTitle.getHeight()) - this.llBottom.getHeight();
        }
        if (this.maxBodyHeight > 0) {
            this.svContent.post(this);
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        int height = this.svContent.getChildAt(0).getHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ResHelper.forceCast(this.svContent.getLayoutParams());
        if (height > this.maxBodyHeight && layoutParams.height != this.maxBodyHeight) {
            layoutParams.height = this.maxBodyHeight;
            this.svContent.setLayoutParams(layoutParams);
        } else {
            if (height >= this.maxBodyHeight || layoutParams.height != this.maxBodyHeight) {
                return;
            }
            layoutParams.height = -2;
            this.svContent.setLayoutParams(layoutParams);
        }
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public void setShareParams(Platform.ShareParams shareParams) {
        this.sp = shareParams;
    }
}
