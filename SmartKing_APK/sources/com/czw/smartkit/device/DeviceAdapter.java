package com.czw.smartkit.device;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.czw.smartkit.R;
import com.czw.smartkit.modes.adapter.RecycleAdapter;
import java.util.List;
import ycble.runchinaup.device.BleDevice;

/* loaded from: classes.dex */
public abstract class DeviceAdapter extends RecycleAdapter<BleDevice, ItemTag> {

    /* loaded from: classes.dex */
    public static class ItemTag extends RecycleAdapter.RecycleTag {
        public ImageView deviceConnFlag;
        private TextView mac;
        private TextView name;

        public ItemTag(View view) {
            super(view);
            this.name = (TextView) $View(R.id.name);
            this.mac = (TextView) $View(R.id.mac);
            this.deviceConnFlag = (ImageView) $View(R.id.deviceConnFlag);
        }
    }

    public DeviceAdapter(Context context, List<BleDevice> list) {
        super(context, list);
    }

    @Override // com.czw.smartkit.modes.adapter.RecycleAdapter
    public void handDataAndView(ItemTag itemTag, final BleDevice bleDevice, final int i) {
        itemTag.name.setText(bleDevice.getName());
        itemTag.mac.setText(bleDevice.getMac());
        itemTag.view.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.device.DeviceAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DeviceAdapter.this.onItemClick(bleDevice, i);
            }
        });
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.czw.smartkit.modes.adapter.RecycleAdapter
    public ItemTag instanceTag(View view) {
        return new ItemTag(view);
    }

    @Override // com.czw.smartkit.modes.adapter.RecycleAdapter
    public int loadItemView() {
        return R.layout.item_device;
    }

    protected void onItemClick(BleDevice bleDevice, int i) {
    }
}
