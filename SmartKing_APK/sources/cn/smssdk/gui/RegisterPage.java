package cn.smssdk.gui;

import android.app.Dialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.layout.RegisterPageLayout;
import cn.smssdk.gui.util.GUISPDB;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.wrapper.TokenVerifyResult;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import java.util.HashMap;

/* loaded from: classes.dex */
public class RegisterPage extends FakeActivity implements View.OnClickListener, TextWatcher {
    private static final String DEFAULT_COUNTRY_ID = "42";
    private Button btnNext;
    private EventHandler callback;
    private String currentCode;
    private String currentId;
    private EditText etPhoneNum;
    private EventHandler handler;
    private ImageView ivClear;
    private OnSendMessageHandler osmHandler;
    private Dialog pd;
    private String tempCode;
    private TextView tvCountry;
    private TextView tvCountryNum;
    private TokenVerifyResult verifyResult;

    /* JADX INFO: Access modifiers changed from: private */
    public void afterVerificationCodeRequested(boolean z) {
        String replaceAll = this.etPhoneNum.getText().toString().trim().replaceAll("\\s*", "");
        String trim = this.tvCountryNum.getText().toString().trim();
        if (trim.startsWith("+")) {
            trim = trim.substring(1);
        }
        String str = "+" + trim + SQLBuilder.BLANK + splitPhoneNum(replaceAll);
        if (z) {
            SmartVerifyPage smartVerifyPage = new SmartVerifyPage();
            smartVerifyPage.setPhone(replaceAll, trim, str);
            smartVerifyPage.showForResult(this.activity, null, this);
        } else {
            IdentifyNumPage identifyNumPage = new IdentifyNumPage();
            identifyNumPage.setPhone(replaceAll, trim, str);
            identifyNumPage.setTempCode(this.tempCode);
            identifyNumPage.showForResult(this.activity, null, this);
            SMSSDK.unregisterEventHandler(this.handler);
        }
    }

    private String[] getCurrentCountry() {
        String mcc = getMCC();
        String[] countryByMCC = !TextUtils.isEmpty(mcc) ? SMSSDK.getCountryByMCC(mcc) : null;
        if (countryByMCC != null) {
            return countryByMCC;
        }
        SMSLog.getInstance().d("no country found by MCC: " + mcc, new Object[0]);
        return SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
    }

    private String getMCC() {
        TelephonyManager telephonyManager = (TelephonyManager) this.activity.getSystemService("phone");
        String networkOperator = telephonyManager.getNetworkOperator();
        return !TextUtils.isEmpty(networkOperator) ? networkOperator : telephonyManager.getSimOperator();
    }

    private void hideKeyboard() {
        ((InputMethodManager) this.activity.getSystemService("input_method")).hideSoftInputFromWindow(this.activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showVerifyResult() {
        hideKeyboard();
        PopupDialog.create((Context) this.activity, (String) null, this.activity.getString(ResHelper.getStringRes(this.activity, "smssdk_identify_success")), this.activity.getResources().getString(ResHelper.getStringRes(this.activity, "smssdk_confirm")), new View.OnClickListener() { // from class: cn.smssdk.gui.RegisterPage.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RegisterPage.this.finish();
            }
        }, (String) null, (View.OnClickListener) null, false, false, false).show();
    }

    private String splitPhoneNum(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        int length = sb.length();
        for (int i = 4; i < length; i += 5) {
            sb.insert(i, ' ');
        }
        sb.reverse();
        return sb.toString();
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void confirmSend(final String str, final String str2) {
        PopupDialog.create(getContext(), getContext().getResources().getString(ResHelper.getStringRes(this.activity, "smssdk_make_sure_mobile_num")), String.format(getContext().getResources().getString(ResHelper.getStringRes(this.activity, "smssdk_make_sure_mobile_detail")), str2 + SQLBuilder.BLANK + splitPhoneNum(str)), getContext().getResources().getString(ResHelper.getStringRes(this.activity, "smssdk_ok")), new View.OnClickListener() { // from class: cn.smssdk.gui.RegisterPage.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (RegisterPage.this.pd != null && RegisterPage.this.pd.isShowing()) {
                    RegisterPage.this.pd.dismiss();
                }
                RegisterPage.this.pd = CommonDialog.ProgressDialog(RegisterPage.this.activity);
                if (RegisterPage.this.pd != null) {
                    RegisterPage.this.pd.show();
                }
                if (TextUtils.isEmpty(RegisterPage.this.tempCode)) {
                    RegisterPage.this.tempCode = GUISPDB.getTempCode();
                }
                SMSLog.getInstance().i("verification phone ==>>" + str, new Object[0]);
                SMSLog.getInstance().i("verification tempCode ==>>" + RegisterPage.this.tempCode, new Object[0]);
                SMSSDK.getVerificationCode(str2, str.trim(), RegisterPage.this.tempCode, RegisterPage.this.osmHandler);
            }
        }, getContext().getResources().getString(ResHelper.getStringRes(this.activity, "smssdk_cancel")), new View.OnClickListener() { // from class: cn.smssdk.gui.RegisterPage.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
            }
        }, true, true, false).show();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        int idRes = ResHelper.getIdRes(this.activity, "ll_back");
        int idRes2 = ResHelper.getIdRes(this.activity, "rl_country");
        int idRes3 = ResHelper.getIdRes(this.activity, "btn_next");
        int idRes4 = ResHelper.getIdRes(this.activity, "iv_clear");
        if (id == idRes) {
            finish();
            return;
        }
        if (id == idRes2) {
            CountryPage countryPage = new CountryPage();
            countryPage.setCountryId(this.currentId);
            countryPage.showForResult(this.activity, null, this);
        } else if (id != idRes3) {
            if (id == idRes4) {
                this.etPhoneNum.getText().clear();
            }
        } else {
            String replaceAll = this.etPhoneNum.getText().toString().trim().replaceAll("\\s*", "");
            String trim = this.tvCountryNum.getText().toString().trim();
            if (this.verifyResult == null) {
                confirmSend(replaceAll, trim);
            } else {
                SMSSDK.login(replaceAll, this.verifyResult);
            }
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        LinearLayout layout = new RegisterPageLayout(this.activity).getLayout();
        if (layout != null) {
            this.activity.setContentView(layout);
            this.currentId = DEFAULT_COUNTRY_ID;
            View findViewById = this.activity.findViewById(ResHelper.getIdRes(this.activity, "ll_back"));
            View findViewById2 = this.activity.findViewById(ResHelper.getIdRes(this.activity, "rl_country"));
            this.btnNext = (Button) this.activity.findViewById(ResHelper.getIdRes(this.activity, "btn_next"));
            this.tvCountry = (TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_country"));
            String[] currentCountry = getCurrentCountry();
            if (currentCountry != null) {
                this.currentCode = currentCountry[1];
                this.tvCountry.setText(currentCountry[0]);
            }
            this.tvCountryNum = (TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_country_num"));
            this.tvCountryNum.setText("+" + this.currentCode);
            this.etPhoneNum = (EditText) this.activity.findViewById(ResHelper.getIdRes(this.activity, "et_write_phone"));
            this.etPhoneNum.setText("");
            this.etPhoneNum.addTextChangedListener(this);
            this.etPhoneNum.requestFocus();
            if (this.etPhoneNum.getText().length() > 0) {
                this.btnNext.setEnabled(true);
                this.ivClear = (ImageView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "iv_clear"));
                this.ivClear.setVisibility(0);
                int bitmapRes = ResHelper.getBitmapRes(this.activity, "smssdk_btn_enable");
                if (bitmapRes > 0) {
                    this.btnNext.setBackgroundResource(bitmapRes);
                }
            }
            this.ivClear = (ImageView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "iv_clear"));
            findViewById.setOnClickListener(this);
            this.btnNext.setOnClickListener(this);
            this.ivClear.setOnClickListener(this);
            findViewById2.setOnClickListener(this);
            this.handler = new EventHandler() { // from class: cn.smssdk.gui.RegisterPage.1
                @Override // cn.smssdk.EventHandler
                public void afterEvent(final int i, final int i2, final Object obj) {
                    RegisterPage.this.runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.RegisterPage.1.1
                        /* JADX WARN: Removed duplicated region for block: B:41:0x00df  */
                        /* JADX WARN: Removed duplicated region for block: B:43:0x010d  */
                        /* JADX WARN: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
                        /* JADX WARN: Removed duplicated region for block: B:46:0x00fd  */
                        @Override // java.lang.Runnable
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct add '--show-bad-code' argument
                        */
                        public void run() {
                            /*
                                Method dump skipped, instructions count: 382
                                To view this dump add '--comments-level debug' option
                            */
                            throw new UnsupportedOperationException("Method not decompiled: cn.smssdk.gui.RegisterPage.AnonymousClass1.RunnableC00151.run():void");
                        }
                    });
                }
            };
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onDestroy() {
        SMSSDK.unregisterEventHandler(this.handler);
    }

    @Override // com.mob.tools.FakeActivity
    public void onPause() {
    }

    @Override // com.mob.tools.FakeActivity
    public void onResult(HashMap<String, Object> hashMap) {
        if (hashMap != null) {
            int intValue = ((Integer) hashMap.get("page")).intValue();
            if (intValue != 1) {
                if (intValue == 2) {
                    Object obj = hashMap.get("res");
                    HashMap hashMap2 = (HashMap) hashMap.get("phone");
                    if (obj == null || hashMap2 == null) {
                        return;
                    }
                    if (this.callback != null) {
                        this.callback.afterEvent(3, -1, hashMap2);
                    }
                    finish();
                    return;
                }
                return;
            }
            this.currentId = (String) hashMap.get(ConnectionModel.ID);
            String[] country = SMSSDK.getCountry(this.currentId);
            if (country != null) {
                this.currentCode = country[1];
                this.tvCountryNum.setText("+" + this.currentCode);
                this.tvCountry.setText(country[0]);
            }
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onResume() {
        SMSSDK.registerEventHandler(this.handler);
        SMSSDK.getToken();
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (charSequence.length() > 0) {
            this.btnNext.setEnabled(true);
            this.ivClear.setVisibility(0);
            int bitmapRes = ResHelper.getBitmapRes(this.activity, "smssdk_btn_enable");
            if (bitmapRes > 0) {
                this.btnNext.setBackgroundResource(bitmapRes);
                return;
            }
            return;
        }
        this.btnNext.setEnabled(false);
        this.ivClear.setVisibility(8);
        int bitmapRes2 = ResHelper.getBitmapRes(this.activity, "smssdk_btn_disenable");
        if (bitmapRes2 > 0) {
            this.btnNext.setBackgroundResource(bitmapRes2);
        }
    }

    public void setOnSendMessageHandler(OnSendMessageHandler onSendMessageHandler) {
        this.osmHandler = onSendMessageHandler;
    }

    public void setRegisterCallback(EventHandler eventHandler) {
        this.callback = eventHandler;
    }

    public void setTempCode(String str) {
        this.tempCode = str;
        GUISPDB.setTempCode(str);
    }

    public void show(Context context) {
        super.show(context, null);
    }
}
