package com.czw.smartkit.setting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.data.RemindSetting;
import com.czw.smartkit.preferenceModule.SharePreferenceRemind;
import com.czw.smartkit.views.popw.SingleScrollerPop;
import com.suke.widget.SwitchButton;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class HrSettingActivity extends TitleActivity implements View.OnClickListener {
    private ImageView item_1_yes;
    private ImageView item_2_yes;
    private TextView leftState;
    RemindSetting remindSetting = null;
    private SwitchButton sbHrSetting;
    private TextView time_len_tv;

    public static ArrayList<String> laodSitTime() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(MainApplication.getContext().getString(R.string.time_len_5_min));
        arrayList.add(MainApplication.getContext().getString(R.string.time_len_10_min));
        arrayList.add(MainApplication.getContext().getString(R.string.time_len_30_min));
        arrayList.add(MainApplication.getContext().getString(R.string.time_len_1_hour));
        arrayList.add(MainApplication.getContext().getString(R.string.time_len_2_hour));
        arrayList.add(MainApplication.getContext().getString(R.string.time_len_6_hour));
        arrayList.add(MainApplication.getContext().getString(R.string.time_len_12_hour));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTimePop() {
        SingleScrollerPop.getPop(getUI()).showPicker($View(R.id.acv_win), laodSitTime(), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.setting.HrSettingActivity.2
            @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
            public void onSelect(int i) {
                HrSettingActivity.this.remindSetting.hrCheckDuration = i;
                HrSettingActivity.this.time_len_tv.setText(HrSettingActivity.laodSitTime().get(i));
                HrSettingActivity.this.writeData();
            }
        }).showTitleWithValue(R.string.hr_timer_check, laodSitTime().get(this.remindSetting.hrCheckDuration));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeData() {
        BleManager.getBleManager().writeData(DataStruct.createRemindEnable(this.remindSetting));
        SharePreferenceRemind.save(this.remindSetting);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.hr_set_tile);
        this.leftState = (TextView) $View(R.id.leftTime);
        this.item_1_yes = (ImageView) $View(R.id.item_1_yes);
        this.item_2_yes = (ImageView) $View(R.id.item_2_yes);
        this.sbHrSetting = (SwitchButton) $View(R.id.hr_setting_cycle_sb);
        this.time_len_tv = (TextView) $View(R.id.time_len_tv);
        $View(R.id.alertTime).setOnClickListener(this);
        $View(R.id.hr_item_1).setOnClickListener(this);
        $View(R.id.hr_item_2).setOnClickListener(this);
        this.remindSetting = SharePreferenceRemind.read();
        if (this.remindSetting.dynamicHr) {
            this.item_1_yes.setVisibility(8);
            this.item_2_yes.setVisibility(0);
        } else {
            this.item_1_yes.setVisibility(0);
            this.item_2_yes.setVisibility(8);
        }
        if (this.remindSetting.hrCycle) {
            this.leftState.setText(R.string.state_open);
        } else {
            this.leftState.setText(R.string.state_close);
        }
        this.time_len_tv.setText(laodSitTime().get(this.remindSetting.hrCheckDuration));
        this.sbHrSetting.setChecked(this.remindSetting.hrCycle);
        this.sbHrSetting.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() { // from class: com.czw.smartkit.setting.HrSettingActivity.1
            @Override // com.suke.widget.SwitchButton.OnCheckedChangeListener
            public void onCheckedChanged(SwitchButton switchButton, boolean z) {
                HrSettingActivity.this.remindSetting.hrCycle = z;
                if (HrSettingActivity.this.remindSetting.hrCycle) {
                    HrSettingActivity.this.leftState.setText(R.string.state_open);
                    HrSettingActivity.this.showTimePop();
                } else {
                    HrSettingActivity.this.leftState.setText(R.string.state_close);
                    HrSettingActivity.this.remindSetting.hrCheckDuration = 0;
                }
                HrSettingActivity.this.writeData();
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_hr_setting;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.alertTime) {
            if (this.sbHrSetting.isChecked()) {
                showTimePop();
                return;
            }
            return;
        }
        switch (id) {
            case R.id.hr_item_1 /* 2131296490 */:
                this.remindSetting.dynamicHr = false;
                this.item_1_yes.setVisibility(0);
                this.item_2_yes.setVisibility(8);
                writeData();
                return;
            case R.id.hr_item_2 /* 2131296491 */:
                this.remindSetting.dynamicHr = true;
                this.item_1_yes.setVisibility(8);
                this.item_2_yes.setVisibility(0);
                writeData();
                return;
            default:
                return;
        }
    }
}
