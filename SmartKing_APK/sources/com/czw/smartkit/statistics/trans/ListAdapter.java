package com.czw.smartkit.statistics.trans;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.czw.smartkit.R;
import com.czw.smartkit.databaseModule.train.TrainDataTable;
import com.czw.smartkit.modes.adapter.RecycleAdapter;
import com.czw.smartkit.util.SkUtils;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ListAdapter extends RecycleAdapter<TrainDataTable, ItemTag> {
    private int index;

    /* loaded from: classes.dex */
    public static class ItemTag extends RecycleAdapter.RecycleTag {
        private TextView datetimeTv;
        private TextView distanceTv;
        private TextView kcalTv;
        private ImageView pathIv;
        private TextView stepTv;

        public ItemTag(View view) {
            super(view);
            this.datetimeTv = (TextView) $View(R.id.datetimeTv);
            this.stepTv = (TextView) $View(R.id.stepTv);
            this.distanceTv = (TextView) $View(R.id.distanceTv);
            this.kcalTv = (TextView) $View(R.id.kcalTv);
            this.pathIv = (ImageView) $View(R.id.pathIv);
        }
    }

    public ListAdapter(Context context, List<TrainDataTable> list) {
        super(context, list);
        this.index = 0;
    }

    public int getIndex() {
        return this.index;
    }

    @Override // com.czw.smartkit.modes.adapter.RecycleAdapter
    public void handDataAndView(ItemTag itemTag, TrainDataTable trainDataTable, final int i) {
        String substring = trainDataTable.getStartTime().substring(11);
        String substring2 = trainDataTable.getEndTime().substring(11);
        itemTag.datetimeTv.setText(this.context.getString(R.string.format_trans_time, String.format(" %s (%s ~ %s)", SkUtils.getTransTimeLem(substring, substring2), substring, substring2)));
        itemTag.stepTv.setText(this.context.getString(R.string.format_trans_step, "  " + trainDataTable.getFasterRate()));
        itemTag.kcalTv.setText(this.context.getString(R.string.format_trans_kcal, Float.valueOf(trainDataTable.getCalorie()).intValue() + " kcal"));
        if (SkUtils.isChinaUnit()) {
            itemTag.distanceTv.setText(this.context.getString(R.string.format_trans_distance, String.format("%.2fKm ", Float.valueOf(Float.valueOf(trainDataTable.getStrength()).floatValue() / 1000.0f))));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(SkUtils.km2Mi((Float.valueOf(trainDataTable.getStrength()).floatValue() / 1000.0f) + ""));
            sb.append("Mi");
            itemTag.distanceTv.setText(this.context.getString(R.string.format_trans_distance, sb.toString()));
        }
        itemTag.pathIv.setVisibility(i != this.index ? 4 : 0);
        itemTag.view.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.statistics.trans.ListAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ListAdapter.this.index = i;
                ListAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override // com.czw.smartkit.modes.adapter.RecycleAdapter
    public ItemTag instanceTag(View view) {
        return new ItemTag(view);
    }

    @Override // com.czw.smartkit.modes.adapter.RecycleAdapter
    public int loadItemView() {
        return R.layout.item_trans_info;
    }
}
