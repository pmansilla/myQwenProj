package cn.smssdk.gui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.layout.IdentifyNumPageLayout;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class IdentifyNumPage extends FakeActivity implements View.OnClickListener, TextWatcher {
    private static final int MIN_REQUEST_VOICE_VERIFY_INTERVAL = 1000;
    private static final int RETRY_INTERVAL = 60;
    private TextView btnSounds;
    private Button btnSubmit;
    private String code;
    private EditText etIdentifyNum;
    private String formatedPhone;
    private EventHandler handler;
    private ImageView ivClear;
    private long lastRequestVVTime;
    private Dialog pd;
    private String phone;
    private BroadcastReceiver smsReceiver;
    private String tempCode;
    private int time = 60;
    private TextView tvIdentifyNotify;
    private TextView tvPhone;
    private TextView tvResend;

    static /* synthetic */ int access$410(IdentifyNumPage identifyNumPage) {
        int i = identifyNumPage.time;
        identifyNumPage.time = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterGet(final int i, final Object obj) {
        runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.IdentifyNumPage.7
            /* JADX WARN: Removed duplicated region for block: B:26:0x008f  */
            /* JADX WARN: Removed duplicated region for block: B:28:0x00b9  */
            /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:31:0x00ab  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void run() {
                /*
                    r5 = this;
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Dialog r0 = cn.smssdk.gui.IdentifyNumPage.access$700(r0)
                    if (r0 == 0) goto L1d
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Dialog r0 = cn.smssdk.gui.IdentifyNumPage.access$700(r0)
                    boolean r0 = r0.isShowing()
                    if (r0 == 0) goto L1d
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Dialog r0 = cn.smssdk.gui.IdentifyNumPage.access$700(r0)
                    r0.dismiss()
                L1d:
                    int r0 = r2
                    r1 = -1
                    r2 = 0
                    if (r0 != r1) goto L4c
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r0 = cn.smssdk.gui.IdentifyNumPage.access$1400(r0)
                    java.lang.String r1 = "smssdk_virificaition_code_sent"
                    int r0 = com.mob.tools.utils.ResHelper.getStringRes(r0, r1)
                    if (r0 <= 0) goto L3e
                    cn.smssdk.gui.IdentifyNumPage r1 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r1 = cn.smssdk.gui.IdentifyNumPage.access$1500(r1)
                    android.widget.Toast r0 = android.widget.Toast.makeText(r1, r0, r2)
                    r0.show()
                L3e:
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    r1 = 60
                    cn.smssdk.gui.IdentifyNumPage.access$402(r0, r1)
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    cn.smssdk.gui.IdentifyNumPage.access$1600(r0)
                    goto Lc6
                L4c:
                    java.lang.Object r0 = r3
                    java.lang.Throwable r0 = (java.lang.Throwable) r0
                    r0.printStackTrace()
                    java.lang.Object r0 = r3
                    java.lang.Throwable r0 = (java.lang.Throwable) r0
                    org.json.JSONObject r1 = new org.json.JSONObject     // Catch: org.json.JSONException -> L82
                    java.lang.String r0 = r0.getMessage()     // Catch: org.json.JSONException -> L82
                    r1.<init>(r0)     // Catch: org.json.JSONException -> L82
                    java.lang.String r0 = "detail"
                    java.lang.String r0 = r1.optString(r0)     // Catch: org.json.JSONException -> L82
                    java.lang.String r3 = "status"
                    int r1 = r1.optInt(r3)     // Catch: org.json.JSONException -> L82
                    boolean r3 = android.text.TextUtils.isEmpty(r0)     // Catch: org.json.JSONException -> L80
                    if (r3 != 0) goto L8b
                    cn.smssdk.gui.IdentifyNumPage r3 = cn.smssdk.gui.IdentifyNumPage.this     // Catch: org.json.JSONException -> L80
                    android.app.Activity r3 = cn.smssdk.gui.IdentifyNumPage.access$1700(r3)     // Catch: org.json.JSONException -> L80
                    android.widget.Toast r0 = android.widget.Toast.makeText(r3, r0, r2)     // Catch: org.json.JSONException -> L80
                    r0.show()     // Catch: org.json.JSONException -> L80
                    return
                L80:
                    r0 = move-exception
                    goto L84
                L82:
                    r0 = move-exception
                    r1 = 0
                L84:
                    com.mob.tools.log.NLog r3 = cn.smssdk.utils.SMSLog.getInstance()
                    r3.w(r0)
                L8b:
                    r0 = 400(0x190, float:5.6E-43)
                    if (r1 < r0) goto Lab
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r0 = cn.smssdk.gui.IdentifyNumPage.access$1800(r0)
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder
                    r3.<init>()
                    java.lang.String r4 = "smssdk_error_desc_"
                    r3.append(r4)
                    r3.append(r1)
                    java.lang.String r1 = r3.toString()
                    int r0 = com.mob.tools.utils.ResHelper.getStringRes(r0, r1)
                    goto Lb7
                Lab:
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r0 = cn.smssdk.gui.IdentifyNumPage.access$1900(r0)
                    java.lang.String r1 = "smssdk_network_error"
                    int r0 = com.mob.tools.utils.ResHelper.getStringRes(r0, r1)
                Lb7:
                    if (r0 <= 0) goto Lc6
                    cn.smssdk.gui.IdentifyNumPage r1 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r1 = cn.smssdk.gui.IdentifyNumPage.access$2000(r1)
                    android.widget.Toast r0 = android.widget.Toast.makeText(r1, r0, r2)
                    r0.show()
                Lc6:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: cn.smssdk.gui.IdentifyNumPage.AnonymousClass7.run():void");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterGetVoice(final int i, final Object obj) {
        runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.IdentifyNumPage.8
            /* JADX WARN: Removed duplicated region for block: B:25:0x0083  */
            /* JADX WARN: Removed duplicated region for block: B:27:0x00ad  */
            /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:30:0x009f  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void run() {
                /*
                    r5 = this;
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Dialog r0 = cn.smssdk.gui.IdentifyNumPage.access$700(r0)
                    if (r0 == 0) goto L1d
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Dialog r0 = cn.smssdk.gui.IdentifyNumPage.access$700(r0)
                    boolean r0 = r0.isShowing()
                    if (r0 == 0) goto L1d
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Dialog r0 = cn.smssdk.gui.IdentifyNumPage.access$700(r0)
                    r0.dismiss()
                L1d:
                    int r0 = r2
                    r1 = -1
                    r2 = 0
                    if (r0 != r1) goto L40
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r0 = cn.smssdk.gui.IdentifyNumPage.access$2100(r0)
                    java.lang.String r1 = "smssdk_send_sounds_success"
                    int r0 = com.mob.tools.utils.ResHelper.getStringRes(r0, r1)
                    if (r0 <= 0) goto Lba
                    cn.smssdk.gui.IdentifyNumPage r1 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r1 = cn.smssdk.gui.IdentifyNumPage.access$2200(r1)
                    android.widget.Toast r0 = android.widget.Toast.makeText(r1, r0, r2)
                    r0.show()
                    goto Lba
                L40:
                    java.lang.Object r0 = r3
                    java.lang.Throwable r0 = (java.lang.Throwable) r0
                    r0.printStackTrace()
                    java.lang.Object r0 = r3
                    java.lang.Throwable r0 = (java.lang.Throwable) r0
                    org.json.JSONObject r1 = new org.json.JSONObject     // Catch: org.json.JSONException -> L76
                    java.lang.String r0 = r0.getMessage()     // Catch: org.json.JSONException -> L76
                    r1.<init>(r0)     // Catch: org.json.JSONException -> L76
                    java.lang.String r0 = "detail"
                    java.lang.String r0 = r1.optString(r0)     // Catch: org.json.JSONException -> L76
                    java.lang.String r3 = "status"
                    int r1 = r1.optInt(r3)     // Catch: org.json.JSONException -> L76
                    boolean r3 = android.text.TextUtils.isEmpty(r0)     // Catch: org.json.JSONException -> L74
                    if (r3 != 0) goto L7f
                    cn.smssdk.gui.IdentifyNumPage r3 = cn.smssdk.gui.IdentifyNumPage.this     // Catch: org.json.JSONException -> L74
                    android.app.Activity r3 = cn.smssdk.gui.IdentifyNumPage.access$2300(r3)     // Catch: org.json.JSONException -> L74
                    android.widget.Toast r0 = android.widget.Toast.makeText(r3, r0, r2)     // Catch: org.json.JSONException -> L74
                    r0.show()     // Catch: org.json.JSONException -> L74
                    return
                L74:
                    r0 = move-exception
                    goto L78
                L76:
                    r0 = move-exception
                    r1 = 0
                L78:
                    com.mob.tools.log.NLog r3 = cn.smssdk.utils.SMSLog.getInstance()
                    r3.w(r0)
                L7f:
                    r0 = 400(0x190, float:5.6E-43)
                    if (r1 < r0) goto L9f
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r0 = cn.smssdk.gui.IdentifyNumPage.access$2400(r0)
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder
                    r3.<init>()
                    java.lang.String r4 = "smssdk_error_desc_"
                    r3.append(r4)
                    r3.append(r1)
                    java.lang.String r1 = r3.toString()
                    int r0 = com.mob.tools.utils.ResHelper.getStringRes(r0, r1)
                    goto Lab
                L9f:
                    cn.smssdk.gui.IdentifyNumPage r0 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r0 = cn.smssdk.gui.IdentifyNumPage.access$2500(r0)
                    java.lang.String r1 = "smssdk_network_error"
                    int r0 = com.mob.tools.utils.ResHelper.getStringRes(r0, r1)
                Lab:
                    if (r0 <= 0) goto Lba
                    cn.smssdk.gui.IdentifyNumPage r1 = cn.smssdk.gui.IdentifyNumPage.this
                    android.app.Activity r1 = cn.smssdk.gui.IdentifyNumPage.access$2600(r1)
                    android.widget.Toast r0 = android.widget.Toast.makeText(r1, r0, r2)
                    r0.show()
                Lba:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: cn.smssdk.gui.IdentifyNumPage.AnonymousClass8.run():void");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterSubmit(final int i, final Object obj) {
        runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.IdentifyNumPage.6
            @Override // java.lang.Runnable
            public void run() {
                int i2;
                if (IdentifyNumPage.this.pd != null && IdentifyNumPage.this.pd.isShowing()) {
                    IdentifyNumPage.this.pd.dismiss();
                }
                if (i == -1) {
                    IdentifyNumPage.this.stopCountDown();
                    PopupDialog.create(IdentifyNumPage.this.getContext(), (String) null, IdentifyNumPage.this.getContext().getResources().getString(ResHelper.getStringRes(IdentifyNumPage.this.getContext(), "smssdk_identify_success")), IdentifyNumPage.this.getContext().getResources().getString(ResHelper.getStringRes(IdentifyNumPage.this.getContext(), "smssdk_confirm")), new View.OnClickListener() { // from class: cn.smssdk.gui.IdentifyNumPage.6.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("res", true);
                            hashMap.put("page", 2);
                            hashMap.put("phone", obj);
                            IdentifyNumPage.this.setResult(hashMap);
                            IdentifyNumPage.this.finish();
                        }
                    }, (String) null, (View.OnClickListener) null, false, false, false).show();
                    return;
                }
                ((Throwable) obj).printStackTrace();
                try {
                    int i3 = new JSONObject(((Throwable) obj).getMessage()).getInt("status");
                    i2 = ResHelper.getStringRes(IdentifyNumPage.this.activity, "smssdk_error_detail_" + i3);
                } catch (JSONException e) {
                    e.printStackTrace();
                    i2 = 0;
                }
                if (i2 == 0) {
                    i2 = ResHelper.getStringRes(IdentifyNumPage.this.activity, "smssdk_virificaition_code_wrong");
                }
                if (i2 > 0) {
                    PopupDialog.create(IdentifyNumPage.this.getContext(), (String) null, IdentifyNumPage.this.getContext().getResources().getString(i2), IdentifyNumPage.this.getContext().getResources().getString(ResHelper.getStringRes(IdentifyNumPage.this.getContext(), "smssdk_confirm")), new View.OnClickListener() { // from class: cn.smssdk.gui.IdentifyNumPage.6.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                        }
                    }, (String) null, (View.OnClickListener) null, true, true, false).show();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void countDown() {
        runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.IdentifyNumPage.3
            @Override // java.lang.Runnable
            public void run() {
                IdentifyNumPage.access$410(IdentifyNumPage.this);
                IdentifyNumPage.this.setResendText(IdentifyNumPage.this.time);
                if (IdentifyNumPage.this.time <= 0) {
                    IdentifyNumPage.this.time = 60;
                } else {
                    IdentifyNumPage.this.runOnUIThread(this, 1000L);
                }
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResendText(int i) {
        if (this.tvResend != null) {
            String string = getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_identify_num_page_resend"));
            if (i == 0) {
                this.tvResend.setText(string);
                this.tvResend.setTextColor(getContext().getResources().getColor(ResHelper.getColorRes(getContext(), "smssdk_main_color")));
                this.tvResend.setClickable(true);
                return;
            }
            this.tvResend.setText(string + SQLBuilder.PARENTHESES_LEFT + i + SQLBuilder.PARENTHESES_RIGHT);
            this.tvResend.setTextColor(getContext().getResources().getColor(ResHelper.getColorRes(getContext(), "smssdk_tv_light_gray")));
            this.tvResend.setClickable(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNotifyDialog() {
        PopupDialog.create(getContext(), (String) null, getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_close_identify_page_dialog")), getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_confirm")), new View.OnClickListener() { // from class: cn.smssdk.gui.IdentifyNumPage.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                IdentifyNumPage.this.stopCountDown();
                IdentifyNumPage.this.finish();
            }
        }, getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_wait")), new View.OnClickListener() { // from class: cn.smssdk.gui.IdentifyNumPage.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
            }
        }, true, true, false).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopCountDown() {
        this.time = 1;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        int idRes = ResHelper.getIdRes(this.activity, "ll_back");
        int idRes2 = ResHelper.getIdRes(this.activity, "btn_submit");
        int idRes3 = ResHelper.getIdRes(this.activity, "iv_clear");
        int idRes4 = ResHelper.getIdRes(this.activity, "tv_voice");
        int idRes5 = ResHelper.getIdRes(this.activity, "tv_resend");
        if (id == idRes) {
            runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.IdentifyNumPage.4
                @Override // java.lang.Runnable
                public void run() {
                    IdentifyNumPage.this.showNotifyDialog();
                }
            });
            return;
        }
        if (id == idRes2) {
            String trim = this.etIdentifyNum.getText().toString().trim();
            if (TextUtils.isEmpty(this.code)) {
                int stringRes = ResHelper.getStringRes(this.activity, "smssdk_write_identify_code");
                if (stringRes > 0) {
                    Toast.makeText(getContext(), stringRes, 0).show();
                    return;
                }
                return;
            }
            if (this.pd != null && this.pd.isShowing()) {
                this.pd.dismiss();
            }
            this.pd = CommonDialog.ProgressDialog(this.activity);
            if (this.pd != null) {
                this.pd.show();
            }
            SMSSDK.submitVerificationCode(this.code, this.phone, trim);
            return;
        }
        if (id == idRes3) {
            this.etIdentifyNum.getText().clear();
            return;
        }
        if (id == idRes4) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.lastRequestVVTime > 1000) {
                this.lastRequestVVTime = currentTimeMillis;
                PopupDialog.create(getContext(), (String) null, getContext().getResources().getString(ResHelper.getStringRes(this.activity, "smssdk_send_sounds_identify_code")), getContext().getResources().getString(ResHelper.getStringRes(this.activity, "smssdk_i_know")), new View.OnClickListener() { // from class: cn.smssdk.gui.IdentifyNumPage.5
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        if (IdentifyNumPage.this.pd != null && IdentifyNumPage.this.pd.isShowing()) {
                            IdentifyNumPage.this.pd.dismiss();
                        }
                        IdentifyNumPage.this.pd = CommonDialog.ProgressDialog(IdentifyNumPage.this.activity);
                        if (IdentifyNumPage.this.pd != null) {
                            IdentifyNumPage.this.pd.show();
                        }
                        SMSSDK.getVoiceVerifyCode(IdentifyNumPage.this.code, IdentifyNumPage.this.phone);
                    }
                }, (String) null, (View.OnClickListener) null, true, true, false).show();
                return;
            }
            return;
        }
        if (id == idRes5) {
            if (this.pd != null && this.pd.isShowing()) {
                this.pd.dismiss();
            }
            this.pd = CommonDialog.ProgressDialog(this.activity);
            if (this.pd != null) {
                this.pd.show();
            }
            SMSSDK.getVerificationCode(this.code, this.phone.trim(), this.tempCode, null);
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        LinearLayout layout = new IdentifyNumPageLayout(this.activity).getLayout();
        if (layout != null) {
            this.activity.setContentView(layout);
            this.activity.findViewById(ResHelper.getIdRes(this.activity, "ll_back")).setOnClickListener(this);
            this.btnSubmit = (Button) this.activity.findViewById(ResHelper.getIdRes(this.activity, "btn_submit"));
            this.btnSubmit.setOnClickListener(this);
            this.btnSubmit.setEnabled(false);
            this.etIdentifyNum = (EditText) this.activity.findViewById(ResHelper.getIdRes(this.activity, "et_put_identify"));
            this.etIdentifyNum.addTextChangedListener(this);
            this.tvIdentifyNotify = (TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_identify_notify"));
            int stringRes = ResHelper.getStringRes(this.activity, "smssdk_send_mobile_detail");
            if (stringRes > 0) {
                this.tvIdentifyNotify.setText(Html.fromHtml(getContext().getString(stringRes)));
            }
            this.tvPhone = (TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_phone"));
            this.tvPhone.setText(this.formatedPhone);
            this.ivClear = (ImageView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "iv_clear"));
            this.ivClear.setOnClickListener(this);
            this.tvResend = (TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_resend"));
            this.tvResend.setOnClickListener(this);
            this.btnSounds = (TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_voice"));
            this.btnSounds.setOnClickListener(this);
            this.handler = new EventHandler() { // from class: cn.smssdk.gui.IdentifyNumPage.1
                @Override // cn.smssdk.EventHandler
                public void afterEvent(int i, int i2, Object obj) {
                    if (i == 3) {
                        IdentifyNumPage.this.afterSubmit(i2, obj);
                    } else if (i == 2) {
                        IdentifyNumPage.this.afterGet(i2, obj);
                    } else if (i == 8) {
                        IdentifyNumPage.this.afterGetVoice(i2, obj);
                    }
                }
            };
            SMSSDK.registerEventHandler(this.handler);
            countDown();
        }
        try {
            if (DeviceHelper.getInstance(this.activity).checkPermission("android.permission.RECEIVE_SMS")) {
                this.smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() { // from class: cn.smssdk.gui.IdentifyNumPage.2
                    @Override // cn.smssdk.SMSSDK.VerifyCodeReadListener
                    public void onReadVerifyCode(final String str) {
                        IdentifyNumPage.this.runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.IdentifyNumPage.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IdentifyNumPage.this.etIdentifyNum.setText(str);
                            }
                        });
                    }
                });
                this.activity.registerReceiver(this.smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
            }
        } catch (Throwable th) {
            th.printStackTrace();
            this.smsReceiver = null;
        }
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onFinish() {
        SMSSDK.unregisterEventHandler(this.handler);
        if (this.smsReceiver != null) {
            try {
                this.activity.unregisterReceiver(this.smsReceiver);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return super.onFinish();
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onKeyEvent(int i, KeyEvent keyEvent) {
        if (i != 4 || keyEvent.getAction() != 0) {
            return false;
        }
        runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.IdentifyNumPage.11
            @Override // java.lang.Runnable
            public void run() {
                IdentifyNumPage.this.showNotifyDialog();
            }
        });
        return true;
    }

    @Override // com.mob.tools.FakeActivity
    public void onPause() {
        super.onPause();
    }

    @Override // com.mob.tools.FakeActivity
    public void onResume() {
        super.onResume();
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (charSequence.length() > 0) {
            this.btnSubmit.setEnabled(true);
            this.ivClear.setVisibility(0);
            int bitmapRes = ResHelper.getBitmapRes(this.activity, "smssdk_btn_enable");
            if (bitmapRes > 0) {
                this.btnSubmit.setBackgroundResource(bitmapRes);
                return;
            }
            return;
        }
        this.btnSubmit.setEnabled(false);
        this.ivClear.setVisibility(8);
        int bitmapRes2 = ResHelper.getBitmapRes(this.activity, "smssdk_btn_disenable");
        if (bitmapRes2 > 0) {
            this.btnSubmit.setBackgroundResource(bitmapRes2);
        }
    }

    public void setPhone(String str, String str2, String str3) {
        this.phone = str;
        this.code = str2;
        this.formatedPhone = str3;
    }

    public void setTempCode(String str) {
        this.tempCode = str;
    }
}
