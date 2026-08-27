package com.czw.smartkit.user;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.czw.modes.widget.TitleBar;
import com.czw.net.NetRespListener;
import com.czw.net.NetResult;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.net.domain.UploadImageResult;
import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.netModule.NetManager;
import com.czw.smartkit.observerModule.TargetChangeHelper;
import com.czw.smartkit.preferenceModule.SharePreferenceUser;
import com.czw.smartkit.util.ImageUtil;
import com.czw.smartkit.util.SkUtils;
import com.czw.smartkit.views.popw.SingleScrollerPop;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import java.util.List;
import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.process.CircleImageProcessor;
import me.panpf.sketch.process.ImageProcessor;
import ycnet.runchinaup.core.ycimpl.data.YCRespListData;
import ycnet.runchinaup.core.ycimpl.response.YCResponseListener;

/* loaded from: classes.dex */
public class UserInfoActivity extends TitleActivity {
    private static int minHeight = 100;
    private static int minStep = 20;
    private static int minWeight = 36;
    private TitleBar titleBar;
    private TextView tv_setp_len;
    private TextView tv_userName;
    private TextView tv_user_Sex;
    private TextView tv_user_age;
    private TextView tv_user_height;
    private TextView tv_user_target;
    private TextView tv_user_weight;
    UserDTO user = null;
    private SketchImageView userHeader;

    public static String getFormatCmFtTextWithSelect(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("<font color='#000000'>" + str + "</font> ");
        sb.append("<font color='#999999'>" + MainApplication.getApp().getString(i) + "</font>");
        if (!SkUtils.isChinaUnit()) {
            sb.append("<font color='#000000'> (" + String.format("%.2f", Double.valueOf(Double.valueOf(str).doubleValue() * 0.0328084d)) + "</font> ");
            sb.append("<font color='#999999'> ft</font>");
            sb.append("<font color='#000000'>)</font>");
        }
        return sb.toString();
    }

    public static String getFormatCmFtTextWithShow(String str, int i) {
        StringBuilder sb = new StringBuilder();
        if (SkUtils.isChinaUnit()) {
            sb.append("<font color='#000000'>" + str + "</font> ");
            sb.append("<font color='#999999'>" + MainApplication.getApp().getString(i) + "</font>");
        } else {
            sb.append("<font color='#000000'> " + String.format("%.2f", Double.valueOf(Double.valueOf(str).doubleValue() * 0.0328084d)) + "</font> ");
            sb.append("<font color='#999999'> ft</font>");
        }
        return sb.toString();
    }

    public static String getFormatKgLbTextWithSelect(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("<font color='#000000'>" + str + "</font> ");
        sb.append("<font color='#999999'>" + MainApplication.getApp().getString(i) + "</font>");
        if (!SkUtils.isChinaUnit()) {
            sb.append("<font color='#000000'> (" + String.format("%.2f", Double.valueOf(Double.valueOf(str).doubleValue() * 2.20462264d)) + "</font> ");
            sb.append("<font color='#999999'> lb</font>");
            sb.append("<font color='#000000'>)</font>");
        }
        return sb.toString();
    }

    public static String getFormatKgLbTextWithShow(String str, int i) {
        StringBuilder sb = new StringBuilder();
        if (SkUtils.isChinaUnit()) {
            sb.append("<font color='#000000'>" + str + "</font> ");
            sb.append("<font color='#999999'>" + MainApplication.getApp().getString(i) + "</font>");
        } else {
            sb.append("<font color='#000000'>" + String.format("%.2f", Double.valueOf(Double.valueOf(str).doubleValue() * 2.20462264d)) + "</font> ");
            sb.append("<font color='#999999'> lb</font>");
        }
        return sb.toString();
    }

    public static ArrayList<String> loadAges() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 1; i < 120; i++) {
            arrayList.add(i + "");
        }
        return arrayList;
    }

    public static ArrayList<String> loadHeight() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = minHeight; i < 250; i++) {
            arrayList.add(getFormatCmFtTextWithSelect(i + "", R.string.unit_cm));
        }
        return arrayList;
    }

    public static ArrayList<String> loadSexs(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(context.getString(R.string.man));
        arrayList.add(context.getString(R.string.woman));
        return arrayList;
    }

    public static ArrayList<String> loadStepLen() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = minStep; i < 100; i++) {
            arrayList.add(getFormatCmFtTextWithSelect(i + "", R.string.unit_cm));
        }
        return arrayList;
    }

    public static ArrayList<String> loadTraget() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 1000; i < 30000; i += 1000) {
            arrayList.add(i + "");
        }
        return arrayList;
    }

    public static ArrayList<String> loadWeight() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = minWeight; i < 150; i++) {
            arrayList.add(getFormatKgLbTextWithSelect(i + "", R.string.unit_kg));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUserInfo(final UserDTO userDTO, final boolean z) {
        showLoadingDialog("");
        NetManager.getNetManager().updateUserInfo(userDTO, new YCResponseListener<YCRespListData>() { // from class: com.czw.smartkit.user.UserInfoActivity.12
            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(YCRespListData yCRespListData) {
                UserInfoActivity.this.dismissLoadingDialog();
                UserInfoActivity.this.updateUserShow(userDTO);
                SharePreferenceUser.save(userDTO);
                if (z) {
                    TargetChangeHelper.getInstance().notifyTargetChange(userDTO.getWalkGoal());
                }
            }
        });
    }

    public void click(View view) {
        toast("click");
    }

    void event() {
        $View(R.id.userHeader).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UserInfoActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UserInfoActivity.this.jump2Gallery();
            }
        });
        $View(R.id.userName).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UserInfoActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UserInfoActivity.this.startActivityForResult(new Intent(UserInfoActivity.this.getUI(), (Class<?>) SetNickNameActivity.class).putExtra("userName", UserInfoActivity.this.user.getNickname()), 100);
            }
        });
        $View(R.id.user_sex).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UserInfoActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SingleScrollerPop.getPop(UserInfoActivity.this.getUI()).showPicker(UserInfoActivity.this.$View(R.id.acv_win), UserInfoActivity.loadSexs(UserInfoActivity.this.getUI()), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.user.UserInfoActivity.5.1
                    @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
                    public void onSelect(int i) {
                        UserInfoActivity.this.user.setSex((i + 1) + "");
                        UserInfoActivity.this.updateUserInfo(UserInfoActivity.this.user, false);
                    }
                }).showTitleWithValue(R.string.user_sex, UserInfoActivity.this.getString(UserInfoActivity.this.user.getSex().equals(AmapLoc.RESULT_TYPE_WIFI_ONLY) ? R.string.man : R.string.woman));
            }
        });
        $View(R.id.user_age).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UserInfoActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SingleScrollerPop.getPop(UserInfoActivity.this.getUI()).showPicker(UserInfoActivity.this.$View(R.id.acv_win), UserInfoActivity.loadAges(), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.user.UserInfoActivity.6.1
                    @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
                    public void onSelect(int i) {
                        UserInfoActivity.this.user.setAge(UserInfoActivity.loadAges().get(i));
                        UserInfoActivity.this.updateUserInfo(UserInfoActivity.this.user, false);
                    }
                }).showTitleWithValue(R.string.user_age, UserInfoActivity.this.user.getAge());
            }
        });
        $View(R.id.user_height).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UserInfoActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SingleScrollerPop.getPop(UserInfoActivity.this.getUI()).showPicker(UserInfoActivity.this.$View(R.id.acv_win), UserInfoActivity.loadHeight(), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.user.UserInfoActivity.7.1
                    @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
                    public void onSelect(int i) {
                        UserInfoActivity.this.user.setHeight((i + UserInfoActivity.minHeight) + "");
                        UserInfoActivity.this.updateUserInfo(UserInfoActivity.this.user, false);
                    }
                }).showTitleWithValue(R.string.user_height, UserInfoActivity.getFormatCmFtTextWithSelect(UserInfoActivity.this.user.getHeight(), R.string.unit_cm));
            }
        });
        $View(R.id.user_weight).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UserInfoActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SingleScrollerPop.getPop(UserInfoActivity.this.getUI()).showPicker(UserInfoActivity.this.$View(R.id.acv_win), UserInfoActivity.loadWeight(), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.user.UserInfoActivity.8.1
                    @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
                    public void onSelect(int i) {
                        UserInfoActivity.this.user.setWeight((UserInfoActivity.minWeight + i) + "");
                        UserInfoActivity.this.updateUserInfo(UserInfoActivity.this.user, false);
                    }
                }).showTitleWithValue(R.string.user_weight, UserInfoActivity.getFormatKgLbTextWithSelect(UserInfoActivity.this.user.getWeight(), R.string.unit_kg));
            }
        });
        $View(R.id.step_target).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UserInfoActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SingleScrollerPop.getPop(UserInfoActivity.this.getUI()).showPicker(UserInfoActivity.this.$View(R.id.acv_win), UserInfoActivity.loadTraget(), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.user.UserInfoActivity.9.1
                    @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
                    public void onSelect(int i) {
                        UserInfoActivity.this.user.setWalkGoal(UserInfoActivity.loadTraget().get(i));
                        UserInfoActivity.this.updateUserInfo(UserInfoActivity.this.user, true);
                    }
                }).showTitleWithValue(R.string.user_target_step, UserInfoActivity.this.user.getWalkGoal());
            }
        });
        $View(R.id.step_len).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UserInfoActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SingleScrollerPop.getPop(UserInfoActivity.this.getUI()).showPicker(UserInfoActivity.this.$View(R.id.acv_win), UserInfoActivity.loadStepLen(), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.user.UserInfoActivity.10.1
                    @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
                    public void onSelect(int i) {
                        UserInfoActivity.this.user.setStepWidth((UserInfoActivity.minStep + i) + "");
                        UserInfoActivity.this.updateUserInfo(UserInfoActivity.this.user, false);
                    }
                }).showTitleWithValue(R.string.user_step_length, UserInfoActivity.getFormatCmFtTextWithSelect(UserInfoActivity.this.user.getStepWidth(), R.string.unit_cm));
            }
        });
    }

    public String getFormatText(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("<font color='#000000'>" + str + "</font> ");
        sb.append("<font color='#999999'>" + getString(i) + "</font>");
        return sb.toString();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar = (TitleBar) $View(R.id.titleBar);
        this.titleBar.setTitleColor(getResources().getColor(R.color.white));
        this.titleBar.setParentBg(R.color.root_bg_color);
        this.titleBar.setLeftImage(R.mipmap.ic_back);
        this.titleBar.setTitle(R.string.user_setting_title);
        this.titleBar.setClick(new TitleBar.TitleClick() { // from class: com.czw.smartkit.user.UserInfoActivity.1
            @Override // com.czw.modes.widget.TitleBar.LeftClick
            public void onLeftClick(View view) {
                UserInfoActivity.this.finish();
            }

            @Override // com.czw.modes.widget.TitleBar.TitleClick
            public void onRightClick(View view) {
            }
        });
        this.userHeader = (SketchImageView) $View(R.id.userHeader);
        this.userHeader.getOptions().setProcessor((ImageProcessor) CircleImageProcessor.getInstance());
        this.tv_userName = (TextView) $View(R.id.tv_userName);
        this.tv_user_Sex = (TextView) $View(R.id.tv_user_Sex);
        this.tv_user_age = (TextView) $View(R.id.tv_user_age);
        this.tv_user_height = (TextView) $View(R.id.tv_user_height);
        this.tv_user_weight = (TextView) $View(R.id.tv_user_weight);
        this.tv_user_target = (TextView) $View(R.id.tv_user_target);
        this.tv_setp_len = (TextView) $View(R.id.tv_setp_len);
        this.user = SharePreferenceUser.read();
        updateUserShow(this.user);
        event();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_user_info;
    }

    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 200) {
            this.user.setNickname(intent.getStringExtra("userName"));
            updateUserInfo(this.user, false);
        }
    }

    @Override // com.czw.smartkit.base.BaseActivity
    protected void onImageSelect(List<LocalMedia> list) {
        this.userHeader.displayImage(list.get(0).getPath());
        getNet().uploadUserHeader(ImageUtil.bitmapToString(list.get(0).getCutPath()), new NetRespListener<NetResult<UploadImageResult>>() { // from class: com.czw.smartkit.user.UserInfoActivity.11
            @Override // com.czw.net.NetRespListener
            public void onResponse(final NetResult<UploadImageResult> netResult) {
                UserInfoActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.UserInfoActivity.11.1
                    @Override // java.lang.Runnable
                    public void run() {
                        UserInfoActivity.this.user.setPhoto(((UploadImageResult) netResult.getData()).getPath());
                        UserInfoActivity.this.updateUserInfo(UserInfoActivity.this.user, false);
                    }
                });
            }
        });
    }

    public void updateUserShow(final UserDTO userDTO) {
        if (userDTO == null) {
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.UserInfoActivity.2
            @Override // java.lang.Runnable
            public void run() {
                UserInfoActivity.this.userHeader.displayImage(userDTO.getPhoto());
                UserInfoActivity.this.tv_userName.setText(userDTO.getNickname());
                UserInfoActivity.this.tv_user_Sex.setText(userDTO.getSex().equals(AmapLoc.RESULT_TYPE_WIFI_ONLY) ? R.string.man : R.string.woman);
                UserInfoActivity.this.tv_user_age.setText(Html.fromHtml(UserInfoActivity.this.getFormatText(userDTO.getAge(), R.string.unit_age)));
                UserInfoActivity.this.tv_user_height.setText(Html.fromHtml(UserInfoActivity.getFormatCmFtTextWithShow(userDTO.getHeight(), R.string.unit_cm)));
                UserInfoActivity.this.tv_user_weight.setText(Html.fromHtml(UserInfoActivity.getFormatKgLbTextWithShow(userDTO.getWeight(), R.string.unit_kg)));
                UserInfoActivity.this.tv_user_target.setText(Html.fromHtml(UserInfoActivity.this.getFormatText(userDTO.getWalkGoal(), R.string.unit_step)));
                UserInfoActivity.this.tv_setp_len.setText(Html.fromHtml(UserInfoActivity.getFormatCmFtTextWithShow(userDTO.getStepWidth(), R.string.unit_cm)));
            }
        });
    }
}
