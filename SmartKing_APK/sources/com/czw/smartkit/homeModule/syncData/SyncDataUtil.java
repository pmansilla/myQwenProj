package com.czw.smartkit.homeModule.syncData;

import com.czw.smartkit.databaseModule.blood.BloodDataTable;
import com.czw.smartkit.databaseModule.blood.BloodServiceImpl;
import com.czw.smartkit.databaseModule.hr.HrDataTable;
import com.czw.smartkit.databaseModule.hr.HrServiceImpl;
import com.czw.smartkit.databaseModule.ox.OxDataTable;
import com.czw.smartkit.databaseModule.ox.OxServiceImpl;
import com.czw.smartkit.databaseModule.sleep.SleepDataTable;
import com.czw.smartkit.databaseModule.sleep.SleepServiceImpl;
import com.czw.smartkit.databaseModule.step.StepDataTable;
import com.czw.smartkit.databaseModule.step.StepServiceImpl;
import com.czw.smartkit.databaseModule.train.TrainDataTable;
import com.czw.smartkit.databaseModule.train.TrainServiceImpl;
import com.czw.smartkit.netModule.NetManager;
import com.czw.smartkit.netModule.tmp.measure.DownloadBloodFromNetEntity;
import com.czw.smartkit.netModule.tmp.measure.DownloadHrOxFromNetEntity;
import com.czw.smartkit.netModule.tmp.sleep.DownloadSleepFromNetEntity;
import com.czw.smartkit.netModule.tmp.step.DownLoadStepFromNetEntity;
import com.czw.smartkit.user.UserUtil;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import ycnet.runchinaup.core.ycimpl.data.YCRespData;
import ycnet.runchinaup.core.ycimpl.data.YCRespListData;
import ycnet.runchinaup.core.ycimpl.response.YCResponseListener;

/* loaded from: classes.dex */
public class SyncDataUtil {
    public HashSet<SyncDataCallback> syncDataCallbackHashSet = new HashSet<>();
    private static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(20);
    private static StepServiceImpl stepService = StepServiceImpl.getInstance();
    private static SleepServiceImpl sleepService = SleepServiceImpl.getInstance();
    private static HrServiceImpl hrService = HrServiceImpl.getInstance();
    private static OxServiceImpl oxService = OxServiceImpl.getInstance();
    private static BloodServiceImpl bloodService = BloodServiceImpl.getInstance();
    private static TrainServiceImpl trainService = TrainServiceImpl.getInstance();
    private static Gson gson = new Gson();
    private static String yyyyMMddFormatString = "yyyy-MM-dd";
    private static SyncDataUtil instance = new SyncDataUtil();

    /* loaded from: classes.dex */
    public interface SyncDataCallback {
        void onDataSyncFinish(SyncDataType syncDataType);
    }

    private SyncDataUtil() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadBloodData() {
        NetManager netManager;
        YCResponseListener<YCRespListData<DownloadBloodFromNetEntity>> yCResponseListener;
        String uid = UserUtil.getUid();
        String format = DateFormatUtils.format(System.currentTimeMillis(), yyyyMMddFormatString);
        String lastDataDate = bloodService.getLastDataDate(uid);
        try {
            try {
                if (DateUtils.parseDate(lastDataDate, new String[]{yyyyMMddFormatString}).getTime() > DateUtils.parseDate(format, new String[]{yyyyMMddFormatString}).getTime()) {
                    lastDataDate = format;
                }
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownloadBloodFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.16
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.BLOOD);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData<DownloadBloodFromNetEntity> yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.BLOOD);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.16.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.bloodService.saveData(DownloadBloodFromNetEntity.getDevDayBloodEntity((List<DownloadBloodFromNetEntity>) yCRespListData.getData()));
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.BLOOD);
                                }
                            });
                        }
                    }
                };
            } catch (ParseException e) {
                e.printStackTrace();
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownloadBloodFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.16
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.BLOOD);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.BLOOD);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.16.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.bloodService.saveData(DownloadBloodFromNetEntity.getDevDayBloodEntity((List<DownloadBloodFromNetEntity>) yCRespListData.getData()));
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.BLOOD);
                                }
                            });
                        }
                    }
                };
            }
            netManager.devBloodPressureList(uid, lastDataDate, format, yCResponseListener);
        } catch (Throwable th) {
            NetManager.getNetManager().devBloodPressureList(uid, lastDataDate, format, new YCResponseListener<YCRespListData<DownloadBloodFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.16
                @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                public void onError(int i, String str) {
                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.BLOOD);
                }

                @Override // ycnet.runchinaup.core.abs.IResponseListener
                public void onSuccess(final YCRespListData yCRespListData) {
                    if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.BLOOD);
                    } else {
                        SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.16.1
                            @Override // java.lang.Runnable
                            public void run() {
                                SyncDataUtil.bloodService.saveData(DownloadBloodFromNetEntity.getDevDayBloodEntity((List<DownloadBloodFromNetEntity>) yCRespListData.getData()));
                                SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.BLOOD);
                            }
                        });
                    }
                }
            });
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadHrData() {
        NetManager netManager;
        YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>> yCResponseListener;
        String uid = UserUtil.getUid();
        String format = DateFormatUtils.format(System.currentTimeMillis(), yyyyMMddFormatString);
        String lastDataDate = hrService.getLastDataDate(uid);
        try {
            try {
                if (DateUtils.parseDate(lastDataDate, new String[]{yyyyMMddFormatString}).getTime() > DateUtils.parseDate(format, new String[]{yyyyMMddFormatString}).getTime()) {
                    lastDataDate = format;
                }
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.12
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData<DownloadHrOxFromNetEntity> yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.12.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.hrService.saveData(DownloadHrOxFromNetEntity.getHrEntity((List<DownloadHrOxFromNetEntity>) yCRespListData.getData()));
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                                }
                            });
                        }
                    }
                };
            } catch (ParseException e) {
                e.printStackTrace();
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.12
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.12.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.hrService.saveData(DownloadHrOxFromNetEntity.getHrEntity((List<DownloadHrOxFromNetEntity>) yCRespListData.getData()));
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                                }
                            });
                        }
                    }
                };
            }
            netManager.devHrList(uid, lastDataDate, format, yCResponseListener);
        } catch (Throwable th) {
            NetManager.getNetManager().devHrList(uid, lastDataDate, format, new YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.12
                @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                public void onError(int i, String str) {
                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                }

                @Override // ycnet.runchinaup.core.abs.IResponseListener
                public void onSuccess(final YCRespListData yCRespListData) {
                    if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                    } else {
                        SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.12.1
                            @Override // java.lang.Runnable
                            public void run() {
                                SyncDataUtil.hrService.saveData(DownloadHrOxFromNetEntity.getHrEntity((List<DownloadHrOxFromNetEntity>) yCRespListData.getData()));
                                SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                            }
                        });
                    }
                }
            });
            throw th;
        }
    }

    private void downloadOxData() {
        NetManager netManager;
        YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>> yCResponseListener;
        String uid = UserUtil.getUid();
        String format = DateFormatUtils.format(System.currentTimeMillis(), yyyyMMddFormatString);
        String lastDataDate = oxService.getLastDataDate(uid);
        try {
            try {
                if (DateUtils.parseDate(lastDataDate, new String[]{yyyyMMddFormatString}).getTime() > DateUtils.parseDate(format, new String[]{yyyyMMddFormatString}).getTime()) {
                    lastDataDate = format;
                }
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.14
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData<DownloadHrOxFromNetEntity> yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.OX);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.14.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.oxService.saveData(DownloadHrOxFromNetEntity.getOxEntity((List<DownloadHrOxFromNetEntity>) yCRespListData.getData()));
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.OX);
                                }
                            });
                        }
                    }
                };
            } catch (ParseException e) {
                e.printStackTrace();
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.14
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.OX);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.14.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.oxService.saveData(DownloadHrOxFromNetEntity.getOxEntity((List<DownloadHrOxFromNetEntity>) yCRespListData.getData()));
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.OX);
                                }
                            });
                        }
                    }
                };
            }
            netManager.devOxList(uid, lastDataDate, format, yCResponseListener);
        } catch (Throwable th) {
            NetManager.getNetManager().devOxList(uid, lastDataDate, format, new YCResponseListener<YCRespListData<DownloadHrOxFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.14
                @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                public void onError(int i, String str) {
                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.HR);
                }

                @Override // ycnet.runchinaup.core.abs.IResponseListener
                public void onSuccess(final YCRespListData yCRespListData) {
                    if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.OX);
                    } else {
                        SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.14.1
                            @Override // java.lang.Runnable
                            public void run() {
                                SyncDataUtil.oxService.saveData(DownloadHrOxFromNetEntity.getOxEntity((List<DownloadHrOxFromNetEntity>) yCRespListData.getData()));
                                SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.OX);
                            }
                        });
                    }
                }
            });
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadSleepData() {
        NetManager netManager;
        YCResponseListener<YCRespListData<DownloadSleepFromNetEntity>> yCResponseListener;
        String uid = UserUtil.getUid();
        String format = DateFormatUtils.format(System.currentTimeMillis(), yyyyMMddFormatString);
        String lastDataDate = sleepService.getLastDataDate(uid);
        try {
            try {
                if (DateUtils.parseDate(lastDataDate, new String[]{yyyyMMddFormatString}).getTime() > DateUtils.parseDate(format, new String[]{yyyyMMddFormatString}).getTime()) {
                    lastDataDate = format;
                }
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownloadSleepFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.10
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.SLEEP);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(YCRespListData<DownloadSleepFromNetEntity> yCRespListData) {
                        if (((yCRespListData.getData() == null) || (yCRespListData == null)) || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.SLEEP);
                        } else {
                            SyncDataUtil.sleepService.saveData(DownloadSleepFromNetEntity.getSleepEntity(yCRespListData.getData()));
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.SLEEP);
                        }
                    }
                };
            } catch (ParseException e) {
                e.printStackTrace();
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownloadSleepFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.10
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.SLEEP);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(YCRespListData<DownloadSleepFromNetEntity> yCRespListData) {
                        if (((yCRespListData.getData() == null) || (yCRespListData == null)) || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.SLEEP);
                        } else {
                            SyncDataUtil.sleepService.saveData(DownloadSleepFromNetEntity.getSleepEntity(yCRespListData.getData()));
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.SLEEP);
                        }
                    }
                };
            }
            netManager.devSleepList(uid, lastDataDate, format, yCResponseListener);
        } catch (Throwable th) {
            NetManager.getNetManager().devSleepList(uid, lastDataDate, format, new YCResponseListener<YCRespListData<DownloadSleepFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.10
                @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                public void onError(int i, String str) {
                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.SLEEP);
                }

                @Override // ycnet.runchinaup.core.abs.IResponseListener
                public void onSuccess(YCRespListData<DownloadSleepFromNetEntity> yCRespListData) {
                    if (((yCRespListData.getData() == null) || (yCRespListData == null)) || yCRespListData.getData().size() < 1) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.SLEEP);
                    } else {
                        SyncDataUtil.sleepService.saveData(DownloadSleepFromNetEntity.getSleepEntity(yCRespListData.getData()));
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.SLEEP);
                    }
                }
            });
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadStepData() {
        NetManager netManager;
        YCResponseListener<YCRespListData<DownLoadStepFromNetEntity>> yCResponseListener;
        String uid = UserUtil.getUid();
        String format = DateFormatUtils.format(System.currentTimeMillis(), yyyyMMddFormatString);
        String lastDataDate = stepService.getLastDataDate(uid);
        try {
            try {
                if (DateUtils.parseDate(lastDataDate, new String[]{yyyyMMddFormatString}).getTime() > DateUtils.parseDate(format, new String[]{yyyyMMddFormatString}).getTime()) {
                    lastDataDate = format;
                }
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownLoadStepFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.8
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.STEP);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData<DownLoadStepFromNetEntity> yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.STEP);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.8.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.stepService.saveData(DownLoadStepFromNetEntity.getStepEntity((List<DownLoadStepFromNetEntity>) yCRespListData.getData()));
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.STEP);
                                }
                            });
                        }
                    }
                };
            } catch (ParseException e) {
                e.printStackTrace();
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<DownLoadStepFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.8
                    @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                    public void onError(int i, String str) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.STEP);
                    }

                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.STEP);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.8.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.stepService.saveData(DownLoadStepFromNetEntity.getStepEntity((List<DownLoadStepFromNetEntity>) yCRespListData.getData()));
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.STEP);
                                }
                            });
                        }
                    }
                };
            }
            netManager.devWalkList(uid, lastDataDate, format, yCResponseListener);
        } catch (Throwable th) {
            NetManager.getNetManager().devWalkList(uid, lastDataDate, format, new YCResponseListener<YCRespListData<DownLoadStepFromNetEntity>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.8
                @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                public void onError(int i, String str) {
                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.STEP);
                }

                @Override // ycnet.runchinaup.core.abs.IResponseListener
                public void onSuccess(final YCRespListData yCRespListData) {
                    if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.STEP);
                    } else {
                        SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.8.1
                            @Override // java.lang.Runnable
                            public void run() {
                                SyncDataUtil.stepService.saveData(DownLoadStepFromNetEntity.getStepEntity((List<DownLoadStepFromNetEntity>) yCRespListData.getData()));
                                SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.STEP);
                            }
                        });
                    }
                }
            });
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadTrainData() {
        NetManager netManager;
        YCResponseListener<YCRespListData<TrainDataTable>> yCResponseListener;
        String uid = UserUtil.getUid();
        String format = DateFormatUtils.format(System.currentTimeMillis(), yyyyMMddFormatString);
        String lastDataDate = trainService.getLastDataDate(uid);
        try {
            try {
                if (DateUtils.parseDate(lastDataDate, new String[]{yyyyMMddFormatString}).getTime() > DateUtils.parseDate(format, new String[]{yyyyMMddFormatString}).getTime()) {
                    lastDataDate = format;
                }
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<TrainDataTable>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.18
                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData<TrainDataTable> yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.TRAIN);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.18.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.trainService.saveDataForSync(yCRespListData.getData());
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.TRAIN);
                                }
                            });
                        }
                    }
                };
            } catch (ParseException e) {
                e.printStackTrace();
                netManager = NetManager.getNetManager();
                yCResponseListener = new YCResponseListener<YCRespListData<TrainDataTable>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.18
                    @Override // ycnet.runchinaup.core.abs.IResponseListener
                    public void onSuccess(final YCRespListData yCRespListData) {
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                            SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.TRAIN);
                        } else {
                            SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.18.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SyncDataUtil.trainService.saveDataForSync(yCRespListData.getData());
                                    SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.TRAIN);
                                }
                            });
                        }
                    }
                };
            }
            netManager.trainList(uid, lastDataDate, format, yCResponseListener);
        } catch (Throwable th) {
            NetManager.getNetManager().trainList(uid, lastDataDate, format, new YCResponseListener<YCRespListData<TrainDataTable>>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.18
                @Override // ycnet.runchinaup.core.abs.IResponseListener
                public void onSuccess(final YCRespListData yCRespListData) {
                    if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() < 1) {
                        SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.TRAIN);
                    } else {
                        SyncDataUtil.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.18.1
                            @Override // java.lang.Runnable
                            public void run() {
                                SyncDataUtil.trainService.saveDataForSync(yCRespListData.getData());
                                SyncDataUtil.this.notifyDataSyncFinish(SyncDataType.TRAIN);
                            }
                        });
                    }
                }
            });
            throw th;
        }
    }

    public static SyncDataUtil getInstance() {
        return instance;
    }

    public void notifyDataSyncFinish(SyncDataType syncDataType) {
        LogUtil.e("notifyDataSyncFinish==>" + syncDataType);
        Iterator<SyncDataCallback> it = this.syncDataCallbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onDataSyncFinish(syncDataType);
        }
    }

    public void registerCallback(SyncDataCallback syncDataCallback) {
        if (this.syncDataCallbackHashSet.contains(syncDataCallback)) {
            return;
        }
        this.syncDataCallbackHashSet.add(syncDataCallback);
    }

    public void startSyncAllData() {
        cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.1
            @Override // java.lang.Runnable
            public void run() {
                SyncDataUtil.this.syncStepData();
            }
        });
        cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.2
            @Override // java.lang.Runnable
            public void run() {
                SyncDataUtil.this.syncSleepData();
            }
        });
        cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.3
            @Override // java.lang.Runnable
            public void run() {
                SyncDataUtil.this.syncHrData();
            }
        });
        cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.4
            @Override // java.lang.Runnable
            public void run() {
                SyncDataUtil.this.syncOxData();
            }
        });
        cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.5
            @Override // java.lang.Runnable
            public void run() {
                SyncDataUtil.this.syncBloodData();
            }
        });
        cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.6
            @Override // java.lang.Runnable
            public void run() {
                SyncDataUtil.this.syncTrainData();
            }
        });
    }

    public void syncBloodData() {
        final List<BloodDataTable> notSyncData = bloodService.getNotSyncData(UserUtil.getUid());
        LogUtil.e("db>>>本地用户" + UserUtil.getUid() + " 待同步的血压数据是:" + gson.toJson(notSyncData));
        if (notSyncData == null || notSyncData.size() <= 0) {
            downloadBloodData();
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (BloodDataTable bloodDataTable : notSyncData) {
            HashMap hashMap = new HashMap();
            hashMap.put("datetime", bloodDataTable.getDateTimeStr() + "");
            hashMap.put("xueya_gao", bloodDataTable.getBpH() + "");
            hashMap.put("xueya_di", bloodDataTable.getBpL() + "");
            arrayList.add(hashMap);
            bloodDataTable.setSync(true);
        }
        String json = gson.toJson(arrayList);
        LogUtil.e("db 批量上传的血压数据:" + json);
        NetManager.getNetManager().addBPBatch(UserUtil.getUid(), json, new YCResponseListener<YCRespData>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.15
            @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
            public void onError(int i, String str) {
                SyncDataUtil.this.downloadBloodData();
            }

            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(YCRespData yCRespData) {
                SyncDataUtil.bloodService.saveData(notSyncData);
                SyncDataUtil.this.downloadBloodData();
            }
        });
    }

    public void syncHrData() {
        final List<HrDataTable> notSyncData = hrService.getNotSyncData(UserUtil.getUid());
        LogUtil.e("db>>>本地用户" + UserUtil.getUid() + " 待同步的心率数据是:" + gson.toJson(notSyncData));
        if (notSyncData == null || notSyncData.size() <= 0) {
            downloadHrData();
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (HrDataTable hrDataTable : notSyncData) {
            HashMap hashMap = new HashMap();
            hashMap.put("datetime", hrDataTable.getDateTimeStr() + "");
            hashMap.put("number", hrDataTable.getNumber() + "");
            arrayList.add(hashMap);
            hrDataTable.setSync(true);
        }
        String json = gson.toJson(arrayList);
        LogUtil.e("db 批量上传的心率数据:" + json);
        NetManager.getNetManager().addHRBatch(UserUtil.getUid(), json, new YCResponseListener<YCRespData>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.11
            @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
            public void onError(int i, String str) {
                SyncDataUtil.this.downloadHrData();
            }

            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(YCRespData yCRespData) {
                SyncDataUtil.hrService.saveData(notSyncData);
                SyncDataUtil.this.downloadHrData();
            }
        });
    }

    public void syncOxData() {
        final List<OxDataTable> notSyncData = oxService.getNotSyncData(UserUtil.getUid());
        LogUtil.e("db>>>本地用户" + UserUtil.getUid() + " 待同步的血氧数据是:" + gson.toJson(notSyncData));
        if (notSyncData == null || notSyncData.size() <= 0) {
            downloadOxData();
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (OxDataTable oxDataTable : notSyncData) {
            HashMap hashMap = new HashMap();
            hashMap.put("datetime", oxDataTable.getDateTimeStr() + "");
            hashMap.put("number", oxDataTable.getNumber() + "");
            arrayList.add(hashMap);
            oxDataTable.setSync(true);
        }
        String json = gson.toJson(arrayList);
        LogUtil.e("db 批量上传的血氧数据:" + json);
        NetManager.getNetManager().addBOBatch(UserUtil.getUid(), json, new YCResponseListener<YCRespData>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.13
            @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
            public void onError(int i, String str) {
                SyncDataUtil.this.downloadHrData();
            }

            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(YCRespData yCRespData) {
                SyncDataUtil.oxService.saveData(notSyncData);
                SyncDataUtil.this.downloadHrData();
            }
        });
    }

    public final void syncSleepData() {
        final List<SleepDataTable> notSyncData = sleepService.getNotSyncData(UserUtil.getUid());
        LogUtil.e("db>>>本地用户" + UserUtil.getUid() + " 待同步的睡眠数据是:" + gson.toJson(notSyncData));
        if (notSyncData == null || notSyncData.size() <= 0) {
            downloadSleepData();
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (SleepDataTable sleepDataTable : notSyncData) {
            HashMap hashMap = new HashMap();
            hashMap.put("startTime", sleepDataTable.getStartTime() + "");
            hashMap.put("endTime", sleepDataTable.getEndTime() + "");
            hashMap.put("totalTime", sleepDataTable.getTotalTime() + "");
            hashMap.put("deepTime", sleepDataTable.getDeepTime() + "");
            hashMap.put("shallowTime", sleepDataTable.getShallowTime() + "");
            hashMap.put("soberTime", sleepDataTable.getSoberTime() + "");
            hashMap.put("record", sleepDataTable.getRecord() + "");
            arrayList.add(hashMap);
            sleepDataTable.setSync(true);
        }
        String json = gson.toJson(arrayList);
        LogUtil.e("db 批量上传的睡眠数据:" + json);
        NetManager.getNetManager().addSleepBatch(UserUtil.getUid(), json, new YCResponseListener<YCRespData>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.9
            @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
            public void onError(int i, String str) {
                SyncDataUtil.this.downloadSleepData();
            }

            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(YCRespData yCRespData) {
                SyncDataUtil.sleepService.saveData(notSyncData);
                SyncDataUtil.this.downloadSleepData();
            }
        });
    }

    public void syncStepData() {
        final List<StepDataTable> notSyncData = stepService.getNotSyncData(UserUtil.getUid());
        LogUtil.e("db>>>本地用户" + UserUtil.getUid() + " 待同步的步数数据是:" + gson.toJson(notSyncData));
        if (notSyncData == null || notSyncData.size() <= 0) {
            downloadStepData();
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (StepDataTable stepDataTable : notSyncData) {
            HashMap hashMap = new HashMap();
            hashMap.put("walkCounts", stepDataTable.getWalkCounts() + "");
            hashMap.put("goalWalk", stepDataTable.getGoalWalk() + "");
            hashMap.put("calorie", stepDataTable.getCalorie() + "");
            hashMap.put("timeConsuming", stepDataTable.getTimeConsuming() + "");
            hashMap.put("distance", stepDataTable.getDistance() + "");
            hashMap.put("startTime", stepDataTable.getStartTime() + "");
            hashMap.put("detailJson", stepDataTable.getDetailJson() + "");
            arrayList.add(hashMap);
            stepDataTable.setSync(true);
        }
        String json = gson.toJson(arrayList);
        LogUtil.e("db 批量上传的步数数据:" + json);
        NetManager.getNetManager().addWalkBatch(UserUtil.getUid(), json, new YCResponseListener<YCRespData>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.7
            @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
            public void onError(int i, String str) {
                SyncDataUtil.this.downloadStepData();
            }

            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(YCRespData yCRespData) {
                SyncDataUtil.stepService.saveData(notSyncData);
                SyncDataUtil.this.downloadStepData();
            }
        });
    }

    public void syncTrainData() {
        final List<TrainDataTable> notSyncData = trainService.getNotSyncData(UserUtil.getUid());
        LogUtil.e("db>>>本地用户" + UserUtil.getUid() + " 待同步的训练数据是:" + gson.toJson(notSyncData));
        if (notSyncData == null || notSyncData.size() <= 0) {
            downloadTrainData();
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (TrainDataTable trainDataTable : notSyncData) {
            HashMap hashMap = new HashMap();
            hashMap.put("startTime", trainDataTable.getStartTime() + "");
            hashMap.put("endTime", trainDataTable.getEndTime() + "");
            hashMap.put("timeConsuming", trainDataTable.getTimeConsuming() + "");
            hashMap.put("strength", trainDataTable.getStrength() + "");
            hashMap.put("calorie", trainDataTable.getCalorie() + "");
            hashMap.put("fasterRate", trainDataTable.getFasterRate() + "");
            hashMap.put("dataDetail", trainDataTable.getDataDetail() + "");
            hashMap.put("locationDetail", trainDataTable.getLocationDetail() + "");
            arrayList.add(hashMap);
            trainDataTable.setSync(true);
        }
        String json = gson.toJson(arrayList);
        LogUtil.e("db 批量上传的训练数据:" + json);
        NetManager.getNetManager().addTrainBatch(UserUtil.getUid(), json, new YCResponseListener<YCRespData>() { // from class: com.czw.smartkit.homeModule.syncData.SyncDataUtil.17
            @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
            public void onError(int i, String str) {
                SyncDataUtil.this.downloadTrainData();
            }

            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(YCRespData yCRespData) {
                SyncDataUtil.trainService.saveData(notSyncData);
                SyncDataUtil.this.downloadTrainData();
            }
        });
    }

    public void unRegisterCallback(SyncDataCallback syncDataCallback) {
        if (this.syncDataCallbackHashSet.contains(syncDataCallback)) {
            this.syncDataCallbackHashSet.remove(syncDataCallback);
        }
    }
}
