package cn.smssdk.gui;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.GroupListView;
import cn.smssdk.gui.layout.CountryListPageLayout;
import cn.smssdk.utils.SMSLog;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class CountryPage extends FakeActivity implements View.OnClickListener, TextWatcher, GroupListView.OnItemClickListener {
    private HashMap<String, String> countryRules;
    private EditText etSearch;
    private EventHandler handler;
    private String id;
    private CountryListView listView;
    private Dialog pd;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: cn.smssdk.gui.CountryPage$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            LinearLayout layout = new CountryListPageLayout(CountryPage.this.activity).getLayout();
            if (layout != null) {
                CountryPage.this.activity.setContentView(layout);
            }
            if (CountryPage.this.countryRules == null || CountryPage.this.countryRules.size() <= 0) {
                CountryPage.this.handler = new EventHandler() { // from class: cn.smssdk.gui.CountryPage.2.1
                    @Override // cn.smssdk.EventHandler
                    public void afterEvent(int i, final int i2, final Object obj) {
                        if (i == 1) {
                            CountryPage.this.runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.CountryPage.2.1.1
                                /* JADX WARN: Removed duplicated region for block: B:22:0x008e  */
                                /* JADX WARN: Removed duplicated region for block: B:24:0x00c0  */
                                /* JADX WARN: Removed duplicated region for block: B:27:0x00ae  */
                                @Override // java.lang.Runnable
                                /*
                                    Code decompiled incorrectly, please refer to instructions dump.
                                    To view partially-correct add '--show-bad-code' argument
                                */
                                public void run() {
                                    /*
                                        r5 = this;
                                        cn.smssdk.gui.CountryPage$2$1 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this
                                        cn.smssdk.gui.CountryPage$2 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.this
                                        cn.smssdk.gui.CountryPage r0 = cn.smssdk.gui.CountryPage.this
                                        android.app.Dialog r0 = cn.smssdk.gui.CountryPage.access$500(r0)
                                        if (r0 == 0) goto L29
                                        cn.smssdk.gui.CountryPage$2$1 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this
                                        cn.smssdk.gui.CountryPage$2 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.this
                                        cn.smssdk.gui.CountryPage r0 = cn.smssdk.gui.CountryPage.this
                                        android.app.Dialog r0 = cn.smssdk.gui.CountryPage.access$500(r0)
                                        boolean r0 = r0.isShowing()
                                        if (r0 == 0) goto L29
                                        cn.smssdk.gui.CountryPage$2$1 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this
                                        cn.smssdk.gui.CountryPage$2 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.this
                                        cn.smssdk.gui.CountryPage r0 = cn.smssdk.gui.CountryPage.this
                                        android.app.Dialog r0 = cn.smssdk.gui.CountryPage.access$500(r0)
                                        r0.dismiss()
                                    L29:
                                        int r0 = r2
                                        r1 = -1
                                        if (r0 != r1) goto L3d
                                        cn.smssdk.gui.CountryPage$2$1 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this
                                        cn.smssdk.gui.CountryPage$2 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.this
                                        cn.smssdk.gui.CountryPage r0 = cn.smssdk.gui.CountryPage.this
                                        java.lang.Object r1 = r3
                                        java.util.ArrayList r1 = (java.util.ArrayList) r1
                                        cn.smssdk.gui.CountryPage.access$600(r0, r1)
                                        goto Lda
                                    L3d:
                                        r0 = 0
                                        java.lang.Object r1 = r3     // Catch: java.lang.Exception -> L81
                                        java.lang.Throwable r1 = (java.lang.Throwable) r1     // Catch: java.lang.Exception -> L81
                                        r1.printStackTrace()     // Catch: java.lang.Exception -> L81
                                        java.lang.Object r1 = r3     // Catch: java.lang.Exception -> L81
                                        java.lang.Throwable r1 = (java.lang.Throwable) r1     // Catch: java.lang.Exception -> L81
                                        org.json.JSONObject r2 = new org.json.JSONObject     // Catch: java.lang.Exception -> L81
                                        java.lang.String r1 = r1.getMessage()     // Catch: java.lang.Exception -> L81
                                        r2.<init>(r1)     // Catch: java.lang.Exception -> L81
                                        java.lang.String r1 = "detail"
                                        java.lang.String r1 = r2.optString(r1)     // Catch: java.lang.Exception -> L81
                                        java.lang.String r3 = "status"
                                        int r2 = r2.optInt(r3)     // Catch: java.lang.Exception -> L81
                                        boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Exception -> L7f
                                        if (r3 != 0) goto L8a
                                        cn.smssdk.gui.CountryPage$2$1 r3 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this     // Catch: java.lang.Exception -> L7f
                                        cn.smssdk.gui.CountryPage$2 r3 = cn.smssdk.gui.CountryPage.AnonymousClass2.this     // Catch: java.lang.Exception -> L7f
                                        cn.smssdk.gui.CountryPage r3 = cn.smssdk.gui.CountryPage.this     // Catch: java.lang.Exception -> L7f
                                        android.app.Activity r3 = cn.smssdk.gui.CountryPage.access$700(r3)     // Catch: java.lang.Exception -> L7f
                                        android.widget.Toast r1 = android.widget.Toast.makeText(r3, r1, r0)     // Catch: java.lang.Exception -> L7f
                                        r1.show()     // Catch: java.lang.Exception -> L7f
                                        cn.smssdk.gui.CountryPage$2$1 r1 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this     // Catch: java.lang.Exception -> L7f
                                        cn.smssdk.gui.CountryPage$2 r1 = cn.smssdk.gui.CountryPage.AnonymousClass2.this     // Catch: java.lang.Exception -> L7f
                                        cn.smssdk.gui.CountryPage r1 = cn.smssdk.gui.CountryPage.this     // Catch: java.lang.Exception -> L7f
                                        r1.finish()     // Catch: java.lang.Exception -> L7f
                                        return
                                    L7f:
                                        r1 = move-exception
                                        goto L83
                                    L81:
                                        r1 = move-exception
                                        r2 = 0
                                    L83:
                                        com.mob.tools.log.NLog r3 = cn.smssdk.utils.SMSLog.getInstance()
                                        r3.w(r1)
                                    L8a:
                                        r1 = 400(0x190, float:5.6E-43)
                                        if (r2 < r1) goto Lae
                                        cn.smssdk.gui.CountryPage$2$1 r1 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this
                                        cn.smssdk.gui.CountryPage$2 r1 = cn.smssdk.gui.CountryPage.AnonymousClass2.this
                                        cn.smssdk.gui.CountryPage r1 = cn.smssdk.gui.CountryPage.this
                                        android.app.Activity r1 = cn.smssdk.gui.CountryPage.access$800(r1)
                                        java.lang.StringBuilder r3 = new java.lang.StringBuilder
                                        r3.<init>()
                                        java.lang.String r4 = "smssdk_error_desc_"
                                        r3.append(r4)
                                        r3.append(r2)
                                        java.lang.String r2 = r3.toString()
                                        int r1 = com.mob.tools.utils.ResHelper.getStringRes(r1, r2)
                                        goto Lbe
                                    Lae:
                                        cn.smssdk.gui.CountryPage$2$1 r1 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this
                                        cn.smssdk.gui.CountryPage$2 r1 = cn.smssdk.gui.CountryPage.AnonymousClass2.this
                                        cn.smssdk.gui.CountryPage r1 = cn.smssdk.gui.CountryPage.this
                                        android.app.Activity r1 = cn.smssdk.gui.CountryPage.access$900(r1)
                                        java.lang.String r2 = "smssdk_network_error"
                                        int r1 = com.mob.tools.utils.ResHelper.getStringRes(r1, r2)
                                    Lbe:
                                        if (r1 <= 0) goto Ld1
                                        cn.smssdk.gui.CountryPage$2$1 r2 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this
                                        cn.smssdk.gui.CountryPage$2 r2 = cn.smssdk.gui.CountryPage.AnonymousClass2.this
                                        cn.smssdk.gui.CountryPage r2 = cn.smssdk.gui.CountryPage.this
                                        android.app.Activity r2 = cn.smssdk.gui.CountryPage.access$1000(r2)
                                        android.widget.Toast r0 = android.widget.Toast.makeText(r2, r1, r0)
                                        r0.show()
                                    Ld1:
                                        cn.smssdk.gui.CountryPage$2$1 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.this
                                        cn.smssdk.gui.CountryPage$2 r0 = cn.smssdk.gui.CountryPage.AnonymousClass2.this
                                        cn.smssdk.gui.CountryPage r0 = cn.smssdk.gui.CountryPage.this
                                        r0.finish()
                                    Lda:
                                        return
                                    */
                                    throw new UnsupportedOperationException("Method not decompiled: cn.smssdk.gui.CountryPage.AnonymousClass2.AnonymousClass1.RunnableC00131.run():void");
                                }
                            });
                        }
                    }
                };
                SMSSDK.registerEventHandler(CountryPage.this.handler);
                SMSSDK.getSupportedCountries();
                return;
            }
            if (CountryPage.this.pd != null && CountryPage.this.pd.isShowing()) {
                CountryPage.this.pd.dismiss();
            }
            CountryPage.this.initPage();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterPrepare() {
        runOnUIThread(new AnonymousClass2());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPage() {
        this.activity.getWindow().setSoftInputMode(32);
        this.activity.findViewById(ResHelper.getIdRes(this.activity, "ll_back")).setOnClickListener(this);
        TextView textView = (TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_title"));
        int stringRes = ResHelper.getStringRes(this.activity, "smssdk_choose_country");
        if (stringRes > 0) {
            textView.setText(stringRes);
        }
        textView.setTextSize(0, ResHelper.dipToPx(getContext(), 17));
        textView.getPaint().setFakeBoldText(true);
        textView.setTextColor(getContext().getResources().getColor(ResHelper.getColorRes(getContext(), "smssdk_464646")));
        this.listView = (CountryListView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "clCountry"));
        this.listView.setOnItemClickListener(this);
        this.listView.setCurrentCountryId(this.id);
        this.etSearch = (EditText) this.activity.findViewById(ResHelper.getIdRes(this.activity, "et_put_identify"));
        this.etSearch.addTextChangedListener(this);
        View findViewById = this.activity.findViewById(ResHelper.getIdRes(this.activity, "iv_clear"));
        if (findViewById != null) {
            findViewById.setOnClickListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCountryListGot(ArrayList<HashMap<String, Object>> arrayList) {
        Iterator<HashMap<String, Object>> it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> next = it.next();
            String str = (String) next.get("zone");
            String str2 = (String) next.get("rule");
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                if (this.countryRules == null) {
                    this.countryRules = new HashMap<>();
                }
                this.countryRules.put(str, str2);
            }
        }
        initPage();
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
        int idRes2 = ResHelper.getIdRes(this.activity, "iv_clear");
        if (id == idRes) {
            finish();
        } else if (id == idRes2) {
            this.etSearch.getText().clear();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        if (this.pd != null && this.pd.isShowing()) {
            this.pd.dismiss();
        }
        this.pd = CommonDialog.ProgressDialog(this.activity);
        if (this.pd != null) {
            this.pd.show();
        }
        SearchEngine.prepare(this.activity, new Runnable() { // from class: cn.smssdk.gui.CountryPage.1
            @Override // java.lang.Runnable
            public void run() {
                CountryPage.this.afterPrepare();
            }
        });
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onFinish() {
        SMSSDK.unregisterEventHandler(this.handler);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(ConnectionModel.ID, this.id);
        hashMap.put("page", 1);
        setResult(hashMap);
        return super.onFinish();
    }

    @Override // cn.smssdk.gui.GroupListView.OnItemClickListener
    public void onItemClick(GroupListView groupListView, View view, int i, int i2) {
        if (i2 >= 0) {
            String[] country = this.listView.getCountry(i, i2);
            if (this.countryRules != null && this.countryRules.containsKey(country[1])) {
                this.id = country[2];
                finish();
            } else {
                int stringRes = ResHelper.getStringRes(this.activity, "smssdk_country_not_support_currently");
                if (stringRes > 0) {
                    Toast.makeText(this.activity, stringRes, 0).show();
                }
            }
        }
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onKeyEvent(int i, KeyEvent keyEvent) {
        try {
            int idRes = ResHelper.getIdRes(this.activity, "llSearch");
            if (i == 4 && keyEvent.getAction() == 0 && this.activity.findViewById(idRes).getVisibility() == 0) {
                this.activity.findViewById(idRes).setVisibility(8);
                this.activity.findViewById(ResHelper.getIdRes(this.activity, "llTitle")).setVisibility(0);
                this.etSearch.setText("");
                return true;
            }
        } catch (Throwable th) {
            SMSLog.getInstance().w(th);
        }
        return super.onKeyEvent(i, keyEvent);
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
        this.listView.onSearch(charSequence.toString().toLowerCase());
    }

    public void setCountryId(String str) {
        this.id = str;
    }

    public void setCountryRuls(HashMap<String, String> hashMap) {
        this.countryRules = hashMap;
    }
}
