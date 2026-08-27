package cn.smssdk.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.entity.Profile;
import cn.smssdk.gui.util.GUISPDB;
import cn.smssdk.utils.SMSLog;
import com.mob.tools.FakeActivity;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AvatarPage extends FakeActivity implements View.OnClickListener {
    public static final String EXTRA_PROFILE = "extra_profile";
    private String avatarUrl;
    private Button btSubmit;
    private EventHandler handler;
    private AsyncImageView ivAvatar;
    private Dialog pd;
    private Profile profile;

    private void initView() {
        TextView textView = (TextView) findViewById(ResHelper.getIdRes(getContext(), "tv_left"));
        textView.setText("");
        textView.setOnClickListener(this);
        ((TextView) findViewById(ResHelper.getIdRes(getContext(), "tv_title"))).setText("");
        ((TextView) findViewById(ResHelper.getIdRes(getContext(), "tv_right"))).setVisibility(4);
        this.ivAvatar = (AsyncImageView) findViewById(ResHelper.getIdRes(getContext(), "iv_avatar"));
        this.ivAvatar.setRound(ResHelper.dipToPx(getContext(), 60));
        this.ivAvatar.setOnClickListener(this);
        this.btSubmit = (Button) findViewById(ResHelper.getIdRes(getContext(), "bt_submit_profile"));
        this.btSubmit.setOnClickListener(this);
        Intent intent = this.activity.getIntent();
        if (intent != null) {
            this.profile = (Profile) intent.getSerializableExtra(EXTRA_PROFILE);
        }
        if (this.profile != null) {
            refreshAvator(this.profile.getAvatar(), this.profile.getNickName());
        }
    }

    private void refreshAvator(String str, String str2) {
        this.avatarUrl = str;
        if (!TextUtils.isEmpty(str)) {
            this.ivAvatar.execute(this.avatarUrl, ResHelper.getBitmapRes(this.activity, "smssdk_cp_default_avatar"));
            ((TextView) findViewById(ResHelper.getIdRes(getContext(), "tv_avatar"))).setVisibility(4);
        }
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        ((TextView) findViewById(ResHelper.getIdRes(getContext(), "et_nickname"))).setText(str2);
    }

    private void submitProfile() {
        if (this.profile == null) {
            Toast.makeText(getContext(), ResHelper.getStringRes(getContext(), "smssdk_msg_profile_empty"), 0).show();
            return;
        }
        this.profile.setAvatar(this.avatarUrl);
        this.profile.setNickName(((TextView) findViewById(ResHelper.getIdRes(getContext(), "et_nickname"))).getText().toString());
        if (this.pd != null && this.pd.isShowing()) {
            this.pd.dismiss();
        }
        this.pd = CommonDialog.ProgressDialog(this.activity);
        if (this.pd != null) {
            this.pd.show();
        }
        SMSSDK.submitUserInfo(this.profile.getUid(), this.profile.getNickName(), this.profile.getAvatar(), this.profile.getCountry(), this.profile.getPhoneNum());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == ResHelper.getIdRes(getContext(), "tv_left")) {
            finish();
        } else if (id == ResHelper.getIdRes(getContext(), "iv_avatar")) {
            new AvatarPickerPage().showForResult(this.activity, null, this);
        } else if (id == ResHelper.getIdRes(getContext(), "bt_submit_profile")) {
            submitProfile();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        super.onCreate();
        this.activity.setContentView(ResHelper.getLayoutRes(getContext(), "smssdk_avatar_page"));
        initView();
        EventHandler eventHandler = new EventHandler() { // from class: cn.smssdk.gui.AvatarPage.1
            @Override // cn.smssdk.EventHandler
            public void afterEvent(int i, int i2, Object obj) {
                super.afterEvent(i, i2, obj);
                if (AvatarPage.this.pd != null && AvatarPage.this.pd.isShowing()) {
                    AvatarPage.this.pd.dismiss();
                }
                if (i2 == -1) {
                    if (i == 5) {
                        GUISPDB.setProfile(AvatarPage.this.profile);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("res", true);
                        AvatarPage.this.setResult(hashMap);
                        AvatarPage.this.finish();
                        return;
                    }
                    return;
                }
                if (i == 5) {
                    try {
                        ((Throwable) obj).printStackTrace();
                        JSONObject jSONObject = new JSONObject(((Throwable) obj).getMessage());
                        String optString = jSONObject.optString("detail");
                        jSONObject.optInt("status");
                        if (TextUtils.isEmpty(optString)) {
                            return;
                        }
                        Toast.makeText(AvatarPage.this.activity, optString, 0).show();
                    } catch (Exception e) {
                        SMSLog.getInstance().w(e);
                    }
                }
            }
        };
        this.handler = eventHandler;
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override // com.mob.tools.FakeActivity
    public void onDestroy() {
        SMSSDK.unregisterEventHandler(this.handler);
    }

    @Override // com.mob.tools.FakeActivity
    public void onResult(HashMap<String, Object> hashMap) {
        super.onResult(hashMap);
        refreshAvator(hashMap != null ? String.valueOf(hashMap.get(AvatarPickerPage.INTENT_PICK_URL)) : null, null);
    }

    public void show(Context context) {
        show(context, null);
    }
}
