package com.czw.smartkit.statistics.trans;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.databaseModule.train.TrainDataTable;
import com.czw.smartkit.databaseModule.train.TrainServiceImpl;
import com.czw.smartkit.statistics.SportPathActivity;
import com.czw.smartkit.user.UserUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.time.DateFormatUtils;

/* loaded from: classes.dex */
public class TransDayPage extends BaseFragment {
    private RecyclerView recordListView;
    private ListAdapter listAdapter = null;
    private ArrayList<TrainDataTable> dataList = new ArrayList<>();

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.recordListView = (RecyclerView) $View(R.id.recordListView);
        this.recordListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.listAdapter = new ListAdapter(getActivity(), this.dataList);
        this.recordListView.setAdapter(this.listAdapter);
        $View(R.id.history_path).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.statistics.trans.TransDayPage.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent(TransDayPage.this.getActivity(), (Class<?>) SportPathActivity.class);
                if (TransDayPage.this.dataList != null && TransDayPage.this.dataList.size() > 0) {
                    intent.putExtra("transDTO", (Serializable) TransDayPage.this.dataList.get(TransDayPage.this.listAdapter.getIndex()));
                }
                TransDayPage.this.jumpTo(intent);
            }
        });
        updateShow(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.page_trans_day;
    }

    public void updateShow(final String str) {
        if (getActivity() == null || this.recordListView == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.statistics.trans.TransDayPage.2
            @Override // java.lang.Runnable
            public void run() {
                TransDayPage.this.dataList.clear();
                List<TrainDataTable> dataByDateAsc = TrainServiceImpl.getInstance().getDataByDateAsc(UserUtil.getUid(), str);
                if (dataByDateAsc != null && dataByDateAsc.size() > 0) {
                    TransDayPage.this.dataList.addAll(dataByDateAsc);
                }
                TransDayPage.this.listAdapter.notifyDataSetChanged();
            }
        });
    }
}
