package com.czw.smartkit.netModule;

import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.databaseModule.step.StepDataTable;
import com.czw.smartkit.databaseModule.train.TrainDataTable;
import com.czw.smartkit.net.Cfg;
import com.czw.smartkit.net.domain.FirmwareDTO;
import com.czw.smartkit.net.domain.UploadImageResult;
import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.netModule.cfg.CommunityCfg;
import com.czw.smartkit.netModule.cfg.DevDataCfg;
import com.czw.smartkit.netModule.cfg.UserCfg;
import com.czw.smartkit.netModule.entity.AppVersionEntity;
import com.czw.smartkit.netModule.entity.LocationEntity;
import com.czw.smartkit.netModule.entity.WeatherEntity;
import com.czw.smartkit.netModule.tmp.measure.DownloadBloodFromNetEntity;
import com.czw.smartkit.netModule.tmp.measure.DownloadHrOxFromNetEntity;
import com.czw.smartkit.netModule.tmp.sleep.DownloadSleepFromNetEntity;
import com.czw.smartkit.netModule.tmp.step.DownLoadStepFromNetEntity;
import com.czw.smartkit.update.AppUpdate;
import com.czw.smartkit.util.GetDeviceId;
import com.czw.smartkit.util.YCAppUtils;
import com.czw.utils.LogUtil;
import com.sun.mail.imap.IMAPStore;
import ycnet.runchinaup.core.abs.IDataParser;
import ycnet.runchinaup.core.abs.IRequest;
import ycnet.runchinaup.core.ycimpl.data.YCResp;
import ycnet.runchinaup.core.ycimpl.data.YCRespData;
import ycnet.runchinaup.core.ycimpl.data.YCRespListData;
import ycnet.runchinaup.core.ycimpl.parser.YCErrCodeParser;
import ycnet.runchinaup.core.ycimpl.parser.YCResponseParser;
import ycnet.runchinaup.core.ycimpl.request.YCReqParaObj;
import ycnet.runchinaup.core.ycimpl.response.YCResponseListener;
import ycnet.runchinaup.log.ycNetLog;
import ycnet.runchinaup.utils.NetworkUtils;

/* loaded from: classes.dex */
public class NetManager extends IRequest implements UserCfg, CommunityCfg, DevDataCfg {
    private static NetManager netManager = new NetManager();
    static final String runchinaUpDomain = "http://www.runchinaup.com/app_upload/index.php/home";
    private YCErrCodeParser ycErrCodeParser;
    private YCNetCodeParserHelper ycNetCodeParserHelper = YCNetCodeParserHelper.getNetCodeParserHelper();

    private NetManager() {
        this.ycErrCodeParser = null;
        this.ycErrCodeParser = new YCErrCodeParser() { // from class: com.czw.smartkit.netModule.NetManager.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // ycnet.runchinaup.core.ycimpl.parser.YCErrCodeParser, ycnet.runchinaup.core.abs.IErrCodeParser
            public void parser(Integer num, String str) {
                ycNetLog.e("" + num);
                NetManager.this.ycNetCodeParserHelper.onErrorCode(num.intValue(), str);
            }
        };
    }

    private void get(String str, YCResponseListener yCResponseListener, Class<?>... clsArr) {
        yCResponseListener.cfgYCErrCodeParser(this.ycErrCodeParser);
        get(str, new YCResponseParser(yCResponseListener, clsArr));
    }

    public static NetManager getNetManager() {
        return netManager;
    }

    private void post(String str, String str2, YCReqParaObj yCReqParaObj, YCResponseListener yCResponseListener, Class<?>... clsArr) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
        }
        if (!TextUtils.isEmpty(str2)) {
            sb.append(str2);
        }
        if (yCReqParaObj != null) {
            sb.append(yCReqParaObj.toString());
        }
        ycNetLog.e(sb.toString());
        yCResponseListener.cfgYCErrCodeParser(this.ycErrCodeParser);
        String str3 = str + str2;
        if (clsArr == null) {
            clsArr = new Class[]{YCResp.class};
        }
        post(str3, (IDataParser) new YCResponseParser(yCResponseListener, clsArr), yCReqParaObj == null ? null : yCReqParaObj.getFormBody());
    }

    private void ycPost(String str, String str2, YCReqParaObj yCReqParaObj, YCResponseListener yCResponseListener, Class<?>... clsArr) {
        if (NetworkUtils.isAvailable(MainApplication.getApp())) {
            post(str, str2, yCReqParaObj, yCResponseListener, clsArr);
        } else {
            LogUtil.e("debug===网络不可用哦");
            NetWorkHelper.getInstance().notifyNetNotAvailable();
        }
    }

    private void ycPost(String str, YCReqParaObj yCReqParaObj, YCResponseListener yCResponseListener, Class<?>... clsArr) {
        if (NetworkUtils.isAvailable(MainApplication.getApp())) {
            post(NetCfg.domainUrl, str, yCReqParaObj, yCResponseListener, clsArr);
        } else {
            LogUtil.e("debug===网络不可用哦");
            NetWorkHelper.getInstance().notifyNetNotAvailable();
        }
    }

    public void addBOBatch(String str, String str2, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("records", str2);
        create.addPara("userId", str);
        ycPost("/body/addBOBatch", create, yCResponseListener, YCRespData.class);
    }

    public void addBPBatch(String str, String str2, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("records", str2);
        create.addPara("userId", str);
        ycPost("/body/addBPBatch", create, yCResponseListener, YCRespData.class);
    }

    public void addHRBatch(String str, String str2, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("records", str2);
        create.addPara("userId", str);
        ycPost("/body/addHRBatch", create, yCResponseListener, YCRespData.class);
    }

    public void addSleepBatch(String str, String str2, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("records", str2);
        create.addPara("userId", str);
        ycPost("/task/addSleepBatch", create, yCResponseListener, YCRespData.class);
    }

    public void addTrainBatch(String str, String str2, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("records", str2);
        create.addPara("userId", str);
        ycPost("/task/addTrainBatch", create, yCResponseListener, YCRespData.class);
    }

    public void addWalkBatch(String str, String str2, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("records", str2);
        create.addPara("userId", str);
        ycPost("/task/addWalkBatch", create, yCResponseListener, YCRespData.class);
    }

    public void deleteUserInfo(String str, YCResponseListener<YCRespData> yCResponseListener) {
        ycPost(Cfg.clearUserData, YCReqParaObj.create("userId", str), yCResponseListener, YCRespData.class);
    }

    public void devAddWalk(StepDataTable stepDataTable, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("startTime", stepDataTable.getStartTime() + "");
        create.addPara("userId", stepDataTable.getUid());
        create.addPara("calorie", stepDataTable.getCalorie() + "");
        create.addPara("goalWalk", stepDataTable.getGoalWalk() + "");
        create.addPara("distance", stepDataTable.getDistance() + "");
        create.addPara("timeConsuming", stepDataTable.getTimeConsuming() + "");
        create.addPara("walkCounts", stepDataTable.getWalkCounts() + "");
        create.addPara("detailJson", stepDataTable.getDetailJson());
        ycPost("/task/addwalk", create, yCResponseListener, YCRespData.class);
    }

    public void devBloodPressureList(String str, String str2, String str3, YCResponseListener<YCRespListData<DownloadBloodFromNetEntity>> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("userId", str);
        create.addPara("startDate", str2);
        create.addPara("endDate", str3);
        ycPost("/record/BPrecord", create, yCResponseListener, YCRespListData.class, DownloadBloodFromNetEntity.class);
    }

    public void devHrList(String str, String str2, String str3, YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("userId", str);
        create.addPara("startDate", str2);
        create.addPara("endDate", str3);
        ycPost("/record/HRrecord", create, yCResponseListener, YCRespListData.class, DownloadHrOxFromNetEntity.class);
    }

    public void devOxList(String str, String str2, String str3, YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("userId", str);
        create.addPara("startDate", str2);
        create.addPara("endDate", str3);
        ycPost("/record/HOrecord", create, yCResponseListener, YCRespListData.class, DownloadHrOxFromNetEntity.class);
    }

    public void devSleepList(String str, String str2, String str3, YCResponseListener<YCRespListData<DownloadSleepFromNetEntity>> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("userId", str);
        create.addPara("startDate", str2);
        create.addPara("endDate", str3);
        ycPost("/record/sleepRecord", create, yCResponseListener, YCRespListData.class, DownloadSleepFromNetEntity.class);
    }

    public void devWalkList(String str, String str2, String str3, YCResponseListener<YCRespListData<DownLoadStepFromNetEntity>> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("userId", str);
        create.addPara("startDate", str2);
        create.addPara("endDate", str3);
        ycPost("/record/walkRecord", create, yCResponseListener, YCRespListData.class, DownLoadStepFromNetEntity.class);
    }

    public void fastRegister(YCResponseListener<YCRespData<UserDTO>> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("registerId", GetDeviceId.getDeviceId(MainApplication.getApp()).toUpperCase());
        create.addPara("nickname", "Smart King");
        ycPost("/user/idLogin", create, yCResponseListener, YCRespData.class, UserDTO.class);
    }

    public void getEmailCode(String str, String str2, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create(NotificationCompat.CATEGORY_EMAIL, str);
        create.addPara("type", str2);
        create.addPara("lang", YCAppUtils.isChainess() ? AmapLoc.RESULT_TYPE_WIFI_ONLY : AmapLoc.RESULT_TYPE_GPS);
        ycPost(Cfg.emailCodeUrl, create, yCResponseListener, YCRespData.class);
    }

    public void getFirmware(String str, YCResponseListener<YCRespListData<FirmwareDTO>> yCResponseListener) {
        ycPost(runchinaUpDomain, "/index/getfirmware", YCReqParaObj.create(IMAPStore.ID_NAME, str), yCResponseListener, YCRespListData.class, FirmwareDTO.class);
    }

    public void getLastAppVersion(YCResponseListener<YCRespListData<AppVersionEntity>> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create(IMAPStore.ID_NAME, "smartking");
        if (!AppUpdate.isGoogleRom(MainApplication.getApp())) {
            create.addPara("isChinaVersion", "true");
        }
        ycPost(Cfg.firmareUrl, "", create, yCResponseListener, YCRespListData.class, AppVersionEntity.class);
    }

    public void getWeather(LocationEntity locationEntity, YCResponseListener<YCRespData<WeatherEntity>> yCResponseListener) {
        ycPost("/system/weather", YCReqParaObj.create("localtion", JSON.toJSONString(locationEntity)), yCResponseListener, YCRespData.class, WeatherEntity.class);
    }

    public void login(String str, String str2, String str3, YCResponseListener<YCRespListData<UserDTO>> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create(NotificationCompat.CATEGORY_EMAIL, str);
        create.addPara("zone_code", str3);
        create.addPara("loginpwd", str2);
        ycPost("/user/login", create, yCResponseListener, YCRespListData.class, UserDTO.class);
    }

    public void registerByEmail(String str, String str2, String str3, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create(NotificationCompat.CATEGORY_EMAIL, str);
        create.addPara("emailCode", str2);
        create.addPara("loginpwd", str3);
        ycPost(Cfg.emailRegisterUrl, create, yCResponseListener, YCRespData.class);
    }

    public void registerPhone(String str, String str2, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("phone", str);
        create.addPara("loginpwd", str2);
        ycPost(Cfg.phoneRegisterUrl, create, yCResponseListener, YCRespData.class);
    }

    public void resetPwdByEemail(String str, String str2, String str3, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create(NotificationCompat.CATEGORY_EMAIL, str);
        create.addPara("code", str2);
        create.addPara("loginpwd", str3);
        ycPost(Cfg.emailResetPwdUrl, create, yCResponseListener, YCRespData.class);
    }

    public void resetPwdByPhone(String str, String str2, YCResponseListener<YCRespData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("phone", str);
        create.addPara("loginpwd", str2);
        ycPost(Cfg.phoneResetPwdUrl, create, yCResponseListener, YCRespData.class);
    }

    public void trainList(String str, String str2, String str3, YCResponseListener<YCRespListData<TrainDataTable>> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("userId", str);
        create.addPara("startDate", str2);
        create.addPara("endDate", str3);
        ycPost("/record/sportRecord", create, yCResponseListener, YCRespListData.class, TrainDataTable.class);
    }

    public void updateUserInfo(UserDTO userDTO, YCResponseListener<YCRespListData> yCResponseListener) {
        YCReqParaObj create = YCReqParaObj.create("userId", userDTO.getUserId());
        create.addPara("walkGoal", userDTO.getWalkGoal());
        create.addPara("nickname", userDTO.getNickname());
        create.addPara("sex", userDTO.getSex());
        create.addPara("photo", userDTO.getPhoto());
        create.addPara("age", userDTO.getAge());
        create.addPara("height", userDTO.getHeight());
        create.addPara("weight", userDTO.getWeight());
        create.addPara("stepWidth", userDTO.getStepWidth());
        ycPost(Cfg.updateUserInfo, create, yCResponseListener, YCRespListData.class);
    }

    public void uploadUserHeader(String str, YCResponseListener<YCRespData<UploadImageResult>> yCResponseListener) {
        ycPost(Cfg.uplaodHeader, YCReqParaObj.create(AmapLoc.TYPE_OFFLINE_CELL, str), yCResponseListener, YCRespData.class, UploadImageResult.class);
    }
}
