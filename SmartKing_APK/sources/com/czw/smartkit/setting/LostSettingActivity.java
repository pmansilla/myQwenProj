package com.czw.smartkit.setting;

import android.view.View;
import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.data.AlostLTO;
import com.czw.smartkit.preferenceModule.SharePreferenceAlost;
import com.czw.smartkit.views.popw.SingleScrollerPop;
import com.suke.widget.SwitchButton;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class LostSettingActivity extends TitleActivity {
    private boolean enable;
    private SwitchButton enableBtn;
    private TextView sit_time;
    private int timeLen = 0;
    private AlostLTO alostLTO = null;

    public static ArrayList<String> loadTimes() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(AmapLoc.RESULT_TYPE_SELF_LAT_LON);
        arrayList.add("15");
        arrayList.add("30");
        arrayList.add("60");
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void packData() {
        BleManager.getBleManager().writeData(new byte[]{112, this.alostLTO.isEnable() ? (byte) 1 : (byte) 0});
        SharePreferenceAlost.save(this.alostLTO);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.lost_title);
        this.alostLTO = SharePreferenceAlost.read();
        if (this.alostLTO == null) {
            this.alostLTO = new AlostLTO();
        }
        this.sit_time = (TextView) $View(R.id.sit_time);
        this.enableBtn = (SwitchButton) $View(R.id.enable);
        this.enableBtn.setChecked(this.alostLTO.isEnable());
        this.sit_time.setText(this.alostLTO.getTimelen() + getString(R.string.unit_second));
        $View(R.id.alertTime).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.setting.LostSettingActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SingleScrollerPop.getPop(LostSettingActivity.this.getUI()).showPicker(LostSettingActivity.this.$View(R.id.acv_win), LostSettingActivity.loadTimes(), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.setting.LostSettingActivity.1.1
                    @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
                    public void onSelect(int i) {
                        LostSettingActivity.this.sit_time.setText(LostSettingActivity.this.getString(R.string.format_time_second, new Object[]{LostSettingActivity.loadTimes().get(i)}));
                        LostSettingActivity.this.timeLen = Integer.valueOf(LostSettingActivity.loadTimes().get(i)).intValue();
                        LostSettingActivity.this.alostLTO.setTimelen(LostSettingActivity.this.timeLen);
                        LostSettingActivity.this.packData();
                    }
                }).showTitleWithValue(R.string.lost_alert_time, LostSettingActivity.this.alostLTO.getTimelen() + "");
            }
        });
        this.enableBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() { // from class: com.czw.smartkit.setting.LostSettingActivity.2
            @Override // com.suke.widget.SwitchButton.OnCheckedChangeListener
            public void onCheckedChanged(SwitchButton switchButton, boolean z) {
                LostSettingActivity.this.enable = z;
                LostSettingActivity.this.alostLTO.setEnable(LostSettingActivity.this.enable);
                LostSettingActivity.this.packData();
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_lost;
    }
}
