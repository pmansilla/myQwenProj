package com.czw.smartkit.net;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import com.amap.location.common.model.AmapLoc;
import com.czw.modes.net.OKHttpUtil;
import com.czw.net.AbsNetImpl;
import com.czw.net.NetRespListener;
import com.czw.net.NetResult;
import com.czw.smartkit.databaseModule.train.TrainDataTable;
import com.czw.smartkit.mob.thridlogin.ThridUserInfo;
import com.czw.smartkit.net.domain.FirmwareDTO;
import com.czw.smartkit.net.domain.UploadImageResult;
import com.czw.smartkit.net.domain.UserDTO;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class NetImpl extends AbsNetImpl implements Cfg {
    private static final NetImpl impl = new NetImpl();
    private FragmentActivity baseActivity;

    private NetImpl() {
    }

    public static void deleteUserData(String str, NetRespListener<NetResult> netRespListener) {
        sendPostWithOutProTip(Cfg.clearUserData, netRespListener, null, new OKHttpUtil.Param("userId", str));
    }

    public static <D> void getLastCheckData(String str, String str2, Class<D> cls, NetRespListener<NetResult<D>> netRespListener) {
        sendPostWithOutProTip(Cfg.getLastCheckInfo, netRespListener, cls, new OKHttpUtil.Param("userId", str), new OKHttpUtil.Param("type", str2));
    }

    public static NetImpl getNetImpl() {
        return impl;
    }

    private static <D> void sendPostWithOutProTip(String str, NetRespListener netRespListener, Class<D> cls, OKHttpUtil.Param... paramArr) {
        OKHttpUtil.getInstance().asyncPostRequest(Cfg.URL + str, packCallback(netRespListener, cls, null, null), paramArr);
    }

    public void getEmailCode(String str, String str2, NetRespListener netRespListener) {
        post(Cfg.emailCodeUrl, netRespListener, null, new OKHttpUtil.Param(NotificationCompat.CATEGORY_EMAIL, str), new OKHttpUtil.Param("lang", AmapLoc.RESULT_TYPE_GPS), new OKHttpUtil.Param("type", str2));
    }

    public void getFirmware(String str, NetRespListener<NetResult<List<FirmwareDTO>>> netRespListener) {
        postWithOutUrlPre(Cfg.firmareUrl, netRespListener, FirmwareDTO.class, new OKHttpUtil.Param(IMAPStore.ID_NAME, str));
    }

    public NetImpl loadActivity(FragmentActivity fragmentActivity) {
        this.baseActivity = fragmentActivity;
        return this;
    }

    @Override // com.czw.net.AbsNetImpl
    protected String loadUrlDomain() {
        return Cfg.URL;
    }

    public void login(String str, String str2, String str3, NetRespListener<NetResult<List<UserDTO>>> netRespListener) {
        post("/user/login", netRespListener, UserDTO.class, new OKHttpUtil.Param(NotificationCompat.CATEGORY_EMAIL, str), new OKHttpUtil.Param("zone_code", str3), new OKHttpUtil.Param("loginpwd", str2));
    }

    @Override // com.czw.net.AbsNetImpl
    protected <D> void post(String str, NetRespListener netRespListener, Class<D> cls, OKHttpUtil.Param... paramArr) {
        QMUITipDialog qMUITipDialog;
        if (this.baseActivity != null) {
            qMUITipDialog = new QMUITipDialog.Builder(this.baseActivity).setIconType(1).setTipWord("").create();
            qMUITipDialog.setCancelable(false);
            qMUITipDialog.setCanceledOnTouchOutside(false);
            qMUITipDialog.show();
        } else {
            qMUITipDialog = null;
        }
        OKHttpUtil.getInstance().asyncPostRequest(loadUrlDomain() + str, packCallback(netRespListener, cls, this.baseActivity, qMUITipDialog), paramArr);
    }

    protected <D> void postWithOutUrlPre(String str, NetRespListener netRespListener, Class<D> cls, OKHttpUtil.Param... paramArr) {
        QMUITipDialog qMUITipDialog;
        if (this.baseActivity != null) {
            qMUITipDialog = new QMUITipDialog.Builder(this.baseActivity).setIconType(1).setTipWord("").create();
            qMUITipDialog.setCancelable(false);
            qMUITipDialog.setCanceledOnTouchOutside(false);
            qMUITipDialog.show();
        } else {
            qMUITipDialog = null;
        }
        OKHttpUtil.getInstance().asyncPostRequest(str, packCallback(netRespListener, cls, this.baseActivity, qMUITipDialog), paramArr);
    }

    public void queryOneKey(String str, String str2, NetRespListener netRespListener) {
        sendPostWithOutProTip(Cfg.historyOneKey, netRespListener, null, new OKHttpUtil.Param("userId", str), new OKHttpUtil.Param("datetime", str2));
    }

    public void queryTrans(String str, String str2, String str3, NetRespListener<NetResult<List<TrainDataTable>>> netRespListener) {
        sendPostWithOutProTip(Cfg.queryTrans, netRespListener, TrainDataTable.class, new OKHttpUtil.Param("userId", str), new OKHttpUtil.Param("types", str2), new OKHttpUtil.Param("dates", str3));
    }

    public void registerByemaile(String str, String str2, String str3, NetRespListener<NetResult> netRespListener) {
        post(Cfg.emailRegisterUrl, netRespListener, null, new OKHttpUtil.Param(NotificationCompat.CATEGORY_EMAIL, str), new OKHttpUtil.Param("emailCode", str2), new OKHttpUtil.Param("loginpwd", str3));
    }

    public void registerPhone(String str, String str2, NetRespListener netRespListener) {
        post(Cfg.phoneRegisterUrl, netRespListener, null, new OKHttpUtil.Param("phone", str), new OKHttpUtil.Param("loginpwd", str2));
    }

    public void resetPwdByEemail(String str, String str2, String str3, NetRespListener netRespListener) {
        post(Cfg.emailResetPwdUrl, netRespListener, null, new OKHttpUtil.Param(NotificationCompat.CATEGORY_EMAIL, str), new OKHttpUtil.Param("code", str2), new OKHttpUtil.Param("loginpwd", str3));
    }

    public void resetPwdByPhone(String str, String str2, NetRespListener netRespListener) {
        post(Cfg.phoneResetPwdUrl, netRespListener, null, new OKHttpUtil.Param("phone", str), new OKHttpUtil.Param("loginpwd", str2));
    }

    public void thridLogin(ThridUserInfo thridUserInfo, NetRespListener<List<UserDTO>> netRespListener) {
        post("/user/login", netRespListener, UserDTO.class, new OKHttpUtil.Param("fromType", ""), new OKHttpUtil.Param("openId", ""), new OKHttpUtil.Param("accessToken", ""), new OKHttpUtil.Param("nickname", ""), new OKHttpUtil.Param("age", "25"), new OKHttpUtil.Param("thirdPhoto", thridUserInfo.getUserIcon()));
    }

    void updateUserInfo(UserDTO userDTO, NetRespListener netRespListener) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new OKHttpUtil.Param("userId", userDTO.getUserId()));
        arrayList.add(new OKHttpUtil.Param("walkGoal", userDTO.getWalkGoal()));
        arrayList.add(new OKHttpUtil.Param("nickname", userDTO.getNickname()));
        arrayList.add(new OKHttpUtil.Param("sex", userDTO.getSex()));
        arrayList.add(new OKHttpUtil.Param("photo", userDTO.getPhoto()));
        arrayList.add(new OKHttpUtil.Param("age", userDTO.getAge()));
        arrayList.add(new OKHttpUtil.Param("height", userDTO.getHeight()));
        arrayList.add(new OKHttpUtil.Param("weight", userDTO.getWeight()));
        arrayList.add(new OKHttpUtil.Param("stepWidth", userDTO.getStepWidth()));
        sendPostWithOutProTip(Cfg.updateUserInfo, netRespListener, null, (OKHttpUtil.Param[]) arrayList.toArray(new OKHttpUtil.Param[arrayList.size()]));
    }

    public void uploadTrans(TrainDataTable trainDataTable, NetRespListener netRespListener) {
        sendPostWithOutProTip(Cfg.uploadTrans, netRespListener, null, new OKHttpUtil.Param("userId", trainDataTable.getUid()), new OKHttpUtil.Param("startTime", trainDataTable.getStartTime()), new OKHttpUtil.Param("endTime", trainDataTable.getEndTime()), new OKHttpUtil.Param("timeConsuming", trainDataTable.getTimeConsuming()), new OKHttpUtil.Param("strength", trainDataTable.getStrength()), new OKHttpUtil.Param("calorie", trainDataTable.getCalorie()), new OKHttpUtil.Param("fasterRate", trainDataTable.getFasterRate()), new OKHttpUtil.Param("dataDetail", AmapLoc.RESULT_TYPE_GPS), new OKHttpUtil.Param("locationDetail", trainDataTable.getLocationDetail()));
    }

    public void uploadUserHeader(String str, NetRespListener<NetResult<UploadImageResult>> netRespListener) {
        sendPostWithOutProTip(Cfg.uplaodHeader, netRespListener, UploadImageResult.class, new OKHttpUtil.Param(AmapLoc.TYPE_OFFLINE_CELL, str));
    }
}
