package cn.smssdk.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.smssdk.gui.entity.Profile;
import cn.smssdk.gui.util.GUISPDB;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class PersonalInfoView {
    private Context context;
    private AsyncImageView ivAvatar;
    private LinearLayout llPhone;
    private TextView tvBind;
    private TextView tvNickname;
    private TextView tvPhone;
    private View view;

    public PersonalInfoView(Context context) {
        this.context = context;
    }

    public View create() {
        this.view = LayoutInflater.from(this.context).inflate(ResHelper.getLayoutRes(this.context, "smssdk_personal_info"), (ViewGroup) null);
        this.view.setVisibility(8);
        Profile profile = GUISPDB.getProfile();
        if (profile != null) {
            AsyncImageView asyncImageView = (AsyncImageView) this.view.findViewById(ResHelper.getIdRes(this.context, "iv_avatar"));
            asyncImageView.setRound(ResHelper.dipToPx(this.context, 30));
            asyncImageView.execute(profile.getAvatar(), ResHelper.getBitmapRes(this.context, "smssdk_cp_default_avatar"));
            ((TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "tv_nickname"))).setText(profile.getNickName());
            this.view.findViewById(ResHelper.getIdRes(this.context, "ll_phone_container")).setVisibility(0);
            ((TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "tv_profile_phone"))).setText(profile.getPhoneNum());
            ((TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "tv_profile_rebind"))).setText(ResHelper.getStringRes(this.context, "smssdk_rebind_profile"));
        }
        return this.view;
    }

    public void updateUI(Profile profile) {
        if (this.view == null || this.context == null || profile == null) {
            return;
        }
        AsyncImageView asyncImageView = (AsyncImageView) this.view.findViewById(ResHelper.getIdRes(this.context, "iv_avatar"));
        asyncImageView.setRound(ResHelper.dipToPx(this.context, 30));
        if (profile == null) {
            asyncImageView.execute((String) null, ResHelper.getBitmapRes(this.context, "smssdk_cp_default_avatar"));
            ((TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "tv_nickname"))).setText(ResHelper.getStringRes(this.context, "smssdk_my_profile"));
            this.view.findViewById(ResHelper.getIdRes(this.context, "ll_phone_container")).setVisibility(8);
            ((TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "tv_profile_rebind"))).setText(ResHelper.getStringRes(this.context, "smssdk_bind_profile"));
            return;
        }
        asyncImageView.execute(profile.getAvatar(), ResHelper.getBitmapRes(this.context, "smssdk_cp_default_avatar"));
        ((TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "tv_nickname"))).setText(profile.getNickName());
        this.view.findViewById(ResHelper.getIdRes(this.context, "ll_phone_container")).setVisibility(0);
        ((TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "tv_profile_phone"))).setText(profile.getPhoneNum());
        ((TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "tv_profile_rebind"))).setText(ResHelper.getStringRes(this.context, "smssdk_rebind_profile"));
    }
}
